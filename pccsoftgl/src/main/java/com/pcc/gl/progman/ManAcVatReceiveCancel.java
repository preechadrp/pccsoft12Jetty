package com.pcc.gl.progman;

import java.math.BigDecimal;


import com.pcc.gl.tbf.TbfACGL_HEADER;
import com.pcc.gl.tbf.TbfACGL_VATPUR;
import com.pcc.gl.tbo.TboACGL_DETAIL;
import com.pcc.gl.tbo.TboACGL_HEADER;
import com.pcc.gl.tbo.TboACGL_VATPUR;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.SqlStr;
import com.pcc.sys.tbm.TbmFPARA_COMP;

public class ManAcVatReceiveCancel {

	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static void saveCancel(LoginBean loginBean, String vou_type, String vou_no) throws Exception {

		try (FDbc dbc = FDbc.connectMasterDb()) {
			dbc.beginTrans();

			TboACGL_HEADER header = new TboACGL_HEADER();

			header.setCOMP_CDE(loginBean.getCOMP_CDE());
			header.setVOU_TYPE(vou_type);
			header.setVOU_NO(vou_no);

			if (TbfACGL_HEADER.find(dbc, header)) {

				if (header.getRECSTA() == 9) {
					throw new Exception("เอกสารมีการยกเลิกแล้ว");
				}

				if (header.getRECSTA() == 2) {
					if (!TbmFPARA_COMP.getString(loginBean.getCOMP_CDE(), "AC_AUTO_APPROVE").toUpperCase()
							.equals("YES")) {
						throw new Exception("เอกสารมีการอนุมัติแล้ว");
					}
				}

				{// === คืนค่ากลับและลบ VATPUR ขาล้าง ภาษีซื้อรอตัด

					SqlStr sql = new SqlStr();
					sql.addLine("select AMT, LINK_NO  from " + TboACGL_VATPUR.tablename);
					sql.addLine("where COMP_CDE='" + loginBean.getCOMP_CDE() + "' and VOU_TYPE='"
							+ Fnc.sqlQuote(vou_type) + "' and VOU_NO='" + Fnc.sqlQuote(vou_no) + "' ");
					sql.addLine("and TAX_TYPE=1 and POST_TYPE=2");
					logger.info(sql.getSql());
					try (java.sql.ResultSet rs = dbc.getResultSetFw(sql.getSql());) {
						while (rs.next()) {

							TboACGL_VATPUR vatpur1 = new TboACGL_VATPUR();
							String linkno = rs.getString("LINK_NO");
							BigDecimal bAMT = Fnc.getBigDecimalValue(rs.getBigDecimal("AMT"));

							// == หาขาตั้งมาลบค่าออก
							String sql2 = "select * from " + TboACGL_VATPUR.tablename + " where COMP_CDE='"
									+ loginBean.getCOMP_CDE() + "' and LINK_NO='" + Fnc.sqlQuote(linkno)
									+ "' and TAX_TYPE=1 and POST_TYPE=1";
							logger.info(sql2);
							try (java.sql.ResultSet rs_postyp1 = dbc.getResultSetFw(sql2);) {
								if (rs_postyp1.next()) {

									TbfACGL_VATPUR.setModel(rs_postyp1, vatpur1);
									BigDecimal old_clear_amt = Fnc.getBigDecimalValue(vatpur1.getCLEAR_AMT());
									BigDecimal new_clear_amt = Fnc.getBigDecimalValue(vatpur1.getCLEAR_AMT()).subtract(bAMT);
									if (new_clear_amt.compareTo(BigDecimal.ZERO) < 0) {
										new_clear_amt = BigDecimal.ZERO;
									}
									vatpur1.setCLEAR_AMT(new_clear_amt);

									if (!TbfACGL_VATPUR.update(dbc, vatpur1,
											"coalesce(CLEAR_AMT,0) = " + Fnc.getStrNumber(old_clear_amt, "#0.0###"))) {
										throw new Exception("ไม่สามารถบันทึกรายการ : Linkno=" + linkno);
									}

								}
							}

						}
					}

					// ===ลบรายการทิ้ง
					SqlStr sql_del = new SqlStr();
					sql_del.addLine("delete  from " + TboACGL_VATPUR.tablename);
					sql_del.addLine("where COMP_CDE='" + loginBean.getCOMP_CDE() + "' and VOU_TYPE='"
							+ Fnc.sqlQuote(vou_type) + "' and VOU_NO='" + Fnc.sqlQuote(vou_no) + "' ");
					sql_del.addLine("and TAX_TYPE=1 and POST_TYPE=2");
					int eff_rec = dbc.executeSql(sql_del.getSql());
					logger.info(sql_del.getSql() + " , eff_rec = " + eff_rec);

				}

				{// === ลบ VATPUR ภาษีซื้อ
					SqlStr sql_del = new SqlStr();
					sql_del.addLine("delete  from " + TboACGL_VATPUR.tablename);
					sql_del.addLine("where COMP_CDE='" + loginBean.getCOMP_CDE() + "' and VOU_TYPE='"
							+ Fnc.sqlQuote(vou_type) + "' and VOU_NO='" + Fnc.sqlQuote(vou_no) + "' ");
					sql_del.addLine("and TAX_TYPE=2 and POST_TYPE=1");
					int eff_rec = dbc.executeSql(sql_del.getSql());
					logger.info(sql_del.getSql() + " , eff_rec = " + eff_rec);
				}

				{// === ลบ acgl_detail
					SqlStr sql_del = new SqlStr();
					sql_del.addLine("delete  from " + TboACGL_DETAIL.tablename);
					sql_del.addLine("where COMP_CDE='" + loginBean.getCOMP_CDE() + "' and VOU_TYPE='"
							+ Fnc.sqlQuote(vou_type) + "' and VOU_NO='" + Fnc.sqlQuote(vou_no) + "' ");
					int eff_rec = dbc.executeSql(sql_del.getSql());
					logger.info(sql_del.getSql() + " , eff_rec = " + eff_rec);
				}

				// === update header.setRECSTA(9);
				int old_RECSTA = header.getRECSTA();
				header.setRECSTA(9);
				if (!TbfACGL_HEADER.update(dbc, header, "RECSTA=" + old_RECSTA)) {
					throw new Exception("ไม่สามารถบันทึกรายการได้");
				}

			} else {
				throw new Exception("ไม่พบรายการ");
			}

			dbc.commit();

		}

	}

}

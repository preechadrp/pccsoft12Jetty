package com.pcc.gl.progman;


import com.pcc.gl.tbf.TbfACGL_HEADER;
import com.pcc.gl.tbo.TboACGL_DETAIL;
import com.pcc.gl.tbo.TboACGL_HEADER;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.SqlStr;
import com.pcc.sys.tbm.TbmFPARA_COMP;

public class ManAcChqWdCancel {

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

				{// === update acgl_detail set CHQ_WD_VOU_TYPE, CHQ_WD_VOU_NO, CHQ_WD_VOU_SEQ,
					// CHQ_WD_USE
					SqlStr sql = new SqlStr();
					sql.addLine("update " + TboACGL_DETAIL.tablename);
					sql.addLine("set CHQ_WD_VOU_TYPE='', CHQ_WD_VOU_NO='', CHQ_WD_VOU_SEQ=0, CHQ_WD_USE=0 ");
					sql.addLine("where COMP_CDE='" + loginBean.getCOMP_CDE() + "' ");
					sql.addLine("and CHQ_WD_VOU_TYPE='" + Fnc.sqlQuote(vou_type) + "' ");
					sql.addLine("and CHQ_WD_VOU_NO='" + Fnc.sqlQuote(vou_no) + "' ");
					sql.addLine("and CHQ_TYPE in ('1','2')");
					int eff_rec = dbc.executeSql(sql.getSql());
					logger.info(sql.getSql() + " , eff_rec = " + eff_rec);
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

	public static void getDataQryForCancel(java.util.List<FModelHasMap> dats, String vou_type, String vou_no,
			java.sql.Date postdateFrom, java.sql.Date postdateTo, LoginBean _loginBean) throws Exception {

		dats.clear();
		try (FDbc dbc = FDbc.connectMasterDb()) {

			SqlStr sql = new SqlStr();
			sql.addLine("select * from " + TboACGL_HEADER.tablename);
			sql.addLine(" where 1=1 ");
			sql.addLine(" and COMP_CDE = '" + _loginBean.getCOMP_CDE() + "' ");
			if (!Fnc.isEmpty(vou_type)) {
				sql.addLine(" and VOU_TYPE  = '" + Fnc.sqlQuote(vou_type) + "' ");
			}
			if (!Fnc.isEmpty(vou_no)) {
				sql.addLine(" and VOU_NO like '" + Fnc.sqlQuote(vou_no) + "%' ");
			}

			if (postdateFrom != null && postdateTo != null) {
				sql.addLine(" and POSTDATE >= '" + postdateFrom + "' and POSTDATE <= '" + postdateTo + "' ");
			} else if (postdateFrom != null) {
				sql.addLine(" and POSTDATE >= '" + postdateFrom + "' ");
			} else if (postdateTo != null) {
				sql.addLine(" and POSTDATE <= '" + postdateTo + "' ");
			}

			sql.addLine(" and RECSTA in (1,2) and POST_APP in ('AcChqWd') ");
			sql.addLine(" order by VOU_TYPE, VOU_NO, POSTDATE");

			System.out.println(sql.getSql());
			try (java.sql.ResultSet rs = dbc.getResultSetFw3(sql.getSql(), 5000);) {
				while (rs.next()) {
					FModelHasMap dat = new FModelHasMap();
					dat.putRecord(rs);
					dats.add(dat);
				}
			}

		}

	}

}

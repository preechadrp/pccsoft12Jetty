package com.pcc.gl.progman;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import com.pcc.gl.tbf.TbfACGL_DETAIL;
import com.pcc.gl.tbf.TbfACGL_HEADER;
import com.pcc.gl.tbm.TbmACGL_DETAIL;
import com.pcc.gl.tbo.TboACCT_NO;
import com.pcc.gl.tbo.TboACGL_DETAIL;
import com.pcc.gl.tbo.TboACGL_HEADER;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.SqlStr;
import com.pcc.sys.tbm.TbmFCOMP;
import com.pcc.sys.tbm.TbmFPARA_COMP;

public class ManAcCloseY {

	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	/**
	 * 
	 * @param _loginBean
	 * @param comp_cde
	 * @param dd         วัน
	 * @param mm         เดือน
	 * @param yy         ปี พ.ศ. หรือ ค.ศ. ขึ้นกับ FnDate.useThaiDate
	 * @throws Exception
	 */
	public static void closeAcct(LoginBean _loginBean, String comp_cde, int dd, int mm, int yy) throws Exception {

		try (FDbc dbc = FDbc.connectMasterDb()) {

			java.sql.Date to_date = null;
			if (FnDate.useThaiDate) {
				to_date = FnDate.getDate(dd, mm, yy - FnDate.toBuddhistInc);// วันสิ้นสุดของปีที่จะปิดบัญชี เช่น 31/12/2016
			} else {
				to_date = FnDate.getDate(dd, mm, yy);// วันสิ้นสุดของปีที่จะปิดบัญชี เช่น 31/12/2016
			}
			java.sql.Date gl_date = TbmFCOMP.getGldate(dbc, _loginBean.getCOMP_CDE(), to_date);// วันเริ่มปีบัญชีของปีที่จะปิด เช่น 01/01/2016 (ค.ศ.)

			String[] acct_id_profit_cumulative = { "" };// รหัสบัญชีกำไรขาดทุน(สะสม)
			get_acct_id_profit(dbc, acct_id_profit_cumulative, _loginBean);

			java.util.List<FModelHasMap> list_sect_id = new ArrayList<>();// ใช้เก็บแผนกต่างๆ เพื่อหาค่า sum
			getAll_Sectid(dbc, list_sect_id, comp_cde, gl_date, to_date);

			String desc = "บันทึกเปิดบัญชี ณ " + FnDate.displayDateString(FnDate.addDay(to_date, 1));
			String vou_no = FnDate.getPrefixDocnoYY(FnDate.addDay(to_date, 1)) + "00000";

			// === clear ของเดิมก่อน
			clearOld(dbc, comp_cde, vou_no);

			// === insert acgl_header
			TboACGL_HEADER ah = new TboACGL_HEADER();
			insert_header(dbc, ah, comp_cde, vou_no, _loginBean, to_date, desc);

			// === loop insert acgl_detail
			try (java.sql.ResultSet rs = getDataDetail(dbc, comp_cde, gl_date, to_date, acct_id_profit_cumulative);) {
				if (rs.next()) {
					try (java.sql.PreparedStatement ps = TbfACGL_DETAIL.getPrepareStmInsert(dbc);) {
						insert_detail(dbc, ps, ah, rs, list_sect_id, desc);// สรุปยอดคงเหลือแต่ละแผนก
						insert_profit_to_detail(dbc, list_sect_id, ah, ps, acct_id_profit_cumulative, desc);// กำไร(ขาดทุน) แต่ละบริษัท
					}
				} else {
					throw new Exception("ไม่พบข้อมูลสำหรับการปิดบัญชีสิ้นปี");
				}
			}

		}

	}

	private static void clearOld(FDbc dbc, String comp_cde, String vou_no) throws SQLException {
		TbmACGL_DETAIL.delete_by_vouno(dbc, comp_cde, "JV", vou_no);
	}

	private static void insert_header(FDbc dbc, TboACGL_HEADER ah, String comp_cde, String vou_no, LoginBean _loginBean,
			Date to_date, String desc) throws SQLException, Exception {

		boolean ah_insert = true;
		ah.setCOMP_CDE(comp_cde);
		ah.setVOU_TYPE("JV");
		ah.setVOU_NO(vou_no);

		if (TbfACGL_HEADER.find(dbc, ah)) {
			ah_insert = false;
		}

		ah.setPOSTDATE(FnDate.addDay(to_date, 1));
		ah.setDESCR(desc);
		ah.setPOST_APP("AcCloseY");
		ah.setRECSTA(2);
		ah.setUPBY(_loginBean.getUSER_ID());
		ah.setUPDT(FnDate.getTodaySqlDateTime());
		ah.setAPPR_BY(_loginBean.getUSER_ID());
		ah.setAPPR_DT(FnDate.getTodaySqlDateTime());

		if (ah_insert) {
			if (!TbfACGL_HEADER.insert(dbc, ah)) {
				throw new Exception("ทำรายการไม่ได้กรุณาตรวจสอบ [INSERT acgl_header]");
			}
		} else {
			if (!TbfACGL_HEADER.update(dbc, ah, "")) {
				throw new Exception("ทำรายการไม่ได้กรุณาตรวจสอบ [UPDATE acgl_header]");
			}
		}

	}

	private static void insert_detail(FDbc dbc, java.sql.PreparedStatement ps, TboACGL_HEADER ah, java.sql.ResultSet rs,
			java.util.List<FModelHasMap> list_sect_id, String desc) throws SQLException, Exception {

		int idx = 0;

		do {
			idx++;

			TboACGL_DETAIL acd = new TboACGL_DETAIL();

			acd.setCOMP_CDE(ah.getCOMP_CDE());
			acd.setVOU_TYPE(ah.getVOU_TYPE());
			acd.setVOU_NO(ah.getVOU_NO());
			acd.setVOU_SEQ(idx);
			acd.setVOU_SEQ_SHOW(idx);
			acd.setPOSTDATE(ah.getPOSTDATE());
			acd.setACCT_ID(rs.getString("ACCT_ID"));

			if (Fnc.getBigDecimalValue(rs.getBigDecimal("AMT")).compareTo(BigDecimal.ZERO) > 0) {// แสดงว่า debit เยอะกว่า

				acd.setAMT(Fnc.getBigDecimalValue(rs.getBigDecimal("AMT")));
				acd.setNUM_TYPE(BigDecimal.ONE);

				sumAmtDrCrToSectid(Fnc.getStr(rs.getString("SECT_ID")), list_sect_id,
						Fnc.getBigDecimalValue(rs.getBigDecimal("AMT")), BigDecimal.ZERO);

			} else if (Fnc.getBigDecimalValue(rs.getBigDecimal("AMT")).compareTo(BigDecimal.ZERO) < 0) {// แสดงว่า credit เยอะกว่า

				acd.setAMT(Fnc.getBigDecimalValue(rs.getBigDecimal("AMT")).negate());
				acd.setNUM_TYPE(BigDecimal.ONE.negate());

				sumAmtDrCrToSectid(Fnc.getStr(rs.getString("SECT_ID")), list_sect_id, BigDecimal.ZERO,
						Fnc.getBigDecimalValue(rs.getBigDecimal("AMT")));

			}

			acd.setSECT_ID(Fnc.getStr(rs.getString("SECT_ID")));
			acd.setDESCR(desc);
			acd.setRECSTA(2);
			acd.setACCT_OPEN("Y");

			ps.clearParameters();
			TbfACGL_DETAIL.setPrepareStm(ps, acd);
			if (ps.executeUpdate() != 1) {
				throw new Exception("ทำรายการไม่ได้กรุณาตรวจสอบ [INSERT acgl_detail]");
			}

		} while (rs.next());

	}

	/**
	 * insert กำไร(ขาดทุน)สะสม แต่ละแผนก
	 * 
	 * @param dbc
	 * @param list_sect_id
	 * @param ah
	 * @param ps
	 * @param acct_id_profit_cumulative
	 * @param desc
	 * @throws Exception
	 */
	private static void insert_profit_to_detail(FDbc dbc, List<FModelHasMap> list_sect_id, TboACGL_HEADER ah,
			java.sql.PreparedStatement ps, String[] acct_id_profit_cumulative, String desc) throws Exception {

		TboACGL_DETAIL acd = new TboACGL_DETAIL();
		int idx = 30000;

		for (FModelHasMap dat : list_sect_id) {

			// DEBIT_AMT เป็นบวก , CREDIT_AMT เป็นลบ
			BigDecimal amount = dat.getBigDecimal("DEBIT_AMT").add(dat.getBigDecimal("CREDIT_AMT"));

			if (amount.compareTo(BigDecimal.ZERO) != 0) {
				idx++;

				acd.setCOMP_CDE(ah.getCOMP_CDE());
				acd.setVOU_TYPE(ah.getVOU_TYPE());
				acd.setVOU_NO(ah.getVOU_NO());
				acd.setVOU_SEQ(idx);
				acd.setVOU_SEQ_SHOW(idx);
				acd.setPOSTDATE(ah.getPOSTDATE());
				acd.setACCT_ID(acct_id_profit_cumulative[0]);

				if (amount.compareTo(BigDecimal.ZERO) > 0) { // แสดงว่าได้กำไร

					// ต้องใส่ credit กำไรขาดทุนสะสม (หมวด 3)
					acd.setNUM_TYPE(BigDecimal.ONE.negate());
					acd.setAMT(amount);

				} else { // แสดงว่าได้ขาดทุน

					// ต้องใส่ debit กำไรขาดทุนสะสม (หมวด 3)
					acd.setNUM_TYPE(BigDecimal.ONE);
					acd.setAMT(amount.abs());

				}

				acd.setSECT_ID(dat.getString("SECT_ID"));
				acd.setDESCR(desc);
				acd.setRECSTA(2);
				acd.setACCT_OPEN("Y");

				ps.clearParameters();
				TbfACGL_DETAIL.setPrepareStm(ps, acd);
				if (ps.executeUpdate() != 1) {
					throw new Exception("ทำรายการไม่ได้กรุณาตรวจสอบ [INSERT acgl_detail]");
				}

			}

		}

	}

	/**
	 * 
	 * @param dbc
	 * @param acct_id_profit_cumulative ตัวแปรรับรหัสบัญชีกำไรขาดทุนสะสม
	 * @throws Exception
	 * @throws SQLException
	 */
	private static void get_acct_id_profit(FDbc dbc, String[] acct_id_profit_cumulative, LoginBean _loginBean)
			throws SQLException, Exception {

		acct_id_profit_cumulative[0] = TbmFPARA_COMP.getString(dbc, _loginBean.getCOMP_CDE(), "AC_ACCT_ID_PROFIT_CUM");

		if (Fnc.isEmpty(acct_id_profit_cumulative[0])) {
			throw new Exception("ยังไม่ระบุพารามิเตอร์ รหัสบัญชีกำไรขาดทุนสะสม");
		}

	}

	/**
	 * 
	 * @param dbc
	 * @param comp_cde
	 * @param glDate
	 * @param toDate
	 * @param acct_id_profit_sum รหัสบัญชีกำไรขาดทุนสะสม
	 * @return
	 * @throws Exception
	 */
	public static java.sql.ResultSet getDataDetail(FDbc dbc, String comp_cde, java.sql.Date glDate,
			java.sql.Date toDate, String[] acct_id_profit_sum) throws Exception {

		SqlStr sql = new SqlStr();
		sql.addLine("select ad.acct_id, ad.sect_id, sum(ad.num_type * ad.amt) as amt ");
		sql.addLine("from " + TboACGL_DETAIL.tablename + " ad ");
		sql.addLine("left join " + TboACCT_NO.tablename + " atn ");
		sql.addLine("  on ad.COMP_CDE=atn.COMP_CDE and ad.acct_id=atn.acct_id ");
		sql.addLine("where ad.comp_cde = '" + comp_cde + "' ");
		sql.addLine("and coalesce(ad.chq_type,'') != '2' "); // ,2=เช็ครอตัดยกมา
		sql.addLine("and atn.acct_type in (1,2,3) ");
		sql.addLine("and coalesce(ad.acct_id,'') <> '" + acct_id_profit_sum[0] + "' ");
		sql.addLine("and ad.postdate >= '" + glDate + "' and ad.postdate <= '" + toDate + "' ");
		sql.addLine("group by ad.acct_id, ad.sect_id");
		sql.addLine("having sum(ad.num_type * ad.amt) <> 0");
		sql.addLine("order by ad.acct_id");

		logger.info(sql.getSql());

		return dbc.getResultSet(sql.getSql());

	}

	/**
	 * sum dr , cr แต่ละแผนก
	 * 
	 * @param sectid
	 * @param list_sect_id
	 * @param debitAmt
	 * @param creditAmt
	 * @throws Exception
	 */
	public static void sumAmtDrCrToSectid(String sectid, java.util.List<FModelHasMap> list_sect_id, BigDecimal debitAmt,
			BigDecimal creditAmt) throws Exception {

		for (FModelHasMap hDat : list_sect_id) {

			if (hDat.getString("SECT_ID").equals(sectid)) {

				if (debitAmt.compareTo(BigDecimal.ZERO) != 0) {
					hDat.setBigDecimal("DEBIT_AMT", debitAmt.add(hDat.getBigDecimal("DEBIT_AMT")));
				}
				if (creditAmt.compareTo(BigDecimal.ZERO) != 0) {
					hDat.setBigDecimal("CREDIT_AMT", creditAmt.add(hDat.getBigDecimal("CREDIT_AMT")));
				}
				break;

			}

		}

	}

	/**
	 * สร้าง bean เก็บค่า sum แต่ละแผนก
	 * 
	 * @param dbc
	 * @param list_sect_id
	 * @param comp_cde
	 * @param fromDate
	 * @param toDate
	 * @throws Exception
	 */
	public static void getAll_Sectid(FDbc dbc, java.util.List<FModelHasMap> list_sect_id, String comp_cde,
			java.sql.Date fromDate, java.sql.Date toDate) throws Exception {

		try (java.sql.ResultSet rs = dbc.getResultSet(" select distinct ad.sect_id from " + TboACGL_DETAIL.tablename +
				" ad where ad.postdate >= '" + fromDate + "' and ad.postdate <= '" + toDate + "' and ad.comp_cde = '" + comp_cde + "' ");) {
			while (rs.next()) {

				FModelHasMap dat = new FModelHasMap();

				dat.setString("SECT_ID", Fnc.getStr(rs.getString("SECT_ID")));
				dat.setBigDecimal("DEBIT_AMT", BigDecimal.ZERO);
				dat.setBigDecimal("CREDIT_AMT", BigDecimal.ZERO);

				list_sect_id.add(dat);

			}
		}

	}

}

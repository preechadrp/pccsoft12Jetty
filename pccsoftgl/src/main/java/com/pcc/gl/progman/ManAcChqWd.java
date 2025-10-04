package com.pcc.gl.progman;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;


import com.pcc.gl.lib.AcRunning;
import com.pcc.gl.tbf.TbfACGL_DETAIL;
import com.pcc.gl.tbf.TbfACGL_HEADER;
import com.pcc.gl.tbo.TboACCT_NO;
import com.pcc.gl.tbo.TboACCT_NO_SUB;
import com.pcc.gl.tbo.TboACGL_DETAIL;
import com.pcc.gl.tbo.TboACGL_HEADER;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.SqlStr;
import com.pcc.sys.tbm.TbmFPARA_COMP;

public class ManAcChqWd {

	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static void getDataQry(java.util.List<FModelHasMap> dats, String vou_type, String vou_no,
			java.sql.Date postdateFrom, java.sql.Date postdateTo, String acct_id_chq, LoginBean _loginBean)
			throws Exception {

		dats.clear();
		try (FDbc dbc = FDbc.connectMasterDb()) {

			SqlStr sql = new SqlStr();
			sql.addLine("select aa.*, bb.ACCT_NAME, cc.ACCT_ID_BANK, dd.ACCT_NAME as ACCT_NAME_BANK");
			sql.addLine("from " + TboACGL_DETAIL.tablename + " aa");
			sql.addLine("left join " + TboACCT_NO.tablename + " bb on aa.COMP_CDE=bb.COMP_CDE and aa.ACCT_ID=bb.ACCT_ID");
			sql.addLine("left join " + TboACCT_NO_SUB.tablename + " cc on aa.COMP_CDE=cc.COMP_CDE and aa.ACCT_ID=cc.ACCT_ID");
			sql.addLine("left join " + TboACCT_NO.tablename + " dd on cc.COMP_CDE=dd.COMP_CDE  and cc.ACCT_ID_BANK=dd.ACCT_ID");
			sql.addLine("where aa.COMP_CDE='" + _loginBean.getCOMP_CDE() + "' ");
			sql.addLine("and aa.CHQ_TYPE='1' and not coalesce(aa.CHQ_WD_USE,0) in (1,2) and aa.RECSTA = 2 ");
			if (!Fnc.isEmpty(acct_id_chq)) {
				sql.addLine("and aa.ACCT_ID='" + Fnc.sqlQuote(acct_id_chq) + "' ");// ค้นหาด้วยเจ้าหนี้
			}
			if (!Fnc.isEmpty(vou_type)) {
				sql.addLine(" and aa.VOU_TYPE = '" + Fnc.sqlQuote(vou_type) + "' ");
			}
			if (!Fnc.isEmpty(vou_no)) {
				sql.addLine(" and aa.VOU_NO like '" + Fnc.sqlQuote(vou_no) + "%' ");
			}

			if (postdateFrom != null && postdateTo != null) {
				sql.addLine(" and aa.POSTDATE >= '" + postdateFrom + "' and aa.POSTDATE <= '" + postdateTo + "' ");
			} else if (postdateFrom != null) {
				sql.addLine(" and aa.POSTDATE >= '" + postdateFrom + "' ");
			} else if (postdateTo != null) {
				sql.addLine(" and aa.POSTDATE <= '" + postdateTo + "' ");
			}

			sql.addLine("order by aa.VOU_TYPE, aa.VOU_NO, aa.POSTDATE");

			logger.info(sql.getSql());
			try (java.sql.ResultSet rs = dbc.getResultSetFw3(sql.getSql(), 10000);) {
				while (rs.next()) {
					FModelHasMap dat = new FModelHasMap();

					dat.putRecord(rs);
					dat.setString("ACCT_NAME_BANK", rs.getString("ACCT_NAME_BANK"));// ฟิลด์ที่ as ชื่อใหม่คำสั่ง putRecord
																					// ไม่รองรับ
					dats.add(dat);
				}
			}

		}

	}

	public static void saveData(FDbc dbc, BigDecimal sumall, boolean chkClearByAcctid, String acct_id_oth,
			Date postdate, String descr, List<FModelHasMap> lst_select, String[] vou_type, String[] vou_no,
			LoginBean loginBean) throws Exception {

		int[] vou_seq = { 0 };

		// ==== insert acgl_header ตัวตั้งต้น
		TboACGL_HEADER header = new TboACGL_HEADER();
		insert_gl_header(dbc, header, loginBean, vou_type, vou_no, postdate, descr);

		// ==== insert acgl_detail ด้วย dr เช็คจ่ายล่วงหน้าและ Cr.รหัสบัญชีที่กำหนด
		insert_gl_detail_dr(dbc, header, loginBean, chkClearByAcctid, lst_select, vou_seq);

		// ==== insert acgl_detail ด้วย acct_id_oth ถ้าเลือก cr. ด้วยรหัสบัญชีอื่น
		insert_gl_detail_by_acct_id_oth(dbc, header, loginBean, chkClearByAcctid, acct_id_oth, sumall, lst_select,
				vou_seq);

	}

	private static void insert_gl_header(FDbc dbc, TboACGL_HEADER header, LoginBean loginBean, String[] vou_type,
			String[] vou_no, Date postdate, String descr) throws SQLException, Exception {

		vou_type[0] = "BK";
		vou_no[0] = AcRunning.getRunningVoucher(dbc, loginBean.getCOMP_CDE(), vou_type[0], postdate);

		header.setCOMP_CDE(loginBean.getCOMP_CDE());
		header.setVOU_TYPE(vou_type[0]);
		header.setVOU_NO(vou_no[0]);
		header.setPOSTDATE(postdate);
		header.setDESCR(descr);
		header.setPOST_APP("AcChqWd");
		header.setUPBY(loginBean.getUSER_ID());
		header.setUPDT(FnDate.getTodaySqlDateTime());
		if (TbmFPARA_COMP.getString(loginBean.getCOMP_CDE(), "AC_AUTO_APPROVE").toUpperCase().equals("YES")) {
			header.setRECSTA(2);
			header.setAPPR_BY(loginBean.getUSER_ID());
			header.setAPPR_DT(FnDate.getTodaySqlDateTime());
		} else {
			header.setRECSTA(1);
			header.setAPPR_BY("");
			header.setAPPR_DT(null);
		}

		if (!TbfACGL_HEADER.insert(dbc, header)) {
			throw new Exception("ไม่สามารถบันทึกกรุณาตรวจสอบ");
		}

	}

	private static void insert_gl_detail_dr(FDbc dbc, TboACGL_HEADER header, LoginBean loginBean,
			boolean chkClearByAcctid, List<FModelHasMap> lst_select, int[] vou_seq) throws Exception {

		for (FModelHasMap dat : lst_select) {

			vou_seq[0]++;

			// == insert DR. TboACGL_DETAIL เช็คจ่ายล่วงหน้า
			TboACGL_DETAIL gl_detail = new TboACGL_DETAIL();

			gl_detail.setCOMP_CDE(header.getCOMP_CDE());
			gl_detail.setVOU_TYPE(header.getVOU_TYPE());
			gl_detail.setVOU_NO(header.getVOU_NO());
			gl_detail.setVOU_SEQ(vou_seq[0]);
			gl_detail.setVOU_SEQ_SHOW(vou_seq[0]);
			gl_detail.setPOSTDATE(header.getPOSTDATE());
			gl_detail.setACCT_ID(dat.getString("ACCT_ID"));
			gl_detail.setAMT(dat.getBigDecimal("AMT"));
			gl_detail.setNUM_TYPE(BigDecimal.ONE);// DR ด้วย
			gl_detail.setSECT_ID(dat.getString("SECT_ID"));
			gl_detail.setDESCR(dat.getString("DESCR"));
			gl_detail.setDESCR_SUB(dat.getString("DESCR_SUB"));
			gl_detail.setRECSTA(header.getRECSTA());
			gl_detail.setSUB_HAS("");
			gl_detail.setSUB_APPR("");
			gl_detail.setSUB_APPR_BY("");

			if (!TbfACGL_DETAIL.insert(dbc, gl_detail)) {
				logger.info("Error Insert acgl_detail ");
				throw new Exception("ไม่สามารถบันทึกรายการ");
			}

			// == insert CR. TboACGL_DETAIL ขาล้าง
			if (!chkClearByAcctid) { // ถ้าไม่เลือกลงด้วยรหัสบัญชีอื่น ให้ Cr. ด้วย ACCT_ID_BANK

				vou_seq[0]++;
				TboACGL_DETAIL gl_detail_credit = new TboACGL_DETAIL();// clone ตัวเดิมมา

				gl_detail_credit.setCOMP_CDE(header.getCOMP_CDE());
				gl_detail_credit.setVOU_TYPE(dat.getString("VOU_TYPE"));
				gl_detail_credit.setVOU_NO(dat.getString("VOU_NO"));
				gl_detail_credit.setVOU_SEQ(dat.getInt("VOU_SEQ"));

				if (!TbfACGL_DETAIL.find(dbc, gl_detail_credit)) {
					logger.info("Error find acgl_detail ");
					throw new Exception("ไม่สามารถบันทึกรายการ (ไม่พบขาตั้ง)");
				}

				gl_detail_credit.setVOU_TYPE(header.getVOU_TYPE());
				gl_detail_credit.setVOU_NO(header.getVOU_NO());
				gl_detail_credit.setVOU_SEQ(vou_seq[0]);
				gl_detail_credit.setVOU_SEQ_SHOW(vou_seq[0]);
				gl_detail_credit.setPOSTDATE(header.getPOSTDATE());
				gl_detail_credit.setACCT_ID(dat.getString("ACCT_ID_BANK"));
				gl_detail_credit.setAMT(dat.getBigDecimal("AMT"));
				gl_detail_credit.setNUM_TYPE(BigDecimal.ONE.negate());// CR
				gl_detail_credit.setRECSTA(header.getRECSTA());
				gl_detail_credit.setSUB_HAS("");
				gl_detail_credit.setSUB_APPR("");
				gl_detail_credit.setSUB_APPR_BY("");
				gl_detail_credit.setCHQ_TYPE("");

				if (!TbfACGL_DETAIL.insert(dbc, gl_detail_credit)) {
					logger.info("Error insert acgl_detail ");
					throw new Exception("ไม่สามารถบันทึกรายการ");
				}

			}

			// == update TboACGL_DETAIL (CHQ_WD_VOU_TYPE, CHQ_WD_VOU_NO,
			// CHQ_WD_VOU_SEQ,CHQ_WD_USE) ขาตั้ง
			TboACGL_DETAIL gl_detail_old = new TboACGL_DETAIL();

			gl_detail_old.setCOMP_CDE(header.getCOMP_CDE());
			gl_detail_old.setVOU_TYPE(dat.getString("VOU_TYPE"));
			gl_detail_old.setVOU_NO(dat.getString("VOU_NO"));
			gl_detail_old.setVOU_SEQ(dat.getInt("VOU_SEQ"));

			if (!TbfACGL_DETAIL.find(dbc, gl_detail_old)) {
				logger.info("Error find acgl_detail ");
				throw new Exception("ไม่สามารถบันทึกรายการ (ไม่พบขาตั้ง)");
			}

			gl_detail_old.setCHQ_WD_VOU_TYPE(gl_detail.getVOU_TYPE());
			gl_detail_old.setCHQ_WD_VOU_NO(gl_detail.getVOU_NO());
			gl_detail_old.setCHQ_WD_VOU_SEQ(gl_detail.getVOU_SEQ());
			gl_detail_old.setCHQ_WD_USE(chkClearByAcctid ? 2 : 1);// 1=เป็นการ W/D ปกติ ,2=ล้างยอดแบบยกเลิกเช็ค

			if (!TbfACGL_DETAIL.update(dbc, gl_detail_old, "coalesce(CHQ_WD_VOU_NO,'') = '' ")) {
				logger.info("Error update acgl_detail ");
				throw new Exception("ไม่สามารถบันทึกรายการ กรุณาตรวจสอบ");
			}

		}

	}

	private static void insert_gl_detail_by_acct_id_oth(FDbc dbc, TboACGL_HEADER header, LoginBean loginBean,
			boolean chkClearByAcctid, String acct_id_oth, BigDecimal sumall, List<FModelHasMap> lst_select,
			int[] vou_seq) throws Exception {

		if (chkClearByAcctid) {

			vou_seq[0]++;

			TboACGL_DETAIL gl_detail = new TboACGL_DETAIL();

			gl_detail.setCOMP_CDE(header.getCOMP_CDE());
			gl_detail.setVOU_TYPE(header.getVOU_TYPE());
			gl_detail.setVOU_NO(header.getVOU_NO());
			gl_detail.setVOU_SEQ(vou_seq[0]);
			gl_detail.setVOU_SEQ_SHOW(vou_seq[0]);
			gl_detail.setPOSTDATE(header.getPOSTDATE());
			gl_detail.setACCT_ID(acct_id_oth);// รหัสบัญชีอื่น
			gl_detail.setNUM_TYPE(BigDecimal.ONE.negate());// CR
			gl_detail.setAMT(sumall);

			FModelHasMap dat1 = lst_select.get(0);// เอาจากแผนกแรก
			gl_detail.setSECT_ID(dat1.getString("SECT_ID"));
			gl_detail.setDESCR("ล้างเช็คจ่ายล่วงหน้า เลขที่เช็ค :");

			String list_chqno = "";// รายการเลขที่เช็ค
			for (FModelHasMap dat : lst_select) {
				list_chqno += dat.getString("CHQ_NO") + " ";
			}
			gl_detail.setDESCR_SUB(Fnc.getStrLeft(list_chqno.trim(), 255));

			gl_detail.setRECSTA(header.getRECSTA());

			if (!TbfACGL_DETAIL.insert(dbc, gl_detail)) {
				logger.info("Error Insert acgl_detail ");
				throw new Exception("ไม่สามารถบันทึกรายการ");
			}

		}

	}

}

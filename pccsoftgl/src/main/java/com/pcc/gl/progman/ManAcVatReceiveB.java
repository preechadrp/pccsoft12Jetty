package com.pcc.gl.progman;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;


import com.pcc.gl.lib.AcRunning;
import com.pcc.gl.tbf.TbfACGL_DETAIL;
import com.pcc.gl.tbf.TbfACGL_HEADER;
import com.pcc.gl.tbf.TbfACGL_VATPUR;
import com.pcc.gl.tbo.TboACGL_DETAIL;
import com.pcc.gl.tbo.TboACGL_HEADER;
import com.pcc.gl.tbo.TboACGL_VATPUR;
import com.pcc.gl.tbo.TboFCUS;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.MyDecimalbox;
import com.pcc.sys.lib.SqlStr;
import com.pcc.sys.tbm.TbmFPARA_COMP;

public class ManAcVatReceiveB {

	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static void getDataQry(java.util.List<FModelHasMap> dats, String vou_type, String vou_no,
			java.sql.Date postdateFrom, java.sql.Date postdateTo, String cust_cde, LoginBean _loginBean)
			throws Exception {

		dats.clear();
		try (FDbc dbc = FDbc.connectMasterDb()) {

			SqlStr sql = new SqlStr();
			sql.addLine("select aa.* ,bb.TITLE,bb.FNAME,bb.LNAME");
			sql.addLine("from " + TboACGL_VATPUR.tablename + " aa");
			sql.addLine("left join " + TboFCUS.tablename + " bb on aa.COMP_CDE=bb.COMP_CDE and aa.CUST_CDE=bb.CUST_CDE");
			sql.addLine("where aa.TAX_TYPE=1 and aa.RECSTA=2 and aa.POST_TYPE=1");
			sql.addLine("and aa.COMP_CDE= '" + _loginBean.getCOMP_CDE() + "' ");

			if (!Fnc.isEmpty(vou_type)) {
				sql.addLine(" and aa.VOU_TYPE  = '" + Fnc.sqlQuote(vou_type) + "' ");
			}

			if (!Fnc.isEmpty(vou_no)) {
				sql.addLine(" and aa.VOU_NO like '" + Fnc.sqlQuote(vou_no) + "%' ");
			}

			if (!Fnc.isEmpty(cust_cde)) {
				sql.addLine("and aa.CUST_CDE='" + Fnc.sqlQuote(cust_cde) + "' ");// ค้นหาด้วยเจ้าหนี้
			}

			if (postdateFrom != null && postdateTo != null) {
				sql.addLine(" and aa.POSTDATE >= '" + postdateFrom + "' and aa.POSTDATE <= '" + postdateTo + "' ");
			} else if (postdateFrom != null) {
				sql.addLine(" and aa.POSTDATE >= '" + postdateFrom + "' ");
			} else if (postdateTo != null) {
				sql.addLine(" and aa.POSTDATE <= '" + postdateTo + "' ");
			}

			sql.addLine("and coalesce(aa.AMT,0) != coalesce(aa.CLEAR_AMT,0) ");
			sql.addLine("order by aa.VOU_TYPE, aa.VOU_NO");

			logger.info(sql.getSql());
			try (java.sql.ResultSet rs = dbc.getResultSetFw3(sql.getSql(), 5000);) {
				while (rs.next()) {
					FModelHasMap dat = new FModelHasMap();
					dat.putRecord(rs);
					dats.add(dat);
				}
			}

		}

	}

	public static void saveData(FDbc dbc, BigDecimal sumrec, BigDecimal sumbase_amt, BigDecimal sumdiff,
			String acct_id_diff, String docno, Date docdate, Date postdate, List<FModelHasMap> lst_select,
			String[] vou_type, String[] vou_no, LoginBean loginBean) throws Exception {

		int[] vou_seq = { 0 };

		// ==== insert acgl_header ตัวตั้งต้น
		TboACGL_HEADER header = new TboACGL_HEADER();
		insert_gl_header(dbc, header, loginBean, vou_type, vou_no, postdate);

		// ==== insert acgl_detail ด้วย sumrec=ภาษีซื้อ
		insert_gl_detail_sumrec(dbc, header, loginBean, sumrec, sumbase_amt, docno, docdate, lst_select, vou_seq);

		// ==== insert acgl_detail ด้วย sumdiff=ส่วนต่าง
		insert_gl_detail_sumdiff(dbc, header, loginBean, sumdiff, acct_id_diff, docno, docdate, lst_select, vou_seq);

		// ==== insert acgl_detail รายการที่เลือก
		insert_gl_detail_clear(dbc, header, loginBean, lst_select, vou_seq);

	}

	private static void insert_gl_header(FDbc dbc, TboACGL_HEADER header, LoginBean loginBean, String[] vou_type,
			String[] vou_no, Date postdate) throws SQLException, Exception {

		vou_type[0] = "VT";
		vou_no[0] = AcRunning.getRunningVoucher(dbc, loginBean.getCOMP_CDE(), vou_type[0], postdate);

		header.setCOMP_CDE(loginBean.getCOMP_CDE());
		header.setVOU_TYPE(vou_type[0]);
		header.setVOU_NO(vou_no[0]);
		header.setPOSTDATE(postdate);
		header.setDESCR("รับภาษีซื้อ");
		header.setPOST_APP("AcVatReceiveB");
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

	private static void insert_gl_detail_sumrec(FDbc dbc, TboACGL_HEADER header, LoginBean loginBean, BigDecimal sumrec,
			BigDecimal sumbase_amt, String docno, Date docdate, List<FModelHasMap> lst_select, int[] vou_seq)
			throws Exception {

		if (sumrec.compareTo(BigDecimal.ZERO) != 0) {
			vou_seq[0]++;

			// == insert TboACGL_DETAIL
			TboACGL_DETAIL gl_detail = new TboACGL_DETAIL();

			gl_detail.setCOMP_CDE(header.getCOMP_CDE());
			gl_detail.setVOU_TYPE(header.getVOU_TYPE());
			gl_detail.setVOU_NO(header.getVOU_NO());
			gl_detail.setVOU_SEQ(vou_seq[0]);
			gl_detail.setVOU_SEQ_SHOW(vou_seq[0]);
			gl_detail.setPOSTDATE(header.getPOSTDATE());
			gl_detail.setACCT_ID(TbmFPARA_COMP.getString(dbc, loginBean.getCOMP_CDE(), "AC_ACCITD_TAX_PUR"));// รหัสบัญชีภาษีซื้อ
			if (sumrec.compareTo(BigDecimal.ZERO) > 0) {
				gl_detail.setAMT(sumrec);
				gl_detail.setNUM_TYPE(BigDecimal.ONE);
			} else {
				gl_detail.setAMT(sumrec.abs());
				gl_detail.setNUM_TYPE(BigDecimal.ONE.negate());
			}

			FModelHasMap dat1 = lst_select.get(0);
			gl_detail.setSECT_ID(dat1.getString("SECT_ID"));

			String desc1 = dat1.getString("TITLE").trim() + dat1.getString("FNAME").trim() + " "
					+ dat1.getString("LNAME").trim();
			gl_detail.setDESCR("รับภาษีซื้อ" + desc1.trim());

			gl_detail.setDESCR_SUB("เลขเอกสาร " + docno + " วันที่เอกสาร " + FnDate.displayDateString(docdate));
			gl_detail.setRECSTA(header.getRECSTA());
			gl_detail.setSUB_HAS("1");// 1=ภาษีซื้อ
			gl_detail.setSUB_APPR("1");// 0=ยังไม่ใส่ตัวย่อย,1=ใส่ตัวย่อยแล้ว,2=ไม่ใส่ตัวย่อย
			gl_detail.setSUB_APPR_BY(header.getUPBY());

			if (!TbfACGL_DETAIL.insert(dbc, gl_detail)) {
				logger.info("Error Insert acgl_detail ");
				throw new Exception("ไม่สามารถบันทึกรายการ");
			}

			// insert acgl_vatpur
			TboACGL_VATPUR vatpur = new TboACGL_VATPUR();

			vatpur.setCOMP_CDE(header.getCOMP_CDE());
			vatpur.setVOU_TYPE(header.getVOU_TYPE());
			vatpur.setVOU_NO(header.getVOU_NO());
			vatpur.setVOU_SEQ(vou_seq[0]);
			vatpur.setVOU_DSEQ(1);
			vatpur.setPOSTDATE(header.getPOSTDATE());
			vatpur.setTAX_TYPE(2);// 1=ภาษีซื้อรอตัด ,2=ภาษีซื้อ
			vatpur.setPOST_TYPE(1);// 1=ขาตั้งหนี้
			vatpur.setVAT_RATE(dat1.getBigDecimal("VAT_RATE"));
			if (sumrec.compareTo(BigDecimal.ZERO) > 0) {
				vatpur.setNUM_TYPE(BigDecimal.ONE);
				vatpur.setAMT(sumrec);
				vatpur.setBASE_AMT(sumbase_amt);
			} else {
				vatpur.setNUM_TYPE(BigDecimal.ONE.negate());
				vatpur.setAMT(sumrec.abs());
				vatpur.setBASE_AMT(sumbase_amt.abs());
			}

			vatpur.setSECT_ID(dat1.getString("SECT_ID"));
			vatpur.setDESCR(gl_detail.getDESCR());
			vatpur.setDOCNO(docno);
			vatpur.setDOCDATE(docdate);
			vatpur.setCUST_CDE(dat1.getString("CUST_CDE"));
			vatpur.setCUST_BRANCH_ID(dat1.getString("CUST_BRANCH_ID"));
			vatpur.setLINK_NO("");// เนื่องจากมาจากหลายรายการได้จึงต้องว่างๆ ไว้
			vatpur.setRECSTA(header.getRECSTA());
			vatpur.setINSBY(loginBean.getUSER_ID());
			vatpur.setINSDT(header.getUPDT());
			vatpur.setUPBY(loginBean.getUSER_ID());
			vatpur.setUPDT(header.getUPDT());

			TbfACGL_VATPUR.insert(dbc, vatpur);

		}

	}

	private static void insert_gl_detail_sumdiff(FDbc dbc, TboACGL_HEADER header, LoginBean loginBean,
			BigDecimal sumdiff, String acct_id_diff, String docno, java.sql.Date docdate, List<FModelHasMap> lst_select,
			int[] vou_seq) throws Exception {

		if (sumdiff.compareTo(BigDecimal.ZERO) != 0) {
			vou_seq[0]++;

			TboACGL_DETAIL gl_detail = new TboACGL_DETAIL();

			gl_detail.setCOMP_CDE(header.getCOMP_CDE());
			gl_detail.setVOU_TYPE(header.getVOU_TYPE());
			gl_detail.setVOU_NO(header.getVOU_NO());
			gl_detail.setVOU_SEQ(vou_seq[0]);
			gl_detail.setVOU_SEQ_SHOW(vou_seq[0]);
			gl_detail.setPOSTDATE(header.getPOSTDATE());
			gl_detail.setACCT_ID(acct_id_diff);// รหัสบัญชีส่วนต่าง

			if (sumdiff.compareTo(BigDecimal.ZERO) > 0) {// ตั้งภาษีซื้อรอตัดมากกว่าใบกำกับภาษี ใน DR ส่วนต่าง
				gl_detail.setNUM_TYPE(BigDecimal.ONE);
				gl_detail.setAMT(sumdiff);
			} else {// ตั้งภาษีรอตัดน้อยกว่าใบกำกับภาษี ให้ CR ส่วนต่าง
				gl_detail.setNUM_TYPE(BigDecimal.ONE.negate());
				gl_detail.setAMT(sumdiff.abs());
			}

			FModelHasMap dat1 = lst_select.get(0);
			gl_detail.setSECT_ID(dat1.getString("SECT_ID"));

			String desc1 = dat1.getString("TITLE").trim() + dat1.getString("FNAME").trim() + " "
					+ dat1.getString("LNAME").trim();
			gl_detail.setDESCR("ส่วนต่างรับภาษีซื้อ" + desc1.trim());

			gl_detail.setDESCR_SUB("เลขเอกสาร " + docno + " วันที่เอกสาร " + FnDate.displayDateString(docdate));
			gl_detail.setRECSTA(header.getRECSTA());

			if (!TbfACGL_DETAIL.insert(dbc, gl_detail)) {
				logger.info("Error Insert acgl_detail ");
				throw new Exception("ไม่สามารถบันทึกรายการ");
			}

		}

	}

	private static void insert_gl_detail_clear(FDbc dbc, TboACGL_HEADER header, LoginBean loginBean,
			List<FModelHasMap> lst_select, int[] vou_seq) throws SQLException, Exception {

		String ac_accitd_tax_pur_wait = TbmFPARA_COMP.getString(dbc, loginBean.getCOMP_CDE(), "AC_ACCITD_TAX_PUR_WAIT");// รหัสบัญชีภาษีซื้อรอตัด
		TboACGL_VATPUR vatpur_old = new TboACGL_VATPUR();// ตัวเก่า
		TboACGL_VATPUR vatpur_clone = new TboACGL_VATPUR();// ตัวล้าง
		int count = 0;

		for (FModelHasMap dat : lst_select) {

			count++;

			getRecord(dbc, vatpur_clone, vatpur_old, dat, count, loginBean);

			// ===update CLEAR_AMT ที่ล้าง
			MyDecimalbox decRCV_AMT = (MyDecimalbox) dat.get("decRCV_AMT");
			BigDecimal old_clear_amt = Fnc.getBigDecimal(vatpur_old.getCLEAR_AMT());
			// decRCV_AMT  เป็นได้ทั้งค่า ลบ และ บวก เพราะคูณ NUM_TYPE
			BigDecimal new_clear_amt = Fnc.getBigDecimal(vatpur_old.getCLEAR_AMT()).add(decRCV_AMT.getValue().abs());
			if (decRCV_AMT.getValue().compareTo(new_clear_amt) > 0) {
				throw new Exception("รับครั้งนี้มากกว่าคงเหลือ ลำดับที่ " + count);
			}

			vatpur_old.setCLEAR_AMT(new_clear_amt);

			if (!TbfACGL_VATPUR.update(dbc, vatpur_old,
					"coalesce(CLEAR_AMT,0) =" + Fnc.getStrBigDecimal(old_clear_amt))) {
				String errMsg = "ไม่สามารถบันทึก รายการที่" + count;
				logger.info(errMsg);
				throw new Exception(errMsg);
			}

			// ===insert รายการล้าง acgl_detail
			vou_seq[0]++;
			TboACGL_DETAIL detail = new TboACGL_DETAIL();

			detail.setCOMP_CDE(header.getCOMP_CDE());
			detail.setVOU_TYPE(header.getVOU_TYPE());
			detail.setVOU_NO(header.getVOU_NO());
			detail.setVOU_SEQ(vou_seq[0]);
			detail.setPOSTDATE(header.getPOSTDATE());
			detail.setVOU_SEQ_SHOW(vou_seq[0]);
			detail.setACCT_ID(ac_accitd_tax_pur_wait);
			detail.setAMT(decRCV_AMT.getValue().abs());
			if (decRCV_AMT.getValue().compareTo(BigDecimal.ZERO) > 0) {
				detail.setNUM_TYPE(BigDecimal.ONE.negate());
			} else {
				detail.setNUM_TYPE(BigDecimal.ONE);
			}
			detail.setSECT_ID(dat.getString("SECT_ID"));
			detail.setDESCR(dat.getString("DESCR"));
			detail.setDESCR_SUB("เลขเอกสาร " + vatpur_old.getDOCNO() + " วันที่เอกสาร "
					+ FnDate.displayDateString(vatpur_old.getDOCDATE()));
			detail.setRECSTA(header.getRECSTA());
			detail.setACCT_OPEN("");

			TbfACGL_DETAIL.insert(dbc, detail);

			// ===insert รายการล้าง acgl_vatpur.POST_TYPE=2=ขาล้างหนี้
			vatpur_clone.setVOU_TYPE(header.getVOU_TYPE());
			vatpur_clone.setVOU_NO(header.getVOU_NO());
			vatpur_clone.setVOU_SEQ(vou_seq[0]);
			vatpur_clone.setPOSTDATE(header.getPOSTDATE());
			vatpur_clone.setPOST_TYPE(2);// 2=ขาล้างหนี้
			if (vatpur_old.getNUM_TYPE().intValue() == 1) {// ขาตั้งเป็นบวกให้เครดิตออก
				vatpur_clone.setNUM_TYPE(BigDecimal.ONE.negate());
			} else {// ขาตั้งเป็นลบให้เดบิตออก
				vatpur_clone.setNUM_TYPE(BigDecimal.ONE);
			}
			vatpur_clone.setAMT(decRCV_AMT.getValue().abs());
			vatpur_clone.setBASE_AMT(BigDecimal.ZERO);
			vatpur_clone.setCLEAR_AMT(BigDecimal.ZERO);
			vatpur_clone.setRECSTA(header.getRECSTA());
			vatpur_clone.setINSBY(header.getUPBY());
			vatpur_clone.setINSDT(header.getUPDT());
			vatpur_clone.setUPBY(header.getUPBY());
			vatpur_clone.setUPDT(header.getUPDT());

			if (!TbfACGL_VATPUR.insert(dbc, vatpur_clone)) {
				String errMsg = "ไม่สามารถบันทึก รายการที่" + count;
				logger.info(errMsg);
				throw new Exception(errMsg);
			}

		}

	}

	private static void getRecord(FDbc dbc, TboACGL_VATPUR vatpur_clone, TboACGL_VATPUR vatpur_old, FModelHasMap dat,
			int count, LoginBean loginBean) throws SQLException, Exception {

		int idx = 1;
		try (java.sql.PreparedStatement ps = TbfACGL_VATPUR.getPrepareStmFind(dbc);) {

			ps.setString(idx++, loginBean.getCOMP_CDE());
			ps.setString(idx++, dat.getString("VOU_TYPE"));
			ps.setString(idx++, dat.getString("VOU_NO"));
			ps.setInt(idx++, dat.getInt("VOU_SEQ"));
			ps.setInt(idx++, dat.getInt("VOU_DSEQ"));

			try (java.sql.ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					TbfACGL_VATPUR.setModel(rs, vatpur_clone);// clone ของเก่าเอาไว้
					TbfACGL_VATPUR.setModel(rs, vatpur_old);// ข้อมูลเก่า
				} else {
					throw new Exception("ไม่พบรายการที่" + count);
				}
			}
		}

	}

}

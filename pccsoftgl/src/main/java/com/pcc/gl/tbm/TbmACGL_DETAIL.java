package com.pcc.gl.tbm;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.pcc.gl.progman.ManAcEntr;
import com.pcc.gl.tbf.TbfACGL_DETAIL;
import com.pcc.gl.tbo.TboACCT_NO;
import com.pcc.gl.tbo.TboACCT_NO_SUB;
import com.pcc.gl.tbo.TboACGL_DETAIL;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.SqlStr;

public class TbmACGL_DETAIL {

	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static void updateRECSTA(FDbc dbc, int recsta, String comp_cde, String vou_type, String vou_no)
			throws SQLException {

		String sql = " UPDATE " + TboACGL_DETAIL.tablename + " set RECSTA = " + recsta
				+ " where COMP_CDE = ? and VOU_TYPE = ? and VOU_NO = ? ";
		java.sql.PreparedStatement ps = dbc.getPreparedStatement(sql);
		int idx = 1;
		ps.setString(idx++, comp_cde);
		ps.setString(idx++, vou_type);
		ps.setString(idx++, vou_no);
		logger.info("UPDATE : " + ps.executeUpdate());

	}

	public static void delete_by_vouno(FDbc dbc, String comp_cde, String vou_type, String vou_no) throws SQLException {

		String sql = " delete from " + TboACGL_DETAIL.tablename
				+ " where COMP_CDE = ? and VOU_TYPE = ? and VOU_NO = ? ";
		java.sql.PreparedStatement ps = dbc.getPreparedStatement(sql);
		int idx = 1;
		ps.setString(idx++, comp_cde);
		ps.setString(idx++, vou_type);
		ps.setString(idx++, vou_no);
		logger.info("DELETE : " + ps.executeUpdate());

	}

	public static void getDataForAcEntr(java.util.List<FModelHasMap> lst_dat, String vou_type, String vou_no,
			LoginBean _loginBean) throws Exception {

		lst_dat.clear();

		try (FDbc dbc = FDbc.connectMasterDb()) {

			SqlStr sql = new SqlStr();
			sql.addLine("select aa.COMP_CDE,aa.VOU_TYPE, aa.VOU_NO, aa.VOU_SEQ, aa.VOU_SEQ_SHOW ,");
			sql.addLine(" aa.ACCT_ID, aa.SECT_ID, aa.NUM_TYPE, aa.AMT, aa.DESCR, aa.DESCR_SUB, aa.SUB_HAS, aa.SUB_APPR, aa.APAR_RECTYP, act.ACCT_NAME, sub.SUB_TYPE");
			sql.addLine("from " + TboACGL_DETAIL.tablename + " aa");
			sql.addLine("left join " + TboACCT_NO.tablename + " act on aa.COMP_CDE=act.COMP_CDE and aa.ACCT_ID=act.ACCT_ID ");
			sql.addLine("left join " + TboACCT_NO_SUB.tablename + " sub on aa.COMP_CDE=sub.COMP_CDE and aa.ACCT_ID=sub.ACCT_ID ");
			sql.addLine("where aa.COMP_CDE = '" + Fnc.sqlQuote(_loginBean.getCOMP_CDE()) + "' ");
			sql.addLine("and aa.VOU_TYPE = '" + Fnc.sqlQuote(vou_type) + "' ");
			sql.addLine("and aa.VOU_NO = '" + Fnc.sqlQuote(vou_no) + "' ");
			sql.addLine("order by aa.VOU_SEQ_SHOW");

			logger.info(sql.getSql());

			TboACGL_DETAIL detail = new TboACGL_DETAIL();
			int[] acct_type = { 0 };

			try (java.sql.ResultSet rs = dbc.getResultSetFw3(sql.getSql(), 1000);) {
				while (rs.next()) {
					FModelHasMap dat = new FModelHasMap();

					dat.putRecord(rs);
					if (Fnc.isEmpty(rs.getString("SUB_HAS"))) {
						if (rs.getInt("SUB_TYPE") == 0) {// ไม่ใช่ เจ้าหนี้,ลูกหนี้,เช็คจ่ายล่วงหน้า ในตาราง ACCT_NO_SUB

							acct_type[0] = 0;
							detail.setACCT_ID(rs.getString("ACCT_ID"));
							detail.setNUM_TYPE(rs.getBigDecimal("NUM_TYPE"));

							ManAcEntr.isSUB_HAS(dbc, detail, _loginBean, acct_type);

							dat.setInt("SUB_TYPE", acct_type[0]);
						}
					} else {
						// กรณีที่ SUB_HAS <> '' อยู่แล้วไม่ต้องเสียเวลาไปค้นหา
						dat.setInt("SUB_TYPE", 99);// mark ค่าเพื่อให้แสดงปุ่มตัวย่อย
					}

					lst_dat.add(dat);
				}
			}

		}

	}

	public static void checkDebitCredit(FDbc dbc, String vou_type, String vou_no, LoginBean _loginBean)
			throws Exception {

		SqlStr sql = new SqlStr();
		sql.addLine("select sum(case when a.NUM_TYPE = 1 then a.AMT else 0 end) as sum_debit , ");
		sql.addLine("sum(case when a.NUM_TYPE = -1 then a.AMT else 0 end) as sum_credit ");
		sql.addLine("from " + TboACGL_DETAIL.tablename + " a ");
		sql.addLine("where a.COMP_CDE='" + Fnc.sqlQuote(_loginBean.getCOMP_CDE()) + "' and a.VOU_TYPE='"
				+ Fnc.sqlQuote(vou_type) + "' and a.VOU_NO='" + Fnc.sqlQuote(vou_no) + "' ");
		try (java.sql.ResultSet rs = dbc.getResultSet(sql.getSql());) {
			if (rs.next()) {

				BigDecimal db = Fnc.getBigDecimal(rs.getBigDecimal("sum_debit"));
				BigDecimal cr = Fnc.getBigDecimal(rs.getBigDecimal("sum_credit"));

				if (db.compareTo(cr) != 0) {
					throw new Exception("ยอดเดบิตไม่เท่ากับเครดิต");
				}

			}
		}

	}

	public static void get_max_vouseq_by_vouno(FDbc dbc, String comp_cde, String vou_type, String vou_no,
			int[] ret_vouseq, int[] ret_vouseq_show) throws SQLException {

		String sql = " select max(VOU_SEQ) as VOU_SEQ , max(VOU_SEQ_SHOW) as VOU_SEQ_SHOW from "
				+ TboACGL_DETAIL.tablename + " where COMP_CDE = ? and VOU_TYPE = ? and VOU_NO = ? ";
		try (java.sql.PreparedStatement ps = dbc.getPreparedStatement(sql);) {
			int idx = 1;
			ps.setString(idx++, comp_cde);
			ps.setString(idx++, vou_type);
			ps.setString(idx++, vou_no);
			try (java.sql.ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					ret_vouseq[0] = rs.getInt("VOU_SEQ");
					ret_vouseq_show[0] = rs.getInt("VOU_SEQ_SHOW");
				}
			}
		}

	}

	public static void updateVouseq_Show(String comp_cde, String vou_type, String vou_no,
			java.util.List<FModelHasMap> lst_dat, int vou_seq, int to_vou_seq_show) throws Exception {

		try (FDbc dbc = FDbc.connectMasterDb()) {
			dbc.beginTrans();
			updateVouseq_Show(dbc, comp_cde, vou_type, vou_no, lst_dat, vou_seq, to_vou_seq_show);
			dbc.commit();
		}

	}

	public static void updateVouseq_Show(FDbc dbc, String comp_cde, String vou_type, String vou_no,
			java.util.List<FModelHasMap> lst_dat, int vou_seq, int to_vou_seq_show) throws Exception {

		// == loop update

		String sql = "SELECT VOU_SEQ, VOU_SEQ_SHOW from " + TboACGL_DETAIL.tablename + " where COMP_CDE = '"
				+ Fnc.sqlQuote(comp_cde) + "' and VOU_TYPE =  '" + Fnc.sqlQuote(vou_type) + "' and VOU_NO =  '"
				+ Fnc.sqlQuote(vou_no) + "' and VOU_SEQ != " + vou_seq + " order by VOU_SEQ_SHOW ";// ไม่เอา vou_seq ที่จะเลื่อน

		String sql_update = " UPDATE " + TboACGL_DETAIL.tablename + " set VOU_SEQ_SHOW = ? "
				+ " where COMP_CDE = ? and VOU_TYPE = ? and VOU_NO = ? and VOU_SEQ = ?";

		try (java.sql.ResultSet rs = dbc.getResultSet(sql);
				java.sql.PreparedStatement ps = dbc.getPreparedStatement(sql_update);) {

			int new_idx = 0;
			int update_count = 0;

			while (rs.next()) {

				new_idx++;
				if (new_idx == to_vou_seq_show) {
					new_idx++;
				}

				int i_vou_seq = rs.getInt("VOU_SEQ");

				ps.clearParameters();

				int idx = 1;
				ps.setInt(idx++, new_idx);// VOU_SEQ_SHOW
				ps.setString(idx++, comp_cde);// COMP_CDE
				ps.setString(idx++, vou_type);// VOU_TYPE
				ps.setString(idx++, vou_no);// VOU_NO
				ps.setInt(idx++, i_vou_seq);// VOU_SEQ
				update_count += ps.executeUpdate();

			}

			logger.info("UPDATE : " + update_count);

			// == update ตัวที่เลือก
			TboACGL_DETAIL detail = new TboACGL_DETAIL();

			detail.setCOMP_CDE(comp_cde);
			detail.setVOU_TYPE(vou_type);
			detail.setVOU_NO(vou_no);
			detail.setVOU_SEQ(vou_seq);

			if (TbfACGL_DETAIL.find(dbc, detail)) {

				if (to_vou_seq_show > lst_dat.size()) {
					detail.setVOU_SEQ_SHOW(lst_dat.size());
				} else {
					detail.setVOU_SEQ_SHOW(to_vou_seq_show);
				}

				if (!TbfACGL_DETAIL.update(dbc, detail, "")) {
					throw new Exception("ไม่สามารถบันทึกได้กรุณาตรวจสอบ");
				}

			}

		}

	}

	public static TboACGL_DETAIL get_record(FDbc dbc, String comp_cde, String vou_type, String vou_no, int vou_seq)
			throws SQLException, Exception {

		TboACGL_DETAIL det = new TboACGL_DETAIL();

		det.setCOMP_CDE(comp_cde);
		det.setVOU_TYPE(vou_type);
		det.setVOU_NO(vou_no);
		det.setVOU_SEQ(vou_seq);

		if (TbfACGL_DETAIL.find(dbc, det)) {
			return det;
		} else {
			return null;
		}

	}

}

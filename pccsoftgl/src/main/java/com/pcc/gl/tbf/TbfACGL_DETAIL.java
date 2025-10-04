package com.pcc.gl.tbf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pcc.gl.tbo.TboACGL_DETAIL;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;

public class TbfACGL_DETAIL {
	public static boolean insert(TboACGL_DETAIL model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return insert(dbc, model);
		}
	}

	public static boolean insert(FDbc dbc, TboACGL_DETAIL model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmInsert(dbc);) {
			setPrepareStm(pstm, model);
			int ins = pstm.executeUpdate();
			if (ins >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean update(TboACGL_DETAIL model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model);
		}
	}

	public static boolean update(FDbc dbc, TboACGL_DETAIL model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmUpdate(dbc, "");) {
			setPrepareStm(pstm, model);
			int ins = pstm.executeUpdate();
			if (ins >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean update(TboACGL_DETAIL model, String fixWhere) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model, fixWhere);
		}
	}

	public static boolean update(FDbc dbc, TboACGL_DETAIL model, String fixWhere) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmUpdate(dbc, fixWhere);) {
			setPrepareStm(pstm, model);
			int ins = pstm.executeUpdate();
			if (ins >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean delete(TboACGL_DETAIL model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return delete(dbc, model);
		}
	}

	public static boolean delete(FDbc dbc, TboACGL_DETAIL model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmDelete(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setString(2, model.getVOU_TYPE());
			pstm.setString(3, model.getVOU_NO());
			pstm.setInt(4, model.getVOU_SEQ());
			int del = pstm.executeUpdate();
			if (del >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean find(TboACGL_DETAIL model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return find(dbc, model);
		}
	}

	public static boolean find(FDbc dbc, TboACGL_DETAIL model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmFind(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setString(2, model.getVOU_TYPE());
			pstm.setString(3, model.getVOU_NO());
			pstm.setInt(4, model.getVOU_SEQ());
			try (ResultSet rs = pstm.executeQuery();) {
				if (rs.next()) {
					setModel(rs, model);
					return true;
				}
			}
		}
		return false;
	}

	public static PreparedStatement getPrepareStmInsert(FDbc dbc) throws SQLException, Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO ").append(TboACGL_DETAIL.tablename).append("(");
		sql.append(" VOU_SEQ_SHOW,");
		sql.append(" POSTDATE,");
		sql.append(" ACCT_ID,");
		sql.append(" AMT,");
		sql.append(" NUM_TYPE,");
		sql.append(" SECT_ID,");
		sql.append(" DESCR,");
		sql.append(" DESCR_SUB,");
		sql.append(" RECSTA,");
		sql.append(" SUB_HAS,");
		sql.append(" SUB_APPR,");
		sql.append(" SUB_APPR_BY,");
		sql.append(" ACCT_OPEN,");
		sql.append(" APAR_VOU_TYPE,");
		sql.append(" APAR_VOU_NO,");
		sql.append(" APAR_VOU_SEQ,");
		sql.append(" APAR_VOU_DSEQ,");
		sql.append(" APAR_RECTYP,");
		sql.append(" APAR_DESCR,");
		sql.append(" APAR_AMT,");
		sql.append(" CHQ_TYPE,");
		sql.append(" CHQ_NO,");
		sql.append(" CHQ_DATE,");
		sql.append(" CHQ_PAYEE,");
		sql.append(" CHQ_WD_VOU_TYPE,");
		sql.append(" CHQ_WD_VOU_NO,");
		sql.append(" CHQ_WD_VOU_SEQ,");
		sql.append(" CHQ_WD_USE,");
		sql.append(" COMP_CDE,");
		sql.append(" VOU_TYPE,");
		sql.append(" VOU_NO,");
		sql.append(" VOU_SEQ)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmUpdate(FDbc dbc, String fixWhere) throws SQLException, Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ").append(TboACGL_DETAIL.tablename).append(" SET ");
		sql.append(" VOU_SEQ_SHOW=?,");
		sql.append(" POSTDATE=?,");
		sql.append(" ACCT_ID=?,");
		sql.append(" AMT=?,");
		sql.append(" NUM_TYPE=?,");
		sql.append(" SECT_ID=?,");
		sql.append(" DESCR=?,");
		sql.append(" DESCR_SUB=?,");
		sql.append(" RECSTA=?,");
		sql.append(" SUB_HAS=?,");
		sql.append(" SUB_APPR=?,");
		sql.append(" SUB_APPR_BY=?,");
		sql.append(" ACCT_OPEN=?,");
		sql.append(" APAR_VOU_TYPE=?,");
		sql.append(" APAR_VOU_NO=?,");
		sql.append(" APAR_VOU_SEQ=?,");
		sql.append(" APAR_VOU_DSEQ=?,");
		sql.append(" APAR_RECTYP=?,");
		sql.append(" APAR_DESCR=?,");
		sql.append(" APAR_AMT=?,");
		sql.append(" CHQ_TYPE=?,");
		sql.append(" CHQ_NO=?,");
		sql.append(" CHQ_DATE=?,");
		sql.append(" CHQ_PAYEE=?,");
		sql.append(" CHQ_WD_VOU_TYPE=?,");
		sql.append(" CHQ_WD_VOU_NO=?,");
		sql.append(" CHQ_WD_VOU_SEQ=?,");
		sql.append(" CHQ_WD_USE=?");
		sql.append(" WHERE COMP_CDE=? AND VOU_TYPE=? AND VOU_NO=? AND VOU_SEQ=? ");
		if (!Fnc.isEmpty(fixWhere)) {
			sql.append(" AND " + fixWhere);
		}
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ").append(TboACGL_DETAIL.tablename);
		sql.append(" WHERE COMP_CDE=? AND VOU_TYPE=? AND VOU_NO=? AND VOU_SEQ=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ").append(TboACGL_DETAIL.tablename);
		sql.append(" WHERE COMP_CDE=? AND VOU_TYPE=? AND VOU_NO=? AND VOU_SEQ=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static void setPrepareStm(PreparedStatement pstm, TboACGL_DETAIL model) throws SQLException, Exception {
		pstm.clearParameters();
		pstm.setInt(1, model.getVOU_SEQ_SHOW());
		pstm.setDate(2, model.getPOSTDATE());
		pstm.setString(3, model.getACCT_ID());
		pstm.setBigDecimal(4, model.getAMT());
		pstm.setBigDecimal(5, model.getNUM_TYPE());
		pstm.setString(6, model.getSECT_ID());
		pstm.setString(7, model.getDESCR());
		pstm.setString(8, model.getDESCR_SUB());
		pstm.setInt(9, model.getRECSTA());
		pstm.setString(10, model.getSUB_HAS());
		pstm.setString(11, model.getSUB_APPR());
		pstm.setString(12, model.getSUB_APPR_BY());
		pstm.setString(13, model.getACCT_OPEN());
		pstm.setString(14, model.getAPAR_VOU_TYPE());
		pstm.setString(15, model.getAPAR_VOU_NO());
		pstm.setInt(16, model.getAPAR_VOU_SEQ());
		pstm.setInt(17, model.getAPAR_VOU_DSEQ());
		pstm.setInt(18, model.getAPAR_RECTYP());
		pstm.setString(19, model.getAPAR_DESCR());
		pstm.setBigDecimal(20, model.getAPAR_AMT());
		pstm.setString(21, model.getCHQ_TYPE());
		pstm.setString(22, model.getCHQ_NO());
		pstm.setDate(23, model.getCHQ_DATE());
		pstm.setString(24, model.getCHQ_PAYEE());
		pstm.setString(25, model.getCHQ_WD_VOU_TYPE());
		pstm.setString(26, model.getCHQ_WD_VOU_NO());
		pstm.setInt(27, model.getCHQ_WD_VOU_SEQ());
		pstm.setInt(28, model.getCHQ_WD_USE());
		pstm.setString(29, model.getCOMP_CDE());
		pstm.setString(30, model.getVOU_TYPE());
		pstm.setString(31, model.getVOU_NO());
		pstm.setInt(32, model.getVOU_SEQ());
	}

	public static void setModel(ResultSet rs, TboACGL_DETAIL model) throws SQLException, Exception {
		model.setVOU_SEQ_SHOW(rs.getInt("VOU_SEQ_SHOW"));
		model.setPOSTDATE(rs.getDate("POSTDATE"));
		model.setACCT_ID(rs.getString("ACCT_ID"));
		model.setAMT(rs.getBigDecimal("AMT"));
		model.setNUM_TYPE(rs.getBigDecimal("NUM_TYPE"));
		model.setSECT_ID(rs.getString("SECT_ID"));
		model.setDESCR(rs.getString("DESCR"));
		model.setDESCR_SUB(rs.getString("DESCR_SUB"));
		model.setRECSTA(rs.getInt("RECSTA"));
		model.setSUB_HAS(rs.getString("SUB_HAS"));
		model.setSUB_APPR(rs.getString("SUB_APPR"));
		model.setSUB_APPR_BY(rs.getString("SUB_APPR_BY"));
		model.setACCT_OPEN(rs.getString("ACCT_OPEN"));
		model.setAPAR_VOU_TYPE(rs.getString("APAR_VOU_TYPE"));
		model.setAPAR_VOU_NO(rs.getString("APAR_VOU_NO"));
		model.setAPAR_VOU_SEQ(rs.getInt("APAR_VOU_SEQ"));
		model.setAPAR_VOU_DSEQ(rs.getInt("APAR_VOU_DSEQ"));
		model.setAPAR_RECTYP(rs.getInt("APAR_RECTYP"));
		model.setAPAR_DESCR(rs.getString("APAR_DESCR"));
		model.setAPAR_AMT(rs.getBigDecimal("APAR_AMT"));
		model.setCHQ_TYPE(rs.getString("CHQ_TYPE"));
		model.setCHQ_NO(rs.getString("CHQ_NO"));
		model.setCHQ_DATE(rs.getDate("CHQ_DATE"));
		model.setCHQ_PAYEE(rs.getString("CHQ_PAYEE"));
		model.setCHQ_WD_VOU_TYPE(rs.getString("CHQ_WD_VOU_TYPE"));
		model.setCHQ_WD_VOU_NO(rs.getString("CHQ_WD_VOU_NO"));
		model.setCHQ_WD_VOU_SEQ(rs.getInt("CHQ_WD_VOU_SEQ"));
		model.setCHQ_WD_USE(rs.getInt("CHQ_WD_USE"));
		model.setCOMP_CDE(rs.getString("COMP_CDE"));
		model.setVOU_TYPE(rs.getString("VOU_TYPE"));
		model.setVOU_NO(rs.getString("VOU_NO"));
		model.setVOU_SEQ(rs.getInt("VOU_SEQ"));
	}
}
package com.pcc.gl.tbf;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pcc.gl.tbo.TboACGL_AR;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;

public class TbfACGL_AR {
	public static boolean insert(TboACGL_AR model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return insert(dbc, model);
		}
	}

	public static boolean insert(FDbc dbc, TboACGL_AR model) throws SQLException, Exception {
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

	public static boolean update(TboACGL_AR model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model);
		}
	}

	public static boolean update(FDbc dbc, TboACGL_AR model) throws SQLException, Exception {
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

	public static boolean update(TboACGL_AR model, String fixWhere) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model, fixWhere);
		}
	}

	public static boolean update(FDbc dbc, TboACGL_AR model, String fixWhere) throws SQLException, Exception {
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

	public static boolean delete(TboACGL_AR model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return delete(dbc, model);
		}
	}

	public static boolean delete(FDbc dbc, TboACGL_AR model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmDelete(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setString(2, model.getVOU_TYPE());
			pstm.setString(3, model.getVOU_NO());
			pstm.setInt(4, model.getVOU_SEQ());
			pstm.setInt(5, model.getVOU_DSEQ());
			int del = pstm.executeUpdate();
			if (del >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean find(TboACGL_AR model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return find(dbc, model);
		}
	}

	public static boolean find(FDbc dbc, TboACGL_AR model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmFind(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setString(2, model.getVOU_TYPE());
			pstm.setString(3, model.getVOU_NO());
			pstm.setInt(4, model.getVOU_SEQ());
			pstm.setInt(5, model.getVOU_DSEQ());
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
		sql.append(" INSERT INTO ").append(TboACGL_AR.tablename).append("(");
		sql.append(" POSTDATE,");
		sql.append(" ACCT_ID,");
		sql.append(" POST_TYPE,");
		sql.append(" NUM_TYPE,");
		sql.append(" AMT,");
		sql.append(" CLEAR_AMT,");
		sql.append(" SECT_ID,");
		sql.append(" DESCR,");
		sql.append(" RECSTA,");
		sql.append(" DOCNO,");
		sql.append(" DOCDATE,");
		sql.append(" CUST_CDE,");
		sql.append(" DUEDATE,");
		sql.append(" LINK_NO,");
		sql.append(" INSBY,");
		sql.append(" INSDT,");
		sql.append(" UPBY,");
		sql.append(" UPDT,");
		sql.append(" COMP_CDE,");
		sql.append(" VOU_TYPE,");
		sql.append(" VOU_NO,");
		sql.append(" VOU_SEQ,");
		sql.append(" VOU_DSEQ)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmUpdate(FDbc dbc, String fixWhere) throws SQLException, Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ").append(TboACGL_AR.tablename).append(" SET ");
		sql.append(" POSTDATE=?,");
		sql.append(" ACCT_ID=?,");
		sql.append(" POST_TYPE=?,");
		sql.append(" NUM_TYPE=?,");
		sql.append(" AMT=?,");
		sql.append(" CLEAR_AMT=?,");
		sql.append(" SECT_ID=?,");
		sql.append(" DESCR=?,");
		sql.append(" RECSTA=?,");
		sql.append(" DOCNO=?,");
		sql.append(" DOCDATE=?,");
		sql.append(" CUST_CDE=?,");
		sql.append(" DUEDATE=?,");
		sql.append(" LINK_NO=?,");
		sql.append(" INSBY=?,");
		sql.append(" INSDT=?,");
		sql.append(" UPBY=?,");
		sql.append(" UPDT=?");
		sql.append(" WHERE COMP_CDE=? AND VOU_TYPE=? AND VOU_NO=? AND VOU_SEQ=? AND VOU_DSEQ=? ");
		if (!Fnc.isEmpty(fixWhere)) {
			sql.append(" AND " + fixWhere);
		}
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ").append(TboACGL_AR.tablename);
		sql.append(" WHERE COMP_CDE=? AND VOU_TYPE=? AND VOU_NO=? AND VOU_SEQ=? AND VOU_DSEQ=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ").append(TboACGL_AR.tablename);
		sql.append(" WHERE COMP_CDE=? AND VOU_TYPE=? AND VOU_NO=? AND VOU_SEQ=? AND VOU_DSEQ=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static void setPrepareStm(PreparedStatement pstm, TboACGL_AR model) throws SQLException, Exception {
		pstm.clearParameters();
		pstm.setDate(1, model.getPOSTDATE());
		pstm.setString(2, model.getACCT_ID());
		pstm.setInt(3, model.getPOST_TYPE());
		pstm.setBigDecimal(4, model.getNUM_TYPE());
		pstm.setBigDecimal(5, model.getAMT());
		pstm.setBigDecimal(6, model.getCLEAR_AMT());
		pstm.setString(7, model.getSECT_ID());
		pstm.setString(8, model.getDESCR());
		pstm.setInt(9, model.getRECSTA());
		pstm.setString(10, model.getDOCNO());
		pstm.setDate(11, model.getDOCDATE());
		pstm.setString(12, model.getCUST_CDE());
		pstm.setDate(13, model.getDUEDATE());
		pstm.setString(14, model.getLINK_NO());
		pstm.setString(15, model.getINSBY());
		pstm.setTimestamp(16, model.getINSDT());
		pstm.setString(17, model.getUPBY());
		pstm.setTimestamp(18, model.getUPDT());
		pstm.setString(19, model.getCOMP_CDE());
		pstm.setString(20, model.getVOU_TYPE());
		pstm.setString(21, model.getVOU_NO());
		pstm.setInt(22, model.getVOU_SEQ());
		pstm.setInt(23, model.getVOU_DSEQ());
	}

	public static void setModel(ResultSet rs, TboACGL_AR model) throws SQLException, Exception {
		model.setPOSTDATE(rs.getDate("POSTDATE"));
		model.setACCT_ID(rs.getString("ACCT_ID"));
		model.setPOST_TYPE(rs.getInt("POST_TYPE"));
		model.setNUM_TYPE(rs.getBigDecimal("NUM_TYPE"));
		model.setAMT(rs.getBigDecimal("AMT"));
		model.setCLEAR_AMT(rs.getBigDecimal("CLEAR_AMT"));
		model.setSECT_ID(rs.getString("SECT_ID"));
		model.setDESCR(rs.getString("DESCR"));
		model.setRECSTA(rs.getInt("RECSTA"));
		model.setDOCNO(rs.getString("DOCNO"));
		model.setDOCDATE(rs.getDate("DOCDATE"));
		model.setCUST_CDE(rs.getString("CUST_CDE"));
		model.setDUEDATE(rs.getDate("DUEDATE"));
		model.setLINK_NO(rs.getString("LINK_NO"));
		model.setINSBY(rs.getString("INSBY"));
		model.setINSDT(rs.getTimestamp("INSDT"));
		model.setUPBY(rs.getString("UPBY"));
		model.setUPDT(rs.getTimestamp("UPDT"));
		model.setCOMP_CDE(rs.getString("COMP_CDE"));
		model.setVOU_TYPE(rs.getString("VOU_TYPE"));
		model.setVOU_NO(rs.getString("VOU_NO"));
		model.setVOU_SEQ(rs.getInt("VOU_SEQ"));
		model.setVOU_DSEQ(rs.getInt("VOU_DSEQ"));
	}
}
package com.pcc.bx.tbf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pcc.bx.tbo.TboBXTMPLDETAIL;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;

public class TbfBXTMPLDETAIL {
	public static boolean insert(TboBXTMPLDETAIL model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return insert(dbc, model);
		}
	}

	public static boolean insert(FDbc dbc, TboBXTMPLDETAIL model) throws SQLException, Exception {
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

	public static boolean update(TboBXTMPLDETAIL model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model);
		}
	}

	public static boolean update(FDbc dbc, TboBXTMPLDETAIL model) throws SQLException, Exception {
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

	public static boolean update(TboBXTMPLDETAIL model, String fixWhere) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model, fixWhere);
		}
	}

	public static boolean update(FDbc dbc, TboBXTMPLDETAIL model, String fixWhere) throws SQLException, Exception {
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

	public static boolean delete(TboBXTMPLDETAIL model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return delete(dbc, model);
		}
	}

	public static boolean delete(FDbc dbc, TboBXTMPLDETAIL model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmDelete(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setInt(2, model.getTMPLCDE());
			pstm.setInt(3, model.getTMPLSEQ());
			int del = pstm.executeUpdate();
			if (del >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean find(TboBXTMPLDETAIL model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return find(dbc, model);
		}
	}

	public static boolean find(FDbc dbc, TboBXTMPLDETAIL model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmFind(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setInt(2, model.getTMPLCDE());
			pstm.setInt(3, model.getTMPLSEQ());
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
		sql.append(" INSERT INTO ").append(TboBXTMPLDETAIL.tablename).append("(");
		sql.append(" DOCHEAD,");
		sql.append(" COPYFOR,");
		sql.append(" UPBY,");
		sql.append(" UPDT,");
		sql.append(" COMP_CDE,");
		sql.append(" TMPLCDE,");
		sql.append(" TMPLSEQ)");
		sql.append(" VALUES(?,?,?,?,?,?,?)");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmUpdate(FDbc dbc, String fixWhere) throws SQLException, Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ").append(TboBXTMPLDETAIL.tablename).append(" SET ");
		sql.append(" DOCHEAD=?,");
		sql.append(" COPYFOR=?,");
		sql.append(" UPBY=?,");
		sql.append(" UPDT=?");
		sql.append(" WHERE COMP_CDE=? AND TMPLCDE=? AND TMPLSEQ=? ");
		if (!Fnc.isEmpty(fixWhere)) {
			sql.append(" AND " + fixWhere);
		}
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ").append(TboBXTMPLDETAIL.tablename);
		sql.append(" WHERE COMP_CDE=? AND TMPLCDE=? AND TMPLSEQ=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ").append(TboBXTMPLDETAIL.tablename);
		sql.append(" WHERE COMP_CDE=? AND TMPLCDE=? AND TMPLSEQ=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static void setPrepareStm(PreparedStatement pstm, TboBXTMPLDETAIL model) throws SQLException, Exception {
		pstm.clearParameters();
		pstm.setString(1, model.getDOCHEAD());
		pstm.setString(2, model.getCOPYFOR());
		pstm.setString(3, model.getUPBY());
		pstm.setTimestamp(4, model.getUPDT());
		pstm.setString(5, model.getCOMP_CDE());
		pstm.setInt(6, model.getTMPLCDE());
		pstm.setInt(7, model.getTMPLSEQ());
	}

	public static void setModel(ResultSet rs, TboBXTMPLDETAIL model) throws SQLException, Exception {
		model.setDOCHEAD(rs.getString("DOCHEAD"));
		model.setCOPYFOR(rs.getString("COPYFOR"));
		model.setUPBY(rs.getString("UPBY"));
		model.setUPDT(rs.getTimestamp("UPDT"));
		model.setCOMP_CDE(rs.getString("COMP_CDE"));
		model.setTMPLCDE(rs.getInt("TMPLCDE"));
		model.setTMPLSEQ(rs.getInt("TMPLSEQ"));
	}
}
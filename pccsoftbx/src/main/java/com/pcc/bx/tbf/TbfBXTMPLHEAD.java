package com.pcc.bx.tbf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pcc.bx.tbo.TboBXTMPLHEAD;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;

public class TbfBXTMPLHEAD {
	public static boolean insert(TboBXTMPLHEAD model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return insert(dbc, model);
		}
	}

	public static boolean insert(FDbc dbc, TboBXTMPLHEAD model) throws SQLException, Exception {
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

	public static boolean update(TboBXTMPLHEAD model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model);
		}
	}

	public static boolean update(FDbc dbc, TboBXTMPLHEAD model) throws SQLException, Exception {
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

	public static boolean update(TboBXTMPLHEAD model, String fixWhere) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model, fixWhere);
		}
	}

	public static boolean update(FDbc dbc, TboBXTMPLHEAD model, String fixWhere) throws SQLException, Exception {
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

	public static boolean delete(TboBXTMPLHEAD model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return delete(dbc, model);
		}
	}

	public static boolean delete(FDbc dbc, TboBXTMPLHEAD model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmDelete(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setInt(2, model.getTMPLCDE());
			int del = pstm.executeUpdate();
			if (del >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean find(TboBXTMPLHEAD model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return find(dbc, model);
		}
	}

	public static boolean find(FDbc dbc, TboBXTMPLHEAD model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmFind(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setInt(2, model.getTMPLCDE());
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
		sql.append(" INSERT INTO ").append(TboBXTMPLHEAD.tablename).append("(");
		sql.append(" DOCPREFIX,");
		sql.append(" TMPLNAME,");
		sql.append(" POSTCOST,");
		sql.append(" UPBY,");
		sql.append(" UPDT,");
		sql.append(" COMP_CDE,");
		sql.append(" TMPLCDE)");
		sql.append(" VALUES(?,?,?,?,?,?,?)");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmUpdate(FDbc dbc, String fixWhere) throws SQLException, Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ").append(TboBXTMPLHEAD.tablename).append(" SET ");
		sql.append(" DOCPREFIX=?,");
		sql.append(" TMPLNAME=?,");
		sql.append(" POSTCOST=?,");
		sql.append(" UPBY=?,");
		sql.append(" UPDT=?");
		sql.append(" WHERE COMP_CDE=? AND TMPLCDE=? ");
		if (!Fnc.isEmpty(fixWhere)) {
			sql.append(" AND " + fixWhere);
		}
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ").append(TboBXTMPLHEAD.tablename);
		sql.append(" WHERE COMP_CDE=? AND TMPLCDE=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ").append(TboBXTMPLHEAD.tablename);
		sql.append(" WHERE COMP_CDE=? AND TMPLCDE=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static void setPrepareStm(PreparedStatement pstm, TboBXTMPLHEAD model) throws SQLException, Exception {
		pstm.clearParameters();
		pstm.setString(1, model.getDOCPREFIX());
		pstm.setString(2, model.getTMPLNAME());
		pstm.setString(3, model.getPOSTCOST());
		pstm.setString(4, model.getUPBY());
		pstm.setTimestamp(5, model.getUPDT());
		pstm.setString(6, model.getCOMP_CDE());
		pstm.setInt(7, model.getTMPLCDE());
	}

	public static void setModel(ResultSet rs, TboBXTMPLHEAD model) throws SQLException, Exception {
		model.setDOCPREFIX(rs.getString("DOCPREFIX"));
		model.setTMPLNAME(rs.getString("TMPLNAME"));
		model.setPOSTCOST(rs.getString("POSTCOST"));
		model.setUPBY(rs.getString("UPBY"));
		model.setUPDT(rs.getTimestamp("UPDT"));
		model.setCOMP_CDE(rs.getString("COMP_CDE"));
		model.setTMPLCDE(rs.getInt("TMPLCDE"));
	}
}
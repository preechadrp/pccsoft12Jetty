package com.pcc.sys.tbf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.tbo.TboFCOMP;

public class TbfFCOMP {
	public static boolean insert(TboFCOMP model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return insert(dbc, model);
		}
	}

	public static boolean insert(FDbc dbc, TboFCOMP model) throws SQLException, Exception {
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

	public static boolean update(TboFCOMP model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model);
		}
	}

	public static boolean update(FDbc dbc, TboFCOMP model) throws SQLException, Exception {
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

	public static boolean update(TboFCOMP model, String fixWhere) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model, fixWhere);
		}
	}

	public static boolean update(FDbc dbc, TboFCOMP model, String fixWhere) throws SQLException, Exception {
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

	public static boolean delete(TboFCOMP model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return delete(dbc, model);
		}
	}

	public static boolean delete(FDbc dbc, TboFCOMP model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmDelete(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			int del = pstm.executeUpdate();
			if (del >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean find(TboFCOMP model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return find(dbc, model);
		}
	}

	public static boolean find(FDbc dbc, TboFCOMP model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmFind(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
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
		sql.append(" INSERT INTO ").append(TboFCOMP.tablename).append("(");
		sql.append(" COMP_NAME,");
		sql.append(" COMP_SHORTNAME,");
		sql.append(" TAXNO,");
		sql.append(" OPENCOMP,");
		sql.append(" GLMONTH,");
		sql.append(" GLDAY,");
		sql.append(" INSBY,");
		sql.append(" INSDT,");
		sql.append(" UPBY,");
		sql.append(" UPDT,");
		sql.append(" COMP_CDE)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?)");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmUpdate(FDbc dbc, String fixWhere) throws SQLException, Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ").append(TboFCOMP.tablename).append(" SET ");
		sql.append(" COMP_NAME=?,");
		sql.append(" COMP_SHORTNAME=?,");
		sql.append(" TAXNO=?,");
		sql.append(" OPENCOMP=?,");
		sql.append(" GLMONTH=?,");
		sql.append(" GLDAY=?,");
		sql.append(" INSBY=?,");
		sql.append(" INSDT=?,");
		sql.append(" UPBY=?,");
		sql.append(" UPDT=?");
		sql.append(" WHERE COMP_CDE=? ");
		if (!Fnc.isEmpty(fixWhere)) {
			sql.append(" AND " + fixWhere);
		}
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ").append(TboFCOMP.tablename);
		sql.append(" WHERE COMP_CDE=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ").append(TboFCOMP.tablename);
		sql.append(" WHERE COMP_CDE=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static void setPrepareStm(PreparedStatement pstm, TboFCOMP model) throws SQLException, Exception {
		pstm.clearParameters();
		pstm.setString(1, model.getCOMP_NAME());
		pstm.setString(2, model.getCOMP_SHORTNAME());
		pstm.setString(3, model.getTAXNO());
		pstm.setDate(4, model.getOPENCOMP());
		pstm.setInt(5, model.getGLMONTH());
		pstm.setInt(6, model.getGLDAY());
		pstm.setString(7, model.getINSBY());
		pstm.setTimestamp(8, model.getINSDT());
		pstm.setString(9, model.getUPBY());
		pstm.setTimestamp(10, model.getUPDT());
		pstm.setString(11, model.getCOMP_CDE());
	}

	public static void setModel(ResultSet rs, TboFCOMP model) throws SQLException, Exception {
		model.setCOMP_NAME(rs.getString("COMP_NAME"));
		model.setCOMP_SHORTNAME(rs.getString("COMP_SHORTNAME"));
		model.setTAXNO(rs.getString("TAXNO"));
		model.setOPENCOMP(rs.getDate("OPENCOMP"));
		model.setGLMONTH(rs.getInt("GLMONTH"));
		model.setGLDAY(rs.getInt("GLDAY"));
		model.setINSBY(rs.getString("INSBY"));
		model.setINSDT(rs.getTimestamp("INSDT"));
		model.setUPBY(rs.getString("UPBY"));
		model.setUPDT(rs.getTimestamp("UPDT"));
		model.setCOMP_CDE(rs.getString("COMP_CDE"));
	}
}
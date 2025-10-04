package com.pcc.sys.tbf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.tbo.TboFCALLMENULOG;

public class TbfFCALLMENULOG {
	public static boolean insert(TboFCALLMENULOG model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return insert(dbc, model);
		}
	}

	public static boolean insert(FDbc dbc, TboFCALLMENULOG model) throws SQLException, Exception {
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

	public static boolean update(TboFCALLMENULOG model, String fixWhere) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model, fixWhere);
		}
	}

	public static boolean update(FDbc dbc, TboFCALLMENULOG model, String fixWhere) throws SQLException, Exception {
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

	public static boolean delete(TboFCALLMENULOG model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return delete(dbc, model);
		}
	}

	public static boolean delete(FDbc dbc, TboFCALLMENULOG model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmDelete(dbc);) {
			pstm.setTimestamp(1, model.getCALLTIME());
			pstm.setString(2, model.getUSER_ID());
			int del = pstm.executeUpdate();
			if (del >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean find(TboFCALLMENULOG model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return find(dbc, model);
		}
	}

	public static boolean find(FDbc dbc, TboFCALLMENULOG model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmFind(dbc);) {
			pstm.setTimestamp(1, model.getCALLTIME());
			pstm.setString(2, model.getUSER_ID());
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
		sql.append(" INSERT INTO ").append(TboFCALLMENULOG.tablename).append("(");
		sql.append(" MENU_ID2,");
		sql.append(" THAI_NAME,");
		sql.append(" CALLTIME,");
		sql.append(" USER_ID)");
		sql.append(" VALUES(?,?,?,?)");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmUpdate(FDbc dbc, String fixWhere) throws SQLException, Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ").append(TboFCALLMENULOG.tablename).append(" SET ");
		sql.append(" MENU_ID2=?,");
		sql.append(" THAI_NAME=?");
		sql.append(" WHERE CALLTIME=? AND USER_ID=? ");
		if (!Fnc.isEmpty(fixWhere)) {
			sql.append(" AND " + fixWhere);
		}
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ").append(TboFCALLMENULOG.tablename);
		sql.append(" WHERE CALLTIME=? AND USER_ID=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ").append(TboFCALLMENULOG.tablename);
		sql.append(" WHERE CALLTIME=? AND USER_ID=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static void setPrepareStm(PreparedStatement pstm, TboFCALLMENULOG model) throws SQLException, Exception {
		pstm.clearParameters();
		pstm.setString(1, model.getMENU_ID2());
		pstm.setString(2, model.getTHAI_NAME());
		pstm.setTimestamp(3, model.getCALLTIME());
		pstm.setString(4, model.getUSER_ID());
	}

	public static void setModel(ResultSet rs, TboFCALLMENULOG model) throws SQLException, Exception {
		model.setMENU_ID2(rs.getString("MENU_ID2"));
		model.setTHAI_NAME(rs.getString("THAI_NAME"));
		model.setCALLTIME(rs.getTimestamp("CALLTIME"));
		model.setUSER_ID(rs.getString("USER_ID"));
	}
}
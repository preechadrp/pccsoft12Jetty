package com.pcc.sys.tbf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.tbo.TboFMENU_GROUP_H;

public class TbfFMENU_GROUP_H {
	public static boolean insert(TboFMENU_GROUP_H model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return insert(dbc, model);
		}
	}

	public static boolean insert(FDbc dbc, TboFMENU_GROUP_H model) throws SQLException, Exception {
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

	public static boolean update(TboFMENU_GROUP_H model, String fixWhere) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model, fixWhere);
		}
	}

	public static boolean update(FDbc dbc, TboFMENU_GROUP_H model, String fixWhere) throws SQLException, Exception {
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

	public static boolean delete(TboFMENU_GROUP_H model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return delete(dbc, model);
		}
	}

	public static boolean delete(FDbc dbc, TboFMENU_GROUP_H model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmDelete(dbc);) {
			pstm.setString(1, model.getUSER_MENU_GROUP());
			int del = pstm.executeUpdate();
			if (del >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean find(TboFMENU_GROUP_H model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return find(dbc, model);
		}
	}

	public static boolean find(FDbc dbc, TboFMENU_GROUP_H model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmFind(dbc);) {
			pstm.setString(1, model.getUSER_MENU_GROUP());
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
		sql.append(" INSERT INTO ").append(TboFMENU_GROUP_H.tablename).append("(");
		sql.append(" THAI_NAME,");
		sql.append(" ENG_NAME,");
		sql.append(" USER_MENU_GROUP)");
		sql.append(" VALUES(?,?,?)");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmUpdate(FDbc dbc, String fixWhere) throws SQLException, Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ").append(TboFMENU_GROUP_H.tablename).append(" SET ");
		sql.append(" THAI_NAME=?,");
		sql.append(" ENG_NAME=?");
		sql.append(" WHERE USER_MENU_GROUP=? ");
		if (!Fnc.isEmpty(fixWhere)) {
			sql.append(" AND " + fixWhere);
		}
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ").append(TboFMENU_GROUP_H.tablename);
		sql.append(" WHERE USER_MENU_GROUP=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ").append(TboFMENU_GROUP_H.tablename);
		sql.append(" WHERE USER_MENU_GROUP=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static void setPrepareStm(PreparedStatement pstm, TboFMENU_GROUP_H model) throws SQLException, Exception {
		pstm.clearParameters();
		pstm.setString(1, model.getTHAI_NAME());
		pstm.setString(2, model.getENG_NAME());
		pstm.setString(3, model.getUSER_MENU_GROUP());
	}

	public static void setModel(ResultSet rs, TboFMENU_GROUP_H model) throws SQLException, Exception {
		model.setTHAI_NAME(rs.getString("THAI_NAME"));
		model.setENG_NAME(rs.getString("ENG_NAME"));
		model.setUSER_MENU_GROUP(rs.getString("USER_MENU_GROUP"));
	}
}
package com.pcc.sys.tbf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.tbo.TboFUSER_MENU;

public class TbfFUSER_MENU {
	public static boolean insert(TboFUSER_MENU model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return insert(dbc, model);
		}
	}

	public static boolean insert(FDbc dbc, TboFUSER_MENU model) throws SQLException, Exception {
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

	public static boolean update(TboFUSER_MENU model, String fixWhere) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model, fixWhere);
		}
	}

	public static boolean update(FDbc dbc, TboFUSER_MENU model, String fixWhere) throws SQLException, Exception {
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

	public static boolean delete(TboFUSER_MENU model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return delete(dbc, model);
		}
	}

	public static boolean delete(FDbc dbc, TboFUSER_MENU model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmDelete(dbc);) {
			pstm.setString(1, model.getUSER_ID());
			pstm.setString(2, model.getCOMP_CDE());
			pstm.setString(3, model.getUSER_MENU_GROUP());
			int del = pstm.executeUpdate();
			if (del >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean find(TboFUSER_MENU model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return find(dbc, model);
		}
	}

	public static boolean find(FDbc dbc, TboFUSER_MENU model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmFind(dbc);) {
			pstm.setString(1, model.getUSER_ID());
			pstm.setString(2, model.getCOMP_CDE());
			pstm.setString(3, model.getUSER_MENU_GROUP());
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
		sql.append(" INSERT INTO ").append(TboFUSER_MENU.tablename).append("(");
		sql.append(" USER_ID,");
		sql.append(" COMP_CDE,");
		sql.append(" USER_MENU_GROUP)");
		sql.append(" VALUES(?,?,?)");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmUpdate(FDbc dbc, String fixWhere) throws SQLException, Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ").append(TboFUSER_MENU.tablename).append(" SET ");
		sql.append(" WHERE USER_ID=? AND COMP_CDE=? AND USER_MENU_GROUP=? ");
		if (!Fnc.isEmpty(fixWhere)) {
			sql.append(" AND " + fixWhere);
		}
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ").append(TboFUSER_MENU.tablename);
		sql.append(" WHERE USER_ID=? AND COMP_CDE=? AND USER_MENU_GROUP=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ").append(TboFUSER_MENU.tablename);
		sql.append(" WHERE USER_ID=? AND COMP_CDE=? AND USER_MENU_GROUP=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static void setPrepareStm(PreparedStatement pstm, TboFUSER_MENU model) throws SQLException, Exception {
		pstm.clearParameters();
		pstm.setString(1, model.getUSER_ID());
		pstm.setString(2, model.getCOMP_CDE());
		pstm.setString(3, model.getUSER_MENU_GROUP());
	}

	public static void setModel(ResultSet rs, TboFUSER_MENU model) throws SQLException, Exception {
		model.setUSER_ID(rs.getString("USER_ID"));
		model.setCOMP_CDE(rs.getString("COMP_CDE"));
		model.setUSER_MENU_GROUP(rs.getString("USER_MENU_GROUP"));
	}
}
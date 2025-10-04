package com.pcc.sys.tbf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.tbo.TboFMENU_GROUP_D;

public class TbfFMENU_GROUP_D {

	public static boolean insert(TboFMENU_GROUP_D model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return insert(dbc, model);
		}
	}

	public static boolean insert(FDbc dbc, TboFMENU_GROUP_D model) throws SQLException, Exception {
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

	public static boolean update(TboFMENU_GROUP_D model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model);
		}
	}

	public static boolean update(FDbc dbc, TboFMENU_GROUP_D model) throws SQLException, Exception {
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

	public static boolean update(TboFMENU_GROUP_D model, String fixWhere) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model, fixWhere);
		}
	}

	public static boolean update(FDbc dbc, TboFMENU_GROUP_D model, String fixWhere) throws SQLException, Exception {
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

	public static boolean delete(TboFMENU_GROUP_D model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return delete(dbc, model);
		}
	}

	public static boolean delete(FDbc dbc, TboFMENU_GROUP_D model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmDelete(dbc);) {
			pstm.setString(1, model.getKEY1());
			pstm.setString(2, model.getUSER_MENU_GROUP());
			int del = pstm.executeUpdate();
			if (del >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean find(TboFMENU_GROUP_D model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return find(dbc, model);
		}
	}

	public static boolean find(FDbc dbc, TboFMENU_GROUP_D model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmFind(dbc);) {
			pstm.setString(1, model.getKEY1());
			pstm.setString(2, model.getUSER_MENU_GROUP());
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
		sql.append(" INSERT INTO ").append(TboFMENU_GROUP_D.tablename).append("(");
		sql.append(" MENU_LEVEL,");
		sql.append(" MODU,");
		sql.append(" MENU_ID1,");
		sql.append(" MENU_ID2,");
		sql.append(" THAI_NAME,");
		sql.append(" ENG_NAME,");
		sql.append(" MENU_ID3,");
		sql.append(" KEY1,");
		sql.append(" USER_MENU_GROUP)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?)");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmUpdate(FDbc dbc, String fixWhere) throws SQLException, Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ").append(TboFMENU_GROUP_D.tablename).append(" SET ");
		sql.append(" MENU_LEVEL=?,");
		sql.append(" MODU=?,");
		sql.append(" MENU_ID1=?,");
		sql.append(" MENU_ID2=?,");
		sql.append(" THAI_NAME=?,");
		sql.append(" ENG_NAME=?,");
		sql.append(" MENU_ID3=?");
		sql.append(" WHERE KEY1=? AND USER_MENU_GROUP=? ");
		if (!Fnc.isEmpty(fixWhere)) {
			sql.append(" AND " + fixWhere);
		}
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ").append(TboFMENU_GROUP_D.tablename);
		sql.append(" WHERE KEY1=? AND USER_MENU_GROUP=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ").append(TboFMENU_GROUP_D.tablename);
		sql.append(" WHERE KEY1=? AND USER_MENU_GROUP=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static void setPrepareStm(PreparedStatement pstm, TboFMENU_GROUP_D model) throws SQLException, Exception {
		pstm.clearParameters();
		pstm.setString(1, model.getMENU_LEVEL());
		pstm.setString(2, model.getMODU());
		pstm.setString(3, model.getMENU_ID1());
		pstm.setString(4, model.getMENU_ID2());
		pstm.setString(5, model.getTHAI_NAME());
		pstm.setString(6, model.getENG_NAME());
		pstm.setString(7, model.getMENU_ID3());
		pstm.setString(8, model.getKEY1());
		pstm.setString(9, model.getUSER_MENU_GROUP());
	}

	public static void setModel(ResultSet rs, TboFMENU_GROUP_D model) throws SQLException, Exception {
		model.setMENU_LEVEL(rs.getString("MENU_LEVEL"));
		model.setMODU(rs.getString("MODU"));
		model.setMENU_ID1(rs.getString("MENU_ID1"));
		model.setMENU_ID2(rs.getString("MENU_ID2"));
		model.setTHAI_NAME(rs.getString("THAI_NAME"));
		model.setENG_NAME(rs.getString("ENG_NAME"));
		model.setMENU_ID3(rs.getString("MENU_ID3"));
		model.setKEY1(rs.getString("KEY1"));
		model.setUSER_MENU_GROUP(rs.getString("USER_MENU_GROUP"));
	}
}
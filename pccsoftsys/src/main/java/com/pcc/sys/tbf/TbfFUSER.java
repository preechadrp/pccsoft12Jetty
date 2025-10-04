package com.pcc.sys.tbf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.tbo.TboFUSER;

public class TbfFUSER {
	public static boolean insert(TboFUSER model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return insert(dbc, model);
		}
	}

	public static boolean insert(FDbc dbc, TboFUSER model) throws SQLException, Exception {
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

	public static boolean update(TboFUSER model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model);
		}
	}

	public static boolean update(FDbc dbc, TboFUSER model) throws SQLException, Exception {
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

	public static boolean update(TboFUSER model, String fixWhere) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model, fixWhere);
		}
	}

	public static boolean update(FDbc dbc, TboFUSER model, String fixWhere) throws SQLException, Exception {
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

	public static boolean delete(TboFUSER model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return delete(dbc, model);
		}
	}

	public static boolean delete(FDbc dbc, TboFUSER model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmDelete(dbc);) {
			pstm.setString(1, model.getUSER_ID());
			int del = pstm.executeUpdate();
			if (del >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean find(TboFUSER model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return find(dbc, model);
		}
	}

	public static boolean find(FDbc dbc, TboFUSER model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmFind(dbc);) {
			pstm.setString(1, model.getUSER_ID());
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
		sql.append(" INSERT INTO ").append(TboFUSER.tablename).append("(");
		sql.append(" PASSWD,");
		sql.append(" TITLE,");
		sql.append(" FNAME_THAI,");
		sql.append(" LNAME_THAI,");
		sql.append(" USER_STATUS,");
		sql.append(" INSBY,");
		sql.append(" INSDT,");
		sql.append(" UPBY,");
		sql.append(" UPDT,");
		sql.append(" EXPIRE_PASS_DATE,");
		sql.append(" UPDATE_PASS_DATE,");
		sql.append(" MENU_LANG,");
		sql.append(" MAN_USER,");
		sql.append(" MAN_MENU_GROUP,");
		sql.append(" LAST_USE_COMP_CDE,");
		sql.append(" LAST_USE_BRANCH_ID,");
		sql.append(" USER_ID)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmUpdate(FDbc dbc, String fixWhere) throws SQLException, Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ").append(TboFUSER.tablename).append(" SET ");
		sql.append(" PASSWD=?,");
		sql.append(" TITLE=?,");
		sql.append(" FNAME_THAI=?,");
		sql.append(" LNAME_THAI=?,");
		sql.append(" USER_STATUS=?,");
		sql.append(" INSBY=?,");
		sql.append(" INSDT=?,");
		sql.append(" UPBY=?,");
		sql.append(" UPDT=?,");
		sql.append(" EXPIRE_PASS_DATE=?,");
		sql.append(" UPDATE_PASS_DATE=?,");
		sql.append(" MENU_LANG=?,");
		sql.append(" MAN_USER=?,");
		sql.append(" MAN_MENU_GROUP=?,");
		sql.append(" LAST_USE_COMP_CDE=?,");
		sql.append(" LAST_USE_BRANCH_ID=?");
		sql.append(" WHERE USER_ID=? ");
		if (!Fnc.isEmpty(fixWhere)) {
			sql.append(" AND " + fixWhere);
		}
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ").append(TboFUSER.tablename);
		sql.append(" WHERE USER_ID=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ").append(TboFUSER.tablename);
		sql.append(" WHERE USER_ID=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static void setPrepareStm(PreparedStatement pstm, TboFUSER model) throws SQLException, Exception {
		pstm.clearParameters();
		pstm.setString(1, model.getPASSWD());
		pstm.setString(2, model.getTITLE());
		pstm.setString(3, model.getFNAME_THAI());
		pstm.setString(4, model.getLNAME_THAI());
		pstm.setString(5, model.getUSER_STATUS());
		pstm.setString(6, model.getINSBY());
		pstm.setTimestamp(7, model.getINSDT());
		pstm.setString(8, model.getUPBY());
		pstm.setTimestamp(9, model.getUPDT());
		pstm.setDate(10, model.getEXPIRE_PASS_DATE());
		pstm.setDate(11, model.getUPDATE_PASS_DATE());
		pstm.setString(12, model.getMENU_LANG());
		pstm.setString(13, model.getMAN_USER());
		pstm.setString(14, model.getMAN_MENU_GROUP());
		pstm.setString(15, model.getLAST_USE_COMP_CDE());
		pstm.setString(16, model.getLAST_USE_BRANCH_ID());
		pstm.setString(17, model.getUSER_ID());
	}

	public static void setModel(ResultSet rs, TboFUSER model) throws SQLException, Exception {
		model.setPASSWD(rs.getString("PASSWD"));
		model.setTITLE(rs.getString("TITLE"));
		model.setFNAME_THAI(rs.getString("FNAME_THAI"));
		model.setLNAME_THAI(rs.getString("LNAME_THAI"));
		model.setUSER_STATUS(rs.getString("USER_STATUS"));
		model.setINSBY(rs.getString("INSBY"));
		model.setINSDT(rs.getTimestamp("INSDT"));
		model.setUPBY(rs.getString("UPBY"));
		model.setUPDT(rs.getTimestamp("UPDT"));
		model.setEXPIRE_PASS_DATE(rs.getDate("EXPIRE_PASS_DATE"));
		model.setUPDATE_PASS_DATE(rs.getDate("UPDATE_PASS_DATE"));
		model.setMENU_LANG(rs.getString("MENU_LANG"));
		model.setMAN_USER(rs.getString("MAN_USER"));
		model.setMAN_MENU_GROUP(rs.getString("MAN_MENU_GROUP"));
		model.setLAST_USE_COMP_CDE(rs.getString("LAST_USE_COMP_CDE"));
		model.setLAST_USE_BRANCH_ID(rs.getString("LAST_USE_BRANCH_ID"));
		model.setUSER_ID(rs.getString("USER_ID"));
	}
}
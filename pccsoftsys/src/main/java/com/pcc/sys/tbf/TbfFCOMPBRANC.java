package com.pcc.sys.tbf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.tbo.TboFCOMPBRANC;

public class TbfFCOMPBRANC {
	public static boolean insert(TboFCOMPBRANC model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return insert(dbc, model);
		}
	}

	public static boolean insert(FDbc dbc, TboFCOMPBRANC model) throws SQLException, Exception {
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

	public static boolean update(TboFCOMPBRANC model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model);
		}
	}

	public static boolean update(FDbc dbc, TboFCOMPBRANC model) throws SQLException, Exception {
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

	public static boolean update(TboFCOMPBRANC model, String fixWhere) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model, fixWhere);
		}
	}

	public static boolean update(FDbc dbc, TboFCOMPBRANC model, String fixWhere) throws SQLException, Exception {
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

	public static boolean delete(TboFCOMPBRANC model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return delete(dbc, model);
		}
	}

	public static boolean delete(FDbc dbc, TboFCOMPBRANC model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmDelete(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setInt(2, model.getBRANC_CDE());
			int del = pstm.executeUpdate();
			if (del >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean find(TboFCOMPBRANC model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return find(dbc, model);
		}
	}

	public static boolean find(FDbc dbc, TboFCOMPBRANC model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmFind(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setInt(2, model.getBRANC_CDE());
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
		sql.append(" INSERT INTO ").append(TboFCOMPBRANC.tablename).append("(");
		sql.append(" BRANC_NAME,");
		sql.append(" BRANC_SHORTNAME,");
		sql.append(" BRANCTAXNO,");
		sql.append(" ADDR1,");
		sql.append(" ADDR2,");
		sql.append(" TELNO,");
		sql.append(" FAXNO,");
		sql.append(" INSBY,");
		sql.append(" INSDT,");
		sql.append(" UPBY,");
		sql.append(" UPDT,");
		sql.append(" COMP_CDE,");
		sql.append(" BRANC_CDE)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmUpdate(FDbc dbc, String fixWhere) throws SQLException, Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ").append(TboFCOMPBRANC.tablename).append(" SET ");
		sql.append(" BRANC_NAME=?,");
		sql.append(" BRANC_SHORTNAME=?,");
		sql.append(" BRANCTAXNO=?,");
		sql.append(" ADDR1=?,");
		sql.append(" ADDR2=?,");
		sql.append(" TELNO=?,");
		sql.append(" FAXNO=?,");
		sql.append(" INSBY=?,");
		sql.append(" INSDT=?,");
		sql.append(" UPBY=?,");
		sql.append(" UPDT=?");
		sql.append(" WHERE COMP_CDE=? AND BRANC_CDE=? ");
		if (!Fnc.isEmpty(fixWhere)) {
			sql.append(" AND " + fixWhere);
		}
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ").append(TboFCOMPBRANC.tablename);
		sql.append(" WHERE COMP_CDE=? AND BRANC_CDE=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ").append(TboFCOMPBRANC.tablename);
		sql.append(" WHERE COMP_CDE=? AND BRANC_CDE=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static void setPrepareStm(PreparedStatement pstm, TboFCOMPBRANC model) throws SQLException, Exception {
		pstm.clearParameters();
		pstm.setString(1, model.getBRANC_NAME());
		pstm.setString(2, model.getBRANC_SHORTNAME());
		pstm.setString(3, model.getBRANCTAXNO());
		pstm.setString(4, model.getADDR1());
		pstm.setString(5, model.getADDR2());
		pstm.setString(6, model.getTELNO());
		pstm.setString(7, model.getFAXNO());
		pstm.setString(8, model.getINSBY());
		pstm.setTimestamp(9, model.getINSDT());
		pstm.setString(10, model.getUPBY());
		pstm.setTimestamp(11, model.getUPDT());
		pstm.setString(12, model.getCOMP_CDE());
		pstm.setInt(13, model.getBRANC_CDE());
	}

	public static void setModel(ResultSet rs, TboFCOMPBRANC model) throws SQLException, Exception {
		model.setBRANC_NAME(rs.getString("BRANC_NAME"));
		model.setBRANC_SHORTNAME(rs.getString("BRANC_SHORTNAME"));
		model.setBRANCTAXNO(rs.getString("BRANCTAXNO"));
		model.setADDR1(rs.getString("ADDR1"));
		model.setADDR2(rs.getString("ADDR2"));
		model.setTELNO(rs.getString("TELNO"));
		model.setFAXNO(rs.getString("FAXNO"));
		model.setINSBY(rs.getString("INSBY"));
		model.setINSDT(rs.getTimestamp("INSDT"));
		model.setUPBY(rs.getString("UPBY"));
		model.setUPDT(rs.getTimestamp("UPDT"));
		model.setCOMP_CDE(rs.getString("COMP_CDE"));
		model.setBRANC_CDE(rs.getInt("BRANC_CDE"));
	}
}

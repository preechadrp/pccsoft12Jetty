package com.pcc.gl.tbf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pcc.gl.tbo.TboACCT_LOCK;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;

public class TbfACCT_LOCK {
	public static boolean insert(TboACCT_LOCK model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return insert(dbc, model);
		}
	}

	public static boolean insert(FDbc dbc, TboACCT_LOCK model) throws SQLException, Exception {
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

	public static boolean update(TboACCT_LOCK model, String fixWhere) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model, fixWhere);
		}
	}

	public static boolean update(FDbc dbc, TboACCT_LOCK model, String fixWhere) throws SQLException, Exception {
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

	public static boolean delete(TboACCT_LOCK model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return delete(dbc, model);
		}
	}

	public static boolean delete(FDbc dbc, TboACCT_LOCK model) throws SQLException, Exception {
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

	public static boolean find(TboACCT_LOCK model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return find(dbc, model);
		}
	}

	public static boolean find(FDbc dbc, TboACCT_LOCK model) throws SQLException, Exception {
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
		sql.append(" INSERT INTO ").append(TboACCT_LOCK.tablename).append("(");
		sql.append(" LOCKTODATE,");
		sql.append(" UPDBY,");
		sql.append(" UPDDT,");
		sql.append(" COMP_CDE)");
		sql.append(" VALUES(?,?,?,?)");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmUpdate(FDbc dbc, String fixWhere) throws SQLException, Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ").append(TboACCT_LOCK.tablename).append(" SET ");
		sql.append(" LOCKTODATE=?,");
		sql.append(" UPDBY=?,");
		sql.append(" UPDDT=?");
		sql.append(" WHERE COMP_CDE=? ");
		if (!Fnc.isEmpty(fixWhere)) {
			sql.append(" AND " + fixWhere);
		}
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ").append(TboACCT_LOCK.tablename);
		sql.append(" WHERE COMP_CDE=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ").append(TboACCT_LOCK.tablename);
		sql.append(" WHERE COMP_CDE=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static void setPrepareStm(PreparedStatement pstm, TboACCT_LOCK model) throws SQLException, Exception {
		pstm.clearParameters();
		pstm.setDate(1, model.getLOCKTODATE());
		pstm.setString(2, model.getUPDBY());
		pstm.setTimestamp(3, model.getUPDDT());
		pstm.setString(4, model.getCOMP_CDE());
	}

	public static void setModel(ResultSet rs, TboACCT_LOCK model) throws SQLException, Exception {
		model.setLOCKTODATE(rs.getDate("LOCKTODATE"));
		model.setUPDBY(rs.getString("UPDBY"));
		model.setUPDDT(rs.getTimestamp("UPDDT"));
		model.setCOMP_CDE(rs.getString("COMP_CDE"));
	}

}
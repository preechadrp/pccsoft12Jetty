package com.pcc.sys.tbf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.tbo.TboFRUNNING;

public class TbfFRUNNING {
	public static boolean insert(TboFRUNNING model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return insert(dbc, model);
		}
	}

	public static boolean insert(FDbc dbc, TboFRUNNING model) throws SQLException, Exception {
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

	public static boolean update(TboFRUNNING model, String fixWhere) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model, fixWhere);
		}
	}

	public static boolean update(FDbc dbc, TboFRUNNING model, String fixWhere) throws SQLException, Exception {
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

	public static boolean delete(TboFRUNNING model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return delete(dbc, model);
		}
	}

	public static boolean delete(FDbc dbc, TboFRUNNING model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmDelete(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setString(2, model.getRUNNING_ID());
			pstm.setString(3, model.getRUNNING_PREFIX());
			int del = pstm.executeUpdate();
			if (del >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean find(TboFRUNNING model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return find(dbc, model);
		}
	}

	public static boolean find(FDbc dbc, TboFRUNNING model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmFind(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setString(2, model.getRUNNING_ID());
			pstm.setString(3, model.getRUNNING_PREFIX());
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
		sql.append(" INSERT INTO ").append(TboFRUNNING.tablename).append("(");
		sql.append(" RUNNING_NO,");
		sql.append(" COMP_CDE,");
		sql.append(" RUNNING_ID,");
		sql.append(" RUNNING_PREFIX)");
		sql.append(" VALUES(?,?,?,?)");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmUpdate(FDbc dbc, String fixWhere) throws SQLException, Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ").append(TboFRUNNING.tablename).append(" SET ");
		sql.append(" RUNNING_NO=?");
		sql.append(" WHERE COMP_CDE=? AND RUNNING_ID=? AND RUNNING_PREFIX=? ");
		if (!Fnc.isEmpty(fixWhere)) {
			sql.append(" AND " + fixWhere);
		}
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ").append(TboFRUNNING.tablename);
		sql.append(" WHERE COMP_CDE=? AND RUNNING_ID=? AND RUNNING_PREFIX=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ").append(TboFRUNNING.tablename);
		sql.append(" WHERE COMP_CDE=? AND RUNNING_ID=? AND RUNNING_PREFIX=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static void setPrepareStm(PreparedStatement pstm, TboFRUNNING model) throws SQLException, Exception {
		pstm.clearParameters();
		pstm.setInt(1, model.getRUNNING_NO());
		pstm.setString(2, model.getCOMP_CDE());
		pstm.setString(3, model.getRUNNING_ID());
		pstm.setString(4, model.getRUNNING_PREFIX());
	}

	public static void setModel(ResultSet rs, TboFRUNNING model) throws SQLException, Exception {
		model.setRUNNING_NO(rs.getInt("RUNNING_NO"));
		model.setCOMP_CDE(rs.getString("COMP_CDE"));
		model.setRUNNING_ID(rs.getString("RUNNING_ID"));
		model.setRUNNING_PREFIX(rs.getString("RUNNING_PREFIX"));
	}
}
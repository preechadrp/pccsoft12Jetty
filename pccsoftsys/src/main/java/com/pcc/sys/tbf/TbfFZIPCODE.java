package com.pcc.sys.tbf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.tbo.TboFZIPCODE;

public class TbfFZIPCODE {
	public static boolean insert(TboFZIPCODE model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return insert(dbc, model);
		}
	}

	public static boolean insert(FDbc dbc, TboFZIPCODE model) throws SQLException, Exception {
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

	public static boolean update(TboFZIPCODE model, String fixWhere) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model, fixWhere);
		}
	}

	public static boolean update(FDbc dbc, TboFZIPCODE model, String fixWhere) throws SQLException, Exception {
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

	public static boolean delete(TboFZIPCODE model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return delete(dbc, model);
		}
	}

	public static boolean delete(FDbc dbc, TboFZIPCODE model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmDelete(dbc);) {
			pstm.setString(1, model.getPROVIN_ID());
			pstm.setString(2, model.getZIPCODE());
			int del = pstm.executeUpdate();
			if (del >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean find(TboFZIPCODE model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return find(dbc, model);
		}
	}

	public static boolean find(FDbc dbc, TboFZIPCODE model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmFind(dbc);) {
			pstm.setString(1, model.getPROVIN_ID());
			pstm.setString(2, model.getZIPCODE());
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
		sql.append(" INSERT INTO ").append(TboFZIPCODE.tablename).append("(");
		sql.append(" DISTRICT_NAME,");
		sql.append(" PROVIN_ID,");
		sql.append(" ZIPCODE)");
		sql.append(" VALUES(?,?,?)");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmUpdate(FDbc dbc, String fixWhere) throws SQLException, Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ").append(TboFZIPCODE.tablename).append(" SET ");
		sql.append(" DISTRICT_NAME=?");
		sql.append(" WHERE PROVIN_ID=? AND ZIPCODE=? ");
		if (!Fnc.isEmpty(fixWhere)) {
			sql.append(" AND " + fixWhere);
		}
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ").append(TboFZIPCODE.tablename);
		sql.append(" WHERE PROVIN_ID=? AND ZIPCODE=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ").append(TboFZIPCODE.tablename);
		sql.append(" WHERE PROVIN_ID=? AND ZIPCODE=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static void setPrepareStm(PreparedStatement pstm, TboFZIPCODE model) throws SQLException, Exception {
		pstm.clearParameters();
		pstm.setString(1, model.getDISTRICT_NAME());
		pstm.setString(2, model.getPROVIN_ID());
		pstm.setString(3, model.getZIPCODE());
	}

	public static void setModel(ResultSet rs, TboFZIPCODE model) throws SQLException, Exception {
		model.setDISTRICT_NAME(rs.getString("DISTRICT_NAME"));
		model.setPROVIN_ID(rs.getString("PROVIN_ID"));
		model.setZIPCODE(rs.getString("ZIPCODE"));
	}
}
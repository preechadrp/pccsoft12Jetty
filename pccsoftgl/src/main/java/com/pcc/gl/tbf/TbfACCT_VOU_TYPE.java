package com.pcc.gl.tbf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pcc.gl.tbo.TboACCT_VOU_TYPE;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;

public class TbfACCT_VOU_TYPE {
	public static boolean insert(TboACCT_VOU_TYPE model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return insert(dbc, model);
		}
	}

	public static boolean insert(FDbc dbc, TboACCT_VOU_TYPE model) throws SQLException, Exception {
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

	public static boolean update(TboACCT_VOU_TYPE model, String fixWhere) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model, fixWhere);
		}
	}

	public static boolean update(FDbc dbc, TboACCT_VOU_TYPE model, String fixWhere) throws SQLException, Exception {
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

	public static boolean delete(TboACCT_VOU_TYPE model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return delete(dbc, model);
		}
	}

	public static boolean delete(FDbc dbc, TboACCT_VOU_TYPE model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmDelete(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setString(2, model.getVOU_TYPE());
			int del = pstm.executeUpdate();
			if (del >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean find(TboACCT_VOU_TYPE model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return find(dbc, model);
		}
	}

	public static boolean find(FDbc dbc, TboACCT_VOU_TYPE model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmFind(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setString(2, model.getVOU_TYPE());
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
		sql.append(" INSERT INTO ").append(TboACCT_VOU_TYPE.tablename).append("(");
		sql.append(" VOU_NAME,");
		sql.append(" USE_PV,");
		sql.append(" USE_RV,");
		sql.append(" UPBY,");
		sql.append(" UPDT,");
		sql.append(" COMP_CDE,");
		sql.append(" VOU_TYPE)");
		sql.append(" VALUES(?,?,?,?,?,?,?)");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmUpdate(FDbc dbc, String fixWhere) throws SQLException, Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ").append(TboACCT_VOU_TYPE.tablename).append(" SET ");
		sql.append(" VOU_NAME=?,");
		sql.append(" USE_PV=?,");
		sql.append(" USE_RV=?,");
		sql.append(" UPBY=?,");
		sql.append(" UPDT=?");
		sql.append(" WHERE COMP_CDE=? AND VOU_TYPE=? ");
		if (!Fnc.isEmpty(fixWhere)) {
			sql.append(" AND " + fixWhere);
		}
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ").append(TboACCT_VOU_TYPE.tablename);
		sql.append(" WHERE COMP_CDE=? AND VOU_TYPE=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ").append(TboACCT_VOU_TYPE.tablename);
		sql.append(" WHERE COMP_CDE=? AND VOU_TYPE=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static void setPrepareStm(PreparedStatement pstm, TboACCT_VOU_TYPE model) throws SQLException, Exception {
		pstm.clearParameters();
		pstm.setString(1, model.getVOU_NAME());
		pstm.setInt(2, model.getUSE_PV());
		pstm.setInt(3, model.getUSE_RV());
		pstm.setString(4, model.getUPBY());
		pstm.setTimestamp(5, model.getUPDT());
		pstm.setString(6, model.getCOMP_CDE());
		pstm.setString(7, model.getVOU_TYPE());
	}

	public static void setModel(ResultSet rs, TboACCT_VOU_TYPE model) throws SQLException, Exception {
		model.setVOU_NAME(rs.getString("VOU_NAME"));
		model.setUSE_PV(rs.getInt("USE_PV"));
		model.setUSE_RV(rs.getInt("USE_RV"));
		model.setUPBY(rs.getString("UPBY"));
		model.setUPDT(rs.getTimestamp("UPDT"));
		model.setCOMP_CDE(rs.getString("COMP_CDE"));
		model.setVOU_TYPE(rs.getString("VOU_TYPE"));
	}
}
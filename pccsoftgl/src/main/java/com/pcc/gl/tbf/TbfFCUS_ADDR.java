package com.pcc.gl.tbf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pcc.gl.tbo.TboFCUS_ADDR;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;

public class TbfFCUS_ADDR {
	public static boolean insert(TboFCUS_ADDR model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return insert(dbc, model);
		}
	}

	public static boolean insert(FDbc dbc, TboFCUS_ADDR model) throws SQLException, Exception {
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

	public static boolean update(TboFCUS_ADDR model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model);
		}
	}

	public static boolean update(FDbc dbc, TboFCUS_ADDR model) throws SQLException, Exception {
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

	public static boolean update(TboFCUS_ADDR model, String fixWhere) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model, fixWhere);
		}
	}

	public static boolean update(FDbc dbc, TboFCUS_ADDR model, String fixWhere) throws SQLException, Exception {
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

	public static boolean delete(TboFCUS_ADDR model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return delete(dbc, model);
		}
	}

	public static boolean delete(FDbc dbc, TboFCUS_ADDR model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmDelete(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setString(2, model.getCUST_CDE());
			pstm.setString(3, model.getADDR_TYP());
			int del = pstm.executeUpdate();
			if (del >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean find(TboFCUS_ADDR model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return find(dbc, model);
		}
	}

	public static boolean find(FDbc dbc, TboFCUS_ADDR model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmFind(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setString(2, model.getCUST_CDE());
			pstm.setString(3, model.getADDR_TYP());
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
		sql.append(" INSERT INTO ").append(TboFCUS_ADDR.tablename).append("(");
		sql.append(" VILAPT1,");
		sql.append(" ADDRESS1,");
		sql.append(" FLOOR1,");
		sql.append(" ROOM1,");
		sql.append(" MOO1,");
		sql.append(" SOI1,");
		sql.append(" ROAD1,");
		sql.append(" TAMBOLNAME,");
		sql.append(" AMPHURID,");
		sql.append(" AMPHURNAME,");
		sql.append(" PROVIN_ID,");
		sql.append(" PROVIN_NAME,");
		sql.append(" ZIPCODE,");
		sql.append(" CUST_REMARK,");
		sql.append(" COMP_CDE,");
		sql.append(" CUST_CDE,");
		sql.append(" ADDR_TYP)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmUpdate(FDbc dbc, String fixWhere) throws SQLException, Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ").append(TboFCUS_ADDR.tablename).append(" SET ");
		sql.append(" VILAPT1=?,");
		sql.append(" ADDRESS1=?,");
		sql.append(" FLOOR1=?,");
		sql.append(" ROOM1=?,");
		sql.append(" MOO1=?,");
		sql.append(" SOI1=?,");
		sql.append(" ROAD1=?,");
		sql.append(" TAMBOLNAME=?,");
		sql.append(" AMPHURID=?,");
		sql.append(" AMPHURNAME=?,");
		sql.append(" PROVIN_ID=?,");
		sql.append(" PROVIN_NAME=?,");
		sql.append(" ZIPCODE=?,");
		sql.append(" CUST_REMARK=?");
		sql.append(" WHERE COMP_CDE=? AND CUST_CDE=? AND ADDR_TYP=? ");
		if (!Fnc.isEmpty(fixWhere)) {
			sql.append(" AND " + fixWhere);
		}
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ").append(TboFCUS_ADDR.tablename);
		sql.append(" WHERE COMP_CDE=? AND CUST_CDE=? AND ADDR_TYP=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ").append(TboFCUS_ADDR.tablename);
		sql.append(" WHERE COMP_CDE=? AND CUST_CDE=? AND ADDR_TYP=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static void setPrepareStm(PreparedStatement pstm, TboFCUS_ADDR model) throws SQLException, Exception {
		pstm.clearParameters();
		pstm.setString(1, model.getVILAPT1());
		pstm.setString(2, model.getADDRESS1());
		pstm.setString(3, model.getFLOOR1());
		pstm.setString(4, model.getROOM1());
		pstm.setString(5, model.getMOO1());
		pstm.setString(6, model.getSOI1());
		pstm.setString(7, model.getROAD1());
		pstm.setString(8, model.getTAMBOLNAME());
		pstm.setInt(9, model.getAMPHURID());
		pstm.setString(10, model.getAMPHURNAME());
		pstm.setInt(11, model.getPROVIN_ID());
		pstm.setString(12, model.getPROVIN_NAME());
		pstm.setString(13, model.getZIPCODE());
		pstm.setString(14, model.getCUST_REMARK());
		pstm.setString(15, model.getCOMP_CDE());
		pstm.setString(16, model.getCUST_CDE());
		pstm.setString(17, model.getADDR_TYP());
	}

	public static void setModel(ResultSet rs, TboFCUS_ADDR model) throws SQLException, Exception {
		model.setVILAPT1(rs.getString("VILAPT1"));
		model.setADDRESS1(rs.getString("ADDRESS1"));
		model.setFLOOR1(rs.getString("FLOOR1"));
		model.setROOM1(rs.getString("ROOM1"));
		model.setMOO1(rs.getString("MOO1"));
		model.setSOI1(rs.getString("SOI1"));
		model.setROAD1(rs.getString("ROAD1"));
		model.setTAMBOLNAME(rs.getString("TAMBOLNAME"));
		model.setAMPHURID(rs.getInt("AMPHURID"));
		model.setAMPHURNAME(rs.getString("AMPHURNAME"));
		model.setPROVIN_ID(rs.getInt("PROVIN_ID"));
		model.setPROVIN_NAME(rs.getString("PROVIN_NAME"));
		model.setZIPCODE(rs.getString("ZIPCODE"));
		model.setCUST_REMARK(rs.getString("CUST_REMARK"));
		model.setCOMP_CDE(rs.getString("COMP_CDE"));
		model.setCUST_CDE(rs.getString("CUST_CDE"));
		model.setADDR_TYP(rs.getString("ADDR_TYP"));
	}
}
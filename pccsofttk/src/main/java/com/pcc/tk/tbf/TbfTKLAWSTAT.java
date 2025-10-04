package com.pcc.tk.tbf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;
import com.pcc.tk.tbo.TboTKLAWSTAT;

public class TbfTKLAWSTAT {
	public static boolean insert(TboTKLAWSTAT model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return insert(dbc, model);
		}
	}

	public static boolean insert(FDbc dbc, TboTKLAWSTAT model) throws SQLException, Exception {
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

	public static boolean update(TboTKLAWSTAT model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model);
		}
	}

	public static boolean update(FDbc dbc, TboTKLAWSTAT model) throws SQLException, Exception {
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

	public static boolean update(TboTKLAWSTAT model, String fixWhere) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model, fixWhere);
		}
	}

	public static boolean update(FDbc dbc, TboTKLAWSTAT model, String fixWhere) throws SQLException, Exception {
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

	public static boolean delete(TboTKLAWSTAT model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return delete(dbc, model);
		}
	}

	public static boolean delete(FDbc dbc, TboTKLAWSTAT model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmDelete(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setInt(2, model.getLAWSTATID());
			int del = pstm.executeUpdate();
			if (del >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean find(TboTKLAWSTAT model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return find(dbc, model);
		}
	}

	public static boolean find(FDbc dbc, TboTKLAWSTAT model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmFind(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setInt(2, model.getLAWSTATID());
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
		sql.append(" INSERT INTO ").append(TboTKLAWSTAT.tablename).append("(");
		sql.append(" LAWSTATNAME,");
		sql.append(" UPDBY,");
		sql.append(" UPDDTE,");
		sql.append(" COMP_CDE,");
		sql.append(" LAWSTATID)");
		sql.append(" VALUES(?,?,?,?,?)");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmUpdate(FDbc dbc, String fixWhere) throws SQLException, Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ").append(TboTKLAWSTAT.tablename).append(" SET ");
		sql.append(" LAWSTATNAME=?,");
		sql.append(" UPDBY=?,");
		sql.append(" UPDDTE=?");
		sql.append(" WHERE COMP_CDE=? AND LAWSTATID=? ");
		if (!Fnc.isEmpty(fixWhere)) {
			sql.append(" AND " + fixWhere);
		}
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ").append(TboTKLAWSTAT.tablename);
		sql.append(" WHERE COMP_CDE=? AND LAWSTATID=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ").append(TboTKLAWSTAT.tablename);
		sql.append(" WHERE COMP_CDE=? AND LAWSTATID=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static void setPrepareStm(PreparedStatement pstm, TboTKLAWSTAT model) throws SQLException, Exception {
		pstm.clearParameters();
		pstm.setString(1, model.getLAWSTATNAME());
		pstm.setString(2, model.getUPDBY());
		pstm.setTimestamp(3, model.getUPDDTE());
		pstm.setString(4, model.getCOMP_CDE());
		pstm.setInt(5, model.getLAWSTATID());
	}

	public static void setModel(ResultSet rs, TboTKLAWSTAT model) throws SQLException, Exception {
		model.setLAWSTATNAME(rs.getString("LAWSTATNAME"));
		model.setUPDBY(rs.getString("UPDBY"));
		model.setUPDDTE(rs.getTimestamp("UPDDTE"));
		model.setCOMP_CDE(rs.getString("COMP_CDE"));
		model.setLAWSTATID(rs.getInt("LAWSTATID"));
	}
}
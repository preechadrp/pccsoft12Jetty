package com.pcc.tk.tbf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;
import com.pcc.tk.tbo.TboTKCOURT;

public class TbfTKCOURT {
	public static boolean insert(TboTKCOURT model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return insert(dbc, model);
		}
	}

	public static boolean insert(FDbc dbc, TboTKCOURT model) throws SQLException, Exception {
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

	public static boolean update(TboTKCOURT model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model);
		}
	}

	public static boolean update(FDbc dbc, TboTKCOURT model) throws SQLException, Exception {
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

	public static boolean update(TboTKCOURT model, String fixWhere) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model, fixWhere);
		}
	}

	public static boolean update(FDbc dbc, TboTKCOURT model, String fixWhere) throws SQLException, Exception {
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

	public static boolean delete(TboTKCOURT model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return delete(dbc, model);
		}
	}

	public static boolean delete(FDbc dbc, TboTKCOURT model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmDelete(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setInt(2, model.getCOURTID());
			int del = pstm.executeUpdate();
			if (del >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean find(TboTKCOURT model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return find(dbc, model);
		}
	}

	public static boolean find(FDbc dbc, TboTKCOURT model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmFind(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setInt(2, model.getCOURTID());
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
		sql.append(" INSERT INTO ").append(TboTKCOURT.tablename).append("(");
		sql.append(" COURTNAME,");
		sql.append(" UPDBY,");
		sql.append(" UPDDTE,");
		sql.append(" COMP_CDE,");
		sql.append(" COURTID)");
		sql.append(" VALUES(?,?,?,?,?)");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmUpdate(FDbc dbc, String fixWhere) throws SQLException, Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ").append(TboTKCOURT.tablename).append(" SET ");
		sql.append(" COURTNAME=?,");
		sql.append(" UPDBY=?,");
		sql.append(" UPDDTE=?");
		sql.append(" WHERE COMP_CDE=? AND COURTID=? ");
		if (!Fnc.isEmpty(fixWhere)) {
			sql.append(" AND " + fixWhere);
		}
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ").append(TboTKCOURT.tablename);
		sql.append(" WHERE COMP_CDE=? AND COURTID=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ").append(TboTKCOURT.tablename);
		sql.append(" WHERE COMP_CDE=? AND COURTID=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static void setPrepareStm(PreparedStatement pstm, TboTKCOURT model) throws SQLException, Exception {
		pstm.clearParameters();
		pstm.setString(1, model.getCOURTNAME());
		pstm.setString(2, model.getUPDBY());
		pstm.setTimestamp(3, model.getUPDDTE());
		pstm.setString(4, model.getCOMP_CDE());
		pstm.setInt(5, model.getCOURTID());
	}

	public static void setModel(ResultSet rs, TboTKCOURT model) throws SQLException, Exception {
		model.setCOURTNAME(rs.getString("COURTNAME"));
		model.setUPDBY(rs.getString("UPDBY"));
		model.setUPDDTE(rs.getTimestamp("UPDDTE"));
		model.setCOMP_CDE(rs.getString("COMP_CDE"));
		model.setCOURTID(rs.getInt("COURTID"));
	}
}
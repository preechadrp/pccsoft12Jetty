package com.pcc.tk.tbf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;
import com.pcc.tk.tbo.TboTKJOBDOCS;

public class TbfTKJOBDOCS {
	public static boolean insert(TboTKJOBDOCS model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return insert(dbc, model);
		}
	}

	public static boolean insert(FDbc dbc, TboTKJOBDOCS model) throws SQLException, Exception {
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

	public static boolean update(TboTKJOBDOCS model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model);
		}
	}

	public static boolean update(FDbc dbc, TboTKJOBDOCS model) throws SQLException, Exception {
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

	public static boolean update(TboTKJOBDOCS model, String fixWhere) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model, fixWhere);
		}
	}

	public static boolean update(FDbc dbc, TboTKJOBDOCS model, String fixWhere) throws SQLException, Exception {
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

	public static boolean delete(TboTKJOBDOCS model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return delete(dbc, model);
		}
	}

	public static boolean delete(FDbc dbc, TboTKJOBDOCS model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmDelete(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setString(2, model.getJOBNO());
			pstm.setInt(3, model.getDOCSEQ());
			int del = pstm.executeUpdate();
			if (del >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean find(TboTKJOBDOCS model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return find(dbc, model);
		}
	}

	public static boolean find(FDbc dbc, TboTKJOBDOCS model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmFind(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setString(2, model.getJOBNO());
			pstm.setInt(3, model.getDOCSEQ());
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
		sql.append(" INSERT INTO ").append(TboTKJOBDOCS.tablename).append("(");
		sql.append(" DOCDESC,");
		sql.append(" IMGSEQ,");
		sql.append(" IMGTYPE,");
		sql.append(" UPDBY,");
		sql.append(" UPDDTE,");
		sql.append(" COMP_CDE,");
		sql.append(" JOBNO,");
		sql.append(" DOCSEQ)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?)");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmUpdate(FDbc dbc, String fixWhere) throws SQLException, Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ").append(TboTKJOBDOCS.tablename).append(" SET ");
		sql.append(" DOCDESC=?,");
		sql.append(" IMGSEQ=?,");
		sql.append(" IMGTYPE=?,");
		sql.append(" UPDBY=?,");
		sql.append(" UPDDTE=?");
		sql.append(" WHERE COMP_CDE=? AND JOBNO=? AND DOCSEQ=? ");
		if (!Fnc.isEmpty(fixWhere)) {
			sql.append(" AND " + fixWhere);
		}
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ").append(TboTKJOBDOCS.tablename);
		sql.append(" WHERE COMP_CDE=? AND JOBNO=? AND DOCSEQ=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ").append(TboTKJOBDOCS.tablename);
		sql.append(" WHERE COMP_CDE=? AND JOBNO=? AND DOCSEQ=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static void setPrepareStm(PreparedStatement pstm, TboTKJOBDOCS model) throws SQLException, Exception {
		pstm.clearParameters();
		pstm.setString(1, model.getDOCDESC());
		pstm.setInt(2, model.getIMGSEQ());
		pstm.setString(3, model.getIMGTYPE());
		pstm.setString(4, model.getUPDBY());
		pstm.setTimestamp(5, model.getUPDDTE());
		pstm.setString(6, model.getCOMP_CDE());
		pstm.setString(7, model.getJOBNO());
		pstm.setInt(8, model.getDOCSEQ());
	}

	public static void setModel(ResultSet rs, TboTKJOBDOCS model) throws SQLException, Exception {
		model.setDOCDESC(rs.getString("DOCDESC"));
		model.setIMGSEQ(rs.getInt("IMGSEQ"));
		model.setIMGTYPE(rs.getString("IMGTYPE"));
		model.setUPDBY(rs.getString("UPDBY"));
		model.setUPDDTE(rs.getTimestamp("UPDDTE"));
		model.setCOMP_CDE(rs.getString("COMP_CDE"));
		model.setJOBNO(rs.getString("JOBNO"));
		model.setDOCSEQ(rs.getInt("DOCSEQ"));
	}
}
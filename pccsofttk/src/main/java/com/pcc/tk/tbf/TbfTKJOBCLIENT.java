package com.pcc.tk.tbf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;
import com.pcc.tk.tbo.TboTKJOBCLIENT;

public class TbfTKJOBCLIENT {
	public static boolean insert(TboTKJOBCLIENT model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return insert(dbc, model);
		}
	}

	public static boolean insert(FDbc dbc, TboTKJOBCLIENT model) throws SQLException, Exception {
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

	public static boolean update(TboTKJOBCLIENT model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model);
		}
	}

	public static boolean update(FDbc dbc, TboTKJOBCLIENT model) throws SQLException, Exception {
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

	public static boolean update(TboTKJOBCLIENT model, String fixWhere) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model, fixWhere);
		}
	}

	public static boolean update(FDbc dbc, TboTKJOBCLIENT model, String fixWhere) throws SQLException, Exception {
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

	public static boolean delete(TboTKJOBCLIENT model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return delete(dbc, model);
		}
	}

	public static boolean delete(FDbc dbc, TboTKJOBCLIENT model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmDelete(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setString(2, model.getJOBNO());
			pstm.setInt(3, model.getSEQ());
			int del = pstm.executeUpdate();
			if (del >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean find(TboTKJOBCLIENT model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return find(dbc, model);
		}
	}

	public static boolean find(FDbc dbc, TboTKJOBCLIENT model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmFind(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setString(2, model.getJOBNO());
			pstm.setInt(3, model.getSEQ());
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
		sql.append(" INSERT INTO ").append(TboTKJOBCLIENT.tablename).append("(");
		sql.append(" CLIENTTITLE,");
		sql.append(" CLIENTFNAME,");
		sql.append(" CLIENTLNAME,");
		sql.append(" TELNO,");
		sql.append(" CLIENTSEQ,");
		sql.append(" UPDBY,");
		sql.append(" UPDDTE,");
		sql.append(" COMP_CDE,");
		sql.append(" JOBNO,");
		sql.append(" SEQ)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?)");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmUpdate(FDbc dbc, String fixWhere) throws SQLException, Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ").append(TboTKJOBCLIENT.tablename).append(" SET ");
		sql.append(" CLIENTTITLE=?,");
		sql.append(" CLIENTFNAME=?,");
		sql.append(" CLIENTLNAME=?,");
		sql.append(" TELNO=?,");
		sql.append(" CLIENTSEQ=?,");
		sql.append(" UPDBY=?,");
		sql.append(" UPDDTE=?");
		sql.append(" WHERE COMP_CDE=? AND JOBNO=? AND SEQ=? ");
		if (!Fnc.isEmpty(fixWhere)) {
			sql.append(" AND " + fixWhere);
		}
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ").append(TboTKJOBCLIENT.tablename);
		sql.append(" WHERE COMP_CDE=? AND JOBNO=? AND SEQ=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ").append(TboTKJOBCLIENT.tablename);
		sql.append(" WHERE COMP_CDE=? AND JOBNO=? AND SEQ=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static void setPrepareStm(PreparedStatement pstm, TboTKJOBCLIENT model) throws SQLException, Exception {
		pstm.clearParameters();
		pstm.setString(1, model.getCLIENTTITLE());
		pstm.setString(2, model.getCLIENTFNAME());
		pstm.setString(3, model.getCLIENTLNAME());
		pstm.setString(4, model.getTELNO());
		pstm.setInt(5, model.getCLIENTSEQ());
		pstm.setString(6, model.getUPDBY());
		pstm.setTimestamp(7, model.getUPDDTE());
		pstm.setString(8, model.getCOMP_CDE());
		pstm.setString(9, model.getJOBNO());
		pstm.setInt(10, model.getSEQ());
	}

	public static void setModel(ResultSet rs, TboTKJOBCLIENT model) throws SQLException, Exception {
		model.setCLIENTTITLE(rs.getString("CLIENTTITLE"));
		model.setCLIENTFNAME(rs.getString("CLIENTFNAME"));
		model.setCLIENTLNAME(rs.getString("CLIENTLNAME"));
		model.setTELNO(rs.getString("TELNO"));
		model.setCLIENTSEQ(rs.getInt("CLIENTSEQ"));
		model.setUPDBY(rs.getString("UPDBY"));
		model.setUPDDTE(rs.getTimestamp("UPDDTE"));
		model.setCOMP_CDE(rs.getString("COMP_CDE"));
		model.setJOBNO(rs.getString("JOBNO"));
		model.setSEQ(rs.getInt("SEQ"));
	}
}
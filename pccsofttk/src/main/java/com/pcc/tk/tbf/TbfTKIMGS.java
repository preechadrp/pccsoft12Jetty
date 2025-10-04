package com.pcc.tk.tbf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;
import com.pcc.tk.tbo.TboTKIMGS;

public class TbfTKIMGS {
	public static boolean insert(TboTKIMGS model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return insert(dbc, model);
		}
	}

	public static boolean insert(FDbc dbc, TboTKIMGS model) throws SQLException, Exception {
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

	public static boolean update(TboTKIMGS model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model);
		}
	}

	public static boolean update(FDbc dbc, TboTKIMGS model) throws SQLException, Exception {
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

	public static boolean update(TboTKIMGS model, String fixWhere) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model, fixWhere);
		}
	}

	public static boolean update(FDbc dbc, TboTKIMGS model, String fixWhere) throws SQLException, Exception {
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

	public static boolean delete(TboTKIMGS model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return delete(dbc, model);
		}
	}

	public static boolean delete(FDbc dbc, TboTKIMGS model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmDelete(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setString(2, model.getSYS_CDE());
			pstm.setInt(3, model.getIMGSEQ());
			int del = pstm.executeUpdate();
			if (del >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean find(TboTKIMGS model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return find(dbc, model);
		}
	}

	public static boolean find(FDbc dbc, TboTKIMGS model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmFind(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setString(2, model.getSYS_CDE());
			pstm.setInt(3, model.getIMGSEQ());
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
		sql.append(" INSERT INTO ").append(TboTKIMGS.tablename).append("(");
		sql.append(" IMGDESC,");
		sql.append(" IMGTYPE,");
		sql.append(" IMGDATA,");
		sql.append(" COMP_CDE,");
		sql.append(" SYS_CDE,");
		sql.append(" IMGSEQ)");
		sql.append(" VALUES(?,?,?,?,?,?)");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmUpdate(FDbc dbc, String fixWhere) throws SQLException, Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ").append(TboTKIMGS.tablename).append(" SET ");
		sql.append(" IMGDESC=?,");
		sql.append(" IMGTYPE=?,");
		sql.append(" IMGDATA=?");
		sql.append(" WHERE COMP_CDE=? AND SYS_CDE=? AND IMGSEQ=? ");
		if (!Fnc.isEmpty(fixWhere)) {
			sql.append(" AND " + fixWhere);
		}
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ").append(TboTKIMGS.tablename);
		sql.append(" WHERE COMP_CDE=? AND SYS_CDE=? AND IMGSEQ=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ").append(TboTKIMGS.tablename);
		sql.append(" WHERE COMP_CDE=? AND SYS_CDE=? AND IMGSEQ=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static void setPrepareStm(PreparedStatement pstm, TboTKIMGS model) throws SQLException, Exception {
		pstm.clearParameters();
		pstm.setString(1, model.getIMGDESC());
		pstm.setString(2, model.getIMGTYPE());
		pstm.setBytes(3, model.getIMGDATA());
		pstm.setString(4, model.getCOMP_CDE());
		pstm.setString(5, model.getSYS_CDE());
		pstm.setInt(6, model.getIMGSEQ());
	}

	public static void setModel(ResultSet rs, TboTKIMGS model) throws SQLException, Exception {
		model.setIMGDESC(rs.getString("IMGDESC"));
		model.setIMGTYPE(rs.getString("IMGTYPE"));
		model.setIMGDATA(rs.getBytes("IMGDATA"));
		model.setCOMP_CDE(rs.getString("COMP_CDE"));
		model.setSYS_CDE(rs.getString("SYS_CDE"));
		model.setIMGSEQ(rs.getInt("IMGSEQ"));
	}
}
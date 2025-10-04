package com.pcc.gl.tbf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pcc.gl.tbo.TboACGL_HEADER;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;

public class TbfACGL_HEADER {
	public static boolean insert(TboACGL_HEADER model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return insert(dbc, model);
		}
	}

	public static boolean insert(FDbc dbc, TboACGL_HEADER model) throws SQLException, Exception {
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

	public static boolean update(TboACGL_HEADER model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model);
		}
	}

	public static boolean update(FDbc dbc, TboACGL_HEADER model) throws SQLException, Exception {
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

	public static boolean update(TboACGL_HEADER model, String fixWhere) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model, fixWhere);
		}
	}

	public static boolean update(FDbc dbc, TboACGL_HEADER model, String fixWhere) throws SQLException, Exception {
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

	public static boolean delete(TboACGL_HEADER model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return delete(dbc, model);
		}
	}

	public static boolean delete(FDbc dbc, TboACGL_HEADER model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmDelete(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setString(2, model.getVOU_TYPE());
			pstm.setString(3, model.getVOU_NO());
			int del = pstm.executeUpdate();
			if (del >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean find(TboACGL_HEADER model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return find(dbc, model);
		}
	}

	public static boolean find(FDbc dbc, TboACGL_HEADER model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmFind(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setString(2, model.getVOU_TYPE());
			pstm.setString(3, model.getVOU_NO());
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
		sql.append(" INSERT INTO ").append(TboACGL_HEADER.tablename).append("(");
		sql.append(" POSTDATE,");
		sql.append(" DESCR,");
		sql.append(" POST_APP,");
		sql.append(" RECSTA,");
		sql.append(" UPBY,");
		sql.append(" UPDT,");
		sql.append(" APPR_BY,");
		sql.append(" APPR_DT,");
		sql.append(" COMP_CDE,");
		sql.append(" VOU_TYPE,");
		sql.append(" VOU_NO)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?)");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmUpdate(FDbc dbc, String fixWhere) throws SQLException, Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ").append(TboACGL_HEADER.tablename).append(" SET ");
		sql.append(" POSTDATE=?,");
		sql.append(" DESCR=?,");
		sql.append(" POST_APP=?,");
		sql.append(" RECSTA=?,");
		sql.append(" UPBY=?,");
		sql.append(" UPDT=?,");
		sql.append(" APPR_BY=?,");
		sql.append(" APPR_DT=?");
		sql.append(" WHERE COMP_CDE=? AND VOU_TYPE=? AND VOU_NO=? ");
		if (!Fnc.isEmpty(fixWhere)) {
			sql.append(" AND " + fixWhere);
		}
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ").append(TboACGL_HEADER.tablename);
		sql.append(" WHERE COMP_CDE=? AND VOU_TYPE=? AND VOU_NO=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ").append(TboACGL_HEADER.tablename);
		sql.append(" WHERE COMP_CDE=? AND VOU_TYPE=? AND VOU_NO=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static void setPrepareStm(PreparedStatement pstm, TboACGL_HEADER model) throws SQLException, Exception {
		pstm.clearParameters();
		pstm.setDate(1, model.getPOSTDATE());
		pstm.setString(2, model.getDESCR());
		pstm.setString(3, model.getPOST_APP());
		pstm.setInt(4, model.getRECSTA());
		pstm.setString(5, model.getUPBY());
		pstm.setTimestamp(6, model.getUPDT());
		pstm.setString(7, model.getAPPR_BY());
		pstm.setTimestamp(8, model.getAPPR_DT());
		pstm.setString(9, model.getCOMP_CDE());
		pstm.setString(10, model.getVOU_TYPE());
		pstm.setString(11, model.getVOU_NO());
	}

	public static void setModel(ResultSet rs, TboACGL_HEADER model) throws SQLException, Exception {
		model.setPOSTDATE(rs.getDate("POSTDATE"));
		model.setDESCR(rs.getString("DESCR"));
		model.setPOST_APP(rs.getString("POST_APP"));
		model.setRECSTA(rs.getInt("RECSTA"));
		model.setUPBY(rs.getString("UPBY"));
		model.setUPDT(rs.getTimestamp("UPDT"));
		model.setAPPR_BY(rs.getString("APPR_BY"));
		model.setAPPR_DT(rs.getTimestamp("APPR_DT"));
		model.setCOMP_CDE(rs.getString("COMP_CDE"));
		model.setVOU_TYPE(rs.getString("VOU_TYPE"));
		model.setVOU_NO(rs.getString("VOU_NO"));
	}
}
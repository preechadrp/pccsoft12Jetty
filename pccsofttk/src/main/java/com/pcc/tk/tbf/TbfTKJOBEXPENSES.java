package com.pcc.tk.tbf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;
import com.pcc.tk.tbo.TboTKJOBEXPENSES;

public class TbfTKJOBEXPENSES {
	public static boolean insert(TboTKJOBEXPENSES model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return insert(dbc, model);
		}
	}

	public static boolean insert(FDbc dbc, TboTKJOBEXPENSES model) throws SQLException, Exception {
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

	public static boolean update(TboTKJOBEXPENSES model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model);
		}
	}

	public static boolean update(FDbc dbc, TboTKJOBEXPENSES model) throws SQLException, Exception {
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

	public static boolean update(TboTKJOBEXPENSES model, String fixWhere) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model, fixWhere);
		}
	}

	public static boolean update(FDbc dbc, TboTKJOBEXPENSES model, String fixWhere) throws SQLException, Exception {
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

	public static boolean delete(TboTKJOBEXPENSES model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return delete(dbc, model);
		}
	}

	public static boolean delete(FDbc dbc, TboTKJOBEXPENSES model) throws SQLException, Exception {
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

	public static boolean find(TboTKJOBEXPENSES model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return find(dbc, model);
		}
	}

	public static boolean find(FDbc dbc, TboTKJOBEXPENSES model) throws SQLException, Exception {
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
		sql.append(" INSERT INTO ").append(TboTKJOBEXPENSES.tablename).append("(");
		sql.append(" EXPENSESID,");
		sql.append(" EXPENSESAMT,");
		sql.append(" WITHDRAWAL_AMT,");
		sql.append(" EXP_REMARK,");
		sql.append(" UPDBY,");
		sql.append(" UPDDTE,");
		sql.append(" EXPCOM_ADV,");
		sql.append(" COMP_CDE,");
		sql.append(" JOBNO,");
		sql.append(" SEQ)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?)");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmUpdate(FDbc dbc, String fixWhere) throws SQLException, Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ").append(TboTKJOBEXPENSES.tablename).append(" SET ");
		sql.append(" EXPENSESID=?,");
		sql.append(" EXPENSESAMT=?,");
		sql.append(" WITHDRAWAL_AMT=?,");
		sql.append(" EXP_REMARK=?,");
		sql.append(" UPDBY=?,");
		sql.append(" UPDDTE=?,");
		sql.append(" EXPCOM_ADV=?");
		sql.append(" WHERE COMP_CDE=? AND JOBNO=? AND SEQ=? ");
		if (!Fnc.isEmpty(fixWhere)) {
			sql.append(" AND " + fixWhere);
		}
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ").append(TboTKJOBEXPENSES.tablename);
		sql.append(" WHERE COMP_CDE=? AND JOBNO=? AND SEQ=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ").append(TboTKJOBEXPENSES.tablename);
		sql.append(" WHERE COMP_CDE=? AND JOBNO=? AND SEQ=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static void setPrepareStm(PreparedStatement pstm, TboTKJOBEXPENSES model) throws SQLException, Exception {
		pstm.clearParameters();
		pstm.setInt(1, model.getEXPENSESID());
		pstm.setBigDecimal(2, model.getEXPENSESAMT());
		pstm.setBigDecimal(3, model.getWITHDRAWAL_AMT());
		pstm.setString(4, model.getEXP_REMARK());
		pstm.setString(5, model.getUPDBY());
		pstm.setTimestamp(6, model.getUPDDTE());
		pstm.setBigDecimal(7, model.getEXPCOM_ADV());
		pstm.setString(8, model.getCOMP_CDE());
		pstm.setString(9, model.getJOBNO());
		pstm.setInt(10, model.getSEQ());
	}

	public static void setModel(ResultSet rs, TboTKJOBEXPENSES model) throws SQLException, Exception {
		model.setEXPENSESID(rs.getInt("EXPENSESID"));
		model.setEXPENSESAMT(rs.getBigDecimal("EXPENSESAMT"));
		model.setWITHDRAWAL_AMT(rs.getBigDecimal("WITHDRAWAL_AMT"));
		model.setEXP_REMARK(rs.getString("EXP_REMARK"));
		model.setUPDBY(rs.getString("UPDBY"));
		model.setUPDDTE(rs.getTimestamp("UPDDTE"));
		model.setEXPCOM_ADV(rs.getBigDecimal("EXPCOM_ADV"));
		model.setCOMP_CDE(rs.getString("COMP_CDE"));
		model.setJOBNO(rs.getString("JOBNO"));
		model.setSEQ(rs.getInt("SEQ"));
	}
}
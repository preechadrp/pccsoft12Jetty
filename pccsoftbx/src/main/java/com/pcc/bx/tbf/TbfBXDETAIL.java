package com.pcc.bx.tbf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pcc.bx.tbo.TboBXDETAIL;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;

public class TbfBXDETAIL {

	public static boolean insert(TboBXDETAIL model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return insert(dbc, model);
		}
	}

	public static boolean insert(FDbc dbc, TboBXDETAIL model) throws SQLException, Exception {
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

	public static boolean update(TboBXDETAIL model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model);
		}
	}

	public static boolean update(FDbc dbc, TboBXDETAIL model) throws SQLException, Exception {
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

	public static boolean update(TboBXDETAIL model, String fixWhere) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model, fixWhere);
		}
	}

	public static boolean update(FDbc dbc, TboBXDETAIL model, String fixWhere) throws SQLException, Exception {
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

	public static boolean delete(TboBXDETAIL model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return delete(dbc, model);
		}
	}

	public static boolean delete(FDbc dbc, TboBXDETAIL model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmDelete(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setString(2, model.getBLNO());
			pstm.setInt(3, model.getSEQ1());
			int del = pstm.executeUpdate();
			if (del >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean find(TboBXDETAIL model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return find(dbc, model);
		}
	}

	public static boolean find(FDbc dbc, TboBXDETAIL model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmFind(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setString(2, model.getBLNO());
			pstm.setInt(3, model.getSEQ1());
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
		sql.append(" INSERT INTO ").append(TboBXDETAIL.tablename).append("(");
		sql.append(" LINETYP,");
		sql.append(" PRODUCTID,");
		sql.append(" PRODUCTNAME,");
		sql.append(" REMARKLINE,");
		sql.append(" QTY,");
		sql.append(" PRICE,");
		sql.append(" SUMPRICE,");
		sql.append(" COMP_CDE,");
		sql.append(" BLNO,");
		sql.append(" SEQ1)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?)");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmUpdate(FDbc dbc, String fixWhere) throws SQLException, Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ").append(TboBXDETAIL.tablename).append(" SET ");
		sql.append(" LINETYP=?,");
		sql.append(" PRODUCTID=?,");
		sql.append(" PRODUCTNAME=?,");
		sql.append(" REMARKLINE=?,");
		sql.append(" QTY=?,");
		sql.append(" PRICE=?,");
		sql.append(" SUMPRICE=?");
		sql.append(" WHERE COMP_CDE=? AND BLNO=? AND SEQ1=? ");
		if (!Fnc.isEmpty(fixWhere)) {
			sql.append(" AND " + fixWhere);
		}
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ").append(TboBXDETAIL.tablename);
		sql.append(" WHERE COMP_CDE=? AND BLNO=? AND SEQ1=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ").append(TboBXDETAIL.tablename);
		sql.append(" WHERE COMP_CDE=? AND BLNO=? AND SEQ1=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static void setPrepareStm(PreparedStatement pstm, TboBXDETAIL model) throws SQLException, Exception {
		pstm.clearParameters();
		pstm.setString(1, model.getLINETYP());
		pstm.setString(2, model.getPRODUCTID());
		pstm.setString(3, model.getPRODUCTNAME());
		pstm.setString(4, model.getREMARKLINE());
		pstm.setBigDecimal(5, model.getQTY());
		pstm.setBigDecimal(6, model.getPRICE());
		pstm.setBigDecimal(7, model.getSUMPRICE());
		pstm.setString(8, model.getCOMP_CDE());
		pstm.setString(9, model.getBLNO());
		pstm.setInt(10, model.getSEQ1());
	}

	public static void setModel(ResultSet rs, TboBXDETAIL model) throws SQLException, Exception {
		model.setLINETYP(rs.getString("LINETYP"));
		model.setPRODUCTID(rs.getString("PRODUCTID"));
		model.setPRODUCTNAME(rs.getString("PRODUCTNAME"));
		model.setREMARKLINE(rs.getString("REMARKLINE"));
		model.setQTY(rs.getBigDecimal("QTY"));
		model.setPRICE(rs.getBigDecimal("PRICE"));
		model.setSUMPRICE(rs.getBigDecimal("SUMPRICE"));
		model.setCOMP_CDE(rs.getString("COMP_CDE"));
		model.setBLNO(rs.getString("BLNO"));
		model.setSEQ1(rs.getInt("SEQ1"));
	}
}
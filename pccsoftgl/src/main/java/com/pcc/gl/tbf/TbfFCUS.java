package com.pcc.gl.tbf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pcc.gl.tbo.TboFCUS;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;

public class TbfFCUS {
	public static boolean insert(TboFCUS model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return insert(dbc, model);
		}
	}

	public static boolean insert(FDbc dbc, TboFCUS model) throws SQLException, Exception {
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

	public static boolean update(TboFCUS model, String fixWhere) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model, fixWhere);
		}
	}

	public static boolean update(FDbc dbc, TboFCUS model, String fixWhere) throws SQLException, Exception {
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

	public static boolean delete(TboFCUS model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return delete(dbc, model);
		}
	}

	public static boolean delete(FDbc dbc, TboFCUS model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmDelete(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setString(2, model.getCUST_CDE());
			int del = pstm.executeUpdate();
			if (del >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean find(TboFCUS model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return find(dbc, model);
		}
	}

	public static boolean find(FDbc dbc, TboFCUS model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmFind(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setString(2, model.getCUST_CDE());
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
		sql.append(" INSERT INTO ").append(TboFCUS.tablename).append("(");
		sql.append(" CUSTYP,");
		sql.append(" TITLE,");
		sql.append(" FNAME,");
		sql.append(" LNAME,");
		sql.append(" IDNO,");
		sql.append(" BRANCH_CODE,");
		sql.append(" AR_CREDIT,");
		sql.append(" AP_CREDIT,");
		sql.append(" TEL1,");
		sql.append(" TEL2,");
		sql.append(" TEL3,");
		sql.append(" FAXNO1,");
		sql.append(" INSBY,");
		sql.append(" INSDT,");
		sql.append(" UPBY,");
		sql.append(" UPDT,");
		sql.append(" COMP_CDE,");
		sql.append(" CUST_CDE)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmUpdate(FDbc dbc, String fixWhere) throws SQLException, Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ").append(TboFCUS.tablename).append(" SET ");
		sql.append(" CUSTYP=?,");
		sql.append(" TITLE=?,");
		sql.append(" FNAME=?,");
		sql.append(" LNAME=?,");
		sql.append(" IDNO=?,");
		sql.append(" BRANCH_CODE=?,");
		sql.append(" AR_CREDIT=?,");
		sql.append(" AP_CREDIT=?,");
		sql.append(" TEL1=?,");
		sql.append(" TEL2=?,");
		sql.append(" TEL3=?,");
		sql.append(" FAXNO1=?,");
		sql.append(" INSBY=?,");
		sql.append(" INSDT=?,");
		sql.append(" UPBY=?,");
		sql.append(" UPDT=?");
		sql.append(" WHERE COMP_CDE=? AND CUST_CDE=? ");
		if (!Fnc.isEmpty(fixWhere)) {
			sql.append(" AND " + fixWhere);
		}
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ").append(TboFCUS.tablename);
		sql.append(" WHERE COMP_CDE=? AND CUST_CDE=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ").append(TboFCUS.tablename);
		sql.append(" WHERE COMP_CDE=? AND CUST_CDE=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static void setPrepareStm(PreparedStatement pstm, TboFCUS model) throws SQLException, Exception {
		pstm.clearParameters();
		pstm.setString(1, model.getCUSTYP());
		pstm.setString(2, model.getTITLE());
		pstm.setString(3, model.getFNAME());
		pstm.setString(4, model.getLNAME());
		pstm.setString(5, model.getIDNO());
		pstm.setString(6, model.getBRANCH_CODE());
		pstm.setInt(7, model.getAR_CREDIT());
		pstm.setInt(8, model.getAP_CREDIT());
		pstm.setString(9, model.getTEL1());
		pstm.setString(10, model.getTEL2());
		pstm.setString(11, model.getTEL3());
		pstm.setString(12, model.getFAXNO1());
		pstm.setString(13, model.getINSBY());
		pstm.setTimestamp(14, model.getINSDT());
		pstm.setString(15, model.getUPBY());
		pstm.setTimestamp(16, model.getUPDT());
		pstm.setString(17, model.getCOMP_CDE());
		pstm.setString(18, model.getCUST_CDE());

	}

	public static void setModel(ResultSet rs, TboFCUS model) throws SQLException, Exception {
		model.setCUSTYP(rs.getString("CUSTYP"));
		model.setTITLE(rs.getString("TITLE"));
		model.setFNAME(rs.getString("FNAME"));
		model.setLNAME(rs.getString("LNAME"));
		model.setIDNO(rs.getString("IDNO"));
		model.setBRANCH_CODE(rs.getString("BRANCH_CODE"));
		model.setAR_CREDIT(rs.getInt("AR_CREDIT"));
		model.setAP_CREDIT(rs.getInt("AP_CREDIT"));
		model.setTEL1(rs.getString("TEL1"));
		model.setTEL2(rs.getString("TEL2"));
		model.setTEL3(rs.getString("TEL3"));
		model.setFAXNO1(rs.getString("FAXNO1"));
		model.setINSBY(rs.getString("INSBY"));
		model.setINSDT(rs.getTimestamp("INSDT"));
		model.setUPBY(rs.getString("UPBY"));
		model.setUPDT(rs.getTimestamp("UPDT"));
		model.setCOMP_CDE(rs.getString("COMP_CDE"));
		model.setCUST_CDE(rs.getString("CUST_CDE"));
	}
}
package com.pcc.bx.tbf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pcc.bx.tbo.TboBXHEADER;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;

public class TbfBXHEADER {

	public static boolean insert(TboBXHEADER model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return insert(dbc, model);
		}
	}

	public static boolean insert(FDbc dbc, TboBXHEADER model) throws SQLException, Exception {
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

	public static boolean update(TboBXHEADER model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model);
		}
	}

	public static boolean update(FDbc dbc, TboBXHEADER model) throws SQLException, Exception {
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

	public static boolean update(TboBXHEADER model, String fixWhere) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model, fixWhere);
		}
	}

	public static boolean update(FDbc dbc, TboBXHEADER model, String fixWhere) throws SQLException, Exception {
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

	public static boolean delete(TboBXHEADER model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return delete(dbc, model);
		}
	}

	public static boolean delete(FDbc dbc, TboBXHEADER model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmDelete(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setString(2, model.getBLNO());
			int del = pstm.executeUpdate();
			if (del >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean find(TboBXHEADER model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return find(dbc, model);
		}
	}

	public static boolean find(FDbc dbc, TboBXHEADER model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmFind(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setString(2, model.getBLNO());
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
		sql.append(" INSERT INTO ").append(TboBXHEADER.tablename).append("(");
		sql.append(" TMPLCDE,");
		sql.append(" BLDATE,");
		sql.append(" VATTYPE,");
		sql.append(" CUST_CDE,");
		sql.append(" IDNO,");
		sql.append(" CUST_TITLE,");
		sql.append(" CUST_FNAME,");
		sql.append(" CUST_LNAME,");
		sql.append(" CUST_ADDR1,");
		sql.append(" CUST_ADDR2,");
		sql.append(" ZIPCODE,");
		sql.append(" REMARK1,");
		sql.append(" VATRATE,");
		sql.append(" VATAMT,");
		sql.append(" BASAMT,");
		sql.append(" TOTALAMT,");
		sql.append(" BRANC_CDE,");
		sql.append(" RECSTA,");
		sql.append(" COSTVATAMT,");
		sql.append(" COSTBASAMT,");
		sql.append(" CUST_CDE_AP,");
		sql.append(" APNAME,");
		sql.append(" SENDDATE,");
		sql.append(" UPBY,");
		sql.append(" UPDT,");
		sql.append(" DISCOUNTAMT,");
		sql.append(" COMP_CDE,");
		sql.append(" BLNO)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmUpdate(FDbc dbc, String fixWhere) throws SQLException, Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ").append(TboBXHEADER.tablename).append(" SET ");
		sql.append(" TMPLCDE=?,");
		sql.append(" BLDATE=?,");
		sql.append(" VATTYPE=?,");
		sql.append(" CUST_CDE=?,");
		sql.append(" IDNO=?,");
		sql.append(" CUST_TITLE=?,");
		sql.append(" CUST_FNAME=?,");
		sql.append(" CUST_LNAME=?,");
		sql.append(" CUST_ADDR1=?,");
		sql.append(" CUST_ADDR2=?,");
		sql.append(" ZIPCODE=?,");
		sql.append(" REMARK1=?,");
		sql.append(" VATRATE=?,");
		sql.append(" VATAMT=?,");
		sql.append(" BASAMT=?,");
		sql.append(" TOTALAMT=?,");
		sql.append(" BRANC_CDE=?,");
		sql.append(" RECSTA=?,");
		sql.append(" COSTVATAMT=?,");
		sql.append(" COSTBASAMT=?,");
		sql.append(" CUST_CDE_AP=?,");
		sql.append(" APNAME=?,");
		sql.append(" SENDDATE=?,");
		sql.append(" UPBY=?,");
		sql.append(" UPDT=?,");
		sql.append(" DISCOUNTAMT=?");
		sql.append(" WHERE COMP_CDE=? AND BLNO=? ");
		if (!Fnc.isEmpty(fixWhere)) {
			sql.append(" AND " + fixWhere);
		}
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ").append(TboBXHEADER.tablename);
		sql.append(" WHERE COMP_CDE=? AND BLNO=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ").append(TboBXHEADER.tablename);
		sql.append(" WHERE COMP_CDE=? AND BLNO=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static void setPrepareStm(PreparedStatement pstm, TboBXHEADER model) throws SQLException, Exception {
		pstm.clearParameters();
		pstm.setInt(1, model.getTMPLCDE());
		pstm.setDate(2, model.getBLDATE());
		pstm.setString(3, model.getVATTYPE());
		pstm.setString(4, model.getCUST_CDE());
		pstm.setString(5, model.getIDNO());
		pstm.setString(6, model.getCUST_TITLE());
		pstm.setString(7, model.getCUST_FNAME());
		pstm.setString(8, model.getCUST_LNAME());
		pstm.setString(9, model.getCUST_ADDR1());
		pstm.setString(10, model.getCUST_ADDR2());
		pstm.setString(11, model.getZIPCODE());
		pstm.setString(12, model.getREMARK1());
		pstm.setBigDecimal(13, model.getVATRATE());
		pstm.setBigDecimal(14, model.getVATAMT());
		pstm.setBigDecimal(15, model.getBASAMT());
		pstm.setBigDecimal(16, model.getTOTALAMT());
		pstm.setInt(17, model.getBRANC_CDE());
		pstm.setInt(18, model.getRECSTA());
		pstm.setBigDecimal(19, model.getCOSTVATAMT());
		pstm.setBigDecimal(20, model.getCOSTBASAMT());
		pstm.setString(21, model.getCUST_CDE_AP());
		pstm.setString(22, model.getAPNAME());
		pstm.setDate(23, model.getSENDDATE());
		pstm.setString(24, model.getUPBY());
		pstm.setTimestamp(25, model.getUPDT());
		pstm.setBigDecimal(26, model.getDISCOUNTAMT());
		pstm.setString(27, model.getCOMP_CDE());
		pstm.setString(28, model.getBLNO());
	}

	public static void setModel(ResultSet rs, TboBXHEADER model) throws SQLException, Exception {
		model.setTMPLCDE(rs.getInt("TMPLCDE"));
		model.setBLDATE(rs.getDate("BLDATE"));
		model.setVATTYPE(rs.getString("VATTYPE"));
		model.setCUST_CDE(rs.getString("CUST_CDE"));
		model.setIDNO(rs.getString("IDNO"));
		model.setCUST_TITLE(rs.getString("CUST_TITLE"));
		model.setCUST_FNAME(rs.getString("CUST_FNAME"));
		model.setCUST_LNAME(rs.getString("CUST_LNAME"));
		model.setCUST_ADDR1(rs.getString("CUST_ADDR1"));
		model.setCUST_ADDR2(rs.getString("CUST_ADDR2"));
		model.setZIPCODE(rs.getString("ZIPCODE"));
		model.setREMARK1(rs.getString("REMARK1"));
		model.setVATRATE(rs.getBigDecimal("VATRATE"));
		model.setVATAMT(rs.getBigDecimal("VATAMT"));
		model.setBASAMT(rs.getBigDecimal("BASAMT"));
		model.setTOTALAMT(rs.getBigDecimal("TOTALAMT"));
		model.setBRANC_CDE(rs.getInt("BRANC_CDE"));
		model.setRECSTA(rs.getInt("RECSTA"));
		model.setCOSTVATAMT(rs.getBigDecimal("COSTVATAMT"));
		model.setCOSTBASAMT(rs.getBigDecimal("COSTBASAMT"));
		model.setCUST_CDE_AP(rs.getString("CUST_CDE_AP"));
		model.setAPNAME(rs.getString("APNAME"));
		model.setSENDDATE(rs.getDate("SENDDATE"));
		model.setUPBY(rs.getString("UPBY"));
		model.setUPDT(rs.getTimestamp("UPDT"));
		model.setDISCOUNTAMT(rs.getBigDecimal("DISCOUNTAMT"));
		model.setCOMP_CDE(rs.getString("COMP_CDE"));
		model.setBLNO(rs.getString("BLNO"));
	}
}

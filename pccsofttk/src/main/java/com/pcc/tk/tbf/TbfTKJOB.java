package com.pcc.tk.tbf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;
import com.pcc.tk.tbo.TboTKJOB;

public class TbfTKJOB {

	public static boolean insert(TboTKJOB model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return insert(dbc, model);
		}
	}

	public static boolean insert(FDbc dbc, TboTKJOB model) throws SQLException, Exception {
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

	public static boolean update(TboTKJOB model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model);
		}
	}

	public static boolean update(FDbc dbc, TboTKJOB model) throws SQLException, Exception {
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

	public static boolean update(TboTKJOB model, String fixWhere) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return update(dbc, model, fixWhere);
		}
	}

	public static boolean update(FDbc dbc, TboTKJOB model, String fixWhere) throws SQLException, Exception {
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

	public static boolean delete(TboTKJOB model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return delete(dbc, model);
		}
	}

	public static boolean delete(FDbc dbc, TboTKJOB model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmDelete(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setString(2, model.getJOBNO());
			int del = pstm.executeUpdate();
			if (del >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean find(TboTKJOB model) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return find(dbc, model);
		}
	}

	public static boolean find(FDbc dbc, TboTKJOB model) throws SQLException, Exception {
		try (PreparedStatement pstm = getPrepareStmFind(dbc);) {
			pstm.setString(1, model.getCOMP_CDE());
			pstm.setString(2, model.getJOBNO());
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
		sql.append(" INSERT INTO ").append(TboTKJOB.tablename).append("(");
		sql.append(" JOBDATE,");
		sql.append(" LAWSTATID,");
		sql.append(" SENDJOBDATE,");
		sql.append(" JOBCODE,");
		sql.append(" CUSTCONTACTNO,");
		sql.append(" PLAINT,");
		sql.append(" SUEATDATE,");
		sql.append(" SUEDDATE,");
		sql.append(" SUECOSTAMT,");
		sql.append(" FEEAMT,");
		sql.append(" REMARK1,");
		sql.append(" LAWBLACKDATE,");
		sql.append(" LAWBLACKNO,");
		sql.append(" LAWREDDATE,");
		sql.append(" LAWREDNO,");
		sql.append(" COURTID,");
		sql.append(" REMARK2,");
		sql.append(" CLOSEDATE,");
		sql.append(" JOBSTAT,");
		sql.append(" DELBY,");
		sql.append(" DELDTE,");
		sql.append(" INSBY,");
		sql.append(" INSDTE,");
		sql.append(" UPDBY,");
		sql.append(" UPDDTE,");
		sql.append(" LAWTYPEID,");
		sql.append(" ZONEID,");
		sql.append(" COMP_CDE,");
		sql.append(" JOBNO)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmUpdate(FDbc dbc, String fixWhere) throws SQLException, Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE ").append(TboTKJOB.tablename).append(" SET ");
		sql.append(" JOBDATE=?,");
		sql.append(" LAWSTATID=?,");
		sql.append(" SENDJOBDATE=?,");
		sql.append(" JOBCODE=?,");
		sql.append(" CUSTCONTACTNO=?,");
		sql.append(" PLAINT=?,");
		sql.append(" SUEATDATE=?,");
		sql.append(" SUEDDATE=?,");
		sql.append(" SUECOSTAMT=?,");
		sql.append(" FEEAMT=?,");
		sql.append(" REMARK1=?,");
		sql.append(" LAWBLACKDATE=?,");
		sql.append(" LAWBLACKNO=?,");
		sql.append(" LAWREDDATE=?,");
		sql.append(" LAWREDNO=?,");
		sql.append(" COURTID=?,");
		sql.append(" REMARK2=?,");
		sql.append(" CLOSEDATE=?,");
		sql.append(" JOBSTAT=?,");
		sql.append(" DELBY=?,");
		sql.append(" DELDTE=?,");
		sql.append(" INSBY=?,");
		sql.append(" INSDTE=?,");
		sql.append(" UPDBY=?,");
		sql.append(" UPDDTE=?,");
		sql.append(" LAWTYPEID=?,");
		sql.append(" ZONEID=?");
		sql.append(" WHERE COMP_CDE=? AND JOBNO=? ");
		if (!Fnc.isEmpty(fixWhere)) {
			sql.append(" AND " + fixWhere);
		}
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ").append(TboTKJOB.tablename);
		sql.append(" WHERE COMP_CDE=? AND JOBNO=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM ").append(TboTKJOB.tablename);
		sql.append(" WHERE COMP_CDE=? AND JOBNO=? ");
		return dbc.getPreparedStatement(sql.toString());
	}

	public static void setPrepareStm(PreparedStatement pstm, TboTKJOB model) throws SQLException, Exception {
		pstm.clearParameters();
		pstm.setDate(1, model.getJOBDATE());
		pstm.setInt(2, model.getLAWSTATID());
		pstm.setDate(3, model.getSENDJOBDATE());
		pstm.setInt(4, model.getJOBCODE());
		pstm.setString(5, model.getCUSTCONTACTNO());
		pstm.setString(6, model.getPLAINT());
		pstm.setDate(7, model.getSUEATDATE());
		pstm.setDate(8, model.getSUEDDATE());
		pstm.setBigDecimal(9, model.getSUECOSTAMT());
		pstm.setBigDecimal(10, model.getFEEAMT());
		pstm.setString(11, model.getREMARK1());
		pstm.setDate(12, model.getLAWBLACKDATE());
		pstm.setString(13, model.getLAWBLACKNO());
		pstm.setDate(14, model.getLAWREDDATE());
		pstm.setString(15, model.getLAWREDNO());
		pstm.setInt(16, model.getCOURTID());
		pstm.setString(17, model.getREMARK2());
		pstm.setDate(18, model.getCLOSEDATE());
		pstm.setString(19, model.getJOBSTAT());
		pstm.setString(20, model.getDELBY());
		pstm.setTimestamp(21, model.getDELDTE());
		pstm.setString(22, model.getINSBY());
		pstm.setTimestamp(23, model.getINSDTE());
		pstm.setString(24, model.getUPDBY());
		pstm.setTimestamp(25, model.getUPDDTE());
		pstm.setInt(26, model.getLAWTYPEID());
		pstm.setInt(27, model.getZONEID());
		pstm.setString(28, model.getCOMP_CDE());
		pstm.setString(29, model.getJOBNO());
	}

	public static void setModel(ResultSet rs, TboTKJOB model) throws SQLException, Exception {
		model.setJOBDATE(rs.getDate("JOBDATE"));
		model.setLAWSTATID(rs.getInt("LAWSTATID"));
		model.setSENDJOBDATE(rs.getDate("SENDJOBDATE"));
		model.setJOBCODE(rs.getInt("JOBCODE"));
		model.setCUSTCONTACTNO(rs.getString("CUSTCONTACTNO"));
		model.setPLAINT(rs.getString("PLAINT"));
		model.setSUEATDATE(rs.getDate("SUEATDATE"));
		model.setSUEDDATE(rs.getDate("SUEDDATE"));
		model.setSUECOSTAMT(rs.getBigDecimal("SUECOSTAMT"));
		model.setFEEAMT(rs.getBigDecimal("FEEAMT"));
		model.setREMARK1(rs.getString("REMARK1"));
		model.setLAWBLACKDATE(rs.getDate("LAWBLACKDATE"));
		model.setLAWBLACKNO(rs.getString("LAWBLACKNO"));
		model.setLAWREDDATE(rs.getDate("LAWREDDATE"));
		model.setLAWREDNO(rs.getString("LAWREDNO"));
		model.setCOURTID(rs.getInt("COURTID"));
		model.setREMARK2(rs.getString("REMARK2"));
		model.setCLOSEDATE(rs.getDate("CLOSEDATE"));
		model.setJOBSTAT(rs.getString("JOBSTAT"));
		model.setDELBY(rs.getString("DELBY"));
		model.setDELDTE(rs.getTimestamp("DELDTE"));
		model.setINSBY(rs.getString("INSBY"));
		model.setINSDTE(rs.getTimestamp("INSDTE"));
		model.setUPDBY(rs.getString("UPDBY"));
		model.setUPDDTE(rs.getTimestamp("UPDDTE"));
		model.setLAWTYPEID(rs.getInt("LAWTYPEID"));
		model.setZONEID(rs.getInt("ZONEID"));
		model.setCOMP_CDE(rs.getString("COMP_CDE"));
		model.setJOBNO(rs.getString("JOBNO"));
	}
}
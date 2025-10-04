package com.pcc.bx.tbm;

import java.util.List;

import com.pcc.bx.tbf.TbfBXHEADER;
import com.pcc.bx.tbo.TboBXHEADER;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.SqlStr;

public class TbmBXHEADER {

	public static void getData(String comp_cde, FModelHasMap para, List<TboBXHEADER> lst_data) throws Exception {

		lst_data.clear();

		try (var dbc = FDbc.connectMasterDb()) {
			
			SqlStr sql = new SqlStr();
			sql.addLine("SELECT * FROM bxheader ");
			sql.addLine(" where COMP_CDE ='" + Fnc.sqlQuote(comp_cde) + "' ");

			if (!Fnc.isEmpty(para.getString("BLNO"))) {
				sql.addLine("and BLNO like '" + Fnc.sqlQuote(para.getString("BLNO")) +"%' ");
			}
			
			if (para.get("BLDATEFROM") != null) {
				sql.addLine(" and BLDATE >= '"+para.getDate("BLDATEFROM")+"' ");
			}
			
			if (para.get("BLDATETO") != null) {
				sql.addLine(" and BLDATE <= '"+para.getDate("BLDATETO")+"' ");
			}

			if (!Fnc.isEmpty(para.getString("CUST_FNAME"))) {
				sql.addLine("and CUST_FNAME like '" + Fnc.sqlQuote(para.getString("CUST_FNAME")) +"%' ");
			}
			
			if (!Fnc.isEmpty(para.getString("CUST_LNAME"))) {
				sql.addLine("and CUST_LNAME like '" + Fnc.sqlQuote(para.getString("CUST_LNAME")) +"%' ");
			}
 
			if (para.getString("MENUID2").equals("FrmBxBill")) {
				sql.addLine("and RECSTA != 9 ");
			} else if (para.getString("MENUID2").equals("FrmBxCostAndAp")) {
				sql.addLine("and RECSTA = 1 ");
			} else if (para.getString("MENUID2").equals("FrmBxBillDel")) {
				sql.addLine("and RECSTA != 9 ");
			}

			sql.addLine(" order by BLNO desc");

			System.out.println(sql.getSql());			
			try (java.sql.ResultSet rs = dbc.getResultSetFw3(sql.getSql(), 1000)) {
				while (rs.next()) {
					TboBXHEADER rec1 = new TboBXHEADER();
					TbfBXHEADER.setModel(rs, rec1);
					lst_data.add(rec1);
				}
			}
			
		}
		
	}
	
//	public static int getMaxCode(String comp_cde) throws Exception {
//		int ret = 0;
//		try (FDbc dbc = FDbc.connectMasterDb()) {
//			String sql = "select max(aa.TMPLCDE) as F1 from bxtmplhead aa where aa.COMP_CDE =?";
//			try (java.sql.ResultSet rs1 = dbc.getResultSetFw2(sql, comp_cde);) {
//				if (rs1.next()) {
//					ret = rs1.getInt(1);
//				}
//			}
//
//		}
//
//		return ret;
//	}
	
}

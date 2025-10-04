package com.pcc.bx.tbm;

import java.util.List;

import com.pcc.bx.tbf.TbfBXTMPLHEAD;
import com.pcc.bx.tbo.TboBXTMPLHEAD;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.SqlStr;

public class TbmBXTMPLHEAD {

	public static void getData(String comp_cde, FModelHasMap para, List<TboBXTMPLHEAD> lst_data) throws Exception {

		lst_data.clear();

		try (var dbc = FDbc.connectMasterDb()) {
			
			SqlStr sql = new SqlStr();
			sql.addLine(" select * from bxtmplhead where COMP_CDE ='" + Fnc.sqlQuote(comp_cde) + "'  ");

			if (para.getInt("TMPLCDE") > 0) {
				sql.addLine("and TMPLCDE = " + para.getInt("TMPLCDE"));
			}
			if (!Fnc.isEmpty(para.getString("DOCPREFIX"))) {
				sql.addLine("and DOCPREFIX like '%" + Fnc.sqlQuote(para.getString("DOCPREFIX")) + "%' ");
			}
			if (!Fnc.isEmpty(para.getString("TMPLNAME"))) {
				sql.addLine("and TMPLNAME like '%" + Fnc.sqlQuote(para.getString("TMPLNAME")) + "%' ");
			}

			sql.addLine("order by TMPLCDE");
			System.out.println(sql.getSql());
			
			try (java.sql.ResultSet rs = dbc.getResultSetFw3(sql.getSql(), 1000)) {
				while (rs.next()) {
					TboBXTMPLHEAD rec1 = new TboBXTMPLHEAD();
					TbfBXTMPLHEAD.setModel(rs, rec1);
					lst_data.add(rec1);
				}
			}
			
		}
		
	}
	
	public static int getMaxCode(String comp_cde) throws Exception {
		int ret = 0;
		try (FDbc dbc = FDbc.connectMasterDb()) {
			String sql = "select max(aa.TMPLCDE) as F1 from bxtmplhead aa where aa.COMP_CDE =?";
			try (java.sql.ResultSet rs1 = dbc.getResultSetFw2(sql, comp_cde);) {
				if (rs1.next()) {
					ret = rs1.getInt(1);
				}
			}

		}

		return ret;
	}
	
}

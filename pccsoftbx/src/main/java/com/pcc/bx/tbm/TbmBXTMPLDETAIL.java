package com.pcc.bx.tbm;

import java.util.List;

import com.pcc.bx.tbf.TbfBXTMPLDETAIL;
import com.pcc.bx.tbo.TboBXTMPLDETAIL;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.SqlStr;

public class TbmBXTMPLDETAIL {

	public static void getData(String comp_cde, Integer tmplcde, List<TboBXTMPLDETAIL> lst_data) throws Exception {

		lst_data.clear();

		try (var dbc = FDbc.connectMasterDb()) {

			SqlStr sql = new SqlStr();
			sql.addLine(" select * from bxtmpldetail where COMP_CDE ='" + Fnc.sqlQuote(comp_cde) + "' and TMPLCDE=" + tmplcde + " order by TMPLSEQ ");
			System.out.println(sql.getSql());

			try (java.sql.ResultSet rs = dbc.getResultSetFw(sql.getSql())) {
				while (rs.next()) {
					TboBXTMPLDETAIL rec1 = new TboBXTMPLDETAIL();
					TbfBXTMPLDETAIL.setModel(rs, rec1);
					lst_data.add(rec1);
				}
			}

		}

	}
	
	public static int getMaxCode(String comp_cde, int tmplcde) throws Exception {
		int ret = 0;
		try (FDbc dbc = FDbc.connectMasterDb()) {
			String sql = "select max(aa.TMPLSEQ) as F1 from bxtmpldetail aa where aa.COMP_CDE =? and aa.TMPLCDE=? ";
			try (java.sql.ResultSet rs1 = dbc.getResultSetFw2(sql, comp_cde, tmplcde);) {
				if (rs1.next()) {
					ret = rs1.getInt(1);
				}
			}

		}

		return ret;
	}
	
	public static int getDeleteAll(FDbc dbc, String comp_cde, int tmplcde) throws Exception {
		int ret = 0;
		String sql = "delete from bxtmpldetail where COMP_CDE =? and TMPLCDE=? ";
		ret = dbc.executeSql2(sql, comp_cde, tmplcde);
		return ret;
	}

}

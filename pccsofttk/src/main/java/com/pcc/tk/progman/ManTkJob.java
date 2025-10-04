package com.pcc.tk.progman;

import java.util.List;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.Fnc;

public class ManTkJob {

	public static void getDataByJobno(String comp_cde, String jobno, List<FModelHasMap> lst_data) throws Exception {
		lst_data.clear();
		try (FDbc dbc = FDbc.connectMasterDb();) {
			String sql = """
					select aa.*,bb.LAWYERNAME  
					from tkjoblawyer aa 
					left join tklawyer bb on aa.COMP_CDE =bb.COMP_CDE and aa.LAWYERID =bb.LAWYERID 
					where aa.COMP_CDE =? and aa.JOBNO=? 
					order by aa.SEQ
					""";
			try(java.sql.ResultSet rs = dbc.getResultSetFw2(sql, comp_cde, jobno)){
				while (rs.next()) {
					FModelHasMap rec = new FModelHasMap();
					lst_data.add(rec);
					rec.putRecord(rs);
				}
			}		
		}
	}
	
	public static void getTkjobdocdesc(String comp_cde, List<String> lst_data) throws Exception {
		lst_data.clear();
		try (FDbc dbc = FDbc.connectMasterDb();) {
			String sql = """
					select DOCDESC from tkjobdocdesc where COMP_CDE=? and SYS_CDE=? order by DOCDESC
					""";
			try (java.sql.ResultSet rs = dbc.getResultSetFw2(sql, comp_cde, "FrmTkJobDoc")) {
				while (rs.next()) {
					lst_data.add(Fnc.getStr(rs.getString("DOCDESC")));
				}
			}
		}
	}

}

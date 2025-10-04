package com.pcc.tk.tbm;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FModelHasMap;

public class TbmTKJOBCLIENT {

	public static void getDataByJobno(String comp_cde, String jobno, java.util.List<FModelHasMap> lst_data) throws Exception {
		lst_data.clear();
		try (FDbc dbc = FDbc.connectMasterDb();) {
			String sql = " select * from tkjobclient where COMP_CDE =? and JOBNO =? order by SEQ  ";
			try(java.sql.ResultSet rs = dbc.getResultSetFw2(sql, comp_cde, jobno)){
				while (rs.next()) {
					FModelHasMap rec = new FModelHasMap();
					lst_data.add(rec);
					
					rec.putRecord(rs);
					
					String sCUSTNAME = rs.getString("CLIENTTITLE")+" "+rs.getString("CLIENTFNAME")+" "+rs.getString("CLIENTLNAME");
					rec.put("CUSTNAME", sCUSTNAME.trim());
				}
			}		
		}
	}

	public static int maxSeq(FDbc dbc, String comp_cde, String jobno) throws Exception {
		int ret = 0;

		String sql = " select max(SEQ) as F1 from tkjobclient where COMP_CDE =? and JOBNO =? ";
		try (java.sql.ResultSet rs = dbc.getResultSetFw2(sql, comp_cde, jobno)) {
			if (rs.next()) {
				ret = rs.getInt("F1");
			}
		}

		return ret;
	}

}

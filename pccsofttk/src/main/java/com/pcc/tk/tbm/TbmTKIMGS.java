package com.pcc.tk.tbm;

import com.pcc.sys.lib.FDbc;

public class TbmTKIMGS {

	public static int getMax_IMGSEQ(String comp_cde, String sys_cde) throws Exception {

		int ret = 0;
		
		String sql = " select max(IMGSEQ) as F1 from tkimgs where COMP_CDE=? and SYS_CDE=? ";
		try (FDbc dbc = FDbc.connectMasterDb();
				java.sql.ResultSet rs = dbc.getResultSetFw2(sql, comp_cde, sys_cde)) {
			
			while (rs.next()) {
				ret = rs.getInt(1);
			}
			
		}
		
		return ret;
		
	}

}

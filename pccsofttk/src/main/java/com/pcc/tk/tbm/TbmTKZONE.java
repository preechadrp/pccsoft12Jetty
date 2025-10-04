package com.pcc.tk.tbm;

import com.pcc.sys.lib.FDbc;
import com.pcc.tk.tbf.TbfTKZONE;
import com.pcc.tk.tbo.TboTKZONE;

public class TbmTKZONE {

	public static int getMaxCode(String comp_cde) throws Exception {
		int ret = 0;
		try (FDbc dbc = FDbc.connectMasterDb()) {
			String sql = "select max(aa.ZONEID) as F1 from tkzone aa where aa.COMP_CDE =?";
			try (java.sql.ResultSet rs1 = dbc.getResultSetFw2(sql, comp_cde);) {
				if (rs1.next()) {
					ret = rs1.getInt(1);
				}
			}

		}

		return ret;
	}
	
	public static String get_ZONEID_NAME(String comp_cde, int zoneid) throws Exception {
		
		TboTKZONE tb1 = new TboTKZONE();

		tb1.setCOMP_CDE(comp_cde);
		tb1.setZONEID(zoneid);
		
		if (TbfTKZONE.find(tb1)) {
			return tb1.getZONENAME();
		} else {
			return "";
		}
		
	}
	
}

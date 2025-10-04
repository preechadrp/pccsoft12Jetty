package com.pcc.tk.tbm;

import com.pcc.sys.lib.FDbc;
import com.pcc.tk.tbf.TbfTKJOBCODE;
import com.pcc.tk.tbo.TboTKJOBCODE;

public class TbmTKJOBCODE {

	public static int getMaxCode(String comp_cde) throws Exception {
		int ret = 0;
		try (FDbc dbc = FDbc.connectMasterDb()) {
			String sql = "select max(aa.JOBCODE) as F1 from tkjobcode aa where aa.COMP_CDE =?";
			try (java.sql.ResultSet rs1 = dbc.getResultSetFw2(sql, comp_cde);) {
				if (rs1.next()) {
					ret = rs1.getInt(1);
				}
			}

		}

		return ret;
	}
	
	public static String get_JOBCODENAME(String comp_cde, int jobcode) throws Exception {

		TboTKJOBCODE tb1 = new TboTKJOBCODE();

		tb1.setCOMP_CDE(comp_cde);
		tb1.setJOBCODE(jobcode);

		if (TbfTKJOBCODE.find(tb1)) {
			return tb1.getJOBNAME();
		} else {
			return "";
		}

	}
	
}

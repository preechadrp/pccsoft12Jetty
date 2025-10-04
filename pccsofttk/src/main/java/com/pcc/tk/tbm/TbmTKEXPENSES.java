package com.pcc.tk.tbm;

import com.pcc.sys.lib.FDbc;

public class TbmTKEXPENSES {

	public static int getMaxCode(String comp_cde) throws Exception {
		int ret = 0;
		try (FDbc dbc = FDbc.connectMasterDb()) {
			String sql = "select max(aa.EXPENSESID) as F1 from tkexpenses aa where aa.COMP_CDE =?";
			try (java.sql.ResultSet rs1 = dbc.getResultSetFw2(sql, comp_cde);) {
				if (rs1.next()) {
					ret = rs1.getInt(1);
				}
			}

		}

		return ret;
	}
	
}

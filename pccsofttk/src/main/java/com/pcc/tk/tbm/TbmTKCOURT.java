package com.pcc.tk.tbm;

import com.pcc.sys.lib.FDbc;
import com.pcc.tk.tbf.TbfTKCOURT;
import com.pcc.tk.tbo.TboTKCOURT;

public class TbmTKCOURT {

	public static int getMaxCode(String comp_cde) throws Exception {
		int ret = 0;
		try (FDbc dbc = FDbc.connectMasterDb()) {
			String sql = "select max(aa.COURTID) as F1 from tkcourt aa where aa.COMP_CDE =?";
			try (java.sql.ResultSet rs1 = dbc.getResultSetFw2(sql, comp_cde);) {
				if (rs1.next()) {
					ret = rs1.getInt(1);
				}
			}

		}

		return ret;
	}
	
	public static String get_COURTID_Name(String comp_cde, int courtid) throws Exception {

		TboTKCOURT tb1 = new TboTKCOURT();

		tb1.setCOMP_CDE(comp_cde);
		tb1.setCOURTID(courtid);

		if (TbfTKCOURT.find(tb1)) {
			return tb1.getCOURTNAME();
		} else {
			return "";
		}

	}
	
}

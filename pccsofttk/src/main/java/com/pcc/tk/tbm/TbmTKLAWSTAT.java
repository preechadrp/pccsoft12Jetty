package com.pcc.tk.tbm;

import com.pcc.sys.lib.FDbc;
import com.pcc.tk.tbf.TbfTKLAWSTAT;
import com.pcc.tk.tbo.TboTKLAWSTAT;

public class TbmTKLAWSTAT {

	public static int getMaxCode(String comp_cde) throws Exception {
		int ret = 0;
		try (FDbc dbc = FDbc.connectMasterDb()) {
			String sql = "select max(aa.LAWSTATID) as F1 from tklawstat aa where aa.COMP_CDE =?";
			try (java.sql.ResultSet rs1 = dbc.getResultSetFw2(sql, comp_cde);) {
				if (rs1.next()) {
					ret = rs1.getInt(1);
				}
			}

		}

		return ret;
	}
	
	public static String get_LAWSTATID(String comp_cde, int lawstatid) throws Exception {

		TboTKLAWSTAT tb1 = new TboTKLAWSTAT();

		tb1.setCOMP_CDE(comp_cde);
		tb1.setLAWSTATID(lawstatid);

		if (TbfTKLAWSTAT.find(tb1)) {
			return  tb1.getLAWSTATNAME();
		} else {
			return "";
		}

	}
	
}

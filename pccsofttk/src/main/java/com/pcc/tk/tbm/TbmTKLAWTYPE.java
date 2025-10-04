package com.pcc.tk.tbm;

import com.pcc.sys.lib.FDbc;
import com.pcc.tk.tbf.TbfTKLAWTYPE;
import com.pcc.tk.tbo.TboTKLAWTYPE;

public class TbmTKLAWTYPE {

	public static int getMaxCode(String comp_cde) throws Exception {
		int ret = 0;
		try (FDbc dbc = FDbc.connectMasterDb()) {
			String sql = "select max(aa.LAWTYPEID) as F1 from tklawtype aa where aa.COMP_CDE =?";
			try (java.sql.ResultSet rs1 = dbc.getResultSetFw2(sql, comp_cde);) {
				if (rs1.next()) {
					ret = rs1.getInt(1);
				}
			}

		}

		return ret;
	}
	
	public static String get_LAWTYPE_NAME(String comp_cde, int lawtypeid) throws Exception {

		TboTKLAWTYPE tklawtype = new TboTKLAWTYPE();

		tklawtype.setCOMP_CDE(comp_cde);
		tklawtype.setLAWTYPEID(lawtypeid);

		if (TbfTKLAWTYPE.find(tklawtype)) {
			return tklawtype.getLAWTYPENAME();
		} else {
			return "";
		}

	}
	
}

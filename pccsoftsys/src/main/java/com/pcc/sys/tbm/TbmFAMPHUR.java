package com.pcc.sys.tbm;

import com.pcc.sys.lib.FDbc;

public class TbmFAMPHUR {

	public static int getMax_AMPHUR_ID() throws Exception {

		int ret = 0;
		try (FDbc dbc = FDbc.connectMasterDb()) {
			String sql1 = " SELECT max(aa.AMPHURID) AS f1 FROM FAMPHUR aa ";
			try (java.sql.ResultSet rs1 = dbc.getResultSet(sql1);) {
				if (rs1.next()) {
					ret = rs1.getInt(1);
				}
			}
		}

		return ret;

	}

}

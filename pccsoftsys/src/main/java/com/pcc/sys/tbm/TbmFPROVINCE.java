package com.pcc.sys.tbm;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.tbf.TbfFPROVINCE;
import com.pcc.sys.tbo.TboFPROVINCE;

public class TbmFPROVINCE {

	public static String getProviceNameWithPrefix(int provin_id) throws Exception {

		String ret = "";

		TboFPROVINCE prov = new TboFPROVINCE();

		prov.setPROVIN_ID(provin_id);

		if (TbfFPROVINCE.find(prov)) {
			if (prov.getPROVIN_ID() == 10) { //กทม.
				ret = prov.getPROVIN_NAME();
			} else {
				ret = "จังหวัด" + prov.getPROVIN_NAME();
			}

		}

		return ret;

	}

	public static int getMax_provin_id() throws Exception {

		int ret = 0;
		try (FDbc dbc = FDbc.connectMasterDb()) {
			String sql1 = " select max(aa.PROVIN_ID) as F1 from fprovince aa ";
			try (java.sql.ResultSet rs1 = dbc.getResultSet(sql1);) {
				if (rs1.next()) {
					ret = rs1.getInt(1);
				}
			}
		}

		return ret;

	}

}

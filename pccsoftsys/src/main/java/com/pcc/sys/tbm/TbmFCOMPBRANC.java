package com.pcc.sys.tbm;

import java.sql.ResultSet;
import java.util.ArrayList;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.tbf.TbfFCOMPBRANC;
import com.pcc.sys.tbo.TboFCOMPBRANC;

public class TbmFCOMPBRANC {

	public static int getMaxBranc(String comp_cde) throws Exception {
		int ret = 0;
		try (FDbc dbc = FDbc.connectMasterDb()) {

			String sql1 = " SELECT max(aa.BRANC_CDE) AS F1 FROM fcompbranc aa WHERE aa.COMP_CDE=? ";
			try (ResultSet rs = dbc.getResultSet2(sql1, comp_cde);) {
				if (rs.next()) {
					ret = rs.getInt(1);
				}
			}

		}
		return ret;
	}

	public static java.util.List<TboFCOMPBRANC> getBrancList(String comp_cde) throws Exception {
		java.util.List<TboFCOMPBRANC> lst_data = new ArrayList<TboFCOMPBRANC>();
		getBranc(comp_cde, lst_data);
		return lst_data;
	}

	public static int getBranc(String comp_cde, java.util.List<TboFCOMPBRANC> lst_data) throws Exception {
		int ret = 0;
		lst_data.clear();
		try (FDbc dbc = FDbc.connectMasterDb()) {

			String sql1 = " SELECT aa.* FROM fcompbranc aa WHERE aa.COMP_CDE=? ";
			try (ResultSet rs = dbc.getResultSet2(sql1, comp_cde);) {
				while (rs.next()) {
					TboFCOMPBRANC rec1 = new TboFCOMPBRANC();
					TbfFCOMPBRANC.setModel(rs, rec1);
					lst_data.add(rec1);
				}
			}

		}
		return ret;
	}

	public static void delAllBranc(FDbc dbc, String comp_cde) throws Exception {
		String sql1 = " delete FROM fcompbranc WHERE COMP_CDE=? ";
		dbc.executeSql2(sql1, comp_cde);
	}

	public static TboFCOMPBRANC getBranc(String comp_cde, int branc_cde) throws Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return getBranc(dbc, comp_cde, branc_cde);
		}
	}
	
	public static TboFCOMPBRANC getBranc(FDbc dbc, String comp_cde, int branc_cde) throws Exception {

		String sql1 = " SELECT * FROM fcompbranc WHERE COMP_CDE=? and BRANC_CDE=?";
		try (ResultSet rs = dbc.getResultSet2(sql1, comp_cde, branc_cde);) {
			if (rs.next()) {
				TboFCOMPBRANC rec1 = new TboFCOMPBRANC();
				TbfFCOMPBRANC.setModel(rs, rec1);
				return rec1;
			} else {
				return null;
			}
		}
		

	}
	
	public static String getBrancAddressNoZipCode(String comp_cde, int branc_cde) throws Exception {
		String ret = "";
		try (FDbc dbc = FDbc.connectMasterDb()) {
			
			TboFCOMPBRANC branc = getBranc(dbc, comp_cde, branc_cde);
			if (branc != null) {
				ret += branc.getADDR1();
				ret += " " + branc.getADDR2();
			}
		}
		return ret;
	}

}

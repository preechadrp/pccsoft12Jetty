package com.pcc.tk.tbm;

import com.pcc.sys.lib.FDbc;

public class TbmTKJOBLAWYER {

	public static int maxSeq(FDbc dbc, String comp_cde, String jobno) throws Exception {
		int ret = 0;

		String sql = " select max(SEQ) as F1 from tkjoblawyer where COMP_CDE =? and JOBNO =? ";
		try (java.sql.ResultSet rs = dbc.getResultSetFw2(sql, comp_cde, jobno)) {
			if (rs.next()) {
				ret = rs.getInt("F1");
			}
		}

		return ret;
	}

	
}

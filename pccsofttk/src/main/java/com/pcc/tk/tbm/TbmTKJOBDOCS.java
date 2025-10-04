package com.pcc.tk.tbm;

import java.util.List;

import com.pcc.sys.lib.FDbc;
import com.pcc.tk.tbf.TbfTKJOBDOCS;
import com.pcc.tk.tbo.TboTKJOBDOCS;

public class TbmTKJOBDOCS {

	public static void getDataByJobno(String comp_cde, String jobno, List<TboTKJOBDOCS> lst_data) throws Exception {

		lst_data.clear();

		String sql = " select * from tkjobdocs where COMP_CDE=? and JOBNO=? order by DOCSEQ ";
		try (FDbc dbc = FDbc.connectMasterDb();
				java.sql.ResultSet rs = dbc.getResultSetFw2(sql, comp_cde, jobno)) {

			while (rs.next()) {
				TboTKJOBDOCS rec = new TboTKJOBDOCS();
				TbfTKJOBDOCS.setModel(rs, rec);
				lst_data.add(rec);
			}

		}

	}

	public static int getMax_DOCSEQ(String comp_cde, String jobno) throws Exception {
		int ret = 0;

		String sql = " select max(DOCSEQ) as F1 from tkjobdocs where COMP_CDE=? and JOBNO=? ";
		try (FDbc dbc = FDbc.connectMasterDb();
				java.sql.ResultSet rs = dbc.getResultSetFw2(sql, comp_cde, jobno)) {

			while (rs.next()) {
				ret = rs.getInt(1);
			}

		}

		return ret;
	}

}

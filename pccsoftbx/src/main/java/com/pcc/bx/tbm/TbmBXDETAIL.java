package com.pcc.bx.tbm;

import java.util.List;

import com.pcc.bx.tbf.TbfBXDETAIL;
import com.pcc.bx.tbo.TboBXDETAIL;
import com.pcc.sys.lib.FDbc;

public class TbmBXDETAIL {

	public static void getData(String comp_cde, String blno, List<TboBXDETAIL> lst_bxdetail) throws Exception {

		lst_bxdetail.clear();
		String sql = "select * from bxdetail where COMP_CDE =? and BLNO =? order by BLNO ,SEQ1 ";
		try (FDbc dbc = FDbc.connectMasterDb();
				java.sql.ResultSet rs = dbc.getResultSetFw2(sql, comp_cde, blno);) {
			
			while (rs.next()) {
				TboBXDETAIL rec = new TboBXDETAIL();
				TbfBXDETAIL.setModel(rs, rec);
				
				lst_bxdetail.add(rec);
			}
			
		}
	}

	public static int getMaxCode(FDbc dbc, String comp_cde, String blno) throws Exception {
		int ret = 0;
		String sql = "select max(seq1) as F1 from bxdetail where COMP_CDE =? and BLNO =?";
		try (java.sql.ResultSet rs1 = dbc.getResultSetFw2(sql, comp_cde, blno);) {
			if (rs1.next()) {
				ret = rs1.getInt(1);
			}
		}

		return ret;
	}

}

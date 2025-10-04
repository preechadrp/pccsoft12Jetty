package com.pcc.gl.tbm;

import java.sql.SQLException;

import com.pcc.gl.tbf.TbfACCT_NO;
import com.pcc.gl.tbo.TboACCT_NO;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.SqlStr;

public class TbmACCT_NO {

	public static void getData(java.util.List<FModelHasMap> dats, String acct_id, String acct_name,
			LoginBean _loginBean) throws Exception {

		dats.clear();
		try (FDbc dbc = FDbc.connectMasterDb()) {
			SqlStr sql = new SqlStr();
			sql.addLine("select * from " + TboACCT_NO.tablename);
			sql.addLine(" where COMP_CDE = '" + Fnc.sqlQuote(_loginBean.getCOMP_CDE()) + "' ");
			if (!Fnc.isEmpty(acct_id)) {
				sql.addLine(" and ACCT_ID  like '" + Fnc.sqlQuote(acct_id) + "%' ");
			}
			if (!Fnc.isEmpty(acct_name)) {
				sql.addLine(" and ACCT_NAME  like '%" + Fnc.sqlQuote(acct_name) + "%' ");
			}
			sql.addLine(" order by ACCT_ID");

			System.out.println(sql.getSql());
			try (java.sql.ResultSet rs = dbc.getResultSetFw3(sql.getSql(), 1000);) {
				while (rs.next()) {
					FModelHasMap dat = new FModelHasMap();
					dat.putRecord(rs);
					dats.add(dat);
				}
			}
		}

	}

	public static String getAcctName(String comp_cde, String acct_id) throws SQLException, Exception {
		String ret = "";

		TboACCT_NO table1 = new TboACCT_NO();

		table1.setCOMP_CDE(comp_cde);
		table1.setACCT_ID(acct_id);

		if (TbfACCT_NO.find(table1)) {
			ret = table1.getACCT_NAME();
		}

		return ret;
	}

}

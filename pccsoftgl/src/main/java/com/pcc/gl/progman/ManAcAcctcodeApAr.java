package com.pcc.gl.progman;

import com.pcc.gl.tbo.TboACCT_NO;
import com.pcc.gl.tbo.TboACCT_NO_SUB;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.SqlStr;

public class ManAcAcctcodeApAr {

	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static void getDataQry(java.util.List<FModelHasMap> lst_dat, String acctid, String acct_name, int sub_type,
			LoginBean _loginBean) throws Exception {

		lst_dat.clear();

		try (FDbc dbc = FDbc.connectMasterDb()) {

			SqlStr sql = new SqlStr();
			sql.addLine("select aa.* ,bb.ACCT_NAME,cc.ACCT_NAME as ACCT_NAME_BANK");
			sql.addLine("from " + TboACCT_NO_SUB.tablename + " aa");
			sql.addLine("left join " + TboACCT_NO.tablename + " bb on aa.COMP_CDE=bb.COMP_CDE and aa.ACCT_ID=bb.ACCT_ID");
			sql.addLine("left join " + TboACCT_NO.tablename + " cc on aa.COMP_CDE=cc.COMP_CDE and aa.ACCT_ID_BANK=cc.ACCT_ID");
			sql.addLine("where aa.COMP_CDE = '" + Fnc.sqlQuote(_loginBean.getCOMP_CDE()) + "' ");

			if (!Fnc.isEmpty(acctid)) {
				sql.addLine("and aa.ACCT_ID like '%" + Fnc.sqlQuote(acctid) + "%' ");
			}

			if (!Fnc.isEmpty(acct_name)) {
				sql.addLine("and bb.ACCT_NAME like '%" + Fnc.sqlQuote(acct_name) + "%' ");
			}

			if (sub_type != 0) {
				sql.addLine("and aa.SUB_TYPE=" + sub_type);
			}

			sql.addLine("order by aa.COMP_CDE,aa.ACCT_ID");

			logger.info(sql.getSql());

			try (java.sql.ResultSet rs = dbc.getResultSetFw3(sql.getSql(), 4000);) {
				while (rs.next()) {

					FModelHasMap dat = new FModelHasMap();
					dat.putRecord(rs);
					lst_dat.add(dat);

				}
			}

		}

	}

}

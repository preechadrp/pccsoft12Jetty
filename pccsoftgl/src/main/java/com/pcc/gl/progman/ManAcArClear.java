package com.pcc.gl.progman;

import java.util.List;


import com.pcc.gl.tbo.TboACGL_AR;
import com.pcc.gl.tbo.TboFCUS;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.SqlStr;

public class ManAcArClear {

	public static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static void getDataQry(List<FModelHasMap> dats, String vou_type, String vou_no, String docno,
			LoginBean loginBean) throws Exception {

		dats.clear();
		try (FDbc dbc = FDbc.connectMasterDb()) {

			SqlStr sql = new SqlStr();
			sql.addLine("select ");
			sql.addLine(" cc.COMP_CDE, cc.VOU_TYPE, cc.VOU_NO, cc.VOU_SEQ, cc.VOU_DSEQ,");
			sql.addLine(" cc.POSTDATE, cc.DOCNO, cc.DOCDATE, cc.CUST_CDE, cc.ACCT_ID, ");
			sql.addLine(" cc.LINK_NO, cc.NUM_TYPE, coalesce(cc.AMT,0) as AMT, cc.RECSTA,");
			sql.addLine(" cc.INSBY, cc.INSDT,");
			sql.addLine(" cc.DUEDATE, cc.DESCR, bb.TITLE, bb.FNAME, bb.LNAME");
			sql.addLine("from " + TboACGL_AR.tablename + " cc");
			sql.addLine(
					"left join " + TboFCUS.tablename + " bb on cc.COMP_CDE=bb.COMP_CDE and cc.CUST_CDE=bb.CUST_CDE");
			sql.addLine("where cc.COMP_CDE='" + Fnc.sqlQuote(loginBean.getCOMP_CDE()) + "' ");
			sql.addLine("and cc.POST_TYPE=1");
			if (!Fnc.isEmpty(vou_type)) {
				sql.addLine("and cc.VOU_TYPE='" + Fnc.sqlQuote(vou_type) + "' ");
			}
			if (!Fnc.isEmpty(vou_no)) {
				sql.addLine("and cc.VOU_NO like '" + Fnc.sqlQuote(vou_no.trim()) + "%' ");
			}
			if (!Fnc.isEmpty(docno)) {
				sql.addLine("and cc.DOCNO like '" + Fnc.sqlQuote(docno.trim()) + "%' ");
			}
			sql.addLine("order by cc.POSTDATE, cc.VOU_TYPE, cc.VOU_NO, cc.VOU_SEQ");

			logger.info(sql.getSql());
			try (java.sql.ResultSet rs = dbc.getResultSetFw3(sql.getSql(), 5000);) {
				while (rs.next()) {
					FModelHasMap dat = new FModelHasMap();
					dat.putRecord(rs);
					dats.add(dat);
				}
			}

		}

	}

	public static void getDataQryByLinkNo(List<FModelHasMap> dats, String link_no, LoginBean loginBean)
			throws Exception {

		dats.clear();
		try (FDbc dbc = FDbc.connectMasterDb()) {

			SqlStr sql = new SqlStr();
			sql.addLine("select ");
			sql.addLine(" cc.COMP_CDE, cc.VOU_TYPE, cc.VOU_NO, cc.VOU_SEQ, cc.VOU_DSEQ,");
			sql.addLine(" cc.POSTDATE, cc.DOCNO, cc.DOCDATE, cc.CUST_CDE, cc.ACCT_ID, ");
			sql.addLine(" cc.LINK_NO, cc.NUM_TYPE, coalesce(cc.AMT,0) as AMT, cc.RECSTA,");
			sql.addLine(" cc.INSBY, cc.INSDT, cc.POST_TYPE,");
			sql.addLine(" cc.DUEDATE, cc.DESCR, bb.TITLE, bb.FNAME, bb.LNAME");
			sql.addLine("from " + TboACGL_AR.tablename + " cc");
			sql.addLine("left join " + TboFCUS.tablename + " bb on cc.COMP_CDE=bb.COMP_CDE and cc.CUST_CDE=bb.CUST_CDE");
			sql.addLine("where cc.COMP_CDE = '" + Fnc.sqlQuote(loginBean.getCOMP_CDE()) + "' ");
			sql.addLine("and cc.LINK_NO = '" + Fnc.sqlQuote(link_no.trim()) + "' ");
			sql.addLine("order by cc.POST_TYPE, cc.POSTDATE, cc.VOU_TYPE, cc.VOU_NO, cc.VOU_SEQ");

			logger.info(sql.getSql());
			try (java.sql.ResultSet rs = dbc.getResultSetFw3(sql.getSql(), 5000);) {
				while (rs.next()) {
					FModelHasMap dat = new FModelHasMap();
					dat.putRecord(rs);
					dats.add(dat);
				}
			}

		}

	}

}
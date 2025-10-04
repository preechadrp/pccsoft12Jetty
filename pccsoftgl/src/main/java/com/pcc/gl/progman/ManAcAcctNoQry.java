package com.pcc.gl.progman;

import java.util.HashMap;
import java.util.Map;

import com.pcc.gl.tbo.TboACCT_NO;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FJasperPrintList;
import com.pcc.sys.lib.FReport;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.SqlStr;

public class ManAcAcctNoQry {

	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static void getReport(LoginBean _loginBean, FJasperPrintList fJasperPrintList, int print_option)
			throws Exception {

		Map reportParams = new HashMap();

		try (FDbc dbc = FDbc.connectMasterDb()) {

			reportParams.put("compName", _loginBean.getTboFcomp().getCOMP_NAME());
			reportParams.put("userName", _loginBean.getTboFuser().getTITLE() + " "
					+ _loginBean.getTboFuser().getFNAME_THAI() + " " + _loginBean.getTboFuser().getLNAME_THAI());
			reportParams.put("reportName", "ผังบัญชี");

			SqlStr sql = new SqlStr();
			sql.addLine("select aa.*");
			sql.addLine("from " + TboACCT_NO.tablename + " aa");
			sql.addLine("where 1=1");
			sql.addLine("and aa.comp_cde= '" + Fnc.sqlQuote(_loginBean.getCOMP_CDE()) + "' ");
			sql.addLine("order by aa.comp_cde, aa.acct_id");

			logger.info(sql.getSql());
			try (java.sql.ResultSet rs = dbc.getResultSet(sql.getSql());) {
				if (rs.next()) {
					rs.beforeFirst();
					if (print_option == 1) {// pdf
						fJasperPrintList.addJasperPrintList(FReport.builtRepByResultSet(
								"/com/pcc/gl/jasper/AcAcctNoQry/AcAcctNoQry.jasper", reportParams, rs));
					} else {// excel
						fJasperPrintList.addJasperPrintList(FReport.builtRepByResultSet(
								"/com/pcc/gl/jasper/AcAcctNoQry/AcAcctNoQry_Excel.jasper", reportParams, rs));
					}
				}
			}

		}
	}

}

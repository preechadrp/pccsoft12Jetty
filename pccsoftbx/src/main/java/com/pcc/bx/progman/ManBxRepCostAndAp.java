package com.pcc.bx.progman;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.pcc.api.core.ApiUtil;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FJRBeanCollectionDataSource;
import com.pcc.sys.lib.FJasperPrintList;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FReport;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.SqlStr;

public class ManBxRepCostAndAp {

	public static String[] getReportService(LoginBean _loginBean, JSONObject requestpara) throws Exception {
		
		FJasperPrintList fJasperPrintList = new FJasperPrintList();

		getReport(_loginBean, requestpara, fJasperPrintList);

		if (fJasperPrintList.getJasperPrintLst().size() > 0) {

			int print_option = ApiUtil.getInt(requestpara, "print_option");

			if (print_option == 1) {
				return FReport.exportBase64_of_PDF(fJasperPrintList.getJasperPrintLst());
			} else {
				return FReport.exportBase64_of_EXCEL(fJasperPrintList.getJasperPrintLst());
			}
		} else {
			throw new Exception("ไม่พบข้อมูล");
		}
		
	}

	private static void getReport(LoginBean _loginBean, JSONObject requestpara, FJasperPrintList fJasperPrintList) throws Exception {

		try (FDbc dbc = FDbc.connectMasterDb()) {

			Map reportParams = new HashMap();

			reportParams.put("compName", _loginBean.getCOMP_NAME());
			reportParams.put("userName", _loginBean.getUserFullName());

			reportParams.put("reportName", "รายงานกำไรขั้นต้น");
			reportParams.put("reportConditionString1", requestpara.getString("reportCondition"));

			FJRBeanCollectionDataSource reportDataSource = createDataSource(dbc, _loginBean, requestpara);

			if (reportDataSource != null) {

				int print_option = ApiUtil.getInt(requestpara, "print_option");

				if (print_option == 1) {// pdf ยังไม่ทำ
					//fJasperPrintList.addJasperPrintList(FReport.builtRepByBean("/com/pcc/bx/jasper/FrmBxRepCostAndAp/FrmBxRepCostAndAp.jrxml", 
					// reportParams, reportDataSource)); 
				} else {// excel
					fJasperPrintList.addJasperPrintList(FReport.builtRepByBean("/com/pcc/bx/jasper/FrmBxRepCostAndAp/FrmBxRepCostAndAp_Excel.jrxml",
							reportParams, reportDataSource));
				}

			}

		}
		
	}
	
	public static FJRBeanCollectionDataSource createDataSource(FDbc dbc, LoginBean _loginBean, JSONObject paras) throws SQLException, Exception {

		SqlStr sql = new SqlStr();
		sql.addLine("SELECT aa.*,bb.POSTCOST ");
		sql.addLine("FROM BXHEADER aa");
		sql.addLine("LEFT JOIN BXTMPLHEAD bb ON aa.COMP_CDE =bb.COMP_CDE AND aa.TMPLCDE =bb.TMPLCDE ");
		sql.addLine("WHERE aa.COMP_CDE ='" + Fnc.sqlQuote(_loginBean.getCOMP_CDE()) + "' ");
		sql.addLine("AND bb.POSTCOST ='Y' ");
		sql.addLine("AND aa.RECSTA = 1");
		
		if (paras.has("tdbBLDATE_FROM")) {
			sql.addLine("and aa.BLDATE >='" + ApiUtil.getSqlDate(paras, "tdbBLDATE_FROM") + "' ");
		}
		if (paras.has("tdbBLDATE_TO")) {
			sql.addLine("and aa.BLDATE <='" + ApiUtil.getSqlDate(paras, "tdbBLDATE_TO") + "' ");
		}

		sql.addLine("ORDER BY aa.BLNO ,aa.BLDATE");
		
		java.util.List<FModelHasMap> list_dat = new ArrayList<FModelHasMap>();
		try (java.sql.ResultSet rs = dbc.getResultSet(sql.getSql());) {
			
			while (rs.next()) {
				FModelHasMap dat = new FModelHasMap();
				list_dat.add(dat);
				dat.putRecord(rs);
				
				String cust_name = Fnc.getStr(rs.getString("CUST_TITLE")) + " " + 
						Fnc.getStr(rs.getString("CUST_FNAME")) + " " + Fnc.getStr(rs.getString("CUST_LNAME"));
				dat.put("CUST_NAME", cust_name.trim());
			}
		}

		if (list_dat.size() > 0) {
			return new FJRBeanCollectionDataSource(list_dat);
		} else {
			return null;
		}

	}

}

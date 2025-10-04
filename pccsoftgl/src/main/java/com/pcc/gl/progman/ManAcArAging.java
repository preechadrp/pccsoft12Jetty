package com.pcc.gl.progman;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import com.pcc.gl.tbo.TboACCT_NO;
import com.pcc.gl.tbo.TboACGL_AR;
import com.pcc.gl.tbo.TboFCUS;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.DynamicRepTemplates;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FJRBeanCollectionDataSource;
import com.pcc.sys.lib.FJasperPrintList;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.SqlStr;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.group.CustomGroupBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.jasperreports.engine.JRDataSource;

public class ManAcArAging {

	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static void getReport(LoginBean _loginBean, java.sql.Date toPostdate, String cust_cde, String fromAcctid,
			FJasperPrintList fJasperPrintList, int print_option) throws Exception {

		try (FDbc dbc = FDbc.connectMasterDb()) {

			Map reportParams = new HashMap();
			reportParams.put("reportName", "รายงานอายุลูกหนี้");
			String reportConditionString1 = "";
			if (toPostdate != null) {
				reportConditionString1 += "สิ้นสุด ณ วันที่ " + FnDate.displayDateString(toPostdate);
			}
			reportParams.put("reportConditionString1", reportConditionString1);

			FJRBeanCollectionDataSource reportDataSource = createDataSource(dbc, _loginBean, toPostdate, cust_cde,
					fromAcctid, print_option);
			if (reportDataSource != null) {
				if (print_option == 1) {// pdf
					fJasperPrintList.addJasperPrintList(
							genReportPDF(reportDataSource, reportParams, _loginBean).toJasperPrint());
				} else {// excel
					fJasperPrintList.addJasperPrintList(
							genReportExcel(reportDataSource, reportParams, _loginBean).toJasperPrint());
				}
			}

		}

	}

	public static FJRBeanCollectionDataSource createDataSource(FDbc dbc, LoginBean _loginBean, java.sql.Date toPostdate,
			String cust_cde, String fromAcctid, int print_option) throws SQLException, Exception {

		SqlStr sql = new SqlStr();
		sql.addLine("select aa1.*");
		sql.addLine(" , aa2.POSTDATE, aa2.ACCT_ID, aa2.SECT_ID, aa2.DESCR , aa2.DOCDATE, bb2.ACCT_NAME");
		sql.addLine(" , cus.TITLE, cus.FNAME, cus.LNAME");
		sql.addLine("from");
		sql.addLine("(");
		sql.addLine(" select COMP_CDE, CUST_CDE, LINK_NO, DOCNO, SUM(COALESCE(NUM_TYPE,0) * COALESCE(AMT,0)) as AMT");
		sql.addLine(" from " + TboACGL_AR.tablename);
		sql.addLine(" where 1=1");
		sql.addLine(" and COMP_CDE = '" + Fnc.sqlQuote(_loginBean.getCOMP_CDE()) + "' ");
		if (!Fnc.isEmpty(cust_cde)) {
			sql.addLine(" and CUST_CDE = '" + Fnc.sqlQuote(cust_cde) + "' ");
		}
		if (!Fnc.isEmpty(fromAcctid)) {
			sql.addLine(" and ACCT_ID = '" + Fnc.sqlQuote(fromAcctid) + "' ");
		}
		sql.addLine(" and POSTDATE <= '" + toPostdate + "' and RECSTA = 2 ");
		sql.addLine(" group by COMP_CDE, CUST_CDE, LINK_NO, DOCNO");
		sql.addLine(" having SUM(COALESCE(NUM_TYPE,0) * COALESCE(AMT,0)) != 0 ");
		sql.addLine(") aa1");
		sql.addLine("left join " + TboACGL_AR.tablename + " aa2 on aa1.COMP_CDE=aa2.COMP_CDE and aa1.CUST_CDE=aa2.CUST_CDE and aa1.LINK_NO=aa2.LINK_NO and aa1.DOCNO=aa2.DOCNO and aa2.POST_TYPE=1 ");
		sql.addLine("left join " + TboACCT_NO.tablename + " bb2 on aa2.COMP_CDE=bb2.COMP_CDE and aa2.ACCT_ID=bb2.ACCT_ID");
		sql.addLine("left join " + TboFCUS.tablename
				+ " cus on aa2.COMP_CDE=cus.COMP_CDE and aa2.CUST_CDE=cus.CUST_CDE and COALESCE(aa2.CUST_CDE,'') != '' ");
		sql.addLine("order by aa1.COMP_CDE, aa1.CUST_CDE, aa2.POSTDATE ,aa2.ACCT_ID");

		logger.info(sql.getSql());
		java.util.List<FModelHasMap> list_dat = new ArrayList<FModelHasMap>();

		try (java.sql.ResultSet rs = dbc.getResultSet(sql.getSql());) {

			while (rs.next()) {

				FModelHasMap dat = new FModelHasMap();
				// ใช้ dat.putRecord ไม่ได้สำหรับ sql แบบนี้
				dat.setString("COMP_CDE", rs.getString("COMP_CDE"));
				dat.setString("CUST_CDE", rs.getString("CUST_CDE"));
				dat.setString("TITLE", rs.getString("TITLE"));
				dat.setString("FNAME", rs.getString("FNAME"));
				dat.setString("LNAME", rs.getString("LNAME"));
				dat.setString("ACCT_ID", rs.getString("ACCT_ID"));
				dat.setString("DOCNO", rs.getString("DOCNO"));
				dat.setDate("POSTDATE", rs.getDate("POSTDATE"));
				dat.setDate("DOCDATE", rs.getDate("DOCDATE"));
				dat.setString("SECT_ID", rs.getString("SECT_ID"));
				dat.setString("DESCR", rs.getString("DESCR"));
				dat.setString("ACCT_NAME", rs.getString("ACCT_NAME"));
				dat.setBigDecimal("AMT", rs.getBigDecimal("AMT"));

				// คำนวนอายุ
				int aging = FnDate.subDate(rs.getDate("POSTDATE"), toPostdate);
				if (aging < 0) {
					aging = 0;
				}
				dat.setInt("F_AGING", aging);

				list_dat.add(dat);

			}
		}

		logger.info("end putRecord");

		if (list_dat.size() > 0) {
			return new FJRBeanCollectionDataSource(list_dat);
		} else {
			return null;
		}

	}

	public static void addFieldToReport(JasperReportBuilder myreport) {
		// กรณีนี้ทำจาก FModelHasMap ให้พิมพ์ชื่อตัวใหญ่ให้ตรงกับตอน ADD
		myreport.addField("COMP_CDE", DynamicReports.type.stringType());
		myreport.addField("CUST_CDE", DynamicReports.type.stringType());
		myreport.addField("TITLE", DynamicReports.type.stringType());
		myreport.addField("FNAME", DynamicReports.type.stringType());
		myreport.addField("LNAME", DynamicReports.type.stringType());
		myreport.addField("ACCT_ID", DynamicReports.type.stringType());
		myreport.addField("DOCNO", DynamicReports.type.stringType());
		myreport.addField("POSTDATE", java.sql.Date.class);
		myreport.addField("DOCDATE", java.sql.Date.class);
		myreport.addField("SECT_ID", DynamicReports.type.stringType());
		myreport.addField("DESCR", DynamicReports.type.stringType());
		myreport.addField("AMT", DynamicReports.type.bigDecimalType());
		myreport.addField("ACCT_NAME", DynamicReports.type.stringType());
		myreport.addField("F_AGING", DynamicReports.type.integerType());

	}

	@SuppressWarnings("serial")
	public static JasperReportBuilder genReportPDF(FJRBeanCollectionDataSource dataSrc, Map reportParams,
			LoginBean _loginBean) {

		StyleBuilder styleDefault = DynamicRepTemplates.getRootStyle();// สำหรับเริ่มต้นค่าใน report นี้

		// Dynamic report
		JasperReportBuilder myreport = DynamicReports.report()
				.setTemplate(DynamicRepTemplates.reportTemplateWithHighlight)
				.setPageFormat(PageType.A4, PageOrientation.LANDSCAPE).setDataSource(dataSrc)
				.columnHeader(DynamicReports.cmp.verticalGap(2));// แก้ปํญหากรณี pdf แสดงสีแถวแบบ highlight
																	// ทำให้ไม่ทับเส้นขอบล่างของ Title Column Header

		// == start เพิ่มฟิลด์
		addFieldToReport(myreport);
		// == end เพิ่มฟิลด์

		// == group varible
		CustomGroupBuilder acctGroup = DynamicReports.grp.group(new AbstractSimpleExpression() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				String s1 = Fnc.getStr(reportParameters.getFieldValue("CUST_CDE"));
				String s2 = Fnc.getStr(reportParameters.getFieldValue("TITLE"));
				String s3 = Fnc.getStr(reportParameters.getFieldValue("FNAME"));
				String s4 = Fnc.getStr(reportParameters.getFieldValue("LNAME"));
				String ret = "รหัสลูกหนี้ " + s1 + " " + s2 + " " + s3 + " " + s4;

				return ret;
			}
		}).setPadding(0);// ทำให้ตรง group header ไม่เยื้องกันระหว่าง detail;
		myreport.groupBy(acctGroup);
		// == end group varible

		// == report varible
		// == end report varible

		// == group Footer
		// == end group Footer

		// == set page header and page footer
		myreport.pageHeader(
				DynamicReports.cmp.horizontalList()
						.add(DynamicReports.cmp.text(_loginBean.getTboFcomp().getCOMP_NAME()).setStyle(styleDefault))
						.newRow()
						.add(DynamicReports.cmp.text(Fnc.getStr(reportParams.get("reportName")) + " "
								+ Fnc.getStr(reportParams.get("reportConditionString1"))).setStyle(styleDefault))
						.newRow()
						.add(DynamicReports.cmp
								.text("ผู้พิมพ์ : " + _loginBean.getUSER_ID() + " วันที่พิมพ์ : "
										+ FnDate.displayDateTimeString(FnDate.getTodayDateTime()))
								.setStyle(styleDefault))
						.newRow().add(DynamicReports.cmp.verticalGap(2)
								.setStyle(DynamicReports.stl.style().setTopBorder(DynamicReports.stl.penThin()))
						// block
						)
		// end block pageHeader
		);

		myreport.pageFooter(DynamicRepTemplates.footerComponentNotBold);
		// == end set page header and page footer

		// == summary
		// == end summary

		// == start เพิ่มคอลัมภ์
		myreport.addColumn(DynamicReports.col.column("ลำดับ", new AbstractSimpleExpression<Integer>() {
			public Integer evaluate(ReportParameters reportParameters) {
				// มาจากตัวแปร ok
				return reportParameters.getReportRowNumber();
			}
		}).setStyle(styleDefault).setFixedWidth(40).setPattern("#,###.")
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));

		myreport.addColumn(DynamicReports.col.column("วันที่", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				java.sql.Date date1 = (java.sql.Date) reportParameters.getFieldValue("POSTDATE");
				return FnDate.displayDateString(date1);
			}
		}).setStyle(styleDefault).setFixedWidth(50));

		myreport.addColumn(DynamicReports.col.column("รหัสบัญชี", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				return Fnc.getStr(reportParameters.getFieldValue("ACCT_ID"));
			}
		}).setStyle(styleDefault).setFixedWidth(50));

		myreport.addColumn(DynamicReports.col.column("ชื่อบัญชี", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				return Fnc.getStr(reportParameters.getFieldValue("ACCT_NAME"));
			}
		}).setStyle(styleDefault).setFixedWidth(200));

		myreport.addColumn(DynamicReports.col.column("จำนวน", new AbstractSimpleExpression<BigDecimal>() {
			@Override
			public BigDecimal evaluate(ReportParameters reportParameters) {
				return (BigDecimal) reportParameters.getFieldValue("AMT");
			}
		}).setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)).setFixedWidth(80).setPattern("#,##0.00")
				.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT));

		myreport.addColumn(DynamicReports.col.column("เลขที่เอกสาร", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				return Fnc.getStr(reportParameters.getFieldValue("DOCNO"));
			}
		}).setStyle(styleDefault).setFixedWidth(100));

		myreport.addColumn(DynamicReports.col.column("วันที่เอกสาร", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				java.sql.Date date1 = (java.sql.Date) reportParameters.getFieldValue("DOCDATE");
				return FnDate.displayDateString(date1);
			}
		}).setStyle(styleDefault).setFixedWidth(60));

		myreport.addColumn(DynamicReports.col.column("คำอธิบาย", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				return Fnc.getStr(reportParameters.getFieldValue("DESCR"));
			}
		}).setStyle(styleDefault));

		myreport.addColumn(DynamicReports.col.column("อายุ/วัน", new AbstractSimpleExpression<Integer>() {
			@Override
			public Integer evaluate(ReportParameters reportParameters) {
				return (Integer) reportParameters.getFieldValue("F_AGING");
			}
		}).setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)).setFixedWidth(50).setPattern("#,##0")
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));

		// == end เพิ่มคอลัมภ์

		return myreport;

	}

	@SuppressWarnings("serial")
	public static JasperReportBuilder genReportExcel(JRDataSource dataSrc, Map reportParams, LoginBean _loginBean) {

		StyleBuilder styleDefault = DynamicRepTemplates.getRootStyle();// สำหรับเริ่มต้นค่าใน report นี้

		JasperReportBuilder myreport = DynamicReports.report()
				.setTemplate(DynamicRepTemplates.reportTemplateNoHighlight)
				.title(DynamicReports.cmp.text(_loginBean.getTboFcomp().getCOMP_NAME()).setStyle(styleDefault),
						DynamicReports.cmp
								.text(Fnc.getStr(reportParams.get("reportName")) + " "
										+ Fnc.getStr(reportParams.get("reportConditionString1")))
								.setStyle(styleDefault),
						DynamicReports.cmp
								.text("ผู้พิมพ์ : " + _loginBean.getUSER_ID() + " วันที่พิมพ์ : "
										+ FnDate.displayDateTimeString(FnDate.getTodayDateTime()))
								.setStyle(styleDefault))
				.setPageFormat(PageType.A3, PageOrientation.LANDSCAPE).setPageMargin(DynamicReports.margin(0))
				.setDataSource(dataSrc).setColumnHeaderPrintWhenExpression(new AbstractSimpleExpression() {
					public Boolean evaluate(ReportParameters reportParameters) {
						if (reportParameters.getPageNumber().intValue() == 1) { // ให้แสดงเฉพาะหน้าที่ 1
							return true;
						} else {
							return false;
						}
					}
				});// ให้แสดงเฉพาะหน้าแรก

		// == start เพิ่มฟิลด์
		addFieldToReport(myreport);
		// == end เพิ่มฟิลด์

		// == group varible
		// == end report varible

		// == group Footer
		// == end group Footer

		// == set page header and page footer
		// == end set page header and page footer

		// == summary
		// == end summary

		// == start เพิ่มคอลัมภ์
		myreport.addColumn(DynamicReports.col.column("ลำดับ", new AbstractSimpleExpression<Integer>() {
			public Integer evaluate(ReportParameters reportParameters) {
				// มาจากตัวแปร ok
				return reportParameters.getReportRowNumber();
			}
		}).setStyle(styleDefault).setFixedWidth(40).setPattern("#,###.")
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));

		myreport.addColumn(DynamicReports.col.column("รหัสลูกหนี้", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				return Fnc.getStr(reportParameters.getFieldValue("CUST_CDE"));
			}
		}).setStyle(styleDefault).setFixedWidth(60));

		myreport.addColumn(DynamicReports.col.column("ชื่อลูกหนี้", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				String s2 = (String) reportParameters.getFieldValue("TITLE");
				String s3 = (String) reportParameters.getFieldValue("FNAME");
				String s4 = (String) reportParameters.getFieldValue("LNAME");
				String ret = s2 + " " + s3 + " " + s4;

				return ret;
			}
		}).setStyle(styleDefault).setFixedWidth(100));

		myreport.addColumn(DynamicReports.col.column("วันที่", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				java.sql.Date date1 = (java.sql.Date) reportParameters.getFieldValue("POSTDATE");
				return FnDate.displayDateString(date1);
			}
		}).setStyle(styleDefault).setFixedWidth(50));

		myreport.addColumn(DynamicReports.col.column("รหัสบัญชี", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				return Fnc.getStr(reportParameters.getFieldValue("ACCT_ID"));
			}
		}).setStyle(styleDefault).setFixedWidth(50));

		myreport.addColumn(DynamicReports.col.column("ชื่อบัญชี", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				return Fnc.getStr(reportParameters.getFieldValue("ACCT_NAME"));
			}
		}).setStyle(styleDefault).setFixedWidth(200));

		myreport.addColumn(DynamicReports.col.column("จำนวน", new AbstractSimpleExpression<BigDecimal>() {
			@Override
			public BigDecimal evaluate(ReportParameters reportParameters) {
				return (BigDecimal) reportParameters.getFieldValue("AMT");
			}
		}).setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)).setFixedWidth(80).setPattern("#,##0.00")
				.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT));

		myreport.addColumn(DynamicReports.col.column("เลขที่เอกสาร", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				return Fnc.getStr(reportParameters.getFieldValue("DOCNO"));
			}
		}).setStyle(styleDefault).setFixedWidth(100));

		myreport.addColumn(DynamicReports.col.column("วันที่เอกสาร", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				java.sql.Date date1 = (java.sql.Date) reportParameters.getFieldValue("DOCDATE");
				return FnDate.displayDateString(date1);
			}
		}).setStyle(styleDefault).setFixedWidth(60));

		myreport.addColumn(DynamicReports.col.column("คำอธิบาย", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				return Fnc.getStr(reportParameters.getFieldValue("DESCR"));
			}
		}).setStyle(styleDefault));

		myreport.addColumn(DynamicReports.col.column("อายุ/วัน", new AbstractSimpleExpression<Integer>() {
			@Override
			public Integer evaluate(ReportParameters reportParameters) {
				return (Integer) reportParameters.getFieldValue("F_AGING");
			}
		}).setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)).setFixedWidth(50).setPattern("#,##0")
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));

		// == end เพิ่มคอลัมภ์

		return myreport;

	}

}
package com.pcc.gl.progman;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.pcc.api.core.ApiUtil;
import com.pcc.gl.tbm.TbmACCT_VOU_TYPE;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.DynamicRepTemplates;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FJasperPrintList;
import com.pcc.sys.lib.FReport;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.SqlStr;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.expression.AbstractComplexExpression;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRResultSetDataSource;

public class ManAcGlbook {

	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static String[] getReportService(LoginBean _loginBean, JSONObject requestpara) throws Exception {
		
		//=== get parameter
		String vou_type = requestpara.getString("vou_type");
		java.sql.Date fromPostdate = ApiUtil.getSqlDate(requestpara, "fromPostdate");
		java.sql.Date toPostdate = ApiUtil.getSqlDate(requestpara, "toPostdate");
		int print_option = ApiUtil.getInt(requestpara, "print_option");
		
		FJasperPrintList fJasperPrintList = new FJasperPrintList();
		getReport(_loginBean, vou_type, fromPostdate, toPostdate, fJasperPrintList, print_option);
		if (fJasperPrintList.getJasperPrintLst().size() > 0) {
			if (print_option == 1) {
				return FReport.exportBase64_of_PDF(fJasperPrintList.getJasperPrintLst());
			} else {
				return FReport.exportBase64_of_EXCEL(fJasperPrintList.getJasperPrintLst());
			}
		} else {
			throw new Exception("ไม่พบข้อมูล");
		}
		
	}
	
	public static void getReport(LoginBean _loginBean, String vou_type, java.sql.Date fromPostdate, java.sql.Date toPostdate,
			FJasperPrintList fJasperPrintList, int print_option) throws Exception {

		try (FDbc dbc = FDbc.connectMasterDb()) {

			Map reportParams = new HashMap();

			reportParams.put("_loginBean", _loginBean);
			reportParams.put("reportName", "รายงานสมุดรายวัน");
			
			String reportConditionString1 = "";
			if (!Fnc.isEmpty(vou_type)) {
				reportConditionString1 = TbmACCT_VOU_TYPE.getVou_type_name(dbc, vou_type, _loginBean) +
						", จากวันที่ " + FnDate.displayDateString(fromPostdate) + " ถึง " + FnDate.displayDateString(toPostdate);
			} else {
				reportConditionString1 = "จากวันที่ " + FnDate.displayDateString(fromPostdate) + " ถึง " + FnDate.displayDateString(toPostdate);
			}
			reportParams.put("reportConditionString1", reportConditionString1);

			SqlStr sql = new SqlStr();
			sql.addLine("""
			select dd.comp_cde, dd.vou_type, dd.vou_no, dd.vou_seq_show, dd.postdate
			, dd.num_type ,dd.acct_id, dd.amt, dd.descr, dd.descr_sub, att.acct_name
			from acgl_detail dd
			left join acct_no att on dd.COMP_CDE=att.COMP_CDE and dd.acct_id=att.acct_id
			""");
			sql.addLine("where dd.comp_cde='" + Fnc.sqlQuote(_loginBean.getCOMP_CDE()) + "' and dd.recsta = 2 ");
			sql.addLine("and coalesce(dd.chq_type,'') != '2' ");
			if (!Fnc.isEmpty(vou_type)) {
				sql.addLine("and dd.vou_type='" + Fnc.sqlQuote(vou_type) + "' ");
			}
			sql.addLine("and dd.postdate between '" + fromPostdate + "' and '" + toPostdate + "' ");
			sql.addLine("order by dd.postdate, dd.comp_cde, dd.vou_type, dd.vou_no,dd.vou_seq_show");

			logger.info(sql.getSql());
			try (java.sql.ResultSet rs = dbc.getResultSet(sql.getSql());) {
				if (rs.next()) {
					rs.beforeFirst();
					JRDataSource reportDataSource = new JRResultSetDataSource(rs);
					if (print_option == 1) {// pdf
						fJasperPrintList.addJasperPrintList(genReportPDF(reportDataSource, reportParams, _loginBean).toJasperPrint());//แก้เป็น jrxml
					} else {// excel
						fJasperPrintList.addJasperPrintList(genReportExcel(reportDataSource, reportParams, _loginBean).toJasperPrint());
					}
				}
			}

		}
	}

	@SuppressWarnings("serial")
	public static JasperReportBuilder genReportPDF(JRDataSource dataSrc, Map reportParams, LoginBean _loginBean) {

		StyleBuilder styleDefault = DynamicRepTemplates.getRootStyle();// สำหรับเริ่มต้นค่าใน report นี้

		// Dynamic report
		JasperReportBuilder myreport = DynamicReports.report()
				.setTemplate(DynamicRepTemplates.reportTemplateWithHighlight)
				.setPageFormat(PageType.A4, PageOrientation.LANDSCAPE).setDataSource(dataSrc)
				.columnHeader(DynamicReports.cmp.verticalGap(2));// แก้ปํญหากรณี pdf แสดงสีแถวแบบ highlight ทำให้ไม่ทับเส้นขอบล่างของ Title Column Header

		ComponentBuilder<?, ?> cmpHead = DynamicReports.cmp.horizontalList()
				.add(DynamicReports.cmp.text(_loginBean.getTboFcomp().getCOMP_NAME()).setStyle(styleDefault)).newRow()
				.add(DynamicReports.cmp.text(Fnc.getStr(reportParams.get("reportName")) + " "
						+ Fnc.getStr(reportParams.get("reportConditionString1"))).setStyle(styleDefault))
				.newRow()
				.add(DynamicReports.cmp.text("ผู้พิมพ์ : " + _loginBean.getUSER_ID() + " วันที่พิมพ์ : "
						+ FnDate.displayDateTimeString(FnDate.getTodayDateTime())).setStyle(styleDefault))
				.newRow().add(DynamicReports.cmp.verticalGap(2)
						.setStyle(DynamicReports.stl.style().setTopBorder(DynamicReports.stl.penThin())));
		myreport.pageHeader(cmpHead);

		myreport.pageFooter(DynamicRepTemplates.footerComponentNotBold);

		// == start เพิ่มฟิลด์
		addFieldToReport(myreport);
		// == end เพิ่มฟิลด์

		// == start เพิ่มคอลัมภ์
		myreport.addColumn(DynamicReports.col.column("ลำดับ", new AbstractComplexExpression<Integer>() {
			@Override
			public Integer evaluate(java.util.List<?> values, ReportParameters reportParameters) {
				return reportParameters.getReportRowNumber();
			}
		}).setStyle(styleDefault).setFixedWidth(40) // setFixedWidth(40) มีหน่วยเป็น pixcel , setWidth(40) มีหน่วยเป็น %
				.setPattern("#,##0.").setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));

		myreport.addColumn(DynamicReports.col.column("วันที่สมุดรายวัน", new AbstractComplexExpression<String>() {
			@Override
			public String evaluate(java.util.List<?> values, ReportParameters reportParameters) {
				java.util.Date date1 = (java.util.Date) reportParameters.getFieldValue("postdate");
				return FnDate.displayDateString(date1);
			}
		}).setStyle(styleDefault).setFixedWidth(50));

		myreport.addColumn(DynamicReports.col.column("สมุดรายวัน", new AbstractComplexExpression<String>() {
			@Override
			public String evaluate(java.util.List<?> values, ReportParameters reportParameters) {
				String s1 = Fnc.getStr(reportParameters.getFieldValue("vou_type"));
				String s2 = Fnc.getStr(reportParameters.getFieldValue("vou_no"));
				return s1 + s2;
			}
		}).setStyle(styleDefault).setFixedWidth(70));

		myreport.addColumn(DynamicReports.col.column("รหัสบัญชี", "acct_id", DynamicReports.type.stringType())
				.setStyle(styleDefault).setFixedWidth(55));

		myreport.addColumn(DynamicReports.col.column("ชื่อบัญชี", "acct_name", DynamicReports.type.stringType())
				.setStyle(styleDefault).setFixedWidth(200));

		myreport.addColumn(DynamicReports.col.column("เดบิต", new AbstractSimpleExpression<BigDecimal>() {
			@Override
			public BigDecimal evaluate(ReportParameters reportParameters) {

				BigDecimal mm = (BigDecimal) reportParameters.getValue("num_type");
				if (mm.intValue() > 0) {
					return reportParameters.getValue("amt");
				} else {
					return null;
				}

			}
		}).setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)).setFixedWidth(70).setPattern("#,##0.00")
				.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT));

		myreport.addColumn(DynamicReports.col.column("เครบิต", new AbstractSimpleExpression<BigDecimal>() {
			@Override
			public BigDecimal evaluate(ReportParameters reportParameters) {

				BigDecimal mm = (BigDecimal) reportParameters.getValue("num_type");
				if (mm.intValue() < 0) {
					return reportParameters.getValue("amt");
				} else {
					return null;
				}

			}
		}).setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)).setFixedWidth(70).setPattern("#,##0.00")
				.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT));

		myreport.addColumn(DynamicReports.col.column("คำอธิบายรายการ", new AbstractComplexExpression<String>() {
			@Override
			public String evaluate(java.util.List<?> values, ReportParameters reportParameters) {
				return Fnc.getStr(reportParameters.getFieldValue("descr")) + " "
						+ Fnc.getStr(reportParameters.getFieldValue("descr_sub"));
			}
		}).setStyle(styleDefault));
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
				.setPageFormat(PageType.A3, PageOrientation.LANDSCAPE).setDataSource(dataSrc)
				.setColumnHeaderPrintWhenExpression(new AbstractSimpleExpression() {
					public Boolean evaluate(ReportParameters reportParameters) {
						if (reportParameters.getPageNumber().intValue() == 1) { // ให้แสดงเฉพาะหน้าที่ 1
							return true;
						} else {
							return false;
						}
					}
				});

		// == start เพิ่มฟิลด์
		addFieldToReport(myreport);
		// == end เพิ่มฟิลด์

		// == start เพิ่มคอลัมภ์
		myreport.addColumn(DynamicReports.col.column("ลำดับ", new AbstractComplexExpression<Integer>() {
			@Override
			public Integer evaluate(java.util.List<?> values, ReportParameters reportParameters) {
				return reportParameters.getReportRowNumber();
			}
		}).setStyle(styleDefault).setFixedWidth(40) // setFixedWidth(40) มีหน่วยเป็น pixcel , setWidth(40) มีหน่วยเป็น %
				.setPattern("#,##0.").setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));

		myreport.addColumn(DynamicReports.col.column("วันที่สมุดรายวัน", new AbstractComplexExpression<String>() {
			@Override
			public String evaluate(java.util.List<?> values, ReportParameters reportParameters) {
				java.util.Date date1 = (java.util.Date) reportParameters.getFieldValue("postdate");
				return FnDate.displayDateString(date1);
			}
		}).setStyle(styleDefault).setFixedWidth(50));

		myreport.addColumn(DynamicReports.col.column("สมุดรายวัน", new AbstractComplexExpression<String>() {
			@Override
			public String evaluate(java.util.List<?> values, ReportParameters reportParameters) {
				String s1 = Fnc.getStr(reportParameters.getFieldValue("vou_type"));
				String s2 = Fnc.getStr(reportParameters.getFieldValue("vou_no"));
				return s1 + s2;
			}
		}).setStyle(styleDefault).setFixedWidth(70));

		myreport.addColumn(DynamicReports.col.column("รหัสบัญชี", "acct_id", DynamicReports.type.stringType())
				.setStyle(styleDefault).setFixedWidth(55));

		myreport.addColumn(DynamicReports.col.column("ชื่อบัญชี", "acct_name", DynamicReports.type.stringType())
				.setStyle(styleDefault).setFixedWidth(250));

		myreport.addColumn(DynamicReports.col.column("เดบิต", new AbstractSimpleExpression<BigDecimal>() {
			@Override
			public BigDecimal evaluate(ReportParameters reportParameters) {

				BigDecimal mm = (BigDecimal) reportParameters.getValue("num_type");
				if (mm.intValue() > 0) {
					return reportParameters.getValue("amt");
				} else {
					return null;
				}

			}
		}).setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)).setFixedWidth(70).setPattern("#,##0.00")
				.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT));

		myreport.addColumn(DynamicReports.col.column("เครบิต", new AbstractSimpleExpression<BigDecimal>() {
			@Override
			public BigDecimal evaluate(ReportParameters reportParameters) {

				BigDecimal mm = (BigDecimal) reportParameters.getValue("num_type");
				if (mm.intValue() < 0) {
					return reportParameters.getValue("amt");
				} else {
					return null;
				}

			}
		}).setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)).setFixedWidth(70).setPattern("#,##0.00")
				.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT));

		myreport.addColumn(DynamicReports.col.column("คำอธิบายรายการ", new AbstractComplexExpression<String>() {
			@Override
			public String evaluate(java.util.List<?> values, ReportParameters reportParameters) {
				return Fnc.getStr(reportParameters.getFieldValue("descr")) + " "
						+ Fnc.getStr(reportParameters.getFieldValue("descr_sub"));
			}
		}).setStyle(styleDefault));
		// == end เพิ่มคอลัมภ์

		return myreport;

	}

	private static void addFieldToReport(JasperReportBuilder myreport) {
		myreport.addField("comp_cde", DynamicReports.type.stringType());
		myreport.addField("vou_type", DynamicReports.type.stringType());
		myreport.addField("vou_no", DynamicReports.type.stringType());
		myreport.addField("postdate", DynamicReports.type.dateType());
		myreport.addField("acct_id", DynamicReports.type.stringType());
		myreport.addField("num_type", DynamicReports.type.bigDecimalType());
		myreport.addField("amt", DynamicReports.type.bigDecimalType());
		myreport.addField("descr", DynamicReports.type.stringType());
		myreport.addField("descr_sub", DynamicReports.type.stringType());
		myreport.addField("acct_name", DynamicReports.type.stringType());
	}

}
package com.pcc.gl.progman;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import com.pcc.gl.tbo.TboACGL_VATSALE;
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
import com.pcc.sys.tbo.TboFSECTION;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.VariableBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.Evaluation;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.jasperreports.engine.JRDataSource;

public class ManAcVatsaleRep {

	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static void getReport(LoginBean _loginBean, java.sql.Date fromPostdate, java.sql.Date toPostdate,
			FJasperPrintList fJasperPrintList, int print_option) throws Exception {

		try (FDbc dbc = FDbc.connectMasterDb()) {

			Map reportParams = new HashMap();
			reportParams.put("reportName", "รายงานภาษีขาย");
			String reportConditionString1 = "";
			if (fromPostdate != null && toPostdate != null) {
				reportConditionString1 += "จากวันที่ " + FnDate.displayDateString(fromPostdate) + " ถึง "
						+ FnDate.displayDateString(toPostdate);
			}
			reportParams.put("reportConditionString1", reportConditionString1);

			FJRBeanCollectionDataSource reportDataSource = createDataSource(dbc, _loginBean, fromPostdate, toPostdate,
					print_option);
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

	public static FJRBeanCollectionDataSource createDataSource(FDbc dbc, LoginBean _loginBean,
			java.sql.Date fromPostdate, java.sql.Date toPostdate, int print_option) throws SQLException, Exception {

		SqlStr sql = new SqlStr();
		sql.addLine("select aa.VOU_TYPE, aa.VOU_NO, aa.POSTDATE, aa.TAX_TYPE, aa.VAT_RATE,");
		sql.addLine("aa.NUM_TYPE*aa.AMT as AMT, aa.NUM_TYPE*aa.BASE_AMT as BASE_AMT,");
		sql.addLine("aa.SECT_ID, aa.DESCR, aa.DOCNO, aa.DOCDATE,");
		sql.addLine("bb.TITLE, bb.FNAME, bb.LNAME, cc.SECT_NAME");
		sql.addLine("from " + TboACGL_VATSALE.tablename + " aa");
		sql.addLine("left join " + TboFCUS.tablename + " bb on aa.COMP_CDE=bb.COMP_CDE and aa.CUST_CDE=bb.CUST_CDE");
		sql.addLine("left join " + TboFSECTION.tablename + " cc on aa.COMP_CDE=cc.COMP_CDE and aa.SECT_ID=cc.SECT_ID");
		sql.addLine("where aa.COMP_CDE='" + Fnc.sqlQuote(_loginBean.getCOMP_CDE())
				+ "' and aa.POST_TYPE=1 and aa.TAX_TYPE=2");
		sql.addLine("and aa.POSTDATE between '" + fromPostdate + "' and '" + toPostdate + "' ");
		sql.addLine("order by aa.VOU_TYPE,aa.VOU_NO, aa.POSTDATE");

		logger.info(sql.getSql());
		java.util.List<FModelHasMap> list_dat = new ArrayList<FModelHasMap>();
		try (java.sql.ResultSet rs = dbc.getResultSet(sql.getSql());) {
			while (rs.next()) {

				FModelHasMap dat = new FModelHasMap();

				dat.setString("VOU_TYPE", rs.getString("VOU_TYPE"));
				dat.setString("VOU_NO", rs.getString("VOU_NO"));
				dat.setDate("POSTDATE", rs.getDate("POSTDATE"));
				dat.setInt("TAX_TYPE", rs.getInt("TAX_TYPE"));
				dat.setBigDecimal("VAT_RATE", rs.getBigDecimal("VAT_RATE"));
				dat.setBigDecimal("AMT", rs.getBigDecimal("AMT"));
				dat.setBigDecimal("BASE_AMT", rs.getBigDecimal("BASE_AMT"));
				dat.setString("SECT_NAME", rs.getString("SECT_NAME"));
				dat.setString("TITLE", rs.getString("TITLE"));
				dat.setString("FNAME", rs.getString("FNAME"));
				dat.setString("LNAME", rs.getString("LNAME"));
				dat.setString("DESCR", rs.getString("DESCR"));
				dat.setString("DOCNO", rs.getString("DOCNO"));
				dat.setDate("DOCDATE", rs.getDate("DOCDATE"));

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
		// == end group varible

		// == report varible
		VariableBuilder<BigDecimal> varSUM_AMT = DynamicReports.variable("varSUM_AMT",
				new AbstractSimpleExpression<BigDecimal>() {
					@Override
					public BigDecimal evaluate(ReportParameters reportParameters) {
						return (BigDecimal) reportParameters.getFieldValue("AMT");
					}
				}, Calculation.SUM);
		varSUM_AMT.setResetType(Evaluation.REPORT);
		varSUM_AMT.setResetGroup(null);
		myreport.addVariable(varSUM_AMT);

		VariableBuilder<BigDecimal> varSUM_BASE_AMT = DynamicReports.variable("varSUM_BASE_AMT",
				new AbstractSimpleExpression<BigDecimal>() {
					@Override
					public BigDecimal evaluate(ReportParameters reportParameters) {
						return (BigDecimal) reportParameters.getFieldValue("BASE_AMT");
					}
				}, Calculation.SUM);
		varSUM_BASE_AMT.setResetType(Evaluation.REPORT);
		varSUM_BASE_AMT.setResetGroup(null);
		myreport.addVariable(varSUM_BASE_AMT);
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
		myreport.summary(

				DynamicReports.cmp.horizontalList(

						DynamicReports.cmp.text("").setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
								.setFixedWidth(40).setStyle(DynamicReports.stl.style(styleDefault)),

						DynamicReports.cmp.text("").setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
								.setFixedWidth(80).setStyle(DynamicReports.stl.style(styleDefault)),

						DynamicReports.cmp.text("").setFixedWidth(100)
								.setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)),

						DynamicReports.cmp.text("รวมทัั้งหมด : ").setFixedWidth(60)
								.setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)),

						DynamicReports.cmp.text(varSUM_AMT).setFixedWidth(80).setPattern("#,##0.00")
								.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
								.setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)),

						DynamicReports.cmp.text(varSUM_BASE_AMT).setFixedWidth(80).setPattern("#,##0.00")
								.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
								.setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)),

						DynamicReports.cmp.text("").setFixedWidth(140)
								.setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)),

						DynamicReports.cmp.text("")
								// .setFixedWidth(140)
								.setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5))

				// Block
				)
		//
		);
		// == end summary

		// == start เพิ่มคอลัมภ์
		myreport.addColumn(DynamicReports.col.column("ลำดับ", new AbstractSimpleExpression<Integer>() {
			public Integer evaluate(ReportParameters reportParameters) {
				return reportParameters.getReportRowNumber();
			}
		}).setStyle(styleDefault).setFixedWidth(40).setPattern("#,###.")
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));

		myreport.addColumn(DynamicReports.col.column("สมุดรายวัน", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				return Fnc.getStr(reportParameters.getFieldValue("VOU_TYPE"))
						+ Fnc.getStr(reportParameters.getFieldValue("VOU_NO"));
			}
		}).setStyle(styleDefault).setFixedWidth(80));

		myreport.addColumn(DynamicReports.col.column("เลขที่เอกสาร", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				return Fnc.getStr(reportParameters.getFieldValue("DOCNO"));
			}
		}).setStyle(styleDefault).setFixedWidth(100));

		myreport.addColumn(DynamicReports.col.column("วันที่เอกสาร", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				return FnDate.displayDateString(reportParameters.getFieldValue("DOCDATE"));
			}
		}).setStyle(styleDefault).setFixedWidth(60));

		myreport.addColumn(DynamicReports.col.column("จำนวน", new AbstractSimpleExpression<BigDecimal>() {
			@Override
			public BigDecimal evaluate(ReportParameters reportParameters) {
				return (BigDecimal) reportParameters.getFieldValue("AMT");
			}
		}).setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)).setFixedWidth(80).setPattern("#,##0.00")
				.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT));

		myreport.addColumn(DynamicReports.col.column("มูลค่าสินค้าสุทธิ", new AbstractSimpleExpression<BigDecimal>() {
			@Override
			public BigDecimal evaluate(ReportParameters reportParameters) {
				return (BigDecimal) reportParameters.getFieldValue("BASE_AMT");
			}
		}).setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)).setFixedWidth(80).setPattern("#,##0.00")
				.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT));

		myreport.addColumn(DynamicReports.col.column("ผู้ซื้อ/ผู้ใช้บริการ", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				String fname = Fnc.getStr(reportParameters.getFieldValue("TITLE")) + " "
						+ Fnc.getStr(reportParameters.getFieldValue("FNAME")) + " "
						+ Fnc.getStr(reportParameters.getFieldValue("LNAME"));
				return fname.trim();
			}
		}).setStyle(styleDefault).setFixedWidth(140));

		myreport.addColumn(DynamicReports.col.column("คำอธิบายรายการ", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				return Fnc.getStr(reportParameters.getFieldValue("DESCR")).trim();
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
				.setPageFormat(PageType.A4, PageOrientation.LANDSCAPE).setPageMargin(DynamicReports.margin(0))
				.setDataSource(dataSrc).setColumnHeaderPrintWhenExpression(new AbstractSimpleExpression() {
					public Boolean evaluate(ReportParameters reportParameters) {
						if (reportParameters.getPageNumber().intValue() == 1) { // ให้แสดงเฉพาะหน้าที่ 1
							return true;
						} else {
							return false;
						}
					}
				});// trick ให้แสดงเฉพาะหน้าแรก

		// == start เพิ่มฟิลด์
		addFieldToReport(myreport);
		// == end เพิ่มฟิลด์

		// == group varible
		// == end group varible

		// == report varible
		VariableBuilder<BigDecimal> varSUM_AMT = DynamicReports.variable("varSUM_AMT",
				new AbstractSimpleExpression<BigDecimal>() {
					@Override
					public BigDecimal evaluate(ReportParameters reportParameters) {
						return (BigDecimal) reportParameters.getFieldValue("AMT");
					}
				}, Calculation.SUM);
		varSUM_AMT.setResetType(Evaluation.REPORT);
		varSUM_AMT.setResetGroup(null);
		myreport.addVariable(varSUM_AMT);

		VariableBuilder<BigDecimal> varSUM_BASE_AMT = DynamicReports.variable("varSUM_BASE_AMT",
				new AbstractSimpleExpression<BigDecimal>() {
					@Override
					public BigDecimal evaluate(ReportParameters reportParameters) {
						return (BigDecimal) reportParameters.getFieldValue("BASE_AMT");
					}
				}, Calculation.SUM);
		varSUM_BASE_AMT.setResetType(Evaluation.REPORT);
		varSUM_BASE_AMT.setResetGroup(null);
		myreport.addVariable(varSUM_BASE_AMT);
		// == end report varible

		// == group Footer
		// == end group Footer

		// == set page header and page footer
		// == end set page header and page footer

		// == summary
		myreport.summary(

				DynamicReports.cmp.horizontalList(

						DynamicReports.cmp.text(" ").setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
								.setFixedWidth(40).setStyle(DynamicReports.stl.style(styleDefault)),

						DynamicReports.cmp.text(" ").setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
								.setFixedWidth(80).setStyle(DynamicReports.stl.style(styleDefault)),

						DynamicReports.cmp.text("").setFixedWidth(100)
								.setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)),

						DynamicReports.cmp.text("รวมทัั้งหมด :").setFixedWidth(60)
								.setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)),

						DynamicReports.cmp.text(varSUM_AMT).setFixedWidth(80).setPattern("#,##0.00")
								.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
								.setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)),

						DynamicReports.cmp.text(varSUM_BASE_AMT).setFixedWidth(80).setPattern("#,##0.00")
								.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
								.setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)),

						DynamicReports.cmp.text("").setFixedWidth(140)
								.setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)),

						DynamicReports.cmp.text("")
								// .setFixedWidth(140)
								.setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5))

				// Block
				)
		//
		);
		// == end summary

		// == start เพิ่มคอลัมภ์
		myreport.addColumn(DynamicReports.col.column("ลำดับ", new AbstractSimpleExpression<Integer>() {
			public Integer evaluate(ReportParameters reportParameters) {
				return reportParameters.getReportRowNumber();
			}
		}).setStyle(styleDefault).setFixedWidth(40).setPattern("#,###.")
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));

		myreport.addColumn(DynamicReports.col.column("สมุดรายวัน", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				return Fnc.getStr(reportParameters.getFieldValue("VOU_TYPE"))
						+ Fnc.getStr(reportParameters.getFieldValue("VOU_NO"));
			}
		}).setStyle(styleDefault).setFixedWidth(80));

		myreport.addColumn(DynamicReports.col.column("เลขที่เอกสาร", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				return Fnc.getStr(reportParameters.getFieldValue("DOCNO"));
			}
		}).setStyle(styleDefault).setFixedWidth(100));

		myreport.addColumn(DynamicReports.col.column("วันที่เอกสาร", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				return FnDate.displayDateString(reportParameters.getFieldValue("DOCDATE"));
			}
		}).setStyle(styleDefault).setFixedWidth(60));

		myreport.addColumn(DynamicReports.col.column("จำนวน", new AbstractSimpleExpression<BigDecimal>() {
			@Override
			public BigDecimal evaluate(ReportParameters reportParameters) {
				return (BigDecimal) reportParameters.getFieldValue("AMT");
			}
		}).setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)).setFixedWidth(80).setPattern("#,##0.00")
				.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT));

		myreport.addColumn(DynamicReports.col.column("มูลค่าสินค้าสุทธิ", new AbstractSimpleExpression<BigDecimal>() {
			@Override
			public BigDecimal evaluate(ReportParameters reportParameters) {
				return (BigDecimal) reportParameters.getFieldValue("BASE_AMT");
			}
		}).setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)).setFixedWidth(80).setPattern("#,##0.00")
				.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT));

		myreport.addColumn(DynamicReports.col.column("ผู้ซื้อ/ผู้ใช้บริการ", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				String fname = Fnc.getStr(reportParameters.getFieldValue("TITLE")) + " "
						+ Fnc.getStr(reportParameters.getFieldValue("FNAME")) + " "
						+ Fnc.getStr(reportParameters.getFieldValue("LNAME"));
				return fname.trim();
			}
		}).setStyle(styleDefault).setFixedWidth(140));

		myreport.addColumn(DynamicReports.col.column("คำอธิบายรายการ", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				return Fnc.getStr(reportParameters.getFieldValue("DESCR")).trim();
			}
		}).setStyle(styleDefault));
		// == end เพิ่มคอลัมภ์

		return myreport;

	}

	public static void addFieldToReport(JasperReportBuilder myreport) {
		// กรณีนี้ทำจาก FModelHasMap ให้พิมพ์ชื่อตัวใหญ่
		myreport.addField("VOU_TYPE", DynamicReports.type.stringType());
		myreport.addField("VOU_NO", DynamicReports.type.stringType());
		myreport.addField("POSTDATE", java.sql.Date.class);
		myreport.addField("TAX_TYPE", DynamicReports.type.integerType());
		myreport.addField("VAT_RATE", DynamicReports.type.bigDecimalType());
		myreport.addField("AMT", DynamicReports.type.bigDecimalType());
		myreport.addField("BASE_AMT", DynamicReports.type.bigDecimalType());
		myreport.addField("SECT_NAME", DynamicReports.type.stringType());
		myreport.addField("TITLE", DynamicReports.type.stringType());
		myreport.addField("FNAME", DynamicReports.type.stringType());
		myreport.addField("LNAME", DynamicReports.type.stringType());
		myreport.addField("DESCR", DynamicReports.type.stringType());
		myreport.addField("DOCNO", DynamicReports.type.stringType());
		myreport.addField("DOCDATE", java.sql.Date.class);

	}

}
package com.pcc.gl.progman;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import com.pcc.gl.tbo.TboACCT_NO;
import com.pcc.gl.tbo.TboACGL_DETAIL;
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
import net.sf.dynamicreports.report.builder.VariableBuilder;
import net.sf.dynamicreports.report.builder.group.CustomGroupBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.Evaluation;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.jasperreports.engine.JRDataSource;

public class ManAcProfit {

	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static void getReport(LoginBean _loginBean, java.sql.Date fromPostdate, java.sql.Date toPostdate,
			FJasperPrintList fJasperPrintList, int print_option) throws Exception {

		try (FDbc dbc = FDbc.connectMasterDb()) {

			Map reportParams = new HashMap();
			reportParams.put("reportName", "รายงานงบกำไรขาดทุน");
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
		sql.addLine("select dd.comp_cde, dd.acct_id, att.acct_name, att.acct_type, SUM( coalesce(dd.amt,0) * coalesce(dd.num_type,0) ) as amt");
		sql.addLine("from " + TboACGL_DETAIL.tablename + " dd");
		sql.addLine("left join " + TboACCT_NO.tablename + " att on dd.COMP_CDE=att.COMP_CDE and dd.acct_id=att.acct_id");
		sql.addLine("where dd.comp_cde='" + Fnc.sqlQuote(_loginBean.getCOMP_CDE()) + "' and dd.recsta = 2 ");
		sql.addLine("and coalesce(dd.chq_type,'') != '2' "); // ,2=เช็ครอตัดยกมา
		sql.addLine("and dd.postdate between '" + fromPostdate + "' and '" + toPostdate + "' ");
		sql.addLine("and att.acct_type in (4,5)");
		sql.addLine("group by dd.comp_cde, dd.acct_id, att.acct_name, att.acct_type");
		sql.addLine("having SUM( coalesce(dd.amt,0) * coalesce(dd.num_type,0) ) != 0");
		sql.addLine("order by dd.comp_cde, att.acct_type, dd.acct_id, att.acct_name");
		//System.out.println(sql.getSql());
		//logger.info(sql.getSql());
		java.util.List<FModelHasMap> list_dat = new ArrayList<FModelHasMap>();
		try (java.sql.ResultSet rs = dbc.getResultSet(sql.getSql());) {
			while (rs.next()) {
				FModelHasMap dat = new FModelHasMap();
				// ใช้ dat.putRecord ไม่ได้สำหรับ sql แบบนี้
				dat.setString("COMP_CDE", rs.getString("COMP_CDE"));
				dat.setString("ACCT_ID", rs.getString("ACCT_ID"));
				dat.setString("ACCT_NAME", rs.getString("ACCT_NAME"));
				dat.setInt("ACCT_TYPE", rs.getInt("ACCT_TYPE"));
				dat.setBigDecimal("AMT", rs.getBigDecimal("AMT"));
				list_dat.add(dat);

			}
		}
		System.out.println("end putRecord");

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
				.setPageFormat(PageType.A4, PageOrientation.PORTRAIT).setDataSource(dataSrc)
				.columnHeader(DynamicReports.cmp.verticalGap(2));// แก้ปํญหากรณี pdf แสดงสีแถวแบบ highlight
																	// ทำให้ไม่ทับเส้นขอบล่างของ Title Column Header

		// == start เพิ่มฟิลด์
		addFieldToReport(myreport);
		// == end เพิ่มฟิลด์

		// == group varible
		CustomGroupBuilder acctGroup = DynamicReports.grp.group(new AbstractSimpleExpression() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				Integer int1 = (Integer) reportParameters.getFieldValue("ACCT_TYPE");
				String ret = "";
				if (int1.intValue() == 4) {
					ret = "รายได้";
				} else if (int1.intValue() == 5) {
					ret = "ค่าใช้จ่าย";
				} else {
					ret = "อื่นๆ";
				}
				return ret;
			}
		});
		myreport.groupBy(acctGroup);
		// == end group varible

		// == report varible
		// รวมสำหรับ Footer
		VariableBuilder<BigDecimal> varSUM_GROUP = DynamicReports.variable("varSUM_GROUP",
				new AbstractSimpleExpression<BigDecimal>() {
					@Override
					public BigDecimal evaluate(ReportParameters reportParameters) {
						Integer acct_type = (Integer) reportParameters.getFieldValue("ACCT_TYPE");
						BigDecimal ret = (BigDecimal) reportParameters.getFieldValue("AMT");
						if (acct_type.intValue() == 4) {// รายได้
							ret = ret.negate();
						}
						return ret;
					}
				}, Calculation.SUM);
		varSUM_GROUP.setResetType(Evaluation.GROUP);
		varSUM_GROUP.setResetGroup(acctGroup);
		myreport.addVariable(varSUM_GROUP);

		VariableBuilder<String> varLABEL_GROUP = DynamicReports.variable("varLABEL_GROUP",
				new AbstractSimpleExpression<String>() {
					@Override
					public String evaluate(ReportParameters reportParameters) {
						Integer acct_type = (Integer) reportParameters.getFieldValue("ACCT_TYPE");
						if (acct_type.intValue() == 4) {// รายได้
							return "รวมรายได้ :";
						} else {
							return "รวมค่าใช้จ่าย :";
						}
					}
				}, Calculation.NOTHING);
		varLABEL_GROUP.setResetType(Evaluation.GROUP);
		varLABEL_GROUP.setResetGroup(acctGroup);
		myreport.addVariable(varLABEL_GROUP);

		VariableBuilder<BigDecimal> varSUM_ALL = DynamicReports.variable("varSUM_ALL",
				new AbstractSimpleExpression<BigDecimal>() {
					@Override
					public BigDecimal evaluate(ReportParameters reportParameters) {

						return (BigDecimal) reportParameters.getFieldValue("AMT");

					}
				}, Calculation.SUM);
		varSUM_ALL.setResetType(Evaluation.REPORT);
		varSUM_ALL.setResetGroup(null);
		myreport.addVariable(varSUM_ALL);
		// == end report varible

		// == group Footer
		myreport.addGroupFooter(acctGroup,

				DynamicReports.cmp.horizontalList(

						DynamicReports.cmp.text(new AbstractSimpleExpression<String>() {
							@Override
							public String evaluate(ReportParameters reportParameters) {
								// Integer acct_type = (Integer) reportParameters.getFieldValue("ACCT_TYPE");
								// //ใช้ไม่ได้จังหวะนี้ต้องทำเป็นตัวแปรแทน
								String s1 = (String) reportParameters.getVariableValue("varLABEL_GROUP");
								return s1;
							}
						}).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
								.setStyle(DynamicReports.stl.style(styleDefault)),

						DynamicReports.cmp.text(new AbstractSimpleExpression<BigDecimal>() {
							@Override
							public BigDecimal evaluate(ReportParameters reportParameters) {
								return (BigDecimal) reportParameters.getVariableValue("varSUM_GROUP");
							}
						}).setFixedWidth(80).setPattern("#,##0.00")
								.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
								.setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5))
				//
				)
		//
		);
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

						DynamicReports.cmp.text(new AbstractSimpleExpression<String>() {
							@Override
							public String evaluate(ReportParameters reportParameters) {
								BigDecimal b1 = (BigDecimal) reportParameters.getVariableValue("varSUM_ALL");
								if (b1.compareTo(BigDecimal.ZERO) < 0) {
									return "กำไร :";
								} else {
									return "ขาดทุน :";
								}

							}
						}).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
								.setStyle(DynamicReports.stl.style(styleDefault)),

						DynamicReports.cmp.text(new AbstractSimpleExpression<BigDecimal>() {
							@Override
							public BigDecimal evaluate(ReportParameters reportParameters) {
								BigDecimal b1 = (BigDecimal) reportParameters.getVariableValue("varSUM_ALL");
								if (b1.compareTo(BigDecimal.ZERO) < 0) {
									return b1.negate();// กำไร
								} else {
									return b1.negate();// ขาดทุน
								}
							}
						}).setFixedWidth(80).setPattern("#,##0.00")
								.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
								.setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5))

				// Block
				)
		//
		);
		// == end summary

		// == start เพิ่มคอลัมภ์
		myreport.addColumn(DynamicReports.col.column("ลำดับ", new AbstractSimpleExpression<Integer>() {
			public Integer evaluate(ReportParameters reportParameters) {
				// มาจากตัวแปร ok
				return reportParameters.getReportRowNumber();
			}
		}).setStyle(styleDefault).setFixedWidth(40).setPattern("#,###.")
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));

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
		}).setStyle(styleDefault));

		myreport.addColumn(DynamicReports.col.column("ยอดคงเหลือ", new AbstractSimpleExpression<BigDecimal>() {
			@Override
			public BigDecimal evaluate(ReportParameters reportParameters) {
				Integer acct_type = (Integer) reportParameters.getFieldValue("ACCT_TYPE");
				BigDecimal ret = (BigDecimal) reportParameters.getFieldValue("AMT");
				if (acct_type.intValue() == 4) {
					return ret.multiply(new BigDecimal("-1"));
				} else {
					return ret;
				}
			}
		}).setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)).setFixedWidth(80).setPattern("#,##0.00")
				.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT));

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
				});

		// == start เพิ่มฟิลด์
		addFieldToReport(myreport);
		// == end เพิ่มฟิลด์

		// == group varible
		CustomGroupBuilder acctGroup = DynamicReports.grp.group(new AbstractSimpleExpression() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				Integer int1 = (Integer) reportParameters.getFieldValue("ACCT_TYPE");
				String ret = "";
				if (int1.intValue() == 4) {
					ret = "รายได้";
				} else if (int1.intValue() == 5) {
					ret = "ค่าใช้จ่าย";
				} else {
					ret = "อื่นๆ";
				}
				return ret;
			}
		}).setPadding(0);// ทำให้ Excel ตรง group header ไม่เยื้องกันระหว่าง detail
		myreport.groupBy(acctGroup);
		// == end group varible

		// == report varible
		// รวมสำหรับ Footer
		VariableBuilder<BigDecimal> varSUM_GROUP = DynamicReports.variable("varSUM_GROUP",
				new AbstractSimpleExpression<BigDecimal>() {
					@Override
					public BigDecimal evaluate(ReportParameters reportParameters) {
						Integer acct_type = (Integer) reportParameters.getFieldValue("ACCT_TYPE");
						BigDecimal ret = (BigDecimal) reportParameters.getFieldValue("AMT");
						if (acct_type.intValue() == 4) {// รายได้
							ret = ret.negate();
						}
						return ret;
					}
				}, Calculation.SUM);
		varSUM_GROUP.setResetType(Evaluation.GROUP);
		varSUM_GROUP.setResetGroup(acctGroup);
		myreport.addVariable(varSUM_GROUP);

		VariableBuilder<String> varLABEL_GROUP = DynamicReports.variable("varLABEL_GROUP",
				new AbstractSimpleExpression<String>() {
					@Override
					public String evaluate(ReportParameters reportParameters) {
						Integer acct_type = (Integer) reportParameters.getFieldValue("ACCT_TYPE");
						if (acct_type.intValue() == 4) {// รายได้
							return "รวมรายได้ :";
						} else {
							return "รวมค่าใช้จ่าย :";
						}
					}
				}, Calculation.NOTHING);
		varLABEL_GROUP.setResetType(Evaluation.GROUP);
		varLABEL_GROUP.setResetGroup(acctGroup);
		myreport.addVariable(varLABEL_GROUP);

		VariableBuilder<BigDecimal> varSUM_ALL = DynamicReports.variable("varSUM_ALL",
				new AbstractSimpleExpression<BigDecimal>() {
					@Override
					public BigDecimal evaluate(ReportParameters reportParameters) {

						return (BigDecimal) reportParameters.getFieldValue("AMT");

					}
				}, Calculation.SUM);
		varSUM_ALL.setResetType(Evaluation.REPORT);
		varSUM_ALL.setResetGroup(null);
		myreport.addVariable(varSUM_ALL);
		// == end report varible

		// == group Footer
		myreport.addGroupFooter(acctGroup,

				DynamicReports.cmp.horizontalList(

						DynamicReports.cmp.text(new AbstractSimpleExpression<String>() {
							@Override
							public String evaluate(ReportParameters reportParameters) {
								// Integer acct_type = (Integer) reportParameters.getFieldValue("ACCT_TYPE");
								// //ใช้ไม่ได้จังหวะนี้ต้องทำเป็นตัวแปรแทน
								String s1 = (String) reportParameters.getVariableValue("varLABEL_GROUP");
								return s1;
							}
						}).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
								.setStyle(DynamicReports.stl.style(styleDefault)),

						DynamicReports.cmp.text(new AbstractSimpleExpression<BigDecimal>() {
							@Override
							public BigDecimal evaluate(ReportParameters reportParameters) {
								return (BigDecimal) reportParameters.getVariableValue("varSUM_GROUP");
							}
						}).setFixedWidth(80).setPattern("#,##0.00")
								.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
								.setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5))
				//
				)
		//
		);
		// == end group Footer

		// == set page header and page footer
		// == end set page header and page footer

		// == summary
		myreport.summary(

				DynamicReports.cmp.horizontalList(

						DynamicReports.cmp.text(new AbstractSimpleExpression<String>() {
							@Override
							public String evaluate(ReportParameters reportParameters) {
								BigDecimal b1 = (BigDecimal) reportParameters.getVariableValue("varSUM_ALL");
								if (b1.compareTo(BigDecimal.ZERO) < 0) {
									return "กำไร :";
								} else {
									return "ขาดทุน :";
								}

							}
						}).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
								.setStyle(DynamicReports.stl.style(styleDefault)),

						DynamicReports.cmp.text(new AbstractSimpleExpression<BigDecimal>() {
							@Override
							public BigDecimal evaluate(ReportParameters reportParameters) {
								BigDecimal b1 = (BigDecimal) reportParameters.getVariableValue("varSUM_ALL");
								if (b1.compareTo(BigDecimal.ZERO) < 0) {
									return b1.negate();// กำไร
								} else {
									return b1.negate();// ขาดทุน
								}
							}
						}).setFixedWidth(80).setPattern("#,##0.00")
								.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
								.setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5))
				// Block
				)
		//
		);
		// == end summary

		// == start เพิ่มคอลัมภ์
		myreport.addColumn(DynamicReports.col.column("ลำดับ", new AbstractSimpleExpression<Integer>() {
			public Integer evaluate(ReportParameters reportParameters) {
				// มาจากตัวแปร ok
				return reportParameters.getReportRowNumber();
			}
		}).setStyle(styleDefault).setFixedWidth(40).setPattern("#,###.")
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));

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
		}).setStyle(styleDefault));

		myreport.addColumn(DynamicReports.col.column("ยอดคงเหลือ", new AbstractSimpleExpression<BigDecimal>() {
			@Override
			public BigDecimal evaluate(ReportParameters reportParameters) {
				Integer acct_type = (Integer) reportParameters.getFieldValue("ACCT_TYPE");
				BigDecimal ret = (BigDecimal) reportParameters.getFieldValue("AMT");
				if (acct_type.intValue() == 4) {
					return ret.multiply(new BigDecimal("-1"));
				} else {
					return ret;
				}
			}
		}).setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)).setFixedWidth(80).setPattern("#,##0.00")
				.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT));
		// == end เพิ่มคอลัมภ์

		return myreport;

	}

	public static void addFieldToReport(JasperReportBuilder myreport) {
		// กรณีนี้ทำจาก FModelHasMap ให้พิมพ์ชื่อตัวใหญ่
		myreport.addField("COMP_CDE", DynamicReports.type.stringType());
		myreport.addField("ACCT_ID", DynamicReports.type.stringType());
		myreport.addField("ACCT_NAME", DynamicReports.type.stringType());
		myreport.addField("ACCT_TYPE", DynamicReports.type.integerType());
		myreport.addField("AMT", DynamicReports.type.bigDecimalType());

	}

}
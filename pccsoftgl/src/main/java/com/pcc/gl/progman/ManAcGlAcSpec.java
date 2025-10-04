package com.pcc.gl.progman;

import java.math.BigDecimal;
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
import com.pcc.sys.tbm.TbmFCOMP;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.VariableBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.group.CustomGroupBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.Evaluation;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.jasperreports.engine.JRDataSource;

public class ManAcGlAcSpec {

	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static void getReport(LoginBean _loginBean, String fromAcctid, String toAcctid, java.sql.Date fromPostdate,
			java.sql.Date toPostdate, FJasperPrintList fJasperPrintList, int print_option) throws Exception {

		Map reportParams = new HashMap();
		try (FDbc dbc = FDbc.connectMasterDb()) {

			java.sql.Date glFrmDate = TbmFCOMP.getGldate(_loginBean.getCOMP_CDE(), fromPostdate);

			reportParams.put("reportName", "รายงานบัญชีแยกประเภท");
			String reportConditionString1 = "";
			if (!Fnc.isEmpty(fromAcctid) && !Fnc.isEmpty(toAcctid)) {
				reportConditionString1 += "จากรหัสบัญชี " + fromAcctid + " ถึง " + toAcctid;
			}
			if (fromPostdate != null && toPostdate != null) {
				reportConditionString1 += "จากวันที่ " + FnDate.displayDateString(fromPostdate) + " ถึง "
						+ FnDate.displayDateString(toPostdate);
			}
			reportParams.put("reportConditionString1", reportConditionString1);

			SqlStr sql = new SqlStr();
			sql.addLine("select * from (");

			// หายอดยกมา
			sql.addLine("  select dd.comp_cde, '' as vou_type, '' as vou_no, 0 as vou_seq_show, null as postdate");
			sql.addLine("  ,1 as num_type ,dd.acct_id,  SUM(coalesce(dd.amt,0) * coalesce(dd.num_type,0)) as amt, 'ยอดยกมา' as descr, '' as descr_sub, att.acct_name, att.acct_type");
			sql.addLine("  from " + TboACGL_DETAIL.tablename + " dd");
			sql.addLine("  left join " + TboACCT_NO.tablename + " att on dd.COMP_CDE=att.COMP_CDE and dd.acct_id=att.acct_id");
			sql.addLine("  where dd.comp_cde='" + _loginBean.getCOMP_CDE() + "' and dd.recsta = 2 ");
			sql.addLine("  and coalesce(dd.chq_type,'') != '2' "); // ,2=เช็ครอตัดยกมา

			if (!Fnc.isEmpty(fromAcctid) && !Fnc.isEmpty(toAcctid)) {
				sql.addLine(" and dd.acct_id between '" + Fnc.sqlQuote(fromAcctid) + "' and '" + Fnc.sqlQuote(toAcctid) + "' ");
			} else if (!Fnc.isEmpty(fromAcctid)) {
				sql.addLine(" and dd.acct_id >= '" + Fnc.sqlQuote(fromAcctid) + "' ");
			} else if (!Fnc.isEmpty(toAcctid)) {
				sql.addLine(" and dd.acct_id <= '" + Fnc.sqlQuote(toAcctid) + "' ");
			}

			if (FnDate.compareDate(fromPostdate, glFrmDate) == 0) {// fromdate เท่ากับวันที่เริ่มงบบัญชี เอาเฉพาะยอดยกมาที่ปิดบัญชี
				sql.addLine(" and coalesce(dd.acct_open,'') = 'Y' ");// เอาเฉพาะตัว cal ยอดยกมา
				sql.addLine(" and dd.postdate = '" + glFrmDate + "' ");
			} else {
				sql.addLine(" and dd.postdate >= '" + glFrmDate + "' and dd.postdate < '" + fromPostdate + "' ");
			}
			sql.addLine(" group by dd.comp_cde,dd.acct_id,att.acct_name, att.acct_type");

			sql.addLine(" union all");

			// ยอดในรอบ
			sql.addLine(" select dd.comp_cde, dd.vou_type, dd.vou_no, dd.vou_seq_show, dd.postdate");
			sql.addLine(" ,dd.num_type ,dd.acct_id, dd.amt, dd.descr, dd.descr_sub, att.acct_name, att.acct_type");
			sql.addLine(" from " + TboACGL_DETAIL.tablename + " dd");
			sql.addLine(" left join " + TboACCT_NO.tablename + " att on dd.COMP_CDE=att.COMP_CDE and dd.acct_id=att.acct_id");
			sql.addLine(" where dd.comp_cde='" + Fnc.sqlQuote(_loginBean.getCOMP_CDE()) + "' and dd.recsta = 2 ");
			sql.addLine(" and coalesce(dd.chq_type,'') != '2' "); // 2=เช็ครอตัดยกมา
			sql.addLine(" and coalesce(dd.acct_open,'') != 'Y' ");// Y=ตัว cal ยอดยกมา

			if (!Fnc.isEmpty(fromAcctid) && !Fnc.isEmpty(toAcctid)) {
				sql.addLine(" and dd.acct_id between '" + Fnc.sqlQuote(fromAcctid) + "' and '" + Fnc.sqlQuote(toAcctid) + "' ");
			} else if (!Fnc.isEmpty(fromAcctid)) {
				sql.addLine(" and dd.acct_id >= '" + Fnc.sqlQuote(fromAcctid) + "' ");
			} else if (!Fnc.isEmpty(toAcctid)) {
				sql.addLine(" and dd.acct_id <= '" + Fnc.sqlQuote(toAcctid) + "' ");
			}

			sql.addLine(" and dd.postdate between '" + fromPostdate + "' and '" + toPostdate + "' ");

			sql.addLine(") mm  ");
			sql.addLine("order by mm.comp_cde, mm.acct_id, mm.postdate, mm.vou_type, mm.vou_no, mm.vou_seq_show");

			logger.info(sql.getSql());
			java.util.List<FModelHasMap> list_dat = new ArrayList<FModelHasMap>();
			try (java.sql.ResultSet rs = dbc.getResultSet3(sql.getSql(), 20000);) {

				while (rs.next()) {

					FModelHasMap dat = new FModelHasMap();
					// ใช้ dat.putRecord ไม่ได้สำหรับ sql แบบนี้
					dat.setString("COMP_CDE", rs.getString("COMP_CDE"));
					dat.setString("VOU_TYPE", rs.getString("VOU_TYPE"));
					dat.setString("VOU_NO", rs.getString("VOU_NO"));
					dat.setInt("VOU_SEQ_SHOW", rs.getInt("VOU_SEQ_SHOW"));
					dat.setDate("POSTDATE", rs.getDate("POSTDATE"));
					dat.setBigDecimal("NUM_TYPE", rs.getBigDecimal("NUM_TYPE"));
					dat.setString("ACCT_ID", rs.getString("ACCT_ID"));
					dat.setBigDecimal("AMT", rs.getBigDecimal("AMT"));
					dat.setString("DESCR", rs.getString("DESCR"));
					dat.setString("DESCR_SUB", rs.getString("DESCR_SUB"));
					dat.setString("ACCT_NAME", rs.getString("ACCT_NAME"));
					dat.setInt("ACCT_TYPE", rs.getInt("ACCT_TYPE"));

					if (Fnc.getStr(rs.getString("DESCR")).equals("ยอดยกมา")) {
						dat.setString("BEFORE_FLG", "Y");
					} else {
						dat.setString("BEFORE_FLG", "");
					}

					list_dat.add(dat);

				}
			}
			logger.info("end putRecord");

			if (list_dat.size() > 0) {
				FJRBeanCollectionDataSource reportDataSource = new FJRBeanCollectionDataSource(list_dat);
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
				String s1 = Fnc.getStr(reportParameters.getFieldValue("ACCT_ID"));
				String s2 = Fnc.getStr(reportParameters.getFieldValue("ACCT_NAME"));
				return s1 + "  :  " + s2;
			}
		});
		myreport.groupBy(acctGroup);
		// == end group varible

		// == report varible
		VariableBuilder<Long> varseq = DynamicReports.variable("VARSEQ", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				if (Fnc.getStr(reportParameters.getFieldValue("BEFORE_FLG")).equals("Y")) {
					return null;// count จะไม่นับรวม
				} else {
					return "";
				}
			}
		}, Calculation.COUNT);
		varseq.setResetType(Evaluation.GROUP);
		varseq.setResetGroup(acctGroup);
		myreport.addVariable(varseq);

		VariableBuilder<BigDecimal> varREMN = DynamicReports.variable("VARREMN",
				new AbstractSimpleExpression<BigDecimal>() {
					@Override
					public BigDecimal evaluate(ReportParameters reportParameters) {
						BigDecimal ret = BigDecimal.ZERO;

						BigDecimal aa = (BigDecimal) reportParameters.getFieldValue("AMT");
						BigDecimal bb = (BigDecimal) reportParameters.getFieldValue("NUM_TYPE");
						ret = aa.multiply(bb);

						return ret;
					}
				}, Calculation.SUM);
		varREMN.setResetType(Evaluation.GROUP);
		varREMN.setResetGroup(acctGroup);
		myreport.addVariable(varREMN);

		VariableBuilder<BigDecimal> varSUM_DR = DynamicReports.variable("VARSUM_DR",
				new AbstractSimpleExpression<BigDecimal>() {
					@Override
					public BigDecimal evaluate(ReportParameters reportParameters) {
						BigDecimal ret = BigDecimal.ZERO;
						if (Fnc.getStr(reportParameters.getFieldValue("BEFORE_FLG")).equals("Y")) {
							ret = null;
						} else {

							BigDecimal mm = (BigDecimal) reportParameters.getValue("NUM_TYPE");
							if (mm.intValue() > 0) {
								ret = (BigDecimal) reportParameters.getFieldValue("AMT");
							} else {
								ret = null;
							}

						}
						return ret;
					}
				}, Calculation.SUM);
		varSUM_DR.setResetType(Evaluation.GROUP);
		varSUM_DR.setResetGroup(acctGroup);
		myreport.addVariable(varSUM_DR);

		VariableBuilder<BigDecimal> varSUM_CR = DynamicReports.variable("VARSUM_CR",
				new AbstractSimpleExpression<BigDecimal>() {
					@Override
					public BigDecimal evaluate(ReportParameters reportParameters) {
						BigDecimal ret = BigDecimal.ZERO;
						if (Fnc.getStr(reportParameters.getFieldValue("BEFORE_FLG")).equals("Y")) {
							ret = null;
						} else {

							BigDecimal mm = (BigDecimal) reportParameters.getValue("NUM_TYPE");
							if (mm.intValue() > 0) {
								ret = null;
							} else {
								ret = (BigDecimal) reportParameters.getFieldValue("AMT");
							}

						}
						return ret;
					}
				}, Calculation.SUM);
		varSUM_CR.setResetType(Evaluation.GROUP);
		varSUM_CR.setResetGroup(acctGroup);
		myreport.addVariable(varSUM_CR);
		// == end report varible

		// == group Footer
		myreport.groupFooter(acctGroup, DynamicReports.cmp.horizontalList(

				DynamicReports.cmp.text("รวม : ").setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
						.setStyle(DynamicReports.stl.style(styleDefault)),

				DynamicReports.cmp.text(varSUM_DR).setFixedWidth(80).setPattern("#,##0.00")
						.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
						.setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)),

				DynamicReports.cmp.text(varSUM_CR).setFixedWidth(80).setPattern("#,##0.00")
						.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
						.setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)),

				DynamicReports.cmp.text("").setStyle(DynamicReports.stl.style(styleDefault)).setFixedWidth(80) // เป็น
																																																																																																																																																																																						// pixcel
																																																																																																																																																																																						// Block
		)
		// block
		);
		// == end group Footer

		// == set page header and page footer
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
		// == end set page header and page footer

		// == start เพิ่มคอลัมภ์
		myreport.addColumn(DynamicReports.col.column("ลำดับ", new AbstractSimpleExpression<Long>() {
			public Long evaluate(ReportParameters reportParameters) {

				// มาจากตัวแปร ok
				// return reportParameters.getReportRowNumber();

				Long var1 = reportParameters.getVariableValue("VARSEQ");// การ count จะเป็น Long
				if (var1.intValue() == 0) {
					return null;
				} else {
					return var1;
				}

			}
		}).setStyle(styleDefault).setWidth(10).setPattern("#,###.")
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));

		myreport.addColumn(DynamicReports.col.column("วันที่สมุดรายวัน", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				java.util.Date date1 = (java.util.Date) reportParameters.getFieldValue("POSTDATE");
				if (Fnc.getStr(reportParameters.getFieldValue("BEFORE_FLG")).equals("Y")) {
					return "";
				} else {
					return FnDate.displayDateString(date1);
				}
			}
		}).setStyle(styleDefault).setWidth(20));

		myreport.addColumn(DynamicReports.col.column("สมุดรายวัน", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				if (Fnc.getStr(reportParameters.getFieldValue("BEFORE_FLG")).equals("Y")) {
					return "";
				} else {
					return Fnc.getStr(reportParameters.getFieldValue("VOU_TYPE"))
							+ Fnc.getStr(reportParameters.getFieldValue("VOU_NO"));
				}
			}
		}).setStyle(styleDefault).setWidth(20));

		myreport.addColumn(DynamicReports.col.column("คำอธิบายรายการ", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				return Fnc.getStr(reportParameters.getFieldValue("DESCR")) + " "
						+ Fnc.getStr(reportParameters.getFieldValue("DESCR_SUB"));
			}
		}).setStyle(styleDefault));

		myreport.addColumn(DynamicReports.col.column("เดบิต", new AbstractSimpleExpression<BigDecimal>() {
			@Override
			public BigDecimal evaluate(ReportParameters reportParameters) {
				if (Fnc.getStr(reportParameters.getFieldValue("BEFORE_FLG")).equals("Y")) {
					return null;
				} else {
					BigDecimal mm = (BigDecimal) reportParameters.getValue("NUM_TYPE");
					if (mm.intValue() > 0) {
						return reportParameters.getValue("AMT");
					} else {
						return null;
					}
				}
			}
		}).setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)).setFixedWidth(80).setPattern("#,##0.00")
				.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT));
//		col_amt_debit.setPrintWhenExpression(new AbstractSimpleExpression() { //trick กรณีเป็นแบบสีสลับกันจะไม่สวยเพราะจะช่องขาวกรณีแถวนั้นเป็นสี
//		public Boolean evaluate(ReportParameters reportParameters) {
//			BigDecimal mm = (BigDecimal) reportParameters.getValue("NUM_TYPE");
//			if (mm.intValue() > 0) {
//				return true;
//			} else {
//				return false;
//			}
//		}
//	});

		myreport.addColumn(DynamicReports.col.column("เครดิต", new AbstractSimpleExpression<BigDecimal>() {
			@Override
			public BigDecimal evaluate(ReportParameters reportParameters) {
				if (Fnc.getStr(reportParameters.getFieldValue("BEFORE_FLG")).equals("Y")) {
					return null;
				} else {
					BigDecimal mm = (BigDecimal) reportParameters.getValue("NUM_TYPE");
					if (mm.intValue() < 0) {
						return reportParameters.getValue("AMT");
					} else {
						return null;
					}
				}
			}
		}).setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)).setFixedWidth(80).setPattern("#,##0.00")
				.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT));

		myreport.addColumn(DynamicReports.col.column("คงเหลือ", new AbstractSimpleExpression<BigDecimal>() {
			@Override
			public BigDecimal evaluate(ReportParameters reportParameters) {
				BigDecimal var1 = reportParameters.getVariableValue("VARREMN");
				Integer acct_type = (Integer) reportParameters.getFieldValue("ACCT_TYPE");
				if ((acct_type == 2 || acct_type == 3 || acct_type == 4) && var1.compareTo(BigDecimal.ZERO) < 0) {// เครดิต
																													// หนี้สิน
																													// ทุน
																													// รายได้
																													// ที่ยอดติดลบ
					var1 = var1.abs();
				}
				return var1;
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
				.setPageFormat(PageType.A4, PageOrientation.LANDSCAPE).setPageMargin(DynamicReports.margin(0)) // trick
				// กรณีเป็น
				// Excel
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

		// == group
		CustomGroupBuilder acctGroup = DynamicReports.grp.group(new AbstractSimpleExpression() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				String s1 = Fnc.getStr(reportParameters.getFieldValue("ACCT_ID"));
				String s2 = Fnc.getStr(reportParameters.getFieldValue("ACCT_NAME"));
				return s1 + "  :  " + s2;
			}
		});
		acctGroup.setPadding(0);// ทำให้ Excel ตรง group header ไม่แสดงเยื้องกันระหว่าง detail;
		// acctGroup.setHeaderLayout(GroupHeaderLayout.EMPTY);//trick ไม่ต้องแสดง group
		// band ในรายงาน
		myreport.groupBy(acctGroup);
		// == end group

		// == varible
		VariableBuilder<Long> varseq = DynamicReports.variable("VARSEQ", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				if (Fnc.getStr(reportParameters.getFieldValue("BEFORE_FLG")).equals("Y")) {
					return null;// count จะไม่นับรวม
				} else {
					return "";
				}
			}
		}, Calculation.COUNT);
		varseq.setResetType(Evaluation.GROUP);
		varseq.setResetGroup(acctGroup);
		myreport.addVariable(varseq);

		VariableBuilder<BigDecimal> varREMN = DynamicReports.variable("VARREMN",
				new AbstractSimpleExpression<BigDecimal>() {
					@Override
					public BigDecimal evaluate(ReportParameters reportParameters) {
						BigDecimal ret = BigDecimal.ZERO;

						BigDecimal aa = (BigDecimal) reportParameters.getFieldValue("AMT");
						BigDecimal bb = (BigDecimal) reportParameters.getFieldValue("NUM_TYPE");
						ret = aa.multiply(bb);

						return ret;
					}
				}, Calculation.SUM);
		varREMN.setResetType(Evaluation.GROUP);
		varREMN.setResetGroup(acctGroup);
		myreport.addVariable(varREMN);

		VariableBuilder<BigDecimal> varSUM_DR = DynamicReports.variable("VARSUM_DR",
				new AbstractSimpleExpression<BigDecimal>() {
					@Override
					public BigDecimal evaluate(ReportParameters reportParameters) {
						BigDecimal ret = BigDecimal.ZERO;
						if (Fnc.getStr(reportParameters.getFieldValue("BEFORE_FLG")).equals("Y")) {
							ret = null;
						} else {

							BigDecimal mm = (BigDecimal) reportParameters.getValue("NUM_TYPE");
							if (mm.intValue() > 0) {
								ret = (BigDecimal) reportParameters.getFieldValue("AMT");
							} else {
								ret = null;
							}

						}
						return ret;
					}
				}, Calculation.SUM);
		varSUM_DR.setResetType(Evaluation.GROUP);
		varSUM_DR.setResetGroup(acctGroup);
		myreport.addVariable(varSUM_DR);

		VariableBuilder<BigDecimal> varSUM_CR = DynamicReports.variable("VARSUM_CR",
				new AbstractSimpleExpression<BigDecimal>() {
					@Override
					public BigDecimal evaluate(ReportParameters reportParameters) {
						BigDecimal ret = BigDecimal.ZERO;
						if (Fnc.getStr(reportParameters.getFieldValue("BEFORE_FLG")).equals("Y")) {
							ret = null;
						} else {

							BigDecimal mm = (BigDecimal) reportParameters.getValue("NUM_TYPE");
							if (mm.intValue() > 0) {
								ret = null;
							} else {
								ret = (BigDecimal) reportParameters.getFieldValue("AMT");
							}

						}
						return ret;
					}
				}, Calculation.SUM);
		varSUM_CR.setResetType(Evaluation.GROUP);
		varSUM_CR.setResetGroup(acctGroup);
		myreport.addVariable(varSUM_CR);
		// == end varible

		// == group Footer
		myreport.groupFooter(acctGroup, DynamicReports.cmp.horizontalList(

				DynamicReports.cmp.text("รวม : ").setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
						.setStyle(DynamicReports.stl.style(styleDefault)),

				DynamicReports.cmp.text(varSUM_DR).setFixedWidth(80).setPattern("#,##0.00")
						.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
						.setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)),

				DynamicReports.cmp.text(varSUM_CR).setFixedWidth(80).setPattern("#,##0.00")
						.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
						.setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)),

				DynamicReports.cmp.text("").setStyle(DynamicReports.stl.style(styleDefault)).setFixedWidth(80) // เป็น
																																																																																																																																																																																						// pixcel
																																																																																																																																																																																						// Block
		)
		// block
		);
		// == end group Footer

		// == set page header and page footer
		// == end set page header and page footer

		// == start เพิ่มคอลัมภ์
		myreport.addColumn(DynamicReports.col.column("ลำดับ", new AbstractSimpleExpression<Long>() {
			public Long evaluate(ReportParameters reportParameters) {
				Long var1 = reportParameters.getVariableValue("VARSEQ");// การ count จะเป็น Long
				if (var1.intValue() == 0) {
					return null;
				} else {
					return var1;
				}

			}
		}).setStyle(styleDefault).setWidth(10).setPattern("#,###.")
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));

		myreport.addColumn(DynamicReports.col.column("วันที่สมุดรายวัน", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				java.util.Date date1 = (java.util.Date) reportParameters.getFieldValue("POSTDATE");
				if (Fnc.getStr(reportParameters.getFieldValue("BEFORE_FLG")).equals("Y")) {
					return "";
				} else {
					return FnDate.displayDateString(date1);
				}
			}
		}).setStyle(styleDefault).setWidth(20));

		myreport.addColumn(DynamicReports.col.column("สมุดรายวัน", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				if (Fnc.getStr(reportParameters.getFieldValue("BEFORE_FLG")).equals("Y")) {
					return "";
				} else {
					return Fnc.getStr(reportParameters.getFieldValue("VOU_TYPE"))
							+ Fnc.getStr(reportParameters.getFieldValue("VOU_NO"));
				}
			}
		}).setStyle(styleDefault).setWidth(20));

		myreport.addColumn(DynamicReports.col.column("คำอธิบายรายการ", new AbstractSimpleExpression<String>() {
			@Override
			public String evaluate(ReportParameters reportParameters) {
				return Fnc.getStr(reportParameters.getFieldValue("DESCR")) + " "
						+ Fnc.getStr(reportParameters.getFieldValue("DESCR_SUB"));
			}
		}).setStyle(styleDefault));

		myreport.addColumn(DynamicReports.col.column("เดบิต", new AbstractSimpleExpression<BigDecimal>() {
			@Override
			public BigDecimal evaluate(ReportParameters reportParameters) {
				if (Fnc.getStr(reportParameters.getFieldValue("BEFORE_FLG")).equals("Y")) {
					return null;
				} else {
					BigDecimal mm = (BigDecimal) reportParameters.getValue("NUM_TYPE");
					if (mm.intValue() > 0) {
						return reportParameters.getValue("AMT");
					} else {
						return null;
					}
				}
			}
		}).setStyle(styleDefault).setFixedWidth(80).setPattern("#,##0.00")
				.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT));

		myreport.addColumn(DynamicReports.col.column("เครดิต", new AbstractSimpleExpression<BigDecimal>() {
			@Override
			public BigDecimal evaluate(ReportParameters reportParameters) {
				if (Fnc.getStr(reportParameters.getFieldValue("BEFORE_FLG")).equals("Y")) {
					return null;
				} else {
					BigDecimal mm = (BigDecimal) reportParameters.getValue("NUM_TYPE");
					if (mm.intValue() < 0) {
						return reportParameters.getValue("AMT");
					} else {
						return null;
					}
				}
			}
		}).setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)).setFixedWidth(80).setPattern("#,##0.00")
				.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT));

		myreport.addColumn(DynamicReports.col.column("คงเหลือ", new AbstractSimpleExpression<BigDecimal>() {
			@Override
			public BigDecimal evaluate(ReportParameters reportParameters) {
				BigDecimal var1 = reportParameters.getVariableValue("VARREMN");
				Integer acct_type = (Integer) reportParameters.getFieldValue("ACCT_TYPE");
				// เครดิต  หนี้สิน  ทุน รายได้ ที่ยอดติดลบ
				if ((acct_type == 2 || acct_type == 3 || acct_type == 4) && var1.compareTo(BigDecimal.ZERO) < 0) {
					var1 = var1.abs();
				}
				return var1;
			}
		}).setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)).setFixedWidth(80).setPattern("#,##0.00")
				.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT));
		// == end เพิ่มคอลัมภ์

		return myreport;

	}

	private static void addFieldToReport(JasperReportBuilder myreport) {
		// *** trick ใช้ชื่อฟิลด์เป็นตัวใหญ่เท่านั้นกรณีใช้ Datasource แบบ FModelHasMap
		// เพราะเก็บชื่อฟิลด์เป็นตัวใหญ่
		myreport.addField("COMP_CDE", DynamicReports.type.stringType());
		myreport.addField("VOU_TYPE", DynamicReports.type.stringType());
		myreport.addField("VOU_NO", DynamicReports.type.stringType());
		myreport.addField("POSTDATE", DynamicReports.type.dateType());
		myreport.addField("ACCT_ID", DynamicReports.type.stringType());
		myreport.addField("NUM_TYPE", DynamicReports.type.bigDecimalType());
		myreport.addField("AMT", DynamicReports.type.bigDecimalType());
		myreport.addField("DESCR", DynamicReports.type.stringType());
		myreport.addField("DESCR_SUB", DynamicReports.type.stringType());
		myreport.addField("ACCT_NAME", DynamicReports.type.stringType());
		myreport.addField("ACCT_TYPE", DynamicReports.type.integerType());
		myreport.addField("BEFORE_FLG", DynamicReports.type.stringType());
	}

}
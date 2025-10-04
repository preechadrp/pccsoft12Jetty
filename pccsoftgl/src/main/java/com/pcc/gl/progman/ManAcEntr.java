package com.pcc.gl.progman;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.pcc.gl.tbf.TbfACCT_NO_SUB;
import com.pcc.gl.tbf.TbfACGL_AP;
import com.pcc.gl.tbf.TbfACGL_AR;
import com.pcc.gl.tbf.TbfACGL_DETAIL;
import com.pcc.gl.tbf.TbfACGL_HEADER;
import com.pcc.gl.tbf.TbfACGL_VATPUR;
import com.pcc.gl.tbf.TbfACGL_VATSALE;
import com.pcc.gl.tbf.TbfACGL_VATWH_CR;
import com.pcc.gl.tbm.TbmACCT_VOU_TYPE;
import com.pcc.gl.tbm.TbmACGL_AP;
import com.pcc.gl.tbm.TbmACGL_AR;
import com.pcc.gl.tbm.TbmACGL_DETAIL;
import com.pcc.gl.tbm.TbmACGL_VATPUR;
import com.pcc.gl.tbm.TbmACGL_VATSALE;
import com.pcc.gl.tbm.TbmACGL_VATWH_CR;
import com.pcc.gl.tbm.TbmACGL_VATWH_DR;
import com.pcc.gl.tbo.TboACCT_NO;
import com.pcc.gl.tbo.TboACCT_NO_SUB;
import com.pcc.gl.tbo.TboACGL_AP;
import com.pcc.gl.tbo.TboACGL_AR;
import com.pcc.gl.tbo.TboACGL_DETAIL;
import com.pcc.gl.tbo.TboACGL_HEADER;
import com.pcc.gl.tbo.TboACGL_VATPUR;
import com.pcc.gl.tbo.TboACGL_VATSALE;
import com.pcc.gl.tbo.TboACGL_VATWH_CR;
import com.pcc.gl.tbo.TboACGL_VATWH_DR;
import com.pcc.gl.tbo.TboFCUS;
import com.pcc.gl.ui.acEntr.AcglAp;
import com.pcc.gl.ui.acEntr.AcglAr;
import com.pcc.gl.ui.acEntr.AcglChq;
import com.pcc.gl.ui.acEntr.AcglVatWhCr;
import com.pcc.gl.ui.acEntr.AcglVatWhDr;
import com.pcc.gl.ui.acEntr.AcglVatpur;
import com.pcc.gl.ui.acEntr.AcglVatsale;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.DynamicRepTemplates;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FJasperPrintList;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FReport;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.MyDecimalbox;
import com.pcc.sys.lib.MyDynamicReport;
import com.pcc.sys.lib.SqlStr;
import com.pcc.sys.lib.ZkUtil;
import com.pcc.sys.tbm.TbmFPARA_COMP;
import com.pcc.sys.tbm.TbmFUSER;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.VariableBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.expression.AbstractComplexExpression;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.Evaluation;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperPrint;

public class ManAcEntr {

	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static void printVoucher(LoginBean _loginBean, String sVou_type, String sVou_no) throws Exception {

		FJasperPrintList fJasperPrintList = new FJasperPrintList();
		try (FDbc dbc = FDbc.connectMasterDb()) {
			printVoucher(dbc, _loginBean, sVou_type, sVou_no, fJasperPrintList);
		}
		if (fJasperPrintList.getJasperPrintLst().size() > 0) {
			FReport.viewPdf(fJasperPrintList.getJasperPrintLst());
			// FReport.viewMsWordToLoad(fJasperPrintList, "AccEntr"); test
		}

	}

	public static void printVoucher(FDbc dbc, LoginBean _loginBean, String sVou_type, String sVou_no,
			FJasperPrintList fJasperPrintList) throws Exception {

		Map<String, Object> reportParams = new HashMap<String, Object>();
		reportParams.put("_loginBean", _loginBean);

		TboACGL_HEADER acH = new TboACGL_HEADER();

		acH.setCOMP_CDE(_loginBean.getCOMP_CDE());
		acH.setVOU_TYPE(sVou_type);
		acH.setVOU_NO(sVou_no);

		if (!TbfACGL_HEADER.find(dbc, acH)) {
			throw new Exception("ไม่พบเอกสาร");
		}

		if (acH.getRECSTA() == 9) {
			throw new Exception("เอกสารถูกยกเลิกแล้ว");
		}

		reportParams.put("P_REPORT_NAME", TbmACCT_VOU_TYPE.getVou_type_name(dbc, acH.getVOU_TYPE(), _loginBean));
		reportParams.put("P_COMP_NAME", _loginBean.getTboFcomp().getCOMP_NAME());
		reportParams.put("P_VOU_TYPE", acH.getVOU_TYPE());
		reportParams.put("P_VOU_NO", acH.getVOU_NO());
		reportParams.put("P_POSTDATE", FnDate.displayDateString(acH.getPOSTDATE()));
		reportParams.put("P_DESCH", acH.getDESCR());
		reportParams.put("P_CREATEBY", TbmFUSER.getUserName(dbc, acH.getUPBY()));

		SqlStr sql = new SqlStr();
		sql.addLine("select dd.comp_cde, dd.vou_type, dd.vou_no, dd.vou_seq_show, dd.postdate");
		sql.addLine(" ,dd.num_type ,dd.acct_id, dd.sect_id, dd.amt, dd.descr, dd.descr_sub, att.acct_name");
		sql.addLine("from " + TboACGL_DETAIL.tablename + " dd");
		sql.addLine("left join " + TboACCT_NO.tablename + " att on dd.COMP_CDE = att.COMP_CDE  and dd.acct_id=att.acct_id");
		sql.addLine("where dd.comp_cde='" + _loginBean.getCOMP_CDE() + "' ");
		sql.addLine("and dd.vou_type = '" + acH.getVOU_TYPE() + "' ");
		sql.addLine("and dd.vou_no = '" + acH.getVOU_NO() + "' ");
		sql.addLine(" order by dd.postdate, dd.comp_cde, dd.vou_type, dd.vou_no,dd.vou_seq_show");

		logger.info(sql.getSql());
		try (java.sql.ResultSet rs = dbc.getResultSet(sql.getSql());) {
			if (rs.next()) {
				rs.beforeFirst();
				JRDataSource reportDataSource = new JRResultSetDataSource(rs);
				fJasperPrintList.addJasperPrintList(genReportPDF_V2(reportDataSource, reportParams, _loginBean).toJasperPrint());
				// fJasperPrintList.addJasperPrintList(genReportPDF_TEST(reportDataSource,
				// reportParams, _loginBean));
			}
		}

	}

	@SuppressWarnings("serial")
	public static JasperReportBuilder genReportPDF_V2(JRDataSource dataSrc, Map<String, Object> reportParams,
			LoginBean _loginBean) {

		StyleBuilder styleDefault = DynamicRepTemplates.getRootStyle();// สำหรับเริ่มต้นค่าใน report นี้
		// trick ** ห้ามสั่ง Templates.rootStyle.setFontSize(11) เด็ดขาด
		// เพราะจะเป็นการเปลี่ยนค่า static ใน Templates.rootStyle ทันที
		// Dynamic report
		JasperReportBuilder myreport = DynamicReports.report()
				.setTemplate(DynamicRepTemplates.reportTemplateWithHighlight)
				.setPageFormat(PageType.A4, PageOrientation.PORTRAIT).summaryWithPageHeaderAndFooter()
				.setDataSource(dataSrc).columnHeader(DynamicReports.cmp.verticalGap(2));// แก้ปํญหากรณี pdf แสดงสีแถวแบบ
																																																																									// highlight
																																																																									// ทำให้ไม่ทับเส้นขอบล่างของ
																																																																									// Title Column Header

		// == start เพิ่มฟิลด์
		addFieldToReport(myreport);
		// == end เพิ่มฟิลด์

		// == group varible
		// == end group vairble

		// == report varible
		addVaribleToReport(myreport);
		// == end report varible

		// == pageHeader
		ComponentBuilder<?, ?> cmpHead = DynamicReports.cmp.horizontalList()
				.add(DynamicReports.cmp.text(Fnc.getStr(reportParams.get("P_COMP_NAME")))
						.setStyle(DynamicReports.stl.style(styleDefault).setFontSize(11)))
				.add(DynamicReports.cmp.text("หน้าที่").setFixedWidth(30)
						.setStyle(DynamicReports.stl.style(styleDefault).setFontSize(11))
						.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT))
				.add(DynamicReports.cmp.pageXslashY().setPageXFixedWidth(13).setPageYFixedWidth(13).setFixedWidth(30)
						.setStyle(DynamicReports.stl.style(styleDefault).setFontSize(11))
						.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT))
				.newRow()
				.add(DynamicReports.cmp
						.text("สมุดรายวัน : " + Fnc.getStr(reportParams.get("P_VOU_TYPE")) + "  "
								+ Fnc.getStr(reportParams.get("P_REPORT_NAME")))
						.setStyle(DynamicReports.stl.style(styleDefault).setFontSize(13)))
				// .newRow()
				.add(DynamicReports.cmp.text("เลขที่ใบสำคัญ : " + Fnc.getStr(reportParams.get("P_VOU_NO")))
						.setStyle(DynamicReports.stl.style(styleDefault).setFontSize(13))
						.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER))
				.add(DynamicReports.cmp.text("วันที่ใบสำคัญ : " + Fnc.getStr(reportParams.get("P_POSTDATE")))
						.setStyle(DynamicReports.stl.style(styleDefault).setFontSize(13))
						.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT))
				.newRow()
				.add(DynamicReports.cmp.text("หมายเหตุ : " + Fnc.getStr(reportParams.get("P_DESCH")))
						.setStyle(DynamicReports.stl.style(styleDefault).setFontSize(13)))
				.newRow().add(DynamicReports.cmp.verticalGap(2));
		myreport.pageHeader(cmpHead);

		// myreport.pageFooter(Templates.footerComponentNotBold);//ตัวเก่า

		myreport.pageFooter(

				DynamicReports.cmp.verticalGap(1).setHeight(10)
						.setStyle(DynamicReports.stl.style().setTopBorder(DynamicReports.stl.penThin())),

				DynamicReports.cmp.horizontalList(

						DynamicReports.cmp.text("ผู้จัดทำ : ").setFixedWidth(50)
								// .setHorizontalTextAlignment(HorizontalTextAlignment.LEFT) //trick ระบบ
								// default ชิดซ้ายอยู่แล้วไม่ต้องใส่
								.setStyle(DynamicReports.stl.style(styleDefault)),

						DynamicReports.cmp.text(Fnc.getStr(reportParams.get("P_CREATEBY"))).setFixedWidth(150)
								.setStyle(DynamicReports.stl.style(styleDefault)),

						DynamicReports.cmp.text("ผู้ตรวจ :").setFixedWidth(40)
								.setStyle(DynamicReports.stl.style(styleDefault)),

						DynamicReports.cmp.text("__________________________").setFixedWidth(160)
								.setStyle(DynamicReports.stl.style(styleDefault)),

						DynamicReports.cmp.text("ผู้อนุมัติ :").setFixedWidth(40)
								.setStyle(DynamicReports.stl.style(styleDefault)),

						DynamicReports.cmp.text("__________________________")
								// .setFixedWidth(50) ** คอลัมภ์สุดท้ายไม่ต้องใส่
								.setStyle(DynamicReports.stl.style(styleDefault))

				), // space comment ตัวชิดขอบสุดของ block มีผลต่อการเยื้องวงเล็บตัวนี้ตอน auto set
				// format code

				DynamicReports.cmp.horizontalList(

						DynamicReports.cmp.text("พิมพ์ : ").setFixedWidth(50)
								.setStyle(DynamicReports.stl.style(styleDefault)),

						DynamicReports.cmp
								.text("วันที่ " + FnDate.displayDateTimeString(FnDate.getTodayDateTime()) + " น.")
								.setFixedWidth(150).setStyle(DynamicReports.stl.style(styleDefault)),

						DynamicReports.cmp.text("(________/________/________)").setFixedWidth(200)
								.setStyle(DynamicReports.stl.style(styleDefault).setLeftPadding(40)),

						DynamicReports.cmp.text("(________/________/________)")
								// .setFixedWidth(50) **คอลัมภ์สุดท้ายไม่ต้องใส่
								.setStyle(DynamicReports.stl.style(styleDefault).setLeftPadding(40))
				// Block
				)
		//
		);

		// == lastPageFooter
		myreport.lastPageFooter(

				DynamicReports.cmp.verticalGap(1)
						.setStyle(DynamicReports.stl.style().setTopBorder(DynamicReports.stl.penThin())),

				DynamicReports.cmp.horizontalList(

						DynamicReports.cmp.text("รวม : ").setFixedWidth(260)
								.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
								.setStyle(DynamicReports.stl.style(styleDefault)),

						DynamicReports.cmp.text(FReport.getVarBigDecimal("varSUMDR")).setFixedWidth(60)
								.setPattern("#,##0.00").setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
								.setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(2)),

						DynamicReports.cmp.text(FReport.getVarBigDecimal("varSUMCR")).setFixedWidth(60)
								.setPattern("#,##0.00").setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
								.setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(2)),

						DynamicReports.cmp.text("").setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5))

				// Block
				),

				DynamicReports.cmp.verticalGap(1).setHeight(10)
						.setStyle(DynamicReports.stl.style().setTopBorder(DynamicReports.stl.penThin())),

				DynamicReports.cmp.horizontalList(

						DynamicReports.cmp.text("ผู้จัดทำ :").setFixedWidth(50)
								// .setHorizontalTextAlignment(HorizontalTextAlignment.LEFT) //trick ระบบ
								// default ชิดซ้ายอยู่แล้วไม่ต้องใส่
								.setStyle(DynamicReports.stl.style(styleDefault)),

						DynamicReports.cmp.text(Fnc.getStr(reportParams.get("P_CREATEBY"))).setFixedWidth(150)
								.setStyle(DynamicReports.stl.style(styleDefault)),

						DynamicReports.cmp.text("ผู้ตรวจ :").setFixedWidth(40)
								.setStyle(DynamicReports.stl.style(styleDefault)),

						DynamicReports.cmp.text("__________________________").setFixedWidth(160)
								.setStyle(DynamicReports.stl.style(styleDefault)),

						DynamicReports.cmp.text("ผู้อนุมัติ :").setFixedWidth(40)
								.setStyle(DynamicReports.stl.style(styleDefault)),

						DynamicReports.cmp.text("__________________________")
								// .setFixedWidth(50) ** คอลัมภ์สุดท้ายไม่ต้องใส่
								.setStyle(DynamicReports.stl.style(styleDefault))

				), // space comment ตัวชิดขอบสุดของ block มีผลต่อการเยื้องวงเล็บตัวนี้ตอน auto set
				// format code

				DynamicReports.cmp.horizontalList(

						DynamicReports.cmp.text("พิมพ์ : ").setFixedWidth(50)
								.setStyle(DynamicReports.stl.style(styleDefault)),

						DynamicReports.cmp
								.text("วันที่ " + FnDate.displayDateTimeString(FnDate.getTodayDateTime()) + " น.")
								.setFixedWidth(150).setStyle(DynamicReports.stl.style(styleDefault)),

						DynamicReports.cmp.text("(________/________/________)").setFixedWidth(200)
								.setStyle(DynamicReports.stl.style(styleDefault).setLeftPadding(40)),

						DynamicReports.cmp.text("(________/________/________)")
								// .setFixedWidth(50) **คอลัมภ์สุดท้ายไม่ต้องใส่
								.setStyle(DynamicReports.stl.style(styleDefault).setLeftPadding(40))
				// Block
				)
		//
		);
		// == end lastpagefooter

		// == start เพิ่มคอลัมภ์
		myreport.addColumn(DynamicReports.col.column("ลำดับ", new AbstractSimpleExpression<Integer>() {
			@Override
			public Integer evaluate(ReportParameters reportParameters) {
				return reportParameters.getReportRowNumber();
			}
		}).setStyle(styleDefault).setFixedWidth(40).setPattern("#,##0.") // setFixedWidth(40) มีหน่วยเป็น pixcel ,
				// setWidth(40) มีหน่วยเป็น %
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));

		myreport.addColumn(DynamicReports.col.column("รหัสบัญชี", "ACCT_ID", DynamicReports.type.stringType())
				.setStyle(styleDefault).setFixedWidth(50));

		myreport.addColumn(DynamicReports.col.column("ชื่อบัญชี", "ACCT_NAME", DynamicReports.type.stringType())
				.setStyle(styleDefault).setFixedWidth(120));

		myreport.addColumn(DynamicReports.col.column("แผนก", "SECT_ID", DynamicReports.type.stringType())
				.setStyle(styleDefault).setFixedWidth(50));

		myreport.addColumn(DynamicReports.col.column("เดบิต", new AbstractSimpleExpression<BigDecimal>() {
			@Override
			public BigDecimal evaluate(ReportParameters reportParameters) {

				BigDecimal mm = (BigDecimal) reportParameters.getValue("NUM_TYPE");
				if (mm.intValue() > 0) {
					return reportParameters.getValue("AMT");
				} else {
					return null;
				}

			}
		}).setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(2)).setFixedWidth(60).setPattern("#,##0.00")
				.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT));

		myreport.addColumn(DynamicReports.col.column("เครบิต", new AbstractSimpleExpression<BigDecimal>() {
			@Override
			public BigDecimal evaluate(ReportParameters reportParameters) {

				BigDecimal mm = (BigDecimal) reportParameters.getValue("NUM_TYPE");
				if (mm.intValue() < 0) {
					return reportParameters.getValue("AMT");
				} else {
					return null;
				}

			}
		}).setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(2)).setFixedWidth(60).setPattern("#,##0.00")
				.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT));

		myreport.addColumn(DynamicReports.col.column("คำอธิบายรายการ", new AbstractComplexExpression<String>() {
			@Override
			public String evaluate(java.util.List<?> values, ReportParameters reportParameters) {
				return Fnc.getStr(reportParameters.getFieldValue("DESCR")) + " "
						+ Fnc.getStr(reportParameters.getFieldValue("DESCR_SUB"));
			}
		}).setStyle(styleDefault));
		// == end เพิ่มคอลัมภ์

		return myreport;

	}

	private static void addVaribleToReport(JasperReportBuilder myreport) {

		VariableBuilder<BigDecimal> varSUMDR = DynamicReports.variable("varSUMDR",
				new AbstractSimpleExpression<BigDecimal>() {

					private static final long serialVersionUID = 1L;

					@Override
					public BigDecimal evaluate(ReportParameters reportParameters) {
						BigDecimal mm = (BigDecimal) reportParameters.getValue("NUM_TYPE");
						if (mm.intValue() > 0) {
							return Fnc.getBigDecimal(reportParameters.getFieldValue("AMT"));
						} else {
							return BigDecimal.ZERO;
						}
					}
				}, Calculation.SUM);
		varSUMDR.setResetType(Evaluation.REPORT);
		varSUMDR.setResetGroup(null);
		myreport.addVariable(varSUMDR);

		VariableBuilder<BigDecimal> varSUMCR = DynamicReports.variable("varSUMCR",
				new AbstractSimpleExpression<BigDecimal>() {

					private static final long serialVersionUID = 1L;

					@Override
					public BigDecimal evaluate(ReportParameters reportParameters) {
						BigDecimal mm = (BigDecimal) reportParameters.getValue("NUM_TYPE");
						if (mm.intValue() < 0) {
							return Fnc.getBigDecimal(reportParameters.getFieldValue("AMT"));
						} else {
							return BigDecimal.ZERO;
						}
					}
				}, Calculation.SUM);
		varSUMCR.setResetType(Evaluation.REPORT);
		varSUMCR.setResetGroup(null);
		myreport.addVariable(varSUMCR);

	}

	private static void addFieldToReport(JasperReportBuilder myreport) {
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
	}

	/**
	 * ตรวจว่ามีตัวย่อยหรือไม่
	 * 
	 * @param detail    ต้องระบุ ACCT_ID และ NUM_TYPE เพราะจะใช้ในการตรวจสอบ
	 *                  นอกนั้นไม่ต้องก็ได้
	 * @param loginBean
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public static boolean isSUB_HAS(TboACGL_DETAIL detail, LoginBean loginBean) throws SQLException, Exception {

		try (FDbc dbc = FDbc.connectMasterDb()) {
			int[] acct_type = { 0 };
			return isSUB_HAS(dbc, detail, loginBean, acct_type);
		}

	}

	/**
	 * ตรวจว่ามีตัวย่อยหรือไม่
	 * 
	 * @param dbc
	 * @param detail    ต้องระบุ ACCT_ID และ NUM_TYPE เพราะจะใช้ในการตรวจสอบ
	 *                  นอกนั้นไม่ต้องก็ได้
	 * @param loginBean
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public static boolean isSUB_HAS(FDbc dbc, TboACGL_DETAIL detail, LoginBean loginBean)
			throws SQLException, Exception {

		int[] acct_type = { 0 };
		return isSUB_HAS(dbc, detail, loginBean, acct_type);

	}

	/**
	 * ตรวจว่ามีตัวย่อยหรือไม่
	 * 
	 * @param detail    ต้องระบุ ACCT_ID และ NUM_TYPE เพราะจะใช้ในการตรวจสอบ
	 *                  นอกนั้นไม่ต้องก็ได้
	 * @param loginBean
	 * @param acct_type ตัวแปรรับค่ากลับ (1=ภาษีซื้อ, 2=ภาษีซื้อรอตัด, 3=ภาษีขาย,
	 *                  4=ภาษีขายรอตัด, 5=รหัสบัญชีภาษีถูกหัก ณ ที่จ่าย ,6=รหัสบัญชี
	 *                  ภงด.3 , 7=รหัสบัญชี ภงด.53 , 20=เจ้าหนี้มีตัวย่อย,
	 *                  21=ลูกหนี้มีตัวย่อย, 22=เช็คจ่ายล่วงหน้า)
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public static boolean isSUB_HAS(TboACGL_DETAIL detail, LoginBean loginBean, int[] acct_type)
			throws SQLException, Exception {

		try (FDbc dbc = FDbc.connectMasterDb()) {
			return isSUB_HAS(dbc, detail, loginBean, acct_type);
		}

	}

	/**
	 * ตรวจว่ามีตัวย่อยหรือไม่
	 * 
	 * @param dbc
	 * @param detail    ต้องระบุ ACCT_ID และ NUM_TYPE เพราะจะใช้ในการตรวจสอบ
	 *                  นอกนั้นไม่ต้องก็ได้
	 * @param loginBean
	 * @param acct_type ตัวแปรรับค่ากลับ (1=ภาษีซื้อ, 2=ภาษีซื้อรอตัด, 3=ภาษีขาย,
	 *                  4=ภาษีขายรอตัด, 5=รหัสบัญชีภาษีถูกหัก ณ ที่จ่าย ,6=รหัสบัญชี
	 *                  ภงด.3 , 7=รหัสบัญชี ภงด.53 ,20=เจ้าหนี้มีตัวย่อย,
	 *                  21=ลูกหนี้มีตัวย่อย, 22=เช็คจ่ายล่วงหน้า)
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public static boolean isSUB_HAS(FDbc dbc, TboACGL_DETAIL detail, LoginBean loginBean, int[] acct_type)
			throws SQLException, Exception {

		boolean ret = false;

		if (!Fnc.isEmpty(detail.getACCT_ID())) {

			if (TbmFPARA_COMP.getString(dbc, loginBean.getCOMP_CDE(), "AC_ACCITD_TAX_PUR").trim().equals(detail.getACCT_ID())) {// == กรณีเป็นภาษีซื้อ
				acct_type[0] = 1;
				ret = true;

			} else if (TbmFPARA_COMP.getString(dbc, loginBean.getCOMP_CDE(), "AC_ACCITD_TAX_PUR_WAIT").trim().equals(detail.getACCT_ID())) {// == กรณีเป็นภาษีซื้อรอตัด
				acct_type[0] = 2;
				ret = true;

			} else if (TbmFPARA_COMP.getString(dbc, loginBean.getCOMP_CDE(), "AC_ACCITD_TAX_SALE").trim().equals(detail.getACCT_ID())) {// == กรณีเป็นภาษีขาย
				acct_type[0] = 3;
				ret = true;

			} else if (TbmFPARA_COMP.getString(dbc, loginBean.getCOMP_CDE(), "AC_ACCITD_TAX_SALE_WAIT").trim().equals(detail.getACCT_ID())) {// == กรณีเป็นภาษีขายรอตัด
				acct_type[0] = 4;
				ret = true;

			} else if (TbmFPARA_COMP.getString(dbc, loginBean.getCOMP_CDE(), "AC_ACCITD_TAX_DR").trim().equals(detail.getACCT_ID())) {// == รหัสบัญชีภาษีถูกหัก ณ ที่จ่าย
				if (detail.getNUM_TYPE().intValue() == 1) { // ขาเดบิต
					acct_type[0] = 5;
					ret = true;
				}

			} else if (TbmFPARA_COMP.getString(dbc, loginBean.getCOMP_CDE(), "AC_ACCITD_TAX_CR_3").trim().equals(detail.getACCT_ID())) {// == รหัสบัญชี ภงด.3
				if (detail.getNUM_TYPE().intValue() == -1) { // ขาเครดิต
					acct_type[0] = 6;
					ret = true;
				}

			} else if (TbmFPARA_COMP.getString(dbc, loginBean.getCOMP_CDE(), "AC_ACCITD_TAX_CR_53").trim().equals(detail.getACCT_ID())) {// == รหัสบัญชี ภงด.53
				if (detail.getNUM_TYPE().intValue() == -1) { // ขาเครดิต
					acct_type[0] = 7;
					ret = true;
				}

			} else {

				TboACCT_NO_SUB acct_no_sub = new TboACCT_NO_SUB();

				acct_no_sub.setCOMP_CDE(loginBean.getCOMP_CDE());
				acct_no_sub.setACCT_ID(detail.getACCT_ID());

				if (TbfACCT_NO_SUB.find(dbc, acct_no_sub)) {

					if (acct_no_sub.getSUB_TYPE() == 1) { // == กรณีเป็นเจ้าหนี้
						acct_type[0] = 20;
						ret = true;
					} else if (acct_no_sub.getSUB_TYPE() == 2) {// == กรณีเป็นลูกหนี้
						acct_type[0] = 21;
						ret = true;
					} else if (acct_no_sub.getSUB_TYPE() == 3) { // == กรณีเป็นเช็คจ่ายล่วงหน้า
						if (detail.getNUM_TYPE().intValue() == -1) { // ขาเครดิต
							acct_type[0] = 22;
							ret = true;
						}
					}

				}

			}
		}

		return ret;

	}

	public static void addSUB_GL(TboACGL_DETAIL detail, LoginBean loginBean, boolean new_mode, Object callForm,
			String callBackMethodName) throws SQLException, Exception {

		int[] acct_type = { 0 };
		isSUB_HAS(detail, loginBean, acct_type);

		if (acct_type[0] == 1 || acct_type[0] == 2) {// == กรณีเป็นภาษีซื้อ หรือ กรณีเป็นภาษีซื้อรอตัด

			if (!ZkUtil.isPopup("AcglVatpur")) {

				AcglVatpur fsub = (AcglVatpur) ZkUtil.callZulFile("/com/pcc/gl/zul/AcEntr/AcglVatpur.zul");
				fsub.setId("AcglVatpur");// ต้องเรียกก่อนตัวอื่น
				fsub.setFormObj();// ต้องเรียกก่อนตัวอื่น
				fsub.setAcgl_detail(detail);
				fsub.setLoginBean(loginBean);
				fsub.setCallForm(callForm);
				fsub.setCallBackMethodName(callBackMethodName);
				fsub.setNew_mode(new_mode);
				if (acct_type[0] == 1) {
					fsub.setTax_type(2);
				} else {
					fsub.setTax_type(1);
				}
				fsub.read_recode();
				fsub.doModal();

			}

		} else if (acct_type[0] == 3 || acct_type[0] == 4) {// == กรณีเป็นภาษีขาย หรือ กรณีเป็นภาษีขายรอตัด

			if (!ZkUtil.isPopup("AcglVatsale")) {
				AcglVatsale fsub = (AcglVatsale) ZkUtil.callZulFile("/com/pcc/gl/zul/AcEntr/AcglVatsale.zul");
				fsub.setId("AcglVatsale");// ต้องเรียกก่อนตัวอื่น
				fsub.setFormObj();// ต้องเรียกก่อนตัวอื่น
				fsub.setAcgl_detail(detail);
				fsub.setLoginBean(loginBean);
				fsub.setCallForm(callForm);
				fsub.setCallBackMethodName(callBackMethodName);
				fsub.setNew_mode(new_mode);
				if (acct_type[0] == 3) { // กรณีเป็นภาษีขาย
					fsub.setTax_type(2);
				} else { // กรณีเป็นภาษีขายรอตัด
					fsub.setTax_type(1);
				}
				fsub.read_recode();
				fsub.doModal();
			}

		} else if (acct_type[0] == 5) {// == รหัสบัญชีภาษีถูกหัก ณ ที่จ่าย

			if (detail.getNUM_TYPE().intValue() == 1) { // ขาเดบิต
				if (!ZkUtil.isPopup("AcglVatWhDr")) {

					AcglVatWhDr fsub = (AcglVatWhDr) ZkUtil.callZulFile("/com/pcc/gl/zul/AcEntr/AcglVatWhDr.zul");
					fsub.setId("AcglVatWhDr");// ต้องเรียกก่อนตัวอื่น
					fsub.setFormObj();// ต้องเรียกก่อนตัวอื่น
					fsub.setAcgl_detail(detail);
					fsub.setLoginBean(loginBean);
					fsub.setCallForm(callForm);
					fsub.setCallBackMethodName(callBackMethodName);
					fsub.setNew_mode(new_mode);
					fsub.read_recode();
					fsub.doModal();

				}
			}

		} else if (acct_type[0] == 6 || acct_type[0] == 7) {// == 6=รหัสบัญชี ภงด.3 , 7=รหัสบัญชี ภงด.53

			if (detail.getNUM_TYPE().intValue() == -1) { // ขาเครดิต
				if (!ZkUtil.isPopup("AcglVatWhCr")) {

					AcglVatWhCr fsub = (AcglVatWhCr) ZkUtil.callZulFile("/com/pcc/gl/zul/AcEntr/AcglVatWhCr.zul");
					fsub.setId("AcglVatWhCr");// ต้องเรียกก่อนตัวอื่น
					fsub.setFormObj();// ต้องเรียกก่อนตัวอื่น
					fsub.setAcgl_detail(detail);
					fsub.setLoginBean(loginBean);
					fsub.setCallForm(callForm);
					fsub.setCallBackMethodName(callBackMethodName);
					fsub.setNew_mode(new_mode);
					if (acct_type[0] == 6) { // ภงด.3
						fsub.setDoc_type(1);
					} else { // ภงด.53
						fsub.setDoc_type(2);
					}
					fsub.read_recode();
					fsub.doModal();

				}
			}

		} else if (acct_type[0] == 20) {// == กรณีเป็นเจ้าหนี้

			if (!ZkUtil.isPopup("AcglAp")) {

				AcglAp fsub = (AcglAp) ZkUtil.callZulFile("/com/pcc/gl/zul/AcEntr/AcglAp.zul");
				fsub.setId("AcglAp");// ต้องเรียกก่อนตัวอื่น
				fsub.setFormObj();// ต้องเรียกก่อนตัวอื่น
				fsub.setAcgl_detail(detail);
				fsub.setLoginBean(loginBean);
				fsub.setCallForm(callForm);
				fsub.setCallBackMethodName(callBackMethodName);
				fsub.setNew_mode(new_mode);
				fsub.read_recode();
				fsub.doModal();

			}

		} else if (acct_type[0] == 21) {// == กรณีเป็นลูกหนี้

			if (!ZkUtil.isPopup("AcglAr")) {

				AcglAr fsub = (AcglAr) ZkUtil.callZulFile("/com/pcc/gl/zul/AcEntr/AcglAr.zul");
				fsub.setId("AcglAr");// ต้องเรียกก่อนตัวอื่น
				fsub.setFormObj();// ต้องเรียกก่อนตัวอื่น
				fsub.setAcgl_detail(detail);
				fsub.setLoginBean(loginBean);
				fsub.setCallForm(callForm);
				fsub.setCallBackMethodName(callBackMethodName);
				fsub.setNew_mode(new_mode);
				fsub.read_recode();
				fsub.doModal();

			}

		} else if (acct_type[0] == 22) { // == กรณีเป็นเช็คจ่ายล่วงหน้า

			if (detail.getNUM_TYPE().intValue() == -1) { // ขาเครดิต

				if (!ZkUtil.isPopup("AcglChq")) {

					AcglChq fsub = (AcglChq) ZkUtil.callZulFile("/com/pcc/gl/zul/AcEntr/AcglChq.zul");
					fsub.setId("AcglChq");// ต้องเรียกก่อนตัวอื่น
					fsub.setFormObj();// ต้องเรียกก่อนตัวอื่น
					fsub.setAcgl_detail(detail);
					fsub.setLoginBean(loginBean);
					fsub.setCallForm(callForm);
					fsub.setCallBackMethodName(callBackMethodName);
					fsub.setNew_mode(new_mode);
					fsub.read_recode();
					fsub.doModal();

				}

			}

		}

	}

	public static void update_RECSTA(FDbc dbc, int recsta, LoginBean loginBean, String vou_type, String vou_no)
			throws SQLException {

		{// == update acgl_ap.RECSTA
			String sql_update_ap = " update " + TboACGL_AP.tablename + " set RECSTA = ? "
					+ " where COMP_CDE = ? and VOU_TYPE = ? and VOU_NO = ? ";
			int eff = dbc.executeSql2(sql_update_ap, recsta, loginBean.getCOMP_CDE(), vou_type, vou_no);
			logger.info("Effect :" + eff);
		}

		{// == update acgl_ar.RECSTA
			String sql_update_ar = " update " + TboACGL_AR.tablename + " set RECSTA = ? "
					+ " where COMP_CDE = ? and VOU_TYPE=? and VOU_NO= ? ";
			int eff = dbc.executeSql2(sql_update_ar, recsta, loginBean.getCOMP_CDE(), vou_type, vou_no);
			logger.info("Effect :" + eff);
		}

		{// == update acgl_vatpur.RECSTA
			String sql_update_vatpure = " update " + TboACGL_VATPUR.tablename + " set RECSTA=? "
					+ " where COMP_CDE=? and VOU_TYPE=? and VOU_NO= ? ";
			int eff = dbc.executeSql2(sql_update_vatpure, recsta, loginBean.getCOMP_CDE(), vou_type, vou_no);
			logger.info("Effect :" + eff);
		}

		{// == update acgl_vatsale.RECSTA
			String sql_update_vatsale = " update " + TboACGL_VATSALE.tablename + " set RECSTA=? "
					+ " where COMP_CDE=? and VOU_TYPE=? and VOU_NO= ? ";
			int eff = dbc.executeSql2(sql_update_vatsale, recsta, loginBean.getCOMP_CDE(), vou_type, vou_no);
			logger.info("Effect :" + eff);
		}

		{// == update acgl_vatwh_cr.RECSTA
			String sql_update_vatwh_cr = " update " + TboACGL_VATWH_CR.tablename + " set RECSTA=? "
					+ " where COMP_CDE=? and VOU_TYPE=? and VOU_NO=? ";
			int eff = dbc.executeSql2(sql_update_vatwh_cr, recsta, loginBean.getCOMP_CDE(), vou_type, vou_no);
			logger.info("Effect :" + eff);
		}

		{// == update acgl_vatwh_dr.RECSTA
			String sql_update_vatwh_dr = " update " + TboACGL_VATWH_DR.tablename + " set RECSTA=? "
					+ " where COMP_CDE=? and VOU_TYPE=? and VOU_NO=? ";
			int eff = dbc.executeSql2(sql_update_vatwh_dr, recsta, loginBean.getCOMP_CDE(), vou_type, vou_no);
			logger.info("Effect :" + eff);
		}

	}

	public static void delete_vou_no(FDbc dbc, LoginBean loginBean, String vou_type, String vou_no)
			throws SQLException, Exception {

		// == update acgl_header
		TboACGL_HEADER acch = new TboACGL_HEADER();

		acch.setCOMP_CDE(loginBean.getCOMP_CDE());
		acch.setVOU_TYPE(vou_type);
		acch.setVOU_NO(vou_no);

		if (TbfACGL_HEADER.find(dbc, acch)) {

			acch.setRECSTA(9);
			acch.setUPBY(loginBean.getUSER_ID());
			acch.setUPDT(FnDate.getTodaySqlDateTime());
			acch.setAPPR_BY("");
			acch.setAPPR_DT(null);

			if (!TbfACGL_HEADER.update(dbc, acch, "")) {
				throw new Exception("ไม่สามารถบันทึกรายการได้กรุณาตรวจสอบ [UPDATE acgl_header]");
			}

		} else {
			throw new Exception("ไม่พบรายการ");
		}

		{// == ตรวจการ WD_CHQ
			String sql = " select count(*) as f1 from " + TboACGL_DETAIL.tablename
					+ " where COMP_CDE = ? and VOU_TYPE = ? and VOU_NO = ? "
					+ " and coalesce(CHQ_TYPE,'') = '1' and coalesce(CHQ_WD_VOU_NO,'') != '' ";
			try (java.sql.ResultSet rs = dbc.getResultSet2(sql, loginBean.getCOMP_CDE(), vou_type, vou_no);) {
				if (rs.next()) {
					if (rs.getInt(1) > 0) {
						throw new Exception("มีการล้างเช็คจ่ายล่วงหน้าไปแล้ว");
					}
				}
			}
		}

		// == delete acgl_detail
		TbmACGL_DETAIL.delete_by_vouno(dbc, loginBean.getCOMP_CDE(), vou_type, vou_no);

		{// == loop acgl_ap ที่เป็นขาล้าง เพื่อคืนค่าที่
			// ส่วนขาตั้งให้ลบได้ถ้ายังไม่ถูกล้าง
			String sql = "select * from " + TboACGL_AP.tablename
					+ " where COMP_CDE = ? and VOU_TYPE = ? and VOU_NO = ? ";
			try (java.sql.ResultSet rs = dbc.getResultSet2(sql, loginBean.getCOMP_CDE(), vou_type, vou_no);) {
				while (rs.next()) {

					TboACGL_AP ap = new TboACGL_AP();
					TbfACGL_AP.setModel(rs, ap);

					if (ap.getPOST_TYPE() == 1) { // ขาตั้ง
						if (Fnc.getBigDecimal(ap.getCLEAR_AMT()).compareTo(BigDecimal.ZERO) != 0) {
							throw new Exception("เอกสารเลขที่ " + ap.getDOCNO() + " มีการล้างยอดไปแล้ว");
						}
						TbfACGL_AP.delete(dbc, ap);

					} else if (ap.getPOST_TYPE() == 2) {// ขาล้าง
						restore_ap_clear_amt(dbc, ap);
					}

				}
			}

		}

		{// == loop acgl_ar ที่เป็นขาล้าง เพื่อคืนค่าที่
			// ส่วนขาตั้งให้ลบได้ถ้ายังไม่ถูกล้าง
			String sql = "select * from " + TboACGL_AR.tablename
					+ " where COMP_CDE = ? and VOU_TYPE = ? and VOU_NO = ? ";
			try (java.sql.ResultSet rs = dbc.getResultSet2(sql, loginBean.getCOMP_CDE(), vou_type, vou_no);) {
				while (rs.next()) {

					TboACGL_AR ar = new TboACGL_AR();
					TbfACGL_AR.setModel(rs, ar);

					if (ar.getPOST_TYPE() == 1) { // ขาตั้ง
						if (Fnc.getBigDecimal(ar.getCLEAR_AMT()).compareTo(BigDecimal.ZERO) != 0) {
							throw new Exception("เอกสารเลขที่ " + ar.getDOCNO() + " มีการล้างยอดไปแล้ว");
						}
						TbfACGL_AR.delete(dbc, ar);

					} else if (ar.getPOST_TYPE() == 2) {// ขาล้าง
						restore_ar_clear_amt(dbc, ar);
					}

				}
			}
		}

		{// == loop delete acgl_vatpur ภาษีซื้อ และ ภาษีซื้อรอตัด
			// จังหวะนี้จะมีแต่ขาตั้งถ้าเป็นภาษีซื้อรอตัด
			String sql = "select * from " + TboACGL_VATPUR.tablename
					+ " where COMP_CDE = ? and VOU_TYPE = ? and VOU_NO = ? ";
			try (java.sql.ResultSet rs = dbc.getResultSet2(sql, loginBean.getCOMP_CDE(), vou_type, vou_no);) {
				while (rs.next()) {

					TboACGL_VATPUR vatpur = new TboACGL_VATPUR();
					TbfACGL_VATPUR.setModel(rs, vatpur);

					if (vatpur.getTAX_TYPE() == 1) { // 1=ภาษีซื้อรอตัด ,2=ภาษีซื้อ
						if (Fnc.getBigDecimal(vatpur.getCLEAR_AMT()).compareTo(BigDecimal.ZERO) != 0
								&& vatpur.getPOST_TYPE() == 1) { // POST_TYPE = 1 คือ ขาตั้งหนี้
							throw new Exception("เอกสารเลขที่ " + vatpur.getDOCNO() + " มีการล้างยอดไปแล้ว");
						}
						TbfACGL_VATPUR.delete(dbc, vatpur);

					} else {// 2=ภาษีซื้อ
						TbfACGL_VATPUR.delete(dbc, vatpur);
					}

				}
			}
		}

		{// == loop delete acgl_vatsale ภาษีขาย และ ภาษีขายรอตัด
			String sql = "select * from " + TboACGL_VATSALE.tablename
					+ " where COMP_CDE = ? and VOU_TYPE = ? and VOU_NO = ? ";
			try (java.sql.ResultSet rs = dbc.getResultSet2(sql, loginBean.getCOMP_CDE(), vou_type, vou_no);) {
				while (rs.next()) {

					TboACGL_VATSALE vatsale = new TboACGL_VATSALE();
					TbfACGL_VATSALE.setModel(rs, vatsale);

					if (vatsale.getTAX_TYPE() == 1) { // 1=ภาษีขายรอตัด ,2=ภาษีขาย
						if (Fnc.getBigDecimal(vatsale.getCLEAR_AMT()).compareTo(BigDecimal.ZERO) != 0
								&& vatsale.getPOST_TYPE() == 1) { // POST_TYPE = 1 คือ ขาตั้งหนี้
							throw new Exception("เอกสารเลขที่ " + vatsale.getDOCNO() + " มีการล้างยอดไปแล้ว");
						}
						TbfACGL_VATSALE.delete(dbc, vatsale);

					} else {// TAX_TYPE = 2 คือ ภาษีขาย
						TbfACGL_VATSALE.delete(dbc, vatsale);
					}

				}
			}
		}

		{// == loop delete acgl_vatwh_cr ภาษีหัก ณ ที่จ่าย
			String sql = "select * from " + TboACGL_VATWH_CR.tablename
					+ " where COMP_CDE = ? and VOU_TYPE = ? and VOU_NO = ? ";
			try (java.sql.ResultSet rs = dbc.getResultSet2(sql, loginBean.getCOMP_CDE(), vou_type, vou_no);) {
				while (rs.next()) {

					TboACGL_VATWH_CR vatwh_cr = new TboACGL_VATWH_CR();
					TbfACGL_VATWH_CR.setModel(rs, vatwh_cr);

					if (!Fnc.isEmpty(vatwh_cr.getDOCNO())) { // DOCNO มีเลขเอกสารที่ gen แล้ว
						if (vatwh_cr.getDOC_TYPE() == 1) {// 1=ภงด. 3 (บุคคลธรรมดา), 2=ภงด.53 (นิติบุคคล)
							throw new Exception("มีการพิมพ์ ภงด. 3 แล้ว");
						} else {
							throw new Exception("มีการพิมพ์ ภงด. 53 แล้ว");
						}
					}
					TbfACGL_VATWH_CR.delete(dbc, vatwh_cr);

				}
			}
		}

		{// == delete acgl_vatwh_dr ภาษีถูกหัก ณ ที่จ่าย
			String sql = "delete from " + TboACGL_VATWH_DR.tablename
					+ " where COMP_CDE = ?  and VOU_TYPE = ? and VOU_NO = ? ";
			int eff = dbc.executeSql2(sql, loginBean.getCOMP_CDE(), vou_type, vou_no);
			logger.info("Effect : " + eff);
		}

	}

	public static void restore_ap_clear_amt(FDbc dbc, TboACGL_DETAIL detail) throws SQLException, Exception {

		TboACGL_AP ap = new TboACGL_AP();

		ap.setCOMP_CDE(detail.getCOMP_CDE());
		ap.setVOU_TYPE(detail.getVOU_TYPE());
		ap.setVOU_NO(detail.getVOU_NO());
		ap.setVOU_SEQ(detail.getVOU_SEQ());
		ap.setVOU_DSEQ(1);

		if (TbfACGL_AP.find(dbc, ap)) {
			restore_ap_clear_amt(dbc, ap);
		}

	}

	public static void restore_ap_clear_amt(FDbc dbc, TboACGL_AP ap) throws SQLException, Exception {

		// restore ค่าคืน
		{// == loop acgl_ap ที่เป็นขาตั้ง
			String sql = "select * from " + TboACGL_AP.tablename
					+ " where COMP_CDE = ? and ACCT_ID = ? and CUST_CDE = ? and LINK_NO = ? and POST_TYPE=1 ";

			try (java.sql.ResultSet rs = dbc.getResultSet4(sql, 1, ap.getCOMP_CDE(), ap.getACCT_ID(), ap.getCUST_CDE(), ap.getLINK_NO());) {
				if (rs.next()) {

					TboACGL_AP ap_type1 = new TboACGL_AP();
					TbfACGL_AP.setModel(rs, ap_type1);

					BigDecimal rem = Fnc.getBigDecimal(ap_type1.getCLEAR_AMT()).abs().subtract(ap.getAMT().abs()); // ยอดล้างไป-ยอดล้าง
					if (rem.compareTo(BigDecimal.ZERO) < 0) {
						rem = BigDecimal.ZERO;
					}
					BigDecimal old_clear_amt = ap_type1.getCLEAR_AMT();
					ap_type1.setCLEAR_AMT(rem);
					if (!TbfACGL_AP.update(dbc, ap_type1, " CLEAR_AMT = " + Fnc.getStrNumber(old_clear_amt, "##0.000"))) {
						throw new Exception("ไม่สามารถบันทึกรายการได้ [update acgl_ap.CLEAR_AMT]");
					}

				}
			}

		}

		// ลบ ap
		TbfACGL_AP.delete(dbc, ap);

	}

	public static void restore_ar_clear_amt(FDbc dbc, TboACGL_DETAIL detail) throws SQLException, Exception {

		TboACGL_AR ar = new TboACGL_AR();

		ar.setCOMP_CDE(detail.getCOMP_CDE());
		ar.setVOU_TYPE(detail.getVOU_TYPE());
		ar.setVOU_NO(detail.getVOU_NO());
		ar.setVOU_SEQ(detail.getVOU_SEQ());
		ar.setVOU_DSEQ(1);

		if (TbfACGL_AR.find(dbc, ar)) {
			restore_ar_clear_amt(dbc, ar);
		}

	}

	public static void restore_ar_clear_amt(FDbc dbc, TboACGL_AR ar) throws SQLException, Exception {

		// restore ค่าคืน
		{// == loop acgl_ar ที่เป็นขาตั้ง
			String sql = "select * from " + TboACGL_AR.tablename
					+ " where COMP_CDE = ? and ACCT_ID = ? and CUST_CDE = ? " + " and LINK_NO = ? and POST_TYPE=1 ";
			try (java.sql.ResultSet rs = dbc.getResultSet4(sql, 1, ar.getCOMP_CDE(), ar.getACCT_ID(), ar.getCUST_CDE(),
					ar.getLINK_NO());) {

				if (rs.next()) {

					TboACGL_AR ar_type1 = new TboACGL_AR();
					TbfACGL_AR.setModel(rs, ar_type1);

					BigDecimal rem = Fnc.getBigDecimal(ar_type1.getCLEAR_AMT()).abs().subtract(ar.getAMT().abs()); // ยอดล้างไป
																													// -
																													// ยอดล้าง
					if (rem.compareTo(BigDecimal.ZERO) < 0) {
						rem = BigDecimal.ZERO;
					}
					BigDecimal old_clear_amt = ar_type1.getCLEAR_AMT();
					ar_type1.setCLEAR_AMT(rem);
					if (!TbfACGL_AR.update(dbc, ar_type1, " CLEAR_AMT = " + Fnc.getStrNumber(old_clear_amt, "##0.000"))) {
						throw new Exception("ไม่สามารถบันทึกรายการได้ [update acgl_ap.CLEAR_AMT]");
					}

				}

			}

		}

		// ลบ ar
		TbfACGL_AR.delete(dbc, ar);
	}

	public static void findAp(String cust_cde, String docno, Date postdate_from, Date postdate_to, String vou_type,
			String vou_no, List<FModelHasMap> lst_find, LoginBean loginBean) throws Exception {

		try (FDbc dbc = FDbc.connectMasterDb()) {

			lst_find.clear();

			SqlStr sql = new SqlStr();
			sql.addLine("select cc.COMP_CDE, cc.VOU_TYPE, cc.VOU_NO, cc.VOU_SEQ, cc.VOU_DSEQ,");
			sql.addLine(" cc.POSTDATE, cc.DOCNO, cc.DOCDATE, cc.CUST_CDE, cc.ACCT_ID, cc.LINK_NO, cc.NUM_TYPE,");
			sql.addLine(" (coalesce(cc.AMT,0)-coalesce(cc.CLEAR_AMT,0))*cc.NUM_TYPE as AMT,");
			sql.addLine(" cc.DUEDATE, cc.DESCR, bb.TITLE, bb.FNAME,bb.LNAME");
			sql.addLine("from " + TboACGL_AP.tablename + " cc");
			sql.addLine("left join " + TboFCUS.tablename + " bb on cc.COMP_CDE=bb.COMP_CDE and cc.CUST_CDE=bb.CUST_CDE");
			sql.addLine("where cc.COMP_CDE='" + Fnc.sqlQuote(loginBean.getCOMP_CDE()) + "' ");
			sql.addLine("and cc.AMT != coalesce(cc.CLEAR_AMT,0) and cc.POST_TYPE=1 and cc.RECSTA=2 ");
			if (!Fnc.isEmpty(cust_cde)) {
				sql.addLine("and cc.CUST_CDE='" + Fnc.sqlQuote(cust_cde) + "' ");
			}
			if (!Fnc.isEmpty(docno)) {
				sql.addLine("and cc.DOCNO like '%" + Fnc.sqlQuote(docno) + "%' ");
			}
			if (postdate_from != null && postdate_to != null) {
				sql.addLine("and cc.POSTDATE between '" + FnDate.getDateString(postdate_from) + "' and '" + FnDate.getDateString(postdate_to) + "' ");
			} else if (postdate_from != null) {
				sql.addLine("and cc.POSTDATE >= '" + FnDate.getDateString(postdate_from) + "' ");
			} else if (postdate_to != null) {
				sql.addLine("and cc.POSTDATE <= '" + FnDate.getDateString(postdate_to) + "' ");
			}
			if (!Fnc.isEmpty(vou_type)) {
				sql.addLine("and cc.VOU_TYPE = '" + Fnc.sqlQuote(vou_type) + "' ");
			}
			if (!Fnc.isEmpty(vou_no)) {
				sql.addLine("and cc.VOU_NO like '%" + Fnc.sqlQuote(vou_no) + "%' ");
			}

			sql.addLine("order by cc.VOU_TYPE,cc.VOU_NO,cc.VOU_SEQ");

			logger.info(sql.getSql());

			try (java.sql.ResultSet rs = dbc.getResultSetFw3(sql.getSql(), 5000);) {
				while (rs.next()) {
					FModelHasMap dat = new FModelHasMap();
					dat.putRecord(rs);
					lst_find.add(dat);
				}
			}

		}

	}

	public static void addPayAp(List<FModelHasMap> lst_select, TboACGL_HEADER acgl_header, LoginBean loginBean)
			throws Exception {

		try (FDbc dbc = FDbc.connectMasterDb()) {

			dbc.beginTrans();

			if (lst_select.size() == 0) {
				throw new Exception("ยังไม่เลือกรายการ");
			}

			int seq = 0;
			try (java.sql.PreparedStatement ps_find = TbfACGL_AP.getPrepareStmFind(dbc);) {

				int[] ret_vouseq = { 0 };
				int[] ret_vouseq_show = { 0 };
				TbmACGL_DETAIL.get_max_vouseq_by_vouno(dbc, loginBean.getCOMP_CDE(), acgl_header.getVOU_TYPE(),
						acgl_header.getVOU_NO(), ret_vouseq, ret_vouseq_show);

				ret_vouseq[0]++;
				ret_vouseq_show[0]++;

				for (FModelHasMap dat : lst_select) {

					seq++;
					TboACGL_AP ap_type1ForUpdate = new TboACGL_AP();// เก็บขาตั้งเดิมสำหรับ update
					TboACGL_AP ap_type1 = new TboACGL_AP();// เก็บขาตั้งเดิม
					TboACGL_AP ap_type2 = new TboACGL_AP();// สำหรับขาล้างสำหรับ insert

					MyDecimalbox decAMT_PAY = (MyDecimalbox) dat.get("decAMT_PAY");

					if (decAMT_PAY.getValue().abs().compareTo(BigDecimal.ZERO) == 0) {
						throw new Exception("ลำดับที่ " + seq + " จำนวนจ่ายไม่ถูกต้อง");
					}

					// ==มีใน acgl_detail ที่เลือกไปก่อนนี้หรือไม่
					checkIsSelect_APAR(dbc, acgl_header, dat, seq, 1, loginBean);

					// ==ค้นหา ap ขาตั้ง
					int idx = 1;
					ps_find.clearParameters();

					ps_find.setString(idx++, loginBean.getCOMP_CDE());
					ps_find.setString(idx++, dat.getString("VOU_TYPE"));
					ps_find.setString(idx++, dat.getString("VOU_NO"));
					ps_find.setInt(idx++, dat.getInt("VOU_SEQ"));
					ps_find.setInt(idx++, dat.getInt("VOU_DSEQ"));
					try (java.sql.ResultSet rs = ps_find.executeQuery();) {
						if (rs.next()) {
							TbfACGL_AP.setModel(rs, ap_type1ForUpdate);
							TbfACGL_AP.setModel(rs, ap_type1);
							TbfACGL_AP.setModel(rs, ap_type2);
						} else {
							throw new Exception("ไม่พบลำดับที่ " + seq + " ในระบบ");
						}
					}

					// == วันที่ขาตั้งต้อง > วันที่ล้างไม่ได้
					if (ap_type1.getPOSTDATE().after(acgl_header.getPOSTDATE())) {
						throw new Exception("ลำดับที่ " + seq + " วันที่ขาตั้งหนี้อยู่หลังวันที่ล้างหนี้ไม่ได้");
					}

					if (ap_type1.getAMT().compareTo(BigDecimal.ZERO) < 0
							|| Fnc.getBigDecimalValue(ap_type1.getCLEAR_AMT()).compareTo(BigDecimal.ZERO) < 0) {
						throw new Exception("ลำดับที่ " + seq + " ข้อมูลขาตั้งหนี้ไม่ถูกต้อง");
					}

					if (ap_type1.getNUM_TYPE().intValue() == 1) { // ยอดตั้งเป็นค่าบวก ยอด key ล้างต้องมากกว่า 0
						if (decAMT_PAY.getValue().compareTo(BigDecimal.ZERO) < 0) {
							throw new Exception("ลำดับที่ " + seq + " ยอดล้างต้องเป็นบวก");
						}
					} else { // ยอดตั้งเป็นค่าลบ ยอด key ล้างต้องน้อยกว่า 0
						if (decAMT_PAY.getValue().compareTo(BigDecimal.ZERO) > 0) {
							throw new Exception("ลำดับที่ " + seq + " ยอดล้างต้องเป็นลบ");
						}
					}

					BigDecimal new_clear_amt = Fnc.getBigDecimalValue(ap_type1.getCLEAR_AMT())
							.add(decAMT_PAY.getValue().abs());

					if (ap_type1.getAMT().compareTo(new_clear_amt) < 0) {
						BigDecimal remain_amt = ap_type1.getAMT().subtract(ap_type1.getCLEAR_AMT())
								.multiply(ap_type1.getNUM_TYPE());
						throw new Exception("ลำดับที่ " + seq + " จำนวนจ่ายไม่ถูกต้อง (คงเหลือคือ "
								+ Fnc.getStrBigDecimal(remain_amt) + " )");
					}

					ap_type1ForUpdate.setCLEAR_AMT(new_clear_amt);

					if (!TbfACGL_AP.update(ap_type1ForUpdate,
							"CLEAR_AMT=" + Fnc.getStrNumber(ap_type1.getCLEAR_AMT(), "##0.00####"))) {
						throw new Exception("ลำดับที่ " + seq + " ไม่สามารถล้างยอดได้");
					}

					// == insert acgl_detail
					TboACGL_DETAIL acgl_detail = new TboACGL_DETAIL();
					insert_ACGL_DETAIL_For_AP(dbc, acgl_header, acgl_detail, ap_type1, decAMT_PAY.getValue().abs(), seq,
							ret_vouseq, ret_vouseq_show, loginBean);

					// == insert acgl_ap = ap_type2
					ap_type2.setVOU_TYPE(acgl_detail.getVOU_TYPE());
					ap_type2.setVOU_NO(acgl_detail.getVOU_NO());
					ap_type2.setVOU_SEQ(acgl_detail.getVOU_SEQ());
					ap_type2.setVOU_DSEQ(1);
					ap_type2.setPOSTDATE(acgl_detail.getPOSTDATE());
					ap_type2.setPOST_TYPE(2);// 1=ขาตั้งหนี้,2=ขาล้างหนี้

					if (ap_type1.getNUM_TYPE().intValue() == 1) {// ถ้าขาตั้งเป็น บวก
						ap_type2.setNUM_TYPE(BigDecimal.ONE.negate());
					} else {// ถ้าขาตั้งเป็น ลบ หรือ CN
						ap_type2.setNUM_TYPE(BigDecimal.ONE);
					}

					ap_type2.setAMT(decAMT_PAY.getValue().abs());
					ap_type2.setCLEAR_AMT(decAMT_PAY.getValue().abs());

					ap_type2.setRECSTA(0);// 0=ยังไม่บันทึก,1=ยังไม่อนุมัติ,2=อนุมัติ,9=ยกเลิก
					ap_type2.setINSBY(loginBean.getUSER_ID());
					ap_type2.setINSDT(FnDate.getTodaySqlDateTime());
					ap_type2.setUPBY(loginBean.getUSER_ID());
					ap_type2.setUPDT(FnDate.getTodaySqlDateTime());

					if (!TbfACGL_AP.insert(dbc, ap_type2)) {
						throw new Exception("ลำดับที่ " + seq + " ไม่สามารถบันทึกรายการได้ [INSERT acgl_ap]");
					}

				}

			}

			dbc.commit();

		}

	}

	public static void insert_ACGL_DETAIL_For_AP(FDbc dbc, TboACGL_HEADER acgl_header, TboACGL_DETAIL acgl_detail,
			TboACGL_AP ap_old, BigDecimal pay_amt, int seq, int[] ret_vouseq, int[] ret_vouseq_show,
			LoginBean loginBean) throws SQLException, Exception {

		acgl_detail.setCOMP_CDE(loginBean.getCOMP_CDE());
		acgl_detail.setVOU_TYPE(acgl_header.getVOU_TYPE());
		acgl_detail.setVOU_NO(acgl_header.getVOU_NO());
		acgl_detail.setVOU_SEQ(ret_vouseq[0]++);
		acgl_detail.setVOU_SEQ_SHOW(ret_vouseq_show[0]++);
		acgl_detail.setPOSTDATE(acgl_header.getPOSTDATE());
		acgl_detail.setACCT_ID(ap_old.getACCT_ID());

		if (ap_old.getNUM_TYPE().intValue() == 1) { // ตั้งเป็นบวก แสดงว่าเครดิตไว้
			acgl_detail.setNUM_TYPE(BigDecimal.ONE);// ให้ debit เจ้าหนี้ออก
		} else {// ขาตั้งเป็นการ CN เจ้าหนี้มา แสดงว่า debit ไว้
			acgl_detail.setNUM_TYPE(BigDecimal.ONE.negate());// ให้ credit เจ้าหนี้ออก
		}

		acgl_detail.setAMT(pay_amt);
		acgl_detail.setSECT_ID(ap_old.getSECT_ID());

		// ต้องดึงจากต้นทางถ้ามี
		TboACGL_DETAIL det1 = TbmACGL_DETAIL.get_record(dbc, loginBean.getCOMP_CDE(), ap_old.getVOU_TYPE(),
				ap_old.getVOU_NO(), ap_old.getVOU_SEQ());
		if (det1 != null) {
			acgl_detail.setDESCR(det1.getDESCR());
			acgl_detail.setDESCR_SUB(det1.getDESCR_SUB());
		} else {
			acgl_detail.setDESCR(ap_old.getDESCR());
		}

		acgl_detail.setRECSTA(0);
		acgl_detail.setSUB_HAS("");
		acgl_detail.setSUB_APPR("");
		acgl_detail.setSUB_APPR_BY("");
		acgl_detail.setACCT_OPEN("");
		acgl_detail.setAPAR_VOU_TYPE(ap_old.getVOU_TYPE());
		acgl_detail.setAPAR_VOU_NO(ap_old.getVOU_NO());
		acgl_detail.setAPAR_VOU_SEQ(ap_old.getVOU_SEQ());
		acgl_detail.setAPAR_VOU_DSEQ(ap_old.getVOU_DSEQ());
		acgl_detail.setAPAR_RECTYP(1);// อืนๆ=manual ,1=ล้างเจ้าหนี้,2=ล้างลูกหนี้
		acgl_detail.setAPAR_DESCR(ap_old.getDESCR());

		// คงเหลือก่อนทำรายการ
		BigDecimal old_remain_amt = ap_old.getAMT().subtract(ap_old.getCLEAR_AMT()).multiply(ap_old.getNUM_TYPE());
		acgl_detail.setAPAR_AMT(old_remain_amt);

		acgl_detail.setCHQ_TYPE("");
		acgl_detail.setCHQ_NO("");
		acgl_detail.setCHQ_DATE(null);
		acgl_detail.setCHQ_PAYEE(null);
		acgl_detail.setCHQ_WD_VOU_TYPE("");
		acgl_detail.setCHQ_WD_VOU_NO("");
		acgl_detail.setCHQ_WD_VOU_SEQ(0);
		acgl_detail.setCHQ_WD_USE(0);

		if (!TbfACGL_DETAIL.insert(dbc, acgl_detail)) {
			throw new Exception("ลำดับที่ " + seq + " ไม่สามารถบันทึกรายการ [INSERT acgl_detail]");
		}

	}

	public static void findAr(String cust_cde, String docno, Date postdate_from, Date postdate_to, String vou_type,
			String vou_no, List<FModelHasMap> lst_find, LoginBean loginBean) throws Exception {

		try (FDbc dbc = FDbc.connectMasterDb()) {
			lst_find.clear();

			SqlStr sql = new SqlStr();
			sql.addLine("select cc.COMP_CDE, cc.VOU_TYPE, cc.VOU_NO, cc.VOU_SEQ, cc.VOU_DSEQ,");
			sql.addLine(" cc.POSTDATE, cc.DOCNO, cc.DOCDATE, cc.CUST_CDE, cc.ACCT_ID, cc.LINK_NO, cc.NUM_TYPE,");
			sql.addLine(" (coalesce(cc.AMT,0)-coalesce(cc.CLEAR_AMT,0))*cc.NUM_TYPE as AMT,");
			sql.addLine(" cc.DUEDATE, cc.DESCR, bb.TITLE, bb.FNAME,bb.LNAME");
			sql.addLine("from " + TboACGL_AR.tablename + " cc");
			sql.addLine("left join " + TboFCUS.tablename + " bb on cc.COMP_CDE=bb.COMP_CDE and cc.CUST_CDE=bb.CUST_CDE");
			sql.addLine("where cc.COMP_CDE='" + Fnc.sqlQuote(loginBean.getCOMP_CDE()) + "' ");
			sql.addLine("and cc.AMT != coalesce(cc.CLEAR_AMT,0) and cc.POST_TYPE=1 and cc.RECSTA=2 ");
			if (!Fnc.isEmpty(cust_cde)) {
				sql.addLine("and cc.CUST_CDE='" + Fnc.sqlQuote(cust_cde) + "' ");
			}
			if (!Fnc.isEmpty(docno)) {
				sql.addLine("and cc.DOCNO like '%" + Fnc.sqlQuote(docno) + "%' ");
			}
			if (postdate_from != null && postdate_to != null) {
				sql.addLine("and cc.POSTDATE between '" + FnDate.getDateString(postdate_from) + "' and '" + FnDate.getDateString(postdate_to) + "' ");
			} else if (postdate_from != null) {
				sql.addLine("and cc.POSTDATE >= '" + FnDate.getDateString(postdate_from) + "' ");
			} else if (postdate_to != null) {
				sql.addLine("and cc.POSTDATE <= '" + FnDate.getDateString(postdate_to) + "' ");
			}
			if (!Fnc.isEmpty(vou_type)) {
				sql.addLine("and cc.VOU_TYPE = '" + Fnc.sqlQuote(vou_type) + "' ");
			}
			if (!Fnc.isEmpty(vou_no)) {
				sql.addLine("and cc.VOU_NO like '%" + Fnc.sqlQuote(vou_no) + "%' ");
			}

			sql.addLine("order by cc.VOU_TYPE,cc.VOU_NO,cc.VOU_SEQ");

			logger.info(sql.getSql());

			try (java.sql.ResultSet rs = dbc.getResultSetFw3(sql.getSql(), 5000);) {
				while (rs.next()) {
					FModelHasMap dat = new FModelHasMap();
					dat.putRecord(rs);
					lst_find.add(dat);
				}
			}

		}

	}

	public static void addPayAr(List<FModelHasMap> lst_select, TboACGL_HEADER acgl_header, LoginBean loginBean)
			throws Exception {

		try (FDbc dbc = FDbc.connectMasterDb()) {
			dbc.beginTrans();

			if (lst_select.size() == 0) {
				throw new Exception("ยังไม่เลือกรายการ");
			}

			int seq = 0;
			try (java.sql.PreparedStatement ps_find = TbfACGL_AR.getPrepareStmFind(dbc);) {

				int[] ret_vouseq = { 0 };
				int[] ret_vouseq_show = { 0 };
				TbmACGL_DETAIL.get_max_vouseq_by_vouno(dbc, loginBean.getCOMP_CDE(), acgl_header.getVOU_TYPE(),
						acgl_header.getVOU_NO(), ret_vouseq, ret_vouseq_show);

				ret_vouseq[0]++;
				ret_vouseq_show[0]++;

				for (FModelHasMap dat : lst_select) {

					seq++;
					TboACGL_AR ar_type1ForUpdate = new TboACGL_AR();// เก็บขาตั้งเดิมสำหรับ update
					TboACGL_AR ar_type1 = new TboACGL_AR();// เก็บขาตั้งเดิม
					TboACGL_AR ar_type2 = new TboACGL_AR();// สำหรับขาล้างสำหรับ insert

					MyDecimalbox decAMT_PAY = (MyDecimalbox) dat.get("decAMT_PAY");

					if (decAMT_PAY.getValue().abs().compareTo(BigDecimal.ZERO) == 0) {
						throw new Exception("ลำดับที่ " + seq + " จำนวนรับไม่ถูกต้อง");
					}

					// ==มีใน acgl_detail ที่เลือกไปก่อนนี้หรือไม่
					checkIsSelect_APAR(dbc, acgl_header, dat, seq, 2, loginBean);

					// ==ค้นหา ar ขาตั้ง
					int idx = 1;
					ps_find.clearParameters();

					ps_find.setString(idx++, loginBean.getCOMP_CDE());
					ps_find.setString(idx++, dat.getString("VOU_TYPE"));
					ps_find.setString(idx++, dat.getString("VOU_NO"));
					ps_find.setInt(idx++, dat.getInt("VOU_SEQ"));
					ps_find.setInt(idx++, dat.getInt("VOU_DSEQ"));
					try (java.sql.ResultSet rs = ps_find.executeQuery();) {
						if (rs.next()) {
							TbfACGL_AR.setModel(rs, ar_type1ForUpdate);
							TbfACGL_AR.setModel(rs, ar_type1);
							TbfACGL_AR.setModel(rs, ar_type2);
						} else {
							throw new Exception("ไม่พบลำดับที่ " + seq + " ในระบบ");
						}
					}

					// == วันที่ขาตั้งต้อง > วันที่ล้างไม่ได้
					if (ar_type1.getPOSTDATE().after(acgl_header.getPOSTDATE())) {
						throw new Exception("ลำดับที่ " + seq + " วันที่ขาตั้งหนี้อยู่หลังวันที่ล้างหนี้ไม่ได้");
					}

					if (ar_type1.getAMT().compareTo(BigDecimal.ZERO) < 0
							|| Fnc.getBigDecimalValue(ar_type1.getCLEAR_AMT()).compareTo(BigDecimal.ZERO) < 0) {
						throw new Exception("ลำดับที่ " + seq + " ข้อมูลขาตั้งหนี้ไม่ถูกต้อง");
					}

					if (ar_type1.getNUM_TYPE().intValue() == 1) { // ยอดตั้งเป็นค่าบวก ยอด key ล้างต้องมากกว่า 0
						if (decAMT_PAY.getValue().compareTo(BigDecimal.ZERO) < 0) {
							throw new Exception("ลำดับที่ " + seq + " ยอดล้างต้องเป็นบวก");
						}
					} else { // ยอดตั้งเป็นค่าลบ ยอด key ล้างต้องน้อยกว่า 0
						if (decAMT_PAY.getValue().compareTo(BigDecimal.ZERO) > 0) {
							throw new Exception("ลำดับที่ " + seq + " ยอดล้างต้องเป็นลบ");
						}
					}

					BigDecimal new_clear_amt = Fnc.getBigDecimalValue(ar_type1.getCLEAR_AMT())
							.add(decAMT_PAY.getValue().abs());

					if (ar_type1.getAMT().compareTo(new_clear_amt) < 0) {
						BigDecimal remain_amt = ar_type1.getAMT().subtract(ar_type1.getCLEAR_AMT())
								.multiply(ar_type1.getNUM_TYPE());
						throw new Exception("ลำดับที่ " + seq + " จำนวนรับไม่ถูกต้อง (คงเหลือคือ "
								+ Fnc.getStrBigDecimal(remain_amt) + " )");
					}

					ar_type1ForUpdate.setCLEAR_AMT(new_clear_amt);

					if (!TbfACGL_AR.update(ar_type1ForUpdate,
							"CLEAR_AMT=" + Fnc.getStrNumber(ar_type1.getCLEAR_AMT(), "##0.00####"))) {
						throw new Exception("ลำดับที่ " + seq + " ไม่สามารถล้างยอดได้");
					}

					// == insert acgl_detail
					TboACGL_DETAIL acgl_detail = new TboACGL_DETAIL();
					insert_ACGL_DETAIL_For_AR(dbc, acgl_header, acgl_detail, ar_type1, decAMT_PAY.getValue().abs(), seq,
							ret_vouseq, ret_vouseq_show, loginBean);

					// == insert acgl_ap = ap_type2
					ar_type2.setVOU_TYPE(acgl_detail.getVOU_TYPE());
					ar_type2.setVOU_NO(acgl_detail.getVOU_NO());
					ar_type2.setVOU_SEQ(acgl_detail.getVOU_SEQ());
					ar_type2.setVOU_DSEQ(1);
					ar_type2.setPOSTDATE(acgl_detail.getPOSTDATE());
					ar_type2.setPOST_TYPE(2);// 1=ขาตั้งหนี้,2=ขาล้างหนี้

					if (ar_type1.getNUM_TYPE().intValue() == 1) {// ถ้าขาตั้งเป็น บวก
						ar_type2.setNUM_TYPE(BigDecimal.ONE.negate());
					} else {// ถ้าขาตั้งเป็น ลบ หรือ CN
						ar_type2.setNUM_TYPE(BigDecimal.ONE);
					}

					ar_type2.setAMT(decAMT_PAY.getValue().abs());
					ar_type2.setCLEAR_AMT(decAMT_PAY.getValue().abs());

					ar_type2.setRECSTA(0);// 0=ยังไม่บันทึก,1=ยังไม่อนุมัติ,2=อนุมัติ,9=ยกเลิก
					ar_type2.setINSBY(loginBean.getUSER_ID());
					ar_type2.setINSDT(FnDate.getTodaySqlDateTime());
					ar_type2.setUPBY(loginBean.getUSER_ID());
					ar_type2.setUPDT(FnDate.getTodaySqlDateTime());

					if (!TbfACGL_AR.insert(dbc, ar_type2)) {
						String msg = "ลำดับที่ " + seq + " ไม่สามารถบันทึกรายการได้ [INSERT acgl_ar]";
						logger.info(msg);
						throw new Exception(msg);
					}

				}
			}

			dbc.commit();

		}

	}

	public static void insert_ACGL_DETAIL_For_AR(FDbc dbc, TboACGL_HEADER acgl_header, TboACGL_DETAIL acgl_detail,
			TboACGL_AR ar_old, BigDecimal pay_amt, int seq, int[] ret_vouseq, int[] ret_vouseq_show,
			LoginBean loginBean) throws SQLException, Exception {

		acgl_detail.setCOMP_CDE(loginBean.getCOMP_CDE());
		acgl_detail.setVOU_TYPE(acgl_header.getVOU_TYPE());
		acgl_detail.setVOU_NO(acgl_header.getVOU_NO());
		acgl_detail.setVOU_SEQ(ret_vouseq[0]++);
		acgl_detail.setVOU_SEQ_SHOW(ret_vouseq_show[0]++);
		acgl_detail.setPOSTDATE(acgl_header.getPOSTDATE());
		acgl_detail.setACCT_ID(ar_old.getACCT_ID());

		if (ar_old.getNUM_TYPE().intValue() == 1) { // ตั้งเป็นบวก คือ debit
			acgl_detail.setNUM_TYPE(BigDecimal.ONE.negate());// ให้ credit ลูกหนี้ออก
		} else {// ขาตั้งเป็นการ CN ลูกหนี้มา คือ credit
			acgl_detail.setNUM_TYPE(BigDecimal.ONE);// ให้ debit ลูกหนี้ออก
		}

		acgl_detail.setAMT(pay_amt);
		acgl_detail.setSECT_ID(ar_old.getSECT_ID());

		// ต้องดึงจากต้นทางถ้ามี
		TboACGL_DETAIL det1 = TbmACGL_DETAIL.get_record(dbc, loginBean.getCOMP_CDE(), ar_old.getVOU_TYPE(),
				ar_old.getVOU_NO(), ar_old.getVOU_SEQ());
		if (det1 != null) {
			acgl_detail.setDESCR(det1.getDESCR());
			acgl_detail.setDESCR_SUB(det1.getDESCR_SUB());
		} else {
			acgl_detail.setDESCR(ar_old.getDESCR());
		}

		acgl_detail.setRECSTA(0);
		acgl_detail.setSUB_HAS("");
		acgl_detail.setSUB_APPR("");
		acgl_detail.setSUB_APPR_BY("");
		acgl_detail.setACCT_OPEN("");
		acgl_detail.setAPAR_VOU_TYPE(ar_old.getVOU_TYPE());
		acgl_detail.setAPAR_VOU_NO(ar_old.getVOU_NO());
		acgl_detail.setAPAR_VOU_SEQ(ar_old.getVOU_SEQ());
		acgl_detail.setAPAR_VOU_DSEQ(ar_old.getVOU_DSEQ());
		acgl_detail.setAPAR_RECTYP(2);// อืนๆ=manual ,1=ล้างเจ้าหนี้,2=ล้างลูกหนี้
		acgl_detail.setAPAR_DESCR(ar_old.getDESCR());

		// คงเหลือก่อนทำรายการ
		BigDecimal old_remain_amt = ar_old.getAMT().subtract(ar_old.getCLEAR_AMT()).multiply(ar_old.getNUM_TYPE());
		acgl_detail.setAPAR_AMT(old_remain_amt);

		acgl_detail.setCHQ_TYPE("");
		acgl_detail.setCHQ_NO("");
		acgl_detail.setCHQ_DATE(null);
		acgl_detail.setCHQ_PAYEE(null);
		acgl_detail.setCHQ_WD_VOU_TYPE("");
		acgl_detail.setCHQ_WD_VOU_NO("");
		acgl_detail.setCHQ_WD_VOU_SEQ(0);
		acgl_detail.setCHQ_WD_USE(0);

		if (!TbfACGL_DETAIL.insert(dbc, acgl_detail)) {
			String msg = "ลำดับที่ " + seq + " ไม่สามารถบันทึกรายการ [INSERT acgl_detail]";
			logger.info(msg);
			throw new Exception(msg);
		}

	}

	/**
	 * มีใน acgl_detail ที่เลือกไปก่อนนี้หรือไม่
	 * 
	 * @param dbc
	 * @param acgl_header
	 * @param dat
	 * @param seq
	 * @param apar_rectyp 1=ขาล้างเจ้าหนี้,2=ข้างล้างลูกหนี้
	 * @param loginBean
	 * @throws SQLException
	 * @throws Exception
	 */
	public static void checkIsSelect_APAR(FDbc dbc, TboACGL_HEADER acgl_header, FModelHasMap dat, int seq,
			int apar_rectyp, LoginBean loginBean) throws SQLException, Exception {

		SqlStr sql_check = new SqlStr();
		sql_check.addLine("select count(*) as f1 from " + TboACGL_DETAIL.tablename);
		sql_check.addLine(" where COMP_CDE=? ");
		sql_check.addLine(" and VOU_TYPE=? ");
		sql_check.addLine(" and VOU_NO=? ");
		sql_check.addLine(" and APAR_VOU_TYPE=? ");
		sql_check.addLine(" and APAR_VOU_NO=? ");
		sql_check.addLine(" and APAR_VOU_SEQ=? ");
		sql_check.addLine(" and APAR_VOU_DSEQ=? ");
		sql_check.addLine(" and APAR_RECTYP=? ");// เป็นขาล้างของ

		int eff_rec = dbc.getRecordCount2(sql_check.getSql(), loginBean.getCOMP_CDE(), acgl_header.getVOU_TYPE(),
				acgl_header.getVOU_NO(), dat.getString("VOU_TYPE"), dat.getString("VOU_NO"), dat.getInt("VOU_SEQ"),
				dat.getInt("VOU_DSEQ"), apar_rectyp);

		if (eff_rec > 0) {
			throw new Exception("ลำดับที่ " + seq + " ถูกเลือกไปแล้ว");
		}

	}

	/**
	 * ลบตัวย่อยทุกตัวยกเว้นตารางใน ex_sub
	 * 
	 * @param dbc
	 * @param acgl_detail
	 * @param ex_sub
	 * @throws SQLException
	 */
	public static void deleteAll_VOU_SEQ(FDbc dbc, TboACGL_DETAIL acgl_detail, String ex_sub) throws SQLException {

		// == delete acgl_ap
		if (!ex_sub.toLowerCase().trim().equals("acgl_ap")) {
			TbmACGL_AP.deleteAll_VOU_SEQ(dbc, acgl_detail);
		}

		// == delete acgl_ar
		if (!ex_sub.toLowerCase().trim().equals("acgl_ar")) {
			TbmACGL_AR.deleteAll_VOU_SEQ(dbc, acgl_detail);
		}

		// == delete acgl_vatpur
		if (!ex_sub.toLowerCase().trim().equals("acgl_vatpur")) {
			TbmACGL_VATPUR.deleteAll_VOU_SEQ(dbc, acgl_detail);
		}

		// == delete acgl_vatsale
		if (!ex_sub.toLowerCase().trim().equals("acgl_vatsale")) {
			TbmACGL_VATSALE.deleteAll_VOU_SEQ(dbc, acgl_detail);
		}

		// == delete acgl_vatwh_cr
		if (!ex_sub.toLowerCase().trim().equals("acgl_vatwh_cr")) {
			TbmACGL_VATWH_CR.deleteAll_VOU_SEQ(dbc, acgl_detail);
		}

		// == delete acgl_vatwh_dr
		if (!ex_sub.toLowerCase().trim().equals("acgl_vatwh_dr")) {
			TbmACGL_VATWH_DR.deleteAll_VOU_SEQ(dbc, acgl_detail);
		}

	}

	public static JasperPrint genReportPDF_TEST(JRDataSource dataSrc, Map<String, Object> reportParams,
			LoginBean _loginBean) throws DRException {
		// test ok 1/4/65
		reportParams.put("P_COMP_NAME", _loginBean.getTboFcomp().getCOMP_NAME());
		reportParams.put("P_REPPORT_NAME", "ทดสอบชื่อ");
		reportParams.put("P_REPPORT_DESC", "ทดสอบอื่นๆ");

		MyDynamicReport rep1 = new MyDynamicReport(dataSrc, 1, PageType.A4, PageOrientation.PORTRAIT, reportParams);

		rep1.setHeader1();

		// addField
		rep1.addFieldString("ACCT_ID");
		rep1.addFieldString("ACCT_NAME");
		rep1.addFieldString("SECT_ID");
		rep1.addFieldBigdecimal("AMT");

		// สร้างตัวแปร
		rep1.createVaribleSummary1("varAMT", "AMT");

		rep1.addColumnSeq("ลำดับ", 40);
		rep1.showLabelToSumBand("รวม");

		rep1.addColumnShowBigdecimalVarible("ลำดับ1", "varAMT", 40, "#,##0.00");
		rep1.showVaribleToSumBand("varAMT");

		rep1.addColumnShowBigdecimalVarible("ลำดับ2", "varAMT", 40, "#,##0");
		rep1.showVaribleToSumBand("varAMT");

		rep1.addColumnStringCenter("รหัสบัญชี", "ACCT_ID", 50);
		rep1.addColumnStringLeft("ชื่อบัญชี", "ACCT_NAME", 250);
		rep1.addColumnStringLeft("รหัสแผนก", "SECT_ID", 50);
		rep1.addColumnBigDecimal1("จำนวน", "AMT", 70);
		rep1.showVaribleToSumBand("varAMT");

		return rep1.buildReport();

	}

}

package com.pcc.gl.progman;

import java.awt.Color;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.pcc.gl.tbf.TbfACGL_VATWH_CR;
import com.pcc.gl.tbf.TbfFCUS;
import com.pcc.gl.tbf.TbfFCUS_ADDR;
import com.pcc.gl.tbm.TbmFCUS_ADDR;
import com.pcc.gl.tbo.TboACGL_VATWH_CR;
import com.pcc.gl.tbo.TboFCUS;
import com.pcc.gl.tbo.TboFCUS_ADDR;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.DynamicRepTemplates;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FJasperPrintList;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FReport;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.SqlStr;
import com.pcc.sys.lib.ThaiBaht;
import com.pcc.sys.tbf.TbfFCOMP;
import com.pcc.sys.tbf.TbfFCOMPBRANC;
import com.pcc.sys.tbo.TboFCOMPBRANC;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.ComponentPositionType;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.StretchType;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.jasperreports.engine.JREmptyDataSource;

public class ManAcVatwhCrPrint {

	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static void getDataQry(java.util.List<FModelHasMap> dats, java.sql.Date postdateFrom,
			java.sql.Date postdateTo, String cust_cde, String vou_type, String vou_no, String doc_type, String menuId2,
			LoginBean _loginBean) throws Exception {

		dats.clear();
		try (FDbc dbc = FDbc.connectMasterDb()) {

			// from acgl_vatwh_cr ภาษีหัก ณ ที่จ่าย
			SqlStr sql = new SqlStr();
			sql.addLine("select aa.*");
			sql.addLine("from " + TboACGL_VATWH_CR.tablename + " aa");
			sql.addLine("where aa.COMP_CDE='" + Fnc.sqlQuote(_loginBean.getCOMP_CDE()) + "' ");

			if (postdateFrom != null && postdateTo != null) {
				sql.addLine(" and aa.POSTDATE >= '" + postdateFrom + "' and aa.POSTDATE <= '" + postdateTo + "' ");
			} else if (postdateFrom != null) {
				sql.addLine(" and aa.POSTDATE >= '" + postdateFrom + "' ");
			} else if (postdateTo != null) {
				sql.addLine(" and aa.POSTDATE <= '" + postdateTo + "' ");
			}

			if (!Fnc.isEmpty(cust_cde)) {
				sql.addLine("and aa.CUST_CDE = '" + Fnc.sqlQuote(cust_cde) + "' ");
			}
			if (!Fnc.isEmpty(vou_type)) {
				sql.addLine(" and aa.VOU_TYPE = '" + Fnc.sqlQuote(vou_type) + "' ");
			}
			if (!Fnc.isEmpty(vou_no)) {
				sql.addLine(" and aa.VOU_NO like '" + Fnc.sqlQuote(vou_no) + "%' ");
			}
			if (menuId2.equals("AcVatwhCrPrint")) {// เป็นการพิมพ์ครั้งแรก
				sql.addLine("and COALESCE(aa.DOCNO,'') = '' ");
			} else if (menuId2.equals("AcVatwhCrPrintRe")) {// re-print
				sql.addLine("and COALESCE(aa.DOCNO,'') != '' ");//
			} else if (menuId2.equals("AcVatwhCrPrintCancel")) {// ยกเลิก-การพิมพ์ AcVatwhCrPrintCancel
				sql.addLine("and COALESCE(aa.DOCNO,'') != '' ");
			}

			sql.addLine("and aa.DOC_TYPE = " + doc_type);
			sql.addLine("and aa.RECSTA = 2 ");
			sql.addLine("order by aa.VOU_TYPE, aa.VOU_NO, aa.POSTDATE");

			logger.info(sql.getSql());
			try (java.sql.ResultSet rs = dbc.getResultSetFw3(sql.getSql(), 10000);) {
				while (rs.next()) {
					FModelHasMap dat = new FModelHasMap();
					dat.putRecord(rs);
					dats.add(dat);
				}
			}

		}

	}

	/**
	 * สร้างเลขที่เอกสาร
	 * 
	 * @param lst_select
	 * @param loginBean
	 * @throws Exception
	 */
	public static void saveGenDocno(List<FModelHasMap> lst_select, LoginBean loginBean) throws Exception {

		try (FDbc dbc = FDbc.connectMasterDb()) {
			dbc.beginTrans();

			String docno = RunningAc.get_ACGL_VATWH_CR_DOCNO(dbc, lst_select.get(0).getInt("DOC_TYPE"), loginBean);// gen เลขเอกสาร
			int count = 0;
			for (FModelHasMap dat : lst_select) {

				count++;
				String vou_type = dat.getString("VOU_TYPE");
				String vou_no = dat.getString("VOU_NO");
				int vou_seq = dat.getInt("VOU_SEQ");
				int vou_dseq = dat.getInt("VOU_DSEQ");

				TboACGL_VATWH_CR acgl_vatwh_cr = new TboACGL_VATWH_CR();

				acgl_vatwh_cr.setCOMP_CDE(loginBean.getCOMP_CDE());
				acgl_vatwh_cr.setVOU_TYPE(vou_type);
				acgl_vatwh_cr.setVOU_NO(vou_no);
				acgl_vatwh_cr.setVOU_SEQ(vou_seq);
				acgl_vatwh_cr.setVOU_DSEQ(vou_dseq);

				if (!TbfACGL_VATWH_CR.find(dbc, acgl_vatwh_cr)) {
					throw new Exception("ไม่พบรายการ");
				}

				if (!Fnc.isEmpty(acgl_vatwh_cr.getDOCNO())) {
					throw new Exception("รายการที่ " + count + " มีการสร้างเลขเอกสารแล้ว");
				}

				acgl_vatwh_cr.setDOCNO(docno);

				if (!TbfACGL_VATWH_CR.update(dbc, acgl_vatwh_cr, "COALESCE(DOCNO,'') = '' ")) {
					throw new Exception("รายการที่ " + count + " ไม่สามารถสร้างเลขเอกสาร");
				}

				dat.setString("DOCNO", docno);// เก็บค่าเพื่อส่งต่อตอนพิมพ์

			}

			dbc.commit();

		}

	}

	public static void printData(List<FModelHasMap> lst_select, FJasperPrintList jasperPrintList, String menuId2,
			LoginBean loginBean) throws Exception {

		try (FDbc dbc = FDbc.connectMasterDb()) {

			TbfFCOMP.find(dbc, loginBean.getTboFcomp());// refresh ข้อมูลใน bean เผื่อแก้ไขในระหว่าง login

			if (menuId2.equals("AcVatwhCrPrint")) {// เป็นการพิมพ์ครั้งแรก
				saveGenDocno(lst_select, loginBean);
			}

			// เป็นการ distinct DOCNO ที่ซ้ำกันออกมา
			FModelHasMap docno_ALL = new FModelHasMap();
			for (FModelHasMap dat : lst_select) {
				if (!Fnc.isEmpty(dat.getString("DOCNO"))) {
					String key1 = dat.getString("DOCNO") + "\r" + dat.getInt("DOC_TYPE");// แยก docno และ doc_type ด้วย
																							// \r
					if (!docno_ALL.containsKey(key1)) {
						docno_ALL.put(key1, dat.getString("DOCNO"));
					}
				}
			}

			for (Map.Entry me2 : docno_ALL.entrySet()) {// loop hasmap

				logger.info("Key: " + me2.getKey() + " & Value: " + me2.getValue());

				// == เริ่ม gen report
				String[] docno_data = Fnc.getStr(me2.getKey()).split("\r");
				String doc_no_for_print = docno_data[0];
				String doc_type_for_print = docno_data[1];

				String[] sWHNAME = { "", "" };
				String[] sWHTYPE = { "", "" };
				BigDecimal[] bVAT_RATE = { BigDecimal.ZERO, BigDecimal.ZERO };
				BigDecimal[] bAMT = { BigDecimal.ZERO, BigDecimal.ZERO };
				BigDecimal[] bBASE_AMT = { BigDecimal.ZERO, BigDecimal.ZERO };
				String sCUST_CDE = "";
				String sCUST_NAME = "";
				java.sql.Date postDate = null;

				SqlStr sql = new SqlStr();
				sql.addLine("select aa.*");
				sql.addLine("from " + TboACGL_VATWH_CR.tablename + " aa");
				sql.addLine("where aa.COMP_CDE='" + Fnc.sqlQuote(loginBean.getCOMP_CDE()) + "' ");
				sql.addLine("and COALESCE(aa.DOCNO,'') = '" + doc_no_for_print + "' ");
				sql.addLine("and aa.DOC_TYPE = " + doc_type_for_print + " and aa.RECSTA = 2 ");
				sql.addLine("order by aa.VOU_TYPE, aa.VOU_NO, aa.POSTDATE");
				logger.info(sql.getSql());
				try (java.sql.ResultSet rs = dbc.getResultSet3(sql.getSql(), 2);) {// ไม่ให้เกิน 2 รายการ
					int count = 0;
					while (rs.next()) {

						sWHNAME[count] = Fnc.getStr(rs.getString("WHNAME"));
						sWHTYPE[count] = Fnc.getStr(rs.getString("WHTYPE"));
						bVAT_RATE[count] = Fnc.getBigDecimalValue(rs.getBigDecimal("VAT_RATE"));
						bAMT[count] = Fnc.getBigDecimalValue(rs.getBigDecimal("AMT"))
								.multiply((rs.getBigDecimal("NUM_TYPE")));
						bBASE_AMT[count] = Fnc.getBigDecimalValue(rs.getBigDecimal("BASE_AMT"))
								.multiply(rs.getBigDecimal("NUM_TYPE"));
						sCUST_CDE = Fnc.getStr(rs.getString("CUST_CDE"));
						sCUST_NAME = Fnc.getStr(rs.getString("CUST_NAME"));
						postDate = rs.getDate("POSTDATE");

						count++;
					}
				}

				Map reportParams = new HashMap();

				TboFCOMPBRANC branch = new TboFCOMPBRANC();

				branch.setCOMP_CDE(loginBean.getCOMP_CDE());
				branch.setBRANC_CDE(1);// default เป็นสาขาที่ 1 เสมอ

				if (TbfFCOMPBRANC.find(dbc, branch)) {
					reportParams.put("ADDR1", branch.getADDR1());
					reportParams.put("ADDR2", branch.getADDR2());
				}

				jasperPrintList.addJasperPrintList(
						builtReport(doc_no_for_print, doc_type_for_print, sWHNAME, sWHTYPE, bVAT_RATE, bAMT, bBASE_AMT,
								sCUST_CDE, sCUST_NAME, postDate, reportParams, loginBean).toJasperPrint());

			}

		}

	}

	private static JasperReportBuilder builtReport(String doc_no_for_print, String doc_type_for_print, String[] sWHNAME,
			String[] sWHTYPE, BigDecimal[] bVAT_RATE, BigDecimal[] bAMT, BigDecimal[] bBASE_AMT, String sCUST_CDE,
			String sCUST_NAME, java.sql.Date postDate, Map reportParams, LoginBean loginBean) throws Exception {

		StyleBuilder styleDefault = DynamicRepTemplates.getRootStyle().setPadding(0);// สำหรับเริ่มต้นค่าใน report นี้
		StyleBuilder style1 = DynamicRepTemplates.getRootStyle().setPadding(0).setFontSize(9);

		// Dynamic report
		JasperReportBuilder myreport = DynamicReports.report()
				.setTemplate(DynamicRepTemplates.reportTemplateWithHighlight)
				.setPageFormat(PageType.A4, PageOrientation.PORTRAIT).summaryWithPageHeaderAndFooter()
				.setDataSource(new JREmptyDataSource())// trick สร้าง JREmptyDataSource
				.columnHeader(DynamicReports.cmp.verticalGap(2));// แก้ปํญหากรณี pdf แสดงสีแถวแบบ highlight
																	// ทำให้ไม่ทับเส้นขอบล่างของ Title Column Header

		// == start เพิ่มฟิลด์
		// == end เพิ่มฟิลด์

		// == group varible
		// == end group vairble

		// == report varible
		// == end report varible

		// == pageHeader
		ComponentBuilder<?, ?> cmpHead = DynamicReports.cmp.horizontalList()
				.add(DynamicReports.cmp
						.text("ฉบับที่ 1 (สำหรับผู้ถูกหักภาษี ณ ที่จ่าย ใช้แนบพร้อมกับแบบแสดงรายการภาษี)")
						.setStyle(DynamicReports.stl.style(style1)))
				.newRow().add(DynamicReports.cmp.text("ฉบับที่ 2 (สำหรับผู้ถูกหักภาษี ณ ที่จ่าย เก็บไว้เป็นหลักฐาน)")
						.setStyle(DynamicReports.stl.style(style1)));
		myreport.pageHeader(cmpHead);

		// == detail
		ComponentBuilder<?, ?> cmdDetailBoxM = DynamicReports.cmp.horizontalList(

				DynamicReports.cmp.text("").setFixedWidth(3)/* จัดขอบ */,

				DynamicReports.cmp.verticalList(

						DynamicReports.cmp.text("หนังสือรับรองการหักภาษี ณ ที่จ่าย")
								.setStyle(DynamicReports.stl.style(styleDefault).setFontSize(16).setPadding(0))
								.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),

						DynamicReports.cmp.horizontalList(

								DynamicReports.cmp.text("")
										.setStyle(DynamicReports.stl.style(styleDefault).setFontSize(10))
										.setFixedWidth(80),

								DynamicReports.cmp.text("ตามมาตรา 50 ทวิ แห่งประมวลรัษฎากร")
										.setStyle(DynamicReports.stl.style(styleDefault).setFontSize(13))
										// .setFixedWidth(70), ให้ระบบ Auto ส่วนที่เหลือ
										.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),

								DynamicReports.cmp.text("เลขที่")
										.setStyle(DynamicReports.stl.style(styleDefault).setFontSize(10).setPadding(0)
												.setTextAlignment(HorizontalTextAlignment.RIGHT,
														VerticalTextAlignment.BOTTOM))
										.setFixedWidth(20),

								DynamicReports.cmp.text(doc_no_for_print)
										.setStyle(DynamicReports.stl.style(styleDefault).setFontSize(10).setPadding(0)
												.setBottomBorder(DynamicReports.stl.penDotted()).setTextAlignment(
														HorizontalTextAlignment.CENTER, VerticalTextAlignment.BOTTOM)// trick
																														// จัดอักษรชิดล่าง
										)
										// .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
										// .setFixedHeight(15)//trick ความสูง object
										// .setStretchType(StretchType.NO_STRETCH) //trick no stretch
										.setFixedWidth(60)

						),

						DynamicReports.cmp.verticalGap(2), getBox1(styleDefault, loginBean, reportParams),
						DynamicReports.cmp.verticalGap(2),
						getBox2(styleDefault, sCUST_CDE, sCUST_NAME, doc_type_for_print, loginBean),
						DynamicReports.cmp.verticalGap(2),
						getBox3(styleDefault, sWHNAME, bVAT_RATE, bAMT, bBASE_AMT, postDate),
						DynamicReports.cmp.verticalGap(2), getBox4(styleDefault), DynamicReports.cmp.verticalGap(2),
						getBox5(styleDefault, sWHTYPE[0]), DynamicReports.cmp.verticalGap(2), getBox6(style1, postDate),
						DynamicReports.cmp.verticalGap(2)),

				DynamicReports.cmp.text("").setFixedWidth(3)/* จัดขอบ */

		).setStyle(DynamicReports.stl.style(styleDefault).setBorder(DynamicReports.stl.penThin()));

		ComponentBuilder<?, ?> cmdDetailBox5 = DynamicReports.cmp.horizontalList(
				DynamicReports.cmp.text("หมายเหตุ : เลขประจำตัวผู้เสียภาษีอากร ( 13 หลัก)* หมายถึง")
						.setStyle(DynamicReports.stl.style(style1)).setFixedWidth(160),
				DynamicReports.cmp.verticalList(
						DynamicReports.cmp.text("1. กรณีบุคคลธรรมดาไทย ให้ใช้เลขบัตรประจำตัวประชาชนของกรมการปกครอง")
								.setStyle(DynamicReports.stl.style(style1)),
						DynamicReports.cmp.text("2. กรณีนิติบุคคล ให้ใช้เลขทะเบียนนิติบุคคลของกรมพัฒนาธุรกิจการค้า")
								.setStyle(DynamicReports.stl.style(style1)),
						DynamicReports.cmp.text(
								"3. กรณีอื่นๆ นอกจาก 1. และ 2. ให้ใช้เลขประจำตัวผู้เสียภาษีอากร (13 หลัก) ของกรมสรรพกร")
								.setStyle(DynamicReports.stl.style(style1))
				/*
				 * DynamicReports.cmp.booleanField(true).setComponentType(BooleanComponentType.
				 * IMAGE_CHECKBOX_1).setImageHeight(10).setImageWidth(10) //trick ใส่ checkbox
				 * component
				 */
				));

		myreport.detail(cmdDetailBoxM, cmdDetailBox5);

		// == footer

		// == summary
		// == end summary

		// == start เพิ่มคอลัมภ์
		// == end เพิ่มคอลัมภ์

		return myreport;

	}

	public static ComponentBuilder<?, ?> getBox1(StyleBuilder styleDefault, LoginBean loginBean, Map reportParams)
			throws SQLException, Exception {

		String idNo = loginBean.getTboFcomp().getTAXNO();

		return DynamicReports.cmp.verticalList(

				DynamicReports.cmp.verticalGap(3),

				DynamicReports.cmp.horizontalList(

						DynamicReports.cmp.text("ผู้มีหน้าที่หักภาษี ณ ที่จ่าย :-")
								.setStyle(DynamicReports.stl.style(styleDefault).setLeftPadding(3))
								.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),

						DynamicReports.cmp.text("เลขที่ประจำตัวผู้เสียภาษีอากร (13 หลัก) * ")
								.setStyle(DynamicReports.stl.style(styleDefault).setFontSize(10)
										.setTextAlignment(HorizontalTextAlignment.RIGHT, VerticalTextAlignment.MIDDLE))
								.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT),
						DynamicReports.cmp.horizontalGap(3),

						getBoxNum(Fnc.getStrAt(idNo, 1), 10, styleDefault, true),
						getBoxNum("-", 5, styleDefault, false),
						getBoxNum(Fnc.getStrAt(idNo, 2), 10, styleDefault, true),
						getBoxNum(Fnc.getStrAt(idNo, 3), 10, styleDefault, true),
						getBoxNum(Fnc.getStrAt(idNo, 4), 10, styleDefault, true),
						getBoxNum(Fnc.getStrAt(idNo, 5), 10, styleDefault, true),
						getBoxNum("-", 5, styleDefault, false),
						getBoxNum(Fnc.getStrAt(idNo, 6), 10, styleDefault, true),
						getBoxNum(Fnc.getStrAt(idNo, 7), 10, styleDefault, true),
						getBoxNum(Fnc.getStrAt(idNo, 8), 10, styleDefault, true),
						getBoxNum(Fnc.getStrAt(idNo, 9), 10, styleDefault, true),
						getBoxNum(Fnc.getStrAt(idNo, 10), 10, styleDefault, true),
						getBoxNum("-", 5, styleDefault, false),
						getBoxNum(Fnc.getStrAt(idNo, 11), 10, styleDefault, true),
						getBoxNum(Fnc.getStrAt(idNo, 12), 10, styleDefault, true),
						getBoxNum("-", 5, styleDefault, false),
						getBoxNum(Fnc.getStrAt(idNo, 13), 10, styleDefault, true), DynamicReports.cmp.horizontalGap(3)),

				DynamicReports.cmp.verticalGap(3),

				DynamicReports.cmp.horizontalList(

						DynamicReports.cmp.text("ชื่อ")
								.setStyle(DynamicReports.stl.style(styleDefault).setLeftPadding(3))
								.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT).setFixedWidth(20),
						DynamicReports.cmp.text(loginBean.getTboFcomp().getCOMP_NAME())
								.setStyle(DynamicReports.stl.style(styleDefault)
										.setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
										.setLeftPadding(3).setBottomBorder(DynamicReports.stl.penDotted()))
								.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),

						DynamicReports.cmp.text("เลขประจำตัวผู้เสียภาษีอากร")
								.setStyle(DynamicReports.stl.style(styleDefault).setFontSize(10)
										.setTextAlignment(HorizontalTextAlignment.RIGHT, VerticalTextAlignment.BOTTOM))
								.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT).setFixedWidth(90),

						DynamicReports.cmp.horizontalGap(3),

						getBoxNum("", 10, styleDefault, true), getBoxNum("-", 5, styleDefault, false),
						getBoxNum("", 10, styleDefault, true), getBoxNum("", 10, styleDefault, true),
						getBoxNum("", 10, styleDefault, true), getBoxNum("", 10, styleDefault, true),
						getBoxNum("-", 5, styleDefault, false), getBoxNum("", 10, styleDefault, true),
						getBoxNum("", 10, styleDefault, true), getBoxNum("", 10, styleDefault, true),
						getBoxNum("", 10, styleDefault, true), getBoxNum("-", 5, styleDefault, false),
						getBoxNum("", 10, styleDefault, true), DynamicReports.cmp.horizontalGap(3)),

				DynamicReports.cmp.text("(ให้ระบุว่าเป็น บุคคล นิติบุคคล บริษัท สมาคม หรือคณะบุคคล)")
						.setStyle(DynamicReports.stl.style(styleDefault).setFontSize(8).setLeftPadding(30))
						.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),

				DynamicReports.cmp.horizontalList(
						DynamicReports.cmp.text("ที่อยู่")
								.setStyle(DynamicReports.stl.style(styleDefault).setLeftPadding(3))
								.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT).setFixedWidth(20),
						DynamicReports.cmp
								.text(Fnc.getStr(reportParams.get("ADDR1")) + " "
										+ Fnc.getStr(reportParams.get("ADDR2")))
								.setStyle(DynamicReports.stl.style(styleDefault).setLeftPadding(3)
										.setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.BOTTOM)
										.setBottomBorder(DynamicReports.stl.penDotted()))
								.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),

						DynamicReports.cmp.horizontalGap(3)),

				DynamicReports.cmp.text(
						"(ให้ระบุ ชื่ออาคาร/หมู่บ้าน ห้องเลขที่ ชั้นที่ ตรอก/ซอย หมู่ที่ ถนน ตำบล/แขวง อำเภอ/เขต จังหวัด)")
						.setStyle(DynamicReports.stl.style(styleDefault).setFontSize(8).setLeftPadding(30))
						.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)

		).setStyle(DynamicReports.stl.style(styleDefault).setBorder(DynamicReports.stl.penThin()));

	}

	public static ComponentBuilder<?, ?> getBox2(StyleBuilder styleDefault, String sCUST_CDE, String sCUST_NAME,
			String doc_type_for_print, LoginBean loginBean) throws SQLException, Exception {

		String idNo = "";
		String addr1 = "";

		TboFCUS cust = new TboFCUS();

		cust.setCOMP_CDE(loginBean.getCOMP_CDE());
		cust.setCUST_CDE(sCUST_CDE);

		if (!TbfFCUS.find(cust)) {
			throw new Exception("ไม่พบลูกค้ารหัส " + sCUST_CDE);
		}

		idNo = cust.getIDNO();

		TboFCUS_ADDR custAddr = new TboFCUS_ADDR();

		custAddr.setCOMP_CDE(loginBean.getCOMP_CDE());
		custAddr.setCUST_CDE(sCUST_CDE);
		custAddr.setADDR_TYP("1");

		if (!TbfFCUS_ADDR.find(custAddr)) {
			throw new Exception("ไม่พบที่อยู่ตามทะเบียนบ้านลูกค้ารหัส " + sCUST_CDE);
		}

		addr1 = TbmFCUS_ADDR.custAddress1(loginBean.getCOMP_CDE(), sCUST_CDE, "1", true);

		return DynamicReports.cmp.verticalList(

				DynamicReports.cmp.verticalGap(3),

				DynamicReports.cmp.horizontalList(

						DynamicReports.cmp.text("ผู้ถูกหักภาษี ณ ที่จ่าย :-")
								.setStyle(DynamicReports.stl.style(styleDefault).setLeftPadding(3))
								.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),

						DynamicReports.cmp.text("เลขที่ประจำตัวผู้เสียภาษีอากร (13 หลัก) * ")
								.setStyle(DynamicReports.stl.style(styleDefault).setFontSize(10)
										.setTextAlignment(HorizontalTextAlignment.RIGHT, VerticalTextAlignment.MIDDLE))
								.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT),
						DynamicReports.cmp.horizontalGap(3),

						getBoxNum(Fnc.getStrAt(idNo, 1), 10, styleDefault, true),
						getBoxNum("-", 5, styleDefault, false),
						getBoxNum(Fnc.getStrAt(idNo, 2), 10, styleDefault, true),
						getBoxNum(Fnc.getStrAt(idNo, 3), 10, styleDefault, true),
						getBoxNum(Fnc.getStrAt(idNo, 4), 10, styleDefault, true),
						getBoxNum(Fnc.getStrAt(idNo, 5), 10, styleDefault, true),
						getBoxNum("-", 5, styleDefault, false),
						getBoxNum(Fnc.getStrAt(idNo, 6), 10, styleDefault, true),
						getBoxNum(Fnc.getStrAt(idNo, 7), 10, styleDefault, true),
						getBoxNum(Fnc.getStrAt(idNo, 8), 10, styleDefault, true),
						getBoxNum(Fnc.getStrAt(idNo, 9), 10, styleDefault, true),
						getBoxNum(Fnc.getStrAt(idNo, 10), 10, styleDefault, true),
						getBoxNum("-", 5, styleDefault, false),
						getBoxNum(Fnc.getStrAt(idNo, 11), 10, styleDefault, true),
						getBoxNum(Fnc.getStrAt(idNo, 12), 10, styleDefault, true),
						getBoxNum("-", 5, styleDefault, false),
						getBoxNum(Fnc.getStrAt(idNo, 13), 10, styleDefault, true), DynamicReports.cmp.horizontalGap(3)),

				DynamicReports.cmp.verticalGap(3),

				DynamicReports.cmp.horizontalList(

						DynamicReports.cmp.text("ชื่อ")
								.setStyle(DynamicReports.stl.style(styleDefault).setLeftPadding(3))
								.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT).setFixedWidth(20),
						DynamicReports.cmp.text(sCUST_NAME)
								.setStyle(DynamicReports.stl.style(styleDefault)
										.setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
										.setLeftPadding(3).setBottomBorder(DynamicReports.stl.penDotted()))
								.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),

						DynamicReports.cmp.text("เลขประจำตัวผู้เสียภาษีอากร")
								.setStyle(DynamicReports.stl.style(styleDefault).setFontSize(10)
										.setTextAlignment(HorizontalTextAlignment.RIGHT, VerticalTextAlignment.BOTTOM))
								.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT).setFixedWidth(90),

						DynamicReports.cmp.horizontalGap(3),

						getBoxNum("", 10, styleDefault, true), getBoxNum("-", 5, styleDefault, false),
						getBoxNum("", 10, styleDefault, true), getBoxNum("", 10, styleDefault, true),
						getBoxNum("", 10, styleDefault, true), getBoxNum("", 10, styleDefault, true),
						getBoxNum("-", 5, styleDefault, false), getBoxNum("", 10, styleDefault, true),
						getBoxNum("", 10, styleDefault, true), getBoxNum("", 10, styleDefault, true),
						getBoxNum("", 10, styleDefault, true), getBoxNum("-", 5, styleDefault, false),
						getBoxNum("", 10, styleDefault, true), DynamicReports.cmp.horizontalGap(3)),

				DynamicReports.cmp.text("(ให้ระบุว่าเป็น บุคคล นิติบุคคล บริษัท สมาคม หรือคณะบุคคล)")
						.setStyle(DynamicReports.stl.style(styleDefault).setFontSize(8).setLeftPadding(30))
						.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),

				DynamicReports.cmp.horizontalList(
						DynamicReports.cmp.text("ที่อยู่")
								.setStyle(DynamicReports.stl.style(styleDefault).setLeftPadding(3))
								.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT).setFixedWidth(20),
						DynamicReports.cmp.text(addr1)
								.setStyle(DynamicReports.stl.style(styleDefault).setLeftPadding(3)
										.setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.BOTTOM)
										.setBottomBorder(DynamicReports.stl.penDotted()))
								.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),

						DynamicReports.cmp.horizontalGap(3)),

				getTextboxDetail(
						"(ให้ระบุ ชื่ออาคาร/หมู่บ้าน ห้องเลขที่ ชั้นที่ ตรอก/ซอย หมู่ที่ ถนน ตำบล/แขวง อำเภอ/เขต จังหวัด)",
						8, 30, 0, 0, 0, false, HorizontalTextAlignment.LEFT, null, styleDefault),

				DynamicReports.cmp.horizontalList(

						DynamicReports.cmp.horizontalList()
								.add(getTextboxDetail("ลำดับที่", 0, 3, 3, 0, 30, false, HorizontalTextAlignment.LEFT,
										VerticalTextAlignment.BOTTOM, styleDefault))
								.add(DynamicReports.cmp.text("")
										.setStyle(DynamicReports.stl.style(styleDefault).setFontSize(10)
												.setBorder(DynamicReports.stl.penThin()).bold())
										.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
										.setStretchType(StretchType.NO_STRETCH).setFixedHeight(14).setFixedWidth(30))
								.add(getTextboxDetail("ใบแนบ", 0, 3, 0, 0, 0, false, HorizontalTextAlignment.LEFT,
										VerticalTextAlignment.BOTTOM, styleDefault))
								.newRow()
								.add(getTextboxDetail("(ให้สามารถอ้างอิงหรือสอบยันกันได้ระหว่างลำดับที่ตาม", 8, 3, 0, 0,
										0, false, HorizontalTextAlignment.LEFT, null, styleDefault))
								.newRow()
								.add(getTextboxDetail("หนังสือรับรองฯ กับแบบยื่นรายการภาษีหักที่จ่าย)", 8, 3, 0, 0, 0,
										false, HorizontalTextAlignment.LEFT, null, styleDefault)),
						DynamicReports.cmp.horizontalList().add(FReport.getBoxCheck(false, 10, 14, 11))
								.add(getTextboxDetail("(1) ภ.ง.ด.1ก", 0, 3, 0, 0, 80, false,
										HorizontalTextAlignment.LEFT, null, styleDefault))
								.newRow().add(FReport.getBoxCheck(false, 10, 14, 11))
								.add(getTextboxDetail("(5) ภ.ง.ด.2ก", 0, 3, 0, 0, 80, false,
										HorizontalTextAlignment.LEFT, null, styleDefault)),
						DynamicReports.cmp.horizontalList().add(FReport.getBoxCheck(false, 10, 14, 11))
								.add(getTextboxDetail("(2) ภ.ง.ด.1ก พิเศษ", 0, 3, 0, 0, 80, false,
										HorizontalTextAlignment.LEFT, null, styleDefault))
								.newRow().add(FReport.getBoxCheck(false, 10, 14, 11))
								.add(getTextboxDetail("(6) ภ.ง.ด.3ก", 0, 3, 0, 0, 80, false,
										HorizontalTextAlignment.LEFT, null, styleDefault)),
						DynamicReports.cmp.horizontalList().add(FReport.getBoxCheck(false, 10, 14, 11))
								.add(getTextboxDetail("(3) ภ.ง.ด.2", 0, 3, 0, 0, 80, false,
										HorizontalTextAlignment.LEFT, null, styleDefault))
								.newRow().add(FReport.getBoxCheck(doc_type_for_print.equals("2"), 10, 14, 11))
								.add(getTextboxDetail("(7) ภ.ง.ด.53", 0, 3, 0, 0, 80, false,
										HorizontalTextAlignment.LEFT, null, styleDefault)),
						DynamicReports.cmp.horizontalList()
								.add(FReport.getBoxCheck(doc_type_for_print.equals("1"), 10, 14, 11))
								.add(getTextboxDetail("(4) ภ.ง.ด.3", 0, 3, 0, 0, 80, false,
										HorizontalTextAlignment.LEFT, null, styleDefault))
								.newRow().add(DynamicReports.cmp.horizontalGap(3)))

		).setStyle(DynamicReports.stl.style(styleDefault).setBorder(DynamicReports.stl.penThin()));

	}

	public static VerticalListBuilder getBox3(StyleBuilder styleDefault, String[] sWHNAME, BigDecimal[] bVAT_RATE,
			BigDecimal[] bAMT, BigDecimal[] bBASE_AMT, java.sql.Date postDate) {

		BigDecimal sum_BASE_AMT = bBASE_AMT[0].add(bBASE_AMT[1]);
		BigDecimal sum_AMT = bAMT[0].add(bAMT[1]);

		VerticalListBuilder vComp1 = DynamicReports.cmp
				.verticalList().add(
						DynamicReports.cmp
								.horizontalList().add(
										DynamicReports.cmp.verticalList().add(DynamicReports.cmp.verticalGap(4))
												.add(DynamicReports.cmp.text("ประเภทเงินได้พึ่งประเมินที่จ่าย")
														.setStyle(DynamicReports.stl.style(styleDefault))
														.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER))
												.setStyle(
														DynamicReports.stl
																.style(styleDefault)
																.setBorder(DynamicReports.stl.penThin())))
								.add(DynamicReports.cmp.verticalList()
										.add(DynamicReports.cmp.text("วัน เดือน")
												.setStyle(DynamicReports.stl.style(styleDefault))
												.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER))
										.add(DynamicReports.cmp.text("หรือปีภาษี ที่จ่าย")
												.setStyle(DynamicReports.stl.style(styleDefault))
												.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER))
										.setStyle(DynamicReports.stl.style(styleDefault)
												.setBorder(DynamicReports.stl.penThin()))
										.setFixedWidth(60))
								.add(DynamicReports.cmp.verticalList().add(DynamicReports.cmp.verticalGap(4))
										.add(DynamicReports.cmp.text("จำนวนเงินที่จ่าย")
												.setStyle(DynamicReports.stl.style(styleDefault))
												.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER))
										.setStyle(DynamicReports.stl
												.style(styleDefault).setBorder(DynamicReports.stl.penThin()))
										.setFixedWidth(80))
								.add(DynamicReports.cmp.verticalList()
										.add(DynamicReports.cmp.text("ภาษีที่หัก")
												.setStyle(DynamicReports.stl.style(styleDefault))
												.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER))
										.add(DynamicReports.cmp
												.text("และนำส่งให้").setStyle(DynamicReports.stl.style(styleDefault))
												.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER))
										.setStyle(DynamicReports.stl.style(styleDefault)
												.setBorder(DynamicReports.stl.penThin()))
										.setFixedWidth(80))

				)
				.add(getBox3_line("1.", "เงินเดือน ค่าจ้าง เบี้ยเลี้ยง โบนัส ฯลฯ ตามมาตรา 40(1)", "", "", "", "", "",
						10, false, styleDefault))
				.add(getBox3_line("2.", "ค่าธรรมเนียม ค่านายหน้า ฯลฯ ตามมาตรา 40(2)", "", "", "", "", "", 10, false,
						styleDefault))
				.add(getBox3_line("3.", "ค่าแห่งลิขสิทธิ์ ฯลฯ ตามมาตรา 40(3)", "", "", "", "", "", 10, false,
						styleDefault))
				.add(getBox3_line("4.", "(ก) ดอกเบี้ย ฯลฯ ตามมาตรา 40(4)(ก)", "", "", "", "", "", 10, false,
						styleDefault))
				.add(getBox3_line("", "(ข) เงินปันผล ส่วนแบ่งกำไร ฯลฯ ตามมาตรา 40(4)(ข)", "", "", "", "", "", 10, false,
						styleDefault))
				.add(getBox3_line("", "(1) กรณีผู้ได้รับเงินปันผลได้รับเครดิตภาษี โดยจ่ายจาก", "", "", "", "", "", 20,
						false, styleDefault))
				.add(getBox3_line("", "กำไรสุทธิของกิจการที่ต้องเสียภาษีเงินได้นิติบุคคลในอัตราดังนี้", "", "", "", "",
						"", 30, false, styleDefault))
				.add(getBox3_line("", "(1.1) อัตราร้อยละ 30 ของกำไรสุทธิ", "", "", "", "", "", 30, false, styleDefault))
				.add(getBox3_line("", "(1.2) อัตราร้อยละ 25 ของกำไรสุทธิ", "", "", "", "", "", 30, false, styleDefault))
				.add(getBox3_line("", "(1.3) อัตราร้อยละ 20 ของกำไรสุทธิ", "", "", "", "", "", 30, false, styleDefault))
				.add(getBox3_line("", "(1.4) อัตราอื่นๆ (ระบุ)........... ของกำไรสุทธิ", "", "", "", "", "", 30, false,
						styleDefault))
				.add(getBox3_line("", "(2) กรณีผู้ได้รับเงินปันผลไม่ได้รับเครดิตภาษี เนื่องจากจ่ายจาก", "", "", "", "",
						"", 20, false, styleDefault))
				.add(getBox3_line("", "(2.1) กำไรสุทธิของกิจการที่ได้รับยกเว้นเงินได้นิติบุคคล", "", "", "", "", "", 30,
						false, styleDefault))
				.add(getBox3_line("", "(2.2) เงินปันผลหรือเงินส่วนแบ่งของกำไรที่ได้รับยกเว้นไม่ต้องนำมารวม", "", "", "",
						"", "", 30, false, styleDefault))
				.add(getBox3_line("", "คำนวณเป็นรายได้เพื่อเสียภาษีเงินได้นิติบุคคบ", "", "", "", "", "", 40, false,
						styleDefault))
				.add(getBox3_line("", "(2.3) กำไรสุทธิส่วนที่ได้หักผลขาดทุนสุทธิยกมาไม่เกิน 5 ปี", "", "", "", "", "",
						30, false, styleDefault))
				.add(getBox3_line("", "ก่อนรอบระยะเวลาบัญชีปัจจุบัน", "", "", "", "", "", 40, false, styleDefault))
				.add(getBox3_line("", "(2.4) กำไรที่รับรู้ทางบัญชีโดยวิธีส่วนได้เสีย (equity method)", "", "", "", "",
						"", 30, false, styleDefault))
				.add(getBox3_line("", "(2.5) อื่นๆ (ระบุ)...........................", "", "", "", "", "", 30, false,
						styleDefault))
				.add(getBox3_line("5.",
						"การจ่ายเงินได้ที่ต้องหักภาษี ณ ที่จ่าย ตามคำสั่งกรมสรรพกรที่ออกตามมาตรา 3 เตรส", "", "", "",
						"", "", 10, false, styleDefault))
				.add(getBox3_line("", "เช่น รางวัล ส่วนลดหรือประโยชน์ใดๆ เนื่องจากการส่งเสริมการขาย รางวัลในการประกวด",
						"", "", "", "", "", 10, false, styleDefault))
				.add(getBox3_line("", "การแข่งขัน การชิงโชค ค่าแสดงของนักแสดงสาธารณะ ค่าจ้าง ทำของ ค่าโฆษณา ค่าเช่า",
						"", "", "", "", "", 10, false, styleDefault))
				.add(getBox3_line("", "ค่าขนส่ง ค่าบริการ ค่าเบี้ยประกันวินาศภัย ฯลฯ", "", "", "", "", "", 10, false,
						styleDefault))
				.add(getBox3_line("6.",
						"อื่นๆ (ระบุ)    - " + sWHNAME[0] + " " + Fnc.getStrNumber(bVAT_RATE[0], "#,##0.##") + " %",
						FnDate.displayDateString(postDate), Fnc.getStrNumber(bBASE_AMT[0], "#,##0.00").split("\\.")[0],
						Fnc.getStrNumber(bBASE_AMT[0], "#,##0.00").split("\\.")[1],
						Fnc.getStrNumber(bAMT[0], "#,##0.00").split("\\.")[0],
						Fnc.getStrNumber(bAMT[0], "#,##0.00").split("\\.")[1], 10, false, styleDefault));

		if (bAMT[1].compareTo(BigDecimal.ZERO) > 0) {
			vComp1.add(getBox3_line("", "- " + sWHNAME[1] + " " + Fnc.getStrNumber(bVAT_RATE[1], "#,##0.##") + " %",
					FnDate.displayDateString(postDate), Fnc.getStrNumber(bBASE_AMT[1], "#,##0.00").split("\\.")[0],
					Fnc.getStrNumber(bBASE_AMT[1], "#,##0.00").split("\\.")[1],
					Fnc.getStrNumber(bAMT[1], "#,##0.00").split("\\.")[0],
					Fnc.getStrNumber(bAMT[1], "#,##0.00").split("\\.")[1], 55, true, styleDefault));
		} else {
			vComp1.add(getBox3_line("", "", "", "", "", "", "", 10, true, styleDefault));
		}

		vComp1.add(DynamicReports.cmp.horizontalList()
				.add(DynamicReports.cmp.text("รวมเงินที่จ่ายและภาษีที่หักนำส่ง")
						.setStyle(DynamicReports.stl.style(styleDefault).setLeftBorder(DynamicReports.stl.penThin())
								.setRightPadding(2))
						.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT))
				.add(DynamicReports.cmp.text(Fnc.getStrNumber(sum_BASE_AMT, "#,##0.00").split("\\.")[0])
						.setStyle(DynamicReports.stl.style(styleDefault).setBorder(DynamicReports.stl.penThin())
								.setBottomBorder(DynamicReports.stl.penDouble()).setRightPadding(2))
						.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT).setFixedWidth(65))
				.add(DynamicReports.cmp.text(Fnc.getStrNumber(sum_BASE_AMT, "#,##0.00").split("\\.")[1])
						.setStyle(DynamicReports.stl.style(styleDefault).setBorder(DynamicReports.stl.penThin())
								.setBottomBorder(DynamicReports.stl.penDouble()).setRightPadding(2))
						.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT).setFixedWidth(15))
				.add(DynamicReports.cmp.text(Fnc.getStrNumber(sum_AMT, "#,##0.00").split("\\.")[0])
						.setStyle(DynamicReports.stl.style(styleDefault).setBorder(DynamicReports.stl.penThin())
								.setBottomBorder(DynamicReports.stl.penDouble()).setRightPadding(2))
						.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT).setFixedWidth(65))
				.add(DynamicReports.cmp.text(Fnc.getStrNumber(sum_AMT, "#,##0.00").split("\\.")[1])
						.setStyle(DynamicReports.stl.style(styleDefault).setBorder(DynamicReports.stl.penThin())
								.setBottomBorder(DynamicReports.stl.penDouble()).setRightPadding(2))
						.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT).setFixedWidth(15))

		).add(DynamicReports.cmp.verticalGap(4)
				.setStyle(DynamicReports.stl.style(styleDefault).setLeftBorder(DynamicReports.stl.penThin())
						.setRightBorder(DynamicReports.stl.penThin())))
				.add(DynamicReports.cmp.horizontalList()
						.add(DynamicReports.cmp.text("รวมเงินภาษีที่หักนำส่ง (ตัวอักษร)")
								.setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(2)
										.setLeftBorder(DynamicReports.stl.penThin()))
								.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT).setFixedWidth(150))
						.add(DynamicReports.cmp
								.text("- " + ThaiBaht.getBahtString(sum_AMT) + " -")
								.setStyle(DynamicReports.stl.style(styleDefault).setFontSize(13).setLeftPadding(5)
										.setRightBorder(DynamicReports.stl.penThin())
										.setBackgroundColor(new Color(204, 204, 204)))
								.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)))
				.add(DynamicReports.cmp.verticalGap(2)
						.setStyle(DynamicReports.stl.style(styleDefault).setLeftBorder(DynamicReports.stl.penThin())
								.setRightBorder(DynamicReports.stl.penThin())
								.setBottomBorder(DynamicReports.stl.penThin())));

		return vComp1;

	}

	public static ComponentBuilder<?, ?> getBox4(StyleBuilder styleDefault) {

		return DynamicReports.cmp.verticalList(

				DynamicReports.cmp.verticalGap(2),

				DynamicReports.cmp.horizontalList(DynamicReports.cmp.text(
						"เงินที่จ่ายเข้า    กบข./กสจ./กองทุนสงเคราะห์ครูโรงเรียนเอกชน...................บาท        กองทุนประกันสังคม...................บาท   กองทุนสำรองเลี้ยงชีพ...................บาท")
						.setStyle(DynamicReports.stl.style(styleDefault).setLeftPadding(3))
						.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)),

				DynamicReports.cmp.verticalGap(2)

		).setStyle(DynamicReports.stl.style(styleDefault).setBorder(DynamicReports.stl.penThin()));

	}

	public static ComponentBuilder<?, ?> getBox5(StyleBuilder styleDefault, String sWHTYPE) {

		return DynamicReports.cmp.verticalList(

				DynamicReports.cmp.verticalGap(2),

				DynamicReports.cmp.horizontalList(
						DynamicReports.cmp.text("ผู้จ่ายเงิน")
								.setStyle(DynamicReports.stl.style(styleDefault).setLeftPadding(3)).setFixedWidth(50)
								.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),

						FReport.getBoxCheck(sWHTYPE.equals("1"), 10, 14, 11),
						DynamicReports.cmp.text("(1) หัก ณ ที่จ่าย")
								.setStyle(DynamicReports.stl.style(styleDefault).setLeftPadding(3))
								.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),

						FReport.getBoxCheck(sWHTYPE.equals("2"), 10, 14, 11),
						DynamicReports.cmp.text("(2) ออกให้ตลอด")
								.setStyle(DynamicReports.stl.style(styleDefault).setLeftPadding(3))
								.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),

						FReport.getBoxCheck(sWHTYPE.equals("3"), 10, 14, 11),
						DynamicReports.cmp.text("(3) ออกให้ครั้งเดียว")
								.setStyle(DynamicReports.stl.style(styleDefault).setLeftPadding(3))
								.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),

						FReport.getBoxCheck(sWHTYPE.equals("4"), 10, 14, 11),
						DynamicReports.cmp.text("(4) อื่นๆ (ระบุ)............................")
								.setStyle(DynamicReports.stl.style(styleDefault).setLeftPadding(3))
								.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)

				),

				DynamicReports.cmp.verticalGap(2)

		).setStyle(DynamicReports.stl.style(styleDefault).setBorder(DynamicReports.stl.penThin()));

	}

	public static ComponentBuilder<?, ?> getBox6(StyleBuilder styleDefault, java.sql.Date postDate) {

		return

		DynamicReports.cmp.horizontalList(

				DynamicReports.cmp.text("คำเตือน")
						.setStyle(DynamicReports.stl.style(styleDefault).setLeftPadding(3)
								.setLeftBorder(DynamicReports.stl.penThin()).setTopBorder(DynamicReports.stl.penThin())
								.setBottomBorder(DynamicReports.stl.penThin()))
						.setFixedWidth(40).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),

				DynamicReports.cmp.text(
						"ผู้ที่มีหน้าที่ออกหนังสือรับรองการหักภาษี ณ ที่จ่าย\nฝ่าฝืนไม่ปฏิบัติตามมาตรา 50 ทวิ   แห่งประมวล\nรัษฎากร   ต้องรับโทษทางอาญาตามมาตรา 35\nแห่งประมวลรัษฎากร")
						.setStyle(DynamicReports.stl.style(styleDefault).setRightBorder(DynamicReports.stl.penThin())
								.setTopBorder(DynamicReports.stl.penThin())
								.setBottomBorder(DynamicReports.stl.penThin()))
						.setFixedWidth(130).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),

				DynamicReports.cmp.horizontalGap(8),

				DynamicReports.cmp.verticalList(

						DynamicReports.cmp
								.text("ขอรับรองว่าข้อความและตัวเลขดังกล่าวข้างต้นถูกต้องตรงกับความจริงทุกประการ")
								.setStyle(DynamicReports.stl.style(styleDefault).setFontSize(12))
								.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),

						DynamicReports.cmp.horizontalList(

								DynamicReports.cmp.text("").setStyle(DynamicReports.stl.style(styleDefault)),

								DynamicReports.cmp.text("ลงชื่อ").setStyle(DynamicReports.stl.style(styleDefault))
										.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT).setFixedWidth(30),

								DynamicReports.cmp
										.verticalList(
												DynamicReports.cmp.text("").setStyle(
														DynamicReports.stl.style(styleDefault)
																.setBottomBorder(DynamicReports.stl.penDotted()))
														.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
												DynamicReports.cmp
														.text(FnDate.getDay(postDate) + "    /    "
																+ FnDate.getThaiMonthFullName(FnDate.getMonth(postDate))
																+ "    /    " + FnDate.getYearBuddhist(postDate))
														.setStyle(DynamicReports.stl.style(styleDefault)
																.setBottomBorder(DynamicReports.stl.penDotted()))
														.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
												DynamicReports.cmp.text("(วัน เดือน ปี ที่ออกหนังสือรับรองฯ)")
														.setStyle(DynamicReports.stl.style(styleDefault))
														.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER))
										.setFixedWidth(120),

								DynamicReports.cmp.text("ผู้จ่ายเงิน").setStyle(DynamicReports.stl.style(styleDefault))
										.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT).setFixedWidth(30),

								DynamicReports.cmp.verticalList(
										// DynamicReports.cmp.ellipse().setFixedDimension(30, 30)
										DynamicReports.cmp.verticalGap(5),
										DynamicReports.cmp.text("ประทับตรา\nนิติบุคคลถ้ามี")
												.setStyle(DynamicReports.stl.style(styleDefault)
														.setBorder(DynamicReports.stl.penThin()).setPadding(0)
														.setTextAlignment(HorizontalTextAlignment.CENTER,
																VerticalTextAlignment.MIDDLE))
												.setPositionType(ComponentPositionType.FIX_RELATIVE_TO_TOP)
												.setFixedWidth(35),
										DynamicReports.cmp.verticalGap(5)),

								DynamicReports.cmp.text("").setStyle(DynamicReports.stl.style(styleDefault)))

				).setStyle(DynamicReports.stl.style(styleDefault).setLeftBorder(DynamicReports.stl.penThin())
						.setRightBorder(DynamicReports.stl.penThin()).setTopBorder(DynamicReports.stl.penThin())
						.setBottomBorder(DynamicReports.stl.penThin()))

		);

	}

	public static ComponentBuilder<?, ?> getBoxNum(String text1, int fixedWidth, StyleBuilder styleDefault,
			boolean withBorder) {
		if (withBorder) {
			return DynamicReports.cmp.text(text1)
					.setStyle(DynamicReports.stl.style(styleDefault).setFontSize(10)
							.setBorder(DynamicReports.stl.penThin()).bold())
					.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER).setStretchType(StretchType.NO_STRETCH)
					.setFixedHeight(14).setFixedWidth(fixedWidth);

		} else {
			return DynamicReports.cmp.text(text1).setStyle(DynamicReports.stl.style(styleDefault).setFontSize(10))
					.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER).setStretchType(StretchType.NO_STRETCH)
					.setFixedHeight(14).setFixedWidth(fixedWidth);
		}
	}

	/**
	 * getTextboxDetail
	 * 
	 * @param text1
	 * @param fontsize
	 * @param leftPadding
	 * @param rightPadding
	 * @param fixedHeight
	 * @param fixedWidth
	 * @param showBorder
	 * @param horizontalTextAlignment
	 * @return
	 */
	public static ComponentBuilder<?, ?> getTextboxDetail(String text1, int fontsize, int leftPadding, int rightPadding,
			int fixedHeight, int fixedWidth, boolean showBorder, HorizontalTextAlignment horizontalTextAlignment,
			VerticalTextAlignment verticalTextAlignment, StyleBuilder styleDefault) {

		TextFieldBuilder txt = DynamicReports.cmp.text(text1);

		StyleBuilder styleText = DynamicReports.stl.style(styleDefault);

		if (fontsize > 0) {
			styleText.setFontSize(fontsize);
		}
		if (leftPadding > 0) {
			styleText.setLeftPadding(leftPadding);
		}
		if (rightPadding > 0) {
			styleText.setRightPadding(rightPadding);
		}
		if (showBorder) {
			styleText.setBorder(DynamicReports.stl.penThin());
		}
		if (horizontalTextAlignment != null && verticalTextAlignment != null) {
			styleText.setTextAlignment(horizontalTextAlignment, verticalTextAlignment);
		}

		if (horizontalTextAlignment != null) {
			txt.setHorizontalTextAlignment(horizontalTextAlignment);
		}
		if (fixedHeight > 0) {
			txt.setFixedHeight(fixedHeight);
		}
		if (fixedWidth > 0) {
			txt.setFixedWidth(fixedWidth);
		}

		txt.setStyle(styleText);

		return txt;

	}

	public static ComponentBuilder<?, ?> getBox3_line(String text1, String text2, String text3, String text4,
			String text5, String text6, String text7, int fixwidth, boolean bottomBorder, StyleBuilder styleDefault) {

		StyleBuilder styleDefault1 = DynamicReports.stl.style(styleDefault);
		styleDefault1.setLeftBorder(DynamicReports.stl.penThin());
		if (bottomBorder) {
			styleDefault1.setBottomBorder(DynamicReports.stl.penThin());
		}

		StyleBuilder styleDefault2 = DynamicReports.stl.style(styleDefault);
		styleDefault2.setRightBorder(DynamicReports.stl.penThin());
		styleDefault2.setRightPadding(2);
		if (bottomBorder) {
			styleDefault2.setBottomBorder(DynamicReports.stl.penThin());
		}

		return DynamicReports.cmp.horizontalList()
				.add(DynamicReports.cmp.text(text1).setFixedWidth(fixwidth)
						.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER).setStyle(styleDefault1))
				.add(DynamicReports.cmp.text(text2).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)
						.setStyle(styleDefault2))
				.add(DynamicReports.cmp.text(text3).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
						.setFixedWidth(60).setStyle(styleDefault2))
				.add(DynamicReports.cmp.text(text4).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
						.setFixedWidth(65).setStyle(styleDefault2))
				.add(DynamicReports.cmp.text(text5).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
						.setFixedWidth(15).setStyle(styleDefault2))
				.add(DynamicReports.cmp.text(text6).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
						.setFixedWidth(65).setStyle(styleDefault2))
				.add(DynamicReports.cmp.text(text7).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
						.setFixedWidth(15).setStyle(styleDefault2));
	}

	/**
	 * @param lst_select
	 * @param loginBean
	 * @throws Exception
	 */
	public static void saveCancel(List<FModelHasMap> lst_select, LoginBean loginBean) throws Exception {

		try (FDbc dbc = FDbc.connectMasterDb()) {
			dbc.beginTrans();

			int count = 0;
			for (FModelHasMap dat : lst_select) {

				count++;
				String vou_type = dat.getString("VOU_TYPE");
				String vou_no = dat.getString("VOU_NO");
				int vou_seq = dat.getInt("VOU_SEQ");
				int vou_dseq = dat.getInt("VOU_DSEQ");

				// ของเดิม
				TboACGL_VATWH_CR acgl_vatwh_cr = new TboACGL_VATWH_CR();

				acgl_vatwh_cr.setCOMP_CDE(loginBean.getCOMP_CDE());
				acgl_vatwh_cr.setVOU_TYPE(vou_type);
				acgl_vatwh_cr.setVOU_NO(vou_no);
				acgl_vatwh_cr.setVOU_SEQ(vou_seq);
				acgl_vatwh_cr.setVOU_DSEQ(vou_dseq);

				if (!TbfACGL_VATWH_CR.find(dbc, acgl_vatwh_cr)) {
					throw new Exception("ไม่พบรายการ");
				}

				// clone ของเดิมไว้ก่อน เพื่อเก็บ history docno ไว้ในระบบ
				TboACGL_VATWH_CR clone_acgl_vatwh_cr = new TboACGL_VATWH_CR();

				clone_acgl_vatwh_cr.setCOMP_CDE(loginBean.getCOMP_CDE());
				clone_acgl_vatwh_cr.setVOU_TYPE(vou_type);
				clone_acgl_vatwh_cr.setVOU_NO(vou_no);
				clone_acgl_vatwh_cr.setVOU_SEQ(vou_seq);
				clone_acgl_vatwh_cr.setVOU_DSEQ(vou_dseq);

				if (!TbfACGL_VATWH_CR.find(dbc, clone_acgl_vatwh_cr)) {
					throw new Exception("ไม่พบรายการ");
				}

				if (!Fnc.isEmpty(acgl_vatwh_cr.getDOCNO())) {

					// update ให้ว่างๆ
					acgl_vatwh_cr.setDOCNO("");

					if (!TbfACGL_VATWH_CR.update(dbc, acgl_vatwh_cr, "COALESCE(DOCNO,'') != '' ")) {
						throw new Exception("รายการที่ " + count + " ไม่สามารถยกเลิกเลขเอกสาร");
					}

					// เก็บเข้า history เปลี่ยน VOU_TYPE=VOU_TYPE+"_CC" , VOU_DSEQ = max(VOU_DSEQ)
					// ของ (VOU_TYPE+"_CC" and VOU_NO and VOU_SEQ)
					// VOU_TYPE  =  varchar(10)  จึงต้องกันเรื่องความยาวไว้ก่อน
					String cc_vou_type = Fnc.getStrLeft(clone_acgl_vatwh_cr.getVOU_TYPE().trim(), 7) + "_CC";
					int max_VOU_DSEQ = getMax_VOU_DSEQ(dbc, clone_acgl_vatwh_cr, cc_vou_type);
					max_VOU_DSEQ++;// บวกเข้า 1 ค่า

					clone_acgl_vatwh_cr.setVOU_TYPE(cc_vou_type);
					clone_acgl_vatwh_cr.setVOU_DSEQ(max_VOU_DSEQ);
					clone_acgl_vatwh_cr.setRECSTA(9);

					if (!TbfACGL_VATWH_CR.insert(dbc, clone_acgl_vatwh_cr)) {
						throw new Exception("รายการที่ " + count + " ไม่สามารถสร้างเลขเอกสาร");
					}

				}

			}

			dbc.commit();

		}

	}

	private static int getMax_VOU_DSEQ(FDbc dbc, TboACGL_VATWH_CR clone_acgl_vatwh_cr, String cc_vou_type)
			throws SQLException {

		int ret = 0;
		SqlStr sql = new SqlStr();
		sql.addLine("select max(VOU_DSEQ) as f1 ");
		sql.addLine("from " + TboACGL_VATWH_CR.tablename);
		sql.addLine("where COMP_CDE = '" + Fnc.sqlQuote(clone_acgl_vatwh_cr.getCOMP_CDE()) + "' ");
		sql.addLine("and VOU_TYPE = '" + Fnc.sqlQuote(cc_vou_type) + "' ");
		sql.addLine("and VOU_NO='" + Fnc.sqlQuote(clone_acgl_vatwh_cr.getVOU_NO()) + "' ");
		sql.addLine("and VOU_SEQ=" + clone_acgl_vatwh_cr.getVOU_SEQ());
		logger.info(sql.getSql());
		try (java.sql.ResultSet rs = dbc.getResultSet(sql.getSql());) {
			if (rs.next()) {
				ret = rs.getInt("f1");
			}
		}

		return ret;

	}

}

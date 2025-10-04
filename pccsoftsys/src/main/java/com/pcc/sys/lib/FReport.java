package com.pcc.sys.lib;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.zkoss.zul.Filedownload;

import com.googlecode.jthaipdf.jasperreports.engine.export.ThaiJRPdfExporter;
import com.pcc.sys.ui.FrmPreviewReport;

import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.StretchType;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleDocxExporterConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOdsReportConfiguration;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

public class FReport {

	public static String useExcel2007 = "YES";

	/**
	 * การนำไฟล์ .jrxml มา compile โดยตรงแทน
	 * @param reportResourcePath
	 * @return
	 * @throws JRException
	 * @throws Exception
	 */
	public static JasperReport getJasperReport(String reportResourcePath) throws JRException, Exception {
		InputStream in = new FReport().getClass().getResourceAsStream(reportResourcePath);
		if (in == null) {
			throw new Exception("ไม่พบไฟล์ : " + reportResourcePath);
		}
		return net.sf.jasperreports.engine.JasperCompileManager.compileReport(in);
	}

	public static JasperPrint builtRepByResultSet(String reportPath, Map<String, Object> reportPara,
			java.sql.ResultSet rs) throws SQLException, Exception {

		JRDataSource reportDataSource = null;

		if (rs == null) {
			reportDataSource = new JREmptyDataSource();
		} else {
			rs.beforeFirst();
			reportDataSource = new JRResultSetDataSource(rs);
		}

		if (reportPara == null) {
			reportPara = new HashMap<String, Object>();
		}

		JasperPrint jasperPrint = null;
		if (reportPath.toUpperCase().endsWith("JRXML")) {
			JasperReport jr = getJasperReport(reportPath);
			jasperPrint = JasperFillManager.fillReport(jr, reportPara, reportDataSource);
		} else if (reportPath.toUpperCase().endsWith("JASPER")) {
			InputStream instm = new FReport().getClass().getResourceAsStream(reportPath);
			jasperPrint = JasperFillManager.fillReport(instm, reportPara, reportDataSource);
		}

		return jasperPrint;

	}

	public static JasperPrint builtRepByBean(String reportPath, Map<String, Object> reportPara,
			FJRBeanCollectionDataSource fJrBeancollectiondatasource) throws Exception {

		if (reportPara == null) {
			reportPara = new HashMap<String, Object>();
		}

		JasperPrint jasperPrint = null;
		if (reportPath.toUpperCase().endsWith("JRXML")) {
			JasperReport jr = getJasperReport(reportPath);
			jasperPrint = JasperFillManager.fillReport(jr, reportPara, fJrBeancollectiondatasource);
		} else if (reportPath.toUpperCase().endsWith("JASPER")) {
			InputStream instm = new FReport().getClass().getResourceAsStream(reportPath);
			jasperPrint = JasperFillManager.fillReport(instm, reportPara, fJrBeancollectiondatasource);
		}
		return jasperPrint;
	}

	public static JasperPrint builtRepByMap(String reportPath, Map<String, Object> reportPara,
			FJRMapCollectionDataSource jrMapCollectionDataSource) throws Exception {

		if (reportPara == null) {
			reportPara = new HashMap<String, Object>();
		}

		JasperPrint jasperPrint = null;
		if (reportPath.toUpperCase().endsWith("JRXML")) {
			JasperReport jr = getJasperReport(reportPath);
			jasperPrint = JasperFillManager.fillReport(jr, reportPara, jrMapCollectionDataSource);
		} else if (reportPath.toUpperCase().endsWith("JASPER")) {
			InputStream instm = new FReport().getClass().getResourceAsStream(reportPath);
			jasperPrint = JasperFillManager.fillReport(instm, reportPara, jrMapCollectionDataSource);
		}

		return jasperPrint;
	}

	/**
	 * แสดง Report ออกเป็น  List ของ PDF
	 * @param jasperPrintList
	 * @throws Exception
	 */
	public static void viewPdf(List<JasperPrint> jasperPrintList) throws Exception {

		if (FConfig.getConfig2_Report_In_Iframe()) {
			viewPdfToIframe(jasperPrintList);
		} else {
			viewPdfToLoad(jasperPrintList);
		}

	}

	private static void viewPdfToIframe(List<JasperPrint> jasperPrintList) throws Exception {
		FrmPreviewReport frmReport = (FrmPreviewReport) ZkUtil.callZulFile("/com/pcc/sys/zul/FrmPreviewReport.zul");
		frmReport.setJasperPrints(jasperPrintList);
		frmReport.setTitle("รายงาน");
		frmReport.doModal();
	}

	private static void viewPdfToLoad(List<JasperPrint> jasperPrintList) throws Exception {

		ThaiJRPdfExporter exporter = new ThaiJRPdfExporter();
		//net.sf.jasperreports.engine.export.JRPdfExporter exporter = new  net.sf.jasperreports.engine.export.JRPdfExporter();

		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();) {

			exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(bos));
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();

//		if (!Fnc.isEmpty(this.printerName)) {
//			String js = "var pp = this.getPrintParams(); \n";
//			if (!Fnc.isEmpty(this.printerName)) { // ระบุ printer
//				js += "pp.printerName = \"" + printerName + "\"; \n";
//				js += "pp.pageHandling = pp.constants.handling.none; \n";
//				js += "var fv = pp.constants.flagValues; \n";
//				js += "pp.flags |= fv.setPageSize; \n";
//			} else {
//				js += "pp.pageHandling = pp.constants.handling.fit; \n"; // เลือกแบบ  Fit อัตโนมัติป้องกัน Report ขอบหาย
//			}
//			js += "this.print(pp);";
//
//			configuration.setPdfJavaScript(js);
//		}

			exporter.setConfiguration(configuration);
			exporter.exportReport();
			//String content_Type = "application/octet-stream";
			String content_Type = "application/pdf";
			Filedownload.save(bos.toByteArray(), content_Type,
					"report" + FnDate.getDateString(FnDate.getTodayDateTime(), "yyyyMMddHHmmss") + ".pdf");

		}

	}

	/**
	 * แสดง Report จาก inputstream_pdf
	 * @param inputstream_pdf
	 * @throws Exception 
	 */
	public static void viewPdf(InputStream inputstream_pdf) throws Exception {

		if (FConfig.getConfig2_Report_In_Iframe()) {
			viewPdfToIframe(inputstream_pdf);
		} else {
			viewPdfToLoad(inputstream_pdf);
		}

	}

	private static void viewPdfToIframe(InputStream inputstream_pdf) throws Exception {
		//ตัวอย่างการดึงไฟล์ก่อนส่งมาที่ method นี้ InputStream inst2 = FReport.class.getResourceAsStream("/mydoc.pdf");
		FrmPreviewReport frmReport = (FrmPreviewReport) ZkUtil.callZulFile("/com/pcc/sys/zul/FrmPreviewReport.zul");
		frmReport.setJasperPrints(null);
		frmReport.setTitle("รายงาน");
		frmReport.setInputStream_pdf(inputstream_pdf);
		frmReport.doModal();
	}

	private static void viewPdfToLoad(InputStream inputstream_pdf) {
		//String content_Type = "application/octet-stream";
		String content_Type = "application/pdf";
		Filedownload.save(inputstream_pdf, content_Type,
				"report" + FnDate.getDateString(FnDate.getTodayDateTime(), "yyyyMMddHHmmss") + ".pdf");
	}

	/**
	 * Export เป็น Excel โดยไม่ต้องแสดงหน้า Preview เป็นการบังคับให้ Download อย่างเดียว
	 * @param jasperPrint
	 * @param fileName ไม่ต้องใส่นามสกุล
	 * @throws Exception
	 */
	public static void exportExcel(JasperPrint jasperPrint, String fileName) throws Exception {

		//== แบบใหม่ v 5.5.x ขึ้นไป

		if (useExcel2007.equals("YES")) {

			//======= สำหรับ excel 2007 ขึ้นไป 
			SimpleXlsxReportConfiguration repConfiguration = new SimpleXlsxReportConfiguration();

			repConfiguration.setOnePagePerSheet(false);
			repConfiguration.setDetectCellType(true);
			repConfiguration.setCollapseRowSpan(false);
			//repConfiguration.setIgnoreCellBackground(true);
			//repConfiguration.setIgnoreCellBorder(true);
			repConfiguration.setRemoveEmptySpaceBetweenColumns(true);
			repConfiguration.setRemoveEmptySpaceBetweenRows(true);
			repConfiguration.setWhitePageBackground(false);

			JRXlsxExporter exporter = new JRXlsxExporter();

			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

			try (ByteArrayOutputStream bos = new ByteArrayOutputStream();) {

				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(bos));
				exporter.setConfiguration(repConfiguration);
				exporter.exportReport();

				String fName = "report.xlsx";
				if (!fileName.equals("")) {
					fName = fileName;
					if (fName.endsWith(".xls")) {
						fName = fName.replace(".xls", ".xlsx");
					}
					if (!fName.endsWith(".xlsx")) {
						fName = fName + ".xlsx";
					}
				}

				Filedownload.save(bos.toByteArray(), "application/octet-stream", fName);

			}

		} else {
			//=== สำหรับ excel 2003 
			SimpleXlsReportConfiguration repConfiguration = new SimpleXlsReportConfiguration();

			repConfiguration.setOnePagePerSheet(false);
			repConfiguration.setDetectCellType(true);
			repConfiguration.setCollapseRowSpan(false);
			//repConfiguration.setIgnoreCellBackground(true);
			//repConfiguration.setIgnoreCellBorder(true);
			repConfiguration.setRemoveEmptySpaceBetweenColumns(true);
			repConfiguration.setRemoveEmptySpaceBetweenRows(true);
			repConfiguration.setWhitePageBackground(false);

			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

			try (ByteArrayOutputStream bos = new ByteArrayOutputStream();) {
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(bos));
				exporter.setConfiguration(repConfiguration);
				exporter.exportReport();

				String fName = "report.xls";
				if (!fileName.equals("")) {
					fName = fileName;
					if (fName.endsWith(".xlsx")) {
						fName = fName.replace(".xlsx", ".xls");
					}
					if (!fName.endsWith(".xls")) {
						fName = fName + ".xls";
					}
				}

				Filedownload.save(bos.toByteArray(), "application/octet-stream", fName);
			}

		}

	}

	/**
	 * สร้าง excel
	 * @param fJasperPrintList
	 * @param fileName  ไม่ต้องใส่นามสกุล
	 * @throws Exception
	 */
	public static void exportExcel(FJasperPrintList fJasperPrintList, String fileName) throws Exception {

		//== แบบใหม่

		if (useExcel2007.equals("YES")) {

			SimpleXlsxReportConfiguration repConfiguration = new SimpleXlsxReportConfiguration();

			repConfiguration.setOnePagePerSheet(false);
			repConfiguration.setDetectCellType(true);
			repConfiguration.setCollapseRowSpan(false);
			//repConfiguration.setIgnoreCellBackground(true);
			//repConfiguration.setIgnoreCellBorder(true);
			repConfiguration.setRemoveEmptySpaceBetweenColumns(true);
			repConfiguration.setRemoveEmptySpaceBetweenRows(true);
			repConfiguration.setWhitePageBackground(false);

			JRXlsxExporter exporter = new JRXlsxExporter();
			exporter.setExporterInput(SimpleExporterInput.getInstance(fJasperPrintList.getJasperPrintLst()));
			try (ByteArrayOutputStream bos = new ByteArrayOutputStream();) {
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(bos));
				exporter.setConfiguration(repConfiguration);
				exporter.exportReport();

				String fName = "report.xlsx";
				if (!fileName.equals("")) {
					fName = fileName;
					if (fName.endsWith(".xls")) {
						fName = fName.replace(".xls", ".xlsx");
					}
					if (!fName.endsWith(".xlsx")) {
						fName = fName + ".xlsx";
					}
				}

				Filedownload.save(bos.toByteArray(), "application/octet-stream", fName);
			}

		} else {

			//==== excel 2003
			SimpleXlsReportConfiguration repConfiguration = new SimpleXlsReportConfiguration();

			repConfiguration.setOnePagePerSheet(false);
			repConfiguration.setDetectCellType(true);
			repConfiguration.setCollapseRowSpan(false);
			//repConfiguration.setIgnoreCellBackground(true);
			//repConfiguration.setIgnoreCellBorder(true);
			repConfiguration.setRemoveEmptySpaceBetweenColumns(true);
			repConfiguration.setRemoveEmptySpaceBetweenRows(true);
			repConfiguration.setWhitePageBackground(false);

			JRXlsExporter exporter = new JRXlsExporter();

			exporter.setExporterInput(SimpleExporterInput.getInstance(fJasperPrintList.getJasperPrintLst()));
			try (ByteArrayOutputStream bos = new ByteArrayOutputStream();) {
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(bos));
				exporter.setConfiguration(repConfiguration);
				exporter.exportReport();

				String fName = "report.xls";
				if (!fileName.equals("")) {
					fName = fileName;
					if (fName.endsWith(".xlsx")) {
						fName = fName.replace(".xlsx", ".xls");
					}
					if (!fName.endsWith(".xls")) {
						fName = fName + ".xls";
					}
				}

				Filedownload.save(bos.toByteArray(), "application/octet-stream", fName);
			}

		}

	}

	/**
	 * Export เป็น Excel และ save ไปยังไฟล์ filePath
	 * @param jasperPrint
	 * @param filePath เช่น d:\tmp\abc.xls  รวม paht+fileName 
	 * @throws Exception
	 */
	public static void exportExcelToFile(JasperPrint jasperPrint, String[] filePath) throws Exception {

		//== แบบใหม่ v 5.5.x ขึ้นไป
		if (useExcel2007.equals("YES")) {

			//======= สำหรับ excel 2007 ขึ้นไป
			String fName = "";
			if (filePath[0].trim().endsWith(".xlsx")) {
				fName = filePath[0];
			} else {
				fName = filePath[0] + ".xlsx";
			}
			filePath[0] = fName;

			SimpleXlsxReportConfiguration repConfiguration = new SimpleXlsxReportConfiguration();

			repConfiguration.setOnePagePerSheet(false);
			repConfiguration.setDetectCellType(true);
			repConfiguration.setCollapseRowSpan(false);
			//repConfiguration.setIgnoreCellBackground(true);
			//repConfiguration.setIgnoreCellBorder(true);
			repConfiguration.setRemoveEmptySpaceBetweenColumns(true);
			repConfiguration.setRemoveEmptySpaceBetweenRows(true);
			repConfiguration.setWhitePageBackground(false);

			JRXlsxExporter exporter = new JRXlsxExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(filePath[0]));
			exporter.setConfiguration(repConfiguration);
			exporter.exportReport();

		} else {

			String fName = "";
			if (filePath[0].trim().endsWith(".xls")) {
				fName = filePath[0];
			} else {
				fName = filePath[0] + ".xls";
			}
			filePath[0] = fName;

			SimpleXlsReportConfiguration repConfiguration = new SimpleXlsReportConfiguration();

			repConfiguration.setOnePagePerSheet(false);
			repConfiguration.setDetectCellType(true);
			repConfiguration.setCollapseRowSpan(false);
			//repConfiguration.setIgnoreCellBackground(true);
			//repConfiguration.setIgnoreCellBorder(true);
			repConfiguration.setRemoveEmptySpaceBetweenColumns(true);
			repConfiguration.setRemoveEmptySpaceBetweenRows(true);
			repConfiguration.setWhitePageBackground(false);

			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(filePath[0]));
			exporter.setConfiguration(repConfiguration);

			exporter.exportReport();

		}

	}

	/**
	 * Export เป็น Excel และ save ไปยังไฟล์ filePath
	 * @param fJasperPrintList
	 * @param filePath  เช่น d:\tmp\abc.xls  รวม paht+fileName
	 * @throws Exception
	 */
	public static void exportExcelToFile(FJasperPrintList fJasperPrintList, String[] filePath) throws Exception {

		//== แบบใหม่
		if (useExcel2007.equals("YES")) {//excel 2007

			String fName = "";
			if (filePath[0].trim().endsWith(".xlsx")) {
				fName = filePath[0];
			} else {
				fName = filePath[0] + ".xlsx";
			}
			filePath[0] = fName;

			SimpleXlsxReportConfiguration repConfiguration = new SimpleXlsxReportConfiguration();

			repConfiguration.setOnePagePerSheet(false);
			repConfiguration.setDetectCellType(true);
			repConfiguration.setCollapseRowSpan(false);
			//repConfiguration.setIgnoreCellBackground(true);
			//repConfiguration.setIgnoreCellBorder(true);
			repConfiguration.setRemoveEmptySpaceBetweenColumns(true);
			repConfiguration.setRemoveEmptySpaceBetweenRows(true);
			repConfiguration.setWhitePageBackground(false);

			JRXlsxExporter exporter = new JRXlsxExporter();
			exporter.setExporterInput(SimpleExporterInput.getInstance(fJasperPrintList.getJasperPrintLst()));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(filePath[0]));
			exporter.setConfiguration(repConfiguration);
			exporter.exportReport();

		} else {//excel 2003

			String fName = "";
			if (filePath[0].trim().endsWith(".xls")) {
				fName = filePath[0];
			} else {
				fName = filePath[0] + ".xls";
			}
			filePath[0] = fName;

			SimpleXlsReportConfiguration repConfiguration = new SimpleXlsReportConfiguration();

			repConfiguration.setOnePagePerSheet(false);
			repConfiguration.setDetectCellType(true);
			repConfiguration.setCollapseRowSpan(false);
			//repConfiguration.setIgnoreCellBackground(true);
			//repConfiguration.setIgnoreCellBorder(true);
			repConfiguration.setRemoveEmptySpaceBetweenColumns(true);
			repConfiguration.setRemoveEmptySpaceBetweenRows(true);
			repConfiguration.setWhitePageBackground(false);

			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setExporterInput(SimpleExporterInput.getInstance(fJasperPrintList.getJasperPrintLst()));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(filePath[0]));
			exporter.setConfiguration(repConfiguration);
			exporter.exportReport();

		}

	}

	/**
	 * Export เป็น open office spreetsheet โดยไม่ต้องแสดงหน้า Preview เป็นการบังคับให้ Download อย่างเดียว
	 * @param jasperPrint
	 * @param fileName
	 * @throws Exception
	 */
	public static void exportOds(JasperPrint jasperPrint, String fileName) throws Exception {

		//== แบบใหม่ v 5.5 ขึ้นไป

		SimpleOdsReportConfiguration reportConfig = new SimpleOdsReportConfiguration();

		reportConfig.setOnePagePerSheet(false);
		reportConfig.setDetectCellType(true);
		reportConfig.setCollapseRowSpan(false);
		reportConfig.setIgnoreCellBackground(true);
		reportConfig.setIgnoreCellBorder(true);
		reportConfig.setRemoveEmptySpaceBetweenColumns(true);
		reportConfig.setRemoveEmptySpaceBetweenRows(true);
		reportConfig.setWhitePageBackground(false);

		JROdsExporter exporter = new JROdsExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();) {
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(bos));
			exporter.setConfiguration(reportConfig);
			exporter.exportReport();

			String fName = "report.ods";
			if (fileName != null) {
				if (!fileName.trim().equals("")) {
					fName = fileName + ".ods";
				}
			}

			Filedownload.save(bos.toByteArray(), "application/octet-stream", fName);
		}

	}

	public static void viewMsWordToLoad(FJasperPrintList fJasperPrint, String fileName) throws Exception {

		//== แบบใหม่
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();) {

			JRDocxExporter exporter = new JRDocxExporter();

			SimpleDocxExporterConfiguration reportConfig = new SimpleDocxExporterConfiguration();

			exporter.setExporterInput(SimpleExporterInput.getInstance(fJasperPrint.getJasperPrintLst()));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(bos));
			exporter.setConfiguration(reportConfig);
			exporter.exportReport();

			String fName = "report.docx";
			if (fileName != null) {
				if (!fileName.trim().equals("")) {
					if (fileName.trim().endsWith(".docx")) {
						fName = fileName;
					} else {
						fName = fileName + ".docx";
					}
				}
			}

			Filedownload.save(bos.toByteArray(), "application/octet-stream", fName);
		}

	}

	/**
	 * สร้าง check box ในรายงาน
	 * @param check
	 * @param fixedWidth ถ้าระบุ 0 จะใช้ default auto ตาม fontSize
	 * @param fixedHeight ถ้าระบุ 0 จะใช้ default auto ตาม fontSize
	 * @param fontSize ต้อง >= 0 ถ้าระบุ 0 จะ default=11
	 * @return
	 */
	public static ComponentBuilder<?, ?> getBoxCheck(
			boolean check, int fixedWidth, int fixedHeight, int fontSize) {
		//สำหรับเริ่มต้น
		StyleBuilder styleDefault = DynamicRepTemplates.getRootStyle()
				.setPadding(0)
				.setFontName("WINGDNG2")//trick ใส่ check box ด้วย font ที่เป็นอักขระเครื่องหมายถูกต้อง
				.setBorder(DynamicReports.stl.penThin())
				.setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
				.bold();

		if (fontSize > 0) {
			styleDefault.setFontSize(fontSize);
		} else {
			styleDefault.setFontSize(11);
		}

		String text1 = check ? "P" : "";//trick ตัว P ให้เท่ากับเครื่องหมายถูกต้องใน font WINGDNG2

		TextFieldBuilder compCheck = DynamicReports.cmp.text(text1)
				.setStyle(styleDefault)
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
				.setStretchType(StretchType.NO_STRETCH);

		if (fixedHeight > 0) {
			compCheck.setFixedHeight(fixedHeight);
		}
		if (fixedWidth > 0) {
			compCheck.setFixedWidth(fixedWidth);
		}

		return compCheck;

	}

	@SuppressWarnings("serial")
	public static AbstractSimpleExpression<BigDecimal> getVarBigDecimal(String varName) {
		return new AbstractSimpleExpression<BigDecimal>() {
			@Override
			public BigDecimal evaluate(ReportParameters reportParameters) {
				return Fnc.getBigDecimalValue(reportParameters.getVariableValue(varName));
			}
		};
	}

	/**
	 * export report เป็น JPG
	 * @param lstJasperPrint
	 * @param zoom  ถ้าระบุเป็น 0l ระบบจะกำหนดเป็น 1.6f อัตโนมัติ
	 * @throws Exception
	 */
	public static void viewToJpg(List<JasperPrint> lstJasperPrint, float zoom) throws Exception {

		try {

			float useZoom = 1.6f;
			if (zoom != 0l) {
				useZoom = zoom;
			}

			//จากการทดสอบไม่สามารถ loop ได้ by preecha 22/5/62
//			for (JasperPrint jasperPrint : lstJasperPrint) {
//				int pageSize = jasperPrint.getPages().size();
//				for (int p = 0; p < pageSize; p++) {
//					System.out.println("p:" + p);
//					JasperPrintManager.getInstance(DefaultJasperReportsContext.getInstance());
//					BufferedImage rendered_image = (BufferedImage) JasperPrintManager.printPageToImage(jasperPrint, p, useZoom);
//					ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
//					ImageIO.write(rendered_image, "jpg", bos1);
//					Filedownload.save(bos1.toByteArray(), "application/octet-stream", "report" + p + ".jpg");
//
//				}
//			}

			JasperPrintManager.getInstance(DefaultJasperReportsContext.getInstance());
			BufferedImage rendered_image = (BufferedImage) JasperPrintManager.printPageToImage(lstJasperPrint.get(0), 0,
					useZoom);
			try (ByteArrayOutputStream bos1 = new ByteArrayOutputStream();) {
				ImageIO.write(rendered_image, "jpg", bos1);
				Filedownload.save(bos1.toByteArray(), "application/octet-stream", "report.jpg");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}
	
	/**
	 * แปลงเป็น pdf และข้อความแบบBase64
	 * @param lstJasperPrint
	 * @return [ข้อความ base64 ของรายงาน , format file]
	 * @throws JRException
	 */
	public static String[] exportBase64_of_PDF(List<JasperPrint> lstJasperPrint) throws JRException {

		com.googlecode.jthaipdf.jasperreports.engine.export.ThaiJRPdfExporter exporter = new com.googlecode.jthaipdf.jasperreports.engine.export.ThaiJRPdfExporter();

		exporter.setExporterInput(SimpleExporterInput.getInstance(lstJasperPrint));
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(bos));
		SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();

		exporter.setConfiguration(configuration);
		exporter.exportReport();
		String ret = java.util.Base64.getEncoder().encodeToString(bos.toByteArray());
		return new String[] { ret, "pdf" };
	}
	
	/**
	 * แบบที่ละ jasperPrint List สำหรับ Excel 2007  ออกเป็น base64
	 * @param lstJasperPrint
	 * @return [ข้อความ base64 ของรายงาน , format file]
	 * @throws Exception
	 */
	public static String[] exportBase64_of_EXCEL(List<JasperPrint> lstJasperPrint) throws Exception {

		try {

			//== แบบใหม่
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			JRXlsxExporter exporter = new JRXlsxExporter();

			exporter.setExporterInput(SimpleExporterInput.getInstance(lstJasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(bos));

			SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();

			configuration.setOnePagePerSheet(false);
			configuration.setDetectCellType(true);
			configuration.setCollapseRowSpan(false);
			//configuration.setIgnoreCellBackground(true);
			//configuration.setIgnoreCellBorder(true);
			configuration.setRemoveEmptySpaceBetweenColumns(true);
			configuration.setRemoveEmptySpaceBetweenRows(true);
			configuration.setWhitePageBackground(false);

			exporter.setConfiguration(configuration);
			exporter.exportReport();

			String ret = java.util.Base64.getEncoder().encodeToString(bos.toByteArray());
			return new String[] { ret, "xlsx" };
		} catch (Exception ex) {
			System.out.println(ex);
			throw ex;
		}

	}
	
	/**
	 * ส่งไฟล์ต่างๆ ออกไปเพื่อให้ front end down load
	 * @param data  เป็น string ที่เข้ารหัส Base64
	 * @param format นามสกุลไฟล์
	 * @param menuId2 รหัสโปรแกรม
	 */
	public static void saveFile(String data, String format, String menuId2) {
		byte[] decode = java.util.Base64.getDecoder().decode(data);
		Filedownload.save(decode, "application/octet-stream",
				menuId2 + "-" + FnDate.getTodayDateTime("yyMMdd-HHmmss") + "." + format);
	}

}

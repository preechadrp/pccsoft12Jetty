package com.pcc.sys.ui;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import org.zkoss.util.media.AMedia;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Window;

import com.pcc.sys.lib.Msg;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

public class FrmPreviewReport extends Window {

	private static final long serialVersionUID = 1L;
	protected Iframe iframeReport;
	private List<JasperPrint> jasperPrintLst;
	private InputStream inputstream_pdf = null;

	public void onCreate() {
		if (jasperPrintLst != null) {
			viewReportFromJasperPrint();
		} else {
			viewReportFromStream();
		}
	}

	private void viewReportFromJasperPrint() {

		try {

			iframeReport = (Iframe) getFellow("iframeReport");

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			com.googlecode.jthaipdf.jasperreports.engine.export.ThaiJRPdfExporter exporter = new com.googlecode.jthaipdf.jasperreports.engine.export.ThaiJRPdfExporter();
			//net.sf.jasperreports.engine.export.JRPdfExporter exporter = new  net.sf.jasperreports.engine.export.JRPdfExporter();

			exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintLst));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(bos));
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();

//			if (!Fnc.isEmpty(this.printerName)) {
//				String js = "var pp = this.getPrintParams(); \n";
//				if (!Fnc.isEmpty(this.printerName)) { // ระบุ printer
//					js += "pp.printerName = \"" + printerName + "\"; \n";
//					js += "pp.pageHandling = pp.constants.handling.none; \n";
//					js += "var fv = pp.constants.flagValues; \n";
//					js += "pp.flags |= fv.setPageSize; \n";
//				} else {
//					js += "pp.pageHandling = pp.constants.handling.fit; \n"; // เลือกแบบ  Fit อัตโนมัติป้องกัน Report ขอบหาย
//				}
//				js += "this.print(pp);";
//
//				configuration.setPdfJavaScript(js);
//			}

			exporter.setConfiguration(configuration);
			exporter.exportReport();

			AMedia am = new AMedia("report", "pdf", "application/pdf", bos.toByteArray());
			iframeReport.setContent(am);

		} catch (Exception ex) {
			Msg.error("ไม่สามารถแสดง report ได้ : " + ex.getMessage());
		}

	}

	private void viewReportFromStream() {

		try {

			iframeReport = (Iframe) getFellow("iframeReport");
			AMedia am = new AMedia("report", "pdf", "application/pdf", this.inputstream_pdf);
			iframeReport.setContent(am);

		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error("ไม่สามารถแสดง report ได้ :" + ex.getMessage());
		}

	}

	public void setJasperPrints(List<JasperPrint> jasperPrintLst) {
		this.jasperPrintLst = jasperPrintLst;
	}

	public InputStream getInputStream_pdf() {
		return inputstream_pdf;
	}

	public void setInputStream_pdf(InputStream streamPdf) {
		this.inputstream_pdf = streamPdf;
	}

}

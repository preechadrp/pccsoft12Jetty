package com.pcc.gl;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;


import org.junit.Test;

import com.googlecode.jthaipdf.jasperreports.engine.export.ThaiJRPdfExporter;
import com.pcc.gl.progman.ManAcBala;
import com.pcc.gl.progman.ManAcProfit;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FJRBeanCollectionDataSource;
import com.pcc.sys.lib.FJasperPrintList;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.progman.UtilComm;

import net.sf.dynamicreports.jasper.builder.export.Exporters;
import net.sf.dynamicreports.jasper.builder.export.JasperXlsExporterBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

public class TestGl {

	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	@Test
	public void test_ManAcProfit() throws DRException {
		// TEST run console

		// === test varible
		int print_option = 1;
		java.sql.Date fromPostdate = FnDate.getDate(01, 03, 2017);
		java.sql.Date toPostdate = FnDate.getDate(01, 05, 2017);
		LoginBean _loginBean = new LoginBean();
		// == end test varible

		//FConfig.setLog4jForAppConsole();

		try (FDbc dbc = FDbc.connectByConsole();) {

			UtilComm.setLoginBeanForApp(dbc, _loginBean);

			Map reportParams = new HashMap();
			reportParams.put("reportName", "รายงานงบกำไรขาดทุน");
			String reportConditionString1 = "";
			if (fromPostdate != null && toPostdate != null) {
				reportConditionString1 += "จากวันที่ " + FnDate.displayDateString(fromPostdate) + " ถึง "
						+ FnDate.displayDateString(toPostdate);
			}
			reportParams.put("reportConditionString1", reportConditionString1);

			FJRBeanCollectionDataSource reportDataSource = ManAcProfit.createDataSource(dbc, _loginBean, fromPostdate, toPostdate,
					print_option);
			if (reportDataSource != null) {
				if (print_option == 0) {// show

					ManAcProfit.genReportPDF(reportDataSource, reportParams, _loginBean).show();

				} else if (print_option == 1) {// แบบ ThaiJRPdfExporter

					FJasperPrintList jasperPrintList = new FJasperPrintList();
					jasperPrintList.addJasperPrintList(
							ManAcProfit.genReportPDF(reportDataSource, reportParams, _loginBean).toJasperPrint());
					ThaiJRPdfExporter exporter = new ThaiJRPdfExporter();
					exporter.setExporterOutput(
							new SimpleOutputStreamExporterOutput(new FileOutputStream("c:/tmp/reportByThaiJRPdf.pdf")));
					exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList.getJasperPrintLst()));
					exporter.exportReport();

				} else if (print_option == 2) {// แบบ ThaiJRPdfExporter
					JasperXlsExporterBuilder xlsExporter = Exporters.xlsExporter("c:/tmp/report.xls")
							.setDetectCellType(true).setIgnorePageMargins(true).setWhitePageBackground(false)
							.setRemoveEmptySpaceBetweenColumns(true);
					ManAcProfit.genReportExcel(reportDataSource, reportParams, _loginBean).toXls(xlsExporter);
				}
			}

			logger.info("Finished");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void test_ManAcBala() throws DRException {
		// TEST run console

		// === test varible
		int print_option = 1;
		java.sql.Date toPostdate = FnDate.getDate(01, 05, 2017);
		LoginBean _loginBean = new LoginBean();
		// == end test varible

		//FConfig.setLog4jForAppConsole();

		try (FDbc dbc = FDbc.connectByConsole();) {

			UtilComm.setLoginBeanForApp(dbc, _loginBean);

			Map reportParams = new HashMap();
			reportParams.put("reportName", "รายงานงบดุล");
			String reportConditionString1 = "สิ้นสุดวันที่ " + FnDate.displayDateString(toPostdate);
			reportParams.put("reportConditionString1", reportConditionString1);

			FJRBeanCollectionDataSource reportDataSource = ManAcBala.createDataSource(dbc, _loginBean, toPostdate, print_option);
			if (reportDataSource != null) {
				if (print_option == 0) {// show

					ManAcBala.genReportPDF(reportDataSource, reportParams, _loginBean).show();

				} else if (print_option == 1) {// แบบ ThaiJRPdfExporter

					FJasperPrintList jasperPrintList = new FJasperPrintList();
					jasperPrintList.addJasperPrintList(
							ManAcBala.genReportPDF(reportDataSource, reportParams, _loginBean).toJasperPrint());
					ThaiJRPdfExporter exporter = new ThaiJRPdfExporter();
					exporter.setExporterOutput(
							new SimpleOutputStreamExporterOutput(new FileOutputStream("c:/tmp/reportByThaiJRPdf.pdf")));
					exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList.getJasperPrintLst()));
					exporter.exportReport();

				} else if (print_option == 2) {// แบบ ThaiJRPdfExporter
					JasperXlsExporterBuilder xlsExporter = Exporters.xlsExporter("c:/tmp/report.xls")
							.setDetectCellType(true).setIgnorePageMargins(true).setWhitePageBackground(false)
							.setRemoveEmptySpaceBetweenColumns(true);
					ManAcBala.genReportExcel(reportDataSource, reportParams, _loginBean).toXls(xlsExporter);
				}
			}

			logger.info("Finished");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void test_ManAcTbal() {
	}

}

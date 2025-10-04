package com.pcc.sys.progman;

import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Calendar;

import com.googlecode.jthaipdf.jasperreports.engine.export.ThaiJRPdfExporter;
import com.pcc.sys.lib.DynamicRepTemplates;
import com.pcc.sys.lib.FJasperPrintList;
import com.pcc.sys.lib.FReport;
import com.pcc.sys.lib.FnDate;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.Exporters;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperXlsExporterBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.expression.AbstractComplexExpression;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

public class ManFrmUsergroup {

	public static void printData(String user_menu_group) throws Exception {
		FJasperPrintList jasperPrintList = new FJasperPrintList();
		jasperPrintList.addJasperPrintList(getReportExample(createDataSource()).toJasperPrint());
		FReport.viewPdf(jasperPrintList.getJasperPrintLst());
	}

	public static JasperReportBuilder getReportExample(JRDataSource dataSrc) {

		StyleBuilder styleDefault = DynamicRepTemplates.getRootStyle();//สำหรับเริ่มต้นค่าใน report นี้

		//ตัวอย่าง Dynamic report
		JasperReportBuilder myreport = report()
				.setTemplate(DynamicRepTemplates.reportTemplate)
				.title(DynamicRepTemplates.createTitleComponent("ทดสอบรายงาน"))
				.pageFooter(DynamicRepTemplates.footerComponent)
				.setDataSource(dataSrc);

		//== start เพิ่มฟิลด์  (ไม่ควรซ้ำกับที่อยู่ใน addColumn )
		myreport.addField("orderdate", type.dateType());
		//== end เพิ่มฟิลด์

		//== start เพิ่มคอลัมภ์
		TextColumnBuilder<String> itemColumn = col.column("Item", "item", type.stringType()).setStyle(styleDefault);
		myreport.addColumn(itemColumn);

		myreport.addColumn(col.column("Order date Thai", new AbstractComplexExpression<String>() { //แบบไม่ต้องสร้างเป็น Class ข้างนอก  OK
			private static final long serialVersionUID = 4417722888733075713L;

			@Override
			public String evaluate(java.util.List<?> values, ReportParameters reportParameters) {
				java.util.Date orderdate = (java.util.Date) reportParameters.getFieldValue("orderdate");
				return FnDate.displayDateString(orderdate);
			}
		}));

		TextColumnBuilder<Integer> quantityColumn = col.column("Quantity", "quantity", type.integerType()).setStyle(styleDefault);
		myreport.addColumn(quantityColumn);

		TextColumnBuilder<BigDecimal> unitPriceColumn = col.column("Unit price", "unitprice", type.bigDecimalType());
		unitPriceColumn.setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(5)).setWidth(40); //setWidth(40); หมายถึง 40%
		unitPriceColumn.setPrintWhenExpression(new AbstractSimpleExpression() {
			private static final long serialVersionUID = 4417722888733075712L;

			public Boolean evaluate(ReportParameters reportParameters) {
				//BigDecimal mm = (BigDecimal) reportParameters.getFieldValue("unitprice");  //ตัวนี้ก็ได้
				BigDecimal mm = (BigDecimal) reportParameters.getValue("unitprice");  //ตัวนี้ก็ได้
				if (mm.compareTo(new BigDecimal(30)) > 0) {
					return false;
				} else {
					return true;
				}
			}
		});
		unitPriceColumn.setPattern("#,##0.000");
		myreport.addColumn(unitPriceColumn);
		//== end เพิ่มคอลัมภ์

		return myreport;

	}

	private static JRDataSource createDataSource() {
		DRDataSource dataSource = new DRDataSource("item", "orderdate", "quantity", "unitprice");
		dataSource.add("ทดสอบ", toDate(2010, 1, 1), 5, new BigDecimal(30));
		dataSource.add("ปากกาแดง", toDate(2010, 1, 3), 1, new BigDecimal(28));
		dataSource.add("กระดาษแข็ง 2.1", toDate(2010, 1, 19), 5, new BigDecimal(32));
		dataSource.add("น่า", toDate(2010, 1, 5), 3, new BigDecimal(11));
		dataSource.add("รู้นั้น", toDate(2010, 1, 11), 1, new BigDecimal(15));
		dataSource.add("Book", toDate(2010, 1, 15), 5, new BigDecimal(10));
		dataSource.add("Book", toDate(2010, 1, 20), 8, new BigDecimal(9));
		return dataSource;
	}

	private static java.util.Date toDate(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.DAY_OF_MONTH, day);
		return c.getTime();
	}

	public static void main(String[] arg) {
		try {
			//ตัวอย่าง=ทดสอบแบบต่างๆ 
			getReportExample(createDataSource()).show();//== preview windows

			//==to pdf
			//แบบที่ 1 แบบ Dynamic Report
			JasperPdfExporterBuilder pdfExporter = Exporters.pdfExporter("c:/tmp/reportJasperPdf.pdf");
			getReportExample(createDataSource()).toPdf(pdfExporter);
			//แบบที่ 2 แบบ ThaiJRPdfExporter
			FJasperPrintList jasperPrintList = new FJasperPrintList();
			jasperPrintList.addJasperPrintList(getReportExample(createDataSource()).toJasperPrint());
			ThaiJRPdfExporter exporter = new ThaiJRPdfExporter();
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(new FileOutputStream("c:/tmp/reportByThaiJRPdf.pdf")));
			exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList.getJasperPrintLst()));
			exporter.exportReport();

			//==to excel
			JasperXlsExporterBuilder xlsExporter = Exporters.xlsExporter("c:/tmp/report.xls")
					.setDetectCellType(true)
					.setIgnorePageMargins(true)
					.setWhitePageBackground(false)
					.setRemoveEmptySpaceBetweenColumns(true);
			getReportExample(createDataSource()).toXls(xlsExporter);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

package com.pcc.sys.lib;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.DRField;
import net.sf.dynamicreports.report.base.DRVariable;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.VariableBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.group.GroupBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.Evaluation;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperPrint;

public class MyDynamicReport {

	protected StyleBuilder styleDefault = DynamicRepTemplates.getRootStyle();//สำหรับเริ่มต้นค่าใน report นี้
	protected HorizontalListBuilder cmpHead = DynamicReports.cmp.horizontalList();
	protected HorizontalListBuilder summaryObject = DynamicReports.cmp.horizontalList();

	private JasperReportBuilder myreport = DynamicReports.report();
	private int widthForSummaryObj = 0;
	public int widthForPage = 0;
	private boolean addSummaryObj = false;
	private int exportTo = 1;

	public JRDataSource dataSrc = null;
	public Map<String, Object> reportParams = null;

	/**
	 * 
	 * @param dataSrc
	 * @param exportTo 1=pdf ,2=excel
	 * @param pagetType
	 * @param orientation
	 * @param reportParams
	 */
	public MyDynamicReport(JRDataSource dataSrc, int exportTo,
			PageType pagetType, PageOrientation orientation,
			Map<String, Object> reportParams) {

		this.dataSrc = dataSrc;
		this.reportParams = reportParams;
		this.exportTo = exportTo;

		myreport
				.setTemplate(DynamicRepTemplates.reportTemplateWithHighlight)
				.summaryWithPageHeaderAndFooter()
				.setPageFormat(pagetType, orientation)
				.setDataSource(this.dataSrc);

		if (exportTo == 1) {//แก้ปํญหากรณี pdf แสดงสีแถวแบบ highlight ทำให้ไม่ทับเส้นขอบล่างของ Title Column Header
			myreport.columnHeader(DynamicReports.cmp.verticalGap(2));
		}

	}

	public void addField(String fieldName, Object dataType) {
		myreport.addField(fieldName, dataType.getClass());
	}

	public void addFieldString(String fieldName) {
		myreport.addField(fieldName, DynamicReports.type.stringType());
	}

	public void addFieldBigdecimal(String fieldName) {
		myreport.addField(fieldName, DynamicReports.type.bigDecimalType());
	}

	/**
	 * header หลักๆ
	 */
	public void setHeader1() {

		cmpHead
				.add(DynamicReports.cmp
						.text(Fnc.getStr(reportParams.get("P_COMP_NAME")))
						.setStyle(DynamicReports.stl.style(styleDefault).setFontSize(11)));

		if (exportTo == 1) {// สำหรับ PDF เท่านั้น

			cmpHead
					.add(DynamicReports.cmp
							.text("หน้าที่")
							.setFixedWidth(30)
							.setStyle(DynamicReports.stl.style(styleDefault).setFontSize(11))
							.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT))
					.add(DynamicReports.cmp
							.pageXslashY()
							.setPageXFixedWidth(13)
							.setPageYFixedWidth(13)
							.setFixedWidth(30)
							.setStyle(DynamicReports.stl.style(styleDefault).setFontSize(11))
							.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT));
		}

		cmpHead
				.newRow()
				.add(DynamicReports.cmp
						.text(Fnc.getStr(reportParams.get("P_REPPORT_NAME")))
						.setStyle(DynamicReports.stl.style(styleDefault).setFontSize(13)))
				.newRow()
				.add(DynamicReports.cmp
						.text(Fnc.getStr(reportParams.get("P_REPPORT_DESC")))
						.setStyle(DynamicReports.stl.style(styleDefault).setFontSize(13)));

		if (exportTo == 1) {

			cmpHead
					.newRow()
					.add(DynamicReports.cmp.verticalGap(2));//เพิ่มช่องไฟ

			myreport.pageHeader(cmpHead);

		} else {
			myreport.title(cmpHead);
		}

	}

	public void addColumnSeq(String title, int width) {

		myreport.addColumn(DynamicReports.col.column(title,
				new AbstractSimpleExpression<Integer>() {

					private static final long serialVersionUID = 1L;

					@Override
					public Integer evaluate(ReportParameters reportParameters) {
						return reportParameters.getReportRowNumber();
					}
				})
				.setStyle(styleDefault)
				.setFixedWidth(width)//setFixedWidth(40)  มีหน่วยเป็น pixcel , setWidth(40) มีหน่วยเป็น %
				.setPattern("#,##0.")
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));

		widthForSummaryObj += width;
		widthForPage += width;

	}

	public void addColumnShowBigdecimalVarible(String title, String varName, int width, String pattern) {

		myreport.addColumn(DynamicReports.col.column(title,
				new AbstractSimpleExpression<BigDecimal>() {

					private static final long serialVersionUID = 1L;

					@Override
					public BigDecimal evaluate(ReportParameters reportParameters) {
						return reportParameters.getVariableValue(varName);
					}
				})
				.setStyle(styleDefault)
				.setFixedWidth(width)//setFixedWidth(40)  มีหน่วยเป็น pixcel , setWidth(40) มีหน่วยเป็น %
				.setPattern(pattern)
				.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT));

		widthForSummaryObj += width;
		widthForPage += width;

	}

	/**
	 * เพิ่มคอลัมภ์ String  ชิดซ้ายปกติ
	 * @param label
	 * @param fieldName
	 * @param width
	 */
	public void addColumnStringLeft(String label, final String fieldName, int width) {

		myreport.addColumn(
				DynamicReports.col.column(label, fieldName, DynamicReports.type.stringType())
						.setStyle(styleDefault)
						.setFixedWidth(width)
		//		
		);

		widthForSummaryObj += width;
		widthForPage += width;

	}

	/**
	 * เพิ่มคอลัมภ์ String  ชิดซ้ายกลาง
	 * @param label
	 * @param fieldName
	 * @param width
	 */
	public void addColumnStringCenter(String label, final String fieldName, int width) {

		myreport.addColumn(
				DynamicReports.col.column(label, fieldName, DynamicReports.type.stringType())
						.setStyle(styleDefault)
						.setFixedWidth(width)
						.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
		//		
		);

		widthForSummaryObj += width;
		widthForPage += width;

	}

	/**
	 * เพิ่มคอลัมภ์ String  ชิดซ้ายขวา
	 * @param label
	 * @param fieldName
	 * @param width
	 */
	public void addColumnStringRight(String label, final String fieldName, int width) {

		myreport.addColumn(
				DynamicReports.col.column(label, fieldName, DynamicReports.type.stringType())
						.setStyle(styleDefault)
						.setFixedWidth(width)
						.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
		//		
		);

		widthForSummaryObj += width;
		widthForPage += width;

	}

	/**
	 * เพิ่มคอลัมภ์ bigdecimal format #,##0.00
	 * @param label
	 * @param fieldName
	 * @param width
	 */
	public void addColumnBigDecimal1(String label, final String fieldName, int width) {

		myreport.addColumn(
				DynamicReports.col.column(label, fieldName, DynamicReports.type.bigDecimalType())
						.setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(2))
						.setFixedWidth(width)
						.setPattern("#,##0.00")
						.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
		//		
		);

		widthForSummaryObj += width;
		widthForPage += width;

	}

	/**
	 * เพิ่มคอลัมภ์ bigdecimal
	 * @param label
	 * @param fieldName
	 * @param width
	 * @param pattern คือ format เช่น #,###.00 เป็นต้น
	 */
	public void addColumnBigDecimal2(String label, final String fieldName, int width, String pattern) {

		myreport.addColumn(
				DynamicReports.col.column(label, fieldName, DynamicReports.type.bigDecimalType())
						.setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(2))
						.setFixedWidth(width)
						.setPattern(pattern)
						.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
		//
		);

		widthForSummaryObj += width;
		widthForPage += width;

	}

	/**
	 * เพิ่มคอลัมภ์ integer format #,##0
	 * @param label
	 * @param fieldName
	 * @param width
	 */
	public void addColumnInteger1(String label, final String fieldName, int width) {

		myreport.addColumn(
				DynamicReports.col.column(label, fieldName, DynamicReports.type.integerType())
						.setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(2))
						.setFixedWidth(width)
						.setPattern("#,##0")
						.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
		//		
		);

		widthForSummaryObj += width;
		widthForPage += width;

	}

	/**
	 * เพิ่มคอลัมภ์ integer
	 * @param label
	 * @param fieldName
	 * @param width
	 * @param pattern เช่น #,##0
	 */
	public void addColumnInteger2(String label, final String fieldName, int width, String pattern) {

		myreport.addColumn(
				DynamicReports.col.column(label, fieldName, DynamicReports.type.integerType())
						.setStyle(DynamicReports.stl.style(styleDefault).setRightPadding(2))
						.setFixedWidth(width)
						.setPattern(pattern)
						.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
		//
		);

		widthForSummaryObj += width;
		widthForPage += width;

	}

	public void showLabelToSumBand(String value) {

		if (this.addSummaryObj == false) {
			this.addSummaryObj = true;
		}

		TextFieldBuilder<String> obj = DynamicReports.cmp
				.text(value)
				.setFixedWidth(this.widthForSummaryObj)
				.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
				.setStyle(DynamicReports.stl.style(styleDefault));

		summaryObject.add(obj);
		this.widthForSummaryObj = 0;//reset

	}

	public void showVaribleToSumBand(final String varName) {

		if (this.addSummaryObj == false) {
			this.addSummaryObj = true;
		}

		TextFieldBuilder<?> obj = null;
		DRVariable<?> objVar = this.getReportVariable(varName);

		System.out.println("objVar.getValueClass().getName() :" + objVar.getValueClass().getName());

		if (objVar.getValueClass().getName().equals("java.math.BigDecimal")) {

			obj = DynamicReports.cmp
					.text(objVar)
					.setFixedWidth(this.widthForSummaryObj)
					.setPattern("#,##0.00")
					.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
					.setStyle(DynamicReports.stl.style(styleDefault));

		} else if (objVar.getValueClass().getName().equals("java.lang.Integer")) {

			obj = DynamicReports.cmp
					.text(objVar)
					.setFixedWidth(this.widthForSummaryObj)
					.setPattern("#,##0")
					.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
					.setStyle(DynamicReports.stl.style(styleDefault));

		} else if (objVar.getValueClass().getName().equals("java.lang.String")) {

			obj = DynamicReports.cmp
					.text(objVar)
					.setFixedWidth(this.widthForSummaryObj)
					.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)
					.setStyle(DynamicReports.stl.style(styleDefault));

		}

		summaryObject.add(obj);
		this.widthForSummaryObj = 0;//reset

	}

	/**
	 * สร้างตัวแปร summary ไม่มีการ reset
	 * @param varName
	 * @param fieldName
	 */
	public void createVaribleSummary1(String varName, String fieldName) {

		DRField<?> field1 = this.getReportField(fieldName);

		if (field1.getValueClass().getName().equals("java.math.BigDecimal")) {

			VariableBuilder<BigDecimal> var1 = DynamicReports.variable(varName,
					new AbstractSimpleExpression<BigDecimal>() {

						private static final long serialVersionUID = 1L;

						@Override
						public BigDecimal evaluate(ReportParameters reportParameters) {
							return reportParameters.getFieldValue(fieldName);
						}
					}, Calculation.SUM);

			var1.setResetType(Evaluation.REPORT);
			myreport.addVariable(var1);

		} else if (field1.getValueClass().getName().equals("java.lang.Integer")) {

			VariableBuilder<Integer> var1 = DynamicReports.variable(varName,
					new AbstractSimpleExpression<Integer>() {

						private static final long serialVersionUID = 1L;

						@Override
						public Integer evaluate(ReportParameters reportParameters) {
							return reportParameters.getFieldValue(fieldName);
						}
					}, Calculation.SUM);

			var1.setResetType(Evaluation.REPORT);
			myreport.addVariable(var1);

		} else {//java.lang.Long

			VariableBuilder<java.lang.Long> var1 = DynamicReports.variable(varName,
					new AbstractSimpleExpression<java.lang.Long>() {

						private static final long serialVersionUID = 1L;

						@Override
						public java.lang.Long evaluate(ReportParameters reportParameters) {
							return reportParameters.getFieldValue(fieldName);
						}
					}, Calculation.SUM);

			var1.setResetType(Evaluation.REPORT);
			myreport.addVariable(var1);

		}

	}

	/**
	 * สร้างตัวแปร summary มีการ reset group
	 * @param varName
	 * @param fieldName
	 * @param groupBuilder groupBuilder for reset
	 */
	public void createVaribleSummary2(String varName, String fieldName, GroupBuilder<?> groupBuilder) {

		DRField<?> field1 = this.getReportField(fieldName);

		if (field1.getValueClass().getName().equals("java.math.BigDecimal")) {

			VariableBuilder<BigDecimal> var1 = DynamicReports.variable(varName,
					new AbstractSimpleExpression<BigDecimal>() {

						private static final long serialVersionUID = 1L;

						@Override
						public BigDecimal evaluate(ReportParameters reportParameters) {
							return reportParameters.getFieldValue(fieldName);
						}
					}, Calculation.SUM);

			if (groupBuilder != null) {
				var1.setResetType(Evaluation.GROUP);
				var1.setResetGroup(groupBuilder);
			} else {
				var1.setResetType(Evaluation.REPORT);
			}
			myreport.addVariable(var1);

		} else if (field1.getValueClass().getName().equals("java.lang.Integer")) {

			VariableBuilder<Integer> var1 = DynamicReports.variable(varName,
					new AbstractSimpleExpression<Integer>() {

						private static final long serialVersionUID = 1L;

						@Override
						public Integer evaluate(ReportParameters reportParameters) {
							return reportParameters.getFieldValue(fieldName);
						}
					}, Calculation.SUM);

			if (groupBuilder != null) {
				var1.setResetType(Evaluation.GROUP);
				var1.setResetGroup(groupBuilder);
			} else {
				var1.setResetType(Evaluation.REPORT);
			}
			myreport.addVariable(var1);

		} else {//java.lang.Long

			VariableBuilder<java.lang.Long> var1 = DynamicReports.variable(varName,
					new AbstractSimpleExpression<java.lang.Long>() {

						private static final long serialVersionUID = 1L;

						@Override
						public java.lang.Long evaluate(ReportParameters reportParameters) {
							return reportParameters.getFieldValue(fieldName);
						}
					}, Calculation.SUM);

			if (groupBuilder != null) {
				var1.setResetType(Evaluation.GROUP);
				var1.setResetGroup(groupBuilder);
			} else {
				var1.setResetType(Evaluation.REPORT);
			}
			myreport.addVariable(var1);

		}

	}

	public DRVariable<?> getReportVariable(String name) {

		List<DRVariable<?>> lst_drVariable = myreport.getReport().getVariables();
		for (DRVariable<?> drVariable : lst_drVariable) {
			if (drVariable.getName().equals(name)) {
				return drVariable;
			}
		}
		return null;

	}

	public DRField<?> getReportField(String name) {

		List<DRField<?>> lst_drField = myreport.getReport().getFields();
		for (DRField<?> drField : lst_drField) {
			if (drField.getName().equals(name)) {
				return drField;
			}
		}
		return null;

	}

	public JasperPrint buildReport() throws DRException {

		if (addSummaryObj == true) {
			myreport.summary(this.summaryObject);
		}

		return myreport.toJasperPrint();

	}

}

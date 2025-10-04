package com.pcc.sys.lib;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

public class FJasperPrintList {

	private List<JasperPrint> jasperPrintLst = new ArrayList<JasperPrint>();

	public List<JasperPrint> getJasperPrintLst() {
		return jasperPrintLst;
	}

	public void setJasperPrintLst(List<JasperPrint> jasperPrintLst) {
		this.jasperPrintLst = jasperPrintLst;
	}

	public void addJasperPrintList(JasperPrint jasperPrint) {
		if (jasperPrint != null) {
			jasperPrintLst.add(jasperPrint);
		}
	}
	
	public void add(JasperPrint jasperPrint) {
		if (jasperPrint != null) {
			jasperPrintLst.add(jasperPrint);
		}
	}
	
	/**
	 * เพิ่มเข้าแบบระบุว่าจะแทรกเป็นลำดับที่เท่าไหร่
	 * @param jasperPrint
	 * @param idx เริ่มต้นจาก 0 และ < size ถ้าระบุ < 0 ระบบจะเอาไปต่อท้ายปกติ
	 */
	public void add(JasperPrint jasperPrint, int idx) {
		if (jasperPrint != null) {
			if (idx >= 0) {
				if (jasperPrintLst.size() > 0) {
					jasperPrintLst.add(idx, jasperPrint);
				} else {
					jasperPrintLst.add(jasperPrint);
				}
			} else {
				jasperPrintLst.add(jasperPrint);
			}
		}
	}

	public void clearJasperPrintList() {
		jasperPrintLst.clear();
	}
	
	public void exportToPdf() throws Exception {
		if (jasperPrintLst.size() > 0) {
			FReport.viewPdf(jasperPrintLst);
		}
	}

	public void exportToExcel(String fileName) throws Exception {
		if (jasperPrintLst.size() > 0) {
			FReport.exportExcel(this, fileName);
		}
	}

	/**
	 * แปลงเป็น pdf และข้อความแบบBase64
	 * @return [ข้อความ base64 ของรายงาน , pdf]
	 * @throws JRException
	 */
	public String[] exportBase64_of_PDF() throws JRException, Exception {
		if (jasperPrintLst.size() > 0) {
			return FReport.exportBase64_of_PDF(jasperPrintLst);
		} else {
			return null;
		}
	}

	/**
	 * แบบที่ละ jasperPrint List สำหรับ Excel 2007  ออกเป็น base64
	 * @return [ข้อความ base64 ของรายงาน , xlsx]
	 * @throws Exception
	 */
	public String[] exportBase64_of_EXCEL() throws Exception {
		if (jasperPrintLst.size() > 0) {
			return FReport.exportBase64_of_EXCEL(jasperPrintLst);
		} else {
			return null;
		}
	}

}

package com.pcc.gl.ui.acBala;

import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;

import com.pcc.gl.progman.ManAcBala;
import com.pcc.sys.lib.FJasperPrintList;
import com.pcc.sys.lib.FReport;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Msg;

public class AcBala extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Datebox tdbToPostdate;

	public Button btnExit, btnPrint, btnSave, btnDelete, btnAdd;

	@Override
	public void setFormObj() {
		tdbToPostdate = (Datebox) getFellow("tdbToPostdate");

		btnExit = (Button) getFellow("btnExit");
		btnPrint = (Button) getFellow("btnPrint");
	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(tdbToPostdate);
		addEnterIndex(btnPrint);

	}

	@Override
	public void formInit() {
		try {
			clearData();
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	private void clearData() {
		tdbToPostdate.focus();
	}

	public void onOK() {
		try {
			super.onOK();
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	private void validateData() throws Exception {

		if (tdbToPostdate.getValue() == null) {
			throw new Exception("ระบุช่อง \"" + tdbToPostdate.getTooltiptext() + "\" ");
		}

	}

	public void onClick_btnPrint(int print_option) {

		try {

			validateData();
			FJasperPrintList fJasperPrintList = new FJasperPrintList();
			ManAcBala.getReport(this.getLoginBean(), FnDate.getSqlDate(tdbToPostdate.getValue()), fJasperPrintList,
					print_option);
			if (fJasperPrintList.getJasperPrintLst().size() > 0) {
				if (print_option == 1) {
					FReport.viewPdf(fJasperPrintList.getJasperPrintLst());
				} else {
					FReport.exportExcel(fJasperPrintList, "AcBala");
				}
			} else {
				throw new Exception("ไม่พบข้อมูล");
			}

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void onClick_Test() {
		// trick ทดสอบจากไฟล์ .jrxml โดยตรง //TEST OK by preecha
		try {

			// validateData();
			FJasperPrintList fJasperPrintList = new FJasperPrintList();
			ManAcBala.getTestReport(this.getLoginBean(), FnDate.getSqlDate(tdbToPostdate.getValue()), fJasperPrintList,
					1);
			if (fJasperPrintList.getJasperPrintLst().size() > 0) {
				// if (print_option == 1) {
				FReport.viewPdf(fJasperPrintList.getJasperPrintLst());
				// } else {
				// FReport.exportExcel(fJasperPrintList, "AcBala");
				// }
			} else {
				throw new Exception("ไม่พบข้อมูล");
			}

		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void onClick_TestJpg() {
		// trick ทดสอบ export เป็น jpg //TEST OK by preecha
		try {
			int print_option = 1;
			validateData();
			FJasperPrintList fJasperPrintList = new FJasperPrintList();
			ManAcBala.getReport(this.getLoginBean(), FnDate.getSqlDate(tdbToPostdate.getValue()), fJasperPrintList,
					print_option);
			if (fJasperPrintList.getJasperPrintLst().size() > 0) {
				FReport.viewToJpg(fJasperPrintList.getJasperPrintLst(), 0l);
			} else {
				throw new Exception("ไม่พบข้อมูล");
			}

		} catch (Exception e) {
			Msg.error(e);
		}
	}

}

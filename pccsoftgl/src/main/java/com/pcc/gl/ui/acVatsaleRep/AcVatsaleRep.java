package com.pcc.gl.ui.acVatsaleRep;

import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;

import com.pcc.gl.progman.ManAcVatsaleRep;
import com.pcc.sys.lib.FJasperPrintList;
import com.pcc.sys.lib.FReport;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Msg;

public class AcVatsaleRep extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Datebox tdbFromPostdate, tdbToPostdate;

	public Button btnExit, btnPrint;

	@Override
	public void setFormObj() {
		tdbFromPostdate = (Datebox) getFellow("tdbFromPostdate");
		tdbToPostdate = (Datebox) getFellow("tdbToPostdate");

		btnExit = (Button) getFellow("btnExit");
		btnPrint = (Button) getFellow("btnPrint");
	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(tdbFromPostdate);
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
		tdbFromPostdate.focus();
	}

	public void onOK() {
		try {
			super.onOK();
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	private void validateData() throws Exception {

		if (tdbFromPostdate.getValue() == null) {
			throw new Exception("ระบุช่อง \"" + tdbFromPostdate.getTooltiptext() + "\" ");
		}

		if (tdbToPostdate.getValue() == null) {
			throw new Exception("ระบุช่อง \"" + tdbToPostdate.getTooltiptext() + "\" ");
		}

		if (tdbFromPostdate.getValue().after(tdbToPostdate.getValue())) {
			throw new Exception("ระบุช่อง \"" + tdbToPostdate.getTooltiptext() + "\" ห้ามน้อยกว่าช่อง \""
					+ tdbFromPostdate.getTooltiptext() + "\" ");
		}

		int diff = FnDate.subDate(tdbFromPostdate.getValue(), tdbToPostdate.getValue());
		if (diff > 366) {
			throw new Exception("ช่วงวันที่ จาก..ถึง.. ห้ามเกิน 366 วัน เพราะจะกระทบกับประสิทธิภาพการทำงานของระบบ ");
		}

	}

	public void onClick_btnPrint(int print_option) {

		try {

			validateData();
			FJasperPrintList fJasperPrintList = new FJasperPrintList();
			ManAcVatsaleRep.getReport(this.getLoginBean(), FnDate.getSqlDate(tdbFromPostdate.getValue()),
					FnDate.getSqlDate(tdbToPostdate.getValue()), fJasperPrintList, print_option);
			if (fJasperPrintList.getJasperPrintLst().size() > 0) {
				if (print_option == 1) {
					FReport.viewPdf(fJasperPrintList.getJasperPrintLst());
				} else {
					FReport.exportExcel(fJasperPrintList, "AcVatsaleRep");
				}
			} else {
				throw new Exception("ไม่พบข้อมูล");
			}

		} catch (Exception e) {
			Msg.error(e);
		}

	}

}

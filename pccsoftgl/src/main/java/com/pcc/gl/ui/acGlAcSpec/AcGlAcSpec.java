package com.pcc.gl.ui.acGlAcSpec;

import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Textbox;

import com.pcc.gl.find.FfACCT_NO;
import com.pcc.gl.progman.ManAcGlAcSpec;
import com.pcc.gl.tbf.TbfACCT_NO;
import com.pcc.gl.tbo.TboACCT_NO;
import com.pcc.sys.lib.FJasperPrintList;
import com.pcc.sys.lib.FReport;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Msg;

public class AcGlAcSpec extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Textbox txtFromAcctid;
	public Textbox txtFromAcctName;
	public Textbox txtToAcctid;
	public Textbox txtToAcctName;
	public Datebox tdbFromPostdate, tdbToPostdate;

	public Button btnExit, btnPrint, btnSave, btnDelete, btnAdd;

	@Override
	public void setFormObj() {
		txtFromAcctid = (Textbox) getFellow("txtFromAcctid");
		txtFromAcctName = (Textbox) getFellow("txtFromAcctName");
		txtToAcctid = (Textbox) getFellow("txtToAcctid");
		txtToAcctName = (Textbox) getFellow("txtToAcctName");
		tdbFromPostdate = (Datebox) getFellow("tdbFromPostdate");
		tdbToPostdate = (Datebox) getFellow("tdbToPostdate");

		btnExit = (Button) getFellow("btnExit");
		btnPrint = (Button) getFellow("btnPrint");
	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(txtFromAcctid);
		addEnterIndex(txtToAcctid);
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
		txtFromAcctid.focus();
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

	}

	public void onClick_btnPrint(int print_option) {

		try {

			validateData();
			FJasperPrintList fJasperPrintList = new FJasperPrintList();
			ManAcGlAcSpec.getReport(this.getLoginBean(), txtFromAcctid.getValue(), txtToAcctid.getValue(),
					FnDate.getSqlDate(tdbFromPostdate.getValue()), FnDate.getSqlDate(tdbToPostdate.getValue()),
					fJasperPrintList, print_option);
			if (fJasperPrintList.getJasperPrintLst().size() > 0) {
				if (print_option == 1) {
					FReport.viewPdf(fJasperPrintList.getJasperPrintLst());
				} else {
					FReport.exportExcel(fJasperPrintList, "AcGlbook");
				}
			} else {
				throw new Exception("ไม่พบข้อมูล");
			}

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void popupFromAcctid() {
		FfACCT_NO.popup("", this.getLoginBean(), this, "doPopupFromAcctid");
	}

	public void doPopupFromAcctid(String acctid) {
		try {
			read_acctid_from(acctid);
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void popupToAcctid() {
		FfACCT_NO.popup("", this.getLoginBean(), this, "doPopupToAcctid");
	}

	public void doPopupToAcctid(String acctid) {
		try {
			read_acctid_to(acctid);
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void onChange_txtFromAcctid() {
		try {
			if (!read_acctid_from(txtFromAcctid.getValue())) {
				txtFromAcctid.setValue("");
				txtFromAcctName.setValue("");
			}
		} catch (Exception e) {
		}
	}

	public void onChange_txtToAcctid() {
		if (!read_acctid_to(txtToAcctid.getValue())) {
			txtToAcctid.setValue("");
			txtToAcctName.setValue("");
		}
	}

	private boolean read_acctid_from(String acct_id) {
		try {

			TboACCT_NO acctno = new TboACCT_NO();
			acctno.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			acctno.setACCT_ID(acct_id);
			if (TbfACCT_NO.find(acctno)) {
				txtFromAcctid.setValue(acctno.getACCT_ID());
				txtFromAcctName.setValue(acctno.getACCT_NAME());
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			return false;
		}
	}

	private boolean read_acctid_to(String acct_id) {
		try {

			TboACCT_NO acctno = new TboACCT_NO();
			acctno.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			acctno.setACCT_ID(acct_id);
			if (TbfACCT_NO.find(acctno)) {
				txtToAcctid.setValue(acctno.getACCT_ID());
				txtToAcctName.setValue(acctno.getACCT_NAME());
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			return false;
		}
	}

}

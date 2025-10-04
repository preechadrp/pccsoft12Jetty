package com.pcc.gl.ui.acApAging;


import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Textbox;

import com.pcc.gl.find.FfACCT_NO;
import com.pcc.gl.find.FfFCUS;
import com.pcc.gl.progman.ManAcApAging;
import com.pcc.gl.tbf.TbfACCT_NO;
import com.pcc.gl.tbm.TbmFCUS;
import com.pcc.gl.tbo.TboACCT_NO;
import com.pcc.sys.lib.FJasperPrintList;
import com.pcc.sys.lib.FReport;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Msg;

public class AcApAging extends FWinMenu {

	private static final long serialVersionUID = 1L;
	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public Datebox tdbToPostdate;
	public Textbox txtCUST_CDE, txtCUST_NAME;
	public Textbox txtFromAcctid;
	public Textbox txtFromAcctName;
	public Button btnExit, btnPrint, btnSave, btnDelete, btnAdd;

	@Override
	public void setFormObj() {
		tdbToPostdate = (Datebox) getFellow("tdbToPostdate");
		txtCUST_CDE = (Textbox) getFellow("txtCUST_CDE");
		txtCUST_NAME = (Textbox) getFellow("txtCUST_NAME");
		txtFromAcctid = (Textbox) getFellow("txtFromAcctid");
		txtFromAcctName = (Textbox) getFellow("txtFromAcctName");

		btnExit = (Button) getFellow("btnExit");
		btnPrint = (Button) getFellow("btnPrint");
	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(tdbToPostdate);
		addEnterIndex(txtCUST_CDE);
		addEnterIndex(txtFromAcctid);
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
		tdbToPostdate.setValue(FnDate.getTodaySqlDate());
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

			ManAcApAging.getReport(this.getLoginBean(), FnDate.getSqlDate(tdbToPostdate.getValue()),
					txtCUST_CDE.getValue(), txtFromAcctid.getValue(), fJasperPrintList, print_option);

			if (fJasperPrintList.getJasperPrintLst().size() > 0) {
				if (print_option == 1) {
					FReport.viewPdf(fJasperPrintList.getJasperPrintLst());
				} else {
					FReport.exportExcel(fJasperPrintList, "AcApRem");
				}
			} else {
				throw new Exception("ไม่พบข้อมูล");
			}

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void onChange_Data(Component comp) {
		try {
			if (comp.getId().equals("txtCUST_CDE")) {
				read_cust(txtCUST_CDE.getValue());
			}
			if (comp.getId().equals("txtFromAcctid")) {
				if (!read_acctid_from(txtFromAcctid.getValue())) {
					txtFromAcctid.setValue("");
					txtFromAcctName.setValue("");
				}
			}
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void popupCUST_CDE() {
		FfFCUS.popup(this.getLoginBean(), this, "doPopupCUST_CDE");
	}

	public void doPopupCUST_CDE(String cust_cde) {

		try {
			if (!read_cust(cust_cde)) {
				txtCUST_CDE.focus();
			}
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	private boolean read_cust(String cust_cde) {

		try {

			String[] cust_name = { "" };
			if (TbmFCUS.getCustName(cust_cde, cust_name, getLoginBean())) {
				txtCUST_CDE.setValue(cust_cde);
				txtCUST_NAME.setValue(cust_name[0]);
				return true;
			} else {
				txtCUST_CDE.setValue("");
				txtCUST_NAME.setValue("");
				return false;
			}

		} catch (Exception e) {
			logger.info(e.getMessage());
			return false;
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

}

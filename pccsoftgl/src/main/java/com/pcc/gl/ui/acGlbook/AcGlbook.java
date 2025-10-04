package com.pcc.gl.ui.acGlbook;

import org.json.JSONObject;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Textbox;

import com.pcc.api.core.ApiUtil;
import com.pcc.gl.find.FfACCT_VOU_TYPE;
import com.pcc.gl.tbf.TbfACCT_VOU_TYPE;
import com.pcc.gl.tbo.TboACCT_VOU_TYPE;
import com.pcc.sys.lib.FReport;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Msg;

public class AcGlbook extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Datebox tdbFromPostdate, tdbToPostdate;
	public Textbox txtVOU_TYPE, txtVOU_NAME;
	public Button btnExit, btnPrint;

	@Override
	public void setFormObj() {
		txtVOU_TYPE = (Textbox) getFellow("txtVOU_TYPE");
		txtVOU_NAME = (Textbox) getFellow("txtVOU_NAME");
		tdbFromPostdate = (Datebox) getFellow("tdbFromPostdate");
		tdbToPostdate = (Datebox) getFellow("tdbToPostdate");

		btnExit = (Button) getFellow("btnExit");
		btnPrint = (Button) getFellow("btnPrint");
	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(txtVOU_TYPE);
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
		txtVOU_TYPE.focus();
	}

	public void onOK() {
		try {
			super.onOK();
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	private void validateData(int print_option) throws Exception {

		if (tdbFromPostdate.getValue() == null) {
			throw new Exception("ระบุช่อง \"" + tdbFromPostdate.getTooltiptext() + "\" ");
		}
		if (tdbToPostdate.getValue() == null) {
			throw new Exception("ระบุช่อง \"" + tdbToPostdate.getTooltiptext() + "\" ");
		}
		
	}

	public void onClick_btnPrint(int print_option) {

		try {

			validateData(print_option);
			
			// call web service
			JSONObject jsonParameter = new JSONObject();

			jsonParameter.put("vou_type", txtVOU_TYPE.getValue());
			jsonParameter.put("fromPostdate", FnDate.getSqlDate(tdbFromPostdate.getValue()));
			jsonParameter.put("toPostdate", FnDate.getSqlDate(tdbToPostdate.getValue()));
			jsonParameter.put("print_option", print_option);

			String requestdesc = "จากวันที่ " + FnDate.displayDateString(tdbFromPostdate.getValue()) +
					" ถึงวันที่  " + FnDate.displayDateString(tdbToPostdate.getValue());

			JSONObject json = ApiUtil.callWebServiceVer2(
					jsonParameter,
					this.getLoginBean(),
					this.getMenuId2(),
					this.getTitle(),
					requestdesc,
					"ApiAcGlbook.getData1",
					"gl");

			if (!ApiUtil.isErrorService(json)) {
				//System.out.println(json.toString());
				FReport.saveFile(json.getString("base64"), json.getString("format"), this.getMenuId2());
			} else {
				throw new Exception(ApiUtil.getErrorService(json));
			}

		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}

	}
	
	public void popupVouType() {
		if (!txtVOU_TYPE.isReadonly()) {
			FfACCT_VOU_TYPE.popup("", this.getLoginBean(), this, "doPopupVouType");
		}
	}
	
	public void doPopupVouType(String vou_type) {
		try {
			read_vou_type(vou_type);
		} catch (Exception e) {
			Msg.error(e);
		}
	}
	
	public void onChange_txtVOU_TYPE() {
		try {
			if (!read_vou_type(txtVOU_TYPE.getValue())) {
				txtVOU_NAME.setValue("");
			}
		} catch (Exception e) {
		}
	}
	
	public boolean read_vou_type(String vou_type) throws Exception {
		boolean res = false;

		TboACCT_VOU_TYPE acctype = new TboACCT_VOU_TYPE();
		acctype.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		acctype.setVOU_TYPE(vou_type);
		if (TbfACCT_VOU_TYPE.find(acctype)) {
			txtVOU_TYPE.setValue(acctype.getVOU_TYPE());
			txtVOU_NAME.setValue(acctype.getVOU_NAME());
			res = true;
		}

		return res;
	}

}

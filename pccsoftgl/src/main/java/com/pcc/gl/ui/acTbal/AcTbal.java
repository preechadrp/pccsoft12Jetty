package com.pcc.gl.ui.acTbal;

import org.json.JSONObject;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;

import com.pcc.api.core.ApiUtil;
import com.pcc.sys.lib.FReport;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Msg;

public class AcTbal extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Datebox tdbFromPostdate, tdbToPostdate;

	public Button btnExit, btnPrint, btnSave, btnDelete, btnAdd;

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

	}

	public void onClick_btnPrint(int print_option) {

		try {

			validateData();
			
			// call web service
			JSONObject jsonParameter = new JSONObject();

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
					"ApiAcTbal.getData1",
					"gl");

			if (!ApiUtil.isErrorService(json)) {
				//System.out.println(json.toString());
				FReport.saveFile(json.getString("base64"), json.getString("format"), this.getMenuId2());
			} else {
				throw new Exception(ApiUtil.getErrorService(json));
			}

		} catch (Exception e) {
			Msg.error(e);
		}

	}

}

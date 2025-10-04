package com.pcc.bx.ui.FrmBxRepCostAndAp;

import org.json.JSONObject;
import org.zkoss.zul.Button;

import com.pcc.api.core.ApiUtil;
import com.pcc.sys.lib.FReport;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.MyDatebox;

public class FrmBxRepCostAndAp extends FWinMenu {

	private static final long serialVersionUID = 1L;
	public Button btnExit;
	//public Button btnPrint;
	public Button btnExcel;
	public MyDatebox tdbBLDATE_FROM;
	public MyDatebox tdbBLDATE_TO;

	@Override
	public void setFormObj() {
		btnExit = (Button) getFellow("btnExit");
		//btnPrint = (Button) getFellow("btnPrint");
		btnExcel = (Button) getFellow("btnExcel");
		tdbBLDATE_FROM = (MyDatebox) getFellow("tdbBLDATE_FROM");
		tdbBLDATE_TO = (MyDatebox) getFellow("tdbBLDATE_TO");
		
	}

	@Override
	public void addEnterIndex() {
		
		addEnterIndex(tdbBLDATE_FROM);
		addEnterIndex(tdbBLDATE_TO);
		//addEnterIndex(btnExit);
		//addEnterIndex(btnPrint);
		addEnterIndex(btnExcel);
		
	}

	@Override
	public void formInit() {
		tdbBLDATE_FROM.setValue(FnDate.addMonth(FnDate.getTodaySqlDate(), -1));
		tdbBLDATE_TO.setValue(FnDate.getTodaySqlDate());
	}
	
	public void onOK() {
		try {
			super.onOK();
		} catch (Exception e) {
			Msg.error(e);
		}
	}
	
	public void onClick_btnPrint(int print_option) {
		
		try {
			validateData();
			
			// call web service
			JSONObject jsonParameter = new JSONObject();

			jsonParameter.put("tdbBLDATE_FROM", tdbBLDATE_FROM.getSqlDate());
			jsonParameter.put("tdbBLDATE_TO", tdbBLDATE_TO.getSqlDate());
			jsonParameter.put("print_option", print_option);

			String requestdesc = "";
			requestdesc += tdbBLDATE_FROM.getTooltiptext() + " :" + FnDate.displayDateString(tdbBLDATE_FROM.getValue());
			requestdesc += ", " + tdbBLDATE_TO.getTooltiptext() + " :" + FnDate.displayDateString(tdbBLDATE_TO.getValue());

			jsonParameter.put("reportCondition", requestdesc);

			JSONObject json = ApiUtil.callWebServiceVer2(
					jsonParameter,
					this.getLoginBean(),
					this.getMenuId2(),
					this.getTitle(),
					requestdesc,
					"ApiFrmBxRepCostAndAp.getData1",
					"bx");

			if (!ApiUtil.isErrorService(json)) {
				//System.out.println(json.toString());
				FReport.saveFile(json.getString("base64"), json.getString("format"), this.getMenuId2());
			} else {
				throw new Exception(ApiUtil.getErrorService(json));
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}
		
	}

	private void validateData() throws Exception {

		if (tdbBLDATE_FROM.getSqlDate() == null || tdbBLDATE_TO.getSqlDate() == null) {
			throw new Exception("กรุณาใส่ช่วงวันที่");
		}
		
	}

}

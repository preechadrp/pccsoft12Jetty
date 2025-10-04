package com.pcc.gl.ui.acWithold;


import org.json.JSONObject;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Textbox;

import com.pcc.api.core.ApiUtil;
import com.pcc.sys.lib.FReport;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.MyDatebox;
import com.pcc.sys.lib.ZkUtil;
import com.pcc.sys.tbm.TbmFCOMPBRANC;
import com.pcc.sys.tbo.TboFCOMPBRANC;

public class AcWithold extends FWinMenu {

	private static final long serialVersionUID = 1L;
	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public Button btnExit;
	public Button btnPrint;
	public Button btnExcel;
	public Button btnText;
	public Intbox intYear;
	public Intbox intMonth;
	public Combobox cmbReportType;
	public MyDatebox tdbTaxDate;
	public Combobox cmbBRANC_CDE;
	public Textbox txtPaidByName;
	public Textbox txtPosition;

	@Override
	public void setFormObj() {
		btnExit = (Button) getFellow("btnExit");
		btnPrint = (Button) getFellow("btnPrint");
		btnExcel = (Button) getFellow("btnExcel");
		btnText = (Button) getFellow("btnText");
		intYear = (Intbox) getFellow("intYear");
		intMonth = (Intbox) getFellow("intMonth");
		cmbReportType = (Combobox) getFellow("cmbReportType");
		tdbTaxDate = (MyDatebox) getFellow("tdbTaxDate");
		cmbBRANC_CDE = (Combobox) getFellow("cmbBRANC_CDE");
		txtPaidByName = (Textbox) getFellow("txtPaidByName");
		txtPosition = (Textbox) getFellow("txtPosition");
	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(intYear);
		addEnterIndex(intMonth);
		addEnterIndex(cmbReportType);
		addEnterIndex(tdbTaxDate);
		addEnterIndex(cmbBRANC_CDE);
		addEnterIndex(txtPaidByName);
		addEnterIndex(txtPosition);
		addEnterIndex(btnPrint);
		addEnterIndex(btnExcel);
		addEnterIndex(btnText);
		addEnterIndex(btnExit);
	}

	@Override
	public void formInit() {
		try {
			java.util.List<TboFCOMPBRANC> lst_data = TbmFCOMPBRANC.getBrancList(this.getLoginBean().getCOMP_CDE());
			for (TboFCOMPBRANC tboFCOMPBRANC : lst_data) {
				ZkUtil.appendItemToComboBox(cmbBRANC_CDE, tboFCOMPBRANC.getBRANC_CDE()+"", tboFCOMPBRANC.getBRANC_NAME());
			}
			
			clearData();
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	private void clearData() {
		intYear.focus();
		intYear.setValue(FnDate.getYear(FnDate.getTodayDateTime()) + 543);
		intMonth.setValue(FnDate.getMonth(FnDate.getTodayDateTime()));
		cmbReportType.setSelectedIndex(0);
		if (cmbBRANC_CDE.getItemCount() > 0) {
			cmbBRANC_CDE.setSelectedIndex(0);
		}
	}

	public void onOK() {
		try {
			super.onOK();
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	private void validateData(int print_option) throws Exception {
		if (intYear.getValue() == null || intYear.getValue().intValue() < 2555) {
			throw new Exception("ระบุช่อง \"" + intYear.getTooltiptext() + "\" ไม่ถูกต้อง");
		}
		if (Fnc.getIntValue(intMonth.getValue()) < 1 || Fnc.getIntValue(intMonth.getValue()) > 12) {
			throw new Exception("ระบุช่อง \"" + intMonth.getTooltiptext() + "\" ไม่ถูกต้อง");
		}
		if (Fnc.isEmpty(ZkUtil.getSelectItemValueComboBox(cmbReportType))) {
			throw new Exception("ระบุช่อง \"" + cmbReportType.getTooltiptext() + "\" ไม่ถูกต้อง");
		}
		if (print_option == 11) {
			if (tdbTaxDate.getValue() == null) {
				throw new Exception("ระบุช่อง \"" + tdbTaxDate.getTooltiptext() + "\" ไม่ถูกต้อง");
			}
			if (Fnc.isEmpty(txtPaidByName.getValue())) {
				throw new Exception("ต้องระบุช่อง \"" + txtPaidByName.getTooltiptext() + "\" ");
			}
			if (Fnc.isEmpty(txtPosition.getValue())) {
				throw new Exception("ต้องระบุช่อง \"" + txtPosition.getTooltiptext() + "\" ");
			}
			if (ZkUtil.getSelectItemValueComboBox(cmbBRANC_CDE).equals("")) {
				throw new Exception("ต้องระบุช่อง \"" + cmbBRANC_CDE.getTooltiptext() + "\" ");
			}
		}
	}

	public void onClick_btnPrint(int print_option) {

		try {

			validateData(print_option);

			// call web service
			JSONObject jsonParameter = new JSONObject();

			jsonParameter.put("intYear", intYear.getValue());
			jsonParameter.put("intMonth", intMonth.getValue());
			jsonParameter.put("cmbReportType", ZkUtil.getSelectItemValueComboBox(cmbReportType));
			jsonParameter.put("tdbTaxDate", tdbTaxDate.getSqlDate());
			jsonParameter.put("cmbBRANC_CDE", ZkUtil.getSelectItemValueComboBox(cmbBRANC_CDE));
			jsonParameter.put("txtPaidByName", txtPaidByName.getValue());
			jsonParameter.put("txtPosition", txtPosition.getValue());

			String requestdesc = "บริษัท " + this.getLoginBean().getCOMP_NAME() + " เดือน " + intMonth.getValue() + " ปี " + intYear.getValue();

			JSONObject json = null;

			if (print_option == 1 || print_option == 2) {
				json = ApiUtil.callWebServiceVer2(
						jsonParameter,
						this.getLoginBean(),
						this.getMenuId2(),
						this.getTitle(),
						requestdesc,
						"ApiAcWithold.getData1",
						"gl");
				
			} else if (print_option == 10) {
				json = ApiUtil.callWebServiceVer2(
						jsonParameter,
						this.getLoginBean(),
						this.getMenuId2(),
						this.getTitle(),
						requestdesc,
						"ApiAcWithold.getData2",
						"gl");
				
			} else if (print_option == 11) {
				json = ApiUtil.callWebServiceVer2(
						jsonParameter,
						this.getLoginBean(),
						this.getMenuId2(),
						this.getTitle(),
						requestdesc,
						"ApiAcWithold.getData11",
						"gl");
				
			}

			if (!ApiUtil.isErrorService(json)) {
				// System.out.println(json.toString());
				FReport.saveFile(json.getString("base64"), json.getString("format"), this.getMenuId2());
			} else {
				throw new Exception(ApiUtil.getErrorService(json));
			}

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void onChange_Data(Component comp) {
		try {
//			if (comp.getId().equals("txtCUST_CDE")) {
//				read_cust(txtCUST_CDE.getValue());
//			}
		} catch (Exception e) {
			Msg.error(e);
		}
	}

}

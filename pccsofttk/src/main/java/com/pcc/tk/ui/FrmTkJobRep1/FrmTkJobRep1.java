package com.pcc.tk.ui.FrmTkJobRep1;

import org.json.JSONObject;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Textbox;

import com.pcc.api.core.ApiUtil;
import com.pcc.sys.lib.FReport;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.MyDatebox;
import com.pcc.sys.lib.ZkUtil;
import com.pcc.tk.find.FfTKCOURT;
import com.pcc.tk.find.FfTKJOBCODE;
import com.pcc.tk.find.FfTKLAWSTAT;
import com.pcc.tk.find.FfTKLAWTYPE;
import com.pcc.tk.find.FfTKLAWYER;
import com.pcc.tk.tbf.TbfTKCOURT;
import com.pcc.tk.tbf.TbfTKJOBCODE;
import com.pcc.tk.tbf.TbfTKLAWSTAT;
import com.pcc.tk.tbf.TbfTKLAWTYPE;
import com.pcc.tk.tbf.TbfTKLAWYER;
import com.pcc.tk.tbo.TboTKCOURT;
import com.pcc.tk.tbo.TboTKJOBCODE;
import com.pcc.tk.tbo.TboTKLAWSTAT;
import com.pcc.tk.tbo.TboTKLAWTYPE;
import com.pcc.tk.tbo.TboTKLAWYER;

public class FrmTkJobRep1 extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Button btnExit;
	//public Button btnPrint;
	public Button btnExcel;
	public Checkbox chkNOTCLOSE;
	public MyDatebox tdbJOBDATE_FROM;
	public MyDatebox tdbJOBDATE_TO;
	public MyDatebox tdbLOGDATE_FROM;
	public MyDatebox tdbLOGDATE_TO;
	public Textbox txtLAWSTATID;
	public Textbox txtLAWSTATNAME;
	public Textbox txtJOBCODE;
	public Textbox txtJOBCODENAME;
	public Textbox txtLAWTYPEID;
	public Textbox txtLAWTYPENAME;
	public Textbox txtLAWYERID;
	public Textbox txtLAWYERNAME;
	public Textbox txtCOURTID;
	public Textbox txtCOURTNAME;

	@Override
	public void setFormObj() {
		btnExit = (Button) getFellow("btnExit");
		//btnPrint = (Button) getFellow("btnPrint");
		btnExcel = (Button) getFellow("btnExcel");
		chkNOTCLOSE = (Checkbox) getFellow("chkNOTCLOSE");
		tdbJOBDATE_FROM = (MyDatebox) getFellow("tdbJOBDATE_FROM");
		tdbJOBDATE_TO = (MyDatebox) getFellow("tdbJOBDATE_TO");
		tdbLOGDATE_FROM = (MyDatebox) getFellow("tdbLOGDATE_FROM");
		tdbLOGDATE_TO = (MyDatebox) getFellow("tdbLOGDATE_TO");
		txtLAWSTATID = (Textbox) getFellow("txtLAWSTATID");
		txtLAWSTATNAME = (Textbox) getFellow("txtLAWSTATNAME");
		txtJOBCODE = (Textbox) getFellow("txtJOBCODE");
		txtJOBCODENAME = (Textbox) getFellow("txtJOBCODENAME");
		txtLAWTYPEID = (Textbox) getFellow("txtLAWTYPEID");
		txtLAWTYPENAME = (Textbox) getFellow("txtLAWTYPENAME");
		txtLAWYERID = (Textbox) getFellow("txtLAWYERID");
		txtLAWYERNAME = (Textbox) getFellow("txtLAWYERNAME");
		txtCOURTID = (Textbox) getFellow("txtCOURTID");
		txtCOURTNAME = (Textbox) getFellow("txtCOURTNAME");

	}

	@Override
	public void addEnterIndex() {

		addEnterIndex(btnExit);
		addEnterIndex(tdbJOBDATE_FROM);
		addEnterIndex(tdbJOBDATE_TO);
		addEnterIndex(tdbLOGDATE_FROM);
		addEnterIndex(tdbLOGDATE_TO);
		addEnterIndex(txtLAWSTATID);
		//addEnterIndex(txtLAWSTATNAME);
		addEnterIndex(txtJOBCODE);
		//addEnterIndex(txtJOBCODENAME);
		addEnterIndex(txtLAWTYPEID);
		//addEnterIndex(txtLAWTYPENAME);
		addEnterIndex(txtLAWYERID);
		//addEnterIndex(txtLAWYERNAME);
		addEnterIndex(txtCOURTID);
		//addEnterIndex(txtCOURTNAME);
		//addEnterIndex(btnPrint);
		addEnterIndex(btnExcel);

	}

	@Override
	public void formInit() {
		try {
			clearData();
		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}

	}

	public void clearData() {
	}

	public void onOK() {
		try {
			if (focusObj == txtLAWSTATID) {
				if (Fnc.isEmpty(txtLAWSTATID.getValue())) {
					popupLAWSTATID();
				} else {
					super.onOK();
				}
			} else if (focusObj == txtJOBCODE) {
				if (Fnc.isEmpty(txtJOBCODE.getValue())) {
					popupJOBCODE();
				} else {
					super.onOK();
				}
			} else {
				super.onOK();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}

	}
	
	public void onChange_Data(HtmlBasedComponent comp) {
		try {
			if (comp != null) {

				if (!Fnc.isEmpty(comp.getId())) {
					ZkUtil.setAndClearOnChang(comp, this.objectList);
				}

				if (comp.getId().equals("txtLAWSTATID")) {
					read_LAWSTATID(Fnc.getIntFromStr(txtLAWSTATID.getValue()));
				}
				if (comp.getId().equals("txtJOBCODE")) {
					read_JOBCODE(Fnc.getIntFromStr(txtJOBCODE.getValue()));
				}
				if (comp.getId().equals("txtLAWTYPEID")) {
					read_LAWTYPE(Fnc.getIntFromStr(txtLAWTYPEID.getValue()));
				}
				if (comp.getId().equals("txtLAWYERID")) {
					read_LAWYERID(Fnc.getIntFromStr(txtLAWYERID.getValue()));
				}
				if (comp.getId().equals("txtCOURTID")) {
					read_COURTID(Fnc.getIntFromStr(txtCOURTID.getValue()));
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void onClick_btnPrint(int print_option) {
		try {

			validateData(print_option);
			
			// call web service
			JSONObject jsonParameter = new JSONObject();

			jsonParameter.put("chkNOTCLOSE", chkNOTCLOSE.isChecked());
			jsonParameter.put("tdbJOBDATE_FROM", tdbJOBDATE_FROM.getSqlDate());
			jsonParameter.put("tdbJOBDATE_TO", tdbJOBDATE_TO.getSqlDate());
			jsonParameter.put("tdbLOGDATE_FROM", tdbLOGDATE_FROM.getSqlDate());
			jsonParameter.put("tdbLOGDATE_TO", tdbLOGDATE_TO.getSqlDate());
			jsonParameter.put("txtLAWSTATID", txtLAWSTATID.getValue());
			jsonParameter.put("txtJOBCODE", txtJOBCODE.getValue());
			jsonParameter.put("txtLAWTYPEID", txtLAWTYPEID.getValue());
			jsonParameter.put("txtLAWYERID", txtLAWYERID.getValue());
			jsonParameter.put("txtCOURTID", txtCOURTID.getValue());
			jsonParameter.put("print_option", print_option);

			String requestdesc = "";
			if (chkNOTCLOSE.isChecked()) {
				requestdesc += "สถานะ : ยังไม่ปิดงาน,";	
			} else {
				requestdesc += "สถานะ : ปิดงานแล้ว,";
			}
			requestdesc += " วันที่รับงาน :" + FnDate.displayDateString(tdbJOBDATE_FROM.getValue());
			requestdesc += ", ถึงวันที่รับงาน :" + FnDate.displayDateString(tdbJOBDATE_TO.getValue());
			requestdesc += ", วันที่สถานะการงาน :" + FnDate.displayDateString(tdbLOGDATE_FROM.getValue());
			requestdesc += ", ถึงวันที่สถานะการงาน :" + FnDate.displayDateString(tdbLOGDATE_TO.getValue());
			requestdesc += ", สถานะคดี  :" + (Fnc.isEmpty(txtLAWSTATNAME.getValue())?"ทั้งหมด":txtLAWSTATNAME.getValue());
			requestdesc += ", Job Code  :" + (Fnc.isEmpty(txtJOBCODENAME.getValue())?"ทั้งหมด":txtJOBCODENAME.getValue());
			requestdesc += ", ประเภทคดี  :" + (Fnc.isEmpty(txtLAWTYPENAME.getValue())?"ทั้งหมด":txtLAWTYPENAME.getValue());
			requestdesc += ", ทนาย  :" + (Fnc.isEmpty(txtLAWYERNAME.getValue())?"ทั้งหมด":txtLAWYERNAME.getValue());
			requestdesc += ", ศาล  :" + (Fnc.isEmpty(txtCOURTNAME.getValue())?"ทั้งหมด":txtCOURTNAME.getValue());

			jsonParameter.put("reportCondition", requestdesc);

			JSONObject json = ApiUtil.callWebServiceVer2(
					jsonParameter,
					this.getLoginBean(),
					this.getMenuId2(),
					this.getTitle(),
					requestdesc,
					"ApiFrmTkJobRep1.getData1",
					"tk");

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

	private void validateData(int print_option) throws Exception {
		if (chkNOTCLOSE.isChecked() == false ) {
			if (tdbJOBDATE_FROM.getSqlDate() == null) {
				throw new Exception("ยังไม่ระบุ"+tdbJOBDATE_FROM.getTooltiptext());
			}
			if (tdbJOBDATE_TO.getSqlDate() == null) {
				throw new Exception("ยังไม่ระบุ"+tdbJOBDATE_TO.getTooltiptext());
			}
		}
	}

	public void popupLAWSTATID() {
		if (!txtLAWSTATID.isReadonly()) {
			FfTKLAWSTAT.popup(this.getLoginBean(), this, "read_LAWSTATID");
		}
	}

	public boolean read_LAWSTATID(int lawstatid) throws Exception {

		TboTKLAWSTAT tb1 = new TboTKLAWSTAT();

		tb1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		tb1.setLAWSTATID(lawstatid);

		if (TbfTKLAWSTAT.find(tb1)) {
			txtLAWSTATID.setValue(tb1.getLAWSTATID() + "");
			txtLAWSTATNAME.setValue(tb1.getLAWSTATNAME());
			return true;
		} else {
			txtLAWSTATNAME.setValue("");
			return false;
		}

	}

	public void popupJOBCODE() {
		if (!txtJOBCODE.isReadonly()) {
			FfTKJOBCODE.popup(this.getLoginBean(), this, "read_JOBCODE");
		}
	}

	public boolean read_JOBCODE(int jobcode) throws Exception {

		TboTKJOBCODE tb1 = new TboTKJOBCODE();

		tb1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		tb1.setJOBCODE(jobcode);

		if (TbfTKJOBCODE.find(tb1)) {
			txtJOBCODE.setValue(tb1.getJOBCODE() + "");
			txtJOBCODENAME.setValue(tb1.getJOBNAME());
			return true;
		} else {
			txtJOBCODENAME.setValue("");
			return false;
		}

	}

	public void popupLAWTYPEID() {
		if (!txtLAWTYPEID.isReadonly()) {
			FfTKLAWTYPE.popup(this.getLoginBean(), this, "read_LAWTYPE");
		}
	}

	public boolean read_LAWTYPE(int lawtypeid) throws Exception {

		TboTKLAWTYPE tklawtype = new TboTKLAWTYPE();

		tklawtype.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		tklawtype.setLAWTYPEID(lawtypeid);

		if (TbfTKLAWTYPE.find(tklawtype)) {
			txtLAWTYPEID.setValue(tklawtype.getLAWTYPEID() + "");
			txtLAWTYPENAME.setValue(tklawtype.getLAWTYPENAME());
			return true;
		} else {
			txtLAWTYPENAME.setValue("");
			return false;
		}

	}

	public void popupCOURTID() {
		if (!txtCOURTID.isReadonly()) {
			FfTKCOURT.popup(this.getLoginBean(), this, "read_COURTID");
		}
	}

	public boolean read_COURTID(int courtid) throws Exception {

		TboTKCOURT tb1 = new TboTKCOURT();

		tb1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		tb1.setCOURTID(courtid);

		if (TbfTKCOURT.find(tb1)) {
			txtCOURTID.setValue(tb1.getCOURTID() + "");
			txtCOURTNAME.setValue(tb1.getCOURTNAME());
			return true;
		} else {
			txtCOURTNAME.setValue("");
			return false;
		}

	}
	
	public void popupLAWYERID() {
		if (!txtLAWYERID.isReadonly()) {
			FfTKLAWYER.popup(this.getLoginBean(), this, "read_LAWYERID");
		}
	}

	public boolean read_LAWYERID(int lawyerid) throws Exception {

		TboTKLAWYER table1 = new TboTKLAWYER();

		table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		table1.setLAWYERID(lawyerid);

		if (TbfTKLAWYER.find(table1)) {
			txtLAWYERID.setValue(table1.getLAWYERID() + "");
			txtLAWYERNAME.setValue(table1.getLAWYERNAME());
			return true;
		} else {
			txtLAWYERNAME.setValue("");
			return false;
		}

	}

}

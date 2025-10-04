package com.pcc.bx.ui.FrmBxBill;

import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.pcc.bx.tbf.TbfBXHEADER;
import com.pcc.bx.tbo.TboBXHEADER;
import com.pcc.gl.find.FfFCUS;
import com.pcc.gl.tbf.TbfFCUS;
import com.pcc.gl.tbo.TboFCUS;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FWindow;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.MyDatebox;
import com.pcc.sys.lib.MyDecimalbox;
import com.pcc.sys.lib.ZkUtil;
import com.pcc.sys.tbf.TbfFCOMPBRANC;
import com.pcc.sys.tbo.TboFCOMPBRANC;

public class FrmBxBillCost extends FWindow {

	private static final long serialVersionUID = 1L;

	public static void showData(LoginBean loginBean, String blno, Window winCall, String menuid2 , EventListener<Event> event1) throws Exception {
		
		winCall.setVisible(false);
		String newID = "FrmBxBillCost" + menuid2; //ป้องกันซ้ำหลายเมนู
		FrmBxBillCost frm1 = (FrmBxBillCost) ZkUtil.callZulFile("/com/pcc/bx/zul/FrmBxBill/FrmBxBillCost.zul");
		
		//==parameter
		frm1.setLoginBean(loginBean);
		frm1.p_win_call = winCall;
		frm1.p_BLNO = blno;
		frm1.p_Event = event1;
		
		//==ค่าอื่นๆ
		frm1.setAppendMode(newID, winCall.getWidth(), winCall.getParent());
		
	}

	public Button btnExit;
	public Button btnSave;
	public Textbox txtBLNO;
	public MyDatebox tdbBLDATE;
	public Combobox cmbVATTYPE;
	public MyDecimalbox decVATRATE;
	public Textbox txtBRANC_CDE;
	public Textbox txtBRANC_NAME;
	public Textbox txtCUST_CDE;
	public Textbox txtCUST_TITLE;
	public Textbox txtCUST_FNAME;
	public Textbox txtCUST_LNAME;
	public MyDecimalbox decCOSTBASAMT;
	public MyDecimalbox decCOSTVATAMT;
	public MyDecimalbox decCOSTTOTAL;
	public Textbox txtCUST_CDE_AP;
	public Textbox txtAPNAME;

	public String p_BLNO = "";
	public Window p_win_call = null;
	public EventListener<Event> p_Event;

	@Override
	public void setFormObj() {

		btnExit = (Button) getFellow("btnExit");
		btnSave = (Button) getFellow("btnSave");
		txtBLNO = (Textbox) getFellow("txtBLNO");
		tdbBLDATE = (MyDatebox) getFellow("tdbBLDATE");
		cmbVATTYPE = (Combobox) getFellow("cmbVATTYPE");
		decVATRATE = (MyDecimalbox) getFellow("decVATRATE");
		txtBRANC_CDE = (Textbox) getFellow("txtBRANC_CDE");
		txtBRANC_NAME = (Textbox) getFellow("txtBRANC_NAME");
		txtCUST_CDE = (Textbox) getFellow("txtCUST_CDE");
		txtCUST_TITLE = (Textbox) getFellow("txtCUST_TITLE");
		txtCUST_FNAME = (Textbox) getFellow("txtCUST_FNAME");
		txtCUST_LNAME = (Textbox) getFellow("txtCUST_LNAME");
		decCOSTBASAMT = (MyDecimalbox) getFellow("decCOSTBASAMT");
		decCOSTVATAMT = (MyDecimalbox) getFellow("decCOSTVATAMT");
		decCOSTTOTAL = (MyDecimalbox) getFellow("decCOSTTOTAL");
		txtCUST_CDE_AP = (Textbox) getFellow("txtCUST_CDE_AP");
		txtAPNAME = (Textbox) getFellow("txtAPNAME");

	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(decCOSTBASAMT);
		addEnterIndex(decCOSTVATAMT);
		addEnterIndex(txtCUST_CDE_AP);
		addEnterIndex(btnSave);
	}

	@Override
	public void formInit() {
		try {
			if (!Fnc.isEmpty(this.p_BLNO)) {
				read_record(this.p_BLNO);
			}
			decCOSTBASAMT.focus();
			decCOSTBASAMT.select();
		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}

	}

	public void onOK() {
		try {
			super.onOK();
		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}

	}

	public void onClose() {
		try {
			super.onClose();
			if (p_win_call != null) {
				p_win_call.setVisible(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void read_record(String blno) throws WrongValueException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			read_record(dbc, blno);
		}
	}

	private void read_record(FDbc dbc, String blno) throws WrongValueException, Exception {
		TboBXHEADER head = new TboBXHEADER();

		head.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		head.setBLNO(blno);

		if (TbfBXHEADER.find(dbc, head)) {

			// === set value
			tdbBLDATE.setValue(head.getBLDATE());
			txtBLNO.setValue(head.getBLNO());
			ZkUtil.setSelectItemComboBoxByValue(cmbVATTYPE, head.getVATTYPE());
			decVATRATE.setValue(head.getVATRATE());
			read_BRANC_CDE(head.getBRANC_CDE());
			txtCUST_CDE.setValue(head.getCUST_CDE());
			txtCUST_TITLE.setValue(head.getCUST_TITLE());
			txtCUST_FNAME.setValue(head.getCUST_FNAME());
			txtCUST_LNAME.setValue(head.getCUST_LNAME());

			decCOSTBASAMT.setValue(head.getCOSTBASAMT());
			decCOSTVATAMT.setValue(head.getCOSTVATAMT());
			decCOSTTOTAL.setValue(decCOSTBASAMT.getValue().add(decCOSTVATAMT.getValue()));
			
			txtCUST_CDE_AP.setValue(head.getCUST_CDE_AP());
			txtAPNAME.setValue(head.getAPNAME());

		}

	}

	public void onClick_Save() {

		boolean[] clearWhenError = { false };

		try (FDbc dbc = FDbc.connectMasterDb()) {
			
			if (!Fnc.isEmpty(txtCUST_CDE_AP.getValue()) && Fnc.isEmpty(txtAPNAME.getValue())) {
				throw new Exception(txtCUST_CDE_AP.getTooltiptext() + " ไม่ถูกต้อง");
			}

			TboBXHEADER head = new TboBXHEADER();

			head.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			head.setBLNO(txtBLNO.getValue());

			if (TbfBXHEADER.find(dbc, head)) {
				head.setCOSTBASAMT(decCOSTBASAMT.getValue());
				head.setCOSTVATAMT(decCOSTVATAMT.getValue());
				head.setCUST_CDE_AP(txtCUST_CDE_AP.getValue());
				head.setAPNAME(txtAPNAME.getValue());
				TbfBXHEADER.update(dbc, head);
			}
			
			if (p_Event != null) {
				p_Event.onEvent(new Event(Events.ON_CLICK, null, -1));
			}
			
			this.onClose();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
			if (clearWhenError[0]) {
				this.onClose();
			}
		}
		
	}

	public void read_BRANC_CDE(int branc_cde) throws Exception {

		TboFCOMPBRANC bn = new TboFCOMPBRANC();

		bn.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		bn.setBRANC_CDE(branc_cde);

		if (TbfFCOMPBRANC.find(bn)) {
			txtBRANC_CDE.setValue(bn.getBRANC_CDE() + "");
			txtBRANC_NAME.setValue(bn.getBRANC_NAME());
		} else {
			txtBRANC_CDE.setValue(branc_cde + "");
			txtBRANC_NAME.setValue("");
		}

	}
	
	public void onChange_Data(HtmlBasedComponent comp) {
		try {
			if (comp != null) {
				if (!Fnc.isEmpty(comp.getId())) {
					ZkUtil.setAndClearOnChang(comp, this.objectList);
					// ตรวจด้วย ZkUtil.doOnChange(xx) ใน onOK()
				}
			}
			//
			if (comp.getId().equals("decCOSTBASAMT") || comp.getId().equals("decCOSTVATAMT")) {
				decCOSTTOTAL.setValue(decCOSTBASAMT.getValue().add(decCOSTVATAMT.getValue()));
			}
			
			if (comp.getId().equals("txtCUST_CDE_AP")) {
				readAP(txtCUST_CDE_AP.getValue());
			}

		} catch (Exception e) {
		}
	}
	
	public void popupCUST_CDE_AP() {
		FfFCUS.popup(this.getLoginBean(), this, "readAP");
	}

	public void readAP(String cust_cde) {
		try {

			TboFCUS cust = new TboFCUS();

			cust.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			cust.setCUST_CDE(cust_cde);

			if (TbfFCUS.find(cust)) {
				txtCUST_CDE_AP.setValue(cust.getCUST_CDE());

				String apName = cust.getTITLE() + " " + cust.getFNAME() + " " + cust.getLNAME();
				txtAPNAME.setValue(apName);
			} else {
				txtAPNAME.setValue("");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


}

package com.pcc.gl.ui.acAcctcodeApAr;

import java.sql.SQLException;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.pcc.gl.find.FfACCT_NO;
import com.pcc.gl.tbf.TbfACCT_NO;
import com.pcc.gl.tbf.TbfACCT_NO_SUB;
import com.pcc.gl.tbo.TboACCT_NO;
import com.pcc.gl.tbo.TboACCT_NO_SUB;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FWindow;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.ZkUtil;

public class AcAcctcodeApArEntr extends FWindow {

	public static void showData(LoginBean loginBean, String acctid, Window winCall, String menuid2, boolean insertMode, EventListener<Event> event1) throws Exception {

		winCall.setVisible(false);
		String newID = "AcAcctcodeApArEntr" + menuid2; // ป้องกันซ้ำหลายเมนู
		AcAcctcodeApArEntr frm1 = (AcAcctcodeApArEntr) ZkUtil.callZulFile("/com/pcc/gl/zul/AcAcctcodeApAr/AcAcctcodeApArEntr.zul");

		// ==parameter
		frm1.setLoginBean(loginBean);
		frm1.p_ACCTID = acctid;
		frm1.p_win_call = winCall;
		frm1.p_Event = event1;
		frm1.p_insertmode = insertMode;

		// ==ค่าอื่นๆ
		frm1.setAppendMode(newID, winCall.getWidth(), winCall.getParent());

	}

	private static final long serialVersionUID = 1L;

	public Textbox txtACCT_ID;
	public Textbox txtACCT_NAME;
	public Combobox cmbSUB_TYPE;
	public Textbox txtACCT_ID_BANK;
	public Textbox txtACCT_NAME_BANK;

	private Textbox txtINSBY, txtINSDT, txtUPBY, txtUPDT;

	public Button btnSave;

	public String p_ACCTID = "";
	public Window p_win_call = null;
	public EventListener<Event> p_Event;
	public boolean p_insertmode = false;

	@Override
	public void setFormObj() {

		txtACCT_ID = (Textbox) getFellow("txtACCT_ID");
		txtACCT_NAME = (Textbox) getFellow("txtACCT_NAME");
		cmbSUB_TYPE = (Combobox) getFellow("cmbSUB_TYPE");
		txtACCT_ID_BANK = (Textbox) getFellow("txtACCT_ID_BANK");
		txtACCT_NAME_BANK = (Textbox) getFellow("txtACCT_NAME_BANK");

		txtINSBY = (Textbox) getFellow("txtINSBY");
		txtINSDT = (Textbox) getFellow("txtINSDT");
		txtUPBY = (Textbox) getFellow("txtUPBY");
		txtUPDT = (Textbox) getFellow("txtUPDT");

		btnSave = (Button) getFellow("btnSave");

	}

	@Override
	public void addEnterIndex() {

		addEnterIndex(txtACCT_ID);
		addEnterIndex(cmbSUB_TYPE);
		addEnterIndex(txtACCT_ID_BANK);
		addEnterIndex(btnSave);

	}

	@Override
	public void formInit() {

		try {
			clearData();
			if (!Fnc.isEmpty(p_ACCTID)) {
				showData(p_ACCTID);
			}
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	private void clearData() {

		txtACCT_ID.setValue("");
		txtACCT_ID.focus();
		txtACCT_NAME.setValue("");
		cmbSUB_TYPE.setSelectedIndex(0);
		txtACCT_ID_BANK.setValue("");
		txtACCT_NAME_BANK.setValue("");

		txtINSBY.setValue("");
		txtINSDT.setValue("");
		txtUPBY.setValue("");
		txtUPDT.setValue("");
		
		onChange_Data(cmbSUB_TYPE);

	}

	public void onOK() {
		try {
			super.onOK();
		} catch (Exception e) {
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

	public void onClick_Save() {

		boolean newrec = true;
		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				validateData(dbc);

				TboACCT_NO_SUB acctNoSub = new TboACCT_NO_SUB();

				acctNoSub.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				acctNoSub.setACCT_ID(txtACCT_ID.getValue().trim());

				if (TbfACCT_NO_SUB.find(dbc, acctNoSub)) {
					newrec = false;
				}

				acctNoSub.setSUB_TYPE(Fnc.getIntFromStr(ZkUtil.getSelectItemValueComboBox(cmbSUB_TYPE)));
				if (acctNoSub.getSUB_TYPE() == 3) {// เช็คจ่ายล่วงหน้า
					acctNoSub.setACCT_ID_BANK(txtACCT_ID_BANK.getValue().trim());
				} else {
					acctNoSub.setACCT_ID_BANK("");
				}

				acctNoSub.setUPBY(this.getLoginBean().getUSER_ID());
				acctNoSub.setUPDT(FnDate.getTodaySqlDateTime());

				if (newrec) {

					acctNoSub.setINSBY(this.getLoginBean().getUSER_ID());
					acctNoSub.setINSDT(FnDate.getTodaySqlDateTime());

					if (!TbfACCT_NO_SUB.insert(dbc, acctNoSub)) {
						throw new Exception("เพิ่มไม่ได้");
					}

				} else {
					if (!TbfACCT_NO_SUB.update(dbc, acctNoSub, "")) {
						throw new Exception("ปรับปรุงไม่ได้");
					}
				}

				dbc.commit();

			}

			Msg.info("บันทึกเรียบร้อย");

			if (p_Event != null) {
				p_Event.onEvent(new Event(Events.ON_CLICK, null, -1));
			}
			this.onClose();

		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}

	}

	public void onChange_Data(Component comp) {
		try {
			
			if (comp.getId().equals("txtACCT_ID")) {
				
				read_ACCT_ID(txtACCT_ID.getValue());
				checkDup(txtACCT_ID.getValue());
				
			}
			
			if (comp.getId().equals("cmbSUB_TYPE")) {
				String value1 = ZkUtil.getSelectItemValueComboBox(cmbSUB_TYPE);
				if (Fnc.getIntFromStr(value1) == 3) {
					txtACCT_ID_BANK.setReadonly(false);
				} else {
					txtACCT_ID_BANK.setReadonly(true);
				}
			}
			
			if (comp.getId().equals("txtACCT_ID_BANK")) {
				read_ACCT_ID_BANK(txtACCT_ID_BANK.getValue());
			}
			
		} catch (Exception e) {
		}
	}

	private void validateData(FDbc dbc) throws Exception {

		if (Fnc.isEmpty(txtACCT_ID.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtACCT_ID.getTooltiptext() + "\" ");
		}
		
		TboACCT_NO acctno = new TboACCT_NO();
		acctno.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		acctno.setACCT_ID(txtACCT_ID.getValue());
		if (!TbfACCT_NO.find(dbc, acctno)) {
			throw new Exception("ระบุช่อง \"" + txtACCT_ID.getTooltiptext() + "\" ไม่ถูกต้อง ");
		}
		
		int sub_type = Fnc.getIntFromStr(ZkUtil.getSelectItemValueComboBox(cmbSUB_TYPE)); 
		if (sub_type == 0) {
			throw new Exception("ระบุช่อง \"" + cmbSUB_TYPE.getTooltiptext() + "\" ไม่ถูกต้อง");
		}
		if (sub_type == 1) { // เจ้าหนี้
			if (acctno.getACCT_TYPE() != 2) {
				throw new Exception("\"" + txtACCT_ID.getTooltiptext() + "\" ต้องเป็นหมวดเจ้าหนี้ถ้าเป็นตัวย่อยของเจ้าหนี้");
			}
		} else if (sub_type == 2) { // ลูกหนี้
			if (acctno.getACCT_TYPE() != 1) {
				throw new Exception("\"" + txtACCT_ID.getTooltiptext() + "\" ต้องเป็นหมวดสินทรัพย์ถ้าเป็นตัวย่อยของลูกหนี้");
			}
		} else if (sub_type == 3) { // เช็คจ่ายล่วงหน้า
			if (acctno.getACCT_TYPE() != 2) {
				throw new Exception("\"" + txtACCT_ID.getTooltiptext() + "\" ต้องเป็นหมวดเจ้าหนี้ถ้าเป็นเช็คจ่ายล่วงหน้า");
			}
			if (!read_ACCT_ID_BANK(txtACCT_ID_BANK.getValue())) {
				throw new Exception("\"" + txtACCT_ID_BANK.getTooltiptext() + "\" ไม่ถูกต้อง");
			}
		}

	}

	public void popupACCT_ID() {
		try {
			
			if (!txtACCT_ID.isReadonly()) {
				FfACCT_NO.popup("", this.getLoginBean(), (event) -> {
					String acct_id =  (String) event.getData();
					//System.out.println("acct_id : " + acct_id);//test ok
					read_ACCT_ID(acct_id);
					//=== อ่านว่าเคย set หรือยังป้องกันซ้ำ ถ้ามีให้แสดงทันที
					checkDup(acct_id);
				});
			}
			
		} catch (Exception e) {
			Msg.error(e);
		}
		
	}

	private boolean read_ACCT_ID(String acct_id) throws Exception {

		boolean res = false;
		TboACCT_NO acctno = new TboACCT_NO();
		acctno.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		acctno.setACCT_ID(acct_id);

		if (TbfACCT_NO.find(acctno)) {
			txtACCT_ID.setValue(acctno.getACCT_ID());
			txtACCT_NAME.setValue(acctno.getACCT_NAME());
			res = true;
		} else {
			txtACCT_NAME.setValue("");
		}
		return res;

	}

	public void popupACCT_ID_BANK() {
		try {
			
			if (!txtACCT_ID_BANK.isReadonly()) {
				FfACCT_NO.popup("", this.getLoginBean(), (event) -> {
					String acct_id =  (String) event.getData();
					//System.out.println("acct_id : " + acct_id);//test ok
					if (read_ACCT_ID_BANK(acct_id)) {
						txtACCT_ID_BANK.focus();
					}
				});
				
			}
			
		} catch (Exception e) {
			Msg.error(e);
		}
			
	}

	private boolean read_ACCT_ID_BANK(String acct_id) throws Exception {

		boolean res = false;
		TboACCT_NO acctno = new TboACCT_NO();
		acctno.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		acctno.setACCT_ID(acct_id);

		if (TbfACCT_NO.find(acctno)) {
			txtACCT_ID_BANK.setValue(acctno.getACCT_ID());
			txtACCT_NAME_BANK.setValue(acctno.getACCT_NAME());
			res = true;
		} else {
			txtACCT_NAME_BANK.setValue("");
		}
		return res;

	}

	public boolean showData(String acct_id) throws Exception {

		// == ค้นหาในตารางตัวย่อย
		TboACCT_NO_SUB acctnoSUB = new TboACCT_NO_SUB();

		acctnoSUB.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		acctnoSUB.setACCT_ID(acct_id);

		if (TbfACCT_NO_SUB.find(acctnoSUB)) {

			read_ACCT_ID(acct_id);
			ZkUtil.setSelectItemComboBoxByValue(cmbSUB_TYPE, acctnoSUB.getSUB_TYPE() + "");
			if (acctnoSUB.getSUB_TYPE() == 3) {// เช็คจ่ายล่วงหน้า
				read_ACCT_ID_BANK(acctnoSUB.getACCT_ID_BANK());
				txtACCT_ID_BANK.setReadonly(false);
			} else {
				txtACCT_ID_BANK.setReadonly(true);
				btnSave.setVisible(false);
			}

			txtINSBY.setValue(acctnoSUB.getINSBY());
			txtINSDT.setValue(FnDate.displayDateTimeString(acctnoSUB.getINSDT()));
			txtUPBY.setValue(acctnoSUB.getUPBY());
			txtUPDT.setValue(FnDate.displayDateTimeString(acctnoSUB.getUPDT()));

			txtACCT_ID.setReadonly(true);
			cmbSUB_TYPE.setDisabled(true);

			return true;

		} else {
			return false;
		}

	}
	
	private void checkDup(String acctid) throws WrongValueException, SQLException, Exception {

		//=== อ่านว่าเคย set หรือยังป้องกันซ้ำ ถ้ามีให้แสดงทันที
		TboACCT_NO_SUB acctNoSub = new TboACCT_NO_SUB();
		acctNoSub.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		acctNoSub.setACCT_ID(txtACCT_ID.getValue());
		if (TbfACCT_NO_SUB.find(acctNoSub)) {
			showData(txtACCT_ID.getValue());
		}
		
	}

}

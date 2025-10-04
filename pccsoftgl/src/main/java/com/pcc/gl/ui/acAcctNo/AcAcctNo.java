package com.pcc.gl.ui.acAcctNo;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Textbox;

import com.pcc.gl.find.FfACCT_NO;
import com.pcc.gl.tbf.TbfACCT_NO;
import com.pcc.gl.tbo.TboACCT_NO;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.ZkUtil;

public class AcAcctNo extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Textbox txtACCT_ID;
	public Textbox txtACCT_NAME;
	public Combobox cmbACCT_TYPE;
	private Textbox txtINSBY, txtINSDT, txtUPBY, txtUPDT;

	public Div div1;

	public Button btnExit, btnBack, btnSave, btnDelete, btnAdd;

	@Override
	public void setFormObj() {
		txtACCT_ID = (Textbox) getFellow("txtACCT_ID");
		txtACCT_NAME = (Textbox) getFellow("txtACCT_NAME");
		cmbACCT_TYPE = (Combobox) getFellow("cmbACCT_TYPE");

		txtINSBY = (Textbox) getFellow("txtINSBY");
		txtINSDT = (Textbox) getFellow("txtINSDT");
		txtUPBY = (Textbox) getFellow("txtUPBY");
		txtUPDT = (Textbox) getFellow("txtUPDT");

		div1 = (Div) getFellow("div1");

		btnExit = (Button) getFellow("btnExit");
		btnBack = (Button) getFellow("btnBack");
		btnSave = (Button) getFellow("btnSave");
		btnDelete = (Button) getFellow("btnDelete");
		btnAdd = (Button) getFellow("btnAdd");
	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(cmbACCT_TYPE);
		addEnterIndex(txtACCT_ID);
		addEnterIndex(btnAdd);
		addEnterIndex(txtACCT_NAME);
		addEnterIndex(btnSave);

	}

	@Override
	public void formInit() {
		try {
			cmbACCT_TYPE.setSelectedIndex(0);
			clearData();
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void clearData() {
		div1.setVisible(false);
		cmbACCT_TYPE.setDisabled(false);
		cmbACCT_TYPE.focus();
		txtACCT_ID.setValue("");
		txtACCT_ID.setReadonly(false);
		txtACCT_NAME.setValue("");

		txtINSBY.setValue("");
		txtINSDT.setValue("");
		txtUPBY.setValue("");
		txtUPDT.setValue("");

		btnExit.setVisible(true);
		btnBack.setVisible(false);
		btnSave.setVisible(false);
		btnDelete.setVisible(false);
		btnAdd.setVisible(true);

	}

	public void onOK() {
		try {
			if (focusObj == txtACCT_ID) {
				if (!txtACCT_ID.isReadonly()) {
					if (Fnc.isEmpty(txtACCT_ID.getValue())) {
						super.onOK();// จะเลื่อนไปปุ่ม btnAdd ถ้า visible อยู่
					} else if (read_record(txtACCT_ID.getValue().trim())) {
						txtACCT_NAME.focus();
					} else {
						popupData();
					}
				}

			} else {
				super.onOK();
			}
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void onClick_Back() {
		clearData();
	}

	private void validateData() throws Exception {

		if (txtACCT_ID.getValue().equals("")) {
			throw new Exception("ระบุช่อง \"" + txtACCT_ID.getTooltiptext() + "\" ");
		}
		if (txtACCT_NAME.getValue().equals("")) {
			throw new Exception("ระบุช่อง \"" + txtACCT_NAME.getTooltiptext() + "\" ");
		}

		if (Fnc.isEmpty(cmbACCT_TYPE.getValue())) {
			throw new Exception("ระบุช่อง \"" + cmbACCT_TYPE.getTooltiptext() + "\" ไม่ถูกต้อง");
		}

	}

	public void onClick_Save() {

		boolean newrec = true;
		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				validateData();

				TboACCT_NO acctno = new TboACCT_NO();

				acctno.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				acctno.setACCT_ID(txtACCT_ID.getValue().trim());

				if (TbfACCT_NO.find(dbc, acctno)) {
					newrec = false;
				}

				acctno.setACCT_NAME(txtACCT_NAME.getValue());
				acctno.setACCT_TYPE(Fnc.getIntFromStr(ZkUtil.getSelectItemValueComboBox(cmbACCT_TYPE)));
				acctno.setUPBY(this.getLoginBean().getUSER_ID());
				acctno.setUPDT(FnDate.getTodaySqlDateTime());

				if (newrec) {

					acctno.setINSBY(this.getLoginBean().getUSER_ID());
					acctno.setINSDT(FnDate.getTodaySqlDateTime());

					if (!TbfACCT_NO.insert(dbc, acctno)) {
						throw new Exception("เพิ่มไม่ได้");
					}

				} else {
					if (!TbfACCT_NO.update(dbc, acctno, "")) {
						throw new Exception("ปรับปรุงไม่ได้");
					}
				}

				dbc.commit();
			}

			Msg.info2("บันทึกเรียบร้อย", this, "clearData");

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void onClick_Delete() {
		Msg.confirm2(Labels.getLabel("comm.label.deleteComfirm") + " ?", this, "doOnClick_Delete");
	}

	public void doOnClick_Delete() {
		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				TboACCT_NO acctno = new TboACCT_NO();
				acctno.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				acctno.setACCT_ID(txtACCT_ID.getValue().trim());
				if (!TbfACCT_NO.delete(dbc, acctno)) {
					throw new Exception("ไม่สามารถลบข้อมูลได้");
				}

				dbc.commit();
			}

			Msg.info2("ลบข้อมูลเรียบร้อยแล้ว", this, "clearData");

		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void onClick_btnAdd() {
		try {
			if (Fnc.isEmpty(cmbACCT_TYPE.getValue())) {
				throw new Exception("ยังไม่เลือก :" + cmbACCT_TYPE.getTooltiptext());
			}
			Msg.inputbox("ใส่รหัสใหม่", 200, txtACCT_ID.getMaxlength(), this, "doOnClick_btnAdd", null);// trick ตัวอย่าง
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void doOnClick_btnAdd(String acct_id) {
		try {
			System.out.println(acct_id);
			if (!read_record(acct_id)) {
				btnExit.setVisible(false);
				btnBack.setVisible(true);
				btnSave.setVisible(true);
				btnDelete.setVisible(false);
				btnAdd.setVisible(false);
				div1.setVisible(true);

				cmbACCT_TYPE.setDisabled(true);
				txtACCT_ID.setReadonly(true);
				txtACCT_ID.setValue(acct_id);
				txtACCT_NAME.focus();
			}
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void popupData() {
		if (!txtACCT_ID.isReadonly()) {
			FfACCT_NO.popup("", this.getLoginBean(), this, "doPopupData");
		}
	}

	public void doPopupData(String acctno) {
		try {
			read_record(acctno);
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	private boolean read_record(String acct_id) throws Exception {
		boolean res = false;

		TboACCT_NO acctno = new TboACCT_NO();
		acctno.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		acctno.setACCT_ID(acct_id);

		if (TbfACCT_NO.find(acctno)) {

			txtACCT_ID.setValue(acctno.getACCT_ID());
			txtACCT_NAME.setValue(acctno.getACCT_NAME()); // ป้องกันตัวเล็กใหญ่
			ZkUtil.setSelectItemComboBoxByValue(cmbACCT_TYPE, acctno.getACCT_TYPE() + "");

			txtINSBY.setValue(acctno.getINSBY());
			txtINSDT.setValue(FnDate.displayDateTimeString(acctno.getINSDT()));
			txtUPBY.setValue(acctno.getUPBY());
			txtUPDT.setValue(FnDate.displayDateTimeString(acctno.getUPDT()));

			btnExit.setVisible(false);
			btnBack.setVisible(true);
			btnSave.setVisible(true);
			btnDelete.setVisible(true);
			btnAdd.setVisible(false);
			cmbACCT_TYPE.setDisabled(true);
			txtACCT_ID.setReadonly(true);
			div1.setVisible(true);

			res = true;
		}

		return res;
	}

}

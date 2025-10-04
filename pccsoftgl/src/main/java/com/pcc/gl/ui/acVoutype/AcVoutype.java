package com.pcc.gl.ui.acVoutype;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.pcc.gl.find.FfACCT_VOU_TYPE;
import com.pcc.gl.tbf.TbfACCT_VOU_TYPE;
import com.pcc.gl.tbo.TboACCT_VOU_TYPE;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;

public class AcVoutype extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Textbox txtVOU_TYPE;
	public Textbox txtVOU_NAME;
	public Checkbox chkUSE_PV;
	public Checkbox chkUSE_RV;

	private Textbox txtUPBY, txtUPDT;

	public Div div1;

	public Button btnExit, btnBack, btnSave, btnDelete, btnAdd;

	@Override
	public void setFormObj() {
		txtVOU_TYPE = (Textbox) getFellow("txtVOU_TYPE");
		txtVOU_NAME = (Textbox) getFellow("txtVOU_NAME");
		chkUSE_PV = (Checkbox) getFellow("chkUSE_PV");
		chkUSE_RV = (Checkbox) getFellow("chkUSE_RV");

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
		addEnterIndex(txtVOU_TYPE);
		addEnterIndex(btnAdd);
		addEnterIndex(txtVOU_NAME);
		addEnterIndex(btnSave);

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
		div1.setVisible(false);
		txtVOU_TYPE.setValue("");
		txtVOU_TYPE.setReadonly(false);
		txtVOU_TYPE.focus();
		txtVOU_NAME.setValue("");
		chkUSE_PV.setChecked(false);
		chkUSE_RV.setChecked(false);

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
			if (focusObj == txtVOU_TYPE) {
				if (!txtVOU_TYPE.isReadonly()) {
					if (Fnc.isEmpty(txtVOU_TYPE.getValue())) {
						super.onOK();// จะเลื่อนไปปุ่ม btnAdd ถ้า visible อยู่
					} else if (read_record(txtVOU_TYPE.getValue().trim())) {
						super.onOK();
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

		if (txtVOU_TYPE.getValue().equals("")) {
			throw new Exception("ระบุช่อง \"" + txtVOU_TYPE.getTooltiptext() + "\" ");
		}
		if (txtVOU_NAME.getValue().equals("")) {
			throw new Exception("ระบุช่อง \"" + txtVOU_NAME.getTooltiptext() + "\" ");
		}

	}

	public void onClick_Save() {

		boolean newrec = true;
		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				validateData();

				TboACCT_VOU_TYPE acct_type = new TboACCT_VOU_TYPE();

				acct_type.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				acct_type.setVOU_TYPE(txtVOU_TYPE.getValue().trim());

				if (TbfACCT_VOU_TYPE.find(dbc, acct_type)) {
					newrec = false;
				}

				acct_type.setVOU_NAME(txtVOU_NAME.getValue());
				acct_type.setUSE_PV(chkUSE_PV.isChecked() ? 1 : 0);
				acct_type.setUSE_RV(chkUSE_RV.isChecked() ? 1 : 0);
				acct_type.setUPBY(this.getLoginBean().getUSER_ID());
				acct_type.setUPDT(FnDate.getTodaySqlDateTime());

				if (newrec) {

					if (!TbfACCT_VOU_TYPE.insert(dbc, acct_type)) {
						throw new Exception("เพิ่มไม่ได้");
					}

				} else {
					if (!TbfACCT_VOU_TYPE.update(dbc, acct_type, "")) {
						throw new Exception("ปรับปรุงไม่ได้");
					}
				}

				dbc.commit();
			}

			clearData();

			Msg.info("บันทึกเรียบร้อย");
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void onClick_Delete() {

		Msg.confirm(Labels.getLabel("comm.label.deleteComfirm") + " ?", "?", (event1) -> {
			if (Messagebox.Button.YES.equals(event1.getButton())) {
				try {
					TboACCT_VOU_TYPE acct_type = new TboACCT_VOU_TYPE();
					acct_type.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
					acct_type.setVOU_TYPE(txtVOU_TYPE.getValue().trim());
					if (!TbfACCT_VOU_TYPE.delete(acct_type)) {
						throw new Exception("ไม่สามารถลบข้อมูลได้");
					}
					clearData();
					Msg.info("ลบข้อมูลเรียบร้อยแล้ว");
				} catch (Exception e) {
					Msg.error(e);
				}
			}
		});

	}

	public void onClick_btnAdd() {
		Msg.inputbox("ใส่รหัสใหม่", 200, txtVOU_TYPE.getMaxlength(), this, "doOnClick_btnAdd", null);
	}

	public void doOnClick_btnAdd(String vou_type) {
		try {
			System.out.println(vou_type);
			if (!read_record(vou_type)) {
				btnExit.setVisible(false);
				btnBack.setVisible(true);
				btnSave.setVisible(true);
				btnDelete.setVisible(false);
				btnAdd.setVisible(false);
				div1.setVisible(true);

				txtVOU_TYPE.setReadonly(true);
				txtVOU_TYPE.setValue(vou_type.trim());
				txtVOU_NAME.focus();
			}
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void popupData() {
		if (!txtVOU_TYPE.isReadonly()) {
			FfACCT_VOU_TYPE.popup("", this.getLoginBean(), this, "doPopupData");
		}
	}

	public void doPopupData(String vou_type) {
		try {
			read_record(vou_type);
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	private boolean read_record(String vou_type) throws Exception {
		boolean res = false;

		TboACCT_VOU_TYPE acct_type = new TboACCT_VOU_TYPE();

		acct_type.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		acct_type.setVOU_TYPE(vou_type);

		if (TbfACCT_VOU_TYPE.find(acct_type)) {

			txtVOU_TYPE.setValue(acct_type.getVOU_TYPE());
			txtVOU_NAME.setValue(acct_type.getVOU_NAME()); // ป้องกันตัวเล็กใหญ่
			chkUSE_PV.setChecked(acct_type.getUSE_PV() == 1);
			chkUSE_RV.setChecked(acct_type.getUSE_RV() == 1);

			txtUPBY.setValue(acct_type.getUPBY());
			txtUPDT.setValue(FnDate.displayDateTimeString(acct_type.getUPDT()));

			btnExit.setVisible(false);
			btnBack.setVisible(true);
			btnSave.setVisible(true);
			btnDelete.setVisible(true);
			btnAdd.setVisible(false);
			txtVOU_TYPE.setReadonly(true);
			div1.setVisible(true);

			res = true;
		}

		return res;
	}

}

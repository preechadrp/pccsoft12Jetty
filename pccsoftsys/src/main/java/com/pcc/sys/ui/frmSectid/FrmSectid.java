package com.pcc.sys.ui.frmSectid;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.pcc.sys.find.FfFSECTION;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.tbf.TbfFSECTION;
import com.pcc.sys.tbo.TboFSECTION;

public class FrmSectid extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Textbox txtSECT_ID;
	public Textbox txtSECT_NAME;

	public Div div1;

	public Button btnExit, btnBack, btnSave, btnDelete, btnAdd;

	@Override
	public void setFormObj() {
		txtSECT_ID = (Textbox) getFellow("txtSECT_ID");
		txtSECT_NAME = (Textbox) getFellow("txtSECT_NAME");

		div1 = (Div) getFellow("div1");

		btnExit = (Button) getFellow("btnExit");
		btnBack = (Button) getFellow("btnBack");
		btnSave = (Button) getFellow("btnSave");
		btnDelete = (Button) getFellow("btnDelete");
		btnAdd = (Button) getFellow("btnAdd");
	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(txtSECT_ID);
		addEnterIndex(btnAdd);
		addEnterIndex(txtSECT_NAME);
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
		txtSECT_ID.setValue("");
		txtSECT_ID.setReadonly(false);
		txtSECT_ID.focus();
		txtSECT_NAME.setValue("");

		btnExit.setVisible(true);
		btnBack.setVisible(false);
		btnSave.setVisible(false);
		btnDelete.setVisible(false);
		btnAdd.setVisible(true);

	}

	public void onOK() {
		try {
			if (focusObj == txtSECT_ID) {
				if (!txtSECT_ID.isReadonly()) {
					if (Fnc.isEmpty(txtSECT_ID.getValue())) {
						super.onOK();//จะเลื่อนไปปุ่ม btnAdd ถ้า visible อยู่
					} else if (read_record(txtSECT_ID.getValue().trim())) {
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

		if (txtSECT_ID.getValue().equals("")) {
			throw new Exception("ระบุช่อง \"" + txtSECT_ID.getTooltiptext() + "\" ");
		}
		if (txtSECT_NAME.getValue().equals("")) {
			throw new Exception("ระบุช่อง \"" + txtSECT_NAME.getTooltiptext() + "\" ");
		}

	}

	public void onClick_Save() {

		boolean newrec = true;
		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				validateData();

				//== FUSER_Table
				TboFSECTION fsect = new TboFSECTION();

				fsect.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				fsect.setSECT_ID(txtSECT_ID.getValue());

				if (TbfFSECTION.find(dbc, fsect)) {
					newrec = false;
				}

				fsect.setSECT_NAME(txtSECT_NAME.getValue());
				fsect.setUPBY(this.getLoginBean().getUSER_ID());
				fsect.setUPDT(FnDate.getTodaySqlDateTime());

				if (newrec) {

					if (!TbfFSECTION.insert(dbc, fsect)) {
						throw new Exception("เพิ่มไม่ได้");
					}

				} else {
					if (!TbfFSECTION.update(dbc, fsect, "")) {
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
					TboFSECTION fsect = new TboFSECTION();
					fsect.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
					fsect.setSECT_ID(txtSECT_ID.getValue().trim());
					if (!TbfFSECTION.delete(fsect)) {
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
		Msg.inputbox("ใส่รหัสใหม่", 200, txtSECT_ID.getMaxlength(), this, "doOnClick_btnAdd", null);
	}

	public void doOnClick_btnAdd(String sect_id) {
		try {
			System.out.println(sect_id);
			if (!read_record(sect_id)) {
				btnExit.setVisible(false);
				btnBack.setVisible(true);
				btnSave.setVisible(true);
				btnDelete.setVisible(false);
				btnAdd.setVisible(false);
				div1.setVisible(true);

				txtSECT_ID.setReadonly(true);
				txtSECT_ID.setValue(sect_id);
				txtSECT_NAME.focus();
			}
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void popupData() {
		if (!txtSECT_ID.isReadonly()) {
			FfFSECTION.popup("", this.getLoginBean(), this, "doPopupData");
		}
	}

	public void doPopupData(String sect_id) {
		try {
			if (!read_record(sect_id)) {
				txtSECT_ID.focus();
			}
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	private boolean read_record(String sect_id) throws Exception {
		boolean res = false;

		TboFSECTION fsect = new TboFSECTION();

		fsect.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		fsect.setSECT_ID(sect_id);

		if (TbfFSECTION.find(fsect)) {

			txtSECT_ID.setValue(fsect.getSECT_ID());
			txtSECT_NAME.setValue(fsect.getSECT_NAME()); // ป้องกันตัวเล็กใหญ่

			btnExit.setVisible(false);
			btnBack.setVisible(true);
			btnSave.setVisible(true);
			btnDelete.setVisible(true);
			btnAdd.setVisible(false);
			txtSECT_ID.setReadonly(true);
			div1.setVisible(true);

			res = true;
		}

		return res;
	}

}

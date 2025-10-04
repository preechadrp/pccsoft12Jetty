package com.pcc.sys.ui.FrmZipcode;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.pcc.sys.find.FfFPROVINCE;
import com.pcc.sys.find.FfFZIPCODE;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.tbf.TbfFPROVINCE;
import com.pcc.sys.tbf.TbfFZIPCODE;
import com.pcc.sys.tbo.TboFPROVINCE;
import com.pcc.sys.tbo.TboFZIPCODE;

public class FrmZipCode extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Textbox txtPROVIN_ID;
	public Textbox txtPROVIN_NAME;
	public Textbox txtZIPCODE;
	public Textbox txtDISTRICT_NAME;

	public Div div1;

	public Button btnExit, btnBack, btnSave, btnDelete, btnAdd;

	@Override
	public void setFormObj() {
		txtPROVIN_ID = (Textbox) getFellow("txtPROVIN_ID");
		txtPROVIN_NAME = (Textbox) getFellow("txtPROVIN_NAME");
		txtZIPCODE = (Textbox) getFellow("txtZIPCODE");
		txtDISTRICT_NAME = (Textbox) getFellow("txtDISTRICT_NAME");

		div1 = (Div) getFellow("div1");

		btnExit = (Button) getFellow("btnExit");
		btnBack = (Button) getFellow("btnBack");
		btnSave = (Button) getFellow("btnSave");
		btnDelete = (Button) getFellow("btnDelete");
		btnAdd = (Button) getFellow("btnAdd");
	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(txtPROVIN_ID);
		addEnterIndex(txtZIPCODE);
		addEnterIndex(btnAdd);
		addEnterIndex(txtDISTRICT_NAME);
		addEnterIndex(btnSave);

	}

	@Override
	public void formInit() {
		try {
			clearData();
		} catch (Exception e) {
			Msg.error(e.getMessage());
		}
	}

	private void clearData() {
		div1.setVisible(false);
		txtPROVIN_ID.setValue("");
		txtPROVIN_ID.setReadonly(false);
		txtPROVIN_ID.focus();
		txtPROVIN_NAME.setValue("");
		txtZIPCODE.setReadonly(false);
		txtZIPCODE.setValue("");
		txtDISTRICT_NAME.setValue("");

		btnExit.setVisible(true);
		btnBack.setVisible(false);
		btnSave.setVisible(false);
		btnDelete.setVisible(false);
		btnAdd.setVisible(true);

	}

	public void onOK() {
		try {
			if (focusObj == txtZIPCODE) {
				if (!txtZIPCODE.isReadonly()) {
					if (Fnc.isEmpty(txtZIPCODE.getValue())) {
						super.onOK();//จะเลื่อนไปปุ่ม btnAdd ถ้า visible อยู่
					} else if (read_record(txtZIPCODE.getValue().trim())) {
						super.onOK();
					} else {
						throw new Exception("ไม่พบข้อมูล");
					}
				}

			} else {
				super.onOK();
			}
		} catch (Exception e) {
			Msg.error(e.getMessage());
		}

	}

	public void onClick_Back() {
		clearData();
	}

	private void validateData() throws Exception {

		if (Fnc.isEmpty(txtPROVIN_ID.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtPROVIN_ID.getTooltiptext() + "\" ");
		}
		if (Fnc.isEmpty(txtZIPCODE.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtZIPCODE.getTooltiptext() + "\" ");
		}
		if (Fnc.isEmpty(txtDISTRICT_NAME.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtDISTRICT_NAME.getTooltiptext() + "\" ");
		}

	}

	public void onClick_Save() {

		boolean newrec = true;
		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				validateData();

				TboFZIPCODE fzip = new TboFZIPCODE();

				fzip.setPROVIN_ID(txtPROVIN_ID.getValue());
				fzip.setZIPCODE(txtZIPCODE.getValue());

				if (TbfFZIPCODE.find(dbc, fzip)) {
					newrec = false;
				}

				fzip.setDISTRICT_NAME(txtDISTRICT_NAME.getValue());

				if (newrec) {

					if (!TbfFZIPCODE.insert(dbc, fzip)) {
						throw new Exception("เพิ่มไม่ได้");
					}

				} else {
					if (!TbfFZIPCODE.update(dbc, fzip, "")) {
						throw new Exception("ปรับปรุงไม่ได้");
					}
				}

				dbc.commit();
			}

			clearData();

			Msg.info("บันทึกเรียบร้อย");
		} catch (Exception e) {
			Msg.error(e.getMessage());
		}

	}

	public void onClick_Delete() {
		Msg.confirm(Labels.getLabel("comm.label.deleteComfirm") + " ?", "?", (event1) -> {
			if (Messagebox.Button.YES.equals(event1.getButton())) {
				try {
					TboFZIPCODE fzip = new TboFZIPCODE();

					fzip.setPROVIN_ID(txtPROVIN_ID.getValue().trim());
					fzip.setZIPCODE(txtZIPCODE.getValue().trim());

					if (!TbfFZIPCODE.delete(fzip)) {
						throw new Exception("ไม่สามารถลบข้อมูลได้");
					}
					clearData();
					Msg.info("ลบข้อมูลเรียบร้อยแล้ว");
				} catch (Exception e) {
					Msg.error(e.getMessage());
				}
			}
		});
	}

	public void onClick_btnAdd() {
		try {
			if (Fnc.isEmpty(txtPROVIN_ID.getValue()) || Fnc.isEmpty(txtPROVIN_NAME.getValue())) {
				throw new Exception("ระบุช่อง \"" + txtPROVIN_ID.getTooltiptext() + "\" ");
			}
			Msg.inputbox("ใส่รหัสใหม่", 200, txtZIPCODE.getMaxlength(), this, "doOnClick_btnAdd", null);
		} catch (Exception e) {
			Msg.error(e.getMessage());
		}
	}

	public void doOnClick_btnAdd(String zipcode) {
		try {
			System.out.println(zipcode);
			if (!read_record(zipcode)) {
				btnExit.setVisible(false);
				btnBack.setVisible(true);
				btnSave.setVisible(true);
				btnDelete.setVisible(false);
				btnAdd.setVisible(false);
				div1.setVisible(true);

				txtPROVIN_ID.setReadonly(true);
				txtZIPCODE.setReadonly(true);
				txtZIPCODE.setValue(zipcode);
				txtDISTRICT_NAME.focus();
			}
		} catch (Exception e) {
			Msg.error(e.getMessage());
		}
	}

	public void onChange_Data(Component comp) {
		try {
			if (comp.getId().equals("txtPROVIN_ID")) {
				read_province(txtPROVIN_ID.getValue());
			}
		} catch (Exception e) {
			Msg.error(e.getMessage());
		}
	}

	public void popupProvince() {
		if (!txtPROVIN_ID.isReadonly()) {
			FfFPROVINCE.popup("", getLoginBean(), this, "doPopupProvince");
		}
	}

	public void doPopupProvince(String provin_id) {
		try {
			read_province(provin_id);
		} catch (Exception e) {
			Msg.error(e.getMessage());
		}
	}

	public void popupData() {
		try {
			if (!txtZIPCODE.isReadonly()) {

				if (Fnc.isEmpty(txtPROVIN_ID.getValue()) || Fnc.isEmpty(txtPROVIN_NAME.getValue())) {
					throw new Exception("ระบุช่อง \"" + txtPROVIN_ID.getTooltiptext() + "\" ");
				}

				FfFZIPCODE.popup(txtPROVIN_ID.getValue().trim(), this.getLoginBean(), this, "doPopupData");
			}
		} catch (Exception e) {
			Msg.error(e.getMessage());
		}
	}

	public void doPopupData(String zipcode) {
		try {
			read_record(zipcode);
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	private boolean read_record(String zipcode) throws Exception {
		boolean res = false;

		TboFZIPCODE fprov = new TboFZIPCODE();

		fprov.setPROVIN_ID(txtPROVIN_ID.getValue());
		fprov.setZIPCODE(zipcode);

		if (TbfFZIPCODE.find(fprov)) {

			txtZIPCODE.setValue(fprov.getZIPCODE());
			txtDISTRICT_NAME.setValue(fprov.getDISTRICT_NAME()); // ป้องกันตัวเล็กใหญ่

			btnExit.setVisible(false);
			btnBack.setVisible(true);
			btnSave.setVisible(true);
			btnDelete.setVisible(true);
			btnAdd.setVisible(false);
			txtPROVIN_ID.setReadonly(true);
			txtZIPCODE.setReadonly(true);
			div1.setVisible(true);

			res = true;
		}

		return res;
	}

	public boolean read_province(String provin_id) throws Exception {
		boolean ret = false;

		TboFPROVINCE prov = new TboFPROVINCE();

		prov.setPROVIN_ID(Fnc.getIntFromStr(provin_id) );

		if (TbfFPROVINCE.find(prov)) {
			ret = true;
			txtPROVIN_ID.setValue(prov.getPROVIN_ID()+"");
			txtPROVIN_NAME.setValue(prov.getPROVIN_NAME());
		} else {
			txtPROVIN_NAME.setValue("");
		}

		return ret;
	}

}

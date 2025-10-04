package com.pcc.sys.ui.FrmProvince;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.pcc.sys.find.FfFPROVINCE;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.tbf.TbfFPROVINCE;
import com.pcc.sys.tbm.TbmFPROVINCE;
import com.pcc.sys.tbo.TboFPROVINCE;

public class FrmProvince extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Textbox txtPROVIN_ID;
	public Textbox txtPROVIN_NAME;

	public Div div1;

	public Button btnExit, btnBack, btnSave, btnDelete, btnAdd;

	@Override
	public void setFormObj() {
		txtPROVIN_ID = (Textbox) getFellow("txtPROVIN_ID");
		txtPROVIN_NAME = (Textbox) getFellow("txtPROVIN_NAME");

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
		addEnterIndex(btnAdd);
		addEnterIndex(txtPROVIN_NAME);
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
		txtPROVIN_ID.setValue("");
		txtPROVIN_ID.setReadonly(false);
		txtPROVIN_ID.focus();
		txtPROVIN_NAME.setValue("");

		btnExit.setVisible(true);
		btnBack.setVisible(false);
		btnSave.setVisible(false);
		btnDelete.setVisible(false);
		btnAdd.setVisible(true);

	}

	public void onOK() {
		try {
			if (focusObj == txtPROVIN_ID) {
				if (!txtPROVIN_ID.isReadonly()) {
					if (Fnc.isEmpty(txtPROVIN_ID.getValue())) {
						super.onOK();//จะเลื่อนไปปุ่ม btnAdd ถ้า visible อยู่
					} else if (read_record(Fnc.getIntFromStr(txtPROVIN_ID.getValue()))) {
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

		if (txtPROVIN_ID.getValue().equals("")) {
			throw new Exception("ระบุช่อง \"" + txtPROVIN_ID.getTooltiptext() + "\" ");
		}
		if (txtPROVIN_NAME.getValue().equals("")) {
			throw new Exception("ระบุช่อง \"" + txtPROVIN_NAME.getTooltiptext() + "\" ");
		}

	}

	public void onClick_Save() {

		boolean newrec = true;
		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				validateData();

				//== FUSER_Table
				TboFPROVINCE fprov = new TboFPROVINCE();

				fprov.setPROVIN_ID(Fnc.getIntFromStr(txtPROVIN_ID.getValue()));

				if (TbfFPROVINCE.find(dbc, fprov)) {
					newrec = false;
				}

				fprov.setPROVIN_NAME(txtPROVIN_NAME.getValue());

				if (newrec) {

					if (!TbfFPROVINCE.insert(dbc, fprov)) {
						throw new Exception("เพิ่มไม่ได้");
					}

				} else {
					if (!TbfFPROVINCE.update(dbc, fprov, "")) {
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
					TboFPROVINCE fprov = new TboFPROVINCE();
					fprov.setPROVIN_ID(Fnc.getIntFromStr(txtPROVIN_ID.getValue().trim()));
					if (!TbfFPROVINCE.delete(fprov)) {
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
		Msg.confirm2(Labels.getLabel("comm.label.ComfirmNew") + " ?", this, "doOnClick_btnAdd");
	}

	public void doOnClick_btnAdd() {
		try {
			int provin_id = TbmFPROVINCE.getMax_provin_id() + 1;

			TboFPROVINCE fprov = new TboFPROVINCE();
			fprov.setPROVIN_ID(provin_id);
			fprov.setPROVIN_NAME("-");
			TbfFPROVINCE.insert(fprov);

			System.out.println(provin_id);
			if (!read_record(provin_id)) {
				btnExit.setVisible(false);
				btnBack.setVisible(true);
				btnSave.setVisible(true);
				btnDelete.setVisible(false);
				btnAdd.setVisible(false);
				div1.setVisible(true);

				txtPROVIN_ID.setReadonly(true);
				txtPROVIN_ID.setValue(provin_id + "");
				txtPROVIN_NAME.focus();
			}
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void popupData() {
		if (!txtPROVIN_ID.isReadonly()) {//ต้องใส่กรณีต้องตรวจสอบ
			FfFPROVINCE.popup("", getLoginBean(), this, "doPopupData");
		}
	}

	public void doPopupData(int provin_id) {
		try {
			read_record(provin_id);
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	private boolean read_record(int provin_id) throws Exception {
		boolean res = false;

		TboFPROVINCE fprov = new TboFPROVINCE();
		fprov.setPROVIN_ID(provin_id);

		if (TbfFPROVINCE.find(fprov)) {

			txtPROVIN_ID.setValue(fprov.getPROVIN_ID() + "");
			txtPROVIN_NAME.setValue(fprov.getPROVIN_NAME()); // ป้องกันตัวเล็กใหญ่

			btnExit.setVisible(false);
			btnBack.setVisible(true);
			btnSave.setVisible(true);
			btnDelete.setVisible(true);
			btnAdd.setVisible(false);
			txtPROVIN_ID.setReadonly(true);
			div1.setVisible(true);

			res = true;
		}

		return res;
	}

}

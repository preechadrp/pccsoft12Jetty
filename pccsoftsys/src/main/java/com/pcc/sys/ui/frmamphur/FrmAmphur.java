package com.pcc.sys.ui.frmamphur;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.pcc.sys.find.FfAMPHURID;
import com.pcc.sys.find.FfFPROVINCE;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.tbf.TbfFAMPHUR;
import com.pcc.sys.tbf.TbfFPROVINCE;
import com.pcc.sys.tbm.TbmFAMPHUR;
import com.pcc.sys.tbo.TboFAMPHUR;
import com.pcc.sys.tbo.TboFPROVINCE;

public class FrmAmphur extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Textbox txtAMPHURID;
	public Textbox txtAMPHURNAME;
	public Textbox txtZIPCODE;
	public Textbox txtPROVIN_ID;
	public Textbox txtPROVIN_NAME;

	public Div div1;

	public Button btnExit, btnBack, btnSave, btnDelete, btnAdd;

	@Override
	public void setFormObj() {
		txtAMPHURID = (Textbox) getFellow("txtAMPHURID");
		txtAMPHURNAME = (Textbox) getFellow("txtAMPHURNAME");
		txtZIPCODE = (Textbox) getFellow("txtZIPCODE");
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
		addEnterIndex(txtAMPHURID);
		addEnterIndex(btnAdd);
		addEnterIndex(txtAMPHURNAME);
		addEnterIndex(txtZIPCODE);
		addEnterIndex(txtPROVIN_ID);
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
		txtAMPHURID.setValue("");
		txtAMPHURID.setReadonly(false);
		txtAMPHURID.focus();
		txtAMPHURNAME.setValue("");
		txtZIPCODE.setValue("");
		txtPROVIN_ID.setValue("");
		txtPROVIN_NAME.setValue("");

		btnExit.setVisible(true);
		btnBack.setVisible(false);
		btnSave.setVisible(false);
		btnDelete.setVisible(false);
		btnAdd.setVisible(true);

	}

	public void onOK() {
		try {
			if (focusObj == txtAMPHURID) {
				if (!txtAMPHURID.isReadonly()) {
					if (Fnc.isEmpty(txtAMPHURID.getValue())) {
						super.onOK();//จะเลื่อนไปปุ่ม btnAdd ถ้า visible อยู่
					} else if (read_record(Fnc.getIntFromStr(txtAMPHURID.getValue()))) {
						super.onOK();
					} else {
						popupData();
					}
				}
			} else if (focusObj == txtPROVIN_ID) {
				if (Fnc.isEmpty(txtPROVIN_ID.getValue())) {
					popupProvince();
				} else {
					super.onOK();
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

		if (txtAMPHURID.getValue().equals("")) {
			throw new Exception("ระบุช่อง \"" + txtAMPHURID.getTooltiptext() + "\" ");
		}
		if (txtAMPHURNAME.getValue().equals("")) {
			throw new Exception("ระบุช่อง \"" + txtAMPHURNAME.getTooltiptext() + "\" ");
		}
		if (txtPROVIN_ID.getValue().equals("")) {
			throw new Exception("ระบุช่อง \"" + txtPROVIN_ID.getTooltiptext() + "\" ");
		}
		if (Fnc.isEmpty(txtPROVIN_NAME.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtPROVIN_ID.getTooltiptext() + "\" ไม่ถูกต้อง ");
		}

	}

	public void onClick_Save() {

		boolean newrec = true;
		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				validateData();

				TboFAMPHUR famph = new TboFAMPHUR();

				famph.setAMPHURID(Fnc.getIntFromStr(txtAMPHURID.getValue()));

				if (TbfFAMPHUR.find(dbc, famph)) {
					newrec = false;
				}

				famph.setAMPHURNAME(txtAMPHURNAME.getValue());
				famph.setZIPCODE(txtZIPCODE.getValue());
				famph.setPROVIN_ID(Fnc.getIntFromStr(txtPROVIN_ID.getValue()));

				if (newrec) {

					if (!TbfFAMPHUR.insert(dbc, famph)) {
						throw new Exception("เพิ่มไม่ได้");
					}

				} else {
					if (!TbfFAMPHUR.update(dbc, famph, "")) {
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
					TboFAMPHUR famph = new TboFAMPHUR();
					famph.setAMPHURID(Fnc.getIntFromStr(txtAMPHURID.getValue().trim()));
					if (!TbfFAMPHUR.delete(famph)) {
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

		Msg.confirm(Labels.getLabel("comm.label.ComfirmNew") + " ?", "?", (even) -> {

			if (even.getButton() == org.zkoss.zul.Messagebox.Button.YES) {
				
				try {
					int amphur_id = TbmFAMPHUR.getMax_AMPHUR_ID() + 1;

					TboFAMPHUR famph = new TboFAMPHUR();
					famph.setAMPHURID(amphur_id);
					famph.setAMPHURNAME("-");
					famph.setZIPCODE("-");
					famph.setPROVIN_ID(0);
					TbfFAMPHUR.insert(famph);

					System.out.println(amphur_id);

					if (!read_record(amphur_id)) {
						btnExit.setVisible(false);
						btnBack.setVisible(true);
						btnSave.setVisible(true);
						btnDelete.setVisible(false);
						btnAdd.setVisible(false);
						div1.setVisible(true);

						txtAMPHURID.setReadonly(true);
						txtAMPHURID.setValue(amphur_id + "");
						txtAMPHURNAME.focus();
					}
				} catch (Exception e) {
					Msg.error(e);
				}
				
			}

		});

	}

	public void popupData() {
		try {

			if (!txtAMPHURID.isReadonly()) {
				FfAMPHURID.popup((event) -> {
					Integer amphurid = (Integer) event.getData();
					read_record(amphurid);
				});
			}

		} catch (Exception e) {
			Msg.error(e);
		}
	}

	private boolean read_record(int amphurid) throws Exception {
		boolean res = false;

		TboFAMPHUR famphur = new TboFAMPHUR();
		famphur.setAMPHURID(amphurid);

		if (TbfFAMPHUR.find(famphur)) {

			txtAMPHURID.setValue(famphur.getAMPHURID() + "");
			txtAMPHURNAME.setValue(famphur.getAMPHURNAME()); // ป้องกันตัวเล็กใหญ่
			txtZIPCODE.setValue(famphur.getZIPCODE());
			read_province(famphur.getPROVIN_ID());

			btnExit.setVisible(false);
			btnBack.setVisible(true);
			btnSave.setVisible(true);
			btnDelete.setVisible(true);
			btnAdd.setVisible(false);
			txtAMPHURID.setReadonly(true);
			div1.setVisible(true);

			res = true;
		}

		return res;
	}

	public void popupProvince() {
		FfFPROVINCE.popup("", getLoginBean(), this, "doPopupProvince");
	}

	public void doPopupProvince(int provin_id) {
		try {
			read_province(provin_id);
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public boolean read_province(int provin_id) throws Exception {
		boolean res = false;

		TboFPROVINCE fprov = new TboFPROVINCE();
		fprov.setPROVIN_ID(provin_id);

		if (TbfFPROVINCE.find(fprov)) {

			txtPROVIN_ID.setValue(fprov.getPROVIN_ID() + "");
			txtPROVIN_NAME.setValue(fprov.getPROVIN_NAME());

			res = true;
		}

		return res;
	}

}

package com.pcc.tk.ui.FrmTkZone;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Textbox;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.ZkUtil;
import com.pcc.tk.find.FfTKZONE;
import com.pcc.tk.lib.TkConst;
import com.pcc.tk.tbf.TbfTKZONE;
import com.pcc.tk.tbm.TbmTKZONE;
import com.pcc.tk.tbo.TboTKZONE;

public class FrmTkZone extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Button btnExit;
	public Button btnBack;
	public Button btnSave;
	public Button btnDelete;
	public Textbox txtZONEID;
	public Button btnAdd;
	public Div div1;
	public Textbox txtZONENAME;
	public Combobox cmbZONEHUB;
	public Textbox txtUPBY;
	public Textbox txtUPDT;

	@Override
	public void setFormObj() {
		btnExit = (Button) getFellow("btnExit");
		btnBack = (Button) getFellow("btnBack");
		btnSave = (Button) getFellow("btnSave");
		btnDelete = (Button) getFellow("btnDelete");
		txtZONEID = (Textbox) getFellow("txtZONEID");
		btnAdd = (Button) getFellow("btnAdd");
		div1 = (Div) getFellow("div1");
		txtZONENAME = (Textbox) getFellow("txtZONENAME");
		cmbZONEHUB = (Combobox) getFellow("cmbZONEHUB");
		txtUPBY = (Textbox) getFellow("txtUPBY");
		txtUPDT = (Textbox) getFellow("txtUPDT");

	}

	@Override
	public void addEnterIndex() {

		addEnterIndex(txtZONEID);
		addEnterIndex(btnAdd);
		addEnterIndex(txtZONENAME);
		addEnterIndex(cmbZONEHUB);
		//addEnterIndex(txtUPBY);
		//addEnterIndex(txtUPDT);

		//addEnterIndex(btnExit);
		//addEnterIndex(btnBack);
		addEnterIndex(btnSave);
		addEnterIndex(btnDelete);
	}

	@Override
	public void formInit() {
		try {
			for (String[] zoneH : TkConst.zonehub) {
				ZkUtil.appendItemToComboBox(cmbZONEHUB, zoneH[0], zoneH[1]);
			}

			clearData();
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void clearData() {
		div1.setVisible(false);

		txtZONEID.setValue("");
		txtZONENAME.setValue("");
		cmbZONEHUB.setSelectedIndex(-1);
		txtUPBY.setValue("");
		txtUPDT.setValue("");

		txtZONEID.setReadonly(false);
		txtZONEID.focus();

		btnExit.setVisible(true);
		btnBack.setVisible(false);
		btnSave.setVisible(false);
		btnDelete.setVisible(false);
		btnAdd.setVisible(true);
	}

	public void onOK() {
		try {
			if (focusObj == txtZONEID) {
				if (!txtZONEID.isReadonly()) {
					if (Fnc.isEmpty(txtZONEID.getValue()) || txtZONEID.getValue().equals("0")) {
						super.onOK();// จะเลื่อนไปปุ่ม btnAdd ถ้า visible อยู่
					} else if (read_record(Fnc.getIntFromStr(txtZONEID.getValue().trim()))) {
						txtZONENAME.focus();
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

	public void onClick_btnAdd() {
		Msg.confirm2(Labels.getLabel("comm.label.ComfirmNew") + " ?", this, "doOnClick_btnAdd");
	}

	public void doOnClick_btnAdd() {
		try {

			TboTKZONE table1 = new TboTKZONE();

			table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());

			int newcode = TbmTKZONE.getMaxCode(this.getLoginBean().getCOMP_CDE()) + 1;
			table1.setZONEID(newcode);

			table1.setZONENAME("??");
			table1.setZONEHUB("");
			table1.setUPDBY(this.getLoginBean().getUSER_ID());
			table1.setUPDDTE(FnDate.getTodaySqlDateTime());

			TbfTKZONE.insert(table1);

			read_record(newcode);
			txtZONENAME.focus();

		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}
	}

	public boolean read_record(int zoneid) throws Exception {
		boolean res = false;

		TboTKZONE table1 = new TboTKZONE();

		table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		table1.setZONEID(zoneid);

		if (TbfTKZONE.find(table1)) {
			txtZONEID.setValue(table1.getZONEID() + "");
			txtZONENAME.setValue(table1.getZONENAME());
			ZkUtil.setSelectItemComboBoxByValue(cmbZONEHUB, table1.getZONEHUB());
			txtUPBY.setValue(table1.getUPDBY());
			txtUPDT.setValue(FnDate.displayDateTimeString(table1.getUPDDTE()));

			btnExit.setVisible(false);
			btnBack.setVisible(true);
			btnSave.setVisible(true);
			btnDelete.setVisible(true);
			btnAdd.setVisible(false);
			txtZONEID.setReadonly(true);
			div1.setVisible(true);

			res = true;
		}

		return res;
	}

	private void validateData() throws Exception {

		if (Fnc.isEmpty(txtZONEID.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtZONEID.getTooltiptext() + "\" ");
		}
		if (Fnc.isEmpty(txtZONENAME.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtZONENAME.getTooltiptext() + "\" ");
		}
		if (Fnc.isEmpty(ZkUtil.getSelectItemValueComboBox(cmbZONEHUB))) {
			throw new Exception("ระบุช่อง \"" + cmbZONEHUB.getTooltiptext() + "\" ");
		}

	}

	public void onClick_Save() {
		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				validateData();

				TboTKZONE table1 = new TboTKZONE();

				table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				table1.setZONEID(Fnc.getIntFromStr(txtZONEID.getValue()));

				if (TbfTKZONE.find(dbc, table1)) {
					table1.setZONENAME(txtZONENAME.getValue());
					table1.setZONEHUB(ZkUtil.getSelectItemValueComboBox(cmbZONEHUB));

					table1.setUPDBY(this.getLoginBean().getUSER_ID());
					table1.setUPDDTE(FnDate.getTodaySqlDateTime());

					TbfTKZONE.update(dbc, table1);
				}

				dbc.commit();
			}

			Msg.info2("บันทึกเรียบร้อย", this, "clearData");

		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void popupData() {
		if (!txtZONEID.isReadonly()) {
			FfTKZONE.popup(this.getLoginBean(), this, "doPopupData");
		}
	}

	public void doPopupData(int zoneid) {
		try {
			if (read_record(zoneid)) {
				txtZONENAME.focus();
			}
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

				TboTKZONE table1 = new TboTKZONE();

				table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				table1.setZONEID(Fnc.getIntFromStr(txtZONEID.getValue()));

				if (TbfTKZONE.find(dbc, table1)) {
					TbfTKZONE.delete(dbc, table1);
				}

				dbc.commit();
			}

			Msg.info2("ลบข้อมูลเรียบร้อยแล้ว", this, "clearData");

		} catch (Exception e) {
			Msg.error(e);
		}
	}

}

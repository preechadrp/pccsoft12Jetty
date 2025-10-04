package com.pcc.tk.ui.FrmTkLawstat;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Textbox;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.tk.find.FfTKLAWSTAT;
import com.pcc.tk.tbf.TbfTKLAWSTAT;
import com.pcc.tk.tbm.TbmTKLAWSTAT;
import com.pcc.tk.tbo.TboTKLAWSTAT;

public class FrmTkLawstat extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Button btnExit;
	public Button btnBack;
	public Button btnSave;
	public Button btnDelete;
	public Textbox txtLAWSTATID;
	public Button btnAdd;
	public Div div1;
	public Textbox txtLAWSTATNAME;
	public Textbox txtUPBY;
	public Textbox txtUPDT;

	@Override
	public void setFormObj() {
		btnExit = (Button) getFellow("btnExit");
		btnBack = (Button) getFellow("btnBack");
		btnSave = (Button) getFellow("btnSave");
		btnDelete = (Button) getFellow("btnDelete");
		txtLAWSTATID = (Textbox) getFellow("txtLAWSTATID");
		btnAdd = (Button) getFellow("btnAdd");
		div1 = (Div) getFellow("div1");
		txtLAWSTATNAME = (Textbox) getFellow("txtLAWSTATNAME");
		txtUPBY = (Textbox) getFellow("txtUPBY");
		txtUPDT = (Textbox) getFellow("txtUPDT");

	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(txtLAWSTATID);
		addEnterIndex(btnAdd);
		addEnterIndex(txtLAWSTATNAME);
		// addEnterIndex(txtUPBY);
		// addEnterIndex(txtUPDT);
		addEnterIndex(btnSave);
		addEnterIndex(btnExit);
		addEnterIndex(btnBack);
		addEnterIndex(btnDelete);

	}

	@Override
	public void formInit() {
		try {
			clearData();
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void clearData() {
		div1.setVisible(false);

		txtLAWSTATID.setValue("");
		txtLAWSTATNAME.setValue("");
		txtUPBY.setValue("");
		txtUPDT.setValue("");

		txtLAWSTATID.setReadonly(false);
		txtLAWSTATID.focus();

		btnExit.setVisible(true);
		btnBack.setVisible(false);
		btnSave.setVisible(false);
		btnDelete.setVisible(false);
		btnAdd.setVisible(true);
	}

	public void onOK() {
		try {
			if (focusObj == txtLAWSTATID) {
				if (!txtLAWSTATID.isReadonly()) {
					if (Fnc.isEmpty(txtLAWSTATID.getValue()) || txtLAWSTATID.getValue().equals("0")) {
						super.onOK();// จะเลื่อนไปปุ่ม btnAdd ถ้า visible อยู่
					} else if (read_record(Fnc.getIntFromStr(txtLAWSTATID.getValue().trim()))) {
						txtLAWSTATNAME.focus();
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

			TboTKLAWSTAT table1 = new TboTKLAWSTAT();

			table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());

			int newcode = TbmTKLAWSTAT.getMaxCode(this.getLoginBean().getCOMP_CDE()) + 1;
			table1.setLAWSTATID(newcode);

			table1.setLAWSTATNAME("??");
			table1.setUPDBY(this.getLoginBean().getUSER_ID());
			table1.setUPDDTE(FnDate.getTodaySqlDateTime());

			TbfTKLAWSTAT.insert(table1);

			read_record(newcode);
			txtLAWSTATNAME.focus();

		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}
	}

	public boolean read_record(int lawstatid) throws Exception {
		boolean res = false;

		TboTKLAWSTAT table1 = new TboTKLAWSTAT();

		table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		table1.setLAWSTATID(lawstatid);

		if (TbfTKLAWSTAT.find(table1)) {
			txtLAWSTATID.setValue(table1.getLAWSTATID() + "");
			txtLAWSTATNAME.setValue(table1.getLAWSTATNAME());

			txtUPBY.setValue(table1.getUPDBY());
			txtUPDT.setValue(FnDate.displayDateTimeString(table1.getUPDDTE()));

			btnExit.setVisible(false);
			btnBack.setVisible(true);
			btnSave.setVisible(true);
			btnDelete.setVisible(true);
			btnAdd.setVisible(false);
			txtLAWSTATID.setReadonly(true);
			div1.setVisible(true);

			res = true;
		}

		return res;
	}

	private void validateData() throws Exception {

		if (Fnc.isEmpty(txtLAWSTATID.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtLAWSTATID.getTooltiptext() + "\" ");
		}
		if (Fnc.isEmpty(txtLAWSTATNAME.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtLAWSTATNAME.getTooltiptext() + "\" ");
		}

	}

	public void onClick_Save() {
		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				validateData();

				TboTKLAWSTAT table1 = new TboTKLAWSTAT();

				table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				table1.setLAWSTATID(Fnc.getIntFromStr(txtLAWSTATID.getValue()));

				if (TbfTKLAWSTAT.find(dbc, table1)) {
					table1.setLAWSTATNAME(txtLAWSTATNAME.getValue());

					table1.setUPDBY(this.getLoginBean().getUSER_ID());
					table1.setUPDDTE(FnDate.getTodaySqlDateTime());

					TbfTKLAWSTAT.update(dbc, table1);
				}

				dbc.commit();
			}

			Msg.info2("บันทึกเรียบร้อย", this, "clearData");

		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void popupData() {
		if (!txtLAWSTATID.isReadonly()) {
			FfTKLAWSTAT.popup(this.getLoginBean(), this, "doPopupData");
		}
	}

	public void doPopupData(int lawstatid) {
		try {
			if (read_record(lawstatid)) {
				txtLAWSTATNAME.focus();
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

				TboTKLAWSTAT table1 = new TboTKLAWSTAT();

				table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				table1.setLAWSTATID(Fnc.getIntFromStr(txtLAWSTATID.getValue()));

				if (TbfTKLAWSTAT.find(dbc, table1)) {
					TbfTKLAWSTAT.delete(dbc, table1);
				}

				dbc.commit();
			}

			Msg.info2("ลบข้อมูลเรียบร้อยแล้ว", this, "clearData");

		} catch (Exception e) {
			Msg.error(e);
		}
	}

}

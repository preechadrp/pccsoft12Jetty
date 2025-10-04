package com.pcc.tk.ui.FrmTkJobstat;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Textbox;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.tk.find.FfTKJOBSTAT;
import com.pcc.tk.tbf.TbfTKJOBSTAT;
import com.pcc.tk.tbm.TbmTKJOBSTAT;
import com.pcc.tk.tbo.TboTKJOBSTAT;

public class FrmTkJobstat extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Button btnExit;
	public Button btnBack;
	public Button btnSave;
	public Button btnDelete;
	public Textbox txtJOBSTATID;
	public Button btnAdd;
	public Div div1;
	public Textbox txtJOBSTATNAME;
	public Textbox txtUPBY;
	public Textbox txtUPDT;

	@Override
	public void setFormObj() {
		btnExit = (Button) getFellow("btnExit");
		btnBack = (Button) getFellow("btnBack");
		btnSave = (Button) getFellow("btnSave");
		btnDelete = (Button) getFellow("btnDelete");
		txtJOBSTATID = (Textbox) getFellow("txtJOBSTATID");
		btnAdd = (Button) getFellow("btnAdd");
		div1 = (Div) getFellow("div1");
		txtJOBSTATNAME = (Textbox) getFellow("txtJOBSTATNAME");
		txtUPBY = (Textbox) getFellow("txtUPBY");
		txtUPDT = (Textbox) getFellow("txtUPDT");

	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(txtJOBSTATID);
		addEnterIndex(btnAdd);
		addEnterIndex(txtJOBSTATNAME);
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

		txtJOBSTATID.setValue("");
		txtJOBSTATNAME.setValue("");
		txtUPBY.setValue("");
		txtUPDT.setValue("");

		txtJOBSTATID.setReadonly(false);
		txtJOBSTATID.focus();

		btnExit.setVisible(true);
		btnBack.setVisible(false);
		btnSave.setVisible(false);
		btnDelete.setVisible(false);
		btnAdd.setVisible(true);
	}

	public void onOK() {
		try {
			if (focusObj == txtJOBSTATID) {
				if (!txtJOBSTATID.isReadonly()) {
					if (Fnc.isEmpty(txtJOBSTATID.getValue()) || txtJOBSTATID.getValue().equals("0")) {
						super.onOK();// จะเลื่อนไปปุ่ม btnAdd ถ้า visible อยู่
					} else if (read_record(Fnc.getIntFromStr(txtJOBSTATID.getValue().trim()))) {
						txtJOBSTATNAME.focus();
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

			TboTKJOBSTAT table1 = new TboTKJOBSTAT();

			table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());

			int newcode = TbmTKJOBSTAT.getMaxCode(this.getLoginBean().getCOMP_CDE()) + 1;
			table1.setJOBSTATID(newcode);

			table1.setJOBSTATNAME("??");
			table1.setUPDBY(this.getLoginBean().getUSER_ID());
			table1.setUPDDTE(FnDate.getTodaySqlDateTime());

			TbfTKJOBSTAT.insert(table1);

			read_record(newcode);
			txtJOBSTATNAME.focus();

		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}
	}

	public boolean read_record(int jobstatid) throws Exception {
		boolean res = false;

		TboTKJOBSTAT table1 = new TboTKJOBSTAT();

		table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		table1.setJOBSTATID(jobstatid);

		if (TbfTKJOBSTAT.find(table1)) {
			txtJOBSTATID.setValue(table1.getJOBSTATID() + "");
			txtJOBSTATNAME.setValue(table1.getJOBSTATNAME());

			txtUPBY.setValue(table1.getUPDBY());
			txtUPDT.setValue(FnDate.displayDateTimeString(table1.getUPDDTE()));

			btnExit.setVisible(false);
			btnBack.setVisible(true);
			btnSave.setVisible(true);
			btnDelete.setVisible(true);
			btnAdd.setVisible(false);
			txtJOBSTATID.setReadonly(true);
			div1.setVisible(true);

			res = true;
		}

		return res;
	}

	private void validateData() throws Exception {

		if (Fnc.isEmpty(txtJOBSTATID.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtJOBSTATID.getTooltiptext() + "\" ");
		}
		if (Fnc.isEmpty(txtJOBSTATNAME.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtJOBSTATNAME.getTooltiptext() + "\" ");
		}

	}

	public void onClick_Save() {
		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				validateData();

				TboTKJOBSTAT table1 = new TboTKJOBSTAT();

				table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				table1.setJOBSTATID(Fnc.getIntFromStr(txtJOBSTATID.getValue()));

				if (TbfTKJOBSTAT.find(dbc, table1)) {
					table1.setJOBSTATNAME(txtJOBSTATNAME.getValue());

					table1.setUPDBY(this.getLoginBean().getUSER_ID());
					table1.setUPDDTE(FnDate.getTodaySqlDateTime());

					TbfTKJOBSTAT.update(dbc, table1);
				}

				dbc.commit();
			}

			Msg.info2("บันทึกเรียบร้อย", this, "clearData");

		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void popupData() {
		if (!txtJOBSTATID.isReadonly()) {
			FfTKJOBSTAT.popup(this.getLoginBean(), this, "doPopupData");
		}
	}

	public void doPopupData(int lawstatid) {
		try {
			if (read_record(lawstatid)) {
				txtJOBSTATNAME.focus();
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

				TboTKJOBSTAT table1 = new TboTKJOBSTAT();

				table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				table1.setJOBSTATID(Fnc.getIntFromStr(txtJOBSTATID.getValue()));

				if (TbfTKJOBSTAT.find(dbc, table1)) {
					TbfTKJOBSTAT.delete(dbc, table1);
				}

				dbc.commit();
			}

			Msg.info2("ลบข้อมูลเรียบร้อยแล้ว", this, "clearData");

		} catch (Exception e) {
			Msg.error(e);
		}
	}

}

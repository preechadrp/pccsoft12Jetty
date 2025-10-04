package com.pcc.tk.ui.FrmTkLawtype;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Textbox;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.tk.find.FfTKLAWTYPE;
import com.pcc.tk.tbf.TbfTKLAWTYPE;
import com.pcc.tk.tbm.TbmTKLAWTYPE;
import com.pcc.tk.tbo.TboTKLAWTYPE;

public class FrmTkLawtype extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Button btnExit;
	public Button btnBack;
	public Button btnSave;
	public Button btnDelete;
	public Textbox txtLAWTYPEID;
	public Button btnAdd;
	public Div div1;
	public Textbox txtLAWTYPENAME;
	public Textbox txtUPBY;
	public Textbox txtUPDT;

	@Override
	public void setFormObj() {
		btnExit = (Button) getFellow("btnExit");
		btnBack = (Button) getFellow("btnBack");
		btnSave = (Button) getFellow("btnSave");
		btnDelete = (Button) getFellow("btnDelete");
		txtLAWTYPEID = (Textbox) getFellow("txtLAWTYPEID");
		btnAdd = (Button) getFellow("btnAdd");
		div1 = (Div) getFellow("div1");
		txtLAWTYPENAME = (Textbox) getFellow("txtLAWTYPENAME");
		txtUPBY = (Textbox) getFellow("txtUPBY");
		txtUPDT = (Textbox) getFellow("txtUPDT");

	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(txtLAWTYPEID);
		addEnterIndex(btnAdd);
		addEnterIndex(txtLAWTYPENAME);
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

		txtLAWTYPEID.setValue("");
		txtLAWTYPENAME.setValue("");
		txtUPBY.setValue("");
		txtUPDT.setValue("");

		txtLAWTYPEID.setReadonly(false);
		txtLAWTYPEID.focus();

		btnExit.setVisible(true);
		btnBack.setVisible(false);
		btnSave.setVisible(false);
		btnDelete.setVisible(false);
		btnAdd.setVisible(true);
	}

	public void onOK() {
		try {
			if (focusObj == txtLAWTYPEID) {
				if (!txtLAWTYPEID.isReadonly()) {
					if (Fnc.isEmpty(txtLAWTYPEID.getValue()) || txtLAWTYPEID.getValue().equals("0")) {
						super.onOK();// จะเลื่อนไปปุ่ม btnAdd ถ้า visible อยู่
					} else if (read_record(Fnc.getIntFromStr(txtLAWTYPEID.getValue().trim()))) {
						txtLAWTYPENAME.focus();
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

			TboTKLAWTYPE table1 = new TboTKLAWTYPE();

			table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());

			int newcode = TbmTKLAWTYPE.getMaxCode(this.getLoginBean().getCOMP_CDE()) + 1;
			table1.setLAWTYPEID(newcode);

			table1.setLAWTYPENAME("??");
			table1.setUPDBY(this.getLoginBean().getUSER_ID());
			table1.setUPDDTE(FnDate.getTodaySqlDateTime());

			TbfTKLAWTYPE.insert(table1);

			read_record(newcode);
			txtLAWTYPENAME.focus();

		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}
	}

	public boolean read_record(int lawtypeid) throws Exception {
		boolean res = false;

		TboTKLAWTYPE table1 = new TboTKLAWTYPE();

		table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		table1.setLAWTYPEID(lawtypeid);

		if (TbfTKLAWTYPE.find(table1)) {
			txtLAWTYPEID.setValue(table1.getLAWTYPEID() + "");
			txtLAWTYPENAME.setValue(table1.getLAWTYPENAME());

			txtUPBY.setValue(table1.getUPDBY());
			txtUPDT.setValue(FnDate.displayDateTimeString(table1.getUPDDTE()));

			btnExit.setVisible(false);
			btnBack.setVisible(true);
			btnSave.setVisible(true);
			btnDelete.setVisible(true);
			btnAdd.setVisible(false);
			txtLAWTYPEID.setReadonly(true);
			div1.setVisible(true);

			res = true;
		}

		return res;
	}

	private void validateData() throws Exception {

		if (Fnc.isEmpty(txtLAWTYPEID.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtLAWTYPEID.getTooltiptext() + "\" ");
		}
		if (Fnc.isEmpty(txtLAWTYPENAME.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtLAWTYPENAME.getTooltiptext() + "\" ");
		}

	}

	public void onClick_Save() {
		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				validateData();

				TboTKLAWTYPE table1 = new TboTKLAWTYPE();

				table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				table1.setLAWTYPEID(Fnc.getIntFromStr(txtLAWTYPEID.getValue()));

				if (TbfTKLAWTYPE.find(dbc, table1)) {
					table1.setLAWTYPENAME(txtLAWTYPENAME.getValue());

					table1.setUPDBY(this.getLoginBean().getUSER_ID());
					table1.setUPDDTE(FnDate.getTodaySqlDateTime());

					TbfTKLAWTYPE.update(dbc, table1);
				}

				dbc.commit();
			}

			Msg.info2("บันทึกเรียบร้อย", this, "clearData");

		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void popupData() {
		if (!txtLAWTYPEID.isReadonly()) {
			FfTKLAWTYPE.popup(this.getLoginBean(), this, "doPopupData");
		}
	}

	public void doPopupData(int lawstatid) {
		try {
			if (read_record(lawstatid)) {
				txtLAWTYPENAME.focus();
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

				TboTKLAWTYPE table1 = new TboTKLAWTYPE();

				table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				table1.setLAWTYPEID(Fnc.getIntFromStr(txtLAWTYPEID.getValue()));

				if (TbfTKLAWTYPE.find(dbc, table1)) {
					TbfTKLAWTYPE.delete(dbc, table1);
				}

				dbc.commit();
			}

			Msg.info2("ลบข้อมูลเรียบร้อยแล้ว", this, "clearData");

		} catch (Exception e) {
			Msg.error(e);
		}
	}

}

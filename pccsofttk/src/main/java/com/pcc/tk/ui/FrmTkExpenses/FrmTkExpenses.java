package com.pcc.tk.ui.FrmTkExpenses;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.tk.find.FfTKEXPENSES;
import com.pcc.tk.tbf.TbfTKEXPENSES;
import com.pcc.tk.tbm.TbmTKEXPENSES;
import com.pcc.tk.tbo.TboTKEXPENSES;

public class FrmTkExpenses extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Button btnExit;
	public Button btnBack;
	public Button btnSave;
	public Button btnDelete;
	public Textbox txtEXPENSESID;
	public Button btnAdd;
	public Div div1;
	public Textbox txtEXPENSESNAME;
	public Textbox txtUPBY;
	public Textbox txtUPDT;

	@Override
	public void setFormObj() {
		btnExit = (Button) getFellow("btnExit");
		btnBack = (Button) getFellow("btnBack");
		btnSave = (Button) getFellow("btnSave");
		btnDelete = (Button) getFellow("btnDelete");
		txtEXPENSESID = (Textbox) getFellow("txtEXPENSESID");
		btnAdd = (Button) getFellow("btnAdd");
		div1 = (Div) getFellow("div1");
		txtEXPENSESNAME = (Textbox) getFellow("txtEXPENSESNAME");
		txtUPBY = (Textbox) getFellow("txtUPBY");
		txtUPDT = (Textbox) getFellow("txtUPDT");

	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(txtEXPENSESID);
		addEnterIndex(btnAdd);
		addEnterIndex(txtEXPENSESNAME);
		//addEnterIndex(txtUPBY);
		//addEnterIndex(txtUPDT);		
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

		txtEXPENSESID.setValue("");
		txtEXPENSESNAME.setValue("");
		txtUPBY.setValue("");
		txtUPDT.setValue("");

		txtEXPENSESID.setReadonly(false);
		txtEXPENSESID.focus();

		btnExit.setVisible(true);
		btnBack.setVisible(false);
		btnSave.setVisible(false);
		btnDelete.setVisible(false);
		btnAdd.setVisible(true);
	}

	public void onOK() {
		try {
			if (focusObj == txtEXPENSESID) {
				if (!txtEXPENSESID.isReadonly()) {
					if (Fnc.isEmpty(txtEXPENSESID.getValue()) || txtEXPENSESID.getValue().equals("0")) {
						super.onOK();// จะเลื่อนไปปุ่ม btnAdd ถ้า visible อยู่
					} else if (read_record(Fnc.getIntFromStr(txtEXPENSESID.getValue().trim()))) {
						txtEXPENSESNAME.focus();
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

			TboTKEXPENSES table1 = new TboTKEXPENSES();

			table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			
			int newcode = TbmTKEXPENSES.getMaxCode(this.getLoginBean().getCOMP_CDE()) + 1;
			table1.setEXPENSESID(newcode);
			
			table1.setEXPENSESNAME("??");
			table1.setUPDBY(this.getLoginBean().getUSER_ID());
			table1.setUPDDTE(FnDate.getTodaySqlDateTime());

			TbfTKEXPENSES.insert(table1);

			read_record(newcode);
			txtEXPENSESNAME.focus();

		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}
	}

	public boolean read_record(int expensesid) throws Exception {
		boolean res = false;

		TboTKEXPENSES table1 = new TboTKEXPENSES();

		table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		table1.setEXPENSESID(expensesid);

		if (TbfTKEXPENSES.find(table1)) {
			txtEXPENSESID.setValue(table1.getEXPENSESID() + "");
			txtEXPENSESNAME.setValue(table1.getEXPENSESNAME());

			txtUPBY.setValue(table1.getUPDBY());
			txtUPDT.setValue(FnDate.displayDateTimeString(table1.getUPDDTE()));

			btnExit.setVisible(false);
			btnBack.setVisible(true);
			btnSave.setVisible(true);
			btnDelete.setVisible(true);
			btnAdd.setVisible(false);
			txtEXPENSESID.setReadonly(true);
			div1.setVisible(true);

			res = true;
		}

		return res;
	}

	private void validateData() throws Exception {

		if (Fnc.isEmpty(txtEXPENSESID.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtEXPENSESID.getTooltiptext() + "\" ");
		}
		if (Fnc.isEmpty(txtEXPENSESNAME.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtEXPENSESNAME.getTooltiptext() + "\" ");
		}

	}

	public void onClick_Save() {
		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				validateData();

				TboTKEXPENSES table1 = new TboTKEXPENSES();

				table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				table1.setEXPENSESID(Fnc.getIntFromStr(txtEXPENSESID.getValue()));

				if (TbfTKEXPENSES.find(dbc, table1)) {
					table1.setEXPENSESNAME(txtEXPENSESNAME.getValue());

					table1.setUPDBY(this.getLoginBean().getUSER_ID());
					table1.setUPDDTE(FnDate.getTodaySqlDateTime());

					TbfTKEXPENSES.update(dbc, table1);
				}

				dbc.commit();
			}

			Msg.info2("บันทึกเรียบร้อย", this, "clearData");

		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void popupData() {
		if (!txtEXPENSESID.isReadonly()) {
			FfTKEXPENSES.popup(this.getLoginBean(), this, "doPopupData");
		}
	}

	public void doPopupData(int expensesid) {
		try {
			if (read_record(expensesid)) {
				txtEXPENSESNAME.focus();
			}
		} catch (Exception e) {
			Msg.error(e);
		}
	}
	
	public void onClick_Delete() {
		Msg.confirm(Labels.getLabel("comm.label.deleteComfirm") + " ?", "?", (event1) -> {
			if (Messagebox.Button.YES.equals(event1.getButton())) {

				try {

					try (FDbc dbc = FDbc.connectMasterDb()) {
						dbc.beginTrans();

						TboTKEXPENSES table1 = new TboTKEXPENSES();

						table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
						table1.setEXPENSESID(Fnc.getIntFromStr(txtEXPENSESID.getValue()));

						if (TbfTKEXPENSES.find(dbc, table1)) {
							TbfTKEXPENSES.delete(dbc, table1);
						}

						dbc.commit();
					}

					Msg.info2("ลบข้อมูลเรียบร้อยแล้ว", this, "clearData");

				} catch (Exception e) {
					Msg.error(e);
				}

			}
		});
	}

}

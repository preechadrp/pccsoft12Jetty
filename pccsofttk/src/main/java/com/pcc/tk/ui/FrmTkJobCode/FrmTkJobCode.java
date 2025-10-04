package com.pcc.tk.ui.FrmTkJobCode;

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
import com.pcc.tk.find.FfTKJOBCODE;
import com.pcc.tk.tbf.TbfTKJOBCODE;
import com.pcc.tk.tbm.TbmTKJOBCODE;
import com.pcc.tk.tbo.TboTKJOBCODE;

public class FrmTkJobCode extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Button btnExit;
	public Button btnBack;
	public Button btnSave;
	public Button btnDelete;
	public Textbox txtJOBCODE;
	public Button btnAdd;
	public Div div1;
	public Textbox txtJOBNAME;
	public Textbox txtUPBY;
	public Textbox txtUPDT;

	@Override
	public void setFormObj() {
		btnExit = (Button) getFellow("btnExit");
		btnBack = (Button) getFellow("btnBack");
		btnSave = (Button) getFellow("btnSave");
		btnDelete = (Button) getFellow("btnDelete");
		txtJOBCODE = (Textbox) getFellow("txtJOBCODE");
		btnAdd = (Button) getFellow("btnAdd");
		div1 = (Div) getFellow("div1");
		txtJOBNAME = (Textbox) getFellow("txtJOBNAME");
		txtUPBY = (Textbox) getFellow("txtUPBY");
		txtUPDT = (Textbox) getFellow("txtUPDT");

	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(txtJOBCODE);
		addEnterIndex(btnAdd);
		addEnterIndex(txtJOBNAME);
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

		txtJOBCODE.setValue("");
		txtJOBNAME.setValue("");
		txtUPBY.setValue("");
		txtUPDT.setValue("");

		txtJOBCODE.setReadonly(false);
		txtJOBCODE.focus();

		btnExit.setVisible(true);
		btnBack.setVisible(false);
		btnSave.setVisible(false);
		btnDelete.setVisible(false);
		btnAdd.setVisible(true);
	}

	public void onOK() {
		try {
			if (focusObj == txtJOBCODE) {
				if (!txtJOBCODE.isReadonly()) {
					if (Fnc.isEmpty(txtJOBCODE.getValue()) || txtJOBCODE.getValue().equals("0")) {
						super.onOK();// จะเลื่อนไปปุ่ม btnAdd ถ้า visible อยู่
					} else if (read_record(Fnc.getIntFromStr(txtJOBCODE.getValue().trim()))) {
						txtJOBNAME.focus();
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

			TboTKJOBCODE table1 = new TboTKJOBCODE();

			table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			
			int newcode = TbmTKJOBCODE.getMaxCode(this.getLoginBean().getCOMP_CDE()) + 1;
			table1.setJOBCODE(newcode);
			
			table1.setJOBNAME("??");
			table1.setUPDBY(this.getLoginBean().getUSER_ID());
			table1.setUPDDTE(FnDate.getTodaySqlDateTime());

			TbfTKJOBCODE.insert(table1);

			read_record(newcode);
			txtJOBNAME.focus();

		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}
	}

	public boolean read_record(int courtid) throws Exception {
		boolean res = false;

		TboTKJOBCODE table1 = new TboTKJOBCODE();

		table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		table1.setJOBCODE(courtid);

		if (TbfTKJOBCODE.find(table1)) {
			txtJOBCODE.setValue(table1.getJOBCODE() + "");
			txtJOBNAME.setValue(table1.getJOBNAME());

			txtUPBY.setValue(table1.getUPDBY());
			txtUPDT.setValue(FnDate.displayDateTimeString(table1.getUPDDTE()));

			btnExit.setVisible(false);
			btnBack.setVisible(true);
			btnSave.setVisible(true);
			btnDelete.setVisible(true);
			btnAdd.setVisible(false);
			txtJOBCODE.setReadonly(true);
			div1.setVisible(true);

			res = true;
		}

		return res;
	}

	private void validateData() throws Exception {

		if (Fnc.isEmpty(txtJOBCODE.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtJOBCODE.getTooltiptext() + "\" ");
		}
		if (Fnc.isEmpty(txtJOBNAME.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtJOBNAME.getTooltiptext() + "\" ");
		}

	}

	public void onClick_Save() {
		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				validateData();

				TboTKJOBCODE table1 = new TboTKJOBCODE();

				table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				table1.setJOBCODE(Fnc.getIntFromStr(txtJOBCODE.getValue()));

				if (TbfTKJOBCODE.find(dbc, table1)) {
					table1.setJOBNAME(txtJOBNAME.getValue());

					table1.setUPDBY(this.getLoginBean().getUSER_ID());
					table1.setUPDDTE(FnDate.getTodaySqlDateTime());

					TbfTKJOBCODE.update(dbc, table1);
				}

				dbc.commit();
			}

			Msg.info2("บันทึกเรียบร้อย", this, "clearData");

		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void popupData() {
		if (!txtJOBCODE.isReadonly()) {
			FfTKJOBCODE.popup(this.getLoginBean(), this, "doPopupData");
		}
	}

	public void doPopupData(int courtid) {
		try {
			if (read_record(courtid)) {
				txtJOBNAME.focus();
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

						TboTKJOBCODE table1 = new TboTKJOBCODE();

						table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
						table1.setJOBCODE(Fnc.getIntFromStr(txtJOBCODE.getValue()));

						if (TbfTKJOBCODE.find(dbc, table1)) {
							TbfTKJOBCODE.delete(dbc, table1);
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

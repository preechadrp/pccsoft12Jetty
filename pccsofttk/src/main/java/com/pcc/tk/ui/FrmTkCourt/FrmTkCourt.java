package com.pcc.tk.ui.FrmTkCourt;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Textbox;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.tk.find.FfTKCOURT;
import com.pcc.tk.tbf.TbfTKCOURT;
import com.pcc.tk.tbm.TbmTKCOURT;
import com.pcc.tk.tbo.TboTKCOURT;

public class FrmTkCourt extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Button btnExit;
	public Button btnBack;
	public Button btnSave;
	public Button btnDelete;
	public Textbox txtCOURTID;
	public Button btnAdd;
	public Div div1;
	public Textbox txtCOURTNAME;
	public Textbox txtUPBY;
	public Textbox txtUPDT;

	@Override
	public void setFormObj() {
		btnExit = (Button) getFellow("btnExit");
		btnBack = (Button) getFellow("btnBack");
		btnSave = (Button) getFellow("btnSave");
		btnDelete = (Button) getFellow("btnDelete");
		txtCOURTID = (Textbox) getFellow("txtCOURTID");
		btnAdd = (Button) getFellow("btnAdd");
		div1 = (Div) getFellow("div1");
		txtCOURTNAME = (Textbox) getFellow("txtCOURTNAME");
		txtUPBY = (Textbox) getFellow("txtUPBY");
		txtUPDT = (Textbox) getFellow("txtUPDT");

	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(txtCOURTID);
		addEnterIndex(btnAdd);
		addEnterIndex(txtCOURTNAME);
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

		txtCOURTID.setValue("");
		txtCOURTNAME.setValue("");
		txtUPBY.setValue("");
		txtUPDT.setValue("");

		txtCOURTID.setReadonly(false);
		txtCOURTID.focus();

		btnExit.setVisible(true);
		btnBack.setVisible(false);
		btnSave.setVisible(false);
		btnDelete.setVisible(false);
		btnAdd.setVisible(true);
	}

	public void onOK() {
		try {
			if (focusObj == txtCOURTID) {
				if (!txtCOURTID.isReadonly()) {
					if (Fnc.isEmpty(txtCOURTID.getValue()) || txtCOURTID.getValue().equals("0")) {
						super.onOK();// จะเลื่อนไปปุ่ม btnAdd ถ้า visible อยู่
					} else if (read_record(Fnc.getIntFromStr(txtCOURTID.getValue().trim()))) {
						txtCOURTNAME.focus();
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

			TboTKCOURT table1 = new TboTKCOURT();

			table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			
			int newcode = TbmTKCOURT.getMaxCode(this.getLoginBean().getCOMP_CDE()) + 1;
			table1.setCOURTID(newcode);
			
			table1.setCOURTNAME("??");
			table1.setUPDBY(this.getLoginBean().getUSER_ID());
			table1.setUPDDTE(FnDate.getTodaySqlDateTime());

			TbfTKCOURT.insert(table1);

			read_record(newcode);
			txtCOURTNAME.focus();

		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}
	}

	public boolean read_record(int courtid) throws Exception {
		boolean res = false;

		TboTKCOURT table1 = new TboTKCOURT();

		table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		table1.setCOURTID(courtid);

		if (TbfTKCOURT.find(table1)) {
			txtCOURTID.setValue(table1.getCOURTID() + "");
			txtCOURTNAME.setValue(table1.getCOURTNAME());

			txtUPBY.setValue(table1.getUPDBY());
			txtUPDT.setValue(FnDate.displayDateTimeString(table1.getUPDDTE()));

			btnExit.setVisible(false);
			btnBack.setVisible(true);
			btnSave.setVisible(true);
			btnDelete.setVisible(true);
			btnAdd.setVisible(false);
			txtCOURTID.setReadonly(true);
			div1.setVisible(true);

			res = true;
		}

		return res;
	}

	private void validateData() throws Exception {

		if (Fnc.isEmpty(txtCOURTID.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtCOURTID.getTooltiptext() + "\" ");
		}
		if (Fnc.isEmpty(txtCOURTNAME.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtCOURTNAME.getTooltiptext() + "\" ");
		}

	}

	public void onClick_Save() {
		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				validateData();

				TboTKCOURT table1 = new TboTKCOURT();

				table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				table1.setCOURTID(Fnc.getIntFromStr(txtCOURTID.getValue()));

				if (TbfTKCOURT.find(dbc, table1)) {
					table1.setCOURTNAME(txtCOURTNAME.getValue());

					table1.setUPDBY(this.getLoginBean().getUSER_ID());
					table1.setUPDDTE(FnDate.getTodaySqlDateTime());

					TbfTKCOURT.update(dbc, table1);
				}

				dbc.commit();
			}

			Msg.info2("บันทึกเรียบร้อย", this, "clearData");

		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void popupData() {
		if (!txtCOURTID.isReadonly()) {
			FfTKCOURT.popup(this.getLoginBean(), this, "doPopupData");
		}
	}

	public void doPopupData(int courtid) {
		try {
			if (read_record(courtid)) {
				txtCOURTNAME.focus();
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

				TboTKCOURT table1 = new TboTKCOURT();

				table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				table1.setCOURTID(Fnc.getIntFromStr(txtCOURTID.getValue()));

				if (TbfTKCOURT.find(dbc, table1)) {
					TbfTKCOURT.delete(dbc, table1);
				}

				dbc.commit();
			}

			Msg.info2("ลบข้อมูลเรียบร้อยแล้ว", this, "clearData");

		} catch (Exception e) {
			Msg.error(e);
		}
	}

}

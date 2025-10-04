package com.pcc.tk.ui.FrmTkLawyer;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Textbox;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.tk.find.FfTKLAWYER;
import com.pcc.tk.tbf.TbfTKLAWYER;
import com.pcc.tk.tbm.TbmTKLAWYER;
import com.pcc.tk.tbo.TboTKLAWYER;

public class FrmTkLawyer extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Button btnExit;
	public Button btnBack;
	public Button btnSave;
	public Button btnDelete;
	public Textbox txtLAWYERID;
	public Button btnAdd;
	public Div div1;
	public Textbox txtLAWYERNAME;
	public Textbox txtTELNO;
	public Textbox txtUPBY;
	public Textbox txtUPDT;

	@Override
	public void setFormObj() {
		btnExit = (Button) getFellow("btnExit");
		btnBack = (Button) getFellow("btnBack");
		btnSave = (Button) getFellow("btnSave");
		btnDelete = (Button) getFellow("btnDelete");
		txtLAWYERID = (Textbox) getFellow("txtLAWYERID");
		btnAdd = (Button) getFellow("btnAdd");
		div1 = (Div) getFellow("div1");
		txtLAWYERNAME = (Textbox) getFellow("txtLAWYERNAME");
		txtTELNO = (Textbox) getFellow("txtTELNO");
		txtUPBY = (Textbox) getFellow("txtUPBY");
		txtUPDT = (Textbox) getFellow("txtUPDT");

	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(txtLAWYERID);
		addEnterIndex(btnAdd);
		addEnterIndex(txtLAWYERNAME);
		addEnterIndex(txtTELNO);
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

		txtLAWYERID.setValue("");
		txtLAWYERNAME.setValue("");
		txtTELNO.setValue("");
		txtUPBY.setValue("");
		txtUPDT.setValue("");

		txtLAWYERID.setReadonly(false);
		txtLAWYERID.focus();

		btnExit.setVisible(true);
		btnBack.setVisible(false);
		btnSave.setVisible(false);
		btnDelete.setVisible(false);
		btnAdd.setVisible(true);
	}

	public void onOK() {
		try {
			if (focusObj == txtLAWYERID) {
				if (!txtLAWYERID.isReadonly()) {
					if (Fnc.isEmpty(txtLAWYERID.getValue()) || txtLAWYERID.getValue().equals("0")) {
						super.onOK();// จะเลื่อนไปปุ่ม btnAdd ถ้า visible อยู่
					} else if (read_record(Fnc.getIntFromStr(txtLAWYERID.getValue().trim()))) {
						txtLAWYERNAME.focus();
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

			TboTKLAWYER table1 = new TboTKLAWYER();

			table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			
			int newcode = TbmTKLAWYER.getMaxCode(this.getLoginBean().getCOMP_CDE()) + 1;
			table1.setLAWYERID(newcode);
			
			table1.setLAWYERNAME("??");
			table1.setTELNO("");
			table1.setUPDBY(this.getLoginBean().getUSER_ID());
			table1.setUPDDTE(FnDate.getTodaySqlDateTime());

			TbfTKLAWYER.insert(table1);

			read_record(newcode);
			txtLAWYERNAME.focus();

		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}
	}

	public boolean read_record(int lawyerid) throws Exception {
		boolean res = false;

		TboTKLAWYER table1 = new TboTKLAWYER();

		table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		table1.setLAWYERID(lawyerid);

		if (TbfTKLAWYER.find(table1)) {
			txtLAWYERID.setValue(table1.getLAWYERID() + "");
			txtLAWYERNAME.setValue(table1.getLAWYERNAME());
			txtTELNO.setValue(table1.getTELNO());

			txtUPBY.setValue(table1.getUPDBY());
			txtUPDT.setValue(FnDate.displayDateTimeString(table1.getUPDDTE()));

			btnExit.setVisible(false);
			btnBack.setVisible(true);
			btnSave.setVisible(true);
			btnDelete.setVisible(true);
			btnAdd.setVisible(false);
			txtLAWYERID.setReadonly(true);
			div1.setVisible(true);

			res = true;
		}

		return res;
	}

	private void validateData() throws Exception {

		if (Fnc.isEmpty(txtLAWYERID.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtLAWYERID.getTooltiptext() + "\" ");
		}
		if (Fnc.isEmpty(txtLAWYERNAME.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtLAWYERNAME.getTooltiptext() + "\" ");
		}

	}

	public void onClick_Save() {
		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				validateData();

				TboTKLAWYER table1 = new TboTKLAWYER();

				table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				table1.setLAWYERID(Fnc.getIntFromStr(txtLAWYERID.getValue()));

				if (TbfTKLAWYER.find(dbc, table1)) {
					table1.setLAWYERNAME(txtLAWYERNAME.getValue());
					table1.setTELNO(txtTELNO.getValue());

					table1.setUPDBY(this.getLoginBean().getUSER_ID());
					table1.setUPDDTE(FnDate.getTodaySqlDateTime());

					TbfTKLAWYER.update(dbc, table1);
				}

				dbc.commit();
			}

			Msg.info2("บันทึกเรียบร้อย", this, "clearData");

		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void popupData() {
		if (!txtLAWYERID.isReadonly()) {
			FfTKLAWYER.popup(this.getLoginBean(), this, "doPopupData");
		}
	}

	public void doPopupData(int lawyerid) {
		try {
			if (read_record(lawyerid)) {
				txtLAWYERNAME.focus();
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

				TboTKLAWYER table1 = new TboTKLAWYER();

				table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				table1.setLAWYERID(Fnc.getIntFromStr(txtLAWYERID.getValue()));

				if (TbfTKLAWYER.find(dbc, table1)) {
					TbfTKLAWYER.delete(dbc, table1);
				}

				dbc.commit();
			}

			Msg.info2("ลบข้อมูลเรียบร้อยแล้ว", this, "clearData");

		} catch (Exception e) {
			Msg.error(e);
		}
	}

}

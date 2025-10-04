package com.pcc.gl.ui.acEntrLock;

import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;

import com.pcc.gl.tbf.TbfACCT_LOCK;
import com.pcc.gl.tbo.TboACCT_LOCK;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Msg;

public class AcEntrLock extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Datebox tdbToPostdate;

	public Button btnExit, btnSave;

	@Override
	public void setFormObj() {
		tdbToPostdate = (Datebox) getFellow("tdbToPostdate");
		btnExit = (Button) getFellow("btnExit");
		btnSave = (Button) getFellow("btnSave");
	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(tdbToPostdate);
		addEnterIndex(btnSave);
	}

	@Override
	public void formInit() {
		try {
			clearData();
			showOldData();
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	private void clearData() {
		tdbToPostdate.focus();
	}

	public void onOK() {
		try {
			super.onOK();
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	private void validateData() throws Exception {

		if (tdbToPostdate.getValue() == null) {
			throw new Exception("ระบุช่อง \"" + tdbToPostdate.getTooltiptext() + "\" ");
		}

	}

	public void onClick_Save() {

		try {

			boolean new_rec = true;
			validateData();

			TboACCT_LOCK accLock = new TboACCT_LOCK();
			accLock.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			if (TbfACCT_LOCK.find(accLock)) {
				new_rec = false;
			}

			accLock.setLOCKTODATE(FnDate.getSqlDate(tdbToPostdate.getValue()));
			accLock.setUPDBY(this.getLoginBean().getUSER_ID());
			accLock.setUPDDT(FnDate.getTodaySqlDateTime());

			if (new_rec) {
				if (!TbfACCT_LOCK.insert(accLock)) {
					throw new Exception("ไม่สามารถบันทึกได้กรุณาตรวจสอบ");
				}
			} else {
				if (!TbfACCT_LOCK.update(accLock, "")) {
					throw new Exception("ไม่สามารถบันทึกได้กรุณาตรวจสอบ");
				}
			}
			Msg.info("บันทึกเรียบร้อย");
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void showOldData() {

		try {

			TboACCT_LOCK accLock = new TboACCT_LOCK();
			accLock.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			if (TbfACCT_LOCK.find(accLock)) {
				tdbToPostdate.setValue(accLock.getLOCKTODATE());
			}

		} catch (Exception e) {
		}

	}

}

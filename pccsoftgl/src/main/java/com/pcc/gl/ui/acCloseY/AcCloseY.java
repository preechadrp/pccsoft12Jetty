package com.pcc.gl.ui.acCloseY;

import org.zkoss.zul.Button;
import org.zkoss.zul.Intbox;

import com.pcc.gl.progman.ManAcCloseY;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.tbm.TbmFCOMP;

public class AcCloseY extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Intbox intDD;
	public Intbox intMM;
	public Intbox intYYYY;

	public Button btnExit, btnStart, btnSave, btnDelete, btnAdd;

	@Override
	public void setFormObj() {
		intDD = (Intbox) getFellow("intDD");
		intMM = (Intbox) getFellow("intMM");
		intYYYY = (Intbox) getFellow("intYYYY");

		btnExit = (Button) getFellow("btnExit");
		btnStart = (Button) getFellow("btnStart");
	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(intYYYY);
		addEnterIndex(btnStart);

	}

	@Override
	public void formInit() {
		try {
			clearData();
			refreshDate();
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	private void clearData() {
		intYYYY.focus();
		intYYYY.setValue(null);
	}

	public void onOK() {
		try {
			super.onOK();
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	private void refreshDate() {
		try {

			if (intYYYY.getValue() == null) {
				if (FnDate.useThaiDate) {
					intYYYY.setValue(FnDate.getYear(FnDate.getTodaySqlDate()) + FnDate.toBuddhistInc);
				} else {
					intYYYY.setValue(FnDate.getYear(FnDate.getTodaySqlDate()));
				}
			}
			int[] ret_dd = { 0 };
			int[] ret_mm = { 0 };

			TbmFCOMP.get_DDMM_CloseY(this.getLoginBean().getCOMP_CDE(), intYYYY.getValue(), ret_dd, ret_mm);

			intDD.setValue(ret_dd[0]);
			intMM.setValue(ret_mm[0]);
			intYYYY.select();

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	private void validateData() throws Exception {

		refreshDate();

		if (Fnc.getIntValue(intYYYY.getValue()) < 2522) {
			throw new Exception("ระบุช่อง \"" + intYYYY.getTooltiptext() + "\"  ไม่ถูกต้อง");
		}

		if (Fnc.getIntValue(intDD.getValue()) < 1) {
			throw new Exception("ระบุช่อง \"วัน\"  ไม่ถูกต้อง");
		}

		if (Fnc.getIntValue(intMM.getValue()) < 1) {
			throw new Exception("ระบุช่อง \"เดือน\"  ไม่ถูกต้อง");
		}

	}

	public void onClick_btnStart() {

		try {

			validateData();
			ManAcCloseY.closeAcct(this.getLoginBean(), this.getLoginBean().getCOMP_CDE(), intDD.getValue(),
					intMM.getValue(), intYYYY.getValue());
			Msg.info("ดำเนินการเรียบร้อย");

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void onChange_intYYYY() {
		refreshDate();
	}

}

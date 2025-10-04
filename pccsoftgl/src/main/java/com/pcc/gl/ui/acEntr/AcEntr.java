package com.pcc.gl.ui.acEntr;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;

import com.pcc.gl.find.FfACCT_NO;
import com.pcc.gl.find.FfACCT_VOU_TYPE;
import com.pcc.gl.find.FfACGL_HEADER;
import com.pcc.gl.lib.AcRunning;
import com.pcc.gl.progman.ManAcEntr;
import com.pcc.gl.tbf.TbfACCT_NO;
import com.pcc.gl.tbf.TbfACCT_VOU_TYPE;
import com.pcc.gl.tbf.TbfACGL_DETAIL;
import com.pcc.gl.tbf.TbfACGL_HEADER;
import com.pcc.gl.tbm.TbmACCT_LOCK;
import com.pcc.gl.tbm.TbmACCT_VOU_TYPE;
import com.pcc.gl.tbm.TbmACGL_DETAIL;
import com.pcc.gl.tbo.TboACCT_NO;
import com.pcc.gl.tbo.TboACCT_VOU_TYPE;
import com.pcc.gl.tbo.TboACGL_DETAIL;
import com.pcc.gl.tbo.TboACGL_HEADER;
import com.pcc.sys.find.FfFSECTION;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.MyDecimalbox;
import com.pcc.sys.lib.ZkUtil;
import com.pcc.sys.tbf.TbfFSECTION;
import com.pcc.sys.tbm.TbmFPARA_COMP;
import com.pcc.sys.tbo.TboFSECTION;

public class AcEntr extends FWinMenu {

	private static final long serialVersionUID = 1L;
	public Datebox tdbPOSTDATE;
	public Textbox txtVOU_TYPE, txtVOU_NAME;
	public Textbox txtVOU_NO, txtDESCRH;
	public Combobox cmbDRCR;
	public Textbox txtACCT_ID, txtACCT_NAME, txtSECT_ID, txtSECT_NAME;
	public MyDecimalbox decAMT, decSUM_DB, decSUM_CR, decSUM_DIFF;
	public Textbox txtDESCR;
	public Div div1;
	public Grid gridAccList;
	public Button btnExit, btnSave, btnDelete;
	public Button btnAdd, btnAddDet, btnUndoDet;
	public Button btnAp, btnAr;
	public Intbox intEdit_VOU_SEQ;

	private java.util.List<FModelHasMap> lst_dat = new ArrayList<FModelHasMap>();
	private final String sPOST_APP = "AcEntr";

	@Override
	public void setFormObj() {

		txtVOU_TYPE = (Textbox) getFellow("txtVOU_TYPE");
		txtVOU_NAME = (Textbox) getFellow("txtVOU_NAME");
		tdbPOSTDATE = (Datebox) getFellow("tdbPOSTDATE");
		txtVOU_NO = (Textbox) getFellow("txtVOU_NO");
		txtDESCRH = (Textbox) getFellow("txtDESCRH");
		cmbDRCR = (Combobox) getFellow("cmbDRCR");
		txtACCT_ID = (Textbox) getFellow("txtACCT_ID");
		txtACCT_NAME = (Textbox) getFellow("txtACCT_NAME");
		txtSECT_ID = (Textbox) getFellow("txtSECT_ID");
		txtSECT_NAME = (Textbox) getFellow("txtSECT_NAME");
		decAMT = (MyDecimalbox) getFellow("decAMT");
		decSUM_DB = (MyDecimalbox) getFellow("decSUM_DB");
		decSUM_CR = (MyDecimalbox) getFellow("decSUM_CR");
		decSUM_DIFF = (MyDecimalbox) getFellow("decSUM_DIFF");
		txtDESCR = (Textbox) getFellow("txtDESCR");
		intEdit_VOU_SEQ = (Intbox) getFellow("intEdit_VOU_SEQ");

		gridAccList = (Grid) getFellow("gridAccList");
		div1 = (Div) getFellow("div1");

		btnExit = (Button) getFellow("btnExit");
		btnSave = (Button) getFellow("btnSave");
		btnDelete = (Button) getFellow("btnDelete");

		btnAdd = (Button) getFellow("btnAdd");
		btnAddDet = (Button) getFellow("btnAddDet");
		btnUndoDet = (Button) getFellow("btnUndoDet");

		btnAp = (Button) getFellow("btnAp");
		btnAr = (Button) getFellow("btnAr");

	}

	@Override
	public void addEnterIndex() {

		addEnterIndex(txtVOU_TYPE);
		// addEnterIndex(txtVOU_NAME);
		addEnterIndex(tdbPOSTDATE);
		addEnterIndex(txtVOU_NO);
		addEnterIndex(btnAdd);
		addEnterIndex(txtDESCRH);
		addEnterIndex(cmbDRCR);
		addEnterIndex(txtACCT_ID);
		// addEnterIndex(txtACCT_NAME);
		addEnterIndex(txtSECT_ID);
		// addEnterIndex(txtSECT_NAME);
		addEnterIndex(decAMT);
		addEnterIndex(txtDESCR);
		addEnterIndex(btnAddDet);
		// addEnterIndex(decSUM_DB);
		// addEnterIndex(decSUM_CR);
		// addEnterIndex(decSUM_DIFF);

	}

	@Override
	public void formInit() {

		// == set style and renderer
		ZkUtil.setGridHeaderStyle(this.gridAccList);
		this.gridAccList.setRowRenderer(this.getRowRenderer1());
		this.clearData();

	}

	public void clearData() {

		txtVOU_TYPE.setValue("");
		txtVOU_TYPE.setReadonly(false);
		txtVOU_TYPE.focus();
		txtVOU_NAME.setValue("");
		tdbPOSTDATE.setValue(FnDate.getTodaySqlDate());
		tdbPOSTDATE.setReadonly(false);
		txtVOU_NO.setValue("");
		txtVOU_NO.setReadonly(false);
		txtDESCRH.setValue("");
		cmbDRCR.setSelectedIndex(0);
		txtACCT_ID.setValue("");
		txtACCT_NAME.setValue("");
		txtSECT_ID.setValue("");
		txtSECT_NAME.setValue("");
		decAMT.setValue(BigDecimal.ZERO);
		txtDESCR.setValue("");
		decSUM_DB.setValue(BigDecimal.ZERO);
		decSUM_CR.setValue(BigDecimal.ZERO);
		decSUM_DIFF.setValue(BigDecimal.ZERO);

		intEdit_VOU_SEQ.setValue(0);
		gridAccList.getRows().getChildren().clear();
		lst_dat.clear();

		div1.setVisible(false);

		btnExit.setVisible(true);
		btnSave.setVisible(false);
		btnDelete.setVisible(false);
		btnAdd.setVisible(true);

		btnAddDet.setLabel("บันทีก(เพิ่ม)");
		btnUndoDet.setVisible(false);
		if (btnAp.isVisible()) {
			btnAp.setDisabled(false);
		}
		if (btnAr.isVisible()) {
			btnAr.setDisabled(false);
		}

	}

	public void onOK() {
		try {
			if (focusObj == txtVOU_TYPE) {
				if (Fnc.isEmpty(txtVOU_TYPE.getValue())) {
					popupVouType();
				} else if (!read_vou_type(txtVOU_TYPE.getValue().trim())) {
					txtVOU_NAME.setValue("");
					popupVouType();
				} else {
					super.onOK();
				}

			} else if (focusObj == txtVOU_NO) {
				if (Fnc.isEmpty(txtVOU_NO.getValue())) {
					super.onOK();// จะเลื่อนไปปุ่ม btnAdd ถ้า visible อยู่
				} else {
					doPopupVouNo(txtVOU_NO.getValue().trim());
				}

			} else if (focusObj == txtACCT_ID) {
				if (Fnc.isEmpty(txtACCT_ID.getValue())) {
					popupAcctNo();
				} else if (!read_acct_id(txtACCT_ID.getValue().trim())) {
					txtACCT_NAME.setValue("");
					popupAcctNo();
				} else {
					super.onOK();
				}

			} else if (focusObj == txtSECT_ID) {
				if (Fnc.isEmpty(txtSECT_ID.getValue())) {
					super.onOK();
				} else if (!read_sect_id(txtSECT_ID.getValue().trim())) {
					txtSECT_ID.setValue("");
					popupSectID();
				} else {
					super.onOK();
				}

			} else {
				super.onOK();
			}
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	private void showData(String vou_no) throws Exception {

		// == ต้องตรวจสอบด้วย check_record ก่อนเสมอ

		try (FDbc dbc = FDbc.connectMasterDb()) {
			dbc.beginTrans();

			// == update acgl_header.RECSTA = 0
			TboACGL_HEADER acch = new TboACGL_HEADER();

			acch.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			acch.setVOU_TYPE(txtVOU_TYPE.getValue());
			acch.setVOU_NO(vou_no);

			if (TbfACGL_HEADER.find(dbc, acch)) {

				tdbPOSTDATE.setValue(acch.getPOSTDATE());
				txtVOU_NO.setValue(acch.getVOU_NO());
				txtDESCRH.setValue(acch.getDESCR());

				int old_recsta = acch.getRECSTA();

				acch.setRECSTA(0);
				acch.setUPBY(this.getLoginBean().getUSER_ID());
				acch.setUPDT(FnDate.getTodaySqlDateTime());

				if (!TbfACGL_HEADER.update(dbc, acch, " RECSTA = " + old_recsta)) {
					throw new Exception("ไม่สามารถเปิดเอกสารแก้ไขได้");
				}

			}

			// == update acgl_detail.RECSTA = 0
			TbmACGL_DETAIL.updateRECSTA(dbc, 0, acch.getCOMP_CDE(), acch.getVOU_TYPE(), acch.getVOU_NO());

			ManAcEntr.update_RECSTA(dbc, 0, this.getLoginBean(), txtVOU_TYPE.getValue(), txtVOU_NO.getValue());

			if (TbmACCT_VOU_TYPE.getUSE_PV(dbc, txtVOU_TYPE.getValue(), this.getLoginBean()) == 1) {
				btnAp.setVisible(true);
			} else {
				btnAp.setVisible(false);
			}
			if (TbmACCT_VOU_TYPE.getUSE_RV(dbc, txtVOU_TYPE.getValue(), this.getLoginBean()) == 1) {
				btnAr.setVisible(true);
			} else {
				btnAr.setVisible(false);
			}

			dbc.commit();

		}

		showDetail();
		div1.setVisible(true);
		tdbPOSTDATE.setReadonly(true);
		txtVOU_TYPE.setReadonly(true);
		txtVOU_NO.setReadonly(true);
		btnAdd.setVisible(false);

		btnExit.setVisible(false);
		btnSave.setVisible(true);
		btnDelete.setVisible(true);
		txtDESCRH.focus();

	}

	public void doShowDetail() {
		try {
			showDetail();
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	private void showDetail() throws Exception {
		TbmACGL_DETAIL.getDataForAcEntr(lst_dat, txtVOU_TYPE.getValue(), txtVOU_NO.getValue(), this.getLoginBean());
		SimpleListModel rstModel = new SimpleListModel(lst_dat);
		this.gridAccList.setModel(rstModel);

		BigDecimal sum_debit = BigDecimal.ZERO;
		BigDecimal sum_credit = BigDecimal.ZERO;
		for (FModelHasMap mdl : lst_dat) {
			if (mdl.getBigDecimal("NUM_TYPE").intValue() == 1) {
				sum_debit = sum_debit.add(mdl.getBigDecimal("AMT"));
			} else {
				sum_credit = sum_credit.add(mdl.getBigDecimal("AMT"));
			}
		}

		decSUM_DB.setValue(sum_debit);
		decSUM_CR.setValue(sum_credit);
		decSUM_DIFF.setValue(sum_debit.subtract(sum_credit));

	}

	public void onClick_Save() {

		String sVou_type = txtVOU_TYPE.getValue().trim();
		String sVou_no = txtVOU_NO.getValue().trim();
		boolean[] clear_on_error = { false };

		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				// == validate
				if (tdbPOSTDATE.getValue() == null) {
					throw new Exception("ยังไม่ระบุช่อง \"" + tdbPOSTDATE.getTooltiptext() + "\"  ");
				}
				if (Fnc.isEmpty(txtVOU_TYPE.getValue())) {
					throw new Exception("ยังไม่ระบุช่อง \"" + txtVOU_TYPE.getTooltiptext() + "\"  ");
				}
				if (!read_vou_type(txtVOU_TYPE.getValue())) {
					throw new Exception("ระบุช่อง \"" + txtVOU_TYPE.getTooltiptext() + "\" ไม่ถูกต้อง");
				}
				// == end validate

				// == update acgl_header
				TboACGL_HEADER acch = new TboACGL_HEADER();

				acch.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				acch.setVOU_TYPE(txtVOU_TYPE.getValue());
				acch.setVOU_NO(txtVOU_NO.getValue().trim());

				if (TbfACGL_HEADER.find(dbc, acch)) {

					acch.setDESCR(txtDESCRH.getValue().trim());
					acch.setUPBY(this.getLoginBean().getUSER_ID());
					acch.setUPDT(FnDate.getTodaySqlDateTime());

					if (TbmFPARA_COMP.getString(this.getLoginBean().getCOMP_CDE(), "AC_AUTO_APPROVE").trim()
							.toUpperCase().equals("YES")) {
						acch.setRECSTA(2);
						acch.setAPPR_BY(this.getLoginBean().getUSER_ID());
						acch.setAPPR_DT(FnDate.getTodaySqlDateTime());
					} else {
						acch.setRECSTA(1);
						acch.setAPPR_BY("");
						acch.setAPPR_DT(null);
					}

					if (!TbfACGL_HEADER.update(dbc, acch, "")) {
						throw new Exception("ไม่สามารถบันทึกรายการได้กรุณาตรวจสอบ [UPDATE acgl_header]");
					}

				} else {
					throw new Exception("ไม่พบรายการ");
				}

				// ==check debit / credit
				TbmACGL_DETAIL.checkDebitCredit(dbc, txtVOU_TYPE.getValue(), txtVOU_NO.getValue(), this.getLoginBean());

				// == update acgl_detail.RECSTA
				int recsta = 1;
				if (TbmFPARA_COMP.getString(this.getLoginBean().getCOMP_CDE(), "AC_AUTO_APPROVE").trim().toUpperCase()
						.equals("YES")) {
					recsta = 2;
				}
				TbmACGL_DETAIL.updateRECSTA(dbc, recsta, this.getLoginBean().getCOMP_CDE(), txtVOU_TYPE.getValue(),
						txtVOU_NO.getValue());

				ManAcEntr.update_RECSTA(dbc, recsta, this.getLoginBean(), txtVOU_TYPE.getValue(), txtVOU_NO.getValue());

				dbc.commit();
			}

			clearData();
			// Msg.info("บันทึกเรียบร้อย");

			// == print สมุดรายวัน
			ManAcEntr.printVoucher(this.getLoginBean(), sVou_type, sVou_no);

		} catch (Exception e) {
			Msg.error(e);
			e.printStackTrace();
			if (clear_on_error[0] == true) {
				clearData();
			}
		}

	}

	public void onClick_Delete() {
		Msg.confirm2(Labels.getLabel("comm.label.deleteComfirm") + " ?", this, "doOnClick_Delete");
	}

	public void doOnClick_Delete() {
		boolean[] clear_on_error = { false };
		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				check_record(dbc, txtVOU_NO.getValue(), clear_on_error);
				ManAcEntr.delete_vou_no(dbc, this.getLoginBean(), txtVOU_TYPE.getValue().trim(),
						txtVOU_NO.getValue().trim());

				dbc.commit();
			}

			clearData();
			Msg.info("ลบข้อมูลเรียบร้อยแล้ว");

		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
			if (clear_on_error[0] == true) {
				clearData();
			}
		}
	}

	/**
	 * เพิ่มสมุดรายวันใหม่
	 */
	public void onClick_btnAdd() {

		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				validate_beforeNewRecord(dbc);

				// == update acgl_header
				TboACGL_HEADER acch = new TboACGL_HEADER();

				acch.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				acch.setVOU_TYPE(txtVOU_TYPE.getValue().trim());

				String s_VOU_NO = AcRunning.getRunningVoucher(dbc, this.getLoginBean().getCOMP_CDE(),
						txtVOU_TYPE.getValue().trim(), FnDate.getSqlDate(tdbPOSTDATE.getValue()));
				txtVOU_NO.setValue(s_VOU_NO);
				acch.setVOU_NO(s_VOU_NO);

				acch.setPOSTDATE(FnDate.getSqlDate(tdbPOSTDATE.getValue()));
				acch.setPOST_APP(this.sPOST_APP);
				acch.setDESCR(txtDESCRH.getValue().trim());
				acch.setRECSTA(0);
				acch.setUPBY(this.getLoginBean().getUSER_ID());
				acch.setUPDT(FnDate.getTodaySqlDateTime());

				if (!TbfACGL_HEADER.insert(dbc, acch)) {
					throw new Exception("ไม่สามารถบันทึกรายการได้ [UPDATE acgl_header]");
				}

				if (TbmACCT_VOU_TYPE.getUSE_PV(dbc, txtVOU_TYPE.getValue(), this.getLoginBean()) == 1) {
					btnAp.setVisible(true);
				} else {
					btnAp.setVisible(false);
				}
				if (TbmACCT_VOU_TYPE.getUSE_RV(dbc, txtVOU_TYPE.getValue(), this.getLoginBean()) == 1) {
					btnAr.setVisible(true);
				} else {
					btnAr.setVisible(false);
				}

				dbc.commit();
			}

			showDetail();
			div1.setVisible(true);
			tdbPOSTDATE.setReadonly(true);
			txtVOU_TYPE.setReadonly(true);
			txtVOU_NO.setReadonly(true);
			btnAdd.setVisible(false);

			btnExit.setVisible(false);
			btnSave.setVisible(true);
			btnDelete.setVisible(true);
			txtDESCRH.focus();

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	/**
	 * เพิ่มรายการ เดบิต/เครดิต
	 */
	public void onClick_btnAddDet() {

		boolean[] clear_on_error = { false };
		try {

			TboACGL_DETAIL detail = null;

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				validate_beforeSaveDetail(dbc);

				// == update acgl_header
				TboACGL_HEADER acch = new TboACGL_HEADER();

				acch.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				acch.setVOU_TYPE(txtVOU_TYPE.getValue());
				acch.setVOU_NO(txtVOU_NO.getValue());

				if (TbfACGL_HEADER.find(dbc, acch)) {

					if (acch.getRECSTA() == 9) {
						clear_on_error[0] = true;
						throw new Exception("รายการมีการยกเลิกแล้ว");
					}

					acch.setDESCR(txtDESCRH.getValue().trim());
					acch.setUPBY(this.getLoginBean().getUSER_ID());
					acch.setUPDT(FnDate.getTodaySqlDateTime());

					if (!TbfACGL_HEADER.update(dbc, acch, "")) {
						throw new Exception("ไม่สามารถบันทึกรายการได้ [UPDATE acgl_header]");
					}

				} else {
					throw new Exception("ไม่พบรายการ");
				}

				// == insert or update acgl_detail
				if (!btnUndoDet.isVisible()) { // insert

					detail = new TboACGL_DETAIL();

					detail.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
					detail.setVOU_TYPE(txtVOU_TYPE.getValue());
					detail.setVOU_NO(txtVOU_NO.getValue());

					int[] ret_vouseq = { 0 };
					int[] ret_vouseq_show = { 0 };

					TbmACGL_DETAIL.get_max_vouseq_by_vouno(dbc, this.getLoginBean().getCOMP_CDE(),
							txtVOU_TYPE.getValue(), txtVOU_NO.getValue(), ret_vouseq, ret_vouseq_show);

					detail.setVOU_SEQ(ret_vouseq[0] + 1);
					detail.setVOU_SEQ_SHOW(ret_vouseq_show[0] + 1);
					detail.setPOSTDATE(FnDate.getSqlDate(tdbPOSTDATE.getValue()));
					detail.setACCT_ID(txtACCT_ID.getValue());
					detail.setAMT(decAMT.getValue());
					detail.setNUM_TYPE(Fnc.getBigDecimalFromStr(ZkUtil.getSelectItemValueComboBox(cmbDRCR)));
					detail.setSECT_ID(txtSECT_ID.getValue());
					detail.setDESCR(txtDESCR.getValue());
					detail.setRECSTA(0);

					int[] acct_type = { 0 };
					if (ManAcEntr.isSUB_HAS(dbc, detail, this.getLoginBean(), acct_type)) {
						detail.setSUB_HAS(acct_type[0] + "");// มีตัวย่อย
						detail.setSUB_APPR("0");// 0=ยังไม่ใส่ตัวย่อย
						detail.setAPAR_RECTYP(0);// อืนๆ=manual ,1=ล้างเจ้าหนี้,2=ล้างลูกหนี้
					} else {
						detail.setSUB_HAS("");
						detail.setSUB_APPR("");
						detail.setAPAR_RECTYP(0);// อืนๆ=manual ,1=ล้างเจ้าหนี้,2=ล้างลูกหนี้
					}

					if (!TbfACGL_DETAIL.insert(dbc, detail)) {
						throw new Exception("ไม่สามารถบันทึกรายการได้ [INSERT acgl_detail]");
					}

				} else { // update

					detail = new TboACGL_DETAIL();

					detail.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
					detail.setVOU_TYPE(txtVOU_TYPE.getValue());
					detail.setVOU_NO(txtVOU_NO.getValue());
					detail.setVOU_SEQ(intEdit_VOU_SEQ.getValue());

					if (TbfACGL_DETAIL.find(dbc, detail)) {

						detail.setPOSTDATE(FnDate.getSqlDate(tdbPOSTDATE.getValue()));
						detail.setACCT_ID(txtACCT_ID.getValue());
						detail.setAMT(decAMT.getValue());
						detail.setNUM_TYPE(Fnc.getBigDecimalFromStr(ZkUtil.getSelectItemValueComboBox(cmbDRCR)));
						detail.setSECT_ID(txtSECT_ID.getValue());
						detail.setDESCR(txtDESCR.getValue());
						detail.setRECSTA(0);

						int[] acct_type = { 0 };
						if (ManAcEntr.isSUB_HAS(dbc, detail, this.getLoginBean(), acct_type)) {
							detail.setSUB_HAS(acct_type[0] + "");// มีตัวย่อย
							detail.setSUB_APPR("0");// 0=ยังไม่ใส่ตัวย่อย
							detail.setAPAR_RECTYP(0);// อืนๆ=manual ,1=ล้างเจ้าหนี้,2=ล้างลูกหนี้
						} else {
							detail.setSUB_HAS("");
							detail.setSUB_APPR("");
							detail.setAPAR_RECTYP(0);// อืนๆ=manual ,1=ล้างเจ้าหนี้,2=ล้างลูกหนี้
						}

						if (!TbfACGL_DETAIL.update(dbc, detail, "")) {
							throw new Exception("ไม่สามารถบันทึกรายการได้ [UPDATE acgl_detail]");
						}

					} else {
						throw new Exception("ไม่พบรายการ");
					}

				}

				dbc.commit();

			}

			// == dt ตัวย่อย
			if (!Fnc.isEmpty(detail.getSUB_HAS())) {
				ManAcEntr.addSUB_GL(detail, this.getLoginBean(), true, this, "doAfterSave");
			} else {
				doAfterSave();
			}

		} catch (Exception e) {
			Msg.error(e);
			if (clear_on_error[0] == true) {
				this.clearData();
			}
		}

	}

	public void doAfterSave() {
		try {
			clearDetailBox();
			showDetail();
			setDisableButtonInGrid(false);
			cmbDRCR.focus();
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	/**
	 * แก้ไขรายการ เดบิต/เครดิต
	 */
	public void onClick_btnUndoDet() {

		try {

			clearDetailBox();
			showDetail();
			setDisableButtonInGrid(false);
			cmbDRCR.focus();

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	private void validate_beforeNewRecord(FDbc dbc) throws Exception {

		if (tdbPOSTDATE.getValue() == null) {
			throw new Exception("ระบุช่อง " + tdbPOSTDATE.getTooltiptext());
		}
		if (!read_vou_type(txtVOU_TYPE.getValue())) {
			throw new Exception("ระบุช่อง " + txtVOU_TYPE.getTooltiptext());
		}
		// ==ตรวจการ Lock ลงบัญชี
		TbmACCT_LOCK.checKPostDate(dbc, this.getLoginBean().getCOMP_CDE(), FnDate.getSqlDate(tdbPOSTDATE.getValue()));

	}

	private void validate_beforeSaveDetail(FDbc dbc) throws Exception {

		if (cmbDRCR.getSelectedIndex() == -1) {
			throw new Exception("ยังไม่เลือกช่อง เดบิต/เครดิต");
		}
		if (!read_acct_id(txtACCT_ID.getValue())) {
			throw new Exception("ระบุช่อง รหัสบัญชี ไม่ถูกต้อง");
		}
		if (!Fnc.isEmpty(txtSECT_ID.getValue())) {
			if (!read_sect_id(txtSECT_ID.getValue())) {
				throw new Exception("ระบุช่อง แผนก ไม่ถูกต้อง");
			}
		}
		if (decAMT.getValue().compareTo(BigDecimal.ZERO) <= 0) {
			throw new Exception("ระบุช่อง จำนวน ไม่ถูกต้อง");
		}

	}

	private void clearDetailBox() {

		cmbDRCR.setSelectedIndex(0);
		txtACCT_ID.setValue("");
		txtACCT_NAME.setValue("");
		txtSECT_ID.setValue("");
		txtSECT_NAME.setValue("");
		decAMT.setValue(BigDecimal.ZERO);
		txtDESCR.setValue("");
		btnAddDet.setLabel("บันทีก(เพิ่ม)");
		btnUndoDet.setVisible(false);
		intEdit_VOU_SEQ.setValue(0);
		if (btnAp.isVisible()) {
			btnAp.setDisabled(false);
		}
		if (btnAr.isVisible()) {
			btnAr.setDisabled(false);
		}

	}

	private void setDisableButtonInGrid(boolean disable) {
		List<Component> lst_comp = gridAccList.getRows().getChildren();
		for (Component component : lst_comp) {
			Row row = (Row) component;
			if (row.getAttribute("btn_edit") != null) {

				Button btn_edit = (Button) row.getAttribute("btn_edit");
				btn_edit.setDisabled(disable);

				Button btn_del = (Button) row.getAttribute("btn_del");
				btn_del.setDisabled(disable);

				Button btn_move = (Button) row.getAttribute("btn_move");
				btn_move.setDisabled(disable);

				Button btn_sub = (Button) row.getAttribute("btn_sub");
				btn_sub.setDisabled(disable);

			}
		}
	}

	public void onChange_txtVOU_TYPE() {
		try {
			if (!read_vou_type(txtVOU_TYPE.getValue())) {
				txtVOU_NAME.setValue("");
			}
		} catch (Exception e) {
		}
	}

	public void onChange_txtACCT_ID() {
		try {
			if (!read_acct_id(txtACCT_ID.getValue())) {
				txtACCT_NAME.setValue("");
			}
		} catch (Exception e) {
		}
	}

	public void onChange_txtSECT_ID() {
		try {
			if (!read_sect_id(txtSECT_ID.getValue())) {
				txtSECT_NAME.setValue("");
			}
		} catch (Exception e) {
		}
	}

	public void onClick_BtnEditRow(org.zkoss.zk.ui.event.Event event) {

		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {

				clearDetailBox();

				Row row = (Row) event.getTarget().getParent();
				int vou_seq = Fnc.getIntValue(row.getAttribute("VOU_SEQ"));

				TboACGL_DETAIL detail = new TboACGL_DETAIL();

				detail.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				detail.setVOU_TYPE(txtVOU_TYPE.getValue());
				detail.setVOU_NO(txtVOU_NO.getValue());
				detail.setVOU_SEQ(vou_seq);

				if (TbfACGL_DETAIL.find(dbc, detail)) {

					if (Fnc.getStr(detail.getCHQ_TYPE()).equals("1")) {// 1=เช็ครอตัดโดยระบบ ,2=เช็ครอตัดยกมา
																		// (ไม่เอาไปออกใน gl และงบต่างๆ)
						if (!Fnc.isEmpty(detail.getCHQ_WD_VOU_NO())) {
							throw new Exception("มีการล้างเช็คจ่ายล่วงหน้าไปแล้ว");
						}
					} else {
						if (Fnc.getStr(detail.getSUB_APPR()).equals("1")) {// 0=ยังไม่ใส่ตัวย่อย,1=ใส่ตัวย่อยแล้ว,2=ไม่ใส่ตัวย่อย
							throw new Exception("ต้องลบตัวย่อยก่อน");
						}
					}

					if (detail.getRECSTA() != 0) {
						throw new Exception("สถานะเอกสารไม่ถูกต้องกรุณาตรวจสอบ");
					}

					if (detail.getAPAR_RECTYP() == 1) {// 1=ล้างเจ้าหนี้
						throw new Exception("แก้ไขไม่ได้ ลบได้อย่างเดียว");
					} else if (detail.getAPAR_RECTYP() == 2) {// 2=ล้างลูกหนี้
						throw new Exception("แก้ไขไม่ได้ ลบได้อย่างเดียว");
					}

					ZkUtil.setSelectItemComboBoxByValue(cmbDRCR, detail.getNUM_TYPE() + "");
					read_acct_id(detail.getACCT_ID());
					read_sect_id(detail.getSECT_ID());
					decAMT.setValue(detail.getAMT());
					txtDESCR.setValue(detail.getDESCR());
					intEdit_VOU_SEQ.setValue(vou_seq);

				} else {
					throw new Exception("ไม่พบรายการ");
				}

			}

			btnAddDet.setLabel("บันทีก(แก้ไข)");
			btnUndoDet.setVisible(true);
			setDisableButtonInGrid(true);
			if (btnAp.isVisible()) {
				btnAp.setDisabled(true);
			}
			if (btnAr.isVisible()) {
				btnAr.setDisabled(true);
			}
			cmbDRCR.focus();

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void onClick_BtnDelRow(org.zkoss.zk.ui.event.Event event) {
		Msg.confirm(Labels.getLabel("comm.label.deleteComfirm") + " ?", "?", (event1) -> {
			if (Messagebox.Button.YES.equals(event1.getButton())) {
				doOnClick_BtnDelRow(event);
			}
		});
	}

	public void doOnClick_BtnDelRow(org.zkoss.zk.ui.event.Event event) {

		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				Row row = (Row) event.getTarget().getParent();
				int vou_seq = Fnc.getIntValue(row.getAttribute("VOU_SEQ"));

				TboACGL_DETAIL detail = new TboACGL_DETAIL();

				detail.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				detail.setVOU_TYPE(txtVOU_TYPE.getValue());
				detail.setVOU_NO(txtVOU_NO.getValue());
				detail.setVOU_SEQ(vou_seq);

				if (TbfACGL_DETAIL.find(dbc, detail)) {

					if (detail.getRECSTA() != 0) {
						throw new Exception("สถานะเอกสารไม่ถูกต้องกรุณาตรวจสอบ");
					}

					if (Fnc.getStr(detail.getCHQ_TYPE()).equals("1")) {// 1=เช็ครอตัดโดยระบบ ,2=เช็ครอตัดยกมา
																		// (ไม่เอาไปออกใน gl และงบต่างๆ)
						if (!Fnc.isEmpty(detail.getCHQ_WD_VOU_NO())) {
							throw new Exception("มีการล้างเช็คจ่ายล่วงหน้าไปแล้ว");
						}
					} else {
						if (Fnc.getStr(detail.getSUB_APPR()).equals("1")) {// 0=ยังไม่ใส่ตัวย่อย,1=ใส่ตัวย่อยแล้ว,2=ไม่ใส่ตัวย่อย
							throw new Exception("ต้องลบตัวย่อยก่อน");
						}
					}

					if (detail.getAPAR_RECTYP() == 1) {// 1=ล้างเจ้าหนี้
						ManAcEntr.restore_ap_clear_amt(dbc, detail);
					} else if (detail.getAPAR_RECTYP() == 2) {// 2=ล้างลูกหนี้
						ManAcEntr.restore_ar_clear_amt(dbc, detail);
					}

					if (!TbfACGL_DETAIL.delete(dbc, detail)) {
						throw new Exception("ลบรายการไม่ได้ กรุณาตรวจสอบ");
					}

				} else {
					throw new Exception("ไม่พบรายการ");
				}

				dbc.commit();
			}

			showDetail();

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void onClick_BtnMoveRow(org.zkoss.zk.ui.event.Event event) {
		Row row = (Row) event.getTarget().getParent();
		Msg.inputbox("ย้ายไปลำดับที่", 200, 8, this, "doOnClick_BtnMoveRow", row);// trick inputbox แบบส่ง object row
																					// เข้าไปด้วย
	}

	public void doOnClick_BtnMoveRow(String seq, Object objRow) {
		try {

			showDetail();// เพื่อ refresh รายการให้เป็นปัจจุบัน

			int to_vouseq_show = Fnc.getIntFromStr(seq);
			if (to_vouseq_show == 0) {
				throw new Exception("ลำดับต้องมากกว่า 0");
			}

			Row row = (Row) objRow;
			int vou_seq = Fnc.getIntValue(row.getAttribute("VOU_SEQ"));
			TbmACGL_DETAIL.updateVouseq_Show(this.getLoginBean().getCOMP_CDE(), txtVOU_TYPE.getValue(),
					txtVOU_NO.getValue(), lst_dat, vou_seq, to_vouseq_show);
			showDetail();

		} catch (Exception e) {
			Msg.error(e);
		}
	}

	/**
	 * ตัวย่อย
	 * 
	 * @param event
	 */
	public void onClick_BtnSubRow(org.zkoss.zk.ui.event.Event event) {

		try {

			Row row = (Row) event.getTarget().getParent();
			int vou_seq = Fnc.getIntValue(row.getAttribute("VOU_SEQ"));

			TboACGL_DETAIL detail = new TboACGL_DETAIL();

			detail.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			detail.setVOU_TYPE(txtVOU_TYPE.getValue());
			detail.setVOU_NO(txtVOU_NO.getValue());
			detail.setVOU_SEQ(vou_seq);

			if (TbfACGL_DETAIL.find(detail)) {
				ManAcEntr.addSUB_GL(detail, this.getLoginBean(), false, this, "doShowDetail");
			}

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void onClick_btnAp() {

		try {
			if (!ZkUtil.isPopup("AcPayAp")) {

				TboACGL_HEADER acgl_header = new TboACGL_HEADER();

				acgl_header.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				acgl_header.setVOU_TYPE(txtVOU_TYPE.getValue());
				acgl_header.setVOU_NO(txtVOU_NO.getValue().trim());

				if (TbfACGL_HEADER.find(acgl_header)) {

					if (acgl_header.getRECSTA() != 0) {
						throw new Exception("สถานะรายการไม่ถูกต้อง");
					}

					AcPayAp fsub = (AcPayAp) ZkUtil.callZulFile("/com/pcc/gl/zul/AcEntr/AcPayAp.zul");
					fsub.setId("AcPayAp");// ต้องเรียกก่อนตัวอื่น
					fsub.setFormObj();// ต้องเรียกก่อนตัวอื่น
					fsub.setLoginBean(this.getLoginBean());
					fsub.setCallForm(this);
					fsub.setCallBackMethodName("doShowDetail");
					fsub.setAcgl_header(acgl_header);
					fsub.doModal();

				}

			}
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void onClick_btnAr() {

		try {
			if (!ZkUtil.isPopup("AcPayAr")) {

				TboACGL_HEADER acgl_header = new TboACGL_HEADER();

				acgl_header.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				acgl_header.setVOU_TYPE(txtVOU_TYPE.getValue());
				acgl_header.setVOU_NO(txtVOU_NO.getValue().trim());

				if (TbfACGL_HEADER.find(acgl_header)) {

					if (acgl_header.getRECSTA() != 0) {
						throw new Exception("สถานะรายการไม่ถูกต้อง");
					}

					AcPayAr fsub = (AcPayAr) ZkUtil.callZulFile("/com/pcc/gl/zul/AcEntr/AcPayAr.zul");
					fsub.setId("AcPayAr");// ต้องเรียกก่อนตัวอื่น
					fsub.setFormObj();// ต้องเรียกก่อนตัวอื่น
					fsub.setLoginBean(this.getLoginBean());
					fsub.setCallForm(this);
					fsub.setCallBackMethodName("doShowDetail");
					fsub.setAcgl_header(acgl_header);
					fsub.doModal();
				}

			}
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void popupVouType() {
		if (!txtVOU_TYPE.isReadonly()) {
			FfACCT_VOU_TYPE.popup("", this.getLoginBean(), this, "doPopupVouType");
		}
	}

	public void doPopupVouType(String vou_type) {
		try {
			read_vou_type(vou_type);
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void popupVouNo() {
		try {
			if (!txtVOU_NO.isReadonly()) {
				if (Fnc.isEmpty(txtVOU_TYPE.getValue())) {
					throw new Exception("ยังไม่ระบุช่อง \"" + txtVOU_TYPE.getTooltiptext() + "\" ");
				}
				FfACGL_HEADER.popup(txtVOU_TYPE.getValue(), txtVOU_NO.getValue(), this.getLoginBean(), this,
						"doPopupVouNo");
			}
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void doPopupVouNo(String vou_no) {

		try {
			if (check_record(vou_no)) {
				Msg.confirm("ต้องการแก้ไขข้อมูลใช่หรือไม่", "?", (event1) -> {
					if (Messagebox.Button.YES.equals(event1.getButton())) {
						try {
							showData(vou_no);
						} catch (Exception e) {
							Msg.error(e);
						}
					}
				});
			}
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void popupAcctNo() {
		if (!txtACCT_ID.isReadonly()) {
			FfACCT_NO.popup("", this.getLoginBean(), this, "doPopupAcctNo");
		}
	}

	public void doPopupAcctNo(String acct_id) {
		try {
			if (read_acct_id(acct_id)) {
				txtACCT_ID.focus();
			}
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void popupSectID() {
		try {
			if (!txtSECT_ID.isReadonly()) {
				FfFSECTION.popup("", this.getLoginBean(), this, "doPopupSectID");
			}
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void doPopupSectID(String sect_id) {
		try {
			if (read_sect_id(sect_id)) {
				txtSECT_ID.focus();
			}
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public boolean read_vou_type(String vou_type) throws Exception {
		boolean res = false;

		TboACCT_VOU_TYPE acctype = new TboACCT_VOU_TYPE();
		acctype.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		acctype.setVOU_TYPE(vou_type);
		if (TbfACCT_VOU_TYPE.find(acctype)) {
			txtVOU_TYPE.setValue(acctype.getVOU_TYPE());
			txtVOU_NAME.setValue(acctype.getVOU_NAME());
			res = true;
		}

		return res;
	}

	public boolean read_acct_id(String acct_id) throws Exception {
		boolean res = false;

		TboACCT_NO acct_no = new TboACCT_NO();
		acct_no.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		acct_no.setACCT_ID(acct_id);
		if (TbfACCT_NO.find(acct_no)) {
			txtACCT_ID.setValue(acct_no.getACCT_ID());
			txtACCT_NAME.setValue(acct_no.getACCT_NAME());
			res = true;
		}

		return res;
	}

	public boolean read_sect_id(String sect_id) throws Exception {
		boolean res = false;

		TboFSECTION fsect = new TboFSECTION();
		fsect.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		fsect.setSECT_ID(sect_id);
		if (TbfFSECTION.find(fsect)) {
			txtSECT_ID.setValue(fsect.getSECT_ID());
			txtSECT_NAME.setValue(fsect.getSECT_NAME());
			res = true;
		}

		return res;
	}

	public boolean check_record(String vou_no) throws Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return check_record(dbc, vou_no, null);
		}
	}

	public boolean check_record(FDbc dbc, String vou_no, boolean[] clear_on_error) throws Exception {
		boolean res = false;

		if (Fnc.isEmpty(txtVOU_TYPE.getValue())) {
			throw new Exception("ยังไม่ระบุช่อง \"" + txtVOU_TYPE.getTooltiptext() + "\"  ");
		}
		if (!read_vou_type(txtVOU_TYPE.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtVOU_TYPE.getTooltiptext() + "\" ไม่ถูกต้อง");
		}

		TboACGL_HEADER acch = new TboACGL_HEADER();

		acch.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		acch.setVOU_TYPE(txtVOU_TYPE.getValue());
		acch.setVOU_NO(vou_no);

		if (TbfACGL_HEADER.find(dbc, acch)) {

			// ถ้าอนุมัติแล้วแก้ไขไม่ได้
			if (acch.getRECSTA() == 2) {

				// check config กลางถ้าเป็น auto approve = false ให้ throw error
				if (!TbmFPARA_COMP.getString(this.getLoginBean().getCOMP_CDE(), "AC_AUTO_APPROVE").toUpperCase()
						.equals("YES")) {
					throw new Exception("รายการมีการอนุมัติแล้ว ไม่อนุญาติให้แก้ไข");
				}

			}

			// ==ตรวจการ Lock ลงบัญชี
			TbmACCT_LOCK.checKPostDate(dbc, this.getLoginBean().getCOMP_CDE(), acch.getPOSTDATE());

			if (acch.getRECSTA() == 9) {
				throw new Exception("รายการมีการยกเลิกแล้ว");
			}

			if (!Fnc.getStr(acch.getPOST_APP()).equals(this.sPOST_APP) && Fnc.isEmpty(acch.getPOST_APP())) {
				throw new Exception("เอกสารนี้ Auto มาจากเมนูอื่นแก้ไขไม่ได้");
			}

			if (acch.getRECSTA() == 0) { // ยังไม่บันทึก

				if (!Fnc.getStr(acch.getUPBY()).equals(this.getLoginBean().getUSER_ID())) {

					// ถ้ายังไม่เกิน 2 ชม. แก้ไขไม่ได้ต้องให้คนทำรายการมาบันทึกรายการเท่านั้น
					if (FnDate.getDayDiffInHour(acch.getUPDT(), FnDate.getTodayDateTime()) < 2) {
						if (clear_on_error != null) {
							clear_on_error[0] = true;
						}
						throw new Exception("กำลังถูกทำรายการโดย user : " + this.getLoginBean().getUSER_ID());
					}
				}

			}

			res = true;
		}

		return res;
	}

	public RowRenderer getRowRenderer1() {

		return (row, dat, index) -> {

			FModelHasMap rs = (FModelHasMap) dat;
			int seq = index + 1;
			row.setSclass("rowGrid1");

			row.appendChild(ZkUtil.gridLabel(seq + ""));

			Button btn_edit = new Button();
			btn_edit.setLabel("แก้ไข");
			btn_edit.setSclass("buttonedit");
			btn_edit.addEventListener(Events.ON_CLICK, (event) -> onClick_BtnEditRow(event));
			if (btnUndoDet.isVisible()) {// ถ้ากด next page ใน mode แก้ไขรายการให้ disable ไว้ตอน render
				btn_edit.setDisabled(true);
			}
			row.appendChild(btn_edit);
			if (rs.getInt("APAR_RECTYP") == 1 || rs.getInt("APAR_RECTYP") == 2) {// 1=ล้างเจ้าหนี้ , 2=ล้างลูกหนี้
				btn_edit.setVisible(false);
			}

			Button btn_del = new Button();
			btn_del.setLabel("ลบ");
			btn_del.setSclass("buttondel");
			btn_del.addEventListener(Events.ON_CLICK, (event) -> onClick_BtnDelRow(event));
			if (btnUndoDet.isVisible()) { // ถ้ากด next page ใน mode แก้ไขรายการให้ disable ไว้ตอน render
				btn_del.setDisabled(true);
			}
			row.appendChild(btn_del);

			Button btn_move = new Button();
			btn_move.setLabel("^");
			btn_move.setTooltiptext("ย้ายลำดับ");
			btn_move.setSclass("buttonmove");
			btn_move.addEventListener(Events.ON_CLICK, (event) -> onClick_BtnMoveRow(event));
			if (btnUndoDet.isVisible()) { // ถ้ากด next page ใน mode แก้ไขรายการให้ disable ไว้ตอน render
				btn_move.setDisabled(true);
			}
			row.appendChild(btn_move);

			Button btn_sub = new Button();
			btn_sub.setLabel("ตัวย่อย");
			btn_sub.setTooltiptext("ตัวย่อย");
			btn_sub.setSclass("buttonedit");
			btn_sub.addEventListener(Events.ON_CLICK, (event) -> onClick_BtnSubRow(event));
			if (btnUndoDet.isVisible()) { // ถ้ากด next page ใน mode แก้ไขรายการให้ disable ไว้ตอน render
				btn_sub.setDisabled(true);
			}
			if (rs.getInt("APAR_RECTYP") == 1 || rs.getInt("APAR_RECTYP") == 2) {// 1=ล้างเจ้าหนี้ , 2=ล้างลูกหนี้
				btn_sub.setVisible(false);
			} else {
				if (rs.getInt("SUB_TYPE") == 0) {// 0=ไม่มีตัวย่อย,1=เจ้าหนี้,2=ลูกหนี้, 3=เช็คจ่ายล่วงหน้า
					btn_sub.setVisible(false);
				} else {
					if (Fnc.getStr(rs.getString("SUB_APPR")).equals("1")) {// 0=ยังไม่ใส่ตัวย่อย,1=ใส่ตัวย่อยแล้ว,2=ไม่ใส่ตัวย่อย
						btn_sub.setSclass("buttoneditsub");
					}
				}
			}
			row.appendChild(btn_sub);

			row.appendChild(ZkUtil.gridTextbox(rs.getString("ACCT_ID")));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("ACCT_NAME")));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("SECT_ID")));
			if (rs.getBigDecimal("NUM_TYPE").intValue() == 1) {
				row.appendChild(ZkUtil.gridDecimalbox(rs.getBigDecimal("AMT")));
				row.appendChild(ZkUtil.gridTextbox(""));
			} else {
				row.appendChild(ZkUtil.gridTextbox(""));
				row.appendChild(ZkUtil.gridDecimalbox(rs.getBigDecimal("AMT")));
			}
			row.appendChild(ZkUtil.gridTextbox(Fnc.getStr(rs.getString("DESCR")) + " " + Fnc.getStr(rs.getString("DESCR_SUB"))));

			// === เพิ่ม Attribute
			row.setAttribute("VOU_SEQ", rs.getInt("VOU_SEQ"));
			row.setAttribute("VOU_SEQ_SHOW", rs.getInt("VOU_SEQ_SHOW"));
			row.setAttribute("btn_edit", btn_edit);
			row.setAttribute("btn_del", btn_del);
			row.setAttribute("btn_move", btn_move);
			row.setAttribute("btn_sub", btn_sub);

		};

	}

}

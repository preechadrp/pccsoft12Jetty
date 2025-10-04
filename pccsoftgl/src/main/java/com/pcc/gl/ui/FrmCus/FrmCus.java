package com.pcc.gl.ui.FrmCus;


import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.pcc.gl.find.FfFCUS;
import com.pcc.gl.tbf.TbfFCUS;
import com.pcc.gl.tbf.TbfFCUS_ADDR;
import com.pcc.gl.tbm.TbmFCUS;
import com.pcc.gl.tbo.TboFCUS;
import com.pcc.gl.tbo.TboFCUS_ADDR;
import com.pcc.sys.find.FfAMPHURID;
import com.pcc.sys.find.FfFPROVINCE;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.ZkUtil;
import com.pcc.sys.progman.RunningComm;
import com.pcc.sys.tbf.TbfFAMPHUR;
import com.pcc.sys.tbf.TbfFPROVINCE;
import com.pcc.sys.tbm.TbmFPARA_COMP;
import com.pcc.sys.tbo.TboFAMPHUR;
import com.pcc.sys.tbo.TboFPROVINCE;

public class FrmCus extends FWinMenu {

	private static final long serialVersionUID = 1L;
	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public Textbox txtCUST_CDE;
	public Textbox txtTITLE;
	public Textbox txtFNAME;
	public Textbox txtLNAME;
	public Combobox cmbCUSTYP;
	public Intbox intAR_CREDIT;
	public Intbox intAP_CREDIT;
	public Textbox txtIDNO, txtBRANCH_CODE;
	public Textbox txtTEL1, txtTEL2, txtTEL3;
	public Textbox txtFAXNO1;

	public Textbox txtVILAPT1;
	public Textbox txtFLOOR1;
	public Textbox txtROOM1;
	public Textbox txtADDRESS1;
	public Textbox txtMOO1;
	public Textbox txtSOI1;
	public Textbox txtROAD1;
	public Textbox txtTAMBOLNAME1;
	public Textbox txtPROVIN_ID1;
	public Textbox txtPROVIN_NAME1;
	public Textbox txtAMPHURID1;
	public Textbox txtAMPHURNAME1;
	public Textbox txtZIPCODE1;
	public Textbox txtVILAPT2;
	public Textbox txtFLOOR2;
	public Textbox txtROOM2;
	public Textbox txtADDRESS2;
	public Textbox txtMOO2;
	public Textbox txtSOI2;
	public Textbox txtROAD2;
	public Textbox txtTAMBOLNAME2;
	public Textbox txtPROVIN_ID2;
	public Textbox txtPROVIN_NAME2;
	public Textbox txtAMPHURID2;
	public Textbox txtAMPHURNAME2;
	public Textbox txtZIPCODE2;
	public Textbox txtVILAPT3;
	public Textbox txtFLOOR3;
	public Textbox txtROOM3;
	public Textbox txtADDRESS3;
	public Textbox txtMOO3;
	public Textbox txtSOI3;
	public Textbox txtROAD3;
	public Textbox txtTAMBOLNAME3;
	public Textbox txtPROVIN_ID3;
	public Textbox txtPROVIN_NAME3;
	public Textbox txtAMPHURID3;
	public Textbox txtAMPHURNAME3;
	public Textbox txtZIPCODE3;

	public Textbox txtINSDT, txtINSBY, txtUPDT, txtUPBY;

	public Div div1;

	public Button btnExit, btnBack, btnSave, btnDelete, btnAdd;
	public Button btnCard;

	private int selPopupFlg = 0;

	@Override
	public void setFormObj() {
		txtCUST_CDE = (Textbox) getFellow("txtCUST_CDE");
		txtTITLE = (Textbox) getFellow("txtTITLE");
		txtFNAME = (Textbox) getFellow("txtFNAME");
		txtLNAME = (Textbox) getFellow("txtLNAME");
		cmbCUSTYP = (Combobox) getFellow("cmbCUSTYP");
		txtIDNO = (Textbox) getFellow("txtIDNO");
		txtBRANCH_CODE = (Textbox) getFellow("txtBRANCH_CODE");
		intAR_CREDIT = (Intbox) getFellow("intAR_CREDIT");
		intAP_CREDIT = (Intbox) getFellow("intAP_CREDIT");
		txtTEL1 = (Textbox) getFellow("txtTEL1");
		txtTEL2 = (Textbox) getFellow("txtTEL2");
		txtTEL3 = (Textbox) getFellow("txtTEL3");
		txtFAXNO1 = (Textbox) getFellow("txtFAXNO1");

		txtVILAPT1 = (Textbox) getFellow("txtVILAPT1");
		txtFLOOR1 = (Textbox) getFellow("txtFLOOR1");
		txtROOM1 = (Textbox) getFellow("txtROOM1");
		txtADDRESS1 = (Textbox) getFellow("txtADDRESS1");
		txtMOO1 = (Textbox) getFellow("txtMOO1");
		txtSOI1 = (Textbox) getFellow("txtSOI1");
		txtROAD1 = (Textbox) getFellow("txtROAD1");
		txtTAMBOLNAME1 = (Textbox) getFellow("txtTAMBOLNAME1");
		txtPROVIN_ID1 = (Textbox) getFellow("txtPROVIN_ID1");
		txtPROVIN_NAME1 = (Textbox) getFellow("txtPROVIN_NAME1");
		txtAMPHURID1 = (Textbox) getFellow("txtAMPHURID1");
		txtAMPHURNAME1 = (Textbox) getFellow("txtAMPHURNAME1");
		txtZIPCODE1 = (Textbox) getFellow("txtZIPCODE1");
		txtVILAPT2 = (Textbox) getFellow("txtVILAPT2");
		txtFLOOR2 = (Textbox) getFellow("txtFLOOR2");
		txtROOM2 = (Textbox) getFellow("txtROOM2");
		txtADDRESS2 = (Textbox) getFellow("txtADDRESS2");
		txtMOO2 = (Textbox) getFellow("txtMOO2");
		txtSOI2 = (Textbox) getFellow("txtSOI2");
		txtROAD2 = (Textbox) getFellow("txtROAD2");
		txtTAMBOLNAME2 = (Textbox) getFellow("txtTAMBOLNAME2");
		txtPROVIN_ID2 = (Textbox) getFellow("txtPROVIN_ID2");
		txtPROVIN_NAME2 = (Textbox) getFellow("txtPROVIN_NAME2");
		txtAMPHURID2 = (Textbox) getFellow("txtAMPHURID2");
		txtAMPHURNAME2 = (Textbox) getFellow("txtAMPHURNAME2");
		txtZIPCODE2 = (Textbox) getFellow("txtZIPCODE2");
		txtVILAPT3 = (Textbox) getFellow("txtVILAPT3");
		txtFLOOR3 = (Textbox) getFellow("txtFLOOR3");
		txtROOM3 = (Textbox) getFellow("txtROOM3");
		txtADDRESS3 = (Textbox) getFellow("txtADDRESS3");
		txtMOO3 = (Textbox) getFellow("txtMOO3");
		txtSOI3 = (Textbox) getFellow("txtSOI3");
		txtROAD3 = (Textbox) getFellow("txtROAD3");
		txtTAMBOLNAME3 = (Textbox) getFellow("txtTAMBOLNAME3");
		txtPROVIN_ID3 = (Textbox) getFellow("txtPROVIN_ID3");
		txtPROVIN_NAME3 = (Textbox) getFellow("txtPROVIN_NAME3");
		txtAMPHURID3 = (Textbox) getFellow("txtAMPHURID3");
		txtAMPHURNAME3 = (Textbox) getFellow("txtAMPHURNAME3");
		txtZIPCODE3 = (Textbox) getFellow("txtZIPCODE3");

		txtINSDT = (Textbox) getFellow("txtINSDT");
		txtINSBY = (Textbox) getFellow("txtINSBY");
		txtUPDT = (Textbox) getFellow("txtUPDT");
		txtUPBY = (Textbox) getFellow("txtUPBY");

		div1 = (Div) getFellow("div1");

		btnExit = (Button) getFellow("btnExit");
		btnBack = (Button) getFellow("btnBack");
		btnSave = (Button) getFellow("btnSave");
		btnDelete = (Button) getFellow("btnDelete");
		btnAdd = (Button) getFellow("btnAdd");
		btnCard = (Button) getFellow("btnCard");
	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(txtCUST_CDE);
		addEnterIndex(btnAdd);
		addEnterIndex(txtTITLE);
		addEnterIndex(txtFNAME);
		addEnterIndex(txtLNAME);
		addEnterIndex(cmbCUSTYP);
		addEnterIndex(txtIDNO);
		addEnterIndex(txtBRANCH_CODE);
		addEnterIndex(intAR_CREDIT);
		addEnterIndex(intAP_CREDIT);
		addEnterIndex(txtTEL1);
		addEnterIndex(txtTEL2);
		addEnterIndex(txtTEL3);
		addEnterIndex(txtFAXNO1);

		addEnterIndex(txtVILAPT1);
		addEnterIndex(txtFLOOR1);
		addEnterIndex(txtROOM1);
		addEnterIndex(txtADDRESS1);
		addEnterIndex(txtMOO1);
		addEnterIndex(txtSOI1);
		addEnterIndex(txtROAD1);
		addEnterIndex(txtTAMBOLNAME1);
		addEnterIndex(txtPROVIN_ID1);
		//addEnterIndex(txtPROVIN_NAME1);
		addEnterIndex(txtAMPHURID1);
		//addEnterIndex(txtAMPHURNAME1);
		addEnterIndex(txtZIPCODE1);
		addEnterIndex(txtVILAPT2);
		addEnterIndex(txtFLOOR2);
		addEnterIndex(txtROOM2);
		addEnterIndex(txtADDRESS2);
		addEnterIndex(txtMOO2);
		addEnterIndex(txtSOI2);
		addEnterIndex(txtROAD2);
		addEnterIndex(txtTAMBOLNAME2);
		addEnterIndex(txtPROVIN_ID2);
		//addEnterIndex(txtPROVIN_NAME2);
		addEnterIndex(txtAMPHURID2);
		//addEnterIndex(txtAMPHURNAME2);
		addEnterIndex(txtZIPCODE2);
		addEnterIndex(txtVILAPT3);
		addEnterIndex(txtFLOOR3);
		addEnterIndex(txtROOM3);
		addEnterIndex(txtADDRESS3);
		addEnterIndex(txtMOO3);
		addEnterIndex(txtSOI3);
		addEnterIndex(txtROAD3);
		addEnterIndex(txtTAMBOLNAME3);
		addEnterIndex(txtPROVIN_ID3);
		//addEnterIndex(txtPROVIN_NAME3);
		addEnterIndex(txtAMPHURID3);
		//addEnterIndex(txtAMPHURNAME3);
		addEnterIndex(txtZIPCODE3);

		addEnterIndex(btnSave);

	}

	@Override
	public void formInit() {
		try {
			clearData();
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	private void clearData() {
		div1.setVisible(false);
		txtCUST_CDE.setValue("");
		txtCUST_CDE.setReadonly(false);
		txtCUST_CDE.focus();
		txtTITLE.setValue("");
		txtFNAME.setValue("");
		txtLNAME.setValue("");
		cmbCUSTYP.setSelectedIndex(-1);
		intAR_CREDIT.setValue(0);
		intAP_CREDIT.setValue(0);
		txtIDNO.setValue("");
		txtBRANCH_CODE.setValue("");
		txtTEL1.setValue("");
		txtTEL2.setValue("");
		txtTEL3.setValue("");
		txtFAXNO1.setValue("");

		txtVILAPT1.setValue("");
		txtFLOOR1.setValue("");
		txtROOM1.setValue("");
		txtADDRESS1.setValue("");
		txtMOO1.setValue("");
		txtSOI1.setValue("");
		txtROAD1.setValue("");
		txtTAMBOLNAME1.setValue("");
		txtPROVIN_ID1.setValue("");
		txtPROVIN_NAME1.setValue("");
		txtAMPHURID1.setValue("");
		txtAMPHURNAME1.setValue("");
		txtZIPCODE1.setValue("");
		txtVILAPT2.setValue("");
		txtFLOOR2.setValue("");
		txtROOM2.setValue("");
		txtADDRESS2.setValue("");
		txtMOO2.setValue("");
		txtSOI2.setValue("");
		txtROAD2.setValue("");
		txtTAMBOLNAME2.setValue("");
		txtPROVIN_ID2.setValue("");
		txtPROVIN_NAME2.setValue("");
		txtAMPHURID2.setValue("");
		txtAMPHURNAME2.setValue("");
		txtZIPCODE2.setValue("");
		txtVILAPT3.setValue("");
		txtFLOOR3.setValue("");
		txtROOM3.setValue("");
		txtADDRESS3.setValue("");
		txtMOO3.setValue("");
		txtSOI3.setValue("");
		txtROAD3.setValue("");
		txtTAMBOLNAME3.setValue("");
		txtPROVIN_ID3.setValue("");
		txtPROVIN_NAME3.setValue("");
		txtAMPHURID3.setValue("");
		txtAMPHURNAME3.setValue("");
		txtZIPCODE3.setValue("");

		txtINSBY.setValue("");
		txtINSDT.setValue("");
		txtUPBY.setValue("");
		txtUPDT.setValue("");

		btnExit.setVisible(true);
		btnBack.setVisible(false);
		btnSave.setVisible(false);
		btnDelete.setVisible(false);
		btnAdd.setVisible(true);
		btnCard.setVisible(true);

	}

	public void onOK() {
		try {
			if (focusObj == txtCUST_CDE) {
				if (!txtCUST_CDE.isReadonly()) {
					if (Fnc.isEmpty(txtCUST_CDE.getValue())) {
						super.onOK();//จะเลื่อนไปปุ่ม btnAdd ถ้า visible อยู่
					} else if (read_record(txtCUST_CDE.getValue().trim())) {
						txtTITLE.focus();
					} else {
						throw new Exception("ไม่พบ" + txtCUST_CDE.getTooltiptext());
					}
				}
			} else if (focusObj == txtPROVIN_ID1) {
				if (ZkUtil.doOnChange(txtPROVIN_ID1) == false) {
					if (Fnc.isEmpty(txtPROVIN_ID1.getValue())) {
						popupProvin(1);
					} else if (read_province(Fnc.getIntFromStr(txtPROVIN_ID1.getValue()), 1)) {
						super.onOK();
					} else {
						popupProvin(1);
					}
				} else {
					super.onOK();
				}
			} else if (focusObj == txtPROVIN_ID2) {
				if (ZkUtil.doOnChange(txtPROVIN_ID2) == false) {
					if (Fnc.isEmpty(txtPROVIN_ID2.getValue())) {
						popupProvin(2);
					} else if (read_province(Fnc.getIntFromStr(txtPROVIN_ID2.getValue()), 2)) {
						super.onOK();
					} else {
						popupProvin(2);
					}
				} else {
					super.onOK();
				}
			} else if (focusObj == txtPROVIN_ID3) {
				if (ZkUtil.doOnChange(txtPROVIN_ID3) == false) {
					if (Fnc.isEmpty(txtPROVIN_ID3.getValue())) {
						popupProvin(3);
					} else if (read_province(Fnc.getIntFromStr(txtPROVIN_ID3.getValue()), 3)) {
						super.onOK();
					} else {
						popupProvin(3);
					}
				} else {
					super.onOK();
				}

			} else if (focusObj == txtAMPHURID1) {
				if (ZkUtil.doOnChange(txtAMPHURID1) == false) {
					if (Fnc.isEmpty(txtAMPHURID1.getValue())) {
						popupAMPHURID(1);
					} else if (read_amphur(Fnc.getIntFromStr(txtAMPHURID1.getValue()), 1)) {
						super.onOK();
					} else {
						popupAMPHURID(1);
					}
				} else {
					super.onOK();
				}
			} else if (focusObj == txtAMPHURID2) {
				if (ZkUtil.doOnChange(txtAMPHURID2) == false) {
					if (Fnc.isEmpty(txtAMPHURID2.getValue())) {
						popupAMPHURID(2);
					} else if (read_amphur(Fnc.getIntFromStr(txtAMPHURID2.getValue()), 2)) {
						super.onOK();
					} else {
						popupAMPHURID(2);
					}
				} else {
					super.onOK();
				}
			} else if (focusObj == txtAMPHURID3) {
				if (ZkUtil.doOnChange(txtAMPHURID3) == false) {
					if (Fnc.isEmpty(txtAMPHURID3.getValue())) {
						popupAMPHURID(3);
					} else if (read_amphur(Fnc.getIntFromStr(txtAMPHURID3.getValue()), 3)) {
						super.onOK();
					} else {
						popupAMPHURID(3);
					}
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

	public void onClick_Back() {
		clearData();
	}

	private void validateData() throws Exception {

		if (Fnc.isEmpty(txtCUST_CDE.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtCUST_CDE.getTooltiptext() + "\" ");
		}

		if (ZkUtil.getSelectItemValueComboBox(cmbCUSTYP).equals("")) {
			throw new Exception("ระบุช่อง \"" + cmbCUSTYP.getTooltiptext() + "\" ");
		}

		if (Fnc.isEmpty(txtFNAME.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtFNAME.getTooltiptext() + "\" ");
		}

		if (Fnc.getIntValue(intAR_CREDIT.getValue()) < 0) {
			throw new Exception("ระบุช่อง \"" + intAR_CREDIT.getTooltiptext() + "\" ไม่ถูกต้อง");
		}

		if (Fnc.getIntValue(intAP_CREDIT.getValue()) < 0) {
			throw new Exception("ระบุช่อง \"" + intAP_CREDIT.getTooltiptext() + "\" ไม่ถูกต้อง");
		}

	}

	public void onClick_Save() {

		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				validateData();

				//== TboFCUS
				TboFCUS cust = new TboFCUS();

				cust.setCOMP_CDE(getLoginBean().getCOMP_CDE());
				cust.setCUST_CDE(txtCUST_CDE.getValue().trim());

				if (!TbfFCUS.find(dbc, cust)) {
					throw new Exception("ข้อมูลถูกลบโดยผู้ใช้อื่นขณะทำรายการ");
				}

				cust.setCUSTYP(ZkUtil.getSelectItemValueComboBox(cmbCUSTYP));
				cust.setTITLE(txtTITLE.getValue());
				cust.setFNAME(txtFNAME.getValue());
				cust.setLNAME(txtLNAME.getValue());
				cust.setIDNO(txtIDNO.getValue());
				if (ZkUtil.getSelectItemValueComboBox(cmbCUSTYP).equals("2")) {
					cust.setBRANCH_CODE(txtBRANCH_CODE.getValue());
				} else {
					cust.setBRANCH_CODE("");
				}
				cust.setAR_CREDIT(intAR_CREDIT.getValue());
				cust.setAP_CREDIT(intAP_CREDIT.getValue());
				cust.setTEL1(txtTEL1.getValue());
				cust.setTEL2(txtTEL2.getValue());
				cust.setTEL3(txtTEL3.getValue());
				cust.setFAXNO1(txtFAXNO1.getValue());

				cust.setUPBY(getLoginBean().getUSER_ID());
				cust.setUPDT(FnDate.getTodaySqlDateTime());

				TbfFCUS.update(dbc, cust, "");

				//== fcus_addr
				{//ที่อยู่ตามทะเบียนบ้าน
					TboFCUS_ADDR cusAdd = new TboFCUS_ADDR();

					cusAdd.setCOMP_CDE(cust.getCOMP_CDE());
					cusAdd.setCUST_CDE(cust.getCUST_CDE());
					cusAdd.setADDR_TYP("1");

					boolean old_rec = TbfFCUS_ADDR.find(dbc, cusAdd);

					cusAdd.setVILAPT1(txtVILAPT1.getValue());
					cusAdd.setFLOOR1(txtFLOOR1.getValue());
					cusAdd.setROOM1(txtROOM1.getValue());
					cusAdd.setADDRESS1(txtADDRESS1.getValue());
					cusAdd.setMOO1(txtMOO1.getValue());
					cusAdd.setSOI1(txtSOI1.getValue());
					cusAdd.setROAD1(txtROAD1.getValue());
					cusAdd.setTAMBOLNAME(txtTAMBOLNAME1.getValue());
					cusAdd.setPROVIN_ID(Fnc.getIntFromStr(txtPROVIN_ID1.getValue()));
					cusAdd.setPROVIN_NAME(txtPROVIN_NAME1.getValue());
					cusAdd.setAMPHURID(Fnc.getIntFromStr(txtAMPHURID1.getValue()));
					cusAdd.setAMPHURNAME(txtAMPHURNAME1.getValue());
					cusAdd.setZIPCODE(txtZIPCODE1.getValue());
					cusAdd.setCUST_REMARK("");

					if (old_rec) {
						TbfFCUS_ADDR.update(dbc, cusAdd, "");
					} else {
						TbfFCUS_ADDR.insert(dbc, cusAdd);
					}

				}

				{//ที่อยู่ส่งเอกสาร
					TboFCUS_ADDR cusAdd = new TboFCUS_ADDR();

					cusAdd.setCOMP_CDE(cust.getCOMP_CDE());
					cusAdd.setCUST_CDE(cust.getCUST_CDE());
					cusAdd.setADDR_TYP("2");

					boolean old_rec = TbfFCUS_ADDR.find(dbc, cusAdd);

					cusAdd.setVILAPT1(txtVILAPT2.getValue());
					cusAdd.setFLOOR1(txtFLOOR2.getValue());
					cusAdd.setROOM1(txtROOM2.getValue());
					cusAdd.setADDRESS1(txtADDRESS2.getValue());
					cusAdd.setMOO1(txtMOO2.getValue());
					cusAdd.setSOI1(txtSOI2.getValue());
					cusAdd.setROAD1(txtROAD2.getValue());
					cusAdd.setTAMBOLNAME(txtTAMBOLNAME2.getValue());
					cusAdd.setPROVIN_ID(Fnc.getIntFromStr(txtPROVIN_ID2.getValue()));
					cusAdd.setPROVIN_NAME(txtPROVIN_NAME2.getValue());
					cusAdd.setAMPHURID(Fnc.getIntFromStr(txtAMPHURID2.getValue()));
					cusAdd.setAMPHURNAME(txtAMPHURNAME2.getValue());
					cusAdd.setZIPCODE(txtZIPCODE2.getValue());
					cusAdd.setCUST_REMARK("");

					if (old_rec) {
						TbfFCUS_ADDR.update(dbc, cusAdd, "");
					} else {
						TbfFCUS_ADDR.insert(dbc, cusAdd);
					}

				}

				{//ที่อยู่ปัจจุบัน
					TboFCUS_ADDR cusAdd = new TboFCUS_ADDR();

					cusAdd.setCOMP_CDE(cust.getCOMP_CDE());
					cusAdd.setCUST_CDE(cust.getCUST_CDE());
					cusAdd.setADDR_TYP("3");

					boolean old_rec = TbfFCUS_ADDR.find(dbc, cusAdd);

					cusAdd.setVILAPT1(txtVILAPT3.getValue());
					cusAdd.setFLOOR1(txtFLOOR3.getValue());
					cusAdd.setROOM1(txtROOM3.getValue());
					cusAdd.setADDRESS1(txtADDRESS3.getValue());
					cusAdd.setMOO1(txtMOO3.getValue());
					cusAdd.setSOI1(txtSOI3.getValue());
					cusAdd.setROAD1(txtROAD3.getValue());
					cusAdd.setTAMBOLNAME(txtTAMBOLNAME3.getValue());
					cusAdd.setPROVIN_ID(Fnc.getIntFromStr(txtPROVIN_ID3.getValue()));
					cusAdd.setPROVIN_NAME(txtPROVIN_NAME3.getValue());
					cusAdd.setAMPHURID(Fnc.getIntFromStr(txtAMPHURID3.getValue()));
					cusAdd.setAMPHURNAME(txtAMPHURNAME3.getValue());
					cusAdd.setZIPCODE(txtZIPCODE3.getValue());
					cusAdd.setCUST_REMARK("");

					if (old_rec) {
						TbfFCUS_ADDR.update(dbc, cusAdd, "");
					} else {
						TbfFCUS_ADDR.insert(dbc, cusAdd);
					}

				}

				dbc.commit();

			}

			clearData();

			Msg.info("บันทึกเรียบร้อย");

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

						//==
						TboFCUS cus = new TboFCUS();

						cus.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
						cus.setCUST_CDE(txtCUST_CDE.getValue().trim());

						if (!TbfFCUS.delete(dbc, cus)) {
							throw new Exception("ไม่สามารถลบข้อมูลได้");
						}

						//==
						TboFCUS_ADDR cusAdd = new TboFCUS_ADDR();

						cusAdd.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
						cusAdd.setCUST_CDE(txtCUST_CDE.getValue().trim());

						cusAdd.setADDR_TYP("1");//1=ตามทะเบียนบ้าน
						TbfFCUS_ADDR.delete(dbc, cusAdd);

						cusAdd.setADDR_TYP("2");//2=ที่ส่งเอกสาร
						TbfFCUS_ADDR.delete(dbc, cusAdd);

						cusAdd.setADDR_TYP("3");//3=ที่อยู่ปัจจุบัน
						TbfFCUS_ADDR.delete(dbc, cusAdd);

						dbc.commit();
					}

					clearData();
					Msg.info("ลบข้อมูลเรียบร้อยแล้ว");

				} catch (Exception e) {
					Msg.error(e);
				}
			}

		});

	}

	public void onClick_btnAdd() {

		try {

			//รหัสลูกค้า ว่างๆ=Auto, 1=ระบุเอง,2=ถ้าว่าจะ Auto หรือ ระบุเอง
			if (Fnc.isEmpty(TbmFPARA_COMP.getString(this.getLoginBean().getCOMP_CDE(), "CM_CUST_ADD_MODE"))) {

				String cust_cde = RunningComm.genCustno(this.getLoginBean());
				doOnClick_btnAdd(cust_cde);

			} else if (TbmFPARA_COMP.getString(this.getLoginBean().getCOMP_CDE(), "CM_CUST_ADD_MODE").equals("1")) {//1=ระบุเอง

				doAddCust_cdeManaul();

			} else if (TbmFPARA_COMP.getString(this.getLoginBean().getCOMP_CDE(), "CM_CUST_ADD_MODE").equals("2")) {//2=ถ้าว่าจะ Auto หรือ ระบุเอง

				Msg.confirm("ต้องการสร้างรหัสลูกค้า Auto ใช่หรือไม่", "?", (event1) -> {
					if (Messagebox.Button.YES.equals(event1.getButton())) {
						String cust_cde = RunningComm.genCustno(getLoginBean());
						doOnClick_btnAdd(cust_cde);
					} else {
						doAddCust_cdeManaul();
					}
				});

			} else {
				throw new Exception("Config ระบบไม่ถูกต้อง (FPARA_SYS->CM_CUST_ADD_MODE)");
			}

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void doAddCust_cdeManaul() {
		Msg.inputbox("ใส่รหัสใหม่", 200, txtCUST_CDE.getMaxlength(), this, "doOnClick_btnAdd", null);
	}

	public void doOnClick_btnAdd(String cust_cde) {

		try {

			if (cust_cde.trim().length() < 7) {
				throw new Exception("ต้องมีความยาวตั้งแต่ 7 อักขระขึ้นไป");
			}

			Fnc.isPassCode(cust_cde);

			if (TbmFCUS.checkCUST_CDE(getLoginBean(), cust_cde)) {
				throw new Exception("มีรหัสนี้แล้ว");
			}

			TboFCUS cust = new TboFCUS();

			cust.setCOMP_CDE(getLoginBean().getCOMP_CDE());
			cust.setCUST_CDE(cust_cde);
			cust.setINSBY(getLoginBean().getUSER_ID());
			cust.setINSDT(FnDate.getTodaySqlDateTime());

			TbfFCUS.insert(cust);

			read_record(cust_cde);

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void onChange_Data(Component comp) {
		try {
			if (comp.getId().equals("txtPROVIN_ID1")) {
				ZkUtil.setAndClearOnChang(txtPROVIN_ID1, this.objectList);
				read_province(Fnc.getIntFromStr(txtPROVIN_ID1.getValue()), 1);
			}
			if (comp.getId().equals("txtPROVIN_ID2")) {
				ZkUtil.setAndClearOnChang(txtPROVIN_ID2, this.objectList);
				read_province(Fnc.getIntFromStr(txtPROVIN_ID2.getValue()), 2);
			}
			if (comp.getId().equals("txtPROVIN_ID3")) {
				ZkUtil.setAndClearOnChang(txtPROVIN_ID3, this.objectList);
				read_province(Fnc.getIntFromStr(txtPROVIN_ID3.getValue()), 3);
			}

			if (comp.getId().equals("txtAMPHURID1")) {
				ZkUtil.setAndClearOnChang(txtAMPHURID1, this.objectList);
				read_amphur(Fnc.getIntFromStr(txtAMPHURID1.getValue()), 1);
			}
			if (comp.getId().equals("txtAMPHURID2")) {
				ZkUtil.setAndClearOnChang(txtAMPHURID2, this.objectList);
				read_amphur(Fnc.getIntFromStr(txtAMPHURID2.getValue()), 2);
			}
			if (comp.getId().equals("txtAMPHURID3")) {
				ZkUtil.setAndClearOnChang(txtAMPHURID3, this.objectList);
				read_amphur(Fnc.getIntFromStr(txtAMPHURID3.getValue()), 3);
			}

		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void popupData() {
		if (!txtCUST_CDE.isReadonly()) {
			FfFCUS.popup(this.getLoginBean(), this, "doPopupData");
		}
	}

	public void doPopupData(String cust_cde) {
		try {
			if (read_record(cust_cde)) {
				txtTITLE.focus();
			} else {
				txtCUST_CDE.focus();
			}
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void popupProvin(int selflg) {
		this.selPopupFlg = selflg;
		FfFPROVINCE.popup("", getLoginBean(), this, "doPopupProvin");
	}

	public void doPopupProvin(int provin_id) {
		try {
			read_province(provin_id, this.selPopupFlg);
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	private boolean read_record(String cust_cde) throws Exception {
		boolean res = false;

		TboFCUS cust = new TboFCUS();

		cust.setCOMP_CDE(getLoginBean().getCOMP_CDE());
		cust.setCUST_CDE(cust_cde);

		if (TbfFCUS.find(cust)) {

			txtCUST_CDE.setValue(cust.getCUST_CDE());
			txtTITLE.setValue(cust.getTITLE()); // ป้องกันตัวเล็กใหญ่
			txtFNAME.setValue(cust.getFNAME());
			txtLNAME.setValue(cust.getLNAME());
			ZkUtil.setSelectItemComboBoxByValue(cmbCUSTYP, cust.getCUSTYP());
			intAR_CREDIT.setValue(cust.getAR_CREDIT());
			intAP_CREDIT.setValue(cust.getAP_CREDIT());
			txtIDNO.setValue(cust.getIDNO());
			txtBRANCH_CODE.setValue(cust.getBRANCH_CODE());
			txtTEL1.setValue(cust.getTEL1());
			txtTEL2.setValue(cust.getTEL2());
			txtTEL3.setValue(cust.getTEL3());
			txtFAXNO1.setValue(cust.getFAXNO1());

			txtINSBY.setValue(cust.getINSBY());
			txtINSDT.setValue(FnDate.displayDateTimeString(cust.getINSDT()));
			txtUPBY.setValue(cust.getUPBY());
			txtUPDT.setValue(FnDate.displayDateTimeString(cust.getUPDT()));

			//==addr1
			{
				TboFCUS_ADDR cusAdd = new TboFCUS_ADDR();

				cusAdd.setCOMP_CDE(cust.getCOMP_CDE());
				cusAdd.setCUST_CDE(cust.getCUST_CDE());
				cusAdd.setADDR_TYP("1");

				if (TbfFCUS_ADDR.find(cusAdd)) {

					txtVILAPT1.setValue(cusAdd.getVILAPT1());
					txtFLOOR1.setValue(cusAdd.getFLOOR1());
					txtROOM1.setValue(cusAdd.getROOM1());
					txtADDRESS1.setValue(cusAdd.getADDRESS1());
					txtMOO1.setValue(cusAdd.getMOO1());
					txtSOI1.setValue(cusAdd.getSOI1());
					txtROAD1.setValue(cusAdd.getROAD1());
					txtTAMBOLNAME1.setValue(cusAdd.getTAMBOLNAME());
					txtPROVIN_ID1.setValue(cusAdd.getPROVIN_ID() + "");
					txtPROVIN_NAME1.setValue(cusAdd.getPROVIN_NAME());
					txtAMPHURID1.setValue(cusAdd.getAMPHURID() + "");
					txtAMPHURNAME1.setValue(cusAdd.getAMPHURNAME());
					txtZIPCODE1.setValue(cusAdd.getZIPCODE());

					//read_amphur(cusAdd.getAMPHURID(), 1);
					//read_province(cusAdd.getPROVIN_ID(), 1);

				}

			}

			//==addr2
			{
				TboFCUS_ADDR cusAdd = new TboFCUS_ADDR();

				cusAdd.setCOMP_CDE(cust.getCOMP_CDE());
				cusAdd.setCUST_CDE(cust.getCUST_CDE());
				cusAdd.setADDR_TYP("2");

				if (TbfFCUS_ADDR.find(cusAdd)) {

					txtVILAPT2.setValue(cusAdd.getVILAPT1());
					txtFLOOR2.setValue(cusAdd.getFLOOR1());
					txtROOM2.setValue(cusAdd.getROOM1());
					txtADDRESS2.setValue(cusAdd.getADDRESS1());
					txtMOO2.setValue(cusAdd.getMOO1());
					txtSOI2.setValue(cusAdd.getSOI1());
					txtROAD2.setValue(cusAdd.getROAD1());
					txtTAMBOLNAME2.setValue(cusAdd.getTAMBOLNAME());
					txtPROVIN_ID2.setValue(cusAdd.getPROVIN_ID() + "");
					txtPROVIN_NAME2.setValue(cusAdd.getPROVIN_NAME());
					txtAMPHURID2.setValue(cusAdd.getAMPHURID() + "");
					txtAMPHURNAME2.setValue(cusAdd.getAMPHURNAME());
					txtZIPCODE2.setValue(cusAdd.getZIPCODE());

					//read_amphur(cusAdd.getAMPHURID(), 2);
					//read_province(cusAdd.getPROVIN_ID(), 2);

				}

			}

			//==addr3
			{
				TboFCUS_ADDR cusAdd = new TboFCUS_ADDR();

				cusAdd.setCOMP_CDE(cust.getCOMP_CDE());
				cusAdd.setCUST_CDE(cust.getCUST_CDE());
				cusAdd.setADDR_TYP("3");

				if (TbfFCUS_ADDR.find(cusAdd)) {

					txtVILAPT3.setValue(cusAdd.getVILAPT1());
					txtFLOOR3.setValue(cusAdd.getFLOOR1());
					txtROOM3.setValue(cusAdd.getROOM1());
					txtADDRESS3.setValue(cusAdd.getADDRESS1());
					txtMOO3.setValue(cusAdd.getMOO1());
					txtSOI3.setValue(cusAdd.getSOI1());
					txtROAD3.setValue(cusAdd.getROAD1());
					txtTAMBOLNAME3.setValue(cusAdd.getTAMBOLNAME());
					txtPROVIN_ID3.setValue(cusAdd.getPROVIN_ID() + "");
					txtPROVIN_NAME3.setValue(cusAdd.getPROVIN_NAME());
					txtAMPHURID3.setValue(cusAdd.getAMPHURID() + "");
					txtAMPHURNAME3.setValue(cusAdd.getAMPHURNAME());
					txtZIPCODE3.setValue(cusAdd.getZIPCODE());

					//read_amphur(cusAdd.getAMPHURID(), 3);
					//read_province(cusAdd.getPROVIN_ID(), 3);

				}

			}

			btnExit.setVisible(false);
			btnBack.setVisible(true);
			btnSave.setVisible(true);
			btnDelete.setVisible(true);
			btnAdd.setVisible(false);
			btnCard.setVisible(false);
			txtCUST_CDE.setReadonly(true);
			div1.setVisible(true);

			res = true;
		}

		return res;

	}

	public boolean read_province(int provin_id, int addtype) throws Exception {
		boolean ret = false;

		TboFPROVINCE prov = new TboFPROVINCE();

		prov.setPROVIN_ID(provin_id);

		if (TbfFPROVINCE.find(prov)) {

			ret = true;

			if (addtype == 1) {
				txtPROVIN_ID1.setValue(prov.getPROVIN_ID() + "");
				txtPROVIN_NAME1.setValue(prov.getPROVIN_NAME());

			} else if (addtype == 2) {
				txtPROVIN_ID2.setValue(prov.getPROVIN_ID() + "");
				txtPROVIN_NAME2.setValue(prov.getPROVIN_NAME());

			} else if (addtype == 3) {
				txtPROVIN_ID3.setValue(prov.getPROVIN_ID() + "");
				txtPROVIN_NAME3.setValue(prov.getPROVIN_NAME());
			}

		} else {

			if (addtype == 1) {
				txtPROVIN_NAME1.setValue("");

			} else if (addtype == 2) {
				txtPROVIN_NAME2.setValue("");

			} else if (addtype == 3) {
				txtPROVIN_NAME3.setValue("");
			}

		}

		return ret;

	}

	public void popupAMPHURID(int selflg) {
		try {
			int provin_id = 0;
			if (selflg == 1) {
				provin_id = Fnc.getIntFromStr(txtPROVIN_ID1.getValue());
			} else if (selflg == 2) {
				provin_id = Fnc.getIntFromStr(txtPROVIN_ID2.getValue());
			} else {
				provin_id = Fnc.getIntFromStr(txtPROVIN_ID3.getValue());
			}
			if (provin_id == 0) {
				throw new Exception("ยังไม่เลือกจังหวัด");
			}
			this.selPopupFlg = selflg;
			FfAMPHURID.popup(provin_id, getLoginBean(), this, "doPopupAMPHURID");
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void doPopupAMPHURID(int amphurid) {
		try {
			read_amphur(amphurid, this.selPopupFlg);
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	private boolean read_amphur(int amphurid, int addtype) throws Exception {

		boolean ret = false;

		TboFAMPHUR table1 = new TboFAMPHUR();

		table1.setAMPHURID(amphurid);

		if (TbfFAMPHUR.find(table1)) {

			ret = true;

			if (addtype == 1) {
				txtAMPHURID1.setValue(table1.getAMPHURID() + "");
				txtAMPHURNAME1.setValue(table1.getAMPHURNAME());
				txtZIPCODE1.setValue(table1.getZIPCODE());

			} else if (addtype == 2) {
				txtAMPHURID2.setValue(table1.getAMPHURID() + "");
				txtAMPHURNAME2.setValue(table1.getAMPHURNAME());
				txtZIPCODE2.setValue(table1.getZIPCODE());

			} else if (addtype == 3) {
				txtAMPHURID3.setValue(table1.getAMPHURID() + "");
				txtAMPHURNAME3.setValue(table1.getAMPHURNAME());
				txtZIPCODE3.setValue(table1.getZIPCODE());
			}

		} else {

			if (addtype == 1) {
				txtAMPHURNAME1.setValue("");
				txtZIPCODE1.setValue("");

			} else if (addtype == 2) {
				txtAMPHURNAME2.setValue("");
				txtZIPCODE2.setValue("");

			} else if (addtype == 3) {
				txtAMPHURNAME3.setValue("");
				txtZIPCODE3.setValue("");
			}

		}

		return ret;

	}

	public void onClick_btnCard() {
		FrmCusCardId.popupForm(this, this.getLoginBean());
	}

	public void copyToAddress2() {
		txtVILAPT2.setValue(txtVILAPT1.getValue());
		txtFLOOR2.setValue(txtFLOOR1.getValue());
		txtROOM2.setValue(txtROOM1.getValue());
		txtADDRESS2.setValue(txtADDRESS1.getValue());
		txtMOO2.setValue(txtMOO1.getValue());
		txtSOI2.setValue(txtSOI1.getValue());
		txtROAD2.setValue(txtROAD1.getValue());
		txtTAMBOLNAME2.setValue(txtTAMBOLNAME1.getValue());
		txtPROVIN_ID2.setValue(txtPROVIN_ID1.getValue());
		txtPROVIN_NAME2.setValue(txtPROVIN_NAME1.getValue());
		txtAMPHURID2.setValue(txtAMPHURID1.getValue());
		txtAMPHURNAME2.setValue(txtAMPHURNAME1.getValue());
		txtZIPCODE2.setValue(txtZIPCODE1.getValue());
	}

	public void copyToAddress3() {
		txtVILAPT3.setValue(txtVILAPT1.getValue());
		txtFLOOR3.setValue(txtFLOOR1.getValue());
		txtROOM3.setValue(txtROOM1.getValue());
		txtADDRESS3.setValue(txtADDRESS1.getValue());
		txtMOO3.setValue(txtMOO1.getValue());
		txtSOI3.setValue(txtSOI1.getValue());
		txtROAD3.setValue(txtROAD1.getValue());
		txtTAMBOLNAME3.setValue(txtTAMBOLNAME1.getValue());
		txtPROVIN_ID3.setValue(txtPROVIN_ID1.getValue());
		txtPROVIN_NAME3.setValue(txtPROVIN_NAME1.getValue());
		txtAMPHURID3.setValue(txtAMPHURID1.getValue());
		txtAMPHURNAME3.setValue(txtAMPHURNAME1.getValue());
		txtZIPCODE3.setValue(txtZIPCODE1.getValue());
	}

}

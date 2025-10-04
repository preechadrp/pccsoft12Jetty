package com.pcc.bx.ui.FrmBxBill;

import java.math.BigDecimal;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.pcc.bx.find.FfBXTMPLHEAD;
import com.pcc.bx.lib.BxRunning;
import com.pcc.bx.progman.ManBxBillEntr;
import com.pcc.bx.tbf.TbfBXDETAIL;
import com.pcc.bx.tbf.TbfBXHEADER;
import com.pcc.bx.tbf.TbfBXTMPLHEAD;
import com.pcc.bx.tbm.TbmBXDETAIL;
import com.pcc.bx.tbo.TboBXDETAIL;
import com.pcc.bx.tbo.TboBXHEADER;
import com.pcc.bx.tbo.TboBXTMPLHEAD;
import com.pcc.gl.find.FfFCUS;
import com.pcc.gl.tbf.TbfFCUS;
import com.pcc.gl.tbm.TbmFCUS_ADDR;
import com.pcc.gl.tbo.TboFCUS;
import com.pcc.gl.tbo.TboFCUS_ADDR;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.find.FfFCOMPBRANC;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FWindow;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.MyDatebox;
import com.pcc.sys.lib.MyDecimalbox;
import com.pcc.sys.lib.ZkUtil;
import com.pcc.sys.tbf.TbfFCOMPBRANC;
import com.pcc.sys.tbo.TboFCOMPBRANC;

public class FrmBxBillEntr extends FWindow {

	private static final long serialVersionUID = 1L;

//	public static void showData(LoginBean loginBean, String blno, EventListener<Event> event1, boolean insertMode) throws Exception {
//		if (ZkUtil.isPopup("FrmBxBillEntr")) {
//			ZkUtil.getWindows("FrmBxBillEntr").focus();
//		} else {
//			FrmBxBillEntr frm1 = (FrmBxBillEntr) ZkUtil.callZulFile("/com/pcc/bx/zul/FrmBxBill/FrmBxBillEntr.zul");
//			frm1.setLoginBean(loginBean);
//			frm1.p_BLNO = blno;
//			frm1.p_Event = event1;
//			frm1.setPosition("top,center");
//			frm1.setWidth(ZkUtil.windowPopupWidth);
//			// frm1.doModal();
//			frm1.p_insertMode = insertMode;
//			frm1.doOverlapped();
//		}
//	}

	public static void showData(LoginBean loginBean, String blno, boolean insertMode, Window winCall, String menuid2, EventListener<Event> event1) throws Exception {

		winCall.setVisible(false);
		String newID = "FrmBxBillEntr" + menuid2; //ป้องกันซ้ำหลายเมนู
		FrmBxBillEntr frm1 = (FrmBxBillEntr) ZkUtil.callZulFile("/com/pcc/bx/zul/FrmBxBill/FrmBxBillEntr.zul");

		//==parameter
		frm1.setLoginBean(loginBean);
		frm1.p_win_call = winCall;
		frm1.p_BLNO = blno;
		frm1.p_Event = event1;
		frm1.p_insertMode = insertMode;

		//==ค่าอื่นๆ
		frm1.setAppendMode(newID, winCall.getWidth(), winCall.getParent());

	}

	public Button btnExit;
	public Button btnSave;
	public Button btnDelete;
	public Textbox txtTMPLCDE;
	public Textbox txtTMPLNAME;
	public Textbox txtDOCPREFIX;
	public MyDatebox tdbBLDATE;
	public Textbox txtBLNO;
	public Combobox cmbVATTYPE;
	public MyDecimalbox decVATRATE;
	public Button btnAdd;
	public Div div1;
	public Textbox txtBRANC_CDE;
	public Textbox txtBRANC_NAME;
	public MyDatebox tdbSENDDATE;
	public Textbox txtCUST_CDE;
	public Textbox txtCUST_TITLE;
	public Textbox txtCUST_FNAME;
	public Textbox txtCUST_LNAME;
	public Textbox txtCUST_ADDR1;
	public Textbox txtCUST_ADDR2;
	public Textbox txtZIPCODE;
	public Textbox txtREMARK1;
	public Combobox cmbLINETYP;
	public Textbox txtPRODUCTID;
	public Textbox txtPRODUCTNAME;
	public Textbox txtREMARKLINE;
	public MyDecimalbox decQTY;
	public MyDecimalbox decPRICE;
	public MyDecimalbox decSUMPRICE;
	public Button btnAdd1;
	public Button btnUndo1;
	public Grid grdData1;
	public MyDecimalbox decBASAMT;
	public MyDecimalbox decDISCOUNTAMT;
	public MyDecimalbox decNETAMT;
	public MyDecimalbox decVATAMT;
	public MyDecimalbox decTOTALAMT;

	private java.util.List<TboBXDETAIL> lst_bxdetail = new java.util.ArrayList<TboBXDETAIL>();
	private int edit_SEQ = 0;

	public String p_BLNO = "";
	public Window p_win_call = null;
	public EventListener<Event> p_Event;
	public boolean p_insertMode = false;

	@Override
	public void setFormObj() {
		btnExit = (Button) getFellow("btnExit");
		btnSave = (Button) getFellow("btnSave");
		btnDelete = (Button) getFellow("btnDelete");
		txtTMPLCDE = (Textbox) getFellow("txtTMPLCDE");
		txtTMPLNAME = (Textbox) getFellow("txtTMPLNAME");
		txtDOCPREFIX = (Textbox) getFellow("txtDOCPREFIX");
		tdbBLDATE = (MyDatebox) getFellow("tdbBLDATE");
		txtBLNO = (Textbox) getFellow("txtBLNO");
		cmbVATTYPE = (Combobox) getFellow("cmbVATTYPE");
		decVATRATE = (MyDecimalbox) getFellow("decVATRATE");
		btnAdd = (Button) getFellow("btnAdd");
		div1 = (Div) getFellow("div1");
		txtBRANC_CDE = (Textbox) getFellow("txtBRANC_CDE");
		txtBRANC_NAME = (Textbox) getFellow("txtBRANC_NAME");
		tdbSENDDATE = (MyDatebox) getFellow("tdbSENDDATE");
		txtCUST_CDE = (Textbox) getFellow("txtCUST_CDE");
		txtCUST_TITLE = (Textbox) getFellow("txtCUST_TITLE");
		txtCUST_FNAME = (Textbox) getFellow("txtCUST_FNAME");
		txtCUST_LNAME = (Textbox) getFellow("txtCUST_LNAME");
		txtCUST_ADDR1 = (Textbox) getFellow("txtCUST_ADDR1");
		txtCUST_ADDR2 = (Textbox) getFellow("txtCUST_ADDR2");
		txtZIPCODE = (Textbox) getFellow("txtZIPCODE");
		txtREMARK1 = (Textbox) getFellow("txtREMARK1");
		cmbLINETYP = (Combobox) getFellow("cmbLINETYP");
		txtPRODUCTID = (Textbox) getFellow("txtPRODUCTID");
		txtPRODUCTNAME = (Textbox) getFellow("txtPRODUCTNAME");
		txtREMARKLINE = (Textbox) getFellow("txtREMARKLINE");
		decQTY = (MyDecimalbox) getFellow("decQTY");
		decPRICE = (MyDecimalbox) getFellow("decPRICE");
		decSUMPRICE = (MyDecimalbox) getFellow("decSUMPRICE");
		btnAdd1 = (Button) getFellow("btnAdd1");
		btnUndo1 = (Button) getFellow("btnUndo1");
		grdData1 = (Grid) getFellow("grdData1");
		decBASAMT = (MyDecimalbox) getFellow("decBASAMT");
		decDISCOUNTAMT = (MyDecimalbox) getFellow("decDISCOUNTAMT");
		decNETAMT = (MyDecimalbox) getFellow("decNETAMT");
		decVATAMT = (MyDecimalbox) getFellow("decVATAMT");
		decTOTALAMT = (MyDecimalbox) getFellow("decTOTALAMT");

	}

	@Override
	public void addEnterIndex() {
		// addEnterIndex(btnExit);
		// addEnterIndex(btnSave);
		// addEnterIndex(btnDelete);
		addEnterIndex(txtTMPLCDE);
		// addEnterIndex(txtTMPLNAME);
		// addEnterIndex(txtDOCPREFIX);
		addEnterIndex(tdbBLDATE);
		// addEnterIndex(txtBLNO);
		addEnterIndex(cmbVATTYPE);
		addEnterIndex(decVATRATE);
		addEnterIndex(btnAdd);
		addEnterIndex(txtBRANC_CDE);
		addEnterIndex(tdbSENDDATE);
		addEnterIndex(txtCUST_CDE);
		// addEnterIndex(txtCUST_TITLE);
		// addEnterIndex(txtCUST_FNAME);
		// addEnterIndex(txtCUST_LNAME);
		// addEnterIndex(txtCUST_ADDR1);
		// addEnterIndex(txtCUST_ADDR2);
		// addEnterIndex(txtZIPCODE);
		addEnterIndex(txtREMARK1);
		addEnterIndex(cmbLINETYP);
		addEnterIndex(txtPRODUCTID);
		addEnterIndex(txtPRODUCTNAME);
		addEnterIndex(txtREMARKLINE);
		addEnterIndex(decQTY);
		addEnterIndex(decPRICE);
		//addEnterIndex(decSUMPRICE);
		addEnterIndex(btnAdd1);
		addEnterIndex(btnUndo1);
		// addEnterIndex(decBASAMT);
		addEnterIndex(decDISCOUNTAMT);
		addEnterIndex(decVATAMT);
		// addEnterIndex(decTOTALAMT);

	}

	@Override
	public void formInit() {
		try {
			ZkUtil.setGridHeaderStyle(grdData1);
			grdData1.setRowRenderer(getRowRendererData1());

			clearData();

			if (!Fnc.isEmpty(this.p_BLNO)) {
				read_record(this.p_BLNO);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}

	}

	public void onOK() {
		try {
			if (focusObj == txtTMPLCDE) {
				if (!txtTMPLCDE.isReadonly()) {
					if (Fnc.isEmpty(txtTMPLCDE.getValue())) {
						popupTMPLCDE();
					} else {
						super.onOK();
					}
				} else {
					super.onOK();
				}
			} else if (focusObj == txtCUST_CDE) {
				if (!txtCUST_CDE.isReadonly()) {
					if (Fnc.isEmpty(txtCUST_CDE.getValue())) {
						popupCUST_CDE();
					} else {
						super.onOK();
					}
				} else {
					super.onOK();
				}
			} else if (focusObj == decDISCOUNTAMT) {
				if (ZkUtil.doOnChange(decDISCOUNTAMT)) {
					super.onOK();
				} else {
					sumData();
				}
			} else if (focusObj == decVATAMT) {
				if (ZkUtil.doOnChange(decVATAMT)) {
					super.onOK();
				} else {
					onChang_VATAMT();
				}
			} else {
				super.onOK();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}

	}

	public void onClose() {
		try {
			super.onClose();
			if (p_win_call != null) {
				p_win_call.setVisible(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onChange_Data(HtmlBasedComponent comp) {
		try {
			if (comp != null) {
				if (!Fnc.isEmpty(comp.getId())) {
					ZkUtil.setAndClearOnChang(comp, this.objectList);
					// ตรวจด้วย ZkUtil.doOnChange(xx) ใน onOK()
				}
			}
			//
			if (comp.getId().equals("txtTMPLCDE")) {
				read_BXTMPLHEAD(Fnc.getIntFromStr(txtTMPLCDE.getValue()));
			}
			if (comp.getId().equals("cmbVATTYPE")) {
				if (ZkUtil.getSelectItemValueComboBox(cmbVATTYPE).equals("1")) {// ราคาไม่รวม VAT(คำนวนจากรวมมูลค่าสินค้า)
					decVATRATE.setReadonly(false);
					decVATRATE.setValue(new BigDecimal("7"));

				} else if (ZkUtil.getSelectItemValueComboBox(cmbVATTYPE).equals("2")) {// ราคาไม่รวม VAT(คำนวนแต่ละหน่วย)
					decVATRATE.setReadonly(false);
					decVATRATE.setValue(new BigDecimal("7"));

				} else if (ZkUtil.getSelectItemValueComboBox(cmbVATTYPE).equals("3")) {// ราคารวม VAT(ถอด Vat จากราคาต่อหน่วย)
					decVATRATE.setReadonly(false);
					decVATRATE.setValue(new BigDecimal("7"));

				} else {
					decVATRATE.setReadonly(true);
					decVATRATE.setValue(BigDecimal.ZERO);
				}
			}
			if (comp.getId().equals("txtBRANC_CDE")) {
				read_BRANC_CDE(Fnc.getIntFromStr(txtBRANC_CDE.getValue()));
			}
			if (comp.getId().equals("txtCUST_CDE")) {
				read_CUST_CDE(txtCUST_CDE.getValue());
			}
			if (comp.getId().equals("cmbLINETYP")) {
				if (ZkUtil.getSelectItemValueComboBox(cmbLINETYP).equals("1")) { // สินค้า
					decQTY.setReadonly(false);
					decPRICE.setReadonly(false);
				} else {
					decQTY.setValue(BigDecimal.ZERO);
					decPRICE.setValue(BigDecimal.ZERO);
					decQTY.setReadonly(true);
					decPRICE.setReadonly(true);
				}
			}
			if (comp.getId().equals("decQTY")) {
				if (decQTY.getValue().compareTo(BigDecimal.ZERO) != 0 && decPRICE.getValue().compareTo(BigDecimal.ZERO) != 0) {
					decSUMPRICE.setValue(decQTY.getValue().multiply(decPRICE.getValue()));
				} else {
					decSUMPRICE.setValue(BigDecimal.ZERO);
				}
			}
			if (comp.getId().equals("decPRICE")) {
				if (decQTY.getValue().compareTo(BigDecimal.ZERO) != 0 && decPRICE.getValue().compareTo(BigDecimal.ZERO) != 0) {
					decSUMPRICE.setValue(decQTY.getValue().multiply(decPRICE.getValue()));
				} else {
					decSUMPRICE.setValue(decPRICE.getValue());
				}
			}
			if (comp.getId().equals("decDISCOUNTAMT")) {
				sumData();
			}
			if (comp.getId().equals("decVATAMT")) {
				onChang_VATAMT();
			}

		} catch (Exception e) {
		}
	}

	public void onClick_btnAdd() {
		try {
			new_record();
		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}
	}

	private void new_record() throws WrongValueException, Exception {

		String new_BLNO = "";

		try (FDbc dbc = FDbc.connectMasterDb()) {
			dbc.beginTrans();

			if (Fnc.isEmpty(txtTMPLCDE.getValue())) {
				throw new Exception("ต้องระบุ " + txtTMPLCDE.getTooltiptext() + " ก่อน");
			}
			if (Fnc.isEmpty(txtTMPLNAME.getValue())) {
				throw new Exception("ระบุ " + txtTMPLCDE.getTooltiptext() + " ไม่ถูกต้อง");
			}
			if (tdbBLDATE.getSqlDate() == null) {
				throw new Exception("ต้องระบุ " + tdbBLDATE.getTooltiptext() + " ก่อน");
			}
			if (txtDOCPREFIX.getValue().length() != 2) {
				throw new Exception("ต้องระบุ " + txtDOCPREFIX.getTooltiptext() + " 2 ตัวอักษร");
			}
			if (Fnc.isEmpty(ZkUtil.getSelectItemValueComboBox(cmbVATTYPE))) {
				throw new Exception("ต้องระบุ " + cmbVATTYPE.getTooltiptext() + " ก่อน");
			}
			if (decVATRATE.getValue().compareTo(BigDecimal.ZERO) < 0) {
				throw new Exception("ระบุ " + decVATRATE.getTooltiptext() + " ไม่ถูกต้อง");
			}

			// ==new
			new_BLNO = BxRunning.genBLNO(dbc, txtDOCPREFIX.getValue(), this.getLoginBean(), tdbBLDATE.getSqlDate());

			TboBXHEADER head = new TboBXHEADER();

			head.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			head.setBLNO(new_BLNO);
			head.setTMPLCDE(Fnc.getIntFromStr(txtTMPLCDE.getValue()));
			head.setBLDATE(tdbBLDATE.getSqlDate());
			head.setVATTYPE(ZkUtil.getSelectItemValueComboBox(cmbVATTYPE));
			head.setCUST_CDE("");
			head.setIDNO("");
			// head.setCUST_TITLE(rs.getString("CUST_TITLE"));
			// head.setCUST_FNAME(rs.getString("CUST_FNAME"));
			// head.setCUST_LNAME(rs.getString("CUST_LNAME"));
			// head.setCUST_ADDR1(rs.getString("CUST_ADDR1"));
			// head.setCUST_ADDR2(rs.getString("CUST_ADDR2"));
			// head.setZIPCODE(rs.getString("ZIPCODE"));
			// head.setREMARK1(rs.getString("REMARK1"));
			head.setVATRATE(decVATRATE.getValue());
			// head.setVATAMT(BigDecimal.ZERO);
			// head.setBASAMT(BigDecimal.ZERO);
			// head.setTOTALAMT(BigDecimal.ZERO);
			head.setBRANC_CDE(1);
			head.setRECSTA(0);
			// head.setCOSTVATAMT(rs.getBigDecimal("COSTVATAMT"));
			// head.setCOSTBASAMT(rs.getBigDecimal("COSTBASAMT"));
			head.setUPBY(this.getLoginBean().getUSER_ID());
			head.setUPDT(FnDate.getTodaySqlDateTime());

			TbfBXHEADER.insert(dbc, head);

			dbc.commit();
		}

		read_record(new_BLNO);

	}

	private void clearData() {

		div1.setVisible(false);
		btnExit.setVisible(true);
		btnSave.setVisible(false);
		btnDelete.setVisible(false);
		btnAdd.setVisible(true);
		btnAdd1.setLabel(Labels.getLabel("comm.label.save"));
		btnUndo1.setVisible(false);
		decDISCOUNTAMT.setReadonly(true);
		decVATAMT.setReadonly(true);
		txtTMPLCDE.focus();

		// == set value
		txtTMPLCDE.setValue("");
		txtTMPLNAME.setValue("");
		txtDOCPREFIX.setValue("");
		tdbBLDATE.setValue(FnDate.getTodaySqlDate());
		txtBLNO.setValue("");
		cmbVATTYPE.setSelectedIndex(0);
		decVATRATE.setValue(BigDecimal.ZERO);
		txtBRANC_CDE.setValue("");
		txtBRANC_NAME.setValue("");
		tdbSENDDATE.setValue(FnDate.getTodaySqlDate());
		txtCUST_CDE.setValue("");
		txtCUST_TITLE.setValue("");
		txtCUST_FNAME.setValue("");
		txtCUST_LNAME.setValue("");
		txtCUST_ADDR1.setValue("");
		txtCUST_ADDR2.setValue("");
		txtZIPCODE.setValue("");
		txtREMARK1.setValue("");
		cmbLINETYP.setSelectedIndex(-1);
		txtPRODUCTID.setValue("");
		txtPRODUCTNAME.setValue("");
		txtREMARKLINE.setValue("");
		decQTY.setValue(BigDecimal.ZERO);
		decPRICE.setValue(BigDecimal.ZERO);
		decSUMPRICE.setValue(BigDecimal.ZERO);
		decBASAMT.setValue(BigDecimal.ZERO);
		decDISCOUNTAMT.setValue(BigDecimal.ZERO);
		decNETAMT.setValue(BigDecimal.ZERO);
		decVATAMT.setValue(BigDecimal.ZERO);
		decTOTALAMT.setValue(BigDecimal.ZERO);

		grdData1.getRows().getChildren().clear();
		lst_bxdetail.clear();
		edit_SEQ = 0;

	}

	private void read_record(String blno) throws WrongValueException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			read_record(dbc, blno);
		}
	}

	private void read_record(FDbc dbc, String blno) throws WrongValueException, Exception {
		TboBXHEADER head = new TboBXHEADER();

		head.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		head.setBLNO(blno);

		if (TbfBXHEADER.find(dbc, head)) {

			if (head.getRECSTA() == 9) {
				throw new Exception("เอกสารมีการยกเลิกแล้ว");
			}

			head.setRECSTA(0);
			TbfBXHEADER.update(dbc, head);

			// === set value
			// txtTMPLCDE.setValue(head.getTMPLCDE()+"");
			// txtTMPLNAME.setValue(head.get);
			// txtDOCPREFIX.setValue(head.getpre);
			read_BXTMPLHEAD(head.getTMPLCDE());
			tdbBLDATE.setValue(head.getBLDATE());
			txtBLNO.setValue(head.getBLNO());
			ZkUtil.setSelectItemComboBoxByValue(cmbVATTYPE, head.getVATTYPE());
			decVATRATE.setValue(head.getVATRATE());
			read_BRANC_CDE(head.getBRANC_CDE());
			tdbSENDDATE.setValue(head.getSENDDATE());
			txtCUST_CDE.setValue(head.getCUST_CDE());
			txtCUST_TITLE.setValue(head.getCUST_TITLE());
			txtCUST_FNAME.setValue(head.getCUST_FNAME());
			txtCUST_LNAME.setValue(head.getCUST_LNAME());
			txtCUST_ADDR1.setValue(head.getCUST_ADDR1());
			txtCUST_ADDR2.setValue(head.getCUST_ADDR2());
			txtZIPCODE.setValue(head.getZIPCODE());
			txtREMARK1.setValue(head.getREMARK1());

			cmbLINETYP.setSelectedIndex(0);
			txtPRODUCTID.setValue("");
			txtPRODUCTNAME.setValue("");
			txtREMARKLINE.setValue("");
			decQTY.setValue(BigDecimal.ZERO);
			decPRICE.setValue(BigDecimal.ZERO);
			decSUMPRICE.setValue(BigDecimal.ZERO);

			decBASAMT.setValue(head.getBASAMT());
			decDISCOUNTAMT.setValue(Fnc.getBigDecimalValue(head.getDISCOUNTAMT()));

			BigDecimal netAmt = head.getBASAMT().subtract(Fnc.getBigDecimalValue(head.getDISCOUNTAMT()));
			decNETAMT.setValue(netAmt);

			decVATAMT.setValue(head.getVATAMT());
			decTOTALAMT.setValue(head.getTOTALAMT());

			showGrid1();

			// === set othoer
			div1.setVisible(true);
			txtTMPLCDE.setReadonly(true);
			tdbBLDATE.setReadonly(true);
			cmbVATTYPE.setDisabled(true);
			decVATRATE.setReadonly(true);
			btnAdd.setVisible(false);
			btnExit.setVisible(false);
			btnSave.setVisible(true);
			if (p_insertMode) {
				btnDelete.setVisible(true);
			} else {
				btnDelete.setVisible(false);
			}
			if (head.getVATTYPE().equals("1")) {
				decDISCOUNTAMT.setReadonly(false);
				decVATAMT.setReadonly(false);
			} else {
				decDISCOUNTAMT.setReadonly(true);
				decVATAMT.setReadonly(true);
			}

			txtBRANC_CDE.focus();

		}

	}

	private void showGrid1() throws WrongValueException, Exception {
		TbmBXDETAIL.getData(this.getLoginBean().getCOMP_CDE(), txtBLNO.getValue(), lst_bxdetail);
		SimpleListModel rstModel = new SimpleListModel(lst_bxdetail);
		this.grdData1.setModel(rstModel);
		this.grdData1.renderAll();
	}

	private RowRenderer<?> getRowRendererData1() {

		return (row, data, index) -> {

			TboBXDETAIL rs = (TboBXDETAIL) data;
			int seq = index + 1;
			row.setStyle(ZkUtil.styleFindLookUp);

			row.setAttribute("DATA1", rs);

			{
				Button btn1 = new Button();
				btn1.setLabel("แก้ไข");
				btn1.setAutodisable("self");
				btn1.addEventListener(Events.ON_CLICK, event -> onClick_Edit1Row(event));
				row.appendChild(btn1);
				row.setAttribute("btn_edit", btn1);
			}

			{
				Button btn1 = new Button();
				btn1.setLabel("ลบ");
				btn1.setSclass("buttondel");
				btn1.setAutodisable("self");
				btn1.addEventListener(Events.ON_CLICK, event -> onClick_Del1Row(event));
				row.appendChild(btn1);
				row.setAttribute("btn_del", btn1);
			}

			row.appendChild(ZkUtil.gridIntbox(seq));
			row.appendChild(ZkUtil.gridTextbox(rs.getPRODUCTID()));
			row.appendChild(ZkUtil.gridTextbox(rs.getPRODUCTNAME()));
			row.appendChild(ZkUtil.gridTextbox(rs.getREMARKLINE()));
			row.appendChild(ZkUtil.gridDecimalbox(rs.getQTY()));
			row.appendChild(ZkUtil.gridDecimalbox(rs.getPRICE()));
			row.appendChild(ZkUtil.gridDecimalbox(rs.getSUMPRICE()));

		};
	}

	private void disableGridButtton(boolean disable) {

		java.util.List<Component> lstRow = grdData1.getRows().getChildren();
		for (Component cmm : lstRow) {

			Row row1 = (Row) cmm;
			if (row1.getAttribute("btn_edit") != null) {
				Button btnEdit = (Button) row1.getAttribute("btn_edit");
				btnEdit.setDisabled(disable);
			}
			if (row1.getAttribute("btn_del") != null) {
				Button btnDel = (Button) row1.getAttribute("btn_del");
				btnDel.setDisabled(disable);
			}

		}

	}

	private void onClick_Edit1Row(Event event) {
		try {
			Component comp = event.getTarget().getParent();
			var rs = (TboBXDETAIL) comp.getAttribute("DATA1");

			TboBXDETAIL det = new TboBXDETAIL();

			det.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			det.setBLNO(rs.getBLNO());
			det.setSEQ1(rs.getSEQ1());

			if (TbfBXDETAIL.find(det)) {

				ZkUtil.setSelectItemComboBoxByValue(cmbLINETYP, det.getLINETYP());
				txtPRODUCTID.setValue(det.getPRODUCTID());
				txtPRODUCTNAME.setValue(det.getPRODUCTNAME());
				txtREMARKLINE.setValue(det.getREMARKLINE());
				decQTY.setValue(det.getQTY());
				decPRICE.setValue(det.getPRICE());
				decSUMPRICE.setValue(det.getSUMPRICE());

				txtPRODUCTID.focus();
				edit_SEQ = rs.getSEQ1();
				btnAdd1.setLabel(Labels.getLabel("comm.label.save"));
				btnUndo1.setVisible(true);
				disableGridButtton(true);
				onChange_Data(cmbLINETYP);

			} else {
				throw new Exception("ไม่พบรายการให้กดปุ่มบันทึกแล้วเข้ามาแก้ไขใหม่");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}
	}

	private void onClick_Del1Row(Event event) {

		Component comp = event.getTarget().getParent();
		var rs = (TboBXDETAIL) comp.getAttribute("DATA1");

		Msg.confirm(Labels.getLabel("comm.label.deleteComfirm") + " ?", "?", (event1) -> {
			if (Messagebox.Button.YES.equals(event1.getButton())) {

				try {

					TboBXDETAIL det = new TboBXDETAIL();

					det.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
					det.setBLNO(rs.getBLNO());
					det.setSEQ1(rs.getSEQ1());

					TbfBXDETAIL.delete(det);

					showGrid1();
					sumData();

				} catch (Exception ex) {
					ex.printStackTrace();
					Msg.error(ex);
				}

			}
		});
	}

	public void onClick_btnAdd1() {

		boolean[] clearWhenError = { false };
		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				// == validate
				if (ZkUtil.getSelectItemValueComboBox(cmbLINETYP).equals("")) {
					throw new Exception("ต้องระบุ " + cmbLINETYP.getTooltiptext());
				}
				if (Fnc.isEmpty(txtPRODUCTNAME.getValue())) {
					throw new Exception("ต้องระบุ " + txtPRODUCTNAME.getTooltiptext());
				}
				if (ZkUtil.getSelectItemValueComboBox(cmbLINETYP).equals("1")) { // สินค้า
					if (decQTY.getValue().compareTo(BigDecimal.ZERO) <= 0) {
						throw new Exception("ต้องระบุ " + decQTY.getTooltiptext());
					}
					if (decPRICE.getValue().compareTo(BigDecimal.ZERO) <= 0) {
						throw new Exception("ต้องระบุ " + decPRICE.getTooltiptext());
					}
				}

				// == update header
				updateHeader(dbc, clearWhenError);

				// ==== insert/update detail
				TboBXDETAIL det = new TboBXDETAIL();

				det.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				det.setBLNO(txtBLNO.getValue());

				if (!btnUndo1.isVisible()) { // เพิ่ม
					int new_TMPLSEQ = TbmBXDETAIL.getMaxCode(dbc, this.getLoginBean().getCOMP_CDE(), txtBLNO.getValue()) + 1;
					det.setSEQ1(new_TMPLSEQ);
				} else {
					det.setSEQ1(edit_SEQ);
					if (!TbfBXDETAIL.find(dbc, det)) {
						clearWhenError[0] = true;
						throw new Exception("ไม่พบรายการที่จะแก้ไข มีการแก้ไขโดยผู้ใช้อื่น");
					}
				}

				det.setLINETYP(ZkUtil.getSelectItemValueComboBox(cmbLINETYP));
				det.setPRODUCTID(txtPRODUCTID.getValue());
				det.setPRODUCTNAME(txtPRODUCTNAME.getValue());
				det.setREMARKLINE(txtREMARKLINE.getValue());
				det.setQTY(decQTY.getValue());
				det.setPRICE(decPRICE.getValue());
				det.setSUMPRICE(decSUMPRICE.getValue());

				if (!btnUndo1.isVisible()) {
					TbfBXDETAIL.insert(dbc, det);
				} else {
					TbfBXDETAIL.update(dbc, det);
				}

				dbc.commit();
			}

			txtPRODUCTID.setValue("");
			txtPRODUCTNAME.setValue("");
			txtREMARKLINE.setValue("");
			decQTY.setValue(BigDecimal.ZERO);
			decPRICE.setValue(BigDecimal.ZERO);
			decSUMPRICE.setValue(BigDecimal.ZERO);

			cmbLINETYP.focus();
			showGrid1();
			sumData();
			btnAdd1.setLabel(Labels.getLabel("comm.label.save"));
			btnUndo1.setVisible(false);
			edit_SEQ = 0;
			onChange_Data(cmbLINETYP);

		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
			if (clearWhenError[0]) {
				this.onClose();
			}
		}

	}

	private TboBXHEADER updateHeader(FDbc dbc, boolean[] clearWhenError) throws Exception {

		TboBXHEADER bxH = new TboBXHEADER();

		bxH.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		bxH.setBLNO(txtBLNO.getValue());

		if (TbfBXHEADER.find(dbc, bxH)) {

			if (bxH.getRECSTA() != 0) {
				clearWhenError[0] = true;
				throw new Exception("สถานะไม่ถูกต้อง");
			}

			bxH.setCUST_CDE(txtCUST_CDE.getValue());
			bxH.setIDNO(Fnc.getStr(txtCUST_CDE.getAttribute("IDNO")));
			bxH.setCUST_TITLE(txtCUST_TITLE.getValue());
			bxH.setCUST_FNAME(txtCUST_FNAME.getValue());
			bxH.setCUST_LNAME(txtCUST_LNAME.getValue());
			bxH.setCUST_ADDR1(txtCUST_ADDR1.getValue());
			bxH.setCUST_ADDR2(txtCUST_ADDR2.getValue());
			bxH.setZIPCODE(txtZIPCODE.getValue());
			bxH.setREMARK1(txtREMARK1.getValue());
			bxH.setVATRATE(decVATRATE.getValue());
			bxH.setVATAMT(decVATAMT.getValue());
			bxH.setBASAMT(decBASAMT.getValue());
			bxH.setTOTALAMT(decTOTALAMT.getValue());
			bxH.setDISCOUNTAMT(decDISCOUNTAMT.getValue());
			bxH.setBRANC_CDE(Fnc.getIntFromStr(txtBRANC_CDE.getValue()));
			bxH.setSENDDATE(tdbSENDDATE.getSqlDate());
			bxH.setUPBY(this.getLoginBean().getUSER_ID());
			bxH.setUPDT(FnDate.getTodaySqlDateTime());

			TbfBXHEADER.update(dbc, bxH);
		} else {
			clearWhenError[0] = true;
			throw new Exception("ไม่พบรายการ");
		}

		return bxH;

	}

	public void onClick_btnUndo1() {

		try {

			txtPRODUCTID.setValue("");
			txtPRODUCTNAME.setValue("");
			txtREMARKLINE.setValue("");
			decQTY.setValue(BigDecimal.ZERO);
			decPRICE.setValue(BigDecimal.ZERO);
			decSUMPRICE.setValue(BigDecimal.ZERO);

			cmbLINETYP.focus();
			btnAdd1.setLabel(Labels.getLabel("comm.label.save"));
			btnUndo1.setVisible(false);
			disableGridButtton(false);
			onChange_Data(cmbLINETYP);

		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}

	}

	public void onClick_Save() {

		boolean[] clearWhenError = { false };

		try (FDbc dbc = FDbc.connectMasterDb()) {
			dbc.beginTrans();

			if (Fnc.isEmpty(txtBRANC_NAME.getValue())) {
				throw new Exception("ต้องระบุ " + txtBRANC_NAME.getTooltiptext());
			}
			if (lst_bxdetail.size() == 0) {
				throw new Exception("ยังไม่มีรายการ");
			}
			if (decTOTALAMT.getValue().compareTo(BigDecimal.ZERO) <= 0) {
				throw new Exception("ยังไม่มียอดสินค้า");
			}

			// == update header
			TboBXHEADER bxH = updateHeader(dbc, clearWhenError);
			bxH.setRECSTA(1);
			TbfBXHEADER.update(dbc, bxH);

			dbc.commit();

			if (p_Event != null) {
				p_Event.onEvent(new Event(Events.ON_CLICK, null, -1));
			}

			ManBxBillEntr.printReport(bxH.getCOMP_CDE(), bxH.getBLNO());

			this.onClose();
		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
			if (clearWhenError[0]) {
				this.onClose();
			}
		}
	}

	public void onClick_Delete() {
		try {

			Msg.confirm(Labels.getLabel("comm.label.deleteComfirm") + " ?", "?", (event1) -> {
				if (Messagebox.Button.YES.equals(event1.getButton())) {

					boolean[] clearWhenError = { false };

					try (FDbc dbc = FDbc.connectMasterDb()) {
						dbc.beginTrans();

						TboBXHEADER bxH = new TboBXHEADER();

						bxH.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
						bxH.setBLNO(txtBLNO.getValue());

						if (TbfBXHEADER.find(dbc, bxH)) {
							if (bxH.getRECSTA() != 0) {
								clearWhenError[0] = true;
								throw new Exception("สถานะไม่ถูกต้อง");
							}
							bxH.setRECSTA(9);
							bxH.setUPBY(this.getLoginBean().getUSER_ID());
							bxH.setUPDT(FnDate.getTodaySqlDateTime());
							TbfBXHEADER.update(dbc, bxH);
						}

						dbc.commit();

						if (p_Event != null) {
							p_Event.onEvent(new Event(Events.ON_CLICK, null, -1));
						}

						this.onClose();

					} catch (Exception ex) {
						ex.printStackTrace();
						Msg.error(ex);
						if (clearWhenError[0]) {
							this.onClose();
						}
					}

				}
			});

		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}
	}

	public void popupTMPLCDE() {
		if (!txtTMPLCDE.isReadonly()) {
			FfBXTMPLHEAD.popup(this.getLoginBean(), this, "read_BXTMPLHEAD");
		}
	}

	public void read_BXTMPLHEAD(int tmplcde) throws WrongValueException, Exception {

		TboBXTMPLHEAD head = new TboBXTMPLHEAD();

		head.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		head.setTMPLCDE(tmplcde);

		if (TbfBXTMPLHEAD.find(head)) {
			txtTMPLCDE.setValue(head.getTMPLCDE() + "");
			txtDOCPREFIX.setValue(head.getDOCPREFIX());
			txtTMPLNAME.setValue(head.getTMPLNAME());
		}

	}

	public void popupCUST_CDE() {
		FfFCUS.popup(this.getLoginBean(), this, "read_CUST_CDE");
	}

	public void read_CUST_CDE(String cust_cde) {
		try {

			TboFCUS cust = new TboFCUS();

			cust.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			cust.setCUST_CDE(cust_cde);

			if (TbfFCUS.find(cust)) {

				txtCUST_CDE.setValue(cust.getCUST_CDE());
				txtCUST_CDE.setAttribute("IDNO", cust.getIDNO());
				txtCUST_TITLE.setValue(cust.getTITLE());
				txtCUST_FNAME.setValue(cust.getFNAME());
				txtCUST_LNAME.setValue(cust.getLNAME());

				TboFCUS_ADDR cust_addr = TbmFCUS_ADDR.getAddr(cust.getCOMP_CDE(), cust.getCUST_CDE(), "2");

				txtCUST_ADDR1.setValue(TbmFCUS_ADDR.custAddressLine1(cust_addr));
				txtCUST_ADDR2.setValue(TbmFCUS_ADDR.custAddressLine2(cust_addr, false));
				txtZIPCODE.setValue(cust_addr.getZIPCODE());

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void popupBRANC_CDE() {
		FfFCOMPBRANC.popup(this.getLoginBean(), this, "read_BRANC_CDE");
	}

	public void read_BRANC_CDE(int branc_cde) throws Exception {

		TboFCOMPBRANC bn = new TboFCOMPBRANC();

		bn.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		bn.setBRANC_CDE(branc_cde);

		if (TbfFCOMPBRANC.find(bn)) {
			txtBRANC_CDE.setValue(bn.getBRANC_CDE() + "");
			txtBRANC_NAME.setValue(bn.getBRANC_NAME());
		} else {
			txtBRANC_CDE.setValue(branc_cde + "");
			txtBRANC_NAME.setValue("");
		}

	}

	private void sumData() {

		String vattype = ZkUtil.getSelectItemValueComboBox(cmbVATTYPE);
		BigDecimal basamt = BigDecimal.ZERO;
		BigDecimal netamt = BigDecimal.ZERO;//รวมมูลค่าสินค้าหลังหักส่วนลด
		BigDecimal vatamt = BigDecimal.ZERO;
		BigDecimal totalamt = BigDecimal.ZERO;

		for (TboBXDETAIL bxdetail : this.lst_bxdetail) {

			if (bxdetail.getLINETYP().equals("1")) {// สินค้า

				if (vattype.equals("1")) {// ราคาไม่รวม VAT(คำนวนจากรวมมูลค่าสินค้า)
					basamt = basamt.add(bxdetail.getSUMPRICE());

				} else if (vattype.equals("2")) {// ราคาไม่รวม VAT(คำนวนแต่ละหน่วย)

					basamt = basamt.add(bxdetail.getSUMPRICE());

					BigDecimal v1 = Fnc.calVat(bxdetail.getPRICE(), decVATRATE.getValue());
					BigDecimal v2 = v1.multiply(bxdetail.getQTY());
					vatamt = vatamt.add(v2);

				} else if (vattype.equals("3")) {// ราคารวม VAT(ถอด Vat จากราคาต่อหน่วย)

					BigDecimal b1 = Fnc.getAmtExcludeVat(bxdetail.getPRICE(), decVATRATE.getValue());
					BigDecimal b2 = b1.multiply(bxdetail.getQTY());
					basamt = basamt.add(b2);

					BigDecimal v1 = Fnc.getAmtVat(bxdetail.getPRICE(), decVATRATE.getValue());
					BigDecimal v2 = v1.multiply(bxdetail.getQTY());
					vatamt = vatamt.add(v2);

				} else {// NOVAT

					basamt = basamt.add(bxdetail.getSUMPRICE());

				}

			}

		}

		if (vattype.equals("1")) {// ราคาไม่รวม VAT(คำนวนจากรวมมูลค่าสินค้าหลังหักส่วนลด)
			if (basamt.compareTo(BigDecimal.ZERO) > 0) {
				if (decDISCOUNTAMT.getValue().compareTo(BigDecimal.ZERO) < 0) {
					decDISCOUNTAMT.setValue(BigDecimal.ZERO);
				}

				netamt = basamt.subtract(decDISCOUNTAMT.getValue());
				vatamt = Fnc.calVat(netamt, decVATRATE.getValue());

			}
		} else {
			netamt = basamt;
		}

		totalamt = netamt.add(vatamt);

		decBASAMT.setValue(basamt);
		decNETAMT.setValue(netamt);
		decVATAMT.setValue(vatamt);
		decTOTALAMT.setValue(totalamt);

	}
	
	private void onChang_VATAMT() {
		
		if (decVATAMT.getValue().compareTo(BigDecimal.ZERO) < 0) {
			decVATAMT.setValue(BigDecimal.ZERO);
		}
		if (decNETAMT.getValue().compareTo(BigDecimal.ZERO) > 0 && decVATAMT.getValue().compareTo(BigDecimal.ZERO) > 0) {
			decTOTALAMT.setValue(decNETAMT.getValue().add(decVATAMT.getValue()));
		} else {
			decTOTALAMT.setValue(decNETAMT.getValue());
		}
		
	}

}

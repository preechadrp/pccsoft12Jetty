package com.pcc.tk.ui.FrmTkJob;

import java.math.BigDecimal;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FWindow;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.MyDatebox;
import com.pcc.sys.lib.MyDecimalbox;
import com.pcc.sys.lib.ZkUtil;
import com.pcc.tk.find.FfTKCOURT;
import com.pcc.tk.find.FfTKJOBCODE;
import com.pcc.tk.find.FfTKLAWSTAT;
import com.pcc.tk.find.FfTKLAWTYPE;
import com.pcc.tk.find.FfTKLAWYER;
import com.pcc.tk.find.FfTKZONE;
import com.pcc.tk.lib.TkRunning;
import com.pcc.tk.progman.ManTkJob;
import com.pcc.tk.tbf.TbfTKCOURT;
import com.pcc.tk.tbf.TbfTKJOB;
import com.pcc.tk.tbf.TbfTKJOBCLIENT;
import com.pcc.tk.tbf.TbfTKJOBCODE;
import com.pcc.tk.tbf.TbfTKJOBLAWYER;
import com.pcc.tk.tbf.TbfTKLAWSTAT;
import com.pcc.tk.tbf.TbfTKLAWTYPE;
import com.pcc.tk.tbf.TbfTKLAWYER;
import com.pcc.tk.tbf.TbfTKZONE;
import com.pcc.tk.tbm.TbmTKJOBCLIENT;
import com.pcc.tk.tbm.TbmTKJOBLAWYER;
import com.pcc.tk.tbo.TboTKCOURT;
import com.pcc.tk.tbo.TboTKJOB;
import com.pcc.tk.tbo.TboTKJOBCLIENT;
import com.pcc.tk.tbo.TboTKJOBCODE;
import com.pcc.tk.tbo.TboTKJOBLAWYER;
import com.pcc.tk.tbo.TboTKLAWSTAT;
import com.pcc.tk.tbo.TboTKLAWTYPE;
import com.pcc.tk.tbo.TboTKLAWYER;
import com.pcc.tk.tbo.TboTKZONE;

public class FrmTkJobEntr extends FWindow {

	private static final long serialVersionUID = 1L;
	
	public static void showData(LoginBean loginBean, String jobno, boolean insertMode ,Window winCall, String menuid2 , EventListener<Event> event1) throws Exception {
		
		winCall.setVisible(false);
		String newID = "FrmTkJobEntr" + menuid2; //ป้องกันซ้ำหลายเมนู
		FrmTkJobEntr frm1 = (FrmTkJobEntr) ZkUtil.callZulFile("/com/pcc/tk/zul/FrmTkJob/FrmTkJobEntr.zul");
		
		//==parameter
		frm1.setLoginBean(loginBean);
		frm1.p_JOBNO = jobno;
		frm1.p_win_call = winCall;
		frm1.p_Event = event1;
		frm1.p_insertMode = insertMode;
		
		//==ค่าอื่นๆ
		frm1.setAppendMode(newID, winCall.getWidth(), winCall.getParent());

	}

	public Button btnExit;
	public Button btnSave;
	public Button btnDelete;
	public MyDatebox tdbJOBDATE;
	public Textbox txtLAWSTATID;
	public Textbox txtLAWSTATNAME;
	public Textbox txtJOBNO;
	public Button btnAdd;
	public Div div1;
	public MyDatebox tdbSENDJOBDATE;
	public Textbox txtJOBCODE;
	public Textbox txtJOBCODENAME;
	public Textbox txtCUSTCONTACTNO;
	public Textbox txtPLAINT;
	public Textbox txtLAWTYPEID;
	public Textbox txtLAWTYPENAME;
	public MyDatebox tdbSUEATDATE;
	public MyDatebox tdbSUEDDATE;
	public MyDecimalbox decSUECOSTAMT;
	public MyDecimalbox decFEEAMT;
	public Textbox txtREMARK1;
	public MyDatebox tdbLAWBLACKDATE;
	public Textbox txtLAWBLACKNO;
	public MyDatebox tdbLAWREDDATE;
	public Textbox txtLAWREDNO;
	public Textbox txtCOURTID;
	public Textbox txtCOURTNAME;
	public Textbox txtZONEID;
	public Textbox txtZONENAME;
	public Textbox txtREMARK2;
	public Label lblLAWSTAT1;
	public Intbox intCLIENTSEQ;
	public Textbox txtCLIENTTITLE;
	public Textbox txtCLIENTFNAME;
	public Textbox txtCLIENTLNAME;
	public Textbox txtTELNO;
	public Button btnAddCus;
	public Button btnUndoCus;
	public Grid grdCust;
	public Column grcCLIENTSEQ;
	public Textbox txtLAWYERID;
	public Textbox txtLAWYERNAME;
	public Button btnAddLawyer;
	public Grid grdLawyer;
	public Textbox txtINSDBY;
	public Textbox txtINSDTE;
	public Textbox txtUPBY;
	public Textbox txtUPDT;

	private java.util.List<FModelHasMap> lst_Cust = new java.util.ArrayList<FModelHasMap>();
	private java.util.List<FModelHasMap> lst_Lawyer = new java.util.ArrayList<FModelHasMap>();
	private int edit_SEQ_CLIENT = 0;
	
	public String p_JOBNO;
	public Window p_win_call = null;
	public EventListener<Event> p_Event;
	public boolean p_insertMode = false;

	@Override
	public void setFormObj() {

		btnExit = (Button) getFellow("btnExit");
		btnSave = (Button) getFellow("btnSave");
		btnDelete = (Button) getFellow("btnDelete");
		tdbJOBDATE = (MyDatebox) getFellow("tdbJOBDATE");
		txtLAWSTATID = (Textbox) getFellow("txtLAWSTATID");
		txtLAWSTATNAME = (Textbox) getFellow("txtLAWSTATNAME");
		txtJOBNO = (Textbox) getFellow("txtJOBNO");
		btnAdd = (Button) getFellow("btnAdd");
		div1 = (Div) getFellow("div1");
		tdbSENDJOBDATE = (MyDatebox) getFellow("tdbSENDJOBDATE");
		txtJOBCODE = (Textbox) getFellow("txtJOBCODE");
		txtJOBCODENAME = (Textbox) getFellow("txtJOBCODENAME");
		txtCUSTCONTACTNO = (Textbox) getFellow("txtCUSTCONTACTNO");
		txtPLAINT = (Textbox) getFellow("txtPLAINT");
		txtLAWTYPEID = (Textbox) getFellow("txtLAWTYPEID");
		txtLAWTYPENAME = (Textbox) getFellow("txtLAWTYPENAME");
		tdbSUEATDATE = (MyDatebox) getFellow("tdbSUEATDATE");
		tdbSUEDDATE = (MyDatebox) getFellow("tdbSUEDDATE");
		decSUECOSTAMT = (MyDecimalbox) getFellow("decSUECOSTAMT");
		decFEEAMT = (MyDecimalbox) getFellow("decFEEAMT");
		txtREMARK1 = (Textbox) getFellow("txtREMARK1");
		tdbLAWBLACKDATE = (MyDatebox) getFellow("tdbLAWBLACKDATE");
		txtLAWBLACKNO = (Textbox) getFellow("txtLAWBLACKNO");
		tdbLAWREDDATE = (MyDatebox) getFellow("tdbLAWREDDATE");
		txtLAWREDNO = (Textbox) getFellow("txtLAWREDNO");
		txtCOURTID = (Textbox) getFellow("txtCOURTID");
		txtCOURTNAME = (Textbox) getFellow("txtCOURTNAME");
		txtZONEID = (Textbox) getFellow("txtZONEID");
		txtZONENAME = (Textbox) getFellow("txtZONENAME");		
		txtREMARK2 = (Textbox) getFellow("txtREMARK2");
		lblLAWSTAT1 = (Label) getFellow("lblLAWSTAT1");
		intCLIENTSEQ = (Intbox) getFellow("intCLIENTSEQ");
		txtCLIENTTITLE = (Textbox) getFellow("txtCLIENTTITLE");
		txtCLIENTFNAME = (Textbox) getFellow("txtCLIENTFNAME");
		txtCLIENTLNAME = (Textbox) getFellow("txtCLIENTLNAME");
		txtTELNO = (Textbox) getFellow("txtTELNO");
		btnAddCus = (Button) getFellow("btnAddCus");
		btnUndoCus = (Button) getFellow("btnUndoCus");
		grdCust = (Grid) getFellow("grdCust");
		grcCLIENTSEQ = (Column) getFellow("grcCLIENTSEQ");
		txtLAWYERID = (Textbox) getFellow("txtLAWYERID");
		txtLAWYERNAME = (Textbox) getFellow("txtLAWYERNAME");
		btnAddLawyer = (Button) getFellow("btnAddLawyer");
		grdLawyer = (Grid) getFellow("grdLawyer");
		txtINSDBY = (Textbox) getFellow("txtINSDBY");
		txtINSDTE = (Textbox) getFellow("txtINSDTE");
		txtUPBY = (Textbox) getFellow("txtUPBY");
		txtUPDT = (Textbox) getFellow("txtUPDT");

	}

	@Override
	public void addEnterIndex() {

		addEnterIndex(btnExit);
		addEnterIndex(btnSave);
		//addEnterIndex(btnDelete);
		addEnterIndex(tdbJOBDATE);
		addEnterIndex(txtLAWSTATID);
		// addEnterIndex(txtLAWSTATNAME);
		//addEnterIndex(txtJOBNO);
		addEnterIndex(btnAdd);
		addEnterIndex(tdbSENDJOBDATE);
		addEnterIndex(txtJOBCODE);
		// addEnterIndex(txtJOBCODENAME);
		addEnterIndex(txtCUSTCONTACTNO);
		addEnterIndex(txtPLAINT);
		addEnterIndex(txtLAWTYPEID);
		addEnterIndex(tdbSUEATDATE);
		addEnterIndex(tdbSUEDDATE);
		addEnterIndex(decSUECOSTAMT);
		addEnterIndex(decFEEAMT);
		addEnterIndex(txtREMARK1);
		addEnterIndex(tdbLAWBLACKDATE);
		addEnterIndex(txtLAWBLACKNO);
		addEnterIndex(tdbLAWREDDATE);
		addEnterIndex(txtLAWREDNO);
		addEnterIndex(txtCOURTID);
		// addEnterIndex(txtCOURTNAME);
		addEnterIndex(txtZONEID);
		addEnterIndex(txtREMARK2);
		addEnterIndex(intCLIENTSEQ);
		addEnterIndex(txtCLIENTTITLE);
		addEnterIndex(txtCLIENTFNAME);
		addEnterIndex(txtCLIENTLNAME);
		addEnterIndex(txtTELNO);
		addEnterIndex(btnAddCus);
		addEnterIndex(btnUndoCus);
		addEnterIndex(txtLAWYERID);
		// addEnterIndex(txtLAWYERNAME);
		addEnterIndex(btnAddLawyer);
		// addEnterIndex(txtINSDBY);
		// addEnterIndex(txtINSDTE);
		// addEnterIndex(txtUPBY);
		// addEnterIndex(txtUPDT);

	}

	@Override
	public void formInit() {
		try {
			ZkUtil.setGridHeaderStyle(grdCust);
			grdCust.setRowRenderer(getRowRendererCust());
			
			ZkUtil.setGridHeaderStyle(grdLawyer);
			grdLawyer.setRowRenderer(getRowRendererLawyer());
			
			clearData();
			if (!Fnc.isEmpty(this.p_JOBNO)) {
				read_record(this.p_JOBNO);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}

	}

	public void clearData() {

		// ==set visible, readonly ,disable ,focus
		div1.setVisible(false);
		btnExit.setVisible(true);
		btnSave.setVisible(false);
		btnDelete.setVisible(false);

		tdbJOBDATE.setReadonly(false);
		tdbJOBDATE.focus();
		txtLAWSTATID.setReadonly(false);
		//txtJOBNO.setReadonly(true);
		btnAdd.setVisible(true);
		btnUndoCus.setVisible(false);

		// == set value
		tdbJOBDATE.setValue(null);
		txtLAWSTATID.setValue("");
		txtLAWSTATNAME.setValue("");
		txtJOBNO.setValue("");
		tdbSENDJOBDATE.setValue(null);
		txtJOBCODE.setValue("");
		txtJOBCODENAME.setValue("");
		txtCUSTCONTACTNO.setValue("");
		txtPLAINT.setValue("");
		txtLAWTYPEID.setValue("");
		txtLAWTYPENAME.setValue("");
		tdbSUEATDATE.setValue(null);
		tdbSUEDDATE.setValue(null);
		decSUECOSTAMT.setValue(BigDecimal.ZERO);
		decFEEAMT.setValue(BigDecimal.ZERO);
		txtREMARK1.setValue("");
		tdbLAWBLACKDATE.setValue(null);
		txtLAWBLACKNO.setValue("");
		tdbLAWREDDATE.setValue(null);
		txtLAWREDNO.setValue("");
		txtCOURTID.setValue("");
		txtCOURTNAME.setValue("");
		txtZONEID.setValue("");
		txtZONENAME.setValue("");
		txtREMARK2.setValue("");
		lblLAWSTAT1.setValue("???");
		
		clearBoxCust();
		clearBoxLawyer();
		
		txtINSDBY.setValue("");
		txtINSDTE.setValue("");
		txtUPBY.setValue("");
		txtUPDT.setValue("");

		lst_Cust.clear();
		lst_Lawyer.clear();
		grdCust.getRows().getChildren().clear();
		grdLawyer.getRows().getChildren().clear();

	}

	public void onOK() {
		try {
			if (focusObj == txtJOBNO) {
				if (!txtJOBNO.isReadonly()) {
					super.onOK();
				}
			} else if (focusObj == txtLAWSTATID) {
				if (Fnc.isEmpty(txtLAWSTATID.getValue())) {
					popupLAWSTATID();
				} else {
					super.onOK();
				}
				
			} else if (focusObj == txtJOBCODE) {
				if (Fnc.isEmpty(txtJOBCODE.getValue())) {
					popupJOBCODE();
				} else {
					super.onOK();
				}
				
			} else if (focusObj == txtLAWTYPEID) {
				if (Fnc.isEmpty(txtLAWTYPEID.getValue())) {
					popupLAWTYPEID();
				} else {
					super.onOK();
				}
				
			} else if (focusObj == txtCOURTID) {
				if (Fnc.isEmpty(txtCOURTID.getValue())) {
					popupCOURTID();
				} else {
					super.onOK();
				}
				
			} else if (focusObj == txtZONEID) {
				if (Fnc.isEmpty(txtZONEID.getValue())) {
					popupZONEID();
				} else {
					super.onOK();
				}
			
			} else if (focusObj == txtLAWYERID) {
				if (Fnc.isEmpty(txtLAWYERID.getValue())) {
					popupLAWYERID();
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

	public void onClick_btnAdd() {
		try {
			if (tdbJOBDATE.getValue() == null) {
				throw new Exception("ต้องระบุ " + tdbJOBDATE.getTooltiptext());
			}

			if (Fnc.isEmpty(txtLAWSTATID.getValue())) {
				throw new Exception("ต้องระบุ " + txtLAWSTATID.getTooltiptext());
			}
			if (read_LAWSTATID(Fnc.getIntFromStr(txtLAWSTATID.getValue())) == false) {
				throw new Exception("ระบุ " + txtLAWSTATID.getTooltiptext() + "ไม่ถูกต้อง");
			}

			Msg.confirm2(Labels.getLabel("comm.label.ComfirmNew") + " ?", this, "doOnClick_btnAdd");

		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}

	}

	public void doOnClick_btnAdd() {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			dbc.beginTrans();

			TboTKJOB table1 = new TboTKJOB();

			table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());

			String newdoc = TkRunning.genJOBNO(dbc, this.getLoginBean(), tdbJOBDATE.getValue());
			table1.setJOBNO(newdoc);

			table1.setJOBDATE(tdbJOBDATE.getSqlDate());
			table1.setLAWSTATID(Fnc.getIntFromStr(txtLAWSTATID.getValue()));
			table1.setJOBSTAT("8");

			table1.setINSBY(this.getLoginBean().getUSER_ID());
			table1.setINSDTE(FnDate.getTodaySqlDateTime());
			table1.setUPDBY(this.getLoginBean().getUSER_ID());
			table1.setUPDDTE(FnDate.getTodaySqlDateTime());

			TbfTKJOB.insert(table1);

			read_record(table1.getJOBNO());
			tdbSENDJOBDATE.focus();

			dbc.commit();
		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}

	}

	public boolean read_record(String jobno) throws Exception {
		boolean res = false;

		TboTKJOB table1 = new TboTKJOB();

		table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		table1.setJOBNO(jobno);

		if (TbfTKJOB.find(table1)) {
			
			if (table1.getJOBSTAT().equals("9")) {
				throw new Exception("รายการมีการยกเลิกแล้ว");
			}
			
			if (table1.getCLOSEDATE() != null) {
				throw new Exception("สถานะปิดงานแล้ว");
			}
			
			table1.setJOBSTAT("8");
			table1.setUPDBY(this.getLoginBean().getUSER_ID());
			table1.setUPDDTE(FnDate.getTodaySqlDateTime());
			TbfTKJOB.update(table1);

			// == setvalue
			tdbJOBDATE.setValue(table1.getJOBDATE());
			txtLAWSTATID.setValue(table1.getLAWSTATID() + "");
			read_LAWSTATID(table1.getLAWSTATID());
			txtJOBNO.setValue(table1.getJOBNO());
			tdbSENDJOBDATE.setValue(table1.getSENDJOBDATE());
			txtJOBCODE.setValue(table1.getJOBCODE() + "");
			read_JOBCODE(table1.getJOBCODE());
			txtCUSTCONTACTNO.setValue(table1.getCUSTCONTACTNO());
			txtPLAINT.setValue(table1.getPLAINT());
			txtLAWTYPEID.setValue(table1.getLAWTYPEID()+"");
			read_LAWTYPE(table1.getLAWTYPEID());
			tdbSUEATDATE.setValue(table1.getSUEATDATE());
			tdbSUEDDATE.setValue(table1.getSUEDDATE());
			decSUECOSTAMT.setValue(table1.getSUECOSTAMT());
			decFEEAMT.setValue(table1.getFEEAMT());
			txtREMARK1.setValue(table1.getREMARK1());
			tdbLAWBLACKDATE.setValue(table1.getLAWBLACKDATE());
			txtLAWBLACKNO.setValue(table1.getLAWBLACKNO());
			tdbLAWREDDATE.setValue(table1.getLAWREDDATE());
			txtLAWREDNO.setValue(table1.getLAWREDNO());
			txtCOURTID.setValue(table1.getCOURTID() + "");
			read_COURTID(table1.getCOURTID());
			read_ZONEID(table1.getZONEID());
			txtREMARK2.setValue(table1.getREMARK2());
			lblLAWSTAT1.setValue(txtLAWSTATNAME.getValue()+"ที่");
			grcCLIENTSEQ.setLabel(txtLAWSTATNAME.getValue()+"ที่");
			txtINSDBY.setValue(table1.getINSBY());
			txtINSDTE.setValue(FnDate.displayDateTimeString(table1.getINSDTE()));
			txtUPBY.setValue(table1.getUPDBY());
			txtUPDT.setValue(FnDate.displayDateTimeString(table1.getUPDDTE()));

			// clear input detail
			clearBoxCust();
			clearBoxLawyer();
			
			showGridCust();
			showGridLawyer();

			// ==visible , readonly
			tdbJOBDATE.setReadonly(true);
			txtLAWSTATID.setReadonly(true);
			//txtJOBNO.setReadonly(true);

			btnExit.setVisible(false);
			btnSave.setVisible(true);
			if (p_insertMode == true) {
				btnDelete.setVisible(true);
			} else {
				btnDelete.setVisible(false);
			}
			btnAdd.setVisible(false);
			div1.setVisible(true);

			res = true;
		}

		return res;
	}

	public void clearBoxCust() {
		intCLIENTSEQ.setText("");
		txtCLIENTTITLE.setValue("");
		txtCLIENTFNAME.setValue("");
		txtCLIENTLNAME.setValue("");
		txtTELNO.setValue("");
		btnAddCus.setLabel(Labels.getLabel("comm.label.add"));
		btnUndoCus.setVisible(false);
		disableCustGridButton(false);
	}

	public void clearBoxLawyer() {
		txtLAWYERID.setValue("");
		txtLAWYERNAME.setValue("");
	}
	
	public void popupLAWSTATID() {
		if (!txtLAWSTATID.isReadonly()) {
			FfTKLAWSTAT.popup(this.getLoginBean(), this, "read_LAWSTATID");
		}
	}
	
	public boolean read_LAWSTATID(int lawstatid) throws Exception {

		TboTKLAWSTAT tb1 = new TboTKLAWSTAT();

		tb1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		tb1.setLAWSTATID(lawstatid);
		
		if (TbfTKLAWSTAT.find(tb1)) {
			txtLAWSTATID.setValue(tb1.getLAWSTATID()+"");
			txtLAWSTATNAME.setValue(tb1.getLAWSTATNAME());
			return true;
		} else {
			txtLAWSTATNAME.setValue("");
			return false;
		}

	}

	public void popupJOBCODE() {
		if (!txtJOBCODE.isReadonly()) {
			FfTKJOBCODE.popup(this.getLoginBean(), this, "read_JOBCODE");
		}
	}
	
	public boolean read_JOBCODE(int jobcode) throws Exception {
		
		TboTKJOBCODE tb1 = new TboTKJOBCODE();

		tb1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		tb1.setJOBCODE(jobcode);
		
		if (TbfTKJOBCODE.find(tb1)) {
			txtJOBCODE.setValue(tb1.getJOBCODE()+"");
			txtJOBCODENAME.setValue(tb1.getJOBNAME());
			return true;
		} else {
			txtJOBCODENAME.setValue("");
			return false;
		}

	}

	public void popupLAWTYPEID() {
		if (!txtLAWTYPEID.isReadonly()) {
			FfTKLAWTYPE.popup(this.getLoginBean(), this, "read_LAWTYPE");
		}
	}
	
	public boolean read_LAWTYPE(int lawtypeid) throws Exception {

		TboTKLAWTYPE tklawtype = new TboTKLAWTYPE();
		
		tklawtype.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		tklawtype.setLAWTYPEID(lawtypeid);
		
		if (TbfTKLAWTYPE.find(tklawtype)) {
			txtLAWTYPEID.setValue(tklawtype.getLAWTYPEID()+"");
			txtLAWTYPENAME.setValue(tklawtype.getLAWTYPENAME());
			return true;
		} else {
			txtLAWTYPENAME.setValue("");
			return false;
		}
		
	}
	
	public void popupCOURTID() {
		if (!txtCOURTID.isReadonly()) {
			FfTKCOURT.popup(this.getLoginBean(), this, "read_COURTID");
		}
	}
	
	public boolean read_COURTID(int courtid) throws Exception {
		
		TboTKCOURT tb1 = new TboTKCOURT();

		tb1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		tb1.setCOURTID(courtid);
		
		if (TbfTKCOURT.find(tb1)) {
			txtCOURTID.setValue(tb1.getCOURTID()+"");
			txtCOURTNAME.setValue(tb1.getCOURTNAME());
			return true;
		} else {
			txtCOURTNAME.setValue("");
			return false;
		}
		
	}
	
	public void popupZONEID() {
		if (!txtCOURTID.isReadonly()) {
			FfTKZONE.popup(this.getLoginBean(), this, "read_ZONEID");
		}
		
	}

	public boolean read_ZONEID(int zoneid) throws Exception {
		
		TboTKZONE tb1 = new TboTKZONE();

		tb1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		tb1.setZONEID(zoneid);
		
		if (TbfTKZONE.find(tb1)) {
			txtZONEID.setValue(tb1.getZONEID()+"");
			txtZONENAME.setValue(tb1.getZONENAME());
			return true;
		} else {
			txtZONENAME.setValue("");
			return false;
		}
		
	}

	public void doPopupJOBNO(String jobno) {

		Msg.confirm(Labels.getLabel("comm.label.ComfirmEdit") + " ?", "?", (event1) -> {
			if (Messagebox.Button.YES.equals(event1.getButton())) {

				try {
					if (read_record(jobno)) {
						tdbSENDJOBDATE.focus();
					}
				} catch (Exception e) {
					Msg.error(e);
				}
				
			}
		});
		
	}
	
	public void popupLAWYERID() {
		if (!txtLAWYERID.isReadonly()) {
			FfTKLAWYER.popup(this.getLoginBean(), this, "read_LAWYERID");
		}
	}

	public boolean read_LAWYERID(int lawyerid) throws Exception {

		TboTKLAWYER table1 = new TboTKLAWYER();

		table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		table1.setLAWYERID(lawyerid);

		if (TbfTKLAWYER.find(table1)) {
			txtLAWYERID.setValue(table1.getLAWYERID() + "");
			txtLAWYERNAME.setValue(table1.getLAWYERNAME());
			return true;
		} else {
			txtLAWYERNAME.setValue("");
			return false;
		}

	}

	private void validateData() throws Exception {

		if (Fnc.isEmpty(txtLAWSTATNAME.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtLAWSTATID.getTooltiptext() + "\" ");
		}
		if (Fnc.isEmpty(txtJOBCODENAME.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtJOBCODE.getTooltiptext() + "\" ");
		}
		if (Fnc.isEmpty(txtLAWTYPENAME.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtLAWTYPEID.getTooltiptext() + "\" ");
		}
		if (decSUECOSTAMT.getValue().compareTo(BigDecimal.ZERO) < 0) {
			throw new Exception("ระบุช่อง \"" + decSUECOSTAMT.getTooltiptext() + "\" ไม่ถูก");
		}
		if (decFEEAMT.getValue().compareTo(BigDecimal.ZERO) < 0) {
			throw new Exception("ระบุช่อง \"" + decFEEAMT.getTooltiptext() + "\" ไม่ถูก");
		}
		if (Fnc.isEmpty(txtCOURTNAME.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtCOURTID.getTooltiptext() + "\" ");
		}
		if (Fnc.isEmpty(txtZONENAME.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtZONEID.getTooltiptext() + "\" ");
		}

	}

	public void onClick_Save() {
		try {

			validateData();

			try (FDbc dbc = FDbc.connectMasterDb();) {
				TboTKJOB table1 = saveHeader(dbc);
				table1.setJOBSTAT("1");
				TbfTKJOB.update(dbc, table1);
			}

			Msg.info("บันทึกเรียกร้อย");
			this.onClose();

		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}

	}
	
	private TboTKJOB saveHeader(FDbc dbc) throws Exception {

		TboTKJOB table1 = new TboTKJOB();

		table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		table1.setJOBNO(txtJOBNO.getValue());

		if (TbfTKJOB.find(dbc, table1)) {

			if (table1.getJOBSTAT().equals("9")) {
				clearData();
				throw new Exception("รายการมีการยกเลิกแล้ว");
			}
			if (!table1.getJOBSTAT().equals("8")) {
				clearData();
				throw new Exception("สถานะรายการไม่ถูกต้อง");
			}
			if (table1.getCLOSEDATE() != null) {
				clearData();
				throw new Exception("สถานะปิดงานแล้ว");
			}

			table1.setSENDJOBDATE(tdbSENDJOBDATE.getSqlDate());
			table1.setJOBCODE(Fnc.getIntFromStr(txtJOBCODE.getValue()));
			table1.setCUSTCONTACTNO(txtCUSTCONTACTNO.getValue());
			table1.setPLAINT(txtPLAINT.getValue());
			table1.setSUEATDATE(tdbSUEATDATE.getSqlDate());
			table1.setSUEDDATE(tdbSUEDDATE.getSqlDate());
			table1.setSUECOSTAMT(decSUECOSTAMT.getValue());
			table1.setFEEAMT(decFEEAMT.getValue());
			table1.setREMARK1(txtREMARK1.getValue());
			table1.setLAWBLACKDATE(tdbLAWBLACKDATE.getSqlDate());
			table1.setLAWBLACKNO(txtLAWBLACKNO.getValue());
			table1.setLAWREDDATE(tdbLAWREDDATE.getSqlDate());
			table1.setLAWREDNO(txtLAWREDNO.getValue());
			table1.setCOURTID(Fnc.getIntFromStr(txtCOURTID.getValue()));
			table1.setZONEID(Fnc.getIntFromStr(txtZONEID.getValue()));
			table1.setREMARK2(txtREMARK2.getValue());
			// table1.setJOBSTAT("1");
			table1.setUPDBY(this.getLoginBean().getUSER_ID());
			table1.setUPDDTE(FnDate.getTodaySqlDateTime());
			table1.setLAWTYPEID(Fnc.getIntFromStr(txtLAWTYPEID.getValue()));

			TbfTKJOB.update(dbc, table1);

			return table1;
		} else {
			return null;
		}

	}
	
	public void onClick_btnAddCus() {
		try {
			if (intCLIENTSEQ.getValue() == null || intCLIENTSEQ.getValue().intValue() == 0) {
				throw new Exception("ต้องระบุ" + lblLAWSTAT1.getTooltiptext());
			}
			if (Fnc.isEmpty(txtCLIENTTITLE.getValue())) {
				throw new Exception("ต้องระบุ" + txtCLIENTTITLE.getTooltiptext());
			}
			if (Fnc.isEmpty(txtCLIENTFNAME.getValue())) {
				throw new Exception("ต้องระบุ" + txtCLIENTFNAME.getTooltiptext());
			}

			try (FDbc dbc = FDbc.connectMasterDb();) {

				saveHeader(dbc);

				TboTKJOBCLIENT jobc = new TboTKJOBCLIENT();

				jobc.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				jobc.setJOBNO(txtJOBNO.getValue());

				int newSeq = 0;
				if (btnUndoCus.isVisible() == false) {
					newSeq = TbmTKJOBCLIENT.maxSeq(dbc, this.getLoginBean().getCOMP_CDE(), txtJOBNO.getValue()) + 1;
					jobc.setSEQ(newSeq);
				} else {
					jobc.setSEQ(this.edit_SEQ_CLIENT);
					if (!TbfTKJOBCLIENT.find(dbc, jobc)) {
						throw new Exception("ไม่พบรายการที่จะแก้ไขอาจถูกลบโดย user อื่นๆ");
					}
				}

				jobc.setCLIENTTITLE(txtCLIENTTITLE.getValue());
				jobc.setCLIENTFNAME(txtCLIENTFNAME.getValue());
				jobc.setCLIENTLNAME(txtCLIENTLNAME.getValue());
				jobc.setTELNO(txtTELNO.getValue());
				jobc.setCLIENTSEQ(intCLIENTSEQ.getValue());
				jobc.setUPDBY(this.getLoginBean().getUSER_ID());
				jobc.setUPDDTE(FnDate.getTodaySqlDateTime());

				if (btnUndoCus.isVisible() == false) {
					TbfTKJOBCLIENT.insert(jobc);
				} else {
					TbfTKJOBCLIENT.update(jobc);
				}

				showGridCust();
				clearBoxCust();
				intCLIENTSEQ.focus();
			}

		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}
	}
	
	public void onClick_btnUndoCus() {
		try {
			clearBoxCust();
			intCLIENTSEQ.focus();
		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}
	}

	public void onChange_Data(HtmlBasedComponent comp) {
		try {
			if (comp != null) {

				if (!Fnc.isEmpty(comp.getId())) {
					ZkUtil.setAndClearOnChang(comp, this.objectList);
				}

				if (comp.getId().equals("txtLAWSTATID")) {
					read_LAWSTATID(Fnc.getIntFromStr(txtLAWSTATID.getValue()));
				}
				if (comp.getId().equals("txtJOBCODE")) {
					read_JOBCODE(Fnc.getIntFromStr(txtJOBCODE.getValue()));
				}
				if (comp.getId().equals("txtCOURTID")) {
					read_COURTID(Fnc.getIntFromStr(txtCOURTID.getValue()));
				}
				if (comp.getId().equals("txtLAWYERID")) {
					read_LAWYERID(Fnc.getIntFromStr(txtLAWYERID.getValue()));
				}
				if (comp.getId().equals("txtLAWTYPEID")) {
					read_LAWTYPE(Fnc.getIntFromStr(txtLAWTYPEID.getValue()));
				}
				if (comp.getId().equals("txtZONEID")) {
					read_ZONEID(Fnc.getIntFromStr(txtZONEID.getValue()));
				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showGridCust() throws WrongValueException, Exception {
		TbmTKJOBCLIENT.getDataByJobno(this.getLoginBean().getCOMP_CDE(), txtJOBNO.getValue(), lst_Cust);
		SimpleListModel rstModel = new SimpleListModel(lst_Cust);
		this.grdCust.setModel(rstModel);
	}

	private RowRenderer<?> getRowRendererCust() {
		return (row, data, index) -> {

			FModelHasMap rs = (FModelHasMap) data;
			int seq = index + 1;
			row.setStyle(ZkUtil.styleFindLookUp);

			row.setAttribute("DATA1", rs);

			// == append child ==//
			{
				Button btn1 = new Button();
				btn1.setLabel("แก้ไข");
				btn1.setAutodisable("self");
				btn1.addEventListener(Events.ON_CLICK, event -> onClick_EditCustRow(event));
				row.appendChild(btn1);
				row.setAttribute("btn_edit", btn1);
			}

			{
				Button btn1 = new Button();
				btn1.setLabel("ลบ");
				btn1.setSclass("buttondel");
				btn1.setAutodisable("self");
				btn1.addEventListener(Events.ON_CLICK, event -> onClick_DelCustRow(event));
				row.appendChild(btn1);
				row.setAttribute("btn_del", btn1);
			}

			row.appendChild(ZkUtil.gridIntbox(seq));
			row.appendChild(ZkUtil.gridIntbox(rs.getInt("CLIENTSEQ")));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("CUSTNAME")));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("TELNO")));

		};
	}

	private void onClick_EditCustRow(Event event) {
		try {
			Component comp = event.getTarget().getParent();
			var rs = (FModelHasMap) comp.getAttribute("DATA1");
			
			TboTKJOBCLIENT jobc = new TboTKJOBCLIENT();

			jobc.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			jobc.setJOBNO(txtJOBNO.getValue());
			jobc.setSEQ(rs.getInt("SEQ"));
			
			if (TbfTKJOBCLIENT.find(jobc)) {
				intCLIENTSEQ.setValue(jobc.getCLIENTSEQ());
				txtCLIENTTITLE.setValue(jobc.getCLIENTTITLE());
				txtCLIENTFNAME.setValue(jobc.getCLIENTFNAME());
				txtCLIENTLNAME.setValue(jobc.getCLIENTLNAME());
				txtTELNO.setValue(jobc.getTELNO());
				
				intCLIENTSEQ.focus();
				edit_SEQ_CLIENT = rs.getInt("SEQ");
				
				btnAddCus.setLabel(Labels.getLabel("comm.label.save"));
				btnUndoCus.setVisible(true);
				disableCustGridButton(true);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}
	}

	private void onClick_DelCustRow(Event event) {

		Component comp = event.getTarget().getParent();
		var rs = (FModelHasMap) comp.getAttribute("DATA1");

		Msg.confirm(Labels.getLabel("comm.label.deleteComfirm") + " ?", "?", (event1) -> {
			if (Messagebox.Button.YES.equals(event1.getButton())) {

				try {

					TboTKJOBCLIENT jobc = new TboTKJOBCLIENT();

					jobc.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
					jobc.setJOBNO(txtJOBNO.getValue());
					jobc.setSEQ(rs.getInt("SEQ"));

					TbfTKJOBCLIENT.delete(jobc);
					showGridCust();

				} catch (Exception ex) {
					ex.printStackTrace();
					Msg.error(ex);
				}

			}
		});

	}

	private void disableCustGridButton(boolean disable) {

		java.util.List<Component> lstRow = grdCust.getRows().getChildren();
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

	public void onClick_btnAddLawyer() {
		try {
			if (Fnc.isEmpty(txtLAWYERNAME.getValue())) {
				throw new Exception("ต้องระบุ"+txtLAWYERID.getTooltiptext());
			}
			
			try(FDbc dbc = FDbc.connectMasterDb()) {
				saveHeader(dbc);
				
				TboTKJOBLAWYER table1 = new TboTKJOBLAWYER();
				
				table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				table1.setJOBNO(txtJOBNO.getValue());
				
				int newseq = TbmTKJOBLAWYER.maxSeq(dbc, this.getLoginBean().getCOMP_CDE(), txtJOBNO.getValue())+1;
				table1.setSEQ(newseq);
				
				table1.setLAWYERID(Fnc.getIntFromStr(txtLAWYERID.getValue()));
				table1.setUPDBY(this.getLoginBean().getUSER_ID());
				table1.setUPDDTE(FnDate.getTodaySqlDateTime());

				TbfTKJOBLAWYER.insert(table1);
				
				showGridLawyer();
				clearBoxLawyer();
				txtLAWYERID.focus();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}
	}
	
	private void showGridLawyer() throws Exception {
		ManTkJob.getDataByJobno(this.getLoginBean().getCOMP_CDE(), txtJOBNO.getValue(), lst_Lawyer);
		SimpleListModel rstModel = new SimpleListModel(lst_Lawyer);
		this.grdLawyer.setModel(rstModel);
	}

	private RowRenderer<?> getRowRendererLawyer() {

		return (row, data, index) -> {

			FModelHasMap rs = (FModelHasMap) data;
			int seq = index + 1;
			row.setStyle(ZkUtil.styleFindLookUp);

			row.setAttribute("DATA1", rs);

			{
				Button btn1 = new Button();
				btn1.setLabel("ลบ");
				btn1.setSclass("buttondel");
				btn1.setAutodisable("self");
				btn1.addEventListener(Events.ON_CLICK, event -> onClick_DelLawyerRow(event));
				row.appendChild(btn1);
				row.setAttribute("btn_del", btn1);
			}

			row.appendChild(ZkUtil.gridIntbox(seq));
			row.appendChild(ZkUtil.gridIntbox(rs.getInt("LAWYERID")));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("LAWYERNAME")));

		};
	}

	private void onClick_DelLawyerRow(Event event) {

		Component comp = event.getTarget().getParent();
		var rs = (FModelHasMap) comp.getAttribute("DATA1");

		Msg.confirm(Labels.getLabel("comm.label.deleteComfirm") + " ?", "?", (event1) -> {
			if (Messagebox.Button.YES.equals(event1.getButton())) {

				try {

					TboTKJOBLAWYER joblw = new TboTKJOBLAWYER();

					joblw.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
					joblw.setJOBNO(txtJOBNO.getValue());
					joblw.setSEQ(rs.getInt("SEQ"));

					TbfTKJOBLAWYER.delete(joblw);
					showGridLawyer();

				} catch (Exception ex) {
					ex.printStackTrace();
					Msg.error(ex);
				}

			}
		});
	}

	public void onClick_Delete() {
		Msg.confirm(Labels.getLabel("comm.label.deleteComfirm") + " ?", "?", (event1) -> {
			if (Messagebox.Button.YES.equals(event1.getButton())) {
				
				try(FDbc dbc = FDbc.connectMasterDb()) {
					dbc.beginTrans();
				
					TboTKJOB table1 = new TboTKJOB();

					table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
					table1.setJOBNO(txtJOBNO.getValue());

					if (TbfTKJOB.find(dbc, table1)) {

						if (table1.getJOBSTAT().equals("9")) {
							clearData();
							throw new Exception("รายการมีการยกเลิกแล้ว");
						}
						if (!table1.getJOBSTAT().equals("8")) {
							clearData();
							throw new Exception("สถานะรายการไม่ถูกต้อง");
						}
						if (table1.getCLOSEDATE() != null) {
							clearData();
							throw new Exception("สถานะปิดงานแล้ว");
						}

						 
						table1.setJOBSTAT("9");
						table1.setUPDBY(this.getLoginBean().getUSER_ID());
						table1.setUPDDTE(FnDate.getTodaySqlDateTime());
						table1.setDELBY(this.getLoginBean().getUSER_ID());
						table1.setDELDTE(FnDate.getTodaySqlDateTime());

						TbfTKJOB.update(dbc, table1);
					}
					
					dbc.commit();
					
					this.onClose();
				} catch (Exception ex) {
					ex.printStackTrace();
					Msg.error(ex);
				}
				//==end
			}
			
		});
	}

	
}

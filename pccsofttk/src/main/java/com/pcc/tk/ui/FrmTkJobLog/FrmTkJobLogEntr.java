package com.pcc.tk.ui.FrmTkJobLog;

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
import com.pcc.tk.find.FfTKEXPENSES;
import com.pcc.tk.find.FfTKJOBSTAT;
import com.pcc.tk.progman.ManTkJob;
import com.pcc.tk.progman.ManTkJobLog;
import com.pcc.tk.tbf.TbfTKCOURT;
import com.pcc.tk.tbf.TbfTKEXPENSES;
import com.pcc.tk.tbf.TbfTKJOB;
import com.pcc.tk.tbf.TbfTKJOBCODE;
import com.pcc.tk.tbf.TbfTKJOBEXPENSES;
import com.pcc.tk.tbf.TbfTKJOBLOG;
import com.pcc.tk.tbf.TbfTKJOBSTAT;
import com.pcc.tk.tbf.TbfTKLAWSTAT;
import com.pcc.tk.tbf.TbfTKLAWTYPE;
import com.pcc.tk.tbf.TbfTKZONE;
import com.pcc.tk.tbm.TbmTKJOBCLIENT;
import com.pcc.tk.tbm.TbmTKJOBEXPENSES;
import com.pcc.tk.tbm.TbmTKJOBLOG;
import com.pcc.tk.tbo.TboTKCOURT;
import com.pcc.tk.tbo.TboTKEXPENSES;
import com.pcc.tk.tbo.TboTKJOB;
import com.pcc.tk.tbo.TboTKJOBCODE;
import com.pcc.tk.tbo.TboTKJOBEXPENSES;
import com.pcc.tk.tbo.TboTKJOBLOG;
import com.pcc.tk.tbo.TboTKJOBSTAT;
import com.pcc.tk.tbo.TboTKLAWSTAT;
import com.pcc.tk.tbo.TboTKLAWTYPE;
import com.pcc.tk.tbo.TboTKZONE;

public class FrmTkJobLogEntr extends FWindow {

	private static final long serialVersionUID = 1L;
	
	public static void showData(LoginBean loginBean, String jobno ,Window winCall, String menuid2 , EventListener<Event> event1) throws Exception {
		
		winCall.setVisible(false);
		String newID = "FrmTkJobLogEntr" + menuid2; //ป้องกันซ้ำหลายเมนู
		FrmTkJobLogEntr frm1 = (FrmTkJobLogEntr) ZkUtil.callZulFile("/com/pcc/tk/zul/FrmTkJobLog/FrmTkJobLogEntr.zul");
		
		//==parameter
		frm1.setLoginBean(loginBean);
		frm1.p_JOBNO = jobno;
		frm1.p_win_call = winCall;
		frm1.p_Event = event1;
		
		//==ค่าอื่นๆ
		frm1.setAppendMode(newID, winCall.getWidth(), winCall.getParent());
		
	}
	
	public Button btnExit;
	//public Button btnBack;
	public Textbox txtJOBNO;
	public MyDatebox tdbJOBDATE;
	public Textbox txtLAWSTATID;
	public Textbox txtLAWSTATNAME;
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
	public Grid grdCust;
	public Column grcCLIENTSEQ;
	public Grid grdLawyer;
	public MyDatebox tdbLOGDATE;
	public Textbox txtJOBSTATID;
	public Textbox txtJOBSTATNAME;
	public Textbox txtJOBSTATREMARK;
	public Button btnAddJobstat;
	public Button btnUndoJobstat;
	public Grid grdJobState;
	public Textbox txtEXPENSESID;
	public Textbox txtEXPENSESNAME;
	public MyDecimalbox decEXPENSESAMT;
	public MyDecimalbox decWITHDRAWAL_AMT;
	public MyDecimalbox decEXPCOM_ADV;
	public Textbox txtEXP_REMARK;
	public Button btnAddJobExp;
	public Button btnUndoJobExp;
	public Grid grdJobExp;
	public MyDecimalbox decSUM_EXPENSESAMT;
	public MyDecimalbox decSUM_WITHDRAWAL_AMT;
	public MyDecimalbox decSUM_EXPCOM_ADV;

	private java.util.List<FModelHasMap> lst_Cust = new java.util.ArrayList<FModelHasMap>();
	private java.util.List<FModelHasMap> lst_Lawyer = new java.util.ArrayList<FModelHasMap>();
	private java.util.List<FModelHasMap> lst_JobStat = new java.util.ArrayList<FModelHasMap>();
	private java.util.List<FModelHasMap> lst_JobExp = new java.util.ArrayList<FModelHasMap>();
	private int edit_SEQ_LOG = 0;
	private int edit_SEQ_EXP = 0;

	public String p_JOBNO = "";
	public Window p_win_call = null;
	public EventListener<Event> p_Event;
	
	@Override
	public void setFormObj() {

		btnExit = (Button) getFellow("btnExit");
		//btnBack = (Button) getFellow("btnBack");
		txtJOBNO = (Textbox) getFellow("txtJOBNO");
		tdbJOBDATE = (MyDatebox) getFellow("tdbJOBDATE");
		txtLAWSTATID = (Textbox) getFellow("txtLAWSTATID");
		txtLAWSTATNAME = (Textbox) getFellow("txtLAWSTATNAME");
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
		grdCust = (Grid) getFellow("grdCust");
		grcCLIENTSEQ = (Column) getFellow("grcCLIENTSEQ");
		grdLawyer = (Grid) getFellow("grdLawyer");
		tdbLOGDATE = (MyDatebox) getFellow("tdbLOGDATE");
		txtJOBSTATID = (Textbox) getFellow("txtJOBSTATID");
		txtJOBSTATNAME = (Textbox) getFellow("txtJOBSTATNAME");
		txtJOBSTATREMARK = (Textbox) getFellow("txtJOBSTATREMARK");
		btnAddJobstat = (Button) getFellow("btnAddJobstat");
		btnUndoJobstat = (Button) getFellow("btnUndoJobstat");
		grdJobState = (Grid) getFellow("grdJobState");
		txtEXPENSESID = (Textbox) getFellow("txtEXPENSESID");
		txtEXPENSESNAME = (Textbox) getFellow("txtEXPENSESNAME");
		decEXPENSESAMT = (MyDecimalbox) getFellow("decEXPENSESAMT");
		decWITHDRAWAL_AMT = (MyDecimalbox) getFellow("decWITHDRAWAL_AMT");
		decEXPCOM_ADV = (MyDecimalbox) getFellow("decEXPCOM_ADV");
		txtEXP_REMARK = (Textbox) getFellow("txtEXP_REMARK");
		btnAddJobExp = (Button) getFellow("btnAddJobExp");
		btnUndoJobExp = (Button) getFellow("btnUndoJobExp");
		grdJobExp = (Grid) getFellow("grdJobExp");
		decSUM_EXPENSESAMT = (MyDecimalbox) getFellow("decSUM_EXPENSESAMT");
		decSUM_WITHDRAWAL_AMT = (MyDecimalbox) getFellow("decSUM_WITHDRAWAL_AMT");
		decSUM_EXPCOM_ADV = (MyDecimalbox) getFellow("decSUM_EXPCOM_ADV");

	}

	@Override
	public void addEnterIndex() {

		addEnterIndex(btnExit);
		//addEnterIndex(btnBack);
		addEnterIndex(txtJOBNO);

		addEnterIndex(tdbLOGDATE);
		addEnterIndex(txtJOBSTATID);
		addEnterIndex(txtJOBSTATREMARK);
		addEnterIndex(btnAddJobstat);
		addEnterIndex(btnUndoJobstat);
		
		addEnterIndex(txtEXPENSESID);
		//addEnterIndex(decEXPENSESAMT);
		addEnterIndex(decWITHDRAWAL_AMT);
		addEnterIndex(decEXPCOM_ADV);
		addEnterIndex(txtEXP_REMARK);
		addEnterIndex(btnAddJobExp);
		addEnterIndex(btnUndoJobExp);

	}

	@Override
	public void formInit() {
		try {
			ZkUtil.setGridHeaderStyle(grdCust);
			grdCust.setRowRenderer(getRowRendererCust());

			ZkUtil.setGridHeaderStyle(grdLawyer);
			grdLawyer.setRowRenderer(getRowRendererLawyer());

			ZkUtil.setGridHeaderStyle(grdJobState);
			grdJobState.setRowRenderer(getRowRendererJobState());

			ZkUtil.setGridHeaderStyle(grdJobExp);
			grdJobExp.setRowRenderer(getRowRendererJobExp());
			
			clearData();
			
			doPopupJOBNO(p_JOBNO);

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void clearData() {

		// == set visible , readonly ,focus
		//div1.setVisible(false);
		txtJOBNO.setReadonly(false);
		txtJOBNO.focus();
		btnExit.setVisible(true);
		//btnBack.setVisible(false);

		// == set value
		txtJOBNO.setValue("");
		tdbJOBDATE.setValue(null);
		txtLAWSTATID.setValue("");
		txtLAWSTATNAME.setValue("");
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
		txtEXPENSESID.setValue("");
		txtEXPENSESNAME.setValue("");
		decEXPENSESAMT.setValue(BigDecimal.ZERO);
		decWITHDRAWAL_AMT.setValue(BigDecimal.ZERO);
		decEXPCOM_ADV.setValue(BigDecimal.ZERO);
		txtEXP_REMARK.setValue("");
		decSUM_EXPENSESAMT.setValue(BigDecimal.ZERO);
		decSUM_WITHDRAWAL_AMT.setValue(BigDecimal.ZERO);
		decSUM_EXPCOM_ADV.setValue(BigDecimal.ZERO);

		clearBoxJobStat();
		clearBoxJobExp();

	}

	public void onOK() {
		try {
			if (focusObj == txtJOBNO) {
				super.onOK();
			} else if (focusObj == decEXPENSESAMT) {
				if (!ZkUtil.doOnChange(decEXPENSESAMT)) {
					onchange_decEXPCOM_ADV();
				}
				super.onOK();
				
			} else if (focusObj == decWITHDRAWAL_AMT) {
				if (!ZkUtil.doOnChange(decWITHDRAWAL_AMT)) {
					onchange_decWITHDRAWAL_AMT();
				}
				super.onOK();
				
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
	
	public void onChange_Data(HtmlBasedComponent comp) {
		try {
			if (comp != null) {

				if (!Fnc.isEmpty(comp.getId())) {
					ZkUtil.setAndClearOnChang(comp, this.objectList);
				}

				if (comp.getId().equals("txtJOBSTATID")) {
					read_JOBSTATID(Fnc.getIntFromStr(txtJOBSTATID.getValue()));
				}
				if (comp.getId().equals("txtEXPENSESID")) {
					read_EXPENSESID(Fnc.getIntFromStr(txtEXPENSESID.getValue()));
				}
				if (comp.getId().equals("decWITHDRAWAL_AMT")) {
					onchange_decWITHDRAWAL_AMT();
				}
				if (comp.getId().equals("decEXPCOM_ADV")) {
					onchange_decEXPCOM_ADV();
				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onClick_Back() {
		clearData();
	}

	public void doPopupJOBNO(String jobno) {

		try {
			if (read_record(jobno)) {
				tdbLOGDATE.focus();
			}
		} catch (Exception e) {
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

			if (table1.getJOBSTAT().equals("8")) {
				throw new Exception("สถานะไม่สมบูรณ์");
			}

			if (table1.getCLOSEDATE() != null) {
				throw new Exception("สถานะปิดงานแล้ว");
			}

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
			grcCLIENTSEQ.setLabel(txtLAWSTATNAME.getValue() + "ที่");

			// clear input detail
			showGridCust();
			showGridLawyer();
			showGridJobState();
			showGridJobExp();

			// ==visible , readonly
			txtJOBNO.setReadonly(true);
			btnExit.setVisible(true);
			//btnBack.setVisible(true);
			div1.setVisible(true);

			res = true;
		}

		return res;
	}

	public boolean read_LAWSTATID(int lawstatid) throws Exception {

		TboTKLAWSTAT tb1 = new TboTKLAWSTAT();

		tb1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		tb1.setLAWSTATID(lawstatid);

		if (TbfTKLAWSTAT.find(tb1)) {
			txtLAWSTATID.setValue(tb1.getLAWSTATID() + "");
			txtLAWSTATNAME.setValue(tb1.getLAWSTATNAME());
			return true;
		} else {
			txtLAWSTATNAME.setValue("");
			return false;
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
	
	public boolean read_JOBCODE(int jobcode) throws Exception {

		TboTKJOBCODE tb1 = new TboTKJOBCODE();

		tb1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		tb1.setJOBCODE(jobcode);

		if (TbfTKJOBCODE.find(tb1)) {
			txtJOBCODE.setValue(tb1.getJOBCODE() + "");
			txtJOBCODENAME.setValue(tb1.getJOBNAME());
			return true;
		} else {
			txtJOBCODENAME.setValue("");
			return false;
		}

	}

	public boolean read_COURTID(int courtid) throws Exception {

		TboTKCOURT tb1 = new TboTKCOURT();

		tb1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		tb1.setCOURTID(courtid);

		if (TbfTKCOURT.find(tb1)) {
			txtCOURTID.setValue(tb1.getCOURTID() + "");
			txtCOURTNAME.setValue(tb1.getCOURTNAME());
			return true;
		} else {
			txtCOURTNAME.setValue("");
			return false;
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

	public void popupJOBSTATID() {
		if (!txtJOBSTATID.isReadonly()) {
			FfTKJOBSTAT.popup(this.getLoginBean(), this, "read_JOBSTATID");
		}
	}

	public boolean read_JOBSTATID(int jobstatid) throws Exception {

		TboTKJOBSTAT table1 = new TboTKJOBSTAT();

		table1.setCOMP_CDE(getLoginBean().getCOMP_CDE());
		table1.setJOBSTATID(jobstatid);

		if (TbfTKJOBSTAT.find(table1)) {
			txtJOBSTATID.setValue(table1.getJOBSTATID() + "");
			txtJOBSTATNAME.setValue(table1.getJOBSTATNAME());
			return true;
		} else {
			txtJOBSTATNAME.setValue("");
			return false;
		}

	}
	
	public void popupEXPENSESID() {
		if (!txtEXPENSESID.isReadonly()) {
			FfTKEXPENSES.popup(this.getLoginBean(), this, "read_EXPENSESID");
		}	
	}
	
	public boolean read_EXPENSESID(int expensesid) throws Exception {

		boolean ret = false;
		var table1 = new TboTKEXPENSES();
		
		table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		table1.setEXPENSESID(expensesid);
		
		if (TbfTKEXPENSES.find(table1)) {
			txtEXPENSESID.setValue(table1.getEXPENSESID() + "");
			txtEXPENSESNAME.setValue(table1.getEXPENSESNAME());
			ret = true;
		} else {
			txtEXPENSESNAME.setValue("");
		}
		
		return ret;
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
			row.appendChild(ZkUtil.gridIntbox(seq));
			row.appendChild(ZkUtil.gridIntbox(rs.getInt("CLIENTSEQ")));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("CUSTNAME")));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("TELNO")));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("UPDBY")));
			row.appendChild(ZkUtil.gridTextbox(FnDate.displayDateTimeString(rs.getTimestamp("UPDDTE"))));

		};
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

			row.appendChild(ZkUtil.gridIntbox(seq));
			row.appendChild(ZkUtil.gridIntbox(rs.getInt("LAWYERID")));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("LAWYERNAME")));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("UPDBY")));
			row.appendChild(ZkUtil.gridTextbox(FnDate.displayDateTimeString(rs.getTimestamp("UPDDTE"))));

		};
	}

	public void onClick_btnAddJobstat() {

		try {
			if (tdbLOGDATE.getSqlDate() == null) {
				throw new Exception("ต้องระบุ" + tdbLOGDATE.getTooltiptext());
			}
			if (Fnc.isEmpty(txtJOBSTATNAME.getValue())) {
				throw new Exception("ต้องระบุ" + txtJOBSTATID.getTooltiptext());
			}

			try (FDbc dbc = FDbc.connectMasterDb()) {

				TboTKJOBLOG joblog = new TboTKJOBLOG();

				joblog.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				joblog.setJOBNO(txtJOBNO.getValue());

				if (btnUndoJobstat.isVisible() == false) {
					int newseq = TbmTKJOBLOG.maxSeq(dbc, this.getLoginBean().getCOMP_CDE(), txtJOBNO.getValue()) + 1;
					joblog.setSEQ(newseq);
				} else {
					joblog.setSEQ(edit_SEQ_LOG);
					if (!TbfTKJOBLOG.find(dbc, joblog)) {
						throw new Exception("ไม่พบรายการอาจถูกลบโดย user อื่น");
					}
				}

				joblog.setLOGDATE(tdbLOGDATE.getSqlDate());
				joblog.setJOBSTATID(Fnc.getIntFromStr(txtJOBSTATID.getValue()));
				joblog.setJOBSTATREMARK(txtJOBSTATREMARK.getValue());
				joblog.setUPDBY(this.getLoginBean().getUSER_ID());
				joblog.setUPDDTE(FnDate.getTodaySqlDateTime());

				if (btnUndoJobstat.isVisible() == false) {
					TbfTKJOBLOG.insert(dbc, joblog);
				} else {
					TbfTKJOBLOG.update(dbc, joblog);
				}

				showGridJobState();
				clearBoxJobStat();
				tdbLOGDATE.focus();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}
	}

	public void onClick_btnUndoJobstat() {
		try {
			clearBoxJobStat();
			tdbLOGDATE.focus();
		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}
	}
	
	private void clearBoxJobStat() {
		tdbLOGDATE.setValue(null);
		txtJOBSTATID.setValue("");
		txtJOBSTATNAME.setValue("");
		txtJOBSTATREMARK.setValue("");
		
		btnAddJobstat.setLabel(Labels.getLabel("comm.label.add"));
		btnUndoJobstat.setVisible(false);
		disableLogGridButton(false);
		
	}

	private void showGridJobState() throws Exception {
		ManTkJobLog.getDataByJobno(this.getLoginBean().getCOMP_CDE(), txtJOBNO.getValue(), lst_JobStat);
		SimpleListModel rstModel = new SimpleListModel(lst_JobStat);
		this.grdJobState.setModel(rstModel);
	}

	private RowRenderer<?> getRowRendererJobState() {
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
				btn1.addEventListener(Events.ON_CLICK, event -> onClick_EditJoblogRow(event));
				row.appendChild(btn1);
				row.setAttribute("btn_edit", btn1);
			}

			{
				Button btn1 = new Button();
				btn1.setLabel("ลบ");
				btn1.setSclass("buttondel");
				btn1.setAutodisable("self");
				btn1.addEventListener(Events.ON_CLICK, event -> onClick_DelJoblogRow(event));
				row.appendChild(btn1);
				row.setAttribute("btn_del", btn1);
			}

			row.appendChild(ZkUtil.gridIntbox(seq));
			row.appendChild(ZkUtil.gridTextbox(FnDate.displayDateString(rs.getDate("LOGDATE"))));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("JOBSTATNAME_SHOW")));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("JOBSTATREMARK")));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("UPDBY")));
			row.appendChild(ZkUtil.gridTextbox(FnDate.displayDateTimeString(rs.getTimestamp("UPDDTE"))));

		};
	}

	private void onClick_EditJoblogRow(Event event) {
		try {
			Component comp = event.getTarget().getParent();
			var rs = (FModelHasMap) comp.getAttribute("DATA1");

			TboTKJOBLOG joblog = new TboTKJOBLOG();

			joblog.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			joblog.setJOBNO(txtJOBNO.getValue());
			joblog.setSEQ(rs.getInt("SEQ"));

			if (TbfTKJOBLOG.find(joblog)) {

				tdbLOGDATE.setValue(joblog.getLOGDATE());
				txtJOBSTATID.setValue(joblog.getJOBSTATID() + "");
				read_JOBSTATID(joblog.getJOBSTATID());
				txtJOBSTATREMARK.setValue(joblog.getJOBSTATREMARK());

				tdbLOGDATE.focus();
				edit_SEQ_LOG = rs.getInt("SEQ");

				btnAddJobstat.setLabel(Labels.getLabel("comm.label.save"));
				btnUndoJobstat.setVisible(true);
				disableLogGridButton(true);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}
	}

	private void onClick_DelJoblogRow(Event event) {

		Component comp = event.getTarget().getParent();
		var rs = (FModelHasMap) comp.getAttribute("DATA1");

		Msg.confirm(Labels.getLabel("comm.label.deleteComfirm") + " ?", "?", (event1) -> {
			if (Messagebox.Button.YES.equals(event1.getButton())) {

				try {
					TboTKJOBLOG joblog = new TboTKJOBLOG();

					joblog.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
					joblog.setJOBNO(txtJOBNO.getValue());
					joblog.setSEQ(rs.getInt("SEQ"));

					TbfTKJOBLOG.delete(joblog);
					showGridJobState();

				} catch (Exception ex) {
					ex.printStackTrace();
					Msg.error(ex);
				}

			}
		});

	}

	private void disableLogGridButton(boolean disable) {

		java.util.List<Component> lstRow = grdJobState.getRows().getChildren();
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

	public void onClick_btnAddJobExp() {

		try {
			
			if (Fnc.isEmpty(txtEXPENSESNAME.getValue())) {
				throw new Exception("ต้องระบุ" + txtEXPENSESID.getTooltiptext());
			}
			if (decEXPENSESAMT.getValue().compareTo(BigDecimal.ZERO) < 0) {
				throw new Exception("ระบุ" + decEXPENSESAMT.getTooltiptext()+ " ไม่ถูกต้อง" );
			}
			if (decWITHDRAWAL_AMT.getValue().compareTo(BigDecimal.ZERO) < 0) {
				throw new Exception("ระบุ" + decWITHDRAWAL_AMT.getTooltiptext()+ " ไม่ถูกต้อง" );
			}
			if (decEXPCOM_ADV.getValue().compareTo(BigDecimal.ZERO) < 0) {
				throw new Exception("ระบุ" + decEXPCOM_ADV.getTooltiptext()+ " ไม่ถูกต้อง" );
			}
			
			try (var dbc = FDbc.connectMasterDb()) {

				var jobExp = new TboTKJOBEXPENSES();

				jobExp.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				jobExp.setJOBNO(txtJOBNO.getValue());

				if (btnUndoJobExp.isVisible() == false) {
					int newseq = TbmTKJOBEXPENSES.maxSeq(dbc, this.getLoginBean().getCOMP_CDE(), txtJOBNO.getValue()) + 1;
					jobExp.setSEQ(newseq);
				} else {
					jobExp.setSEQ(edit_SEQ_EXP);
					if (!TbfTKJOBEXPENSES.find(dbc, jobExp)) {
						throw new Exception("ไม่พบรายการอาจถูกลบโดย user อื่น");
					}
				}

				jobExp.setEXPENSESID(Fnc.getIntFromStr(txtEXPENSESID.getValue()));
				jobExp.setEXPENSESAMT(decEXPENSESAMT.getValue());
				jobExp.setWITHDRAWAL_AMT(decWITHDRAWAL_AMT.getValue());
				jobExp.setEXPCOM_ADV(decEXPCOM_ADV.getValue());
				jobExp.setEXP_REMARK(txtEXP_REMARK.getValue());
				jobExp.setUPDBY(this.getLoginBean().getUSER_ID());
				jobExp.setUPDDTE(FnDate.getTodaySqlDateTime());

				if (btnUndoJobExp.isVisible() == false) {
					TbfTKJOBEXPENSES.insert(dbc, jobExp);
				} else {
					TbfTKJOBEXPENSES.update(dbc, jobExp);
				}

				showGridJobExp();
				clearBoxJobExp();
				txtEXPENSESID.focus();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}
	}

	public void onClick_btnUndoJobExp() {
		try {
			clearBoxJobExp();
			txtEXPENSESID.focus();
		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}
	}
	
	public void clearBoxJobExp() {
		txtEXPENSESID.setValue("");
		txtEXPENSESNAME.setValue("");
		decEXPENSESAMT.setValue(BigDecimal.ZERO);
		decWITHDRAWAL_AMT.setValue(BigDecimal.ZERO);
		decEXPCOM_ADV.setValue(BigDecimal.ZERO);
		txtEXP_REMARK.setValue("");
		
		btnAddJobExp.setLabel(Labels.getLabel("comm.label.add"));
		btnUndoJobExp.setVisible(false);
		disableExpGridButton(false);

	}

	private void showGridJobExp() throws WrongValueException, Exception {
		ManTkJobLog.getJobExpenses(this.getLoginBean().getCOMP_CDE(), txtJOBNO.getValue(), lst_JobExp);
		SimpleListModel rstModel = new SimpleListModel(lst_JobExp);
		this.grdJobExp.setModel(rstModel);
		//แสดงผลรวม
		BigDecimal[] sumExp = ManTkJobLog.getSUMEXP(this.getLoginBean().getCOMP_CDE(), txtJOBNO.getValue());
		decSUM_EXPENSESAMT.setValue(sumExp[0]);
		decSUM_WITHDRAWAL_AMT.setValue(sumExp[1]);
		decSUM_EXPCOM_ADV.setValue(sumExp[2]);
	}
	
	private RowRenderer<?> getRowRendererJobExp() {
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
				btn1.addEventListener(Events.ON_CLICK, event -> onClick_EditJobExpRow(event));
				row.appendChild(btn1);
				row.setAttribute("btn_edit", btn1);
			}

			{
				Button btn1 = new Button();
				btn1.setLabel("ลบ");
				btn1.setSclass("buttondel");
				btn1.setAutodisable("self");
				btn1.addEventListener(Events.ON_CLICK, event -> onClick_DelJobExpRow(event));
				row.appendChild(btn1);
				row.setAttribute("btn_del", btn1);
			}

			row.appendChild(ZkUtil.gridIntbox(seq));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("EXPENSESNAME_SHOW")));
			row.appendChild(ZkUtil.gridDecimalbox(rs.getBigDecimal("EXPENSESAMT")));
			row.appendChild(ZkUtil.gridDecimalbox(rs.getBigDecimal("WITHDRAWAL_AMT")));
			row.appendChild(ZkUtil.gridDecimalbox(rs.getBigDecimal("EXPCOM_ADV")));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("EXP_REMARK")));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("UPDBY")));
			row.appendChild(ZkUtil.gridTextbox(FnDate.displayDateTimeString(rs.getTimestamp("UPDDTE"))));

		};
	}

	private void onClick_EditJobExpRow(Event event) {
		try {
			Component comp = event.getTarget().getParent();
			var rs = (FModelHasMap) comp.getAttribute("DATA1");
			
			var jobExp = new TboTKJOBEXPENSES();

			jobExp.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			jobExp.setJOBNO(txtJOBNO.getValue());
			jobExp.setSEQ(rs.getInt("SEQ"));

			if (TbfTKJOBEXPENSES.find(jobExp)) {

				txtEXPENSESID.setValue(jobExp.getEXPENSESID() + "");
				read_EXPENSESID(jobExp.getEXPENSESID());
				decEXPENSESAMT.setValue(jobExp.getEXPENSESAMT());
				decWITHDRAWAL_AMT.setValue(jobExp.getWITHDRAWAL_AMT());
				decEXPCOM_ADV.setValue(jobExp.getEXPCOM_ADV());
				txtEXP_REMARK.setValue(jobExp.getEXP_REMARK());

				txtEXPENSESID.focus();
				edit_SEQ_EXP = rs.getInt("SEQ");

				btnAddJobExp.setLabel(Labels.getLabel("comm.label.save"));
				btnUndoJobExp.setVisible(true);
				disableExpGridButton(true);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}
	}

	private void onClick_DelJobExpRow(Event event) {

		Component comp = event.getTarget().getParent();
		var rs = (FModelHasMap) comp.getAttribute("DATA1");

		Msg.confirm(Labels.getLabel("comm.label.deleteComfirm") + " ?", "?", (event1) -> {
			if (Messagebox.Button.YES.equals(event1.getButton())) {

				try {
					var jobExp = new TboTKJOBEXPENSES();

					jobExp.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
					jobExp.setJOBNO(txtJOBNO.getValue());
					jobExp.setSEQ(rs.getInt("SEQ"));

					TbfTKJOBEXPENSES.delete(jobExp);
					showGridJobExp();

				} catch (Exception ex) {
					ex.printStackTrace();
					Msg.error(ex);
				}

			}
		});

	}

	private void disableExpGridButton(boolean disable) {
		java.util.List<Component> lstRow = grdJobExp.getRows().getChildren();
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
	
	public void onchange_decWITHDRAWAL_AMT() {
		BigDecimal amt1 = decWITHDRAWAL_AMT.getValue().add(decEXPCOM_ADV.getValue());
		decEXPENSESAMT.setValue(amt1);
	}
	
	public void onchange_decEXPCOM_ADV() {
		BigDecimal amt1 = decWITHDRAWAL_AMT.getValue().add(decEXPCOM_ADV.getValue());
		decEXPENSESAMT.setValue(amt1);
	}
	
}

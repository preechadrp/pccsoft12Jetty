package com.pcc.tk.ui.FrmTkJob;

import java.math.BigDecimal;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FJasperPrintList;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FReport;
import com.pcc.sys.lib.FWindow;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.MyDatebox;
import com.pcc.sys.lib.MyDecimalbox;
import com.pcc.sys.lib.ZkUtil;
import com.pcc.tk.progman.ManTkJob;
import com.pcc.tk.progman.ManTkJobInq;
import com.pcc.tk.progman.ManTkJobLog;
import com.pcc.tk.tbf.TbfTKCOURT;
import com.pcc.tk.tbf.TbfTKIMGS;
import com.pcc.tk.tbf.TbfTKJOB;
import com.pcc.tk.tbf.TbfTKJOBCODE;
import com.pcc.tk.tbf.TbfTKLAWSTAT;
import com.pcc.tk.tbf.TbfTKLAWTYPE;
import com.pcc.tk.tbf.TbfTKZONE;
import com.pcc.tk.tbm.TbmTKJOBCLIENT;
import com.pcc.tk.tbm.TbmTKJOBDOCS;
import com.pcc.tk.tbo.TboTKCOURT;
import com.pcc.tk.tbo.TboTKIMGS;
import com.pcc.tk.tbo.TboTKJOB;
import com.pcc.tk.tbo.TboTKJOBCODE;
import com.pcc.tk.tbo.TboTKJOBDOCS;
import com.pcc.tk.tbo.TboTKLAWSTAT;
import com.pcc.tk.tbo.TboTKLAWTYPE;
import com.pcc.tk.tbo.TboTKZONE;

public class FrmTkJobView extends FWindow {

	private static final long serialVersionUID = 1L;
	
	public static void showData(LoginBean loginBean, String jobno ,Window winCall, String menuid2 , EventListener<Event> event1) throws Exception {
		
		winCall.setVisible(false);
		String newID = "FrmTkJobView" + menuid2; //ป้องกันซ้ำหลายเมนู
		FrmTkJobView frm1 = (FrmTkJobView) ZkUtil.callZulFile("/com/pcc/tk/zul/FrmTkJob/FrmTkJobView.zul");
		
		//==parameter
		frm1.setLoginBean(loginBean);
		frm1.p_JOBNO = jobno;
		frm1.p_win_call = winCall;
		frm1.p_Event = event1;
		
		//==ค่าอื่นๆ
		frm1.setAppendMode(newID, winCall.getWidth(), winCall.getParent());
	}

	public Button btnExit;
	public Textbox txtJOBNO;
	public MyDatebox tdbJOBDATE;
	public Textbox txtLAWSTATID;
	public Textbox txtLAWSTATNAME;
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
	public MyDatebox tdbCLOSEDATE;
	public Grid grdCust;
	public Column grcCLIENTSEQ;
	public Grid grdLawyer;
	public Grid grdJobState;
	public Grid grdJobExp;
	public MyDecimalbox decSUM_EXPENSESAMT;
	public MyDecimalbox decSUM_WITHDRAWAL_AMT;
	public MyDecimalbox decSUM_EXPCOM_ADV;
	public Label lblJOBSTATNAME;
	public Grid grdImg1;

	private java.util.List<FModelHasMap> lst_Cust = new java.util.ArrayList<FModelHasMap>();
	private java.util.List<FModelHasMap> lst_Lawyer = new java.util.ArrayList<FModelHasMap>();
	private java.util.List<FModelHasMap> lst_JobStat = new java.util.ArrayList<FModelHasMap>();
	private java.util.List<FModelHasMap> lst_JobExp = new java.util.ArrayList<FModelHasMap>();
	private java.util.List<TboTKJOBDOCS> lst_imgs = new java.util.ArrayList<TboTKJOBDOCS>();
	
	public String p_JOBNO;
	public Window p_win_call = null;
	public EventListener<Event> p_Event;

	@Override
	public void setFormObj() {

		btnExit = (Button) getFellow("btnExit");
		txtJOBNO = (Textbox) getFellow("txtJOBNO");
		tdbJOBDATE = (MyDatebox) getFellow("tdbJOBDATE");
		txtLAWSTATID = (Textbox) getFellow("txtLAWSTATID");
		txtLAWSTATNAME = (Textbox) getFellow("txtLAWSTATNAME");
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
		tdbCLOSEDATE = (MyDatebox) getFellow("tdbCLOSEDATE");
		grdCust = (Grid) getFellow("grdCust");
		grcCLIENTSEQ = (Column) getFellow("grcCLIENTSEQ");
		grdLawyer = (Grid) getFellow("grdLawyer");
		grdJobState = (Grid) getFellow("grdJobState");
		grdJobExp = (Grid) getFellow("grdJobExp");
		decSUM_EXPENSESAMT = (MyDecimalbox) getFellow("decSUM_EXPENSESAMT");
		decSUM_WITHDRAWAL_AMT = (MyDecimalbox) getFellow("decSUM_WITHDRAWAL_AMT");
		decSUM_EXPCOM_ADV = (MyDecimalbox) getFellow("decSUM_EXPCOM_ADV");
		lblJOBSTATNAME = (Label) getFellow("lblJOBSTATNAME");
		grdImg1 = (Grid) getFellow("grdImg1");
		
	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(btnExit);
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
			
			ZkUtil.setGridHeaderStyle(grdImg1);
			grdImg1.setRowRenderer(getRowRendererImgs());

			read_record(this.p_JOBNO);

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void onOK() {
		try {
			super.onOK();
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

	public boolean read_record(String jobno) throws Exception {
		boolean res = false;

		TboTKJOB table1 = new TboTKJOB();

		table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		table1.setJOBNO(jobno);

		if (TbfTKJOB.find(table1)) {

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
			tdbCLOSEDATE.setValue(table1.getCLOSEDATE());
			
			String statName = "";
			if (table1.getJOBSTAT().equals("1")) {
				statName = "ปกติ";
			} else if (table1.getJOBSTAT().equals("8")) {
				statName = "ไม่สมบูรณ์";
			} else if (table1.getJOBSTAT().equals("9")) {
				statName = "ยกเลิก";
			}
			lblJOBSTATNAME.setValue(statName);

			// clear input detail
			showGridCust();
			showGridLawyer();
			showGridJobState();
			showGridJobExp();
			showGridImgs();

			// ==visible , readonly

			res = true;
		}

		return res;
	}

	private boolean read_LAWSTATID(int lawstatid) throws Exception {

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

	private boolean read_LAWTYPE(int lawtypeid) throws Exception {

		TboTKLAWTYPE tklawtype = new TboTKLAWTYPE();

		tklawtype.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		tklawtype.setLAWTYPEID(lawtypeid);

		if (TbfTKLAWTYPE.find(tklawtype)) {
			txtLAWTYPEID.setValue(tklawtype.getLAWTYPEID() + "");
			txtLAWTYPENAME.setValue(tklawtype.getLAWTYPENAME());
			return true;
		} else {
			txtLAWTYPENAME.setValue("");
			return false;
		}

	}

	private boolean read_JOBCODE(int jobcode) throws Exception {

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

	private boolean read_COURTID(int courtid) throws Exception {

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
			row.appendChild(ZkUtil.gridIntbox(seq));
			row.appendChild(ZkUtil.gridTextbox(FnDate.displayDateString(rs.getDate("LOGDATE"))));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("JOBSTATNAME_SHOW")));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("JOBSTATREMARK")));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("UPDBY")));
			row.appendChild(ZkUtil.gridTextbox(FnDate.displayDateTimeString(rs.getTimestamp("UPDDTE"))));

		};
	}

	private void showGridJobExp() throws WrongValueException, Exception {
		ManTkJobLog.getJobExpenses(this.getLoginBean().getCOMP_CDE(), txtJOBNO.getValue(), lst_JobExp);
		SimpleListModel rstModel = new SimpleListModel(lst_JobExp);
		this.grdJobExp.setModel(rstModel);
		// แสดงผลรวม
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
	
	public void onClick_btnPrint(int print_option) {

		try {

			FJasperPrintList fJasperPrintList = new FJasperPrintList();
			ManTkJobInq.printData(p_JOBNO, this.getLoginBean(), fJasperPrintList);
			if (fJasperPrintList.getJasperPrintLst().size() > 0) {
				FReport.viewPdf(fJasperPrintList.getJasperPrintLst());
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}
	}
	
	private void showGridImgs() throws WrongValueException, Exception {
		TbmTKJOBDOCS.getDataByJobno(this.getLoginBean().getCOMP_CDE(), p_JOBNO, lst_imgs);
		SimpleListModel rstModel = new SimpleListModel(lst_imgs);
		this.grdImg1.setModel(rstModel);
	}
	
	private RowRenderer<?> getRowRendererImgs() {
		return (row, data, index) -> {

			TboTKJOBDOCS rs = (TboTKJOBDOCS) data;
			int seq = index + 1;
			row.setStyle(ZkUtil.styleFindLookUp);

			row.setAttribute("DATA1", rs);

			// == append child ==//
			{
				Button btn1 = new Button();
				btn1.setLabel("Download");
				btn1.setAutodisable("self");
				btn1.addEventListener(Events.ON_CLICK, event -> onClick_DownloadRow(event));
				row.appendChild(btn1);
				row.setAttribute("btn_load", btn1);
			}

			row.appendChild(ZkUtil.gridIntbox(seq));
			row.appendChild(ZkUtil.gridTextbox(rs.getDOCDESC()));
			row.appendChild(ZkUtil.gridTextbox(rs.getIMGTYPE()));
			row.appendChild(ZkUtil.gridTextbox(rs.getUPDBY()));
			row.appendChild(ZkUtil.gridTextbox(FnDate.displayDateTimeString(rs.getUPDDTE())));

		};
	}

	private void onClick_DownloadRow(Event event) {

		try {

			Component comp = event.getTarget().getParent();
			var rs = (TboTKJOBDOCS) comp.getAttribute("DATA1");

			TboTKIMGS tkimgs = new TboTKIMGS();

			tkimgs.setCOMP_CDE(rs.getCOMP_CDE());
			tkimgs.setSYS_CDE("FrmTkJobDoc");
			tkimgs.setIMGSEQ(rs.getIMGSEQ());

			if (TbfTKIMGS.find(tkimgs)) {
				ZkUtil.saveFile(tkimgs.getIMGDATA(), tkimgs.getIMGTYPE(), "FrmTkJobDoc");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}

	}

}

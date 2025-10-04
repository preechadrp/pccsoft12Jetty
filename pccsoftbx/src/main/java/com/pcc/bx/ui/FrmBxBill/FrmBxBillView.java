package com.pcc.bx.ui.FrmBxBill;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.pcc.bx.progman.ManBxBillEntr;
import com.pcc.bx.tbf.TbfBXHEADER;
import com.pcc.bx.tbf.TbfBXTMPLHEAD;
import com.pcc.bx.tbm.TbmBXDETAIL;
import com.pcc.bx.tbo.TboBXDETAIL;
import com.pcc.bx.tbo.TboBXHEADER;
import com.pcc.bx.tbo.TboBXTMPLHEAD;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FWindow;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.MyDatebox;
import com.pcc.sys.lib.MyDecimalbox;
import com.pcc.sys.lib.ZkUtil;
import com.pcc.sys.tbf.TbfFCOMPBRANC;
import com.pcc.sys.tbo.TboFCOMPBRANC;

public class FrmBxBillView extends FWindow {

	private static final long serialVersionUID = 1L;
	
	public static void showData(LoginBean loginBean, String blno,Window winCall, String menuid2 , EventListener<Event> event1) throws Exception {
		
		winCall.setVisible(false);
		String newID = "FrmBxBillView" + menuid2; //ป้องกันซ้ำหลายเมนู
		FrmBxBillView frm1 = (FrmBxBillView) ZkUtil.callZulFile("/com/pcc/bx/zul/FrmBxBill/FrmBxBillView.zul");
		
		//==parameter
		frm1.setLoginBean(loginBean);
		frm1.p_win_call = winCall;
		frm1.p_BLNO = blno;
		frm1.p_Event = event1;
		
		//==ค่าอื่นๆ
		frm1.setAppendMode(newID, winCall.getWidth(), winCall.getParent());
		
	}

	public Button btnExit;
	public Button btnPrint;
	public Textbox txtTMPLCDE;
	public Textbox txtTMPLNAME;
	public Textbox txtDOCPREFIX;
	public MyDatebox tdbBLDATE;
	public Textbox txtBLNO;
	public Combobox cmbVATTYPE;
	public MyDecimalbox decVATRATE;
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
	public Grid grdData1;
	public MyDecimalbox decBASAMT;
	public MyDecimalbox decDISCOUNTAMT;
	public MyDecimalbox decNETAMT;
	public MyDecimalbox decVATAMT;
	public MyDecimalbox decTOTALAMT;
	public Textbox txtSTATUS;

	private java.util.List<TboBXDETAIL> lst_bxdetail = new java.util.ArrayList<TboBXDETAIL>();

	public String p_BLNO = "";
	public Window p_win_call = null;
	public EventListener<Event> p_Event;

	@Override
	public void setFormObj() {
		btnExit = (Button) getFellow("btnExit");
		btnPrint = (Button) getFellow("btnPrint");
		txtTMPLCDE = (Textbox) getFellow("txtTMPLCDE");
		txtTMPLNAME = (Textbox) getFellow("txtTMPLNAME");
		txtDOCPREFIX = (Textbox) getFellow("txtDOCPREFIX");
		tdbBLDATE = (MyDatebox) getFellow("tdbBLDATE");
		txtBLNO = (Textbox) getFellow("txtBLNO");
		cmbVATTYPE = (Combobox) getFellow("cmbVATTYPE");
		decVATRATE = (MyDecimalbox) getFellow("decVATRATE");
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
		grdData1 = (Grid) getFellow("grdData1");
		decBASAMT = (MyDecimalbox) getFellow("decBASAMT");
		decDISCOUNTAMT = (MyDecimalbox) getFellow("decDISCOUNTAMT");
		decNETAMT = (MyDecimalbox) getFellow("decNETAMT");
		decVATAMT = (MyDecimalbox) getFellow("decVATAMT");
		decTOTALAMT = (MyDecimalbox) getFellow("decTOTALAMT");
		txtSTATUS = (Textbox) getFellow("txtSTATUS");

	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(btnExit);
	}

	@Override
	public void formInit() {
		try {
			ZkUtil.setGridHeaderStyle(grdData1);
			grdData1.setRowRenderer(getRowRendererData1());

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
			super.onOK();
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

			decBASAMT.setValue(head.getBASAMT());
			decDISCOUNTAMT.setValue(Fnc.getBigDecimalValue(head.getDISCOUNTAMT()));
			decNETAMT.setValue(head.getBASAMT().subtract(Fnc.getBigDecimalValue(head.getDISCOUNTAMT())));
			decVATAMT.setValue(head.getVATAMT());
			decTOTALAMT.setValue(head.getTOTALAMT());

			String statName = "";
			if (head.getRECSTA() == 9) {
				statName = "ยกเลิก";
			} else if (head.getRECSTA() == 1) {
				statName = "สมบูรณ์";
			} else if (head.getRECSTA() == 0) {
				statName = "ไม่สมบูรณ์";
			}
			txtSTATUS.setValue(statName);

			showGrid1();

			// === set othoer
			div1.setVisible(true);
			txtTMPLCDE.setReadonly(true);
			tdbBLDATE.setReadonly(true);
			cmbVATTYPE.setDisabled(true);
			decVATRATE.setReadonly(true);

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

			row.appendChild(ZkUtil.gridIntbox(seq));
			row.appendChild(ZkUtil.gridTextbox(rs.getPRODUCTID()));
			row.appendChild(ZkUtil.gridTextbox(rs.getPRODUCTNAME()));
			row.appendChild(ZkUtil.gridTextbox(rs.getREMARKLINE()));
			row.appendChild(ZkUtil.gridDecimalbox(rs.getQTY()));
			row.appendChild(ZkUtil.gridDecimalbox(rs.getPRICE()));
			row.appendChild(ZkUtil.gridDecimalbox(rs.getSUMPRICE()));

		};
	}

	public void onClick_Print() {

		boolean[] clearWhenError = { false };

		try {
			ManBxBillEntr.printReport(this.getLoginBean().getCOMP_CDE(), txtBLNO.getValue());
		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
			if (clearWhenError[0]) {
				this.onClose();
			}
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
	
	public void read_BRANC_CDE(int branc_cde) throws Exception {

		TboFCOMPBRANC bn = new TboFCOMPBRANC();
		
		bn.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		bn.setBRANC_CDE(branc_cde);
		
		if (TbfFCOMPBRANC.find(bn)) {
			txtBRANC_CDE.setValue(bn.getBRANC_CDE()+"");
			txtBRANC_NAME.setValue(bn.getBRANC_NAME());
		} else {
			txtBRANC_CDE.setValue(branc_cde+"");
			txtBRANC_NAME.setValue("");
		}
		
	}

}

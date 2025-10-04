package com.pcc.bx.ui.FrmBxTempl;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.pcc.bx.tbf.TbfBXTMPLHEAD;
import com.pcc.bx.tbm.TbmBXTMPLDETAIL;
import com.pcc.bx.tbo.TboBXTMPLDETAIL;
import com.pcc.bx.tbo.TboBXTMPLHEAD;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FWindow;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.ZkUtil;

public class FrmBxTemplView extends FWindow {

	private static final long serialVersionUID = 1L;
	
	public static void showData(LoginBean loginBean, int tmplcde, Window winCall, String menuid2) throws Exception {

		winCall.setVisible(false);
		String newID = "FrmBxTemplView" + menuid2; // ป้องกันซ้ำหลายเมนู
		FrmBxTemplView frm1 = (FrmBxTemplView) ZkUtil.callZulFile("/com/pcc/bx/zul/FrmBxTempl/FrmBxTemplView.zul");

		// ==parameter
		frm1.setLoginBean(loginBean);
		frm1.p_TMPLCDE = tmplcde;
		frm1.p_win_call = winCall;

		// ==ค่าอื่นๆ
		frm1.setId(newID);
		frm1.setTitle("");
		frm1.setBorder("none");
		frm1.setWidth(winCall.getWidth());
		frm1.setParent(winCall.getParent());//ใช้คำสั่งนี้แล้วไม่ต้องใช้คำสั่ง frm1.doEmbedded();
		
	}

	public Button btnExit;
	public Intbox intTMPLCDE;
	public Textbox txtDOCPREFIX;
	public Div div1;
	public Textbox txtTMPLNAME;
	public Combobox cmbPOSTCOST;
	public Grid grdData1;

	private java.util.List<TboBXTMPLDETAIL> lst_bxtmpldetail = new java.util.ArrayList<TboBXTMPLDETAIL>();

	public int p_TMPLCDE = 0;
	public Window p_win_call = null;

	@Override
	public void setFormObj() {
		btnExit = (Button) getFellow("btnExit");
		intTMPLCDE = (Intbox) getFellow("intTMPLCDE");
		txtDOCPREFIX = (Textbox) getFellow("txtDOCPREFIX");
		div1 = (Div) getFellow("div1");
		txtTMPLNAME = (Textbox) getFellow("txtTMPLNAME");
		cmbPOSTCOST = (Combobox) getFellow("cmbPOSTCOST");
		grdData1 = (Grid) getFellow("grdData1");

	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(btnExit);
		addEnterIndex(intTMPLCDE);
		//addEnterIndex(txtDOCPREFIX);
		//addEnterIndex(txtTMPLNAME);
	}

	@Override
	public void formInit() {
		try {
			ZkUtil.setGridHeaderStyle(grdData1);
			grdData1.setRowRenderer(getRowRendererData1());

			if (this.p_TMPLCDE != 0) {
				read_record(this.p_TMPLCDE);
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

	private void read_record(int tmplcde) throws WrongValueException, Exception {

		TboBXTMPLHEAD head = new TboBXTMPLHEAD();

		head.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		head.setTMPLCDE(tmplcde);

		if (TbfBXTMPLHEAD.find(head)) {

			intTMPLCDE.setValue(head.getTMPLCDE());
			txtDOCPREFIX.setValue(head.getDOCPREFIX());
			txtTMPLNAME.setValue(head.getTMPLNAME());
			ZkUtil.setSelectItemComboBoxByValue(cmbPOSTCOST, head.getPOSTCOST());

			showGrid1();
		}

	}

	private void showGrid1() throws WrongValueException, Exception {
		TbmBXTMPLDETAIL.getData(this.getLoginBean().getCOMP_CDE(), intTMPLCDE.getValue(), lst_bxtmpldetail);
		SimpleListModel rstModel = new SimpleListModel(lst_bxtmpldetail);
		this.grdData1.setModel(rstModel);
	}

	private RowRenderer<?> getRowRendererData1() {

		return (row, data, index) -> {

			TboBXTMPLDETAIL rs = (TboBXTMPLDETAIL) data;
			int seq = index + 1;
			row.setStyle(ZkUtil.styleFindLookUp);

			row.appendChild(ZkUtil.gridIntbox(seq));
			row.appendChild(ZkUtil.gridTextbox(rs.getDOCHEAD()));
			row.appendChild(ZkUtil.gridTextbox(rs.getCOPYFOR()));

		};
	}

}

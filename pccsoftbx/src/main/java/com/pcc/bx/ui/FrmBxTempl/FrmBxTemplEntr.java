package com.pcc.bx.ui.FrmBxTempl;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.pcc.bx.tbf.TbfBXTMPLDETAIL;
import com.pcc.bx.tbf.TbfBXTMPLHEAD;
import com.pcc.bx.tbm.TbmBXTMPLDETAIL;
import com.pcc.bx.tbm.TbmBXTMPLHEAD;
import com.pcc.bx.tbo.TboBXTMPLDETAIL;
import com.pcc.bx.tbo.TboBXTMPLHEAD;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FWindow;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.ZkUtil;

public class FrmBxTemplEntr extends FWindow {

	private static final long serialVersionUID = 1L;
	
	public static void showData(LoginBean loginBean, int tmplcde, Window winCall, String menuid2, EventListener<Event> event1) throws Exception {

		winCall.setVisible(false);
		String newID = "FrmBxTemplEntr" + menuid2; // ป้องกันซ้ำหลายเมนู
		FrmBxTemplEntr frm1 = (FrmBxTemplEntr) ZkUtil.callZulFile("/com/pcc/bx/zul/FrmBxTempl/FrmBxTemplEntr.zul");

		// ==parameter
		frm1.setLoginBean(loginBean);
		frm1.p_TMPLCDE = tmplcde;
		frm1.p_Event = event1;
		frm1.p_win_call = winCall;

		// ==ค่าอื่นๆ
		frm1.setId(newID);
		frm1.setTitle("");
		frm1.setBorder("none");
		frm1.setWidth(winCall.getWidth());
		frm1.setParent(winCall.getParent());//ใช้คำสั่งนี้แล้วไม่ต้องใช้คำสั่ง frm1.doEmbedded();
	}

	public Button btnExit;
	public Button btnSave;
	public Button btnDelete;
	public Intbox intTMPLCDE;
	public Textbox txtDOCPREFIX;
	public Button btnAdd;
	public Div div1;
	public Textbox txtTMPLNAME;
	public Combobox cmbPOSTCOST;
	public Textbox txtDOCHEAD;
	public Textbox txtCOPYFOR;
	public Button btnAdd1;
	public Button btnUndo1;
	public Grid grdData1;

	private java.util.List<TboBXTMPLDETAIL> lst_bxtmpldetail = new java.util.ArrayList<TboBXTMPLDETAIL>();
	private int edit_SEQ = 0;

	public int p_TMPLCDE = 0;
	public Window p_win_call = null;
	public EventListener<Event> p_Event;

	@Override
	public void setFormObj() {
		btnExit = (Button) getFellow("btnExit");
		btnSave = (Button) getFellow("btnSave");
		btnDelete = (Button) getFellow("btnDelete");
		intTMPLCDE = (Intbox) getFellow("intTMPLCDE");
		txtDOCPREFIX = (Textbox) getFellow("txtDOCPREFIX");
		btnAdd = (Button) getFellow("btnAdd");
		div1 = (Div) getFellow("div1");
		txtTMPLNAME = (Textbox) getFellow("txtTMPLNAME");
		cmbPOSTCOST = (Combobox) getFellow("cmbPOSTCOST");
		txtDOCHEAD = (Textbox) getFellow("txtDOCHEAD");
		txtCOPYFOR = (Textbox) getFellow("txtCOPYFOR");
		btnAdd1 = (Button) getFellow("btnAdd1");
		btnUndo1 = (Button) getFellow("btnUndo1");
		grdData1 = (Grid) getFellow("grdData1");

	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(btnExit);
		addEnterIndex(btnSave);
		addEnterIndex(btnDelete);
		addEnterIndex(intTMPLCDE);
		addEnterIndex(txtDOCPREFIX);
		addEnterIndex(btnAdd);
		addEnterIndex(txtTMPLNAME);
		addEnterIndex(cmbPOSTCOST);
		addEnterIndex(txtDOCHEAD);
		addEnterIndex(txtCOPYFOR);
		addEnterIndex(btnAdd1);
		addEnterIndex(btnUndo1);

	}

	@Override
	public void formInit() {
		try {
			ZkUtil.setGridHeaderStyle(grdData1);
			grdData1.setRowRenderer(getRowRendererData1());

			clearData();

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
	
	public void onClick_btnAdd() {
		try {
			if (!Fnc.isEmpty(txtDOCPREFIX.getValue())) {
				new_record();
			} else {
				throw new Exception("ต้องระบุ " + txtDOCPREFIX.getTooltiptext());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e.getMessage());
		}
	}

	private void new_record() throws WrongValueException, Exception {

		if (txtDOCPREFIX.getValue().length() != 2) {
			throw new Exception("ต้องระบุ " + txtDOCPREFIX.getTooltiptext() + " 2 ตัวอักษร");
		}

		Fnc.isPassCode(txtDOCPREFIX.getValue());

		int tmplcde = TbmBXTMPLHEAD.getMaxCode(this.getLoginBean().getCOMP_CDE()) + 1;

		TboBXTMPLHEAD head = new TboBXTMPLHEAD();

		head.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		head.setTMPLCDE(tmplcde);
		head.setDOCPREFIX(txtDOCPREFIX.getValue());
		head.setTMPLNAME("?");
		head.setPOSTCOST("N");
		head.setUPBY(this.getLoginBean().getUSER_ID());
		head.setUPDT(FnDate.getTodaySqlDateTime());

		if (TbfBXTMPLHEAD.insert(head)) {
			read_record(tmplcde);
		}

	}

	private void clearData() {

		div1.setVisible(false);
		txtDOCPREFIX.setReadonly(false);
		txtDOCPREFIX.setValue("");
		txtDOCPREFIX.focus();
		cmbPOSTCOST.setSelectedIndex(0);

		btnExit.setVisible(true);
		btnSave.setVisible(false);
		btnDelete.setVisible(false);
		btnAdd.setVisible(true);
		btnAdd1.setVisible(true);
		btnUndo1.setVisible(false);
		grdData1.getRows().getChildren().clear();

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

			div1.setVisible(true);
			txtDOCPREFIX.setReadonly(true);
			txtTMPLNAME.focus();

			btnExit.setVisible(false);
			btnSave.setVisible(true);
			btnDelete.setVisible(true);
			btnAdd.setVisible(false);

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
			row.appendChild(ZkUtil.gridTextbox(rs.getDOCHEAD()));
			row.appendChild(ZkUtil.gridTextbox(rs.getCOPYFOR()));

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
			var rs = (TboBXTMPLDETAIL) comp.getAttribute("DATA1");

			TboBXTMPLDETAIL tmplDet = new TboBXTMPLDETAIL();

			tmplDet.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			tmplDet.setTMPLCDE(rs.getTMPLCDE());
			tmplDet.setTMPLSEQ(rs.getTMPLSEQ());

			if (TbfBXTMPLDETAIL.find(tmplDet)) {
				txtDOCHEAD.setValue(tmplDet.getDOCHEAD());
				txtCOPYFOR.setValue(tmplDet.getCOPYFOR());

				txtDOCHEAD.focus();
				edit_SEQ = rs.getTMPLSEQ();

				btnAdd1.setLabel(Labels.getLabel("comm.label.save"));
				btnUndo1.setVisible(true);
				disableGridButtton(true);
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
		var rs = (TboBXTMPLDETAIL) comp.getAttribute("DATA1");

		Msg.confirm(Labels.getLabel("comm.label.deleteComfirm") + " ?", "?", (event1) -> {
			if (Messagebox.Button.YES.equals(event1.getButton())) {

				try {

					TboBXTMPLDETAIL tmplDet = new TboBXTMPLDETAIL();

					tmplDet.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
					tmplDet.setTMPLCDE(rs.getTMPLCDE());
					tmplDet.setTMPLSEQ(rs.getTMPLSEQ());

					TbfBXTMPLDETAIL.delete(tmplDet);

					showGrid1();

				} catch (Exception ex) {
					ex.printStackTrace();
					Msg.error(ex);
				}

			}
		});
	}

	public void onClick_btnAdd1() {

		try {

			if (Fnc.isEmpty(txtDOCHEAD.getValue())) {
				throw new Exception("ต้องระบุ " + txtDOCHEAD.getTooltiptext() + "");
			}
			if (Fnc.isEmpty(txtCOPYFOR.getValue())) {
				throw new Exception("ต้องระบุ " + txtCOPYFOR.getTooltiptext() + "");
			}

			//== update header
			TboBXTMPLHEAD tmpH = new TboBXTMPLHEAD();
			
			tmpH.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			tmpH.setTMPLCDE(intTMPLCDE.getValue());
			
			if (TbfBXTMPLHEAD.find(tmpH)) {
				tmpH.setTMPLNAME(txtTMPLNAME.getValue());
				tmpH.setUPBY(this.getLoginBean().getUSER_ID());
				tmpH.setPOSTCOST(ZkUtil.getSelectItemValueComboBox(cmbPOSTCOST));
				tmpH.setUPDT(FnDate.getTodaySqlDateTime());

				TbfBXTMPLHEAD.update(tmpH);
			}
			
			//==== save detail
			TboBXTMPLDETAIL tmplDet = new TboBXTMPLDETAIL();

			tmplDet.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			tmplDet.setTMPLCDE(intTMPLCDE.getValue());

			if (!btnUndo1.isVisible()) { // เพิ่ม
				int new_TMPLSEQ = TbmBXTMPLDETAIL.getMaxCode(this.getLoginBean().getCOMP_CDE(), intTMPLCDE.getValue()) + 1;
				tmplDet.setTMPLSEQ(new_TMPLSEQ);
			} else {
				tmplDet.setTMPLSEQ(edit_SEQ);
			}

			tmplDet.setDOCHEAD(txtDOCHEAD.getValue());
			tmplDet.setCOPYFOR(txtCOPYFOR.getValue());
			tmplDet.setUPBY(this.getLoginBean().getUSER_ID());
			tmplDet.setUPDT(FnDate.getTodaySqlDateTime());

			if (!btnUndo1.isVisible()) { 
				TbfBXTMPLDETAIL.insert(tmplDet);
			} else {
				TbfBXTMPLDETAIL.update(tmplDet);
			}

			txtDOCHEAD.setValue("");
			txtCOPYFOR.setValue("");
			txtDOCHEAD.focus();
			showGrid1();
			btnAdd1.setLabel(Labels.getLabel("comm.label.save"));
			btnUndo1.setVisible(false);
			edit_SEQ = 0;

		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}

	}

	public void onClick_btnUndo1() {

		try {

			txtDOCHEAD.setValue("");
			txtCOPYFOR.setValue("");
			txtDOCHEAD.focus();
			btnAdd1.setLabel(Labels.getLabel("comm.label.save"));
			btnUndo1.setVisible(false);
			disableGridButtton(false);

		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}

	}

	public void onClick_Save() {
		try {
			
			if (Fnc.isEmpty(txtTMPLNAME.getValue())) {
				throw new Exception("ต้องระบุ " + txtTMPLNAME.getTooltiptext() + "");
			}
			if (Fnc.isEmpty(ZkUtil.getSelectItemValueComboBox(cmbPOSTCOST))) {
				throw new Exception("ต้องระบุ " + cmbPOSTCOST.getTooltiptext() + "");
			}
			
			//== update header
			TboBXTMPLHEAD tmpH = new TboBXTMPLHEAD();
			
			tmpH.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			tmpH.setTMPLCDE(intTMPLCDE.getValue());
			
			if (TbfBXTMPLHEAD.find(tmpH)) {
				tmpH.setTMPLNAME(txtTMPLNAME.getValue());
				tmpH.setPOSTCOST(ZkUtil.getSelectItemValueComboBox(cmbPOSTCOST));
				tmpH.setUPBY(this.getLoginBean().getUSER_ID());
				tmpH.setUPDT(FnDate.getTodaySqlDateTime());

				TbfBXTMPLHEAD.update(tmpH);
				
				if (p_Event != null) {
					p_Event.onEvent(new Event(Events.ON_CLICK, null, -1));
				}
			}
			
			this.onClose();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}
	}

	public void onClick_Delete() {
		try {

			Msg.confirm(Labels.getLabel("comm.label.deleteComfirm") + " ?", "?", (event1) -> {
				if (Messagebox.Button.YES.equals(event1.getButton())) {

					try (FDbc dbc = FDbc.connectMasterDb()) {
						dbc.beginTrans();
						
						//ลบ header
						TboBXTMPLHEAD tmpH = new TboBXTMPLHEAD();
						
						tmpH.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
						tmpH.setTMPLCDE(intTMPLCDE.getValue());
						
						if (TbfBXTMPLHEAD.find(dbc, tmpH)) {
							TbfBXTMPLHEAD.delete(dbc,tmpH);
						}

						//ลบ detail
						TbmBXTMPLDETAIL.getDeleteAll(dbc, tmpH.getCOMP_CDE(), tmpH.getTMPLCDE());
						
						dbc.commit();
						
						if (p_Event != null) {
							p_Event.onEvent(new Event(Events.ON_CLICK, null, -1));
						}

						onClose();
						
					} catch (Exception ex) {
						ex.printStackTrace();
						Msg.error(ex);
					}

				}
			});
			
		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}
	}

}

package com.pcc.bx.ui.FrmBxTempl;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;

import com.pcc.bx.tbf.TbfBXTMPLHEAD;
import com.pcc.bx.tbm.TbmBXTMPLHEAD;
import com.pcc.bx.tbo.TboBXTMPLHEAD;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.ZkUtil;

public class FrmBxTempl extends FWinMenu {

	private static final long serialVersionUID = 571025269901822481L;
	public Button btnExit;
	public Intbox intTMPLCDE;
	public Textbox txtTMPLNAME;
	public Textbox txtDOCPREFIX;
	public Button btnShow;
	public Button btnAdd;
	public Grid grdViewt;

	private java.util.List<TboBXTMPLHEAD> lst_data = new java.util.ArrayList<TboBXTMPLHEAD>();
	private int sel_TMPLCDE = 0;

	@Override
	public void setFormObj() {
		btnExit = (Button) getFellow("btnExit");
		intTMPLCDE = (Intbox) getFellow("intTMPLCDE");
		txtTMPLNAME = (Textbox) getFellow("txtTMPLNAME");
		txtDOCPREFIX = (Textbox) getFellow("txtDOCPREFIX");
		btnShow = (Button) getFellow("btnShow");
		btnAdd = (Button) getFellow("btnAdd");
		grdViewt = (Grid) getFellow("grdViewt");

	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(btnExit);
		addEnterIndex(intTMPLCDE);
		addEnterIndex(txtTMPLNAME);
		addEnterIndex(txtDOCPREFIX);
		addEnterIndex(btnShow);
		addEnterIndex(btnAdd);

	}

	@Override
	public void formInit() {
		try {
			ZkUtil.setGridHeaderStyle(grdViewt);
			grdViewt.setRowRenderer(getRowRendererView1());
			clearData();
			showGridViewt();
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

	private void clearData() {
		grdViewt.getRows().getChildren().clear();
	}

	public void onClick_btnAdd() {
		try {
			FrmBxTemplEntr.showData(this.getLoginBean(), 0, this, this.getMenuId2(), (event) -> {
				onClick_btnShow();
			});
		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}
	}

	public void onClick_btnShow() {
		try {
			showGridViewt();
		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}

	}

	private void showGridViewt() throws WrongValueException, Exception {

		FModelHasMap para = new FModelHasMap();

		para.setInt("TMPLCDE", Fnc.getIntValue(intTMPLCDE.getValue()));
		para.setString("TMPLNAME", txtTMPLNAME.getValue());
		para.setString("DOCPREFIX", txtDOCPREFIX.getValue());

		TbmBXTMPLHEAD.getData(this.getLoginBean().getCOMP_CDE(), para, lst_data);
		SimpleListModel rstModel = new SimpleListModel(lst_data);
		this.grdViewt.setModel(rstModel);

	}

	private RowRenderer<?> getRowRendererView1() {

		return (row, data, index) -> {

			TboBXTMPLHEAD rs = (TboBXTMPLHEAD) data;
			int seq = index + 1;
			row.setStyle(ZkUtil.styleFindLookUp);

			row.setAttribute("DATA1", rs);

			// == append child ==//
			{
				Button btn1 = new Button();
				btn1.setLabel("ดูรายละเอียด");
				btn1.setAutodisable("self");
				btn1.addEventListener(Events.ON_CLICK, event -> onClick_ViewRow(event));
				row.appendChild(btn1);
				row.setAttribute("btn_view", btn1);
			}

			if (this.getMenuId2().equals("FrmBxTempl")) {
				Button btn1 = new Button();
				btn1.setLabel("แก้ไข");
				btn1.setAutodisable("self");
				btn1.addEventListener(Events.ON_CLICK, event -> onClick_EditJobRow(event));
				row.appendChild(btn1);
				row.setAttribute("btn_edit", btn1);

			} else {
				row.appendChild(ZkUtil.gridLabel("-"));
			}

			row.appendChild(ZkUtil.gridIntbox(seq));
			row.appendChild(ZkUtil.gridLabel(rs.getTMPLCDE() + ""));
			row.appendChild(ZkUtil.gridLabel(rs.getTMPLNAME()));
			row.appendChild(ZkUtil.gridLabel(rs.getDOCPREFIX()));
			row.appendChild(ZkUtil.gridLabel(Fnc.getStr(rs.getPOSTCOST()).equals("Y")?"Yes":"No"));
			row.appendChild(ZkUtil.gridLabel(rs.getUPBY()));
			row.appendChild(ZkUtil.gridLabel(FnDate.displayDateTimeString(rs.getUPDT())));

		};
	}

	private void onClick_ViewRow(Event event) {
		try {
			Component comp = event.getTarget().getParent();
			var rs = (TboBXTMPLHEAD) comp.getAttribute("DATA1");
			sel_TMPLCDE = rs.getTMPLCDE();

			TboBXTMPLHEAD tmpH = new TboBXTMPLHEAD();

			tmpH.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			tmpH.setTMPLCDE(sel_TMPLCDE);

			if (!TbfBXTMPLHEAD.find(tmpH)) {
				throw new Exception("ไม่พบรายการ");
			}

			FrmBxTemplView.showData(this.getLoginBean(), sel_TMPLCDE,  this, this.getMenuId2());

		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}
	}

	private void onClick_EditJobRow(Event event) {

		try {

			Component comp = event.getTarget().getParent();
			var rs = (TboBXTMPLHEAD) comp.getAttribute("DATA1");
			sel_TMPLCDE = rs.getTMPLCDE();
			
			TboBXTMPLHEAD tmpH = new TboBXTMPLHEAD();

			tmpH.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			tmpH.setTMPLCDE(sel_TMPLCDE);

			if (!TbfBXTMPLHEAD.find(tmpH)) {
				throw new Exception("ไม่พบรายการ");
			}

			Msg.confirm("ยืนยันแก้ไข?", "?", (event1) -> {
				if (Messagebox.Button.YES.equals(event1.getButton())) {
					try {
						FrmBxTemplEntr.showData(this.getLoginBean(), tmpH.getTMPLCDE(), this, this.getMenuId2(), (event2) -> {
							onClick_btnShow();
						});
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

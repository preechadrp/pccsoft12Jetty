package com.pcc.bx.ui.FrmBxBill;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;

import com.pcc.bx.tbf.TbfBXHEADER;
import com.pcc.bx.tbm.TbmBXHEADER;
import com.pcc.bx.tbo.TboBXHEADER;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.MyDatebox;
import com.pcc.sys.lib.ZkUtil;

public class FrmBxBill extends FWinMenu {

	private static final long serialVersionUID = 571025269901822481L;

	public Button btnExit;
	public Button btnAdd;
	public Textbox txtBLNO;
	public MyDatebox tdbBLDATEFROM;
	public MyDatebox tdbBLDATETO;
	public Textbox txtCUST_FNAME;
	public Textbox txtCUST_LNAME;
	public Button btnShow;
	public Grid grdViewt;
	private Column colDel;

	private java.util.List<TboBXHEADER> lst_data = new java.util.ArrayList<TboBXHEADER>();
	private String sel_BLNO = "";

	@Override
	public void setFormObj() {
		btnExit = (Button) getFellow("btnExit");
		btnAdd = (Button) getFellow("btnAdd");
		txtBLNO = (Textbox) getFellow("txtBLNO");
		tdbBLDATEFROM = (MyDatebox) getFellow("tdbBLDATEFROM");
		tdbBLDATETO = (MyDatebox) getFellow("tdbBLDATETO");
		txtCUST_FNAME = (Textbox) getFellow("txtCUST_FNAME");
		txtCUST_LNAME = (Textbox) getFellow("txtCUST_LNAME");
		btnShow = (Button) getFellow("btnShow");
		grdViewt = (Grid) getFellow("grdViewt");
		colDel = (Column) getFellow("colDel");

	}

	@Override
	public void addEnterIndex() {
		// addEnterIndex(btnExit);
		// addEnterIndex(btnAdd);
		addEnterIndex(txtBLNO);
		addEnterIndex(tdbBLDATEFROM);
		addEnterIndex(tdbBLDATETO);
		addEnterIndex(txtCUST_FNAME);
		addEnterIndex(txtCUST_LNAME);
		addEnterIndex(btnShow);

	}

	@Override
	public void formInit() {
		try {
			ZkUtil.setGridHeaderStyle(grdViewt);
			grdViewt.setRowRenderer(getRowRendererView1());

			clearData();
			showGridViewt();

			if (this.getMenuId2().equals("FrmBxBill")) {
				btnAdd.setVisible(true);
			} else {
				btnAdd.setVisible(false);
			}

			if (this.getMenuId2().equals("FrmBxBillInq")) {
				colDel.setVisible(false);
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

	private void clearData() {
		grdViewt.getRows().getChildren().clear();
	}

	public void onClick_btnAdd() {
		try {
			FrmBxBillEntr.showData(this.getLoginBean(), "", true, this, this.getMenuId2(), (event2) -> showGridViewt());
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

		para.setString("BLNO", txtBLNO.getValue());
		para.setDate("BLDATEFROM", tdbBLDATEFROM.getSqlDate());
		para.setDate("BLDATETO", tdbBLDATETO.getSqlDate());
		para.setString("CUST_FNAME", txtCUST_FNAME.getValue());
		para.setString("CUST_LNAME", txtCUST_LNAME.getValue());
		para.setString("MENUID2", this.getMenuId2());

		TbmBXHEADER.getData(this.getLoginBean().getCOMP_CDE(), para, lst_data);
		SimpleListModel rstModel = new SimpleListModel(lst_data);
		this.grdViewt.setModel(rstModel);

	}

	private RowRenderer<?> getRowRendererView1() {

		return (row, data, index) -> {

			TboBXHEADER rs = (TboBXHEADER) data;
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

			if (this.getMenuId2().equals("FrmBxBill")) {
				Button btn1 = new Button();
				btn1.setLabel("แก้ไข");
				btn1.setAutodisable("self");
				btn1.addEventListener(Events.ON_CLICK, event -> onClick_EditDataRow(event));
				row.appendChild(btn1);
				row.setAttribute("btn_edit", btn1);

			} else if (this.getMenuId2().equals("FrmBxCostAndAp")) {
				Button btn1 = new Button();
				btn1.setLabel("บันทึกต้นทุน");
				btn1.setAutodisable("self");
				btn1.addEventListener(Events.ON_CLICK, event -> onClick_CostDataRow(event));
				row.appendChild(btn1);
				row.setAttribute("btn_cost", btn1);

			} else if (this.getMenuId2().equals("FrmBxBillDel")) {
				Button btn1 = new Button();
				btn1.setLabel("ยกเลิก");
				btn1.setAutodisable("self");
				btn1.setSclass("buttondel");
				btn1.addEventListener(Events.ON_CLICK, event -> onClick_CancelDataRow(event));
				row.appendChild(btn1);
				row.setAttribute("btn_cencel", btn1);

			} else {
				row.appendChild(ZkUtil.gridLabel("-"));
			}

			if (rs.getRECSTA() == 9) {
				row.appendChild(ZkUtil.gridLabel("ยกเลิก","color:red;"));
			} else if (rs.getRECSTA() == 0) {
				row.appendChild(ZkUtil.gridLabel("ไม่สมบูรณ์","color:#ff6600;"));
			} else {
				row.appendChild(ZkUtil.gridLabel("ปกติ","color:green;"));
			}
			row.appendChild(ZkUtil.gridIntbox(seq));
			row.appendChild(ZkUtil.gridLabel(rs.getBLNO()));
			row.appendChild(ZkUtil.gridLabel(FnDate.displayDateString(rs.getBLDATE())));

			String custName = Fnc.getStr(rs.getCUST_TITLE()) + " " +
					Fnc.getStr(rs.getCUST_FNAME()) + " " +
					Fnc.getStr(rs.getCUST_LNAME());
			row.appendChild(ZkUtil.gridLabel(custName.trim()));
			row.appendChild(ZkUtil.gridDecimalbox(rs.getBASAMT()));
			row.appendChild(ZkUtil.gridDecimalbox(rs.getVATAMT()));
			row.appendChild(ZkUtil.gridDecimalbox(rs.getTOTALAMT()));
			row.appendChild(ZkUtil.gridDecimalbox(rs.getCOSTBASAMT()));
			row.appendChild(ZkUtil.gridDecimalbox(rs.getCOSTVATAMT()));
			row.appendChild(ZkUtil.gridLabel(rs.getUPBY()));
			row.appendChild(ZkUtil.gridLabel(FnDate.displayDateTimeString(rs.getUPDT())));

		};
	}

	private void onClick_ViewRow(Event event) {
		try {
			Component comp = event.getTarget().getParent();
			var rs = (TboBXHEADER) comp.getAttribute("DATA1");
			sel_BLNO = rs.getBLNO();

			TboBXHEADER bxH = new TboBXHEADER();

			bxH.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			bxH.setBLNO(sel_BLNO);

			if (!TbfBXHEADER.find(bxH)) {
				throw new Exception("ไม่พบรายการ");
			}

			FrmBxBillView.showData(this.getLoginBean(), sel_BLNO, this, this.getMenuId2() ,(event2) ->{
				this.setVisible(true);
			});

		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}
	}

	private void onClick_EditDataRow(Event event) {

		try {

			Component comp = event.getTarget().getParent();
			var rs = (TboBXHEADER) comp.getAttribute("DATA1");
			sel_BLNO = rs.getBLNO();

			TboBXHEADER bxH = new TboBXHEADER();

			bxH.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			bxH.setBLNO(sel_BLNO);

			if (!TbfBXHEADER.find(bxH)) {
				throw new Exception("ไม่พบรายการ");
			}

			if (bxH.getRECSTA() == 9) {
				throw new Exception("รายการยกเลิกแล้ว");
			}

			Msg.confirm("ยืนยันแก้ไข?", "?", (event1) -> {
				if (Messagebox.Button.YES.equals(event1.getButton())) {
					try {
						try {
							FrmBxBillEntr.showData(this.getLoginBean(), sel_BLNO, false, this, this.getMenuId2(), (event2) -> showGridViewt());
						} catch (Exception ex) {
							ex.printStackTrace();
							Msg.error(ex);
						}
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

	private void onClick_CostDataRow(Event event) {

		try {

			Component comp = event.getTarget().getParent();
			var rs = (TboBXHEADER) comp.getAttribute("DATA1");
			sel_BLNO = rs.getBLNO();

			TboBXHEADER bxH = new TboBXHEADER();

			bxH.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			bxH.setBLNO(sel_BLNO);

			if (!TbfBXHEADER.find(bxH)) {
				throw new Exception("ไม่พบรายการ");
			}

			if (bxH.getRECSTA() == 9) {
				throw new Exception("รายการยกเลิกแล้ว");
			}
			if (bxH.getRECSTA() == 0) {
				throw new Exception("รายการไม่สมบูรณ์");
			}
			FrmBxBillCost.showData(this.getLoginBean(), bxH.getBLNO(), this, this.getMenuId2() ,event1 -> showGridViewt());
		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}
	}

	private void onClick_CancelDataRow(Event event) {

		try {

			Component comp = event.getTarget().getParent();
			var rs = (TboBXHEADER) comp.getAttribute("DATA1");
			sel_BLNO = rs.getBLNO();

			TboBXHEADER bxH = new TboBXHEADER();

			bxH.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			bxH.setBLNO(sel_BLNO);

			if (!TbfBXHEADER.find(bxH)) {
				throw new Exception("ไม่พบรายการ");
			}

			if (bxH.getRECSTA() == 9) {
				throw new Exception("รายการยกเลิกแล้ว");
			}

			Msg.confirm("ยืนยันยกเลิก?", "?", (event1) -> {
				if (Messagebox.Button.YES.equals(event1.getButton())) {
					try {
						try {
							
							bxH.setRECSTA(9);
							bxH.setUPBY(this.getLoginBean().getUSER_ID());
							bxH.setUPDT(FnDate.getTodaySqlDateTime());
							TbfBXHEADER.update(bxH);
							
							Msg.info("เรียบร้อย");
							showGridViewt();
							
						} catch (Exception ex) {
							ex.printStackTrace();
							Msg.error(ex);
						}
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

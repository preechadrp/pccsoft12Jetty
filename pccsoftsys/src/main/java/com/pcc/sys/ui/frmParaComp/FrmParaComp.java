package com.pcc.sys.ui.frmParaComp;

import java.util.ArrayList;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;

import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.ZkUtil;
import com.pcc.sys.tbf.TbfFPARA_COMP;
import com.pcc.sys.tbm.TbmFPARA_NAME;
import com.pcc.sys.tbo.TboFPARA_COMP;

public class FrmParaComp extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Textbox txtPARA_ID;
	public Textbox txtPARA_DESC;

	public Grid gridList;

	public Button btnExit, btnFind;

	private java.util.List<FModelHasMap> lst_parameter = new ArrayList<FModelHasMap>();

	@Override
	public void setFormObj() {
		txtPARA_ID = (Textbox) getFellow("txtPARA_ID");
		txtPARA_DESC = (Textbox) getFellow("txtPARA_DESC");

		gridList = (Grid) getFellow("gridList");

		btnExit = (Button) getFellow("btnExit");
		btnFind = (Button) getFellow("btnFind");

	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(txtPARA_ID);
		addEnterIndex(txtPARA_DESC);
		addEnterIndex(btnFind);

	}

	@Override
	public void formInit() {

		try {

			ZkUtil.setGridHeaderStyle(gridList);
			this.gridList.setRowRenderer((row, data, index) -> {

				FModelHasMap rs = (FModelHasMap) data;
				int seq = index + 1;
				row.setSclass("rowGrid1");

				row.appendChild(ZkUtil.gridLabel(seq + ""));
				row.appendChild(ZkUtil.gridLabel(rs.getString("PARA_ID")));

				//PARA_VALUE
				Div div1 = new Div();

				Div divs = new Div();
				Label lbl1 = ZkUtil.gridLabel(rs.getString("PARA_DESC"));
				divs.appendChild(lbl1);
				div1.appendChild(divs);

				org.zkoss.zul.Textbox txtv1 = new org.zkoss.zul.Textbox();
				txtv1.setWidth("40%"); //ของความกว้าง cell
				txtv1.setStyle("background-color: transparent;");
				txtv1.setValue(rs.getString("PARA_VALUE"));
				txtv1.setMaxlength(60);
//				txtv1.addEventListener(Events.ON_CHANGE, (event) -> doOnClickRow(event)); //ไม่ ok ใน Iphone
				div1.appendChild(txtv1);

				Button btn_save1 = new Button();
				btn_save1.setStyle(" margin-left: 5px;");
				btn_save1.setLabel("บันทึก");
				btn_save1.addEventListener(Events.ON_CLICK, (event) -> doOnClickRow(event));
				div1.appendChild(btn_save1);

				row.appendChild(div1);

				//=== เพิ่ม Attribute
				row.setAttribute("PARA_ID", rs.getString("PARA_ID"));
				row.setAttribute("txtv1", txtv1);

			});

			show_data("", "");

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

	public void onClick_btnFind() {
		try {
			show_data(txtPARA_ID.getValue(), txtPARA_DESC.getValue());
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void onClick_btnShowAll() {
		try {
			show_data("", "");
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	private void show_data(String para_id, String para_desc) throws Exception {
		this.gridList.getRows().getChildren().clear();
		TbmFPARA_NAME.getDataQry(lst_parameter, para_id, para_desc, this.getMenuId2(), this.getLoginBean());
		SimpleListModel rstModel = new SimpleListModel(lst_parameter);
		this.gridList.setModel(rstModel);
	}

	private void doOnClickRow(Event event) {
		try {

			Row row = (Row) event.getTarget().getParent().getParent();

			String sPARA_ID = Fnc.getStr(row.getAttribute("PARA_ID"));
			Textbox txtv1 = (Textbox) row.getAttribute("txtv1");

			boolean new_rec = true;

			TboFPARA_COMP fpc = new TboFPARA_COMP();

			fpc.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			fpc.setPARA_ID(sPARA_ID);

			if (TbfFPARA_COMP.find(fpc)) {
				new_rec = false;
			}

			fpc.setPARA_VALUE(txtv1.getValue());

			if (new_rec) {
				if (!TbfFPARA_COMP.insert(fpc)) {
					throw new Exception("ไม่สามารถบันทึกได้กรุณาลองใหม่");
				}
			} else {
				if (!TbfFPARA_COMP.update(fpc, "")) {
					throw new Exception("ไม่สามารถบันทึกได้กรุณาลองใหม่");
				}
			}

			System.out.println("doOnClickRow");
			Msg.info("บันทึกเรียบร้อย");

		} catch (Exception e) {
			Msg.error(e);
		}

	}

}

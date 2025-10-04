package com.pcc.gl.ui.acArClear;

import java.util.ArrayList;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;

import com.pcc.gl.find.FfACCT_VOU_TYPE;
import com.pcc.gl.lib.FConstAc;
import com.pcc.gl.progman.ManAcArClear;
import com.pcc.gl.tbf.TbfACCT_VOU_TYPE;
import com.pcc.gl.tbo.TboACCT_VOU_TYPE;
import com.pcc.gl.ui.acApClear.AcApClearView;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.ZkUtil;

public class AcArClear extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Textbox txtVOU_TYPE;
	public Textbox txtVOU_NAME;
	public Textbox txtVOU_NO;
	public Textbox txtDOCNO;
	private Grid gridAccList;

	private java.util.List<FModelHasMap> lst_gl_header = new ArrayList<FModelHasMap>();

	public Button btnExit, btnFind;

	@Override
	public void setFormObj() {
		txtVOU_TYPE = (Textbox) getFellow("txtVOU_TYPE");
		txtVOU_NAME = (Textbox) getFellow("txtVOU_NAME");
		txtVOU_NO = (Textbox) getFellow("txtVOU_NO");
		txtDOCNO = (Textbox) getFellow("txtDOCNO");
		gridAccList = (Grid) getFellow("gridAccList");

		btnExit = (Button) getFellow("btnExit");
		btnFind = (Button) getFellow("btnFind");
	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(txtVOU_TYPE);
		addEnterIndex(txtVOU_NO);
		addEnterIndex(txtDOCNO);
		addEnterIndex(btnFind);

	}

	@Override
	public void formInit() {
		try {

			ZkUtil.setGridHeaderStyle(gridAccList);
			this.gridAccList.setRowRenderer(getRowRenderer1());
			clearData();

		} catch (Exception e) {
			Msg.error(e);
		}
	}

	private void clearData() {
		txtVOU_TYPE.setValue("");
		txtVOU_NAME.setValue("");
		txtVOU_NO.setValue("");
		txtDOCNO.setValue("");
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
			this.gridAccList.getRows().getChildren().clear();
			ManAcArClear.getDataQry(lst_gl_header, txtVOU_TYPE.getValue(), txtVOU_NO.getValue(), txtDOCNO.getValue(),
					this.getLoginBean());
			this.gridAccList.setModel(new SimpleListModel(lst_gl_header));
			this.gridAccList.renderAll();

		} catch (Exception e) {
			Msg.error(e);
		}
	}

	private void doOnClickRow(Event event) {
		try {

			Row row = (Row) event.getTarget().getParent();

			String sLINK_NO = Fnc.getStr(row.getAttribute("LINK_NO"));

			if (!ZkUtil.isPopup("AcApClearView")) {
				AcApClearView fAcApClearView = (AcApClearView) ZkUtil
						.callZulFile("/com/pcc/gl/zul/AcApClear/AcApClearView.zul");
				fAcApClearView.setId("AcApClearView");// ต้องเรียกก่อนตัวอื่น
				fAcApClearView.setFormObj();// ต้องเรียกก่อนตัวอื่น
				fAcApClearView.setLink_no(sLINK_NO);
				fAcApClearView.setLoginBean(this.getLoginBean());
				fAcApClearView.showData();
				fAcApClearView.doModal();
			}

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void popupVouType() {
		if (!txtVOU_TYPE.isReadonly()) {
			FfACCT_VOU_TYPE.popup("", this.getLoginBean(), this, "doPopupVouType");
		}
	}

	public void doPopupVouType(String vou_type) {
		try {
			read_vou_type(vou_type);
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void onChange_txtVOU_TYPE() {
		try {
			if (!read_vou_type(txtVOU_TYPE.getValue())) {
				txtVOU_NAME.setValue("");
			}
		} catch (Exception e) {
		}
	}

	public boolean read_vou_type(String vou_type) throws Exception {
		boolean res = false;

		TboACCT_VOU_TYPE acctype = new TboACCT_VOU_TYPE();
		acctype.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		acctype.setVOU_TYPE(vou_type);
		if (TbfACCT_VOU_TYPE.find(acctype)) {
			txtVOU_TYPE.setValue(acctype.getVOU_TYPE());
			txtVOU_NAME.setValue(acctype.getVOU_NAME());
			res = true;
		}

		return res;
	}

	private RowRenderer getRowRenderer1() {

		return (row, dat, index) -> {

			FModelHasMap rs = (FModelHasMap) dat;
			int seq = index + 1;
			row.setSclass("rowGrid1");

			row.appendChild(ZkUtil.gridLabel(seq + ""));

			Button btn_view = new Button();
			btn_view.setLabel("ดูรายละเอียด");
			btn_view.setSclass("buttonView");
			btn_view.addEventListener(Events.ON_CLICK, (event) -> doOnClickRow(event));
			row.appendChild(btn_view);

			row.appendChild(ZkUtil.gridLabel(rs.getString("VOU_TYPE") + rs.getString("VOU_NO")));
			row.appendChild(ZkUtil.gridLabel(FnDate.displayDateString(rs.getDate("POSTDATE"))));

			String custName = Fnc.getStr(rs.getString("TITLE")) + " " + Fnc.getStr(rs.getString("FNAME")) + " "
					+ Fnc.getStr(rs.getString("LNAME"));
			row.appendChild(ZkUtil.gridTextbox(custName));

			row.appendChild(ZkUtil.gridTextbox(rs.getString("DOCNO")));
			row.appendChild(ZkUtil.gridLabel(FnDate.displayDateString(rs.getDate("DOCDATE"))));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("DESCR")));

			String statName = FConstAc.get_gl_status_name(rs.getInt("RECSTA") + "");
			String sStyle = FConstAc.get_gl_status_style(rs.getInt("RECSTA") + "");
			row.appendChild(ZkUtil.gridLabel(statName, sStyle));

			row.appendChild(ZkUtil.gridLabel(rs.getString("INSBY")));
			row.appendChild(ZkUtil.gridLabel(FnDate.displayDateTimeString(rs.getTimestamp("INSDT"))));

			// === เพิ่ม Attribute
			row.setAttribute("LINK_NO", rs.getString("LINK_NO"));

		};

	}

}

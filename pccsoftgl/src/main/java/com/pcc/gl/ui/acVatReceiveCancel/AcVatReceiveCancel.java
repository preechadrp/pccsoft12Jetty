package com.pcc.gl.ui.acVatReceiveCancel;

import java.util.ArrayList;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;

import com.pcc.gl.find.FfACCT_VOU_TYPE;
import com.pcc.gl.progman.ManAcVatReceiveCancel;
import com.pcc.gl.tbf.TbfACCT_VOU_TYPE;
import com.pcc.gl.tbm.TbmACGL_HEADER;
import com.pcc.gl.tbo.TboACCT_VOU_TYPE;
import com.pcc.gl.ui.acEntrQry.AcEntrView;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.ZkUtil;

public class AcVatReceiveCancel extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Datebox tdbPOSTDATE_FROM;
	public Datebox tdbPOSTDATE_TO;
	public Textbox txtVOU_TYPE;
	public Textbox txtVOU_NAME;
	public Textbox txtVOU_NO;
	private Grid gridAccList;

	private java.util.List<FModelHasMap> lst_gl_header = new ArrayList<FModelHasMap>();

	public Button btnExit, btnFind;

	@Override
	public void setFormObj() {
		tdbPOSTDATE_FROM = (Datebox) getFellow("tdbPOSTDATE_FROM");
		tdbPOSTDATE_TO = (Datebox) getFellow("tdbPOSTDATE_TO");
		txtVOU_TYPE = (Textbox) getFellow("txtVOU_TYPE");
		txtVOU_NAME = (Textbox) getFellow("txtVOU_NAME");
		txtVOU_NO = (Textbox) getFellow("txtVOU_NO");
		gridAccList = (Grid) getFellow("gridAccList");

		btnExit = (Button) getFellow("btnExit");
		btnFind = (Button) getFellow("btnFind");
	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(tdbPOSTDATE_FROM);
		addEnterIndex(tdbPOSTDATE_TO);
		addEnterIndex(txtVOU_TYPE);
		addEnterIndex(txtVOU_NO);
		addEnterIndex(btnFind);

	}

	@Override
	public void formInit() {
		try {

			ZkUtil.setGridHeaderStyle(gridAccList);
			this.gridAccList.setRowRenderer(getRowRenderer1());
			clearData();
			read_vou_type("VT");

		} catch (Exception e) {
			Msg.error(e);
		}
	}

	private void clearData() {
		tdbPOSTDATE_FROM.setValue(null);
		txtVOU_TYPE.setValue("");
		txtVOU_NAME.setValue("");
		txtVOU_NO.setValue("");
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
			FnDate.getSqlDate(tdbPOSTDATE_FROM.getValue());
			this.gridAccList.getRows().getChildren().clear();
			TbmACGL_HEADER.getDataQryForAcVatReceiveCancel(lst_gl_header, txtVOU_TYPE.getValue(), txtVOU_NO.getValue(),
					FnDate.getSqlDate(tdbPOSTDATE_FROM.getValue()), FnDate.getSqlDate(tdbPOSTDATE_TO.getValue()),
					this.getLoginBean());
			this.gridAccList.setModel(new SimpleListModel(lst_gl_header));

			// renderAll() ช่วยให้ได้ทั้งคุณสมบัติ
			// 1. paging ที่แสดงผลเร็ว
			// 2. object ทุกตัวใน row
			// ถูกสร้างและเข้าถึงได้ทันทีโดยไม่ต้องคลิ๊กไปที่หน้านั้นก่อน
			// 3. ทำ sort column ได้เวลา click ที่หัวคอลัมภ์นั้นๆ
			this.gridAccList.renderAll();

		} catch (Exception e) {
			Msg.error(e);
		}
	}

	private void doOnClickRow(Event event) {
		try {

			Row row = (Row) event.getTarget().getParent();

			String sVOU_TYPE = Fnc.getStr(row.getAttribute("VOU_TYPE"));
			String sVOU_NO = Fnc.getStr(row.getAttribute("VOU_NO"));

			if (!ZkUtil.isPopup("AcEntrView")) {
				AcEntrView fAcEntrView = (AcEntrView) ZkUtil.callZulFile("/com/pcc/gl/zul/AcEntrQry/AcEntrView.zul");
				fAcEntrView.setId("AcEntrView");// ต้องเรียกก่อนตัวอื่น
				fAcEntrView.setFormObj();// ต้องเรียกก่อนตัวอื่น
				fAcEntrView.vou_type = sVOU_TYPE;
				fAcEntrView.vou_no = sVOU_NO;
				fAcEntrView.setLoginBean(this.getLoginBean());
				fAcEntrView.showData();
				fAcEntrView.doModal();
			}

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	private void doOnClickCancelRow(Event event) {
		try {

			Row row = (Row) event.getTarget().getParent();

			String vou_type = Fnc.getStr(row.getAttribute("VOU_TYPE"));
			String vou_no = Fnc.getStr(row.getAttribute("VOU_NO"));

			ManAcVatReceiveCancel.saveCancel(this.getLoginBean(), vou_type, vou_no);
			onClick_btnFind();

			Msg.info("เรียบร้อย");

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

		return (Row row, Object dat, int index) -> {

			FModelHasMap rs = (FModelHasMap) dat;
			int seq = index + 1;
			row.setSclass("rowGrid1");

			row.appendChild(ZkUtil.gridLabel(seq + ""));

			Button btn_view = new Button();
			btn_view.setLabel("ดูรายละเอียด");
			btn_view.setSclass("buttonView");
			btn_view.addEventListener(Events.ON_CLICK, (event) -> doOnClickRow(event));
			row.appendChild(btn_view);

			Button btn_cancel = new Button();
			btn_cancel.setLabel("ยกเลิก-รับภาษี");
			btn_cancel.setSclass("buttonView");
			btn_cancel.addEventListener(Events.ON_CLICK, (event) -> doOnClickCancelRow(event));
			row.appendChild(btn_cancel);

			row.appendChild(ZkUtil.gridLabel(rs.getString("VOU_TYPE") + rs.getString("VOU_NO")));
			row.appendChild(ZkUtil.gridLabel(FnDate.displayDateString(rs.getDate("POSTDATE"))));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("DESCR")));

			String statName = "";
			if (rs.getInt("RECSTA") == 0) {
				statName = "ยังไม่บันทึก";
			} else if (rs.getInt("RECSTA") == 1) {
				statName = "ยังไม่อนุมัติ";
			} else if (rs.getInt("RECSTA") == 2) {
				statName = "อนุมัติ";
			} else if (rs.getInt("RECSTA") == 9) {
				statName = "ยกเลิก";
			}
			row.appendChild(ZkUtil.gridLabel(statName));
			row.appendChild(ZkUtil.gridLabel(rs.getString("UPBY")));
			row.appendChild(ZkUtil.gridLabel(FnDate.displayDateTimeString(rs.getTimestamp("UPDT"))));
			row.appendChild(ZkUtil.gridLabel(rs.getString("APPR_BY")));
			row.appendChild(ZkUtil.gridLabel(FnDate.displayDateTimeString(rs.getTimestamp("APPR_DT"))));

			// === เพิ่ม Attribute
			row.setAttribute("VOU_TYPE", rs.getString("VOU_TYPE"));
			row.setAttribute("VOU_NO", rs.getString("VOU_NO"));

		};

	}

}

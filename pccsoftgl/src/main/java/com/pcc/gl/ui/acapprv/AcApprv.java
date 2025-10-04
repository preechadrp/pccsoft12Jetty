package com.pcc.gl.ui.acapprv;

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
import com.pcc.gl.lib.FConstAc;
import com.pcc.gl.progman.ManAcEntr;
import com.pcc.gl.tbf.TbfACCT_VOU_TYPE;
import com.pcc.gl.tbf.TbfACGL_HEADER;
import com.pcc.gl.tbm.TbmACGL_DETAIL;
import com.pcc.gl.tbm.TbmACGL_HEADER;
import com.pcc.gl.tbo.TboACCT_VOU_TYPE;
import com.pcc.gl.tbo.TboACGL_HEADER;
import com.pcc.gl.ui.acEntrQry.AcEntrView;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.ZkUtil;

public class AcApprv extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Datebox tdbPOSTDATE_FROM;
	public Datebox tdbPOSTDATE_TO;
	public Textbox txtVOU_TYPE;
	public Textbox txtVOU_NAME;
	public Textbox txtVOU_NO;
	private Grid gridAccList;

	private java.util.List<FModelHasMap> lst_gl_header = new ArrayList<FModelHasMap>();

	public Button btnExit, btnFind;
	private final String sApprove = "อนุมัติ";
	private final String sNotApprove = "ยกเลิกอนุมัติ";

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

		// === setRowRenderer
		this.gridAccList.setRowRenderer(this.getRowRenderer());
		ZkUtil.setGridHeaderStyle(gridAccList);
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
			clearData();
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
			TbmACGL_HEADER.getDataQryAppv(lst_gl_header, txtVOU_TYPE.getValue(), txtVOU_NO.getValue(),
					FnDate.getSqlDate(tdbPOSTDATE_FROM.getValue()), FnDate.getSqlDate(tdbPOSTDATE_TO.getValue()),
					this.getMenuId2(), this.getLoginBean());
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

	public void doOnClickApprvRow(Event event) {

		try {

			Row row = (Row) event.getTarget().getParent();

			String sVOU_TYPE = Fnc.getStr(row.getAttribute("VOU_TYPE"));
			String sVOU_NO = Fnc.getStr(row.getAttribute("VOU_NO"));

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				TboACGL_HEADER acgl_header = new TboACGL_HEADER();

				acgl_header.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				acgl_header.setVOU_TYPE(sVOU_TYPE);
				acgl_header.setVOU_NO(sVOU_NO);

				if (TbfACGL_HEADER.find(dbc, acgl_header)) {

					if (acgl_header.getRECSTA() == 1) {// ยังไม่อนุมัติ

						acgl_header.setRECSTA(2);// 2=อนุมัติ
						if (!TbfACGL_HEADER.update(dbc, acgl_header, "")) {
							throw new Exception("ไม่สามารถบันทึกข้อมูลได้");
						}
						TbmACGL_DETAIL.updateRECSTA(dbc, 2, this.getLoginBean().getCOMP_CDE(), sVOU_TYPE, sVOU_NO);
						ManAcEntr.update_RECSTA(dbc, 2, this.getLoginBean(), sVOU_TYPE, sVOU_NO);

					} else if (acgl_header.getRECSTA() == 2) {// อนุมัติแล้ว

						acgl_header.setRECSTA(1);// 1=ยังไม่อนุมัติ
						if (!TbfACGL_HEADER.update(dbc, acgl_header, "")) {
							throw new Exception("ไม่สามารถบันทึกข้อมูลได้");
						}
						TbmACGL_DETAIL.updateRECSTA(dbc, 1, this.getLoginBean().getCOMP_CDE(), sVOU_TYPE, sVOU_NO);
						ManAcEntr.update_RECSTA(dbc, 1, this.getLoginBean(), sVOU_TYPE, sVOU_NO);

					} else {
						throw new Exception("สถานะเอกสารไม่ถูกต้อง");
					}

				}

				dbc.commit();
			}

			onClick_btnFind();

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

	public RowRenderer getRowRenderer() {

		return (row, dat, index) -> {

			FModelHasMap rs = (FModelHasMap) dat;
			int seq = index + 1;
			row.setSclass("rowGrid1");

			row.appendChild(ZkUtil.gridLabel(seq + ""));

			Button btn_apprv = new Button();
			btn_apprv.addEventListener(Events.ON_CLICK, (event) -> doOnClickApprvRow(event));
			if (rs.getInt("RECSTA") == 0) {
				btn_apprv.setVisible(false);
			} else if (rs.getInt("RECSTA") == 1) {
				btn_apprv.setVisible(true);
				btn_apprv.setLabel(sApprove);
			} else if (rs.getInt("RECSTA") == 2) {
				btn_apprv.setVisible(true);
				btn_apprv.setLabel(sNotApprove);
			} else if (rs.getInt("RECSTA") == 9) {
				btn_apprv.setVisible(false);
			}
			row.appendChild(btn_apprv);

			Button btn_view = new Button();
			btn_view.setLabel("ดูรายละเอียด");
			btn_view.setSclass("buttonView");
			btn_view.addEventListener(Events.ON_CLICK, (event) -> doOnClickRow(event));
			row.appendChild(btn_view);

			row.appendChild(ZkUtil.gridLabel(rs.getString("VOU_TYPE") + rs.getString("VOU_NO")));
			row.appendChild(ZkUtil.gridLabel(FnDate.displayDateString(rs.getDate("POSTDATE"))));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("DESCR")));

			String statName = FConstAc.get_gl_status_name(rs.getInt("RECSTA") + "");
			String sStyle = FConstAc.get_gl_status_style(rs.getInt("RECSTA") + "");
			row.appendChild(ZkUtil.gridLabel(statName, sStyle));

			row.appendChild(ZkUtil.gridLabel(rs.getString("UPBY")));
			row.appendChild(ZkUtil.gridLabel(FnDate.displayDateTimeString(rs.getTimestamp("UPDT"))));
			row.appendChild(ZkUtil.gridLabel(rs.getString("APPR_BY")));
			row.appendChild(ZkUtil.gridLabel(FnDate.displayDateTimeString(rs.getTimestamp("APPR_DT"))));

			// === เพิ่ม Attribute
			row.setAttribute("VOU_TYPE", rs.getString("VOU_TYPE"));
			row.setAttribute("VOU_NO", rs.getString("VOU_NO"));
			row.setAttribute("btn_apprv", btn_apprv);

		};

	}

}

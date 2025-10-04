package com.pcc.gl.ui.acAcctcodeApAr;

import java.util.ArrayList;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;

import com.pcc.gl.progman.ManAcAcctcodeApAr;
import com.pcc.gl.tbf.TbfACCT_NO_SUB;
import com.pcc.gl.tbo.TboACCT_NO_SUB;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.ZkUtil;

public class AcAcctcodeApAr extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Textbox txtACCT_ID;
	public Textbox txtACCT_NAME;
	public Combobox cmbSUB_TYPE;

	private Grid gridAccList;

	private java.util.List<FModelHasMap> lst_acct_no_sub = new ArrayList<FModelHasMap>();

	public Button btnExit;
	public Button btnShow;

	@Override
	public void setFormObj() {

		txtACCT_ID = (Textbox) getFellow("txtACCT_ID");
		txtACCT_NAME = (Textbox) getFellow("txtACCT_NAME");
		cmbSUB_TYPE = (Combobox) getFellow("cmbSUB_TYPE");

		gridAccList = (Grid) getFellow("gridAccList");

		btnExit = (Button) getFellow("btnExit");
		btnShow = (Button) getFellow("btnShow");

	}

	@Override
	public void addEnterIndex() {

		addEnterIndex(txtACCT_ID);
		addEnterIndex(txtACCT_NAME);
		addEnterIndex(cmbSUB_TYPE);
		addEnterIndex(btnShow);

	}

	@Override
	public void formInit() {

		try {

			// == set style and renderer
			ZkUtil.setGridHeaderStyle(this.gridAccList);
			this.gridAccList.setRowRenderer(this.getRowRenderer1());
			
			cmbSUB_TYPE.setSelectedIndex(0);
			
			onClick_btnRefresh();

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

	public void onChange_Data(Component comp) {
		try {
			// if (comp.getId().equals("txtACCT_ID")) {
			// read_ACCT_ID(txtACCT_ID.getValue());
			// }
		} catch (Exception e) {
		}
	}

	public void onClick_btnAdd() {
		try {
			AcAcctcodeApArEntr.showData(this.getLoginBean(), "", this, this.getMenuId2(), true, (event1) ->onClick_btnRefresh());
		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}
	}
	
	public void onClick_btnShow() {
		try {
			onClick_btnRefresh();
		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}
	}

	public void onClick_btnRefresh() throws Exception {
		int subtype = Fnc.getIntFromStr(ZkUtil.getSelectItemValueComboBox(cmbSUB_TYPE));
		ManAcAcctcodeApAr.getDataQry(lst_acct_no_sub, txtACCT_ID.getValue(), txtACCT_NAME.getValue(), subtype, this.getLoginBean());
		SimpleListModel rstModel = new SimpleListModel(lst_acct_no_sub);
		this.gridAccList.setModel(rstModel);
		this.gridAccList.renderAll();
	}

	public RowRenderer getRowRenderer1() {

		return (row, dat, index) -> {

			FModelHasMap rs = (FModelHasMap) dat;
			int seq = index + 1;
			row.setSclass("rowGrid1");

			row.appendChild(ZkUtil.gridLabel(seq + ""));

			Button btn_edit = new Button();
			btn_edit.setLabel("ดู/แก้ไข");
			btn_edit.setSclass("buttonedit");
			btn_edit.addEventListener(Events.ON_CLICK, (event) -> onClick_BtnEditRow(event));
			row.appendChild(btn_edit);

			Button btn_del = new Button();
			btn_del.setLabel("ลบ");
			btn_del.setSclass("buttondel");
			btn_del.addEventListener(Events.ON_CLICK, (event) -> onClick_BtnDelRow(event));
			row.appendChild(btn_del);

			row.appendChild(ZkUtil.gridTextbox(rs.getString("ACCT_ID")));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("ACCT_NAME")));

			String sub_type_desc = "";
			if (rs.getInt("SUB_TYPE") == 1) {
				sub_type_desc = "เจ้าหนี้";
			} else if (rs.getInt("SUB_TYPE") == 2) {
				sub_type_desc = "ลูกหนี้";
			} else if (rs.getInt("SUB_TYPE") == 3) {
				sub_type_desc = "เช็คจ่ายล่วงหน้า";
			}
			row.appendChild(ZkUtil.gridTextbox(sub_type_desc));

			String sACCT_ID_BANK = "";
			if (!Fnc.isEmpty(rs.getString("ACCT_ID_BANK"))) {
				sACCT_ID_BANK = rs.getString("ACCT_ID_BANK") + ":" + rs.getString("ACCT_NAME_BANK");
			}
			row.appendChild(ZkUtil.gridTextbox(sACCT_ID_BANK));

			row.appendChild(ZkUtil.gridTextbox(rs.getString("INSBY")));
			row.appendChild(ZkUtil.gridTextbox(FnDate.displayDateTimeString(rs.getTimestamp("INSDT"))));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("UPBY")));
			row.appendChild(ZkUtil.gridTextbox(FnDate.displayDateTimeString(rs.getTimestamp("UPDT"))));

			// === เพิ่ม Attribute
			row.setAttribute("ACCT_ID", rs.getString("ACCT_ID"));

		};

	}

	public void onClick_BtnEditRow(org.zkoss.zk.ui.event.Event event) {

		try {

			Row row = (Row) event.getTarget().getParent();
			String sACCT_ID = Fnc.getStr(row.getAttribute("ACCT_ID"));
			AcAcctcodeApArEntr.showData(this.getLoginBean(), sACCT_ID, this, this.getMenuId2(), false, (event1) ->onClick_btnRefresh());

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void onClick_BtnDelRow(org.zkoss.zk.ui.event.Event event) {

		Msg.confirm(Labels.getLabel("comm.label.deleteComfirm") + " ?", "?", (event1) -> {
			if (Messagebox.Button.YES.equals(event1.getButton())) {

				Row row = (Row) event.getTarget().getParent();
				String sACCT_ID = Fnc.getStr(row.getAttribute("ACCT_ID"));

				try {

					TboACCT_NO_SUB acctnoSub = new TboACCT_NO_SUB();

					acctnoSub.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
					acctnoSub.setACCT_ID(sACCT_ID);

					if (!TbfACCT_NO_SUB.delete(acctnoSub)) {
						throw new Exception("ไม่สามารถลบข้อมูลได้");
					}

					onClick_btnRefresh();
					Msg.info("ลบข้อมูลเรียบร้อยแล้ว");

				} catch (Exception e) {
					e.printStackTrace();
					Msg.error(e);
				}
			}

		});

	}

}

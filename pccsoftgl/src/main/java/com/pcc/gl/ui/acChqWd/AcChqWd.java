package com.pcc.gl.ui.acChqWd;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;

import com.pcc.gl.find.FfACCT_NO;
import com.pcc.gl.find.FfACCT_VOU_TYPE;
import com.pcc.gl.progman.ManAcChqWd;
import com.pcc.gl.progman.ManAcEntr;
import com.pcc.gl.tbf.TbfACCT_NO;
import com.pcc.gl.tbf.TbfACCT_VOU_TYPE;
import com.pcc.gl.tbm.TbmACCT_LOCK;
import com.pcc.gl.tbo.TboACCT_NO;
import com.pcc.gl.tbo.TboACCT_VOU_TYPE;
import com.pcc.gl.tbo.TboACGL_DETAIL;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.MyDecimalbox;
import com.pcc.sys.lib.ZkUtil;

public class AcChqWd extends FWinMenu {

	private static final long serialVersionUID = 1L;
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public Datebox tdbPOSTDATE_FROM;
	public Datebox tdbPOSTDATE_TO;
	public Textbox txtACCT_ID_CHQ, txtACCT_NAME_CHQ;
	public Textbox txtVOU_TYPE;
	public Textbox txtVOU_NAME;
	public Textbox txtVOU_NO;

	public MyDecimalbox decSUMALL;
	public Checkbox chkClearByAcctid;
	// public MyDecimalbox decSUMDIFF;
	public Textbox txtACCT_ID_OTH;
	public Textbox txtACCT_NAME_OTH;
	public Datebox tdbPOSTDATE;
	public Textbox txtDESCR;

	private Grid gridList1, gridList2;

	private java.util.List<FModelHasMap> lst_gl_vatpur = new ArrayList<FModelHasMap>();
	private java.util.List<FModelHasMap> lst_select = new ArrayList<FModelHasMap>();

	public Button btnExit, btnSave, btnFind;

	@Override
	public void setFormObj() {

		tdbPOSTDATE_FROM = (Datebox) getFellow("tdbPOSTDATE_FROM");
		tdbPOSTDATE_TO = (Datebox) getFellow("tdbPOSTDATE_TO");
		txtACCT_ID_CHQ = (Textbox) getFellow("txtACCT_ID_CHQ");
		txtACCT_NAME_CHQ = (Textbox) getFellow("txtACCT_NAME_CHQ");
		txtVOU_TYPE = (Textbox) getFellow("txtVOU_TYPE");
		txtVOU_NAME = (Textbox) getFellow("txtVOU_NAME");
		txtVOU_NO = (Textbox) getFellow("txtVOU_NO");

		decSUMALL = (MyDecimalbox) getFellow("decSUMALL");
		chkClearByAcctid = (Checkbox) getFellow("chkClearByAcctid");
		txtACCT_ID_OTH = (Textbox) getFellow("txtACCT_ID_OTH");
		txtACCT_NAME_OTH = (Textbox) getFellow("txtACCT_NAME_OTH");
		tdbPOSTDATE = (Datebox) getFellow("tdbPOSTDATE");
		txtDESCR = (Textbox) getFellow("txtDESCR");

		gridList1 = (Grid) getFellow("gridList1");
		gridList2 = (Grid) getFellow("gridList2");

		btnExit = (Button) getFellow("btnExit");
		btnSave = (Button) getFellow("btnSave");
		btnFind = (Button) getFellow("btnFind");

		// == set renderer
		this.gridList1.setRowRenderer(this.getRowRendererFind());
		this.gridList2.setRowRenderer(this.getRowRendererSel());

		ZkUtil.setGridHeaderStyle(gridList1);
		ZkUtil.setGridHeaderStyle(gridList2);
	}

	@Override
	public void addEnterIndex() {

		addEnterIndex(tdbPOSTDATE_FROM);
		addEnterIndex(tdbPOSTDATE_TO);
		addEnterIndex(txtACCT_ID_CHQ);
		addEnterIndex(txtVOU_TYPE);
		addEnterIndex(txtVOU_NO);
		addEnterIndex(btnFind);
		addEnterIndex(txtACCT_ID_OTH);
		addEnterIndex(tdbPOSTDATE);
		addEnterIndex(txtDESCR);
		addEnterIndex(btnSave);

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
		tdbPOSTDATE_TO.setValue(null);
		txtACCT_ID_CHQ.setValue("");
		txtACCT_NAME_CHQ.setValue("");
		txtVOU_TYPE.setValue("");
		txtVOU_NAME.setValue("");
		txtVOU_NO.setValue("");
		decSUMALL.setValue(BigDecimal.ZERO);

		chkClearByAcctid.setChecked(false);
		txtACCT_ID_OTH.setReadonly(true);
		txtACCT_ID_OTH.setValue("");
		txtACCT_NAME_OTH.setValue("");

		tdbPOSTDATE.setValue(null);
		txtDESCR.setValue("");

		this.lst_gl_vatpur.clear();
		this.gridList1.getRows().getChildren().clear();

		this.lst_select.clear();
		show_gridList2();

	}

	public void onOK() {
		try {
			if (focusObj == txtACCT_ID_OTH) {
				if (Fnc.isEmpty(txtACCT_ID_OTH.getValue())) {
					popupACCTID();
				} else {
					super.onOK();
				}
			} else {
				super.onOK();
			}
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void onClick_btnFind() {
		try {

			this.gridList1.getRows().getChildren().clear();
			ManAcChqWd.getDataQry(lst_gl_vatpur, txtVOU_TYPE.getValue(), txtVOU_NO.getValue(),
					FnDate.getSqlDate(tdbPOSTDATE_FROM.getValue()), FnDate.getSqlDate(tdbPOSTDATE_TO.getValue()),
					txtACCT_ID_CHQ.getValue(), this.getLoginBean());
			this.gridList1.setModel(new SimpleListModel(lst_gl_vatpur));
			this.gridList1.renderAll();

		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void onClick_btnSave() {

		try {

			String[] vou_type = { "" };
			String[] vou_no = { "" };

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				validateData(dbc);

				BigDecimal sumall = decSUMALL.getValue();// ยอดล้างจริง
				String acct_id_oth = txtACCT_ID_OTH.getValue();// รหัสบัญชีอื่นๆ
				java.sql.Date postdate = FnDate.getSqlDate(tdbPOSTDATE.getValue());// วันที่ใบสำคัญ

				ManAcChqWd.saveData(dbc, sumall, chkClearByAcctid.isChecked(), acct_id_oth, postdate,
						txtDESCR.getValue(), this.lst_select, vou_type, vou_no, this.getLoginBean());

				dbc.commit();

			}

			clearData();

			if (!Fnc.isEmpty(vou_no[0])) {
				ManAcEntr.printVoucher(this.getLoginBean(), vou_type[0], vou_no[0]);
			}

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	private void validateData(FDbc dbc) throws Exception {

		if (this.lst_select.size() == 0) {
			throw new Exception("ยังไม่เลือกรายการ");
		}

		if (tdbPOSTDATE.getValue() == null) {
			throw new Exception(tdbPOSTDATE.getTooltiptext() + " ยังไม่ระบุ");
		}

		// ==ตรวจการ Lock ลงบัญชี
		TbmACCT_LOCK.checKPostDate(dbc, this.getLoginBean().getCOMP_CDE(), FnDate.getSqlDate(tdbPOSTDATE.getValue()));

		// ==วันที่ใบสำคัญต้อง >= postdate ของรายการที่เลือก
		int count = 0;
		for (FModelHasMap dat : this.lst_select) {
			count++;
			if (FnDate.compareDate(dat.getDate("POSTDATE"), FnDate.getSqlDate(tdbPOSTDATE.getValue())) > 0) {// ถ้าวันที่ตั้งมากกว่าวันที่ล้าง
				throw new Exception("วันที่ขาตั้งมากกว่าวันที่ล้างยอด ลำดับที่ " + count);
			}
		}

		if (chkClearByAcctid.isChecked()) {
			if (!read_acctid_chq(txtACCT_ID_OTH.getValue())) {
				throw new Exception(txtACCT_ID_OTH.getTooltiptext() + " ไม่ถูกต้อง");
			}

			// == ต้องเป็นรหัสที่ไม่มีตัวย่อย เพื่อเวลาผิดจะได้ไม่ถอยเยอะ
			TboACGL_DETAIL detail = new TboACGL_DETAIL();
			detail.setACCT_ID(txtACCT_ID_OTH.getValue().trim());
			detail.setNUM_TYPE(BigDecimal.ONE.negate());// เครดิต
			if (ManAcEntr.isSUB_HAS(dbc, detail, this.getLoginBean())) {
				throw new Exception(txtACCT_ID_OTH.getTooltiptext() + " ห้ามเป็นรหัสบัญชีที่มีตัวย่อย");
			}
		}

		if (Fnc.isEmpty(txtDESCR.getValue())) {
			throw new Exception(txtDESCR.getTooltiptext() + " ยังไม่ระบุ");
		}

	}

	public void popupVouType() {
		FfACCT_VOU_TYPE.popup("", this.getLoginBean(), this, "doPopupVouType");
	}

	public void doPopupVouType(String vou_type) {
		try {
			read_vou_type(vou_type);
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void onChange_Data(Component comp) {
		try {

			if (comp.getId().equals("txtACCT_ID_CHQ")) {
				if (!read_acctid_chq(txtACCT_ID_CHQ.getValue())) {
					txtACCT_NAME_CHQ.setValue("");
				}
			} else if (comp.getId().equals("txtVOU_TYPE")) {
				if (!read_vou_type(txtVOU_TYPE.getValue())) {
					txtVOU_NAME.setValue("");
				}
			} else if (comp.getId().equals("txtACCT_ID_OTH")) {
				if (!read_acctid(txtACCT_ID_OTH.getValue())) {
					txtACCT_NAME_OTH.setValue("");
				}
			}

		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void onClick_chkClearByAcctid() {
		if (chkClearByAcctid.isChecked()) {
			txtACCT_ID_OTH.setReadonly(false);
		} else {
			txtACCT_ID_OTH.setReadonly(true);
			txtACCT_ID_OTH.setValue("");
			txtACCT_NAME_OTH.setValue("");
		}
	}

	private boolean read_vou_type(String vou_type) throws Exception {
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

	public void popupACCTID_CHQ() {
		if (!txtACCT_ID_CHQ.isReadonly()) {
			FfACCT_NO.popup("", this.getLoginBean(), this, "doPopupACCTID_CHQ");
		}
	}

	public void doPopupACCTID_CHQ(String acctid_chq) {

		try {
			if (read_acctid_chq(acctid_chq)) {
				txtACCT_ID_CHQ.focus();
			}
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	private boolean read_acctid_chq(String acct_id) throws SQLException, Exception {

		TboACCT_NO acct = new TboACCT_NO();
		acct.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		acct.setACCT_ID(acct_id);
		if (TbfACCT_NO.find(acct)) {
			txtACCT_ID_CHQ.setValue(acct.getACCT_ID());
			txtACCT_NAME_CHQ.setValue(acct.getACCT_NAME());
			return true;
		} else {
			return false;
		}

	}

	public void popupACCTID() {
		if (!txtACCT_ID_OTH.isReadonly()) {
			FfACCT_NO.popup("", this.getLoginBean(), this, "doPopupACCTID");
		}
	}

	public void doPopupACCTID(String acctid) {
		try {
			if (read_acctid(acctid)) {
				txtACCT_ID_OTH.focus();
			}
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	private boolean read_acctid(String acct_id) throws SQLException, Exception {

		TboACCT_NO acct = new TboACCT_NO();
		acct.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		acct.setACCT_ID(acct_id);
		if (TbfACCT_NO.find(acct)) {
			txtACCT_ID_OTH.setValue(acct.getACCT_ID());
			txtACCT_NAME_OTH.setValue(acct.getACCT_NAME());
			return true;
		} else {
			return false;
		}

	}

	public RowRenderer getRowRendererFind() {

		return (row, dat, index) -> {

			FModelHasMap rs = (FModelHasMap) dat;
			int seq = index + 1;
			row.setSclass("rowGrid1");

			row.appendChild(ZkUtil.gridLabel(seq + ""));

			Button btn_sel = new Button();
			btn_sel.setLabel("เลือก");
			btn_sel.setAutodisable("self");
			if (isSelect(rs)) {
				btn_sel.setDisabled(true);
			}
			btn_sel.addEventListener(Events.ON_CLICK, (event) -> onClick_btnSelRow(event));
			row.appendChild(btn_sel);

			row.appendChild(ZkUtil.gridTextbox(rs.getString("VOU_TYPE") + rs.getString("VOU_NO")));
			row.appendChild(ZkUtil.gridTextbox(FnDate.displayDateString(rs.getDate("POSTDATE"))));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("CHQ_NO")));
			row.appendChild(ZkUtil.gridTextbox(FnDate.displayDateString(rs.getDate("CHQ_DATE"))));
			row.appendChild(ZkUtil.gridTextbox(Fnc.getStr(rs.getString("CHQ_PAYEE"))));
			row.appendChild(ZkUtil.gridDecimalbox(rs.getBigDecimal("AMT")));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("DESCR")));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("ACCT_ID") + " " + rs.getString("ACCT_NAME")));
			row.appendChild(
					ZkUtil.gridTextbox(rs.getString("ACCT_ID_BANK") + " " + rs.getString("ACCT_NAME_BANK")));

			// === เพิ่ม Attribute
			row.setAttribute("DAT_REC", rs);
			row.setAttribute("btn_sel", btn_sel);

		};

	}

	public boolean isSelect(FModelHasMap rs) throws Exception {

		boolean ret = false;

		if (this.lst_select.size() > 0) {

			for (FModelHasMap dat : this.lst_select) {

				if (dat.getString("VOU_TYPE").equals(rs.getString("VOU_TYPE"))
						&& dat.getString("VOU_NO").equals(rs.getString("VOU_NO"))
						&& dat.getInt("VOU_SEQ") == rs.getInt("VOU_SEQ")) {
					ret = true;
					break;
				}

			}

		}

		return ret;
	}

	public void onClick_btnSelRow(org.zkoss.zk.ui.event.Event event) {

		try {

			logger.info("onClick_ChkSelRow");
			FModelHasMap sel_rs = (FModelHasMap) event.getTarget().getParent().getAttribute("DAT_REC");

			if (isSelect(sel_rs)) {
				throw new Exception("เลือกแล้ว");
			} else {

				// ==เพิ่มรายการที่เลือก
				FModelHasMap dat = new FModelHasMap();

				dat.setString("VOU_TYPE", sel_rs.getString("VOU_TYPE"));
				dat.setString("VOU_NO", sel_rs.getString("VOU_NO"));
				dat.setInt("VOU_SEQ", sel_rs.getInt("VOU_SEQ"));
				dat.setDate("POSTDATE", sel_rs.getDate("POSTDATE"));
				dat.setString("CHQ_NO", sel_rs.getString("CHQ_NO"));
				dat.setDate("CHQ_DATE", sel_rs.getDate("CHQ_DATE"));
				dat.setString("CHQ_PAYEE", sel_rs.getString("CHQ_PAYEE"));
				dat.setBigDecimal("AMT", sel_rs.getBigDecimal("AMT"));
				dat.setString("DESCR", sel_rs.getString("DESCR"));
				dat.setString("DESCR_SUB", sel_rs.getString("DESCR_SUB"));
				dat.setString("SECT_ID", sel_rs.getString("SECT_ID"));
				dat.setString("ACCT_ID", sel_rs.getString("ACCT_ID"));
				dat.setString("ACCT_NAME", sel_rs.getString("ACCT_NAME"));
				dat.setString("ACCT_ID_BANK", sel_rs.getString("ACCT_ID_BANK"));
				dat.setString("ACCT_NAME_BANK", sel_rs.getString("ACCT_NAME_BANK"));

				this.lst_select.add(dat);
				Button btn_sel = (Button) event.getTarget();
				btn_sel.setDisabled(true);
				show_gridList2();

			}

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public RowRenderer getRowRendererSel() {

		return (row, dat, index) -> {

			FModelHasMap rs = (FModelHasMap) dat;
			int seq = index + 1;
			row.setSclass("rowGrid1");

			row.appendChild(ZkUtil.gridLabel(seq + ""));

			Button btn_del = new Button();
			btn_del.setLabel("ลบ");
			btn_del.setAutodisable("self");
			btn_del.setSclass("buttondel");
			btn_del.addEventListener(Events.ON_CLICK, (event) -> onClick_BtnDelRow(event));
			row.appendChild(btn_del);

			row.appendChild(ZkUtil.gridTextbox(rs.getString("VOU_TYPE") + rs.getString("VOU_NO")));
			row.appendChild(ZkUtil.gridTextbox(FnDate.displayDateString(rs.getDate("POSTDATE"))));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("CHQ_NO")));
			row.appendChild(ZkUtil.gridTextbox(FnDate.displayDateString(rs.getDate("CHQ_DATE"))));
			row.appendChild(ZkUtil.gridTextbox(Fnc.getStr(rs.getString("CHQ_PAYEE"))));
			row.appendChild(ZkUtil.gridDecimalbox(rs.getBigDecimal("AMT")));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("DESCR")));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("ACCT_ID") + " " + rs.getString("ACCT_NAME")));
			row.appendChild(
					ZkUtil.gridTextbox(rs.getString("ACCT_ID_BANK") + " " + rs.getString("ACCT_NAME_BANK")));

			// === เพิ่ม Attribute
			row.setAttribute("DAT_REC", rs);

		};

	}

	public void onClick_BtnDelRow(org.zkoss.zk.ui.event.Event event) {
		// ลบรายการที่เลือก
		try {
			logger.info("onClick_BtnDelRow");
			FModelHasMap del_rs = (FModelHasMap) event.getTarget().getParent().getAttribute("DAT_REC");

			String delete_vou_type = del_rs.getString("VOU_TYPE");
			String delete_vou_no = del_rs.getString("VOU_NO");
			int delete_vou_seq = del_rs.getInt("VOU_SEQ");

			// ==loop mark ปุ่ม "เลือก" เป็น setDisabled(false) ที่ gridList1 ตัวที่ค้นหา
			if (this.gridList1.getRows().getChildren().size() > 0) {

				List<Component> lst_row = this.gridList1.getRows().getChildren();
				for (Component comp : lst_row) {

					FModelHasMap find_rs = (FModelHasMap) comp.getAttribute("DAT_REC");

					String find_vou_type = find_rs.getString("VOU_TYPE");
					String find_vou_no = find_rs.getString("VOU_NO");
					int find_vou_seq = find_rs.getInt("VOU_SEQ");

					if (delete_vou_type.equals(find_vou_type) && delete_vou_no.equals(find_vou_no)
							&& delete_vou_seq == find_vou_seq) {

						Button btn_sel = (Button) comp.getAttribute("btn_sel");
						btn_sel.setDisabled(false);
						break;

					}

				}
			}

			// ==ลบรายการออกจาก gridList2
			this.lst_select.remove(del_rs);
			show_gridList2();

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	private void show_gridList2() {

		try {

			gridList2.getRows().getChildren().clear();
			SimpleListModel rstModel = new SimpleListModel(this.lst_select);
			gridList2.setModel(rstModel);
			gridList2.renderAll();

			sum_select();

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	private void sum_select() throws Exception {
		// == sum amt
		BigDecimal sSUMALL = BigDecimal.ZERO;// decRCV_AMT
		for (FModelHasMap row : this.lst_select) {
			sSUMALL = sSUMALL.add(row.getBigDecimal("AMT"));
		}
		decSUMALL.setValue(sSUMALL);// ยอดรวม
	}

}

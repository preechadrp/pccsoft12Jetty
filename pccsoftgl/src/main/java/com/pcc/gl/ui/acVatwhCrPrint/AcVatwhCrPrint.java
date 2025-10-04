package com.pcc.gl.ui.acVatwhCrPrint;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;

import com.pcc.gl.find.FfACCT_VOU_TYPE;
import com.pcc.gl.find.FfFCUS;
import com.pcc.gl.progman.ManAcVatwhCrPrint;
import com.pcc.gl.tbf.TbfACCT_VOU_TYPE;
import com.pcc.gl.tbm.TbmFCUS;
import com.pcc.gl.tbo.TboACCT_VOU_TYPE;
import com.pcc.sys.lib.FConstComm;
import com.pcc.sys.lib.FJasperPrintList;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FReport;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.ZkUtil;

public class AcVatwhCrPrint extends FWinMenu {

	private static final long serialVersionUID = 1L;
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public Datebox tdbPOSTDATE_FROM;
	public Datebox tdbPOSTDATE_TO;
	public Textbox txtCUST_CDE, txtCUST_NAME;
	public Textbox txtVOU_TYPE;
	public Textbox txtVOU_NAME;
	public Textbox txtVOU_NO;
	public Combobox cmbDOC_TYPE;

	private Grid gridList1, gridList2;

	private java.util.List<FModelHasMap> lst_vatwh_cr = new ArrayList<FModelHasMap>();
	private java.util.List<FModelHasMap> lst_select = new ArrayList<FModelHasMap>();

	public Button btnExit, btnPrint, btnFind;

	@Override
	public void setFormObj() {

		tdbPOSTDATE_FROM = (Datebox) getFellow("tdbPOSTDATE_FROM");
		tdbPOSTDATE_TO = (Datebox) getFellow("tdbPOSTDATE_TO");
		txtCUST_CDE = (Textbox) getFellow("txtCUST_CDE");
		txtCUST_NAME = (Textbox) getFellow("txtCUST_NAME");
		txtVOU_TYPE = (Textbox) getFellow("txtVOU_TYPE");
		txtVOU_NAME = (Textbox) getFellow("txtVOU_NAME");
		txtVOU_NO = (Textbox) getFellow("txtVOU_NO");
		cmbDOC_TYPE = (Combobox) getFellow("cmbDOC_TYPE");

		gridList1 = (Grid) getFellow("gridList1");
		gridList2 = (Grid) getFellow("gridList2");

		btnExit = (Button) getFellow("btnExit");
		btnPrint = (Button) getFellow("btnPrint");
		btnFind = (Button) getFellow("btnFind");

		// == set renderer
		this.gridList1.setRowRenderer(this.getRowRendererFind());
		this.gridList2.setRowRenderer(this.getRowRendererSel());

		ZkUtil.setGridHeaderStyle(gridList1);
		ZkUtil.setGridHeaderStyle(gridList2);

		ZkUtil.appendItemToComboBox(cmbDOC_TYPE, "1", "ภงด.3 (บุคคลธรรมดา)");
		ZkUtil.appendItemToComboBox(cmbDOC_TYPE, "2", "ภงด.53 (นิติบุคคล)");

	}

	@Override
	public void addEnterIndex() {

		addEnterIndex(tdbPOSTDATE_FROM);
		addEnterIndex(tdbPOSTDATE_TO);
		addEnterIndex(txtCUST_CDE);
		addEnterIndex(txtVOU_TYPE);
		addEnterIndex(txtVOU_NO);
		addEnterIndex(cmbDOC_TYPE);
		addEnterIndex(btnFind);

	}

	@Override
	public void formInit() {

		try {

			if (this.getMenuId2().equals("AcVatwhCrPrintCancel")) {
				btnPrint.setLabel(Labels.getLabel("comm.label.saveCancel"));
			}

			clearData();
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	private void clearData() {

		tdbPOSTDATE_FROM.setValue(null);
		tdbPOSTDATE_TO.setValue(null);
		txtCUST_CDE.setValue("");
		txtCUST_NAME.setValue("");
		txtVOU_TYPE.setValue("");
		txtVOU_NAME.setValue("");
		txtVOU_NO.setValue("");
		cmbDOC_TYPE.setSelectedIndex(0);

		this.lst_vatwh_cr.clear();
		this.gridList1.getRows().getChildren().clear();

		this.lst_select.clear();
		show_gridList2();

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

			String doc_type = ZkUtil.getSelectItemValueComboBox(cmbDOC_TYPE);
			String cust_cde = txtCUST_CDE.getValue().trim();

			this.gridList1.getRows().getChildren().clear();
			ManAcVatwhCrPrint.getDataQry(this.lst_vatwh_cr, FnDate.getSqlDate(tdbPOSTDATE_FROM.getValue()),
					FnDate.getSqlDate(tdbPOSTDATE_TO.getValue()), cust_cde, txtVOU_TYPE.getValue(),
					txtVOU_NO.getValue(), doc_type, this.getMenuId2(), this.getLoginBean());
			this.gridList1.setModel(new SimpleListModel(lst_vatwh_cr));
			this.gridList1.renderAll();

		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void onClick_btnPrint(int print_option) {

		try {

			FJasperPrintList jasperPrintList = new FJasperPrintList();

			validateData();

			if (this.getMenuId2().equals("AcVatwhCrPrintCancel")) {// ยกเลิกการพิมพ์

				Msg.confirm("ยืนยันการยกเลิก", "?", (event1) -> {
					if (Messagebox.Button.YES.equals(event1.getButton())) {
						try {
							ManAcVatwhCrPrint.saveCancel(lst_select, getLoginBean());
							lst_vatwh_cr.clear();
							gridList1.getRows().getChildren().clear();
							lst_select.clear();
							show_gridList2();
						} catch (Exception e) {
							Msg.error(e);
						}
					}
				});

			} else {

				ManAcVatwhCrPrint.printData(this.lst_select, jasperPrintList, this.getMenuId2(), this.getLoginBean());

				this.lst_vatwh_cr.clear();
				this.gridList1.getRows().getChildren().clear();
				this.lst_select.clear();
				show_gridList2();

				if (jasperPrintList.getJasperPrintLst().size() > 0) {
					FReport.viewPdf(jasperPrintList.getJasperPrintLst());
				}

			}

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	private void validateData() throws Exception {

		if (this.lst_select.size() == 0) {
			throw new Exception("ยังไม่เลือกรายการ");
		}

		if (this.getMenuId2().equals("AcVatwhCrPrint")) {// กรณีเป็นการพิมพ์ครั้งแรก

			if (this.lst_select.size() > 2) {
				throw new Exception("เลือกไม่เกิน 2 รายการ");
			}

			// ต้องเป็น cust_cde เดียวกัน
			if (this.lst_select.size() > 1) {

				FModelHasMap dat1 = this.lst_select.get(0);
				FModelHasMap dat2 = this.lst_select.get(1);
				if (!dat1.getString("CUST_CDE").equals(dat2.getString("CUST_CDE"))) {
					throw new Exception("ต้องเป็นผู้ถูกหักเดียวกัน");
				}

			}

			// ต้องเป็นเลขที่ใบสำคัญเดียวกัน
		} else {
			// reprint-ไม่จำกัดจำนวนที่เลือก
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

	public void onChange_Data(Component comp) {
		try {

			if (comp.getId().equals("txtVOU_TYPE")) {
				if (!read_vou_type(txtVOU_TYPE.getValue())) {
					txtVOU_NAME.setValue("");
				}
			} else if (comp.getId().equals("txtCUST_CDE")) {
				if (!read_cust(txtCUST_CDE.getValue())) {
					txtCUST_NAME.setValue("");
				}
			}

		} catch (Exception e) {
			Msg.error(e);
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

	public void popupCUST_CDE() {
		FfFCUS.popup(this.getLoginBean(), this, "doPopupCUST_CDE");
	}

	public void doPopupCUST_CDE(String cust_cde) {

		try {
			if (read_cust(cust_cde)) {
				txtCUST_CDE.focus();
			}
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	private boolean read_cust(String cust_cde) {

		try {

			String[] cust_name = { "" };
			if (TbmFCUS.getCustName(cust_cde, cust_name, getLoginBean())) {
				txtCUST_CDE.setValue(cust_cde);
				txtCUST_NAME.setValue(cust_name[0]);
				return true;
			} else {
				txtCUST_CDE.setValue("");
				txtCUST_NAME.setValue("");
				return false;
			}

		} catch (Exception e) {
			logger.info(e.getMessage());
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
			row.appendChild(ZkUtil.gridTextbox(rs.getString("CUST_CDE")));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("CUST_NAME")));
			row.appendChild(ZkUtil.gridTextbox(FConstComm.getDOC_TYPE_DESC(rs.getInt("DOC_TYPE"))));
			row.appendChild(ZkUtil.gridTextbox(Fnc.getStr(rs.getString("WHNAME"))));
			row.appendChild(ZkUtil.gridTextbox(FConstComm.getWHTYPE_DESC(Fnc.getStr(rs.getString("WHTYPE")))));
			row.appendChild(ZkUtil.gridDecimalbox(rs.getBigDecimal("VAT_RATE")));

			BigDecimal amt = rs.getBigDecimal("AMT").multiply(rs.getBigDecimal("NUM_TYPE"));
			row.appendChild(ZkUtil.gridDecimalbox(amt));

			BigDecimal base_amt = rs.getBigDecimal("BASE_AMT").multiply(rs.getBigDecimal("NUM_TYPE"));
			row.appendChild(ZkUtil.gridDecimalbox(base_amt));

			row.appendChild(ZkUtil.gridTextbox(rs.getString("DOCNO")));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("DESCR")));

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
						&& dat.getInt("VOU_SEQ") == rs.getInt("VOU_SEQ")
						&& dat.getInt("VOU_DSEQ") == rs.getInt("VOU_DSEQ")) {
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
				dat.setInt("VOU_DSEQ", sel_rs.getInt("VOU_DSEQ"));
				dat.setDate("POSTDATE", sel_rs.getDate("POSTDATE"));
				dat.setInt("DOC_TYPE", sel_rs.getInt("DOC_TYPE"));
				dat.setString("WHNAME", sel_rs.getString("WHNAME"));
				dat.setString("WHTYPE", sel_rs.getString("WHTYPE"));
				dat.setBigDecimal("VAT_RATE", sel_rs.getBigDecimal("VAT_RATE"));
				dat.setBigDecimal("NUM_TYPE", sel_rs.getBigDecimal("NUM_TYPE"));
				dat.setBigDecimal("AMT", sel_rs.getBigDecimal("AMT"));
				dat.setBigDecimal("BASE_AMT", sel_rs.getBigDecimal("BASE_AMT"));
				dat.setString("DOCNO", sel_rs.getString("DOCNO"));
				dat.setString("CUST_CDE", sel_rs.getString("CUST_CDE"));
				dat.setString("CUST_NAME", sel_rs.getString("CUST_NAME"));
				dat.setString("DESCR", sel_rs.getString("DESCR"));

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
			row.appendChild(ZkUtil.gridTextbox(rs.getString("CUST_CDE")));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("CUST_NAME")));
			row.appendChild(ZkUtil.gridTextbox(FConstComm.getDOC_TYPE_DESC(rs.getInt("DOC_TYPE"))));
			row.appendChild(ZkUtil.gridTextbox(Fnc.getStr(rs.getString("WHNAME"))));
			row.appendChild(ZkUtil.gridTextbox(FConstComm.getWHTYPE_DESC(Fnc.getStr(rs.getString("WHTYPE")))));
			row.appendChild(ZkUtil.gridDecimalbox(rs.getBigDecimal("VAT_RATE")));

			BigDecimal amt = rs.getBigDecimal("AMT").multiply(rs.getBigDecimal("NUM_TYPE"));
			row.appendChild(ZkUtil.gridDecimalbox(amt));

			BigDecimal base_amt = rs.getBigDecimal("BASE_AMT").multiply(rs.getBigDecimal("NUM_TYPE"));
			row.appendChild(ZkUtil.gridDecimalbox(base_amt));

			row.appendChild(ZkUtil.gridTextbox(rs.getString("DOCNO")));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("DESCR")));

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
			int delete_vou_dseq = del_rs.getInt("VOU_DSEQ");

			// ==loop mark ปุ่ม "เลือก" เป็น setDisabled(false) ที่ gridList1 ตัวที่ค้นหา
			if (this.gridList1.getRows().getChildren().size() > 0) {

				List<Component> lst_row = this.gridList1.getRows().getChildren();
				for (Component comp : lst_row) {

					FModelHasMap find_rs = (FModelHasMap) comp.getAttribute("DAT_REC");

					String find_vou_type = find_rs.getString("VOU_TYPE");
					String find_vou_no = find_rs.getString("VOU_NO");
					int find_vou_seq = find_rs.getInt("VOU_SEQ");
					int find_vou_dseq = find_rs.getInt("VOU_DSEQ");

					if (delete_vou_type.equals(find_vou_type) && delete_vou_no.equals(find_vou_no)
							&& delete_vou_seq == find_vou_seq && delete_vou_dseq == find_vou_dseq) {

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

		} catch (Exception e) {
			Msg.error(e);
		}

	}

}

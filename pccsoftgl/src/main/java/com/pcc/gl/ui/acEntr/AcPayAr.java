package com.pcc.gl.ui.acEntr;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;

import com.pcc.gl.find.FfACCT_VOU_TYPE;
import com.pcc.gl.find.FfFCUS;
import com.pcc.gl.progman.ManAcEntr;
import com.pcc.gl.tbf.TbfACCT_VOU_TYPE;
import com.pcc.gl.tbm.TbmFCUS;
import com.pcc.gl.tbo.TboACCT_VOU_TYPE;
import com.pcc.gl.tbo.TboACGL_HEADER;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FWinUtil;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.MyDecimalbox;
import com.pcc.sys.lib.ZkUtil;

public class AcPayAr extends FWinUtil {

	private static final long serialVersionUID = 1L;
	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public Textbox txtCUST_CDE, txtCUST_NAME;
	public Textbox txtDOCNO;
	public Datebox tdbPOSTDATE_FROM, tdbPOSTDATE_TO;
	public Textbox txtVOU_TYPE, txtVOU_NAME, txtVOU_NO;
	public Button btnExit, btnFind;
	public Grid gridList1, gridList2;

	private Object callForm;
	private String callBackMethodName;

	private boolean doMapObj = false;
	private TboACGL_HEADER acgl_header = null;

	private java.util.List<FModelHasMap> lst_find = new ArrayList<FModelHasMap>();
	private java.util.List<FModelHasMap> lst_select = new ArrayList<FModelHasMap>();

	@Override
	public void setFormObj() {

		if (this.doMapObj == false) {

			txtCUST_CDE = (Textbox) getFellow("txtCUST_CDE");
			txtCUST_NAME = (Textbox) getFellow("txtCUST_NAME");
			txtDOCNO = (Textbox) getFellow("txtDOCNO");
			tdbPOSTDATE_FROM = (Datebox) getFellow("tdbPOSTDATE_FROM");
			tdbPOSTDATE_TO = (Datebox) getFellow("tdbPOSTDATE_TO");
			txtVOU_TYPE = (Textbox) getFellow("txtVOU_TYPE");
			txtVOU_NAME = (Textbox) getFellow("txtVOU_NAME");
			txtVOU_NO = (Textbox) getFellow("txtVOU_NO");

			gridList1 = (Grid) getFellow("gridList1");
			gridList2 = (Grid) getFellow("gridList2");
			btnExit = (Button) getFellow("btnExit");
			btnFind = (Button) getFellow("btnFind");

			this.doMapObj = true;

			// == set render ถ้ามี
			this.gridList1.setRowRenderer(this.getRowRendererFind());
			this.gridList2.setRowRenderer(this.getRowRendererSel());
		}

	}

	@Override
	public void addEnterIndex() {

		addEnterIndex(txtCUST_CDE);
		addEnterIndex(txtDOCNO);
		addEnterIndex(tdbPOSTDATE_FROM);
		addEnterIndex(tdbPOSTDATE_TO);
		addEnterIndex(txtVOU_TYPE);
		addEnterIndex(txtVOU_NO);
		addEnterIndex(btnFind);

	}

	@Override
	public void formInit() {
		this.setPosition("center,top");
		ZkUtil.setGridHeaderStyle(gridList1);
		ZkUtil.setGridHeaderStyle(gridList2);
	}

	public void onOK() {
		try {

			if (focusObj == txtCUST_CDE) {
				if (Fnc.isEmpty(txtCUST_CDE.getValue())) {
					popupCUST_CDE();
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

	public void onClick_btnSave() {

		try {

			ManAcEntr.addPayAr(this.lst_select, this.acgl_header, this.getLoginBean());
			ZkUtil.callBackMethod(this.getCallForm(), this.getCallBackMethodName());

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void onClick_btnFind() {

		try {

			gridList1.getRows().getChildren().clear();
			ManAcEntr.findAr(txtCUST_CDE.getValue(), txtDOCNO.getValue(), tdbPOSTDATE_FROM.getValue(),
					tdbPOSTDATE_TO.getValue(), txtVOU_TYPE.getValue(), txtVOU_NO.getValue(), this.lst_find,
					this.getLoginBean());
			SimpleListModel rstModel = new SimpleListModel(this.lst_find);
			gridList1.setModel(rstModel);
			gridList1.renderAll();

		} catch (Exception e) {
			Msg.error(e);
		}

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

	public void onChange_Data(Component comp) {
		try {
			if (comp.getId().equals("txtCUST_CDE")) {
				read_cust(txtCUST_CDE.getValue());
			}
			if (comp.getId().equals("txtVOU_TYPE")) {
				if (!read_vou_type(txtVOU_TYPE.getValue())) {
					txtVOU_NAME.setValue("");
				}
			}
		} catch (Exception e) {
			Msg.error(e);
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
			if (isSelect(rs)) {
				btn_sel.setDisabled(true);
			}
			btn_sel.addEventListener(Events.ON_CLICK, (event) -> onClick_btnSelRow(event));
			row.appendChild(btn_sel);

			row.appendChild(ZkUtil.gridTextbox(rs.getString("VOU_TYPE") + rs.getString("VOU_NO")));
			row.appendChild(ZkUtil.gridTextbox(FnDate.displayDateString(rs.getDate("POSTDATE"))));

			String custName = Fnc.getStr(rs.getString("TITLE")) + " " + Fnc.getStr(rs.getString("FNAME")) + " "
					+ Fnc.getStr(rs.getString("LNAME"));
			row.appendChild(ZkUtil.gridTextbox(custName.trim()));

			row.appendChild(ZkUtil.gridTextbox(rs.getString("DOCNO")));
			row.appendChild(ZkUtil.gridTextbox(FnDate.displayDateString(rs.getDate("DOCDATE"))));
			row.appendChild(ZkUtil.gridDecimalbox(rs.getBigDecimal("AMT")));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("DESCR")));

			// === เพิ่ม Attribute
			row.setAttribute("DAT_REC", rs);
			row.setAttribute("btn_sel", btn_sel);

		};

	}

	public RowRenderer getRowRendererSel() {

		return (row, dat, index) -> {

			FModelHasMap rs = (FModelHasMap) dat;
			int seq = index + 1;
			row.setSclass("rowGrid1");

			row.appendChild(ZkUtil.gridLabel(seq + ""));

			Button btn_del = new Button();
			btn_del.setLabel("ลบ");
			btn_del.setSclass("buttondel");
			btn_del.addEventListener(Events.ON_CLICK, (event) -> onClick_BtnDelRow(event));
			row.appendChild(btn_del);

			row.appendChild(ZkUtil.gridTextbox(rs.getString("VOU_TYPE") + rs.getString("VOU_NO")));
			row.appendChild(ZkUtil.gridTextbox(FnDate.displayDateString(rs.getDate("POSTDATE"))));

			String custName = Fnc.getStr(rs.getString("TITLE")) + " " + Fnc.getStr(rs.getString("FNAME")) + " "
					+ Fnc.getStr(rs.getString("LNAME"));
			row.appendChild(ZkUtil.gridTextbox(custName.trim()));

			row.appendChild(ZkUtil.gridTextbox(rs.getString("DOCNO")));
			row.appendChild(ZkUtil.gridTextbox(FnDate.displayDateString(rs.getDate("DOCDATE"))));
			row.appendChild(ZkUtil.gridDecimalbox(rs.getBigDecimal("AMT")));
			row.appendChild((MyDecimalbox) rs.get("decAMT_PAY"));// จ่ายจำนวน
			row.appendChild(ZkUtil.gridTextbox(rs.getString("DESCR")));

			// === เพิ่ม Attribute
			row.setAttribute("DAT_REC", rs);

		};

	}

	public void onClick_btnSelRow(org.zkoss.zk.ui.event.Event event) {

		try {

			logger.info("onClick_ChkSelRow");
			FModelHasMap sel_rs = (FModelHasMap) event.getTarget().getParent().getAttribute("DAT_REC");

			if (isSelect(sel_rs)) {
				throw new Exception("เลือกแล้ว");
			} else {

				// ==ห้ามเป็น acgl_header ที่เลือกทำรายการ
				if (sel_rs.getString("VOU_TYPE").equals(this.getAcgl_header().getVOU_TYPE())
						&& sel_rs.getString("VOU_NO").equals(this.getAcgl_header().getVOU_NO())) {
					throw new Exception("ห้ามเลือกสมุดรายวันนี้");
				}

				// ==เพิ่มรายการที่เลือก
				FModelHasMap dat = new FModelHasMap();

				dat.setString("VOU_TYPE", sel_rs.getString("VOU_TYPE"));
				dat.setString("VOU_NO", sel_rs.getString("VOU_NO"));
				dat.setInt("VOU_SEQ", sel_rs.getInt("VOU_SEQ"));
				dat.setInt("VOU_DSEQ", sel_rs.getInt("VOU_DSEQ"));
				dat.setDate("POSTDATE", sel_rs.getDate("POSTDATE"));
				dat.setString("TITLE", sel_rs.getString("TITLE"));
				dat.setString("FNAME", sel_rs.getString("FNAME"));
				dat.setString("LNAME", sel_rs.getString("LNAME"));
				dat.setString("DOCNO", sel_rs.getString("DOCNO"));
				dat.setDate("DOCDATE", sel_rs.getDate("DOCDATE"));
				dat.setBigDecimal("AMT", sel_rs.getBigDecimal("AMT"));

				MyDecimalbox decAMT_PAY = new MyDecimalbox();
				decAMT_PAY.setWidth("98%"); // ของความกว้าง cell
				decAMT_PAY.setValue(BigDecimal.ZERO);
				dat.put("decAMT_PAY", decAMT_PAY);

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

	public void show_gridList2() {

		try {

			gridList2.getRows().getChildren().clear();
			SimpleListModel rstModel = new SimpleListModel(this.lst_select);
			gridList2.setModel(rstModel);
			gridList2.renderAll();

		} catch (Exception e) {
			Msg.error(e);
		}

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

	public TboACGL_HEADER getAcgl_header() {
		return acgl_header;
	}

	public void setAcgl_header(TboACGL_HEADER acgl_header) {
		this.acgl_header = acgl_header;
	}

	public Object getCallForm() {
		return callForm;
	}

	public void setCallForm(Object callForm) {
		this.callForm = callForm;
	}

	public String getCallBackMethodName() {
		return callBackMethodName;
	}

	public void setCallBackMethodName(String callBackMethodName) {
		this.callBackMethodName = callBackMethodName;
	}

}

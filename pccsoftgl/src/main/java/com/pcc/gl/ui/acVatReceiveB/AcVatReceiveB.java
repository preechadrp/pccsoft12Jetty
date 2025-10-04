package com.pcc.gl.ui.acVatReceiveB;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;

import com.pcc.gl.find.FfACCT_NO;
import com.pcc.gl.find.FfACCT_VOU_TYPE;
import com.pcc.gl.find.FfFCUS;
import com.pcc.gl.progman.ManAcEntr;
import com.pcc.gl.progman.ManAcVatReceiveB;
import com.pcc.gl.tbf.TbfACCT_NO;
import com.pcc.gl.tbf.TbfACCT_VOU_TYPE;
import com.pcc.gl.tbm.TbmACCT_LOCK;
import com.pcc.gl.tbm.TbmFCUS;
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

public class AcVatReceiveB extends FWinMenu {

	private static final long serialVersionUID = 1L;
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public Datebox tdbPOSTDATE_FROM;
	public Datebox tdbPOSTDATE_TO;
	public Textbox txtCUST_CDE, txtCUST_NAME;
	public Textbox txtVOU_TYPE;
	public Textbox txtVOU_NAME;
	public Textbox txtVOU_NO;

	public MyDecimalbox decSUMALL;
	public MyDecimalbox decSUMREC;
	public MyDecimalbox decSUMBASE_AMT;
	public MyDecimalbox decSUMDIFF;
	public Textbox txtACCT_ID_DIFF;
	public Textbox txtACCT_NAME_DIFF;
	public Textbox txtDOCNO;
	public Datebox tdbDOCDATE;
	public Datebox tdbPOSTDATE;

	private Grid gridList1, gridList2;

	private java.util.List<FModelHasMap> lst_gl_vatpur = new ArrayList<FModelHasMap>();
	private java.util.List<FModelHasMap> lst_select = new ArrayList<FModelHasMap>();

	public Button btnExit, btnSave, btnFind;

	@Override
	public void setFormObj() {

		tdbPOSTDATE_FROM = (Datebox) getFellow("tdbPOSTDATE_FROM");
		tdbPOSTDATE_TO = (Datebox) getFellow("tdbPOSTDATE_TO");
		txtCUST_CDE = (Textbox) getFellow("txtCUST_CDE");
		txtCUST_NAME = (Textbox) getFellow("txtCUST_NAME");
		txtVOU_TYPE = (Textbox) getFellow("txtVOU_TYPE");
		txtVOU_NAME = (Textbox) getFellow("txtVOU_NAME");
		txtVOU_NO = (Textbox) getFellow("txtVOU_NO");

		decSUMALL = (MyDecimalbox) getFellow("decSUMALL");
		decSUMREC = (MyDecimalbox) getFellow("decSUMREC");
		decSUMBASE_AMT = (MyDecimalbox) getFellow("decSUMBASE_AMT");
		decSUMDIFF = (MyDecimalbox) getFellow("decSUMDIFF");
		txtACCT_ID_DIFF = (Textbox) getFellow("txtACCT_ID_DIFF");
		txtACCT_NAME_DIFF = (Textbox) getFellow("txtACCT_NAME_DIFF");
		txtDOCNO = (Textbox) getFellow("txtDOCNO");
		tdbDOCDATE = (Datebox) getFellow("tdbDOCDATE");
		tdbPOSTDATE = (Datebox) getFellow("tdbPOSTDATE");

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
		addEnterIndex(txtCUST_CDE);
		addEnterIndex(txtVOU_TYPE);
		addEnterIndex(txtVOU_NO);
		addEnterIndex(btnFind);

		addEnterIndex(decSUMREC);
		addEnterIndex(decSUMBASE_AMT);
		addEnterIndex(txtACCT_ID_DIFF);
		addEnterIndex(txtDOCNO);
		addEnterIndex(tdbDOCDATE);
		addEnterIndex(tdbPOSTDATE);
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
		txtCUST_CDE.setValue("");
		txtCUST_NAME.setValue("");
		txtVOU_TYPE.setValue("");
		txtVOU_NAME.setValue("");
		txtVOU_NO.setValue("");
		decSUMALL.setValue(BigDecimal.ZERO);
		decSUMREC.setValue(BigDecimal.ZERO);
		decSUMDIFF.setValue(BigDecimal.ZERO);
		txtACCT_ID_DIFF.setValue("");
		txtACCT_ID_DIFF.setReadonly(true);
		txtACCT_NAME_DIFF.setValue("");
		txtDOCNO.setValue("");
		tdbDOCDATE.setValue(null);
		tdbPOSTDATE.setValue(null);

		this.lst_gl_vatpur.clear();
		this.gridList1.getRows().getChildren().clear();

		this.lst_select.clear();
		show_gridList2();

	}

	public void onOK() {
		try {
			if (focusObj == decSUMBASE_AMT) {
				if (decSUMDIFF.getValue().compareTo(BigDecimal.ZERO) != 0) {
					txtACCT_ID_DIFF.focus();
				} else {
					txtDOCNO.focus();
				}
			} else if (focusObj == txtACCT_ID_DIFF) {
				if (Fnc.isEmpty(txtACCT_ID_DIFF.getValue())) {
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
			ManAcVatReceiveB.getDataQry(lst_gl_vatpur, txtVOU_TYPE.getValue(), txtVOU_NO.getValue(),
					FnDate.getSqlDate(tdbPOSTDATE_FROM.getValue()), FnDate.getSqlDate(tdbPOSTDATE_TO.getValue()),
					txtCUST_CDE.getValue(), this.getLoginBean());
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

				BigDecimal sumrec = decSUMREC.getValue();// ยอดรับภาษีซื้อจริง
				BigDecimal sumbase_amt = decSUMBASE_AMT.getValue();// มูลค่าสินค้า/บริการสุทธิ
				BigDecimal sumdiff = decSUMDIFF.getValue();// ผลต่าง
				String acct_id_diff = txtACCT_ID_DIFF.getValue();// รหัสบัญชีส่วนต่าง
				String docno = txtDOCNO.getValue().trim();// เลขที่เอกสาร
				java.sql.Date docdate = FnDate.getSqlDate(tdbDOCDATE.getValue());// วันที่เอกสาร
				java.sql.Date postdate = FnDate.getSqlDate(tdbPOSTDATE.getValue());// วันที่ใบสำคัญ

				ManAcVatReceiveB.saveData(dbc, sumrec, sumbase_amt, sumdiff, acct_id_diff, docno, docdate, postdate,
						this.lst_select, vou_type, vou_no, this.getLoginBean());

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

		if (decSUMALL.getValue().compareTo(BigDecimal.ZERO) != 0) { // ยอดรวม != 0 ,เพราะบางกรณียอดรวมอาจเป็น 0
																	// ทำแค่จับรายการมา offset กันเฉยๆ

			if (decSUMALL.getValue().compareTo(BigDecimal.ZERO) > 0) { // ยอดรวม > 0

				if (decSUMREC.getValue().compareTo(BigDecimal.ZERO) < 0) {
					throw new Exception(decSUMREC.getTooltiptext() + " ห้ามติดลบ");
				}
				if (decSUMBASE_AMT.getValue().compareTo(BigDecimal.ZERO) < 0) {
					throw new Exception(decSUMBASE_AMT.getTooltiptext() + " ห้ามติดลบ");
				}

			} else {

				if (decSUMREC.getValue().compareTo(BigDecimal.ZERO) > 0) {
					throw new Exception(decSUMREC.getTooltiptext() + " ต้องเป็นค่าลบ");
				}
				if (decSUMBASE_AMT.getValue().compareTo(BigDecimal.ZERO) > 0) {
					throw new Exception(decSUMBASE_AMT.getTooltiptext() + " ต้องเป็นค่าลบ");
				}

			}

			if (decSUMDIFF.getValue().compareTo(BigDecimal.ZERO) != 0) {

				if (Fnc.isEmpty(txtACCT_ID_DIFF.getValue())) {
					throw new Exception(txtACCT_ID_DIFF.getTooltiptext() + " ยังไม่ระบุ");
				}
				if (!read_acctid(txtACCT_ID_DIFF.getValue())) {
					throw new Exception(txtACCT_ID_DIFF.getTooltiptext() + " ไม่ถูกต้อง");
				}

				// == ถ้าระบุรหัสบัญชีส่วนต่างต้องเป็นรหัสที่ไม่มีตัวย่อย
				// เพื่อเวลาผิดจะได้ไม่ถอยเยอะ
				TboACGL_DETAIL detail = new TboACGL_DETAIL();
				detail.setACCT_ID(txtACCT_ID_DIFF.getValue().trim());
				// 1 = ตั้งมากกว่ายอดภาษีจริงในใบกำกับภาษี ลงขาเดบิต
				// ปกติส่วนต่างถือเป็นค่าใช้จ่ายหรือสินทรัพย์
				// -1 = ตั้งยอดน้อยกว่ายอดภาษีจริงที่ได้รับ ลงขาเครดิต ปกติส่วนต่างถือเป็นรายได้
				detail.setNUM_TYPE(decSUMDIFF.getValue().compareTo(BigDecimal.ZERO) > 0 ? BigDecimal.ONE
						: BigDecimal.ONE.negate());
				if (ManAcEntr.isSUB_HAS(dbc, detail, this.getLoginBean())) {
					throw new Exception(txtACCT_ID_DIFF.getTooltiptext() + " ห้ามเป็นรหัสบัญชีที่มีตัวย่อย");
				}

			}

			if (Fnc.isEmpty(txtDOCNO.getValue())) {
				throw new Exception(txtDOCNO.getTooltiptext() + " ยังไม่ระบุ");
			}

			if (tdbDOCDATE.getValue() == null) {
				throw new Exception(tdbDOCDATE.getTooltiptext() + " ยังไม่ระบุ");
			}

		}

		// ==วันที่ใบสำคัญต้อง >= postdate ของรายการที่เลือก
		int count = 0;
		for (FModelHasMap dat : this.lst_select) {
			count++;
			MyDecimalbox decRCV_AMT = (MyDecimalbox) dat.get("decRCV_AMT");
			if (decRCV_AMT.getValue().compareTo(BigDecimal.ZERO) == 0) {
				throw new Exception("ยังไม่ระบุยอด ลำดับที่ " + count);
			}

			if (dat.getBigDecimal("AMT").compareTo(BigDecimal.ZERO) > 0) {// ยอดเป็นบวกตัวรับห้ามติดลบ
				if (decRCV_AMT.getValue().compareTo(BigDecimal.ZERO) < 0) {
					throw new Exception("ยังไม่ระบุยอด ลำดับที่ " + count);
				}
			} else {
				if (decRCV_AMT.getValue().compareTo(BigDecimal.ZERO) > 0) {
					throw new Exception("ยังไม่ระบุยอด ลำดับที่ " + count);
				}
			}

			if (decRCV_AMT.getValue().abs().compareTo(dat.getBigDecimal("AMT").abs()) > 0) {
				throw new Exception("ยอดรับมากกว่ายอดคงเหลือ ลำดับที่ " + count);
			}
			// ถ้าวันที่ตั้งมากกว่าวันที่ล้าง
			if (FnDate.compareDate(dat.getDate("POSTDATE"), FnDate.getSqlDate(tdbPOSTDATE.getValue())) > 0) {
				throw new Exception("วันที่ขาตั้งมากกว่าวันที่ล้างยอด ลำดับที่ " + count);
			}
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
			if (comp.getId().equals("txtCUST_CDE")) {
				read_cust(txtCUST_CDE.getValue());
			} else if (comp.getId().equals("txtVOU_TYPE")) {
				if (!read_vou_type(txtVOU_TYPE.getValue())) {
					txtVOU_NAME.setValue("");
				}
			} else if (comp.getId().equals("txtACCT_ID_DIFF")) {
				if (!read_acctid(txtACCT_ID_DIFF.getValue())) {
					txtACCT_NAME_DIFF.setValue("");
				}
			} else if (comp.getId().equals("decSUMREC")) {
				calDiff();
			}

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

	public void popupACCTID() {
		if (!txtACCT_ID_DIFF.isReadonly()) {
			FfACCT_NO.popup("", this.getLoginBean(), this, "doPopupACCTID");
		}
	}

	public void doPopupACCTID(String acctid) {

		try {
			if (read_acctid(acctid)) {
				txtACCT_ID_DIFF.focus();
			}
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public boolean read_acctid(String acct_id) throws SQLException, Exception {

		TboACCT_NO acct = new TboACCT_NO();
		acct.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		acct.setACCT_ID(acct_id);
		if (TbfACCT_NO.find(acct)) {
			txtACCT_ID_DIFF.setValue(acct.getACCT_ID());
			txtACCT_NAME_DIFF.setValue(acct.getACCT_NAME());
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
			row.appendChild(ZkUtil.gridTextbox(rs.getString("DOCNO")));
			row.appendChild(ZkUtil.gridTextbox(FnDate.displayDateString(rs.getDate("DOCDATE"))));

			String custName = Fnc.getStr(rs.getString("TITLE")) + " " + Fnc.getStr(rs.getString("FNAME")) + " "
					+ Fnc.getStr(rs.getString("LNAME"));
			row.appendChild(ZkUtil.gridTextbox(custName.trim()));

			row.appendChild(ZkUtil.gridTextbox(rs.getString("DESCR")));
			BigDecimal amt = rs.getBigDecimal("AMT").subtract(rs.getBigDecimal("CLEAR_AMT"))
					.multiply(rs.getBigDecimal("NUM_TYPE"));
			row.appendChild(ZkUtil.gridDecimalbox(amt));

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
				dat.setString("TITLE", sel_rs.getString("TITLE"));
				dat.setString("FNAME", sel_rs.getString("FNAME"));
				dat.setString("LNAME", sel_rs.getString("LNAME"));
				dat.setString("DOCNO", sel_rs.getString("DOCNO"));
				dat.setDate("DOCDATE", sel_rs.getDate("DOCDATE"));
				dat.setString("SECT_ID", sel_rs.getString("SECT_ID"));
				dat.setString("CUST_CDE", sel_rs.getString("CUST_CDE"));
				dat.setString("TITLE", sel_rs.getString("TITLE"));
				dat.setString("FNAME", sel_rs.getString("FNAME"));
				dat.setString("LNAME", sel_rs.getString("LNAME"));
				dat.setString("CUST_BRANCH_ID", sel_rs.getString("CUST_BRANCH_ID"));
				dat.setString("LINK_NO", sel_rs.getString("LINK_NO"));
				dat.setBigDecimal("VAT_RATE", sel_rs.getBigDecimal("VAT_RATE"));

				BigDecimal amt = sel_rs.getBigDecimal("AMT").subtract(sel_rs.getBigDecimal("CLEAR_AMT"))
						.multiply(sel_rs.getBigDecimal("NUM_TYPE"));
				dat.setBigDecimal("AMT", amt);

				dat.setString("DESCR", sel_rs.getString("DESCR"));

				// == ใส่ opject เพื่อรับคราวนี้
				MyDecimalbox decRCV_AMT = ZkUtil.gridDecimalboxEdit(amt);
				decRCV_AMT.addEventListener(Events.ON_CHANGE, (event1) -> onCHANGE_decRCV_AMT(event1));
				decRCV_AMT.addEventListener(Events.ON_OK, (event1) -> onCHANGE_decRCV_AMT(event1));
				dat.put("decRCV_AMT", decRCV_AMT);

				this.lst_select.add(dat);
				Button btn_sel = (Button) event.getTarget();
				btn_sel.setDisabled(true);
				show_gridList2();

			}

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void onCHANGE_decRCV_AMT(org.zkoss.zk.ui.event.Event event) {
		sum_select();
	}

	public RowRenderer getRowRendererSel() {

		return (Row row, Object dat, int index) -> {

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
			row.appendChild(ZkUtil.gridTextbox(rs.getString("DOCNO")));
			row.appendChild(ZkUtil.gridTextbox(FnDate.displayDateString(rs.getDate("DOCDATE"))));

			String custName = Fnc.getStr(rs.getString("TITLE")) + " " + Fnc.getStr(rs.getString("FNAME")) + " "
					+ Fnc.getStr(rs.getString("LNAME"));
			row.appendChild(ZkUtil.gridTextbox(custName.trim()));

			row.appendChild(ZkUtil.gridTextbox(rs.getString("DESCR")));
			row.appendChild(ZkUtil.gridDecimalbox(rs.getBigDecimal("AMT")));
			row.appendChild((MyDecimalbox) rs.get("decRCV_AMT"));

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

	public void show_gridList2() {

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

	private void sum_select() {
		// == sum amt
		BigDecimal sSUMALL = BigDecimal.ZERO;// decRCV_AMT
		for (FModelHasMap row : this.lst_select) {
			MyDecimalbox decRCV_AMT = (MyDecimalbox) row.get("decRCV_AMT");
			sSUMALL = sSUMALL.add(decRCV_AMT.getValue());
		}
		decSUMALL.setValue(sSUMALL);// ยอดรวม
		decSUMREC.setValue(sSUMALL);// ยอดรับภาษีซื้อจริง
		decSUMDIFF.setValue(BigDecimal.ZERO);// ส่วนต่าง
		calDiff();
	}

	public void calDiff() {
		if (decSUMALL.getValue().compareTo(BigDecimal.ZERO) != 0) {
			BigDecimal diff = decSUMALL.getValue().subtract(decSUMREC.getValue());
			decSUMDIFF.setValue(diff);
		} else {
			decSUMDIFF.setValue(BigDecimal.ZERO);
		}
		if (decSUMDIFF.getValue().compareTo(BigDecimal.ZERO) != 0) {
			txtACCT_ID_DIFF.setReadonly(false);
		} else {
			txtACCT_ID_DIFF.setReadonly(true);
			txtACCT_ID_DIFF.setValue("");
			txtACCT_NAME_DIFF.setValue("");
		}
	}

}

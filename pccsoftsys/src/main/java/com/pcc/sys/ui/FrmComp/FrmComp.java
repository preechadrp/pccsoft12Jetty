package com.pcc.sys.ui.FrmComp;

import java.util.ArrayList;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;

import com.pcc.sys.find.FfFCOMP;
import com.pcc.sys.lib.FDbH2;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.SqlStr;
import com.pcc.sys.lib.ZkUtil;
import com.pcc.sys.tbf.TbfFCOMP;
import com.pcc.sys.tbf.TbfFCOMPBRANC;
import com.pcc.sys.tbm.TbmFCOMPBRANC;
import com.pcc.sys.tbo.TboFCOMP;
import com.pcc.sys.tbo.TboFCOMPBRANC;

public class FrmComp extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Textbox txtCOMP_CDE;
	public Textbox txtCOMP_NAME;
	public Textbox txtCOMP_SHORTNAME;
	public Textbox txtTAXNO;
	public Datebox tdbOPENCOMP;
	public Intbox intGLMONTH;
	public Intbox intGLDAY;

	public Div div1;
	public Grid grid2;

	public Button btnExit, btnBack, btnSave, btnDelete, btnAdd;

	public java.util.List<TboFCOMPBRANC> lst_data = new ArrayList<>();

	public TboFCOMPBRANC recordFlag = null;

	@Override
	public void setFormObj() {
		txtCOMP_CDE = (Textbox) getFellow("txtCOMP_CDE");
		txtCOMP_NAME = (Textbox) getFellow("txtCOMP_NAME");
		txtCOMP_SHORTNAME = (Textbox) getFellow("txtCOMP_SHORTNAME");
		txtTAXNO = (Textbox) getFellow("txtTAXNO");
		tdbOPENCOMP = (Datebox) getFellow("tdbOPENCOMP");
		intGLMONTH = (Intbox) getFellow("intGLMONTH");
		intGLDAY = (Intbox) getFellow("intGLDAY");
		div1 = (Div) getFellow("div1");
		grid2 = (Grid) getFellow("grid2");

		btnExit = (Button) getFellow("btnExit");
		btnBack = (Button) getFellow("btnBack");
		btnSave = (Button) getFellow("btnSave");
		btnDelete = (Button) getFellow("btnDelete");
		btnAdd = (Button) getFellow("btnAdd");

		ZkUtil.setGridHeaderStyle(grid2);
		grid2.setRowRenderer(getRowRenderer1());
	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(txtCOMP_CDE);
		addEnterIndex(btnAdd);
		addEnterIndex(txtCOMP_NAME);
		addEnterIndex(txtCOMP_SHORTNAME);
		addEnterIndex(txtTAXNO);
		addEnterIndex(tdbOPENCOMP);
		addEnterIndex(intGLMONTH);
		addEnterIndex(intGLDAY);
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
		div1.setVisible(false);
		txtCOMP_CDE.setValue("");
		txtCOMP_CDE.setReadonly(false);
		txtCOMP_CDE.focus();
		txtCOMP_NAME.setValue("");
		txtCOMP_SHORTNAME.setValue("");
		txtTAXNO.setValue("");
		tdbOPENCOMP.setValue(null);
		intGLMONTH.setValue(0);
		intGLDAY.setValue(0);

		btnExit.setVisible(true);
		btnBack.setVisible(false);
		btnSave.setVisible(false);
		btnDelete.setVisible(false);
		btnAdd.setVisible(true);

		this.lst_data.clear();
		refreshBranchGrid();

	}

	public void onOK() {
		try {
			if (focusObj == txtCOMP_CDE) {
				if (!txtCOMP_CDE.isReadonly()) {
					if (Fnc.isEmpty(txtCOMP_CDE.getValue())) {
						super.onOK();//จะเลื่อนไปปุ่ม btnAdd ถ้า visible อยู่
					} else if (read_record(txtCOMP_CDE.getValue().trim())) {
						super.onOK();
					} else {
						popupData();
					}
				}

			} else {
				super.onOK();
			}
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void onClick_Back() {
		clearData();
	}

	private void validateData() throws Exception {

		if (txtCOMP_CDE.getValue().equals("")) {
			throw new Exception("ระบุช่อง \"" + txtCOMP_CDE.getTooltiptext() + "\" ");
		}
		if (txtCOMP_NAME.getValue().equals("")) {
			throw new Exception("ระบุช่อง \"" + txtCOMP_NAME.getTooltiptext() + "\" ");
		}
		if (txtCOMP_SHORTNAME.getValue().equals("")) {
			throw new Exception("ระบุช่อง \"" + txtCOMP_SHORTNAME.getTooltiptext() + "\" ");
		}
		if (txtTAXNO.getValue().equals("")) {
			throw new Exception("ระบุช่อง \"" + txtTAXNO.getTooltiptext() + "\" ");
		}
		if (tdbOPENCOMP.getValue() == null) {
			throw new Exception("ระบุช่อง \"" + tdbOPENCOMP.getTooltiptext() + "\" ");
		}

		if (Fnc.getIntValue(intGLMONTH.getValue()) < 1 || Fnc.getIntValue(intGLMONTH.getValue()) > 12) {
			throw new Exception("ระบุช่อง \"" + intGLMONTH.getTooltiptext() + "\" ไม่ถูกต้อง");
		}
		if (Fnc.getIntValue(intGLDAY.getValue()) < 1 || Fnc.getIntValue(intGLDAY.getValue()) > 31) {
			throw new Exception("ระบุช่อง \"" + intGLDAY.getTooltiptext() + "\" ไม่ถูกต้อง");
		}

		if (Fnc.getIntValue(intGLMONTH.getValue()) == 4
				|| Fnc.getIntValue(intGLMONTH.getValue()) == 6
				|| Fnc.getIntValue(intGLMONTH.getValue()) == 9
				|| Fnc.getIntValue(intGLMONTH.getValue()) == 11) {

			if (Fnc.getIntValue(intGLDAY.getValue()) > 30) {
				throw new Exception("ระบุช่อง \"" + intGLDAY.getTooltiptext() + "\" ห้ามเกิน 30");
			}

		} else if (Fnc.getIntValue(intGLMONTH.getValue()) == 2) {

			if (Fnc.getIntValue(intGLDAY.getValue()) > 29) {
				throw new Exception("ระบุช่อง \"" + intGLDAY.getTooltiptext() + "\" ห้ามเกิน 29");
			}

		} else {

			if (Fnc.getIntValue(intGLDAY.getValue()) > 31) {
				throw new Exception("ระบุช่อง \"" + intGLDAY.getTooltiptext() + "\" ห้ามเกิน 31");
			}

		}

		if (this.lst_data.size() == 0) {
			throw new Exception("กรุณาเพิ่มสาขาอย่างน้อย 1 สาขา");
		}

	}

	public void onClick_Save() {

		boolean newrec = true;
		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				validateData();

				//== FUSER_Table
				TboFCOMP fcomp = new TboFCOMP();

				fcomp.setCOMP_CDE(txtCOMP_CDE.getValue());

				if (TbfFCOMP.find(dbc, fcomp)) {
					newrec = false;
				}

				fcomp.setCOMP_NAME(txtCOMP_NAME.getValue());
				fcomp.setCOMP_SHORTNAME(txtCOMP_SHORTNAME.getValue());
				fcomp.setTAXNO(txtTAXNO.getValue());
				fcomp.setOPENCOMP(FnDate.getSqlDate(tdbOPENCOMP.getValue()));
				fcomp.setGLMONTH(Fnc.getIntValue(intGLMONTH.getValue()));
				fcomp.setGLDAY(Fnc.getIntValue(intGLDAY.getValue()));
				//==
				fcomp.setUPBY(this.getLoginBean().getUSER_ID());
				fcomp.setUPDT(FnDate.getTodaySqlDateTime());

				if (newrec) {

					fcomp.setINSBY(this.getLoginBean().getTboFuser().getUSER_ID());
					fcomp.setINSDT(FnDate.getTodaySqlDateTime());
					if (!TbfFCOMP.insert(dbc, fcomp)) {
						throw new Exception("เพิ่มไม่ได้");
					}

				} else {
					if (!TbfFCOMP.update(dbc, fcomp, "")) {
						throw new Exception("ปรับปรุงไม่ได้");
					}
				}

				//del old record
				TbmFCOMPBRANC.delAllBranc(dbc, txtCOMP_CDE.getValue());
				//loop save branch
				for (TboFCOMPBRANC rec1 : this.lst_data) {
					TbfFCOMPBRANC.insert(dbc, rec1);
				}

				dbc.commit();
			}

			clearData();

			Msg.info("บันทึกเรียบร้อย");
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void onClick_Delete() {
		Msg.confirm2(Labels.getLabel("comm.label.deleteComfirm") + " ?", this, "doOnClick_Delete");
	}

	public void doOnClick_Delete() {
		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				TboFCOMP comp = new TboFCOMP();
				comp.setCOMP_CDE(txtCOMP_CDE.getValue().trim());
				if (!TbfFCOMP.delete(dbc, comp)) {
					throw new Exception("ไม่สามารถลบข้อมูลได้");
				}
				//del old record
				TbmFCOMPBRANC.delAllBranc(dbc, txtCOMP_CDE.getValue());

				dbc.commit();
			}

			clearData();
			Msg.info("ลบข้อมูลเรียบร้อยแล้ว");
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void onClick_btnAdd() {
		Msg.inputbox("ใส่รหัสใหม่", 200, txtCOMP_CDE.getMaxlength(), this, "doOnClick_btnAdd", null);
	}

	public void doOnClick_btnAdd(String comp_cde) {
		try {
			System.out.println(comp_cde);
			if (!read_record(comp_cde)) {
				btnExit.setVisible(false);
				btnBack.setVisible(true);
				btnSave.setVisible(true);
				btnDelete.setVisible(false);
				btnAdd.setVisible(false);
				div1.setVisible(true);

				txtCOMP_CDE.setReadonly(true);
				txtCOMP_CDE.setValue(comp_cde);
				txtCOMP_NAME.focus();
			}
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void popupData() {
		if (!txtCOMP_CDE.isReadonly()) {
			FfFCOMP.popup("", "", this.getLoginBean(), this, "doPopupData");
		}
	}

	public void doPopupData(String comp_cde) {
		try {
			if (!read_record(comp_cde)) {
				txtCOMP_CDE.focus();
			}
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	private boolean read_record(String comp_cde) throws Exception {
		boolean res = false;

		TboFCOMP fcomp = new TboFCOMP();
		fcomp.setCOMP_CDE(comp_cde);

		if (TbfFCOMP.find(fcomp)) {

			txtCOMP_CDE.setValue(fcomp.getCOMP_CDE());
			txtCOMP_NAME.setValue(fcomp.getCOMP_NAME()); // ป้องกันตัวเล็กใหญ่
			txtCOMP_SHORTNAME.setValue(fcomp.getCOMP_SHORTNAME());
			txtTAXNO.setValue(fcomp.getTAXNO());
			tdbOPENCOMP.setValue(fcomp.getOPENCOMP());
			intGLMONTH.setValue(fcomp.getGLMONTH());
			intGLDAY.setValue(fcomp.getGLDAY());

			btnExit.setVisible(false);
			btnBack.setVisible(true);
			btnSave.setVisible(true);
			btnDelete.setVisible(true);
			btnAdd.setVisible(false);
			txtCOMP_CDE.setReadonly(true);
			div1.setVisible(true);

			TbmFCOMPBRANC.getBranc(txtCOMP_CDE.getValue(), this.lst_data);
			refreshBranchGrid();

			res = true;
		}

		return res;
	}

	public void onClick_btnAddBranch() {

		try {
			manageBranch(false, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void manageBranch(boolean editmode, int branc_cde) {

		try {
			java.io.InputStream inputStream = FrmComp.class.getResourceAsStream(
					"/com/pcc/sys/zul/FrmComp/FrmCompBranch.zul");
			FrmCompBranch win1 = (FrmCompBranch) Executions.createComponentsDirectly(
					new java.io.InputStreamReader(inputStream, "UTF-8"), "zul", null, null);
			win1.setPosition("top,center");
			win1.setLoginBean(this.getLoginBean());
			win1.COMP_CDE = txtCOMP_CDE.getValue();
			win1.callBackMethodName = "refreshBranchGrid";
			win1.callBackClass = this;
			win1.lst_data = this.lst_data;

			if (editmode == true) {
				win1.editMode = true;
				win1.BRANC_CDE = branc_cde;
			} else {
				win1.editMode = false;
			}

			win1.doModal();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void refreshBranchGrid() {
		System.out.println("refreshBranchGrid()");
		SimpleListModel rstModel = new SimpleListModel<TboFCOMPBRANC>(this.lst_data);
		this.grid2.setModel(rstModel);
	}

	public RowRenderer<Object> getRowRenderer1() {

		return (row, dat, index) -> {

			TboFCOMPBRANC rs = (TboFCOMPBRANC) dat;
			row.setStyle(ZkUtil.styleFindLookUp);

			row.appendChild(ZkUtil.gridLabel((index + 1) + ""));

			Button btn_edit = new Button();
			btn_edit.setLabel("แก้ไข");
			btn_edit.setSclass("buttonedit");
			btn_edit.addEventListener(Events.ON_CLICK, (event) -> onClick_BtnEditRow(event));
			row.appendChild(btn_edit);

			Button btn_del = new Button();
			btn_del.setLabel("ลบ");
			btn_del.setSclass("buttondel");
			btn_del.addEventListener(Events.ON_CLICK, (event) -> onClick_BtnDelRow(event));
			row.appendChild(btn_del);

			row.appendChild(ZkUtil.gridLabel(rs.getBRANC_CDE() + ""));
			row.appendChild(ZkUtil.gridLabel(rs.getBRANC_NAME()));
			row.appendChild(ZkUtil.gridLabel(rs.getBRANC_SHORTNAME()));
			row.appendChild(ZkUtil.gridLabel(rs.getBRANCTAXNO()));
			row.appendChild(ZkUtil.gridLabel(rs.getADDR1()));
			row.appendChild(ZkUtil.gridLabel(rs.getADDR2()));
			row.appendChild(ZkUtil.gridLabel(rs.getTELNO()));
			row.appendChild(ZkUtil.gridLabel(rs.getFAXNO()));

			//=== เพิ่ม Attribute
			row.setAttribute("data1", rs);

		};

	}

	public void onClick_BtnEditRow(org.zkoss.zk.ui.event.Event event) {
		Row row = (Row) event.getTarget().getParent();
		this.recordFlag = (TboFCOMPBRANC) row.getAttribute("data1");
		manageBranch(true, this.recordFlag.getBRANC_CDE());
	}

	public void onClick_BtnDelRow(org.zkoss.zk.ui.event.Event event) {
		try {
			Row row = (Row) event.getTarget().getParent();
			this.recordFlag = (TboFCOMPBRANC) row.getAttribute("data1");
			if (this.recordFlag.getBRANC_CDE() == 1) {
				throw new Exception("รหัสนี้สงวนลบไม่ได้");
			} else {
				Msg.confirm2(Labels.getLabel("comm.label.deleteComfirm") + " ?", this, "doOnClick_BtnDelRow");
			}
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void doOnClick_BtnDelRow() {
		try {
			for (TboFCOMPBRANC rec1 : this.lst_data) {
				if (rec1 == this.recordFlag) {
					this.lst_data.remove(rec1);
					break;
				}
			}
			refreshBranchGrid();
		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}
	}

	public void testH2Database() {
		try {
			//test h2 database  test ok 31/8/2566
			try (FDbH2 dbM = FDbH2.connect()) {

				//create table
				SqlStr sql = new SqlStr();
				sql.addLine("CREATE TABLE NEWTABLE (");
				sql.addLine("    FCODE INTEGER NOT NULL,");
				sql.addLine("    FNAME VARCHAR(100),");
				sql.addLine("    CONSTRAINT NEWTABLE_PK PRIMARY KEY (FCODE)");
				sql.addLine(")");
				dbM.executeSql(sql.getSql());

				//insert table
				String sql2 = " INSERT INTO PUBLIC.NEWTABLE (FCODE,FNAME) VALUES (1,'F1')";
				dbM.executeSql(sql2);

				String sql3 = " INSERT INTO PUBLIC.NEWTABLE (FCODE,FNAME) VALUES (2,'F2')";
				dbM.executeSql(sql3);

				//test insert แบบ  resultset
				java.sql.ResultSet rsU = dbM.getResultSetCRUD("select * from NEWTABLE where 1=0");
				rsU.moveToInsertRow();
				rsU.updateInt("FCODE", 3);
				rsU.updateString("FNAME", "F3");
				//rsU.getString("FNAME"); //จะเกิด Error ถ้ายังไม่ rsU.insertRow(); แต่ถ้าอยู่ใน mode update จะได้ค่าเก่าไม่ใช่ค่าที่ update เข้าไปล่าสุดยกเว้น  rsU.updateRow(); แล้ว
				rsU.insertRow();//save ลงฐานข้อมูลจริงๆ

				try (java.sql.ResultSet rs1 = dbM.getResultSet("select * from NEWTABLE");) {
					while (rs1.next()) {
						System.out.println(rs1.getInt("FCODE") + " , " + rs1.getString("FNAME"));
					}
				}

				//test update
				try (java.sql.ResultSet rs1 = dbM.getResultSetCRUD("select * from NEWTABLE where FCODE=2 ");) {
					if (rs1.next()) {
						rs1.updateString("FNAME", "F2edit");
						rs1.updateRow();
					}
				}

				try (java.sql.ResultSet rs1 = dbM.getResultSet("select * from NEWTABLE");) {
					while (rs1.next()) {
						System.out.println(rs1.getInt("FCODE") + " , " + rs1.getString("FNAME"));
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

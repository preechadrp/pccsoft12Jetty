package com.pcc.sys.ui.FrmLoginUser;

import java.sql.SQLException;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;

import com.pcc.sys.beans.UserMenuCompBeans;
import com.pcc.sys.find.FfFCOMP;
import com.pcc.sys.find.FindComm;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.ZkUtil;
import com.pcc.sys.progman.ManFMainMn;
import com.pcc.sys.tbf.TbfFCOMP;
import com.pcc.sys.tbf.TbfFMENU_GROUP_H;
import com.pcc.sys.tbf.TbfFUSER;
import com.pcc.sys.tbf.TbfFUSER_MENU;
import com.pcc.sys.tbm.TbmFUSER_MENU;
import com.pcc.sys.tbo.TboFCOMP;
import com.pcc.sys.tbo.TboFMENU_GROUP_H;
import com.pcc.sys.tbo.TboFUSER;
import com.pcc.sys.tbo.TboFUSER_MENU;

public class FrmLoginUser extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Textbox txtUSER_ID;
	public Textbox txtUSERPASS;
	public Textbox txtTITLE;
	public Textbox txtFNAME_THAI;
	public Textbox txtLNAME_THAI;
	public Checkbox chkRightUser, chkRightUsermenu, chkStatus;
	public Textbox txtCOMP_CDE;
	public Textbox txtCOMP_NAME;
	public Textbox txtUSER_MENU_GROUP;
	public Textbox txtUSER_MENU_GROUP_NAME;
	public Grid grid01;
	public Div div1;

	public Button btnSetPassword;
	public Button btnExit, btnBack, btnSave, btnDelete, btnAdd, btnAddGroup;

	java.util.List<UserMenuCompBeans> list_fuser_menu = new java.util.ArrayList<>();

	@Override
	public void setFormObj() {
		if (!this.isDoSetFormObject()) {
			this.setDoSetFormObject(true);

			txtUSER_ID = (Textbox) getFellow("txtUSER_ID");
			txtUSERPASS = (Textbox) getFellow("txtUSERPASS");
			txtTITLE = (Textbox) getFellow("txtTITLE");
			txtFNAME_THAI = (Textbox) getFellow("txtFNAME_THAI");
			txtLNAME_THAI = (Textbox) getFellow("txtLNAME_THAI");
			chkRightUser = (Checkbox) getFellow("chkRightUser");
			chkRightUsermenu = (Checkbox) getFellow("chkRightUsermenu");
			chkStatus = (Checkbox) getFellow("chkStatus");

			txtCOMP_CDE = (Textbox) getFellow("txtCOMP_CDE");
			txtCOMP_NAME = (Textbox) getFellow("txtCOMP_NAME");
			txtUSER_MENU_GROUP = (Textbox) getFellow("txtUSER_MENU_GROUP");
			txtUSER_MENU_GROUP_NAME = (Textbox) getFellow("txtUSER_MENU_GROUP_NAME");

			grid01 = (Grid) getFellow("grid01");
			div1 = (Div) getFellow("div1");

			btnSetPassword = (Button) getFellow("btnSetPassword");
			btnAddGroup = (Button) getFellow("btnAddGroup");

			btnExit = (Button) getFellow("btnExit");
			btnBack = (Button) getFellow("btnBack");
			btnSave = (Button) getFellow("btnSave");
			btnDelete = (Button) getFellow("btnDelete");
			btnAdd = (Button) getFellow("btnAdd");
		}
	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(txtUSER_ID);
		addEnterIndex(btnAdd);
		addEnterIndex(btnSetPassword);
		addEnterIndex(txtTITLE);
		addEnterIndex(txtFNAME_THAI);
		addEnterIndex(txtLNAME_THAI);
		addEnterIndex(chkStatus);
		addEnterIndex(txtCOMP_CDE);
		addEnterIndex(txtUSER_MENU_GROUP);
		addEnterIndex(btnAddGroup);

	}

	@Override
	public void formInit() {
		try {

			ZkUtil.setGridHeaderStyle(grid01);
			this.grid01.setRowRenderer(
					(row, dat, index) -> {

						UserMenuCompBeans rs = (UserMenuCompBeans) dat;
						int seq = index + 1;
						row.setSclass("rowGrid1");

						//== display
						row.appendChild(ZkUtil.gridLabel(seq + ""));
						row.appendChild(ZkUtil.GridButton(Labels.getLabel("comm.label.delete"), row,
								(event) -> doOnDelClickRow(event), "buttondel"));
						row.appendChild(ZkUtil.gridLabel(rs.getCOMP_CDE()));
						row.appendChild(ZkUtil.gridLabel(rs.getCOMP_NAME()));
						row.appendChild(ZkUtil.gridLabel(rs.getUSER_MENU_GROUP()));
						row.appendChild(ZkUtil.gridLabel(rs.getTHAI_NAME()));

						//== set Attribute
						row.setAttribute("COMP_CDE", rs.getCOMP_CDE());
						row.setAttribute("USER_MENU_GROUP", rs.getUSER_MENU_GROUP());

					});

			clearData();
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	private void clearData() {
		div1.setVisible(false);
		txtUSER_ID.setValue("");
		txtUSER_ID.setReadonly(false);
		txtUSER_ID.focus();
		txtUSERPASS.setValue("");
		txtTITLE.setValue("");
		txtFNAME_THAI.setValue("");
		txtLNAME_THAI.setValue("");

		chkRightUser.setChecked(false);
		chkRightUsermenu.setChecked(false);
		chkStatus.setChecked(false);
		grid01.getRows().getChildren().clear();
		list_fuser_menu.clear();

		btnExit.setVisible(true);
		btnBack.setVisible(false);
		btnSave.setVisible(false);
		btnDelete.setVisible(false);
		btnAdd.setVisible(true);
	}

	public void onOK() {
		try {
			if (focusObj == txtUSER_ID) {
				if (!txtUSER_ID.isReadonly()) {
					if (Fnc.isEmpty(txtUSER_ID.getValue())) {
						super.onOK();//จะเลื่อนไปปุ่ม btnAdd ถ้า visible อยู่
					} else if (read_record(txtUSER_ID.getValue().trim())) {
						super.onOK();
					} else {
						popupUserId();
					}
				}

			} else if (focusObj == txtCOMP_CDE) {
				if (!txtCOMP_CDE.isReadonly()) {
					if (Fnc.isEmpty(txtCOMP_CDE.getValue())) {
						popupCOMP_CDE();
					} else if (read_comp(txtCOMP_CDE.getValue().trim())) {
						super.onOK();
					} else {
						popupCOMP_CDE();
					}
				}

			} else if (focusObj == txtUSER_MENU_GROUP) {
				if (!txtUSER_MENU_GROUP.isReadonly()) {
					if (Fnc.isEmpty(txtUSER_MENU_GROUP.getValue())) {
						popupUSER_MENU_GROUP();
					} else if (read_fmenu_group(txtUSER_MENU_GROUP.getValue().trim())) {
						super.onOK();
					} else {
						popupUSER_MENU_GROUP();
					}
				}

			} else {
				super.onOK();
			}
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void popupSetPassword() {
		try {
			if (!Fnc.isEmpty(txtUSER_ID.getValue())) {
				final SetUserPassword win = (SetUserPassword) ZkUtil.callZulFile("/com/pcc/sys/zul/FrmLoginUser/SetUserPassword.zul");
				win.setCallForm(this);
				win.setMethodName("doPopupSetPassword");
				win.doModal();
			}
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void doPopupSetPassword(String password) {
		try {
			txtUSERPASS.setValue(password);
			txtTITLE.focus();
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void onClick_Back() {
		clearData();
	}

	private void validateData() throws Exception {

		if (txtUSER_ID.getValue().equals("")) {
			throw new Exception("ระบุช่อง \"" + txtUSER_ID.getTooltiptext() + "\" ");
		}
		if (txtUSERPASS.getValue().equals("")) {
			throw new Exception("ระบุช่อง \"" + txtUSERPASS.getTooltiptext() + "\" ");
		}
		if (txtTITLE.getValue().equals("")) {
			throw new Exception("ระบุช่อง \"" + txtTITLE.getTooltiptext() + "\" ");
		}
		if (txtFNAME_THAI.getValue().equals("")) {
			throw new Exception("ระบุช่อง \"" + txtFNAME_THAI.getTooltiptext() + "\" ");
		}
		//if (txtLNAME_THAI.getValue().equals("")) {
		//	throw new Exception("ระบุช่อง \"" + txtLNAME_THAI.getTooltiptext() + "\" ");
		//}

	}

	public void onClick_Save() {

		boolean newrec = true;
		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				validateData();

				//== FUSER_Table
				TboFUSER fuser = new TboFUSER();

				fuser.setUSER_ID(txtUSER_ID.getValue());

				if (TbfFUSER.find(dbc, fuser)) {
					newrec = false;
				}

				fuser.setPASSWD(txtUSERPASS.getValue());
				fuser.setTITLE(txtTITLE.getValue());
				fuser.setFNAME_THAI(txtFNAME_THAI.getValue());
				fuser.setLNAME_THAI(txtLNAME_THAI.getValue());
				fuser.setUSER_STATUS(chkStatus.isChecked() ? "1" : "0");
				fuser.setUPBY(this.getLoginBean().getUSER_ID());
				fuser.setUPDT(FnDate.getTodaySqlDateTime());
				//fuser.setEXPIRE_PASS_DATE(null);//ยังไม่ใช้
				//fuser.setUPDATE_PASS_DATE(null);ยังไม่ใช้
				//fuser.setMENU_LANG("");//ตอน Switch ภาษา
				fuser.setMAN_USER(chkRightUser.isChecked() ? "1" : "0");
				fuser.setMAN_MENU_GROUP(chkRightUsermenu.isChecked() ? "1" : "0");
				//fuser.setLAST_USE_COMP_CDE("");//ตอน Switch บริษัท
				//fuser.setLAST_USE_BRANCH_ID("");//ตอน Switch บริษัท

				if (newrec) {

					fuser.setINSBY(this.getLoginBean().getUSER_ID());
					fuser.setINSDT(FnDate.getTodaySqlDateTime());
					if (!TbfFUSER.insert(dbc, fuser)) {
						throw new Exception("เพิ่มไม่ได้");
					}

				} else {
					if (!TbfFUSER.update(dbc, fuser, "")) {
						throw new Exception("ปรับปรุงไม่ได้");
					}
				}

				//== insert FUSER_MENU
				ManFMainMn.clearOldData(dbc, fuser.getUSER_ID());

				TboFUSER_MENU fuserMenu = new TboFUSER_MENU();
				for (UserMenuCompBeans fuser_menu1 : this.list_fuser_menu) {
					fuserMenu.setUSER_ID(fuser.getUSER_ID());
					fuserMenu.setCOMP_CDE(fuser_menu1.getCOMP_CDE());
					fuserMenu.setUSER_MENU_GROUP(fuser_menu1.getUSER_MENU_GROUP());
					TbfFUSER_MENU.insert(dbc, fuserMenu);
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

		Msg.confirm(Labels.getLabel("comm.label.deleteComfirm") + " ?", "?", (event1) -> {
			if (Messagebox.Button.YES.equals(event1.getButton())) {

				try {
					try (FDbc dbc = FDbc.connectMasterDb()) {
						dbc.beginTrans();

						//== del FUSER
						TboFUSER user = new TboFUSER();
						user.setUSER_ID(txtUSER_ID.getValue().trim());
						if (!TbfFUSER.delete(dbc, user)) {
							throw new Exception("ไม่สามารถลบข้อมูลได้");
						}

						//== del FUSER_MENU
						TbmFUSER_MENU.del_from_user(dbc, txtUSER_ID.getValue());

						dbc.commit();
					}
					clearData();
					Msg.info("ลบข้อมูลเรียบร้อยแล้ว");
				} catch (Exception e) {
					Msg.error(e);
				}

			}
		});
	}

	public void popupUserId() {
		if (!txtUSER_ID.isReadonly()) {
			//tick แบบแก้ไขปัญหา ใน zk 8 กรณี set แบบ set disable-event-thread = true เพราะ zk8 ตัวนี้จะหายไป
			FindComm.selUsername2("", this.getLoginBean(), this, "doPopupUserId");
		}
	}

	public void doPopupUserId(String userid) {
		try {
			if (read_record(userid)) {
				btnSetPassword.focus();
			} else {
				txtUSER_ID.focus();
			}
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public boolean read_record(String userid) throws Exception {
		boolean res = false;

		TboFUSER fuser = new TboFUSER();
		fuser.setUSER_ID(userid);

		if (TbfFUSER.find(fuser)) {

			txtUSER_ID.setValue(fuser.getUSER_ID());
			txtUSERPASS.setValue(fuser.getPASSWD()); // ป้องกันตัวเล็กใหญ่
			txtTITLE.setValue(fuser.getTITLE());
			txtFNAME_THAI.setValue(fuser.getFNAME_THAI());
			txtLNAME_THAI.setValue(fuser.getLNAME_THAI());
			chkRightUser.setChecked(Fnc.getStr(fuser.getMAN_USER()).equals("1"));
			chkRightUsermenu.setChecked(Fnc.getStr(fuser.getMAN_MENU_GROUP()).equals("1"));
			chkStatus.setChecked(Fnc.getStr(fuser.getUSER_STATUS()).equals("1"));

			btnDelete.setVisible(true);

			TbmFUSER_MENU.get_from_user(list_fuser_menu, fuser.getUSER_ID());
			showFUSER_MENU_GROUP();

			btnExit.setVisible(false);
			btnBack.setVisible(true);
			btnSave.setVisible(true);
			btnAdd.setVisible(false);
			txtUSER_ID.setReadonly(true);

			div1.setVisible(true);

			res = true;
		}

		return res;
	}

	private void showFUSER_MENU_GROUP() throws SQLException, Exception {

		this.grid01.getRows().getChildren().clear();
		this.grid01.setModel(new SimpleListModel(this.list_fuser_menu));//ถ้าใช้ beans จึงจะ sort จาก grid ได้ใช้ hasmap ทำไมได้

	}

	private void doOnDelClickRow(Event event) {

		Msg.confirm(Labels.getLabel("comm.label.deleteComfirm") + " ?", "?", (event1) -> {
			if (Messagebox.Button.YES.equals(event1.getButton())) {

				try {
					Row row = (Row) event.getTarget().getParent();

					String sCOMP_CDE = Fnc.getStr(row.getAttribute("COMP_CDE"));
					String sUSER_MENU_GROUP = Fnc.getStr(row.getAttribute("USER_MENU_GROUP"));

					for (UserMenuCompBeans fuser_menu : list_fuser_menu) {
						if (fuser_menu.getCOMP_CDE().equals(sCOMP_CDE) &&
								fuser_menu.getUSER_MENU_GROUP().equals(sUSER_MENU_GROUP)) {
							list_fuser_menu.remove(fuser_menu);
							break;
						}
					}
					showFUSER_MENU_GROUP();
				} catch (Exception e) {
					Msg.error(e);
				}

			}
		});

	}

	public void onClick_btnAdd() {
		Msg.inputbox("ใส่ชื่อ Login ใหม่", 200, txtUSER_ID.getMaxlength(), this, "doOnClick_btnAdd", null);
	}

	public void doOnClick_btnAdd(String user_id) {
		try {
			System.out.println(user_id);
			if (!read_record(user_id)) {
				btnExit.setVisible(false);
				btnBack.setVisible(true);
				btnSave.setVisible(true);
				btnDelete.setVisible(false);
				btnAdd.setVisible(false);
				div1.setVisible(true);

				txtUSER_ID.setReadonly(true);
				txtUSER_ID.setValue(user_id);
				btnSetPassword.focus();
			}
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void onClick_btnAddGroup() {

		try {

			String sCOMP_CDE = txtCOMP_CDE.getValue();
			String sUSER_MENU_GROUP = txtUSER_MENU_GROUP.getValue();
			if (Fnc.isEmpty(sCOMP_CDE) || Fnc.isEmpty(sUSER_MENU_GROUP)) {
				throw new Exception("ระบุข้อมูลไม่ครบ");
			}

			if (!checkDub(sCOMP_CDE, sUSER_MENU_GROUP)) { //ถ้าไม่ซ้ำ

				UserMenuCompBeans dat = new UserMenuCompBeans();

				dat.setCOMP_CDE(sCOMP_CDE);

				TboFCOMP comp = new TboFCOMP();
				comp.setCOMP_CDE(sCOMP_CDE);
				if (TbfFCOMP.find(comp)) {
					dat.setCOMP_NAME(comp.getCOMP_NAME());
				} else {
					throw new Exception("รหัสบริษัทไม่ถูกต้อง");
				}

				dat.setUSER_MENU_GROUP(sUSER_MENU_GROUP);

				TboFMENU_GROUP_H fmenu_group_h = new TboFMENU_GROUP_H();
				fmenu_group_h.setUSER_MENU_GROUP(sUSER_MENU_GROUP);
				if (TbfFMENU_GROUP_H.find(fmenu_group_h)) {
					dat.setTHAI_NAME(fmenu_group_h.getTHAI_NAME());
				} else {
					throw new Exception("รหัสกลุ่มสิทธิ์ไม่ถูกต้อง");
				}

				list_fuser_menu.add(dat);

				txtCOMP_CDE.focus();
				txtCOMP_CDE.setValue("");
				txtCOMP_NAME.setValue("");
				txtUSER_MENU_GROUP.setValue("");
				txtUSER_MENU_GROUP_NAME.setValue("");

				showFUSER_MENU_GROUP();

			}

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	/**
	 * ตรวจรายการซ้ำ
	 * @param sCOMP_CDE
	 * @param sUSER_MENU_GROUP
	 * @return
	 * @throws Exception
	 */
	private boolean checkDub(String sCOMP_CDE, String sUSER_MENU_GROUP) throws Exception {

		for (UserMenuCompBeans fuser_menu : this.list_fuser_menu) {

			if (fuser_menu.getCOMP_CDE().equals(sCOMP_CDE) &&
					fuser_menu.getUSER_MENU_GROUP().equals(sUSER_MENU_GROUP)) {
				this.list_fuser_menu.remove(fuser_menu);
				return true;
			}

		}

		return false;

	}

	public void popupCOMP_CDE() {
		if (!txtCOMP_CDE.isReadonly()) {
			FfFCOMP.popup("", "", this.getLoginBean(), this, "doPopupCOMP_CDE");
		}
	}

	public void doPopupCOMP_CDE(String comp_cde) {

		try {
			if (read_comp(comp_cde)) {
				txtCOMP_CDE.focus();
			} else {
				txtCOMP_NAME.setValue("");
			}
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void popupUSER_MENU_GROUP() {
		if (!txtUSER_MENU_GROUP.isReadonly()) {
			FindComm.selUserGroup(txtUSER_MENU_GROUP.getValue(), this.getLoginBean(), this, "doPopupUSER_MENU_GROUP");
		}
	}

	public void doPopupUSER_MENU_GROUP(String user_menu_group) {

		try {
			if (read_fmenu_group(user_menu_group)) {
				txtUSER_MENU_GROUP.focus();
			} else {
				txtUSER_MENU_GROUP_NAME.setValue("");
			}
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void onChange_txtCOMP_CDE() {

		try {
			if (!read_comp(txtCOMP_CDE.getValue())) {
				txtCOMP_NAME.setValue("");
			}
		} catch (Exception e) {
		}

	}

	public void onChange_txtUSER_MENU_GROUP() {

		try {
			if (!read_fmenu_group(txtUSER_MENU_GROUP.getValue())) {
				txtUSER_MENU_GROUP_NAME.setValue("");
			}
		} catch (Exception e) {
		}

	}

	private boolean read_comp(String comp_cde) throws SQLException, Exception {
		TboFCOMP comp = new TboFCOMP();
		comp.setCOMP_CDE(comp_cde);
		if (TbfFCOMP.find(comp)) {
			txtCOMP_CDE.setValue(comp.getCOMP_CDE());
			txtCOMP_NAME.setValue(comp.getCOMP_NAME());
			return true;
		} else {
			return false;
		}
	}

	private boolean read_fmenu_group(String user_menu_group) throws SQLException, Exception {
		TboFMENU_GROUP_H fmenu_group_h = new TboFMENU_GROUP_H();
		fmenu_group_h.setUSER_MENU_GROUP(user_menu_group);
		if (TbfFMENU_GROUP_H.find(fmenu_group_h)) {
			txtUSER_MENU_GROUP.setValue(fmenu_group_h.getUSER_MENU_GROUP());
			txtUSER_MENU_GROUP_NAME.setValue(fmenu_group_h.getTHAI_NAME());
			return true;
		} else {
			return false;
		}
	}

}

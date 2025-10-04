package com.pcc.sys.ui.frmusergroup;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import com.pcc.sys.beans.MenuListBeans;
import com.pcc.sys.find.FindComm;
import com.pcc.sys.lib.FConstComm;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.ZkUtil;
import com.pcc.sys.progman.ManFMainMn;
import com.pcc.sys.progman.ManFrmUsergroup;
import com.pcc.sys.tbf.TbfFMENU_GROUP_D;
import com.pcc.sys.tbf.TbfFMENU_GROUP_H;
import com.pcc.sys.tbm.TbmFMENU_GROUP_D;
import com.pcc.sys.tbo.TboFMENU_GROUP_D;
import com.pcc.sys.tbo.TboFMENU_GROUP_H;

public class FrmUsergroup extends FWinMenu {

	private static final long serialVersionUID = 1L;
	public Textbox txtUSER_MENU_GROUP;
	public Textbox txtTHAI_NAME;
	public Div div1;
	public Grid gridProgList;
	public Button btnExit, btnBack, btnSave, btnDelete, btnAdd;//btnPrint, 

	@Override
	public void setFormObj() {
		txtUSER_MENU_GROUP = (Textbox) getFellow("txtUSER_MENU_GROUP");
		txtTHAI_NAME = (Textbox) getFellow("txtTHAI_NAME");
		div1 = (Div) getFellow("div1");
		gridProgList = (Grid) getFellow("gridProgList");

		btnExit = (Button) getFellow("btnExit");
		btnBack = (Button) getFellow("btnBack");
		btnSave = (Button) getFellow("btnSave");
		btnDelete = (Button) getFellow("btnDelete");
		//btnPrint = (Button) getFellow("btnPrint");
		btnAdd = (Button) getFellow("btnAdd");
	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(txtUSER_MENU_GROUP);
		addEnterIndex(btnAdd);
		addEnterIndex(txtTHAI_NAME);
	}

	@Override
	public void formInit() {
		ZkUtil.setGridHeaderStyle(gridProgList);

		clearData();
		loadProgList();

	}

	public void clearData() {
		txtUSER_MENU_GROUP.setValue("");
		txtUSER_MENU_GROUP.setReadonly(false);
		txtTHAI_NAME.setValue("");

		div1.setVisible(false);

		btnExit.setVisible(true);
		btnBack.setVisible(false);
		btnSave.setVisible(false);
		btnDelete.setVisible(false);
		//btnPrint.setVisible(false);
		btnAdd.setVisible(true);

		setCheckboxValueInGrid(false);
	}

	public void onOK() {
		try {
			if (focusObj == txtUSER_MENU_GROUP) {
				if (!txtUSER_MENU_GROUP.isReadonly()) {
					if (Fnc.isEmpty(txtUSER_MENU_GROUP.getValue())) {
						super.onOK();//จะเลื่อนไปปุ่ม btnAdd ถ้า visible อยู่
					} else if (read_record(txtUSER_MENU_GROUP.getValue().trim())) {
						super.onOK();
					} else {
						popupUserGroup();
					}
				}
			} else {
				super.onOK();
			}
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void loadProgList() {

		gridProgList.getRows().getChildren().clear();
		Integer n1 = 0;

		try {
			List<MenuListBeans> menuSubList = ManFMainMn.menuList(this.getLoginBean());
			for (MenuListBeans mn : menuSubList) {

				n1++;

				Row row1 = new Row();
				row1.appendChild(ZkUtil.gridLabel(n1 + ""));

				String menuLabel_show = mn.getThai_title();
				//if (Format.getString(_user.getLanguage()).equals("2")) {
				//	menuLabel_show = mn.getEng_title();
				//}

				if (mn.getLevel().equals("1M")) {
					Label lbl1 = ZkUtil.gridLabel(menuLabel_show);
					row1.appendChild(lbl1);

				} else if (mn.getLevel().equals("2")) {
					Checkbox chk = new Checkbox();
					chk.setLabel(menuLabel_show);
					chk.setStyle("margin-left: 40px;");
					row1.appendChild(chk);

				} else if (mn.getLevel().equals("2M")) {
					Label lbl1 = ZkUtil.gridLabel(menuLabel_show);
					lbl1.setStyle("margin-left: 40px;");
					row1.appendChild(lbl1);

				} else if (mn.getLevel().equals("3")) {
					Checkbox chk = new Checkbox();
					chk.setLabel(menuLabel_show);
					chk.setStyle("margin-left: 80px;");
					row1.appendChild(chk);

				} else if (mn.getLevel().equals("3M")) {
					Label lbl1 = ZkUtil.gridLabel(menuLabel_show);
					lbl1.setStyle("margin-left: 80px;");
					row1.appendChild(lbl1);

				} else {
					Checkbox chk = new Checkbox();
					chk.setLabel(menuLabel_show);
					chk.setStyle("margin-left: 120px;");
					row1.appendChild(chk);

				}

				row1.setAttribute("MenuListBeans", mn);
				gridProgList.getRows().appendChild(row1);

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void setCheckboxValueInGrid(boolean value) {
		java.util.List<Row> comps = gridProgList.getRows().getChildren();
		for (Row row : comps) {
			if (row.getChildren().get(1) instanceof Checkbox) {
				Checkbox chk = (Checkbox) row.getChildren().get(1);
				chk.setChecked(value);
			}
		}
	}

	public void setCheckboxMenu(String _menuid3) {

		java.util.List<Row> comps_row = gridProgList.getRows().getChildren();
		for (Row row : comps_row) {

			MenuListBeans mn = (MenuListBeans) row.getAttribute("MenuListBeans");
			if (row.getChildren().get(1) instanceof Checkbox) {
				Checkbox chk = (Checkbox) row.getChildren().get(1);
				if (mn.getMenuid3().equals(_menuid3)) {
					chk.setChecked(true);
					break;
				}
			}

		}

	}

	public void validateData() throws Exception {

		if (txtUSER_MENU_GROUP.getValue().equals("")) {
			throw new Exception("ระบุรหัส");
		}
		if (txtTHAI_NAME.getValue().equals("")) {
			throw new Exception("ระบุชื่อ");
		}

	}

	public void onClick_Back() {
		clearData();
	}

	public void onClick_Save() {

		try {
			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				validateData();

				TboFMENU_GROUP_H menu_group_h = new TboFMENU_GROUP_H();
				menu_group_h.setUSER_MENU_GROUP(txtUSER_MENU_GROUP.getValue().trim());

				boolean newrec = true;
				if (TbfFMENU_GROUP_H.find(dbc, menu_group_h)) {
					newrec = false;
				}

				menu_group_h.setTHAI_NAME(txtTHAI_NAME.getValue().trim());
				if (newrec) {
					if (!TbfFMENU_GROUP_H.insert(dbc, menu_group_h)) {
						throw new Exception("ไม่สามารถบันทึกข้อมูล");
					}
				} else {
					if (!TbfFMENU_GROUP_H.update(dbc, menu_group_h, "")) {
						throw new Exception("ไม่สามารถแก้ไขข้อมูล");
					}
				}

				// clear all in USER_MENU ก่อน
				TbmFMENU_GROUP_D.clearOldMenuGroup(dbc, menu_group_h);

				// เริ่ม insert ใหม่
				boolean has_1M = false;
				boolean has_2M = false;
				boolean has_3M = false;

				TboFMENU_GROUP_D menu_group_d = new TboFMENU_GROUP_D();
				java.util.List<Row> rows_list = gridProgList.getRows().getChildren();
				for (int n1 = rows_list.size(); n1 > 0; n1--) {

					Row row = rows_list.get(n1 - 1);

					Component com1 = row.getChildren().get(1);
					if (com1 instanceof Checkbox) {//เป็น checkbox
						Checkbox chk = (Checkbox) com1;

						if (chk.isChecked()) {

							MenuListBeans mn = (MenuListBeans) row.getAttribute("MenuListBeans");

							menu_group_d.setKEY1(Fnc.getStrRight("0000" + n1, 4));
							menu_group_d.setUSER_MENU_GROUP(menu_group_h.getUSER_MENU_GROUP());
							menu_group_d.setMENU_LEVEL(mn.getLevel());
							menu_group_d.setMODU(mn.getModuid());
							menu_group_d.setMENU_ID1(mn.getMenuid1());
							menu_group_d.setMENU_ID2(mn.getMenuid2());
							menu_group_d.setMENU_ID3(mn.getMenuid3());
							menu_group_d.setTHAI_NAME(mn.getThai_title());
							menu_group_d.setENG_NAME(mn.getEng_title());

							if (!TbfFMENU_GROUP_D.insert(dbc, menu_group_d)) {
								throw new Exception("ไม่สามารถบันทึกข้อมูล");
							}

							if (mn.getLevel().equals("2")) {
								has_1M = true;
							} else if (mn.getLevel().equals("3")) {
								has_1M = true;
								has_2M = true;
							} else if (mn.getLevel().equals("4")) {
								has_1M = true;
								has_2M = true;
								has_3M = true;
							}

						}

					} else { //เป็น Label

						boolean insert_rec = false;

						MenuListBeans mn = (MenuListBeans) row.getAttribute("MenuListBeans");
						if (mn.getLevel().equals("1M")) {
							if (has_1M) {
								insert_rec = true;
								has_1M = false;
							}
						} else if (mn.getLevel().equals("2M")) {
							if (has_2M) {
								insert_rec = true;
								has_2M = false;
							}
						} else if (mn.getLevel().equals("3M")) {
							if (has_3M) {
								insert_rec = true;
								has_3M = false;
							}
						}

						if (insert_rec) {

							menu_group_d.setKEY1(Fnc.getStrRight("0000" + n1, 4));
							menu_group_d.setUSER_MENU_GROUP(menu_group_h.getUSER_MENU_GROUP());
							menu_group_d.setMENU_LEVEL(mn.getLevel());
							menu_group_d.setMODU(mn.getModuid());
							menu_group_d.setMENU_ID1(mn.getMenuid1());
							menu_group_d.setMENU_ID2(mn.getMenuid2());
							menu_group_d.setMENU_ID3(mn.getMenuid3());
							menu_group_d.setTHAI_NAME(mn.getThai_title());
							menu_group_d.setENG_NAME(mn.getEng_title());

							if (!TbfFMENU_GROUP_D.insert(dbc, menu_group_d)) {
								throw new Exception("ไม่สามารถบันทึกข้อมูล");
							}

						}

					}

				}

				dbc.commit();
			}

			Msg.info("บันทึกเรียบร้อย");
			clearData();

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

						TboFMENU_GROUP_H fh = new TboFMENU_GROUP_H();
						fh.setUSER_MENU_GROUP(txtUSER_MENU_GROUP.getValue());
						if (TbfFMENU_GROUP_H.delete(dbc, fh)) {
							TbmFMENU_GROUP_D.clearOldMenuGroup(dbc, fh);
						} else {
							throw new Exception("ไม่สามารถลบรายการได้กรุณาตรวจสอบ");
						}

						dbc.commit();
					}

					Msg.info("ลบข้อมูลเรียบร้อยแล้ว");
					clearData();
				} catch (Exception e) {
					Msg.error(e);
				}
			}
		});

	}

	public void onClick_Print() {
		try {
			ManFrmUsergroup.printData(txtUSER_MENU_GROUP.getValue());
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void onClick_btnAdd() {
		Msg.inputbox("ใส่รหัสกลุ่มสิทธิ์ใหม่", 200, txtUSER_MENU_GROUP.getMaxlength(), this, "doOnClick_btnAdd", null);
	}

	public void doOnClick_btnAdd(String user_menu_group) {
		try {
			System.out.println(user_menu_group);
			if (!read_record(user_menu_group)) {
				btnExit.setVisible(false);
				btnBack.setVisible(true);
				btnSave.setVisible(true);
				btnDelete.setVisible(false);
				btnAdd.setVisible(false);
				div1.setVisible(true);

				txtUSER_MENU_GROUP.setReadonly(true);
				txtUSER_MENU_GROUP.setValue(user_menu_group);
				txtTHAI_NAME.focus();
			}
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void popupUserGroup() {
		if (!txtUSER_MENU_GROUP.isReadonly()) {
			FindComm.selUserGroup(txtUSER_MENU_GROUP.getValue(), this.getLoginBean(), this, "doPopupUserGroup");
		}
	}

	public void doPopupUserGroup(String user_menu_group) {
		try {
			if (user_menu_group.equals(FConstComm.AdminGroup)) {
				Msg.info("เป็นกลุ่มสงวนของระบบแก้ไขไม่ได้");
			} else {
				if (Fnc.isEmpty(user_menu_group)) {
					txtUSER_MENU_GROUP.focus();
				} else {
					read_record(user_menu_group);
				}
			}
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public boolean read_record(String userMenuGroup) throws Exception {
		boolean res = false;

		TboFMENU_GROUP_H usergroup_tb = new TboFMENU_GROUP_H();
		usergroup_tb.setUSER_MENU_GROUP(userMenuGroup);

		if (TbfFMENU_GROUP_H.find(usergroup_tb)) {
			txtUSER_MENU_GROUP.setValue(usergroup_tb.getUSER_MENU_GROUP());
			txtTHAI_NAME.setValue(usergroup_tb.getTHAI_NAME());
			btnDelete.setVisible(true);
			//btnPrint.setVisible(true);
			loadMenu(userMenuGroup);

			div1.setVisible(true);
			btnExit.setVisible(false);
			btnBack.setVisible(true);
			btnSave.setVisible(true);
			btnAdd.setVisible(false);
			txtUSER_MENU_GROUP.setReadonly(true);
			txtTHAI_NAME.focus();

			res = true;
		} else {
			setCheckboxValueInGrid(false);
		}

		return res;
	}

	public void loadMenu(String userGroup) throws Exception {

		setCheckboxValueInGrid(false);

		try {
			List<FModelHasMap> dats = new ArrayList();
			TbmFMENU_GROUP_D.getData(userGroup, dats);
			if (dats.size() > 0) {
				for (FModelHasMap dat : dats) {
					setCheckboxMenu(dat.getString("MENU_ID3"));
				}
			}

		} catch (Exception e) {
			throw e;
		}

	}

}

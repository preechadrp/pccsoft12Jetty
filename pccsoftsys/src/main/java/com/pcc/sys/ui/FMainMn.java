package com.pcc.sys.ui;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Textbox;

import com.pcc.api.core.ApiUtil;
import com.pcc.api.core.JwtTokenUtil;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.beans.MenuListBeans;
import com.pcc.sys.find.FfFCOMP;
import com.pcc.sys.lib.FCallMenu;
import com.pcc.sys.lib.FConfig;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FLogUsedTime;
import com.pcc.sys.lib.FWindow;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.ZkUtil;
import com.pcc.sys.progman.ManFMainMn;
import com.pcc.sys.tbf.TbfFCOMP;
import com.pcc.sys.tbf.TbfFUSER;
import com.pcc.sys.tbo.TboFCOMP;
import com.pcc.sys.tbo.TboFUSER;

public class FMainMn extends FWindow {

	private static final long serialVersionUID = 1L;

	public static final int infoTab = 0;//1= กรณีเขียนแบบมี info tab ,0=ถ้าไม่มี info tab
	private Menubar mainMenubar;
	//public Toolbarbutton btnUser, btnUserGroup;
	public Button btnUser, btnUserGroup;
	public Tabbox tabOpenMain;
	private Textbox txtOpenMenu;
	public Label lblComp;
	public Label lblUser;
	public Label lblDbName;
	public Label lblAppInfo;
	public LoginBean loginBean = new LoginBean();//ต้องสร้างเป็น instance ใหม่ช่วยเรื่อง multi companay กรณีเปิดหลาย tab ใน browser เดียวกัน

	private final Hashtable<Object, Object> menus = new Hashtable<Object, Object>();
	private final Hashtable<Object, Object> menupopups = new Hashtable<Object, Object>();
	private final Hashtable<Object, Object> menuitems = new Hashtable<Object, Object>();

	@Override
	public void setFormObj() {
		mainMenubar = (Menubar) getFellow("mainMenubar");
		tabOpenMain = (Tabbox) getFellow("tabOpenMain");
		btnUser = (Button) getFellow("btnUser");
		btnUserGroup = (Button) getFellow("btnUserGroup");
		txtOpenMenu = (Textbox) getFellow("txtOpenMenu");

		lblComp = (Label) getFellow("lblComp");
		lblUser = (Label) getFellow("lblUser");
		lblDbName = (Label) getFellow("lblDbName");
		lblAppInfo = (Label) getFellow("lblAppInfo");

	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(txtOpenMenu);
	}

	@Override
	public void formInit() {

		try {

			if (getLoginSession() == null) {
				Executions.sendRedirect("/login");
				return;//เพื่อให้บรรทัดถัดไปไม่ทำงาน
			}
			this.setNewLoginBean(getLoginSession());

			if (Fnc.getStr(this.loginBean.getTboFuser().getMAN_USER()).equals("1")) {
				btnUser.setVisible(true);
			} else {
				btnUser.setVisible(false);
			}
			if (Fnc.getStr(this.loginBean.getTboFuser().getMAN_MENU_GROUP()).equals("1")) {
				btnUserGroup.setVisible(true);
			} else {
				btnUserGroup.setVisible(false);
			}

			//this.setTitle(FConfig.getConfig1_AppName());
			this.lblAppInfo.setValue(FConfig.getConfig1_AppName());
			this.lblComp.setValue(this.loginBean.getCOMP_CDE() + ":" + this.loginBean.getTboFcomp().getCOMP_NAME());
			this.lblUser.setValue(this.loginBean.getTboFuser().getUSER_ID());
			this.setDbName();
			this.genMenu(this.loginBean);
			if (FConfig.getConfig2_VisibletxtOpenMenu()) {
				txtOpenMenu.setVisible(true);
			} else {
				txtOpenMenu.setVisible(false);
			}
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void setNewLoginBean(LoginBean loginBean_session) throws Exception {

		TboFUSER user_tb = new TboFUSER(); //ต้องสร้างเป็น instance ใหม่
		user_tb.setUSER_ID(loginBean_session.getUSER_ID());
		if (TbfFUSER.find(user_tb)) {

			this.loginBean.setTboFuser(user_tb);

			TboFCOMP comp = new TboFCOMP();  //ต้องสร้างเป็น instance ใหม่
			comp.setCOMP_CDE(loginBean_session.getCOMP_CDE());
			if (TbfFCOMP.find(comp)) {//มีรหัสบริษัทอยู่จริง
				this.loginBean.setTboFcomp(comp);
			}
			
			//== JWT
			var jwtUtil = new JwtTokenUtil();
			var jwt = jwtUtil.generateToken(loginBean.getTboFuser().getUSER_ID());
			this.loginBean.setJwt(jwt);
		}

	}

	public void onOK() {
		try {
			if (focusObj == txtOpenMenu) {
				if (Fnc.isEmpty(txtOpenMenu.getValue())) {
					runMenu(txtOpenMenu.getValue());
				}
			} else {
				super.onOK();
			}
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void setDbName() {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			
			String dbname = dbc.getDbName();
			String showDesc = FConfig.getConfig2("ShowDesc");
			if (!Fnc.isEmpty(showDesc)) {
				dbname = dbname + " - " + showDesc;
			}
			lblDbName.setValue(dbname);
			
		} catch (Exception e) {
			lblDbName.setValue("NO-DB");
		}
	}

	public void genMenu(LoginBean loginBean) {

		String key1 = "", key2 = "", key3 = "", key4 = "";
		java.util.List<MenuListBeans> lst_menu = new ArrayList<MenuListBeans>();

		try {

			ManFMainMn.getData(lst_menu, loginBean);
			int key_idx = 0;
			String menu_level = "";
			String menu_title = "";
			String menu_id1 = "";
			String menu_id2 = "";
			String menu_id3 = "";
			String tooltipText = "";
			String moduId = "";
			for (MenuListBeans row : lst_menu) {

				key_idx++;

				menu_level = Fnc.getStr(row.getLevel());
//				System.out.println(menu_level + ":level");
				menu_title = Fnc.getStr(row.getThai_title());
//				if (Fnc.getStr(loginBean.getFuser().getMENU_LANG()).equals("E")) {
//					menu_title = row.getEng_title();
//				}
				menu_id1 = Fnc.getStr(row.getMenuid1());
				menu_id2 = Fnc.getStr(row.getMenuid2());
				menu_id3 = Fnc.getStr(row.getMenuid3());

				moduId = row.getModuid();
				tooltipText = row.getTooltipText();
//				System.out.println(menu_link);

				if (menu_level.equals("1M")) {//level 1M

					key1 = Fnc.getStrRight("00000000" + key_idx, 8);

					Menu mn = new Menu();
					mn.setLabel(menu_title);
					mn.setParent(this.mainMenubar);
					if (!Fnc.isEmpty(tooltipText)) {
						mn.setTooltiptext(tooltipText);
					}
					menus.put(key1, mn);

					Menupopup mnp = new Menupopup();
					mnp.setParent(mn);
					menupopups.put(key1, mnp);

				} else if (menu_level.equals("2")) { //level 2

					key2 = Fnc.getStrRight("00000000" + key_idx, 8);

					Menuitem mni = new Menuitem();
					mni.setLabel(menu_title);
					if (!Fnc.isEmpty(row.getSclass())) {
						mni.setSclass(row.getSclass());
					}
					if (!Fnc.isEmpty(tooltipText)) {
						mni.setTooltiptext(tooltipText);
					}
					mni.setParent((Menupopup) menupopups.get(key1));
					mni.addEventListener("onClick", new FCallMenu(this, moduId, menu_title, menu_id1, menu_id2, menu_id3));
					menuitems.put(key2, mni);

				} else if (menu_level.equals("2M")) {//level 2M

					key2 = Fnc.getStrRight("00000000" + key_idx, 8);

					Menu mn = new Menu();
					mn.setLabel(menu_title);
					mn.setParent((Menupopup) menupopups.get(key1));
					menus.put(key2, mn);

					Menupopup mnp = new Menupopup();
					mnp.setParent(mn);
					menupopups.put(key2, mnp);

				} else if (menu_level.equals("3")) { //level 3

					key3 = Fnc.getStrRight("00000000" + key_idx, 8);

					Menuitem mni = new Menuitem();
					mni.setLabel(menu_title);
					if (!Fnc.isEmpty(row.getSclass())) {
						mni.setSclass(row.getSclass());
					}
					if (!Fnc.isEmpty(tooltipText)) {
						mni.setTooltiptext(tooltipText);
					}
					mni.setParent((Menupopup) menupopups.get(key2));
					mni.addEventListener("onClick", new FCallMenu(this, moduId, menu_title, menu_id1, menu_id2, menu_id3));
					menuitems.put(key3, mni);

				} else if (menu_level.equals("3M")) {//level 3M

					key3 = Fnc.getStrRight("00000000" + key_idx, 8);

					Menu mn = new Menu();
					mn.setLabel(menu_title);
					mn.setParent((Menupopup) menupopups.get(key2));
					menus.put(key3, mn);

					Menupopup mnp = new Menupopup();
					mnp.setParent(mn);
					menupopups.put(key3, mnp);

				} else if (menu_level.equals("4")) { //level 4

					key4 = Fnc.getStrRight("00000000" + key_idx, 8);

					Menuitem mni = new Menuitem();
					mni.setLabel(menu_title);
					if (!Fnc.isEmpty(row.getSclass())) {
						mni.setSclass(row.getSclass());
					}
					if (!Fnc.isEmpty(tooltipText)) {
						mni.setTooltiptext(tooltipText);
					}
					mni.addEventListener("onClick", new FCallMenu(this, moduId, menu_title, menu_id1, menu_id2, menu_id3));
					mni.setParent((Menupopup) menupopups.get(key3));
					menuitems.put(key4, mni);

				}

			}

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	private boolean checkOpenMenu() {
		boolean res = true;
		if (this.tabOpenMain.getTabpanels().getChildren().size() == (0 + infoTab)) {
			res = false;
		}
		return res;
	}

	public void logout() {
		if (!checkOpenMenu()) {
			ManLogin.logOut();
		} else {
			Msg.error("กรุณาปิดโปรแกรมต่างๆ ก่อน");
		}
	}

	public void menuUser() {
		runMenu("sys", "FrmLoginUser", "FrmLoginUser", "FrmLoginUser" , btnUser.getLabel(), true);
	}

	public void menuUserGroup() {
		runMenu("sys", "FrmUsergroup", "FrmUsergroup", "FrmUsergroup" , btnUserGroup.getLabel(), true);
	}

	public void menuChComp() {
		try {

			if (!checkOpenMenu()) {
				FfFCOMP.popup("", loginBean.getTboFuser().getUSER_ID(), loginBean, this, "doMenuChComp");
			} else {
				throw new Exception("กรุณาปิดโปรแกรมต่างๆ ก่อน");
			}

		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void doMenuChComp(String comp_cde) {
		try {

			if (!comp_cde.equals(loginBean.getCOMP_CDE())) {

				if (TbfFUSER.find(loginBean.getTboFuser())) {

					loginBean.getTboFuser().setLAST_USE_COMP_CDE(comp_cde);//เปลี่ยนบริษัท
					if (TbfFUSER.update(loginBean.getTboFuser(), "")) {

						loginBean.getTboFcomp().setCOMP_CDE(comp_cde);

						if (TbfFCOMP.find(loginBean.getTboFcomp())) {

							ManLogin.setLoginSession(loginBean);

							//call main menu
							Executions.sendRedirect("/menu");
						}
					}
				}

			}

		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void menuPassWord() {
		FrmChgpass win;
		try {
			win = (FrmChgpass) ZkUtil.callZulFile("/com/pcc/sys/zul/FrmChgpass.zul");
			win.setLoginBean(this.loginBean);
			win.doModal();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void runMenu(String menuId) {
		//..
	}

	/**
	 * runMenu
	 * @param modulId โมดูล
	 * @param menuId1 รหัสโปรแกรม
	 * @param menuId2 รหัสโปรแกรม
	 * @param menuId3 รหัสโปรแกรม
	 * @param menuTitle ชื่อโปรแกรม
	 * @param checkRight ตรวจสิทธิ์การใช้เมนู
	 */
	public void runMenu(String modulId, String menuId1, String menuId2, String menuId3, String menuTitle, boolean checkRight) {

		try {
			FCallMenu fcall = new FCallMenu(this, modulId, menuTitle, menuId1, menuId2, menuId3);
			fcall.setCheckRight(checkRight);
			fcall.openMenu();

		} catch (Exception e) {
			Msg.error("ไม่สามารถเรียกโปรแกรมได้");
		}

	}

	public void findApp() {
		//for test
		try {

			//FindComm.seUserCompListAdd(null, this.loginBean, this, "doFindApp");//test ok
			//FSearchData.testPopup2();//test ok  13/8/63
			//FSearchData.testPopup3();//test ok  13/8/63

			//test ok
			//LoginBean loginBean = LoginManager.getLoginSession(null);
			//FindComm.seComp(this.compid, loginBean.getTbFuser().getUSER_ID());
			//FindComm.selComp("", "");
			//FindComm.selUsername2("");
			//Msg.info("abcd");
			//Msg.error("aaa");
			//System.out.println(this);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void doFindApp(java.util.List<String> fcomp_lst) {
		for (String comp : fcomp_lst) {
			System.err.println(comp);
		}
	}

	public void test1() {
		try (FLogUsedTime log = new FLogUsedTime(this.loginBean, "mainmenu", "เมนูหลัก", "test1")) {
			//try {
			//Thread.sleep(8000);
			//Thread.sleep(4000);
			//String jwt = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTcwMDI0ODQ4MywiaWF0IjoxNzAwMjEyNDgzfQ.Q-v5mB0U-Yhm8ReG-VhykHblpgt2d1NiiRFdCcpYxOLqDt0ZJTMVYf0AjMaBZ8CeLGUzlERcuIDrAZphImi7Ug";
			ApiUtil.TestCallWebServiceVer2(this.loginBean);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * getLoginSession()
	 * @return
	 * @throws Exception
	 */
	private static LoginBean getLoginSession() throws Exception {
		Object obj = org.zkoss.zk.ui.Sessions.getCurrent().getAttribute(ManLogin.sessionLoginName);
		if (obj != null) {
			return (LoginBean) obj;
		} else {
			return null;
		}
	}

}

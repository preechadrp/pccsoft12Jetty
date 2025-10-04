package com.pcc.sys.lib;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.progman.ManFMainMn;
import com.pcc.sys.tbf.TbfFCALLMENULOG;
import com.pcc.sys.tbo.TboFCALLMENULOG;
import com.pcc.sys.ui.FMainMn;
import com.pcc.sys.ui.ManLogin;

public abstract class FWinMenu extends FWindow {

	private static final long serialVersionUID = 1L;

	private int call_mode = 0;
	private String menuId1 = "";
	private String menuId2 = "";
	private String menuId3 = "";

	/**
	 * การเรียกเมนู
	 * @return 0=เรียกแบบเพิ่มเข้า Tab ปกติ , 1 = เรียกแบบ Modal
	 */
	public int getCall_mode() {
		return this.call_mode;
	}

	/**
	 * การเรียกเมนู
	 * @param call_mode  0=เรียกแบบเพิ่มเข้า Tab ปกติ , 1 = เรียกแบบ Modal
	 */
	public void setCall_mode(int call_mode) {
		this.call_mode = call_mode;
	}

	public String getMenuId1() {
		return menuId1;
	}

	public void setMenuId1(String menuId1) {
		this.menuId1 = menuId1;
	}

	/**
	 * เรียกโดย MenuId2 ไหน เราจะส่ง USER_MENU.MENU_ID2 มาเพื่อแยกกรณี 1 โปรแกรมทำเมนูให้เลือกมากกว่า 1 ที่
	 * @return
	 */
	public String getMenuId2() {
		return menuId2;
	}

	/**
	 * ส่ง MenuId2 เข้ามาเพื่อแยกกรณี 1 โปรแกรมทำเมนูให้เลือกมากกว่า 1 ที่
	 * @param progID2
	 */
	public void setMenuId2(String menuId2) {
		this.menuId2 = menuId2;
	}

	/**
	 * ใช้จัดการเมนูสิิทธิ์การใช้งาน ห้ามซ้ำ
	 * @return
	 */
	public String getMenuId3() {
		return menuId3;
	}

	/**
	 * ใช้จัดการเมนูสิิทธิ์การใช้งาน ห้ามซ้ำ
	 * @param menuId3
	 */
	public void setMenuId3(String menuId3) {
		this.menuId3 = menuId3;
	}

	public void onCreate() {

		try {

			if (getLoginSession() == null) {
				Executions.sendRedirect("/login");
			}
			if (this.getLoginBean() == null) {
				Msg.error("ไม่ได้ set ค่า Login");
				Executions.sendRedirect("/login");
			}

			super.onCreate();

			if (this.getCall_mode() != 0) { //เรียกแบบ Popup
				this.setStyle(ZkUtil.styleCenterWindow);
				this.setTitle(getTitle() + "  [ " + this.getMenuId2() + " ]");
			}
			if (Fnc.isEmpty(this.menuId2)) {
				this.onClose();
			}
			printMenuName();
			writeCallMenuLog();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void writeCallMenuLog() throws Exception {
		TboFCALLMENULOG log = new TboFCALLMENULOG();

		log.setMENU_ID2(this.getMenuId2());
		log.setCALLTIME(FnDate.getTodaySqlDateTime());
		String menuName = ManFMainMn.getMenuThaiName(this.getMenuId2());
		if (this.getMenuId2().equals("FrmLoginUser")) {
			menuName = "จัดการผู้ใช้ระบบ";
		} else if (this.getMenuId2().equals("FrmUsergroup")) {
			menuName = "กลุ่มสิทธิ์เมนู";
		}
		log.setTHAI_NAME(menuName);
		log.setUSER_ID(this.getLoginBean().getTboFuser().getUSER_ID());

		TbfFCALLMENULOG.insert(log);
	}

	public void onClose() {

		this.setVisible(false);
		try {
			FMainMn mainmenu = (FMainMn) this.getPage().getFellow("FMainMn");
			if (this.call_mode == 0) { //เรียกแบบเพิ่ม Tab ปกติ
				removeTap(mainmenu, this.getMenuId2());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.onClose();
	}

	public static void removeTap(FMainMn mainmenu, String menuId2) {
		java.util.List<Component> comp_list = mainmenu.tabOpenMain.getTabpanels().getChildren();
		for (Component component : comp_list) {
			Tabpanel tabpanel1 = (Tabpanel) component;
			if (tabpanel1.getChildren().size() > 0) {
				Component comp = tabpanel1.getChildren().get(0);
				if (comp instanceof FWinMenu) {
					FWinMenu win1 = (FWinMenu) tabpanel1.getChildren().get(0);
					if (win1.getMenuId2().equals(menuId2)) {
						int idx = tabpanel1.getIndex();

						tabpanel1.detach();
						Tab tab1 = (Tab) mainmenu.tabOpenMain.getTabs().getChildren().get(idx);
						tab1.detach();

						int c_tab = mainmenu.tabOpenMain.getTabpanels().getChildren().size();
						if (c_tab > 0) {
							mainmenu.tabOpenMain.setSelectedIndex(c_tab - 1);
						}
						break;
					}
				}
			}
		}
	}

	public void printMenuName() {
		// ปั้มเพิ่ม - ให้มันปริ้นชื่อโปรแกรมออก console ตัดปัญหาต้องมาถามว่า Progid อะไร
		System.out.println(new java.sql.Time(System.currentTimeMillis()) + ", " + this.getMenuId2() + ", ");
	}
	
	private static LoginBean getLoginSession() throws Exception {
		Object obj = org.zkoss.zk.ui.Sessions.getCurrent().getAttribute(ManLogin.sessionLoginName);
		if (obj != null) {
			return (LoginBean) obj;
		} else {
			return null;
		}
	}

}

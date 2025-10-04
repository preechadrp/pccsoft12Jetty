package com.pcc.sys.lib;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.progman.ManFMainMn;
import com.pcc.sys.ui.FMainMn;

public class FCallMenu implements EventListener<Event> {

	private String menu_id1;
	private String menu_id2;
	private String menu_id3;
	private String zulPath;
	private FMainMn fmenu;
	private String winTittle = "";
	public boolean checkRight = true;

	public String getMenu_id1() {
		return menu_id1;
	}

	public void setMenu_id1(String menu_id1) {
		this.menu_id1 = menu_id1;
	}

	public String getMenu_id2() {
		return menu_id2;
	}

	public void setMenu_id2(String menu_id2) {
		this.menu_id2 = menu_id2;
	}
	
	public String getMenu_id3() {
		return menu_id3;
	}

	public void setMenu_id3(String menu_id3) {
		this.menu_id3 = menu_id3;
	}

	public boolean getCheckRight() {
		return checkRight;
	}

	public void setCheckRight(boolean checkRight) {
		this.checkRight = checkRight;
	}

	/**
	 * @param fmenu FMenu
	 * @param moduleId เช่น sys,ac,hp
	 * @param title เช่น "ข้อมูลผู้ใช้"
	 * @param menu_id1 
	 * @param menu_id2 
	 * @param menu_id3 
	 */
	public FCallMenu(FMainMn fmenu, String moduleId, String title, String menu_id1, String menu_id2, String menu_id3) {
		this.fmenu = fmenu;
		this.zulPath = "/com/pcc/" + moduleId + "/zul/" + menu_id1 + ".zul"; // /com/pcc/sys/zul/
		this.winTittle = title;
		this.setMenu_id1(menu_id1);
		this.setMenu_id2(menu_id2);
		this.setMenu_id3(menu_id3);
	}

	public void onEvent(Event event) {
		openMenu();
	}

	public void openMenu() {
		try {

			java.util.List<Component> comp_list = null;
			int tabSize = this.fmenu.tabOpenMain.getTabpanels().getChildren().size();
			if (tabSize > 0) {
				comp_list = this.fmenu.tabOpenMain.getTabpanels().getChildren();
			}

			if (!isOpenMenu(comp_list, this.getMenu_id2())) {

				checkPermission();//ตรวจสิทธิ์การใช้งาน

				int maxOpenMenu = 15; //จำนวนโปรแกรมที่เปิดได้สูงสุด
				if (tabSize >= (maxOpenMenu + FMainMn.infoTab)) {
					throw new Exception("ไม่อนุญาติให้เปิดโปรแกรมมากกว่า " + maxOpenMenu + " โปรแกรม");
				}

				try {

					//FWinMenu win = (FWinMenu) Executions.createComponents(this.menu_link, null, null);
					// แบบดึงจาก stream
					//FWinMenu win = (FWinMenu) FCallMenu.callZulFile("/org/zul/ac/Glvatsal.zul");
					System.out.println("this.zulPath : " + this.zulPath);
					FWinMenu win = (FWinMenu) ZkUtil.callZulFile(this.zulPath);
					win.setLoginBean(this.fmenu.loginBean);
					win.setMenuId1(this.getMenu_id1());
					win.setMenuId2(this.getMenu_id2());
					win.setMenuId3(this.getMenu_id3());
					win.setId(this.getMenu_id2());//ป้องกันการชนกัน
					win.setTitle("");//ไม่ต้องใส่แสดงใน Tab แทน
					win.setBorder("none");
					win.setWidth("100%");

					//Tab tap1 = new Tab(this.winTittle + " [" + this.getMenu_id1() + "]");
					Tab tap1 = new Tab(this.winTittle);
					tap1.setTooltiptext(this.getMenu_id1());
					this.fmenu.tabOpenMain.getTabs().appendChild(tap1);

					Tabpanel tabpanel = new Tabpanel();
					this.fmenu.tabOpenMain.getTabpanels().appendChild(tabpanel);
					this.fmenu.tabOpenMain.setSelectedIndex(tap1.getIndex());

					win.setParent(tabpanel);
					win.doEmbedded();

				} catch (Exception e) {
					e.printStackTrace();
					throw new Exception("เมนูนี้ยังพัฒนาไม่เสร็จ");
				}

			}

		} catch (Exception e) {
			Msg.error(e);
		}
	}

	/**
	 * ตรวจสิทธิ์การใช้งาน
	 * @throws Exception
	 */
	private void checkPermission() throws Exception {
		if (this.getCheckRight()) {
			LoginBean _loginBean = this.fmenu.loginBean;
			if (this.getMenu_id2().equals("FrmLoginUser")) {//จัดการผู้ใช้ระบบ
				if (!Fnc.getStr(_loginBean.getTboFuser().getMAN_USER()).equals("1")) {
					throw new Exception(Labels.getLabel("comm.label.msgNoRight"));
				}
			} else if (this.getMenu_id2().equals("FrmUsergroup")) {//กลุ่มสิทธิ์เมนู
				if (!Fnc.getStr(_loginBean.getTboFuser().getMAN_MENU_GROUP()).equals("1")) {
					throw new Exception(Labels.getLabel("comm.label.msgNoRight"));
				}
			} else {
				if (!ManFMainMn.isAdmintrator(_loginBean.getUSER_ID(), _loginBean.getCOMP_CDE())) {
					if (!ManFMainMn.can_use_menu(_loginBean.getTboFuser().getUSER_ID(), this.getMenu_id2())) {
						throw new Exception(Labels.getLabel("comm.label.msgNoRight"));
					}
				}
			}
		}
	}

	private boolean isOpenMenu(java.util.List<Component> comp_list, String menuId2) throws Exception {
		if (comp_list != null) {
			for (Component component : comp_list) {
				Tabpanel tp = (Tabpanel) component;
				if (tp.getChildren().size() > 0) {
					Component comp = tp.getChildren().get(0);
					if (comp instanceof FWinMenu) {
						FWinMenu win1 = (FWinMenu) tp.getChildren().get(0);
						if (win1.getMenuId2().equals(menuId2.trim())) {
							this.fmenu.tabOpenMain.setSelectedIndex(tp.getIndex());
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}

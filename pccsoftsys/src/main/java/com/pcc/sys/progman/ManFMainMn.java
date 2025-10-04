package com.pcc.sys.progman;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.beans.MenuListBeans;
import com.pcc.sys.lib.FConstComm;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FnFile;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.SqlStr;
import com.pcc.sys.tbo.TboFMENU_GROUP_D;
import com.pcc.sys.tbo.TboFUSER_MENU;

/**
 * 
 * @author preecha.d
 *
 */
public class ManFMainMn {

	static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
	
	/**
	 * ชื่อโปรแกรมไทย
	 * @throws Exception 
	 */
	public static String getMenuThaiName(String menuid2) throws Exception {
		String res = "";
		List<MenuListBeans> menuSubList = menuList(null);
		for (MenuListBeans m : menuSubList) {
			if (m.getMenuid2().equals(menuid2)) {
				res = m.getThai_title();
				break;
			}
		}
		return res;
	}

	/**
	 * ชื่อโปรแกรม Eng
	 * @throws Exception 
	 */
	public static String getMenuEngName(String menuid) throws Exception {
		String res = "";
		List<MenuListBeans> menuSubList = menuList(null);
		for (MenuListBeans m : menuSubList) {
			if (m.getMenuid2().equals(menuid)) {
				res = m.getEng_title();
				break;
			}
		}
		return res;
	}

	public static List<MenuListBeans> menuList(LoginBean loginBean) throws Exception {

		List<MenuListBeans> list = new java.util.ArrayList<MenuListBeans>();
		
		java.util.List<String> modules = new ArrayList<String>();
		FnFile.loadTextFileToList("/config2.txt", modules);
		String prefix = "module=";
		
		for (String mod : modules) {
			if (!Fnc.isEmpty(mod) && mod.startsWith(prefix) ) {
				String modName = mod.substring(prefix.length());
				log.debug("module Name : {}", modName);
				//if (modName.equals("bx")) {
				//	System.out.println("debug");
				//}
				loadMenu(list, "/com/pcc/" + modName.trim() + "/menuList.txt");
			}
		}

		return list;

	}

	public static void loadMenu(List<MenuListBeans> list, String menuListFile) {

		java.util.List<String> lstData = new ArrayList<String>();
		try {
			FnFile.loadTextFileToList(menuListFile, lstData);
		} catch (Exception e) {
			//..
		}

		if (lstData.size() > 0) {
			for (String lineData1 : lstData) {
				//System.out.println(lineData1); //test
				if (lineData1.trim().startsWith("-")) {

					//#-ชื่อเมนูไทย ` ชื่อเมนู Eng ถ้าว่างจะใช้ชื่อเมนูไทย ` โมดูล ` รหัสโปรแกรม ` รหัสเรียกโปรแกรม ถ้าว่างจะเท่ากับรหัสโปรแกรม ` รหัสโปรแกรมแยกเมนู(ห้ามซ้ำกัน) `css class ` ToolTipText
					String level = "";
					String lineData2 = "";//ไม่เอาเครื่องหมาย -
					if (lineData1.trim().startsWith("----")) {
						level = "4";
						lineData2 = lineData1.trim().substring(4);
					} else if (lineData1.trim().startsWith("---")) {
						level = "3";
						lineData2 = lineData1.trim().substring(3);
					} else if (lineData1.trim().startsWith("--")) {
						level = "2";
						lineData2 = lineData1.trim().substring(2);
					} else if (lineData1.trim().startsWith("-")) {
						level = "1M";
						lineData2 = lineData1.trim().substring(1);
					}
					String[] lineDataArr = lineData2.split("`");

					String key = "";
					String moduid = getStringArray(lineDataArr, 3).trim(); //โมดูล 
					String menuid1 = getStringArray(lineDataArr, 4).trim(); //รหัสเมนู
					String menuid2 = getStringArray(lineDataArr, 5).trim(); //รหัสเมนู2
					if (Fnc.isEmpty(menuid2)) {
						menuid2 = menuid1;
					}
					String menuid3 = getStringArray(lineDataArr, 6).trim(); //รหัสเมนู3
					if (Fnc.isEmpty(menuid3)) {
						menuid3 = menuid2;
					}
					String thai_title = getStringArray(lineDataArr, 1).trim(); //ชื่อเมนูภาษาไทย
					String eng_title = getStringArray(lineDataArr, 2).trim(); //ชื่อเมนูภาษา Eng
					if (Fnc.isEmpty(eng_title)) {
						eng_title = thai_title;
					}
					String sclass = getStringArray(lineDataArr, 7).trim();//CSS class
					String tooltipText = getStringArray(lineDataArr, 8).trim();//tooltiptext

					if (level.equals("2")) {
						if (Fnc.isEmpty(menuid1)) {
							level = "2M";
						}
					} else if (level.equals("3")) {
						if (Fnc.isEmpty(menuid1)) {
							level = "3M";
						}
					}

					list.add(new MenuListBeans(key, level, moduid, menuid1, menuid2, menuid3, thai_title, eng_title, sclass, tooltipText));

				}
			}
		} else {
			log.debug(menuListFile + " ไม่มีข้อความเมนู");
		}

	}

	public static String getStringArray(String[] arr, int index) {
		String ret = "";
		if (arr.length >= index) {
			ret = arr[index - 1];
		}
		return ret;
	}

	public static void getDataTest(java.util.List<MenuListBeans> dats, String user_group) throws Exception {

		dats.clear();

		dats.add(new MenuListBeans("0001", "1M", "comm", "", "", "",  "ข้อมูลทั่วไป", "Setup", "", ""));
		dats.add(new MenuListBeans("0002", "2", "comm", "F002", "F002", "F002", "บริษัท", "Company", "", ""));
		dats.add(new MenuListBeans("0003", "2", "comm", "F003", "F003", "F003", "สาขา", "Branch", "", ""));
		dats.add(new MenuListBeans("0004", "2M", "comm", "", "", "อื่นๆ", "", "Other", "", ""));
		dats.add(new MenuListBeans("0005", "3", "comm", "F005", "F005", "F005", "จังหวัด", "Province", "", ""));
		dats.add(new MenuListBeans("0006", "3M", "comm", "", "", "sub4", "", "sub4", "", ""));
		dats.add(new MenuListBeans("0007", "4", "comm", "F007", "F007", "F002", "F007", "F007", "", ""));

	}

	/**
	 * แบบนี้ไม่ต้องไป  update user_menu_group กรณี ManFMainMn.menuSubList มีการเปลี่ยนแปลงลำดับ
	 * @param lst_menu
	 * @param loginBean
	 * @throws Exception
	 */
	public static void getData(java.util.List<MenuListBeans> lst_menu, LoginBean loginBean) throws Exception {

		lst_menu.clear();
		try (FDbc dbc = FDbc.connectMasterDb()) {

			java.util.List<MenuListBeans> dats = menuList(loginBean); //นำค่าเข้า list ทั้งหมดก่อน

			boolean b_isAdmintrator = isAdmintrator(dbc, loginBean.getUSER_ID(), loginBean.getCOMP_CDE());

			if (b_isAdmintrator) {

				for (MenuListBeans mn : dats) {
					lst_menu.add(mn);
				}

			} else if (!b_isAdmintrator) { //กรณีไม่ใช่ Administrator

				//== ทำการ mark "SHOW" เมนูที่มีค่าใน MENU_ID2
				markMenuUse(dbc, loginBean, dats);

				//== ทำการ mark "SHOW" เมนูที่เป็นตัวบรรจุเมนู
				markMenuContainer(dats);

				//=== เก็บตัวที่ mark "SHOW" เข้า lst_menu
				for (MenuListBeans mn : dats) {
					if (mn.getKey().equals("SHOW")) {
						lst_menu.add(mn);
					}
				}

			}

		}

	}

	/**
	 * ทำการ mark "SHOW" เมนูที่มีค่าใน MENU_ID2
	 * @param dbc
	 * @param loginBean
	 * @param dats
	 * @throws SQLException
	 * @throws Exception
	 */
	private static void markMenuUse(FDbc dbc, LoginBean loginBean, java.util.List<MenuListBeans> dats) throws SQLException, Exception {
		java.util.List<String> lst_USER_MENU_GROUP = getUserGroupList(dbc, loginBean.getUSER_ID(), loginBean.getCOMP_CDE());
		SqlStr sql = new SqlStr();
		sql.addLine("SELECT DISTINCT MENU_LEVEL,  MODU,  MENU_ID1,  MENU_ID2 , MENU_ID3, THAI_NAME,  ENG_NAME ");
		sql.addLine("FROM " + TboFMENU_GROUP_D.tablename + " WHERE USER_MENU_GROUP IN (" + Fnc.sqlInStr(lst_USER_MENU_GROUP) + ") ");
		sql.addLine("ORDER BY KEY1");
		log.debug(sql.getSql());
		try (java.sql.ResultSet rs = dbc.getResultSetFw3(sql.getSql(), 1000);) {
			while (rs.next()) {

				for (MenuListBeans mn : dats) {

					if (!Fnc.isEmpty(mn.getMenuid3()) && mn.getMenuid3().equals(Fnc.getStr(rs.getString("MENU_ID3")))) {
						mn.setKey("SHOW");
						break;
					}

				}

			}
		}
	}

	/**
	 * ทำการ mark "SHOW" เมนูที่เป็นตัวบรรจุเมนู
	 * @param dats
	 */
	private static void markMenuContainer(java.util.List<MenuListBeans> dats) {

		boolean has_1M = false;
		boolean has_2M = false;
		boolean has_3M = false;
		int idx = dats.size();

		while (idx > 0) {

			idx--;
			MenuListBeans mn = dats.get(idx);

			if (mn.getKey().equals("SHOW")) { //เป็นเมนู ID

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

			} else { //เป็นเมนูที่บรรจุเมนูย่อย

				boolean show_menu = false;

				if (mn.getLevel().equals("1M")) {
					if (has_1M) {
						show_menu = true;
						has_1M = false;
					}
				} else if (mn.getLevel().equals("2M")) {
					if (has_2M) {
						show_menu = true;
						has_2M = false;
					}
				} else if (mn.getLevel().equals("3M")) {
					if (has_3M) {
						show_menu = true;
						has_3M = false;
					}
				}

				if (show_menu) {
					mn.setKey("SHOW");
				}

			}

		}

	}

	public static boolean isAdmintrator(String user_id, String comp_cde) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return isAdmintrator(dbc, user_id, comp_cde);
		}
	}

	public static boolean isAdmintrator(FDbc dbc, String user_id, String comp_cde) throws SQLException {
		String sql = " SELECT count(*) FROM " + TboFUSER_MENU.tablename + " WHERE USER_ID = '" +
				Fnc.sqlQuote(user_id) + "' AND USER_MENU_GROUP = '" + FConstComm.AdminGroup + "' AND COMP_CDE = '" + Fnc.sqlQuote(comp_cde) + "' ";
		log.debug(sql);
		return dbc.getRecordCount(sql) > 0 ? true : false;
	}

	public static java.util.List<String> getUserGroupList(FDbc dbc, String user_id, String comp_cde) throws SQLException {
		java.util.List<String> list = new ArrayList<>();
		String sql = " SELECT USER_MENU_GROUP FROM " + TboFUSER_MENU.tablename +
				" WHERE USER_ID = '" + Fnc.sqlQuote(user_id) + "' and COMP_CDE = '" + Fnc.sqlQuote(comp_cde) + "' ";
		log.debug(sql);
		try (java.sql.ResultSet rs = dbc.getResultSet(sql);) {
			while (rs.next()) {
				list.add(rs.getString("USER_MENU_GROUP"));
			}
		}
		return list;
	}

	public static void clearOldData(FDbc dbc, String user_id) throws SQLException {
		String sql = " DELETE FROM " + TboFUSER_MENU.tablename + " WHERE USER_ID = '" + Fnc.sqlQuote(user_id) + "' ";
		log.debug(sql);
		dbc.executeSql(sql);
	}

	public static boolean can_use_menu(String userid, String menu_id2) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return can_use_menu(dbc, userid, menu_id2);
		}
	}

	public static boolean can_use_menu(FDbc dbc, String userid, String menu_id2) throws SQLException {
		String sql = "SELECT count(*) FROM " + TboFUSER_MENU.tablename + " u LEFT JOIN " + TboFMENU_GROUP_D.tablename;
		sql += " ug ON u.USER_MENU_GROUP=ug.USER_MENU_GROUP ";
		sql += " WHERE u.USER_ID='" + Fnc.sqlQuote(userid) + "' AND ug.MENU_ID2='" + Fnc.sqlQuote(menu_id2) + "' ";
		log.debug(sql);
		return dbc.getRecordCount(sql) > 0 ? true : false;
	}

}

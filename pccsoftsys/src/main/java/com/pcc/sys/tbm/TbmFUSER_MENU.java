package com.pcc.sys.tbm;

import java.sql.SQLException;
import java.util.ArrayList;

import com.pcc.sys.beans.UserMenuCompBeans;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.SqlStr;
import com.pcc.sys.tbo.TboFUSER_MENU;

public class TbmFUSER_MENU {

	public static void del_from_user(FDbc dbc, String user_id) throws SQLException {

		String sql = " DELETE FROM " + TboFUSER_MENU.tablename + " WHERE USER_ID = '" + Fnc.sqlQuote(user_id) + "' ";
		dbc.executeSql(sql);

	}

	public static java.util.List<String> getUserCompList(FDbc dbc, String user_id) throws SQLException {
		java.util.List<String> list = new ArrayList<>();
		String sql = " SELECT DISTINCT COMP_CDE FROM " + TboFUSER_MENU.tablename + " WHERE USER_ID = '" + Fnc.sqlQuote(user_id) + "' ";
		try (java.sql.ResultSet rs = dbc.getResultSet(sql);) {
			while (rs.next()) {
				list.add(rs.getString("COMP_CDE"));
			}
		}
		return list;
	}

	public static void get_from_user(java.util.List<UserMenuCompBeans> list_fuser_menu, String user_id) throws Exception {

		list_fuser_menu.clear();

		try (FDbc dbc = FDbc.connectMasterDb()) {

			SqlStr sql = new SqlStr();
			sql.addLine("select aa.USER_ID,aa.COMP_CDE,aa.USER_MENU_GROUP,");
			sql.addLine("bb.THAI_NAME,cc.COMP_NAME");
			sql.addLine("from fuser_menu aa");
			sql.addLine("left join fmenu_group_h bb on aa.USER_MENU_GROUP=bb.USER_MENU_GROUP");
			sql.addLine("left join fcomp cc on aa.COMP_CDE=cc.COMP_CDE");
			sql.addLine("where aa.USER_ID = '" + Fnc.sqlQuote(user_id) + "' ");
			sql.addLine("order by aa.USER_ID,aa.COMP_CDE,aa.USER_MENU_GROUP");

			System.out.println(sql.getSql());

			try (java.sql.ResultSet rs = dbc.getResultSet(sql.getSql());) {
				while (rs.next()) {
					UserMenuCompBeans dat = new UserMenuCompBeans();

					dat.setUSER_ID(rs.getString("USER_ID"));
					dat.setCOMP_CDE(rs.getString("COMP_CDE"));
					dat.setUSER_MENU_GROUP(rs.getString("USER_MENU_GROUP"));
					dat.setTHAI_NAME(rs.getString("THAI_NAME"));
					dat.setCOMP_NAME(rs.getString("COMP_NAME"));

					list_fuser_menu.add(dat);
				}
			}

		}

	}

	public static int checkUserComp(FDbc dbc, String user_id, String comp_cde) throws SQLException {
		String sql = " SELECT count(*) as F1 FROM " +
				TboFUSER_MENU.tablename + " WHERE USER_ID = '" + Fnc.sqlQuote(user_id) +
				"' and COMP_CDE = '" + Fnc.sqlQuote(comp_cde) + "' ";
		System.out.println(sql);
		return dbc.getRecordCount(sql);
	}

}

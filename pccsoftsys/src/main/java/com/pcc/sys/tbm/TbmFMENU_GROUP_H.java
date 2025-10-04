package com.pcc.sys.tbm;

import java.util.ArrayList;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.tbo.TboFMENU_GROUP_H;

public class TbmFMENU_GROUP_H {

	public static void getData(java.util.List<FModelHasMap> dats, String user_menu_group, String thai_name) throws Exception {

		dats.clear();
		try (FDbc dbc = FDbc.connectMasterDb()) {
			String sql = " select * from " + TboFMENU_GROUP_H.tablename;
			sql += " where 1=1 ";
			if (!Fnc.isEmpty(user_menu_group)) {
				sql += " and USER_MENU_GROUP  like '%" + Fnc.sqlQuote(user_menu_group) + "%' ";
			}
			if (!Fnc.isEmpty(thai_name)) {
				sql += " and THAI_NAME  like '%" + Fnc.sqlQuote(thai_name) + "%' ";
			}

			System.out.println(sql);
			try (java.sql.ResultSet rs = dbc.getResultSetFw3(sql, 1000);) {
				while (rs.next()) {
					FModelHasMap dat = new FModelHasMap();
					dat.putRecord(rs);
					dats.add(dat);
				}
			}
		}

	}


	/**
	 * รายการที่ไม่มีใน list_fmenu_group_h_table
	 * @param dats
	 * @param list_fmenu_group_h_table
	 * @throws Exception
	 */
	public static void getDataNotIn(java.util.List<FModelHasMap> dats, java.util.List<TboFMENU_GROUP_H> list_fmenu_group_h_table) throws Exception {

		dats.clear();
		try (FDbc dbc = FDbc.connectMasterDb()) {
			String sql = " select * from " + TboFMENU_GROUP_H.tablename;
			sql += " where 1=1 ";
			java.util.List<String> ls1 = new ArrayList<>();
			for (TboFMENU_GROUP_H fmenu_GROUP_H_Table : list_fmenu_group_h_table) {
				ls1.add(fmenu_GROUP_H_Table.getUSER_MENU_GROUP());
			}
			if (ls1.size() > 0) {
				sql += " and NOT USER_MENU_GROUP IN (" + Fnc.sqlInStr(ls1) + ") ";
			}
			try (java.sql.ResultSet rs = dbc.getResultSetFw3(sql, 1000);) {
				while (rs.next()) {
					FModelHasMap dat = new FModelHasMap();
					dat.putRecord(rs);
					dats.add(dat);
				}
			}
		}

	}

}

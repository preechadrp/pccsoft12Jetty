package com.pcc.sys.tbm;

import java.sql.SQLException;
import java.util.List;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.tbo.TboFMENU_GROUP_H;

public class TbmFMENU_GROUP_D {

	public static void getData(String userGroup, List<FModelHasMap> dats) throws Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			String sql = "SELECT * FROM fmenu_group_d WHERE USER_MENU_GROUP='" + userGroup + "' ORDER BY key1 DESC ";
			System.out.println(sql);
			try (java.sql.ResultSet rs = dbc.getResultSet(sql);) {
				while (rs.next()) {
					FModelHasMap rec = new FModelHasMap();
					rec.putRecord(rs);
					dats.add(rec);
				}
			}
		}
	}

	public static void clearOldMenuGroup(FDbc dbc, TboFMENU_GROUP_H menu_group_h) throws SQLException {
		String sql = "DELETE FROM fmenu_group_d WHERE USER_MENU_GROUP='" + menu_group_h.getUSER_MENU_GROUP() + "' ";
		System.out.println(sql);
		dbc.executeSql(sql);
	}

}

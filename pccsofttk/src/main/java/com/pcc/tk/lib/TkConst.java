package com.pcc.tk.lib;

public class TkConst {

	public static String[][] zonehub = {
			{ "N", "ภาคเหนือ" },
			{ "NE", "ภาคตะวันออกเฉียงเหนือ" },
			{ "C", "ภาคกลาง" },
			{ "E", "ภาคตะวันออก" },
			{ "W", "ภาคตะวันตก" },
			{ "S", "ภาคใต้" }
	};
	
	public static String getZoneHubName(String zonehub) {
		for (String[] zh : TkConst.zonehub) {
			if (zh[0].equals(zonehub)) {
				return zh[1];
			}
		}
		return "";
	}
	
}

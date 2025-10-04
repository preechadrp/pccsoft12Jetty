package com.pcc.sys.tbf;

import com.pcc.sys.lib.FDbc;

public class TbfUtil {

	/**
	 * รันคำสั่ง sql = update ,delete ,insert  (เชื่อม Default Database)
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static int executeSql(String sql) throws Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return dbc.executeSql(sql);
		}
	}

}

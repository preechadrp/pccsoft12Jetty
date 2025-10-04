package com.pcc.sys.tbm;

import java.sql.SQLException;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.tbf.TbfFUSER;
import com.pcc.sys.tbo.TboFUSER;

public class TbmFUSER {

	public static void getData(java.util.List<FModelHasMap> dats, String usercode, String firstname, String lastname) throws Exception {

		dats.clear();
		try (FDbc dbc = FDbc.connectMasterDb()) {
			String sql = " select * from " + TboFUSER.tablename;
			sql += " where 1=1 ";
			if (!Fnc.isEmpty(usercode)) {
				sql += " and USER_ID  like '%" + Fnc.sqlQuote(usercode) + "%' ";
			}
			if (!Fnc.isEmpty(firstname)) {
				sql += " and FNAME_THAI  like '%" + Fnc.sqlQuote(firstname) + "%' ";
			}
			if (!Fnc.isEmpty(lastname)) {
				sql += " and LNAME_THAI  like '%" + Fnc.sqlQuote(lastname) + "%' ";
			}

			System.out.println(sql);
			try (java.sql.ResultSet rs = dbc.getResultSetFw3(sql, 1000);) {
				while (rs.next()) {
					FModelHasMap dat = new FModelHasMap();

					dat.putRecord(rs);
					//dat.setBigDecimal("ฟิลด์ที่ต้องการเพิ่มแต่ต้องไม่ซ้ำข้างในเดิม", xxx);

					dats.add(dat);
				}
			}
		}

	}

	public static String getUserName(FDbc dbc, String user_id) throws SQLException, Exception {

		String ret = "";

		TboFUSER usr = new TboFUSER();

		usr.setUSER_ID(user_id);

		if (TbfFUSER.find(dbc, usr)) {
			ret = (usr.getTITLE() + " " + usr.getFNAME_THAI() + " " + usr.getLNAME_THAI()).trim();
		}

		return ret;

	}

}

package com.pcc.sys.tbm;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.SqlStr;
import com.pcc.sys.tbf.TbfFPARA_SYS;
import com.pcc.sys.tbo.TboFPARA_SYS;

public class TbmFPARA_SYS {

	public static String getString(String para_id) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return getString(dbc, para_id);
		}
	}

	public static String getString(FDbc dbc, String para_id) throws SQLException, Exception {
		TboFPARA_SYS tbo = new TboFPARA_SYS();
		tbo.setPARA_ID(para_id);
		if (TbfFPARA_SYS.find(dbc, tbo)) {
			return Fnc.getStr(tbo.getPARA_VALUE());
		} else {
			return "";
		}
	}

	public static String getStringWithException(String para_id) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return getStringWithException(dbc, para_id);
		}
	}

	public static String getStringWithException(FDbc dbc, String para_id) throws SQLException, Exception {
		TboFPARA_SYS tbo = new TboFPARA_SYS();
		tbo.setPARA_ID(para_id);
		if (TbfFPARA_SYS.find(dbc, tbo)) {
			if (Fnc.isEmpty(tbo.getPARA_VALUE())) {
				throw new Exception("ยังไม่กำหนดพารามิเตอร์ : " + para_id);
			}
			return Fnc.getStr(tbo.getPARA_VALUE());
		} else {
			throw new Exception("ไม่พบพารามิเตอร์ : " + para_id);
		}
	}

	public static BigDecimal getBigDecimal(String para_id) throws SQLException, Exception {

		try (FDbc dbc = FDbc.connectMasterDb()) {
			return getBigDecimal(dbc, para_id);
		}

	}

	public static BigDecimal getBigDecimal(FDbc fdb, String para_id) throws SQLException, Exception {

		TboFPARA_SYS tbo = new TboFPARA_SYS();
		tbo.setPARA_ID(para_id);
		if (TbfFPARA_SYS.find(fdb, tbo)) {
			if (Fnc.isEmpty(tbo.getPARA_VALUE())) {
				return BigDecimal.ZERO;
			} else {
				return new BigDecimal(Fnc.getStr(tbo.getPARA_VALUE()));
			}
		} else {
			return BigDecimal.ZERO;
		}

	}

	public static java.sql.Date getSqlDate(String para_id) throws Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return getSqlDate(dbc, para_id);
		}
	}

	public static java.sql.Date getSqlDate(FDbc dbc, String para_id) throws Exception {

		TboFPARA_SYS tbo = new TboFPARA_SYS();

		tbo.setPARA_ID(para_id);

		if (TbfFPARA_SYS.find(dbc, tbo)) {

			String paraValue = Fnc.getStr(tbo.getPARA_VALUE());

			System.out.println("paraValue = " + paraValue);
			String[] dmy = paraValue.split("/");
			if (dmy.length != 3) {
				throw new Exception("ค่าพารามิเตอร์ของ " + para_id + " กำหนดไม่ถูกต้อง กรุณาติดต่อ Support");
			}
			int day1 = Fnc.getIntFromStr(dmy[0]);
			int month1 = Fnc.getIntFromStr(dmy[1]);
			int year1 = Fnc.getIntFromStr(dmy[2]) - FnDate.toBuddhistInc;
			if (day1 < 1 || day1 > 31) {
				throw new Exception("ค่าพารามิเตอร์ของ " + para_id + " กำหนดไม่ถูกต้อง กรุณาติดต่อ Support");
			}
			if (month1 < 0 || month1 > 12) {
				throw new Exception("ค่าพารามิเตอร์ของ " + para_id + " กำหนดไม่ถูกต้อง กรุณาติดต่อ Support");
			}
			if (year1 < 1900) {
				throw new Exception("ค่าพารามิเตอร์ของ " + para_id + " กำหนดไม่ถูกต้อง กรุณาติดต่อ Support");
			}
			java.sql.Date res = FnDate.getDate(day1, month1, year1);

			return res;

		} else {
			return null;
		}

	}

	public static TboFPARA_SYS getRecord(String para_id) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return getRecord(dbc, para_id);
		}
	}

	public static TboFPARA_SYS getRecord(FDbc dbc, String para_id) throws SQLException, Exception {
		TboFPARA_SYS tbo = new TboFPARA_SYS();
		tbo.setPARA_ID(para_id);
		if (TbfFPARA_SYS.find(dbc, tbo)) {
			return tbo;
		} else {
			return null;
		}
	}

	public static void getDataQry(
			java.util.List<FModelHasMap> dats,
			String para_id, String para_desc,
			LoginBean _loginBean) throws Exception {

		dats.clear();
		try (FDbc dbc = FDbc.connectMasterDb()) {

			SqlStr sql = new SqlStr();
			sql.addLine("select aa.*");
			sql.addLine("from " + TboFPARA_SYS.tablename + " aa");
			sql.addLine("where 1=1");
			if (!Fnc.isEmpty(para_id)) {
				sql.addLine("and aa.para_id like '%" + Fnc.sqlQuote(para_id.trim()) + "%' ");
			}
			if (!Fnc.isEmpty(para_desc)) {
				sql.addLine("and aa.para_desc like '%" + Fnc.sqlQuote(para_desc.trim()) + "%' ");
			}
			sql.addLine(" order by aa.para_id");

			System.out.println(sql.getSql());
			try (java.sql.ResultSet rs = dbc.getResultSetFw3(sql.getSql(), 1000);) {
				while (rs.next()) {
					FModelHasMap dat = new FModelHasMap();

					dat.setString("PARA_ID", rs.getString("PARA_ID"));
					dat.setString("PARA_VALUE", rs.getString("PARA_VALUE"));
					dat.setString("PARA_DESC", rs.getString("PARA_DESC"));

					dats.add(dat);
				}
			}
		}

	}
}

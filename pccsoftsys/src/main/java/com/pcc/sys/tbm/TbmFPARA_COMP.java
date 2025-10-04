package com.pcc.sys.tbm;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.tbf.TbfFPARA_COMP;
import com.pcc.sys.tbo.TboFPARA_COMP;

public class TbmFPARA_COMP {

	public static String getString(String comp_cde, String para_id) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return getString(dbc, comp_cde, para_id);
		}
	}

	public static String getString(FDbc dbc, String comp_cde, String para_id) throws SQLException, Exception {
		TboFPARA_COMP tbo = new TboFPARA_COMP();
		tbo.setCOMP_CDE(comp_cde);
		tbo.setPARA_ID(para_id);
		if (TbfFPARA_COMP.find(dbc, tbo)) {
			return Fnc.getStr(tbo.getPARA_VALUE()).trim();
		} else {
			return "";
		}
	}

	public static String getStringWithException(String comp_cde, String para_id) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return getStringWithException(dbc, comp_cde, para_id);
		}
	}

	public static String getStringWithException(FDbc dbc, String comp_cde, String para_id)
			throws SQLException, Exception {
		TboFPARA_COMP tbo = new TboFPARA_COMP();
		tbo.setCOMP_CDE(comp_cde);
		tbo.setPARA_ID(para_id);
		if (TbfFPARA_COMP.find(dbc, tbo)) {
			if (!Fnc.isEmpty(tbo.getPARA_VALUE())) {
				return Fnc.getStr(tbo.getPARA_VALUE()).trim();
			} else {
				throw new Exception("ยังไม่กำหนดพารามิเตอร์ : " + para_id);
			}
		} else {
			throw new Exception("ไม่พบพารามิเตอร์ : " + para_id);
		}
	}

	public static BigDecimal getBigDecimal(String comp_cde, String para_id) throws SQLException, Exception {

		try (FDbc dbc = FDbc.connectMasterDb()) {
			return getBigDecimal(dbc, comp_cde, para_id);
		}

	}

	public static BigDecimal getBigDecimal(FDbc fdb, String comp_cde, String para_id) throws SQLException, Exception {

		TboFPARA_COMP tbo = new TboFPARA_COMP();
		tbo.setCOMP_CDE(comp_cde);
		tbo.setPARA_ID(para_id);
		if (TbfFPARA_COMP.find(fdb, tbo)) {
			if (Fnc.isEmpty(tbo.getPARA_VALUE())) {
				return BigDecimal.ZERO;
			} else {
				return new BigDecimal(Fnc.getStr(tbo.getPARA_VALUE()));
			}
		} else {
			return BigDecimal.ZERO;
		}

	}

	public static BigDecimal getBigDecimalWithException(String comp_cde, String para_id)
			throws SQLException, Exception {

		try (FDbc dbc = FDbc.connectMasterDb()) {
			return getBigDecimalWithException(dbc, comp_cde, para_id);
		}

	}

	public static BigDecimal getBigDecimalWithException(FDbc fdb, String comp_cde, String para_id)
			throws SQLException, Exception {

		TboFPARA_COMP tbo = new TboFPARA_COMP();
		tbo.setCOMP_CDE(comp_cde);
		tbo.setPARA_ID(para_id);
		if (TbfFPARA_COMP.find(fdb, tbo)) {
			if (Fnc.isEmpty(tbo.getPARA_VALUE())) {
				return BigDecimal.ZERO;
			} else {
				return new BigDecimal(Fnc.getStr(tbo.getPARA_VALUE()));
			}
		} else {
			throw new Exception("ไม่พบพารามิเตอร์ : " + para_id);
		}

	}

	public static java.sql.Date getSqlDate(String comp_cde, String para_id) throws Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return getSqlDate(dbc, comp_cde, para_id);
		}

	}

	public static java.sql.Date getSqlDate(FDbc dbc, String comp_cde, String para_id) throws Exception {

		TboFPARA_COMP tbo = new TboFPARA_COMP();

		tbo.setCOMP_CDE(comp_cde);
		tbo.setPARA_ID(para_id);

		if (TbfFPARA_COMP.find(dbc, tbo)) {

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

	public static java.sql.Date getSqlDateWithException(String comp_cde, String para_id) throws Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return getSqlDateWithException(dbc, comp_cde, para_id);
		}

	}

	public static java.sql.Date getSqlDateWithException(FDbc dbc, String comp_cde, String para_id) throws Exception {

		TboFPARA_COMP tbo = new TboFPARA_COMP();

		tbo.setCOMP_CDE(comp_cde);
		tbo.setPARA_ID(para_id);

		if (TbfFPARA_COMP.find(dbc, tbo)) {

			if (Fnc.isEmpty(tbo.getPARA_VALUE())) {
				throw new Exception("ยังไม่กำหนดพารามิเตอร์ : " + para_id);
			}

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
			throw new Exception("ไม่พบพารามิเตอร์ : " + para_id);
		}

	}

	public static TboFPARA_COMP getRecord(String comp_cde, String para_id) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return getRecord(dbc, comp_cde, para_id);
		}
	}

	public static TboFPARA_COMP getRecord(FDbc dbc, String comp_cde, String para_id) throws SQLException, Exception {
		TboFPARA_COMP tbo = new TboFPARA_COMP();
		tbo.setCOMP_CDE(comp_cde);
		tbo.setPARA_ID(para_id);
		if (TbfFPARA_COMP.find(dbc, tbo)) {
			return tbo;
		} else {
			return null;
		}
	}

}

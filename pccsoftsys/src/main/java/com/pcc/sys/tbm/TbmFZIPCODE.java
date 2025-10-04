package com.pcc.sys.tbm;

import java.sql.SQLException;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.tbf.TbfFCOMP;
import com.pcc.sys.tbo.TboFCOMP;

public class TbmFZIPCODE {

	/**
	 * วันเริ่มปีบัญชี
	 * @param comp_cde
	 * @param fDate ถ้ารายงานเรียกแบบสิ้นสุด ณ วันที่ให้ส่ง to date มา , ถ้ารายงานเรียกแบบจากวันที่ถึงวันที่ ให้ส่ง from date มา
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public static java.sql.Date getGldate(String comp_cde, java.sql.Date fDate) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			return getGldate(dbc, comp_cde, fDate);
		}
	}

	/**
	 * วันเริ่มปีบัญชี
	 * @param dbc
	 * @param comp_cde
	 * @param fDate ถ้ารายงานเรียกแบบสิ้นสุด ณ วันที่ให้ส่ง to date มา , ถ้ารายงานเรียกแบบจากวันที่ถึงวันที่ ให้ส่ง from date มา
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public static java.sql.Date getGldate(FDbc dbc, String comp_cde, java.sql.Date fDate) throws SQLException, Exception {
		int glDay = 0;
		int glMonth = 0;
		int year = FnDate.getYear(fDate);

		TboFCOMP ct = new TboFCOMP();
		ct.setCOMP_CDE(comp_cde);
		if (TbfFCOMP.find(dbc, ct)) {
			glDay = ct.getGLDAY();
			glMonth = ct.getGLMONTH();
		}

		if (glDay == 0 || glMonth == 0) {
			throw new Exception("กรุณาระบุวันและเดือนเริ่มงบของบริษัท");
		}

		java.sql.Date gldate = FnDate.getDate(glDay, glMonth, year);
		if (gldate.after(fDate)) { //ถ้า GL Date มากกว่าวันที่เรียกต้องลบ 1 ปี , แสดงว่าวันเริ่มงบไม่ใช่วันที่ 1/1/xxxx
			return FnDate.getDate(glDay, glMonth, year - 1);
		} else {
			return gldate;
		}

	}

	/**
	 * หา วัน เดือน ของปีที่จะปิดบัญชี
	 * @param compid
	 * @param close_yy ปีที่ต้องการปิดบัญชี (พ.ศ.)
	 * @param ret_dd ตัวแปรรับวัน
	 * @param ret_mm ตัวแปรรับเดือน
	 * @throws SQLException
	 * @throws Exception
	 */
	public static void get_DDMM_CloseY(String compid, int close_yy, int[] ret_dd, int[] ret_mm) throws SQLException, Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			get_DDMM_CloseY(dbc, compid, close_yy, ret_dd, ret_mm);
		}
	}

	/**
	 * หา วัน เดือน ของปีที่จะปิดบัญชี
	 * @param dbc
	 * @param compid
	 * @param close_yy ปีที่ต้องการปิดบัญชี (พ.ศ.)
	 * @param ret_dd ตัวแปรรับวัน
	 * @param ret_mm ตัวแปรรับเดือน 
	 * @throws SQLException
	 * @throws Exception
	 */
	public static void get_DDMM_CloseY(FDbc dbc, String compid, int close_yy, int[] ret_dd, int[] ret_mm) throws SQLException, Exception {

		int glDay = 0;
		int glMonth = 0;

		TboFCOMP ct = new TboFCOMP();
		ct.setCOMP_CDE(compid);
		if (TbfFCOMP.find(dbc, ct)) {
			glDay = ct.getGLDAY();
			glMonth = ct.getGLMONTH();
		}

		if (glDay == 0 || glMonth == 0) {
			throw new Exception("กรุณาระบุวันและเดือนเริ่มงบของบริษัท");
		}

		java.sql.Date gl_date = FnDate.addDay(FnDate.getDate(glDay, glMonth, (close_yy - FnDate.toBuddhistInc) + 1), -1); //ถอย 1 วัน

		ret_dd[0] = FnDate.getDay(gl_date);
		ret_mm[0] = FnDate.getMonth(gl_date);

	}
}

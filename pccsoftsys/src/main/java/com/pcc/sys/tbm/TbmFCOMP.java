package com.pcc.sys.tbm;

import java.sql.SQLException;
import java.util.ArrayList;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.tbf.TbfFCOMP;
import com.pcc.sys.tbo.TboFCOMP;

public class TbmFCOMP {

	/**
	 * รายการที่ไม่มีใน list_fcomp_table
	 * @param dats
	 * @param not_in_list_fcomp_table
	 * @throws Exception
	 */
	public static void getDataNotIn(java.util.List<FModelHasMap> dats, java.util.List<TboFCOMP> not_in_list_fcomp_table)
			throws Exception {

		dats.clear();
		try (FDbc dbc = FDbc.connectMasterDb()) {
			String sql = " select * from " + TboFCOMP.tablename;
			sql += " where 1=1 ";
			if (not_in_list_fcomp_table != null) {
				java.util.List<String> ls1 = new ArrayList<>();
				for (TboFCOMP fcomp_table : not_in_list_fcomp_table) {
					ls1.add(fcomp_table.getCOMP_CDE());
				}
				if (ls1.size() > 0) {
					sql += " and NOT COMP_CDE IN (" + Fnc.sqlInStr(ls1) + ") ";
				}
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

	/**
	 * วันเริ่มรอบบัญชี
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
	 * วันเริ่มรอบบัญชี
	 * @param dbc
	 * @param comp_cde
	 * @param fDate ถ้ารายงานเรียกแบบสิ้นสุด ณ วันที่ให้ส่ง to date มา , ถ้ารายงานเรียกแบบจากวันที่ถึงวันที่ ให้ส่ง from date มา
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public static java.sql.Date getGldate(FDbc dbc, String comp_cde, java.sql.Date fDate)
			throws SQLException, Exception {
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
		java.sql.Date fromDate = FnDate.getDate(FnDate.getDay(fDate), FnDate.getMonth(fDate), year);
		
		if (gldate.after(fromDate)) { //ถ้า GL Date มากกว่าวันที่เรียกต้องลบ 1 ปี , แสดงว่าวันเริ่มงบไม่ใช่วันที่ 1/1/xxxx
			return FnDate.getDate(glDay, glMonth, year - 1);
		} else {
			return gldate;
		}

	}

	/**
	 * หา วัน เดือน ของปีที่จะปิดบัญชี
	 * @param compid
	 * @param close_yy ปีที่ต้องการปิดบัญชี (พ.ศ. หรือ ค.ศ ขึ้นอยู่กับระบบตอนนั้น)
	 * @param ret_dd ตัวแปรรับวัน
	 * @param ret_mm ตัวแปรรับเดือน
	 * @throws SQLException
	 * @throws Exception
	 */
	public static void get_DDMM_CloseY(String compid, int close_yy, int[] ret_dd, int[] ret_mm)
			throws SQLException, Exception {
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
	public static void get_DDMM_CloseY(FDbc dbc, String compid, int close_yy, int[] ret_dd, int[] ret_mm)
			throws SQLException, Exception {

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

		if (glDay == 29 && glMonth == 2) {//ตรวจวันที่เริ่มงบถ้าเป็น 29/2/yyyy กรณีบางปีต้องเป็นวันที่ 28/2/yyyy แทน

			if (FnDate.useThaiDate) {//วันสุดท้ายของเดือน ก.พ. ในปีนี้
				glDay = FnDate.getMaxdayInMonth(FnDate.getDate(1, 2, (close_yy - FnDate.toBuddhistInc)));
			} else {
				glDay = FnDate.getMaxdayInMonth(FnDate.getDate(1, 2, close_yy));
			}

		}

		java.sql.Date gl_date = null;
		if (FnDate.useThaiDate) {
			gl_date = FnDate.addDay(FnDate.getDate(glDay, glMonth, (close_yy - FnDate.toBuddhistInc) + 1), -1); //ถอย 1 วัน
		} else {
			gl_date = FnDate.addDay(FnDate.getDate(glDay, glMonth, close_yy + 1), -1); //ถอย 1 วัน
		}

		ret_dd[0] = FnDate.getDay(gl_date);
		ret_mm[0] = FnDate.getMonth(gl_date);

	}

	public static TboFCOMP getTable(String comp_cde) throws Exception {
		TboFCOMP table1 = new TboFCOMP();
		table1.setCOMP_CDE(comp_cde);
		if (TbfFCOMP.find(table1)) {
			return table1;
		} else {
			return null;
		}
	}
}

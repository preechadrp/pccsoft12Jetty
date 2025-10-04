package com.pcc.sys.tbf;

import java.sql.SQLException;
import java.util.Locale;

public class Tbf {

	public static java.util.Calendar getCal() {
		return java.util.Calendar.getInstance(Locale.ENGLISH);
	}

	/**
	 * Database ที่มีปัญหาที่ต้องแปลงคือ Mssql, Mariadb แต่ firebird ก็ใช้ได้เช่นกัน
	 * @param idx
	 * @param pstm
	 * @param date
	 * @throws SQLException
	 */
	public static void setDate(int idx, java.sql.PreparedStatement pstm, java.sql.Date date) throws SQLException {
		if (date != null) {
			pstm.setString(idx, date.toString());
		} else {
			pstm.setString(idx, null);

		}
	}

	/**
	 * Database ที่มีปัญหาที่ต้องแปลงคือ Mssql, Mariadb แต่ firebird ก็ใช้ได้เช่นกัน
	 * @param idx
	 * @param pstm
	 * @param datetime
	 * @throws SQLException
	 */
	public static void setDateTime(int idx, java.sql.PreparedStatement pstm, java.sql.Timestamp datetime) throws SQLException {
		if (datetime != null) {
			pstm.setString(idx, datetime.toString());
		} else {
			pstm.setString(idx, null);
		}
	}

}

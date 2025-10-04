package com.pcc.sys.lib;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class FnDate {

	/**
	 * บอกว่าระบบนี้จะใช้ พ.ศ. หรือ ค.ศ. ในการ input/display ข้อมูล
	 */
	public static final boolean useThaiDate = true;

	/**
	 * ใช้ - เป็นตัวกั้น รูปแบบ yyyy-MM-dd HH:mm:ss
	 */
	public static final SimpleDateFormat sdf_yyyyMMddHHmmss_Typ1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
	/**
	 * ใช้ - เป็นตัวกั้น รูปแบบ dd-MM-yyyy
	 */
	public static final SimpleDateFormat sdf_ddMMyyyy_typ1 = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
	/**
	 * ใช้ - เป็นตัวกั้น รูปแบบ yyyy-MM-dd
	 */
	public static final SimpleDateFormat sdf_yyyyMMdd_typ1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	/**
	 * ใช้ / เป็นตัวกั้น รูปแบบ dd/MM/yyyy
	 */
	public static final SimpleDateFormat sdf_ddMMyyyy_typ2 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

	public static final int toBuddhistInc = 543;
	public static final String[] shortMonthThaiName = new String[] { "ม.ค.", "ก.พ.", "มี.ค.", "เม.ย.", "พ.ค.", "มิ.ย.", "ก.ค.", "ส.ค.", "ก.ย.", "ต.ค.", "พ.ย.", "ธ.ค." };
	public static final String[] monthThaiName = new String[] { "มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม" };

	/**
	 * แสดงวันที่เป็นข้อความ
	 * @param date
	 * @return dd/MM/yyyy (ปี พ.ศ. หรือ ค.ศ. ขึ้นอยู่กับ useThaiDate)
	 */
	public static String displayDateString(java.util.Date date) {
		String rt = "";
		if (date != null) {
			String[] s = sdf_ddMMyyyy_typ2.format(date).split("/");
			if (useThaiDate) {
				rt = s[0] + "/" + s[1] + "/" + (Fnc.getIntFromStr(s[2]) + toBuddhistInc);
			} else {
				rt = s[0] + "/" + s[1] + "/" + (Fnc.getIntFromStr(s[2]));
			}
		}
		return rt;
	}

	/**
	 * แสดงวันที่เป็นข้อความ
	 * @param date
	 * @return dd/MM/yyyy HH:mm (ปี พ.ศ. หรือ ค.ศ. ขึ้นอยู่กับ useThaiDate)
	 */
	public static String displayDateTimeString(java.util.Date date) {
		String useFormat = "dd/MM/yyyy/HH/mm";
		String rt = "";
		if (date != null) {
			SimpleDateFormat FmDt = new SimpleDateFormat(useFormat, Locale.ENGLISH);
			String[] s = FmDt.format(date).split("/");
			if (useThaiDate) {
				rt = s[0] + "/" + s[1] + "/" + (Fnc.getIntFromStr(s[2]) + toBuddhistInc) + " " + s[3] + ":" + s[4];
			} else {
				rt = s[0] + "/" + s[1] + "/" + (Fnc.getIntFromStr(s[2])) + " " + s[3] + ":" + s[4];
			}
		}
		return rt;
	}

	/**
	 * ตรวจสอบวันเดือนปีว่าถูกต้องหรือไม่ @รูปแบบของ input : dd/MM/yyyy"
	 * @param date
	 * @return
	 */
	public static boolean isDate(String date) {
		try {
			sdf_ddMMyyyy_typ2.parse(date);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * เปรียบเทียบวันที่ว่าเท่ากันหรือไม่
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isDateEqual(java.util.Date date1, java.util.Date date2) {
		int y1 = getYear(date1);
		int y2 = getYear(date2);
		int m1 = getMonth(date1);
		int m2 = getMonth(date2);
		int d1 = getDay(date1);
		int d2 = getDay(date2);

		if (y1 != y2) {
			return false;
		} else if (m1 != m2) {
			return false;
		} else if (d1 != d2) {
			return false;
		} else {
			return true;
		}
	}

	public static java.sql.Date addDay(java.sql.Date sqlDate, int day) {
		try {
			Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
			calendar.setTime(sqlDate);
			calendar.add(Calendar.DATE, day);
			return new java.sql.Date(calendar.getTime().getTime());
		} catch (Exception e) {
			return null;
		}
	}

	public static java.util.Date addDay(java.util.Date sqlDate, int day) {
		try {
			Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
			calendar.setTime(sqlDate);
			calendar.add(Calendar.DATE, day);
			return new java.sql.Date(calendar.getTime().getTime());
		} catch (Exception e) {
			return null;
		}
	}

	public static java.sql.Date subDay(java.sql.Date sqlDate, int day) {
		try {
			Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
			calendar.setTime(sqlDate);
			calendar.add(Calendar.DATE, -day);
			return new java.sql.Date(calendar.getTime().getTime());
		} catch (Exception e) {
			return null;
		}
	}

	public static java.sql.Date addMonth(java.sql.Date date, int month) {
		try {
			Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
			calendar.setTime(date);
			calendar.add(Calendar.MONTH, month);
			return new java.sql.Date(calendar.getTime().getTime());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static java.sql.Date addYear(java.sql.Date date, int year) {
		try {
			Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
			calendar.setTime(date);
			calendar.add(Calendar.YEAR, year);
			return new java.sql.Date(calendar.getTime().getTime());
		} catch (Exception e) {
			return null;
		}
	}

	public static int subDate(java.util.Date startDate, java.util.Date endDate) {
		long s = startDate.getTime();
		long e = endDate.getTime();
		Long xx = e - s;
		return (int) (xx / 86400000);
	}

	public static int subDate(java.sql.Date startDate, java.sql.Date endDate) {
		long s = startDate.getTime();
		long e = endDate.getTime();
		Long xx = e - s;
		return (int) (xx / 86400000);
	}

	public static int checkSumDay(int yearCheck) {

		int YEAR = 2000; // 366 day

		if (YEAR == yearCheck) {
			return 366;
		} else {
			int sizeChk = (yearCheck - YEAR);

			for (int i = sizeChk; i < sizeChk + 1; i++) {
				YEAR = YEAR + 4;

				if (yearCheck == (YEAR)) {
					return 366;
				}
			}
		}

		return 365;
	}

	/**
	 * เปรียบเทียบวันที่ 
	 * @param date1
	 * @param date2
	 * @return date1 > date2 ส่งค่า 1 , date1 = date2 ส่งค่า 0 , อื่นๆ ส่ง -1
	 */
	public static int compareDate(java.util.Date date1, java.util.Date date2) {
		int res = 0;
		int d1 = 0;
		int d2 = 0;
		if (date1 != null) {
			d1 = Fnc.getIntFromStr(getDateString(date1, "yyyyMMdd"));
		}
		if (date2 != null) {
			d2 = Fnc.getIntFromStr(getDateString(date2, "yyyyMMdd"));
		}

		if (d1 > d2) {
			res = 1;
		} else if (d1 == d2) {
			res = 0;
		} else {
			res = -1;
		}
		//logger.info("d1 = " + d1 + " , d2 = " + d2 + " , res = " + res);
		return res;
	}

	public static int getDay(java.util.Date date) {
		Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
		calendar.setTime(date);
		return calendar.get(Calendar.DATE);
	}

	public static int getDay(java.sql.Date date) {
		Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
		calendar.setTime(date);
		return calendar.get(Calendar.DATE);
	}

	public static int getMonth(java.util.Date date) {
		Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}

	public static int getMonth(java.sql.Date date) {
		Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}

	public static int getYear(String date) {
		try {
			java.util.Date d = sdf_ddMMyyyy_typ2.parse(date);
			return getYear(d);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static int getYear(java.util.Date date) {
		Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
		calendar.setTime(date);
		int res = calendar.get(Calendar.YEAR);
		return res;
	}

	public static int getYear(java.sql.Date date) {
		Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
		calendar.setTime(date);
		int res = calendar.get(Calendar.YEAR);
		return res;
	}

	/**
	 * ดีงปี 2 ตัวหลัง สำหรับ Running เลขเอกสารต่างๆ (ปี พ.ศ. หรือ ค.ศ. ขึ้นอยู่กับ useThaiDate)
	 * @param date
	 * @return
	 */
	public static String getPrefixDocnoYY(java.util.Date date) {
		String year = new java.sql.Date(date.getTime()).toString();
		if (useThaiDate) {
			year = "" + (Integer.parseInt(year.substring(0, 4)) + toBuddhistInc);
		} else {
			year = "" + Integer.parseInt(year.substring(0, 4));
		}
		return year.substring(2);
	}

	/**
	 * ดีงปี 2 ตัวหลัง (ปี พ.ศ. หรือ ค.ศ. ขึ้นอยู่กับ useThaiDate)
	 * @param date
	 * @return
	 */
	public static String getYearYY(java.util.Date date) {
		String year = new java.sql.Date(date.getTime()).toString();
		if (useThaiDate) {
			year = "" + (Integer.parseInt(year.substring(0, 4)) + toBuddhistInc);
		} else {
			year = "" + Integer.parseInt(year.substring(0, 4));
		}
		return year.substring(2);
	}

	/**
	 * ดึงปี พ.ศ.
	 * @param date
	 * @return
	 */
	public static int getYearBuddhist(java.util.Date date) {
		return getYear(date) + toBuddhistInc;
	}

	/**
	 * นำวันที่มาแสดงเป็นปี พ.ศ.
	 * @param date
	 * @return dd/MM/yyyy
	 */
	public static String getDateBuddhistByDateCristDDMMYY(java.util.Date date) {
		// "yyyy-MM-dd HH:mm:ss" For full datetime ,Max time yyyy-MM-dd 23:59:59
		String sDate = "";
		if (date != null) {
			String s1 = "";
			s1 = sdf_ddMMyyyy_typ2.format(date);

			String[] dateSplit = s1.split("/");
			Integer year = Integer.parseInt(dateSplit[2]) + toBuddhistInc;
			sDate = dateSplit[0] + "/" + dateSplit[1] + "/" + sDate + year.toString();
		}
		return sDate;
	}

	/**
	 * getDateString
	 * @param date
	 * @return dd/mm/yyyy ปี ค.ศ.
	 */
	public static String getDateStringDMY(java.util.Date date) {
		String yy = getYear(date) + "";
		String mm = Fnc.getStrRight("0" + getMonth(date), 2);
		String dd = Fnc.getStrRight("0" + getDay(date), 2);
		String dateGet = dd + "-" + mm + "-" + yy;
		return dateGet;
	}

	/**
	 * getDateString2
	 * @param date
	 * @return yyyy/mm/dd  ปี ค.ศ.
	 */
	public static String getDateStringYMD(java.util.Date date) {
		String yy = getYear(date) + "";
		String mm = Fnc.getStrRight("0" + getMonth(date), 2);
		String dd = Fnc.getStrRight("0" + getDay(date), 2);
		String dateGet = yy + "-" + mm + "-" + dd;
		return dateGet;
	}

	/**
	 * แปลงวันที่เป็นตาม Formate ที่ต้องการ
	 * @param date รับได้ทั้ง java.util.Date , java.sql.Date , java.sql.Timestamp
	 * @param format ถ้าไม่ระบุจะเท่ากับ yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getDateString(java.util.Date date, String format) {
		String useFormat = "yyyy-MM-dd HH:mm:ss";
		if (!format.equals("")) {
			useFormat = format;
		}
		String rt = "";
		if (date != null) {
			SimpleDateFormat FmDt = new SimpleDateFormat(useFormat, Locale.ENGLISH);
			rt = FmDt.format(date);
		}
		return rt;
	}

	/**
	 * ดึงวันที่เป็น string
	 * @param date
	 * @return yyyy-MM-dd
	 */
	public static String getDateString(java.util.Date date) {
		String useFormat = "yyyy-MM-dd";
		String rt = "";
		if (date != null) {
			SimpleDateFormat FmDt = new SimpleDateFormat(useFormat, Locale.ENGLISH);
			rt = FmDt.format(date);
		}
		return rt;
	}

	public static int getYearCristToBuddhist(String year) {
		int initYear = Integer.parseInt(year);
		return initYear + toBuddhistInc;
	}

	public static String getThaiMonth(String month) {
		int mm = Integer.parseInt(month);
		return monthThaiName[mm - 1];
	}

	/**
	 * แปลงเป็นวันที่ไทย
	 * @param date ต้องเป็นรูปแบบ dd/MM/yyyy ปี ค.ศ.
	 * @return dd/MM/yyyy ปี พ.ศ.
	 */
	public static String getThaiDate(String str_date) {
		String rt = "";
		String[] s1 = str_date.split("/");
		if (s1.length == 3) {
			rt = s1[0] + "/" + s1[1] + "/" + (Fnc.getIntFromStr(s1[2]) + toBuddhistInc);
		}
		return rt;
	}

	/**
	 * เดือนไทยแบบเต็ม
	 * @param mm
	 * @return
	 */
	public static String getThaiMonthFullName(int mm) {
		String res = "";
		if (mm > 0 && mm < 13) {
			res = monthThaiName[mm - 1];
		}
		return res;
	}

	/**
	 * เดือนไทยแบบเต็ม
	 * @param mm
	 * @return
	 */
	public static String getThaiMonthFullName(String mm) {
		String res = "";
		Integer nMM = Fnc.getIntFromStr(mm);
		res = getThaiMonthFullName(nMM);
		return res;
	}

	public static String getShortThaiMonth(String month) {
		int mm = Integer.parseInt(month);
		return shortMonthThaiName[mm - 1];
	}

	public static java.sql.Date getSqlDate(java.util.Date utilDate) {
		if (utilDate != null) {
			return new java.sql.Date(utilDate.getTime());
		} else {
			return null;
		}
	}

	public static java.util.Date getUtilDate(java.sql.Date sqlDate) {
		return sqlDate;
	}

	public static java.sql.Date getDate(int day, int month, int year) {
		Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
		calendar.set(Calendar.DATE, day); // 1-31
		calendar.set(Calendar.MONTH, month - 1); // 0-11
		calendar.set(Calendar.YEAR, year); // ค.ศ.
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		java.util.Date d = calendar.getTime();
		return new java.sql.Date(d.getTime());
	}

	public static String getFullDate(java.sql.Date date) {
		if (date == null) {
			return "";
		} else {
			int day = getDay(date);
			String month = String.valueOf(getMonth(date)); //by Preecha 13/11/2555
			int year = getYearBuddhist(date);
			String thaiMonth = getThaiMonth(month);

			return day + " " + thaiMonth + " " + year;
		}
	}

	public static int getAge(java.sql.Date birthDate) {
		if (birthDate == null) {
			return 0;
		}
		Calendar c1 = new GregorianCalendar();
		c1.setTime(birthDate);
		Calendar c2 = new GregorianCalendar();
		c2.setTime(new java.util.Date());
		return c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
	}

	public static int getAge(java.sql.Date birthDate, java.sql.Date dateToCal) {
		if (birthDate == null) {
			return 0;
		}
		//กรณีที่ วันที่เริ่มคำนวนเป็น null ให้ใช้วันที่ปัจจุบัน
		if (dateToCal == null) {
			dateToCal = getTodaySqlDate();
		}
		Calendar c1 = new GregorianCalendar();
		c1.setTime(birthDate);
		Calendar c2 = new GregorianCalendar();
		c2.setTime(dateToCal);
		return c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
	}

	public static int getAgeFromDate(java.sql.Date birthDate, java.sql.Date dateToCal) {
		if (birthDate == null || dateToCal == null) {
			return 0;
		}
		Calendar c1 = new GregorianCalendar();
		c1.setTime(birthDate);
		Calendar c2 = new GregorianCalendar();
		c2.setTime(dateToCal);
		return c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
	}

	public static int getMonthDiff(java.sql.Date date1, java.sql.Date date2) {
		Calendar c1 = new GregorianCalendar();
		c1.setTime(date1);
		Calendar c2 = new GregorianCalendar();
		c2.setTime(date2);

		int diffMont = (12 * c2.get(Calendar.YEAR) + c2.get(Calendar.MONTH)) - (12 * c1.get(Calendar.YEAR) + c1.get(Calendar.MONTH));
		return Math.abs(diffMont);
	}

	/**
	 * วันสุดท้ายของเดือน
	 * @param date
	 * @return
	 */
	public static int getMaxdayInMonth(java.util.Date date) {

		Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
		calendar.setTime(date);
		int lastDate = calendar.getActualMaximum(Calendar.DATE);
		return lastDate;

	}

	/**
	 * ใช้หาวันของสัปดาห์ เช่น วันอาทิตย์ = 1
	 * @param date
	 * @return
	 */
	public static int getWeekDay(java.util.Date date) {
		Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * Format ที่ส่งมาคือ 01-12-2552 หรือ 01/12/2552 (ปี พ.ศ. หรือ ค.ศ. ขึ้นอยู่กับ useThaiDate)
	 * @param strDate
	 * @return
	 */
	public static java.sql.Date getDateFromString(String strDate) {

		java.sql.Date date;

		try {
			// แบบสั้น ๆ คิดง่าย ๆ
			if (strDate.length() != 10) {
				System.out.println("Format Date is not correct for this method");
				return null;
			}
			if (useThaiDate) {
				date = java.sql.Date.valueOf("" + (Integer.parseInt(strDate.substring(6, strDate.length())) - toBuddhistInc) + "-" + strDate.substring(3, 5) + "-" + strDate.substring(0, 2));
			} else {
				date = java.sql.Date.valueOf("" + (Integer.parseInt(strDate.substring(6, strDate.length()))) + "-" + strDate.substring(3, 5) + "-" + strDate.substring(0, 2));
			}
			return date;
		} catch (NumberFormatException e) {
			return null;
		}

	}

	/**
	 * ดึง ปี(พ.ศ.)เดือน รูปแบบ yymm เช่น 5501  (ปี พ.ศ. หรือ ค.ศ. ขึ้นอยู่กับ useThaiDate)
	 * @return
	 */
	public static String getCurYYMM() {
		String rt = "";

		String strYY = FnDate.getPrefixDocnoYY(getTodaySqlDate());
		Integer iMM = FnDate.getMonth(getTodaySqlDate());
		String strMM = "0" + iMM.toString();

		rt = strYY + Fnc.getStrRight(strMM, 2);

		return rt;
	}

	/**
	 * วันที่ปัจจุบัน แบบ java.sql.Date
	 * @return วันที่ไม่รวมเวลา
	 */
	public static java.sql.Date getTodaySqlDate() {
		//return new java.sql.Date(System.currentTimeMillis());
		return java.sql.Date.valueOf(getDateString(new java.util.Date())); //ตัดเวลาออก
	}

	/**
	 * วันที่ปัจจุบัน+เวลา แบบ java.sql.Date
	 * @return
	 */
	public static java.sql.Timestamp getTodaySqlDateTime() {
		//== ส่งแบบ '2017-05-24 22:00:10.399000'
		return new java.sql.Timestamp(System.currentTimeMillis());
	}

	/**
	 * วันที่ปัจจุบัน+เวลา แบบ java.util.Date
	 * @return
	 */
	public static java.util.Date getTodayDateTime() {
		return new java.util.Date(System.currentTimeMillis());
	}

	/**
	 * วันที่ปัจจุบัน
	 * @param format ถ้าไม่ระบุ default = yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getTodayDateTime(String format) {
		String useFormat = "yyyy-MM-dd HH:mm:ss";
		if (!format.equals("")) {
			useFormat = format;
			SimpleDateFormat sdf = new SimpleDateFormat(useFormat, Locale.ENGLISH);
			return sdf.format(getTodayDateTime());
		} else {
			return sdf_yyyyMMddHHmmss_Typ1.format(getTodayDateTime());
		}

	}

	/**
	 * เปรียบเทียบวันว่าต่างกันกี่ชั่วโมง(จากวันเวลาถึงวันเวลา)
	 * @param frdate 
	 * @param todate 
	 * @return
	 */
	public static long getDayDiffInHour(java.util.Date frdate, java.util.Date todate) {
		try {
			long diff = todate.getTime() - frdate.getTime();

			long diffHours2 = diff / (60 * 60 * 1000);

			return diffHours2;

		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * เปรียบเทียบวันว่าต่างกันกี่ชั่วโมง(ที่เวลา 00.00.00 น.)
	 * @param fromDate 
	 * @param toDate 
	 * @return
	 */
	public static long getDayDiffInHour2(java.util.Date fromDate, java.util.Date toDate) {
		try {
			java.sql.Date date1 = java.sql.Date.valueOf(getDateString(fromDate));
			java.sql.Date date2 = java.sql.Date.valueOf(getDateString(toDate));

			long startMillis = date1.getTime();
			long endMillis = date2.getTime();
			long diff = Math.abs(endMillis - startMillis);

			long diffHours2 = diff / (60 * 60 * 1000);

			return diffHours2;

		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * ผลต่างระดับมิลิวินาที (จากวันเวลาถึงวันเวลา)
	 * @param frdate
	 * @param todate
	 * @return
	 */
	public static long getDayDiffInMilliseconds(java.util.Date frdate, java.util.Date todate) {
		try {
			long diff = todate.getTime() - frdate.getTime();
			return diff;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * หาผลต่างจำนวนวัน(ที่เวลา 00.00.00 น.)
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public static int getDayDiffInDay(java.util.Date fromDate, java.util.Date toDate) {

		java.sql.Date date1 = java.sql.Date.valueOf(getDateString(fromDate));
		java.sql.Date date2 = java.sql.Date.valueOf(getDateString(toDate));

		long startMillis = date1.getTime();
		long endMillis = date2.getTime();
		long diff = Math.abs(endMillis - startMillis);
		int diffInDays = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

		//System.out.println("date : " + date1 +":"+startMillis+ " , " + date2 +":"+endMillis+  "  = " + diffInDays);

		return diffInDays;

	}

	/**
	 * Format ที่ส่งมาคือ 2013-03-28 (ปี ค.ศ.)
	 * @param strDate
	 * @return
	 */
	public static java.sql.Date getDateByStringYYYY_MM_DD(String strDate) {
		java.sql.Date date;

		try {
			if (strDate.length() != 10) {
				return null;
			}
			date = java.sql.Date.valueOf(strDate);

		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}

		return date;

	}

	public static void main(String[] art) {
		System.out.println(getTodayDateTime(""));//2017-05-30 00:32:55
		System.out.println(getTodayDateTime("yyyy-MM-dd"));//2017-05-30
		System.out.println(getTodayDateTime());//Tue May 30 00:32:55 ICT 2017
		System.out.println(getTodaySqlDateTime());//2017-05-30 00:33:59.701
		System.out.println(getTodaySqlDate());//2017-05-30
		System.out.println(getDateFromString("01/12/2560"));//2017-12-01

		LocalDate d1 = java.time.LocalDate.now();
		System.out.println("d1 : " + d1);

		LocalDate d2 = java.time.LocalDate.of(2027, 4, 13);
		System.out.println("d2 : " + d2);

		if (d1.isAfter(d2)) {
			System.out.println("d1 > d2");
		} else {
			System.out.println("d1 <= d2");
		}

		LocalDate d3 = java.time.LocalDate.of(2027, 4, 13);
		System.out.println("d3 : " + d3);
		if (d2.isEqual(d3)) {
			System.out.println("d2=d3");
		} else {
			System.out.println("d2<>d3");
		}

		//แปลง sqldate เป็น localdate
		LocalDate localDate1 = getTodaySqlDate().toLocalDate();
		System.out.println("convert sqldate to localDate1 : " + localDate1);

		//แปลง localdate เป็น sqlDate
		java.sql.Date sqlDate1 = java.sql.Date.valueOf(d1);
		System.out.println("convert local date to sqlDate1 : " + sqlDate1);

	}

}

package com.pcc.sys.lib;

import java.sql.SQLException;

public class FConstComm {

	//== ระบบกลาง
	public static final String AdminGroup = "Administrator"; //ตัวแปรกลาง
	public static final int topRow = 500;//Top record
	public static final boolean encodePassword = true; //เข้ารหัสสำหรับรหัสผ่าน user
	//public static final String msgNoRight = "ไม่มีสิทธิ์ใช้เมนูนี้";
	
	/**
	 * 1 = รันแบบ spring boot , 2 = deploy ใน tomcat
	 */
	public static int runAppMode = 1;
	//== End ระบบกลาง

//	/**
//	 * การเพิ่มรหัสลูกค้า ว่างๆ=Auto, 1=ระบุเอง,2=ถ้าว่าจะ Auto หรือ ระบุเอง
//	 * @return
//	 * @throws SQLException
//	 * @throws Exception
//	 */
//	public static String getCUST_ADD_MODE() throws SQLException, Exception {
//
//		FDbc dbc = new FDbc();
//		try {
//			dbc.connect();
//			return getCUST_ADD_MODE(dbc);
//		} finally {
//			dbc.disconnect();
//		}
//
//	}

//	/**
//	 * การเพิ่มรหัสลูกค้า ว่างๆ=Auto, 1=ระบุเอง,2=ถ้าว่าจะ Auto หรือ ระบุเอง
//	 * @param dbc
//	 * @return
//	 * @throws SQLException
//	 * @throws Exception
//	 */
//	public static String getCUST_ADD_MODE(FDbc dbc) throws SQLException, Exception {
//
//		return TbmFPARA_SYS.getString(dbc, "CM_CUST_ADD_MODE").trim();
//
//	}

	/**
	 * เงื่อนไขการหัก 1=หัก ณ ที่จ่าย, 2=ออกให้ตลอดไป, 3=ออกให้ครั้งเดียว,4=อื่นๆ
	 * @param whtype
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public static String getWHTYPE_DESC(String whtype) throws SQLException, Exception {

		String whtypeName = "";
		if (whtype.equals("1")) {
			whtypeName = "หัก ณ ที่จ่าย";
		} else if (whtype.equals("2")) {
			whtypeName = "ออกให้ตลอดไป";
		} else if (whtype.equals("3")) {
			whtypeName = "ออกให้ครั้งเดียว";
		} else if (whtype.equals("4")) {
			whtypeName = "อื่นๆ";
		}
		return whtypeName;

	}

	/**
	 * 1=ภงด. 3 (บุคคลธรรมดา), 2=ภงด.53 (นิติบุคคล)
	 * @param doc_type
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public static String getDOC_TYPE_DESC(int doc_type) throws SQLException, Exception {

		String doctypeName = "";
		if (doc_type == 1) {//1=ภงด. 3 (บุคคลธรรมดา)
			doctypeName = "ภงด.3";
		} else if (doc_type == 2) {//2=ภงด.53 (นิติบุคคล)
			doctypeName = "ภงด.53";
		}
		return doctypeName;

	}

}

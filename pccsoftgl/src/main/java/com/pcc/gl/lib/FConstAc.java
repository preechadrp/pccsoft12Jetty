package com.pcc.gl.lib;

public class FConstAc {

	// == ระบบบัญชี
//	public static String accitd_tax_pur = "";//รหัสบัญชีภาษีซื้อ
//	public static String accitd_tax_pur_wait = "";//รหัสบัญชีภาษีซื้อรอตัด
//	public static String accitd_tax_sale = "";//รหัสบัญชีภาษีขาย
//	public static String accitd_tax_sale_wait = "";//รหัสบัญชีภาษีขายรอตัด
//	public static String accitd_tax_dr = "";//รหัสบัญชีภาษีถูกหัก ณ ที่จ่าย
//	public static String accitd_tax_cr_3 = "";//รหัสบัญชี ภงด.3
//	public static String accitd_tax_cr_53 = "";//รหัสบัญชี ภงด.53
	// == End ระบบบัญชี

	public static String[][] gl_status = { { "0", "ยังไม่บันทึก" }, { "1", "รออนุมัติ" }, { "2", "อนุมัติ" },
			{ "9", "ยกเลิก" }, };

	public static String[][] gl_status_style = { { "0", "color:green" }, { "1", "color:blue" }, { "2", "color:black" },
			{ "9", "color:red" }, };

	public static String get_gl_status_name(String code) {
		String ret = "";
		for (String[] s1 : gl_status) {
			if (s1[0].equals(code)) {
				ret = s1[1];
				break;
			}
		}
		return ret;
	}

	public static String get_gl_status_style(String code) {
		String ret = "";
		for (String[] s1 : gl_status_style) {
			if (s1[0].equals(code)) {
				ret = s1[1];
				break;
			}
		}
		return ret;
	}

//	public static boolean getAutoApprove() throws SQLException, Exception {
//
//		FDbc dbc = new FDbc();
//		try {
//			dbc.connect();
//			return getAutoApprove(dbc);
//		} finally {
//			dbc.disconnect();
//		}
//
//	}
//
//	public static boolean getAutoApprove(FDbc dbc) throws SQLException, Exception {
//
//		boolean ret = false;
//
//		if (Fnc.isEmpty(TbmFPARA_SYS.getString(dbc, "AC_AUTO_APPROVE"))) {
//			ret = true;
//		}
//
//		return ret;
//
//	}

	public static String[][] gl_acc_type = { { "1", "สินทรัพย์" }, { "2", "เจ้าหนี้" }, { "3", "ทุน" },
			{ "4", "รายได้" }, { "5", "ค่าใช้จ่าย" } };

	public static String get_acc_type_name(String code) {
		String ret = "";
		for (String[] s1 : gl_acc_type) {
			if (s1[0].equals(code)) {
				ret = s1[1];
				break;
			}
		}
		return ret;
	}

}

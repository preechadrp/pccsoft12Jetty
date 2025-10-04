package com.pcc.gl.find;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FSearchData;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.SqlStr;

public class FfACCT_NO_SUB {

	/**
	 * รหัสบัญชที่มีตัวย่อย
	 * 
	 * @param loginBean
	 * @param callForm   ชื่อ class ที่เรียก method นี้
	 * @param methodName ชื่อ method ของ callForm ที่จะให้ทำหรือ call
	 *                   กลับหลังจากค้นหาและเลือก
	 */
	public static void popup(LoginBean loginBean, Object callForm, String methodName) {

		try {

			FSearchData sDt = new FSearchData();

			sDt.setWindowsID("SelPop");

			SqlStr sql = new SqlStr();
			//sql.addLine("select aa.* ,bb.ACCT_NAME,coalesce(cc.ACCT_NAME,'') AS ACCT_NAME_BANK");
			sql.addLine("select aa.* ,bb.ACCT_NAME, concat(coalesce(aa.ACCT_ID_BANK,''),' - ',coalesce(cc.ACCT_NAME,'')) AS ACCT_NAME_BANK");//firebird
			sql.addLine("from acct_no_sub aa");
			sql.addLine("left join acct_no bb on aa.COMP_CDE=bb.COMP_CDE and aa.ACCT_ID=bb.ACCT_ID");
			sql.addLine("left join acct_no cc on aa.COMP_CDE=cc.COMP_CDE and aa.ACCT_ID_BANK=cc.ACCT_ID");
			sDt.setSelectSql(sql.getSql());

			// fixWhere เอาไว้ส่งพวกรหัสบริษัทที่ login ขณะนั้นๆ
			sDt.getWhereFix().add("aa.COMP_CDE = '" + Fnc.sqlQuote(loginBean.getCOMP_CDE()) + "' ");

			// order by
			sDt.getOrderBy().add("aa.COMP_CDE,aa.ACCT_ID");

			// return field
			sDt.setReturnFieldName("ACCT_ID");

			// Object ที่ใช้เป็นเงื่อนไขการค้นหา
			sDt.getWhereObject().add(FSearchData.whereTextbox("aa.ACCT_ID", "รหัสบัญชี", "aa.ACCT_ID", "150px", "", ""));
			sDt.getWhereObject().add(FSearchData.whereTextboxWithOperator("bb.ACCT_NAME", "ชื่อบัญชี", "bb.ACCT_NAME","150px", "", "", "like1"));

			// show column
			sDt.getShowColum().add(FSearchData.genColumn("รหัสบัญชี", "ACCT_ID", "STRING", "100px", "ACCT_ID", null));
			sDt.getShowColum().add(FSearchData.genColumn("ชื่อบัญชี", "ACCT_NAME", "STRING", "250px", "ACCT_NAME", null));

			String[][] sub_type_descAndStyle = { { "1", "เจ้าหนี้", "color:red; text-align:left;" },
					{ "2", "ลูกหนี้", "color:green;  text-align:left;" },
					{ "3", "เช็คจ่ายล่วงหน้า", "color:blue; text-align:left;" }, };
			sDt.getShowColum().add(FSearchData.genColumn("ตัวย่อย", "SUB_TYPE", "INTEGER", "120px", "SUB_TYPE", sub_type_descAndStyle));
			sDt.getShowColum().add(FSearchData.genColumn("รหัสบัญชีล้างเช็คจ่ายล่วงหน้า", "ACCT_NAME_BANK", "STRING",	"250px", "ACCT_NAME_BANK", null));
			sDt.getShowColum().add(FSearchData.genColumn("ผู้สร้าง", "INSBY", "STRING", "100px", "INSBY", null));
			sDt.getShowColum().add(FSearchData.genColumn("วันสร้าง", "INSDT", "DATETIME", "150px", "INSDT", null));
			sDt.getShowColum().add(FSearchData.genColumn("ผู้ปรับปรุง", "UPBY", "STRING", "100px", "UPBY", null));
			sDt.getShowColum().add(FSearchData.genColumn("วันปรับปรุง", "UPDT", "DATETIME", "150px", "UPDT", null));

			sDt.setInitShow(true);
			sDt.setCallForm(callForm);
			sDt.setCallMethodName(methodName);

			sDt.doPopup();

		} catch (Exception e) {
			Msg.error(e);
		}

	}

}

package com.pcc.tk.find;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FSearchData;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.SqlStr;

public class FfTKEXPENSES {

	public static void popup(LoginBean loginBean, Object callForm, String methodName) {

		try {

			FSearchData sDt = new FSearchData();

			sDt.setWindowsID("FfTKEXPENSES");

			SqlStr sql = new SqlStr();
			sql.addLine("select aa.EXPENSESID,aa.EXPENSESNAME,aa.UPDBY,aa.UPDDTE");
			sql.addLine("from tkexpenses aa");
			sDt.setSelectSql(sql.getSql());

			// fixWhere เอาไว้ส่งพวกรหัสบริษัทที่ login ขณะนั้นๆ
			sDt.getWhereFix().add("aa.COMP_CDE = '" + Fnc.sqlQuote(loginBean.getCOMP_CDE()) + "' ");

			// order by
			sDt.getOrderBy().add("aa.EXPENSESID");

			// return
			sDt.setReturnFieldName("aa.EXPENSESID");

			// Object ที่ใช้เป็นเงื่อนไขการค้นหา
			sDt.getWhereObject().add(FSearchData.whereIntbox("aa.EXPENSESID", "รหัสค่าใช้จ่าย", "aa.EXPENSESID", "150px", ""));
			sDt.getWhereObject().add(FSearchData.whereTextboxWithOperator("aa.EXPENSESNAME", "ชื่อค่าใช้จ่าย", "aa.EXPENSESNAME", "150px", "", "","like1"));

			// show column
			sDt.getShowColum().add(FSearchData.genColumn("รหัสค่าใช้จ่าย", "aa.EXPENSESID", "INTEGER", "130px", "aa.EXPENSESID", null));
			sDt.getShowColum().add(FSearchData.genColumn("ชื่อค่าใช้จ่าย", "aa.EXPENSESNAME", "STRING", "", "aa.EXPENSESNAME", null));
			sDt.getShowColum().add(FSearchData.genColumn("ผู้ปรับปรุงรายการ", "bb.UPDBY", "STRING", "150px", "bb.UPDBY", null));
			sDt.getShowColum().add(FSearchData.genColumn("วันปรับปรุงรายการ", "bb.UPDDTE", "DATETIME", "150px", "bb.UPDDTE", null));

			sDt.setInitShow(true);
			sDt.setCallForm(callForm);
			sDt.setCallMethodName(methodName);

			sDt.doPopup();

		} catch (Exception e) {
			Msg.error(e);
		}

	}
	
}

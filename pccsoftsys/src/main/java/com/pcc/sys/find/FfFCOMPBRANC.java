package com.pcc.sys.find;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FSearchData;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.SqlStr;

public class FfFCOMPBRANC {

	public static void popup(LoginBean loginBean, Object callForm, String methodName) {

		try {

			FSearchData sDt = new FSearchData();

			sDt.setWindowsID("SelPop");

			SqlStr sql = new SqlStr();
			sql.addLine(" select BRANC_CDE ,BRANC_NAME ,BRANC_SHORTNAME ,BRANCTAXNO from FCOMPBRANC ");
			sDt.setSelectSql(sql.getSql());

			// fixWhere เอาไว้ส่งพวกรหัสบริษัทที่ login ขณะนั้นๆ
			sDt.getWhereFix().add("COMP_CDE = '" + Fnc.sqlQuote(loginBean.getCOMP_CDE()) + "' ");

			// order by
			sDt.getOrderBy().add("BRANC_CDE");

			// return field
			sDt.setReturnFieldName("BRANC_CDE");

			// Object ที่ใช้เป็นเงื่อนไขการค้นหา
			sDt.getWhereObject().add(FSearchData.whereIntbox("BRANC_CDE", "รหัสสาขา", "BRANC_CDE", "150px", ""));
			sDt.getWhereObject().add(FSearchData.whereTextboxWithOperator("BRANC_NAME", "ชื่อสาขา", "BRANC_NAME","150px", "", "", "like1"));

			// show column
			sDt.getShowColum().add(FSearchData.genColumn("รหัสสาขา", "BRANC_CDE", "INTEGER", "150px", "BRANC_CDE", null));
			sDt.getShowColum().add(FSearchData.genColumn("ชื่อสาขา", "BRANC_NAME", "STRING", "", "BRANC_NAME", null));
			sDt.getShowColum().add(FSearchData.genColumn("ชื่อย่อ", "BRANC_SHORTNAME", "STRING", "120px", "BRANC_SHORTNAME", null));
			sDt.getShowColum().add(FSearchData.genColumn("เลขภาษีสาขาที่จดทะเบียน", "BRANCTAXNO", "STRING", "150px", "BRANCTAXNO", null));

			sDt.setInitShow(true);
			sDt.setCallForm(callForm);
			sDt.setCallMethodName(methodName);

			sDt.doPopup();

		} catch (Exception e) {
			Msg.error(e);
		}

	}
	
}

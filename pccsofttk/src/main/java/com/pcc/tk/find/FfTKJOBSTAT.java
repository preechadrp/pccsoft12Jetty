package com.pcc.tk.find;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FSearchData;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.SqlStr;

public class FfTKJOBSTAT {

	public static void popup(LoginBean loginBean, Object callForm, String methodName) {

		try {

			FSearchData sDt = new FSearchData();

			sDt.setWindowsID("FfTKJOBSTAT");

			SqlStr sql = new SqlStr();
			sql.addLine("select aa.JOBSTATID,aa.JOBSTATNAME,aa.UPDBY,aa.UPDDTE");
			sql.addLine("from tkjobstat aa");
			sDt.setSelectSql(sql.getSql());

			// fixWhere เอาไว้ส่งพวกรหัสบริษัทที่ login ขณะนั้นๆ
			sDt.getWhereFix().add("aa.COMP_CDE = '" + Fnc.sqlQuote(loginBean.getCOMP_CDE()) + "' ");

			// order by
			sDt.getOrderBy().add("aa.JOBSTATID");

			// return
			sDt.setReturnFieldName("aa.JOBSTATID");

			// Object ที่ใช้เป็นเงื่อนไขการค้นหา
			sDt.getWhereObject().add(FSearchData.whereIntbox("aa.JOBSTATID", "รหัสสถานะการทำงาน", "aa.JOBSTATID", "150px", ""));
			sDt.getWhereObject().add(FSearchData.whereTextboxWithOperator("aa.JOBSTATNAME", "ชื่อสถานะการทำงาน", "aa.JOBSTATNAME", "150px", "", "","like1"));

			// show column
			sDt.getShowColum().add(FSearchData.genColumn("รหัสสถานะการทำงาน", "aa.JOBSTATID", "INTEGER", "130px", "aa.JOBSTATID", null));
			sDt.getShowColum().add(FSearchData.genColumn("ชื่อสถานะการทำงาน", "aa.JOBSTATNAME", "STRING", "", "aa.JOBSTATNAME", null));
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

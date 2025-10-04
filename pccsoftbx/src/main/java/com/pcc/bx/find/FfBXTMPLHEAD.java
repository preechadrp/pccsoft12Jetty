package com.pcc.bx.find;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FSearchData;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.SqlStr;

public class FfBXTMPLHEAD {

	public static void popup(LoginBean loginBean, Object callForm, String methodName) {

		try {

			FSearchData sDt = new FSearchData();

			sDt.setWindowsID("FfTKCOURT");

			SqlStr sql = new SqlStr();
			sql.addLine("select * from bxtmplhead aa");
			sDt.setSelectSql(sql.getSql());

			// fixWhere เอาไว้ส่งพวกรหัสบริษัทที่ login ขณะนั้นๆ
			sDt.getWhereFix().add("aa.COMP_CDE = '" + Fnc.sqlQuote(loginBean.getCOMP_CDE()) + "' ");

			// order by
			sDt.getOrderBy().add("aa.TMPLCDE");

			// return
			sDt.setReturnFieldName("aa.TMPLCDE");

			// Object ที่ใช้เป็นเงื่อนไขการค้นหา
			sDt.getWhereObject().add(FSearchData.whereIntbox("aa.TMPLCDE", "รหัสรูปแบบเอกสาร", "aa.TMPLCDE", "150px", ""));
			sDt.getWhereObject().add(FSearchData.whereTextboxWithOperator("aa.DOCPREFIX", "อักษรนำหน้าเลขเอกสาร", "aa.DOCPREFIX", "150px", "", "","like1"));
			sDt.getWhereObject().add(FSearchData.whereTextboxWithOperator("aa.TMPLNAME", "ชื่อรูปแบบเอกสาร", "aa.TMPLNAME", "150px", "", "","like1"));

			// show column
			sDt.getShowColum().add(FSearchData.genColumn("รหัสรูปแบบเอกสาร", "aa.TMPLCDE", "INTEGER", "130px", "aa.TMPLCDE", null));
			sDt.getShowColum().add(FSearchData.genColumn("อักษรนำหน้าเลขเอกสาร", "aa.DOCPREFIX", "STRING", "150px", "aa.DOCPREFIX", null));
			sDt.getShowColum().add(FSearchData.genColumn("ชื่อรูปแบบเอกสาร", "aa.TMPLNAME", "STRING", "", "aa.TMPLNAME", null));
			sDt.getShowColum().add(FSearchData.genColumn("ผู้ปรับปรุงรายการ", "aa.UPBY", "STRING", "150px", "aa.UPBY", null));
			sDt.getShowColum().add(FSearchData.genColumn("วันปรับปรุงรายการ", "aa.UPDT", "DATETIME", "150px", "aa.UPDT", null));

			sDt.setInitShow(true);
			sDt.setCallForm(callForm);
			sDt.setCallMethodName(methodName);

			sDt.doPopup();

		} catch (Exception e) {
			Msg.error(e);
		}

	}
	
}

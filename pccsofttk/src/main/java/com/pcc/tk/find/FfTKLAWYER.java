package com.pcc.tk.find;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FSearchData;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.SqlStr;

public class FfTKLAWYER {

	public static void popup(LoginBean loginBean, Object callForm, String methodName) {

		try {

			FSearchData sDt = new FSearchData();

			sDt.setWindowsID("FfBLPAYBY1");

			SqlStr sql = new SqlStr();
			sql.addLine("select aa.LAWYERID,aa.LAWYERNAME,aa.TELNO,aa.UPDBY,aa.UPDDTE");
			sql.addLine("from tklawyer aa");
			sDt.setSelectSql(sql.getSql());

			// fixWhere เอาไว้ส่งพวกรหัสบริษัทที่ login ขณะนั้นๆ
			sDt.getWhereFix().add("aa.COMP_CDE = '" + Fnc.sqlQuote(loginBean.getCOMP_CDE()) + "' ");

			// order by
			sDt.getOrderBy().add("aa.LAWYERID");

			// return
			sDt.setReturnFieldName("aa.LAWYERID");

			// Object ที่ใช้เป็นเงื่อนไขการค้นหา
			sDt.getWhereObject().add(FSearchData.whereIntbox("aa.LAWYERID", "รหัสทนาย", "aa.LAWYERID", "150px", ""));
			sDt.getWhereObject().add(FSearchData.whereTextboxWithOperator("aa.LAWYERNAME", "ชื่อทนาย", "aa.LAWYERNAME", "150px", "", "", "like1"));

			// show column
			sDt.getShowColum().add(FSearchData.genColumn("รหัสทนาย", "aa.LAWYERID", "INTEGER", "100px", "aa.LAWYERID", null));
			sDt.getShowColum().add(FSearchData.genColumn("ชื่อทนาย", "aa.LAWYERNAME", "STRING", "", "aa.LAWYERNAME", null));
			sDt.getShowColum().add(FSearchData.genColumn("เบอร์โทร", "aa.TELNO", "STRING", "200px", "aa.TELNO", null));
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

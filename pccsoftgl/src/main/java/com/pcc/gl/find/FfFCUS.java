package com.pcc.gl.find;

import com.pcc.gl.tbo.TboFCUS;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FSearchData;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.SqlStr;

public class FfFCUS {

	public static void popup(LoginBean loginBean, Object callForm, String methodName) {

		try {

			FSearchData sDt = new FSearchData();

			sDt.setWindowsID("SelPop");

			SqlStr sql = new SqlStr();
			sql.addLine(" select * from " + TboFCUS.tablename);
			sDt.setSelectSql(sql.getSql());

			// fixWhere เอาไว้ส่งพวกรหัสบริษัทที่ login ขณะนั้นๆ
			sDt.getWhereFix().add("COMP_CDE = '" + Fnc.sqlQuote(loginBean.getCOMP_CDE()) + "' ");

			// order by
			sDt.getOrderBy().add("CUST_CDE");

			// return field
			sDt.setReturnFieldName("CUST_CDE");

			// Object ที่ใช้เป็นเงื่อนไขการค้นหา
			sDt.getWhereObject().add(FSearchData.whereTextbox("CUST_CDE", "รหัส", "CUST_CDE", "150px", "", ""));
			sDt.getWhereObject()
					.add(FSearchData.whereTextboxWithOperator("FNAME", "ชื่อ", "FNAME", "150px", "", "", "like1"));
			sDt.getWhereObject()
					.add(FSearchData.whereTextboxWithOperator("LNAME", "นามสกุล", "LNAME", "150px", "", "", "like1"));
			sDt.getWhereObject()
					.add(FSearchData.whereTextbox("IDNO", "เลขประจำตัวผู้เสียภาษี", "IDNO", "150px", "", ""));

			// show column
			sDt.getShowColum().add(FSearchData.genColumn("รหัส", "CUST_CDE", "STRING", "150px", "CUST_CDE", null));
			sDt.getShowColum().add(FSearchData.genColumn("คำนำหน้า", "TITLE", "STRING", "150px", "TITLE", null));
			sDt.getShowColum().add(FSearchData.genColumn("ชื่อ", "FNAME", "STRING", "170px", "FNAME", null));
			sDt.getShowColum().add(FSearchData.genColumn("นามสกุล", "LNAME", "STRING", "170px", "LNAME", null));
			sDt.getShowColum().add(FSearchData.genColumn("เลขประจำตัวผู้เสียภาษี", "IDNO", "STRING", "", "IDNO", null));

			sDt.setInitShow(true);
			sDt.setCallForm(callForm);
			sDt.setCallMethodName(methodName);

			sDt.doPopup();

		} catch (Exception e) {
			Msg.error(e);
		}

	}

}

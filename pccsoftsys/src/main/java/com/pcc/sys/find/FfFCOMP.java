package com.pcc.sys.find;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FSearchData;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.SqlStr;
import com.pcc.sys.tbo.TboFCOMP;
import com.pcc.sys.tbo.TboFUSER_MENU;

public class FfFCOMP {

	public static void popup(String comp_cde, String user_id, LoginBean loginBean, Object callForm, String methodName) {

		try {

			FSearchData sDt = new FSearchData();

			sDt.setWindowsID("SelPop");

			SqlStr sql = new SqlStr();
			sql.addLine("select * from " + TboFCOMP.tablename);
			sDt.setSelectSql(sql.getSql());

			// fixWhere เอาไว้ส่งพวกรหัสบริษัทที่ login ขณะนั้นๆ
			if (!Fnc.isEmpty(user_id)) {
				sDt.getWhereFix().add("COMP_CDE IN (SELECT COMP_CDE FROM " + TboFUSER_MENU.tablename
						+ " WHERE USER_ID ='" + Fnc.sqlQuote(user_id) + "' ) ");
			}
			// order by
			sDt.getOrderBy().add("COMP_CDE");

			// return field
			sDt.setReturnFieldName("COMP_CDE");

			// Object ที่ใช้เป็นเงื่อนไขการค้นหา
			sDt.getWhereObject().add(FSearchData.whereTextbox("COMP_CDE", "รหัสบริษัท", "COMP_CDE", "150px", "", ""));
			sDt.getWhereObject().add(FSearchData.whereTextboxWithOperator("COMP_NAME", "ชื่อบริษัท", "COMP_NAME",
					"150px", "", "", "like1"));

			// show column
			sDt.getShowColum()
					.add(FSearchData.genColumn("รหัสบริษัท", "COMP_CDE", "STRING", "150px", "COMP_CDE", null));
			sDt.getShowColum().add(FSearchData.genColumn("ชื่อบริษัท", "COMP_NAME", "STRING", "", "COMP_NAME", null));

			sDt.setInitShow(true);
			sDt.setCallForm(callForm);
			sDt.setCallMethodName(methodName);

			sDt.doPopup();

		} catch (Exception e) {
			Msg.error(e);
		}
	}

}

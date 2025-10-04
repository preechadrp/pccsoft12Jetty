package com.pcc.sys.find;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FSearchData;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.SqlStr;
import com.pcc.sys.tbo.TboFZIPCODE;

public class FfFZIPCODE {

	public static void popup(String provin_id, LoginBean loginBean, Object callForm, String methodName) {

		try {

			FSearchData sDt = new FSearchData();

			sDt.setWindowsID("SelPop");

			SqlStr sql = new SqlStr();
			sql.addLine(" select * from " + TboFZIPCODE.tablename);
			sDt.setSelectSql(sql.getSql());

			// fixWhere เอาไว้ส่งพวกรหัสบริษัทที่ login ขณะนั้นๆ
			if (!Fnc.isEmpty(provin_id)) {
				sDt.getWhereFix().add("PROVIN_ID = '" + Fnc.sqlQuote(provin_id) + "' ");
			}

			// order by
			sDt.getOrderBy().add("ZIPCODE");

			// return field
			sDt.setReturnFieldName("ZIPCODE");

			// Object ที่ใช้เป็นเงื่อนไขการค้นหา
			sDt.getWhereObject().add(FSearchData.whereTextbox("ZIPCODE", "รหัสไปรษณีย์", "ZIPCODE", "150px", "", ""));
			sDt.getWhereObject()
					.add(FSearchData.whereTextbox("DISTRICT_NAME", "เขต/อำเภอ", "DISTRICT_NAME", "150px", "", ""));

			// show column
			sDt.getShowColum()
					.add(FSearchData.genColumn("รหัสไปรษณีย์", "ZIPCODE", "STRING", "150px", "ZIPCODE", null));
			sDt.getShowColum()
					.add(FSearchData.genColumn("เขต/อำเภอ", "DISTRICT_NAME", "STRING", "", "DISTRICT_NAME", null));

			sDt.setInitShow(true);
			sDt.setCallForm(callForm);
			sDt.setCallMethodName(methodName);

			sDt.doPopup();

		} catch (Exception e) {
			Msg.error(e);
		}

	}

}

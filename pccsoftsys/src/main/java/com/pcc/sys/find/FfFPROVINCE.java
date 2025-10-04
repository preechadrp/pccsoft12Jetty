package com.pcc.sys.find;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FSearchData;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.SqlStr;
import com.pcc.sys.tbo.TboFPROVINCE;

public class FfFPROVINCE {

	public static void popup(String provin_id, LoginBean loginBean, Object callForm, String methodName) {

		try {

			FSearchData sDt = new FSearchData();

			sDt.setWindowsID("SelPop");

			SqlStr sql = new SqlStr();
			sql.addLine(" select * from " + TboFPROVINCE.tablename);
			sDt.setSelectSql(sql.getSql());

			// fixWhere เอาไว้ส่งพวกรหัสบริษัทที่ login ขณะนั้นๆ
			// ..

			// order by
			sDt.getOrderBy().add("PROVIN_ID");

			// return field
			sDt.setReturnFieldName("PROVIN_ID");

			// Object ที่ใช้เป็นเงื่อนไขการค้นหา
			sDt.getWhereObject().add(FSearchData.whereIntbox("PROVIN_ID", "รหัสจังหวัด", "PROVIN_ID", "150px", ""));
			sDt.getWhereObject().add(FSearchData.whereTextboxWithOperator("PROVIN_NAME", "ชื่อจังหวัด", "PROVIN_NAME",
					"150px", "", "", "like2"));

			// show column
			sDt.getShowColum()
					.add(FSearchData.genColumn("รหัสจังหวัด", "PROVIN_ID", "INTEGER", "150px", "PROVIN_ID", null));
			sDt.getShowColum()
					.add(FSearchData.genColumn("ชื่อจังหวัด", "PROVIN_NAME", "STRING", "", "PROVIN_NAME", null));

			sDt.setInitShow(true);
			sDt.setCallForm(callForm);
			sDt.setCallMethodName(methodName);

			sDt.doPopup();

		} catch (Exception e) {
			Msg.error(e);
		}

	}

}

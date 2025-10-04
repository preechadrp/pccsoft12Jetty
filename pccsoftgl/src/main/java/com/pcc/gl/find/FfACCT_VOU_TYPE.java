package com.pcc.gl.find;

import com.pcc.gl.tbo.TboACCT_VOU_TYPE;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FSearchData;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.SqlStr;

public class FfACCT_VOU_TYPE {

	/**
	 * เลือกรหัสสมุดรายวัน
	 * 
	 * @param vou_type
	 * @param loginBean
	 * @param callForm   ชื่อ class ที่เรียก method นี้
	 * @param methodName ชื่อ method ของ callForm ที่จะให้ทำหรือ call
	 *                   กลับหลังจากค้นหาและเลือก
	 */
	public static void popup(String vou_type, LoginBean loginBean, Object callForm, String methodName) {

		try {

			FSearchData sDt = new FSearchData();

			sDt.setWindowsID("SelPop");

			SqlStr sql = new SqlStr();
			sql.addLine("select * from " + TboACCT_VOU_TYPE.tablename);
			sDt.setSelectSql(sql.getSql());

			// fixWhere เอาไว้ส่งพวกรหัสบริษัทที่ login ขณะนั้นๆ
			sDt.getWhereFix().add("COMP_CDE = '" + Fnc.sqlQuote(loginBean.getCOMP_CDE()) + "' ");

			// order by
			sDt.getOrderBy().add("VOU_TYPE");

			// return field
			sDt.setReturnFieldName("VOU_TYPE");

			// Object ที่ใช้เป็นเงื่อนไขการค้นหา
			sDt.getWhereObject()
					.add(FSearchData.whereTextbox("VOU_TYPE", "รหัสสมุดรายวัน", "VOU_TYPE", "150px", "", vou_type));
			sDt.getWhereObject().add(FSearchData.whereTextboxWithOperator("VOU_NAME", "ชื่อสมุดรายวัน", "VOU_NAME",
					"150px", "", "", "like1"));

			// show column
			sDt.getShowColum()
					.add(FSearchData.genColumn("รหัสสมุดรายวัน", "VOU_TYPE", "STRING", "150px", "VOU_TYPE", null));
			sDt.getShowColum().add(FSearchData.genColumn("ชื่อสมุดรายวัน", "VOU_NAME", "STRING", "", "VOU_NAME", null));
			String[][] userPvDescAndStyle = { { "0", "", "color:red; text-align:center;" },
					{ "1", "YES", " text-align:center;" }, };
			sDt.getShowColum().add(FSearchData.genColumn("ล้างยอดเจ้าหนี้ได้", "USE_PV", "INTEGER", "120px", "USE_PV",
					userPvDescAndStyle));
			String[][] userRvDescAndStyle = { { "0", "", "color:red; text-align:center;" },
					{ "1", "YES", " text-align:center;" }, };
			sDt.getShowColum().add(FSearchData.genColumn("ล้างยอดลูกหนี้ได้", "USE_RV", "INTEGER", "120px", "USE_RV",
					userRvDescAndStyle));

			sDt.setInitShow(true);
			sDt.setCallForm(callForm);
			sDt.setCallMethodName(methodName);

			sDt.doPopup();

		} catch (Exception e) {
			Msg.error(e);
		}

	}

}

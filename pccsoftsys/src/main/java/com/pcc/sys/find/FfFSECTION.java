package com.pcc.sys.find;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FSearchData;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.SqlStr;
import com.pcc.sys.tbo.TboFSECTION;

public class FfFSECTION {

	public static void popup(String sect_id, LoginBean loginBean, Object callForm, String methodName) {

		try {

			FSearchData sDt = new FSearchData();

			sDt.setWindowsID("SelPop");

			SqlStr sql = new SqlStr();
			sql.addLine(" select * from " + TboFSECTION.tablename);
			sDt.setSelectSql(sql.getSql());

			// fixWhere เอาไว้ส่งพวกรหัสบริษัทที่ login ขณะนั้นๆ
			sDt.getWhereFix().add("COMP_CDE = '" + Fnc.sqlQuote(loginBean.getCOMP_CDE()) + "' ");

			// order by
			sDt.getOrderBy().add("SECT_ID");

			// return field
			sDt.setReturnFieldName("SECT_ID");

			// Object ที่ใช้เป็นเงื่อนไขการค้นหา
			sDt.getWhereObject().add(FSearchData.whereTextbox("SECT_ID", "รหัสแผนก", "SECT_ID", "150px", "", ""));
			sDt.getWhereObject().add(FSearchData.whereTextbox("SECT_NAME", "ชื่อแผนก", "SECT_NAME", "150px", "", ""));

			// show column
			sDt.getShowColum().add(FSearchData.genColumn("รหัสแผนก", "SECT_ID", "STRING", "150px", "SECT_ID", null));
			sDt.getShowColum().add(FSearchData.genColumn("ชื่อแผนก", "SECT_NAME", "STRING", "", "SECT_NAME", null));

			sDt.setInitShow(true);
			sDt.setCallForm(callForm);
			sDt.setCallMethodName(methodName);

			sDt.doPopup();

		} catch (Exception e) {
			Msg.error(e);
		}

	}

}

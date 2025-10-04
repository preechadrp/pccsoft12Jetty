package com.pcc.gl.find;

import com.pcc.gl.tbo.TboACGL_HEADER;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FSearchData;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.SqlStr;

public class FfACGL_HEADER {

	/**
	 * เลือกเลขที่ใบสำคัญ
	 * 
	 * @param vou_type
	 * @param vou_no
	 * @param loginBean
	 * @param callForm   ชื่อ class ที่เรียก method นี้
	 * @param methodName ชื่อ method ของ callForm ที่จะให้ทำหรือ call
	 *                   กลับหลังจากค้นหาและเลือก
	 */
	public static void popup(String vou_type, String vou_no, LoginBean loginBean, Object callForm, String methodName) {

		try {

			FSearchData sDt = new FSearchData();

			sDt.setWindowsID("SelPop");

			SqlStr sql = new SqlStr();
			sql.addLine("select * from " + TboACGL_HEADER.tablename);
			sDt.setSelectSql(sql.getSql());

			// fixWhere เอาไว้ส่งพวกรหัสบริษัทที่ login ขณะนั้นๆ
			sDt.getWhereFix().add("COMP_CDE = '" + Fnc.sqlQuote(loginBean.getCOMP_CDE()) + "' ");
			sDt.getWhereFix().add("VOU_TYPE = '" + Fnc.sqlQuote(vou_type) + "' ");

			// order by
			sDt.getOrderBy().add("POSTDATE desc");
			sDt.getOrderBy().add("VOU_NO desc");

			// return field
			sDt.setReturnFieldName("VOU_NO");

			// Object ที่ใช้เป็นเงื่อนไขการค้นหา
			sDt.getWhereObject().add(FSearchData.whereTextbox("VOU_NO", "เลขที่ใบสำคัญ", "VOU_NO", "150px", "", ""));
			sDt.getWhereObject().add(FSearchData.whereDatebox("POSTDATE", "วันที่ใบสำคัญ", "POSTDATE", "150px", ""));

			// show column
			sDt.getShowColum()
					.add(FSearchData.genColumn("รหัสสมุดรายวัน", "VOU_TYPE", "STRING", "120px", "VOU_TYPE", null));
			sDt.getShowColum().add(FSearchData.genColumn("เลขที่ใบสำคัญ", "VOU_NO", "STRING", "120px", "VOU_NO", null));
			sDt.getShowColum()
					.add(FSearchData.genColumn("วันที่ใบสำคัญ", "POSTDATE", "DATE", "110px", "POSTDATE", null));
			sDt.getShowColum().add(FSearchData.genColumn("ผู้ทำรายการ", "UPBY", "STRING", "150px", "UPBY", null));
			sDt.getShowColum().add(FSearchData.genColumn("วันทำรายการ", "UPDT", "DATETIME", "140px", "UPDT", null));
			sDt.getShowColum()
					.add(FSearchData.genColumn("ผู้อนุมัติรายการ", "APPR_BY", "STRING", "150px", "APPR_BY", null));
			sDt.getShowColum()
					.add(FSearchData.genColumn("วันอนุมัติรายการ", "APPR_DT", "DATETIME", "140px", "APPR_DT", null));
			sDt.getShowColum().add(FSearchData.genColumn("หมายเหตุ", "DESCR", "STRING", "350px", "DESCR", null));

			sDt.setInitShow(true);
			sDt.setCallForm(callForm);
			sDt.setCallMethodName(methodName);

			sDt.doPopup();

		} catch (Exception e) {
			Msg.error(e);
		}

	}

}

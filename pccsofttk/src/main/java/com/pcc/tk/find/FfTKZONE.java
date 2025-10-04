package com.pcc.tk.find;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FSearchData;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.SqlStr;
import com.pcc.tk.lib.TkConst;

public class FfTKZONE {

	public static void popup(LoginBean loginBean, Object callForm, String methodName) {

		try {

			FSearchData sDt = new FSearchData();

			sDt.setWindowsID("FfTKZONE");

			SqlStr sql = new SqlStr();
			sql.addLine("select aa.ZONEID,aa.ZONENAME,aa.ZONEHUB,aa.UPDBY,aa.UPDDTE");
			sql.addLine("from tkzone aa");
			sDt.setSelectSql(sql.getSql());

			// fixWhere เอาไว้ส่งพวกรหัสบริษัทที่ login ขณะนั้นๆ
			sDt.getWhereFix().add("aa.COMP_CDE = '" + Fnc.sqlQuote(loginBean.getCOMP_CDE()) + "' ");

			// order by
			sDt.getOrderBy().add("aa.ZONEID");

			// return
			sDt.setReturnFieldName("aa.ZONEID");

			// Object ที่ใช้เป็นเงื่อนไขการค้นหา
			sDt.getWhereObject().add(FSearchData.whereIntbox("aa.ZONEID", "รหัส Zone", "aa.ZONEID", "150px", ""));
			sDt.getWhereObject().add(FSearchData.whereTextboxWithOperator("aa.ZONENAME", "ชื่อ Zone", "aa.ZONENAME", "150px", "", "","like1"));

			// show column
			sDt.getShowColum().add(FSearchData.genColumn("รหัส Zone", "aa.ZONEID", "INTEGER", "100px", "aa.ZONEID", null));
			sDt.getShowColum().add(FSearchData.genColumn("ชื่อ Zone", "aa.ZONENAME", "STRING", "", "aa.ZONENAME", null));
			{
				//ตัวอย่าง แสดงคำอธิบายรายการแทนรหัสและใส่ style ของ cel
				//กรณีไม่ต้องการทำแบบนี้ให้ใช้หลักการ case when .. else .. end as MyField ในระดับ select เลย
				String[][] descAndStyle = {
						{ "N", TkConst.getZoneHubName("N"), "color:red; text-align:left;" },
						{ "NE", TkConst.getZoneHubName("NE"), " text-align:left;" },
						{ "C", TkConst.getZoneHubName("C"), "color:blue; text-align:left;" },
						{ "E", TkConst.getZoneHubName("E"), "color:pink; text-align:left;" },
						{ "W", TkConst.getZoneHubName("W"), "color:green; text-align:left;" },
						{ "S", TkConst.getZoneHubName("S"), "color:#3399ff; text-align:left;" },
				};
				sDt.getShowColum().add(FSearchData.genColumn("HUB", "ZONEHUB", "STRING", "150px", "ZONEHUB", descAndStyle));
			}
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

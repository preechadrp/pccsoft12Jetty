package com.pcc.sys.find;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FSearchData;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.SqlStr;

public class FfAMPHURID {

	public static void popup(int provin_id, LoginBean loginBean, Object callForm, String methodName) {

		try {

			FSearchData sDt = new FSearchData();

			sDt.setWindowsID("SelPop");

			SqlStr sql = new SqlStr();
			sql.addLine(" select * from famphur");
			sDt.setSelectSql(sql.getSql());

			// fixWhere เอาไว้ส่งพวกรหัสบริษัทที่ login ขณะนั้นๆ
			sDt.getWhereFix().add("PROVIN_ID = " + provin_id);
			// ..

			// order by
			sDt.getOrderBy().add("AMPHURID");

			// return field
			sDt.setReturnFieldName("AMPHURID");

			// Object ที่ใช้เป็นเงื่อนไขการค้นหา
			sDt.getWhereObject().add(FSearchData.whereIntbox("AMPHURID", "รหัสอำเภอ/เขต", "AMPHURID", "150px", ""));
			sDt.getWhereObject().add(FSearchData.whereTextboxWithOperator("AMPHURNAME", "ชื่ออำเภอ/เขต", "AMPHURNAME",
					"150px", "", "", "like1"));

			// show column
			sDt.getShowColum().add(FSearchData.genColumn("รหัสอำเภอ/เขต", "AMPHURID", "INTEGER", "150px", "AMPHURID", null));
			sDt.getShowColum().add(FSearchData.genColumn("ชื่ออำเภอ/เขต", "AMPHURNAME", "STRING", "", "AMPHURNAME", null));

			sDt.setInitShow(true);
			sDt.setCallForm(callForm);
			sDt.setCallMethodName(methodName);

			sDt.doPopup();

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public static void popup(EventListener<Event> event_callBack) {

		try {

			FSearchData sDt = new FSearchData();

			sDt.setWindowsID("SelPop");

			SqlStr sql = new SqlStr();
			sql.addLine("select aa.AMPHURID,aa.AMPHURNAME,aa.PROVIN_ID,aa.ZIPCODE,pp.PROVIN_NAME ");
			sql.addLine("from famphur aa");
			sql.addLine("LEFT JOIN FPROVINCE pp ON aa.PROVIN_ID =pp.PROVIN_ID");
			sDt.setSelectSql(sql.getSql());

			// fixWhere เอาไว้ส่งพวกรหัสบริษัทที่ login ขณะนั้นๆ
			//sDt.getWhereFix().add("PROVIN_ID = " + provin_id);
			// ..

			// order by
			sDt.getOrderBy().add("aa.AMPHURID");

			// return field
			sDt.setReturnFieldName("aa.AMPHURID");

			// Object ที่ใช้เป็นเงื่อนไขการค้นหา
			sDt.getWhereObject().add(FSearchData.whereIntbox("aa.AMPHURID", "รหัสอำเภอ/เขต", "aa.AMPHURID", "150px", ""));
			sDt.getWhereObject().add(FSearchData.whereTextboxWithOperator("aa.AMPHURNAME", "ชื่ออำเภอ/เขต", "aa.AMPHURNAME",
					"150px", "", "", "like1"));
			sDt.getWhereObject().add(FSearchData.whereIntbox("aa.PROVIN_ID", "รหัสจังหวัด", "aa.PROVIN_ID", "150px", ""));
			sDt.getWhereObject().add(FSearchData.whereTextboxWithOperator("pp.PROVIN_NAME", "ชื่อจังหวัด", "pp.PROVIN_NAME",
					"150px", "", "", "like1"));

			// show column
			sDt.getShowColum().add(FSearchData.genColumn("รหัสอำเภอ/เขต", "AMPHURID", "INTEGER", "150px", "AMPHURID", null));
			sDt.getShowColum().add(FSearchData.genColumn("ชื่ออำเภอ/เขต", "AMPHURNAME", "STRING", "300px", "AMPHURNAME", null));
			sDt.getShowColum().add(FSearchData.genColumn("รหัสไปรษณีย์", "ZIPCODE", "STRING", "180px", "ZIPCODE", null));
			sDt.getShowColum().add(FSearchData.genColumn("รหัสจังหวัด", "PROVIN_ID", "INTEGER", "150px", "PROVIN_ID", null));
			sDt.getShowColum().add(FSearchData.genColumn("ชื่อจังหวัด", "PROVIN_NAME", "STRING", "300px", "PROVIN_NAME", null));

			sDt.setInitShow(true);
			sDt.setEvent_callBack(event_callBack);

			sDt.doPopup();

		} catch (Exception e) {
			Msg.error(e);
		}

	}

}

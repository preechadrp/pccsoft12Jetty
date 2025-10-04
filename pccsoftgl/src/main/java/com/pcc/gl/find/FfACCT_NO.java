package com.pcc.gl.find;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

import com.pcc.gl.tbo.TboACCT_NO;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FSearchData;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.SqlStr;

public class FfACCT_NO {

	/**
	 * เลือกรหัสบัญชี
	 * 
	 * @param acct_id
	 * @param loginBean  ถ้าส่งค่า null มาจะแสดงทั้งหมด
	 * @param callForm   ชื่อ class ที่เรียก method นี้
	 * @param methodName ชื่อ method ของ callForm ที่จะให้ทำหรือ call
	 *                   กลับหลังจากค้นหาและเลือก
	 */
	public static void popup(String acct_id, LoginBean loginBean, Object callForm, String methodName) {

		try {

			FSearchData sDt = new FSearchData();

			sDt.setWindowsID("SelPop");

			SqlStr sql = new SqlStr();
			sql.addLine("select * from " + TboACCT_NO.tablename);
			sDt.setSelectSql(sql.getSql());

			// fixWhere เอาไว้ส่งพวกรหัสบริษัทที่ login ขณะนั้นๆ
			sDt.getWhereFix().add("COMP_CDE = '" + Fnc.sqlQuote(loginBean.getCOMP_CDE()) + "' ");

			// order by
			sDt.getOrderBy().add("ACCT_ID");

			// return
			sDt.setReturnFieldName("ACCT_ID");

			// Object ที่ใช้เป็นเงื่อนไขการค้นหา
			sDt.getWhereObject().add(FSearchData.whereTextbox("ACCT_ID", "รหัสบัญชี", "ACCT_ID", "150px", "", acct_id));
			sDt.getWhereObject().add(FSearchData.whereTextboxWithOperator("ACCT_NAME", "ชื่อบัญชี", "ACCT_NAME", "150px", "", "", "like1"));

			// show column
			sDt.getShowColum().add(FSearchData.genColumn("รหัสบัญชี", "ACCT_ID", "STRING", "150px", "ACCT_ID", null));
			sDt.getShowColum().add(FSearchData.genColumn("ชื่อบัญชี", "ACCT_NAME", "STRING", "", "ACCT_NAME", null));
			String[][] acctDescAndStyle = { { "1", "สินทรัพย์", "text-align:left;" },
					{ "2", "หนี้สิน", "text-align:left;" }, { "3", "ทุน", "text-align:left;" },
					{ "4", "รายได้", "text-align:left;" }, { "5", "ค่าใช้จ่าย", "text-align:left;" }, };
			sDt.getShowColum().add(FSearchData.genColumn("หมวดบัญชี", "ACCT_TYPE", "INTEGER", "150px", "ACCT_TYPE", acctDescAndStyle));

			sDt.setInitShow(true);
			sDt.setCallForm(callForm);
			sDt.setCallMethodName(methodName);

			sDt.doPopup();

		} catch (Exception e) {
			Msg.error(e);
		}

	}
	
	/**
	 * เลือกรหัสบัญชี
	 * 
	 * @param acct_id
	 * @param loginBean  ถ้าส่งค่า null มาจะแสดงทั้งหมด
	 * @param event_callBack ชื่อ event ของ windows ที่เรียก ที่จะให้ทำหรือ call กลับหลังจากค้นหาและเลือก
	 */
	public static void popup(String acct_id, LoginBean loginBean, EventListener<Event> event_callBack) {

		try {

			FSearchData sDt = new FSearchData();

			sDt.setWindowsID("SelPop");

			SqlStr sql = new SqlStr();
			sql.addLine("select * from " + TboACCT_NO.tablename);
			sDt.setSelectSql(sql.getSql());

			// fixWhere เอาไว้ส่งพวกรหัสบริษัทที่ login ขณะนั้นๆ
			sDt.getWhereFix().add("COMP_CDE = '" + Fnc.sqlQuote(loginBean.getCOMP_CDE()) + "' ");

			// order by
			sDt.getOrderBy().add("ACCT_ID");

			// return
			sDt.setReturnFieldName("ACCT_ID");

			// Object ที่ใช้เป็นเงื่อนไขการค้นหา
			sDt.getWhereObject().add(FSearchData.whereTextbox("ACCT_ID", "รหัสบัญชี", "ACCT_ID", "150px", "", acct_id));
			sDt.getWhereObject().add(FSearchData.whereTextboxWithOperator("ACCT_NAME", "ชื่อบัญชี", "ACCT_NAME", "150px", "", "", "like1"));

			// show column
			sDt.getShowColum().add(FSearchData.genColumn("รหัสบัญชี", "ACCT_ID", "STRING", "150px", "ACCT_ID", null));
			sDt.getShowColum().add(FSearchData.genColumn("ชื่อบัญชี", "ACCT_NAME", "STRING", "", "ACCT_NAME", null));
			String[][] acctDescAndStyle = { { "1", "สินทรัพย์", "text-align:left;" },
					{ "2", "หนี้สิน", "text-align:left;" }, { "3", "ทุน", "text-align:left;" },
					{ "4", "รายได้", "text-align:left;" }, { "5", "ค่าใช้จ่าย", "text-align:left;" }, };
			sDt.getShowColum().add(FSearchData.genColumn("หมวดบัญชี", "ACCT_TYPE", "INTEGER", "150px", "ACCT_TYPE", acctDescAndStyle));

			sDt.setInitShow(true);
			sDt.setEvent_callBack(event_callBack);

			sDt.doPopup();

		} catch (Exception e) {
			Msg.error(e);
		}

	}

}

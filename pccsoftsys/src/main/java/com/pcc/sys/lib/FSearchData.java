package com.pcc.sys.lib;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Textbox;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.tbo.TboFCOMP;

public class FSearchData {

	public static void testPopup1() {

		try {
			//test
			TboFCOMP tboFcomp = new TboFCOMP();
			tboFcomp.setCOMP_CDE("M03");

			LoginBean loginBean = new LoginBean();
			loginBean.setTboFcomp(tboFcomp);

			FSearchData.testFindVoucher1(loginBean, new FSearchData(), "testCallBack1");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void testCallBack1(String value) {
		System.out.println("testCallBack1 = value : " + value);
	}

	public static void testFindVoucher1(LoginBean loginBean, Object callForm, String callMethodName) {
		//ตัวอย่างการใช้งาน
		try {

			FSearchData sDt = new FSearchData();

			SqlStr sql = new SqlStr();
			sql.addLine("SELECT hh.VOU_TYPE, hh.VOU_NO, hh.POSTDATE, hh.DESCR, trim(coalesce(ff.TITLE,'')  || ' ' ||  coalesce(ff.FNAME_THAI,'') || ' ' || coalesce(ff.LNAME_THAI,'') ) as UPDATEBY , hh.RECSTA");
			sql.addLine("FROM ACGL_HEADER hh");
			sql.addLine("LEFT JOIN FUSER ff on hh.UPBY=ff.USER_ID");
			sDt.setSelectSql(sql.getSql());

			//fixWhere เอาไว้ส่งพวกรหัสบริษัทที่ login ขณะนั้นๆ
			if (loginBean != null) {
				sDt.getWhereFix().add(" hh.COMP_CDE = '" + sqlQuote(loginBean.getCOMP_CDE()) + "' ");
			}
			sDt.getWhereFix().add(" hh.VOU_TYPE='PV' ");

			//group by 
			//..

			//having
			//..

			//order by
			sDt.getOrderBy().add("hh.VOU_TYPE, hh.VOU_NO");

			//return fieldValue
			sDt.setReturnFieldName("VOU_NO");

			//Object ที่ใช้เป็นเงื่อนไขการค้นหา
			sDt.getWhereObject().add(FSearchData.whereTextbox("VOU_NO", "เลขใบสำคัญ", "hh.VOU_NO", "150px", "", ""));
			sDt.getWhereObject().add(FSearchData.whereDatebox("POSTDATE", "วันที่ใบสำคัญ", "hh.POSTDATE", "150px", ""));

			{//== ตัวอย่าง combobox
				Combobox obj = FSearchData.whereCombobox("RECSTA", "สถานะ1", "hh.RECSTA", "150px", "", null);

				//== การเพิ่มเข้าตรงๆ ทีละรายการ
				appendItemToComboBox(obj, "", "ทั้งหมด");
				appendItemToComboBox(obj, "0", "ยังไม่บันทึก");
				appendItemToComboBox(obj, "1", "ยังไม่อนุมัติ");
				appendItemToComboBox(obj, "2", "อนุมัติ");
				appendItemToComboBox(obj, "9", "ยกเลิก");

				sDt.getWhereObject().add(obj);
			}

			{ //== ตัวอย่าง combobox สร้าง array ที่มีอยู่แล้ว ส่วนใหญ่เอาไว้สำหรับพวกที่เป็น constant อยู่แล้ว
				String[][] status2 = {
						{ "0", "ยังไม่บันทึก" },
						{ "1", "ยังไม่อนุมัติ" },
						{ "2", "อนุมัติ" },
						{ "9", "ยกเลิก" },
						//{ "x1", "ยกเลิกอนุมัติ" },
				};
				Combobox obj = FSearchData.whereCombobox("RECSTA2", "สถานะ2", "hh.RECSTA", "150px", "", status2);
				//ตัวอย่าง กรณีค่าว่างๆ มีความหมาย สมมุติ ว่างๆ = ยกเลิกอนุมัติ
				//Combobox obj = FSearchData.whereCombobox("RECSTA2", "สถานะ2", "(case when coalesce(hh.RECSTA,'')='' then 'x1' else hh.RECSTA end)", "150px", "", status2);

				//== การแทรกรายการแรกจากที่มี จากตัวอย่างนี้คือต้องการคำว่าทั้งหมดให้เป็นรายการแรก
				Comboitem citem = new Comboitem("ทั้งหมด");
				citem.setValue("");
				obj.getItems().add(0, citem);//ลำดับที่ 1 เริ่มนับจาก 0,1,2,.....

				sDt.getWhereObject().add(obj);
			}

			sDt.getShowColum().add(FSearchData.genColumn("รหัสสมุดรายวัน", "hh.VOU_TYPE", "STRING", "100px", "hh.VOU_TYPE", null));
			sDt.getShowColum().add(FSearchData.genColumn("เลขใบสำคัญ", "hh.VOU_NO", "STRING", "130px", "hh.VOU_NO", null));
			sDt.getShowColum().add(FSearchData.genColumn("วันที่ใบสำคัญ", "hh.POSTDATE", "DATE", "110px", "hh.POSTDATE", null));
			sDt.getShowColum().add(FSearchData.genColumn("คำอธิบายรายการ", "hh.DESCR", "STRING", "", "hh.DESCR", null));
			sDt.getShowColum().add(FSearchData.genColumn("ผู้ปรับปรุงรายการ", "UPDATEBY", "STRING", "110px", "UPDATEBY", null));
			{
				//ตัวอย่าง แสดงคำอธิบายรายการแทนรหัสและใส่ style ของ cel
				//กรณีไม่ต้องการทำแบบนี้ให้ใช้หลักการ case when .. else .. end as MyField ในระดับ select เลย
				String[][] descAndStyle = {
						{ "0", "ยังไม่บันทึก", "color:red; text-align:left;" },
						{ "1", "ยังไม่อนุมัติ", " text-align:left;" },
						{ "2", "อนุมัติ", "color:blue; text-align:left;" },
						{ "9", "ยกเลิก", "color:red; text-align:left;" } //เนื่องจากเป็นฟิลด์ตัวเลขจึงต้องใส่ text-align:left; เพื่อให้ข้อความชิดซ้ายมือ
				};
				sDt.getShowColum().add(FSearchData.genColumn("สถานะ", "RECSTA", "INTEGER", "100px", "RECSTA", descAndStyle));
			}

			sDt.setInitShow(true);
			sDt.setCallForm(callForm);
			sDt.setCallMethodName(callMethodName);

			sDt.doPopup();

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public static void testPopup2() {

		try {
			//test แบบ checkbox จะ return ค่าเป็น list ของ hasmap ซึ่ง hasmap จะประกอบด้วยฟิลด์ตั้งแต่ 1 ฟิลด์ขึ้นไป
			TboFCOMP tboFcomp = new TboFCOMP();
			tboFcomp.setCOMP_CDE("M03");

			LoginBean loginBean = new LoginBean();
			loginBean.setTboFcomp(tboFcomp);

			FSearchData.testFindVoucher2(loginBean, new FSearchData(), "testCallBack2");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void testCallBack2(java.util.ArrayList<FModelHasMap> value) {
		try {
			System.out.println("testCallBack2 Show data");
			for (FModelHasMap dat : value) {
				System.out.print(dat.getString("VOU_TYPE"));
				System.out.println(dat.getString("VOU_NO"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void testFindVoucher2(LoginBean loginBean, Object callForm, String callMethodName) {
		//ตัวอย่างการใช้งาน
		try {

			FSearchData sDt = new FSearchData();

			SqlStr sql = new SqlStr();
			sql.addLine("SELECT hh.VOU_TYPE, hh.VOU_NO, hh.POSTDATE, hh.DESCR, trim(coalesce(ff.TITLE,'')  || ' ' ||  coalesce(ff.FNAME_THAI,'') || ' ' || coalesce(ff.LNAME_THAI,'') ) as UPDATEBY , hh.RECSTA");
			sql.addLine("FROM ACGL_HEADER hh");
			sql.addLine("LEFT JOIN FUSER ff on hh.UPBY=ff.USER_ID");
			sDt.setSelectSql(sql.getSql());

			//fixWhere เอาไว้ส่งพวกรหัสบริษัทที่ login ขณะนั้นๆ
			if (loginBean != null) {
				sDt.getWhereFix().add(" hh.COMP_CDE = '" + sqlQuote(loginBean.getCOMP_CDE()) + "' ");
			}
			sDt.getWhereFix().add(" hh.VOU_TYPE='PV' ");

			//group by 
			//..

			//having
			//..

			//order by
			sDt.getOrderBy().add("hh.VOU_TYPE, hh.VOU_NO");

			//return fieldname
			sDt.setReturnFieldName("VOU_TYPE\nVOU_NO");

			//Object ที่ใช้เป็นเงื่อนไขการค้นหา
			sDt.getWhereObject().add(FSearchData.whereTextbox("VOU_NO", "เลขใบสำคัญ", "hh.VOU_NO", "150px", "", ""));
			sDt.getWhereObject().add(FSearchData.whereDatebox("POSTDATE", "วันที่ใบสำคัญ", "hh.POSTDATE", "150px", ""));

			{//== ตัวอย่าง combobox
				Combobox obj = FSearchData.whereCombobox("RECSTA", "สถานะ1", "hh.RECSTA", "150px", "", null);

				//== การเพิ่มเข้าตรงๆ ทีละรายการ
				appendItemToComboBox(obj, "", "ทั้งหมด");
				appendItemToComboBox(obj, "0", "ยังไม่บันทึก");
				appendItemToComboBox(obj, "1", "ยังไม่อนุมัติ");
				appendItemToComboBox(obj, "2", "อนุมัติ");
				appendItemToComboBox(obj, "9", "ยกเลิก");

				sDt.getWhereObject().add(obj);
			}

			{ //== ตัวอย่าง combobox สร้าง array ที่มีอยู่แล้ว ส่วนใหญ่เอาไว้สำหรับพวกที่เป็น constant อยู่แล้ว
				String[][] status2 = {
						{ "0", "ยังไม่บันทึก" },
						{ "1", "ยังไม่อนุมัติ" },
						{ "2", "อนุมัติ" },
						{ "9", "ยกเลิก" },
				};
				Combobox obj = FSearchData.whereCombobox("RECSTA2", "สถานะ2", "hh.RECSTA", "150px", "", status2);

				//== การแทรกรายการแรกจากที่มี จากตัวอย่างนี้คือต้องการคำว่าทั้งหมดให้เป็นรายการแรก
				Comboitem citem = new Comboitem("ทั้งหมด");
				citem.setValue("");
				obj.getItems().add(0, citem);//ลำดับที่ 1 เริ่มนับจาก 0,1,2,.....

				sDt.getWhereObject().add(obj);
			}

			sDt.getShowColum().add(FSearchData.genColumn("รหัสสมุดรายวัน", "hh.VOU_TYPE", "STRING", "100px", "hh.VOU_TYPE", null));
			sDt.getShowColum().add(FSearchData.genColumn("เลขใบสำคัญ", "hh.VOU_NO", "STRING", "130px", "hh.VOU_NO", null));
			sDt.getShowColum().add(FSearchData.genColumn("วันที่ใบสำคัญ", "hh.POSTDATE", "DATE", "110px", "hh.POSTDATE", null));
			sDt.getShowColum().add(FSearchData.genColumn("คำอธิบายรายการ", "hh.DESCR", "STRING", "", "hh.DESCR", null));
			sDt.getShowColum().add(FSearchData.genColumn("ผู้ปรับปรุงรายการ", "UPDATEBY", "STRING", "110px", "UPDATEBY", null));
			{ //ตัวอย่าง แสดงคำอธิบายรายการแทนรหัสและใส่ style ของ cel
				String[][] descAndStyle = {
						{ "0", "ยังไม่บันทึก", "color:red; text-align:left;" },
						{ "1", "ยังไม่อนุมัติ", " text-align:left;" },
						{ "2", "อนุมัติ", "color:blue; text-align:left;" },
						{ "9", "ยกเลิก", "color:red; text-align:left;" } //เนื่องจากเป็นฟิลด์ตัวเลขจึงต้องใส่ text-align:left; เพื่อให้ข้อความชิดซ้ายมือ
				};
				sDt.getShowColum().add(FSearchData.genColumn("สถานะ", "RECSTA", "INTEGER", "100px", "RECSTA", descAndStyle));
			}

			sDt.setInitShow(true);
			sDt.setShowCheckbox(true);
			sDt.setCallForm(callForm);
			sDt.setCallMethodName(callMethodName);

			sDt.doPopup();

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public static void testPopup3() {

		try {
			//test  การ return ค่ามากว่า 1 ฟิลด์ จะ return เป็น hasmap 1 อันซึ่งประกอบด้วยหลายๆ ฟิลด์ที่เรากำหนด
			TboFCOMP tboFcomp = new TboFCOMP();
			tboFcomp.setCOMP_CDE("M03");

			LoginBean loginBean = new LoginBean();
			loginBean.setTboFcomp(tboFcomp);

			FSearchData.testFindVoucher3(loginBean, new FSearchData(), "testCallBack3");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void testCallBack3(java.util.ArrayList<FModelHasMap> value) {
		try {
			// value จะมี hasmap แค่แถวเดียว
			System.out.println("testCallBack3 Show data");
			for (FModelHasMap dat : value) {
				System.out.print(dat.getString("VOU_TYPE"));
				System.out.println(dat.getString("VOU_NO"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void testFindVoucher3(LoginBean loginBean, Object callForm, String callMethodName) {
		//ตัวอย่างการใช้งาน
		try {

			FSearchData sDt = new FSearchData();

			SqlStr sql = new SqlStr();
			sql.addLine("SELECT hh.VOU_TYPE, hh.VOU_NO, hh.POSTDATE, hh.DESCR, trim(coalesce(ff.TITLE,'')  || ' ' ||  coalesce(ff.FNAME_THAI,'') || ' ' || coalesce(ff.LNAME_THAI,'') ) as UPDATEBY , hh.RECSTA");
			sql.addLine("FROM ACGL_HEADER hh");
			sql.addLine("LEFT JOIN FUSER ff on hh.UPBY=ff.USER_ID");
			sDt.setSelectSql(sql.getSql());

			//fixWhere เอาไว้ส่งพวกรหัสบริษัทที่ login ขณะนั้นๆ
			if (loginBean != null) {
				sDt.getWhereFix().add(" hh.COMP_CDE = '" + sqlQuote(loginBean.getCOMP_CDE()) + "' ");
			}
			sDt.getWhereFix().add(" hh.VOU_TYPE='PV' ");

			//group by 
			//..

			//having
			//..

			//order by
			sDt.getOrderBy().add("hh.VOU_TYPE, hh.VOU_NO");

			//return fieldValue
			sDt.setReturnFieldName("VOU_TYPE\nVOU_NO");

			//Object ที่ใช้เป็นเงื่อนไขการค้นหา
			sDt.getWhereObject().add(FSearchData.whereTextbox("VOU_NO", "เลขใบสำคัญ", "hh.VOU_NO", "150px", "", ""));
			sDt.getWhereObject().add(FSearchData.whereDatebox("POSTDATE", "วันที่ใบสำคัญ", "hh.POSTDATE", "150px", ""));

			{//== ตัวอย่าง combobox
				Combobox obj = FSearchData.whereCombobox("RECSTA", "สถานะ1", "hh.RECSTA", "150px", "", null);

				//== การเพิ่มเข้าตรงๆ ทีละรายการ
				appendItemToComboBox(obj, "", "ทั้งหมด");
				appendItemToComboBox(obj, "0", "ยังไม่บันทึก");
				appendItemToComboBox(obj, "1", "ยังไม่อนุมัติ");
				appendItemToComboBox(obj, "2", "อนุมัติ");
				appendItemToComboBox(obj, "9", "ยกเลิก");

				sDt.getWhereObject().add(obj);
			}

			{ //== ตัวอย่าง combobox สร้าง array ที่มีอยู่แล้ว ส่วนใหญ่เอาไว้สำหรับพวกที่เป็น constant อยู่แล้ว
				String[][] status2 = {
						{ "0", "ยังไม่บันทึก" },
						{ "1", "ยังไม่อนุมัติ" },
						{ "2", "อนุมัติ" },
						{ "9", "ยกเลิก" },
				};
				Combobox obj = FSearchData.whereCombobox("RECSTA2", "สถานะ2", "hh.RECSTA", "150px", "", status2);

				//== การแทรกรายการแรกจากที่มี จากตัวอย่างนี้คือต้องการคำว่าทั้งหมดให้เป็นรายการแรก
				Comboitem citem = new Comboitem("ทั้งหมด");
				citem.setValue("");
				obj.getItems().add(0, citem);//ลำดับที่ 1 เริ่มนับจาก 0,1,2,.....

				sDt.getWhereObject().add(obj);
			}

			sDt.getShowColum().add(FSearchData.genColumn("รหัสสมุดรายวัน", "hh.VOU_TYPE", "STRING", "100px", "hh.VOU_TYPE", null));
			sDt.getShowColum().add(FSearchData.genColumn("เลขใบสำคัญ", "hh.VOU_NO", "STRING", "130px", "hh.VOU_NO", null));
			sDt.getShowColum().add(FSearchData.genColumn("วันที่ใบสำคัญ", "hh.POSTDATE", "DATE", "110px", "hh.POSTDATE", null));
			sDt.getShowColum().add(FSearchData.genColumn("คำอธิบายรายการ", "hh.DESCR", "STRING", "", "hh.DESCR", null));
			sDt.getShowColum().add(FSearchData.genColumn("ผู้ปรับปรุงรายการ", "UPDATEBY", "STRING", "110px", "UPDATEBY", null));
			{ //ตัวอย่าง แสดงคำอธิบายรายการแทนรหัสและใส่ style ของ cel
				String[][] descAndStyle = {
						{ "0", "ยังไม่บันทึก", "color:red; text-align:left;" },
						{ "1", "ยังไม่อนุมัติ", " text-align:left;" },
						{ "2", "อนุมัติ", "color:blue; text-align:left;" },
						{ "9", "ยกเลิก", "color:red; text-align:left;" } //เนื่องจากเป็นฟิลด์ตัวเลขจึงต้องใส่ text-align:left; เพื่อให้ข้อความชิดซ้ายมือ
				};
				sDt.getShowColum().add(FSearchData.genColumn("สถานะ", "RECSTA", "INTEGER", "100px", "RECSTA", descAndStyle));
			}

			sDt.setInitShow(true);
			sDt.setCallForm(callForm);
			sDt.setCallMethodName(callMethodName);

			sDt.doPopup();

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	private String windowsID = "";

	/**
	 * ความกว้าง windows เช่น 950px ถ้าไม่ระบุจะเท่ากับ 95% ของหน้าจอ 
	 */
	private String windowsWidth = "";

	/**
	 * ตัวอย่าง 
	 select cus.IDNO, tit.TITNAM, cus.TITCDE, cus.THNAME , cus.THNAME, cus.BIRDTE   
	 from ACCT_CUST cus 
	 left join TITLE_TABLE tit on cus.TITCDE=tit.TITCDE
	 */
	private String selectSql = "";

	/**
	 * ตัวอย่าง "cus.compid='C02' "
	 */
	private java.util.List<String> whereFix = new ArrayList<String>();

	/**
	 * ฟิลด์ที่จะ groupBy เช่น "cus.IDNO, tit.TITNAM" หลักการเหมือน fixwhere คือมี alias ด้วยถ้า selectSql มี alias
	 */
	private java.util.List<String> groupBy = new ArrayList<String>();

	/**
	 * เช่น sum(tra.flgnum*tra.TRAQTY) <> 0  ไม่ต้องมีคำว่า having
	 */
	private java.util.List<String> having = new ArrayList<String>();

	/**
	 * ฟิลด์สำหรับเรียง เช่น "cus.IDNO, tit.TITNAM"
	 */
	private java.util.List<String> orderBy = new ArrayList<String>();

	/**
	 * ปัจจุบันใช้ได้กับ Textbox, Datebox, Combobox
	 */
	private java.util.List<HtmlBasedComponent> whereObject = new ArrayList<HtmlBasedComponent>();

	/**
	 * คอลัมภ์ที่แสดง
	 */
	private java.util.List<HashMap> showColum = new ArrayList<HashMap>();

	/**
	 * คอลัมภ์ที่เอาไปใช้ในการเรียงแต่ไม่แสดงใน grid ซึ่งคอลัมภ์นี้จะเกิดการ add เข้าไปใน model ที่แสดงใน grid
	 */
	private java.util.List<HashMap> hideColum = new ArrayList<HashMap>();

	/**
	 * ถ้าไม่ระบุให้เท่ากับ 500 รายการทันที ถ้าไม่จำกัดให้ใส่ -1
	 */
	private int showLimitRow = 500;

	/**
	 * ปัจจุบันได้แต่ฟิลด์ที่เป็น STRING
	 * "IDNO" ไม่ต้องใส่ Alias กรณีมีมากกว่า 1 ฟิลด์ใช้ "IDNO\nAGE\nCUSTNAME"  ใช้ \n กั้นไว้ ซึ่งจะ return ค่าเข้า returnValueMapList แทน
	 */
	private String returnFieldName = "";

	/**
	 * ได้แก่ STRING,INTEGER,NUMBER,DATE,DATETIME ปัจจุบันได้แต่ STRING
	 * ตัวอย่าง "STRING" กรณีมีค่า returnFieldName มากกว่า 1 ฟิลด์ไม่ต้องใส่
	 */
	private String returnFieldType = "STRING";

	/**
	 * ค่าที่ส่งกลับ
	 */
	private Object returnValue = null;

	/**
	 * ส่งค่ากลับกรณีมีหลายๆ ฟิลด์และหลายๆ รายการ
	 */
	private java.util.List<FModelHasMap> returnValueMapList = new ArrayList<FModelHasMap>();

	/**
	 * ให้แสดงข้อมูลเมื่อแสดงหน้าจอ จำนวนรายการจะเท่ากับ showLimitRow ที่กำหนด
	 */
	private boolean initShow = true;

	/**
	 * แสดง checkbox ให้เลือก
	 */
	private boolean showCheckbox = false;

	/**
	 * Form ที่ต้องการให้เรียกกลับ
	 */
	private Object callForm = null;

	/**
	 * ชื่อ method ที่ต้องการให้เรียกใน callForm (Form ที่ต้องการให้เรียกกลับ)
	 */
	private String callMethodName = "";

	public String getWindowsID() {
		return windowsID;
	}

	public void setWindowsID(String windowsID) {
		this.windowsID = windowsID;
	}

	/**
	 * ความกว้าง windows เช่น 950px ถ้าไม่ระบุจะเท่ากับ 95% ของหน้าจอ
	 * @return
	 */
	public String getWindowsWidth() {
		return windowsWidth;
	}

	/**
	 * ความกว้าง windows เช่น 950px ถ้าไม่ระบุจะเท่ากับ 95% ของหน้าจอ 
	 * @param windowsWidth
	 */
	public void setWindowsWidth(String windowsWidth) {
		this.windowsWidth = windowsWidth;
	}

	public String getSelectSql() {
		return selectSql;
	}

	public void setSelectSql(String selectSql) {
		this.selectSql = selectSql;
	}

	public java.util.List<String> getWhereFix() {
		return whereFix;
	}

	public void setWhereFix(java.util.List<String> whereFix) {
		this.whereFix = whereFix;
	}

	public java.util.List<HtmlBasedComponent> getWhereObject() {
		return whereObject;
	}

	public void setWhereObject(java.util.List<HtmlBasedComponent> whereObject) {
		this.whereObject = whereObject;
	}

	public java.util.List<String> getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(java.util.List<String> groupBy) {
		this.groupBy = groupBy;
	}

	public java.util.List<String> getHaving() {
		return having;
	}

	public void setHaving(java.util.List<String> having) {
		this.having = having;
	}

	public java.util.List<String> getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(java.util.List<String> orderBy) {
		this.orderBy = orderBy;
	}

	public java.util.List<HashMap> getShowColum() {
		return showColum;
	}

	public void setShowColum(java.util.List<HashMap> showColum) {
		this.showColum = showColum;
	}

	public java.util.List<HashMap> getHideColum() {
		return hideColum;
	}

	public void setHideColum(java.util.List<HashMap> hideColum) {
		this.hideColum = hideColum;
	}

	public int getShowLimitRow() {
		return showLimitRow;
	}

	public void setShowLimitRow(int showLimitRow) {
		this.showLimitRow = showLimitRow;
	}

	/**
	 * "IDNO" ไม่ต้องใส่ Alias กรณีมีมากกว่า 1 ฟิลด์ใช้ "IDNO\nAGE\nCUSTNAME"  ใช้ \n กั้นไว้ ซึ่งจะ return ค่าเข้า returnValueMapList แทน
	 * @return
	 */
	public String getReturnFieldName() {
		return returnFieldName;
	}

	/**
	 * ตัวอย่าง
	 * "IDNO" ไม่ต้องใส่ Alias กรณีมีมากกว่า 1 ฟิลด์ใช้ "IDNO\nAGE\nCUSTNAME"  ใช้ \n กั้นไว้ ซึ่งจะ return ค่าเข้า returnValueMapList แทน
	 * @param returnFieldName
	 */
	public void setReturnFieldName(String returnFieldName) {
		this.returnFieldName = returnFieldName;
	}

	public String getReturnFieldType() {
		return returnFieldType;
	}

	public void setReturnFieldType(String returnFieldType) {
		this.returnFieldType = returnFieldType;
	}

	public Object getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}

	public java.util.List<FModelHasMap> getReturnValueMapList() {
		return returnValueMapList;
	}

	public void setReturnValueMapList(java.util.List<FModelHasMap> returnValueMapList) {
		this.returnValueMapList = returnValueMapList;
	}

	/**
	 * default = true
	 * @return
	 */
	public boolean isInitShow() {
		return initShow;
	}

	/**
	 * default = true
	 * @param initShow
	 */
	public void setInitShow(boolean initShow) {
		this.initShow = initShow;
	}

	public Object getCallForm() {
		return callForm;
	}

	public void setCallForm(Object callForm) {
		this.callForm = callForm;
	}

	public String getCallMethodName() {
		return callMethodName;
	}

	public void setCallMethodName(String callMethodName) {
		this.callMethodName = callMethodName;
	}

	/**
	 * คอลัมภ์สำหรับแสดง
	 * @param title  title
	 * @param field  ชื่อฟิลด์ เช่น VOU_NO หรือ hh.VOU_NO โดย hh หมายถึง alias ตอน select ก็ได้
	 * @param fieldType ชนิดฟิลด์ข้อมูลอย่างใดอย่างหนึ่งได้แก่ STRING, DATE, DATETIME, NUMBER, INTEGER
	 * @param columWidth ความกว้างคอลัมภ์ เช่น 200px หากว่างๆ จะ auto ส่วนที่เหลือ
	 * @param sortAbleColumn ระบุฟิลด์ใส่ส่วนหัวให้สามารถ click เรียงไปมาได้ เช่น VOU_NO หรือ hh.VOU_NO โดย hh หมายถึง alias ตอน select ก็ได้
	 * @param descAndStyle ถ้าไม่ระบุให้ใส่ null
	 * @return
	 */
	public static HashMap genColumn(String title, String field, String fieldType, String columWidth, String sortAbleColumn, String[][] descAndStyle) {

		HashMap e1 = new HashMap();
		e1.put("FTitle", title);
		e1.put("FField", field);
		e1.put("FFieldType", fieldType);
		e1.put("FColumWidth", columWidth);
		e1.put("FSortAbleColumn", sortAbleColumn);
		e1.put("FdescAndStyle", descAndStyle);

		return e1;

	}

	/**
	 * คอลัมภ์สำหรับซ่อน (ต้องไม่ซ้ำกับ genColumn) ,ดูตัวอย่างได้ใน SelPopup.testFindVoucher.. 
	 * @param field  ชื่อฟิลด์ เช่น VOU_NO หรือ hh.VOU_NO โดย hh หมายถึง alias ตอน select ก็ได้
	 * @return
	 */
	public static HashMap genHideColumn(String field) {

		HashMap e1 = new HashMap();
		e1.put("FField", field);

		return e1;

	}

	public boolean isShowCheckbox() {
		return showCheckbox;
	}

	public void setShowCheckbox(boolean showCheckbox) {
		this.showCheckbox = showCheckbox;
	}

	public void doPopup() throws Exception {

		boolean showPopup = true;

		if (!isEmpty(this.getWindowsID())) { //ป้องกันการเปิดซ้ำในเวลากดเร็วๆ
			if (Executions.getCurrent().getDesktop().getFirstPage().hasFellow(this.getWindowsID())) {
				showPopup = false;
			}
		}

		if (showPopup) {
			//===แบบสร้างจาก .zul
//			FSearchPopup win1 = (FSearchPopup) ZkUtil.callZulFile("/com/pcc/sys/lib/FSearchPopup.zul");
//
//			win1.setTitle("ค้นหา");
//			win1.setPosition("top,center");
//			win1.fSearchData = this;
//			if (!isEmpty(this.getWindowsID())) {
//				win1.setId(this.getWindowsID());
//			}
//			if (!isEmpty(this.getWindowsWidth())) {
//				win1.setWidth(this.getWindowsWidth());
//			}
//
//			win1.doModal();

			//===แบบ code java ล้วนๆ
			FSearchPopup2 win1 = new FSearchPopup2(this);
			win1.setParent(Executions.getCurrent().getDesktop().getFirstPage().getFirstRoot());
			win1.setTitle("ค้นหา");
			win1.setPosition("top,center");
			if (!isEmpty(this.getWindowsID())) {
				win1.setId(this.getWindowsID());
			}
			if (!isEmpty(this.getWindowsWidth())) {
				win1.setWidth(this.getWindowsWidth());
			}

			win1.doModal();
		}

	}

	/**
	 * สร้าง Textbox ตัวนี้จะค้นหาแบบ like searchString%
	 * 
	 * @param id         idของ Component นั้นๆ ห้ามซ้ำกัน
	 * @param whereLabel ข้อความข้างหน้าช่องที่จะค้นหา
	 * @param whereField ฟิลด์ที่จะค้นหา เช่น hh.custno กรณีมี alias ต้องใส่ hh.
	 *                   ด้วย ถ้าไม่มีไม่ต้องใส่
	 * @param width      ความกว้างของ Object ค้นหา
	 * @param style      ถ้าไม่ระบุจะใช้ค่า "background-color: white !important;
	 *                   color: #000000 !important;"
	 * @param initValue  ค่าเริ่มต้น
	 * @return
	 */
	public static Textbox whereTextbox(
			String id, String whereLabel, String whereField,
			String width, String style, String initValue) {

		Textbox obj = new Textbox();
		obj.setId("txt" + id);
		obj.setAttribute("WHERE_LABEL", whereLabel);
		obj.setAttribute("WHERE_FIELD", whereField);
		obj.setAttribute("WHERE_OPERATOR", "like2");

		if (!isEmpty(width)) {
			obj.setWidth(width);
		}

		if (!isEmpty(style)) {
			obj.setStyle(style);
		}

		obj.setValue(initValue);

		return obj;

	}

	/**
	 * สร้าง Textbox
	 * @param id  idของ Component นั้นๆ ห้ามซ้ำกัน
	 * @param whereLabel ข้อความข้างหน้าช่องที่จะค้นหา
	 * @param whereField  ฟิลด์ที่จะค้นหา เช่น hh.custno  กรณีมี alias ต้องใส่ hh. ด้วย ถ้าไม่มีไม่ต้องใส่
	 * @param width ความกว้างของ Object ค้นหา
	 * @param style ถ้าไม่ระบุจะใช้ค่า "background-color: white !important; color: #000000 !important;"
	 * @param initValue ค่าเริ่มต้น
	 * @param operator เช่น like1('%a%' ตรงส่วนใดส่วนหนึ่ง) ,like2('aa%' ตรงตัวหน้า) ,like3('%aa' ตรงตัวหลัง), 
	 * <, <=, >, >=, != , <> , =  *กรณีใช้ like ให้ใช้ like2 เพราะ index ฐานข้อมูลมักเป็นแบบ b-tree
	 * @return
	 */
	public static Textbox whereTextboxWithOperator(
			String id, String whereLabel, String whereField,
			String width, String style, String initValue, String operator) {

		Textbox obj = new Textbox();
		obj.setId("txt" + id);
		obj.setAttribute("WHERE_LABEL", whereLabel);
		obj.setAttribute("WHERE_FIELD", whereField);
		obj.setAttribute("WHERE_OPERATOR", operator);

		if (!isEmpty(width)) {
			obj.setWidth(width);
		}

		if (!isEmpty(style)) {
			obj.setStyle(style);
		}

		obj.setValue(initValue);

		return obj;

	}

	/**
	 * สร้าง Intbox
	 * @param id  idของ Component นั้นๆ ห้ามซ้ำกัน
	 * @param whereLabel ข้อความข้างหน้าช่องที่จะค้นหา
	 * @param whereField  ฟิลด์ที่จะค้นหา เช่น hh.custno  กรณีมี alias ต้องใส่ hh. ด้วย ถ้าไม่มีไม่ต้องใส่
	 * @param width ความกว้างของ Object ค้นหา
	 * @param style ถ้าไม่ระบุจะใช้ค่า "background-color: white !important; color: #000000 !important;"
	 * @return
	 */
	public static Intbox whereIntbox(String id, String whereLabel, String whereField, String width, String style) {

		Intbox obj = new Intbox();
		obj.setId("int" + id);
		obj.setAttribute("WHERE_LABEL", whereLabel);
		obj.setAttribute("WHERE_FIELD", whereField);
		obj.setAttribute("WHERE_OPERATOR", "=");

		if (!isEmpty(width)) {
			obj.setWidth(width);
		}

		if (!isEmpty(style)) {
			obj.setStyle(style);
		}

		return obj;

	}

	/**
	 * สร้าง Intbox
	 * @param id  idของ Component นั้นๆ ห้ามซ้ำกัน
	 * @param whereLabel ข้อความข้างหน้าช่องที่จะค้นหา
	 * @param whereField  ฟิลด์ที่จะค้นหา เช่น hh.custno  กรณีมี alias ต้องใส่ hh. ด้วย ถ้าไม่มีไม่ต้องใส่
	 * @param width ความกว้างของ Object ค้นหา
	 * @param style ถ้าไม่ระบุจะใช้ค่า "background-color: white !important; color: #000000 !important;"
	 * @param operator เช่น <, <=, >, >=, != , <> , =
	 * @return
	 */
	public static Intbox whereIntboxWithOperator(String id, String whereLabel,
			String whereField, String width, String style, String operator) {

		Intbox obj = new Intbox();
		obj.setId("int" + id);
		obj.setAttribute("WHERE_LABEL", whereLabel);
		obj.setAttribute("WHERE_FIELD", whereField);
		obj.setAttribute("WHERE_OPERATOR", operator);

		if (!isEmpty(width)) {
			obj.setWidth(width);
		}

		if (!isEmpty(style)) {
			obj.setStyle(style);
		}

		return obj;

	}

	/**
	 * สร้าง MyDecimalbox
	 * @param id  idของ Component นั้นๆ ห้ามซ้ำกัน
	 * @param whereLabel ข้อความข้างหน้าช่องที่จะค้นหา
	 * @param whereField  ฟิลด์ที่จะค้นหา เช่น hh.custno  กรณีมี alias ต้องใส่ hh. ด้วย ถ้าไม่มีไม่ต้องใส่
	 * @param width ความกว้างของ Object ค้นหา
	 * @param style ถ้าไม่ระบุจะใช้ค่า "background-color: white !important; color: #000000 !important;"
	 * @return
	 */
	public static MyDecimalbox whereMyDecimalbox(String id, String whereLabel, String whereField, String width, String style) {

		MyDecimalbox obj = new MyDecimalbox();
		obj.setId("dec" + id);
		obj.setAttribute("WHERE_LABEL", whereLabel);
		obj.setAttribute("WHERE_FIELD", whereField);
		obj.setAttribute("WHERE_OPERATOR", "=");

		if (!isEmpty(width)) {
			obj.setWidth(width);
		}

		if (!isEmpty(style)) {
			obj.setStyle(style);
		}

		return obj;

	}

	/**
	 * สร้าง MyDecimalbox
	 * @param id  idของ Component นั้นๆ ห้ามซ้ำกัน
	 * @param whereLabel ข้อความข้างหน้าช่องที่จะค้นหา
	 * @param whereField  ฟิลด์ที่จะค้นหา เช่น hh.custno  กรณีมี alias ต้องใส่ hh. ด้วย ถ้าไม่มีไม่ต้องใส่
	 * @param width ความกว้างของ Object ค้นหา
	 * @param style ถ้าไม่ระบุจะใช้ค่า "background-color: white !important; color: #000000 !important;"
	 * @param operator เช่น <, <=, >, >=, != , <> , =
	 * @return
	 */
	public static MyDecimalbox whereMyDecimalboxWithOperator(String id, String whereLabel,
			String whereField, String width, String style, String operator) {

		MyDecimalbox obj = new MyDecimalbox();
		obj.setId("dec" + id);
		obj.setAttribute("WHERE_LABEL", whereLabel);
		obj.setAttribute("WHERE_FIELD", whereField);
		obj.setAttribute("WHERE_OPERATOR", operator);

		if (!isEmpty(width)) {
			obj.setWidth(width);
		}

		if (!isEmpty(style)) {
			obj.setStyle(style);
		}

		return obj;

	}

	/**
	 * สร้าง Datebox
	 * @param id  idของ Component นั้นๆ ห้ามซ้ำกัน
	 * @param whereLabel ข้อความข้างหน้าช่องที่จะค้นหา
	 * @param whereField  ฟิลด์ที่จะค้นหา เช่น hh.custno  กรณีมี alias ต้องใส่ hh. ด้วย ถ้าไม่มีไม่ต้องใส่
	 * @param width ความกว้างของ Object ค้นหา
	 * @param style ถ้าไม่ระบุจะใช้ค่า "background-color: white !important; color: #000000 !important;"
	 * @return
	 */
	public static Datebox whereDatebox(String id, String whereLabel, String whereField, String width, String style) {

		Datebox obj = new Datebox();
		obj.setId("tdb" + id);
		obj.setAttribute("WHERE_LABEL", whereLabel);
		obj.setAttribute("WHERE_FIELD", whereField);
		obj.setAttribute("WHERE_OPERATOR", "=");

		if (!isEmpty(width)) {
			obj.setWidth(width);
		}

		if (!isEmpty(style)) {
			obj.setStyle(style);
		}

		obj.setValue(null);
		obj.setLocale("th_TH");
		obj.setFormat("dd/MM/yyyy");

		return obj;

	}

	/**
	 * สร้าง Datebox
	 * @param id  idของ Component นั้นๆ ห้ามซ้ำกัน
	 * @param whereLabel ข้อความข้างหน้าช่องที่จะค้นหา
	 * @param whereField  ฟิลด์ที่จะค้นหา เช่น hh.custno  กรณีมี alias ต้องใส่ hh. ด้วย ถ้าไม่มีไม่ต้องใส่
	 * @param width ความกว้างของ Object ค้นหา
	 * @param style ถ้าไม่ระบุจะใช้ค่า "background-color: white !important; color: #000000 !important;"
	 * @param operator เช่น <, <=, >, >=, != , <> , =
	 * @return
	 */
	public static Datebox whereDateboxWithOperator(String id, String whereLabel, String whereField,
			String width, String style, String operator) {

		Datebox obj = new Datebox();
		obj.setId("tdb" + id);
		obj.setAttribute("WHERE_LABEL", whereLabel);
		obj.setAttribute("WHERE_FIELD", whereField);
		obj.setAttribute("WHERE_OPERATOR", operator);

		if (!isEmpty(width)) {
			obj.setWidth(width);
		}

		if (!isEmpty(style)) {
			obj.setStyle(style);
		}

		obj.setValue(null);
		obj.setLocale("th_TH");
		obj.setFormat("dd/MM/yyyy");

		return obj;

	}

	/**
	 * สร้าง combobox
	 * @param id  idของ Component นั้นๆ ห้ามซ้ำกัน
	 * @param whereLabel ข้อความข้างหน้าช่องที่จะค้นหา
	 * @param whereField  ฟิลด์ที่จะค้นหา เช่น hh.custno  กรณีมี alias ต้องใส่ hh. ด้วย ถ้าไม่มีไม่ต้องใส่
	 * @param width ความกว้างของ Object ค้นหา
	 * @param style ถ้าไม่ระบุจะใช้ค่า "background-color: white !important; color: #000000 !important;"
	 * @param valueAdd เป็น array 2 มิติ ถ้าไม่ใช้ให้ส่งค่า null เข้ามา
	 * @return
	 */
	public static Combobox whereCombobox(String id, String whereLabel, String whereField,
			String width, String style, String[][] valueAdd) {

		Combobox obj = new Combobox();
		obj.setId("cmb" + id);
		obj.setAttribute("WHERE_LABEL", whereLabel);
		obj.setAttribute("WHERE_FIELD", whereField);
		obj.setAttribute("WHERE_OPERATOR", "=");

		if (!isEmpty(width)) {
			obj.setWidth(width);
		}

		if (!isEmpty(style)) {
			obj.setStyle(style);
		} else {
			obj.setStyle("background-color: white !important; color: #000000 !important;");
		}

		obj.setReadonly(true);
		obj.setAutodrop(true);

		if (valueAdd != null) {
			appendItemToComboBox(obj, valueAdd);
		}

		return obj;

	}

	/**
	 * สร้าง combobox
	 * @param id  idของ Component นั้นๆ ห้ามซ้ำกัน
	 * @param whereLabel ข้อความข้างหน้าช่องที่จะค้นหา
	 * @param whereField  ฟิลด์ที่จะค้นหา เช่น hh.custno  กรณีมี alias ต้องใส่ hh. ด้วย ถ้าไม่มีไม่ต้องใส่
	 * @param width ความกว้างของ Object ค้นหา
	 * @param style ถ้าไม่ระบุจะใช้ค่า "background-color: white !important; color: #000000 !important;"
	 * @param valueAdd เป็น array 2 มิติ ถ้าไม่ใช้ให้ส่งค่า null เข้ามา
	 * @param operator เช่น like1('%a%' ตรงส่วนใดส่วนหนึ่ง) ,like2('aa%' ตรงตัวหน้า) ,like3('%aa' ตรงตัวหลัง), 
	 * <, <=, >, >=, != , <> , = *กรณีใช้ like ให้ใช้ like2 เพราะ index ฐานข้อมูลมักเป็นแบบ b-tree
	 * @return
	 */
	public static Combobox whereComboboxWithOperator(String id, String whereLabel, String whereField,
			String width, String style, String[][] valueAdd, String operator) {

		Combobox obj = new Combobox();
		obj.setId("cmb" + id);
		obj.setAttribute("WHERE_LABEL", whereLabel);
		obj.setAttribute("WHERE_FIELD", whereField);
		obj.setAttribute("WHERE_OPERATOR", operator);

		if (!isEmpty(width)) {
			obj.setWidth(width);
		}

		if (!isEmpty(style)) {
			obj.setStyle(style);
		} else {
			obj.setStyle("background-color: white !important; color: #000000 !important;");
		}

		obj.setReadonly(true);
		obj.setAutodrop(true);

		if (valueAdd != null) {
			appendItemToComboBox(obj, valueAdd);
		}

		return obj;

	}

	public static boolean isEmpty(String str) {
		boolean res = false;
		if (str == null) {
			res = true;
		} else if (str.trim().equals("")) {
			res = true;
		}
		return res;
	}

	public static String getStr(Object obj) {
		if (obj instanceof String) {
			return (String) obj;
		} else {
			return "";
		}
	}

	public static String getStrBigDecimal(BigDecimal amount) {
		if (amount == null) {
			return "";
		}
		DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");
		return decimalFormat.format(amount);
	}

	public static String sqlQuote(String s) {
		return getStr(s).replace("\'", "\'\'");
	}

	/**
	 * เพิ่ม Item เข้า Combobox
	 * @param combobox
	 * @param value
	 * @param label
	 */
	public static void appendItemToComboBox(org.zkoss.zul.Combobox combobox, String value, String label) {
		Comboitem citem = new Comboitem();
		citem.setValue(value);
		citem.setLabel(label);
		combobox.appendChild(citem);
	}

	/**
	 * เพิ่ม Item เข้า Combobox
	 * @param combobox
	 * @param data 
	 */
	public static void appendItemToComboBox(org.zkoss.zul.Combobox combobox, String[][] data) {
		for (String[] str : data) {
			String value = str[0].toString();
			String label = str[1].toString();
			appendItemToComboBox(combobox, value, label);
		}
	}

	private EventListener<Event> event_callBack = null;

	public EventListener<Event> getEvent_callBack() {
		return event_callBack;
	}

	public void setEvent_callBack(EventListener<Event> event_callBack) {
		this.event_callBack = event_callBack;
	}
	
}

package com.pcc.tk.find;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FSearchData;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;

public class FfTKJOB {

	public static void popup(LoginBean loginBean, Object callForm, String methodName) {

		try {

			FSearchData sDt = new FSearchData();

			sDt.setWindowsID("FfTKJOB");

			String sql = """
					select JOBNO,JOBDATE,CUSTCONTACTNO,SUECOSTAMT,FEEAMT,LAWBLACKDATE,
					 LAWBLACKNO,LAWREDDATE,LAWREDNO,JOBSTAT,UPDBY,UPDDTE
					from tkjob
					""";
			sDt.setSelectSql(sql);

			// fixWhere เอาไว้ส่งพวกรหัสบริษัทที่ login ขณะนั้นๆ
			sDt.getWhereFix().add("COMP_CDE = '" + Fnc.sqlQuote(loginBean.getCOMP_CDE()) + "' ");

			// order by
			sDt.getOrderBy().add("JOBNO");

			// return
			sDt.setReturnFieldName("JOBNO");

			// Object ที่ใช้เป็นเงื่อนไขการค้นหา
			sDt.getWhereObject().add(FSearchData.whereTextboxWithOperator("JOBNO", "เลขที่งาน", "JOBNO", "150px", "", "","like2"));
			sDt.getWhereObject().add(FSearchData.whereDatebox("JOBDATE", "วันที่งาน", "JOBDATE", "150px", ""));
			sDt.getWhereObject().add(FSearchData.whereDatebox("LAWBLACKDATE", "วันที่คดีดำ", "LAWBLACKDATE", "150px", ""));
			sDt.getWhereObject().add(FSearchData.whereTextboxWithOperator("LAWBLACKNO", "เลขคดีดำ", "LAWBLACKNO", "150px", "", "","like1"));
			sDt.getWhereObject().add(FSearchData.whereDatebox("LAWREDDATE", "วันที่คดีแดง", "LAWREDDATE", "150px", ""));
			sDt.getWhereObject().add(FSearchData.whereTextboxWithOperator("LAWREDNO", "เลขคดีแดง", "LAWREDNO", "150px", "", "","like1"));

			// show column
			sDt.getShowColum().add(FSearchData.genColumn("เลขที่งาน", "JOBNO", "STRING", "130px", "JOBNO", null));
			sDt.getShowColum().add(FSearchData.genColumn("วันที่งาน", "JOBDATE", "DATE", "130px", "JOBDATE", null));
			sDt.getShowColum().add(FSearchData.genColumn("เลขสัญญาลูกค้า", "CUSTCONTACTNO", "STRING", "", "CUSTCONTACTNO", null));
			sDt.getShowColum().add(FSearchData.genColumn("ทุนทรัพย์ฟ้อง", "SUECOSTAMT", "NUMBER", "", "SUECOSTAMT", null));
			sDt.getShowColum().add(FSearchData.genColumn("ค่าฤชาธรรมเนียม", "FEEAMT", "NUMBER", "", "FEEAMT", null));
			{
				//ตัวอย่าง แสดงคำอธิบายรายการแทนรหัสและใส่ style ของ cel
				//กรณีไม่ต้องการทำแบบนี้ให้ใช้หลักการ case when .. else .. end as MyField ในระดับ select เลย
				String[][] descAndStyle = {
						{ "1", "สมบูรณ์", " text-align:left;" },
						{ "8", "ไม่สมบูรณ์", "color:pink;" },
						{ "9", "ยกเลิก", "color:red;" }
				};
				sDt.getShowColum().add(FSearchData.genColumn("สถานะ", "JOBSTAT", "STRING", "100px", "JOBSTAT", descAndStyle));
			}
			sDt.getShowColum().add(FSearchData.genColumn("ผู้ปรับปรุงรายการ", "UPDBY", "STRING", "150px", "UPDBY", null));
			sDt.getShowColum().add(FSearchData.genColumn("วันปรับปรุงรายการ", "UPDDTE", "DATETIME", "150px", "UPDDTE", null));

			sDt.setInitShow(true);
			sDt.setCallForm(callForm);
			sDt.setCallMethodName(methodName);

			sDt.doPopup();

		} catch (Exception e) {
			Msg.error(e);
		}

	}
	
}

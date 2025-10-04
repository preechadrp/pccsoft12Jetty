package com.pcc.gl.progman;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.gencode.app.FSqlStr;
import com.pcc.api.core.ApiUtil;
import com.pcc.gl.tbm.TbmFCUS_ADDR;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FJRBeanCollectionDataSource;
import com.pcc.sys.lib.FJasperPrintList;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FReport;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.tbm.TbmFCOMPBRANC;
import com.pcc.sys.tbo.TboFCOMPBRANC;

public class ManAcWithold {

	public static String[] getTextTax(LoginBean _loginBean, JSONObject requestpara) throws Exception {

		String txt1 = "";
		if (requestpara.getString("cmbReportType").equals("3")) { // 1=ภงด. 3 (บุคคลธรรมดา)
			txt1 = genTextFileTyp3(_loginBean, requestpara);
		} else if (requestpara.getString("cmbReportType").equals("53")) { // 2=ภงด.53 (นิติบุคคล)
			txt1 = genTextFileTyp53(_loginBean, requestpara);
		} else {
			throw new Exception("พารามิเตอร์ไม่ถูกต้อง");
		}

		String ret = java.util.Base64.getEncoder().encodeToString(txt1.getBytes("windows-874"));
		return new String[] { ret, "txt" };

	}

	private static ResultSet getData(FDbc dbc, LoginBean _loginBean, JSONObject requestpara) throws SQLException {

		int year = requestpara.getInt("intYear") - 543;
		int month = requestpara.getInt("intMonth");
		String reportType = requestpara.getString("cmbReportType");

		FSqlStr sql = new FSqlStr();
		String sqltxt = """
				select aa.*,cus.IDNO,cus.CUSTYP,cus.BRANCH_CODE,cus.TITLE,cus.FNAME,cus.LNAME
				 ,addr.VILAPT1,addr.ROOM1,addr.FLOOR1 ,addr.ADDRESS1 ,addr.MOO1 ,addr.SOI1
				 ,addr.ROAD1,addr.TAMBOLNAME,addr.AMPHURNAME,addr.PROVIN_NAME,addr.ZIPCODE
				 from acgl_vatwh_cr aa
				 left join fcus cus on aa.COMP_CDE=cus.COMP_CDE and aa.CUST_CDE=cus.CUST_CDE
				 left join fcus_addr addr on cus.COMP_CDE=addr.COMP_CDE and cus.CUST_CDE =addr.CUST_CDE and addr.ADDR_TYP ='1'
				""";
		sql.append(sqltxt.trim());
		sql.append(" where aa.COMP_CDE ='" + _loginBean.getCOMP_CDE() + "' ");
		sql.append(" and aa.RECSTA != '9' ");
		//sql.append(" and year(aa.POSTDATE) = " + year); 
		//sql.append(" and month(aa.POSTDATE) = " + month);
		sql.append(" and FYEAR(aa.POSTDATE) = " + year);//firebird 
		sql.append(" and FMONTH(aa.POSTDATE) = " + month);
		if (reportType.equals("3")) {
			sql.append(" and aa.DOC_TYPE = 1 -- 1=ภงด 3");
		} else {
			sql.append(" and aa.DOC_TYPE = 2 -- 2=ภงด53");
		}
		sql.append(" order by aa.POSTDATE");
		System.out.println("getData : " + sql.getSql());
		return dbc.getResultSetFw(sql.getSql());
	}

	/**
	 * ภงด. 3
	 * 
	 * @param _loginBean
	 * @param requestpara
	 * @return
	 * @throws Exception
	 */
	public static String genTextFileTyp3(LoginBean _loginBean, JSONObject requestpara) throws Exception {

		System.out.println("genTextFileTyp3");
		
		try (FDbc dbc = FDbc.connectMasterDb();
				java.sql.ResultSet rs = getData(dbc, _loginBean, requestpara);) {

			List<String> textList = new ArrayList<String>();

			String strCrlf = "\r\n";
			String pipe = "|";

			int seq = 0;
			while (rs.next()) {

				String str_detail = "";

				// ==ลำดับที่ 5 char
				seq++;
				str_detail += pipe + getStringWithLeftZero(seq + "", 5) + pipe;

				// เลขประจำตัวผู้เสียภาษีอากร 13 char
				String strCidnum = Fnc.isEmpty(rs.getString("IDNO")) ? "" : rs.getString("IDNO");
				str_detail += getStringWithLeftZero(strCidnum, 13) + pipe;

				// ==สาขา 5 char
				if (Fnc.getStr(rs.getString("CUSTYP")).equals("2")) { //ประเภทลูกค้า 1=บุคคลธรรมดา , 2=นิติบุคคล
					str_detail += getStringWithLeftZero(Fnc.getStr(rs.getString("BRANCH_CODE")), 5) + pipe;
				} else { // บุคคลธรรมดา
					str_detail += getStringWithLeftZero("0", 5) + pipe;
				}

				// ==คำนำหน้าชื่อ 40 char
				String strTitnam = Fnc.getStr(rs.getString("TITLE"));
				str_detail += getStringWithSpace(strTitnam, 40) + pipe;

				// ==ชื่อ 100 char
				str_detail += getStringWithSpace(Fnc.getStr(rs.getString("FNAME")), 100) + pipe;

				// ==นามสกุล 80 char
				str_detail += getStringWithSpace(Fnc.getStr(rs.getString("LNAME")), 80) + pipe;

				// ==ชื่ออาคาร/หมู่บ้าน 30 char
				str_detail += getStringWithSpace(Fnc.getStr(rs.getString("VILAPT1")), 30) + pipe;

				// == ห้องเลขที่ 10 char
				str_detail += getStringWithSpace(Fnc.getStr(rs.getString("ROOM1")), 10) + pipe;

				// ==ชั้นที่ 3 char
				str_detail += getStringWithSpace(Fnc.getStr(rs.getString("FLOOR1")), 3) + pipe;

				// ==เลขที่ 20 char
				str_detail += getStringWithSpace(Fnc.getStr(rs.getString("ADDRESS1")), 20) + pipe;

				// ==หมูที่ 2 char
				str_detail += getStringWithSpace(Fnc.getStr(rs.getString("MOO1")), 2) + pipe;

				// ==ตรอก/ซอย 30 char
				str_detail += getStringWithSpace(Fnc.getStr(rs.getString("SOI1")), 30) + pipe;

				// ==ถนน 30 char
				str_detail += getStringWithSpace(Fnc.getStr(rs.getString("ROAD1")), 30) + pipe;

				// ==ตำบล/แขวง 30 char
				str_detail += getStringWithSpace(Fnc.getStr(rs.getString("TAMBOLNAME")), 30) + pipe;

				// ==อำเภอ/เขต 30 char
				str_detail += getStringWithSpace(Fnc.getStr(rs.getString("AMPHURNAME")), 30) + pipe;

				// ==จังหวัด 40 char
				str_detail += getStringWithSpace(Fnc.getStr(rs.getString("PROVIN_NAME")), 40) + pipe;

				// ==รหัสไปรษณีย์ 5
				str_detail += getStringWithSpace(rs.getString("ZIPCODE"), 5) + pipe;

				// == วัน เดือน ปี ที่จ่าย (dd/mm/yyyy) พ.ศ.
				str_detail += getStringWithSpace(FnDate.displayDateString(rs.getDate("POSTDATE")), 10) + pipe;

				// == ประเภทเงินได้พึงประเมินที่จ่าย 200 char
				str_detail += getStringWithSpace(Fnc.getStr(rs.getString("WHNAME")), 200) + pipe;

				// == อัตราภาษีร้อยละ (4,2)
				str_detail += getStringWithLeftZero(getNumericFormat(rs.getBigDecimal("VAT_RATE")), 7) + pipe;

				// == จำนวนเงินที่จ่าย (15,2)
				str_detail += getStringWithLeftZero(getNumericFormat(rs.getBigDecimal("BASE_AMT")), 18) + pipe;

				// == จำนวนเงินภาษีที่หัก (15,2)
				str_detail += getStringWithLeftZero(getNumericFormat(rs.getBigDecimal("AMT")), 18) + pipe;

				// == เงื่อนไขการหักภาษี 1 char
				str_detail += getStringWithSpace(Fnc.getStr(rs.getString("WHTYPE")), 1);

				// == 24-35
				str_detail += "|||||||||||";

				// == end
				str_detail += strCrlf;

				textList.add(str_detail);
			}

			if (textList.size() > 0) {
				String ret = "";
				for (String str : textList) {
					ret += str;
				}
				return ret;
			} else {
				throw new Exception("ไม่พบข้อมูล");
			}

		}

	}

	public static String genTextFileTyp53(LoginBean _loginBean, JSONObject requestpara) throws Exception {

		System.out.println("genTextFileTyp53");

		try (FDbc dbc = FDbc.connectMasterDb();
				java.sql.ResultSet rs = getData(dbc, _loginBean, requestpara);) {

			List<String> textList = new ArrayList<String>();

			String strCrlf = "\r\n";
			String pipe = "|";

			int seq = 0;
			while (rs.next()) {

				String str_detail = "";

				// ==ลำดับที่ 5 char
				seq++;
				str_detail += pipe + getStringWithLeftZero(seq + "", 5) + pipe;

				// ==เลขประจำตัวผู้เสียภาษีอากร 13 char
				String strCidnum = Fnc.isEmpty(rs.getString("IDNO")) ? "" : rs.getString("IDNO");
				str_detail += getStringWithLeftZero(strCidnum, 13) + pipe;

				// ==สาขา 5 char
				if (Fnc.getStr(rs.getString("CUSTYP")).equals("2")) { //ประเภทลูกค้า 1=บุคคลธรรมดา , 2=นิติบุคคล
					str_detail += getStringWithLeftZero(Fnc.getStr(rs.getString("BRANCH_CODE")), 5) + pipe;
				} else { // บุคคลธรรมดา
					str_detail += getStringWithLeftZero("0", 5) + pipe;
				}

				// ==คำนำหน้าชื่อ 40 char
				String strTitnam = Fnc.getStr(rs.getString("TITLE"));
				str_detail += getStringWithSpace(strTitnam, 40) + pipe;

				// ==ชื่อ 160 char
				String custName = Fnc.getStr(rs.getString("FNAME")) + " " + Fnc.getStr(rs.getString("LNAME"));
				str_detail += getStringWithSpace(custName, 160) + pipe;

				// ==ชื่ออาคาร/หมู่บ้าน 30 char
				str_detail += getStringWithSpace(Fnc.getStr(rs.getString("VILAPT1")), 30) + pipe;

				// == ห้องเลขที่ 10 char
				str_detail += getStringWithSpace(Fnc.getStr(rs.getString("ROOM1")), 10) + pipe;

				// ==ชั้นที่ 3 char
				str_detail += getStringWithSpace(Fnc.getStr(rs.getString("FLOOR1")), 3) + pipe;

				// ==เลขที่ 20 char
				str_detail += getStringWithSpace(Fnc.getStr(rs.getString("ADDRESS1")), 20) + pipe;

				// ==หมูที่ 2 char
				str_detail += getStringWithSpace(Fnc.getStr(rs.getString("MOO1")), 2) + pipe;

				// ==ตรอก/ซอย 30 char
				str_detail += getStringWithSpace(Fnc.getStr(rs.getString("SOI1")), 30) + pipe;

				// ==ถนน 30 char
				str_detail += getStringWithSpace(Fnc.getStr(rs.getString("ROAD1")), 30) + pipe;

				// ==ตำบล/แขวง 30 char
				str_detail += getStringWithSpace(Fnc.getStr(rs.getString("TAMBOLNAME")), 30) + pipe;

				// ==อำเภอ/เขต 30 char
				str_detail += getStringWithSpace(Fnc.getStr(rs.getString("AMPHURNAME")), 30) + pipe;

				// ==จังหวัด 40 char
				str_detail += getStringWithSpace(Fnc.getStr(rs.getString("PROVIN_NAME")), 40) + pipe;

				// ==รหัสไปรษณีย์ 5
				str_detail += getStringWithSpace(rs.getInt("ZIPCODE") + "", 5) + pipe;

				// == วัน เดือน ปี ที่จ่าย (dd/mm/yyyy) พ.ศ.
				str_detail += getStringWithSpace(FnDate.displayDateString(rs.getDate("POSTDATE")), 10) + pipe;

				// == ประเภทเงินได้พึงประเมินที่จ่าย 200 char
				str_detail += getStringWithSpace(Fnc.getStr(rs.getString("WHNAME")), 200) + pipe;

				// == อัตราภาษีร้อยละ (4,2)
				str_detail += getStringWithLeftZero(getNumericFormat(rs.getBigDecimal("VAT_RATE")), 7) + pipe;

				// == จำนวนเงินที่จ่าย (15,2)
				str_detail += getStringWithLeftZero(getNumericFormat(rs.getBigDecimal("BASE_AMT")), 18) + pipe;

				// == จำนวนเงินภาษีที่หัก (15,2)
				str_detail += getStringWithLeftZero(getNumericFormat(rs.getBigDecimal("AMT")), 18) + pipe;

				// == เงื่อนไขการหักภาษี 1 char
				str_detail += getStringWithSpace(Fnc.getStr(rs.getString("WHTYPE")), 1) + pipe;

				// == 23-34
				str_detail += "|||||||||||";

				// == end
				str_detail += strCrlf;

				textList.add(str_detail);
			}

			if (textList.size() > 0) {
				String ret = "";
				for (String str : textList) {
					ret += str;
				}
				return ret;
			} else {
				throw new Exception("ไม่พบข้อมูล");
			}

		}

	}

	public static String getNumericFormat(BigDecimal amount) throws Exception {
		String ret = "";

		if (Fnc.getBigDecimalValue(amount).compareTo(BigDecimal.ZERO) == 0) {
			ret = "0.00";
		} else {

			String strAmount = Fnc.getStrBigDecimal(amount).replace(",", "");
			int amtLenght = strAmount.length();
			int remainLenght = 19 - amtLenght;

			if (remainLenght > 0) {
				for (int i = 0; i < remainLenght; i++) {
					strAmount = "0" + strAmount;
				}
				ret = strAmount;
			}
		}

		return ret;
	}

	public static String getStringWithSpace(String text, int len) {
		String res = "";
		if (text.trim().length() > len) {
			res = Fnc.getStrLeft(text, len);
		} else {
			res = text;
		}
		return res;
	}

	public static String getStringWithLeftZero(String text, int len) {
		String tmp = "000000000000000000000000000000000000" + text.trim();
		String res = Fnc.getStrRight(tmp, len);
		return res;
	}

	public static String[] getReportService(LoginBean _loginBean, JSONObject requestpara) throws Exception {
		throw new Exception("กำลังพัฒนา");//XXX
	}

	public static String[] getTaxForm(LoginBean _loginBean, JSONObject requestpara) throws Exception {

		int year = requestpara.getInt("intYear") - 543;
		int month = requestpara.getInt("intMonth");
		String reportType = requestpara.getString("cmbReportType");
		java.sql.Date taxdate = ApiUtil.getSqlDate(requestpara, "tdbTaxDate");
		String branc_cde = requestpara.getString("cmbBRANC_CDE");
		String paidByName = requestpara.getString("txtPaidByName");
		String position = requestpara.getString("txtPosition");

		FJasperPrintList jasperPrintList = new FJasperPrintList();

		Map reportParams = new HashMap();
		reportParams.put("USERNAME", _loginBean.getUserFullName());
		reportParams.put("COMPNAME", _loginBean.getCOMP_NAME());
		reportParams.put("COMPTAXID", _loginBean.getTboFcomp().getTAXNO());
		//reportParams.put("BRAN_SEQ", _loginBean.getTboFcomp().getTAXNO());
		reportParams.put("MONTH", month);
		reportParams.put("YEAR", year);
		reportParams.put("MONTH_YEAR", month + "  ปี " + year);
		reportParams.put("PAIDNAME", paidByName);
		reportParams.put("POSITION", position);
		reportParams.put("TAXDATE", taxdate);

		TboFCOMPBRANC fcompbranc = TbmFCOMPBRANC.getBranc(_loginBean.getCOMP_CDE(), Fnc.getIntFromStr(branc_cde));
		if (fcompbranc != null) {
			reportParams.put("BRANID", Fnc.getStr(fcompbranc.getBRANCTAXNO())); // น่าจะต้องเลือกสาขาตอนส่งมาด้วย
			reportParams.put("ADDR1", Fnc.getStr(fcompbranc.getADDR1()));
			reportParams.put("ADDR2", Fnc.getStr(fcompbranc.getADDR2()));
			reportParams.put("TEL1", Fnc.getStr(fcompbranc.getTELNO()));
		}

		try (FDbc dbc = FDbc.connectMasterDb();) {

			java.util.List<FModelHasMap> list_dat = new ArrayList<FModelHasMap>();

			try (java.sql.ResultSet rs = getData(dbc, _loginBean, requestpara);) {
				while (rs.next()) {
					FModelHasMap rec1 = new FModelHasMap();
					rec1.putRecord(rs);

					String address = TbmFCUS_ADDR.custAddress1(dbc, _loginBean.getCOMP_CDE(), rs.getString("CUST_CDE"), "1", true);
					rec1.put("SHOW_CUST_ADDR", address);

					list_dat.add(rec1);
				}
			}

			if (list_dat.size() == 0) {
				throw new Exception("ไม่พบข้อมูล");
			}

			if (reportType.equals("3")) { // ภงด. 3 (บุคคลธรรมดา)

				jasperPrintList.addJasperPrintList(FReport.builtRepByBean(
						"/com/pcc/gl/jasper/AcWithold/AcWitholdTax3_Inform.jrxml",
						reportParams, new FJRBeanCollectionDataSource(list_dat)));

				jasperPrintList.addJasperPrintList(FReport.builtRepByBean(
						"/com/pcc/gl/jasper/AcWithold/AcWitholdTax3_Det.jrxml",
						reportParams, new FJRBeanCollectionDataSource(list_dat)));

			} else if (reportType.equals("53")) { // ภงด.53 (นิติบุคคล)

				jasperPrintList.addJasperPrintList(FReport.builtRepByBean(
						"/com/pcc/gl/jasper/AcWithold/AcWitholdTax53_Inform.jrxml",
						reportParams, new FJRBeanCollectionDataSource(list_dat)));

				jasperPrintList.addJasperPrintList(FReport.builtRepByBean(
						"/com/pcc/gl/jasper/AcWithold/AcWitholdTax53_Det.jrxml",
						reportParams, new FJRBeanCollectionDataSource(list_dat)));

			}

		}

		if (jasperPrintList.getJasperPrintLst().size() > 0) {
			return FReport.exportBase64_of_PDF(jasperPrintList.getJasperPrintLst());
		} else {
			throw new Exception("ไม่พบข้อมูล");
		}

	}

}

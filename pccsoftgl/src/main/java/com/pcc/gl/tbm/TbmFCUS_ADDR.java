package com.pcc.gl.tbm;

import com.pcc.gl.tbf.TbfFCUS_ADDR;
import com.pcc.gl.tbo.TboFCUS_ADDR;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;

public class TbmFCUS_ADDR {

	/**
	 * ที่อยู่ลูกค้าแบบบรรทัดเดียวไม่มีรหัส ปณ.
	 * @param comp_cde
	 * @param cust_cde
	 * @param addr_typ
	 * @param withZipcode  แสดงรหัส ปณ. ด้วย
	 * @return
	 * @throws Exception 
	 */
	public static String custAddress1(String comp_cde, String cust_cde, String addr_typ, boolean withZipcode)
			throws Exception {
		try(FDbc dbc = FDbc.connectMasterDb()){
			return custAddress1(dbc, comp_cde, cust_cde, addr_typ, withZipcode);
		}
	}
	
	/**
	 * ที่อยู่ลูกค้าแบบบรรทัดเดียวไม่มีรหัส ปณ.
	 * @param dbc
	 * @param comp_cde
	 * @param cust_cde
	 * @param addr_typ
	 * @param withZipcode  แสดงรหัส ปณ. ด้วย
	 * @return
	 * @throws Exception 
	 */
	public static String custAddress1(FDbc dbc, String comp_cde, String cust_cde, String addr_typ, boolean withZipcode)
			throws Exception {

		String ret = "";
		TboFCUS_ADDR table1 = new TboFCUS_ADDR();
		table1.setCOMP_CDE(comp_cde);
		table1.setCUST_CDE(cust_cde);
		table1.setADDR_TYP(addr_typ);
		if (TbfFCUS_ADDR.find(dbc, table1)) {
			ret += table1.getVILAPT1();
			ret += " " + table1.getADDRESS1();

			if (!Fnc.isEmpty(table1.getFLOOR1())) {
				ret += " ชั้น " + table1.getFLOOR1();
			}

			if (!Fnc.isEmpty(table1.getROOM1())) {
				ret += " ห้อง " + table1.getROOM1();
			}

			if (!Fnc.isEmpty(table1.getMOO1())) {
				ret += " หมูที่ " + table1.getMOO1();
			}

			if (!Fnc.isEmpty(table1.getSOI1())) {
				ret += " ซอย" + table1.getSOI1();
			}

			if (!Fnc.isEmpty(table1.getROAD1())) {
				ret += " ถนน" + table1.getROAD1();
			}

			if (!Fnc.isEmpty(table1.getTAMBOLNAME())) {
				ret += " " + table1.getTAMBOLNAME();
			}

			if (!Fnc.isEmpty(table1.getAMPHURNAME())) {
				ret += " " + table1.getAMPHURNAME();
			}

			if (!Fnc.isEmpty(table1.getPROVIN_NAME())) {
				ret += " " + table1.getPROVIN_NAME();
			}

			if (withZipcode) {
				ret += " " + table1.getZIPCODE();
			}

		}

		return ret.trim();

	}

	public static String custAddressLine1(String comp_cde, String cust_cde, String addr_typ) throws Exception {

		String ret = "";
		TboFCUS_ADDR table1 = new TboFCUS_ADDR();
		table1.setCOMP_CDE(comp_cde);
		table1.setCUST_CDE(cust_cde);
		table1.setADDR_TYP(addr_typ);
		if (TbfFCUS_ADDR.find(table1)) {
			ret += table1.getVILAPT1();
			ret += " " + table1.getADDRESS1();

			if (!Fnc.isEmpty(table1.getFLOOR1())) {
				ret += " ชั้น " + table1.getFLOOR1();
			}

			if (!Fnc.isEmpty(table1.getROOM1())) {
				ret += " ห้อง " + table1.getROOM1();
			}

			if (!Fnc.isEmpty(table1.getMOO1())) {
				ret += " หมูที่ " + table1.getMOO1();
			}

			if (!Fnc.isEmpty(table1.getSOI1())) {
				ret += " ซอย" + table1.getSOI1();
			}

			if (!Fnc.isEmpty(table1.getROAD1())) {
				ret += " ถนน" + table1.getROAD1();
			}

			if (!Fnc.isEmpty(table1.getTAMBOLNAME())) {
				ret += " " + table1.getTAMBOLNAME();
			}

		}

		return ret;

	}

	public static String custAddressLine2(String comp_cde, String cust_cde, String addr_typ, boolean withZipcode)
			throws Exception {

		String ret = "";
		TboFCUS_ADDR table1 = new TboFCUS_ADDR();
		table1.setCOMP_CDE(comp_cde);
		table1.setCUST_CDE(cust_cde);
		table1.setADDR_TYP(addr_typ);
		if (TbfFCUS_ADDR.find(table1)) {

			if (!Fnc.isEmpty(table1.getAMPHURNAME())) {
				ret += table1.getAMPHURNAME();
			}

			if (!Fnc.isEmpty(table1.getPROVIN_NAME())) {
				ret += " " + table1.getPROVIN_NAME();
			}

			if (withZipcode) {
				ret += " " + table1.getZIPCODE();
			}

		}

		return ret.trim();

	}

	public static String custAddressLine1(TboFCUS_ADDR fcus_addr) throws Exception {

		String ret = "";

		ret += fcus_addr.getVILAPT1();
		ret += " " + fcus_addr.getADDRESS1();

		if (!Fnc.isEmpty(fcus_addr.getFLOOR1())) {
			ret += " ชั้น " + fcus_addr.getFLOOR1();
		}

		if (!Fnc.isEmpty(fcus_addr.getROOM1())) {
			ret += " ห้อง " + fcus_addr.getROOM1();
		}

		if (!Fnc.isEmpty(fcus_addr.getMOO1())) {
			ret += " หมูที่ " + fcus_addr.getMOO1();
		}

		if (!Fnc.isEmpty(fcus_addr.getSOI1())) {
			ret += " ซอย" + fcus_addr.getSOI1();
		}

		if (!Fnc.isEmpty(fcus_addr.getROAD1())) {
			ret += " ถนน" + fcus_addr.getROAD1();
		}

		if (!Fnc.isEmpty(fcus_addr.getTAMBOLNAME())) {
			ret += " " + fcus_addr.getTAMBOLNAME();
		}

		return ret.trim();

	}

	public static String custAddressLine2(TboFCUS_ADDR fcus_addr, boolean withZipcode) throws Exception {

		String ret = "";
		if (!Fnc.isEmpty(fcus_addr.getAMPHURNAME())) {
			ret += fcus_addr.getAMPHURNAME();
		}

		if (!Fnc.isEmpty(fcus_addr.getPROVIN_NAME())) {
			ret += " " + fcus_addr.getPROVIN_NAME();
		}

		if (withZipcode) {
			ret += " " + fcus_addr.getZIPCODE();
		}

		return ret.trim();

	}

	/**
	 * 
	 * @param comp_cde
	 * @param cust_cde
	 * @param addr_typ ประเภทที่อยู่ 1=ตามทะเบียนบ้าน 2=ที่ส่งเอกสาร 3=ที่อยู่ปัจจุบัน
	 * @return
	 * @throws Exception
	 */
	public static TboFCUS_ADDR getAddr(String comp_cde, String cust_cde, String addr_typ) throws Exception {

		TboFCUS_ADDR table1 = new TboFCUS_ADDR();

		table1.setCOMP_CDE(comp_cde);
		table1.setCUST_CDE(cust_cde);
		table1.setADDR_TYP(addr_typ);

		if (TbfFCUS_ADDR.find(table1)) {
			return table1;
		} else {
			return null;
		}

	}

}

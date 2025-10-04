package com.pcc.gl.tbo;

import com.pcc.sys.tbo.TABLE;

public class TboFCUS_ADDR extends TABLE {

	private String COMP_CDE;
	private String CUST_CDE;
	private String ADDR_TYP;
	private String VILAPT1;
	private String ADDRESS1;
	private String FLOOR1;
	private String ROOM1;
	private String MOO1;
	private String SOI1;
	private String ROAD1;
	private String TAMBOLNAME;
	private int AMPHURID = 0;
	private String AMPHURNAME;
	private int PROVIN_ID = 0;
	private String PROVIN_NAME;
	private String ZIPCODE;
	private String CUST_REMARK;

	public static final String tablename = "fcus_addr";

	public String getTableName() {
		return schema + tablename;
	}

	public TboFCUS_ADDR() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) throws Exception {
		if (getStr(comp_cde).length() > 5) {
			throw new Exception("ฟิลด์ FCUS_ADDR.COMP_CDE ข้อมูลยาวเกิน 5 ตัวอักษร ตัวอย่างข้อมูล : \n " + comp_cde + " ");
		}
		this.COMP_CDE = comp_cde;
	}

	public String getCUST_CDE() {
		return this.CUST_CDE;
	}

	public void setCUST_CDE(String cust_cde) throws Exception {
		if (getStr(cust_cde).length() > 15) {
			throw new Exception("ฟิลด์ FCUS_ADDR.CUST_CDE ข้อมูลยาวเกิน 15 ตัวอักษร ตัวอย่างข้อมูล : \n " + cust_cde + " ");
		}
		this.CUST_CDE = cust_cde;
	}

	public String getADDR_TYP() {
		return this.ADDR_TYP;
	}

	public void setADDR_TYP(String addr_typ) throws Exception {
		if (getStr(addr_typ).length() > 1) {
			throw new Exception("ฟิลด์ FCUS_ADDR.ADDR_TYP ข้อมูลยาวเกิน 1 ตัวอักษร ตัวอย่างข้อมูล : \n " + addr_typ + " ");
		}
		this.ADDR_TYP = addr_typ;
	}

	public String getVILAPT1() {
		return this.VILAPT1;
	}

	public void setVILAPT1(String vilapt1) throws Exception {
		if (getStr(vilapt1).length() > 60) {
			throw new Exception("ฟิลด์ FCUS_ADDR.VILAPT1 ข้อมูลยาวเกิน 60 ตัวอักษร ตัวอย่างข้อมูล : \n " + vilapt1 + " ");
		}
		this.VILAPT1 = vilapt1;
	}

	public String getADDRESS1() {
		return this.ADDRESS1;
	}

	public void setADDRESS1(String address1) throws Exception {
		if (getStr(address1).length() > 200) {
			throw new Exception("ฟิลด์ FCUS_ADDR.ADDRESS1 ข้อมูลยาวเกิน 200 ตัวอักษร ตัวอย่างข้อมูล : \n " + address1 + " ");
		}
		this.ADDRESS1 = address1;
	}

	public String getFLOOR1() {
		return this.FLOOR1;
	}

	public void setFLOOR1(String floor1) throws Exception {
		if (getStr(floor1).length() > 5) {
			throw new Exception("ฟิลด์ FCUS_ADDR.FLOOR1 ข้อมูลยาวเกิน 5 ตัวอักษร ตัวอย่างข้อมูล : \n " + floor1 + " ");
		}
		this.FLOOR1 = floor1;
	}

	public String getROOM1() {
		return this.ROOM1;
	}

	public void setROOM1(String room1) throws Exception {
		if (getStr(room1).length() > 10) {
			throw new Exception("ฟิลด์ FCUS_ADDR.ROOM1 ข้อมูลยาวเกิน 10 ตัวอักษร ตัวอย่างข้อมูล : \n " + room1 + " ");
		}
		this.ROOM1 = room1;
	}

	public String getMOO1() {
		return this.MOO1;
	}

	public void setMOO1(String moo1) throws Exception {
		if (getStr(moo1).length() > 5) {
			throw new Exception("ฟิลด์ FCUS_ADDR.MOO1 ข้อมูลยาวเกิน 5 ตัวอักษร ตัวอย่างข้อมูล : \n " + moo1 + " ");
		}
		this.MOO1 = moo1;
	}

	public String getSOI1() {
		return this.SOI1;
	}

	public void setSOI1(String soi1) throws Exception {
		if (getStr(soi1).length() > 30) {
			throw new Exception("ฟิลด์ FCUS_ADDR.SOI1 ข้อมูลยาวเกิน 30 ตัวอักษร ตัวอย่างข้อมูล : \n " + soi1 + " ");
		}
		this.SOI1 = soi1;
	}

	public String getROAD1() {
		return this.ROAD1;
	}

	public void setROAD1(String road1) throws Exception {
		if (getStr(road1).length() > 30) {
			throw new Exception("ฟิลด์ FCUS_ADDR.ROAD1 ข้อมูลยาวเกิน 30 ตัวอักษร ตัวอย่างข้อมูล : \n " + road1 + " ");
		}
		this.ROAD1 = road1;
	}

	public String getTAMBOLNAME() {
		return this.TAMBOLNAME;
	}

	public void setTAMBOLNAME(String tambolname) throws Exception {
		if (getStr(tambolname).length() > 30) {
			throw new Exception("ฟิลด์ FCUS_ADDR.TAMBOLNAME ข้อมูลยาวเกิน 30 ตัวอักษร ตัวอย่างข้อมูล : \n " + tambolname + " ");
		}
		this.TAMBOLNAME = tambolname;
	}

	public int getAMPHURID() {
		return this.AMPHURID;
	}

	public void setAMPHURID(int amphurid) throws Exception {
		this.AMPHURID = amphurid;
	}

	public String getAMPHURNAME() {
		return this.AMPHURNAME;
	}

	public void setAMPHURNAME(String amphurname) throws Exception {
		if (getStr(amphurname).length() > 40) {
			throw new Exception("ฟิลด์ FCUS_ADDR.AMPHURNAME ข้อมูลยาวเกิน 40 ตัวอักษร ตัวอย่างข้อมูล : \n " + amphurname + " ");
		}
		this.AMPHURNAME = amphurname;
	}

	public int getPROVIN_ID() {
		return this.PROVIN_ID;
	}

	public void setPROVIN_ID(int provin_id) throws Exception {
		this.PROVIN_ID = provin_id;
	}

	public String getPROVIN_NAME() {
		return this.PROVIN_NAME;
	}

	public void setPROVIN_NAME(String provin_name) throws Exception {
		if (getStr(provin_name).length() > 100) {
			throw new Exception("ฟิลด์ FCUS_ADDR.PROVIN_NAME ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + provin_name + " ");
		}
		this.PROVIN_NAME = provin_name;
	}

	public String getZIPCODE() {
		return this.ZIPCODE;
	}

	public void setZIPCODE(String zipcode) throws Exception {
		if (getStr(zipcode).length() > 20) {
			throw new Exception("ฟิลด์ FCUS_ADDR.ZIPCODE ข้อมูลยาวเกิน 20 ตัวอักษร ตัวอย่างข้อมูล : \n " + zipcode + " ");
		}
		this.ZIPCODE = zipcode;
	}

	public String getCUST_REMARK() {
		return this.CUST_REMARK;
	}

	public void setCUST_REMARK(String cust_remark) throws Exception {
		if (getStr(cust_remark).length() > 200) {
			throw new Exception("ฟิลด์ FCUS_ADDR.CUST_REMARK ข้อมูลยาวเกิน 200 ตัวอักษร ตัวอย่างข้อมูล : \n " + cust_remark + " ");
		}
		this.CUST_REMARK = cust_remark;
	}

}
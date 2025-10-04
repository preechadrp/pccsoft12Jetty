package com.pcc.sys.tbo;

import java.sql.Timestamp;

public class TboFCOMPBRANC extends TABLE {

	private String COMP_CDE;
	private int BRANC_CDE = 0;
	private String BRANC_NAME;
	private String BRANC_SHORTNAME;
	private String BRANCTAXNO;
	private String ADDR1;
	private String ADDR2;
	private String TELNO;
	private String FAXNO;
	private String INSBY;
	private Timestamp INSDT;
	private String UPBY;
	private Timestamp UPDT;

	public static final String tablename = "fcompbranc";

	public String getTableName() {
		return schema + tablename;
	}

	public TboFCOMPBRANC() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) throws Exception {
		if (getStr(comp_cde).length() > 5) {
			throw new Exception("ฟิลด์ FCOMPBRANC.COMP_CDE ข้อมูลยาวเกิน 5 ตัวอักษร ตัวอย่างข้อมูล : \n " + comp_cde + " ");
		}
		this.COMP_CDE = comp_cde;
	}

	public int getBRANC_CDE() {
		return this.BRANC_CDE;
	}

	public void setBRANC_CDE(int branc_cde) throws Exception {
		this.BRANC_CDE = branc_cde;
	}

	public String getBRANC_NAME() {
		return this.BRANC_NAME;
	}

	public void setBRANC_NAME(String branc_name) throws Exception {
		if (getStr(branc_name).length() > 100) {
			throw new Exception("ฟิลด์ FCOMPBRANC.BRANC_NAME ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + branc_name + " ");
		}
		this.BRANC_NAME = branc_name;
	}

	public String getBRANC_SHORTNAME() {
		return this.BRANC_SHORTNAME;
	}

	public void setBRANC_SHORTNAME(String branc_shortname) throws Exception {
		if (getStr(branc_shortname).length() > 100) {
			throw new Exception("ฟิลด์ FCOMPBRANC.BRANC_SHORTNAME ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + branc_shortname + " ");
		}
		this.BRANC_SHORTNAME = branc_shortname;
	}

	public String getBRANCTAXNO() {
		return this.BRANCTAXNO;
	}

	public void setBRANCTAXNO(String branctaxno) throws Exception {
		if (getStr(branctaxno).length() > 5) {
			throw new Exception("ฟิลด์ FCOMPBRANC.BRANCTAXNO ข้อมูลยาวเกิน 5 ตัวอักษร ตัวอย่างข้อมูล : \n " + branctaxno + " ");
		}
		this.BRANCTAXNO = branctaxno;
	}

	public String getADDR1() {
		return this.ADDR1;
	}

	public void setADDR1(String addr1) throws Exception {
		if (getStr(addr1).length() > 200) {
			throw new Exception("ฟิลด์ FCOMPBRANC.ADDR1 ข้อมูลยาวเกิน 200 ตัวอักษร ตัวอย่างข้อมูล : \n " + addr1 + " ");
		}
		this.ADDR1 = addr1;
	}

	public String getADDR2() {
		return this.ADDR2;
	}

	public void setADDR2(String addr2) throws Exception {
		if (getStr(addr2).length() > 200) {
			throw new Exception("ฟิลด์ FCOMPBRANC.ADDR2 ข้อมูลยาวเกิน 200 ตัวอักษร ตัวอย่างข้อมูล : \n " + addr2 + " ");
		}
		this.ADDR2 = addr2;
	}

	public String getTELNO() {
		return this.TELNO;
	}

	public void setTELNO(String telno) throws Exception {
		if (getStr(telno).length() > 200) {
			throw new Exception("ฟิลด์ FCOMPBRANC.TELNO ข้อมูลยาวเกิน 200 ตัวอักษร ตัวอย่างข้อมูล : \n " + telno + " ");
		}
		this.TELNO = telno;
	}

	public String getFAXNO() {
		return this.FAXNO;
	}

	public void setFAXNO(String faxno) throws Exception {
		if (getStr(faxno).length() > 200) {
			throw new Exception("ฟิลด์ FCOMPBRANC.FAXNO ข้อมูลยาวเกิน 200 ตัวอักษร ตัวอย่างข้อมูล : \n " + faxno + " ");
		}
		this.FAXNO = faxno;
	}

	public String getINSBY() {
		return this.INSBY;
	}

	public void setINSBY(String insby) throws Exception {
		if (getStr(insby).length() > 30) {
			throw new Exception("ฟิลด์ FCOMPBRANC.INSBY ข้อมูลยาวเกิน 30 ตัวอักษร ตัวอย่างข้อมูล : \n " + insby + " ");
		}
		this.INSBY = insby;
	}

	public Timestamp getINSDT() {
		return this.INSDT;
	}

	public void setINSDT(Timestamp insdt) throws Exception {
		this.INSDT = insdt;
	}

	public String getUPBY() {
		return this.UPBY;
	}

	public void setUPBY(String upby) throws Exception {
		if (getStr(upby).length() > 30) {
			throw new Exception("ฟิลด์ FCOMPBRANC.UPBY ข้อมูลยาวเกิน 30 ตัวอักษร ตัวอย่างข้อมูล : \n " + upby + " ");
		}
		this.UPBY = upby;
	}

	public Timestamp getUPDT() {
		return this.UPDT;
	}

	public void setUPDT(Timestamp updt) throws Exception {
		this.UPDT = updt;
	}
}

package com.pcc.sys.tbo;

import java.sql.Date;
import java.sql.Timestamp;

public class TboFCOMP extends TABLE {

	private String COMP_CDE;
	private String COMP_NAME;
	private String COMP_SHORTNAME;
	private String TAXNO;
	private Date OPENCOMP;
	private int GLMONTH = 0;
	private int GLDAY = 0;
	private String INSBY;
	private Timestamp INSDT;
	private String UPBY;
	private Timestamp UPDT;

	public static final String tablename = "fcomp";

	public String getTableName() {
		return schema + tablename;
	}

	public TboFCOMP() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) throws Exception {
		if (getStr(comp_cde).length() > 5) {
			throw new Exception("ฟิลด์ FCOMP.COMP_CDE ข้อมูลยาวเกิน 5 ตัวอักษร ตัวอย่างข้อมูล : \n " + comp_cde + " ");
		}
		this.COMP_CDE = comp_cde;
	}

	public String getCOMP_NAME() {
		return this.COMP_NAME;
	}

	public void setCOMP_NAME(String comp_name) throws Exception {
		if (getStr(comp_name).length() > 100) {
			throw new Exception("ฟิลด์ FCOMP.COMP_NAME ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + comp_name + " ");
		}
		this.COMP_NAME = comp_name;
	}

	public String getCOMP_SHORTNAME() {
		return this.COMP_SHORTNAME;
	}

	public void setCOMP_SHORTNAME(String comp_shortname) throws Exception {
		if (getStr(comp_shortname).length() > 100) {
			throw new Exception("ฟิลด์ FCOMP.COMP_SHORTNAME ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + comp_shortname + " ");
		}
		this.COMP_SHORTNAME = comp_shortname;
	}

	public String getTAXNO() {
		return this.TAXNO;
	}

	public void setTAXNO(String taxno) throws Exception {
		if (getStr(taxno).length() > 100) {
			throw new Exception("ฟิลด์ FCOMP.TAXNO ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + taxno + " ");
		}
		this.TAXNO = taxno;
	}

	public Date getOPENCOMP() {
		return this.OPENCOMP;
	}

	public void setOPENCOMP(Date opencomp) throws Exception {
		this.OPENCOMP = opencomp;
	}

	public int getGLMONTH() {
		return this.GLMONTH;
	}

	public void setGLMONTH(int glmonth) throws Exception {
		this.GLMONTH = glmonth;
	}

	public int getGLDAY() {
		return this.GLDAY;
	}

	public void setGLDAY(int glday) throws Exception {
		this.GLDAY = glday;
	}

	public String getINSBY() {
		return this.INSBY;
	}

	public void setINSBY(String insby) throws Exception {
		if (getStr(insby).length() > 30) {
			throw new Exception("ฟิลด์ FCOMP.INSBY ข้อมูลยาวเกิน 30 ตัวอักษร ตัวอย่างข้อมูล : \n " + insby + " ");
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
			throw new Exception("ฟิลด์ FCOMP.UPBY ข้อมูลยาวเกิน 30 ตัวอักษร ตัวอย่างข้อมูล : \n " + upby + " ");
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
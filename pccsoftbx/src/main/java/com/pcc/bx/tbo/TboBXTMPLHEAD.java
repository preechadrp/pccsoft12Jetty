package com.pcc.bx.tbo;

import java.sql.Timestamp;

import com.pcc.sys.tbo.TABLE;

public class TboBXTMPLHEAD extends TABLE {
	private String COMP_CDE;
	private int TMPLCDE = 0;
	private String DOCPREFIX;
	private String TMPLNAME;
	private String POSTCOST;
	private String UPBY;
	private Timestamp UPDT;

	public static final String tablename = "bxtmplhead";

	public String getTableName() {
		return schema + tablename;
	}

	public TboBXTMPLHEAD() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) throws Exception {
		if (getStr(comp_cde).length() > 5) {
			throw new Exception("ฟิลด์ BXTMPLHEAD.COMP_CDE ข้อมูลยาวเกิน 5 ตัวอักษร ตัวอย่างข้อมูล : \n " + comp_cde + " ");
		}
		this.COMP_CDE = comp_cde;
	}

	public int getTMPLCDE() {
		return this.TMPLCDE;
	}

	public void setTMPLCDE(int tmplcde) throws Exception {
		this.TMPLCDE = tmplcde;
	}

	public String getDOCPREFIX() {
		return this.DOCPREFIX;
	}

	public void setDOCPREFIX(String docprefix) throws Exception {
		if (getStr(docprefix).length() > 2) {
			throw new Exception("ฟิลด์ BXTMPLHEAD.DOCPREFIX ข้อมูลยาวเกิน 2 ตัวอักษร ตัวอย่างข้อมูล : \n " + docprefix + " ");
		}
		this.DOCPREFIX = docprefix;
	}

	public String getTMPLNAME() {
		return this.TMPLNAME;
	}

	public void setTMPLNAME(String tmplname) throws Exception {
		if (getStr(tmplname).length() > 250) {
			throw new Exception("ฟิลด์ BXTMPLHEAD.TMPLNAME ข้อมูลยาวเกิน 250 ตัวอักษร ตัวอย่างข้อมูล : \n " + tmplname + " ");
		}
		this.TMPLNAME = tmplname;
	}

	public String getPOSTCOST() {
		return this.POSTCOST;
	}

	public void setPOSTCOST(String postcost) throws Exception {
		if (getStr(postcost).length() > 1) {
			throw new Exception("ฟิลด์ BXTMPLHEAD.POSTCOST ข้อมูลยาวเกิน 1 ตัวอักษร ตัวอย่างข้อมูล : \n " + postcost + " ");
		}
		this.POSTCOST = postcost;
	}

	public String getUPBY() {
		return this.UPBY;
	}

	public void setUPBY(String upby) throws Exception {
		if (getStr(upby).length() > 30) {
			throw new Exception("ฟิลด์ BXTMPLHEAD.UPBY ข้อมูลยาวเกิน 30 ตัวอักษร ตัวอย่างข้อมูล : \n " + upby + " ");
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
package com.pcc.bx.tbo;

import java.sql.Timestamp;

import com.pcc.sys.tbo.TABLE;

public class TboBXTMPLDETAIL extends TABLE {

	private String COMP_CDE;
	private int TMPLCDE = 0;
	private int TMPLSEQ = 0;
	private String DOCHEAD;
	private String COPYFOR;
	private String UPBY;
	private Timestamp UPDT;

	public static final String tablename = "bxtmpldetail";

	public String getTableName() {
		return schema + tablename;
	}

	public TboBXTMPLDETAIL() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) throws Exception {
		if (getStr(comp_cde).length() > 5) {
			throw new Exception("ฟิลด์ BXTMPLDETAIL.COMP_CDE ข้อมูลยาวเกิน 5 ตัวอักษร ตัวอย่างข้อมูล : \n " + comp_cde + " ");
		}
		this.COMP_CDE = comp_cde;
	}

	public int getTMPLCDE() {
		return this.TMPLCDE;
	}

	public void setTMPLCDE(int tmplcde) throws Exception {
		this.TMPLCDE = tmplcde;
	}

	public int getTMPLSEQ() {
		return this.TMPLSEQ;
	}

	public void setTMPLSEQ(int tmplseq) throws Exception {
		this.TMPLSEQ = tmplseq;
	}

	public String getDOCHEAD() {
		return this.DOCHEAD;
	}

	public void setDOCHEAD(String dochead) throws Exception {
		if (getStr(dochead).length() > 250) {
			throw new Exception("ฟิลด์ BXTMPLDETAIL.DOCHEAD ข้อมูลยาวเกิน 250 ตัวอักษร ตัวอย่างข้อมูล : \n " + dochead + " ");
		}
		this.DOCHEAD = dochead;
	}

	public String getCOPYFOR() {
		return this.COPYFOR;
	}

	public void setCOPYFOR(String copyfor) throws Exception {
		if (getStr(copyfor).length() > 250) {
			throw new Exception("ฟิลด์ BXTMPLDETAIL.COPYFOR ข้อมูลยาวเกิน 250 ตัวอักษร ตัวอย่างข้อมูล : \n " + copyfor + " ");
		}
		this.COPYFOR = copyfor;
	}

	public String getUPBY() {
		return this.UPBY;
	}

	public void setUPBY(String upby) throws Exception {
		if (getStr(upby).length() > 30) {
			throw new Exception("ฟิลด์ BXTMPLDETAIL.UPBY ข้อมูลยาวเกิน 30 ตัวอักษร ตัวอย่างข้อมูล : \n " + upby + " ");
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
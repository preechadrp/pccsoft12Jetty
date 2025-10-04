package com.pcc.tk.tbo;

import java.sql.Timestamp;

import com.pcc.sys.tbo.TABLE;

public class TboTKJOBDOCS extends TABLE {

	private String COMP_CDE;
	private String JOBNO;
	private int DOCSEQ = 0;
	private String DOCDESC;
	private int IMGSEQ = 0;
	private String IMGTYPE;
	private String UPDBY;
	private Timestamp UPDDTE;

	public static final String tablename = "tkjobdocs";

	public String getTableName() {
		return schema + tablename;
	}

	public TboTKJOBDOCS() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) throws Exception {
		if (getStr(comp_cde).length() > 5) {
			throw new Exception("ฟิลด์ TKJOBDOCS.COMP_CDE ข้อมูลยาวเกิน 5 ตัวอักษร ตัวอย่างข้อมูล : \n " + comp_cde + " ");
		}
		this.COMP_CDE = comp_cde;
	}

	public String getJOBNO() {
		return this.JOBNO;
	}

	public void setJOBNO(String jobno) throws Exception {
		if (getStr(jobno).length() > 20) {
			throw new Exception("ฟิลด์ TKJOBDOCS.JOBNO ข้อมูลยาวเกิน 20 ตัวอักษร ตัวอย่างข้อมูล : \n " + jobno + " ");
		}
		this.JOBNO = jobno;
	}

	public int getDOCSEQ() {
		return this.DOCSEQ;
	}

	public void setDOCSEQ(int docseq) throws Exception {
		this.DOCSEQ = docseq;
	}

	public String getDOCDESC() {
		return this.DOCDESC;
	}

	public void setDOCDESC(String docdesc) throws Exception {
		if (getStr(docdesc).length() > 250) {
			throw new Exception("ฟิลด์ TKJOBDOCS.DOCDESC ข้อมูลยาวเกิน 250 ตัวอักษร ตัวอย่างข้อมูล : \n " + docdesc + " ");
		}
		this.DOCDESC = docdesc;
	}

	public int getIMGSEQ() {
		return this.IMGSEQ;
	}

	public void setIMGSEQ(int imgseq) throws Exception {
		this.IMGSEQ = imgseq;
	}

	public String getIMGTYPE() {
		return this.IMGTYPE;
	}

	public void setIMGTYPE(String imgtype) throws Exception {
		if (getStr(imgtype).length() > 50) {
			throw new Exception("ฟิลด์ TKJOBDOCS.IMGTYPE ข้อมูลยาวเกิน 50 ตัวอักษร ตัวอย่างข้อมูล : \n " + imgtype + " ");
		}
		this.IMGTYPE = imgtype;
	}

	public String getUPDBY() {
		return this.UPDBY;
	}

	public void setUPDBY(String updby) throws Exception {
		if (getStr(updby).length() > 100) {
			throw new Exception("ฟิลด์ TKJOBDOCS.UPDBY ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + updby + " ");
		}
		this.UPDBY = updby;
	}

	public Timestamp getUPDDTE() {
		return this.UPDDTE;
	}

	public void setUPDDTE(Timestamp upddte) throws Exception {
		this.UPDDTE = upddte;
	}
}
package com.pcc.tk.tbo;

import java.sql.Timestamp;

import com.pcc.sys.tbo.TABLE;

public class TboTKJOBCODE extends TABLE {

	private String COMP_CDE;
	private int JOBCODE = 0;
	private String JOBNAME;
	private String UPDBY;
	private Timestamp UPDDTE;

	public static final String tablename = "tkjobcode";

	public String getTableName() {
		return schema + tablename;
	}

	public TboTKJOBCODE() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) throws Exception {
		if (getStr(comp_cde).length() > 5) {
			throw new Exception("ฟิลด์ TKJOBCODE.COMP_CDE ข้อมูลยาวเกิน 5 ตัวอักษร ตัวอย่างข้อมูล : \n " + comp_cde + " ");
		}
		this.COMP_CDE = comp_cde;
	}

	public int getJOBCODE() {
		return this.JOBCODE;
	}

	public void setJOBCODE(int jobcode) throws Exception {
		this.JOBCODE = jobcode;
	}

	public String getJOBNAME() {
		return this.JOBNAME;
	}

	public void setJOBNAME(String jobname) throws Exception {
		if (getStr(jobname).length() > 250) {
			throw new Exception("ฟิลด์ TKJOBCODE.JOBNAME ข้อมูลยาวเกิน 250 ตัวอักษร ตัวอย่างข้อมูล : \n " + jobname + " ");
		}
		this.JOBNAME = jobname;
	}

	public String getUPDBY() {
		return this.UPDBY;
	}

	public void setUPDBY(String updby) throws Exception {
		if (getStr(updby).length() > 100) {
			throw new Exception("ฟิลด์ TKJOBCODE.UPDBY ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + updby + " ");
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
package com.pcc.tk.tbo;

import java.sql.Timestamp;
import com.pcc.sys.tbo.TABLE;

public class TboTKJOBSTAT extends TABLE {

	private String COMP_CDE;
	private int JOBSTATID = 0;
	private String JOBSTATNAME;
	private String UPDBY;
	private Timestamp UPDDTE;

	public static final String tablename = "tkjobstat";

	public String getTableName() {
		return schema + tablename;
	}

	public TboTKJOBSTAT() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) throws Exception {
		if (getStr(comp_cde).length() > 5) {
			throw new Exception("ฟิลด์ TKJOBSTAT.COMP_CDE ข้อมูลยาวเกิน 5 ตัวอักษร ตัวอย่างข้อมูล : \n " + comp_cde + " ");
		}
		this.COMP_CDE = comp_cde;
	}

	public int getJOBSTATID() {
		return this.JOBSTATID;
	}

	public void setJOBSTATID(int jobstatid) throws Exception {
		this.JOBSTATID = jobstatid;
	}

	public String getJOBSTATNAME() {
		return this.JOBSTATNAME;
	}

	public void setJOBSTATNAME(String jobstatname) throws Exception {
		if (getStr(jobstatname).length() > 250) {
			throw new Exception("ฟิลด์ TKJOBSTAT.JOBSTATNAME ข้อมูลยาวเกิน 250 ตัวอักษร ตัวอย่างข้อมูล : \n " + jobstatname + " ");
		}
		this.JOBSTATNAME = jobstatname;
	}

	public String getUPDBY() {
		return this.UPDBY;
	}

	public void setUPDBY(String updby) throws Exception {
		if (getStr(updby).length() > 100) {
			throw new Exception("ฟิลด์ TKJOBSTAT.UPDBY ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + updby + " ");
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
package com.pcc.tk.tbo;

import java.sql.Timestamp;

import com.pcc.sys.tbo.TABLE;

public class TboTKZONE extends TABLE {

	private String COMP_CDE;
	private int ZONEID = 0;
	private String ZONENAME;
	private String ZONEHUB;
	private String UPDBY;
	private Timestamp UPDDTE;

	public static final String tablename = "tkzone";

	public String getTableName() {
		return schema + tablename;
	}

	public TboTKZONE() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) throws Exception {
		if (getStr(comp_cde).length() > 5) {
			throw new Exception("ฟิลด์ TKZONE.COMP_CDE ข้อมูลยาวเกิน 5 ตัวอักษร ตัวอย่างข้อมูล : \n " + comp_cde + " ");
		}
		this.COMP_CDE = comp_cde;
	}

	public int getZONEID() {
		return this.ZONEID;
	}

	public void setZONEID(int zoneid) throws Exception {
		this.ZONEID = zoneid;
	}

	public String getZONENAME() {
		return this.ZONENAME;
	}

	public void setZONENAME(String zonename) throws Exception {
		if (getStr(zonename).length() > 250) {
			throw new Exception("ฟิลด์ TKZONE.ZONENAME ข้อมูลยาวเกิน 250 ตัวอักษร ตัวอย่างข้อมูล : \n " + zonename + " ");
		}
		this.ZONENAME = zonename;
	}

	public String getZONEHUB() {
		return this.ZONEHUB;
	}

	public void setZONEHUB(String zonehub) throws Exception {
		if (getStr(zonehub).length() > 10) {
			throw new Exception("ฟิลด์ TKZONE.ZONEHUB ข้อมูลยาวเกิน 10 ตัวอักษร ตัวอย่างข้อมูล : \n " + zonehub + " ");
		}
		this.ZONEHUB = zonehub;
	}

	public String getUPDBY() {
		return this.UPDBY;
	}

	public void setUPDBY(String updby) throws Exception {
		if (getStr(updby).length() > 100) {
			throw new Exception("ฟิลด์ TKZONE.UPDBY ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + updby + " ");
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
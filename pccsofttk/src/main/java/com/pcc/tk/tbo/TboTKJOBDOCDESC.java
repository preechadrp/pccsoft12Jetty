package com.pcc.tk.tbo;

import com.pcc.sys.tbo.TABLE;

public class TboTKJOBDOCDESC extends TABLE {

	private String COMP_CDE;
	private String SYS_CDE;
	private String DOCDESC;

	public static final String tablename = "tkjobdocdesc";

	public String getTableName() {
		return schema + tablename;
	}

	public TboTKJOBDOCDESC() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) throws Exception {
		if (getStr(comp_cde).length() > 5) {
			throw new Exception("ฟิลด์ TKJOBDOCDESC.COMP_CDE ข้อมูลยาวเกิน 5 ตัวอักษร ตัวอย่างข้อมูล : \n " + comp_cde + " ");
		}
		this.COMP_CDE = comp_cde;
	}

	public String getSYS_CDE() {
		return this.SYS_CDE;
	}

	public void setSYS_CDE(String sys_cde) throws Exception {
		if (getStr(sys_cde).length() > 50) {
			throw new Exception("ฟิลด์ TKJOBDOCDESC.SYS_CDE ข้อมูลยาวเกิน 50 ตัวอักษร ตัวอย่างข้อมูล : \n " + sys_cde + " ");
		}
		this.SYS_CDE = sys_cde;
	}

	public String getDOCDESC() {
		return this.DOCDESC;
	}

	public void setDOCDESC(String docdesc) throws Exception {
		if (getStr(docdesc).length() > 250) {
			throw new Exception("ฟิลด์ TKJOBDOCDESC.DOCDESC ข้อมูลยาวเกิน 250 ตัวอักษร ตัวอย่างข้อมูล : \n " + docdesc + " ");
		}
		this.DOCDESC = docdesc;
	}
}
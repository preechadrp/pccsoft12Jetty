package com.pcc.tk.tbo;

import java.sql.Timestamp;

import com.pcc.sys.tbo.TABLE;

public class TboTKEXPENSES extends TABLE {

	private String COMP_CDE;
	private int EXPENSESID = 0;
	private String EXPENSESNAME;
	private String UPDBY;
	private Timestamp UPDDTE;

	public static final String tablename = "tkexpenses";

	public String getTableName() {
		return schema + tablename;
	}

	public TboTKEXPENSES() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) throws Exception {
		if (getStr(comp_cde).length() > 5) {
			throw new Exception("ฟิลด์ TKEXPENSES.COMP_CDE ข้อมูลยาวเกิน 5 ตัวอักษร ตัวอย่างข้อมูล : \n " + comp_cde + " ");
		}
		this.COMP_CDE = comp_cde;
	}

	public int getEXPENSESID() {
		return this.EXPENSESID;
	}

	public void setEXPENSESID(int expensesid) throws Exception {
		this.EXPENSESID = expensesid;
	}

	public String getEXPENSESNAME() {
		return this.EXPENSESNAME;
	}

	public void setEXPENSESNAME(String expensesname) throws Exception {
		if (getStr(expensesname).length() > 250) {
			throw new Exception("ฟิลด์ TKEXPENSES.EXPENSESNAME ข้อมูลยาวเกิน 250 ตัวอักษร ตัวอย่างข้อมูล : \n " + expensesname + " ");
		}
		this.EXPENSESNAME = expensesname;
	}

	public String getUPDBY() {
		return this.UPDBY;
	}

	public void setUPDBY(String updby) throws Exception {
		if (getStr(updby).length() > 100) {
			throw new Exception("ฟิลด์ TKEXPENSES.UPDBY ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + updby + " ");
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
package com.pcc.tk.tbo;

import java.sql.Timestamp;
import com.pcc.sys.tbo.TABLE;

public class TboTKCOURT extends TABLE {

	private String COMP_CDE;
	private int COURTID = 0;
	private String COURTNAME;
	private String UPDBY;
	private Timestamp UPDDTE;

	public static final String tablename = "tkcourt";

	public String getTableName() {
		return schema + tablename;
	}

	public TboTKCOURT() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) throws Exception {
		if (getStr(comp_cde).length() > 5) {
			throw new Exception("ฟิลด์ TKCOURT.COMP_CDE ข้อมูลยาวเกิน 5 ตัวอักษร ตัวอย่างข้อมูล : \n " + comp_cde + " ");
		}
		this.COMP_CDE = comp_cde;
	}

	public int getCOURTID() {
		return this.COURTID;
	}

	public void setCOURTID(int courtid) throws Exception {
		this.COURTID = courtid;
	}

	public String getCOURTNAME() {
		return this.COURTNAME;
	}

	public void setCOURTNAME(String courtname) throws Exception {
		if (getStr(courtname).length() > 250) {
			throw new Exception("ฟิลด์ TKCOURT.COURTNAME ข้อมูลยาวเกิน 250 ตัวอักษร ตัวอย่างข้อมูล : \n " + courtname + " ");
		}
		this.COURTNAME = courtname;
	}

	public String getUPDBY() {
		return this.UPDBY;
	}

	public void setUPDBY(String updby) throws Exception {
		if (getStr(updby).length() > 100) {
			throw new Exception("ฟิลด์ TKCOURT.UPDBY ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + updby + " ");
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
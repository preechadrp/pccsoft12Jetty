package com.pcc.tk.tbo;

import java.sql.Timestamp;
import com.pcc.sys.tbo.TABLE;

public class TboTKLAWSTAT extends TABLE {

	private String COMP_CDE;
	private int LAWSTATID = 0;
	private String LAWSTATNAME;
	private String UPDBY;
	private Timestamp UPDDTE;

	public static final String tablename = "tklawstat";

	public String getTableName() {
		return schema + tablename;
	}

	public TboTKLAWSTAT() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) throws Exception {
		if (getStr(comp_cde).length() > 5) {
			throw new Exception("ฟิลด์ TKLAWSTAT.COMP_CDE ข้อมูลยาวเกิน 5 ตัวอักษร ตัวอย่างข้อมูล : \n " + comp_cde + " ");
		}
		this.COMP_CDE = comp_cde;
	}

	public int getLAWSTATID() {
		return this.LAWSTATID;
	}

	public void setLAWSTATID(int lawstatid) throws Exception {
		this.LAWSTATID = lawstatid;
	}

	public String getLAWSTATNAME() {
		return this.LAWSTATNAME;
	}

	public void setLAWSTATNAME(String lawstatname) throws Exception {
		if (getStr(lawstatname).length() > 250) {
			throw new Exception("ฟิลด์ TKLAWSTAT.LAWSTATNAME ข้อมูลยาวเกิน 250 ตัวอักษร ตัวอย่างข้อมูล : \n " + lawstatname + " ");
		}
		this.LAWSTATNAME = lawstatname;
	}

	public String getUPDBY() {
		return this.UPDBY;
	}

	public void setUPDBY(String updby) throws Exception {
		if (getStr(updby).length() > 100) {
			throw new Exception("ฟิลด์ TKLAWSTAT.UPDBY ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + updby + " ");
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
package com.pcc.tk.tbo;

import java.sql.Timestamp;
import com.pcc.sys.tbo.TABLE;

public class TboTKLAWTYPE extends TABLE {

	private String COMP_CDE;
	private int LAWTYPEID = 0;
	private String LAWTYPENAME;
	private String UPDBY;
	private Timestamp UPDDTE;

	public static final String tablename = "tklawtype";

	public String getTableName() {
		return schema + tablename;
	}

	public TboTKLAWTYPE() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) throws Exception {
		if (getStr(comp_cde).length() > 5) {
			throw new Exception("ฟิลด์ TKLAWTYPE.COMP_CDE ข้อมูลยาวเกิน 5 ตัวอักษร ตัวอย่างข้อมูล : \n " + comp_cde + " ");
		}
		this.COMP_CDE = comp_cde;
	}

	public int getLAWTYPEID() {
		return this.LAWTYPEID;
	}

	public void setLAWTYPEID(int lawtypeid) throws Exception {
		this.LAWTYPEID = lawtypeid;
	}

	public String getLAWTYPENAME() {
		return this.LAWTYPENAME;
	}

	public void setLAWTYPENAME(String lawtypename) throws Exception {
		if (getStr(lawtypename).length() > 250) {
			throw new Exception("ฟิลด์ TKLAWTYPE.LAWTYPENAME ข้อมูลยาวเกิน 250 ตัวอักษร ตัวอย่างข้อมูล : \n " + lawtypename + " ");
		}
		this.LAWTYPENAME = lawtypename;
	}

	public String getUPDBY() {
		return this.UPDBY;
	}

	public void setUPDBY(String updby) throws Exception {
		if (getStr(updby).length() > 100) {
			throw new Exception("ฟิลด์ TKLAWTYPE.UPDBY ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + updby + " ");
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
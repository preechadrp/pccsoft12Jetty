package com.pcc.tk.tbo;

import java.sql.Timestamp;
import com.pcc.sys.tbo.TABLE;

public class TboTKLAWYER extends TABLE {

	private String COMP_CDE;
	private int LAWYERID = 0;
	private String LAWYERNAME;
	private String TELNO;
	private String UPDBY;
	private Timestamp UPDDTE;

	public static final String tablename = "tklawyer";

	public String getTableName() {
		return schema + tablename;
	}

	public TboTKLAWYER() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) throws Exception {
		if (getStr(comp_cde).length() > 5) {
			throw new Exception("ฟิลด์ TKLAWYER.COMP_CDE ข้อมูลยาวเกิน 5 ตัวอักษร ตัวอย่างข้อมูล : \n " + comp_cde + " ");
		}
		this.COMP_CDE = comp_cde;
	}

	public int getLAWYERID() {
		return this.LAWYERID;
	}

	public void setLAWYERID(int lawyerid) throws Exception {
		this.LAWYERID = lawyerid;
	}

	public String getLAWYERNAME() {
		return this.LAWYERNAME;
	}

	public void setLAWYERNAME(String lawyername) throws Exception {
		if (getStr(lawyername).length() > 250) {
			throw new Exception("ฟิลด์ TKLAWYER.LAWYERNAME ข้อมูลยาวเกิน 250 ตัวอักษร ตัวอย่างข้อมูล : \n " + lawyername + " ");
		}
		this.LAWYERNAME = lawyername;
	}

	public String getTELNO() {
		return this.TELNO;
	}

	public void setTELNO(String telno) throws Exception {
		if (getStr(telno).length() > 250) {
			throw new Exception("ฟิลด์ TKLAWYER.TELNO ข้อมูลยาวเกิน 250 ตัวอักษร ตัวอย่างข้อมูล : \n " + telno + " ");
		}
		this.TELNO = telno;
	}

	public String getUPDBY() {
		return this.UPDBY;
	}

	public void setUPDBY(String updby) throws Exception {
		if (getStr(updby).length() > 100) {
			throw new Exception("ฟิลด์ TKLAWYER.UPDBY ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + updby + " ");
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
package com.pcc.tk.tbo;

import java.sql.Timestamp;

import com.pcc.sys.tbo.TABLE;

public class TboTKJOBLAWYER extends TABLE {

	private String COMP_CDE;
	private String JOBNO;
	private int SEQ = 0;
	private int LAWYERID = 0;
	private String UPDBY;
	private Timestamp UPDDTE;

	public static final String tablename = "tkjoblawyer";

	public String getTableName() {
		return schema + tablename;
	}

	public TboTKJOBLAWYER() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) throws Exception {
		if (getStr(comp_cde).length() > 5) {
			throw new Exception("ฟิลด์ TKJOBLAWYER.COMP_CDE ข้อมูลยาวเกิน 5 ตัวอักษร ตัวอย่างข้อมูล : \n " + comp_cde + " ");
		}
		this.COMP_CDE = comp_cde;
	}

	public String getJOBNO() {
		return this.JOBNO;
	}

	public void setJOBNO(String jobno) throws Exception {
		if (getStr(jobno).length() > 20) {
			throw new Exception("ฟิลด์ TKJOBLAWYER.JOBNO ข้อมูลยาวเกิน 20 ตัวอักษร ตัวอย่างข้อมูล : \n " + jobno + " ");
		}
		this.JOBNO = jobno;
	}

	public int getSEQ() {
		return this.SEQ;
	}

	public void setSEQ(int seq) throws Exception {
		this.SEQ = seq;
	}

	public int getLAWYERID() {
		return this.LAWYERID;
	}

	public void setLAWYERID(int lawyerid) throws Exception {
		this.LAWYERID = lawyerid;
	}

	public String getUPDBY() {
		return this.UPDBY;
	}

	public void setUPDBY(String updby) throws Exception {
		if (getStr(updby).length() > 100) {
			throw new Exception("ฟิลด์ TKJOBLAWYER.UPDBY ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + updby + " ");
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
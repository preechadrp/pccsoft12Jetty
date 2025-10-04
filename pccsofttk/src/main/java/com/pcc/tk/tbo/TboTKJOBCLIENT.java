package com.pcc.tk.tbo;

import java.sql.Timestamp;

import com.pcc.sys.tbo.TABLE;

public class TboTKJOBCLIENT extends TABLE {

	private String COMP_CDE;
	private String JOBNO;
	private int SEQ = 0;
	private String CLIENTTITLE;
	private String CLIENTFNAME;
	private String CLIENTLNAME;
	private String TELNO;
	private int CLIENTSEQ = 0;
	private String UPDBY;
	private Timestamp UPDDTE;

	public static final String tablename = "tkjobclient";

	public String getTableName() {
		return schema + tablename;
	}

	public TboTKJOBCLIENT() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) throws Exception {
		if (getStr(comp_cde).length() > 5) {
			throw new Exception("ฟิลด์ TKJOBCLIENT.COMP_CDE ข้อมูลยาวเกิน 5 ตัวอักษร ตัวอย่างข้อมูล : \n " + comp_cde + " ");
		}
		this.COMP_CDE = comp_cde;
	}

	public String getJOBNO() {
		return this.JOBNO;
	}

	public void setJOBNO(String jobno) throws Exception {
		if (getStr(jobno).length() > 20) {
			throw new Exception("ฟิลด์ TKJOBCLIENT.JOBNO ข้อมูลยาวเกิน 20 ตัวอักษร ตัวอย่างข้อมูล : \n " + jobno + " ");
		}
		this.JOBNO = jobno;
	}

	public int getSEQ() {
		return this.SEQ;
	}

	public void setSEQ(int seq) throws Exception {
		this.SEQ = seq;
	}

	public String getCLIENTTITLE() {
		return this.CLIENTTITLE;
	}

	public void setCLIENTTITLE(String clienttitle) throws Exception {
		if (getStr(clienttitle).length() > 50) {
			throw new Exception("ฟิลด์ TKJOBCLIENT.CLIENTTITLE ข้อมูลยาวเกิน 50 ตัวอักษร ตัวอย่างข้อมูล : \n " + clienttitle + " ");
		}
		this.CLIENTTITLE = clienttitle;
	}

	public String getCLIENTFNAME() {
		return this.CLIENTFNAME;
	}

	public void setCLIENTFNAME(String clientfname) throws Exception {
		if (getStr(clientfname).length() > 150) {
			throw new Exception("ฟิลด์ TKJOBCLIENT.CLIENTFNAME ข้อมูลยาวเกิน 150 ตัวอักษร ตัวอย่างข้อมูล : \n " + clientfname + " ");
		}
		this.CLIENTFNAME = clientfname;
	}

	public String getCLIENTLNAME() {
		return this.CLIENTLNAME;
	}

	public void setCLIENTLNAME(String clientlname) throws Exception {
		if (getStr(clientlname).length() > 150) {
			throw new Exception("ฟิลด์ TKJOBCLIENT.CLIENTLNAME ข้อมูลยาวเกิน 150 ตัวอักษร ตัวอย่างข้อมูล : \n " + clientlname + " ");
		}
		this.CLIENTLNAME = clientlname;
	}

	public String getTELNO() {
		return this.TELNO;
	}

	public void setTELNO(String telno) throws Exception {
		if (getStr(telno).length() > 200) {
			throw new Exception("ฟิลด์ TKJOBCLIENT.TELNO ข้อมูลยาวเกิน 200 ตัวอักษร ตัวอย่างข้อมูล : \n " + telno + " ");
		}
		this.TELNO = telno;
	}

	public int getCLIENTSEQ() {
		return this.CLIENTSEQ;
	}

	public void setCLIENTSEQ(int clientseq) throws Exception {
		this.CLIENTSEQ = clientseq;
	}

	public String getUPDBY() {
		return this.UPDBY;
	}

	public void setUPDBY(String updby) throws Exception {
		if (getStr(updby).length() > 100) {
			throw new Exception("ฟิลด์ TKJOBCLIENT.UPDBY ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + updby + " ");
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
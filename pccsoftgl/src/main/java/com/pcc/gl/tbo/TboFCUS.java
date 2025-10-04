package com.pcc.gl.tbo;

import java.sql.Timestamp;

import com.pcc.sys.tbo.TABLE;

public class TboFCUS extends TABLE {

	private String COMP_CDE;
	private String CUST_CDE;
	private String CUSTYP;
	private String TITLE;
	private String FNAME;
	private String LNAME;
	private String IDNO;
	private String BRANCH_CODE;
	private int AR_CREDIT;
	private int AP_CREDIT;
	private String TEL1;
	private String TEL2;
	private String TEL3;
	private String FAXNO1;
	private String INSBY;
	private Timestamp INSDT;
	private String UPBY;
	private Timestamp UPDT;

	public static final String tablename = "fcus";

	public String getTableName() {
		return schema + tablename;
	}

	public TboFCUS() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) {
		this.COMP_CDE = comp_cde;
	}

	public String getCUST_CDE() {
		return this.CUST_CDE;
	}

	public void setCUST_CDE(String cust_cde) {
		this.CUST_CDE = cust_cde;
	}

	public String getCUSTYP() {
		return this.CUSTYP;
	}

	public void setCUSTYP(String custyp) {
		this.CUSTYP = custyp;
	}

	public String getTITLE() {
		return this.TITLE;
	}

	public void setTITLE(String title) {
		this.TITLE = title;
	}

	public String getFNAME() {
		return this.FNAME;
	}

	public void setFNAME(String fname) {
		this.FNAME = fname;
	}

	public String getLNAME() {
		return this.LNAME;
	}

	public void setLNAME(String lname) {
		this.LNAME = lname;
	}

	public String getIDNO() {
		return this.IDNO;
	}

	public void setIDNO(String idno) {
		this.IDNO = idno;
	}

	public String getBRANCH_CODE() {
		return this.BRANCH_CODE;
	}

	public void setBRANCH_CODE(String branch_code) {
		this.BRANCH_CODE = branch_code;
	}

	public int getAR_CREDIT() {
		return this.AR_CREDIT;
	}

	public void setAR_CREDIT(int ar_credit) {
		this.AR_CREDIT = ar_credit;
	}

	public int getAP_CREDIT() {
		return this.AP_CREDIT;
	}

	public void setAP_CREDIT(int ap_credit) {
		this.AP_CREDIT = ap_credit;
	}

	public String getTEL1() {
		return this.TEL1;
	}

	public void setTEL1(String tel1) {
		this.TEL1 = tel1;
	}

	public String getTEL2() {
		return this.TEL2;
	}

	public void setTEL2(String tel2) {
		this.TEL2 = tel2;
	}

	public String getTEL3() {
		return this.TEL3;
	}

	public void setTEL3(String tel3) {
		this.TEL3 = tel3;
	}

	public String getFAXNO1() {
		return this.FAXNO1;
	}

	public void setFAXNO1(String faxno1) {
		this.FAXNO1 = faxno1;
	}

	public String getINSBY() {
		return this.INSBY;
	}

	public void setINSBY(String insby) {
		this.INSBY = insby;
	}

	public Timestamp getINSDT() {
		return this.INSDT;
	}

	public void setINSDT(Timestamp insdt) {
		this.INSDT = insdt;
	}

	public String getUPBY() {
		return this.UPBY;
	}

	public void setUPBY(String upby) {
		this.UPBY = upby;
	}

	public Timestamp getUPDT() {
		return this.UPDT;
	}

	public void setUPDT(Timestamp updt) {
		this.UPDT = updt;
	}
}
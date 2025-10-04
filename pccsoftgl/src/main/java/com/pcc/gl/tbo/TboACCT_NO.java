package com.pcc.gl.tbo;

import java.sql.Timestamp;

import com.pcc.sys.tbo.TABLE;

public class TboACCT_NO extends TABLE {

	private String COMP_CDE;
	private String ACCT_ID;
	private String ACCT_NAME;
	private int ACCT_TYPE;
	private String INSBY;
	private Timestamp INSDT;
	private String UPBY;
	private Timestamp UPDT;

	public static final String tablename = "acct_no";

	public String getTableName() {
		return schema + tablename;
	}

	public TboACCT_NO() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) {
		this.COMP_CDE = comp_cde;
	}

	public String getACCT_ID() {
		return this.ACCT_ID;
	}

	public void setACCT_ID(String acct_id) {
		this.ACCT_ID = acct_id;
	}

	public String getACCT_NAME() {
		return this.ACCT_NAME;
	}

	public void setACCT_NAME(String acct_name) {
		this.ACCT_NAME = acct_name;
	}

	public int getACCT_TYPE() {
		return this.ACCT_TYPE;
	}

	public void setACCT_TYPE(int acct_type) {
		this.ACCT_TYPE = acct_type;
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
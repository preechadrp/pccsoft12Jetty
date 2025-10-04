package com.pcc.gl.tbo;

import java.sql.Timestamp;

import com.pcc.sys.tbo.TABLE;

public class TboACCT_NO_SUB extends TABLE {

	private String COMP_CDE;
	private String ACCT_ID;
	private int SUB_TYPE;
	private String ACCT_ID_BANK;
	private String INSBY;
	private Timestamp INSDT;
	private String UPBY;
	private Timestamp UPDT;

	public static final String tablename = "acct_no_sub";

	public String getTableName() {
		return schema + tablename;
	}

	public TboACCT_NO_SUB() {
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

	public int getSUB_TYPE() {
		return this.SUB_TYPE;
	}

	public void setSUB_TYPE(int sub_type) {
		this.SUB_TYPE = sub_type;
	}

	public String getACCT_ID_BANK() {
		return this.ACCT_ID_BANK;
	}

	public void setACCT_ID_BANK(String acct_id_bank) {
		this.ACCT_ID_BANK = acct_id_bank;
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
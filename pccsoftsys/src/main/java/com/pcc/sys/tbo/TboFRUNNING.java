package com.pcc.sys.tbo;

public class TboFRUNNING extends TABLE {

	private String COMP_CDE;
	private String RUNNING_ID;
	private String RUNNING_PREFIX;
	private int RUNNING_NO;

	public static final String tablename = "frunning";

	public String getTableName() {
		return schema + tablename;
	}

	public TboFRUNNING() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) {
		this.COMP_CDE = comp_cde;
	}

	public String getRUNNING_ID() {
		return this.RUNNING_ID;
	}

	public void setRUNNING_ID(String running_id) {
		this.RUNNING_ID = running_id;
	}

	public String getRUNNING_PREFIX() {
		return this.RUNNING_PREFIX;
	}

	public void setRUNNING_PREFIX(String running_prefix) {
		this.RUNNING_PREFIX = running_prefix;
	}

	public int getRUNNING_NO() {
		return this.RUNNING_NO;
	}

	public void setRUNNING_NO(int running_no) {
		this.RUNNING_NO = running_no;
	}
}
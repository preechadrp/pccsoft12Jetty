package com.pcc.sys.tbo;

public class TboFZIPCODE extends TABLE {

	private String PROVIN_ID;
	private String ZIPCODE;
	private String DISTRICT_NAME;

	public static final String tablename = "fzipcode";

	public String getTableName() {
		return schema + tablename;
	}

	public TboFZIPCODE() {
		super();
	}

	public String getPROVIN_ID() {
		return this.PROVIN_ID;
	}

	public void setPROVIN_ID(String provin_id) {
		this.PROVIN_ID = provin_id;
	}

	public String getZIPCODE() {
		return this.ZIPCODE;
	}

	public void setZIPCODE(String zipcode) {
		this.ZIPCODE = zipcode;
	}

	public String getDISTRICT_NAME() {
		return this.DISTRICT_NAME;
	}

	public void setDISTRICT_NAME(String district_name) {
		this.DISTRICT_NAME = district_name;
	}
}
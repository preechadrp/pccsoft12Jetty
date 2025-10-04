package com.pcc.sys.tbo;

import java.sql.Timestamp;

public class TboFSECTION extends TABLE {

	private String COMP_CDE;
	private String SECT_ID;
	private String SECT_NAME;
	private String UPBY;
	private Timestamp UPDT;

	public static final String tablename = "fsection";

	public String getTableName() {
		return schema + tablename;
	}

	public TboFSECTION() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) {
		this.COMP_CDE = comp_cde;
	}

	public String getSECT_ID() {
		return this.SECT_ID;
	}

	public void setSECT_ID(String sect_id) {
		this.SECT_ID = sect_id;
	}

	public String getSECT_NAME() {
		return this.SECT_NAME;
	}

	public void setSECT_NAME(String sect_name) {
		this.SECT_NAME = sect_name;
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
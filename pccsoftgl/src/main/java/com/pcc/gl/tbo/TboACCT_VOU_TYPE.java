package com.pcc.gl.tbo;

import java.sql.Timestamp;

import com.pcc.sys.tbo.TABLE;

public class TboACCT_VOU_TYPE extends TABLE {

	private String COMP_CDE;
	private String VOU_TYPE;
	private String VOU_NAME;
	private int USE_PV;
	private int USE_RV;
	private String UPBY;
	private Timestamp UPDT;

	public static final String tablename = "acct_vou_type";

	public String getTableName() {
		return schema + tablename;
	}

	public TboACCT_VOU_TYPE() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) {
		this.COMP_CDE = comp_cde;
	}

	public String getVOU_TYPE() {
		return this.VOU_TYPE;
	}

	public void setVOU_TYPE(String vou_type) {
		this.VOU_TYPE = vou_type;
	}

	public String getVOU_NAME() {
		return this.VOU_NAME;
	}

	public void setVOU_NAME(String vou_name) {
		this.VOU_NAME = vou_name;
	}

	public int getUSE_PV() {
		return this.USE_PV;
	}

	public void setUSE_PV(int use_pv) {
		this.USE_PV = use_pv;
	}

	public int getUSE_RV() {
		return this.USE_RV;
	}

	public void setUSE_RV(int use_rv) {
		this.USE_RV = use_rv;
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
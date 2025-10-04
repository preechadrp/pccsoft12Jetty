package com.pcc.gl.tbo;

import java.sql.Date;
import java.sql.Timestamp;

import com.pcc.sys.tbo.TABLE;

public class TboACCT_LOCK extends TABLE {

	private String COMP_CDE;
	private Date LOCKTODATE;
	private String UPDBY;
	private Timestamp UPDDT;

	public static final String tablename = "acct_lock";

	public String getTableName() {
		return schema + tablename;
	}

	public TboACCT_LOCK() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) {
		this.COMP_CDE = comp_cde;
	}

	public Date getLOCKTODATE() {
		return this.LOCKTODATE;
	}

	public void setLOCKTODATE(Date locktodate) {
		this.LOCKTODATE = locktodate;
	}

	public String getUPDBY() {
		return this.UPDBY;
	}

	public void setUPDBY(String updby) {
		this.UPDBY = updby;
	}

	public Timestamp getUPDDT() {
		return this.UPDDT;
	}

	public void setUPDDT(Timestamp upddt) {
		this.UPDDT = upddt;
	}
}
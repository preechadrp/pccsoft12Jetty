package com.pcc.gl.tbo;

import java.sql.Date;
import java.sql.Timestamp;

import com.pcc.sys.tbo.TABLE;

public class TboACGL_HEADER extends TABLE {

	private String COMP_CDE;
	private String VOU_TYPE;
	private String VOU_NO;
	private Date POSTDATE;
	private String DESCR;
	private String POST_APP;
	private int RECSTA;
	private String UPBY;
	private Timestamp UPDT;
	private String APPR_BY;
	private Timestamp APPR_DT;

	public static final String tablename = "acgl_header";

	public String getTableName() {
		return schema + tablename;
	}

	public TboACGL_HEADER() {
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

	public String getVOU_NO() {
		return this.VOU_NO;
	}

	public void setVOU_NO(String vou_no) {
		this.VOU_NO = vou_no;
	}

	public Date getPOSTDATE() {
		return this.POSTDATE;
	}

	public void setPOSTDATE(Date postdate) {
		this.POSTDATE = postdate;
	}

	public String getDESCR() {
		return this.DESCR;
	}

	public void setDESCR(String descr) {
		this.DESCR = descr;
	}

	public String getPOST_APP() {
		return this.POST_APP;
	}

	public void setPOST_APP(String post_app) {
		this.POST_APP = post_app;
	}

	public int getRECSTA() {
		return this.RECSTA;
	}

	public void setRECSTA(int recsta) {
		this.RECSTA = recsta;
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

	public String getAPPR_BY() {
		return this.APPR_BY;
	}

	public void setAPPR_BY(String appr_by) {
		this.APPR_BY = appr_by;
	}

	public Timestamp getAPPR_DT() {
		return this.APPR_DT;
	}

	public void setAPPR_DT(Timestamp appr_dt) {
		this.APPR_DT = appr_dt;
	}
}
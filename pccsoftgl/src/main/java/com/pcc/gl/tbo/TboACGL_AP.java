package com.pcc.gl.tbo;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.pcc.sys.tbo.TABLE;

public class TboACGL_AP extends TABLE {

	private String COMP_CDE;
	private String VOU_TYPE;
	private String VOU_NO;
	private int VOU_SEQ;
	private int VOU_DSEQ;
	private Date POSTDATE;
	private String ACCT_ID;
	private int POST_TYPE;
	private BigDecimal NUM_TYPE;
	private BigDecimal AMT;
	private BigDecimal CLEAR_AMT;
	private String SECT_ID;
	private String DESCR;
	private int RECSTA;
	private String DOCNO;
	private Date DOCDATE;
	private String CUST_CDE;
	private Date DUEDATE;
	private String LINK_NO;
	private String INSBY;
	private Timestamp INSDT;
	private String UPBY;
	private Timestamp UPDT;

	public static final String tablename = "acgl_ap";

	public String getTableName() {
		return schema + tablename;
	}

	public TboACGL_AP() {
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

	public int getVOU_SEQ() {
		return this.VOU_SEQ;
	}

	public void setVOU_SEQ(int vou_seq) {
		this.VOU_SEQ = vou_seq;
	}

	public int getVOU_DSEQ() {
		return this.VOU_DSEQ;
	}

	public void setVOU_DSEQ(int vou_dseq) {
		this.VOU_DSEQ = vou_dseq;
	}

	public Date getPOSTDATE() {
		return this.POSTDATE;
	}

	public void setPOSTDATE(Date postdate) {
		this.POSTDATE = postdate;
	}

	public String getACCT_ID() {
		return this.ACCT_ID;
	}

	public void setACCT_ID(String acct_id) {
		this.ACCT_ID = acct_id;
	}

	public int getPOST_TYPE() {
		return this.POST_TYPE;
	}

	public void setPOST_TYPE(int post_type) {
		this.POST_TYPE = post_type;
	}

	public BigDecimal getNUM_TYPE() {
		return this.NUM_TYPE;
	}

	public void setNUM_TYPE(BigDecimal num_type) {
		this.NUM_TYPE = num_type;
	}

	public BigDecimal getAMT() {
		return this.AMT;
	}

	public void setAMT(BigDecimal amt) {
		this.AMT = amt;
	}

	public BigDecimal getCLEAR_AMT() {
		return this.CLEAR_AMT;
	}

	public void setCLEAR_AMT(BigDecimal clear_amt) {
		this.CLEAR_AMT = clear_amt;
	}

	public String getSECT_ID() {
		return this.SECT_ID;
	}

	public void setSECT_ID(String sect_id) {
		this.SECT_ID = sect_id;
	}

	public String getDESCR() {
		return this.DESCR;
	}

	public void setDESCR(String descr) {
		this.DESCR = descr;
	}

	public int getRECSTA() {
		return this.RECSTA;
	}

	public void setRECSTA(int recsta) {
		this.RECSTA = recsta;
	}

	public String getDOCNO() {
		return this.DOCNO;
	}

	public void setDOCNO(String docno) {
		this.DOCNO = docno;
	}

	public Date getDOCDATE() {
		return this.DOCDATE;
	}

	public void setDOCDATE(Date docdate) {
		this.DOCDATE = docdate;
	}

	public String getCUST_CDE() {
		return this.CUST_CDE;
	}

	public void setCUST_CDE(String cust_cde) {
		this.CUST_CDE = cust_cde;
	}

	public Date getDUEDATE() {
		return this.DUEDATE;
	}

	public void setDUEDATE(Date duedate) {
		this.DUEDATE = duedate;
	}

	public String getLINK_NO() {
		return this.LINK_NO;
	}

	public void setLINK_NO(String link_no) {
		this.LINK_NO = link_no;
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
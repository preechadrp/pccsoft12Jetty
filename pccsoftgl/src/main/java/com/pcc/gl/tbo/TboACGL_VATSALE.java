package com.pcc.gl.tbo;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.pcc.sys.tbo.TABLE;

public class TboACGL_VATSALE extends TABLE {

	private String COMP_CDE;
	private String VOU_TYPE;
	private String VOU_NO;
	private int VOU_SEQ;
	private int VOU_DSEQ;
	private Date POSTDATE;
	private int TAX_TYPE;
	private int POST_TYPE;
	private BigDecimal VAT_RATE;
	private BigDecimal NUM_TYPE;
	private BigDecimal AMT;
	private BigDecimal BASE_AMT;
	private BigDecimal CLEAR_AMT;
	private String SECT_ID;
	private String DESCR;
	private String DOCNO;
	private Date DOCDATE;
	private String CUST_CDE;
	private String CUST_BRANCH_ID;
	private String LINK_NO;
	private int RECSTA;
	private String INSBY;
	private Timestamp INSDT;
	private String UPBY;
	private Timestamp UPDT;

	public static final String tablename = "acgl_vatsale";

	public String getTableName() {
		return schema + tablename;
	}

	public TboACGL_VATSALE() {
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

	public int getTAX_TYPE() {
		return this.TAX_TYPE;
	}

	public void setTAX_TYPE(int tax_type) {
		this.TAX_TYPE = tax_type;
	}

	public int getPOST_TYPE() {
		return this.POST_TYPE;
	}

	public void setPOST_TYPE(int post_type) {
		this.POST_TYPE = post_type;
	}

	public BigDecimal getVAT_RATE() {
		return this.VAT_RATE;
	}

	public void setVAT_RATE(BigDecimal vat_rate) {
		this.VAT_RATE = vat_rate;
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

	public BigDecimal getBASE_AMT() {
		return this.BASE_AMT;
	}

	public void setBASE_AMT(BigDecimal base_amt) {
		this.BASE_AMT = base_amt;
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

	public String getCUST_BRANCH_ID() {
		return this.CUST_BRANCH_ID;
	}

	public void setCUST_BRANCH_ID(String cust_branch_id) {
		this.CUST_BRANCH_ID = cust_branch_id;
	}

	public String getLINK_NO() {
		return this.LINK_NO;
	}

	public void setLINK_NO(String link_no) {
		this.LINK_NO = link_no;
	}

	public int getRECSTA() {
		return this.RECSTA;
	}

	public void setRECSTA(int recsta) {
		this.RECSTA = recsta;
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
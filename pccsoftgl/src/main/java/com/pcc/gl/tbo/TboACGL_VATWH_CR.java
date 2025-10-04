package com.pcc.gl.tbo;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.pcc.sys.tbo.TABLE;

public class TboACGL_VATWH_CR extends TABLE {

	private String COMP_CDE;
	private String VOU_TYPE;
	private String VOU_NO;
	private int VOU_SEQ;
	private int VOU_DSEQ;
	private Date POSTDATE;
	private int DOC_TYPE;
	private String WHNAME;
	private String WHTYPE;
	private BigDecimal VAT_RATE;
	private BigDecimal NUM_TYPE;
	private BigDecimal AMT;
	private BigDecimal BASE_AMT;
	private String SECT_ID;
	private String DESCR;
	private int RECSTA;
	private String DOCNO;
	private String CUST_CDE;
	private String CUST_NAME;
	private String INSBY;
	private Timestamp INSDT;
	private String UPBY;
	private Timestamp UPDT;

	public static final String tablename = "acgl_vatwh_cr";

	public String getTableName() {
		return schema + tablename;
	}

	public TboACGL_VATWH_CR() {
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

	public int getDOC_TYPE() {
		return this.DOC_TYPE;
	}

	public void setDOC_TYPE(int doc_type) {
		this.DOC_TYPE = doc_type;
	}

	public String getWHNAME() {
		return this.WHNAME;
	}

	public void setWHNAME(String whname) {
		this.WHNAME = whname;
	}

	public String getWHTYPE() {
		return this.WHTYPE;
	}

	public void setWHTYPE(String whtype) {
		this.WHTYPE = whtype;
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

	public String getCUST_CDE() {
		return this.CUST_CDE;
	}

	public void setCUST_CDE(String cust_cde) {
		this.CUST_CDE = cust_cde;
	}

	public String getCUST_NAME() {
		return this.CUST_NAME;
	}

	public void setCUST_NAME(String cust_name) {
		this.CUST_NAME = cust_name;
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
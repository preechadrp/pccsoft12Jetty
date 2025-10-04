package com.pcc.gl.tbo;

import java.math.BigDecimal;
import java.sql.Date;

import com.pcc.sys.tbo.TABLE;

public class TboACGL_DETAIL extends TABLE {

	private String COMP_CDE;
	private String VOU_TYPE;
	private String VOU_NO;
	private int VOU_SEQ;
	private int VOU_SEQ_SHOW;
	private Date POSTDATE;
	private String ACCT_ID;
	private BigDecimal AMT;
	private BigDecimal NUM_TYPE;
	private String SECT_ID;
	private String DESCR;
	private String DESCR_SUB;
	private int RECSTA;
	private String SUB_HAS;
	private String SUB_APPR;
	private String SUB_APPR_BY;
	private String ACCT_OPEN;
	private String APAR_VOU_TYPE;
	private String APAR_VOU_NO;
	private int APAR_VOU_SEQ;
	private int APAR_VOU_DSEQ;
	private int APAR_RECTYP;
	private String APAR_DESCR;
	private BigDecimal APAR_AMT;
	private String CHQ_TYPE;
	private String CHQ_NO;
	private Date CHQ_DATE;
	private String CHQ_PAYEE;
	private String CHQ_WD_VOU_TYPE;
	private String CHQ_WD_VOU_NO;
	private int CHQ_WD_VOU_SEQ;
	private int CHQ_WD_USE;

	public static final String tablename = "acgl_detail";

	public String getTableName() {
		return schema + tablename;
	}

	public TboACGL_DETAIL() {
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

	public int getVOU_SEQ_SHOW() {
		return this.VOU_SEQ_SHOW;
	}

	public void setVOU_SEQ_SHOW(int vou_seq_show) {
		this.VOU_SEQ_SHOW = vou_seq_show;
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

	public BigDecimal getAMT() {
		return this.AMT;
	}

	public void setAMT(BigDecimal amt) {
		this.AMT = amt;
	}

	public BigDecimal getNUM_TYPE() {
		return this.NUM_TYPE;
	}

	public void setNUM_TYPE(BigDecimal num_type) {
		this.NUM_TYPE = num_type;
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

	public String getDESCR_SUB() {
		return this.DESCR_SUB;
	}

	public void setDESCR_SUB(String descr_sub) {
		this.DESCR_SUB = descr_sub;
	}

	public int getRECSTA() {
		return this.RECSTA;
	}

	public void setRECSTA(int recsta) {
		this.RECSTA = recsta;
	}

	public String getSUB_HAS() {
		return this.SUB_HAS;
	}

	public void setSUB_HAS(String sub_has) {
		this.SUB_HAS = sub_has;
	}

	public String getSUB_APPR() {
		return this.SUB_APPR;
	}

	public void setSUB_APPR(String sub_appr) {
		this.SUB_APPR = sub_appr;
	}

	public String getSUB_APPR_BY() {
		return this.SUB_APPR_BY;
	}

	public void setSUB_APPR_BY(String sub_appr_by) {
		this.SUB_APPR_BY = sub_appr_by;
	}

	public String getACCT_OPEN() {
		return this.ACCT_OPEN;
	}

	public void setACCT_OPEN(String acct_open) {
		this.ACCT_OPEN = acct_open;
	}

	public String getAPAR_VOU_TYPE() {
		return this.APAR_VOU_TYPE;
	}

	public void setAPAR_VOU_TYPE(String apar_vou_type) {
		this.APAR_VOU_TYPE = apar_vou_type;
	}

	public String getAPAR_VOU_NO() {
		return this.APAR_VOU_NO;
	}

	public void setAPAR_VOU_NO(String apar_vou_no) {
		this.APAR_VOU_NO = apar_vou_no;
	}

	public int getAPAR_VOU_SEQ() {
		return this.APAR_VOU_SEQ;
	}

	public void setAPAR_VOU_SEQ(int apar_vou_seq) {
		this.APAR_VOU_SEQ = apar_vou_seq;
	}

	public int getAPAR_VOU_DSEQ() {
		return this.APAR_VOU_DSEQ;
	}

	public void setAPAR_VOU_DSEQ(int apar_vou_dseq) {
		this.APAR_VOU_DSEQ = apar_vou_dseq;
	}

	public int getAPAR_RECTYP() {
		return this.APAR_RECTYP;
	}

	public void setAPAR_RECTYP(int apar_rectyp) {
		this.APAR_RECTYP = apar_rectyp;
	}

	public String getAPAR_DESCR() {
		return this.APAR_DESCR;
	}

	public void setAPAR_DESCR(String apar_descr) {
		this.APAR_DESCR = apar_descr;
	}

	public BigDecimal getAPAR_AMT() {
		return this.APAR_AMT;
	}

	public void setAPAR_AMT(BigDecimal apar_amt) {
		this.APAR_AMT = apar_amt;
	}

	public String getCHQ_TYPE() {
		return this.CHQ_TYPE;
	}

	public void setCHQ_TYPE(String chq_type) {
		this.CHQ_TYPE = chq_type;
	}

	public String getCHQ_NO() {
		return this.CHQ_NO;
	}

	public void setCHQ_NO(String chq_no) {
		this.CHQ_NO = chq_no;
	}

	public Date getCHQ_DATE() {
		return this.CHQ_DATE;
	}

	public void setCHQ_DATE(Date chq_date) {
		this.CHQ_DATE = chq_date;
	}

	public String getCHQ_PAYEE() {
		return this.CHQ_PAYEE;
	}

	public void setCHQ_PAYEE(String chq_payee) {
		this.CHQ_PAYEE = chq_payee;
	}

	public String getCHQ_WD_VOU_TYPE() {
		return this.CHQ_WD_VOU_TYPE;
	}

	public void setCHQ_WD_VOU_TYPE(String chq_wd_vou_type) {
		this.CHQ_WD_VOU_TYPE = chq_wd_vou_type;
	}

	public String getCHQ_WD_VOU_NO() {
		return this.CHQ_WD_VOU_NO;
	}

	public void setCHQ_WD_VOU_NO(String chq_wd_vou_no) {
		this.CHQ_WD_VOU_NO = chq_wd_vou_no;
	}

	public int getCHQ_WD_VOU_SEQ() {
		return this.CHQ_WD_VOU_SEQ;
	}

	public void setCHQ_WD_VOU_SEQ(int chq_wd_vou_seq) {
		this.CHQ_WD_VOU_SEQ = chq_wd_vou_seq;
	}

	public int getCHQ_WD_USE() {
		return this.CHQ_WD_USE;
	}

	public void setCHQ_WD_USE(int chq_wd_use) {
		this.CHQ_WD_USE = chq_wd_use;
	}

}
package com.pcc.bx.tbo;

import java.math.BigDecimal;

import com.pcc.sys.tbo.TABLE;

public class TboBXDETAIL extends TABLE {

	private String COMP_CDE;
	private String BLNO;
	private int SEQ1 = 0;
	private String LINETYP;
	private String PRODUCTID;
	private String PRODUCTNAME;
	private String REMARKLINE;
	private BigDecimal QTY = BigDecimal.ZERO;
	private BigDecimal PRICE = BigDecimal.ZERO;
	private BigDecimal SUMPRICE = BigDecimal.ZERO;

	public static final String tablename = "bxdetail";

	public String getTableName() {
		return schema + tablename;
	}

	public TboBXDETAIL() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) throws Exception {
		if (getStr(comp_cde).length() > 5) {
			throw new Exception("ฟิลด์ BXDETAIL.COMP_CDE ข้อมูลยาวเกิน 5 ตัวอักษร ตัวอย่างข้อมูล : \n " + comp_cde + " ");
		}
		this.COMP_CDE = comp_cde;
	}

	public String getBLNO() {
		return this.BLNO;
	}

	public void setBLNO(String blno) throws Exception {
		if (getStr(blno).length() > 15) {
			throw new Exception("ฟิลด์ BXDETAIL.BLNO ข้อมูลยาวเกิน 15 ตัวอักษร ตัวอย่างข้อมูล : \n " + blno + " ");
		}
		this.BLNO = blno;
	}

	public int getSEQ1() {
		return this.SEQ1;
	}

	public void setSEQ1(int seq1) throws Exception {
		this.SEQ1 = seq1;
	}

	public String getLINETYP() {
		return this.LINETYP;
	}

	public void setLINETYP(String linetyp) throws Exception {
		if (getStr(linetyp).length() > 50) {
			throw new Exception("ฟิลด์ BXDETAIL.LINETYP ข้อมูลยาวเกิน 50 ตัวอักษร ตัวอย่างข้อมูล : \n " + linetyp + " ");
		}
		this.LINETYP = linetyp;
	}

	public String getPRODUCTID() {
		return this.PRODUCTID;
	}

	public void setPRODUCTID(String productid) throws Exception {
		if (getStr(productid).length() > 50) {
			throw new Exception("ฟิลด์ BXDETAIL.PRODUCTID ข้อมูลยาวเกิน 50 ตัวอักษร ตัวอย่างข้อมูล : \n " + productid + " ");
		}
		this.PRODUCTID = productid;
	}

	public String getPRODUCTNAME() {
		return this.PRODUCTNAME;
	}

	public void setPRODUCTNAME(String productname) throws Exception {
		if (getStr(productname).length() > 250) {
			throw new Exception("ฟิลด์ BXDETAIL.PRODUCTNAME ข้อมูลยาวเกิน 250 ตัวอักษร ตัวอย่างข้อมูล : \n " + productname + " ");
		}
		this.PRODUCTNAME = productname;
	}

	public String getREMARKLINE() {
		return this.REMARKLINE;
	}

	public void setREMARKLINE(String remarkline) throws Exception {
		if (getStr(remarkline).length() > 250) {
			throw new Exception("ฟิลด์ BXDETAIL.REMARKLINE ข้อมูลยาวเกิน 250 ตัวอักษร ตัวอย่างข้อมูล : \n " + remarkline + " ");
		}
		this.REMARKLINE = remarkline;
	}

	public BigDecimal getQTY() {
		return this.QTY;
	}

	public void setQTY(BigDecimal qty) throws Exception {
		this.QTY = qty;
	}

	public BigDecimal getPRICE() {
		return this.PRICE;
	}

	public void setPRICE(BigDecimal price) throws Exception {
		this.PRICE = price;
	}

	public BigDecimal getSUMPRICE() {
		return this.SUMPRICE;
	}

	public void setSUMPRICE(BigDecimal sumprice) throws Exception {
		this.SUMPRICE = sumprice;
	}
}
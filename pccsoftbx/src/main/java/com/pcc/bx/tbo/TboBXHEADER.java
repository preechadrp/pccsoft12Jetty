package com.pcc.bx.tbo;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.pcc.sys.tbo.TABLE;

public class TboBXHEADER extends TABLE {
	private String COMP_CDE;
	private int TMPLCDE = 0;
	private String BLNO;
	private Date BLDATE;
	private String VATTYPE;
	private String CUST_CDE;
	private String IDNO;
	private String CUST_TITLE;
	private String CUST_FNAME;
	private String CUST_LNAME;
	private String CUST_ADDR1;
	private String CUST_ADDR2;
	private String ZIPCODE;
	private String REMARK1;
	private BigDecimal VATRATE = BigDecimal.ZERO;
	private BigDecimal VATAMT = BigDecimal.ZERO;
	private BigDecimal BASAMT = BigDecimal.ZERO;
	private BigDecimal TOTALAMT = BigDecimal.ZERO;
	private int BRANC_CDE = 0;
	private int RECSTA = 0;
	private BigDecimal COSTVATAMT = BigDecimal.ZERO;
	private BigDecimal COSTBASAMT = BigDecimal.ZERO;
	private String CUST_CDE_AP;
	private String APNAME;
	private Date SENDDATE;
	private String UPBY;
	private Timestamp UPDT;
	private BigDecimal DISCOUNTAMT = BigDecimal.ZERO;

	public static final String tablename = "bxheader";

	public String getTableName() {
		return schema + tablename;
	}

	public TboBXHEADER() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) throws Exception {
		if (getStr(comp_cde).length() > 5) {
			throw new Exception("ฟิลด์ BXHEADER.COMP_CDE ข้อมูลยาวเกิน 5 ตัวอักษร ตัวอย่างข้อมูล : \n " + comp_cde + " ");
		}
		this.COMP_CDE = comp_cde;
	}

	public int getTMPLCDE() {
		return this.TMPLCDE;
	}

	public void setTMPLCDE(int tmplcde) throws Exception {
		this.TMPLCDE = tmplcde;
	}

	public String getBLNO() {
		return this.BLNO;
	}

	public void setBLNO(String blno) throws Exception {
		if (getStr(blno).length() > 15) {
			throw new Exception("ฟิลด์ BXHEADER.BLNO ข้อมูลยาวเกิน 15 ตัวอักษร ตัวอย่างข้อมูล : \n " + blno + " ");
		}
		this.BLNO = blno;
	}

	public Date getBLDATE() {
		return this.BLDATE;
	}

	public void setBLDATE(Date bldate) throws Exception {
		this.BLDATE = bldate;
	}

	public String getVATTYPE() {
		return this.VATTYPE;
	}

	public void setVATTYPE(String vattype) throws Exception {
		if (getStr(vattype).length() > 1) {
			throw new Exception("ฟิลด์ BXHEADER.VATTYPE ข้อมูลยาวเกิน 1 ตัวอักษร ตัวอย่างข้อมูล : \n " + vattype + " ");
		}
		this.VATTYPE = vattype;
	}

	public String getCUST_CDE() {
		return this.CUST_CDE;
	}

	public void setCUST_CDE(String cust_cde) throws Exception {
		if (getStr(cust_cde).length() > 15) {
			throw new Exception("ฟิลด์ BXHEADER.CUST_CDE ข้อมูลยาวเกิน 15 ตัวอักษร ตัวอย่างข้อมูล : \n " + cust_cde + " ");
		}
		this.CUST_CDE = cust_cde;
	}

	public String getIDNO() {
		return this.IDNO;
	}

	public void setIDNO(String idno) throws Exception {
		if (getStr(idno).length() > 20) {
			throw new Exception("ฟิลด์ BXHEADER.IDNO ข้อมูลยาวเกิน 20 ตัวอักษร ตัวอย่างข้อมูล : \n " + idno + " ");
		}
		this.IDNO = idno;
	}

	public String getCUST_TITLE() {
		return this.CUST_TITLE;
	}

	public void setCUST_TITLE(String cust_title) throws Exception {
		if (getStr(cust_title).length() > 200) {
			throw new Exception("ฟิลด์ BXHEADER.CUST_TITLE ข้อมูลยาวเกิน 200 ตัวอักษร ตัวอย่างข้อมูล : \n " + cust_title + " ");
		}
		this.CUST_TITLE = cust_title;
	}

	public String getCUST_FNAME() {
		return this.CUST_FNAME;
	}

	public void setCUST_FNAME(String cust_fname) throws Exception {
		if (getStr(cust_fname).length() > 200) {
			throw new Exception("ฟิลด์ BXHEADER.CUST_FNAME ข้อมูลยาวเกิน 200 ตัวอักษร ตัวอย่างข้อมูล : \n " + cust_fname + " ");
		}
		this.CUST_FNAME = cust_fname;
	}

	public String getCUST_LNAME() {
		return this.CUST_LNAME;
	}

	public void setCUST_LNAME(String cust_lname) throws Exception {
		if (getStr(cust_lname).length() > 200) {
			throw new Exception("ฟิลด์ BXHEADER.CUST_LNAME ข้อมูลยาวเกิน 200 ตัวอักษร ตัวอย่างข้อมูล : \n " + cust_lname + " ");
		}
		this.CUST_LNAME = cust_lname;
	}

	public String getCUST_ADDR1() {
		return this.CUST_ADDR1;
	}

	public void setCUST_ADDR1(String cust_addr1) throws Exception {
		if (getStr(cust_addr1).length() > 200) {
			throw new Exception("ฟิลด์ BXHEADER.CUST_ADDR1 ข้อมูลยาวเกิน 200 ตัวอักษร ตัวอย่างข้อมูล : \n " + cust_addr1 + " ");
		}
		this.CUST_ADDR1 = cust_addr1;
	}

	public String getCUST_ADDR2() {
		return this.CUST_ADDR2;
	}

	public void setCUST_ADDR2(String cust_addr2) throws Exception {
		if (getStr(cust_addr2).length() > 200) {
			throw new Exception("ฟิลด์ BXHEADER.CUST_ADDR2 ข้อมูลยาวเกิน 200 ตัวอักษร ตัวอย่างข้อมูล : \n " + cust_addr2 + " ");
		}
		this.CUST_ADDR2 = cust_addr2;
	}

	public String getZIPCODE() {
		return this.ZIPCODE;
	}

	public void setZIPCODE(String zipcode) throws Exception {
		if (getStr(zipcode).length() > 20) {
			throw new Exception("ฟิลด์ BXHEADER.ZIPCODE ข้อมูลยาวเกิน 20 ตัวอักษร ตัวอย่างข้อมูล : \n " + zipcode + " ");
		}
		this.ZIPCODE = zipcode;
	}

	public String getREMARK1() {
		return this.REMARK1;
	}

	public void setREMARK1(String remark1) throws Exception {
		if (getStr(remark1).length() > 200) {
			throw new Exception("ฟิลด์ BXHEADER.REMARK1 ข้อมูลยาวเกิน 200 ตัวอักษร ตัวอย่างข้อมูล : \n " + remark1 + " ");
		}
		this.REMARK1 = remark1;
	}

	public BigDecimal getVATRATE() {
		return this.VATRATE;
	}

	public void setVATRATE(BigDecimal vatrate) throws Exception {
		this.VATRATE = vatrate;
	}

	public BigDecimal getVATAMT() {
		return this.VATAMT;
	}

	public void setVATAMT(BigDecimal vatamt) throws Exception {
		this.VATAMT = vatamt;
	}

	public BigDecimal getBASAMT() {
		return this.BASAMT;
	}

	public void setBASAMT(BigDecimal basamt) throws Exception {
		this.BASAMT = basamt;
	}

	public BigDecimal getTOTALAMT() {
		return this.TOTALAMT;
	}

	public void setTOTALAMT(BigDecimal totalamt) throws Exception {
		this.TOTALAMT = totalamt;
	}

	public int getBRANC_CDE() {
		return this.BRANC_CDE;
	}

	public void setBRANC_CDE(int branc_cde) throws Exception {
		this.BRANC_CDE = branc_cde;
	}

	public int getRECSTA() {
		return this.RECSTA;
	}

	public void setRECSTA(int recsta) throws Exception {
		this.RECSTA = recsta;
	}

	public BigDecimal getCOSTVATAMT() {
		return this.COSTVATAMT;
	}

	public void setCOSTVATAMT(BigDecimal costvatamt) throws Exception {
		this.COSTVATAMT = costvatamt;
	}

	public BigDecimal getCOSTBASAMT() {
		return this.COSTBASAMT;
	}

	public void setCOSTBASAMT(BigDecimal costbasamt) throws Exception {
		this.COSTBASAMT = costbasamt;
	}

	public String getCUST_CDE_AP() {
		return this.CUST_CDE_AP;
	}

	public void setCUST_CDE_AP(String cust_cde_ap) throws Exception {
		if (getStr(cust_cde_ap).length() > 15) {
			throw new Exception("ฟิลด์ BXHEADER.CUST_CDE_AP ข้อมูลยาวเกิน 15 ตัวอักษร ตัวอย่างข้อมูล : \n " + cust_cde_ap + " ");
		}
		this.CUST_CDE_AP = cust_cde_ap;
	}

	public String getAPNAME() {
		return this.APNAME;
	}

	public void setAPNAME(String apname) throws Exception {
		if (getStr(apname).length() > 250) {
			throw new Exception("ฟิลด์ BXHEADER.APNAME ข้อมูลยาวเกิน 250 ตัวอักษร ตัวอย่างข้อมูล : \n " + apname + " ");
		}
		this.APNAME = apname;
	}

	public Date getSENDDATE() {
		return this.SENDDATE;
	}

	public void setSENDDATE(Date senddate) throws Exception {
		this.SENDDATE = senddate;
	}

	public String getUPBY() {
		return this.UPBY;
	}

	public void setUPBY(String upby) throws Exception {
		if (getStr(upby).length() > 35) {
			throw new Exception("ฟิลด์ BXHEADER.UPBY ข้อมูลยาวเกิน 35 ตัวอักษร ตัวอย่างข้อมูล : \n " + upby + " ");
		}
		this.UPBY = upby;
	}

	public Timestamp getUPDT() {
		return this.UPDT;
	}

	public void setUPDT(Timestamp updt) throws Exception {
		this.UPDT = updt;
	}

	public BigDecimal getDISCOUNTAMT() {
		return this.DISCOUNTAMT;
	}

	public void setDISCOUNTAMT(BigDecimal discountamt) throws Exception {
		this.DISCOUNTAMT = discountamt;
	}
}
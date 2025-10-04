package com.pcc.tk.tbo;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.pcc.sys.tbo.TABLE;

public class TboTKJOBEXPENSES extends TABLE {

	private String COMP_CDE;
	private String JOBNO;
	private int SEQ = 0;
	private int EXPENSESID = 0;
	private BigDecimal EXPENSESAMT = BigDecimal.ZERO;
	private BigDecimal WITHDRAWAL_AMT = BigDecimal.ZERO;
	private String EXP_REMARK;
	private String UPDBY;
	private Timestamp UPDDTE;
	private BigDecimal EXPCOM_ADV = BigDecimal.ZERO;

	public static final String tablename = "tkjobexpenses";

	public String getTableName() {
		return schema + tablename;
	}

	public TboTKJOBEXPENSES() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) throws Exception {
		if (getStr(comp_cde).length() > 5) {
			throw new Exception("ฟิลด์ TKJOBEXPENSES.COMP_CDE ข้อมูลยาวเกิน 5 ตัวอักษร ตัวอย่างข้อมูล : \n " + comp_cde + " ");
		}
		this.COMP_CDE = comp_cde;
	}

	public String getJOBNO() {
		return this.JOBNO;
	}

	public void setJOBNO(String jobno) throws Exception {
		if (getStr(jobno).length() > 20) {
			throw new Exception("ฟิลด์ TKJOBEXPENSES.JOBNO ข้อมูลยาวเกิน 20 ตัวอักษร ตัวอย่างข้อมูล : \n " + jobno + " ");
		}
		this.JOBNO = jobno;
	}

	public int getSEQ() {
		return this.SEQ;
	}

	public void setSEQ(int seq) throws Exception {
		this.SEQ = seq;
	}

	public int getEXPENSESID() {
		return this.EXPENSESID;
	}

	public void setEXPENSESID(int expensesid) throws Exception {
		this.EXPENSESID = expensesid;
	}

	public BigDecimal getEXPENSESAMT() {
		return this.EXPENSESAMT;
	}

	public void setEXPENSESAMT(BigDecimal expensesamt) throws Exception {
		this.EXPENSESAMT = expensesamt;
	}

	public BigDecimal getWITHDRAWAL_AMT() {
		return this.WITHDRAWAL_AMT;
	}

	public void setWITHDRAWAL_AMT(BigDecimal withdrawal_amt) throws Exception {
		this.WITHDRAWAL_AMT = withdrawal_amt;
	}

	public String getEXP_REMARK() {
		return this.EXP_REMARK;
	}

	public void setEXP_REMARK(String exp_remark) throws Exception {
		if (getStr(exp_remark).length() > 250) {
			throw new Exception("ฟิลด์ TKJOBEXPENSES.EXP_REMARK ข้อมูลยาวเกิน 250 ตัวอักษร ตัวอย่างข้อมูล : \n " + exp_remark + " ");
		}
		this.EXP_REMARK = exp_remark;
	}

	public String getUPDBY() {
		return this.UPDBY;
	}

	public void setUPDBY(String updby) throws Exception {
		if (getStr(updby).length() > 100) {
			throw new Exception("ฟิลด์ TKJOBEXPENSES.UPDBY ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + updby + " ");
		}
		this.UPDBY = updby;
	}

	public Timestamp getUPDDTE() {
		return this.UPDDTE;
	}

	public void setUPDDTE(Timestamp upddte) throws Exception {
		this.UPDDTE = upddte;
	}

	public BigDecimal getEXPCOM_ADV() {
		return this.EXPCOM_ADV;
	}

	public void setEXPCOM_ADV(BigDecimal expcom_adv) throws Exception {
		this.EXPCOM_ADV = expcom_adv;
	}
}
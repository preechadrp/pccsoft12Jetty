package com.pcc.tk.tbo;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.pcc.sys.tbo.TABLE;

public class TboTKJOB extends TABLE {

	private String COMP_CDE;
	private String JOBNO;
	private Date JOBDATE;
	private int LAWSTATID = 0;
	private Date SENDJOBDATE;
	private int JOBCODE = 0;
	private String CUSTCONTACTNO;
	private String PLAINT;
	private Date SUEATDATE;
	private Date SUEDDATE;
	private BigDecimal SUECOSTAMT = BigDecimal.ZERO;
	private BigDecimal FEEAMT = BigDecimal.ZERO;
	private String REMARK1;
	private Date LAWBLACKDATE;
	private String LAWBLACKNO;
	private Date LAWREDDATE;
	private String LAWREDNO;
	private int COURTID = 0;
	private String REMARK2;
	private Date CLOSEDATE;
	private String JOBSTAT;
	private String DELBY;
	private Timestamp DELDTE;
	private String INSBY;
	private Timestamp INSDTE;
	private String UPDBY;
	private Timestamp UPDDTE;
	private int LAWTYPEID = 0;
	private int ZONEID = 0;

	public static final String tablename = "tkjob";

	public String getTableName() {
		return schema + tablename;
	}

	public TboTKJOB() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) throws Exception {
		if (getStr(comp_cde).length() > 5) {
			throw new Exception("ฟิลด์ TKJOB.COMP_CDE ข้อมูลยาวเกิน 5 ตัวอักษร ตัวอย่างข้อมูล : \n " + comp_cde + " ");
		}
		this.COMP_CDE = comp_cde;
	}

	public String getJOBNO() {
		return this.JOBNO;
	}

	public void setJOBNO(String jobno) throws Exception {
		if (getStr(jobno).length() > 20) {
			throw new Exception("ฟิลด์ TKJOB.JOBNO ข้อมูลยาวเกิน 20 ตัวอักษร ตัวอย่างข้อมูล : \n " + jobno + " ");
		}
		this.JOBNO = jobno;
	}

	public Date getJOBDATE() {
		return this.JOBDATE;
	}

	public void setJOBDATE(Date jobdate) throws Exception {
		this.JOBDATE = jobdate;
	}

	public int getLAWSTATID() {
		return this.LAWSTATID;
	}

	public void setLAWSTATID(int lawstatid) throws Exception {
		this.LAWSTATID = lawstatid;
	}

	public Date getSENDJOBDATE() {
		return this.SENDJOBDATE;
	}

	public void setSENDJOBDATE(Date sendjobdate) throws Exception {
		this.SENDJOBDATE = sendjobdate;
	}

	public int getJOBCODE() {
		return this.JOBCODE;
	}

	public void setJOBCODE(int jobcode) throws Exception {
		this.JOBCODE = jobcode;
	}

	public String getCUSTCONTACTNO() {
		return this.CUSTCONTACTNO;
	}

	public void setCUSTCONTACTNO(String custcontactno) throws Exception {
		if (getStr(custcontactno).length() > 100) {
			throw new Exception("ฟิลด์ TKJOB.CUSTCONTACTNO ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + custcontactno + " ");
		}
		this.CUSTCONTACTNO = custcontactno;
	}

	public String getPLAINT() {
		return this.PLAINT;
	}

	public void setPLAINT(String plaint) throws Exception {
		if (getStr(plaint).length() > 250) {
			throw new Exception("ฟิลด์ TKJOB.PLAINT ข้อมูลยาวเกิน 250 ตัวอักษร ตัวอย่างข้อมูล : \n " + plaint + " ");
		}
		this.PLAINT = plaint;
	}

	public Date getSUEATDATE() {
		return this.SUEATDATE;
	}

	public void setSUEATDATE(Date sueatdate) throws Exception {
		this.SUEATDATE = sueatdate;
	}

	public Date getSUEDDATE() {
		return this.SUEDDATE;
	}

	public void setSUEDDATE(Date sueddate) throws Exception {
		this.SUEDDATE = sueddate;
	}

	public BigDecimal getSUECOSTAMT() {
		return this.SUECOSTAMT;
	}

	public void setSUECOSTAMT(BigDecimal suecostamt) throws Exception {
		this.SUECOSTAMT = suecostamt;
	}

	public BigDecimal getFEEAMT() {
		return this.FEEAMT;
	}

	public void setFEEAMT(BigDecimal feeamt) throws Exception {
		this.FEEAMT = feeamt;
	}

	public String getREMARK1() {
		return this.REMARK1;
	}

	public void setREMARK1(String remark1) throws Exception {
		if (getStr(remark1).length() > 250) {
			throw new Exception("ฟิลด์ TKJOB.REMARK1 ข้อมูลยาวเกิน 250 ตัวอักษร ตัวอย่างข้อมูล : \n " + remark1 + " ");
		}
		this.REMARK1 = remark1;
	}

	public Date getLAWBLACKDATE() {
		return this.LAWBLACKDATE;
	}

	public void setLAWBLACKDATE(Date lawblackdate) throws Exception {
		this.LAWBLACKDATE = lawblackdate;
	}

	public String getLAWBLACKNO() {
		return this.LAWBLACKNO;
	}

	public void setLAWBLACKNO(String lawblackno) throws Exception {
		if (getStr(lawblackno).length() > 100) {
			throw new Exception("ฟิลด์ TKJOB.LAWBLACKNO ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + lawblackno + " ");
		}
		this.LAWBLACKNO = lawblackno;
	}

	public Date getLAWREDDATE() {
		return this.LAWREDDATE;
	}

	public void setLAWREDDATE(Date lawreddate) throws Exception {
		this.LAWREDDATE = lawreddate;
	}

	public String getLAWREDNO() {
		return this.LAWREDNO;
	}

	public void setLAWREDNO(String lawredno) throws Exception {
		if (getStr(lawredno).length() > 100) {
			throw new Exception("ฟิลด์ TKJOB.LAWREDNO ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + lawredno + " ");
		}
		this.LAWREDNO = lawredno;
	}

	public int getCOURTID() {
		return this.COURTID;
	}

	public void setCOURTID(int courtid) throws Exception {
		this.COURTID = courtid;
	}

	public String getREMARK2() {
		return this.REMARK2;
	}

	public void setREMARK2(String remark2) throws Exception {
		if (getStr(remark2).length() > 250) {
			throw new Exception("ฟิลด์ TKJOB.REMARK2 ข้อมูลยาวเกิน 250 ตัวอักษร ตัวอย่างข้อมูล : \n " + remark2 + " ");
		}
		this.REMARK2 = remark2;
	}

	public Date getCLOSEDATE() {
		return this.CLOSEDATE;
	}

	public void setCLOSEDATE(Date closedate) throws Exception {
		this.CLOSEDATE = closedate;
	}

	public String getJOBSTAT() {
		return this.JOBSTAT;
	}

	public void setJOBSTAT(String jobstat) throws Exception {
		if (getStr(jobstat).length() > 2) {
			throw new Exception("ฟิลด์ TKJOB.JOBSTAT ข้อมูลยาวเกิน 2 ตัวอักษร ตัวอย่างข้อมูล : \n " + jobstat + " ");
		}
		this.JOBSTAT = jobstat;
	}

	public String getDELBY() {
		return this.DELBY;
	}

	public void setDELBY(String delby) throws Exception {
		if (getStr(delby).length() > 100) {
			throw new Exception("ฟิลด์ TKJOB.DELBY ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + delby + " ");
		}
		this.DELBY = delby;
	}

	public Timestamp getDELDTE() {
		return this.DELDTE;
	}

	public void setDELDTE(Timestamp deldte) throws Exception {
		this.DELDTE = deldte;
	}

	public String getINSBY() {
		return this.INSBY;
	}

	public void setINSBY(String insby) throws Exception {
		if (getStr(insby).length() > 100) {
			throw new Exception("ฟิลด์ TKJOB.INSBY ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + insby + " ");
		}
		this.INSBY = insby;
	}

	public Timestamp getINSDTE() {
		return this.INSDTE;
	}

	public void setINSDTE(Timestamp insdte) throws Exception {
		this.INSDTE = insdte;
	}

	public String getUPDBY() {
		return this.UPDBY;
	}

	public void setUPDBY(String updby) throws Exception {
		if (getStr(updby).length() > 100) {
			throw new Exception("ฟิลด์ TKJOB.UPDBY ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + updby + " ");
		}
		this.UPDBY = updby;
	}

	public Timestamp getUPDDTE() {
		return this.UPDDTE;
	}

	public void setUPDDTE(Timestamp upddte) throws Exception {
		this.UPDDTE = upddte;
	}

	public int getLAWTYPEID() {
		return this.LAWTYPEID;
	}

	public void setLAWTYPEID(int lawtypeid) throws Exception {
		this.LAWTYPEID = lawtypeid;
	}

	public int getZONEID() {
		return this.ZONEID;
	}

	public void setZONEID(int zoneid) throws Exception {
		this.ZONEID = zoneid;
	}

}
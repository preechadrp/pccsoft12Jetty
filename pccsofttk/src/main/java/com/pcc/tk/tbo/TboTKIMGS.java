package com.pcc.tk.tbo;

import com.pcc.sys.tbo.TABLE;

public class TboTKIMGS extends TABLE {

	private String COMP_CDE;
	private String SYS_CDE;
	private int IMGSEQ = 0;
	private String IMGDESC;
	private String IMGTYPE;
	private byte[] IMGDATA;

	public static final String tablename = "tkimgs";

	public String getTableName() {
		return schema + tablename;
	}

	public TboTKIMGS() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) throws Exception {
		if (getStr(comp_cde).length() > 5) {
			throw new Exception("ฟิลด์ TKIMGS.COMP_CDE ข้อมูลยาวเกิน 5 ตัวอักษร ตัวอย่างข้อมูล : \n " + comp_cde + " ");
		}
		this.COMP_CDE = comp_cde;
	}

	public String getSYS_CDE() {
		return this.SYS_CDE;
	}

	public void setSYS_CDE(String sys_cde) throws Exception {
		if (getStr(sys_cde).length() > 50) {
			throw new Exception("ฟิลด์ TKIMGS.SYS_CDE ข้อมูลยาวเกิน 50 ตัวอักษร ตัวอย่างข้อมูล : \n " + sys_cde + " ");
		}
		this.SYS_CDE = sys_cde;
	}

	public int getIMGSEQ() {
		return this.IMGSEQ;
	}

	public void setIMGSEQ(int imgseq) throws Exception {
		this.IMGSEQ = imgseq;
	}

	public String getIMGDESC() {
		return this.IMGDESC;
	}

	public void setIMGDESC(String imgdesc) throws Exception {
		if (getStr(imgdesc).length() > 250) {
			throw new Exception("ฟิลด์ TKIMGS.IMGDESC ข้อมูลยาวเกิน 250 ตัวอักษร ตัวอย่างข้อมูล : \n " + imgdesc + " ");
		}
		this.IMGDESC = imgdesc;
	}

	public String getIMGTYPE() {
		return this.IMGTYPE;
	}

	public void setIMGTYPE(String imgtype) throws Exception {
		if (getStr(imgtype).length() > 50) {
			throw new Exception("ฟิลด์ TKIMGS.IMGTYPE ข้อมูลยาวเกิน 50 ตัวอักษร ตัวอย่างข้อมูล : \n " + imgtype + " ");
		}
		this.IMGTYPE = imgtype;
	}

	public byte[] getIMGDATA() {
		return this.IMGDATA;
	}

	public void setIMGDATA(byte[] imgdata) throws Exception {
		this.IMGDATA = imgdata;
	}
}
package com.pcc.sys.tbo;

public class TboFAMPHUR extends TABLE {

	private int AMPHURID = 0;
	private String AMPHURNAME;
	private int PROVIN_ID = 0;
	private String ZIPCODE;

	public static final String tablename = "famphur";

	public String getTableName() {
		return schema + tablename;
	}

	public TboFAMPHUR() {
		super();
	}

	public int getAMPHURID() {
		return this.AMPHURID;
	}

	public void setAMPHURID(int amphurid) throws Exception {
		this.AMPHURID = amphurid;
	}

	public String getAMPHURNAME() {
		return this.AMPHURNAME;
	}

	public void setAMPHURNAME(String amphurname) throws Exception {
		if (getStr(amphurname).length() > 40) {
			throw new Exception("ฟิลด์ FAMPHUR.AMPHURNAME ข้อมูลยาวเกิน 40 ตัวอักษร ตัวอย่างข้อมูล : \n " + amphurname + " ");
		}
		this.AMPHURNAME = amphurname;
	}

	public int getPROVIN_ID() {
		return this.PROVIN_ID;
	}

	public void setPROVIN_ID(int provin_id) throws Exception {
		this.PROVIN_ID = provin_id;
	}

	public String getZIPCODE() {
		return this.ZIPCODE;
	}

	public void setZIPCODE(String zipcode) throws Exception {
		if (getStr(zipcode).length() > 20) {
			throw new Exception("ฟิลด์ FAMPHUR.ZIPCODE ข้อมูลยาวเกิน 20 ตัวอักษร ตัวอย่างข้อมูล : \n " + zipcode + " ");
		}
		this.ZIPCODE = zipcode;
	}
}
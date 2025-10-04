package com.pcc.sys.tbo;

public class TboFPROVINCE extends TABLE {

	private int PROVIN_ID = 0;
	private String PROVIN_NAME;

	public static final String tablename = "fprovince";

	public String getTableName() {
		return schema + tablename;
	}

	public TboFPROVINCE() {
		super();
	}

	public int getPROVIN_ID() {
		return this.PROVIN_ID;
	}

	public void setPROVIN_ID(int provin_id) throws Exception {
		this.PROVIN_ID = provin_id;
	}

	public String getPROVIN_NAME() {
		return this.PROVIN_NAME;
	}

	public void setPROVIN_NAME(String provin_name) throws Exception {
		if (getStr(provin_name).length() > 100) {
			throw new Exception("ฟิลด์ FPROVINCE.PROVIN_NAME ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + provin_name + " ");
		}
		this.PROVIN_NAME = provin_name;
	}
}
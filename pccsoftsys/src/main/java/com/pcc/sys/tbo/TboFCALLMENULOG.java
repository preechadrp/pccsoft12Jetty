package com.pcc.sys.tbo;

import java.sql.Timestamp;

public class TboFCALLMENULOG extends TABLE {

	private String MENU_ID2;
	private String THAI_NAME;
	private Timestamp CALLTIME;
	private String USER_ID;

	public static final String tablename = "fcallmenulog";

	public String getTableName() {
		return schema + tablename;
	}

	public TboFCALLMENULOG() {
		super();
	}

	public String getMENU_ID2() {
		return this.MENU_ID2;
	}

	public void setMENU_ID2(String menu_id2) {
		this.MENU_ID2 = menu_id2;
	}

	public String getTHAI_NAME() {
		return this.THAI_NAME;
	}

	public void setTHAI_NAME(String thai_name) {
		this.THAI_NAME = thai_name;
	}

	public Timestamp getCALLTIME() {
		return this.CALLTIME;
	}

	public void setCALLTIME(Timestamp calltime) {
		this.CALLTIME = calltime;
	}

	public String getUSER_ID() {
		return this.USER_ID;
	}

	public void setUSER_ID(String user_id) {
		this.USER_ID = user_id;
	}
}
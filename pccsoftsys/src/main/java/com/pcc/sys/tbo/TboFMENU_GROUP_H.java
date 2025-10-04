package com.pcc.sys.tbo;

public class TboFMENU_GROUP_H extends TABLE {

	private String USER_MENU_GROUP;
	private String THAI_NAME;
	private String ENG_NAME;

	public static final String tablename = "fmenu_group_h";

	public String getTableName() {
		return schema + tablename;
	}

	public TboFMENU_GROUP_H() {
		super();
	}

	public String getUSER_MENU_GROUP() {
		return this.USER_MENU_GROUP;
	}

	public void setUSER_MENU_GROUP(String user_menu_group) {
		this.USER_MENU_GROUP = user_menu_group;
	}

	public String getTHAI_NAME() {
		return this.THAI_NAME;
	}

	public void setTHAI_NAME(String thai_name) {
		this.THAI_NAME = thai_name;
	}

	public String getENG_NAME() {
		return this.ENG_NAME;
	}

	public void setENG_NAME(String eng_name) {
		this.ENG_NAME = eng_name;
	}
}
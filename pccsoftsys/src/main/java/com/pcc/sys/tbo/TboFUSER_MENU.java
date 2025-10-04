package com.pcc.sys.tbo;

public class TboFUSER_MENU extends TABLE {

	private String USER_ID;
	private String COMP_CDE;
	private String USER_MENU_GROUP;

	public static final String tablename = "fuser_menu";

	public String getTableName() {
		return schema + tablename;
	}

	public TboFUSER_MENU() {
		super();
	}

	public String getUSER_ID() {
		return this.USER_ID;
	}

	public void setUSER_ID(String user_id) {
		this.USER_ID = user_id;
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) {
		this.COMP_CDE = comp_cde;
	}

	public String getUSER_MENU_GROUP() {
		return this.USER_MENU_GROUP;
	}

	public void setUSER_MENU_GROUP(String user_menu_group) {
		this.USER_MENU_GROUP = user_menu_group;
	}
}
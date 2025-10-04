package com.pcc.sys.tbo;

public class TboFMENU_GROUP_D extends TABLE {

	private String USER_MENU_GROUP;
	private String KEY1;
	private String MENU_LEVEL;
	private String MODU;
	private String MENU_ID1;
	private String MENU_ID2;
	private String THAI_NAME;
	private String ENG_NAME;
	private String MENU_ID3;

	public static final String tablename = "fmenu_group_d";

	public String getTableName() {
		return schema + tablename;
	}

	public TboFMENU_GROUP_D() {
		super();
	}

	public String getUSER_MENU_GROUP() {
		return this.USER_MENU_GROUP;
	}

	public void setUSER_MENU_GROUP(String user_menu_group) throws Exception {
		if (getStr(user_menu_group).length() > 20) {
			throw new Exception("ฟิลด์ FMENU_GROUP_D.USER_MENU_GROUP ข้อมูลยาวเกิน 20 ตัวอักษร ตัวอย่างข้อมูล : \n " + user_menu_group + " ");
		}
		this.USER_MENU_GROUP = user_menu_group;
	}

	public String getKEY1() {
		return this.KEY1;
	}

	public void setKEY1(String key1) throws Exception {
		if (getStr(key1).length() > 15) {
			throw new Exception("ฟิลด์ FMENU_GROUP_D.KEY1 ข้อมูลยาวเกิน 15 ตัวอักษร ตัวอย่างข้อมูล : \n " + key1 + " ");
		}
		this.KEY1 = key1;
	}

	public String getMENU_LEVEL() {
		return this.MENU_LEVEL;
	}

	public void setMENU_LEVEL(String menu_level) throws Exception {
		if (getStr(menu_level).length() > 2) {
			throw new Exception("ฟิลด์ FMENU_GROUP_D.MENU_LEVEL ข้อมูลยาวเกิน 2 ตัวอักษร ตัวอย่างข้อมูล : \n " + menu_level + " ");
		}
		this.MENU_LEVEL = menu_level;
	}

	public String getMODU() {
		return this.MODU;
	}

	public void setMODU(String modu) throws Exception {
		if (getStr(modu).length() > 100) {
			throw new Exception("ฟิลด์ FMENU_GROUP_D.MODU ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + modu + " ");
		}
		this.MODU = modu;
	}

	public String getMENU_ID1() {
		return this.MENU_ID1;
	}

	public void setMENU_ID1(String menu_id1) throws Exception {
		if (getStr(menu_id1).length() > 50) {
			throw new Exception("ฟิลด์ FMENU_GROUP_D.MENU_ID1 ข้อมูลยาวเกิน 50 ตัวอักษร ตัวอย่างข้อมูล : \n " + menu_id1 + " ");
		}
		this.MENU_ID1 = menu_id1;
	}

	public String getMENU_ID2() {
		return this.MENU_ID2;
	}

	public void setMENU_ID2(String menu_id2) throws Exception {
		if (getStr(menu_id2).length() > 50) {
			throw new Exception("ฟิลด์ FMENU_GROUP_D.MENU_ID2 ข้อมูลยาวเกิน 50 ตัวอักษร ตัวอย่างข้อมูล : \n " + menu_id2 + " ");
		}
		this.MENU_ID2 = menu_id2;
	}

	public String getTHAI_NAME() {
		return this.THAI_NAME;
	}

	public void setTHAI_NAME(String thai_name) throws Exception {
		if (getStr(thai_name).length() > 100) {
			throw new Exception("ฟิลด์ FMENU_GROUP_D.THAI_NAME ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + thai_name + " ");
		}
		this.THAI_NAME = thai_name;
	}

	public String getENG_NAME() {
		return this.ENG_NAME;
	}

	public void setENG_NAME(String eng_name) throws Exception {
		if (getStr(eng_name).length() > 100) {
			throw new Exception("ฟิลด์ FMENU_GROUP_D.ENG_NAME ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + eng_name + " ");
		}
		this.ENG_NAME = eng_name;
	}

	public String getMENU_ID3() {
		return this.MENU_ID3;
	}

	public void setMENU_ID3(String menu_id3) throws Exception {
		if (getStr(menu_id3).length() > 50) {
			throw new Exception("ฟิลด์ FMENU_GROUP_D.MENU_ID3 ข้อมูลยาวเกิน 50 ตัวอักษร ตัวอย่างข้อมูล : \n " + menu_id3 + " ");
		}
		this.MENU_ID3 = menu_id3;
	}

}
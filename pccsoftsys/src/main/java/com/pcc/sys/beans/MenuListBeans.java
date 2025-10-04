package com.pcc.sys.beans;

public class MenuListBeans {
	//{ "1", "comm", "", "", "ข้อมูลทั่วไป", "Setup" ,""},
	//{ "2", "ac", "FrmComp", "FrmComp", "บริษัท", "company" ,""},
	//level, module, menuid1, menuid2, thai_title, eng_title, sclass
	private String key;
	private String level;
	private String moduid;
	private String menuid1;
	private String menuid2;
	private String menuid3;
	private String thai_title;
	private String eng_title;
	private String sclass;
	private String tooltipText;

	public MenuListBeans() {
		//..
	}

	/**
	 * constructor
	 * @param key key
	 * @param level ระดับ
	 * @param moduid รหัสกลุ่ม
	 * @param menuid1 รหัสเมนู 1 บอกให้รู้ว่าจะเรียกไฟล์ .zul ไหน
	 * @param menuid2 รหัสเมนู 2 ใช้แยก process การทำงาน
	 * @param menuid3 รหัสเมนู 3 ใช้เป็นตัวจับตอนจัดกลุ่มเมนู
	 * @param thai_title ชื่อเมนูภาษาไทย
	 * @param eng_title ชื่อเมนูภาษา Eng
	 * @param sclass CSS class
	 * @param tooltipText
	 */
	public MenuListBeans(String key, String level, String moduid, String menuid1,
			String menuid2, String menuid3, String thai_title, String eng_title, String sclass, String tooltipText) {
		this.key = key;
		this.level = level;
		this.moduid = moduid;
		this.menuid1 = menuid1;
		this.menuid2 = menuid2;
		this.menuid3 = menuid3;
		this.thai_title = thai_title;
		this.eng_title = eng_title;
		this.sclass = sclass;
		this.tooltipText = tooltipText;

	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getModuid() {
		return moduid;
	}

	public void setModuid(String moduid) {
		this.moduid = moduid;
	}

	public String getMenuid1() {
		return menuid1;
	}

	public void setMenuid1(String menuid1) {
		this.menuid1 = menuid1;
	}

	public String getMenuid2() {
		return menuid2;
	}

	public void setMenuid2(String menuid2) {
		this.menuid2 = menuid2;
	}

	public String getMenuid3() {
		return menuid3;
	}

	public void setMenuid3(String menuid3) {
		this.menuid3 = menuid3;
	}

	public String getThai_title() {
		return thai_title;
	}

	public void setThai_title(String thai_title) {
		this.thai_title = thai_title;
	}

	public String getEng_title() {
		return eng_title;
	}

	public void setEng_title(String eng_title) {
		this.eng_title = eng_title;
	}

	public String getSclass() {
		return sclass;
	}

	public void setSclass(String sclass) {
		this.sclass = sclass;
	}

	public String getTooltipText() {
		return tooltipText;
	}

	public void setTooltipText(String tooltipText) {
		this.tooltipText = tooltipText;
	}

}

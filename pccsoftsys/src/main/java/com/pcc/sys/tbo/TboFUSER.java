package com.pcc.sys.tbo;

import java.sql.Date;
import java.sql.Timestamp;

public class TboFUSER extends TABLE {

	private String USER_ID;
	private String PASSWD;
	private String TITLE;
	private String FNAME_THAI;
	private String LNAME_THAI;
	private String USER_STATUS;
	private String INSBY;
	private Timestamp INSDT;
	private String UPBY;
	private Timestamp UPDT;
	private Date EXPIRE_PASS_DATE;
	private Date UPDATE_PASS_DATE;
	private String MENU_LANG;
	private String MAN_USER;
	private String MAN_MENU_GROUP;
	private String LAST_USE_COMP_CDE;
	private String LAST_USE_BRANCH_ID;

	public static final String tablename = "fuser";

	public String getTableName() {
		return schema + tablename;
	}

	public TboFUSER() {
		super();
	}

	public String getUSER_ID() {
		return this.USER_ID;
	}

	public void setUSER_ID(String user_id) throws Exception {
		if (getStr(user_id).length() > 30) {
			throw new Exception("ฟิลด์ FUSER.USER_ID ข้อมูลยาวเกิน 30 ตัวอักษร ตัวอย่างข้อมูล : \n " + user_id + " ");
		}
		this.USER_ID = user_id;
	}

	public String getPASSWD() {
		return this.PASSWD;
	}

	public void setPASSWD(String passwd) throws Exception {
		if (getStr(passwd).length() > 100) {
			throw new Exception("ฟิลด์ FUSER.PASSWD ข้อมูลยาวเกิน 100 ตัวอักษร ตัวอย่างข้อมูล : \n " + passwd + " ");
		}
		this.PASSWD = passwd;
	}

	public String getTITLE() {
		return this.TITLE;
	}

	public void setTITLE(String title) throws Exception {
		if (getStr(title).length() > 25) {
			throw new Exception("ฟิลด์ FUSER.TITLE ข้อมูลยาวเกิน 25 ตัวอักษร ตัวอย่างข้อมูล : \n " + title + " ");
		}
		this.TITLE = title;
	}

	public String getFNAME_THAI() {
		return this.FNAME_THAI;
	}

	public void setFNAME_THAI(String fname_thai) throws Exception {
		if (getStr(fname_thai).length() > 50) {
			throw new Exception("ฟิลด์ FUSER.FNAME_THAI ข้อมูลยาวเกิน 50 ตัวอักษร ตัวอย่างข้อมูล : \n " + fname_thai + " ");
		}
		this.FNAME_THAI = fname_thai;
	}

	public String getLNAME_THAI() {
		return this.LNAME_THAI;
	}

	public void setLNAME_THAI(String lname_thai) throws Exception {
		if (getStr(lname_thai).length() > 50) {
			throw new Exception("ฟิลด์ FUSER.LNAME_THAI ข้อมูลยาวเกิน 50 ตัวอักษร ตัวอย่างข้อมูล : \n " + lname_thai + " ");
		}
		this.LNAME_THAI = lname_thai;
	}

	public String getUSER_STATUS() {
		return this.USER_STATUS;
	}

	public void setUSER_STATUS(String user_status) throws Exception {
		if (getStr(user_status).length() > 25) {
			throw new Exception("ฟิลด์ FUSER.USER_STATUS ข้อมูลยาวเกิน 25 ตัวอักษร ตัวอย่างข้อมูล : \n " + user_status + " ");
		}
		this.USER_STATUS = user_status;
	}

	public String getINSBY() {
		return this.INSBY;
	}

	public void setINSBY(String insby) throws Exception {
		if (getStr(insby).length() > 30) {
			throw new Exception("ฟิลด์ FUSER.INSBY ข้อมูลยาวเกิน 30 ตัวอักษร ตัวอย่างข้อมูล : \n " + insby + " ");
		}
		this.INSBY = insby;
	}

	public Timestamp getINSDT() {
		return this.INSDT;
	}

	public void setINSDT(Timestamp insdt) throws Exception {
		this.INSDT = insdt;
	}

	public String getUPBY() {
		return this.UPBY;
	}

	public void setUPBY(String upby) throws Exception {
		if (getStr(upby).length() > 30) {
			throw new Exception("ฟิลด์ FUSER.UPBY ข้อมูลยาวเกิน 30 ตัวอักษร ตัวอย่างข้อมูล : \n " + upby + " ");
		}
		this.UPBY = upby;
	}

	public Timestamp getUPDT() {
		return this.UPDT;
	}

	public void setUPDT(Timestamp updt) throws Exception {
		this.UPDT = updt;
	}

	public Date getEXPIRE_PASS_DATE() {
		return this.EXPIRE_PASS_DATE;
	}

	public void setEXPIRE_PASS_DATE(Date expire_pass_date) throws Exception {
		this.EXPIRE_PASS_DATE = expire_pass_date;
	}

	public Date getUPDATE_PASS_DATE() {
		return this.UPDATE_PASS_DATE;
	}

	public void setUPDATE_PASS_DATE(Date update_pass_date) throws Exception {
		this.UPDATE_PASS_DATE = update_pass_date;
	}

	public String getMENU_LANG() {
		return this.MENU_LANG;
	}

	public void setMENU_LANG(String menu_lang) throws Exception {
		if (getStr(menu_lang).length() > 1) {
			throw new Exception("ฟิลด์ FUSER.MENU_LANG ข้อมูลยาวเกิน 1 ตัวอักษร ตัวอย่างข้อมูล : \n " + menu_lang + " ");
		}
		this.MENU_LANG = menu_lang;
	}

	public String getMAN_USER() {
		return this.MAN_USER;
	}

	public void setMAN_USER(String man_user) throws Exception {
		if (getStr(man_user).length() > 1) {
			throw new Exception("ฟิลด์ FUSER.MAN_USER ข้อมูลยาวเกิน 1 ตัวอักษร ตัวอย่างข้อมูล : \n " + man_user + " ");
		}
		this.MAN_USER = man_user;
	}

	public String getMAN_MENU_GROUP() {
		return this.MAN_MENU_GROUP;
	}

	public void setMAN_MENU_GROUP(String man_menu_group) throws Exception {
		if (getStr(man_menu_group).length() > 1) {
			throw new Exception("ฟิลด์ FUSER.MAN_MENU_GROUP ข้อมูลยาวเกิน 1 ตัวอักษร ตัวอย่างข้อมูล : \n " + man_menu_group + " ");
		}
		this.MAN_MENU_GROUP = man_menu_group;
	}

	public String getLAST_USE_COMP_CDE() {
		return this.LAST_USE_COMP_CDE;
	}

	public void setLAST_USE_COMP_CDE(String last_use_comp_cde) throws Exception {
		if (getStr(last_use_comp_cde).length() > 5) {
			throw new Exception("ฟิลด์ FUSER.LAST_USE_COMP_CDE ข้อมูลยาวเกิน 5 ตัวอักษร ตัวอย่างข้อมูล : \n " + last_use_comp_cde + " ");
		}
		this.LAST_USE_COMP_CDE = last_use_comp_cde;
	}

	public String getLAST_USE_BRANCH_ID() {
		return this.LAST_USE_BRANCH_ID;
	}

	public void setLAST_USE_BRANCH_ID(String last_use_branch_id) throws Exception {
		if (getStr(last_use_branch_id).length() > 5) {
			throw new Exception("ฟิลด์ FUSER.LAST_USE_BRANCH_ID ข้อมูลยาวเกิน 5 ตัวอักษร ตัวอย่างข้อมูล : \n " + last_use_branch_id + " ");
		}
		this.LAST_USE_BRANCH_ID = last_use_branch_id;
	}
}
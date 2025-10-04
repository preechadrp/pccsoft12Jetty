package com.pcc.sys.beans;

import java.io.Serializable;

import com.pcc.sys.lib.Fnc;
import com.pcc.sys.tbo.TboFCOMP;
import com.pcc.sys.tbo.TboFUSER;

public class LoginBean implements Serializable {

	private static final long serialVersionUID = 7154286215887728036L;
	
	private TboFUSER tboFuser;
	private TboFCOMP tboFcomp;
	private String jwt = "";

	public TboFUSER getTboFuser() {
		return tboFuser;
	}

	public void setTboFuser(TboFUSER fuser) {
		this.tboFuser = fuser;
	}

	public TboFCOMP getTboFcomp() {
		return tboFcomp;
	}

	public void setTboFcomp(TboFCOMP fcomp) {
		this.tboFcomp = fcomp;
	}

	/**
	 * ช่วยให้ไม่ต้องพิมพ์ยาวแบบนี้ getTboFcomp().getCOMP_CDE()
	 * @return
	 */
	public String getCOMP_CDE() {
		return this.getTboFcomp().getCOMP_CDE();
	}
	
	public String getCOMP_NAME() {
		return this.getTboFcomp().getCOMP_NAME();
	}

	/**
	 * ช่วยให้ไม่ต้องพิมพ์ยาวแบบนี้ getTboFuser().getUSER_ID()
	 * @return
	 */
	public String getUSER_ID() {
		return this.getTboFuser().getUSER_ID();
	}
	
	public String getUserFullName() {
		String value = Fnc.getStr(getTboFuser().getTITLE()) + " " +
				Fnc.getStr(getTboFuser().getFNAME_THAI()) + " " +
				Fnc.getStr(getTboFuser().getLNAME_THAI());
		return value.trim();
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
	
}

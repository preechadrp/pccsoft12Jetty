package com.pcc.sys.lib;

public class MyDatebox extends org.zkoss.zul.Datebox {

	private static final long serialVersionUID = 1L;

	public MyDatebox() {
		setCols(11);
		setLocale("th_TH");
		setFormat("dd/MM/yyyy");
		setWidth("120px");
		//System.out.println("==> MyDatebox");
	}
	
	public void setReadonly(boolean readonly) {
		super.setReadonly(readonly);
		if (readonly == true) {
			this.setButtonVisible(false);
		} else {
			this.setButtonVisible(true);
		}
		//System.out.println("==> MyDatebox setReadonly");
	}
	
	public java.sql.Date getSqlDate() {
		if (this.getValue() != null) {
			return new java.sql.Date(this.getValue().getTime());
		} else {
			return null;
		}
	}

}

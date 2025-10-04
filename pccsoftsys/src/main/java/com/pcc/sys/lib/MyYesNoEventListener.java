package com.pcc.sys.lib;

import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;

import com.pcc.sys.beans.LoginBean;

/**
 * สำหรับ confirm dialog
 * @author by preecha 25/5/61
 */
public abstract class MyYesNoEventListener implements EventListener<Messagebox.ClickEvent> {
	private org.zkoss.zk.ui.event.Event subEvent;
	private LoginBean loginBean;
	private FModelHasMap parameter;
	private Object obj;
	private String callNextMethodName;

	public MyYesNoEventListener(Object obj, String callNextMethodName) {
		this.obj = obj;
		this.callNextMethodName = callNextMethodName;
	}

	/**
	 * ส่ง loginBean ของปุ่มที่เราเรียกเพื่อใช้เรียกย้อนกลับกรณีต้องการใช้
	 * @param loginBean
	 */
	public MyYesNoEventListener(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	/**
	 * ส่ง event และ loginBean ของปุ่มที่เราเรียกเพื่อใช้เรียกย้อนกลับกรณีต้องการใช้, ถ้าไม่มีให้ระบุเป็น null
	 * @param subEvent
	 * @param loginBean
	 */
	public MyYesNoEventListener(org.zkoss.zk.ui.event.Event subEvent, LoginBean loginBean) {
		this.subEvent = subEvent;
		this.loginBean = loginBean;
	}

	/**
	 * ส่ง parameter ของปุ่มที่เราเรียกเพื่อใช้เรียกย้อนกลับกรณีต้องการใช้, ถ้าไม่มีให้ระบุเป็น null
	 * @param loginBean
	 * @param parameter
	 */
	public MyYesNoEventListener(LoginBean loginBean, FModelHasMap parameter) {
		this.loginBean = loginBean;
		this.parameter = parameter;
	}

	/**
	 * ส่ง event , loginBean , parameter ของปุ่มที่เราเรียกเพื่อใช้เรียกย้อนกลับกรณีต้องการใช้, ถ้าไม่มีให้ระบุเป็น null
	 * @param subEvent
	 * @param loginBean
	 * @param parameter
	 */
	public MyYesNoEventListener(org.zkoss.zk.ui.event.Event subEvent, LoginBean loginBean, FModelHasMap parameter) {
		this.subEvent = subEvent;
		this.loginBean = loginBean;
		this.parameter = parameter;
	}

	public org.zkoss.zk.ui.event.Event getSubEvent() {
		return subEvent;
	}

	public void setSubEvent(org.zkoss.zk.ui.event.Event subEvent) {
		this.subEvent = subEvent;
	}

	public LoginBean getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	public FModelHasMap getParameter() {
		return parameter;
	}

	public void setParameter(FModelHasMap parameter) {
		this.parameter = parameter;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public String getCallNextMethodName() {
		return callNextMethodName;
	}

	public void setCallNextMethodName(String callNextMethodName) {
		this.callNextMethodName = callNextMethodName;
	}

}

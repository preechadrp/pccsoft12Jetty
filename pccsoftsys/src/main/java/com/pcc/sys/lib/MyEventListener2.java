package com.pcc.sys.lib;

import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;

/**
 * สำหรับ confirm dialog
 * @author by preecha 25/5/61
 */
public abstract class MyEventListener2 implements EventListener<Messagebox.ClickEvent> {

	private Object obj;
	private String callNextMethodName;

	public MyEventListener2(Object obj, String callNextMethodName) {
		this.obj = obj;
		this.callNextMethodName = callNextMethodName;
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

package com.pcc.sys.lib;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.InputElement;

import com.pcc.sys.beans.LoginBean;

/**
 * Main Windows
 * @author preecha.d
 *
 */
public abstract class FWindow extends Window {

	private static final long serialVersionUID = 1L;
	protected List<HtmlBasedComponent> objectList = new ArrayList<>();
	protected HtmlBasedComponent focusObj = null;
	private boolean doSetFormObject = false;
	private LoginBean loginBean;

	public void onCreate() {
		setCtrlKeys("#up#down");
		if (doSetFormObject == false) {
			setFormObj();
		}
		addEnterIndex();
		formInit();
	}

	/**
	 * map control ต่างๆ บน page
	 */
	public abstract void setFormObj();

	/**
	 * เพิ่ม control ที่ต้องการให้มีผลต่อการกดปุ่ม ctrl และ enter และ อื่นๆ
	 */
	public abstract void addEnterIndex();

	/**
	 * เมื่อฟอร์มเริ่มทำงานครั้งแรก
	 */
	public abstract void formInit();

	/**
	 * addSeqEnter เพื่อใช้ในการลำดับการโฟกัสกรณีกดปุ่ม Enter
	 * @param component
	 */
	public void addEnterIndex(HtmlBasedComponent component) {
		objectList.add(component);
		//เมื่อ Component โฟกัส
		component.addEventListener(Events.ON_FOCUS, new EventListener<Event>() {
			public void onEvent(Event ev) throws Exception {
				focusObj = (HtmlBasedComponent) ev.getTarget();
			}
		});
	}

	/**
	 * เมื่อกดปุ่ม Enter ใน Control ต่างๆ<br />
	 * เท่ากับกำหนด onOk ให้ Window เพราะ Class Window จะทำงานอัตโนมัติเมื่อสร้าง method นี้ขึ้นมา
	 */
	public void onOK() {
		if (focusObj instanceof Combobox) {
			moveNextObject();
		} else if (focusObj instanceof InputElement) {
			moveNextObject();
		}
	}

	private void moveNextObject() {
		int length = objectList.size();
		int curIndex = objectList.indexOf(focusObj);
		int maxMove = 20;

		for (int n = 0; n < maxMove; n++) {
			if (curIndex < (length - 1)) {
				int nextIndex = curIndex + 1;
				focusObj = objectList.get(nextIndex);
				if (ableMoveToObject(focusObj)) {
					moveToObject(focusObj);
					return;
				} else {
					curIndex++;
				}
			} else {
				return; //กรณีเป็นตัวสุดท้ายไม่ move  (By Preecha)
			}	
		}
		
	}

	private void moveToObject(HtmlBasedComponent focusObject) {
		focusObject.focus();
		if (focusObject instanceof InputElement) {
			((InputElement) focusObject).select();
		}
	}

	private boolean ableMoveToObject(HtmlBasedComponent comp) {
		//Textbox ,Bandbox ,Combobox ,Datebox, Decimalbox, Doublebox, Intbox
		// are subclass of InputElement

		if (comp instanceof Combobox) {
			//System.out.println("Combobox");
			Combobox combobox = (Combobox) comp;
			return (!combobox.isDisabled()) && combobox.isVisible();
		} else if (comp instanceof Button) {
			//System.out.println("Button");
			Button button = (Button) comp;
			return (!button.isDisabled()) && button.isVisible();
		} else if (comp instanceof Textbox) {
			//System.out.println("Textbox");
			Textbox textbox = (Textbox) comp;
			return (!textbox.isDisabled()) && textbox.isVisible() && (!textbox.isReadonly());
		} else if (comp instanceof InputElement) {
			//System.out.println("InputElement");
			InputElement inputElement = (InputElement) comp;
			return (!inputElement.isDisabled()) && inputElement.isVisible() && (!inputElement.isReadonly());		
		} else {
			//System.out.println("xxx = "+comp.getClass().getName());
		}
		return false;
	}

	/**
	 * ใช้ flag ค่าว่าได้เรียก method SetFormObj() แล้ว
	 * @return
	 */
	public boolean isDoSetFormObject() {
		return doSetFormObject;
	}

	/**
	 * ใช้ flag ค่าว่าได้เรียก method SetFormObj() แล้ว
	 * @param doSetFormObject
	 */
	public void setDoSetFormObject(boolean doSetFormObject) {
		this.doSetFormObject = doSetFormObject;
	}

	public LoginBean getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	/**
	 * เพิ่มเข้าใน tabpanal
	 * @param newID
	 * @param width
	 * @param parent
	 */
	public void setAppendMode(String newID, String width, Component parent) {
		this.setTitle("");
		this.setBorder("none");
		this.setId(newID);
		this.setWidth(width);
		this.setParent(parent);//ใช้คำสั่งนี้แล้วไม่ต้องใช้คำสั่ง frm1.doEmbedded();
	}
	
}

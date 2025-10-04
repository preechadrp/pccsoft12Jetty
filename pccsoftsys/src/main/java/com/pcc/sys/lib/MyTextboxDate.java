package com.pcc.sys.lib;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Textbox;

/**
 * แก้ปัญหากรณี ระบุวันที่ไม่ถูกไม่ให้ขึ้นข้อความ Error กรณีไปกดปุ่ม Clear
 * @author preecha.d
 *
 */
public class MyTextboxDate extends FTextboxDateMaster {

	private static final long serialVersionUID = 1L;

	public void onEvent(Event event) {
		if (event.getName().equalsIgnoreCase("onBlur")) {
			String error = isValidDate();
			if (!Fnc.isEmpty(error)) {
				Textbox tb = (Textbox) event.getTarget();
				tb.setText("");
			}
		}
	}
}

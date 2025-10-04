package com.pcc.sys.lib;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * 
 * @author by preecha.d 25/5/2561
 *
 */
public class Msg {

//	public static void confirm(String showMsg, String titleMsg, MyYesNoEventListener eventListener) {//แก้ไขเรื่อง disable-event-thread = true
//		Messagebox.show(
//				showMsg,
//				titleMsg,
//				new Messagebox.Button[] { Messagebox.Button.YES, Messagebox.Button.NO },
//				Messagebox.QUESTION,
//				eventListener);
//	}

	/**
	 * แก้ไขเรื่อง disable-event-thread = true
	 * @param showMsg
	 * @param titleMsg
	 * @param eventListener
	 */
	public static void confirm(String showMsg, String titleMsg, EventListener<Messagebox.ClickEvent> eventListener) {
		Messagebox.show(
				showMsg,
				titleMsg,
				new Messagebox.Button[] { Messagebox.Button.YES, Messagebox.Button.NO },
				Messagebox.QUESTION,
				eventListener);
	}

	/**
	 * แก้ไขเรื่อง disable-event-thread = true
	 * @param showMsg
	 * @param obj
	 * @param callNextMethodName
	 */
	public static void confirm2(String showMsg, Object obj, String callNextMethodName) {

		Messagebox.show(
				showMsg,
				"?",
				new Messagebox.Button[] { Messagebox.Button.YES, Messagebox.Button.NO },
				Messagebox.QUESTION,
				(event1) -> {
					if (Messagebox.Button.YES.equals(event1.getButton())) {
						if (obj != null && !Fnc.isEmpty(callNextMethodName)) {
							ZkUtil.callBackMethod(obj, callNextMethodName);
						}
					}
				});

	}

// แก้ไขเรื่อง disable-event-thread = true	
//	public static void confirm3(String showMsg, String titleMsg, MyEventListener eventListener) {
//		Messagebox.show(
//				showMsg,
//				titleMsg,
//				new Messagebox.Button[] { Messagebox.Button.YES, Messagebox.Button.NO },
//				Messagebox.QUESTION,
//				eventListener);
//	}

	public static void error(String message) {
		Messagebox.show(message, "Error", Messagebox.OK, Messagebox.ERROR);
	}

	public static void error(Exception ex) {
		error(ex, 400);
	}

	public static void error(Exception ex, int win_width) {

		try (java.io.InputStream inputStream = FSearchData.class.getResourceAsStream("/com/pcc/sys/lib/MsgWin.zul");
				java.io.InputStreamReader inpStrR = new java.io.InputStreamReader(inputStream, "UTF-8");) {

			MsgWin win1;
			win1 = (MsgWin) Executions.createComponentsDirectly(inpStrR, "zul", null, null);
			win1.setException(ex);
			if (win_width > 0) {
				win1.setWidth(win_width + "px");
			} else {
				win1.setWidth("400px");
			}
			win1.doModal();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void info(String message) {
		Messagebox.show(message, "Info", Messagebox.OK, Messagebox.INFORMATION);
	}

	public static void info2(String showMsg, Object obj, String callNextMethodName) {//แก้ไขเรื่อง disable-event-thread = true

		MyOkEventListener eventListener = new MyOkEventListener(obj, callNextMethodName) {
			public void onEvent(org.zkoss.zul.Messagebox.ClickEvent event1) throws Exception {
				if (this.getObj() != null && !Fnc.isEmpty(this.getCallNextMethodName())) {
					ZkUtil.callBackMethod(this.getObj(), this.getCallNextMethodName());
				}
			}
		};

		Messagebox.show(
				showMsg,
				"Info",
				new Messagebox.Button[] { Messagebox.Button.OK },
				Messagebox.INFORMATION,
				eventListener);

	}

	/**
	 * หน้าจอรับค่าข้อความ
	 * @param msg ข้อความที่แสดง
	 * @param win_width ความกว้างหน้าจอ windows
	 * @param maxlen ความกว้างข้อความที่รับ
	 * @param callForm หน้าจอที่เรียก
	 * @param methodName ต้องเป็น method ที่รับ String 1 พารามิเตอร์
	 * @param callBackObject object ที่ต้องการส่งกลับด้วยตอนเรียก methodName จะเป็น พารามิเตอร์ตัวที่ 2 เช่น Row object หรือ Event Object เป็นต้น
	 * @throws Exception 
	 */
	public static void inputbox(String msg, int win_width, int maxlen, Object callForm, String methodName, Object callBackObject) {
		try {
			final Window dialog = (Window) ZkUtil.callZulFile("/com/pcc/sys/zul/FrmInputBox.zul");

			if (win_width > 0) {
				dialog.setWidth(win_width + "px");
			}

			Label lblMsg = (Label) dialog.getFellow("lblMsg");
			lblMsg.setValue(msg);

			Textbox tb = (Textbox) dialog.getFellow("tb");
			if (maxlen > 0) {
				tb.setMaxlength(maxlen);
			}
			tb.addEventListener("onOK", (event) -> {
				
				Textbox tb1 = (Textbox) dialog.getFellow("tb");
				if (Fnc.isEmpty(tb1.getValue())) {
					Msg.error("ยังไม่ระบุข้อมูล");
				} else {
					if (callBackObject != null) {
						ZkUtil.callBackMethod(callForm, methodName, tb1.getValue(), callBackObject);
					} else {
						ZkUtil.callBackMethod(callForm, methodName, tb1.getValue());
					}
					dialog.detach();
				}
			});

			Button okBtn = (Button) dialog.getFellow("okBtn");
			okBtn.addEventListener("onClick", (event) -> {
				
				Textbox tb1 = (Textbox) dialog.getFellow("tb");
				if (Fnc.isEmpty(tb1.getValue())) {
					Msg.error("ยังไม่ระบุข้อมูล");
				} else {
					if (callBackObject != null) {
						ZkUtil.callBackMethod(callForm, methodName, tb1.getValue(), callBackObject);
					} else {
						ZkUtil.callBackMethod(callForm, methodName, tb1.getValue());
					}
					dialog.detach();
				}
				
			});

			Button cacelBtn = (Button) dialog.getFellow("cacelBtn");
			cacelBtn.addEventListener("onClick", (event) -> {
				dialog.detach();
			});

			dialog.doModal();
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	/**
	 * input box รับวันที่
	 * @param msg
	 * @param win_width ความกว้างของ window popup เป็น px
	 * @param textdatebox_width ความกว้างของ object ถ้าเป็น 0 จะใช้ค่า default
	 * @param callForm หน้าจอที่เรียก
	 * @param methodName ต้องเป็น method ที่รับ String 1 พารามิเตอร์
	 * @param callBackObject object ที่ต้องการส่งกลับด้วยตอนเรียก methodName จะเป็น พารามิเตอร์ตัวที่ 2 เช่น Row object หรือ Event Object เป็นต้น
	 * @return 
	 */
	public static void inputDatebox(String msg, int win_width, int textdatebox_width, Object callForm, String methodName, Object callBackObject) {
		try {
			final Window dialog = (Window) ZkUtil.callZulFile("/com/pcc/sys/zul/FrmInputDateBox.zul");

			if (win_width > 0) {
				dialog.setWidth(win_width + "px");
			}

			Label lblMsg = (Label) dialog.getFellow("lblMsg");
			lblMsg.setValue(msg);

			Datebox tb = (Datebox) dialog.getFellow("tb");
			if (textdatebox_width > 0) {
				tb.setWidth(textdatebox_width + "px");
			}
			tb.addEventListener("onOK", (event) -> {
				
				Datebox tb1 = (Datebox) dialog.getFellow("tb");
				if (tb1.getValue() == null) {
					Msg.error("ยังไม่ระบุข้อมูล");
				} else {
					if (callBackObject != null) {
						ZkUtil.callBackMethod(callForm, methodName, FnDate.getSqlDate(tb1.getValue()), callBackObject);
					} else {
						ZkUtil.callBackMethod(callForm, methodName, FnDate.getSqlDate(tb1.getValue()));
					}
					dialog.detach();
				}
				
			});

			Button okBtn = (Button) dialog.getFellow("okBtn");
			okBtn.addEventListener("onClick", (event) -> {
				
				Datebox tb1 = (Datebox) dialog.getFellow("tb");
				if (tb1.getValue() == null) {
					Msg.error("ยังไม่ระบุข้อมูล");
				} else {
					if (callBackObject != null) {
						ZkUtil.callBackMethod(callForm, methodName, FnDate.getSqlDate(tb1.getValue()), callBackObject);
					} else {
						ZkUtil.callBackMethod(callForm, methodName, FnDate.getSqlDate(tb1.getValue()));
					}
					dialog.detach();
				}
				
			});

			Button cacelBtn = (Button) dialog.getFellow("cacelBtn");
			cacelBtn.addEventListener("onClick", (event) -> {
				dialog.detach();
			});
			dialog.doModal();
		} catch (Exception e) {
			Msg.error(e);
		}

	}
	
	/**
	 * input box รับวันที่
	 * @param msg
	 * @param win_width ความกว้างของ window popup เป็น px ถ้าเป็น 0 จะใช้ค่า default
	 * @param textdatebox_width ความกว้างของ object ถ้าเป็น 0 จะใช้ค่า default
	 * @param callForm หน้าจอที่เรียก
	 * @param methodName ต้องเป็น method ที่รับ String 1 พารามิเตอร์
	 * @return 
	 */
	public static void inputDatebox2(String msg, int win_width, int textdatebox_width, Object callForm, String methodName) {
		try {
			final Window dialog = (Window) ZkUtil.callZulFile("/com/pcc/sys/zul/FrmInputDateBox.zul");

			if (win_width > 0) {
				dialog.setWidth(win_width + "px");
			}

			Label lblMsg = (Label) dialog.getFellow("lblMsg");
			lblMsg.setValue(msg);

			Datebox tb = (Datebox) dialog.getFellow("tb");
			if (textdatebox_width > 0) {
				tb.setWidth(textdatebox_width + "px");
			}
			tb.addEventListener("onOK", (event) -> {
				
				Datebox tb1 = (Datebox) dialog.getFellow("tb");
				if (tb1.getValue() == null) {
					Msg.error("ยังไม่ระบุข้อมูล");
				} else {
					ZkUtil.callBackMethod(callForm, methodName, FnDate.getSqlDate(tb1.getValue()));
					dialog.detach();
				}
				
			});

			Button okBtn = (Button) dialog.getFellow("okBtn");
			okBtn.addEventListener("onClick", (event) -> {
				
				Datebox tb1 = (Datebox) dialog.getFellow("tb");
				if (tb1.getValue() == null) {
					Msg.error("ยังไม่ระบุข้อมูล");
				} else {
					ZkUtil.callBackMethod(callForm, methodName, FnDate.getSqlDate(tb1.getValue()));
					dialog.detach();
				}

			});

			Button cacelBtn = (Button) dialog.getFellow("cacelBtn");
			cacelBtn.addEventListener("onClick", (event) -> {
				dialog.detach();
			});
			dialog.doModal();
		} catch (Exception e) {
			Msg.error(e);
		}

	}

}

package com.pcc.sys.lib;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Row;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ZkUtil {

	public static final String styleCenterWindow = "margin-left: auto;margin-right: auto;";
	public static final String styleFindLookUp = "height: 21px;padding: 0px;vertical-align: top;";
	public static final String flgOnChange = "flgOnChange";
	public static final String windowPopupWidth = "1200px";//ความขอหน้าจอที่ modal หรือ overlaps

	public static Class<?>[] getMethodParameterType(Object form, String methodName, Object... returnValue) {
		//== หาจำนวน method
		Method[] list_method = form.getClass().getMethods();
		for (Method method : list_method) {

			//System.out.println(" method.getName() :" + method.getName());
			if (method.getName().equals(methodName)) {

				//method.getParameterCount(); //จำนวน parameter

				//Parameter[] list_para = method.getParameters();
				//for (Parameter para : list_para) {
				//	System.out.println(" === para Name : " + para.getName());//จะชื่อลักษณะ arg0, arg1,....,argXXX
				//}

				//==ดูประเภทข้อมูลของพารามิเตอร์
				Class<?>[] list_parameterType = method.getParameterTypes();
				if (list_parameterType.length == returnValue.length) {
					int idx = 0;
					boolean sameAll = false;
					for (Class<?> para : list_parameterType) {
						//System.out.println(" === para.getTypeName() : " + para.getTypeName()); //int
						//System.out.println(" === returnValue[idx].getClass().getName() : " + returnValue[idx].getClass().getName()); //java.lang.Integer
						//int == java.lang.Integer
						//double = java.lang.Double
						//float  = java.lang.Float
						if (para.getTypeName().equals("int")
								&& returnValue[idx].getClass().getName().equals("java.lang.Integer")) {
							sameAll = true;
						} else if (para.getTypeName().equals("double")
								&& returnValue[idx].getClass().getName().equals("java.lang.Double")) {
							sameAll = true;
						} else if (para.getTypeName().equals("float")
								&& returnValue[idx].getClass().getName().equals("java.lang.Float")) {
							sameAll = true;
						} else if (para == returnValue[idx].getClass()) {
							sameAll = true;
						} else {
							sameAll = false;
							break;
						}
						idx++;
					}
					if (sameAll) { //ถ้าเหมือนกันทุกลำดับให้ return ค่าออกไป
						return list_parameterType;
					}
				}

			}

		}

		Class<?>[] cArg = {};
		return cArg;

	}

	public static boolean callBackMethod(Object form, String methodName) {

		//แก้ปัญหา performance แบบ set disable-event-thread = true
		//trick ที่มา https://www.zkoss.org/wiki/ZK_Configuration_Reference/zk.xml/The_system-config_Element/The_disable-event-thread_Element

		java.lang.reflect.Method method = null;
		try {
			if (form != null) {
				Class<?>[] cArg = {};
				method = form.getClass().getMethod(methodName, cArg);
				method.invoke(form);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			e.printStackTrace();
			return false;
		} finally {
			method = null;
		}
		return true;

	}

	public static boolean callBackMethod(Object form, String methodName, Object... returnValue) {

		//แก้ปัญหา performance แบบ set disable-event-thread = true
		//trick ที่มา https://www.zkoss.org/wiki/ZK_Configuration_Reference/zk.xml/The_system-config_Element/The_disable-event-thread_Element

		java.lang.reflect.Method method = null;
		try {
			if (form != null) {
				//Class[] cArg = { String.class };
				Class<?>[] cArg = getMethodParameterType(form, methodName, returnValue);
				if (cArg.length > 0) {
					method = form.getClass().getMethod(methodName, cArg);
					method.invoke(form, returnValue);
				} else {
					method = form.getClass().getMethod(methodName);
					method.invoke(form);
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			e.printStackTrace();
			return false;
		} finally {
			method = null;
		}
		return true;

	}

//	public static boolean callBackMethod(Object form, String methodName, String returnValue, Object callBackObject) {
//
//		//แก้ปัญหา performance แบบ set disable-event-thread = true
//		//trick ที่มา https://www.zkoss.org/wiki/ZK_Configuration_Reference/zk.xml/The_system-config_Element/The_disable-event-thread_Element
//
//		java.lang.reflect.Method method = null;
//		try {
//			if (form != null) {
//				if (callBackObject == null) {
//					Class[] cArg = { String.class };
//					method = form.getClass().getMethod(methodName, cArg);
//					method.invoke(form, returnValue);
//				} else {
//					Class[] cArg = { String.class, Object.class };
//					method = form.getClass().getMethod(methodName, cArg);
//					method.invoke(form, returnValue, callBackObject);
//				}
//			}
//		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
//			e.printStackTrace();
//			return false;
//		} finally {
//			method = null;
//		}
//		return true;
//
//	}

//	public static boolean callBackMethod(Object form, String methodName, java.sql.Date returnValue, Object callBackObject) {
//
//		//แก้ปัญหา performance แบบ set disable-event-thread = true
//		//trick ที่มา https://www.zkoss.org/wiki/ZK_Configuration_Reference/zk.xml/The_system-config_Element/The_disable-event-thread_Element
//
//		java.lang.reflect.Method method = null;
//		try {
//			if (form != null) {
//				if (callBackObject == null) {
//					Class[] cArg = { java.sql.Date.class };
//					method = form.getClass().getMethod(methodName, cArg);
//					method.invoke(form, returnValue);
//				} else {
//					Class[] cArg = { java.sql.Date.class, Object.class };
//					method = form.getClass().getMethod(methodName, cArg);
//					method.invoke(form, returnValue, callBackObject);
//				}
//			}
//		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
//			e.printStackTrace();
//			return false;
//		} finally {
//			method = null;
//		}
//		return true;
//
//	}

	public static boolean isPopup(String id) {

		//Component comp = Executions.getCurrent().getDesktop().getFirstPage().getFellowIfAny(id);
		//System.out.println("comp :" + comp);
		//if (comp != null) {
		//	return true;
		//} else {
		//	return false;
		//}
		return Executions.getCurrent().getDesktop().getFirstPage().hasFellow(id);

	}
	
	public static Window getWindows(String id) {

		//Component comp = Executions.getCurrent().getDesktop().getFirstPage().getFellowIfAny(id);
		//System.out.println("comp :" + comp);
		//if (comp != null) {
		//	return true;
		//} else {
		//	return false;
		//}
		return (Window) Executions.getCurrent().getDesktop().getFirstPage().getFellow(id);

	}

	public static org.zkoss.zul.Label gridLabel(String value) {
		String str = Fnc.getStr(value);
		org.zkoss.zul.Label lbl1 = new org.zkoss.zul.Label(str);
		return lbl1;
	}

	public static org.zkoss.zul.Label gridLabel(String value, String style) {
		String str = Fnc.getStr(value);
		org.zkoss.zul.Label lbl1 = new org.zkoss.zul.Label(str);
		if (!Fnc.isEmpty(style)) {
			lbl1.setStyle(style);
		}
		return lbl1;
	}

	/**
	 * สร้าง text box สำหรับ grid แสดงแบบซ้ายมือ
	 * @param value
	 * @param parentGrid
	 * @param column
	 * @return
	 */
	public static org.zkoss.zul.Textbox gridTextbox(String value) {
		org.zkoss.zul.Textbox comp = new org.zkoss.zul.Textbox();
		comp.setWidth("98%"); //ของความกว้าง cell
		comp.setStyle("border-width: 0px;background-color: transparent;");
		comp.setTabindex(-1);
		comp.setReadonly(true);
		comp.setValue(value);

		return comp;
	}

	/**
	 * สร้าง text box สำหรับ grid สำหรับแก้ไขได้
	 * @param value ค่าที่ต้องการให้แสดง
	 * @param maxlength จำนวนตัวอักษรที่ key ได้ ,ถ้าระบุเป็น 0 คือไม่จำกัด
	 * @return
	 */
	public static org.zkoss.zul.Textbox gridTextboxEdit(String value, int maxlength) {
		org.zkoss.zul.Textbox comp = new org.zkoss.zul.Textbox();
		comp.setWidth("98%"); //ของความกว้าง cell
		comp.setValue(value);
		if (maxlength > 0) {
			comp.setMaxlength(maxlength);
		}
		return comp;
	}

	/**
	 * สร้าง text box สำหรับ grid แสดงแบบตรงกลาง
	 * @param value
	 * @param parentGrid
	 * @param column
	 * @return
	 */
	public static org.zkoss.zul.Textbox gridTextboxCenter(String value) {
		org.zkoss.zul.Textbox comp = new org.zkoss.zul.Textbox();
		comp.setWidth("98%"); //ของความกว้าง cell
		comp.setStyle("border-width: 0px;background-color: transparent;text-align: center;");
		comp.setReadonly(true);
		comp.setTabindex(-1);
		comp.setValue(value);

		return comp;
	}

	/**
	 * สร้าง text box สำหรับ grid แสดงแบบขวามือ
	 * @param value
	 * @param parentGrid
	 * @param column
	 * @return
	 */
	public static org.zkoss.zul.Textbox gridTextboxRight(String value) {
		org.zkoss.zul.Textbox comp = new org.zkoss.zul.Textbox();
		comp.setWidth("98%"); //ของความกว้าง cell
		comp.setStyle("border-width: 0px;background-color: transparent;text-align: right;");
		comp.setReadonly(true);
		comp.setTabindex(-1);
		comp.setValue(value);

		return comp;
	}

	/**
	 * สร้าง intbox สำหรับ grid
	 * @param value
	 * @param parentGrid
	 * @param column
	 * @return
	 */
	public static org.zkoss.zul.Intbox gridIntbox(Integer value) {
		org.zkoss.zul.Intbox comp = new org.zkoss.zul.Intbox();
		comp.setWidth("98%"); //ของความกว้าง cell
		comp.setStyle("border-width: 0px;background-color: transparent;text-align: right;");
		comp.setReadonly(true);
		comp.setTabindex(-1);
		comp.setValue(value);
		return comp;
	}

	public static Datebox gridDateboxReadOnly(java.sql.Date value) {
		Datebox comp = new Datebox();
		comp.setWidth("98%"); //ของความกว้าง cell
		comp.setReadonly(true);
		comp.setTabindex(-1);
		comp.setValue(value);
		comp.setFormat("dd/MM/yyyy");
		if (FnDate.useThaiDate) {
			comp.setLocale("th_TH");
		}
		return comp;
	}

	public static Datebox gridDateboxEdit(java.sql.Date value) {
		Datebox comp = new Datebox();
		comp.setWidth("98%"); //ของความกว้าง cell
		comp.setValue(value);
		comp.setFormat("dd/MM/yyyy");
		if (FnDate.useThaiDate) {
			comp.setLocale("th_TH");
		}
		return comp;
	}

	/**
	 * สร้าง MyDecimalbox2 สำหรับ grid
	 * @param value
	 * @param parentGrid
	 * @param column
	 * @return
	 */
	public static MyDecimalbox gridDecimalbox(BigDecimal value) {
		MyDecimalbox comp = new MyDecimalbox();
		comp.setWidth("98%"); //ของความกว้าง cell
		comp.setStyle("border-width: 0px;background-color: transparent;text-align: right;");
		comp.setReadonly(true);
		comp.setTabindex(-1);
		comp.setValue(value);
		return comp;
	}

	public static MyDecimalbox gridDecimalboxEdit(BigDecimal value) {
		MyDecimalbox comp = new MyDecimalbox();
		comp.setWidth("98%"); //ของความกว้าง cell
		comp.setValue(value);
		return comp;
	}

	/**
	 * setGridHeaderStyle
	 * @param grid
	 */
	public static void setGridHeaderStyle(org.zkoss.zul.Grid grid) {
		Column col = null;
		Integer gridColCount = grid.getColumns().getChildren().size();
		for (int n1 = 0; n1 < gridColCount; n1++) {
			col = (Column) grid.getColumns().getChildren().get(n1);
			col.setStyle("text-align: center;");
		}
	}

	/**
	 * setListBoxHeaderStyle
	 * @param lsbox
	 */
	public static void setListBoxHeaderStyle(org.zkoss.zul.Listbox lsbox) {
		Listheader lh = null;
		Integer lsbColCount = lsbox.getListhead().getChildren().size();
		for (int n1 = 0; n1 < lsbColCount; n1++) {
			lh = (Listheader) lsbox.getListhead().getChildren().get(n1);
			lh.setStyle("text-align: center;");
		}
	}

	public static String getLabelValue(Event event, Integer index) {
		Label comp = (Label) event.getTarget().getParent().getChildren().get(index);
		return Fnc.getStr(comp.getValue());
	}

	public static String getTextboxValue(Event event, Integer index) {
		Textbox comp = (Textbox) event.getTarget().getParent().getChildren().get(index);
		return Fnc.getStr(comp.getValue());
	}

	public static java.sql.Date getDateboxValue(Event event, Integer index) {
		Datebox comp = (Datebox) event.getTarget().getParent().getChildren().get(index);
		return FnDate.getSqlDate(comp.getValue());
	}

	public static Integer getIntboxValue(Event event, Integer index) {
		Intbox comp = (Intbox) event.getTarget().getParent().getChildren().get(index);
		return comp.getValue();
	}

	public static BigDecimal getDecimalboxValue(Event event, Integer index) {
		MyDecimalbox comp = (MyDecimalbox) event.getTarget().getParent().getChildren().get(index);
		return comp.getValue();
	}

//	/**
//	 * เปลี่ยนสีเวลา Focus และ Lost Focus
//	 * @param btn
//	 */
//	public static void addFocusAndBlurEventToButton(Button btn) {
//		btn.addEventListener(Events.ON_FOCUS, new EventListener<Event>() {
//			@Override
//			public void onEvent(Event evt) throws Exception {
//				((Button) evt.getTarget()).setStyle("background-color: #6600FF;color: #FFFFFF;");
//			}
//		});
//		btn.addEventListener(Events.ON_BLUR, new EventListener<Event>() {
//			@Override
//			public void onEvent(Event evt) throws Exception {
//				((Button) evt.getTarget()).setStyle("background-color: #DDDDDD;color: #000000;");
//			}
//		});
//	}
//
//	/**
//	 * เปลี่ยนสีเวลา Focus และ Lost Focus (สำหรับปุ่มลบหรือยกเลิก)
//	 * @param btn
//	 * @param isDeleteButton  (เป็นปุ่มลบหรือยกเลิก)
//	 */
//	public static void addFocusAndBlurEventToButton(Button btn, boolean isDeleteButton) {
//		btn.addEventListener(Events.ON_FOCUS, new EventListener<Event>() {
//			@Override
//			public void onEvent(Event evt) throws Exception {
//				((Button) evt.getTarget()).setStyle("background-color: #6600FF;color: #FFFFFF;");
//			}
//		});
//		btn.addEventListener(Events.ON_BLUR, new EventListener<Event>() {
//			@Override
//			public void onEvent(Event evt) throws Exception {
//				((Button) evt.getTarget()).setStyle("background-color: #DDDDDD;color: red;");
//			}
//		});
//	}

	/**
	 * กำหนดการแสดงค่าตาม item value ของ combobox
	 * @param cmb
	 * @param value 
	 */
	public static void setSelectItemComboBoxByValue(org.zkoss.zul.Combobox cmb, String value) {
		int itemCount = cmb.getItemCount();
		if (!Fnc.isEmpty(value)) {
			for (int n1 = 0; n1 < itemCount; n1++) {
				if (cmb.getItemAtIndex(n1).getValue().toString().equals(value)) {
					cmb.setSelectedIndex(n1);
					break;
				}
			}
		} else if (itemCount > 0) {
			cmb.setSelectedIndex(0);
		} else {
			cmb.setSelectedIndex(-1);
		}
	}

	/**
	 * ดึงค่าจาก value จาก comboitem ใน Combobox
	 * @param cmb
	 * @return
	 */
	public static String getSelectItemValueComboBox(org.zkoss.zul.Combobox cmb) {
		String res = "";
		String sDisplayValue = cmb.getValue();
		int itemcount = cmb.getItemCount();
		if (itemcount > 0) {
			for (int n1 = 0; n1 < itemcount; n1++) {
				String sLabel = cmb.getItemAtIndex(n1).getLabel();
				if (sLabel.equals(sDisplayValue)) {
					res = cmb.getItemAtIndex(n1).getValue();
				}
			}
		}
		return res;
	}

	/**
	 * เพิ่ม Item เข้า Combobox
	 * @param combobox
	 * @param value
	 * @param label
	 */
	public static void appendItemToComboBox(org.zkoss.zul.Combobox combobox, String value, String label) {
		Comboitem citem = new Comboitem();
		citem.setValue(value);
		citem.setLabel(label);
		combobox.appendChild(citem);
	}

	/**
	 * เพิ่ม Item เข้า Combobox
	 * @param combobox
	 * @param data 
	 */
	public static void appendItemToComboBox(org.zkoss.zul.Combobox combobox, String[][] data) {
		for (String[] str : data) {
			String value = str[0].toString();
			String label = str[1].toString();
			appendItemToComboBox(combobox, value, label);
		}
	}

	/**
	 * Set Disable TabIndex
	 * @param index
	 * @param tabbox
	 * @param disable
	 */
	public static void setDisableTabIndex(int index, org.zkoss.zul.Tabbox tabbox, boolean disable) {
		Tab tb = (Tab) tabbox.getTabs().getChildren().get(index);
		tb.setDisabled(disable);
	}

	/**
	 * setVisibleTabIndex
	 * @param index
	 * @param tabbox
	 * @param visible
	 */
	public static void setVisibleTabIndex(int index, org.zkoss.zul.Tabbox tabbox, boolean visible) {
		Tab tb = (Tab) tabbox.getTabs().getChildren().get(index);
		if (tb != null) {
			tb.setVisible(visible);
		}
	}

	/**
	 * ซ่อน Tab
	 * @param tabpanel
	 * @param tabbox
	 * @param visible
	 */
	public static void setVisibleTabpanel(org.zkoss.zul.Tabpanel tabpanel, boolean visible) {
		int index = tabpanel.getIndex();
		Tab tb = (Tab) tabpanel.getTabbox().getTabs().getChildren().get(index);
		if (tb != null) {
			tb.setVisible(visible);
		}
	}

	/**
	 * ดึง Tabpanel ในลำดับที่ index 
	 * @param tabbox
	 * @param index
	 * @return
	 */
	public static org.zkoss.zul.Tabpanel getTabpanelIndex(org.zkoss.zul.Tabbox tabbox, int index) {
		org.zkoss.zul.Tabpanel tabpanel = (org.zkoss.zul.Tabpanel) tabbox.getTabpanels().getChildren().get(index);
		return tabpanel;
	}

	public static Button ListCellButton(String label, Listitem item, org.zkoss.zk.ui.event.EventListener<?> on_click_ev,
			String sclass) {
		Listcell cell1 = new Listcell();
		Button btn1 = new Button();
		btn1.setLabel(label);
		btn1.setTabindex(-1);
		btn1.setAutodisable("self");
		btn1.setSclass(sclass);
		if (on_click_ev != null) {
			btn1.addEventListener(Events.ON_CLICK, on_click_ev);
		}
		cell1.appendChild(btn1);
		cell1.setParent(item);
		return btn1;
	}

	public static Image ListCellImageSel(String label, Listitem item,
			org.zkoss.zk.ui.event.EventListener<?> on_click_ev) {
		Listcell cell1 = new Listcell();
		Image img1 = new Image();
		img1.setSrc("/img/Select-icon.png");
		img1.setTooltiptext("Click to select.");
		img1.setHeight("50%");
		img1.setWidth("50%");
		if (on_click_ev != null) {
			img1.addEventListener(Events.ON_CLICK, on_click_ev);
		}
		cell1.appendChild(img1);
		cell1.setParent(item);
		return img1;
	}

	public static Button GridButton(String label, Row row, org.zkoss.zk.ui.event.EventListener<?> on_click_ev,
			String sclass) {
		Button btn1 = new Button();
		btn1.setLabel(label);
		btn1.setTabindex(-1);
		btn1.setAutodisable("self");
		btn1.setSclass(sclass);
		if (on_click_ev != null) {
			btn1.addEventListener(Events.ON_CLICK, on_click_ev);
		}
		return btn1;
	}

	public static void onCtrlKey(KeyEvent evt, Listbox listBox1) {

		if (listBox1.getItemCount() > 0) {

			int keyCode = evt.getKeyCode();

			if (keyCode == KeyEvent.UP) {
				int se = listBox1.getSelectedIndex();
				if (se > 0) {
					listBox1.setSelectedIndex(se - 1);
				}
			} else if (keyCode == KeyEvent.DOWN) {
				int se = listBox1.getSelectedIndex();
				if (se + 1 < listBox1.getItemCount()) {
					listBox1.setSelectedIndex(se + 1);
				}
			}

		}

	}

	public static Component callZulFile(String path) throws Exception {
		//System.out.println("zul file path : " + path);
		InputStream inputStream = FCallMenu.class.getResourceAsStream(path);
		if (inputStream == null) {
			throw new Exception("เมนูนี้ยังพัฒนาไม่เสร็จ");
		}
		return Executions.createComponentsDirectly(new java.io.InputStreamReader(inputStream, "UTF-8"), "zul", null,
				null);
	}

	public static boolean doOnChange(HtmlBasedComponent component) {
		if (component.getAttribute(flgOnChange) == null) {
			//System.out.println("doOnChange==false");
			return false;
		} else {
			java.sql.Timestamp time1 = (java.sql.Timestamp) component.getAttribute(flgOnChange);
			if (FnDate.getDayDiffInMilliseconds(time1, FnDate.getTodaySqlDateTime()) > 1000) { //มากกว่า 1 วินาทีให้ทำใหม่
				return false;
			} else {
				return true;
			}
		}
	}

	public static void setAndClearOnChang(HtmlBasedComponent comp1,
			java.util.List<HtmlBasedComponent> controlComponentList) {

		comp1.setAttribute(flgOnChange, FnDate.getTodaySqlDateTime());

		for (Component comp2 : controlComponentList) {
			if (comp2 != comp1) {
				comp2.setAttribute(flgOnChange, null);
			}
		}

	}
	
	public static void saveFile(byte[] data, String format, String fileName) {
		Filedownload.save(data, "application/octet-stream",
				fileName + "-" + FnDate.getTodayDateTime("yyMMdd-HHmmss") + "." + format);
	}

	/**
	 * Upload
	 * @param numberOfFile จำนวนไฟล์ที่รับ
	 * @param limitSizeMb ขนาดไฟล์สูงสุด (MB)
	 * @param listener
	 * @throws Exception
	 */
	public static void upLoadFile(int numberOfFile, int limitSizeMb, EventListener<UploadEvent> listener) throws Exception {
		
		System.out.println("upLoadFile...");
		
		// 1024 byte = 1 kb , 1024kb = 1 MB
		int sizeByte = 1024 * 1024 * limitSizeMb;// size in byte
		int sizeKb = sizeByte / 1024;// size in kb

		Map<String, Object> params = new HashMap<String, Object>();

		// params.put("message", message == null ? Messages.get(MZul.UPLOAD_MESSAGE) :
		// message);
		// params.put("title", title == null ? Messages.get(MZul.UPLOAD_TITLE) : title);
		// params.put("max", new Integer(max == 0 ? 1 : max > 1000 ? 1000 : max < -1000 ? -1000 : max));
		// params.put("accept", accept);
		// params.put("native", Boolean.valueOf(alwaysNative));
		// params.put("maxsize", String.valueOf(maxsize));
		// params.put("listener", listener);
        //<button label="Upload" upload="true,maxsize=-1,multiple=true,accept=audio/*|video/*|image/*,native" />
		Fileupload.get(params, "เลือกภาพ(ไม่เกิน " + limitSizeMb + " MB)", "เลือกไฟล์", numberOfFile, sizeKb, false, listener);

	}

}

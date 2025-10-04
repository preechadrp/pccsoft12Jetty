package com.pcc.sys.find;

import java.util.ArrayList;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;

import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FWinUtil;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.ZkUtil;
import com.pcc.sys.tbm.TbmFMENU_GROUP_H;

public class SeUserGroup extends FWinUtil {

	private static final long serialVersionUID = 1L;
	private Textbox txtUSER_MENU_GROUP, txtTHAI_NAME;
	private Listbox listBox1;
	private java.util.List<FModelHasMap> dats = new ArrayList<FModelHasMap>();;
	private Object callForm;
	private String methodName;

	private String user_menu_group = "";

	@Override
	public void setFormObj() {
		txtTHAI_NAME = (Textbox) getFellow("txtTHAI_NAME");
		txtUSER_MENU_GROUP = (Textbox) getFellow("txtUSER_MENU_GROUP");
		listBox1 = (Listbox) getFellow("listBox1");
	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(txtTHAI_NAME);
		addEnterIndex(txtUSER_MENU_GROUP);
	}

	@Override
	public void formInit() {
		this.setPosition("center,top");
		ZkUtil.setListBoxHeaderStyle(listBox1);
		this.listBox1.setItemRenderer(this.getListitemRenderer1());
		if (!user_menu_group.equals("")) {
			txtUSER_MENU_GROUP.setValue(user_menu_group);
		}
		startSearch();
	}

	public void onOK() {
		if (focusObj == txtUSER_MENU_GROUP ||
				focusObj == txtTHAI_NAME) {
			startSearch();
		}
	}

	/**
	 * เริ่มค้นหา
	 */
	public void startSearch() {

		try {

			listBox1.getItems().clear();
			TbmFMENU_GROUP_H.getData(dats, txtUSER_MENU_GROUP.getValue(), txtTHAI_NAME.getValue());
			SimpleListModel rstModel = new SimpleListModel(dats);
			listBox1.setModel(rstModel);
			if (rstModel.getSize() > 0) {
				listBox1.setSelectedIndex(0);
				listBox1.focus();
			}

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	private void doOnDbClickRow(Event event) {
		String sUSER_MENU_GROUP = "";
		if (event == null) {
			Listitem itemL = listBox1.getSelectedItem();
			if (itemL != null) {
				sUSER_MENU_GROUP = Fnc.getStr(itemL.getAttribute("USER_MENU_GROUP"));
			}
		} else if (event.getTarget() instanceof Listitem) {
			Listitem itemL = (Listitem) event.getTarget();
			sUSER_MENU_GROUP = Fnc.getStr(itemL.getAttribute("USER_MENU_GROUP"));
		}

		if (!Fnc.isEmpty(sUSER_MENU_GROUP)) {
			System.out.println(sUSER_MENU_GROUP);
			this.setUser_menu_group(sUSER_MENU_GROUP);
			ZkUtil.callBackMethod(this.getCallForm(), this.getMethodName(), sUSER_MENU_GROUP);
			this.dats.clear();
			this.dats = null;
			super.onClose();
		}
	}

	public void onClick_btnOK() {
		doOnDbClickRow(null);
	}

	public Object getCallForm() {
		return callForm;
	}

	public void setCallForm(Object callForm) {
		this.callForm = callForm;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getUser_menu_group() {
		if (!this.user_menu_group.equals("")) {
			return this.user_menu_group;
		} else {
			return "";
		}
	}

	public void setUser_menu_group(String user_menu_group) {
		this.user_menu_group = user_menu_group;
	}

	private ListitemRenderer getListitemRenderer1() {

		return (item, data, index) -> {

			FModelHasMap rs = (FModelHasMap) data;

			//== Event
			item.addEventListener(Events.ON_DOUBLE_CLICK, (event) -> doOnDbClickRow(event));
			item.addEventListener(Events.ON_OK, (event) -> doOnDbClickRow(event));

			//=== เพิ่ม Attribute
			item.setAttribute("USER_MENU_GROUP", rs.getString("USER_MENU_GROUP"));

			//== display data
			new Listcell((index + 1) + "").setParent(item);
			new Listcell(rs.getString("USER_MENU_GROUP")).setParent(item);
			new Listcell(rs.getString("THAI_NAME")).setParent(item);

		};

	}

}

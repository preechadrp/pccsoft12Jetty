package com.pcc.sys.find;

import java.util.ArrayList;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;

import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FWinUtil;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.ZkUtil;
import com.pcc.sys.tbm.TbmFUSER;

public class SeUsername2 extends FWinUtil {

	private static final long serialVersionUID = 1L;
	private Textbox txtUSER_ID, txtFIRSTNAME, txtLASTNAME;
	private Listbox listBox1;
	private java.util.List<FModelHasMap> dats = new ArrayList<FModelHasMap>();
	private Object callForm;
	private String methodName;

	private String user_id = "";

	@Override
	public void setFormObj() {
		txtFIRSTNAME = (Textbox) getFellow("txtFIRSTNAME");
		txtLASTNAME = (Textbox) getFellow("txtLASTNAME");
		txtUSER_ID = (Textbox) getFellow("txtUSER_ID");
		listBox1 = (Listbox) getFellow("listBox1");
	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(txtFIRSTNAME);
		addEnterIndex(txtLASTNAME);
		addEnterIndex(txtUSER_ID);
	}

	@Override
	public void formInit() {
		this.setPosition("center,top");
		ZkUtil.setListBoxHeaderStyle(listBox1);
		this.listBox1.setItemRenderer(

				(item, data, index) -> {

					FModelHasMap rs = (FModelHasMap) data;

					//== Event
					item.addEventListener(Events.ON_DOUBLE_CLICK, (event) -> doOnDbClickRow(event));
					item.addEventListener(Events.ON_OK, (event) -> doOnDbClickRow(event));

					//=== เพิ่ม Attribute
					item.setAttribute("USER_ID", rs.getString("USER_ID"));

					//== display data
					new Listcell((index + 1) + "").setParent(item);
					new Listcell(rs.getString("USER_ID")).setParent(item);
					String userName = rs.getString("TITLE") + " " + rs.getString("FNAME_THAI") + " "
							+ rs.getString("LNAME_THAI");
					new Listcell(userName.trim()).setParent(item);
					new Listcell(rs.getString("MAN_USER").equals("1") ? "YES" : "NO").setParent(item);
					new Listcell(rs.getString("MAN_MENU_GROUP").equals("1") ? "YES" : "NO").setParent(item);
					new Listcell(rs.getString("USER_STATUS").equals("1") ? "เปิดการใช้งาน" : "ปิดการใช้งาน")
							.setParent(item);
					new Listcell(rs.getString("INSBY")).setParent(item);
					new Listcell(FnDate.displayDateTimeString(rs.getTimestamp("INSDT"))).setParent(item);
					new Listcell(rs.getString("UPBY")).setParent(item);
					new Listcell(FnDate.displayDateTimeString(rs.getTimestamp("UPDT"))).setParent(item);

				});
		if (!user_id.equals("")) {
			txtUSER_ID.setValue(user_id);
		}
		startSearch();
	}

	public void onOK() {
		if (focusObj == txtUSER_ID ||
				focusObj == txtFIRSTNAME ||
				focusObj == txtLASTNAME) {
			startSearch();
		}
	}

	/**
	 * เริ่มค้นหา
	 */
	public void startSearch() {

		try {

			listBox1.getItems().clear();
			TbmFUSER.getData(dats, txtUSER_ID.getValue(), txtFIRSTNAME.getValue(), txtLASTNAME.getValue());
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
		String sUSER_ID = "";
		if (event == null) {
			Listitem itemL = listBox1.getSelectedItem();
			if (itemL != null) {
				sUSER_ID = Fnc.getStr(itemL.getAttribute("USER_ID"));
			}
		} else if (event.getTarget() instanceof Listitem) {
			Listitem itemL = (Listitem) event.getTarget();
			sUSER_ID = Fnc.getStr(itemL.getAttribute("USER_ID"));
		}

		if (!Fnc.isEmpty(sUSER_ID)) {
			System.out.println(sUSER_ID);
			this.setUser_Id(sUSER_ID);
			ZkUtil.callBackMethod(this.getCallForm(), this.getMethodName(), sUSER_ID);

			//== Standard Command
			dats.clear();
			dats = null;
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

	public String getUser_Id() {
		if (!this.user_id.equals("")) {
			return this.user_id;
		} else {
			return "";
		}
	}

	public void setUser_Id(String user_id) {
		this.user_id = user_id;
	}

}

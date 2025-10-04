package com.pcc.sys.find;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.SimpleListModel;

import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FWinUtil;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.ZkUtil;
import com.pcc.sys.tbm.TbmFCOMP;
import com.pcc.sys.tbo.TboFCOMP;

public class SeUserCompList extends FWinUtil {

	private static final long serialVersionUID = 1L;
	private Listbox listBox1;
	private java.util.List<FModelHasMap> dats = new ArrayList<FModelHasMap>();;
	private Object callForm;
	private String methodName;

	private java.util.List<TboFCOMP> not_in_fcomp_table_lst = null;
	private java.util.List<String> fcomp_lst = new ArrayList<String>();

	@Override
	public void setFormObj() {
		listBox1 = (Listbox) getFellow("listBox1");
	}

	@Override
	public void addEnterIndex() {
	}

	@Override
	public void formInit() {
		this.setPosition("center,top");

		ZkUtil.setListBoxHeaderStyle(listBox1);
		this.listBox1.setItemRenderer(
				new ListitemRenderer<Object>() {
					public void render(Listitem item, Object data, int index) throws Exception {

						FModelHasMap rs = (FModelHasMap) data;

						//=== เพิ่ม Attribute
						item.setAttribute("COMP_CDE", rs.getString("COMP_CDE"));

						//== display data
						new Listcell("").setParent(item);
						new Listcell(rs.getString("COMP_CDE")).setParent(item);
						new Listcell(rs.getString("COMP_NAME")).setParent(item);

					}
				});

		if (not_in_fcomp_table_lst == null) {
			not_in_fcomp_table_lst = new ArrayList<>();
		}
		startSearch();
	}

	public void onOK() {
	}

	/**
	 * เริ่มค้นหา
	 */
	public void startSearch() {

		try {

			TbmFCOMP.getDataNotIn(dats, this.not_in_fcomp_table_lst);
			SimpleListModel rstModel = new SimpleListModel(dats);
			rstModel.setMultiple(true);//ต้อง set muti ที่ model แทนไม่งั้น checkbox แบบ muti จะหาย
			listBox1.setModel(rstModel);
			listBox1.renderAll();//สั่งให้ render object ทุกแถวโดยไม่ต้องรอ click next ไปที่หน้านั้น

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void onClick_OK() {

		try {
			Set<Listitem> list = listBox1.getSelectedItems();//รายการที่เลือก
			for (Listitem listitem : list) {
				this.fcomp_lst.add(Fnc.getStr(listitem.getAttribute("COMP_CDE")));
			}

			java.lang.reflect.Method method3 = null;
			try {
				Class[] cArg = { java.util.List.class };
				method3 = this.getCallForm().getClass().getMethod(this.getMethodName(), cArg);
				method3.invoke(this.getCallForm(), this.fcomp_lst);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			} finally {
				method3 = null;
			}

			//== Standard Command
			dats.clear();
			dats = null;
			super.onClose();

		} catch (Exception e) {
			Msg.error(e);
		}

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

	public java.util.List<TboFCOMP> getNot_in_fcomp_table_lst() {
		return not_in_fcomp_table_lst;
	}

	public void setNot_in_fcomp_table_lst(java.util.List<TboFCOMP> not_in_fcomp_table_lst) {
		this.not_in_fcomp_table_lst = not_in_fcomp_table_lst;
	}

	public java.util.List<String> getFcomp_lst() {
		return fcomp_lst;
	}

	public void setFcomp_lst(java.util.List<String> fcomp_lst) {
		this.fcomp_lst = fcomp_lst;
	}

}

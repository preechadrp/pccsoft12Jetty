package com.pcc.sys.lib;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;

public class FSearchPopup extends FWinUtil {

	private static final long serialVersionUID = 1L;
	public boolean isSel = false;
	public List<HtmlBasedComponent> componentWhereList = new ArrayList<HtmlBasedComponent>();
	public FSearchData fSearchData = null;
	public Div divSearch;
	public Listbox listBox1;

	private java.util.List<FModelHasMap> dataSet = new ArrayList<FModelHasMap>();

	@Override
	public void setFormObj() {
		divSearch = (Div) getFellow("divSearch");
		listBox1 = (Listbox) getFellow("listBox1");

	}

	@Override
	public void addEnterIndex() {
	}

	@Override
	public void formInit() {

		divSearch.setSclass("mainGridLayout2");
		divSearch.setStyle("grid-template-columns: auto auto auto auto;");

		//== สร้าง input ที่ใช้เป็นเงื่อนไขในการค้นหา
		for (HtmlBasedComponent cmp : this.fSearchData.getWhereObject()) {

			String tooltipText = "";
			String label_desc = FSearchData.getStr(cmp.getAttribute("WHERE_LABEL"));
			String operator = FSearchData.getStr(cmp.getAttribute("WHERE_OPERATOR"));
			if (operator.toLowerCase().equals("like1")) {
				label_desc += " (*ab*)";
				tooltipText = "กรอกให้ตรงส่วนใดส่วนหนึ่งของข้อมูลที่ต้องการค้นหา";
			} else if (operator.toLowerCase().equals("like2")) {
				label_desc += " (ab*)";
				tooltipText = "กรอกให้ตรงตัวหน้าของข้อมูลที่ต้องการค้นหา";
			} else if (operator.toLowerCase().equals("like3")) {
				label_desc += " (*ab)";
				tooltipText = "กรอกให้ตรงตัวหลังของข้อมูลที่ต้องการค้นหา";
			} else if (operator.toLowerCase().equals("=")) {
				label_desc += " (=)";
			} else if (operator.toLowerCase().equals("<>")) {
				label_desc += " (<>)";
			} else if (operator.toLowerCase().equals("!=")) {
				label_desc += " (<>)";
			} else if (operator.toLowerCase().equals(">")) {
				label_desc += " (>)";
			} else if (operator.toLowerCase().equals(">=")) {
				label_desc += " (>=)";
			} else if (operator.toLowerCase().equals("<")) {
				label_desc += " (<)";
			} else if (operator.toLowerCase().equals("<=")) {
				label_desc += " (<=)";
			}

			Label lblDesc = new Label(label_desc);
			divSearch.appendChild(lblDesc);

			divSearch.appendChild(cmp);
			if (!tooltipText.equals("")) {
				cmp.setTooltiptext(tooltipText);
			}
			this.addEnterIndex(cmp);
			this.componentWhereList.add(cmp);

		}

		//=== สร้างคอลัมภ์ที่ใช้แสดงใน grid
		if (this.fSearchData.isShowCheckbox()) {
			listBox1.setCheckmark(true);
			Listheader itH1 = new Listheader("");
			itH1.setWidth("40px");
			itH1.setAlign("center");
			listBox1.getListhead().appendChild(itH1);
		}
		Listheader itH = new Listheader("ลำดับ");
		itH.setWidth("60px");
		itH.setAlign("center");
		listBox1.getListhead().appendChild(itH);

		for (HashMap col1 : this.fSearchData.getShowColum()) {

			String title = FSearchData.getStr(col1.get("FTitle"));
			Listheader itH2 = new Listheader(title);
			listBox1.getListhead().appendChild(itH2);

			String showColumFieldType = FSearchData.getStr(col1.get("FFieldType"));
			if (showColumFieldType.equals("STRING")) {
				itH2.setAlign("left");
			} else if (showColumFieldType.equals("DATE")) {
				itH2.setAlign("center");
			} else if (showColumFieldType.equals("DATETIME")) {
				itH2.setAlign("center");
			} else if (showColumFieldType.equals("NUMBER")) {
				itH2.setAlign("right");
			} else if (showColumFieldType.equals("INTEGER")) {
				itH2.setAlign("right");
			} else {
				itH2.setAlign("left");
			}

			String shortColumName = FSearchData.getStr(col1.get("FSortAbleColumn"));
			if (!FSearchData.isEmpty(shortColumName)) {
				shortColumName = getFieldName(shortColumName);
				itH2.setSort("auto(" + shortColumName + ")");
			}

			String showColumWidth = FSearchData.getStr(col1.get("FColumWidth"));
			if (!FSearchData.isEmpty(showColumWidth)) {
				itH2.setWidth(showColumWidth);
			}

		}

		listBox1.setItemRenderer(this.getListitemRenderer1());
		ZkUtil.setListBoxHeaderStyle(listBox1);

		if (fSearchData.isInitShow()) {
			searchData();
		}

	}

	public void onOK() {

		try {

			if (isSearchComponent(focusObj)) {
				System.out.println("ON OK");
				searchData();
			}

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public boolean isSearchComponent(Component component) {
		for (HtmlBasedComponent comp1 : componentWhereList) {
			if (component == comp1) {
				return true;
			}
		}
		return false;
	}

	private void searchData() {

		try {

			listBox1.getItems().clear();

			//== สร้าง sql
			SqlStr sql = new SqlStr();
			sql.addLine(fSearchData.getSelectSql());
			sql.addLine(" where 1=1 ");
			if (fSearchData.getWhereFix().size() > 0) {
				for (String fixw : fSearchData.getWhereFix()) {
					sql.addLine(" and " + fixw + " ");
				}
			}
			for (HtmlBasedComponent comp1 : componentWhereList) { //loop ค่าที่จะค้นหาใน input box

				if (comp1.getId().startsWith("int")) {
					Integer value = ((Intbox) comp1).getValue();
					if (value != null) {
						String where_field = FSearchData.getStr(comp1.getAttribute("WHERE_FIELD"));
						String operator = FSearchData.getStr(comp1.getAttribute("WHERE_OPERATOR"));
						sql.addLine(" and " + where_field + " " + operator + " " + value + " ");
					}

				} else if (comp1.getId().startsWith("dec")) {
					BigDecimal value = ((MyDecimalbox) comp1).getValue();
					if (value != null) {
						String where_field = FSearchData.getStr(comp1.getAttribute("WHERE_FIELD"));
						String operator = FSearchData.getStr(comp1.getAttribute("WHERE_OPERATOR"));
						sql.addLine(" and " + where_field + " " + operator + " " + FSearchData.getStrBigDecimal(value) + " ");
					}

				} else if (comp1.getId().startsWith("tdb")) {
					java.util.Date value = ((Datebox) comp1).getValue();
					if (value != null) {
						String where_field = FSearchData.getStr(comp1.getAttribute("WHERE_FIELD"));
						String operator = FSearchData.getStr(comp1.getAttribute("WHERE_OPERATOR"));
						sql.addLine(" and " + where_field + " " + operator + " '" + FnDate.getDateString(value, "yyyy-MM-dd") + "' ");
					}

				} else if (comp1.getId().startsWith("txt")) {
					String value = ((Textbox) comp1).getValue();
					if (!FSearchData.isEmpty(value)) {
						String where_field = FSearchData.getStr(comp1.getAttribute("WHERE_FIELD"));
						String operator = FSearchData.getStr(comp1.getAttribute("WHERE_OPERATOR"));
						if (operator.toLowerCase().equals("like1")) {//%s%
							sql.addLine(" and " + where_field + " like '%" + FSearchData.sqlQuote(value) + "%' ");
						} else if (operator.toLowerCase().equals("like2")) {//s%
							sql.addLine(" and " + where_field + " like '" + FSearchData.sqlQuote(value) + "%' ");
						} else if (operator.toLowerCase().equals("like3")) {//%s
							sql.addLine(" and " + where_field + " like '%" + FSearchData.sqlQuote(value) + "' ");
						} else {
							sql.addLine(" and " + where_field + " " + operator + " '" + FSearchData.sqlQuote(value) + "' ");
						}

					}

				} else if (comp1.getId().startsWith("cmb")) {
					Combobox b1 = (Combobox) comp1;
					String value = ZkUtil.getSelectItemValueComboBox(b1);
					if (!FSearchData.isEmpty(value)) {
						String where_field = FSearchData.getStr(comp1.getAttribute("WHERE_FIELD"));
						String operator = FSearchData.getStr(comp1.getAttribute("WHERE_OPERATOR"));
						if (operator.toLowerCase().equals("like1")) {//%s%
							sql.addLine(" and " + where_field + " like '%" + FSearchData.sqlQuote(value) + "%' ");
						} else if (operator.toLowerCase().equals("like2")) {//s%
							sql.addLine(" and " + where_field + " like '" + FSearchData.sqlQuote(value) + "%' ");
						} else if (operator.toLowerCase().equals("like3")) {//%s
							sql.addLine(" and " + where_field + " like '%" + FSearchData.sqlQuote(value) + "' ");
						} else {
							sql.addLine(" and " + where_field + " " + operator + " '" + FSearchData.sqlQuote(value) + "' ");
						}
					}

				}

			}

			if (fSearchData.getGroupBy().size() > 0) {
				String groupby = "";
				for (String groupField : fSearchData.getGroupBy()) {
					if (FSearchData.isEmpty(groupby)) {
						groupby += groupField;
					} else {
						groupby += " ," + groupField;
					}
				}
				sql.addLine("group by " + groupby);
			}

			if (fSearchData.getHaving().size() > 0) {
				String having = "";
				for (String havingField : fSearchData.getHaving()) {
					if (FSearchData.isEmpty(having)) {
						having += havingField;
					} else {
						having += " ," + havingField;
					}
				}
				sql.addLine("having " + having);
			}

			if (fSearchData.getOrderBy().size() > 0) {
				String orderby = "";
				for (String order : fSearchData.getOrderBy()) {
					if (FSearchData.isEmpty(orderby)) {
						orderby += order;
					} else {
						orderby += " ," + order;
					}
				}
				sql.addLine(" order by " + orderby);
			}
			System.out.println("sql : " + sql.getSql());

			dataSet.clear();
			try (FDbc dbc = FDbc.connectMasterDb()) {
				try (java.sql.ResultSet rs = dbc.getResultSet3(sql.getSql(), fSearchData.getShowLimitRow());) {
					while (rs.next()) {

						FModelHasMap rec = new FModelHasMap();

						//ฟิลด์ที่แสดงใน grid
						for (HashMap col1 : this.fSearchData.getShowColum()) {
							String showFieldName = getFieldName(FSearchData.getStr(col1.get("FField")));
							rec.put(showFieldName, rs.getObject(showFieldName));//ให้เป็นฟิลด์ตัวใหญ่ไปเลย
						}

						//ฟิลด์ที่ไม่แสดงใน grid
						for (HashMap col1 : this.fSearchData.getHideColum()) {
							String showHideFieldName = getFieldName(FSearchData.getStr(col1.get("FField")));
							rec.put(showHideFieldName, rs.getObject(showHideFieldName));//ให้เป็นฟิลด์ตัวใหญ่ไปเลย 
						}

						dataSet.add(rec);
					}
				}
			}

			SimpleListModel rstModel = new SimpleListModel(dataSet);

			if (this.fSearchData.isShowCheckbox()) {
				rstModel.setMultiple(true);//ต้อง set muti ที่ model แทนไม่งั้น checkbox แบบ muti จะหาย
				listBox1.setModel(rstModel);
				listBox1.renderAll();//สั่งให้ render object ทุกแถวโดยไม่ต้องรอ click next ไปที่หน้านั้น
			} else {
				listBox1.setModel(rstModel);
			}

			if (rstModel.getSize() > 0) {
				if (!this.fSearchData.isShowCheckbox()) { //ถ้าไม่ทำแบบนี้จะติ๊กเลือกรายการแรกทุกครั้งที่ค้นหา
					listBox1.setSelectedIndex(0);
				}
				listBox1.focus();
			}

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	private ListitemRenderer getListitemRenderer1() {

		return (item, data, index) -> {

			FModelHasMap rs = (FModelHasMap) data;

			//=== Event
			if (fSearchData.isShowCheckbox() == false) {
				item.addEventListener(Events.ON_DOUBLE_CLICK, (event) -> doOnDbClickRow(event));
				item.addEventListener(Events.ON_OK, (event) -> doOnDbClickRow(event));
			}

			//=== เพิ่ม Attribute
			if (FSearchData.isEmpty(fSearchData.getReturnFieldName())) {
				throw new Exception("ReturnFieldName ไม่ถูกต้อง");
			}

			String[] fieldList = fSearchData.getReturnFieldName().split("\n");
			for (String fName : fieldList) {
				String returnFieldName = getFieldName(fName);
				item.setAttribute(returnFieldName, rs.get(returnFieldName));
			}

			//=== display data
			if (fSearchData.isShowCheckbox()) {
				new Listcell("").setParent(item);
			}
			new Listcell((index + 1) + "").setParent(item);

			//int idx = 0;
			for (HashMap col1 : fSearchData.getShowColum()) {

				String showFieldName = getFieldName(FSearchData.getStr(col1.get("FField")));

				String showColumFieldType = FSearchData.getStr(col1.get("FFieldType")).trim();
				if (FSearchData.isEmpty(showColumFieldType)) {
					Msg.error("คอลัมภ์ " + showFieldName + "ยังไม่ระบุชนิดข้อมูล");
				}

				String[][] descAndStyle = null;
				if (col1.get("FdescAndStyle") != null) {
					descAndStyle = (String[][]) col1.get("FdescAndStyle");
				}

				if (showColumFieldType.equals("STRING")) {

					if (descAndStyle != null) {

						Listcell lc = new Listcell(getDescValue(descAndStyle, rs.getString(showFieldName)));
						String fSt = getStyleValue(descAndStyle, rs.getString(showFieldName));
						if (!FSearchData.isEmpty(fSt)) {
							lc.setStyle(fSt);
						}
						lc.setParent(item);

					} else {
						new Listcell(rs.getString(showFieldName)).setParent(item);
					}

				} else if (showColumFieldType.equals("DATE")) {

					new Listcell(FnDate.displayDateString(rs.getDate(showFieldName))).setParent(item);

				} else if (showColumFieldType.equals("DATETIME")) {

					new Listcell(FnDate.displayDateTimeString(rs.getTimestamp(showFieldName))).setParent(item);

				} else if (showColumFieldType.equals("NUMBER")) {

					if (descAndStyle != null) {

						Listcell lc = new Listcell(getDescValue(descAndStyle, FSearchData.getStrBigDecimal(rs.getBigDecimal(showFieldName))));

						String fSt = getStyleValue(descAndStyle, FSearchData.getStrBigDecimal(rs.getBigDecimal(showFieldName)));
						if (!FSearchData.isEmpty(fSt)) {
							lc.setStyle(fSt);
						}
						lc.setParent(item);

					} else {
						new Listcell(FSearchData.getStrBigDecimal(rs.getBigDecimal(showFieldName))).setParent(item);
					}

				} else if (showColumFieldType.equals("INTEGER")) {

					if (descAndStyle != null) {

						Listcell lc = new Listcell(getDescValue(descAndStyle, rs.getInt(showFieldName) + ""));

						String fSt = getStyleValue(descAndStyle, rs.getInt(showFieldName) + "");
						if (!FSearchData.isEmpty(fSt)) {
							lc.setStyle(fSt);
						}
						lc.setParent(item);

					} else {
						new Listcell(rs.getInt(showFieldName) + "").setParent(item);
					}

				} else {
					Listcell lc = new Listcell(rs.getString(showFieldName));
					lc.setParent(item);

				}

			}

		};

	}

	private void doOnDbClickRow(Event event) {

		if (fSearchData.isShowCheckbox()) { //เลือกแบบ checkbox ส่งค่ากลับเป็น hasmap

			Set<Listitem> lst_listitem = listBox1.getSelectedItems();
			if (lst_listitem.size() > 0) {

				String[] fieldList = fSearchData.getReturnFieldName().split("\n");

				for (Listitem listitem : lst_listitem) {

					FModelHasMap dat = new FModelHasMap();

					for (String fName : fieldList) {
						String returnFieldName = getFieldName(fName);
						if (!FSearchData.isEmpty(returnFieldName)) {
							dat.put(returnFieldName, listitem.getAttribute(returnFieldName));
						}
					}

					fSearchData.getReturnValueMapList().add(dat);

				}
				ZkUtil.callBackMethod(this.fSearchData.getCallForm(), this.fSearchData.getCallMethodName(), fSearchData.getReturnValueMapList());

				//== Standard Command
				dataSet.clear();
				dataSet = null;
				super.onClose();
			}

		} else { //ไม่เลือกแบบ checkbox

			Listitem item1 = listBox1.getSelectedItem();
			if (item1 != null) {

				String[] fieldList = fSearchData.getReturnFieldName().split("\n");

				if (fieldList.length > 1) { //คืนค่ามากกว่า 1 ฟิลด์จะใส่ค่ากลับเป็น hasmap แทน

					FModelHasMap dat = new FModelHasMap();

					for (String fName : fieldList) {
						String returnFieldName = getFieldName(fName);
						if (!FSearchData.isEmpty(returnFieldName)) {
							dat.put(returnFieldName, item1.getAttribute(returnFieldName));
						}
					}

					fSearchData.getReturnValueMapList().add(dat);

					ZkUtil.callBackMethod(this.fSearchData.getCallForm(), this.fSearchData.getCallMethodName(), fSearchData.getReturnValueMapList());

					//== Standard Command
					dataSet.clear();
					dataSet = null;
					super.onClose();

				} else {

					String returnFieldName = getFieldName(fieldList[0]);

					if (item1.getAttribute(returnFieldName) != null) {

						fSearchData.setReturnValue(item1.getAttribute(returnFieldName));
						System.out.println(returnFieldName + " : " + fSearchData.getReturnValue());

						ZkUtil.callBackMethod(this.fSearchData.getCallForm(), this.fSearchData.getCallMethodName(), fSearchData.getReturnValue());

						//== Standard Command
						dataSet.clear();
						dataSet = null;
						super.onClose();
					}

				}

			}

		}

	}

	public static String getDescValue(String[][] data1, String code) {
		String ret = "";
		for (String[] s1 : data1) {
			if (s1[0].equals(code)) {
				if (s1.length >= 2) {
					ret = s1[1];
				}
				break;
			}
		}
		return ret;
	}

	public static String getStyleValue(String[][] data1, String code) {
		String ret = "";
		for (String[] s1 : data1) {
			if (s1[0].equals(code)) {
				if (s1.length >= 3) {
					ret = s1[2];
				}
				break;
			}
		}
		return ret;
	}

	/**
	 * ตัดเอาเฉพาะชื่อฟิลด์ไม่เอา Alias เช่น cus.THNAME ให้เอา THNAME ก็พอ
	 * @param fName
	 * @return
	 */
	protected String getFieldName(String fName) {
		String returnFieldName = fName.trim().toUpperCase();
		if (returnFieldName.split("\\.").length > 1) {
			String[] s1 = returnFieldName.split("\\.");
			returnFieldName = s1[s1.length - 1].trim();
		}
		return returnFieldName;
	}

	public void onClick_btnOK() {
		doOnDbClickRow(null);
	}

}

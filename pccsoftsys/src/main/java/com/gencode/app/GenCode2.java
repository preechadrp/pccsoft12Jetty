/*
 * The MIT License
 *
 * Copyright (c) 2020 preecha daroonpunth (prch13@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.gencode.app;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class GenCode2 extends JPanel {
	private static final long serialVersionUID = 1L;
	public JScrollPane scrollPane;
	public JLabel lblNewLabel;
	public JButton btnNewButton;
	public JButton btnCopy;
	public JScrollPane scrollPane_2;
	public JButton btnMapControl;
	public JButton btnClearValue;
	public JButton btnAddcontrol;
	public JButton btnDefineTextbox;
	public JButton btnSetdatatomodel;
	public JLabel lblModelname;
	public JTextField txtModelname;
	public JLabel lblCode;
	public JLabel lblNewLabel_1;
	public JLabel lblCmbCombobox;
	public JTextArea ta_field_list;
	private final JTextArea ta_result = new JTextArea();

	public void doCopytoClipboard() {
		String data = ta_result.getText();
		StringSelection sData = new StringSelection(data);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(sData, sData);
	}

	public void doPastClipboard() {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable clipData = clipboard.getContents(clipboard);
		if (clipData != null) {
			try {
				if (clipData.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					String s = (String) (clipData.getTransferData(DataFlavor.stringFlavor));
					ta_field_list.setText(s);
				}
			} catch (Exception e) {
				System.err.println("Flavor unsupported: " + e);
			}
		}

	}

	public java.util.List<String> getComponentZul() {
		java.util.List<String> lstComponent = new java.util.ArrayList<String>();
		String zulString = ta_field_list.getText();
		String[] data1 = zulString.split("id=\"");

		String[] arrPrefixComponent = {
				"dec", "tdb", "lbl", "txt", "int", "cmb", "chk", "btn", "grd", "div", "grc"
		};

		int idx1 = 0;
		for (String s1 : data1) {
			// System.out.println(s1);
			idx1++;
			if (idx1 > 1) {// เริ่มจากลำดับที่ 2
				int idxDoubleQoute = s1.indexOf("\"");
				String compName = s1.substring(0, idxDoubleQoute);
				// System.out.println(compName);
				String compNameLower = compName.toLowerCase();
				if (startWithInArray(compNameLower, arrPrefixComponent)) {
					lstComponent.add(compName);
				}
			}
		}

		return lstComponent;
	}

	public String getMyClassName(String field) {
		String res = "Textbox";
		String[][] arrClass = {
				{ "dec", "MyDecimalbox" },
				{ "tdb", "MyDatebox" },
				{ "lbl", "Label" },
				{ "txt", "Textbox" },
				{ "int", "Intbox" },
				{ "cmb", "Combobox" },
				{ "chk", "Checkbox" },
				{ "btn", "Button" },
				{ "grd", "Grid" },
				{ "div", "Div" },
				{ "grc", "Column" }
		};

		for (String[] prefix : arrClass) {
			if (field.toLowerCase().startsWith(prefix[0])) {
				res = prefix[1];
				break;
			}
		}

		return res;
	}

	public void doDefineControl() {
		ta_result.setText("");
		String strValue = "";
		List<String> lstComp = getComponentZul();
		if (lstComp.size() > 0) {
			for (String comp : lstComp) {
				strValue = "public " + getMyClassName(comp.trim()) + " " + comp.trim() + ";\n";
				ta_result.append(strValue);
			}
		}
	}

	public void doMapControl() {
		ta_result.setText("");
		String strValue = "";
		List<String> lstComp = getComponentZul();
		if (lstComp.size() > 0) {
			for (String comp : lstComp) {
				strValue = comp.trim() + " = (" + getMyClassName(comp.trim()) + ") getFellow(\"" + comp.trim() + "\");\n";
				ta_result.append(strValue);
			}
		}
	}

	private void doAddEnterIndex() {
		ta_result.setText("");
		
		String[] arrExcept = { "grd", "div", "lbl", "grc" };//ไม่ต้อง addEnterIndex
		
		List<String> lstComp = getComponentZul();
		if (lstComp.size() > 0) {
			for (String comp : lstComp) {
				if (startWithInArray(comp, arrExcept)) {
					//nothing
				} else {
					String strValue = "addEnterIndex(" + comp + ");\n";
					ta_result.append(strValue);
				}
			}
		}
	}

	private void doClearValue() {
		ta_result.setText("");
		
		String[] arrExcept = { "grd", "div", "btn", "lst", "grc" };
		
		List<String> lstComp = getComponentZul();
		if (lstComp.size() > 0) {
			String strValue = "";
			for (String comp : lstComp) {

				if (startWithInArray(comp, arrExcept)) {
					//nothing
				} else {
					if (comp.trim().toLowerCase().startsWith("cmb")) {
						strValue = comp.trim() + ".setSelectedIndex(-1);\n";
					} else if (comp.trim().toLowerCase().startsWith("dec")) {
						strValue = comp.trim() + ".setValue(BigDecimal.ZERO);\n";
					} else if (comp.trim().toLowerCase().startsWith("chk")) {
						strValue = comp.trim() + ".setChecked(false);\n";
					} else if (comp.trim().toLowerCase().startsWith("tdb")) {
						strValue = comp.trim() + ".setValue(null);\n";
					} else if (comp.trim().toLowerCase().startsWith("int")) {
						strValue = comp.trim() + ".setText(\"\");\n";
					} else {
						strValue = comp.trim() + ".setValue(\"\");\n";
					}
					ta_result.append(strValue);
				}
			}
		}
	}

	public boolean startWithInArray(String compName , String[] arrPrefix) {
		boolean exist = false; 
		for (String prefix : arrPrefix) {
			if (compName.startsWith(prefix)) {
				exist = true;
				break;
			}
		}
		return exist;
	}

	public void doSetDataToModel() {
		StringBuilder sb = new StringBuilder();
		String sModelName = txtModelname.getText().trim();
		ta_result.setText("");
		List<String> lstComp = getComponentZul();
		if (lstComp.size() > 0) {

			String strValue = "";
			for (String comp : lstComp) {
				if (comp.trim().toLowerCase().startsWith("tdb")) {
					strValue = sModelName + ".set" + comp.trim().substring(3).toUpperCase() + "(" + comp.trim() + ".getDate());\n";
				} else if (comp.trim().toLowerCase().startsWith("cmb")) {
					String s1 = "UIUtility.getSelectItemValueComboBox(" + comp.trim() + ")";
					strValue = sModelName + ".set" + comp.trim().substring(3).toUpperCase() + "(" + s1 + ");\n";
				} else if (comp.trim().toLowerCase().startsWith("chk")) {
					strValue = sModelName + ".set" + comp.trim().substring(3).toUpperCase() + "(" + comp.trim() + ".isChecked());\n";
				} else {
					strValue = sModelName + ".set" + comp.trim().substring(3).toUpperCase() + "(" + comp.trim() + ".getValue());\n";
				}
				sb.append(strValue);
			}

			ta_result.setText(sb.toString());

		}
	}

	public void onResized() {
		int w = this.getWidth();
		int h = this.getHeight();
		if (w > 61 && h > 300) {
			scrollPane_2.setSize(w - 50, h - 310);
			scrollPane_2.setViewportView(ta_result);
		}
	}

	/**
	 * Create the panel.
	 */
	public GenCode2() {

		initialize();
	}

	private void initialize() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				onResized();
			}
		});
		setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(25, 30, 265, 249);
		add(scrollPane);

		ta_field_list = new JTextArea();
		ta_field_list.setText(
				"<separator />\r\n <textbox id=\"txtMicroServiceLink\" value=\"\" visible=\"false\" />\r\n\r\n <div style=\"grid-template-columns: 50% 50%;\" sclass=\"mainGridLayout1\">\r\n\r\n\t<div style=\"grid-template-columns: auto auto;\" sclass=\"mainGridLayout1\">\r\n\t\t<label value=\"เลขบัตร\" />\r\n\t\t<textbox id=\"txtCitizenID\" readonly=\"true\" width=\"250px\" />\r\n\t\t<label value=\"คำนำหน้าชื่อ\" />\r\n\t\t<textbox id=\"txtNameTH_Prefix\" readonly=\"true\" width=\"250px\" />\r\n\t\t<label value=\"ชื่อ\" />\r\n\t\t<textbox id=\"txtNameTH_FirstName\" readonly=\"true\" width=\"250px\" />\r\n\t\t<label value=\"นามสกุล\" />\r\n\t\t<textbox id=\"txtNameTH_SurName\" readonly=\"true\" width=\"250px\" />\r\n\t\t<label value=\"เพศ\" />\r\n\t\t<textbox id=\"txtSex\" readonly=\"true\" width=\"250px\" />\r\n\t\t<label value=\"วันเกิด\" />\r\n\t\t<textbox id=\"txtBirthDate\" readonly=\"true\" width=\"250px\" />\r\n\t\t<label value=\"อายุ\" />\r\n\t\t<textbox id=\"txtAge\" readonly=\"true\" width=\"250px\" />\r\n\t\t<label value=\"เลขที่\" />\r\n\t\t<textbox id=\"txtAddress_No\" readonly=\"true\" width=\"250px\" />\r\n\t\t<label value=\"หมู่ที่\" />\r\n\t\t<textbox id=\"txtAddress_Moo\" readonly=\"true\" width=\"250px\" />\r\n\t\t<label value=\"ซอย\" />\r\n\t\t<textbox id=\"txtAddress_Soi\" readonly=\"true\" width=\"250px\" />\r\n\t\t<label value=\"ถนน\" />\r\n\t\t<textbox id=\"txtAddress_Road\" readonly=\"true\" width=\"250px\" />\r\n\t\t<label value=\"ตำบล/แขวง\" />\r\n\t\t<textbox id=\"txtAddress_Tumbol\" readonly=\"true\" width=\"250px\" />\r\n\t\t<label value=\"อำเภอ/เขต\" />\r\n\t\t<textbox id=\"txtAddress_Amphur\" readonly=\"true\" width=\"250px\" />\r\n\t\t<label value=\"จังหวัด\" />\r\n\t\t<textbox id=\"txtAddress_Province\" readonly=\"true\" width=\"250px\" />\r\n\r\n\t</div>\r\n\r\n\t<div style=\"text-align: center;\">\r\n\t\t<image id=\"img11\" visible=\"true\" width=\"45%\" />\r\n\t</div>\r\n\t\r\n </div>");
		scrollPane.setViewportView(ta_field_list);

		lblNewLabel = new JLabel("Zul string");
		lblNewLabel.setBounds(26, 11, 55, 16);
		add(lblNewLabel);

		btnNewButton = new JButton("<- PAST");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doPastClipboard();
			}
		});
		btnNewButton.setBounds(300, 22, 149, 26);
		add(btnNewButton);

		btnCopy = new JButton("Copy To Clipboard");
		btnCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCopytoClipboard();
			}
		});
		btnCopy.setBounds(302, 263, 149, 26);
		add(btnCopy);

		scrollPane_2 = new JScrollPane();
		scrollPane_2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_2.setBounds(25, 291, 708, 189);
		add(scrollPane_2);
		scrollPane_2.setViewportView(ta_result);

		btnMapControl = new JButton("Map control");
		btnMapControl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doMapControl();
			}
		});
		btnMapControl.setBounds(300, 80, 149, 26);
		add(btnMapControl);

		btnClearValue = new JButton("Clear Value");
		btnClearValue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doClearValue();
			}
		});
		btnClearValue.setBounds(300, 138, 149, 26);
		add(btnClearValue);

		btnAddcontrol = new JButton("addToControls");
		btnAddcontrol.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAddEnterIndex();
			}
		});
		btnAddcontrol.setBounds(300, 109, 149, 26);
		add(btnAddcontrol);

		btnDefineTextbox = new JButton("Define control");
		btnDefineTextbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doDefineControl();
			}
		});
		btnDefineTextbox.setBounds(300, 50, 149, 26);
		add(btnDefineTextbox);

		btnSetdatatomodel = new JButton("setDataToModel");
		btnSetdatatomodel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSetDataToModel();
			}
		});
		btnSetdatatomodel.setBounds(300, 167, 149, 26);
		add(btnSetdatatomodel);

		lblModelname = new JLabel("Model name :");
		lblModelname.setBounds(459, 169, 87, 16);
		add(lblModelname);

		txtModelname = new JTextField();
		txtModelname.setText("modelName");
		txtModelname.setColumns(10);
		txtModelname.setBounds(539, 167, 118, 22);
		add(txtModelname);

		lblCode = new JLabel("หลักการตั้งชื่อ สามตัวแรกของชื่อจะเอาไปใช้สร้าง Code ดังนี้ ");
		lblCode.setBounds(300, 200, 511, 20);
		add(lblCode);

		lblNewLabel_1 = new JLabel("dec=MyDecimalbox2 , tdb = TextDatebox , txt = Textbox , int = Intbox");
		lblNewLabel_1.setBounds(300, 221, 536, 14);
		add(lblNewLabel_1);

		lblCmbCombobox = new JLabel("cmb = Combobox , chk = Checkbox , btn = Button,div=Div,grd=Grid");
		lblCmbCombobox.setBounds(300, 238, 536, 14);
		add(lblCmbCombobox);
	}
}

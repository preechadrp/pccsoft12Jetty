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
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

public class GenCode1 extends JPanel {
	private static final long serialVersionUID = 1L;
	public JScrollPane scrollPane;
	public JLabel lblNewLabel;
	public JButton btnCopy;
	public JScrollPane scrollPane_2;
	public JButton btnCreateFacade;
	public JButton btnCreateModel;
	public JTextField txtTablename;
	public JLabel lblTableName;
	// public JTextField txtDbconndefaultdb;
	public JTextArea ta_field_list;
	public JTextArea ta_result;
	// private JCheckBox chckDbName;

	public void doCopytoClipboard() {
		String data = ta_result.getText();
		StringSelection sData = new StringSelection(data);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(sData, sData);
	}

	public String getMyClassName(String field) {
		String res = "Textbox";
		if (field.toLowerCase().startsWith("dec")) {
			res = "MyDecimalbox2";
		} else if (field.toLowerCase().startsWith("tdb")) {
			res = "TextDatebox";
		} else if (field.toLowerCase().startsWith("txt")) {
			res = "Textbox";
		} else if (field.toLowerCase().startsWith("int")) {
			res = "Intbox";
		} else if (field.toLowerCase().startsWith("cmb")) {
			res = "Combobox";
		}

		return res;
	}

	public void doCreateFacade() {
		if (!txtTablename.getText().trim().equals("") && !ta_field_list.getText().trim().equals("")) {
			ta_result.setText("");

			//(Primary Key)
			String[] pkName = ta_field_list.getText().trim().split("\n");
			Util1.trimStringInArray(pkName);

			Map<String, Object> hashMap = new LinkedHashMap<String, Object>();
			hashMap.put("@tableName", txtTablename.getText().trim().toUpperCase());// ระบุชื่อตาราง
			hashMap.put("@daoClassName", "Tbf" + txtTablename.getText().trim().toUpperCase());// ระบุชื่อ Class
			hashMap.put("@daoPackageName", "pcc.bus.tbf");//ระบุชื่อ Package
			hashMap.put("@daoPKName", pkName);
			// if (chckDbName.isSelected()) {
			// hashMap.put("@daoDefaultDB", txtDbconndefaultdb.getText().trim());
			// }

			GenTbf createFacade = new GenTbf();
			ta_result.append(createFacade.getCodeStringBuffer(hashMap));
		}
	}

	public void doCreateModel() {
		if (!txtTablename.getText().trim().equals("")) {
			ta_result.setText("");
			GenTbo createFacade = new GenTbo();
			Map<String, Object> hashMap = new LinkedHashMap<String, Object>();
			hashMap.put("@tableName", txtTablename.getText().trim().toLowerCase());
			hashMap.put("@daoClassName", "Tbo" + txtTablename.getText().trim().toUpperCase());
			hashMap.put("@daoPackageName", "pcc.bus.tbo");
			String sb = createFacade.getCodeStringBuffer(hashMap);
			ta_result.append(sb);
		}
	}

	public String upperCharAt4(String str) {
		String s1 = str.substring(0, 3);
		String s2 = upperFirstChar(str.substring(3));
		return s1 + s2;
	}

	public String upperFirstChar(String str) {
		Integer n1 = str.length();
		String s2 = str.substring(0, 1).toUpperCase().concat(str.substring(1, n1));
		return s2;
	}

	public void onResized() {
		int w = this.getWidth();
		int h = this.getHeight();
		if (w > 61 && h > 300) {
			scrollPane_2.setSize(w - 50, h - 220);
			scrollPane_2.setViewportView(ta_result);
		}
	}

	/**
	 * Create the panel.
	 */
	public GenCode1() {

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
		scrollPane.setBounds(342, 33, 185, 106);
		add(scrollPane);

		ta_field_list = new JTextArea();
		ta_field_list.setText("USER_ID");
		scrollPane.setViewportView(ta_field_list);

		lblNewLabel = new JLabel("Primary Fields");
		lblNewLabel.setBounds(344, 11, 99, 16);
		add(lblNewLabel);

		btnCopy = new JButton("Copy To Clipboard");
		btnCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCopytoClipboard();
			}
		});
		btnCopy.setBounds(500, 161, 222, 26);
		add(btnCopy);

		scrollPane_2 = new JScrollPane();
		scrollPane_2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_2.setBounds(24, 198, 708, 277);
		add(scrollPane_2);

		ta_result = new JTextArea();
		scrollPane_2.setViewportView(ta_result);

		btnCreateFacade = new JButton("สร้าง Facade");
		btnCreateFacade.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCreateFacade();
			}
		});
		btnCreateFacade.setBounds(273, 161, 222, 26);
		add(btnCreateFacade);

		btnCreateModel = new JButton("สร้าง Model");
		btnCreateModel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCreateModel();
			}
		});
		btnCreateModel.setBounds(24, 161, 239, 26);
		add(btnCreateModel);

		txtTablename = new JTextField();
		txtTablename.setText("fuser");
		txtTablename.setColumns(10);
		txtTablename.setBounds(118, 35, 214, 26);
		add(txtTablename);

		lblTableName = new JLabel("Table Name");
		lblTableName.setHorizontalAlignment(SwingConstants.LEFT);
		lblTableName.setBounds(26, 37, 86, 16);
		add(lblTableName);

//		txtDbconndefaultdb = new JTextField();
//		txtDbconndefaultdb.setText("DbName.SHKSOFTDB");
//		txtDbconndefaultdb.setColumns(10);
//		txtDbconndefaultdb.setBounds(118, 72, 214, 26);
//		add(txtDbconndefaultdb);
//
//		chckDbName = new JCheckBox("ระบุ DbName");
//		chckDbName.setHorizontalAlignment(SwingConstants.LEFT);
//		chckDbName.setBounds(6, 73, 106, 23);
//		add(chckDbName);
	}
}

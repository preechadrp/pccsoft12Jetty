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

import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
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

public class GenCode3 extends JPanel {

	private static final long serialVersionUID = 1L;
	public JScrollPane scrollPane_1;
	public JTextArea ta_sql;
	public JLabel lblNewLabel;
	public JButton btnCopy;
	public JScrollPane scrollPane_2;
	public JTextArea ta_result;
	public JButton btnGenCode;
	public JLabel lblModelname;
	public JTextField txtModelname;
	public JButton btnPast;
	private JButton btnSetmodel;

	public void doPastClipboard() {

		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable clipData = clipboard.getContents(clipboard);
		if (clipData != null) {
			try {
				if (clipData.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					String s = (String) (clipData.getTransferData(DataFlavor.stringFlavor));
					ta_sql.setText(s);
				}
			} catch (Exception e) {
				System.err.println("Flavor unsupported: " + e);
			}
		}

	}

	public void doCopytoClipboard() {
		String data = ta_result.getText();
		StringSelection sData = new StringSelection(data);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(sData, sData);
	}

	public void doGenCode() {
		ta_result.setText("");
		String[] str = ta_sql.getText().split("\n");
		//Util1.trimStringInArray(str);

		ta_result.append("String " + txtModelname.getText().trim() + " = \"\";\n");
		if (str.length != 0) {
			for (int i = 0; i < str.length; i++) {
				ta_result.append(txtModelname.getText().trim() + " += \" " + str[i].trim() + "\";\n");
			}
		}
	}

	public void doGenCode2() {
		ta_result.setText("");
		String[] str = ta_sql.getText().split("\n");
		//Util1.trimStringInArray(str);
		ta_result.append("SqlStr " + txtModelname.getText().trim() + " = new SqlStr();\n");
		if (str.length != 0) {
			for (int i = 0; i < str.length; i++) {
				ta_result.append(txtModelname.getText().trim() + ".addLine(\"" + str[i] + "\");\n");
			}
		}
	}

	public void doGenPrepare() {
		ta_result.setText("");
		String[] str = ta_sql.getText().split(",");
		Util1.trimStringInArray(str);

		ta_result.append("String sql_insert = \" INSERT INTO xxxx (" + ta_sql.getText().trim() + ")\";\n");
		String q1 = "";
		if (str.length != 0) {
			for (int i = 0; i < str.length; i++) {
				if (!q1.equals("")) {
					q1 += ",";
				}
				q1 += "?";
			}
		}
		ta_result.append("sql_insert+=\" VALUES(" + q1 + ")\";\n");
		ta_result.append("java.sql.PreparedStatement ps = dbc.getPreparedStatement(sql_insert);\n");
		ta_result.append("int indx = 1; \n");
		if (str.length != 0) {
			for (int i = 0; i < str.length; i++) {
				ta_result.append("ps.setString(indx++, rs2.getString(\"" + str[i].trim() + "\")); //" + str[i].trim() + "\n");
			}
		}
		ta_result.append("ps.executeUpdate();");

	}

	protected void doGenModel1() {
		Map<String, Object> hashMap = new LinkedHashMap<String, Object>();
		hashMap.put("@sql", ta_sql.getText());
		hashMap.put("@daoClassName", txtModelname.getText());

		ta_result.setText("");
		GenModelBySql cr = new GenModelBySql();
		StringBuffer sb = cr.getCodeStringBuffer(hashMap);
		ta_result.append(sb.toString());
	}

	protected void doSetToModel() {
		Map<String, Object> hashMap = new LinkedHashMap<String, Object>();
		hashMap.put("@sql", ta_sql.getText());
		hashMap.put("@daoClassName", txtModelname.getText());

		ta_result.setText("");
		SetModelBySql cr = new SetModelBySql();
		StringBuffer sb = cr.getCodeStringBuffer(hashMap);
		ta_result.append(sb.toString());
	}

	public void onResized() {
		int w = this.getWidth();
		int h = this.getHeight();
		if (w > 61 && h > 300) {
			scrollPane_1.setSize(w - 45, scrollPane_1.getHeight());
			scrollPane_1.setViewportView(ta_sql);
			scrollPane_2.setSize(w - 45, h - 310);
			scrollPane_2.setViewportView(ta_result);
		}
	}

	/**
	 * Create the frame.
	 */
	public GenCode3() {
		initialize();
	}

	private void initialize() {
		setBounds(100, 100, 838, 525);
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				onResized();
			}
		});
		this.setLayout(null);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setBounds(24, 30, 706, 225);
		this.add(scrollPane_1);

		ta_sql = new JTextArea();
		ta_sql.setText("select * from ACCT_DETAIL");
		scrollPane_1.setViewportView(ta_sql);

		lblNewLabel = new JLabel("คำสั่ง SQL");
		lblNewLabel.setBounds(25, 6, 123, 16);
		this.add(lblNewLabel);

		btnCopy = new JButton("Copy To Clipboard");
		btnCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCopytoClipboard();
			}
		});
		btnCopy.setBounds(372, 257, 149, 26);
		this.add(btnCopy);

		scrollPane_2 = new JScrollPane();
		scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_2.setBounds(24, 286, 706, 189);
		this.add(scrollPane_2);

		ta_result = new JTextArea();
		scrollPane_2.setViewportView(ta_result);

		btnGenCode = new JButton("sql +=");
		btnGenCode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doGenCode();
			}
		});
		btnGenCode.setBounds(190, 257, 88, 26);
		this.add(btnGenCode);

		lblModelname = new JLabel("ตัวแปร :");
		lblModelname.setBounds(23, 262, 55, 16);
		this.add(lblModelname);

		txtModelname = new JTextField();
		txtModelname.setText("sql");
		txtModelname.setBounds(81, 257, 100, 26);
		this.add(txtModelname);
		txtModelname.setColumns(10);

		btnPast = new JButton("Past From Clipboard");
		btnPast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doPastClipboard();
			}
		});
		btnPast.setBounds(195, 3, 166, 26);
		this.add(btnPast);

		JButton btnGenPrepare = new JButton("Gen Prepare");
		btnGenPrepare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doGenPrepare();
			}
		});
		btnGenPrepare.setBounds(524, 257, 105, 26);
		add(btnGenPrepare);

		JButton btnGenCode_1 = new JButton("SqlStr");
		btnGenCode_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doGenCode2();
			}
		});
		btnGenCode_1.setBounds(281, 257, 88, 26);
		add(btnGenCode_1);

		JButton btnMode = new JButton("Model1");
		btnMode.setMargin(new Insets(2, 2, 2, 2));
		btnMode.setToolTipText("สร้าง model จาก sql");
		btnMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doGenModel1();
			}

		});
		btnMode.setBounds(632, 257, 87, 26);
		add(btnMode);

		btnSetmodel = new JButton("setModel1");
		btnSetmodel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSetToModel();
			}
		});
		btnSetmodel.setToolTipText("set ค่า model จาก result set");
		btnSetmodel.setMargin(new Insets(2, 2, 2, 2));
		btnSetmodel.setBounds(722, 257, 75, 26);
		add(btnSetmodel);
	}

}

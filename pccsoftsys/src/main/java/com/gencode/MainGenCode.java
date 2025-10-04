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
package com.gencode;

import java.awt.CardLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import com.gencode.app.GenCode1;
import com.gencode.app.GenCode2;
import com.gencode.app.GenCode3;
import com.gencode.app.Util1;

public class MainGenCode extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JTabbedPane tabbedPane;
	public GenCode1 genCodeTable;
	public GenCode2 genCode2;
	public GenCode3 genCode3;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					com.gencode.app.MyTheme1.setTheme("javax.swing.plaf.metal.MetalLookAndFeel");
					// com.gencode.app.MyTheme2.setTheme();

					MainGenCode frame = new MainGenCode();
					Util1.doSetCenterScreen(frame);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainGenCode() {
		initialize();
	}

	private void initialize() {
		setTitle("Tool (Update 31/7/2556 8.39)");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 846, 548);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, "name_8866307397600");

		genCodeTable = new GenCode1();
		tabbedPane.addTab("Model&Facade", null, genCodeTable, null);

		genCode2 = new GenCode2();
		tabbedPane.addTab("MapControl", null, genCode2, null);

		genCode3 = new GenCode3();
		tabbedPane.addTab("Make SQL To Varible", null, genCode3, null);
	}
}

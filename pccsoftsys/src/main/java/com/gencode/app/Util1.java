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

import java.awt.Dimension;

public class Util1 {

	public static void doSetCenterScreen(Object frmName) {
		// set form center
		Dimension scr = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

		if (frmName instanceof javax.swing.JFrame) {
			Dimension fr = ((javax.swing.JFrame) frmName).getSize();
			((javax.swing.JFrame) frmName).setLocation((scr.width - fr.width) / 2, (scr.height - fr.height) / 2);
		} else if (frmName instanceof javax.swing.JDialog) {
			Dimension fr = ((javax.swing.JDialog) frmName).getSize();
			((javax.swing.JDialog) frmName).setLocation((scr.width - fr.width) / 2, (scr.height - fr.height) / 2);
		}
	}

	public static void doShowMessage(String pMsg) {
		javax.swing.JOptionPane.showMessageDialog(null, pMsg);
	}
	
	public static void trimStringInArray(String[] str) {
		//trim ทั้งหมดก่อนส่ง
		int strlen = str.length;
		for (int n1 = 0; n1 < strlen; n1++) {
			str[n1] = str[n1].trim();
		}
	}

}

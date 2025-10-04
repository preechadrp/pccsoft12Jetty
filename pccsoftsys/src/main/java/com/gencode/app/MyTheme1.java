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

import java.awt.Font;
import java.io.InputStream;

import javax.swing.UIManager;

public class MyTheme1 {

	private static String fontName = "/com/gencode/font/tahoma.ttf";
	private static Float appDefaultFontSize = 13f;
	public static Font appDefaultFont = null;

	public static void setTheme(String iLookAndFeelName) {
		try {

			/*
			 * == standard == com.sun.java.swing.plaf.gtk.GTKLookAndFeel
			 * com.sun.java.swing.plaf.motif.MotifLookAndFeel
			 * com.sun.java.swing.plaf.windows.WindowsLookAndFeel
			 * javax.swing.plaf.metal.MetalLookAndFeel
			 * 
			 * == jgoodies == com.jgoodies.looks.plastic.Plastic3DLookAndFeel
			 * com.jgoodies.looks.plastic.PlasticXPLookAndFeel
			 */
			UIManager.setLookAndFeel(iLookAndFeelName);

			loadFontStream();
			if (appDefaultFont != null) {
				java.util.Enumeration<?> keys = UIManager.getDefaults().keys();
				while (keys.hasMoreElements()) {
					Object key = keys.nextElement();
					Object value = UIManager.get(key);
					if (value instanceof javax.swing.plaf.FontUIResource)
						UIManager.put(key, appDefaultFont);
				}
			}
			// set border of windows to select LookAndFeel Style
			javax.swing.JFrame.setDefaultLookAndFeelDecorated(true);
			javax.swing.JDialog.setDefaultLookAndFeelDecorated(true);

		} catch (Exception e) {
			// nothing
		}
	}

	public static void loadFontStream() {
		// http://java.sun.com/docs/books/tutorial/2d/text/fonts.html
		try (InputStream fontStream = MyTheme1.class.getResourceAsStream(fontName);) {
			appDefaultFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
			appDefaultFont = appDefaultFont.deriveFont(Font.PLAIN, appDefaultFontSize);
		} catch (Exception e) {
			appDefaultFont = null;
		}
	}
}

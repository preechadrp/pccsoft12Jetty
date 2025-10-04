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

/**
 *
 * @author peepo
 */
import java.awt.Font;
import java.io.InputStream;

import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;

public class MyTheme2 extends DefaultMetalTheme {

	private static final String fontName = "/com/gencode/font/tahoma.ttf";
	private Font appDefaultFont = null;

	public static void setTheme() {
		javax.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme(new MyTheme2());
	}

	public MyTheme2() {
		//javax.swing.JFrame.setDefaultLookAndFeelDecorated(true);
		//javax.swing.JDialog.setDefaultLookAndFeelDecorated(true);
	}

	private Font loadFontStream() {
		Font font = null;
		try {
			if (appDefaultFont == null) {
				// InputStream fontStream =
				// Toolkit.getDefaultToolkit().getClass().getResourceAsStream(fontName);
				try (InputStream fontStream = MyTheme2.class.getResourceAsStream(fontName);) {
					appDefaultFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
				}
			}
			font = appDefaultFont;
		} catch (Exception e) {
		}
		return font;
	}

	public String getName() {
		return "My Theme";
	}

	public FontUIResource getControlTextFont() {
		//return new FontUIResource("Tahoma", Font.PLAIN, 13); //ok
		return new FontUIResource(loadFontStream().deriveFont(Font.PLAIN, 13));
	}

	public FontUIResource getSystemTextFont() {
		//return new FontUIResource("Tahoma", Font.PLAIN, 13);
		return new FontUIResource(loadFontStream().deriveFont(Font.PLAIN, 13));
	}

	public FontUIResource getUserTextFont() {
		//return new FontUIResource("Tahoma", Font.PLAIN, 13);
		return new FontUIResource(loadFontStream().deriveFont(Font.PLAIN, 13));
	}

	public FontUIResource getMenuTextFont() {
		//return new FontUIResource("Tahoma", Font.PLAIN, 13);
		return new FontUIResource(loadFontStream().deriveFont(Font.PLAIN, 13));
	}

	public FontUIResource getWindowTitleFont() {
		//return new FontUIResource("Tahoma", Font.BOLD, 14);
		return new FontUIResource(loadFontStream().deriveFont(Font.BOLD, 14));
	}

	public FontUIResource getSubTextFont() {
		//return new FontUIResource("Tahoma", Font.PLAIN, 13);
		return new FontUIResource(loadFontStream().deriveFont(Font.PLAIN, 13));
	}

}

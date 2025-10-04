package com.pcc.sys.lib;

import java.math.BigDecimal;

import org.zkoss.zul.Decimalbox;

/**
 * สำหรับการ input ตัวเลขที่ต้องการให้ตัดเลขทศนิยมให้เหลือ 2 ตำแหน่งเท่านั้น
 * @author preecha.d
 *
 */
public class MyDecimalbox extends Decimalbox {

	private static final long serialVersionUID = 1L;

	/** Creates a new instance of NimDecimalbox */
	public MyDecimalbox() {
		setFormat("###,##0.00");
		setStyle("text-align:right");
	}

	/**
	 * ดึงเอาตัวเลขตัดเครื่องหมายคอมม่า (,) ออก
	 * @return
	 */
	public String getNumberString() {
		String s1 = this.getText();
		if (s1 == null || s1.equals("")) {
			s1 = "0";
		}
		return s1.replace(",", "");
	}

	/**
	 * ดึงเอาค่าที่แสดงที่เรามองเห็นจริง
	 * @return
	 */
	public BigDecimal getValue() {
		BigDecimal res = new BigDecimal(getNumberString());
		return res;
	}

}

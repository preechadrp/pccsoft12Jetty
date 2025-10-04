package com.pcc.sys.lib;

/**
 * สำหรับ dynamic report ช่วยกรณี Export ออกเป็น Excel  (ใช้เก็บความกว้างทั้งหมดของคอลัมภ์ เพื่อกำหนดความกว้างของกระดาษ)
 * @author preecha daroonpunth
 *
 */
public class RepWidthKeep {

	int sumWith = 0;

	public int add(int w) {
		this.sumWith += w;
		return w;
	}

	public int getSumWidth() {
		return this.sumWith;
	}

	public void clear() {
		this.sumWith = 0;
	}

}

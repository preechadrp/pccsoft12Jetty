package com.pcc.sys.lib;

/*
 * ฟังก์ชั่น Utilities ทั่วไปที่ไม่เชื่อมต่อกับฐานข้อมูล   
 * By Mr.Preecha
 */
public class FDbUtil {

	/**
	 * หาจำนวนหน้า
	 * @param totalRow จำนวนรายการทั้งหมด
	 * @param rowPerPage จำนวนแถวต่อหน้า
	 * @return
	 */
	public static Integer getTotalPage(Integer totalRow, Integer rowPerPage) {
		int nP1 = Math.round(totalRow / rowPerPage); //จำนวนหน้าแบบเต็ม
		int totalPage = nP1;
		nP1 = totalRow % rowPerPage; //หาเศษ
		if (nP1 > 0) {
			totalPage += 1;
		}
		return totalPage;
	}

	/**
	 * หาลำดับเริ่มอ่าน Record
	 * @param totalRow
	 * @param rowPerPage
	 * @param requestPage
	 * @return
	 */
	public static Integer getStartRow(int totalRow, int rowPerPage, int requestPage) {
		int startRow = 0;
		if (requestPage > 1) {
			startRow = (rowPerPage * (requestPage - 1)) + 1;
			if (startRow > totalRow) {
				startRow = totalRow;
			}
		} else {
			startRow = 1;
		}
		return startRow;
	}

}

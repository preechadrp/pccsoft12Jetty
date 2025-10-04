package com.pcc.sys.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FExcel2007FileLoader {

	private XSSFWorkbook book;
	private InputStream inst;

	public FExcel2007FileLoader() {
		//nothing
	}

	public void open(File f) throws IOException {
		inst = new FileInputStream(f);
		book = new XSSFWorkbook(inst);
	}

	public void open(InputStream f) throws IOException {
		book = new XSSFWorkbook(f);
	}

	public void close() throws IOException {
		if (inst != null) {
			inst.close();
		}
	}

	/**
	 * อ่าน xlsx
	 * @param sheetName ชื่อ sheet หากไม่พบตามชื่อจะเอา sheet แรก โดยเริ่มนับ sheet จาก 0
	 * @param beginRow เริ่มนับจาก 0
	 * @param fixReadToColumn เริ่มนับจาก 1 ถ้าระบุเป็น 0 จะหาจำนวน column เอง
	 * @return
	 */
	public List<Properties> read(String sheetName, int beginRow, int fixReadToColumn) {

		XSSFSheet sheet = book.getSheet(sheetName);
		if (sheet == null) {
			sheet = book.getSheetAt(0);
		}

		List<Properties> list = new ArrayList<Properties>();
		Object[] order = new Object[0];

		if (sheet == null) {
			throw new NullPointerException("Sheet not found.");
		}

		XSSFRow first = sheet.getRow(beginRow);
		//== get Column and count Column
		for (int n1 = 0; n1 <= first.getLastCellNum(); n1++) {
			XSSFCell cell = first.getCell(n1);
			if (fixReadToColumn > 0) {
				if (n1 >= fixReadToColumn) {
					break;
				}
			} else {
				if (cell == null) {
					break;
				}
			}
			Object[] temp = new Object[n1 + 1];
			System.arraycopy(order, 0, temp, 0, n1);
			order = temp;
			order[n1] = "cell_" + n1;
		}

		//== get Row and count Row
		for (int i = (beginRow); i <= sheet.getLastRowNum(); i++) {
			XSSFRow row = sheet.getRow(i);
			if (row == null)
				break;
			Properties data = new Properties();
			for (int n1 = 0; n1 < order.length; n1++) {
				XSSFCell cell = row.getCell(n1);
				if (cell == null)
					continue;
				if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
					data.put(order[n1], cell.getStringCellValue());
				} else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
					double db = cell.getNumericCellValue();
					String value = db % 1 == 0 ? "" + (long) db : "" + db;
					data.put(order[n1], value);
				} else if (cell.getCellType() == XSSFCell.CELL_TYPE_FORMULA) {
					double db = cell.getNumericCellValue();
					String value = db % 1 == 0 ? "" + (long) db : "" + db;
					data.put(order[n1], value);
				}
			}
			if (data.size() < 1) {
				continue;
			}
			list.add(data);
		}

		return list;

	}

	public static void main(String[] args) throws Exception {
		FExcel2007FileLoader efl = new FExcel2007FileLoader();
		File f = new File("C:/test/testFile.xlsx");
		efl.open(f);
		java.util.List<Properties> data = efl.read("Sheet1", 0, 3);
		efl.close();
		//System.out.println("data.size() : " + data.size());
		//int readLine = 2;
		//int lineConfig = 2;
		for (Properties p : data) {
			p.list(System.out);
		}
	}

}

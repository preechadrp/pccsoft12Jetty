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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GenTbo {

	public String getCodeStringBuffer(Map<String, Object> hashMap) {

		String res = "";
		res += getPackage(hashMap);
		res += getImportClass();
		res += getCodeString(hashMap);

		return res;
	}

	private String getPackage(Map<String, Object> hashMap) {
		return "package " + (String) hashMap.get("@daoPackageName") + ";\n";
	}

	public String getImportClass() {
		StringBuffer sb = new StringBuffer();
		sb.append("\nimport java.math.BigDecimal;");
		sb.append("\nimport java.sql.Date;");
		sb.append("\nimport java.sql.Timestamp;");
		sb.append("\nimport com.pcc.sys.tbo.TABLE;");
		return sb.toString();
	}

	public String getCodeString(Map<String, Object> hashMap) {

		StringBuffer sbCode = new StringBuffer();
		try (Connection conn = DbHelper.getConnection();) {

			StringBuffer psStringBuffer = new StringBuffer();
			String tableName = (String) hashMap.get("@tableName");
			psStringBuffer.append("SELECT * FROM ").append(tableName);
			PreparedStatement ps = conn.prepareStatement(psStringBuffer.toString());
			ps.setMaxRows(1);
			ResultSet rs = ps.executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();

			List<String> columnCLassName1 = new ArrayList<String>();//columnName colunmClass
			int[] columnSize1 = new int[columnCount];

			for (int y = 1; y <= columnCount; y++) {

				//===หา columnSize1
				if (rsmd.getColumnClassName(y).equals("java.lang.String")) {
					int size1 = rsmd.getColumnDisplaySize(y);
					columnSize1[y - 1] = size1;
					System.out.println(size1);
				}

				//===หา columnCLassName1
				String columnClassName = rsmd.getColumnClassName(y) + ":" + rsmd.getColumnName(y);
				columnCLassName1.add(columnClassName);
				//for test
//				System.out.println(" rsmd.getColumnName(y) :" + rsmd.getColumnName(y));
//				System.out.println(" rsmd.getColumnClassName(y) :" + rsmd.getColumnClassName(y));
//				System.out.println(" rsmd.getColumnType(y) :" + rsmd.getColumnType(y));
//				System.out.println(" rsmd.getColumnTypeName(y) :" + rsmd.getColumnTypeName(y));
//				if (rsmd.getColumnName(y).equals("NameImage")) {
//					System.out.println(rsmd.getColumnName(y));
//				}

			}

			String classNameM = ((String) hashMap.get("@daoClassName"));//ชื่อ Class เป็นตัวใหญ่

			sbCode.append("\npublic class ").append(classNameM).append(" extends TABLE{\n");

			//loop ประกาศตัวแปรฟิลด์
			for (int r = 0; r < columnCLassName1.size(); r++) {
				String data = columnCLassName1.get(r);
				data = data.replace(".", ":");
				String[] splitClassName = data.split(":");

//				String coName = rsmd.getColumnName(r + 1); //for test
//				if (coName.equals("NameImage")) {
//					System.out.println(coName);
//				}

				String v3 = "";
				if (splitClassName.length == 2) {
					v3 = "Blob";
				} else {
					v3 = splitClassName[2];
				}

				if (v3.equals("Integer")) {
					v3 = "int";
				}
				if (v3.equals("Blob")) {
					v3 = "byte[]";
				}
				if (v3.equals("Integer") || v3.equals("int")) {
					sbCode.append("\n\tprivate ").append(v3).append(" ").append(rsmd.getColumnName(r + 1).toUpperCase()).append(" = 0;");
				} else if (v3.equals("String")) {
					sbCode.append("\n\tprivate ").append(v3).append(" ").append(rsmd.getColumnName(r + 1).toUpperCase()).append(";");
				} else if (v3.equals("BigDecimal")) {
					sbCode.append("\n\tprivate ").append(v3).append(" ").append(rsmd.getColumnName(r + 1).toUpperCase()).append(" = BigDecimal.ZERO;");
				} else {
					sbCode.append("\n\tprivate ").append(v3).append(" ").append(rsmd.getColumnName(r + 1).toUpperCase()).append(";");
				}

			}

			sbCode.append("\n");
			sbCode.append("\n\tpublic static final String tablename=\"").append(tableName).append("\";");
			
			sbCode.append("\n\tpublic String getTableName() {");
			sbCode.append("\n\t return schema+tablename;");
			sbCode.append("\n\t}");

			sbCode.append("\n\tpublic").append(" ").append((String) hashMap.get("@daoClassName")).append("(){");
			sbCode.append("\n\t	super();");
			sbCode.append("\n\t}");

			//=== loop สร้าง setter , getter
			for (int r = 0; r < columnCLassName1.size(); r++) {
				String data = columnCLassName1.get(r);
				data = data.replace(".", ":");
				String[] splitClassName = data.split(":");

				//String v3 = splitClassName[2];
				String v3 = "";
				if (splitClassName.length == 2) {
					v3 = "Blob";
				} else {
					v3 = splitClassName[2];
				}

				if (v3.equals("Integer")) {
					v3 = "int";
				}
				if (v3.equals("Blob")) {
					v3 = "byte[]";
				}

				String v4 = "";
				if (splitClassName.length == 2) {
					v4 = splitClassName[1];
				} else {
					v4 = splitClassName[3];
				}

				sbCode.append("\n\tpublic").append(" ").append(v3).append(" ").append("get").append(v4.toUpperCase()).append("() {");
				sbCode.append("\n\t return").append(" this.").append(v4.toUpperCase()).append(";");
				sbCode.append("\n\t}");
				sbCode.append("\n\tpublic void set").append(v4.toUpperCase()).append("(").append(v3).append(" ").append(v4.toLowerCase()).append(") throws Exception {");
				if (v3.equals("String")) {
					sbCode.append("\n\t if (getStr(" + v4.toLowerCase() + ").length() > " + columnSize1[r] + ") { ");
					sbCode.append("\n\t   throw new Exception(\"ฟิลด์ " + tableName.toUpperCase() + "." + v4.toUpperCase() + " ข้อมูลยาวเกิน " + columnSize1[r] + " ตัวอักษร ตัวอย่างข้อมูล : \\n \" +" + v4.toLowerCase() + "+\" \"); ");
					sbCode.append("\n\t }");
				}
				sbCode.append("\n\t this.").append(v4.toUpperCase()).append("=").append(v4.toLowerCase()).append(";");
				sbCode.append("\n\t}");

			}

			sbCode.append("\n}");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sbCode.toString();
	}

//	public static void main(String[] args) {
//		GenTbo createFacade = new GenTbo();
//		Map<String, Object> hashMap = new HashMap<String, Object>();
//		hashMap.put("@tableName", "FUSER");
//		hashMap.put("@daoClassName", "TboFUSER");
//		hashMap.put("@daoPackageName", "pcc.bus.tbo");
//		String ss = createFacade.getCodeStringBuffer(hashMap);
//		System.out.println(ss);
//	}

}

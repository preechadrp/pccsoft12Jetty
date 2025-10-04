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

public class GenModelBySql {

//	public static void main(String[] args) {
//		Map<String, Object> hashMap = new LinkedHashMap<String, Object>();
//		hashMap.put("@sql", "select * from ACCT_DETAIL");
//		hashMap.put("@daoClassName", "ACCT_DETAIL_BEANS");
//
//		GenModelBySql cr = new GenModelBySql();
//		StringBuffer sb = cr.getCodeStringBuffer(hashMap);
//		System.out.println(new String(sb));
//	}

	public StringBuffer getCodeStringBuffer(Map<String, Object> hashMap) {
		StringBuffer sb = new StringBuffer();

		sb.append("\nimport java.math.BigDecimal;");
		sb.append("\nimport java.sql.Date;\n");

		try (Connection conn = DbHelper.getConnection();) {

			StringBuffer psStringBuffer = new StringBuffer();
			String sql1 = (String) hashMap.get("@sql");
			psStringBuffer.append(sql1);
			PreparedStatement ps = conn.prepareStatement(psStringBuffer.toString());
			ResultSet rs = ps.executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			List<String> columnNameNew = new ArrayList<String>();//columnName
			List<String> columnCLassNameNew = new ArrayList<String>();//columnName colunmClass
			List<String> className = new ArrayList<String>(); //columnClassNew
			String columnClassName = "";
			for (int t = 1; t <= columnCount; t++) {
				String columnName = rsmd.getColumnName(t);
				columnNameNew.add(columnName.toUpperCase());
			}

			for (int y = 1; y <= columnCount; y++) {
				columnClassName = rsmd.getColumnClassName(y) + ":" + rsmd.getColumnName(y);
				columnCLassNameNew.add(columnClassName);
			}
			sb = new StringBuffer("\npublic class ").append((String) hashMap.get("@daoClassName")).append(" {\n");
			for (int r = 0; r < columnCLassNameNew.size(); r++) {
				String data = columnCLassNameNew.get(r);
				data = data.replace(".", ":");
				String[] splitClassName = data.split(":");

				String v3 = splitClassName[2];
				if (v3.equals("Integer")) {
					v3 = "Integer";
				}
				if (v3.equals("Blob")) {
					v3 = "byte[]";
				}
				if (v3.equals("Integer")) {
					sb.append("\n\tprivate ").append(v3).append(" ").append(rsmd.getColumnName(r + 1)).append(" = 0;");
				} else if (v3.equals("String")) {
					sb.append("\n\tprivate ").append(v3).append(" ").append(rsmd.getColumnName(r + 1)).append(" = \"\";");
				} else if (v3.equals("BigDecimal")) {
					sb.append("\n\tprivate ").append(v3).append(" ").append(rsmd.getColumnName(r + 1)).append(" = BigDecimal.ZERO;");
				} else {
					sb.append("\n\tprivate ").append(v3).append(" ").append(rsmd.getColumnName(r + 1)).append(";");
				}
				className.add(v3);

			}

			sb.append("\n");

			for (int r = 0; r < columnCLassNameNew.size(); r++) {
				String data = columnCLassNameNew.get(r);
				data = data.replace(".", ":");
				String[] splitClassName = data.split(":");

				String v3 = splitClassName[2];
				String v4 = splitClassName[3];
				if (v3.equals("Integer")) {
					v3 = "Integer";
				}
				if (v3.equals("Blob")) {
					v3 = "byte[]";
				}
				sb.append("\n\tpublic").append(" ").append(v3).append(" ").append("get").append(v4).append("() {");
				sb.append("\n\t return").append(" this.").append(v4).append(";");
				sb.append("\n\t}");
				sb.append("\n\tpublic void set").append(v4).append("(").append(v3).append(" ").append(v4.toLowerCase()).append("){");
				sb.append("\n\t this.").append(v4).append("=").append(v4.toLowerCase()).append(";");
				sb.append("\n\t}");

			}
			sb.append("\n}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb;
	}

}

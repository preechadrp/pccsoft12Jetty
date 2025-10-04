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

public class SetModelBySql {

//	public static void main(String[] args) {
//
//		Map<String, Object> hashMap = new LinkedHashMap<String, Object>();
//		hashMap.put("@sql", "SELECT *  from HPENFNOTE");
//		hashMap.put("@daoClassName", "HPENFNOTE_BEAN");
//
//		SetModelBySql cr = new SetModelBySql();
//		StringBuffer sb = cr.getCodeStringBuffer(hashMap);
//		System.out.println(new String(sb));
//	}

	public StringBuffer getCodeStringBuffer(Map<String, Object> hashMap) {

		StringBuffer sb = new StringBuffer();
		try (Connection conn = DbHelper.getConnection();) {

			StringBuffer psStringBuffer = new StringBuffer();
			String sql1 = (String) hashMap.get("@sql");
			String className1 = (String) hashMap.get("@daoClassName");
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

			sb.append(className1).append(" beans = new ").append(className1).append("();\n");
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
					sb.append("beans.set").append(rsmd.getColumnName(r + 1)).append("(rs.getInt(\"").append(rsmd.getColumnName(r + 1)).append("\"));\n");
				} else if (v3.equals("String")) {
					sb.append("beans.set").append(rsmd.getColumnName(r + 1)).append("(rs.getString(\"").append(rsmd.getColumnName(r + 1)).append("\"));\n");
				} else if (v3.equals("BigDecimal")) {
					sb.append("beans.set").append(rsmd.getColumnName(r + 1)).append("(rs.getBigDecimal(\"").append(rsmd.getColumnName(r + 1)).append("\"));\n");
				} else {
					sb.append("beans.set").append(rsmd.getColumnName(r + 1)).append("(rs.get" + v3 + "(\"").append(rsmd.getColumnName(r + 1)).append("\"));\n");
				}
				className.add(v3);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb;
	}

}

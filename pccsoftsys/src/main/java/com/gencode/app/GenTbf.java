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

public class GenTbf {

	private String tableName = null;
	private String daoClassName = null;
	private String daoPackageName = null;
	private String[] pkName = null;

	public String[] getPkName() {
		return pkName;
	}

	public void setPkName(String[] pkName) {
		this.pkName = pkName;
	}

	public String getDaoClassName() {
		return daoClassName;
	}

	public void setDaoClassName(String daoClassName) {
		this.daoClassName = daoClassName;
	}

	public String getDaoPackageName() {
		return daoPackageName;
	}

	public void setDaoPackageName(String daoPackageName) {
		this.daoPackageName = daoPackageName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getCodeStringBuffer(Map<String, Object> hashMap) {
		this.setPkName((String[]) hashMap.get("@daoPKName"));
		this.setTableName((String) hashMap.get("@tableName"));
		this.setDaoPackageName((String) hashMap.get("@daoPackageName"));
		this.setDaoClassName((String) hashMap.get("@daoClassName"));

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
		sb.append("import java.sql.PreparedStatement;\n");
		sb.append("import java.sql.ResultSet;\n");
		sb.append("import java.sql.SQLException;\n");
		sb.append("import com.pcc.sys.lib.FDbc;\n");
		sb.append("import com.pcc.sys.lib.Fnc;\n");
		return sb.toString();

	}

	public String getCodeString(Map<String, Object> hashMap) {

		String sTmp1 = "";
		FSqlStr sb = new FSqlStr(); // extends DAOAbstract {");
		try (Connection conn = DbHelper.getConnection();) {

			StringBuffer psStringBuffer = new StringBuffer();
			String tableName = (String) hashMap.get("@tableName");
			psStringBuffer.append("SELECT * FROM ").append("").append(tableName);
			PreparedStatement ps = conn.prepareStatement(psStringBuffer.toString());
			ps.setMaxRows(1);
			ResultSet rs = ps.executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			String[] pkName = (String[]) hashMap.get("@daoPKName");
			List<String> columnNameNew = new ArrayList<String>();
			List<String> columnCLassNameNew = new ArrayList<String>();
			List<String> className = new ArrayList<String>();
			List<String> classNameUpdate = new ArrayList<String>();
			List<String> classNameTemp = new ArrayList<String>();

			String columnClassName = "";
			for (int t = 1; t <= columnCount; t++) {
				String columnName = rsmd.getColumnName(t);
				columnNameNew.add(columnName.toUpperCase());
				classNameUpdate.add(columnName.toUpperCase());
			}

			for (int i = 0; i < columnNameNew.size(); i++) {
				for (int j = 0; j < pkName.length; j++) {
					if (columnNameNew.get(i).toUpperCase().equalsIgnoreCase(pkName[j].toUpperCase())) {
						columnNameNew.remove(i);
						classNameUpdate.remove(i);
						--i;
						break;
					}
				}
			}

			for (int j = 0; j < pkName.length; j++) {
				columnNameNew.add(pkName[j].toUpperCase());
			}

			for (int y = 1; y <= columnCount; y++) {
				columnClassName = rsmd.getColumnClassName(y) + ":" + rsmd.getColumnName(y);
				columnCLassNameNew.add(columnClassName);
			}

			for (int r = 0; r < columnCLassNameNew.size(); r++) {
				String data = columnCLassNameNew.get(r);
				data = data.replace(".", ":");
				String[] splitClassName = data.split(":");

				String v3 = "";
				if (splitClassName.length == 2) {
					v3 = "Blob";
				} else {
					v3 = splitClassName[2];
				}
				//String v3 = splitClassName[2];
				if (v3.equals("Integer")) {
					v3 = "Int";
				}
				if (v3.equals("Blob")) {
					v3 = "Bytes";
				}
				className.add(v3);
			}

			int check = 0;
			for (int u = 0; u < className.size(); u++) {
				String data = columnCLassNameNew.get(check);
				data = data.replace(".", ":");
				String[] splitClassName = data.split(":");

				//String v3 = splitClassName[2];//Data Type
				String v3 = "";
				if (splitClassName.length == 2) {
					v3 = "Blob";
				} else {
					v3 = splitClassName[2];
				}
				//String v4 = splitClassName[3];//Field Name
				String v4 = "";
				if (splitClassName.length == 2) {
					v4 = splitClassName[1];
				} else {
					v4 = splitClassName[3];
				}
				for (int j = 0; j < pkName.length; j++) {
					if (v4.toUpperCase().equalsIgnoreCase(pkName[j].toUpperCase())) {
						className.remove(u);
						if (v3.equals("Integer")) {
							v3 = "Int";
						}
						if (v3.equals("Blob")) {
							v3 = "Bytes";
						}
						classNameTemp.add(v3);
						u--;
						break;
					}
				}
				check++;
			}

			for (int l = 0; l < classNameTemp.size(); l++) {
				className.add(classNameTemp.get(l));
			}

//			String daoDefaultDB = "";
//			if (hashMap.get("@daoDefaultDB") != null) {
//				daoDefaultDB = ((String) hashMap.get("@daoDefaultDB")).trim();
//			}

			String classNameM = ((String) hashMap.get("@daoClassName"));//ชื่อ class เป็นตัวใหญ่
			sb.append("public class " + classNameM + "{");

			//==== public static boolean insert(..)
			sb.append("public static boolean insert(Tbo" + (String) hashMap.get("@tableName") + " model) throws SQLException, Exception{");
			sb.append("   try (FDbc dbc = FDbc.connectMasterDb()) {");
			sb.append("      return insert(dbc, model);");
			sb.append("   }");
			sb.append("}");

			//===== public static boolean insert(FDbc,...)
			sb.append("public static boolean insert(FDbc dbc,Tbo" + (String) hashMap.get("@tableName") + " model) throws SQLException, Exception{");
			sb.append("try(PreparedStatement pstm = getPrepareStmInsert(dbc);){");
			sb.append("  setPrepareStm(pstm, model);");
			sb.append("  int ins = pstm.executeUpdate();");
			sb.append("  if (ins >= 1) {");
			sb.append("	 return true;");
			sb.append("  } else {");
			sb.append("	 return false;");
			sb.append("  }");
			sb.append(" }");
			sb.append("}");
			
			//===== public static boolean update(..) no fixwhere
			sb.append(" public static boolean update(Tbo" + (String) hashMap.get("@tableName") + " model) throws SQLException, Exception{");
			sb.append("   try (FDbc dbc = FDbc.connectMasterDb()) {");
			sb.append("      return update(dbc, model);");
			sb.append("   }");
			sb.append(" }");

			//====== public static boolean update(FDbc ,...) no fixwhere
			sb.append(" public static boolean update(FDbc dbc,Tbo" + (String) hashMap.get("@tableName") + " model) throws SQLException, Exception{");
			sb.append("   try(PreparedStatement pstm = getPrepareStmUpdate(dbc,\"\");){");
			sb.append("     setPrepareStm(pstm, model);");
			sb.append("     int ins = pstm.executeUpdate();");
			sb.append("     if (ins >= 1) {");
			sb.append("       return true;");
			sb.append("     } else {");
			sb.append("       return false;");
			sb.append("     }");
			sb.append("   }");
			sb.append("}");

			//===== public static boolean update(..)
			sb.append(" public static boolean update(Tbo" + (String) hashMap.get("@tableName") + " model,String fixWhere) throws SQLException, Exception{");
			sb.append("   try (FDbc dbc = FDbc.connectMasterDb()) {");
			sb.append("      return update(dbc, model,fixWhere);");
			sb.append("   }");
			sb.append(" }");

			//====== public static boolean update(FDbc ,...)
			sb.append(" public static boolean update(FDbc dbc,Tbo" + (String) hashMap.get("@tableName") + " model,String fixWhere) throws SQLException, Exception{");
			sb.append("   try(PreparedStatement pstm = getPrepareStmUpdate(dbc,fixWhere);){");
			sb.append("     setPrepareStm(pstm, model);");
			sb.append("     int ins = pstm.executeUpdate();");
			sb.append("     if (ins >= 1) {");
			sb.append("       return true;");
			sb.append("     } else {");
			sb.append("       return false;");
			sb.append("     }");
			sb.append("   }");
			sb.append("}");

			//==== public static boolean delete(..)
			sb.append(" public static boolean delete(Tbo" + (String) hashMap.get("@tableName") + " model) throws SQLException, Exception{");
			sb.append("   try (FDbc dbc = FDbc.connectMasterDb()) {");
			sb.append("     return delete(dbc, model);");
			sb.append("   }");
			sb.append(" }");

			//==== public static boolean delete(FDbc,..)
			sb.append(" public static boolean delete(FDbc dbc,Tbo" + (String) hashMap.get("@tableName") + " model) throws SQLException, Exception{");
			sb.append("  try(PreparedStatement pstm = getPrepareStmDelete(dbc);){");
			for (int t = 0; t < columnCount; t++) {
				for (int p = 0; p < pkName.length; p++) {
					if (columnNameNew.get(t).equals(pkName[p])) {
//						if (className.get(t).equals("Date")) {
//							sb.append(" 	Tbf.setDate(" + (p + 1) + ",pstm, model.get" + columnNameNew.get(t) + "());");
//						} else if (className.get(t).equals("Timestamp")) {
//							sb.append(" 	Tbf.setDateTime(" + (p + 1) + ",pstm, model.get" + columnNameNew.get(t) + "());");
//						} else {
//							sb.append(" 	pstm.set" + className.get(t) + "(" + (p + 1) + "," + "model.get" + columnNameNew.get(t) + "());");
//						}
						sb.append(" 	pstm.set" + className.get(t) + "(" + (p + 1) + "," + "model.get" + columnNameNew.get(t) + "());");//แก้ปัญหาโดยเมื่อเริ่มต้นโปรเจคให้ Locale.setDefault(Locale.ENGLISH);
					}
				}
			}
			sb.append("   int del = pstm.executeUpdate();");
			sb.append("   if (del >= 1) {");
			sb.append("  	  return true;");
			sb.append("   } else {");
			sb.append("	  return false;");
			sb.append("   }");
			sb.append("  }");
			sb.append(" }");

			//==== public static boolean find(..)
			sb.append(" public static boolean find(Tbo" + (String) hashMap.get("@tableName") + " model) throws SQLException, Exception {");
			sb.append("   try (FDbc dbc = FDbc.connectMasterDb()) {");
			sb.append("      return find(dbc,model);");
			sb.append("   } ");
			sb.append("} ");

			//==== public static boolean find(FDbc db,..)
			sb.append(" public static boolean find(FDbc dbc,Tbo" + (String) hashMap.get("@tableName") + " model) throws SQLException, Exception {");
			sb.append("   try(PreparedStatement pstm = getPrepareStmFind(dbc);) {");
			for (int t = 0; t < columnCount; t++) {
				for (int p = 0; p < pkName.length; p++) {
					if (columnNameNew.get(t).equals(pkName[p])) {
//						if (className.get(t).equals("Date")) {
//							sb.append("  Tbf.setDate(" + (p + 1) + ",pstm, model.get" + columnNameNew.get(t) + "());");
//						} else if (className.get(t).equals("Timestamp")) {
//							sb.append("  Tbf.setDateTime(" + (p + 1) + ",pstm, model.get" + columnNameNew.get(t) + "());");
//						} else {
//							sb.append("  pstm.set" + className.get(t) + "(" + (p + 1) + "," + "model.get" + columnNameNew.get(t) + "());");
//						}

						//แก้ปัญหาโดยเมื่อเริ่มต้นโปรเจคให้ Locale.setDefault(Locale.ENGLISH);
						sb.append("  pstm.set" + className.get(t) + "(" + (p + 1) + "," + "model.get" + columnNameNew.get(t) + "());");
					}
				}
			}
			sb.append("     try (ResultSet rs = pstm.executeQuery();){");
			sb.append("   	if (rs.next()) {");
			sb.append("   		setModel(rs, model);");
			sb.append("   		return true;");
			sb.append("   	}");
			sb.append("     }");
			sb.append("   }");
			sb.append("   return false;");
			sb.append("}");

			//==== public static PreparedStatement getPreparStmtInsert 
			sb.append(" public static PreparedStatement getPrepareStmInsert(FDbc dbc) throws SQLException, Exception{");
			sb.append(" StringBuffer sql = new StringBuffer();");
			sb.append(" sql.append(\" INSERT INTO \").append(Tbo" + (String) hashMap.get("@tableName") + ".tablename" + ")" + ".append(\"(\");");
			for (int i = 0; i < columnNameNew.size(); i++) {
				if (i != columnNameNew.size() - 1) {
					sb.append(" sql.append(\" " + columnNameNew.get(i) + ",\");");
				} else {
					sb.append(" sql.append(\" " + columnNameNew.get(i) + ")\");");
				}
			}
			sTmp1 = "";
			for (int p = 0; p < columnNameNew.size(); p++) {
				if (p != columnNameNew.size() - 1) {
					sTmp1 += "?,";
				} else {
					sTmp1 += "?)\");";
				}
			}
			sb.append(" sql.append(\" VALUES(" + sTmp1);
			sb.append(" return dbc.getPreparedStatement(sql.toString());");
			sb.append(" }");

			//==== public static PreparedStatement getPreparStmtUpdate 
			sb.append(" public static PreparedStatement getPrepareStmUpdate(FDbc dbc,String fixWhere) throws SQLException, Exception{");
			sb.append(" StringBuffer sql = new StringBuffer();");
			sb.append(" sql.append(\" UPDATE \").append(Tbo" + ((String) hashMap.get("@tableName") + ".tablename") + ").append(\" SET \");");
			for (int i = 0; i < classNameUpdate.size(); i++) {
				if (i != classNameUpdate.size() - 1) {
					sb.append(" sql.append(\" " + classNameUpdate.get(i) + "=?,\");");
				} else {
					sb.append(" sql.append(\" " + classNameUpdate.get(i) + "=?\");");
				}
			}
			sTmp1 = "";
			for (int p = 0; p < pkName.length; p++) {
				if (p != pkName.length - 1) {
					sTmp1 += pkName[p] + "=? AND ";
				} else {
					sTmp1 += pkName[p] + "=? \");";
				}
			}
			sb.append(" sql.append(\" WHERE " + sTmp1);
			sb.append("if (!Fnc.isEmpty(fixWhere)) {");
			sb.append("	sql.append(\" AND \" + fixWhere);");
			sb.append("}");
			sb.append(" return dbc.getPreparedStatement(sql.toString());");
			sb.append("}");

			//==== public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {
			sb.append("public static PreparedStatement getPrepareStmDelete(FDbc dbc) throws SQLException {");
			sb.append(" StringBuffer sql = new StringBuffer();");
			sb.append(" sql.append(\" DELETE FROM \").append(Tbo" + ((String) hashMap.get("@tableName") + ".tablename") + ");");
			sTmp1 = "";
			for (int p = 0; p < pkName.length; p++) {
				if (p != pkName.length - 1) {
					sTmp1 += pkName[p] + "=? AND ";
				} else {
					sTmp1 += pkName[p] + "=? \");";
				}
			}
			sb.append(" sql.append(\" WHERE " + sTmp1);
			sb.append(" return dbc.getPreparedStatement(sql.toString());");
			sb.append("}");

			//=== public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {
			sb.append("public static PreparedStatement getPrepareStmFind(FDbc dbc) throws SQLException {");
			sb.append("StringBuffer sql = new StringBuffer();");
			sb.append("sql.append(\" SELECT * FROM \").append(Tbo" + ((String) hashMap.get("@tableName") + ".tablename") + ");");
			sTmp1 = "";
			for (int p = 0; p < pkName.length; p++) {
				if (p != pkName.length - 1) {
					sTmp1 += pkName[p] + "=? AND ";
				} else {
					sTmp1 += pkName[p] + "=? \");";
				}
			}
			sb.append(" sql.append(\" WHERE " + sTmp1);
			sb.append("return dbc.getPreparedStatement(sql.toString());");
			sb.append("}");

			//==== public static  void setPrepareStm(PreparedStatement pstm,Medel model)
			sb.append(" public static void setPrepareStm(PreparedStatement pstm,Tbo" + (String) hashMap.get("@tableName") + " model) throws SQLException, Exception{");
			sb.append("	pstm.clearParameters(); ");
			for (int t = 0; t < columnCount; t++) {
//				if (className.get(t).equals("Date")) {
//					sb.append("  Tbf.setDate(" + (t + 1) + ",pstm, model.get" + columnNameNew.get(t) + "());");
//				} else if (className.get(t).equals("Timestamp")) {
//					sb.append("  Tbf.setDateTime(" + (t + 1) + ",pstm, model.get" + columnNameNew.get(t) + "());");
//				} else {
//					sb.append("  pstm.set" + className.get(t) + "(" + (t + 1) + "," + "model.get" + columnNameNew.get(t) + "());");
//				}
				sb.append("  pstm.set" + className.get(t) + "(" + (t + 1) + "," + "model.get" + columnNameNew.get(t) + "());");//แก้ปัญหาโดยเมื่อเริ่มต้นโปรเจคให้ Locale.setDefault(Locale.ENGLISH);
			}
			sb.append("  }");

			//==== public static void setModel(..)
			sb.append(" public static void setModel(ResultSet rs,Tbo" + (String) hashMap.get("@tableName") + " model) throws SQLException, Exception{");
			for (int t = 0; t < columnCount; t++) {
//				if (className.get(t).equals("Date")) {
//					sb.append(" model.set" + columnNameNew.get(t) + "(" + "rs.get" + className.get(t) + "(\"" + columnNameNew.get(t) + "\", Tbf.getCal()));");
//				} else if (className.get(t).equals("Timestamp")) {
//					sb.append(" model.set" + columnNameNew.get(t) + "(" + "rs.get" + className.get(t) + "(\"" + columnNameNew.get(t) + "\", Tbf.getCal()));");
//				} else {
//					sb.append(" model.set" + columnNameNew.get(t) + "(" + "rs.get" + className.get(t) + "(\"" + columnNameNew.get(t) + "\"));");
//				}
				sb.append(" model.set" + columnNameNew.get(t) + "(" + "rs.get" + className.get(t) + "(\"" + columnNameNew.get(t) + "\"));");//แก้ปัญหาโดยเมื่อเริ่มต้นโปรเจคให้ Locale.setDefault(Locale.ENGLISH);
			}
			sb.append("}");

			//== end class
			sb.append("}");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.getSql();
	}

//	public static void main(String[] args) {
//		GenTbf createFacade = new GenTbf();
//		Map<String, Object> hashMap = new HashMap<String, Object>();
//
//		String[] pkName = { "USER_ID" };//ระบุคีย์หลัก (Primary Key)
//		hashMap.put("@tableName", "FUSER");// ระบุชื่อตาราง
//		hashMap.put("@daoClassName", "TbfFUSER");// ระบุชื่อ Class
//		hashMap.put("@daoPackageName", "pcc.bus.tbf");//ระบุชื่อ Package
//		hashMap.put("@daoPKName", pkName);
//		String sb = createFacade.getCodeStringBuffer(hashMap);
//
//		System.out.println(sb);
//	}

}

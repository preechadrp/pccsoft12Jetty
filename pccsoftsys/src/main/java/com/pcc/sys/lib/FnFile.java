package com.pcc.sys.lib;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FnFile {

	public static String loadTextFile(String file, boolean withCarriageReturn) throws Exception {
		String text = "";
		try (var inst1 = FnFile.class.getResourceAsStream(file);
				var isr1 = new InputStreamReader(inst1, "UTF-8");
				var br1 = new BufferedReader(isr1);) {

			String strLine = "";
			while ((strLine = br1.readLine()) != null) {
				if (withCarriageReturn) {
					text += strLine + "\n";
				} else {
					text += strLine;
				}
			}
			return text;
		} catch (Exception e) {
			throw e;
		}
	}

	public static void loadTextFileToList(String file, java.util.List<String> lstData) throws Exception {

		lstData.clear();

		try (
				var inst1 = FnFile.class.getResourceAsStream(file);
				var isr1 = new InputStreamReader(inst1, "UTF-8");
				var br1 = new BufferedReader(isr1);) {

			String strLine = "";
			while ((strLine = br1.readLine()) != null) {
				lstData.add(strLine);
			}
		} catch (Exception e) {
			throw e;
		}

	}

	public static String loadSqlFile(String file) throws Exception {

		String text = "";
		try (
				var inst1 = FnFile.class.getResourceAsStream(file);
				var isr1 = new InputStreamReader(inst1, "UTF-8");
				var br1 = new BufferedReader(isr1);) {

			String strLine = "";
			while ((strLine = br1.readLine()) != null) {
				if (strLine.trim().startsWith("--")) {
					//..
				} else {
					text += strLine + "\n";
				}
			}
			return text;
		} catch (Exception e) {
			throw e;
		}

	}
	
	/**
	 * หารายการไฟล์ทั้งหมดใน Folder ที่ต้องการ
	 * @param folder
	 * @param lst_file
	 */
	public static void listFilesForFolder(final java.io.File folder, java.util.List<java.io.File> lst_file) {

		for (final java.io.File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry, lst_file);
			} else {
				lst_file.add(fileEntry);
			}
		}

	}
	
	public static void main(String[] arg) {

		//TEST listFilesForFolder
		java.util.List<java.io.File> lst_file = new ArrayList<>();
		final java.io.File folder = new java.io.File("D:/ShkSoftV2/ShkCore/WebContent/HP");
		listFilesForFolder(folder, lst_file);
		for (java.io.File file : lst_file) {
			System.out.print(file.getAbsoluteFile() + " ,");//เช่น D:\ShkSoftV2\ShkCore\WebContent\HP\TcSubrogation.zul
			System.out.println(file.getName());//TcSubrogation.zul
		}
		lst_file.clear();

	}
	
}

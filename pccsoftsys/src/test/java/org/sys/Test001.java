package org.sys;

import java.util.ArrayList;

import org.junit.Test;

import com.pcc.sys.lib.FOsUtil;
import com.pcc.sys.lib.FnFile;

public class Test001 {

	@Test
	public void FnFile_listFilesForFolder() {

		//TEST listFilesForFolder
		java.util.List<java.io.File> lst_file = new ArrayList<>();
		final java.io.File folder = new java.io.File("E:/ShkSoftV2/ShkCore/WebContent/HP");
		FnFile.listFilesForFolder(folder, lst_file);
		for (java.io.File file : lst_file) {
			System.out.print(file.getAbsoluteFile() + " ,");//เช่น D:\ShkSoftV2\ShkCore\WebContent\HP\TcSubrogation.zul
			System.out.println(file.getName());//TcSubrogation.zul
		}
		lst_file.clear();

	}

	@Test
	public void test_FOsUtil() {

		System.out.println(FOsUtil.OS);

		if (FOsUtil.isWindows()) {
			System.out.println("This is Windows");
		} else if (FOsUtil.isMac()) {
			System.out.println("This is Mac");
		} else if (FOsUtil.isUnix()) {
			System.out.println("This is Unix or Linux");
		} else if (FOsUtil.isSolaris()) {
			System.out.println("This is Solaris");
		} else {
			System.out.println("Your OS is not support!!");
		}

	}

}

package com.pcc.sys.lib;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * This utility compresses a list of files to standard ZIP format file.
 * It is able to compress all sub files and sub directories, recursively.
 * @author www.codejava.net
 *
 */
public class ZipUtility {

	public static void main(String[] args) {
		testzip1();
	}

	public static void testzip1() {
		// String[] myFiles = {
		// "E:/Test/PIC1.JPG",
		// "E:/Test/PIC2.JPG",
		// "E:/Test/PIC3.JPG",
		// "E:/Test/PIC4.JPG" };

		// test ok 28/1/65 by preecha
		// �繡�� zip Foder = tmp ������Դ�٨������ folder tmp
		// ��͹��������仨�������������
		// folder tmp ������ zip ��
		String[] myFiles = { "E:/tmp" };

		// test ok 28/1/65 by preecha
		// ���� zip ���� folder=sub1 ���㹹�鹨л�Сͺ�������������ͧ sub1
		// String[] myFiles = { "E:/tmp/sub1" };

		// test ok 28/1/65 by preecha
		// zip ���ç�
		// String[] myFiles = { "E:/tmp/npae.xls" };

		String zipFile = "E:/tmp2/MyPics.zip";

		ZipUtility zipUtil = new ZipUtility();
		try {
			zipUtil.zip(myFiles, zipFile);
		} catch (Exception ex) {
			// some errors occurred
			ex.printStackTrace();
		}
	}

	/**
	 * A constants for buffer size used to read/write data
	 */
	private static final int BUFFER_SIZE = 4096;

	/**
	 * Compresses a list of files to a destination zip file
	 * @param listFiles A collection of files and directories
	 * @param destZipFile The path of the destination zip file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void zip(List<File> listFiles, String destZipFile) throws FileNotFoundException,
			IOException {

		try (FileOutputStream outfS = new FileOutputStream(destZipFile);
				ZipOutputStream zos = new ZipOutputStream(outfS);) {

			for (File file : listFiles) {
				if (file.isDirectory()) {
					zipDirectory(file, file.getName(), zos);
				} else {
					zipFile(file, zos);
				}
			}
			zos.flush();

		}
	}

	/**
	 * Compresses files represented in an array of paths
	 * @param files a String array containing file paths
	 * @param destZipFile The path of the destination zip file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void zip(String[] files, String destZipFile) throws FileNotFoundException, IOException {
		List<File> listFiles = new ArrayList<File>();
		for (int i = 0; i < files.length; i++) {
			listFiles.add(new File(files[i]));
		}
		zip(listFiles, destZipFile);
	}

	/**
	 * Adds a directory to the current zip output stream
	 * @param folder the directory to be  added
	 * @param parentFolder the path of parent directory
	 * @param zos the current zip output stream
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void zipDirectory(File folder, String parentFolder,
			ZipOutputStream zos) throws FileNotFoundException, IOException {

		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				zipDirectory(file, parentFolder + "/" + file.getName(), zos);
				continue;
			}
			zos.putNextEntry(new ZipEntry(parentFolder + "/" + file.getName()));
			try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
				byte[] bytesIn = new byte[BUFFER_SIZE];
				int read = 0;
				while ((read = bis.read(bytesIn)) != -1) {
					zos.write(bytesIn, 0, read);
				}
			}
			zos.closeEntry();
		}

	}

	/**
	 * Adds a file to the current zip output stream
	 * @param file the file to be added
	 * @param zos the current zip output stream
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void zipFile(File file, ZipOutputStream zos)
			throws FileNotFoundException, IOException {

		zos.putNextEntry(new ZipEntry(file.getName()));
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
			//long bytesRead = 0;
			byte[] bytesIn = new byte[BUFFER_SIZE];
			int read = 0;
			while ((read = bis.read(bytesIn)) != -1) {
				zos.write(bytesIn, 0, read);
				//bytesRead += read;
			}
		}
		zos.closeEntry();

	}

}
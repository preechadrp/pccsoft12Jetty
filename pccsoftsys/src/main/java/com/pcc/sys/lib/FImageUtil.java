package com.pcc.sys.lib;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RandomAccessFileOrArray;
import com.lowagie.text.pdf.codec.TiffImage;

public class FImageUtil {

	/**
	 * resize ภาพ
	 * @param imageData
	 * @param to_width ความกว้างที่ต้องการ
	 * @param ret ตัวที่จะส่งค่ากลับ
	 * @throws IOException
	 */
	public static void resizeImageToWidth(byte[] imageData, int to_width, ByteArrayInputStream[] ret)
			throws IOException {

		if (imageData != null) {
			if (imageData.length > 0) {

				BufferedImage img2 = ImageIO.read(new ByteArrayInputStream(imageData));
				if (img2 != null) {
					int original_width = img2.getWidth();
					int original_height = img2.getHeight();

					//หาความสูงที่ความกว้าง to_width
					int to_height = (int) (to_width * original_height / original_width);

					System.out.println("to_width :" + to_width + " to_height :" + to_height);

					BufferedImage bImage1 = createResizedCopy(img2, to_width, to_height);
					try (ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
						ImageIO.write(bImage1, "jpg", baos);
						baos.flush();
						ret[0] = new ByteArrayInputStream(baos.toByteArray());
					}

				}

			}
		}

	}

	/**
	 * resize ภาพ
	 * @param imageData
	 * @param to_height ความสูงที่ต้องการ
	 * @param ret ตัวที่จะส่งค่ากลับ
	 * @throws IOException
	 */
	public static void resizeImageToHeight(byte[] imageData, int to_height, ByteArrayInputStream[] ret)
			throws IOException {

		if (imageData != null) {
			if (imageData.length > 0) {

				BufferedImage img2 = ImageIO.read(new ByteArrayInputStream(imageData));
				if (img2 != null) {
					int original_width = img2.getWidth();
					int original_height = img2.getHeight();

					//หาความกว้างที่ความสูง to_height
					int to_width = (int) (to_height * original_width / original_height);

					System.out.println("new_width :" + to_width + " new_height :" + to_height);

					BufferedImage bImage1 = createResizedCopy(img2, to_width, to_height);
					try (ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
						ImageIO.write(bImage1, "jpg", baos);
						baos.flush();
						ret[0] = new ByteArrayInputStream(baos.toByteArray());
					}

				}

			}
		}

	}

	private static BufferedImage createResizedCopy(java.awt.Image originalImage, int scaledWidth, int scaledHeight) {
		System.out.println("resizing...");
		BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.SCALE_SMOOTH);
		Graphics2D g = scaledBI.createGraphics();
		g.setComposite(AlphaComposite.Src);
		g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
		g.dispose();
		return scaledBI;
	}

	/**
	 * สร้าง Byte ภาพจาก RenderedImage
	 * @param img
	 * @param format เช่น PNG,JPG เป็นต้น
	 * @return
	 * @throws IOException
	 */
	public static byte[] getByteImageFormat(java.awt.image.RenderedImage img, String format) throws IOException {

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
			ImageIO.write(img, format, baos);
			return baos.toByteArray();
		}

	}

	/**
	 * แปลง tif byte เป็น pdf byte (ขนาด B4) แบบหลายๆ หน้าได้ ,by preecha 22/6/64
	 * @param imageByte ของ TIFF byte
	 * @return imageByte ของ PDF byte
	 * @throws Exception
	 */
	public static byte[] convertTiffToPdf(byte[] imageByte) throws Exception {

		byte[] ret = null;

		try {
			//อ่านจากไฟล์
			//RandomAccessFileOrArray myTiffFile = new RandomAccessFileOrArray("c:\\test\\หนังสือสัญญา.tiff");
			//อ่านจาก byte ของ tiff
			RandomAccessFileOrArray myTiffFile = new RandomAccessFileOrArray(imageByte);

			//Find number of images in Tiff file
			int numberOfPages = TiffImage.getNumberOfPages(myTiffFile);
			System.out.println("Number of Images in Tiff File" + numberOfPages);
			Document TifftoPDF = new Document();

			//เขียนลงไฟล์
			//PdfWriter.getInstance(TifftoPDF, new FileOutputStream("c:\\test\\tiff2Pdf.pdf"));
			//เขียนลง ByteArrayOutputStream แทน
			try (ByteArrayOutputStream bOut = new ByteArrayOutputStream()) {
				PdfWriter.getInstance(TifftoPDF, bOut);

				TifftoPDF.setPageSize(PageSize.B4);
				TifftoPDF.setMargins(1f, 1f, 1f, 1f);
				TifftoPDF.open();
				//Run a for loop to extract images from Tiff file
				//into a Image object and add to PDF recursively
				for (int n1 = 1; n1 <= numberOfPages; n1++) {

					com.lowagie.text.Image tempImage = TiffImage.getTiffImage(myTiffFile, n1);

					//				System.out.println(tempImage.getDpiX());
					//				System.out.println(tempImage.getDpiY());
					//				System.out.println(tempImage.getWidth());
					//				System.out.println(tempImage.getHeight());

					tempImage.scalePercent(7200f / tempImage.getDpiX(), 7200f / tempImage.getDpiY());

					TifftoPDF.add(tempImage);

				}
				TifftoPDF.close();
				System.out.println("Tiff to PDF Conversion in Java Completed");

				ret = bOut.toByteArray();

			}

			return ret;

		} catch (Exception i1) {
			i1.printStackTrace();
			throw new Exception(i1.getMessage());
		}

	}

	/**
	 * แปลง image byte เป็น pdf byte (ขนาด A4) ,by preecha 22/6/64
	 * @param imageByte ของ image byte
	 * @return imageByte ของ PDF byte
	 * @throws Exception
	 */
	public static byte[] convertImageToPdf(byte[] imageByte) throws Exception {

		byte[] ret = null;

		try (ByteArrayOutputStream bOut = new ByteArrayOutputStream();) {

			Document imgToPDF = new Document();
			PdfWriter.getInstance(imgToPDF, bOut);
			imgToPDF.setPageSize(PageSize.A4);
			imgToPDF.setMargins(1f, 1f, 1f, 1f);
			imgToPDF.open();
			//==========

			com.lowagie.text.Image tempImage = com.lowagie.text.Image.getInstance(imageByte);//กรณีเอาจากไฟล์ Image.getInstance("dog.jpg");
			tempImage.scalePercent(7200f / tempImage.getDpiX(), 7200f / tempImage.getDpiY()); //ค่า 7200f มาจาก 72f*100 ไม่ทราบที่มาแต่เห็นตัวอย่างใน net
			imgToPDF.add(tempImage);

			//=======
			imgToPDF.close();
			System.out.println("Image to PDF Conversion in Java Completed");

			ret = bOut.toByteArray();

			return ret;

		} catch (Exception i1) {
			i1.printStackTrace();
			throw new Exception(i1.getMessage());
		}

	}

}

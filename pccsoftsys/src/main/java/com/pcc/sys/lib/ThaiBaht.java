package com.pcc.sys.lib;

import java.math.BigDecimal;

/**
 * Main Windows
 * @author preecha.d 01/01/2562
 *
 */
public class ThaiBaht {
	public static void main(String[] arg) {
		System.out.println(getBahtString(new BigDecimal("110625710.01")));
	}

	/**
	 *
	 * @param amount
	 * @return
	 */
	public static String getBahtString(BigDecimal amt) {
		try {
			
			String amount = Fnc.getStrBigDecimal2(amt);

			StringBuffer result = new StringBuffer();
			String satang, baht;

			if (amount.startsWith("-")) {
				amount = amount.substring(1);
				result.append("ลบ");
			}
			if (amount.endsWith(".")) {
				amount = amount.substring(0, amount.length() - 1);
			}
			if (amount.endsWith(".00")) {
				amount = amount.substring(0, amount.length() - 3);
			}

			if (amount.indexOf('.') > -1) {
				String money[] = amount.split("\\.");
				baht = money[0];
				if (toInt(money[1]) > 0) {
					satang = money[1] + "";
				} else {
					satang = "";
				}
			} else {
				baht = amount;
				satang = "";
			}

			baht = baht.replace(",", "");
			satang = satang.replace(",", "");

			if (baht.length() > 0) {
				result.append(changeNum(baht));
				result.append("บาท");
			}

			if (satang.length() > 0) {
				int isatang = Fnc.getIntFromStr(satang); //แปลงเป็นตัวเลขป้องกัน .01
				if (isatang > 0) {
					result.append(changeNum(isatang + ""));
					result.append("สตางค์");
				} else {
					result.append("ถ้วน");
				}
			} else {
				result.append("ถ้วน");
			}

			return result.toString();
		} catch (Exception e) {
			return "จำนวนเงินไม่ถูกต้อง";
		}
	}

	private static String changeNum(String num) {
		int max;
		String r, n;
		num = num.trim();
		max = num.length();

		StringBuffer result = new StringBuffer();
		for (int i = 0; i < max; i++) {
			r = chooseUnit((max - i) % 6);
			n = chooseNum(Integer.parseInt(num.substring(i, i + 1)));

			if (r.equals("สิบ") && n.equals("หนึ่ง")) {
				n = "";
			}
			if (n.equals("หนึ่ง") && r.equals("") && max != 1) {
				n = "เอ็ด";
			}
			if (i == 0 && n.equals("เอ็ด") && max > 1) {
				n = "หนึ่ง";
			}
			if (r.equals("สิบ") && n.equals("สอง")) {
				n = "ยี่";
			}
			if (r.equals("") && (max - i) > 6) {
				r = "ล้าน";
			}

			if (!n.equals("ศูนย์")) {
				result.append(n);
				result.append(r);
			} else {
				if (r.equals("ล้าน")) {
					result.append(r);
				}
			}
		}
		if (result.length() == 0) {
			return "ศูนย์";
		}
		return result.toString();
	}

	private static String chooseUnit(int indx) {
		String[] unit = { "แสน", "", "สิบ", "ร้อย", "พัน", "หมื่น" };
		return unit[indx];
	}

	private static String chooseNum(int indx) {
		String[] num = { "ศูนย์", "หนึ่ง", "สอง", "สาม", "สี่", "ห้า", "หก", "เจ็ด", "แปด", "เก้า" };
		return num[indx];
	}

	public static int toInt(String str) {
		int value = 0;
		try {
			value = Integer.parseInt(str.replaceAll(",", ""));
		} catch (Exception ex) {
			value = 0;
		}
		return value;
	}
}

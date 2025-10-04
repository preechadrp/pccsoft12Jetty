package com.pcc.sys.lib;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Fnc {

	public static void main(String[] arg) {
		String xxxx = "TEST_456777";
		System.out.println(getStrLeft(xxxx, 4));
	}

	public static boolean isEmpty(String str) {
		boolean res = false;
		if (str == null) {
			res = true;
		} else if (str.trim().equals("")) {
			res = true;
		}
		return res;
	}

	/**
	 * ตรวจสอบอักขระแรกของรหัสต่างๆ ช่วยป้องกันการ key พวกอักขระบนล่างเช่น การัน ในภาษาไทย
	 * @param code
	 * @return
	 * @throws Exception 
	 */
	public static void isPassCode(String code) throws Exception {
		if (code != null) {
			char[] c = code.toCharArray();
			if ((c[0] >= '0' && c[0] <= '9')
					|| (c[0] >= 'A' && c[0] <= 'Z')
					|| (c[0] >= 'a' && c[0] <= 'z')
					|| (c[0] >= 'ก' && c[0] <= 'ฮ')
					|| c[0] == 'เ'
					|| c[0] == 'แ'
					|| c[0] == 'โ'
					|| c[0] == 'ไ'
					|| c[0] == 'ใ') {
				//OK
			} else {
				throw new Exception("ใส่อักขระตัวแรกสำหรับสร้างรหัสไม่เหมาะสม (แนะนำ 0-9,a-z,A-Z,ก-ฮ,เ,แ,โ,ไ,ใ)");
			}
		}

	}

	public static String getStr(Object obj) {
		if (obj instanceof String) {
			return (String) obj;
		} else {
			return "";
		}
	}

	public static String getStrNumber(Number number, String pattern) {
		Number n1 = number;
		if (n1 == null) {
			n1 = 0;
		}
		java.text.DecimalFormat formater = (DecimalFormat) java.text.NumberFormat.getNumberInstance();
		formater.applyPattern(pattern);
		return formater.format(n1);
	}

	/**
	 * แปลงเป็น string รูปแบบ ###,##0.00
	 * @param amount
	 * @return
	 */
	public static String getStrBigDecimal(BigDecimal amount) {
		if (amount == null) {
			return "";
		}
		DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");
		return decimalFormat.format(amount);
	}
	
	/**
	 * แปลงเป็น string รูปแบบ ##0.00
	 * @param amount
	 * @return
	 */
	public static String getStrBigDecimal2(BigDecimal amount) {
		if (amount == null) {
			return "";
		}
		DecimalFormat decimalFormat = new DecimalFormat("##0.00");
		return decimalFormat.format(amount);
	}

	/**
	 * หยิบข้อความจากด้านขวามือ
	 * @param text ข้อความ
	 * @param length จำนวนตัวอักษรที่ต้องการนับจากขวามือ
	 * @return String
	 */
	public static String getStrRight(String text, int length) {
		if (text != null) {
			if (text.length() >= length) {
				return text.substring(text.length() - length, text.length());
			} else {
				return text;
			}
		} else {
			return "";
		}
	}

	public static String getStrLeft(String text, int length) {
		if (text != null) {
			if (text.length() >= length) {
				return text.substring(0, length);
			} else {
				return text;
			}
		} else {
			return "";
		}
	}

	/**
	 * เอาอักษรตำแหน่งที่
	 * @param text
	 * @param at เริ่มนับจาก 1
	 * @return
	 */
	public static String getStrAt(String text, int at) {
		if (text != null) {
			if (text.length() < at) {
				return "";
			} else {
				return text.charAt(at - 1) + "";
			}
		} else {
			return "";
		}
	}

	/**
	 * หยิบตัวอักษรจาก begin นับไปจำนวน count
	 * @param text
	 * @param begin เริ่มนับจาก 0...
	 * @param count เริ่มนับจาก 1...
	 * @return
	 */
	public static String getSubStr(String text, int begin, int count) {
		String ret = "";
		if (text != null) {
			if (text.length() > begin) {
				String s1 = text.substring(begin);

				String s2 = "";
				if (s1.length() >= count) {
					s2 = s1.substring(0, count);
				} else {
					s2 = s1.substring(0);
				}
				ret = s2;
			}
		}
		return ret;
	}

	/**
	 * สร้าง String สำหรับ sql แบบ in 
	 * @param data_list เป็น array string เช่น [a,b,c,b]
	 * @param fieldName ชื่อฟิลด์ที่จะ in sql
	 * @param option 1=and , 2=or
	  * @param be อักษรที่ใช้ปิดหัวท้ายค่า เช่น Single Quot
	 * @return เช่น 'a','b','c','b'
	 */
	public static String getStrInSql(String[] data_list, String fieldName, int option, String be) {
		String result = "";
		if (data_list.length > 0) {
			String tmp1 = "";
			for (String dat : data_list) {
				if (!tmp1.equals("")) {
					tmp1 += ",";
				}
				if (Fnc.isEmpty(be)) {
					tmp1 += dat;
				} else {
					tmp1 += be + dat + be;
				}
			}
			if (option == 1) {
				result = " and ";
			} else if (option == 2) {
				result = " or ";
			}
			result += fieldName + " in (" + tmp1 + ") ";
		}
		return result;
	}

	public static BigDecimal getBigDecimal(Object obj) {
		if (obj instanceof BigDecimal) {
			return getBigDecimalValue((BigDecimal) obj);
		} else if (obj instanceof String) {
			return getBigDecimalFromStr((String) obj);
		} else {
			return BigDecimal.ZERO;
		}
	}

	public static BigDecimal getBigDecimalValue(BigDecimal bigDecimal) {
		return bigDecimal == null ? BigDecimal.ZERO : bigDecimal;
	}

	public static BigDecimal getBigDecimalFromStr(String str) {
		BigDecimal result = new BigDecimal(0);
		str = str.replaceAll(",", "");
		result = new BigDecimal(str);
		return result;
	}
	
	/**
	 * เอาเลขไม่รวมทศนิยม
	 * @param b1
	 * @return
	 */
	public static BigDecimal getBigDecimalF1(BigDecimal b1) {
		String[] bs = getStrBigDecimal(b1).split("\\.");
		return getBigDecimalFromStr(bs[0]);
	}
	
	/**
	 * เอาเลขทศนิยม
	 * @param b1
	 * @return
	 */
	public static BigDecimal getBigDecimalF2(BigDecimal b1) {
		String[] bs = getStrBigDecimal(b1).split("\\.");
		return getBigDecimalFromStr(bs[1]);
	}

	public static int getIntValue(Integer value) {
		return value == null ? 0 : value.intValue();
	}

	public static int getIntValue(Object value) {
		return value == null ? 0 : (int) value;
	}

	public static int getIntFromStr(String str) {
		try {
			return Integer.parseInt(str);
		} catch (Exception ex) {
			return 0;
		}
	}

	/**
	 * แก้ปัญหากรณีข้อมูลมี single quote ( ' ) 
	 * @param s
	 * @return
	 */
	public static String sqlQuote(String s) {
		return getStr(s).replace("\'", "\'\'");
	}

	/**
	 * return ค่า  'a','b','c'
	 * @param sl
	 * @return
	 */
	public static String sqlInStr(java.util.List<String> sl) {
		String ret = "";
		if (sl != null) {
			for (String s : sl) {
				if (isEmpty(ret)) {
					ret += "'" + sqlQuote(s) + "'";
				} else {
					ret += ",'" + sqlQuote(s) + "'";
				}
			}
		}
		return ret;
	}

	/**
	 * บวก str += s+\n
	 * @param str
	 * @param s
	 */
	public static void addLine(String[] str, String s) {
		str[0] += s + "\n";
	}

	/**
	 * หามูลค่าสินค้าไม่รวม VAT
	 * @param amt มูลค่ารวม VAT
	 * @param vatRate ตั้งแต่ 1,.....
	 * @return
	 */
	public static BigDecimal getAmtExcludeVat(BigDecimal amt, BigDecimal vatRate) {

		BigDecimal ret = BigDecimal.ZERO;
		if (vatRate.compareTo(BigDecimal.ZERO) == 0) {
			ret = amt;
		} else if (getBigDecimalValue(amt).compareTo(BigDecimal.ZERO) > 0) {

			BigDecimal div1 = new BigDecimal("100").add(vatRate);

			// มูลค่า VAT = amt*vatrate/(100+vatrate)
			BigDecimal vatAmt = amt
					.multiply(vatRate)
					.divide(div1, 2, RoundingMode.HALF_UP);//ตัวหารห้ามเป็น 0

			ret = amt.subtract(vatAmt);

		}
		return ret;

	}

	/**
	 * หามูลค่า VAT จากมูลค่ารวม VAT
	 * @param amt มูลค่ารวม VAT
	 * @param vatRate vatRate ตั้งแต่ 1,.....
	 * @return
	 */
	public static BigDecimal getAmtVat(BigDecimal amt, BigDecimal vatRate) {

		BigDecimal ret = BigDecimal.ZERO;
		if (vatRate.compareTo(BigDecimal.ZERO) == 0) {
			//nothing
		} else if (getBigDecimalValue(amt).compareTo(BigDecimal.ZERO) > 0) {

			BigDecimal div1 = new BigDecimal("100").add(vatRate);

			// มูลค่า VAT = amt*vatrate/(100+vatrate)
			ret = amt
					.multiply(vatRate)
					.divide(div1, 2, RoundingMode.HALF_UP);//ตัวหารห้ามเป็น 0

		}
		return ret;

	}
	
	/**
	 * คำนวนภาษี  = มูลค่าสินค้า*อัตราภาษี
	 * @param amt
	 * @param vatRate
	 * @return
	 */
	public static BigDecimal calVat(BigDecimal amt, BigDecimal vatRate) {

		BigDecimal ret = BigDecimal.ZERO;
		if (vatRate.compareTo(BigDecimal.ZERO) == 0) {
			//nothing
		} else if (getBigDecimalValue(amt).compareTo(BigDecimal.ZERO) > 0) {

			BigDecimal div1 = new BigDecimal("100");

			// มูลค่า VAT = amt*vatrate/100
			ret = amt
					.multiply(vatRate)
					.divide(div1, 2, RoundingMode.HALF_UP);//ตัวหารห้ามเป็น 0

		}
		return ret;

	}

	/**
	 * หาค่าเฉลี่ยต่อหน่วย
	 * @param bAMT มูลค่าหน่วยที่ต้องการเฉลี่ย
	 * @param bQTY จำนวนหน่วยที่ต้องการหายอด
	 * @return ค่าเฉลี่ยต่อหน่วย
	 */
	public static BigDecimal avgPerUnit(BigDecimal bAMT, BigDecimal bQTY) {
		BigDecimal bAvg1 = bAMT.divide(bQTY, 2, RoundingMode.HALF_UP);//ค่าเฉลี่ยต่อชิ้น  เช่น 100/3=33.33  , 50/3=16.67
		return bAvg1;
	}

	/**
	 * หาค่าเฉลี่ยต่อหน่วยชิ้นสุดท้าย
	 * @param bAMT มูลค่าหน่วยที่ต้องการเฉลี่ย
	 * @param bQTY จำนวนหน่วยที่ต้องการหายอด
	 * @return ค่าเฉลี่ยต่อหน่วยชิ้นสุดท้าย
	 */
	public static BigDecimal avgPerUnitLast(BigDecimal bAMT, BigDecimal bQTY) {

		BigDecimal bAvg1 = bAMT.divide(bQTY, 2, RoundingMode.HALF_UP);//ค่าเฉลี่ยต่อชิ้น  เช่น 100/3=33.33  , 50/3=16.67
		BigDecimal bAMT2 = bAvg1.multiply(bQTY);//มูลค่าทรัพย์สินเมื่อคูณ  เช่น 33.33*3=99.99  , 16.67*3=50.01
		BigDecimal bDiff = bAMT.subtract(bAMT2);//ส่วนต่าง  เช่น 100-99.99=1  , 50-50.01=-0.01
		BigDecimal bAvg2 = bAvg1.add(bDiff);//ชิ้นสุดท้าย เช่น 33.33+1=33.34  , 16.67+(-0.01) = 16.66

		return bAvg2;
	}

	/**
	 * เพิ่มสมาชิกเข้าไปใน Array String
	 * @param fromArr
	 * @param appendString
	 */
	public static String[] appendStringArray(String[] fromArr, String appendString) {
		String[] ret = java.util.Arrays.copyOf(fromArr, fromArr.length + 1);//clone และขยาย
		ret[ret.length - 1] = appendString; //เก็บค่าเข้าช่องสุดท้าย
		return ret;
	}
	
	public static String showCardId(String cardId) {
		cardId = Fnc.getStr(cardId);
		StringBuilder b = new StringBuilder(cardId);
		try {
			b.insert(12, " ");
			b.insert(10, " ");
			b.insert(5, " ");
			b.insert(1, " ");
		} catch (Exception ex) {
			return cardId;
		}
		return new String(b);
	}

}

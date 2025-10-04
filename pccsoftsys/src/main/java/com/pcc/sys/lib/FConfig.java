package com.pcc.sys.lib;

import java.io.InputStreamReader;
import java.util.Properties;

public class FConfig {

	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static String getConfig1(String key) {

		Properties props = new Properties();

		try {

			try (InputStreamReader in1 = new InputStreamReader(
					FConfig.class.getResourceAsStream("/config1.txt"), "UTF-8");) {

				props.load(in1);
				String value = props.getProperty(key);
				if (value == null) {
					return "";
				} else {
					return value.trim();
				}

			}

		} catch (Exception e) {
			return "";
		}

	}

	public static String getConfig1_AppName() {
		//String s1 = getConfig1("app_name");
		String s2 = getConfig1("svnver");
		String s3 = getConfig1("update");
		//return s1 + " (svn: " + s2 + " , update: " + s3 + ")";
		return "(svn: " + s2 + " , update: " + s3 + ")";
	}

	public static String getConfig2(String key) {

		Properties props = new Properties();

		try {

			try (InputStreamReader in1 = new InputStreamReader(
					FConfig.class.getResourceAsStream("/config2.txt"), "UTF-8");) {

				props.load(in1);
				String value = props.getProperty(key);
				if (value == null) {
					return "";
				} else {
					return value.trim();
				}
			}

		} catch (Exception e) {
			return "";
		}

	}

	public static String getConfig2_MasterDb() {
		String s = getConfig2("MasterDb");
		if (Fnc.isEmpty(s)) {
			s = "jdbc/MASTERDB";
		}
		logger.debug("MasterDb = " + s);
		return s;
	}

	public static boolean getConfig2_VisibletxtOpenMenu() {
		String s = getConfig2("txtOpenMenu");
		if (s.trim().toUpperCase().equals("YES")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean getConfig2_Report_In_Iframe() {
		String s = getConfig2("Report_In_Iframe");
		if (s.trim().toUpperCase().equals("YES")) {
			return true;
		} else {
			return false;
		}
	}
}

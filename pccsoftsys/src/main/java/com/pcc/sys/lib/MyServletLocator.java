package com.pcc.sys.lib;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import javax.servlet.ServletContext;

public class MyServletLocator implements org.zkoss.util.resource.LabelLocator {

	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
	
	private ServletContext _svlctx;
	private String _name;

	public MyServletLocator(ServletContext svlctx, String name) {
        _svlctx = svlctx;
        _name = name;
    }

	public URL locate(Locale locale) throws MalformedURLException {
		String s1 = "/WEB-INF/labels/" + _name + "_" + locale + ".txt";
		if (locale == null) {
			s1 = "/WEB-INF/labels/" + _name + "_en.txt";
		}
		logger.debug(s1);
		return _svlctx.getResource(s1);
	}
}
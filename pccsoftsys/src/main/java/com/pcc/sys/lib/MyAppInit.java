package com.pcc.sys.lib;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.WebApp;

import javax.servlet.ServletContext;

public class MyAppInit implements org.zkoss.zk.ui.util.WebAppInit {
	
	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
	
	public MyAppInit() {
		logger.info("MyAppInit -- constructor");
	}
	public void init(WebApp wapp) throws Exception {
		logger.info("MyAppInit -- init");
		//Labels.register(new FooDBLocator(("moduleX"));
		//Labels.register(new FooDBLocator(("moduleY"));
		//Labels.register(new FooServletLocator((ServletContext)wapp.getServletContext(), "module-1"));
		Labels.register(new MyServletLocator((ServletContext) wapp.getServletContext(), "mylabel"));
    }
}
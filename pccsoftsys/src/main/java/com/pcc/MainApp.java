package com.pcc;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

import com.pcc.api.core.AppApiServlet;
import com.pcc.api.core.Authen;
import com.pcc.api.core.JwtAuthFilter;
import com.pcc.sys.lib.FConstComm;
import com.pcc.sys.lib.MyStartConfigListener;

public class MainApp {

	static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
	
	public static void main(String[] args) throws Exception {

		log.info("<== Start by main method ==>");
		FConstComm.runAppMode = 1; //มีผลกับการเชื่อมฐานข้อมูล

		int appPort = 8877;
		int maxThreads = 100;
		int minThreads = 10;
		int idleTimeout = 120;

		var threadPool = new QueuedThreadPool(maxThreads, minThreads, idleTimeout);

		Server server = new Server(threadPool);

		// ==== add connector
		var connector = new ServerConnector(server);
		connector.setPort(appPort);
		server.addConnector(connector);

		// ==== แบบใช้ WebAppContext ต้องเพิ่ม lib = jetty-webapp
		var webapp = new WebAppContext();
		webapp.setContextPath("/"); // อยู่ใน root เลย

		java.net.URL webResource = MainApp.class.getResource("/webapp/");
		log.info("webResource : " + webResource.toString());
		log.info("toExternalForm : " + webResource.toExternalForm());
		//รันด้วย .jar 
		//webResource : jar:file:/D:/javaDemo1/demojetty10/target/demojetty10-0.0.1.jar!/webapp/
		//toExternalForm : jar:file:/D:/javaDemo1/demojetty10/target/demojetty10-0.0.1.jar!/webapp/
		//
		//รันด้วย IDE 
		//webResource : file:/D:/javaDemo1/demojetty10/target/classes/webapp/
		//toExternalForm : file:/D:/javaDemo1/demojetty10/target/classes/webapp/
		webapp.setWarResource(Resource.newResource(webResource));
	 
		// เพิ่ม servlet
		webapp.addServlet(IndexServlet.class, "");// Home Page
		webapp.addServlet(Authen.class, "/auth/login");
		webapp.addServlet(AppApiServlet.class, "/appapi");
		webapp.addServlet(MenuServlet.class, "/menu");
		webapp.addServlet(LoginServlet.class, "/login");

		// เพิ่ม ServletHolder ของ zk framework แทนการใช้ web.xml
		ServletHolder zkLoaderHolder = new ServletHolder(org.zkoss.zk.ui.http.DHtmlLayoutServlet.class);
		zkLoaderHolder.setInitParameter("update-uri", "/zkau");
		zkLoaderHolder.setInitOrder(1);
		webapp.addServlet(zkLoaderHolder, "*.zul");

		webapp.addServlet(org.zkoss.zk.au.http.DHtmlUpdateServlet.class, "/zkau/*");

		// เพิ่ม filter
		webapp.addFilter(JwtAuthFilter.class, "/appapi/*", EnumSet.of(DispatcherType.REQUEST));

		// เพิ่ม Listener
		webapp.addEventListener(new MyStartConfigListener());
		webapp.addEventListener(new org.zkoss.zk.ui.http.HttpSessionListener()); //zk Listener

		

		// เพิ่มเข้า handlers
		HandlerList handlers = new HandlerList();
		handlers.addHandler(webapp);

		server.setHandler(handlers);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				// ใช้เวลาหยุดเซิร์ฟเวอร์
				server.setStopTimeout(60 * 1000l);// รอ 60 นาทีก่อนจะบังคับปิด
				server.stop();
				System.out.println("Jetty server stopped gracefully");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}));

		server.start();
		server.join();

	}

}

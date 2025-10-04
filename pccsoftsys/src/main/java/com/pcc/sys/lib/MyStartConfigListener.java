package com.pcc.sys.lib;

import java.util.Locale;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * ระบบจะรันตัวเองอัตโนมัติเมื่อ tomcat ทำงาน ไม่ต้องประกาศใน web.xml เพราะใช้
 * Annotation
 *
 */
public class MyStartConfigListener implements ServletContextListener {

	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
	
	/**
	 * Default constructor.
	 */
	public MyStartConfigListener() {
		logger.info("MyStartConfigListener -- constructor");

		// แก้ปัญหาเรื่อง prepare statment ( setdate ,settimestamp )
		// สำคัญมากถ้าเกี่ยวกับการใช้ JDBC เชื่อมฐานข้อมูล
		// ทดสอบเมื่อ 5/3/63 เมนูบันทึกบัญชี GL(สมุดรายวัน)
		Locale.setDefault(Locale.ENGLISH);
		
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		logger.info("MyStartConfigListener -- contextInitialized");
	}
	
	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		logger.info("MyStartConfigListener -- contextDestroyed");
	}

}

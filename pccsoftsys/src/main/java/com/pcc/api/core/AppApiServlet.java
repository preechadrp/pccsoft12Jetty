package com.pcc.api.core;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AppApiServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public AppApiServlet() {
		log.debug("AppApiServlet -- constructor");
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ApiUtil.callApiResponseJsonPath(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		// String inputString = request.getParameter("input").toUpperCase();
		PrintWriter out = response.getWriter();
		out.println("hello world appapi");

	}

}

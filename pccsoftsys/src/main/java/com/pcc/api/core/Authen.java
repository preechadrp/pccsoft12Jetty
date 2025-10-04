package com.pcc.api.core;

import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONObject;
import org.zkoss.util.resource.Labels;

import com.pcc.sys.lib.FEncrypterData;
import com.pcc.sys.tbf.TbfFUSER;
import com.pcc.sys.tbo.TboFUSER;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Authen extends HttpServlet {

	private static final long serialVersionUID = 1148279302105379626L;
	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
	
	public Authen() {
		logger.info("Authen -- constructor");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		//ตัวอย่าง link ทดสอบ http://localhost:8080/pccsoftV9Web/auth/login

		// String inputString = request.getParameter("input").toUpperCase();
		PrintWriter out = response.getWriter();
		out.println("hello world login");

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		//ตัวอย่าง link ทดสอบ http://localhost:8080/pccsoftV9Web/auth/login
		logger.info("doPost Login");

		PrintWriter out = ApiUtil.getPrintWriter(response);

		try {

			String requestData = ApiUtil.readPostData(request);
			// System.out.println(requestData);

			JSONObject jsObj = new JSONObject(requestData);
			// เขียนขั้นตอนเปรียบเทียบ user / password ที่ส่งมาเปรียบเทียบกับ user
			// ในระบบของเราเองตรงนี้
			System.out.println(jsObj.toString());
			String userName = jsObj.getString("username");
			String password = jsObj.getString("password");

			TboFUSER user_tb = new TboFUSER();
			user_tb.setUSER_ID(userName);

			if (TbfFUSER.find(user_tb)) {

				FEncrypterData crypter = new FEncrypterData();
				String password_enc = crypter.encode(password);

				if (!password_enc.equals(user_tb.getPASSWD())) {
					throw new Exception(Labels.getLabel("login.label.errorPassword"));
				}

			} else {
				throw new Exception(Labels.getLabel("login.label.errorUser"));
			}

			JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
			final String token = jwtTokenUtil.generateToken(userName);

			JSONObject jsObjResponse = new JSONObject();
			jsObjResponse.put("token", token);

			out.println(jsObjResponse.toString());

		} catch (Exception ex) {
			out.println(ApiUtil.errorService(ex));
		} finally {
			out.close();
		}

	}

}

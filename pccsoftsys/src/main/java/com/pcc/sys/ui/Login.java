package com.pcc.sys.ui;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.find.FfFsign001;
import com.pcc.sys.lib.FConfig;
import com.pcc.sys.lib.FEncrypterData;
import com.pcc.sys.lib.FWindow;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.tbf.TbfFUSER;
import com.pcc.sys.tbo.TboFUSER;

public class Login extends FWindow {

	private static final long serialVersionUID = 1L;

	private static int testThread = 1;
	private Button btnLogin;
	private Textbox txtUserName, txtPassword;
	public Label lblINFO;

	@Override
	public void setFormObj() {
		txtUserName = (Textbox) getFellow("txtUserName");
		txtPassword = (Textbox) getFellow("txtPassword");
		btnLogin = (Button) getFellow("btnLogin");
		lblINFO = (Label) getFellow("lblINFO");

	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(txtUserName);
		addEnterIndex(txtPassword);
		addEnterIndex(btnLogin);
	}

	@Override
	public void formInit() {

		try {
			if (getLoginSession() != null) {
				Executions.sendRedirect("/menu");
			} else {

				//this.setTitle(FConfig.getConfig1_AppName());
				lblINFO.setValue(FConfig.getConfig1_AppName());

//			String userAgent = Executions.getCurrent().getUserAgent();
//			 System.out.println("userAgent : " + userAgent);
//			 ตระกูล Webkit = Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.3 (KHTML, like Gecko) Chrome/22.0.1218.0 Safari/537.3     
//			 ตระกูล Gecko = Mozilla/5.0 (Windows NT 6.1; rv:15.0) Gecko/20100101 Firefox/15.0.1
//			 MSIE 8.0 = Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0)
//			 MSIE 7.x = Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0)

//			//ตรวจสอบ Browser ที่ใช้งานได้
//			if (userAgent.toUpperCase().indexOf("MSIE 7.") > 0 || userAgent.toUpperCase().indexOf("MSIE 6.") > 0) {
//				Executions.sendRedirect("/errorchk.zul");
//			}

				txtUserName.focus();
			}
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void onOK() {
		/*
		 * Button จะไม่ทำงานใน onOK ตอนที่เรากดปุ่ม Enter
		 */
		if (focusObj == txtPassword) {
			if (txtUserName.getValue().equals("") || txtPassword.getValue().equals("")) {
				txtUserName.focus();
			} else {
				login();
			}
		} else {
			super.onOK();
		}
	}

	public void login() {
		//loginTest();
		loginMain();
		//Executions.sendRedirect("/menu");
	}

	public void loginTest() {
		//Executions.sendRedirect("/menu");

		//TEST
		try {

			//TEST 1
			int a = testThread;
			testThread++;
			System.out.println("wait : " + a);
			if (testThread == 1) {
				//Msg.info("OK " + a);
				Thread.sleep(3 * 1000);
			} else {
				//Msg.info("OK" + a);
				Thread.sleep(2 * 1000);
			}
			//END TEST 1

			//TEST 2
//			int a = testThread;
//			testThread++;
//			System.out.println("wait : " + a);
//			Thread.sleep(3 * 1000);
			//END TEST 2

			System.out.println("go on : " + a);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void loginMain() {

		String userName = txtUserName.getValue();
		String password = txtPassword.getValue();

		try {

			TboFUSER user_tb = new TboFUSER();
			user_tb.setUSER_ID(userName);

			if (TbfFUSER.find(user_tb)) {

				FEncrypterData crypter = new FEncrypterData();
				String password_enc = crypter.encode(password);

				if (!password_enc.equals(user_tb.getPASSWD())) {
					throw new Exception(Labels.getLabel("login.label.errorPassword"));
				}

				LoginBean loginBean = new LoginBean();
				loginBean.setTboFuser(user_tb);
				ManLogin.saveLogin(loginBean);

				//call main menu
				Executions.sendRedirect("/menu");

			} else {
				throw new Exception(Labels.getLabel("login.label.errorUser"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}

	}

	public void testPopup() {
		//FSearchData.testPopup();
		//FSearchData.testPopup2();//แบบ checkbox
		FfFsign001.popup();

	}
	
	/**
	 * getLoginSession()
	 * @return
	 * @throws Exception
	 */
	private static LoginBean getLoginSession() throws Exception {
		Object obj = org.zkoss.zk.ui.Sessions.getCurrent().getAttribute(ManLogin.sessionLoginName);
		if (obj != null) {
			return (LoginBean) obj;
		} else {
			return null;
		}
	}

}

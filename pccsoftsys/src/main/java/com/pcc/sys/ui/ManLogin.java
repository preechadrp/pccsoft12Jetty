package com.pcc.sys.ui;

import java.sql.SQLException;

import com.pcc.api.core.JwtTokenUtil;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.tbf.TbfFCOMP;
import com.pcc.sys.tbf.TbfFUSER;
import com.pcc.sys.tbm.TbmFUSER_MENU;
import com.pcc.sys.tbo.TboFCOMP;

public class ManLogin {

	public static final String sessionLoginName = "MyLoginSession";

	public static void setLoginSession(LoginBean loginBean) {
		if (Fnc.isEmpty(loginBean.getJwt())) {
			//== JWT
			var jwtUtil = new JwtTokenUtil();
			var jwt = jwtUtil.generateToken(loginBean.getTboFuser().getUSER_ID());
			loginBean.setJwt(jwt);
		}
		org.zkoss.zk.ui.Sessions.getCurrent().setAttribute(sessionLoginName, loginBean);
		String userName = loginBean.getTboFuser().getTITLE() + " " + loginBean.getTboFuser().getFNAME_THAI() + " " + loginBean.getTboFuser().getLNAME_THAI();
		org.zkoss.zk.ui.Sessions.getCurrent().setAttribute("user", userName + " , " + loginBean.getTboFcomp().getCOMP_NAME());
	}

	public static void saveLogin(LoginBean loginBean) throws Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {
			dbc.beginTrans();
			saveLogin(dbc, loginBean);
			dbc.commit();
		}
	}

	public static void saveLogin(FDbc dbc, LoginBean loginBean) throws SQLException, Exception {

		boolean findFirst = false;//ค้นหาบริษัทที่สามารถเข้าใช้ระบบได้
		if (!Fnc.isEmpty(loginBean.getTboFuser().getLAST_USE_COMP_CDE())) {
			
			int res = TbmFUSER_MENU.checkUserComp(dbc, loginBean.getUSER_ID(), loginBean.getTboFuser().getLAST_USE_COMP_CDE());
			if (res > 0) {

				TboFCOMP comp = new TboFCOMP();
				comp.setCOMP_CDE(loginBean.getTboFuser().getLAST_USE_COMP_CDE());
				if (TbfFCOMP.find(dbc, comp)) {//มีรหัสบริษัทอยู่จริง
					loginBean.setTboFcomp(comp);
					findFirst = false;
				} else {
					findFirst = true;
				}

			} else {
				findFirst = true;
			}

		} else {
			findFirst = true;
		}

		if (findFirst) {
			java.util.List<String> lst = TbmFUSER_MENU.getUserCompList(dbc, loginBean.getTboFuser().getUSER_ID());
			if (lst.size() == 0) {
				throw new Exception("User นี้ยังไม่ได้กำหนดสิทธิ์การเข้าใช้ระบบ");
			} else {
				TboFCOMP comp = new TboFCOMP();
				comp.setCOMP_CDE(lst.get(0));
				if (TbfFCOMP.find(dbc, comp)) {
					loginBean.setTboFcomp(comp);

					//update Fuser.LAST_USE_COMP_CDE
					loginBean.getTboFuser().setLAST_USE_COMP_CDE(comp.getCOMP_CDE());
					TbfFUSER.update(dbc, loginBean.getTboFuser(), "");
				} else {
					throw new Exception("User กำหนดสิทธิ์การเข้าระบบไม่ถูกต้อง");
				}
			}
		}

		setLoginSession(loginBean);

	}

	public static void logOut() {
		org.zkoss.zk.ui.Sessions.getCurrent().invalidate();
		org.zkoss.zk.ui.Executions.sendRedirect("/login");
	}

}

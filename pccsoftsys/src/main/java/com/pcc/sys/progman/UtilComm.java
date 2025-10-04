package com.pcc.sys.progman;

import java.sql.SQLException;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.tbf.TbfFCOMP;
import com.pcc.sys.tbf.TbfFUSER;
import com.pcc.sys.tbo.TboFCOMP;
import com.pcc.sys.tbo.TboFUSER;

public class UtilComm {

	public static void setLoginBeanForApp(FDbc fdb, LoginBean _loginBean) throws SQLException, Exception {
		TboFCOMP comp = new TboFCOMP();
		comp.setCOMP_CDE("M03");
		TbfFCOMP.find(fdb, comp);
		_loginBean.setTboFcomp(comp);

		TboFUSER user = new TboFUSER();
		user.setUSER_ID("admin");
		TbfFUSER.find(fdb, user);
		_loginBean.setTboFuser(user);
	}
}

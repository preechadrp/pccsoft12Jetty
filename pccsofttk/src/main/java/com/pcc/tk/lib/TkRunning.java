package com.pcc.tk.lib;

import java.util.Date;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.tbm.TbmFRUNNING;

public class TkRunning {

	public static String genJOBNO(FDbc dbc, LoginBean loginBean, Date date) throws Exception {
		//รูปแบบ JB630500001
		String ret = "JB" + TbmFRUNNING.getRunning(dbc, loginBean.getCOMP_CDE(), "TKJOBNO", date, 4, true);
		return ret;
	}

}

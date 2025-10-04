package com.pcc.bx.lib;

import java.util.Date;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.tbm.TbmFRUNNING;

public class BxRunning {

	public static String genBLNO(FDbc dbc, String prefix ,LoginBean loginBean, Date date) throws Exception {
		//รูปแบบ JB630500001
		String ret = prefix + TbmFRUNNING.getRunning(dbc, loginBean.getCOMP_CDE(), "BxBill", date, 4, true);
		return ret;
	}
	
}

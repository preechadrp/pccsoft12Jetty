package com.pcc.sys.progman;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.tbm.TbmFRUNNING;

/**
 * Running ต่างๆ ของ Common
 * @author preecha
 *
 */
public class RunningComm {


	public static String genCustno(LoginBean loginBean) throws Exception {
		String ret = TbmFRUNNING.getRunning(loginBean.getCOMP_CDE(), "CUST_CDE", FnDate.getTodaySqlDate(), 4, true);
		return ret;
	}

}

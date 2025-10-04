package com.pcc.gl.progman;


import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.tbm.TbmFRUNNING;

/**
 * Running ต่างๆ ของ Accout
 * 
 * @author preecha
 *
 */
public class RunningAc {

	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	/**
	 * สำหรับ acgl_ap.LINK_NO , acgl_ar.LINK_NO , acgl_vatpur.LINK_NO,
	 * acgl_vatsale.LINK_NO
	 * 
	 * @param dbc
	 * @param loginBean
	 * @return
	 * @throws Exception
	 */
	public static String get_LINK_NO(FDbc dbc, LoginBean loginBean) throws Exception {
		String ret = loginBean.getCOMP_CDE() + "-"
				+ TbmFRUNNING.getRunning(dbc, loginBean.getCOMP_CDE(), "LINK_NO", FnDate.getTodaySqlDate(), 10, false);
		logger.info(ret);
		return ret;
	}

	/**
	 * เลขเอกสารหัก ณ ที่จ่าย
	 * 
	 * @param dbc
	 * @param doc_type  1=ภงด. 3 (บุคคลธรรมดา), 2=ภงด.53 (นิติบุคคล)
	 * @param loginBean
	 * @return
	 * @throws Exception
	 */
	public static String get_ACGL_VATWH_CR_DOCNO(FDbc dbc, int doc_type, LoginBean loginBean) throws Exception {
		String ret = "";
		if (doc_type == 1) {
			ret = TbmFRUNNING.getRunning(dbc, loginBean.getCOMP_CDE(), "VATWH_CR_TYP1", FnDate.getTodaySqlDate(), 10,
					false);
		} else {
			ret = TbmFRUNNING.getRunning(dbc, loginBean.getCOMP_CDE(), "VATWH_CR_TYP2", FnDate.getTodaySqlDate(), 10,
					false);
		}
		logger.info(ret);
		return ret;
	}

}

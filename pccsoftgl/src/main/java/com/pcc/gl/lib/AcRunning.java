package com.pcc.gl.lib;

import com.pcc.gl.tbf.TbfACGL_HEADER;
import com.pcc.gl.tbo.TboACGL_HEADER;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.tbm.TbmFRUNNING;

public class AcRunning {

	/**
	 * สำหรับ Gen สมุดรายวันการลงบัญชี
	 * 
	 * @param dbc
	 * @param comp_cde
	 * @param running_id
	 * @param docdate
	 * @return
	 * @throws Exception
	 */
	public static String getRunningVoucher(FDbc dbc, String comp_cde, String running_id, java.util.Date docdate)
			throws Exception {

		String s_VOU_NO = "";
		TboACGL_HEADER acch = new TboACGL_HEADER();

		while (true) {

			acch.setCOMP_CDE(comp_cde);
			acch.setVOU_TYPE(running_id);

			s_VOU_NO = TbmFRUNNING.getRunning(dbc, comp_cde, running_id, docdate, 5, true);
			acch.setVOU_NO(s_VOU_NO);

			if (TbfACGL_HEADER.find(dbc, acch)) {
				continue;
			} else {
				break;
			}

		}

		return s_VOU_NO;

	}

}

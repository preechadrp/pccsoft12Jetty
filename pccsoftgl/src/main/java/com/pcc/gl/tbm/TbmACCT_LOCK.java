package com.pcc.gl.tbm;

import java.sql.SQLException;

import com.pcc.gl.tbf.TbfACCT_LOCK;
import com.pcc.gl.tbo.TboACCT_LOCK;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FnDate;

public class TbmACCT_LOCK {

	public static void checKPostDate(String comp_cde, java.sql.Date postdate) throws SQLException, Exception {

		try (FDbc dbc = FDbc.connectMasterDb()) {
			checKPostDate(dbc, comp_cde, postdate);
		}

	}

	public static void checKPostDate(FDbc dbc, String comp_cde, java.sql.Date postdate) throws SQLException, Exception {

		TboACCT_LOCK mod = new TboACCT_LOCK();

		mod.setCOMP_CDE(comp_cde);

		if (TbfACCT_LOCK.find(dbc, mod)) {

			if (mod.getLOCKTODATE() != null) {

				if (FnDate.compareDate(postdate, mod.getLOCKTODATE()) <= 0) {
					throw new Exception("มีการ Lock การลงบัญชีในช่วงวันที่นี้แล้ว");
				}

			}

		}

	}

}

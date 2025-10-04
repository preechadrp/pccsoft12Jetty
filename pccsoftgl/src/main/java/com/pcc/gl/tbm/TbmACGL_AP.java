package com.pcc.gl.tbm;

import java.sql.SQLException;

import com.pcc.gl.tbo.TboACGL_AP;
import com.pcc.gl.tbo.TboACGL_DETAIL;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;

public class TbmACGL_AP {

	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static void deleteAll_VOU_SEQ(FDbc dbc, TboACGL_DETAIL acgl_detail) throws SQLException {

		String sql = " DELETE FROM " + TboACGL_AP.tablename + " WHERE COMP_CDE='"
				+ Fnc.sqlQuote(acgl_detail.getCOMP_CDE()) + "' and VOU_TYPE='" + Fnc.sqlQuote(acgl_detail.getVOU_TYPE())
				+ "' and VOU_NO='" + Fnc.sqlQuote(acgl_detail.getVOU_NO()) + "' and VOU_SEQ="
				+ acgl_detail.getVOU_SEQ();
		logger.info(sql);
		logger.info("Eff : " + dbc.executeSql(sql));

	}

}

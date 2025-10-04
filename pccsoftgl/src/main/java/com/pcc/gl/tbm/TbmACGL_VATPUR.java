package com.pcc.gl.tbm;

import java.sql.SQLException;

import com.pcc.gl.tbo.TboACGL_DETAIL;
import com.pcc.gl.tbo.TboACGL_VATPUR;
import com.pcc.sys.lib.FDbc;

public class TbmACGL_VATPUR {

	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static void deleteAll_VOU_SEQ(FDbc dbc, TboACGL_DETAIL acgl_detail) throws SQLException {

		String sql2 = " DELETE FROM " + TboACGL_VATPUR.tablename
				+ " WHERE COMP_CDE=? and VOU_TYPE=? and VOU_NO=? and VOU_SEQ=?";
		int eff = dbc.executeSql2(sql2, acgl_detail.getCOMP_CDE(), acgl_detail.getVOU_TYPE(), acgl_detail.getVOU_NO(),
				acgl_detail.getVOU_SEQ());
		logger.info("Eff : " + eff);
	}

}

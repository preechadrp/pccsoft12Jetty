package com.pcc.gl.tbm;

import java.sql.SQLException;

import com.pcc.gl.tbf.TbfACCT_VOU_TYPE;
import com.pcc.gl.tbo.TboACCT_VOU_TYPE;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.Fnc;

public class TbmACCT_VOU_TYPE {

	public static String getVou_type_name(FDbc dbc, String vou_type, LoginBean loginBean)
			throws SQLException, Exception {
		String ret = "";

		TboACCT_VOU_TYPE vut = new TboACCT_VOU_TYPE();
		vut.setCOMP_CDE(loginBean.getCOMP_CDE());
		vut.setVOU_TYPE(vou_type);
		if (TbfACCT_VOU_TYPE.find(dbc, vut)) {
			ret = Fnc.getStr(vut.getVOU_NAME());
		}

		return ret;
	}

	public static int getUSE_PV(FDbc dbc, String vou_type, LoginBean loginBean) throws SQLException, Exception {
		TboACCT_VOU_TYPE vut = new TboACCT_VOU_TYPE();
		vut.setCOMP_CDE(loginBean.getCOMP_CDE());
		vut.setVOU_TYPE(vou_type);
		if (TbfACCT_VOU_TYPE.find(dbc, vut)) {
			return vut.getUSE_PV();
		} else {
			return 0;
		}
	}

	public static int getUSE_RV(FDbc dbc, String vou_type, LoginBean loginBean) throws SQLException, Exception {
		TboACCT_VOU_TYPE vut = new TboACCT_VOU_TYPE();
		vut.setCOMP_CDE(loginBean.getCOMP_CDE());
		vut.setVOU_TYPE(vou_type);
		if (TbfACCT_VOU_TYPE.find(dbc, vut)) {
			return vut.getUSE_RV();
		} else {
			return 0;
		}
	}

}

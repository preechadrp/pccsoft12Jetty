package com.pcc.api.gl;

import org.json.JSONObject;

import com.pcc.api.core.ApiUtil;
import com.pcc.gl.progman.ManAcTbal;
import com.pcc.sys.beans.LoginBean;

public class ApiAcTbal {

	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
	
	public static String getData1(JSONObject requestpara, LoginBean _loginBean) {

		try {
			//String compid = jsonObj.getString("compid");
			//String branid = jsonObj.getString("branid");
			logger.info("getData1 : {}", requestpara.toString());

			//=== call method
			JSONObject jsonObjReturn = new JSONObject();
			String [] data = ManAcTbal.getReportService(_loginBean, requestpara);

			jsonObjReturn.put("base64", data[0]);
			jsonObjReturn.put("format", data[1]);

			return jsonObjReturn.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			return ApiUtil.errorService(e);
		}

	}
	
}

package com.pcc.api.tk;

import org.json.JSONObject;

import com.pcc.api.core.ApiUtil;
import com.pcc.sys.beans.LoginBean;
import com.pcc.tk.progman.ManTkJobRep1;

public class ApiFrmTkJobRep1 {

	public static String getData1(JSONObject requestpara, LoginBean _loginBean) {

		try {
			//String compid = jsonObj.getString("compid");
			//String branid = jsonObj.getString("branid");
			System.out.println("getData1 " + requestpara.toString());

			//=== call method
			JSONObject jsonObjReturn = new JSONObject();
			String [] data = ManTkJobRep1.getReportService(_loginBean, requestpara);
			jsonObjReturn.put("base64", data[0]);
			jsonObjReturn.put("format", data[1]);

			return jsonObjReturn.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			return ApiUtil.errorService(e);
		}

	}
	
}

package com.pcc.api.gl;

import org.json.JSONObject;

import com.pcc.api.core.ApiUtil;
import com.pcc.gl.progman.ManAcWithold;
import com.pcc.sys.beans.LoginBean;

public class ApiAcWithold {

	public static String getData1(JSONObject requestpara, LoginBean _loginBean) {

		try {
			//String compid = jsonObj.getString("compid");
			//String branid = jsonObj.getString("branid");
			System.out.println("getData1 " + requestpara.toString());

			//=== call method
			
			String [] data = ManAcWithold.getReportService(_loginBean, requestpara);
			JSONObject jsonObjReturn = new JSONObject();
			jsonObjReturn.put("base64", data[0]);
			jsonObjReturn.put("format", data[1]);

			return jsonObjReturn.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			return ApiUtil.errorService(e);
		}

	}
	
	public static String getData2(JSONObject requestpara, LoginBean _loginBean) {

		try {
			//String compid = jsonObj.getString("compid");
			//String branid = jsonObj.getString("branid");
			System.out.println("getData2 " + requestpara.toString());

			//=== call method
			String [] data = ManAcWithold.getTextTax(_loginBean, requestpara);
			JSONObject jsonObjReturn = new JSONObject();
			jsonObjReturn.put("base64", data[0]);
			jsonObjReturn.put("format", data[1]);

			return jsonObjReturn.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			return ApiUtil.errorService(e);
		}

	}
	
	public static String getData11(JSONObject requestpara, LoginBean _loginBean) {

		try {
			//String compid = jsonObj.getString("compid");
			//String branid = jsonObj.getString("branid");
			System.out.println("getData11 " + requestpara.toString());

			//=== call method
			String [] data = ManAcWithold.getTaxForm(_loginBean, requestpara);
			JSONObject jsonObjReturn = new JSONObject();
			jsonObjReturn.put("base64", data[0]);
			jsonObjReturn.put("format", data[1]);

			return jsonObjReturn.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			return ApiUtil.errorService(e);
		}

	}
	
}

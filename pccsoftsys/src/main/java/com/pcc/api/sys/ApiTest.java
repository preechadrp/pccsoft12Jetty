package com.pcc.api.sys;

import org.json.JSONObject;

import com.pcc.api.core.ApiUtil;
import com.pcc.sys.tbo.TboFUSER;

public class ApiTest {

	public static String getData1(JSONObject requestpara, TboFUSER user) {

		try {
			//String compid = jsonObj.getString("compid");
			//String branid = jsonObj.getString("branid");
			//System.out.println("sevice1 " + jsonObj.toString());

			String comp_cde = requestpara.getString("comp_cde");
			String[] lst_product = ApiUtil.getArrayString(requestpara, "lst_product");
			java.sql.Date fromdate = ApiUtil.getSqlDate(requestpara, "fromdate");
			int option = ApiUtil.getInt(requestpara, "option");

			JSONObject jsonObjReturn = new JSONObject();

			jsonObjReturn.put("comp_cde", comp_cde);
			jsonObjReturn.put("lst_product", lst_product);
			jsonObjReturn.put("fromdate", fromdate);
			jsonObjReturn.put("option", option);

			//Thread.sleep(7000l); //test log use time ให้เกิน 5 วินาที

			return jsonObjReturn.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return ApiUtil.errorService(e);
		}

	}

}

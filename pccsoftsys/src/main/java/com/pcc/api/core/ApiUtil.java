package com.pcc.api.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Filedownload;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FConfig;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.tbf.TbfFCOMP;
import com.pcc.sys.tbf.TbfFUSER;
import com.pcc.sys.tbo.TboFCOMP;
import com.pcc.sys.tbo.TboFUSER;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiUtil {

	static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
	public static final String webserviceUrl = FConfig.getConfig2("WebserviceUrl");

	public static java.io.PrintWriter getPrintWriter(HttpServletResponse response) throws IOException {

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Credentials", "false");
		response.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST");
		//response.setHeader("Access-Control-Max-Age", "1209600");

		return response.getWriter();

	}

	/**
	 * อ่านข้อมูลที่ส่งมา
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static String readPostData(HttpServletRequest request) throws IOException {

		try (InputStreamReader itreader = new InputStreamReader(request.getInputStream() ,"UTF-8");
				BufferedReader in = new BufferedReader(itreader);) {

			String data = "";
			String line = null;
			while ((line = in.readLine()) != null) {
				data += line;
			}
			return data;

		}

	}

	/**
	 * ฟังก์ชั่นคืนค่า json กรณี webservice ทำงาน Error
	 * 
	 * @param ex
	 * @return
	 */
	public static String errorService(Exception ex) {
		JSONObject jsonObjReturn = new JSONObject();
		if (ex.getMessage() == null) {
			jsonObjReturn.put("errMsg", ex.getStackTrace()[0].toString());
		} else {
			jsonObjReturn.put("errMsg", ex.getMessage());
		}
		return jsonObjReturn.toString();
	}
	
	/**
	 * ฟังก์ชั่นคืนค่า json กรณี webservice ทำงาน Error
	 * @param errMsg
	 * @return
	 */
	public static JSONObject errorService(String errMsg) {
		JSONObject jsonObjReturn = new JSONObject();
		if (errMsg != null) {
			jsonObjReturn.put("errMsg", errMsg);
		} else {
			jsonObjReturn.put("errMsg", "Unknown Error");
		}
		return jsonObjReturn;
	}

	/**
	 * ตรวจว่าส่ง error มาหรือไม่
	 * @param jsonObj
	 * @return
	 */
	public static boolean isErrorService(JSONObject jsonObj) {
		if (jsonObj != null) {
			if (jsonObj.has("errMsg")) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * ดึง error ออกมา
	 * @param jsonObj
	 * @return
	 */
	public static String getErrorService(JSONObject jsonObj) {
		if (jsonObj != null) {
			if (jsonObj.has("errMsg")) {
				return jsonObj.getString("errMsg");
			}
		}
		return "";
	}

	/**
	 *  call api ไปที่ server  ใช้ HttpClient สำหรับ JAVA 11 ขึ้นไป
	 * @param jsonParameter พารามิเตอร์ที่เรียก service นั้นๆ
	 * @param _loginBean loginBean ปัจุุบัน 
	 * @param menuid2
	 * @param menu_thai_name
	 * @param requestdesc  คำอธิบายการเรียก service เช่นเงิื่อนไขรายงานที่เรียก เป็นต้น
	 * @param callApi
	 * @param svModuleName เช่น sys,gl เป็นต้น
	 * @return
	 * @throws Exception 
	 * @throws JSONException 
	 */
	public static JSONObject callWebServiceVer2(JSONObject jsonParameter, LoginBean _loginBean,
			String menuid2, String menu_thai_name, String requestdesc, String callApi, String svModuleName)
			throws JSONException, Exception {

		JSONObject jsonRoot = new JSONObject();
		jsonRoot.put("callapi", callApi);
		jsonRoot.put("callapiModule", svModuleName);
		jsonRoot.put("menu_id2", menuid2);
		jsonRoot.put("menu_thai_name", menu_thai_name);
		jsonRoot.put("user_id", _loginBean.getUSER_ID());
		jsonRoot.put("comp_cde", _loginBean.getCOMP_CDE());
		jsonRoot.put("requestdesc", requestdesc);
		jsonRoot.put("requestpara", jsonParameter);

		String urlParameters = jsonRoot.toString();
		log.debug("urlParameters :" + urlParameters);
		byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

		String url = webserviceUrl + "/appapi";
		//System.out.println("url : " + url);

		{ //new By HttpClient

			HttpClient client = HttpClient.newHttpClient();

			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(url))
					//.timeout(Duration.ofMinutes(1))
					.header("Content-Type", "application/json; charset=utf-8")
					.header("Authorization", "Bearer " + _loginBean.getJwt())
					.POST(BodyPublishers.ofByteArray(postData))
					.build();

			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

			int status = response.statusCode();
			//System.out.println("status : " + status);
			//200 = ผ่าน
			//401 = token key ไม่ถูก

			if (status == 200) {
				if (response.body().length() > 0) {
					JSONObject jsonReturn = new JSONObject(response.body());
					return jsonReturn;
				} else {
					return ApiUtil.errorService("Api ไม่มีการส่งข้อมูลกลับมา");
				}
			} else {
				return ApiUtil.errorService("เชื่อมต่อ Api ไม่ได้ (Error Code : " + status + ")");
			}

		}

	}

	/**
	 * ทำการ login
	 * 
	 * @param username
	 * @param password
	 * @return json web token
	 * @throws MalformedURLException
	 * @throws ProtocolException
	 * @throws IOException
	 */
	public static JSONObject postLogin(String username, String password)
			throws MalformedURLException, ProtocolException, IOException, Exception {

		JSONObject jsonRoot = new JSONObject();
		jsonRoot.put("username", username);
		jsonRoot.put("password", password);

		String urlParameters = jsonRoot.toString();
		// System.out.println("urlParameters :" + urlParameters);
		byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

		String url = webserviceUrl + "/auth/login";

		{ // new By HttpClient

			HttpClient client = HttpClient.newHttpClient();

			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(url))
					// .timeout(Duration.ofMinutes(1))
					.header("Content-Type", "application/json; charset=utf-8")
					.POST(BodyPublishers.ofByteArray(postData))
					.build();

			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

			int status = response.statusCode();
			// System.out.println("status : " + status);
			// 200 = ผ่าน
			// 401 = token key ไม่ถูก

			if (status == 200) {
				if (response.body().length() > 0) {
					JSONObject jsonReturn = new JSONObject(response.body());
					return jsonReturn;
				} else {
					return null;
				}
			} else {
				return null;
			}

		}

	}

	/**
	 * เรียก api ที่ต้องการ ซึ่งจะส่ง json ออกไปเสมอ
	 * @param request
	 * @param response
	 */
	public static void callApiResponseJsonPath(
			HttpServletRequest request, 
			HttpServletResponse response) {

		/*รูปแบบตัวอย่าง json ที่ส่งมา
		 * {
		 *   "callapi" : "ApiTest.getData1",  โดย ApiTest คือชื่อ class , getData1 คือชื่อ method 
		 *   "callapiModule" : "sys"
		 *   "menu_id2" : "glxxxxxxx",
		 *   "menu_thai_name" : "glxxxxxxx",
		 *   "user_id" : "glxxxxxxx",
		 *   "comp_cde" : "glxxxxxxx",      
		 *   "requestdesc" : "ขายจากวันที่ xxx ถึงวันที่ xxx ",
		 *   "requestpara" : {
		 *       "fromdate" : "2022-01-01" , 
		 *       "todate" : "2022-01-31" ,
		 *       ..... แปลผันตาม service นั้นๆ
		 *    }
		 * }
		 * 
		 */

		try (ApiLogUseTime apilogusetime = new ApiLogUseTime(ApiUtil.readPostData(request));
			 PrintWriter out = ApiUtil.getPrintWriter(response);) {

			//ค้นหา user ในระบบเพื่อ set ก่อนส่งไปประมวลผล
			String comp_cde = apilogusetime.getJsonObj().optString("comp_cde");
			String user_id = apilogusetime.getJsonObj().optString("user_id");
			//System.out.println(user_id);
			if (Fnc.isEmpty(user_id)) {
				throw new Exception("ไม่ระบุชื่อเข้าระบบ");
			}

			//=====ค้นหา user
			LoginBean _loginBean = new LoginBean();

			try (FDbc dbc = FDbc.connectMasterDb()) {
				var fcomp = new TboFCOMP();
				fcomp.setCOMP_CDE(comp_cde);
				if (!TbfFCOMP.find(dbc, fcomp)) {
					throw new Exception("ไม่พบบริษัทนี้");
				}
				_loginBean.setTboFcomp(fcomp);
							
				var user_tb = new TboFUSER();
				user_tb.setUSER_ID(user_id);
				if (TbfFUSER.find(dbc, user_tb)) {
					if (!user_tb.getUSER_STATUS().equals("1")) {
						throw new Exception("ผู้ใช้นี้ปิดการใช้งานแล้ว");
					}
				} else {
					throw new Exception(Labels.getLabel("login.label.errorUser"));
				}
				_loginBean.setTboFuser(user_tb);
			}

			//===== หา class , method ที่จะเรียก		
			String callApi = apilogusetime.getJsonObj().getString("callapi");
			String[] cls_and_method = callApi.split("\\.");
			String callapiModule = apilogusetime.getJsonObj().getString("callapiModule");
			String className =  "com.pcc.api." + callapiModule.toLowerCase()+ "." + cls_and_method[0];
			String methodName = cls_and_method[1];
			log.debug("className : " + className);
			log.debug("methodName : " + methodName);

			//====== เรียก api ที่ต้องการ
			Class<?> cls = null;
			try {
				cls = Class.forName(className);
			} catch (Exception e) {
				throw new Exception("ไม่พบ Api " + callApi);
			}
			Object obj = cls.getDeclaredConstructor().newInstance();
			Method mt = cls.getMethod(methodName, apilogusetime.getJsonObj().getClass(), _loginBean.getClass());

			// invoke ไม่สามารถส่ง error มาได้จึงต้องบังคับส่งเป็น string ออกมาแทน
			// รูปแบบ method ที่เรียกจะต้องมีรูปแบบ  public static String  methodName(JSONObject jsonObj, TboFUSER user)  เท่านั้น
			String returnData = Fnc.getStr(mt.invoke(obj, apilogusetime.getJsonObj().getJSONObject("requestpara"), _loginBean));

			out.println(returnData);
			
			log.debug("===> end reponse");

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			try(PrintWriter out = ApiUtil.getPrintWriter(response);){
				out.println(ApiUtil.errorService(e));
			} catch (Exception ex) {
				//nothing
			}
		}

	}

	/**
	 * แปลง json array ให้เป็น array ของ String 
	 * @param jsonObj เช่น  "key":["bb","bb","cc"] เป็นต้น
	 * @param key
	 * @return array string
	 */
	public static String[] getArrayString(JSONObject jsonObj, String key) {
		String[] ret = {};
		if (jsonObj.has(key)) {
			JSONArray arr_brand = jsonObj.getJSONArray(key);
			if (arr_brand.length() > 0) {
				for (Object obj : arr_brand) {
					ret = Fnc.appendStringArray(ret, obj.toString());
				}
			}
		}
		return ret;
	}

	/**
	 * แปลง json array ให้เป็น List ของ String 
	 * @param jsonObj ตัวอย่าง "key":["bb","bb","cc"] เป็นต้น
	 * @param key
	 * @return List&lt;String&gt;
	 */
	public static java.util.List<String> getListString(JSONObject jsonObj, String key) {
		java.util.List<String> ret = new java.util.ArrayList<>();
		if (jsonObj.has(key)) {
			JSONArray arr_brand = jsonObj.getJSONArray(key);
			if (arr_brand.length() > 0) {
				for (Object obj : arr_brand) {
					ret.add(obj.toString());
				}
			}
		}
		return ret;
	}

	/**
	 * ดึงค่าวันที่จาก jsonobject
	 * @param jsonObj
	 * @param key
	 * @return
	 */
	public static java.sql.Date getSqlDate(JSONObject jsonObj, String key) {
		if (jsonObj.has(key)) {
			return FnDate.getDateByStringYYYY_MM_DD(jsonObj.getString(key));
		} else {
			return null;
		}
	}

	/**
	 * ดึงตัวเลข (int)จาก json object
	 * @param jsonObj
	 * @param key
	 * @return
	 */
	public static int getInt(JSONObject jsonObj, String key) {
		if (jsonObj.has(key)) {
			return jsonObj.getInt(key);
		} else {
			return 0;
		}
	}

	/**
	 * ส่งไฟล์ต่างๆ ออกไปเพื่อให้ user save
	 * @param data  เป็น string ที่เข้ารหัส Base64 เท่านั้น
	 * @param format นามสกุลไฟล์
	 * @param menu_id2 รหัสโปรแกรม
	 */
	public static void loadFile(String data, String format, String menu_id2) {
		byte[] decode = java.util.Base64.getDecoder().decode(data);
		Filedownload.save(decode, "application/octet-stream",
				menu_id2 + "_T" + FnDate.getTodayDateTime("HHmmss") + "." + format);
	}

	public static void TestCallWebServiceVer2(LoginBean _loginBean) throws JSONException, Exception {

		String menuid2 = "GlEntr";
		String menu_thai_name = "บันทึกบัญชี";
		String requestdesc = "test ขายจากวันที่ xxx ถึงวันที่ xxx ";
		String callApi = "ApiTest.getData1";
		String svModuleName = "sys";

		JSONObject jsonParameter = new JSONObject();

		jsonParameter.put("comp_cde", _loginBean.getCOMP_CDE());
		jsonParameter.put("fromdate", "2022-02-01");
		jsonParameter.put("option", 1);

		String[] lst_product = { "aa", "bb", "cc" };
		jsonParameter.put("lst_product", lst_product);
		
		//JSONObject json = callWebService(jsonParameter, _loginBean, menuid2, menu_thai_name, requestdesc, callApi, svModuleName);
		JSONObject json = callWebServiceVer2(jsonParameter, _loginBean, menuid2, menu_thai_name, requestdesc, callApi, svModuleName);
		if (json != null) {
			System.out.println(json.toString());
		}

	}

}

/*
 * CREATE TABLE `apilogusetime` (
    `INSDTE` datetime(3) NOT NULL,
    `USETIME` bigint(20) NOT NULL COMMENT 'เวลาที่ใช้มีหน่วยเป็น milisecond',
    `MENU_ID2` varchar(60) NOT null comment 'รหัสโปรแกรมที่เรียก' ,
    `MENU_THAI_NAME` varchar(500) DEFAULT null comment 'ชื่อโปรแกรมที่เรียก',
    `USER_ID` varchar(30) DEFAULT null comment 'ชื่อ userlogin',
    `COMP_CDE` varchar(10) DEFAULT null comment 'รหัสบริษัที่ login อยู่',
    `REQUESTDESC` varchar(500) DEFAULT null comment 'คำอธิบายการเรียก web service',
    `LOG_STATUS` varchar(15) DEFAULT null comment 'สถานะ proccessing , completed',
    PRIMARY KEY (`INSDTE`,`MENU_ID2`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='เก็บ Log เวลาที่นานเกินกว่าที่กำหนด';
 */

package com.pcc.api.core;

import org.json.JSONObject;

import com.pcc.sys.lib.FDbc;

public class ApiLogUseTime implements AutoCloseable {

	private static final long maxMilliSecond = 5 * 1000;// เกินกี่วินาทีจึงจะ log เวลา
	private long startTime = 0l;
	private long elapsed = 0l;
	private JSONObject jsonObj;
	private boolean inserted_Log = false;

	public ApiLogUseTime(String requestData) {
		startTime = System.currentTimeMillis();

		JSONObject jsonObj = new JSONObject(requestData);
		this.setJsonObj(jsonObj);

		Thread thread1 = new Thread(() -> {
			try {
				//System.out.println("thread1 is running");
				Thread.sleep(maxMilliSecond);
				//System.out.println("thread1 wake up");

				//ถึง maxMilliSecond ถ้ายังไม่ inserted_Log ให้ insert ไว้ก่อน
				if (inserted_Log == false) {
					insertLog();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		thread1.start();

	}

	public JSONObject getJsonObj() {
		return jsonObj;
	}

	public void setJsonObj(JSONObject jsonObj) {
		this.jsonObj = jsonObj;
	}

	private void insertLog() throws Exception {

		inserted_Log = true;

		// ทำการ log time
		elapsed = System.currentTimeMillis() - startTime;
		// System.out.println("elapsed " + elapsed);

		//if (elapsed >= 2) {//test

		try (FDbc dbc = FDbc.connectMasterDb()) {
			//17/12/67  firebirdsql ต้องใส่ into  หลังคำสั่ง insert ลักษณะนี้
			String sqlInsert = "insert into apilogusetime(INSDTE,USETIME,MENU_ID2,MENU_THAI_NAME,USER_ID,COMP_CDE,REQUESTDESC,LOG_STATUS)"
					+ "values(?,?,?,?,?,?,?,?)";
			try (java.sql.PreparedStatement pst = dbc.getPreparedStatement(sqlInsert);) {
				int idx = 1;
				pst.setTimestamp(idx++, new java.sql.Timestamp(startTime));// INSDTE
				pst.setLong(idx++, elapsed);// USETIME
				pst.setString(idx++, this.jsonObj.getString("menu_id2"));// MENU_ID2
				pst.setString(idx++, this.jsonObj.getString("menu_thai_name"));// MENU_THAI_NAME
				pst.setString(idx++, this.jsonObj.getString("user_id"));// USER_ID
				pst.setString(idx++, this.jsonObj.getString("comp_cde"));// COMP_CDE
				pst.setString(idx++, this.jsonObj.getString("requestdesc"));// REQUESTDESC
				pst.setString(idx++, "proccessing");// LOG_STATUS
				pst.executeUpdate();
			}

		}

	}

	private void updateLog() {

		//System.out.println("updateLog");

		// ทำการ log time
		elapsed = System.currentTimeMillis() - startTime;
		//System.out.println("elapsed " + elapsed);

		String sql_update = " update apilogusetime set USETIME=? ,LOG_STATUS=? where INSDTE=? and MENU_ID2=? ";

		try (FDbc dbc = FDbc.connectMasterDb();
				java.sql.PreparedStatement ps = dbc.getPreparedStatement(sql_update);) {

			ps.setLong(1, elapsed); // USETIME เวลา
			ps.setString(2, "completed"); //LOG_STATUS	
			ps.setTimestamp(3, new java.sql.Timestamp(startTime));// INSDTE
			ps.setString(4, this.jsonObj.getString("menu_id2")); //MENU_ID2

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void close() throws Exception {
		//System.out.println("LogUseTime.close()");
		if (inserted_Log == true) {
			updateLog();
		} else {
			inserted_Log = true; //ป้องกันไม่ให้ thread1 เรียก insertLog()
		}
	}

}

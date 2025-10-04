/* โครงสร้างตาราง
CREATE TABLE `logusetime` (
	`INSDTE` datetime(3) NOT NULL,
	`USETIME` bigint(20) NOT NULL COMMENT 'เวลาที่ใช้มีหน่วยเป็น milisecond',
	`LOG_STATUS` varchar(50) DEFAULT NULL,
	`MENU_ID` varchar(50) NOT NULL,
	`MENU_NAME` varchar(200) DEFAULT NULL,
	`USER_ID` varchar(30) DEFAULT NULL,
	`COMP_CDE` varchar(5) DEFAULT NULL,
	`LOG_REMARK` varchar(500) DEFAULT NULL,
PRIMARY KEY (`INSDTE`,`MENU_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=tis620;
 */

package com.pcc.sys.lib;

import com.pcc.sys.beans.LoginBean;

public class FLogUsedTime implements AutoCloseable {

	private long startTime = 0l;
	private long elapsed = 0l;
	private static final long maxMilliSecond = 5 * 1000;// เกินกี่วินาทีจึงจะ log เวลา
	private String menu_id;
	private String menu_name;
	private LoginBean loginBean;
	private String log_remark;
	private boolean inserted_Log = false;

	/**
	 * กำหนดค่า
	 * @param loginBean
	 * @param menu_id รหัสโปรแกรม
	 * @param menu_name ชื่อโปรแกรม
	 * @param log_remark  คำอธิบายหรือหมายเหตุ
	 */
	public FLogUsedTime(LoginBean loginBean, String menu_id, String menu_name, String log_remark) {

		this.startTime = System.currentTimeMillis();
		this.loginBean = loginBean;
		this.menu_id = menu_id;
		this.menu_name = menu_name;
		this.log_remark = log_remark;
		
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

	@Override
	public void close() {
		//System.out.println("LogUseTime.close()");
		if (inserted_Log == true) {
			updateLog();
		} else {
			inserted_Log = true; //ป้องกันไม่ให้ thread1 เรียก insertLog()
		}
	}

	private void insertLog() {

		//System.out.println("insertLog");
		inserted_Log = true; //flag ค่าช่วยตรวจสอบ

		// ทำการ log time
		elapsed = System.currentTimeMillis() - startTime;
		//System.out.println("elapsed " + elapsed);
		if (elapsed >= maxMilliSecond) {

			String sql_insert = " INSERT INTO logusetime (INSDTE, USETIME, LOG_STATUS, MENU_ID, MENU_NAME"
					+ ", USER_ID, COMP_CDE, LOG_REMARK)"
					+ " VALUES(?,?,?,?,?,?,?,?)";

			try (FDbc dbc = FDbc.connectMasterDb();
					java.sql.PreparedStatement ps = dbc.getPreparedStatement(sql_insert);) {

				ps.setTimestamp(1, new java.sql.Timestamp(startTime));// INSDTE
				ps.setLong(2, elapsed); // USETIME เวลา
				ps.setString(3, "proccessing"); //LOG_STATUS
				ps.setString(4, this.menu_id); //MENU_ID
				ps.setString(5, this.menu_name); //MENU_NAME
				ps.setString(6, this.loginBean.getUSER_ID()); //USER_ID
				ps.setString(7, this.loginBean.getCOMP_CDE()); //COMP_CDE
				ps.setString(8, Fnc.getStrLeft(this.log_remark, 500)); //LOG_REMARK

				ps.executeUpdate();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private void updateLog() {

		//System.out.println("updateLog");

		// ทำการ log time
		elapsed = System.currentTimeMillis() - startTime;
		//System.out.println("elapsed " + elapsed);

		String sql_update = " update logusetime set USETIME=? ,LOG_STATUS=? where INSDTE=? and MENU_ID=? ";

		try (FDbc dbc = FDbc.connectMasterDb();
				java.sql.PreparedStatement ps = dbc.getPreparedStatement(sql_update);) {

			ps.setLong(1, elapsed); // USETIME เวลา
			ps.setString(2, "completed"); //LOG_STATUS	
			ps.setTimestamp(3, new java.sql.Timestamp(startTime));// INSDTE
			ps.setString(4, this.menu_id); //MENU_ID

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

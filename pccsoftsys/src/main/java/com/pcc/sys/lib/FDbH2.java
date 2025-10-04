package com.pcc.sys.lib;

import java.sql.Connection;

public class FDbH2 extends FDbcMaster {

	public static FDbH2 connect() throws Exception {
		FDbH2 dbH = new FDbH2();
		dbH.connectDb();
		return dbH;
	}

	public void connectDb() throws Exception {

		String driverName = "org.h2.Driver";
		String urlConnect = "jdbc:h2:mem:";
		String userName = "sa";
		String passWord = "sa";

		Class.forName(driverName);
		objConn = java.sql.DriverManager.getConnection(urlConnect, userName, passWord);
		objConn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
	}

	@Override
	public void close() {
		System.out.println("disconnect DbH");
		try {
			if (!objConn.isClosed()) {
				objConn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

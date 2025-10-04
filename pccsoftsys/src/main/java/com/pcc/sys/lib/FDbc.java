package com.pcc.sys.lib;

import java.sql.Connection;

public class FDbc extends FDbcMaster {

	private static final String MASTERDB_NAME = FConfig.getConfig2_MasterDb();

	/**
	 * เรียกแบบไม่ต้อง new ใช้ใน try{..}
	 * @return FDbc ที่เชื่อมแล้ว
	 * @throws Exception
	 */
	public static FDbc connectMasterDb() throws Exception {
		FDbc dbc = new FDbc();
		if (FConstComm.runAppMode == 1) {
			dbc.connect2();
		} else {
			dbc.connect1("MASTERDB");
		}
		return dbc;
	}

	/**
	 * ทำการเชื่อมต่อ databaseName
	 * @param dbname
	 * @throws Exception
	 */
	private void connect1(String dbname) throws Exception {
		javax.sql.DataSource ds = null;

		try {

			javax.naming.Context initCtx = new javax.naming.InitialContext();
			javax.naming.Context envCtx = (javax.naming.Context) initCtx.lookup("java:comp/env");
			if (dbname.equals("MASTERDB")) {
				ds = (javax.sql.DataSource) envCtx.lookup(MASTERDB_NAME);
			} else {
				ds = (javax.sql.DataSource) envCtx.lookup("jdbc/" + dbname);
			}

			objConn = ds.getConnection();
			objConn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

		} catch (java.sql.SQLException ex) {
			throw new Exception(ex.getMessage());
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}

	}

	private void connect2() throws Exception {

		try {

			String driverName = "org.firebirdsql.jdbc.FBDriver";
			String urlConnect = "jdbc:firebird://localhost:3050/D:/pccsoft12Jetty/TSN.FDB?encoding=UTF8";
			if (FOsUtil.isUnix()) {
				urlConnect = "jdbc:firebird://localhost:3050//var/lib/firebird/data/TSNTEST.FDB?encoding=UTF8";
			}
			String userName = "sysdba";
			String passWord = "masterkey";

			Class.forName(driverName);
			objConn = java.sql.DriverManager.getConnection(urlConnect, userName, passWord);
			if (objConn == null) {
				logger.info("dbc.objConn == null");
			}
			objConn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

		} catch (java.sql.SQLException ex) {
			throw new Exception(ex.getMessage());
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}

	}
	
	public static FDbc connectByConsole() throws Exception {

		try {

			FDbc dbc = new FDbc();

			String driverName = "org.firebirdsql.jdbc.FBDriver";
			String urlConnect = "jdbc:firebird://localhost:3050/D:/Firebird/databaseV5x/TSN.FDB?encoding=UTF8";
			String userName = "sysdba";
			String passWord = "masterkey";

			Class.forName(driverName);
			dbc.objConn = java.sql.DriverManager.getConnection(urlConnect, userName, passWord);
			dbc.objConn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

			return dbc;

		} catch (java.sql.SQLException ex) {
			throw new Exception(ex.getMessage());
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}

	}

}

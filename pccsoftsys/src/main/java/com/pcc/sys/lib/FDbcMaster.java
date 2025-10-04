package com.pcc.sys.lib;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

public abstract class FDbcMaster implements AutoCloseable {
	
	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	// Connection.TRANSACTION_SERIALIZABLE
	// Connection.TRANSACTION_NONE
	// Connection.TRANSACTION_READ_UNCOMMITTED
	// Connection.TRANSACTION_REPEATABLE_READ
	// Connection.TRANSACTION_READ_COMMITTED

	public FDbcMaster() {
		//แก้ปัญหาเรื่อง prepare statment ( setdate ,settimestamp ) 
		//สำคัญมากถ้าเกี่ยวกับการใช้ JDBC เชื่อมฐานข้อมูล 
		//ทดสอบเมื่อ 5/3/63 เมนูบันทึกบัญชี GL(สมุดรายวัน)
		if (Locale.getDefault() != Locale.ENGLISH) {
			Locale.setDefault(Locale.ENGLISH);
			logger.debug("Locale.setDefault(Locale.ENGLISH)");
		}
	}

	protected Connection objConn = null;

	/**
	 * เวลารอหลังสั่ง execute sql (default) (วินาที)
	 */
	private int queryTimeOut = 60;

	/**
	 * ทำการ Disconnect database
	 */
	public void disconnect() {
		try {
			if (objConn != null) {
				if (objConn.isClosed() == false) {
					if (objConn.getAutoCommit() == false) {
						//System.out.println("rollback on disconnect");
						objConn.rollback();
						objConn.setAutoCommit(true);
						objConn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
					}
					objConn.close();
					objConn = null;
				}
			}
		} catch (Exception ex) {
		}
	}

	public void close() {
		//System.out.println("disconnect on autoclose");
		disconnect();
	}

	/**
	 * ตรวจสอบว่าเชื่อม Database หรือไม่
	 * @param conn
	 * @return boolean
	 */
	public boolean isConnected() {
		boolean rt = false;
		try {
			if (objConn != null) {
				if (objConn.isClosed() == false) {
					rt = true;
				}
			}
		} catch (Exception ex) {
		}
		return rt;
	}

	/**
	 * Start transaction process
	 * @throws SQLException
	 */
	public void beginTrans() throws SQLException {
		logger.debug("beginTrans()");
		objConn.setAutoCommit(false);
		objConn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
	}

	/**
	 * commit transaction process
	 * @throws SQLException
	 */
	public void commit() throws SQLException {
		logger.debug("commit()");
		objConn.commit();
		objConn.setAutoCommit(true);
		objConn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
	}

	/**
	 * rollback transaction process
	 */
	public void rollback() {
		System.out.println("rollback()");
		try {
			if (objConn.getAutoCommit() == false) {
				objConn.rollback();
			}
		} catch (SQLException ex) {
		}
		try {
			objConn.setAutoCommit(true);
			objConn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
		} catch (SQLException ex) {
		}
	}

	/**
	 * SQL = UPDATE , DELETE , INSERT
	 * @param sql
	 * @return จำนวน record ที่มีการเปลี่ยนแปลง
	 * @throws SQLException
	 */
	public int executeSql(String sql) throws SQLException {
		java.sql.Statement stm = objConn.createStatement();
		stm.setQueryTimeout(this.getQueryTimeOut());
		return stm.executeUpdate(sql);
	}

	/**
	 * SQL = UPDATE , DELETE , INSERT
	 * @param sql
	 * @param values ojbect array
	 * @return จำนวน record ที่มีการเปลี่ยนแปลง
	 * @throws SQLException
	 */
	public int executeSql2(String sql, Object... values) throws SQLException {
		java.sql.PreparedStatement stm = objConn.prepareStatement(sql);

		int idx = 1;
		for (Object obj : values) {
			stm.setObject(idx++, obj);
		}

		stm.setQueryTimeout(this.getQueryTimeOut());

		return stm.executeUpdate();
	}

	/**
	 * ResultSet
	 * @param sql
	 * @return ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, 
	 * ResultSet.HOLD_CURSORS_OVER_COMMIT
	 * @throws SQLException
	 */
	public ResultSet getResultSet(String sql) throws SQLException {
		java.sql.Statement stm = objConn.createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY,
				ResultSet.HOLD_CURSORS_OVER_COMMIT);
		stm.setQueryTimeout(this.getQueryTimeOut());
		return stm.executeQuery(sql);
	}

	/**
	 * ResultSet
	 * @param sql
	 * @param values ojbect array
	 * @return ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, 
	 * ResultSet.HOLD_CURSORS_OVER_COMMIT
	 * @throws SQLException
	 */
	public ResultSet getResultSet2(String sql, Object... values) throws SQLException {

		java.sql.PreparedStatement stm = objConn.prepareStatement(
				sql,
				ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY,
				ResultSet.HOLD_CURSORS_OVER_COMMIT);

		int idx = 1;
		for (Object obj : values) {
			stm.setObject(idx++, obj);
		}

		stm.setQueryTimeout(this.getQueryTimeOut());

		return stm.executeQuery();
	}

	/**
	 * ResultSet
	 * @param sql
	 * @param maxRow จำนวน record ที่ต้องการดึงมา
	 * @return ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, 
	 * ResultSet.HOLD_CURSORS_OVER_COMMIT
	 * @throws Exception
	 */
	public ResultSet getResultSet3(String sql, int maxRow) throws Exception {
		try {
			java.sql.Statement stm = objConn.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY,
					ResultSet.HOLD_CURSORS_OVER_COMMIT);
			stm.setMaxRows(maxRow);
			stm.setQueryTimeout(this.getQueryTimeOut());
			return stm.executeQuery(sql);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * ResultSet
	 * @param sql
	 * @param maxRow จำนวน record ที่ต้องการดึงมา
	 * @param values ojbect array
	 * @return ResultSet.TYPE_SCROLL_INSENSITIVE, 
	 * ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT
	 * @throws Exception
	 */
	public ResultSet getResultSet4(String sql, int maxRow, Object... values) throws Exception {
		try {

			java.sql.PreparedStatement stm = objConn.prepareStatement(
					sql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY,
					ResultSet.HOLD_CURSORS_OVER_COMMIT);

			int idx = 1;
			for (Object obj : values) {
				stm.setObject(idx++, obj);
			}

			stm.setQueryTimeout(this.getQueryTimeOut());
			stm.setMaxRows(maxRow);

			return stm.executeQuery();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * ResultSet
	 * @param sql
	 * @return ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, 
	 * ResultSet.HOLD_CURSORS_OVER_COMMIT
	 * @throws SQLException
	 */
	public ResultSet getResultSetFw(String sql) throws SQLException {
		java.sql.Statement stm = objConn.createStatement(
				ResultSet.TYPE_FORWARD_ONLY,
				ResultSet.CONCUR_READ_ONLY,
				ResultSet.HOLD_CURSORS_OVER_COMMIT);
		stm.setQueryTimeout(this.getQueryTimeOut());
		return stm.executeQuery(sql);
	}

	/**
	 * ResultSet
	 * @param sql
	 * @param values ojbect array
	 * @return ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, 
	 * ResultSet.HOLD_CURSORS_OVER_COMMIT
	 * @throws SQLException
	 */
	public ResultSet getResultSetFw2(String sql, Object... values) throws SQLException {

		java.sql.PreparedStatement stm = objConn.prepareStatement(
				sql,
				ResultSet.TYPE_FORWARD_ONLY,
				ResultSet.CONCUR_READ_ONLY,
				ResultSet.HOLD_CURSORS_OVER_COMMIT);

		int idx = 1;
		for (Object obj : values) {
			stm.setObject(idx++, obj);
		}

		stm.setQueryTimeout(this.getQueryTimeOut());
		return stm.executeQuery();

	}

	/**
	 * ResultSet
	 * @param sql
	 * @param maxRow จำนวน record ที่ต้องการดึงมา
	 * @return ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, 
	 * ResultSet.HOLD_CURSORS_OVER_COMMIT
	 * @throws Exception
	 */
	public ResultSet getResultSetFw3(String sql, int maxRow) throws Exception {
		try {
			java.sql.Statement stm = objConn.createStatement(
					ResultSet.TYPE_FORWARD_ONLY,
					ResultSet.CONCUR_READ_ONLY,
					ResultSet.HOLD_CURSORS_OVER_COMMIT);
			stm.setMaxRows(maxRow);
			stm.setQueryTimeout(this.getQueryTimeOut());
			return stm.executeQuery(sql);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * ResultSet
	 * @param sql
	 * @param maxRow จำนวน record ที่ต้องการดึงมา
	 * @param values object array
	 * @return ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, 
	 * ResultSet.HOLD_CURSORS_OVER_COMMIT
	 * @throws Exception
	 */
	public ResultSet getResultSetFw4(String sql, int maxRow, Object... values) throws Exception {
		try {
			java.sql.PreparedStatement stm = objConn.prepareStatement(
					sql,
					ResultSet.TYPE_FORWARD_ONLY,
					ResultSet.CONCUR_READ_ONLY,
					ResultSet.HOLD_CURSORS_OVER_COMMIT);

			int idx = 1;
			for (Object obj : values) {
				stm.setObject(idx++, obj);
			}

			stm.setMaxRows(maxRow);
			stm.setQueryTimeout(this.getQueryTimeOut());

			return stm.executeQuery();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * ResultSet  เปิด resultset เพื่อ crud ข้อมูล
	 * @param sql
	 * @return ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE, 
	 * ResultSet.HOLD_CURSORS_OVER_COMMIT
	 * @throws SQLException
	 */
	public ResultSet getResultSetCRUD(String sql) throws SQLException {
		java.sql.Statement stm = objConn.createStatement(
				ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_UPDATABLE,
				ResultSet.HOLD_CURSORS_OVER_COMMIT);
		stm.setQueryTimeout(this.getQueryTimeOut());
		return stm.executeQuery(sql);
	}

	/**
	 * สร้าง PreparedStatement
	 * @param conn
	 * @param sql
	 * @return PreparedStatement
	 * @throws SQLException
	 */
	public PreparedStatement getPreparedStatement(String sql) throws SQLException {
		java.sql.PreparedStatement stm = objConn.prepareStatement(sql);
		stm.setQueryTimeout(this.getQueryTimeOut());
		return stm;
	}

	/**
	 * สร้าง Statement ที่เป็น ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, 
	 * ResultSet.HOLD_CURSORS_OVER_COMMIT
	 * @return
	 * @throws Exception
	 */
	public java.sql.Statement getStatement() throws Exception {
		try {
			java.sql.Statement stm = objConn.createStatement(
					ResultSet.TYPE_FORWARD_ONLY,
					ResultSet.CONCUR_READ_ONLY,
					ResultSet.HOLD_CURSORS_OVER_COMMIT);
			stm.setQueryTimeout(this.getQueryTimeOut());
			return stm;
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * ใช้ช่วย count หาจำนวน record
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public Integer getRecordCount(String sql) throws SQLException {
		Integer rt = 0;

		try (java.sql.ResultSet rs = this.getResultSet(sql);) {
			if (rs.next()) {
				rt = rs.getInt(1);
			}
		}

		return rt;
	}

	/**
	 * ใช้ช่วย count หาจำนวน record
	 * @param sql
	 * @param values
	 * @return
	 * @throws SQLException
	 */
	public Integer getRecordCount2(String sql, Object... values) throws SQLException {
		Integer rt = 0;

		try (java.sql.ResultSet rs = this.getResultSetFw2(sql, values);) {
			if (rs.next()) {
				rt = rs.getInt(1);
			}
		}

		return rt;
	}

	/**
	 * เวลารอเมื่อสั่งคำสั่ง sql ต่างๆ ไปยัง Database 
	 * @return วินาที
	 */
	public int getQueryTimeOut() {
		return queryTimeOut;
	}

	/**
	 * กำหนดเวลารอเมื่อสั่งคำสั่ง sql ต่างๆ ไปยัง Database
	 * @param queryTimeOut วินาที (ค่าปกติคือ 10 วินาที)
	 */
	public void setQueryTimeOut(int queryTimeOut) {
		this.queryTimeOut = queryTimeOut;
	}

	/**
	 * ชื่อปัจจุบันของ Database ที่ใช้
	 * @throws SQLException 
	 */
	public String getDbName() throws SQLException {

		String result = "";
		if (getDbType(objConn) == 4) {
			String sql = " select rdb$get_context('SYSTEM', 'DB_NAME') from rdb$database ";
			try (java.sql.ResultSet rs = this.getResultSet(sql);) {
				if (rs.next()) {
					String s1 = rs.getString(1);

					//System.out.println(s1);
					//กรณีเป็น Windows เช่น D:\FIREBIRD\DATABASE\PCCDB2X5.FDB  
					//กรณีเป็น Linux เช่น /var/lib/firebird/PCCDB2X5.FDB

					String[] tmp1 = s1.replace("\\", "/").split("/");
					String data1 = tmp1[tmp1.length - 1];
					String[] tmp2 = data1.split("\\.");
					result = tmp2[0];
				}
			}
		} else {
			result = objConn.getCatalog();//Support = Mssql, Mysql, Mariadb, PostgreSQL
			//Mssql script = SELECT DB_NAME() AS DataBaseName
			//Mariadb script = SELECT DATABASE()
		}
		return result;

	}

	/**
	 * ชนิดฐานข้อมูลที่เชื่อมอยู่
	 * @return 1=mariadb,2=sqlserver,3=postgresql,4=firebirdsql
	 * @throws SQLException 
	 */
	public int getUseDbType() throws SQLException {
		return getDbType(this.objConn);
	}

	/**
	 * ชนิดฐานข้อมูล เช่น MSSQL, ORACLE, MYSQL
	 * @return 1=mariadb,2=sqlserver,3=postgresql,4=firebirdsql
	 * @throws SQLException 
	 */
	protected static int getDbType(Connection conn1) throws SQLException {
		int ret = 0;

		//ได้ทั้ง factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"  
		//และ factory="org.apache.tomcat.dbcp.dbcp.BasicDataSourceFactory"
		String url = conn1.getMetaData().getURL();
		//System.out.println("url = " + url);

		if (url.contains("jdbc:mariadb")) {
			//jdbc:mariadb://127.0.0.1:3306/pccdb?useUnicode=true&serverTimezone=UTC, UserName=root, MariaDB connector/J
			ret = 1;
		} else if (url.contains("jdbc:sqlserver")) {
			//jdbc:sqlserver://192.168.200.27:1433;databaseName=shkmaster600528A;applicationName=shkdev;
			ret = 2;
		} else if (url.contains("jdbc:postgresql")) {
			//jdbc:postgresql://127.0.0.1:5432/db1
			ret = 3;
		} else if (url.contains("jdbc:firebird")) {
			//jdbc:firebirdsql:java://127.0.0.1/D:/Firebird/database/PCCDB2x5.FDB, UserName=SYSDBA, Jaybird JCA/JDBC driver
			ret = 4;
		}
		return ret;
	}

}

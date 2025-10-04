package com.pcc.sys.lib;

public class SqlStr {
	private String sql = "";

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public void addLine(String sql) {
		if (this.sql.equals("")) {
			this.sql += sql;
		} else {
			this.sql += "\n" + sql;
		}
	}

	public static void main(String[] aa) {
		SqlStr sql = new SqlStr();

		sql.addLine("select *");
		sql.addLine("from");
		sql.addLine("abc");

		System.out.println(sql.getSql());
		System.out.println(sql.getSql().replace("from", "bbbb"));
	}
}

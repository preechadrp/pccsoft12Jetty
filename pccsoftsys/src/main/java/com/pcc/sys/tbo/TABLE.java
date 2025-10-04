package com.pcc.sys.tbo;

public abstract class TABLE {

	public String schema = "";

	public TABLE() {
	}

	public TABLE(String schema) {
		this.schema = schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getSchema() {
		return schema;
	}

	public abstract String getTableName();

	protected String getStr(Object obj) {
		if (obj instanceof String) {
			return (String) obj;
		} else {
			return "";
		}
	}

}

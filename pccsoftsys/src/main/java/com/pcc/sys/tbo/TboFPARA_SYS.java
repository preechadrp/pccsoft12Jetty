package com.pcc.sys.tbo;

public class TboFPARA_SYS extends TABLE {

	private String PARA_ID;
	private String PARA_VALUE;
	private String PARA_DESC;

	public static final String tablename = "fpara_sys";

	public String getTableName() {
		return schema + tablename;
	}

	public TboFPARA_SYS() {
		super();
	}

	public String getPARA_ID() {
		return this.PARA_ID;
	}

	public void setPARA_ID(String para_id) {
		this.PARA_ID = para_id;
	}

	public String getPARA_VALUE() {
		return this.PARA_VALUE;
	}

	public void setPARA_VALUE(String para_value) {
		this.PARA_VALUE = para_value;
	}

	public String getPARA_DESC() {
		return this.PARA_DESC;
	}

	public void setPARA_DESC(String para_desc) {
		this.PARA_DESC = para_desc;
	}
}
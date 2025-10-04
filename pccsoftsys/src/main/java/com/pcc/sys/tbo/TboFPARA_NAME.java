package com.pcc.sys.tbo;

public class TboFPARA_NAME extends TABLE {

	private String PARA_ID;
	private String PARA_DESC;
	private String SYS_USE;
	private String DATA_TYPE;

	public static final String tablename = "fpara_name";

	public String getTableName() {
		return schema + tablename;
	}

	public TboFPARA_NAME() {
		super();
	}

	public String getPARA_ID() {
		return this.PARA_ID;
	}

	public void setPARA_ID(String para_id) {
		this.PARA_ID = para_id;
	}

	public String getPARA_DESC() {
		return this.PARA_DESC;
	}

	public void setPARA_DESC(String para_desc) {
		this.PARA_DESC = para_desc;
	}

	public String getSYS_USE() {
		return this.SYS_USE;
	}

	public void setSYS_USE(String sys_use) {
		this.SYS_USE = sys_use;
	}

	public String getDATA_TYPE() {
		return this.DATA_TYPE;
	}

	public void setDATA_TYPE(String data_type) {
		this.DATA_TYPE = data_type;
	}
}
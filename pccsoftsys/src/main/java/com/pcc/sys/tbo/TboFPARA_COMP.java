package com.pcc.sys.tbo;

public class TboFPARA_COMP extends TABLE {

	private String COMP_CDE;
	private String PARA_ID;
	private String PARA_VALUE;

	public static final String tablename = "fpara_comp";

	public String getTableName() {
		return schema + tablename;
	}

	public TboFPARA_COMP() {
		super();
	}

	public String getCOMP_CDE() {
		return this.COMP_CDE;
	}

	public void setCOMP_CDE(String comp_cde) {
		this.COMP_CDE = comp_cde;
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
}
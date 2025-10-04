package com.pcc.sys.lib;

public class FTest {

	public static void main(String[] args) {
		FDbc dbc = new FDbc();
		aa(dbc);//ทดสอบรับด้วย super class 

	}
	
	public static void aa(FDbcMaster d) {
		System.out.println("ok");
	}

}

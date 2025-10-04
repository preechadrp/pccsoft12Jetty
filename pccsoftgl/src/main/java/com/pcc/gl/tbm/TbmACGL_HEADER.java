package com.pcc.gl.tbm;

import com.pcc.gl.tbo.TboACGL_HEADER;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.SqlStr;

public class TbmACGL_HEADER {

	public static void getDataQry(java.util.List<FModelHasMap> dats, String vou_type, String vou_no,
			java.sql.Date postdateFrom, java.sql.Date postdateTo, String recsta, LoginBean _loginBean)
			throws Exception {

		dats.clear();
		try (FDbc dbc = FDbc.connectMasterDb()) {

			SqlStr sql = new SqlStr();
			sql.addLine("select * from " + TboACGL_HEADER.tablename);
			sql.addLine(" where 1=1 ");
			sql.addLine(" and COMP_CDE = '" + _loginBean.getCOMP_CDE() + "' ");
			if (!Fnc.isEmpty(vou_type)) {
				sql.addLine(" and VOU_TYPE  = '" + Fnc.sqlQuote(vou_type) + "' ");
			}
			if (!Fnc.isEmpty(vou_no)) {
				sql.addLine(" and VOU_NO like '" + Fnc.sqlQuote(vou_no) + "%' ");
			}

			if (postdateFrom != null && postdateTo != null) {
				sql.addLine(" and POSTDATE >= '" + postdateFrom + "' and POSTDATE <= '" + postdateTo + "' ");
			} else if (postdateFrom != null) {
				sql.addLine(" and POSTDATE >= '" + postdateFrom + "' ");
			} else if (postdateTo != null) {
				sql.addLine(" and POSTDATE <= '" + postdateTo + "' ");
			}

			if (!Fnc.isEmpty(recsta)) {
				sql.addLine(" and RECSTA = " + recsta + " ");
			}
			sql.addLine(" order by VOU_TYPE, VOU_NO, POSTDATE");

			System.out.println(sql.getSql());
			try (java.sql.ResultSet rs = dbc.getResultSetFw3(sql.getSql(), 5000);) {
				while (rs.next()) {
					FModelHasMap dat = new FModelHasMap();
					dat.putRecord(rs);
					dats.add(dat);
				}
			}

		}

	}

	public static void getDataQryAppv(java.util.List<FModelHasMap> dats, String vou_type, String vou_no,
			java.sql.Date postdateFrom, java.sql.Date postdateTo, String menuid2, LoginBean _loginBean)
			throws Exception {

		dats.clear();
		try (FDbc dbc = FDbc.connectMasterDb()) {
			SqlStr sql = new SqlStr();
			sql.addLine("select * from " + TboACGL_HEADER.tablename);
			sql.addLine(" where 1=1 ");
			sql.addLine(" and COMP_CDE = '" + _loginBean.getCOMP_CDE() + "' ");
			if (!Fnc.isEmpty(vou_type)) {
				sql.addLine(" and VOU_TYPE  = '" + Fnc.sqlQuote(vou_type) + "' ");
			}
			if (!Fnc.isEmpty(vou_no)) {
				sql.addLine(" and VOU_NO like '" + Fnc.sqlQuote(vou_no) + "%' ");
			}

			if (postdateFrom != null && postdateTo != null) {
				sql.addLine(" and POSTDATE >= '" + postdateFrom + "' and POSTDATE <= '" + postdateTo + "' ");
			} else if (postdateFrom != null) {
				sql.addLine(" and POSTDATE >= '" + postdateFrom + "' ");
			} else if (postdateTo != null) {
				sql.addLine(" and POSTDATE <= '" + postdateTo + "' ");
			}

			if (menuid2.equals("AcApprvCancel")) {
				sql.addLine(" and RECSTA = 2 ");
			} else {
				sql.addLine(" and RECSTA = 1 ");
			}
			sql.addLine(" order by VOU_TYPE, VOU_NO, POSTDATE");

			System.out.println(sql.getSql());
			try (java.sql.ResultSet rs = dbc.getResultSetFw3(sql.getSql(), 5000);) {
				while (rs.next()) {
					FModelHasMap dat = new FModelHasMap();
					dat.putRecord(rs);
					dats.add(dat);
				}
			}

		}

	}

	public static void getDataQryForAcVatReceiveCancel(java.util.List<FModelHasMap> dats, String vou_type,
			String vou_no, java.sql.Date postdateFrom, java.sql.Date postdateTo, LoginBean _loginBean)
			throws Exception {

		dats.clear();
		try (FDbc dbc = FDbc.connectMasterDb()) {
			SqlStr sql = new SqlStr();
			sql.addLine("select * from " + TboACGL_HEADER.tablename);
			sql.addLine(" where 1=1 ");
			sql.addLine(" and COMP_CDE = '" + _loginBean.getCOMP_CDE() + "' ");
			if (!Fnc.isEmpty(vou_type)) {
				sql.addLine(" and VOU_TYPE  = '" + Fnc.sqlQuote(vou_type) + "' ");
			}
			if (!Fnc.isEmpty(vou_no)) {
				sql.addLine(" and VOU_NO like '" + Fnc.sqlQuote(vou_no) + "%' ");
			}

			if (postdateFrom != null && postdateTo != null) {
				sql.addLine(" and POSTDATE >= '" + postdateFrom + "' and POSTDATE <= '" + postdateTo + "' ");
			} else if (postdateFrom != null) {
				sql.addLine(" and POSTDATE >= '" + postdateFrom + "' ");
			} else if (postdateTo != null) {
				sql.addLine(" and POSTDATE <= '" + postdateTo + "' ");
			}

			sql.addLine(" and RECSTA in (1,2) and POST_APP in ('AcVatReceiveA','AcVatReceiveB') ");
			sql.addLine(" order by VOU_TYPE, VOU_NO, POSTDATE");

			System.out.println(sql.getSql());
			try (java.sql.ResultSet rs = dbc.getResultSetFw3(sql.getSql(), 5000);) {
				while (rs.next()) {
					FModelHasMap dat = new FModelHasMap();
					dat.putRecord(rs);
					dats.add(dat);
				}
			}

		}

	}

}

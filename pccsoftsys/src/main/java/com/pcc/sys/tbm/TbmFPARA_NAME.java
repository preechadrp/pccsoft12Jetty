package com.pcc.sys.tbm;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.SqlStr;
import com.pcc.sys.tbo.TboFPARA_COMP;
import com.pcc.sys.tbo.TboFPARA_NAME;

public class TbmFPARA_NAME {

	public static void getDataQry(
			java.util.List<FModelHasMap> dats,
			String para_id, String para_desc,
			String menuId2,
			LoginBean _loginBean) throws Exception {

		dats.clear();
		try (FDbc dbc = FDbc.connectMasterDb()) {

			SqlStr sql = new SqlStr();
			sql.addLine("select aa.*,");
			sql.addLine("(select bb.para_value from " + TboFPARA_COMP.tablename + " bb where aa.para_id=bb.para_id and bb.comp_cde='" + Fnc.sqlQuote(_loginBean.getCOMP_CDE()) + "') as PARA_VALUE");
			sql.addLine("from " + TboFPARA_NAME.tablename + " aa");
			sql.addLine("where 1=1");
			if (!Fnc.isEmpty(para_id)) {
				sql.addLine("and aa.para_id like '%" + Fnc.sqlQuote(para_id.trim()) + "%' ");
			}
			if (!Fnc.isEmpty(para_desc)) {
				sql.addLine("and aa.para_desc like '%" + Fnc.sqlQuote(para_desc.trim()) + "%' ");
			}
			if (menuId2.equals("FrmParaCompMT")) {
				sql.addLine("and aa.sys_use = 'MT' ");
			} else if (menuId2.equals("FrmParaCompAc")) {
				sql.addLine("and aa.sys_use = 'AC' ");
			} else if (menuId2.equals("FrmParaCompBL")) {
				sql.addLine("and aa.sys_use = 'BL' ");
			} else {
				sql.addLine("and aa.sys_use = 'SYS' ");
			}
			sql.addLine(" order by aa.sys_use");

			System.out.println(sql.getSql());
			try (java.sql.ResultSet rs = dbc.getResultSetFw3(sql.getSql(), 1000);) {
				while (rs.next()) {
					FModelHasMap dat = new FModelHasMap();

					dat.setString("PARA_ID", rs.getString("PARA_ID"));
					dat.setString("PARA_DESC", rs.getString("PARA_DESC"));
					dat.setString("DATA_TYPE", rs.getString("DATA_TYPE"));
					dat.setString("PARA_VALUE", rs.getString("PARA_VALUE"));

					dats.add(dat);
				}
			}

		}

	}

}

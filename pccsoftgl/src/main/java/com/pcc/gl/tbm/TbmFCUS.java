package com.pcc.gl.tbm;

import java.sql.SQLException;

import com.pcc.gl.tbf.TbfFCUS;
import com.pcc.gl.tbo.TboFCUS;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.SqlStr;

public class TbmFCUS {

	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static boolean checkCUST_CDE(LoginBean loginBean, String cust_cde) throws SQLException, Exception {

		TboFCUS cus = new TboFCUS();

		cus.setCOMP_CDE(loginBean.getCOMP_CDE());
		cus.setCUST_CDE(cust_cde);

		if (TbfFCUS.find(cus)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * ดึงชื่อลูกค้า
	 * @param cust_cde
	 * @param cust_info หากส่ง array มา 2 ช่องจะได้ [ ชื่อ , รหัสเลขสาขาตามสรรพกร]
	 * @param loginBean
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public static boolean getCustName(String cust_cde, String[] cust_info, LoginBean loginBean)
			throws SQLException, Exception {

		try (FDbc dbc = FDbc.connectMasterDb()) {
			return getCustName(dbc, cust_cde, cust_info, loginBean);
		}

	}

	/**
	 * ดึงชื่อลูกค้า
	 * @param dbc
	 * @param cust_cde
	 * @param cust_info หากส่ง array มา 2 ช่องจะได้ [ ชื่อ , รหัสเลขสาขาตามสรรพกร]
	 * @param loginBean
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public static boolean getCustName(FDbc dbc, String cust_cde, String[] cust_info, LoginBean loginBean)
			throws SQLException, Exception {

		TboFCUS cust = new TboFCUS();

		cust.setCOMP_CDE(loginBean.getCOMP_CDE());
		cust.setCUST_CDE(cust_cde);

		if (TbfFCUS.find(dbc, cust)) {
			cust_info[0] = (cust.getTITLE().trim() + " " + cust.getFNAME().trim() + " " + cust.getLNAME().trim())
					.trim();
			if (cust_info.length == 2) {
				if (cust.getCUSTYP().equals("2")) {
					cust_info[1] = cust.getBRANCH_CODE();
				}
			}
			return true;
		} else {
			return false;
		}

	}

	public static String getCustName(String cust_cde, LoginBean loginBean) throws SQLException, Exception {
		String[] cust_info = { "", "" };
		if (getCustName(cust_cde, cust_info, loginBean)) {
			return cust_info[0];
		} else {
			return "";
		}

	}

	public static String getCustName(FDbc dbc, String cust_cde, LoginBean loginBean) throws SQLException, Exception {
		String[] cust_info = { "", "" };
		if (getCustName(dbc, cust_cde, cust_info, loginBean)) {
			return cust_info[0];
		} else {
			return "";
		}
	}

	public static void getCust1(String cust_cde, String fname, String lname,
			String idno, LoginBean loginBean, java.util.List<FModelHasMap> lst_data) throws SQLException, Exception {

		try (FDbc dbc = FDbc.connectMasterDb()) {
			getCust1(dbc, cust_cde, fname, lname, idno, loginBean, lst_data);
		}

	}

	public static void getCust1(FDbc dbc, String cust_cde, String fname, String lname,
			String idno, LoginBean loginBean, java.util.List<FModelHasMap> lst_data) throws SQLException, Exception {

		lst_data.clear();

		SqlStr sql = new SqlStr();
		sql.addLine("select aa.* from fcus aa where aa.COMP_CDE = ? ");
		sql.addLine("and (aa.CUST_CDE = ?");
		sql.addLine(" or (coalesce(aa.FNAME,'')=? and coalesce(aa.LNAME,'')=?)");
		sql.addLine(" or coalesce(aa.IDNO,'') = ?");
		sql.addLine(")");
		sql.addLine("order by aa.CUST_CDE ");
		try (java.sql.ResultSet rs1 = dbc.getResultSetFw2(sql.getSql(), loginBean.getCOMP_CDE(),
				cust_cde, fname, lname, idno);) {
			while (rs1.next()) {
				FModelHasMap rec1 = new FModelHasMap();
				lst_data.add(rec1);
				rec1.putRecord(rs1);
			}
		}

	}

}

package com.pcc.tk.progman;

import java.math.BigDecimal;
import java.util.List;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.Fnc;

public class ManTkJobLog {

	public static void getDataByJobno(String comp_cde, String jobno, List<FModelHasMap> lst_data) throws Exception {
		lst_data.clear();
		try (FDbc dbc = FDbc.connectMasterDb();) {
			String sql = """
					select aa.*,bb.JOBSTATNAME
					from tkjoblog aa
					left join tkjobstat bb on aa.COMP_CDE =bb.COMP_CDE and aa.JOBSTATID= bb.JOBSTATID
					where aa.COMP_CDE=? and aa.JOBNO=?
					order by aa.SEQ
					""";
			try (java.sql.ResultSet rs = dbc.getResultSetFw2(sql, comp_cde, jobno)) {
				while (rs.next()) {
					FModelHasMap rec = new FModelHasMap();
					lst_data.add(rec);
					rec.putRecord(rs);

					rec.put("JOBSTATNAME_SHOW", rs.getInt("JOBSTATID") + " : " + Fnc.getStr(rs.getString("JOBSTATNAME")));
				}
			}
		}

	}
	
	public static void getJobExpenses(String comp_cde, String jobno, List<FModelHasMap> lst_data) throws Exception {
		lst_data.clear();
		try (FDbc dbc = FDbc.connectMasterDb();) {
			String sql = """
					select aa.*,bb.EXPENSESNAME  
					from tkjobexpenses aa
					left join tkexpenses bb on aa.COMP_CDE=bb.COMP_CDE and aa.EXPENSESID=bb.EXPENSESID 
					where aa.COMP_CDE=? and aa.JOBNO=?
					order by aa.SEQ
					""";
			try (java.sql.ResultSet rs = dbc.getResultSetFw2(sql, comp_cde, jobno)) {
				while (rs.next()) {
					FModelHasMap rec = new FModelHasMap();
					lst_data.add(rec);
					rec.putRecord(rs);

					rec.put("EXPENSESNAME_SHOW", rs.getInt("EXPENSESID") + " : " + Fnc.getStr(rs.getString("EXPENSESNAME")));
				}
			}
		}

	}

	public static BigDecimal[] getSUMEXP(String comp_cde, String jobno) throws Exception {

		BigDecimal[] ret = { BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO  };

		try (FDbc dbc = FDbc.connectMasterDb();) {
			String sql = """
					select sum(aa.EXPENSESAMT) as EXPENSESAMT ,  sum(aa.WITHDRAWAL_AMT) as WITHDRAWAL_AMT
					,  sum(aa.EXPCOM_ADV) as EXPCOM_ADV
					from tkjobexpenses aa
					where aa.COMP_CDE=? and aa.JOBNO=?
					""";
			try (java.sql.ResultSet rs = dbc.getResultSetFw2(sql, comp_cde, jobno)) {
				while (rs.next()) {
					ret[0] = Fnc.getBigDecimalValue(rs.getBigDecimal("EXPENSESAMT"));
					ret[1] = Fnc.getBigDecimalValue(rs.getBigDecimal("WITHDRAWAL_AMT"));
					ret[2] = Fnc.getBigDecimalValue(rs.getBigDecimal("EXPCOM_ADV"));
				}
			}
		}

		return ret;
	}

}

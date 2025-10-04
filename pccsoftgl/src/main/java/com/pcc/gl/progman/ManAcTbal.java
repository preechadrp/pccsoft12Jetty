package com.pcc.gl.progman;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.pcc.api.core.ApiUtil;
import com.pcc.gl.tbo.TboACCT_NO;
import com.pcc.gl.tbo.TboACGL_DETAIL;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FJRBeanCollectionDataSource;
import com.pcc.sys.lib.FJasperPrintList;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FReport;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.SqlStr;
import com.pcc.sys.tbm.TbmFCOMP;

public class ManAcTbal {

	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static void getReport(LoginBean _loginBean, java.sql.Date fromPostdate, java.sql.Date toPostdate,
			FJasperPrintList fJasperPrintList, int print_option) throws Exception {

		try (FDbc dbc = FDbc.connectMasterDb()) {

			Map reportParams = new HashMap();
			
			reportParams.put("compName", _loginBean.getTboFcomp().getCOMP_NAME());
			reportParams.put("userName", _loginBean.getTboFuser().getTITLE() +" "+
			   _loginBean.getTboFuser().getFNAME_THAI()+" "+
			   _loginBean.getTboFuser().getLNAME_THAI());
			
			reportParams.put("reportName", "รายงานงบทดลอง");
			String reportConditionString1 = "";
			if (fromPostdate != null && toPostdate != null) {
				reportConditionString1 += "จากวันที่ " + FnDate.displayDateString(fromPostdate) + " ถึง "
						+ FnDate.displayDateString(toPostdate);
			}
			reportParams.put("reportConditionString1", reportConditionString1);

			FJRBeanCollectionDataSource reportDataSource = createDataSource(dbc, _loginBean, fromPostdate, toPostdate,
					print_option);
			if (reportDataSource != null) {
				if (print_option == 1) {// pdf
					fJasperPrintList.addJasperPrintList(FReport.builtRepByBean("/com/pcc/gl/jasper/AcTbal/AcTbal.jrxml", 
							reportParams, reportDataSource));
				} else {// excel
					fJasperPrintList.addJasperPrintList(FReport.builtRepByBean("/com/pcc/gl/jasper/AcTbal/AcTbal_Excel.jrxml", 
							reportParams, reportDataSource));
				}
			}

		}

	}

	public static FJRBeanCollectionDataSource createDataSource(FDbc dbc, LoginBean _loginBean,
			java.sql.Date fromPostdate, java.sql.Date toPostdate, int print_option) throws SQLException, Exception {

		java.sql.Date glFrmDate = TbmFCOMP.getGldate(dbc, _loginBean.getCOMP_CDE(), fromPostdate);

		SqlStr sql = new SqlStr();
		sql.addLine("select mm.comp_cde, mm.acct_id, att.acct_name, att.acct_type,");
		sql.addLine(" SUM(mm.BEGIN_DR) as BEGIN_DR ,");
		sql.addLine(" SUM(mm.BEGIN_CR) as BEGIN_CR,");
		sql.addLine(" SUM(mm.SUMDR) as SUMDR ,");
		sql.addLine(" SUM(mm.SUMCR) as SUMCR ");
		sql.addLine("from (");
		sql.addLine(" ");

		// ยอดยกมา
		sql.addLine(" select dd.comp_cde, dd.acct_id,  ");
		sql.addLine("  SUM( case when dd.num_type = 1 then coalesce(dd.amt,0) else 0 end) as BEGIN_DR,");
		sql.addLine("  SUM( case when dd.num_type = -1 then coalesce(dd.amt,0) else 0 end) as BEGIN_CR,");
		sql.addLine("  0 as SUMDR,");
		sql.addLine("  0 as SUMCR");
		sql.addLine(" from " + TboACGL_DETAIL.tablename + " dd");
		sql.addLine(" where dd.comp_cde='" + _loginBean.getCOMP_CDE() + "' and dd.recsta = 2 ");
		sql.addLine(" and coalesce(dd.chq_type,'') != '2' "); // ,2=เช็ครอตัดยกมา
		if (FnDate.compareDate(fromPostdate, glFrmDate) == 0) {// fromPostdate เท่ากับวันที่เริ่มงบบัญชีเอาเฉพาะยอดยกมาที่ปิดบัญชี
			sql.addLine(" and coalesce(dd.acct_open,'') = 'Y' ");
			sql.addLine(" and dd.postdate = '" + glFrmDate + "' ");
		} else {
			sql.addLine(" and dd.postdate >= '" + glFrmDate + "' and dd.postdate < '" + fromPostdate + "' ");
		}
		sql.addLine(" group by dd.comp_cde, dd.acct_id");
		sql.addLine(" ");

		sql.addLine(" union all");

		// ยอดในรอบ
		sql.addLine(" ");
		sql.addLine(" select dd.comp_cde, dd.acct_id,");
		sql.addLine("  0 as BEGIN_DR,");
		sql.addLine("  0 as BEGIN_CR,");
		sql.addLine("  SUM( case when dd.num_type = 1 then coalesce(dd.amt,0) else 0 end) as SUMDR,");
		sql.addLine("  SUM( case when dd.num_type = -1 then coalesce(dd.amt,0) else 0 end) as SUMCR");
		sql.addLine(" from " + TboACGL_DETAIL.tablename + " dd");
		sql.addLine(" where dd.comp_cde='" + _loginBean.getCOMP_CDE() + "' ");
		sql.addLine(" and dd.recsta = 2 and coalesce(dd.acct_open,'') != 'Y' ");
		sql.addLine(" and coalesce(dd.chq_type,'') != '2' "); // ,2=เช็ครอตัดยกมา
		sql.addLine(" and dd.postdate between '" + fromPostdate + "' and '" + toPostdate + "' ");
		sql.addLine(" group by dd.comp_cde, dd.acct_id");
		sql.addLine(" ");
		sql.addLine(") mm");
		sql.addLine("left join " + TboACCT_NO.tablename + " att on mm.COMP_CDE=att.COMP_CDE and mm.acct_id=att.acct_id ");
		sql.addLine("group by mm.comp_cde, mm.acct_id, att.acct_name, att.acct_type");
		sql.addLine("order by mm.acct_id");

		logger.info(sql.getSql());
		java.util.List<FModelHasMap> list_dat = new ArrayList<FModelHasMap>();
		try (java.sql.ResultSet rs = dbc.getResultSet(sql.getSql());) {
			while (rs.next()) {
				if (rs.getBigDecimal("BEGIN_DR").compareTo(BigDecimal.ZERO) != 0
						|| rs.getBigDecimal("BEGIN_CR").compareTo(BigDecimal.ZERO) != 0
						|| rs.getBigDecimal("SUMDR").compareTo(BigDecimal.ZERO) != 0
						|| rs.getBigDecimal("SUMCR").compareTo(BigDecimal.ZERO) != 0) {

					FModelHasMap dat = new FModelHasMap();
					// ใช้ dat.putRecord ไม่ได้สำหรับ sql แบบนี้
					dat.setString("COMP_CDE", rs.getString("COMP_CDE"));
					dat.setString("ACCT_ID", rs.getString("ACCT_ID"));
					dat.setString("ACCT_NAME", rs.getString("ACCT_NAME"));
					dat.setInt("ACCT_TYPE", rs.getInt("ACCT_TYPE"));
					
					dat.setBigDecimal("BEGIN_DR", rs.getBigDecimal("BEGIN_DR"));
					dat.setBigDecimal("BEGIN_CR", rs.getBigDecimal("BEGIN_CR"));
										
					{//ยอดยกมาที่ใช้แสดง
						BigDecimal ret = Fnc.getBigDecimal(rs.getBigDecimal("BEGIN_DR"))
								.subtract(Fnc.getBigDecimal(rs.getBigDecimal("BEGIN_CR")));
						
						if (ret.compareTo(BigDecimal.ZERO) > 0) {
							dat.setBigDecimal("BEGIN_DR_SHOW", ret);
						}
						if (ret.compareTo(BigDecimal.ZERO) < 0) {
							dat.setBigDecimal("BEGIN_CR_SHOW", ret.abs());
						}
					}
					
					{//ในงวด
						BigDecimal bSUMDR = Fnc.getBigDecimal(rs.getBigDecimal("SUMDR"));
						if (bSUMDR.compareTo(BigDecimal.ZERO) != 0) {
							dat.setBigDecimal("SUMDR", bSUMDR);
						}
						
						BigDecimal bSUMCR = Fnc.getBigDecimal(rs.getBigDecimal("SUMCR"));
						if (bSUMCR.compareTo(BigDecimal.ZERO) != 0) {
							dat.setBigDecimal("SUMCR", bSUMCR);
						}
					}
					
					{//ยอดยกไปที่ใช้แสดง
						BigDecimal ret = BigDecimal.ZERO;
						BigDecimal begin_dr = Fnc.getBigDecimal(rs.getBigDecimal("BEGIN_DR"));
						BigDecimal begin_cr = Fnc.getBigDecimal(rs.getBigDecimal("BEGIN_CR"));
						BigDecimal dr = Fnc.getBigDecimal(rs.getBigDecimal("SUMDR"));
						BigDecimal cr = Fnc.getBigDecimal(rs.getBigDecimal("SUMCR"));

						ret = begin_dr.add(dr).subtract(begin_cr).subtract(cr);

						//เดบิตยกไป
						if (ret.compareTo(BigDecimal.ZERO) > 0) {
							dat.setBigDecimal("END_DR_SHOW", ret);
						}	
						//เครดิตยกไป
						if (ret.compareTo(BigDecimal.ZERO) < 0) {
							dat.setBigDecimal("END_CR_SHOW", ret.abs());
						}
					}

					list_dat.add(dat);
				}

			}
		}
		logger.info("end putRecord");

		if (list_dat.size() > 0) {
			return new FJRBeanCollectionDataSource(list_dat);
		} else {
			return null;
		}

	}

	public static String[] getReportService(LoginBean _loginBean, JSONObject requestpara) throws Exception {
		
		//=== get parameter
		java.sql.Date fromPostdate = ApiUtil.getSqlDate(requestpara, "fromPostdate");
		java.sql.Date toPostdate = ApiUtil.getSqlDate(requestpara, "toPostdate");
		int print_option = ApiUtil.getInt(requestpara, "print_option");
		
		FJasperPrintList fJasperPrintList = new FJasperPrintList();
		getReport(_loginBean, fromPostdate, toPostdate, fJasperPrintList, print_option);
		if (fJasperPrintList.getJasperPrintLst().size() > 0) {
			if (print_option == 1) {
				return FReport.exportBase64_of_PDF(fJasperPrintList.getJasperPrintLst());
			} else {
				return FReport.exportBase64_of_EXCEL(fJasperPrintList.getJasperPrintLst());
			}
		} else {
			throw new Exception("ไม่พบข้อมูล");
		}
		
	}
	
}
package com.pcc.tk.progman;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.pcc.api.core.ApiUtil;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FJRBeanCollectionDataSource;
import com.pcc.sys.lib.FJasperPrintList;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FReport;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.SqlStr;

public class ManTkJobRep1 {

	public static void getReport(LoginBean _loginBean, JSONObject requestpara, FJasperPrintList fJasperPrintList) throws Exception {

		try (FDbc dbc = FDbc.connectMasterDb()) {

			Map reportParams = new HashMap();

			reportParams.put("compName", _loginBean.getCOMP_NAME());
			reportParams.put("userName", _loginBean.getUserFullName());

			reportParams.put("reportName", "รายงาน Job");
			reportParams.put("reportConditionString1", requestpara.getString("reportCondition"));

			FJRBeanCollectionDataSource reportDataSource = createDataSource(dbc, _loginBean, requestpara);

			if (reportDataSource != null) {

				int print_option = ApiUtil.getInt(requestpara, "print_option");

				if (print_option == 1) {// pdf ยังไม่ทำ
					//fJasperPrintList.addJasperPrintList(FReport.builtRepByBean("/com/pcc/tk/jasper/FrmTkJobRep1/FrmTkJobRep1.jasper", 
					//		reportParams, reportDataSource)); 
				} else {// excel
					fJasperPrintList.addJasperPrintList(FReport.builtRepByBean("/com/pcc/tk/jasper/FrmTkJobRep1/FrmTkJobRep1_Excel.jasper",
							reportParams, reportDataSource));
				}

			}

		}

	}

	public static FJRBeanCollectionDataSource createDataSource(FDbc dbc, LoginBean _loginBean, JSONObject paras) throws SQLException, Exception {
		
		SqlStr sqlLawyer = new SqlStr();
		sqlLawyer.addLine("select cy.SEQ,ly.LAWYERNAME  ");
		sqlLawyer.addLine("  from tkjoblawyer cy ");
		sqlLawyer.addLine("  left join tklawyer ly on cy.COMP_CDE=ly.COMP_CDE and cy.LAWYERID=ly.LAWYERID");
		sqlLawyer.addLine("  where cy.COMP_CDE=? and cy.JOBNO=?");
		sqlLawyer.addLine("  order by cy.SEQ ");
		
		SqlStr sqlClient = new SqlStr();
		sqlClient.addLine("select ck.CLIENTTITLE ,ck.CLIENTFNAME ,ck.CLIENTLNAME,ck.TELNO ,ck.CLIENTSEQ  ");
		sqlClient.addLine("from tkjobclient ck ");
		sqlClient.addLine("where ck.COMP_CDE =? and ck.JOBNO =? ");
		sqlClient.addLine("order by ck.CLIENTSEQ ");

		SqlStr sql = new SqlStr();
		sql.addLine("""
				select aa.*
				,bb.LAWSTATNAME, cc.JOBNAME ,dd.LAWTYPENAME  ,ff.COURTNAME 
				,jk.LOGDATE ,jk.JOBSTATID ,jk.JOBSTATREMARK ,jk.UPDBY as UPDBY2 ,jk.UPDDTE as UPDDTE2
				,js.JOBSTATNAME 
				,(select sum(ex.EXPENSESAMT) from tkjobexpenses ex where ex.COMP_CDE=aa.COMP_CDE and ex.JOBNO=aa.JOBNO) as SUM_EXPENSESAMT
                ,(select sum(ex.WITHDRAWAL_AMT) from tkjobexpenses ex where ex.COMP_CDE=aa.COMP_CDE and ex.JOBNO=aa.JOBNO) as SUM_WITHDRAWAL_AMT
                ,(select sum(ex.EXPCOM_ADV) from tkjobexpenses ex where ex.COMP_CDE=aa.COMP_CDE and ex.JOBNO=aa.JOBNO) as SUM_EXPCOM_ADV
				from tkjob aa
				left join tklawstat  bb on aa.COMP_CDE =bb.COMP_CDE and aa.LAWSTATID = bb.LAWSTATID
				left join tkjobcode cc on aa.COMP_CDE = cc.COMP_CDE and aa.JOBCODE =cc.JOBCODE 
				left join tklawtype dd on aa.COMP_CDE =dd.COMP_CDE and aa.LAWTYPEID =dd.LAWTYPEID  
				left join tkcourt ff on aa.COMP_CDE =ff.COMP_CDE  and aa.COURTID =ff.COURTID 
				left join tkjoblog jk on aa.COMP_CDE =jk.COMP_CDE and aa.JOBNO =jk.JOBNO 
				left join tkjobstat js on jk.COMP_CDE =js.COMP_CDE and jk.JOBSTATID = js.JOBSTATID
				""");
		
		sql.addLine("where aa.COMP_CDE ='" + Fnc.sqlQuote(_loginBean.getCOMP_CDE()) + "' ");
		sql.addLine("and aa.JOBSTAT = '1' ");
		
		if (paras.getBoolean("chkNOTCLOSE")) {
			sql.addLine("and aa.CLOSEDATE is null ");	
		}
		
		if (paras.has("tdbJOBDATE_FROM")) {
			sql.addLine("and aa.JOBDATE >='" + ApiUtil.getSqlDate(paras, "tdbJOBDATE_FROM") + "' ");
		}
		if (paras.has("tdbJOBDATE_TO")) {
			sql.addLine("and aa.JOBDATE <='" + ApiUtil.getSqlDate(paras, "tdbJOBDATE_TO") + "' ");
		}
		
		if (paras.has("tdbLOGDATE_FROM")) {
			sql.addLine("and jk.LOGDATE >='" + ApiUtil.getSqlDate(paras, "tdbLOGDATE_FROM") + "' ");
		}
		if (paras.has("tdbLOGDATE_TO")) {
			sql.addLine("and jk.LOGDATE <='" + ApiUtil.getSqlDate(paras, "tdbLOGDATE_TO") + "' ");
		}
		
		if (!Fnc.isEmpty(paras.getString("txtLAWSTATID"))) {
			sql.addLine("and aa.LAWSTATID = " + paras.getString("txtLAWSTATID"));
		}
		
		if (!Fnc.isEmpty(paras.getString("txtJOBCODE"))) {
			sql.addLine("and aa.JOBCODE  = " + paras.getString("txtJOBCODE"));
		}
		
		if (!Fnc.isEmpty(paras.getString("txtLAWTYPEID"))) {
			sql.addLine("and aa.LAWTYPEID = " + paras.getString("txtLAWTYPEID"));
		}
		
		if (!Fnc.isEmpty(paras.getString("txtLAWYERID"))) {
			sql.addLine("and (");
			sql.addLine("  select count(*) from tkjoblawyer cy ");
			sql.addLine("  where cy.COMP_CDE=aa.COMP_CDE and cy.JOBNO=aa.JOBNO");
			sql.addLine("   and cy.LAWYERID = " + paras.getString("txtLAWYERID"));
			sql.addLine(") > 0");
		}
		
		if (!Fnc.isEmpty(paras.getString("txtCOURTID"))) {
			sql.addLine("and aa.COURTID = " + paras.getString("txtCOURTID"));
		}
			
		sql.addLine("order by aa.JOBNO,jk.LOGDATE");
		
		java.util.List<FModelHasMap> list_dat = new ArrayList<FModelHasMap>();
		try (java.sql.ResultSet rs = dbc.getResultSet(sql.getSql());
				java.sql.PreparedStatement psLawyer = dbc.getPreparedStatement(sqlLawyer.getSql());
				java.sql.PreparedStatement psClient = dbc.getPreparedStatement(sqlClient.getSql());
				//
				) {
			
			while (rs.next()) {
				FModelHasMap dat = new FModelHasMap();
				list_dat.add(dat);
				
				dat.putRecord(rs);
				
				//==จำนวนวันทำงานจากวันที่เปิด JOB
				int dayCount = 0;
				if (dat.getDate("CLOSEDATE") == null) {
					dayCount = FnDate.getDayDiffInDay(dat.getDate("JOBDATE"), FnDate.getTodaySqlDate());	
				} else {
					dayCount = FnDate.getDayDiffInDay(dat.getDate("JOBDATE"), dat.getDate("CLOSEDATE"));	
				}
				dat.put("DAYWORK", dayCount + " วัน");
				
				
				//== ทนาย
				psLawyer.clearParameters();
				psLawyer.setString(1, dat.getString("COMP_CDE"));
				psLawyer.setString(2, dat.getString("JOBNO"));
				try (java.sql.ResultSet rsLawyer = psLawyer.executeQuery()) {
					String lawyer_List = "";
					while (rsLawyer.next()) {
						if (!Fnc.isEmpty(lawyer_List)) {
							lawyer_List += "\n" + rsLawyer.getString("LAWYERNAME");
						} else {
							lawyer_List += rsLawyer.getString("LAWYERNAME");
						}
					}
					dat.put("LAWYER_LIST" , lawyer_List);
				}
				
				//== ลูกความ
				psClient.clearParameters();
				psClient.setString(1, dat.getString("COMP_CDE"));
				psClient.setString(2, dat.getString("JOBNO"));
				try (java.sql.ResultSet rsClient = psClient.executeQuery()) {
					String client_list = "";
					while (rsClient.next()) {
						
						String s1 = rsClient.getInt("CLIENTSEQ") + " " + 
								rsClient.getString("CLIENTTITLE") + " " + rsClient.getString("CLIENTFNAME") + " " + rsClient.getString("CLIENTLNAME") +
								" เบอร์ติดต่อ " + rsClient.getString("TELNO") ;
						
						if (!Fnc.isEmpty(client_list)) {
							client_list += "\n" + s1.trim(); 
						} else {
							client_list = s1.trim();
						}
					}
					dat.put("CLIENT_LIST", client_list);
				}

			}
		}

		if (list_dat.size() > 0) {
			return new FJRBeanCollectionDataSource(list_dat);
		} else {
			return null;
		}

	}

	public static String[] getReportService(LoginBean _loginBean, JSONObject requestpara) throws Exception {

		FJasperPrintList fJasperPrintList = new FJasperPrintList();

		getReport(_loginBean, requestpara, fJasperPrintList);

		if (fJasperPrintList.getJasperPrintLst().size() > 0) {

			int print_option = ApiUtil.getInt(requestpara, "print_option");

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

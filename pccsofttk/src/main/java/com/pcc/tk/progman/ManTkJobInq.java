package com.pcc.tk.progman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FJRBeanCollectionDataSource;
import com.pcc.sys.lib.FJasperPrintList;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FReport;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.SqlStr;
import com.pcc.tk.tbf.TbfTKJOB;
import com.pcc.tk.tbf.TbfTKLAWSTAT;
import com.pcc.tk.tbm.TbmTKCOURT;
import com.pcc.tk.tbm.TbmTKJOBCODE;
import com.pcc.tk.tbm.TbmTKLAWSTAT;
import com.pcc.tk.tbm.TbmTKLAWTYPE;
import com.pcc.tk.tbm.TbmTKZONE;
import com.pcc.tk.tbo.TboTKJOB;
import com.pcc.tk.tbo.TboTKLAWSTAT;

public class ManTkJobInq {

	public static void getDataByJobno(String comp_cde, FModelHasMap para, List<FModelHasMap> lst_data) throws Exception {

		lst_data.clear();

		try (var dbc = FDbc.connectMasterDb()) {

			SqlStr sql = new SqlStr();
			sql.addLine("select aa.*,bb.LAWSTATNAME,");
			sql.addLine("case ");
			sql.addLine("    when JOBSTAT = '1' then 'ปกติ'");
			sql.addLine("    when JOBSTAT = '8' then 'ไม่สมบูรณ์'");
			sql.addLine("    when JOBSTAT = '9' then 'ยกเลิก'");
			sql.addLine("    else '-'");
			sql.addLine("end as JOBSTATNAME");
			sql.addLine("from tkjob aa ");
			sql.addLine("left join tklawstat  bb on aa.COMP_CDE =bb.COMP_CDE and aa.LAWSTATID = bb.LAWSTATID");
			sql.addLine("where aa.COMP_CDE ='" + comp_cde + "' ");

			if (para.getDate("JOBDATE_FROM") != null) {
				sql.addLine("and aa.JOBDATE >= '" + para.getDate("JOBDATE_FROM") + "' ");
			}
			if (para.getDate("JOBDATE_TO") != null) {
				sql.addLine("and aa.JOBDATE <= '" + para.getDate("JOBDATE_TO") + "' ");
			}
			if (!Fnc.isEmpty(para.getString("JOBNO"))) {
				sql.addLine("and aa.JOBNO like '" + Fnc.sqlQuote(para.getString("JOBNO")) + "%' ");
			}
			if (para.getInt("LAWSTATID") > 0) {
				sql.addLine("and aa.LAWSTATID = " + para.getInt("LAWSTATID"));
			}
			if (para.getInt("JOBCODE") > 0) {
				sql.addLine("and aa.JOBCODE = " + para.getInt("JOBCODE"));
			}
			if (para.getInt("LAWTYPEID") > 0) {
				sql.addLine("and aa.LAWTYPEID = " + para.getInt("LAWTYPEID"));
			}
			if (!Fnc.isEmpty(para.getString("LAWBLACKNO"))) {
				sql.addLine("and aa.LAWBLACKNO like '%" + Fnc.sqlQuote(para.getString("LAWBLACKNO")) + "%' ");
			}
			if (!Fnc.isEmpty(para.getString("LAWREDNO"))) {
				sql.addLine("and aa.LAWREDNO like '%" + Fnc.sqlQuote(para.getString("LAWREDNO")) + "%' ");
			}
			if (para.getInt("COURTID") > 0) {
				sql.addLine("and aa.COURTID = " + para.getInt("COURTID"));
			}
			if (para.getInt("ZONEID") > 0) {
				sql.addLine("and aa.ZONEID = " + para.getInt("ZONEID"));
			}

			if (!Fnc.isEmpty(para.getString("CLIENTFNAME")) || !Fnc.isEmpty(para.getString("CLIENTLNAME"))) {
				sql.addLine("and ( ");
				sql.addLine(" select count(*) from tkjobclient ck ");
				sql.addLine(" where ck.COMP_CDE=aa.COMP_CDE and ck.JOBNO=aa.JOBNO ");
				sql.addLine(" and ck.CLIENTFNAME like '%" + Fnc.sqlQuote(para.getString("CLIENTFNAME")) + "%' ");
				sql.addLine(" and ck.CLIENTLNAME like '%" + Fnc.sqlQuote(para.getString("CLIENTLNAME")) + "%' ");
				sql.addLine(") > 0 ");
			}

			if (!Fnc.isEmpty(para.getString("LAWYERNAME"))) {
				sql.addLine("and ( ");
				sql.addLine("select count(*) from tkjoblawyer cy ");
				sql.addLine("left join tklawyer ly on cy.COMP_CDE=ly.COMP_CDE and cy.LAWYERID=ly.LAWYERID ");
				sql.addLine("where cy.COMP_CDE=aa.COMP_CDE and cy.JOBNO=aa.JOBNO ");
				sql.addLine(" and ly.LAWYERNAME like '%" + Fnc.sqlQuote(para.getString("LAWYERNAME")) + "%' ");
				sql.addLine(") > 0 ");
			}
			sql.addLine("order by aa.JOBNO desc");
			System.out.println(sql.getSql());

			String sqlClient = """
					select ck.CLIENTTITLE ,ck.CLIENTFNAME ,ck.CLIENTLNAME
					from tkjobclient ck
					where ck.COMP_CDE =? and ck.JOBNO =?
					order by ck.seq
					""";

			String sqlLwy = """
					  select cy.SEQ,ly.LAWYERNAME
					  from tkjoblawyer cy
					  left join tklawyer ly on cy.COMP_CDE=ly.COMP_CDE and cy.LAWYERID=ly.LAWYERID
					  where cy.COMP_CDE=? and cy.JOBNO=?
					  order by cy.SEQ
					""";

			try (java.sql.ResultSet rs = dbc.getResultSetFw3(sql.getSql(), 1000);
					java.sql.PreparedStatement psClient = dbc.getPreparedStatement(sqlClient);
					java.sql.PreparedStatement psLwy = dbc.getPreparedStatement(sqlLwy)) {

				while (rs.next()) {
					FModelHasMap rec = new FModelHasMap();
					lst_data.add(rec);
					rec.putRecord(rs);

					{// ลูกความ
						String clientNames = "";
						psClient.clearParameters();
						psClient.setString(1, comp_cde);
						psClient.setString(2, rs.getString("JOBNO"));
						try (java.sql.ResultSet rsClient = psClient.executeQuery()) {
							while (rsClient.next()) {
								if (Fnc.isEmpty(clientNames)) {
									clientNames = Fnc.getStr(rsClient.getString("CLIENTTITLE")) + " " +
											Fnc.getStr(rsClient.getString("CLIENTFNAME")) + " " + Fnc.getStr(rsClient.getString("CLIENTLNAME"));
								} else {
									clientNames = clientNames.trim() + " ," + Fnc.getStr(rsClient.getString("CLIENTTITLE")) + " " +
											Fnc.getStr(rsClient.getString("CLIENTFNAME")) + " " + Fnc.getStr(rsClient.getString("CLIENTLNAME"));
								}
							}
						}
						rec.put("CLIENTNAMES", clientNames);

					}

					{// ทนาย
						String lawyerNames = "";
						psLwy.clearParameters();
						psLwy.setString(1, comp_cde);
						psLwy.setString(2, rs.getString("JOBNO"));
						try (java.sql.ResultSet rsLwy = psLwy.executeQuery()) {
							while (rsLwy.next()) {
								if (Fnc.isEmpty(lawyerNames)) {
									lawyerNames = rsLwy.getString("LAWYERNAME");
								} else {
									lawyerNames = lawyerNames.trim() + " ," + rsLwy.getString("LAWYERNAME");
								}
							}
						}
						rec.put("LAWYERNAMES", lawyerNames);
					}

				}

			}

		}

	}

	public static void printData(String jobno, LoginBean loginBean, FJasperPrintList fJasperPrintList) throws Exception {

		try (var dbc = FDbc.connectMasterDb()) {
			TboTKJOB tkjob = new TboTKJOB();

			tkjob.setCOMP_CDE(loginBean.getCOMP_CDE());
			tkjob.setJOBNO(jobno);

			if (TbfTKJOB.find(tkjob)) {

				// === header
				Map reportParams = new HashMap();

				reportParams.put("JOBNO", tkjob.getJOBNO());
				reportParams.put("JOBDATE", tkjob.getJOBDATE());
				reportParams.put("LAWSTATID",tkjob.getLAWSTATID());
				reportParams.put("LAWSTATNAME",TbmTKLAWSTAT.get_LAWSTATID(tkjob.getCOMP_CDE(), tkjob.getLAWSTATID()));
				reportParams.put("SENDJOBDATE",tkjob.getSENDJOBDATE());
				reportParams.put("JOBCODE",tkjob.getJOBCODE());
				reportParams.put("JOBCODENAME", TbmTKJOBCODE.get_JOBCODENAME(tkjob.getCOMP_CDE(), tkjob.getJOBCODE()));
				reportParams.put("CUSTCONTACTNO",tkjob.getCUSTCONTACTNO());
				reportParams.put("PLAINT",tkjob.getPLAINT());
				reportParams.put("LAWTYPEID",tkjob.getLAWTYPEID());
				reportParams.put("LAWTYPENAME", TbmTKLAWTYPE.get_LAWTYPE_NAME(tkjob.getCOMP_CDE(), tkjob.getLAWTYPEID()));
				reportParams.put("SUEATDATE",tkjob.getSUEATDATE());
				reportParams.put("SUEDDATE",tkjob.getSUEDDATE());
				reportParams.put("SUECOSTAMT",tkjob.getSUECOSTAMT());
				reportParams.put("FEEAMT",tkjob.getFEEAMT());
				reportParams.put("REMARK1",tkjob.getREMARK1());
				reportParams.put("LAWBLACKDATE",tkjob.getLAWBLACKDATE());
				reportParams.put("LAWBLACKNO",tkjob.getLAWBLACKNO());
				reportParams.put("LAWREDDATE",tkjob.getLAWREDDATE());
				reportParams.put("LAWREDNO",tkjob.getLAWREDNO());
				reportParams.put("COURTID",tkjob.getCOURTID());
				reportParams.put("COURTNAME",TbmTKCOURT.get_COURTID_Name(tkjob.getCOMP_CDE(), tkjob.getCOURTID()));
				reportParams.put("ZONEID",tkjob.getZONEID());
				reportParams.put("ZONENAME",TbmTKZONE.get_ZONEID_NAME(tkjob.getCOMP_CDE(), tkjob.getZONEID()));
				reportParams.put("REMARK2",tkjob.getREMARK2());
				reportParams.put("CLOSEDATE",tkjob.getCLOSEDATE());

				java.util.List<FModelHasMap> list_dat = new ArrayList<FModelHasMap>();

				// === ลูกความ REPGROUP=1
				getCustByJobno(dbc, loginBean.getCOMP_CDE(), tkjob.getJOBNO(), tkjob.getLAWSTATID(), list_dat, "1");

				// === ทนาย REPGROUP=2
				getLayerByJobno(dbc, loginBean.getCOMP_CDE(), tkjob.getJOBNO(), list_dat, "2");

				// === สถานะการทำงาน REPGROUP=3
				getJobstatByJobno(dbc, loginBean.getCOMP_CDE(), tkjob.getJOBNO(), list_dat, "3");

				// === ค่าใช้จ่าย REPGROUP=4
				getJobExpenses(dbc, loginBean.getCOMP_CDE(), tkjob.getJOBNO(), list_dat, "4");

				fJasperPrintList.addJasperPrintList(FReport.builtRepByBean(
						"/com/pcc/tk/jasper/FrmTkJob/FrmTkJob.jrxml",
						reportParams, new FJRBeanCollectionDataSource(list_dat)));

			}

		}

	}

	public static void getCustByJobno(FDbc dbc, String comp_cde, String jobno,int lawstatid, 
			java.util.List<FModelHasMap> lst_data, String REPGROUP) throws Exception {
		
		String sLAWSTATNAME = "";
		{
			TboTKLAWSTAT tb1 = new TboTKLAWSTAT();
	
			tb1.setCOMP_CDE(comp_cde);
			tb1.setLAWSTATID(lawstatid);
	
			if (TbfTKLAWSTAT.find(tb1)) {
				sLAWSTATNAME = tb1.getLAWSTATNAME();
			}
		}
		
		String sql = " select * from tkjobclient where COMP_CDE =? and JOBNO =? order by CLIENTSEQ  ";
		try (java.sql.ResultSet rs = dbc.getResultSetFw2(sql, comp_cde, jobno)) {
			while (rs.next()) {
				FModelHasMap rec = new FModelHasMap();
				lst_data.add(rec);
				//rec.putRecord(rs);
				
				String sCLIENTSEQ_DESC = sLAWSTATNAME + "ที่  " + rs.getInt("CLIENTSEQ");
				rec.put("CLIENTSEQ_DESC", sCLIENTSEQ_DESC);

				String sCUSTNAME = rs.getString("CLIENTTITLE") + " " + rs.getString("CLIENTFNAME") + " " + rs.getString("CLIENTLNAME");
				rec.put("CUSTNAME", sCUSTNAME.trim());
				
				rec.put("TELNO", Fnc.getStr(rs.getString("TELNO")));

				rec.put("REPGROUP", REPGROUP);
			}
		}
		
	}

	public static void getLayerByJobno(FDbc dbc, String comp_cde, String jobno, List<FModelHasMap> lst_data, String REPGROUP) throws Exception {
		
		String sql = """
				select aa.*,bb.LAWYERNAME
				from tkjoblawyer aa
				left join tklawyer bb on aa.COMP_CDE =bb.COMP_CDE and aa.LAWYERID =bb.LAWYERID
				where aa.COMP_CDE =? and aa.JOBNO=?
				order by aa.SEQ
				""";
		try (java.sql.ResultSet rs = dbc.getResultSetFw2(sql, comp_cde, jobno)) {
			while (rs.next()) {
				FModelHasMap rec = new FModelHasMap();
				lst_data.add(rec);
				//rec.putRecord(rs);

				rec.setInt("LAWYERID",rs.getInt("LAWYERID"));
				rec.setString("LAWYERNAME",rs.getString("LAWYERNAME"));
				rec.put("REPGROUP", REPGROUP);
			}
		}
	}

	public static void getJobstatByJobno(FDbc dbc, String comp_cde, String jobno, List<FModelHasMap> lst_data, String REPGROUP) throws Exception {

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
				rec.put("REPGROUP", REPGROUP);
			}
		}

	}
	
	public static void getJobExpenses(FDbc dbc, String comp_cde, String jobno, List<FModelHasMap> lst_data, String REPGROUP) throws Exception {
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
				rec.put("REPGROUP", REPGROUP);
			}
		}

	}

}

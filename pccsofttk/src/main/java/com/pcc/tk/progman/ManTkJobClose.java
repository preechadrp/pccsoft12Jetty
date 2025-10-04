package com.pcc.tk.progman;

import java.util.List;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.SqlStr;

public class ManTkJobClose {

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
				sql.addLine(" and ck.CLIENTFNAME like '%"+Fnc.sqlQuote(para.getString("CLIENTFNAME"))+"%' ");
				sql.addLine(" and ck.CLIENTLNAME like '%"+Fnc.sqlQuote(para.getString("CLIENTLNAME"))+"%' ");  
				sql.addLine(") > 0 ");
			}
			
			if (!Fnc.isEmpty(para.getString("LAWYERNAME"))) {
				sql.addLine("and ( ");
				sql.addLine("select count(*) from tkjoblawyer cy "); 
				sql.addLine("left join tklawyer ly on cy.COMP_CDE=ly.COMP_CDE and cy.LAWYERID=ly.LAWYERID ");
				sql.addLine("where cy.COMP_CDE=aa.COMP_CDE and cy.JOBNO=aa.JOBNO ");
				sql.addLine(" and ly.LAWYERNAME like '%"+Fnc.sqlQuote(para.getString("LAWYERNAME"))+"%' ");
				sql.addLine(") > 0 ");
			}
			
			if (para.getString("MenuId2").equals("FrmTkJob")) {
				sql.addLine("and aa.JOBSTAT != '9' ");
			} else if (para.getString("MenuId2").equals("FrmTkJobLog")) {
				sql.addLine("and aa.JOBSTAT = '1' ");
			} else if (para.getString("MenuId2").equals("FrmTkJobDoc")) {
				sql.addLine("and aa.JOBSTAT = '1' ");
			} else if (para.getString("MenuId2").equals("FrmTkJobClose")) {
				sql.addLine("and aa.JOBSTAT = '1' ");
			} else if (para.getString("MENUID2").equals("FrmTkJobCancel")) {
				sql.addLine("and aa.JOBSTAT != '9' ");
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
			
			try (java.sql.ResultSet rs = dbc.getResultSetFw3(sql.getSql(),1000);
					java.sql.PreparedStatement psClient = dbc.getPreparedStatement(sqlClient);
					java.sql.PreparedStatement psLwy = dbc.getPreparedStatement(sqlLwy)) {
				
				while (rs.next()) {
					FModelHasMap rec = new FModelHasMap();
					lst_data.add(rec);
					rec.putRecord(rs);
					
					{//ลูกความ
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
						rec.put("CLIENTNAMES",clientNames);
						
					}
					
					{//ทนาย
						String lawyerNames = "";
						psLwy.clearParameters();
						psLwy.setString(1, comp_cde);
						psLwy.setString(2, rs.getString("JOBNO"));
						try(java.sql.ResultSet rsLwy = psLwy.executeQuery()){
							while (rsLwy.next()) {
								if (Fnc.isEmpty(lawyerNames)) {
									lawyerNames = rsLwy.getString("LAWYERNAME");
								} else {
									lawyerNames = lawyerNames.trim()+" ,"+ rsLwy.getString("LAWYERNAME");
								}
							}
						}
						rec.put("LAWYERNAMES", lawyerNames);
					}
					
				}
				
			}

		}

	}

}

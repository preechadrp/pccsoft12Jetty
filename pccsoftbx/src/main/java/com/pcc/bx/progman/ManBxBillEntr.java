package com.pcc.bx.progman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pcc.bx.tbf.TbfBXHEADER;
import com.pcc.bx.tbm.TbmBXDETAIL;
import com.pcc.bx.tbm.TbmBXTMPLDETAIL;
import com.pcc.bx.tbo.TboBXDETAIL;
import com.pcc.bx.tbo.TboBXHEADER;
import com.pcc.bx.tbo.TboBXTMPLDETAIL;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FJRBeanCollectionDataSource;
import com.pcc.sys.lib.FJasperPrintList;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FReport;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.tbm.TbmFCOMP;
import com.pcc.sys.tbm.TbmFCOMPBRANC;
import com.pcc.sys.tbo.TboFCOMP;
import com.pcc.sys.tbo.TboFCOMPBRANC;

public class ManBxBillEntr {

	public static void printReport(String comp_cde, String blno) throws Exception {
		try (FDbc dbc = FDbc.connectMasterDb()) {

			TboBXHEADER bxH = new TboBXHEADER();

			bxH.setCOMP_CDE(comp_cde);
			bxH.setBLNO(blno);

			if (TbfBXHEADER.find(dbc, bxH)) {

				FJasperPrintList fJasperPrintList = new FJasperPrintList();
				List<TboBXTMPLDETAIL> lst_data = new ArrayList<TboBXTMPLDETAIL>();

				TbmBXTMPLDETAIL.getData(comp_cde, bxH.getTMPLCDE(), lst_data);
				if (lst_data.size() == 0) {
					throw new Exception("ไม่พบหัวเอกสารนี้");
				}

				for (TboBXTMPLDETAIL bxtmpldetail : lst_data) {

					Map reportParams = new HashMap();

					// == หัวเอกสาร
					reportParams.put("COPY_TYPE", bxtmpldetail.getDOCHEAD());
					reportParams.put("COPY_FOR", "สำหรับ" + bxtmpldetail.getCOPYFOR());

					// ==บริษัท
					String p_COMPNAME = "";
					String p_BRANNAME = "";
					String p_comp_address = "";
					String p_comp_telno = "";
					TboFCOMP fcomp = TbmFCOMP.getTable(comp_cde);
					if (fcomp != null) {
						p_COMPNAME = fcomp.getCOMP_NAME();
					}
					TboFCOMPBRANC branc = TbmFCOMPBRANC.getBranc(dbc, comp_cde, bxH.getBRANC_CDE());
					if (branc != null) {
						p_BRANNAME = Fnc.getStr(branc.getBRANC_NAME());
						p_comp_address += branc.getADDR1() + " " + branc.getADDR2();
						p_comp_telno = branc.getTELNO();
					}

					reportParams.put("COMPNAME", p_COMPNAME);
					reportParams.put("BRANNAME", p_BRANNAME);
					reportParams.put("COMPADDR1", p_comp_address);
					reportParams.put("P_COMP_TELNO", p_comp_telno);
					reportParams.put("COMPTAXID", fcomp.getTAXNO());

					// ==รายละเอียด
					reportParams.put("BLNO", bxH.getBLNO());
					reportParams.put("BLDATE", bxH.getBLDATE());
					reportParams.put("CUST_CDE", bxH.getCUST_CDE());

					String p_custname = bxH.getCUST_TITLE() + " " + bxH.getCUST_FNAME() + " " + bxH.getCUST_LNAME();
					reportParams.put("CUSTOMER_NAME", p_custname.trim());
					reportParams.put("CUST_IDNO", bxH.getIDNO());
					reportParams.put("CUST_BRAN_VAT", "");
					reportParams.put("CUST_ADDRESS1", Fnc.getStr(bxH.getCUST_ADDR1()));
					reportParams.put("CUST_ADDRESS2", Fnc.getStr(bxH.getCUST_ADDR2()) + " " + Fnc.getStr(bxH.getZIPCODE()));
					reportParams.put("VATRATE", bxH.getVATRATE());
					reportParams.put("VATAMT", bxH.getVATAMT());
					reportParams.put("BASAMT", bxH.getBASAMT());
					reportParams.put("DISCOUNTAMT", Fnc.getBigDecimalValue(bxH.getDISCOUNTAMT()));
					reportParams.put("NETAMT", bxH.getBASAMT().subtract(Fnc.getBigDecimalValue(bxH.getDISCOUNTAMT())));
					reportParams.put("TOTALAMT", bxH.getTOTALAMT());
					reportParams.put("REMARK1", bxH.getREMARK1());

					java.util.List<FModelHasMap> list_dat = new ArrayList<FModelHasMap>();

					List<TboBXDETAIL> lst_bxdetail = new ArrayList<TboBXDETAIL>();
					TbmBXDETAIL.getData(comp_cde, blno, lst_bxdetail);
					if (lst_bxdetail.size() != 0) {
						int idx = 0;
						for (TboBXDETAIL bxdetail : lst_bxdetail) {
							

							FModelHasMap rec1 = new FModelHasMap();

							if (bxdetail.getLINETYP().equals("1")) {
								
								idx++;
								rec1.put("LINESEQ", idx);
	
								String description = Fnc.getStr(bxdetail.getPRODUCTID()) + " " + Fnc.getStr(bxdetail.getPRODUCTNAME()) + " " +
										Fnc.getStr(bxdetail.getREMARKLINE());
								rec1.put("DESCRIPTION", description.trim());
	
								rec1.put("QTY", bxdetail.getQTY());
								rec1.put("PRICE", bxdetail.getPRICE());
								rec1.put("SUMPRICE", bxdetail.getSUMPRICE());
								
							} else {
								
								rec1.put("LINESEQ", null);
								
								String description = Fnc.getStr(bxdetail.getPRODUCTID()) + " " + Fnc.getStr(bxdetail.getPRODUCTNAME()) + " " +
										Fnc.getStr(bxdetail.getREMARKLINE());
								rec1.put("DESCRIPTION", description.trim());
	
								rec1.put("QTY", null);
								rec1.put("PRICE", null);
								rec1.put("SUMPRICE", null);
								
							}
							
							list_dat.add(rec1);
						}
					}

					fJasperPrintList.addJasperPrintList(FReport.builtRepByBean(
							"/com/pcc/bx/jasper/FrmBxBill/FrmBxBill.jrxml",
							reportParams, new FJRBeanCollectionDataSource(list_dat)));
				}

				if (fJasperPrintList.getJasperPrintLst().size() > 0) {
					FReport.viewPdf(fJasperPrintList.getJasperPrintLst());
				}

			} else {
				throw new Exception("ไม่พบข้อมูล");
			}

		}

	}

}

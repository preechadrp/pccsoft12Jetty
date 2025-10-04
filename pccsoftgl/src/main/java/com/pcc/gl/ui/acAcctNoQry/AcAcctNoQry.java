package com.pcc.gl.ui.acAcctNoQry;

import java.util.ArrayList;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;

import com.pcc.gl.lib.FConstAc;
import com.pcc.gl.progman.ManAcAcctNoQry;
import com.pcc.gl.tbm.TbmACCT_NO;
import com.pcc.sys.lib.FJasperPrintList;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FReport;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.ZkUtil;

public class AcAcctNoQry extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Textbox txtACCT_ID;
	public Textbox txtACCT_NAME;

	private Listbox listBox1;

	private java.util.List<FModelHasMap> lst_acct_no = new ArrayList<FModelHasMap>();

	public Button btnExit, btnFind;

	@Override
	public void setFormObj() {
		txtACCT_ID = (Textbox) getFellow("txtACCT_ID");
		txtACCT_NAME = (Textbox) getFellow("txtACCT_NAME");
		listBox1 = (Listbox) getFellow("listBox1");

		btnExit = (Button) getFellow("btnExit");
		btnFind = (Button) getFellow("btnFind");

	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(txtACCT_ID);
		addEnterIndex(txtACCT_NAME);
		addEnterIndex(btnFind);

	}

	@Override
	public void formInit() {
		try {

			// == set style and Renderer
			ZkUtil.setListBoxHeaderStyle(listBox1);
			this.listBox1.setItemRenderer((item, data, index) -> {

				FModelHasMap rs = (FModelHasMap) data;

				// === เพิ่ม Attribute
				item.setAttribute("ACCT_ID", rs.getString("ACCT_ID"));

				// == display data
				new Listcell((index + 1) + "").setParent(item);
				new Listcell(rs.getString("ACCT_ID")).setParent(item);
				new Listcell(rs.getString("ACCT_NAME")).setParent(item);

				String acctName = FConstAc.get_acc_type_name(rs.getInt("ACCT_TYPE") + "");
				new Listcell(acctName).setParent(item);

			});

			clearData();
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	private void clearData() {
		txtACCT_ID.setValue("");
		txtACCT_NAME.setValue("");
	}

	public void onOK() {
		try {
			super.onOK();
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void onClick_btnFind() {
		try {

			listBox1.getItems().clear();
			TbmACCT_NO.getData(lst_acct_no, txtACCT_ID.getValue(), txtACCT_NAME.getValue(), this.getLoginBean());
			SimpleListModel rstModel = new SimpleListModel(lst_acct_no);
			listBox1.setModel(rstModel);

		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public void onClick_btnPrint(int print_option) {
		try {

			FJasperPrintList fJasperPrintList = new FJasperPrintList();
			ManAcAcctNoQry.getReport(this.getLoginBean(), fJasperPrintList, print_option);
			if (fJasperPrintList.getJasperPrintLst().size() > 0) {
				if (print_option == 1) { // pdf
					FReport.viewPdf(fJasperPrintList.getJasperPrintLst());
					// FReport.viewMsWordToLoad(fJasperPrintList, "AcAcctNoQry"); //Test
				} else {// excel
					FReport.exportExcel(fJasperPrintList, "AcAcctNoQry");
					// FReport.exportExcelToFile(fJasperPrintList, new String[] {
					// "C:\\tmp\\AcAcctNoQry.xls" });//test ผ่าน
				}
			} else {
				throw new Exception("ไม่พบข้อมูล");
			}

		} catch (Exception e) {
			Msg.error(e);
		}
	}

}

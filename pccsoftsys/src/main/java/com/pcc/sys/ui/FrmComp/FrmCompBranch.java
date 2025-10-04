package com.pcc.sys.ui.FrmComp;

import java.util.ArrayList;

import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;

import com.pcc.sys.lib.FWindow;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.ZkUtil;
import com.pcc.sys.tbo.TboFCOMPBRANC;

public class FrmCompBranch extends FWindow {

	private static final long serialVersionUID = 1L;

	public Textbox txtBRANC_NAME;
	public Textbox txtBRANC_SHORTNAME;
	public Textbox txtBRANCTAXNO;
	public Textbox txtADDR1;
	public Textbox txtADDR2;
	public Textbox txtTELNO;
	public Textbox txtFAXNO;
	public Button btnSave;
	public Button btnExit;

	//==parameter จากข้างนอก
	public boolean editMode = false;
	public String COMP_CDE = "";
	public int BRANC_CDE = 0;
	public String callBackMethodName = "";
	public Object callBackClass = null;
	public java.util.List<TboFCOMPBRANC> lst_data = new ArrayList<>();
	private TboFCOMPBRANC old_table1 = new TboFCOMPBRANC();

	@Override
	public void setFormObj() {

		txtBRANC_NAME = (Textbox) getFellow("txtBRANC_NAME");
		txtBRANC_SHORTNAME = (Textbox) getFellow("txtBRANC_SHORTNAME");
		txtBRANCTAXNO = (Textbox) getFellow("txtBRANCTAXNO");
		txtADDR1 = (Textbox) getFellow("txtADDR1");
		txtADDR2 = (Textbox) getFellow("txtADDR2");
		txtTELNO = (Textbox) getFellow("txtTELNO");
		txtFAXNO = (Textbox) getFellow("txtFAXNO");
		btnSave = (Button) getFellow("btnSave");
		btnExit = (Button) getFellow("btnExit");

	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(txtBRANC_NAME);
		addEnterIndex(txtBRANC_SHORTNAME);
		addEnterIndex(txtBRANCTAXNO);
		addEnterIndex(txtADDR1);
		addEnterIndex(txtADDR2);
		addEnterIndex(txtTELNO);
		addEnterIndex(txtFAXNO);
		addEnterIndex(btnSave);
		addEnterIndex(btnExit);
	}

	@Override
	public void formInit() {

		if (BRANC_CDE != 0 && editMode == true) {
			//อ่านรายการ 
			try {
				
				for (TboFCOMPBRANC rec1 : lst_data) {
					if (rec1.getBRANC_CDE() == this.BRANC_CDE) {
						
						old_table1 = rec1;
						
						txtBRANC_NAME.setValue(old_table1.getBRANC_NAME());
						txtBRANC_SHORTNAME.setValue(old_table1.getBRANC_SHORTNAME());
						txtBRANCTAXNO.setValue(old_table1.getBRANCTAXNO());
						txtADDR1.setValue(old_table1.getADDR1());
						txtADDR2.setValue(old_table1.getADDR2());
						txtTELNO.setValue(old_table1.getTELNO());
						txtFAXNO.setValue(old_table1.getFAXNO());
						
						break;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public void onOK() {

		try {
			super.onOK();
		} catch (Exception e) {
		}

	}

	public void onClick_btnSave() {

		try {

			if (BRANC_CDE != 0 && editMode == true) {

				for (TboFCOMPBRANC rec1 : this.lst_data) {
					if (rec1.getBRANC_CDE() == old_table1.getBRANC_CDE()) {

						rec1.setBRANC_NAME(txtBRANC_NAME.getValue());
						rec1.setBRANC_SHORTNAME(txtBRANC_SHORTNAME.getValue());
						rec1.setBRANCTAXNO(txtBRANCTAXNO.getValue());
						rec1.setADDR1(txtADDR1.getValue());
						rec1.setADDR2(txtADDR2.getValue());
						rec1.setTELNO(txtTELNO.getValue());
						rec1.setFAXNO(txtFAXNO.getValue());
						rec1.setUPBY(this.getLoginBean().getUSER_ID());
						rec1.setUPDT(FnDate.getTodaySqlDateTime());

						break;
					}
				}

			} else {
				TboFCOMPBRANC new_table1 = new TboFCOMPBRANC();
				new_table1.setCOMP_CDE(this.COMP_CDE);
				int new_BRANC_CDE = getMaxBranch() + 1;
				new_table1.setBRANC_CDE(new_BRANC_CDE);
				new_table1.setBRANC_NAME(txtBRANC_NAME.getValue());
				new_table1.setBRANC_SHORTNAME(txtBRANC_SHORTNAME.getValue());
				new_table1.setBRANCTAXNO(txtBRANCTAXNO.getValue());
				new_table1.setADDR1(txtADDR1.getValue());
				new_table1.setADDR2(txtADDR2.getValue());
				new_table1.setTELNO(txtTELNO.getValue());
				new_table1.setFAXNO(txtFAXNO.getValue());
				new_table1.setUPBY(this.getLoginBean().getUSER_ID());
				new_table1.setUPDT(FnDate.getTodaySqlDateTime());
				new_table1.setINSBY(this.getLoginBean().getUSER_ID());
				new_table1.setINSDT(FnDate.getTodaySqlDateTime());
				lst_data.add(new_table1);
			}

			if (!Fnc.isEmpty(callBackMethodName)) {
				ZkUtil.callBackMethod(this.callBackClass, this.callBackMethodName);
			}

			this.onClose();

		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e.getMessage());
		}
	}

	private int getMaxBranch() {
		int ret = 0;
		if (lst_data.size() > 0) {
			TboFCOMPBRANC rec1 = lst_data.get(lst_data.size() - 1);
			ret = rec1.getBRANC_CDE();
		}
		return ret;
	}

}

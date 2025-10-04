package com.pcc.gl.ui.acEntrQry;


import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;

import com.pcc.gl.tbf.TbfACGL_VATWH_DR;
import com.pcc.gl.tbm.TbmFCUS;
import com.pcc.gl.tbo.TboACGL_DETAIL;
import com.pcc.gl.tbo.TboACGL_VATWH_DR;
import com.pcc.sys.lib.FWinUtil;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.MyDatebox;
import com.pcc.sys.lib.MyDecimalbox;

public class AcglVatWhDrView extends FWinUtil {

	private static final long serialVersionUID = 1L;
	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public Textbox txtDESCR;
	public Textbox txtCUST_CDE, txtCUST_NAME, txtCUST_BRANCH_ID;
	public Textbox txtDOCNO;
	public MyDatebox tdbDOCDATE;
	public MyDecimalbox decVAT_RATE;
	public MyDecimalbox decAMT;
	public MyDecimalbox decBASE_AMT;

	public Button btnExit;

	private boolean doMapObj = false;
	private TboACGL_DETAIL acgl_detail;

	@Override
	public void setFormObj() {

		if (this.doMapObj == false) {

			txtDESCR = (Textbox) getFellow("txtDESCR");
			txtCUST_CDE = (Textbox) getFellow("txtCUST_CDE");
			txtCUST_NAME = (Textbox) getFellow("txtCUST_NAME");
			txtCUST_BRANCH_ID = (Textbox) getFellow("txtCUST_BRANCH_ID");
			txtDOCNO = (Textbox) getFellow("txtDOCNO");
			tdbDOCDATE = (MyDatebox) getFellow("tdbDOCDATE");
			decVAT_RATE = (MyDecimalbox) getFellow("decVAT_RATE");
			decAMT = (MyDecimalbox) getFellow("decAMT");
			decBASE_AMT = (MyDecimalbox) getFellow("decBASE_AMT");

			btnExit = (Button) getFellow("btnExit");

			this.doMapObj = true;

			// == set render ถ้ามี
		}

	}

	@Override
	public void addEnterIndex() {
	}

	@Override
	public void formInit() {
	}

	public void onOK() {
	}

	public void read_recode() {

		try {

			TboACGL_VATWH_DR vatwhDr = new TboACGL_VATWH_DR();

			vatwhDr.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			vatwhDr.setVOU_TYPE(this.getAcgl_detail().getVOU_TYPE());
			vatwhDr.setVOU_NO(this.getAcgl_detail().getVOU_NO());
			vatwhDr.setVOU_SEQ(this.getAcgl_detail().getVOU_SEQ());
			vatwhDr.setVOU_DSEQ(1);

			if (TbfACGL_VATWH_DR.find(vatwhDr)) {

				txtDESCR.setValue(vatwhDr.getDESCR());
				txtCUST_CDE.setValue(vatwhDr.getCUST_CDE());

				String[] cust_info = { "", "" };
				TbmFCUS.getCustName(vatwhDr.getCUST_CDE(), cust_info, this.getLoginBean());
				txtCUST_NAME.setValue(cust_info[0]);
				txtCUST_BRANCH_ID.setValue(cust_info[1]);

				txtDOCNO.setValue(vatwhDr.getDOCNO());
				tdbDOCDATE.setValue(vatwhDr.getDOCDATE());

				decVAT_RATE.setValue(vatwhDr.getVAT_RATE());
				decAMT.setValue(vatwhDr.getAMT());
				decBASE_AMT.setValue(vatwhDr.getBASE_AMT());

			}

		} catch (Exception e) {
			Msg.info(e.getMessage());
		}

	}

	public TboACGL_DETAIL getAcgl_detail() {
		return acgl_detail;
	}

	public void setAcgl_detail(TboACGL_DETAIL acgl_detail) {
		this.acgl_detail = acgl_detail;
	}

}

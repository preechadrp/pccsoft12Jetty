package com.pcc.gl.ui.acEntrQry;


import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;

import com.pcc.gl.tbf.TbfACGL_AP;
import com.pcc.gl.tbm.TbmFCUS;
import com.pcc.gl.tbo.TboACGL_AP;
import com.pcc.gl.tbo.TboACGL_DETAIL;
import com.pcc.sys.lib.FWinUtil;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.MyDatebox;

public class AcglApView extends FWinUtil {

	private static final long serialVersionUID = 1L;
	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public Textbox txtDESCR, txtDOCNO;
	public Textbox txtCUST_CDE, txtCUST_NAME;
	public MyDatebox tdbDOCDATE, tdbDUEDATE;
	public Button btnExit;

	private boolean doMapObj = false;
	private TboACGL_DETAIL acgl_detail;

	@Override
	public void setFormObj() {

		if (this.doMapObj == false) {

			txtDESCR = (Textbox) getFellow("txtDESCR");
			txtDOCNO = (Textbox) getFellow("txtDOCNO");
			tdbDOCDATE = (MyDatebox) getFellow("tdbDOCDATE");
			txtCUST_CDE = (Textbox) getFellow("txtCUST_CDE");
			txtCUST_NAME = (Textbox) getFellow("txtCUST_NAME");
			tdbDUEDATE = (MyDatebox) getFellow("tdbDUEDATE");

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

			TboACGL_AP ap = new TboACGL_AP();

			ap.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			ap.setVOU_TYPE(this.getAcgl_detail().getVOU_TYPE());
			ap.setVOU_NO(this.getAcgl_detail().getVOU_NO());
			ap.setVOU_SEQ(this.getAcgl_detail().getVOU_SEQ());
			ap.setVOU_DSEQ(1);

			if (TbfACGL_AP.find(ap)) {

				txtDESCR.setValue(ap.getDESCR());
				txtDOCNO.setValue(ap.getDOCNO());
				tdbDOCDATE.setValue(ap.getDOCDATE());
				txtCUST_CDE.setValue(ap.getCUST_CDE());

				String[] cust_name = { "" };
				TbmFCUS.getCustName(ap.getCUST_CDE(), cust_name, this.getLoginBean());
				txtCUST_NAME.setValue(cust_name[0]);

				tdbDUEDATE.setValue(ap.getDUEDATE());

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

package com.pcc.gl.ui.acEntrQry;


import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;

import com.pcc.gl.tbf.TbfACGL_VATPUR;
import com.pcc.gl.tbm.TbmFCUS;
import com.pcc.gl.tbo.TboACGL_DETAIL;
import com.pcc.gl.tbo.TboACGL_VATPUR;
import com.pcc.sys.lib.FWinUtil;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.MyDatebox;
import com.pcc.sys.lib.MyDecimalbox;

public class AcglVatpurView extends FWinUtil {

	private static final long serialVersionUID = 1L;
	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public Textbox txtDESCR, txtDOCNO;
	public Textbox txtCUST_CDE, txtCUST_NAME, txtCUST_BRANCH_ID;
	public MyDatebox tdbDOCDATE;
	public MyDecimalbox decVAT_RATE;
	public MyDecimalbox decAMT;
	public MyDecimalbox decBASE_AMT;

	public Button btnExit;

	private boolean doMapObj = false;
	private TboACGL_DETAIL acgl_detail;
	private int tax_type = 0;// 1=ภาษีซื้อรอตัด ,2=ภาษีซื้อ

	@Override
	public void setFormObj() {

		if (this.doMapObj == false) {

			txtDESCR = (Textbox) getFellow("txtDESCR");
			txtDOCNO = (Textbox) getFellow("txtDOCNO");
			tdbDOCDATE = (MyDatebox) getFellow("tdbDOCDATE");
			txtCUST_CDE = (Textbox) getFellow("txtCUST_CDE");
			txtCUST_NAME = (Textbox) getFellow("txtCUST_NAME");
			txtCUST_BRANCH_ID = (Textbox) getFellow("txtCUST_BRANCH_ID");
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

		addEnterIndex(txtDOCNO);
		addEnterIndex(tdbDOCDATE);
		addEnterIndex(txtCUST_CDE);
		addEnterIndex(decVAT_RATE);
		addEnterIndex(decBASE_AMT);

	}

	@Override
	public void formInit() {
	}

	public void onOK() {
	}

	public void read_recode() {

		try {

			TboACGL_VATPUR vatpur = new TboACGL_VATPUR();

			vatpur.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			vatpur.setVOU_TYPE(this.getAcgl_detail().getVOU_TYPE());
			vatpur.setVOU_NO(this.getAcgl_detail().getVOU_NO());
			vatpur.setVOU_SEQ(this.getAcgl_detail().getVOU_SEQ());
			vatpur.setVOU_DSEQ(1);

			if (TbfACGL_VATPUR.find(vatpur)) {

				txtDESCR.setValue(vatpur.getDESCR());
				txtDOCNO.setValue(vatpur.getDOCNO());
				tdbDOCDATE.setValue(vatpur.getDOCDATE());
				txtCUST_CDE.setValue(vatpur.getCUST_CDE());

				String[] cust_info = { "", "" };
				TbmFCUS.getCustName(vatpur.getCUST_CDE(), cust_info, this.getLoginBean());
				txtCUST_NAME.setValue(cust_info[0]);
				txtCUST_BRANCH_ID.setValue(cust_info[1]);

				decVAT_RATE.setValue(vatpur.getVAT_RATE());
				decAMT.setValue(vatpur.getAMT());
				decBASE_AMT.setValue(vatpur.getBASE_AMT());

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

	/**
	 * @return 1=ภาษีซื้อรอตัด ,2=ภาษีซื้อ
	 */
	public int getTax_type() {
		return tax_type;
	}

	/**
	 * @param tax_type 1=ภาษีซื้อรอตัด ,2=ภาษีซื้อ
	 */
	public void setTax_type(int tax_type) {
		this.tax_type = tax_type;
	}

}

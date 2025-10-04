package com.pcc.gl.ui.acEntrQry;


import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Textbox;

import com.pcc.gl.tbf.TbfACGL_VATWH_CR;
import com.pcc.gl.tbm.TbmFCUS;
import com.pcc.gl.tbo.TboACGL_DETAIL;
import com.pcc.gl.tbo.TboACGL_VATWH_CR;
import com.pcc.sys.lib.FWinUtil;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.MyDecimalbox;
import com.pcc.sys.lib.ZkUtil;

public class AcglVatWhCrView extends FWinUtil {

	private static final long serialVersionUID = 1L;
	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public Textbox txtDESCR;
	public Textbox txtCUST_CDE, txtCUST_NAME, txtCUST_BRANCH_ID;
	public Textbox txtWHNAME;
	public Combobox cmbWHTYPE;
	public MyDecimalbox decVAT_RATE;
	public MyDecimalbox decAMT;
	public MyDecimalbox decBASE_AMT;

	public Button btnExit;

	private boolean doMapObj = false;
	private TboACGL_DETAIL acgl_detail;
	private int doc_type = 0;// 1=ภงด. 3 (บุคคลธรรมดา), 2=ภงด.53 (นิติบุคคล)

	@Override
	public void setFormObj() {

		if (this.doMapObj == false) {

			txtDESCR = (Textbox) getFellow("txtDESCR");
			txtCUST_CDE = (Textbox) getFellow("txtCUST_CDE");
			txtCUST_NAME = (Textbox) getFellow("txtCUST_NAME");
			txtCUST_BRANCH_ID = (Textbox) getFellow("txtCUST_BRANCH_ID");
			txtWHNAME = (Textbox) getFellow("txtWHNAME");
			cmbWHTYPE = (Combobox) getFellow("cmbWHTYPE");
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

		addEnterIndex(txtCUST_CDE);
		addEnterIndex(txtWHNAME);
		addEnterIndex(cmbWHTYPE);
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

			TboACGL_VATWH_CR vatwhCr = new TboACGL_VATWH_CR();

			vatwhCr.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			vatwhCr.setVOU_TYPE(this.getAcgl_detail().getVOU_TYPE());
			vatwhCr.setVOU_NO(this.getAcgl_detail().getVOU_NO());
			vatwhCr.setVOU_SEQ(this.getAcgl_detail().getVOU_SEQ());
			vatwhCr.setVOU_DSEQ(1);

			if (TbfACGL_VATWH_CR.find(vatwhCr)) {

				txtDESCR.setValue(vatwhCr.getDESCR());
				txtCUST_CDE.setValue(vatwhCr.getCUST_CDE());

				String[] cust_info = { "", "" };
				TbmFCUS.getCustName(vatwhCr.getCUST_CDE(), cust_info, this.getLoginBean());
				txtCUST_NAME.setValue(cust_info[0]);
				txtCUST_BRANCH_ID.setValue(cust_info[1]);

				txtWHNAME.setValue(vatwhCr.getWHNAME());
				ZkUtil.setSelectItemComboBoxByValue(cmbWHTYPE, vatwhCr.getWHTYPE());

				decVAT_RATE.setValue(vatwhCr.getVAT_RATE());
				decAMT.setValue(vatwhCr.getAMT());
				decBASE_AMT.setValue(vatwhCr.getBASE_AMT());

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
	 * @return 1=ภงด. 3 (บุคคลธรรมดา), 2=ภงด.53 (นิติบุคคล)
	 */
	public int getDoc_type() {
		return doc_type;
	}

	/**
	 * @param tax_type 1=ภงด. 3 (บุคคลธรรมดา), 2=ภงด.53 (นิติบุคคล)
	 */
	public void setDoc_type(int doc_type) {
		this.doc_type = doc_type;
	}

}

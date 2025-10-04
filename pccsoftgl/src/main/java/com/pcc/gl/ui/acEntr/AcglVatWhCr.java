package com.pcc.gl.ui.acEntr;

import java.math.BigDecimal;


import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Textbox;

import com.pcc.gl.find.FfFCUS;
import com.pcc.gl.progman.ManAcEntr;
import com.pcc.gl.tbf.TbfACGL_DETAIL;
import com.pcc.gl.tbf.TbfACGL_VATWH_CR;
import com.pcc.gl.tbm.TbmFCUS;
import com.pcc.gl.tbo.TboACGL_DETAIL;
import com.pcc.gl.tbo.TboACGL_VATWH_CR;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FWinUtil;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.MyDecimalbox;
import com.pcc.sys.lib.ZkUtil;

public class AcglVatWhCr extends FWinUtil {

	private static final long serialVersionUID = 1L;
	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public Textbox txtDESCR;
	public Textbox txtCUST_CDE, txtCUST_NAME, txtCUST_BRANCH_ID;
	public Textbox txtWHNAME;
	public Combobox cmbWHTYPE;
	public MyDecimalbox decVAT_RATE;
	public MyDecimalbox decAMT;
	public MyDecimalbox decBASE_AMT;

	public Button btnExit, btnSaveSub, btnSaveNoSub;

	private boolean doMapObj = false;
	private TboACGL_DETAIL acgl_detail;
	private boolean new_mode = true;
	private int doc_type = 0;// 1=ภงด. 3 (บุคคลธรรมดา), 2=ภงด.53 (นิติบุคคล)

	private Object callForm;
	private String callBackMethodName;

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
			btnSaveSub = (Button) getFellow("btnSaveSub");
			btnSaveNoSub = (Button) getFellow("btnSaveNoSub");

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
		addEnterIndex(btnSaveSub);

	}

	@Override
	public void formInit() {
	}

	public void onOK() {
		try {

			if (focusObj == txtCUST_CDE) {
				if (Fnc.isEmpty(txtCUST_CDE.getValue())) {
					popupCUST_CDE();
				} else {
					super.onOK();
				}
			} else {
				super.onOK();
			}

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	private void validateData() throws Exception {

		if (Fnc.isEmpty(txtCUST_CDE.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtCUST_CDE.getTooltiptext() + "\" ");
		}
		if (Fnc.isEmpty(txtWHNAME.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtWHNAME.getTooltiptext() + "\" ");
		}
		if (ZkUtil.getSelectItemValueComboBox(cmbWHTYPE).equals("")) {
			throw new Exception("ระบุช่อง \"" + cmbWHTYPE.getTooltiptext() + "\" ");
		}
		if (decVAT_RATE.getValue().compareTo(BigDecimal.ZERO) == 0) {
			throw new Exception("ระบุช่อง \"" + decVAT_RATE.getTooltiptext() + "\" ");
		}
		if (decBASE_AMT.getValue().compareTo(BigDecimal.ZERO) == 0) {
			throw new Exception("ระบุช่อง \"" + decBASE_AMT.getTooltiptext() + "\" ");
		}

	}

	public void onClick_btnSave(int save_option) {

		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				if (save_option == 1) {
					validateData();
				}

				if (!TbfACGL_DETAIL.find(dbc, this.getAcgl_detail())) {
					throw new Exception("ไม่พบรายการ GL");
				}

				if (save_option == 1) {

					// == insert/update acgl_vatwh_cr
					TboACGL_VATWH_CR vatwhCr = new TboACGL_VATWH_CR();

					vatwhCr.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
					vatwhCr.setVOU_TYPE(this.getAcgl_detail().getVOU_TYPE());
					vatwhCr.setVOU_NO(this.getAcgl_detail().getVOU_NO());
					vatwhCr.setVOU_SEQ(this.getAcgl_detail().getVOU_SEQ());
					vatwhCr.setVOU_DSEQ(1);

					boolean old_rec = TbfACGL_VATWH_CR.find(dbc, vatwhCr);

					if (!Fnc.isEmpty(vatwhCr.getDOCNO())) {
						throw new Exception("มีการพิมพ์เอกสารแล้วแก้ไขไม่ได้");
					}

					vatwhCr.setPOSTDATE(this.getAcgl_detail().getPOSTDATE());
					vatwhCr.setDOC_TYPE(this.getDoc_type());// 1=ภงด. 3 (บุคคลธรรมดา), 2=ภงด.53 (นิติบุคคล)
					vatwhCr.setWHNAME(txtWHNAME.getValue());
					vatwhCr.setWHTYPE(ZkUtil.getSelectItemValueComboBox(cmbWHTYPE));
					vatwhCr.setVAT_RATE(decVAT_RATE.getValue());
					vatwhCr.setNUM_TYPE(BigDecimal.ONE);
					vatwhCr.setAMT(decAMT.getValue());
					vatwhCr.setBASE_AMT(decBASE_AMT.getValue());
					vatwhCr.setSECT_ID(this.getAcgl_detail().getSECT_ID());
					vatwhCr.setDESCR(txtDESCR.getValue());
					vatwhCr.setRECSTA(0);// 0=ยังไม่บันทึก,1=ยังไม่อนุมัติ,2=อนุมัติ,9=ยกเลิก'
					vatwhCr.setDOCNO("");
					vatwhCr.setCUST_CDE(txtCUST_CDE.getValue());
					vatwhCr.setCUST_NAME(txtCUST_NAME.getValue());

					vatwhCr.setUPBY(this.getLoginBean().getUSER_ID());
					vatwhCr.setUPDT(FnDate.getTodaySqlDateTime());

					if (!old_rec) {
						vatwhCr.setINSBY(this.getLoginBean().getUSER_ID());
						vatwhCr.setINSDT(FnDate.getTodaySqlDateTime());
						TbfACGL_VATWH_CR.insert(dbc, vatwhCr);
					} else {
						TbfACGL_VATWH_CR.update(dbc, vatwhCr, "");
					}

					// == update
					this.getAcgl_detail().setSUB_APPR("1");// 0=ยังไม่ใส่ตัวย่อย,1=ใส่ตัวย่อยแล้ว,2=ไม่ใส่ตัวย่อย
					this.getAcgl_detail().setSUB_APPR_BY(this.getLoginBean().getUSER_ID());// คนที่บันทึกตัวย่อย
					this.getAcgl_detail().setDESCR_SUB(txtCUST_NAME.getValue());

					if (!TbfACGL_DETAIL.update(dbc, this.getAcgl_detail(), "")) {
						throw new Exception("ไม่สามารถบันทึกรายการได้");
					}

				} else {

					TboACGL_VATWH_CR vatwhCr = new TboACGL_VATWH_CR();

					vatwhCr.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
					vatwhCr.setVOU_TYPE(this.getAcgl_detail().getVOU_TYPE());
					vatwhCr.setVOU_NO(this.getAcgl_detail().getVOU_NO());
					vatwhCr.setVOU_SEQ(this.getAcgl_detail().getVOU_SEQ());
					vatwhCr.setVOU_DSEQ(1);

					if (TbfACGL_VATWH_CR.find(dbc, vatwhCr)) {
						if (!Fnc.isEmpty(vatwhCr.getDOCNO())) {
							throw new Exception("มีการพิมพ์เอกสารแล้วแก้ไขไม่ได้");
						}
						TbfACGL_VATWH_CR.delete(dbc, vatwhCr);
					}

					// == update
					this.getAcgl_detail().setSUB_APPR("2");// 0=ยังไม่ใส่ตัวย่อย,1=ใส่ตัวย่อยแล้ว,2=ไม่ใส่ตัวย่อย
					this.getAcgl_detail().setSUB_APPR_BY(this.getLoginBean().getUSER_ID());// คนที่บันทึกตัวย่อย
					this.getAcgl_detail().setDESCR_SUB("");

					if (!TbfACGL_DETAIL.update(dbc, this.getAcgl_detail(), "")) {
						throw new Exception("ไม่สามารถบันทึกรายการได้");
					}

				}

				// == delete ที่ตารางอื่นๆ ป้องกันกรณีตกค้างที่
				// this.getAcgl_detail().getVOU_SEQ() นี้
				ManAcEntr.deleteAll_VOU_SEQ(dbc, this.getAcgl_detail(), "acgl_vatwh_cr");

				dbc.commit();

			}

			ZkUtil.callBackMethod(this.getCallForm(), this.getCallBackMethodName());
			this.onClose();

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void popupCUST_CDE() {
		FfFCUS.popup(this.getLoginBean(), this, "doPopupCUST_CDE");
	}

	public void doPopupCUST_CDE(String cust_cde) {

		try {
			if (read_cust(cust_cde)) {
				txtCUST_CDE.focus();
			}
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	private boolean read_cust(String cust_cde) {

		try {

			String[] cust_info = { "", "" };
			if (TbmFCUS.getCustName(cust_cde, cust_info, this.getLoginBean())) {
				txtCUST_CDE.setValue(cust_cde);
				txtCUST_NAME.setValue(cust_info[0]);
				txtCUST_BRANCH_ID.setValue(cust_info[1]);
				return true;
			} else {
				txtCUST_CDE.setValue("");
				txtCUST_NAME.setValue("");
				txtCUST_BRANCH_ID.setValue("");
				return false;
			}

		} catch (Exception e) {
			logger.info(e.getMessage());
			return false;
		}

	}

	public void read_recode() {

		try {

			txtDESCR.setValue(this.getAcgl_detail().getDESCR());

			TboACGL_VATWH_CR vatwhCr = new TboACGL_VATWH_CR();

			vatwhCr.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			vatwhCr.setVOU_TYPE(this.getAcgl_detail().getVOU_TYPE());
			vatwhCr.setVOU_NO(this.getAcgl_detail().getVOU_NO());
			vatwhCr.setVOU_SEQ(this.getAcgl_detail().getVOU_SEQ());
			vatwhCr.setVOU_DSEQ(1);

			if (TbfACGL_VATWH_CR.find(vatwhCr)) {

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

			} else {

				decVAT_RATE.setValue(BigDecimal.ZERO);
				decAMT.setValue(this.getAcgl_detail().getAMT());
				decBASE_AMT.setValue(BigDecimal.ZERO);

			}

			txtCUST_CDE.focus();

			if (isNew_mode()) {
				btnExit.setVisible(false);
			}

		} catch (Exception e) {
			Msg.info(e.getMessage());
		}

	}

	public void onChange_Data(Component comp) {
		try {
			if (comp.getId().equals("txtCUST_CDE")) {
				read_cust(txtCUST_CDE.getValue());
			}
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	public TboACGL_DETAIL getAcgl_detail() {
		return acgl_detail;
	}

	public void setAcgl_detail(TboACGL_DETAIL acgl_detail) {
		this.acgl_detail = acgl_detail;
	}

	public boolean isNew_mode() {
		return new_mode;
	}

	public void setNew_mode(boolean new_mode) {
		this.new_mode = new_mode;
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

	public Object getCallForm() {
		return callForm;
	}

	public void setCallForm(Object callForm) {
		this.callForm = callForm;
	}

	public String getCallBackMethodName() {
		return callBackMethodName;
	}

	public void setCallBackMethodName(String callBackMethodName) {
		this.callBackMethodName = callBackMethodName;
	}

}

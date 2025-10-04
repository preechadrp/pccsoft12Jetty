package com.pcc.gl.ui.acEntr;

import java.math.BigDecimal;


import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Textbox;

import com.pcc.gl.find.FfFCUS;
import com.pcc.gl.progman.ManAcEntr;
import com.pcc.gl.progman.RunningAc;
import com.pcc.gl.tbf.TbfACGL_AP;
import com.pcc.gl.tbf.TbfACGL_DETAIL;
import com.pcc.gl.tbm.TbmFCUS;
import com.pcc.gl.tbo.TboACGL_AP;
import com.pcc.gl.tbo.TboACGL_DETAIL;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FWinUtil;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.ZkUtil;

public class AcglAp extends FWinUtil {

	private static final long serialVersionUID = 1L;
	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public Textbox txtDESCR, txtDOCNO;
	public Textbox txtCUST_CDE, txtCUST_NAME;
	public Datebox tdbDOCDATE, tdbDUEDATE;
	public Button btnExit, btnSaveSub, btnSaveNoSub;

	private boolean doMapObj = false;
	private TboACGL_DETAIL acgl_detail;
	private boolean new_mode = true;

	private Object callForm;
	private String callBackMethodName;

	@Override
	public void setFormObj() {

		if (this.doMapObj == false) {

			txtDESCR = (Textbox) getFellow("txtDESCR");
			txtDOCNO = (Textbox) getFellow("txtDOCNO");
			tdbDOCDATE = (Datebox) getFellow("tdbDOCDATE");
			txtCUST_CDE = (Textbox) getFellow("txtCUST_CDE");
			txtCUST_NAME = (Textbox) getFellow("txtCUST_NAME");
			tdbDUEDATE = (Datebox) getFellow("tdbDUEDATE");

			btnExit = (Button) getFellow("btnExit");
			btnSaveSub = (Button) getFellow("btnSaveSub");
			btnSaveNoSub = (Button) getFellow("btnSaveNoSub");

			this.doMapObj = true;

			// == set render ถ้ามี
		}

	}

	@Override
	public void addEnterIndex() {

		addEnterIndex(txtDOCNO);
		addEnterIndex(tdbDOCDATE);
		addEnterIndex(txtCUST_CDE);
		addEnterIndex(tdbDUEDATE);
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

		if (Fnc.isEmpty(txtDOCNO.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtDOCNO.getTooltiptext() + "\" ");
		}
		if (tdbDOCDATE.getValue() == null) {
			throw new Exception("ระบุช่อง \"" + tdbDOCDATE.getTooltiptext() + "\" ");
		}
		if (Fnc.isEmpty(txtCUST_CDE.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtCUST_CDE.getTooltiptext() + "\" ");
		}
		if (tdbDUEDATE.getValue() == null) {
			throw new Exception("ระบุช่อง \"" + tdbDUEDATE.getTooltiptext() + "\" ");
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

					// == insert/update TboACGL_AP
					TboACGL_AP ap = new TboACGL_AP();

					ap.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
					ap.setVOU_TYPE(this.getAcgl_detail().getVOU_TYPE());
					ap.setVOU_NO(this.getAcgl_detail().getVOU_NO());
					ap.setVOU_SEQ(this.getAcgl_detail().getVOU_SEQ());
					ap.setVOU_DSEQ(1);

					boolean old_rec = TbfACGL_AP.find(dbc, ap);

					if (Fnc.getBigDecimal(ap.getCLEAR_AMT()).compareTo(BigDecimal.ZERO) != 0) {
						throw new Exception("รายการมีการทำล้างยอดแล้ว ไม่อนุญาติให้แก้ไข");
					}

					ap.setPOSTDATE(this.getAcgl_detail().getPOSTDATE());
					ap.setACCT_ID(this.getAcgl_detail().getACCT_ID());
					ap.setPOST_TYPE(1);// 1=ขาตั้งหนี้,2=ขาล้างหนี้

					if (this.getAcgl_detail().getNUM_TYPE().intValue() == 1) { // เดบิต
						ap.setNUM_TYPE(BigDecimal.ONE.negate());
					} else {
						ap.setNUM_TYPE(BigDecimal.ONE);
					}

					ap.setAMT(this.getAcgl_detail().getAMT());
					ap.setCLEAR_AMT(BigDecimal.ZERO);
					ap.setSECT_ID(this.getAcgl_detail().getSECT_ID());
					ap.setDESCR(this.getAcgl_detail().getDESCR());
					ap.setRECSTA(0);// 0=ยังไม่บันทึก,1=ยังไม่อนุมัติ,2=อนุมัติ,9=ยกเลิก'
					ap.setDOCNO(txtDOCNO.getValue());
					ap.setDOCDATE(FnDate.getSqlDate(tdbDOCDATE.getValue()));
					ap.setCUST_CDE(txtCUST_CDE.getValue());
					ap.setDUEDATE(FnDate.getSqlDate(tdbDUEDATE.getValue()));
					if (Fnc.isEmpty(ap.getLINK_NO())) {
						ap.setLINK_NO(RunningAc.get_LINK_NO(dbc, this.getLoginBean()));
					}

					ap.setUPBY(this.getLoginBean().getUSER_ID());
					ap.setUPDT(FnDate.getTodaySqlDateTime());

					if (!old_rec) {
						ap.setINSBY(this.getLoginBean().getUSER_ID());
						ap.setINSDT(FnDate.getTodaySqlDateTime());
						TbfACGL_AP.insert(dbc, ap);
					} else {
						TbfACGL_AP.update(dbc, ap, "");
					}

					// == update
					this.getAcgl_detail().setSUB_APPR("1");// 0=ยังไม่ใส่ตัวย่อย,1=ใส่ตัวย่อยแล้ว,2=ไม่ใส่ตัวย่อย
					this.getAcgl_detail().setSUB_APPR_BY(this.getLoginBean().getUSER_ID());// คนที่บันทึกตัวย่อย
					this.getAcgl_detail().setDESCR_SUB("เลขที่เอกสาร " + txtDOCNO.getValue() + " วันที่ "
							+ FnDate.displayDateString(tdbDOCDATE.getValue()) + " " + txtCUST_NAME.getValue());

					if (!TbfACGL_DETAIL.update(dbc, this.getAcgl_detail(), "")) {
						throw new Exception("ไม่สามารถบันทึกรายการได้");
					}

				} else {

					TboACGL_AP ap = new TboACGL_AP();

					ap.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
					ap.setVOU_TYPE(this.getAcgl_detail().getVOU_TYPE());
					ap.setVOU_NO(this.getAcgl_detail().getVOU_NO());
					ap.setVOU_SEQ(this.getAcgl_detail().getVOU_SEQ());
					ap.setVOU_DSEQ(1);

					if (TbfACGL_AP.find(dbc, ap)) {
						if (Fnc.getBigDecimal(ap.getCLEAR_AMT()).compareTo(BigDecimal.ZERO) != 0) {
							throw new Exception("รายการมีการทำล้างยอดแล้ว ไม่อนุญาติให้แก้ไข");
						}
						TbfACGL_AP.delete(dbc, ap);
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
				ManAcEntr.deleteAll_VOU_SEQ(dbc, this.getAcgl_detail(), "acgl_ap");

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

			String[] cust_name = { "" };
			if (TbmFCUS.getCustName(cust_cde, cust_name, getLoginBean())) {
				txtCUST_CDE.setValue(cust_cde);
				txtCUST_NAME.setValue(cust_name[0]);
				return true;
			} else {
				txtCUST_CDE.setValue("");
				txtCUST_NAME.setValue("");
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

			TboACGL_AP ap = new TboACGL_AP();

			ap.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			ap.setVOU_TYPE(this.getAcgl_detail().getVOU_TYPE());
			ap.setVOU_NO(this.getAcgl_detail().getVOU_NO());
			ap.setVOU_SEQ(this.getAcgl_detail().getVOU_SEQ());
			ap.setVOU_DSEQ(1);

			if (TbfACGL_AP.find(ap)) {

				txtDOCNO.setValue(ap.getDOCNO());
				tdbDOCDATE.setValue(ap.getDOCDATE());
				txtCUST_CDE.setValue(ap.getCUST_CDE());

				String[] cust_name = { "" };
				TbmFCUS.getCustName(ap.getCUST_CDE(), cust_name, this.getLoginBean());
				txtCUST_NAME.setValue(cust_name[0]);

				tdbDUEDATE.setValue(ap.getDUEDATE());

			}

			txtDOCNO.focus();

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

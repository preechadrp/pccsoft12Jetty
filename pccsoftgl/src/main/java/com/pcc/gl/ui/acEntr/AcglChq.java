package com.pcc.gl.ui.acEntr;


import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Textbox;

import com.pcc.gl.progman.ManAcEntr;
import com.pcc.gl.tbf.TbfACGL_DETAIL;
import com.pcc.gl.tbo.TboACGL_DETAIL;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FWinUtil;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.ZkUtil;

public class AcglChq extends FWinUtil {

	private static final long serialVersionUID = 1L;
	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public Textbox txtCHQ_PAYEE;
	public Datebox tdbCHQ_DATE;
	public Textbox txtCHQ_NO;
	public Button btnExit, btnSaveSub;

	private boolean doMapObj = false;
	private TboACGL_DETAIL acgl_detail;
	private boolean new_mode = true;

	private Object callForm;
	private String callBackMethodName;

	@Override
	public void setFormObj() {

		if (this.doMapObj == false) {

			txtCHQ_PAYEE = (Textbox) getFellow("txtCHQ_PAYEE");
			tdbCHQ_DATE = (Datebox) getFellow("tdbCHQ_DATE");
			txtCHQ_NO = (Textbox) getFellow("txtCHQ_NO");

			btnExit = (Button) getFellow("btnExit");
			btnSaveSub = (Button) getFellow("btnSaveSub");

			this.doMapObj = true;

			// == set render ถ้ามี
		}

	}

	@Override
	public void addEnterIndex() {

		addEnterIndex(txtCHQ_PAYEE);
		addEnterIndex(tdbCHQ_DATE);
		addEnterIndex(txtCHQ_NO);
		addEnterIndex(btnSaveSub);

	}

	@Override
	public void formInit() {
	}

	public void onOK() {
		try {
			super.onOK();
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	private void validateData() throws Exception {

		if (Fnc.isEmpty(txtCHQ_PAYEE.getValue())) {
			throw new Exception("ระบุช่อง \"" + txtCHQ_PAYEE.getTooltiptext() + "\" ");
		}
		if (tdbCHQ_DATE.getValue() == null) {
			throw new Exception("ระบุช่อง \"" + tdbCHQ_DATE.getTooltiptext() + "\" ");
		}

	}

	public void onClick_btnSave() {

		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {
				dbc.beginTrans();

				validateData();

				if (!TbfACGL_DETAIL.find(dbc, this.getAcgl_detail())) {
					throw new Exception("ไม่พบรายการ GL");
				}

				// == update
				this.getAcgl_detail().setSUB_APPR("1");// 0=ยังไม่ใส่ตัวย่อย,1=ใส่ตัวย่อยแล้ว,2=ไม่ใส่ตัวย่อย
				this.getAcgl_detail().setSUB_APPR_BY(this.getLoginBean().getUSER_ID());// คนที่บันทึกตัวย่อย
				String desc = txtCHQ_PAYEE.getValue() + " Chqno: " + txtCHQ_NO.getValue() + " - "
						+ FnDate.displayDateString(tdbCHQ_DATE.getValue());
				if (Fnc.isEmpty(txtCHQ_NO.getValue())) {
					desc = txtCHQ_PAYEE.getValue() + " - " + FnDate.displayDateString(tdbCHQ_DATE.getValue());
				}
				this.getAcgl_detail().setDESCR_SUB(desc);

				this.getAcgl_detail().setCHQ_TYPE("1");// 1=เช็ครอตัดโดยระบบ ,2=เช็ครอตัดยกมา (ไม่เอาไปออกใน gl
														// และงบต่างๆ)
				this.getAcgl_detail().setCHQ_NO(txtCHQ_NO.getValue());
				this.getAcgl_detail().setCHQ_DATE(FnDate.getSqlDate(tdbCHQ_DATE.getValue()));
				this.getAcgl_detail().setCHQ_PAYEE(txtCHQ_PAYEE.getValue());

				if (!TbfACGL_DETAIL.update(dbc, this.getAcgl_detail(), "")) {
					throw new Exception("ไม่สามารถบันทึกรายการได้");
				}

				// == delete ที่ตารางอื่นๆ ป้องกันกรณีตกค้างที่
				// this.getAcgl_detail().getVOU_SEQ() นี้
				ManAcEntr.deleteAll_VOU_SEQ(dbc, this.getAcgl_detail(), "");

				dbc.commit();

			}

			ZkUtil.callBackMethod(this.getCallForm(), this.getCallBackMethodName());
			this.onClose();

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void read_recode() {

		try {

			if (!TbfACGL_DETAIL.find(this.getAcgl_detail())) {
				throw new Exception("ไม่พบรายการ GL");
			}

			txtCHQ_PAYEE.setValue(this.getAcgl_detail().getCHQ_PAYEE());
			tdbCHQ_DATE.setValue(this.getAcgl_detail().getCHQ_DATE());
			txtCHQ_NO.setValue(this.getAcgl_detail().getCHQ_NO());
			txtCHQ_PAYEE.focus();

			if (isNew_mode()) {
				btnExit.setVisible(false);
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

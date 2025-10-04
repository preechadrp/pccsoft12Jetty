package com.pcc.gl.ui.acEntrQry;


import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;

import com.pcc.gl.tbf.TbfACGL_DETAIL;
import com.pcc.gl.tbo.TboACGL_DETAIL;
import com.pcc.sys.lib.FWinUtil;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.MyDatebox;

public class AcglChqView extends FWinUtil {

	private static final long serialVersionUID = 1L;
	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public Textbox txtCHQ_PAYEE;
	public MyDatebox tdbCHQ_DATE;
	public Textbox txtCHQ_NO;
	public Button btnExit;

	private boolean doMapObj = false;
	private TboACGL_DETAIL acgl_detail;

	@Override
	public void setFormObj() {

		if (this.doMapObj == false) {

			txtCHQ_PAYEE = (Textbox) getFellow("txtCHQ_PAYEE");
			tdbCHQ_DATE = (MyDatebox) getFellow("tdbCHQ_DATE");
			txtCHQ_NO = (Textbox) getFellow("txtCHQ_NO");

			btnExit = (Button) getFellow("btnExit");

			this.doMapObj = true;

			// == set render ถ้ามี
		}

	}

	@Override
	public void addEnterIndex() {

		addEnterIndex(txtCHQ_PAYEE);
		addEnterIndex(tdbCHQ_DATE);
		addEnterIndex(txtCHQ_NO);

	}

	@Override
	public void formInit() {
	}

	public void onOK() {
	}

	public void read_recode() {

		try {

			if (!TbfACGL_DETAIL.find(this.getAcgl_detail())) {
				throw new Exception("ไม่พบรายการ GL");
			}

			txtCHQ_PAYEE.setValue(this.getAcgl_detail().getCHQ_PAYEE());
			tdbCHQ_DATE.setValue(this.getAcgl_detail().getCHQ_DATE());
			txtCHQ_NO.setValue(this.getAcgl_detail().getCHQ_NO());

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

package com.pcc.sys.ui.FrmLoginUser;

import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;

import com.pcc.sys.lib.FConstComm;
import com.pcc.sys.lib.FEncrypterData;
import com.pcc.sys.lib.FWindow;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.ZkUtil;

public class SetUserPassword extends FWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Textbox txtNewpass, txtRenewpass;
	private Button btnOk;

//	private String return_password = "";

	private Object callForm;
	private String methodName;

//	public String getReturn_password() {
//		return return_password;
//	}
//
//	public void setReturn_password(String return_password) {
//		this.return_password = return_password;
//	}

	@Override
	public void setFormObj() {
		txtNewpass = (Textbox) getFellow("txtNewpass");
		txtRenewpass = (Textbox) getFellow("txtRenewpass");
		btnOk = (Button) getFellow("btnOk");

	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(txtNewpass);
		addEnterIndex(txtRenewpass);
		addEnterIndex(btnOk);

	}

	@Override
	public void formInit() {
	}

	public void onClick_btnOk() {
		try {
			String p1 = txtNewpass.getValue().trim();
			String p2 = txtRenewpass.getValue().trim();
			if (p1.equals("")) {
				throw new Exception("ระบุรหัสผ่าน");
			}
			if (!p1.equals(p2)) {
				throw new Exception("รหัสผ่านไม่ตรงกัน");
			} else {
				String password1 = "";

				if (FConstComm.encodePassword) {
					FEncrypterData crypter = new FEncrypterData();
					password1 = crypter.encode(p1);
				} else {
					password1 = p1;
				}

				ZkUtil.callBackMethod(this.getCallForm(), this.getMethodName(), password1);
				super.onClose();

			}
		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void onClick_btnCancel() {
		super.onClose();
	}

	public Object getCallForm() {
		return callForm;
	}

	public void setCallForm(Object callForm) {
		this.callForm = callForm;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

}

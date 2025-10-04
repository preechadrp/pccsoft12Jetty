package com.pcc.sys.ui;

import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FEncrypterData;
import com.pcc.sys.lib.FWindow;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.tbf.TbfFUSER;

public class FrmChgpass extends FWindow {

	private static final long serialVersionUID = 1L;

	private Textbox txtUserpass, txtNewpass, txtRenewpass;
	private Label lblUsername;
	private Button btnOk, btnCancel;
	private boolean hideBtnCancel = false;
	private String oldPassword = "";

	private LoginBean loginBean = null;

	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void setFormObj() {
		lblUsername = (Label) getFellow("lblUsername");
		txtUserpass = (Textbox) getFellow("txtUserpass");
		txtNewpass = (Textbox) getFellow("txtNewpass");
		txtRenewpass = (Textbox) getFellow("txtRenewpass");
		btnOk = (Button) getFellow("btnOk");
		btnCancel = (Button) getFellow("btnCancel");
	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(txtUserpass);
		addEnterIndex(txtNewpass);
		addEnterIndex(txtRenewpass);
		addEnterIndex(btnOk);
		addEnterIndex(btnCancel);
	}

	@Override
	public void formInit() {
		try {
			lblUsername.setValue(getLoginBean().getTboFuser().getTITLE() + getLoginBean().getTboFuser().getFNAME_THAI() + " " + getLoginBean().getTboFuser().getLNAME_THAI());
			txtUserpass.setValue("");
			txtUserpass.focus();
		} catch (Exception e) {
		}
	}

	public void onOK() {
		super.onOK();
	}

	public void onClick_btnOk() {
		try {

			try (FDbc dbc = FDbc.connectMasterDb()) {

				if (!TbfFUSER.find(dbc, loginBean.getTboFuser())) {
					throw new Exception("ไม่พบ User นี้กรุณาตรวจสอบ");
				}

				String input_userpass = txtUserpass.getValue().trim();
				String input_new_password = txtNewpass.getValue().trim();
				String input_renew_password = txtRenewpass.getValue().trim();

				String old_password = loginBean.getTboFuser().getPASSWD();

				if (input_userpass.equals("")) {
					throw new Exception("ระบุรหัสผ่านเดิม");
				}
				if (input_new_password.equals("")) {
					throw new Exception("ระบุรหัสผ่านใหม่");
				}
				if (input_renew_password.equals("")) {
					throw new Exception("ระบุยืนยันรหัสผ่านใหม่");
				}
				if (!input_new_password.equals(input_renew_password)) {
					throw new Exception("รหัสผ่านใหม่ไม่เท่ากับยืนยันรหัสผ่าน");
				}
				if (input_userpass.equals(input_new_password)) {
					throw new Exception("รหัสผ่านใหม่ต้องไม่เท่ากับรหัสผ่านเดิม");
				}

				FEncrypterData desencrypter = new FEncrypterData();
				String old_password_enc = desencrypter.encode(input_userpass);
				String new_password_enc = desencrypter.encode(input_new_password);

				if (!old_password.equals(old_password_enc)) {
					throw new Exception("รหัสผ่านเดิมไม่ถูกต้อง");
				}

				loginBean.getTboFuser().setPASSWD(new_password_enc);
				TbfFUSER.update(dbc, loginBean.getTboFuser(), "");

			}

			this.onClose();

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public boolean isHideBtnCancel() {
		return hideBtnCancel;
	}

	public void setHideBtnCancel(boolean hideBtnCancel) {
		this.hideBtnCancel = hideBtnCancel;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public LoginBean getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	public void onClose() {
		super.onClose();
	}

}

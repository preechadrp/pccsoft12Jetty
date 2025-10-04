package com.pcc.sys.lib;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

public class MsgWin extends FWinUtil {

	private static final long serialVersionUID = 1L;

	public Label lblMsg;
	public Button btnOK;
	public Button btnDetail;
	public Textbox txtStack;

	private Exception exception;

	@Override
	public void setFormObj() {
		lblMsg = (Label) getFellow("lblMsg");
		btnOK = (Button) getFellow("btnOK");
		btnDetail = (Button) getFellow("btnDetail");
		txtStack = (Textbox) getFellow("txtStack");
	}

	@Override
	public void addEnterIndex() {
	}

	@Override
	public void formInit() {
		if (exception != null) {
			lblMsg.setValue(exception.getMessage());
		}
	}

	public void onOK() {
	}

	public void showDetail() {

		try {

			if (exception != null) {

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				exception.printStackTrace(new PrintStream(baos));
				txtStack.setValue(baos.toString("UTF-8"));

				if (btnDetail.getLabel().equals("ดูรายละเอียด>>")) {
					txtStack.setVisible(true);
					btnDetail.setLabel("ซ่อนรายละเอียด<<");
				} else {
					txtStack.setVisible(false);
					btnDetail.setLabel("ดูรายละเอียด>>");
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

}

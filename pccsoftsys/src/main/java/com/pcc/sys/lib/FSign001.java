package com.pcc.sys.lib;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;

/**
 * รับลายเซน
 * @author preecha 6/5/65
 *
 */
public class FSign001 extends FWinUtil {

	private static final long serialVersionUID = 1L;

	public org.zkoss.zul.Image img22;

	public boolean click_ok = false;
	public String imgData = "";

	@Override
	public void setFormObj() {
		img22 = (org.zkoss.zul.Image) getFellow("img22");
	}

	@Override
	public void addEnterIndex() {
	}

	@Override
	public void formInit() {

	}

	public void onOK() {
		try {
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	@Listen("onClickOKFSign001")
	public void onClickOKFSign001(Event evt) {

		try {

			Object[] data = (Object[]) evt.getData();

			System.out.println("data[0] : " + data[0].toString());//data[0] จะมีค่าเป็น "image/svg+xml;base64";
			System.out.println("data[1] : " + data[1]);

			this.imgData = data[1].toString();//เก็บแค่ช่อง 2

			//== การโยนเข้า stream และนำไปใช้ใน jasper report
			//java.io.InputStream im = new java.io.ByteArrayInputStream(new org.apache.commons.codec.binary.Base64().decodeBase64(data[1].toString().getBytes("UTF-8")));
			this.img22.setSrc("data:" + data[0] + "," + data[1]);//แสดงในหน้า web

			this.click_ok = true;
			this.detach();

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	public void onClose() {
		this.click_ok = false;
		this.detach();
	}

	public void onClick_btnNo() {
		this.click_ok = false;
		this.detach();
	}

}

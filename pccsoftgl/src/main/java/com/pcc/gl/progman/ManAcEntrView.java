package com.pcc.gl.progman;

import java.sql.SQLException;

import com.pcc.gl.tbo.TboACGL_DETAIL;
import com.pcc.gl.ui.acEntrQry.AcglApView;
import com.pcc.gl.ui.acEntrQry.AcglArView;
import com.pcc.gl.ui.acEntrQry.AcglChqView;
import com.pcc.gl.ui.acEntrQry.AcglVatWhCrView;
import com.pcc.gl.ui.acEntrQry.AcglVatWhDrView;
import com.pcc.gl.ui.acEntrQry.AcglVatpurView;
import com.pcc.gl.ui.acEntrQry.AcglVatsaleView;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.ZkUtil;

public class ManAcEntrView {

	public static void viewSUB_GL(TboACGL_DETAIL detail, LoginBean loginBean) throws SQLException, Exception {

		int[] acct_type = { 0 };
		ManAcEntr.isSUB_HAS(detail, loginBean, acct_type);

		if (acct_type[0] == 1 || acct_type[0] == 2) {// == กรณีเป็นภาษีซื้อ หรือ กรณีเป็นภาษีซื้อรอตัด

			if (!ZkUtil.isPopup("AcglVatpurView")) {

				AcglVatpurView fsub = (AcglVatpurView) ZkUtil
						.callZulFile("/com/pcc/gl/zul/AcEntrQry/AcglVatpurView.zul");
				fsub.setId("AcglVatpurView");// ต้องเรียกก่อนตัวอื่น
				fsub.setFormObj();// ต้องเรียกก่อนตัวอื่น
				fsub.setAcgl_detail(detail);
				fsub.setLoginBean(loginBean);
				if (acct_type[0] == 1) {
					fsub.setTax_type(2);
				} else {
					fsub.setTax_type(1);
				}
				fsub.read_recode();
				fsub.doModal();

			}

		} else if (acct_type[0] == 3 || acct_type[0] == 4) {// == กรณีเป็นภาษีขาย หรือ กรณีเป็นภาษีขายรอตัด

			if (!ZkUtil.isPopup("AcglVatsaleView")) {
				AcglVatsaleView fsub = (AcglVatsaleView) ZkUtil
						.callZulFile("/com/pcc/gl/zul/AcEntrQry/AcglVatsaleView.zul");
				fsub.setId("AcglVatsaleView");// ต้องเรียกก่อนตัวอื่น
				fsub.setFormObj();// ต้องเรียกก่อนตัวอื่น
				fsub.setAcgl_detail(detail);
				fsub.setLoginBean(loginBean);
				if (acct_type[0] == 3) { // กรณีเป็นภาษีขาย
					fsub.setTax_type(2);
				} else { // กรณีเป็นภาษีขายรอตัด
					fsub.setTax_type(1);
				}
				fsub.read_recode();
				fsub.doModal();
			}

		} else if (acct_type[0] == 5) {// == รหัสบัญชีภาษีถูกหัก ณ ที่จ่าย

			if (detail.getNUM_TYPE().intValue() == 1) { // ขาเดบิต
				if (!ZkUtil.isPopup("AcglVatWhDrView")) {

					AcglVatWhDrView fsub = (AcglVatWhDrView) ZkUtil
							.callZulFile("/com/pcc/gl/zul/AcEntrQry/AcglVatWhDrView.zul");
					fsub.setId("AcglVatWhDrView");// ต้องเรียกก่อนตัวอื่น
					fsub.setFormObj();// ต้องเรียกก่อนตัวอื่น
					fsub.setAcgl_detail(detail);
					fsub.setLoginBean(loginBean);
					fsub.read_recode();
					fsub.doModal();

				}
			}

		} else if (acct_type[0] == 6 || acct_type[0] == 7) {// == 6=รหัสบัญชี ภงด.3 , 7=รหัสบัญชี ภงด.53

			if (detail.getNUM_TYPE().intValue() == -1) { // ขาเครดิต
				if (!ZkUtil.isPopup("AcglVatWhCrView")) {

					AcglVatWhCrView fsub = (AcglVatWhCrView) ZkUtil
							.callZulFile("/com/pcc/gl/zul/AcEntrQry/AcglVatWhCrView.zul");
					fsub.setId("AcglVatWhCrView");// ต้องเรียกก่อนตัวอื่น
					fsub.setFormObj();// ต้องเรียกก่อนตัวอื่น
					fsub.setAcgl_detail(detail);
					fsub.setLoginBean(loginBean);
					if (acct_type[0] == 6) { // ภงด.3
						fsub.setDoc_type(1);
					} else { // ภงด.53
						fsub.setDoc_type(2);
					}
					fsub.read_recode();
					fsub.doModal();

				}
			}

		} else if (acct_type[0] == 20) {// == กรณีเป็นเจ้าหนี้

			if (!ZkUtil.isPopup("AcglApView")) {

				AcglApView fsub = (AcglApView) ZkUtil.callZulFile("/com/pcc/gl/zul/AcEntrQry/AcglApView.zul");
				fsub.setId("AcglApView");// ต้องเรียกก่อนตัวอื่น
				fsub.setFormObj();// ต้องเรียกก่อนตัวอื่น
				fsub.setAcgl_detail(detail);
				fsub.setLoginBean(loginBean);
				fsub.read_recode();
				fsub.doModal();

			}

		} else if (acct_type[0] == 21) {// == กรณีเป็นลูกหนี้

			if (!ZkUtil.isPopup("AcglArView")) {

				AcglArView fsub = (AcglArView) ZkUtil.callZulFile("/com/pcc/gl/zul/AcEntrQry/AcglArView.zul");
				fsub.setId("AcglArView");// ต้องเรียกก่อนตัวอื่น
				fsub.setFormObj();// ต้องเรียกก่อนตัวอื่น
				fsub.setAcgl_detail(detail);
				fsub.setLoginBean(loginBean);
				fsub.read_recode();
				fsub.doModal();

			}

		} else if (acct_type[0] == 22) { // == กรณีเป็นเช็คจ่ายล่วงหน้า

			if (detail.getNUM_TYPE().intValue() == -1) { // ขาเครดิต

				if (!ZkUtil.isPopup("AcglChqView")) {

					AcglChqView fsub = (AcglChqView) ZkUtil.callZulFile("/com/pcc/gl/zul/AcEntrQry/AcglChqView.zul");
					fsub.setId("AcglChqView");// ต้องเรียกก่อนตัวอื่น
					fsub.setFormObj();// ต้องเรียกก่อนตัวอื่น
					fsub.setAcgl_detail(detail);
					fsub.setLoginBean(loginBean);
					fsub.read_recode();
					fsub.doModal();

				}

			}

		}

	}

}

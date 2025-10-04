package com.pcc.sys.find;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.ZkUtil;
import com.pcc.sys.tbo.TboFCOMP;

public class FindComm {

	/**
	 * เลือก method แบบ set disable-event-thread = true เพราะ zk8 ตัวนี้จะหายไป
	 * @param username
	 * @param loginBean
	 * @param callForm ชื่อ class ที่เรียก method นี้
	 * @param methodName  ชื่อ method ของ callForm ที่จะให้ทำหรือ call กลับหลังจากค้นหาและเลือก
	 */
	public static void selUsername2(String username, LoginBean loginBean, Object callForm, String methodName) {
		//ทดสอบ invoke กรณี <disable-event-thread>true</disable-event-thread>
		try {
			String id = "SeUsername2";
			if (!ZkUtil.isPopup(id)) {
				final SeUsername2 win = (SeUsername2) ZkUtil.callZulFile("/com/pcc/sys/zul/SeUsername2.zul");
				win.setId(id);
				win.setLoginBean(loginBean);
				win.setCallForm(callForm);
				win.setMethodName(methodName);
				//==
				win.setUser_Id(username);
				//==
				win.doModal();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}


	}

	public static void selUserGroup(String user_menu_group, LoginBean loginBean, Object callForm, String methodName) {

		try {

			String id = "SeUserGroup";
			if (!ZkUtil.isPopup(id)) {
				final SeUserGroup win = (SeUserGroup) ZkUtil.callZulFile("/com/pcc/sys/zul/SeUserGroup.zul");
				win.setId(id);
				win.setLoginBean(loginBean);
				win.setCallForm(callForm);
				win.setMethodName(methodName);
				//==
				win.setUser_menu_group(user_menu_group);
				//==
				win.doModal();
			}

		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}


	}

	public static void seUserCompListAdd(java.util.List<TboFCOMP> not_in_fcomp_table_lst, LoginBean loginBean, Object callForm, String methodName) {

		try {
			String id = "SeUserCompList";
			if (!ZkUtil.isPopup(id)) {
				final SeUserCompList win = (SeUserCompList) ZkUtil.callZulFile("/com/pcc/sys/zul/SeUserCompList.zul");
				win.setId(id);
				win.setLoginBean(loginBean);
				win.setCallForm(callForm);
				win.setMethodName(methodName);
				//==
				win.setNot_in_fcomp_table_lst(not_in_fcomp_table_lst);
				//==
				win.doModal();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}


	}

}

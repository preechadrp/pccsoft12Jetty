package com.pcc.sys.find;

import com.pcc.sys.lib.FSign001;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.ZkUtil;

public class FfFsign001 {

	public static void popup() {

		try {

			FSign001 win1 = (FSign001) ZkUtil.callZulFile("/com/pcc/sys/lib/FSign001.zul");
			win1.setPosition("center,top");
			win1.doModal();

		} catch (Exception e) {
			Msg.error(e);
		}

	}

}

package com.pcc.sys.lib.asterisk;

import org.asteriskjava.live.ChannelState;
import org.asteriskjava.live.LinkedChannelHistoryEntry;

public class FRunnable implements Runnable {

	private boolean printRingingMsg = true;
	private boolean printUpMsg = true;
	private FAsterisk fAsterisk = null;

	public FRunnable(FAsterisk fAsterisk) {
		this.fAsterisk = fAsterisk;
	}

	@Override
	public void run() {

		try {

			//System.out.println("Monitor channel on " + channel);
			/* ตัวอย่างข้อมูล
			 AsteriskChannel[
				id='1636710483.1356',
				name='SIP/7872-00000454',
				callerId='"7872-PSTG-Preecha" <7872>',
				state='UP',
				account='',
				dateOfCreation=Fri Nov 12 16:48:03 ICT 2021,
				dialedChannel=AsteriskChannel[
				  [id='1636710489.1359',name='SIP/7875-00000457'],
				  xxxx
				],
				dialingChannel=null,
				linkedChannel=null
			  ]
			 */

			if (fAsterisk.channel.getState().compareTo(ChannelState.DOWN) == 0) {
				//สายไม่ได้ใช้งาน
				System.out.println("Channel Status =>DOWN");
				fAsterisk.channel_status_desc = "DOWN";

			} else if (fAsterisk.channel.getState().compareTo(ChannelState.DIALING) == 0) {
				//กดโทรออก
				System.out.println("Channel Status =>DIALING");
				fAsterisk.channel_status_desc = "DIALING";

			} else if (fAsterisk.channel.getState().compareTo(ChannelState.RINGING) == 0) {
				//หลังกดโทรแต่ยังไม่ยกหูโทรศัพท์
				if (printRingingMsg == true) {
					printRingingMsg = false;
					System.out.println("Channel Status =>RINGING");
					fAsterisk.channel_status_desc = "RINGING";
				}

			} else if (fAsterisk.channel.getState().compareTo(ChannelState.UP) == 0) {
				//เริ่มตั้งแต่ยกหูโทรศัพท์เป็นต้นไป(รับและคุยสาย) ส่วนปลายทางจะรับไม่รับระบบจะไม่รู้
				if (printUpMsg == true) {
					printUpMsg = false;
					System.out.println("Channel Status =>UP");
					fAsterisk.channel_status_desc = "UP";
				}

				if (fAsterisk.timeDateLinked == null) {
					//ตัวนี้ size > 0 ถ้าปลายทางรับสาย
					java.util.List<LinkedChannelHistoryEntry> lst_channel = fAsterisk.channel.getLinkedChannelHistory();
					for (LinkedChannelHistoryEntry lkChannel : lst_channel) {
						fAsterisk.timeDateLinked = lkChannel.getDateLinked();//Thu Nov 18 17:34:31 ICT 2021   เวลาปลายทางรับสาย
						//fAsterisk.timeDateUnlinked = lkChannel.getDateUnlinked();//Thu Nov 18 17:34:35 ICT 2021  เวลาปลายทางวางสาย
						//lkChannel.getChannel().getCallerId().getNumber();//จากข้างบนคือ 0902917888 ซึ่งเป็นเบอร์ปลายทาง
					}
				}

			} else if (fAsterisk.channel.getState().compareTo(ChannelState.BUSY) == 0) {
				//สายไม่ว่าง
				System.out.println("Channel Status =>BUSY");
				//จากการทดสอบ ดักใน onBusy ของ OriginateCallBack ได้ผลกว่า เพราะตัวนี้ดักไม่ทัน
				fAsterisk.channel_status_desc = "BUSY";

			} else if (fAsterisk.channel.getState().compareTo(ChannelState.HUNGUP) == 0) {
				//หลังจากวางสาย ไม่ว่าต้นทางวางสายหรือปลายทางวางสาย
				System.out.println("Channel Status =>HUNGUP");
				fAsterisk.channel_status_desc = "HUNGUP";

				if (fAsterisk.timeHangup == 0l) {//ป้องกัน thread ถัดมาทำงานซ้ำ

					fAsterisk.timeHangup = System.currentTimeMillis();

					//ตัวนี้ size > 0 ถ้าปลายทางรับสาย
					java.util.List<LinkedChannelHistoryEntry> lst_channel = fAsterisk.channel.getLinkedChannelHistory();
					for (LinkedChannelHistoryEntry lkChannel : lst_channel) {
						//fAsterisk.timeDateLinked = lkChannel.getDateLinked();//Thu Nov 18 17:34:31 ICT 2021   เวลาปลายทางรับสาย
						fAsterisk.timeDateUnlinked = lkChannel.getDateUnlinked();//Thu Nov 18 17:34:35 ICT 2021  เวลาปลายทางวางสาย
						//lkChannel.getChannel().getCallerId().getNumber();//จากข้างบนคือ 0902917888 ซึ่งเป็นเบอร์ปลายทาง
					}

					fAsterisk.scheduler.shutdown();
					System.out.println("scheduler.shutdown()");
					fAsterisk.disconnectServer();

				}

			} else if (fAsterisk.channel.getState().compareTo(ChannelState.DIALING_OFFHOOK) == 0) {
				System.out.println("Channel Status =>DIALING_OFFHOOK");
				fAsterisk.channel_status_desc = "DIALING_OFFHOOK";

			} else if (fAsterisk.channel.getState().compareTo(ChannelState.PRERING) == 0) {
				System.out.println("Channel Status =>PRERING");
				fAsterisk.channel_status_desc = "PRERING";

			} else if (fAsterisk.channel.getState().compareTo(ChannelState.RING) == 0) {
				System.out.println("Channel Status =>RING");
				fAsterisk.channel_status_desc = "RING";

			} else if (fAsterisk.channel.getState().compareTo(ChannelState.RSRVD) == 0) {
				System.out.println("Channel Status =>RSRVD");
				fAsterisk.channel_status_desc = "RSRVD";

			}

		} catch (Exception e) {
			e.printStackTrace();

			fAsterisk.channel_status_desc = "ERROR";
			if (fAsterisk.timeHangup == 0l) {

				fAsterisk.timeHangup = System.currentTimeMillis();

				if (fAsterisk.timeDateLinked != null) {
					fAsterisk.timeDateUnlinked = new java.util.Date();
				}

			}

		}

	}

}

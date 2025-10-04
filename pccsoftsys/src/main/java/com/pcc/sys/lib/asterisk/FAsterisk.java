package com.pcc.sys.lib.asterisk;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.CallerId;
import org.asteriskjava.live.DefaultAsteriskServer;
import org.asteriskjava.live.DialedChannelHistoryEntry;
import org.asteriskjava.live.LinkedChannelHistoryEntry;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.CommandAction;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.response.CommandResponse;
import org.asteriskjava.manager.response.ManagerResponse;

/**
 * 
 * @author by preecha 5/2/65
 *
 */
public class FAsterisk {

	public static void main(String[] args) {

		EventQueue.invokeLater(() -> {
			try {
				FAsterisk hel1 = new FAsterisk();
				hel1.useOriginateActionWithMonitor("7872", "preecha", "from-internal", "0902917888");// test ผ่าน
																										// 12/11/64

//				List<String> lst1 = hel1.useCommandAction("sip show peers");
//				for (String string1 : args) {
//					System.out.println(string1);
//				}

//				hel1.checkAllChannelStatus();

			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}

//	private String asterisk_host = "192.168.13.21";
//	private String asterisk_user = "admin";
//	private String asterisk_password = "amp111";
//	private int asterisk_port = 5038;

	private String asterisk_host = "192.168.13.9";
	private String asterisk_user = "dev";
	private String asterisk_password = "it@dev";
	private int asterisk_port = 5038;

	public DefaultAsteriskServer asteriskServer = null;
	public AsteriskChannel channel = null;
	private Long timeout = 20000L;
	private String mCallerId = "";
	private String mContext = "";
	private String mToExten = "";
	private String mChannel = "";
	public ScheduledExecutorService scheduler = null;
	public FRunnable fRunnable = null;

	/**
	 * เวลาที่ต้นทางยกสาย
	 */
	public long timeSuccess = 0l;

	/**
	 * เวลาที่ hungup หรือวางสาย
	 */
	public long timeHangup = 0l;

	/**
	 * เวลาที่ไม่มีผู้ตอบรับ
	 */
	public long timeNoAnswer = 0l;

	/**
	 * เวลาที่บอกว่าสายไม่ว่าง
	 */
	public long timeBusy = 0l;

	/**
	 * เวลาปลายทางรับสาย
	 */
	public java.util.Date timeDateLinked = null;

	/**
	 * เวลาปลายทางวางสาย
	 */
	public java.util.Date timeDateUnlinked = null;

	/**
	 * สถานะการโทรปัจจุบัน
	 */
	public String channel_status_desc = "";

	private void connectServer() throws IllegalStateException, IOException, AuthenticationFailedException, TimeoutException {

		if (asteriskServer == null) {
			asteriskServer = new DefaultAsteriskServer(
					this.getAsterisk_host(),
					this.getAsterisk_port(),
					this.getAsterisk_user(),
					this.getAsterisk_password());//status ManagerConnection = INITIAL
			asteriskServer.getManagerConnection().login(); //status ManagerConnection = CONNECTED
			//System.out.println("asteriskServer.getVersion()=" + asteriskServer.getVersion());
			//asteriskServer.getManagerConnection().logoff();  status ManagerConnection = DISCONNECTED
			System.out.println("Successfully login");
			//System.out.println("asteriskServer.getManagerConnection().getState().name()=" + asteriskServer.getManagerConnection().getState().name());//เช่น INITIAL,CONNECTED,DISCONNECTED
		} else {
			if (!asteriskServer.getManagerConnection().getState().name().equals("CONNECTED")) {
				asteriskServer.getManagerConnection().login();
				System.out.println("Successfully login");
			}
		}

	}

	public void disconnectServer() {

		if (asteriskServer == null) {
			//nothing
		} else {
			if (scheduler != null) {
				if (scheduler.isShutdown() == false) {
					scheduler.shutdownNow();
				}
			}
			if (!asteriskServer.getManagerConnection().getState().name().equals("CONNECTED")) {
				asteriskServer.shutdown();
				System.out.println("Successfully Disconnect Server");
			}
		}

	}

	/**
	 * ส่ง command ไปยัง asterisk server (ตัวนี้ใช้แล้วจะ disconect asterisk server ทันที)
	 * @param command
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws TimeoutException
	 * @throws AuthenticationFailedException
	 */
	public List<String> useCommandAction(String command) throws IllegalArgumentException,
			IllegalStateException, IOException, TimeoutException, AuthenticationFailedException {

		try {
			connectServer();
			return useCommandAction(this.asteriskServer, command);
		} finally {
			disconnectServer();
		}

	}

	/**
	 * ส่ง command ไปยัง asterisk server
	 * @param asteriskServer
	 * @param command
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws TimeoutException
	 * @throws AuthenticationFailedException
	 */
	public List<String> useCommandAction(
			DefaultAsteriskServer asteriskServer,
			String command) throws IllegalArgumentException, IllegalStateException, IOException,
			TimeoutException, AuthenticationFailedException {

		List<String> list = null;
		CommandAction action = new CommandAction();

		//action.setCommand("core show help");
		//action.setCommand("sip show peers");
		action.setCommand(command);

		CommandResponse cmdResponse = (CommandResponse) asteriskServer.getManagerConnection().sendAction(action);
		list = cmdResponse.getResult();
		return list;

	}

	public void checkAllChannelStatus() throws IllegalStateException, IOException,
			AuthenticationFailedException, TimeoutException {

		connectServer();

		Collection<AsteriskChannel> lstChannel = new ArrayList<AsteriskChannel>();

		lstChannel = asteriskServer.getChannels();//เฉพาะตัวที่ active

		//AsteriskChannel chn1 = asteriskServer.getChannelByName("7872");
		//if (chn1 != null) {
		//	lstChannel.add(chn1);
		//}

		/**
		 * ประโยชน์ตัวนี้คือ ใช้ประยุกต์ทำโปรแกรมดักการโทรเข้าจากภายนอกแล้วเอาเบอร์ภายนอก
		 * ไปค้นหาว่า sale คนไหนโทรหาลูกค้าล่าสุด
		 * แล้วแสดงที่หน้าจอ operation เพื่อโอนสายให้ sale ที่รับผิดชอบลูกค้านั้นๆ
		 */

		for (AsteriskChannel asteriskChannel : lstChannel) {

			boolean checkNo = true;

			//if (asteriskChannel.getCallerId().getNumber().equals("7872")) {
			//	checkNo = true;
			//}

			if (checkNo == true) {

				String s1 = "==>";

				s1 += ",getCallerId().getNumber() : " + asteriskChannel.getCallerId().getNumber();

				if (asteriskChannel.getLinkedChannel() != null) { //จะมีค่าเมื่อรับสายแล้วเท่านั้น
					s1 += ", linkNumber : " + asteriskChannel.getLinkedChannel().getCallerId().getNumber();
				}

				s1 += ", getCallerId().getName() : " + asteriskChannel.getCallerId().getName() +
						", getState() : " + asteriskChannel.getState() +
						", getName() : " + asteriskChannel.getName();

				List<DialedChannelHistoryEntry> ll1 = asteriskChannel.getDialedChannelHistory(); //ทดสอบแล้วจับเบอร์ต้นทางไม่ได้
				for (DialedChannelHistoryEntry dc2 : ll1) {
					s1 += " , call from channel : " + dc2.getChannel().getDialingChannel().getCallerId().getNumber();
				}

				List<LinkedChannelHistoryEntry> ll2 = asteriskChannel.getLinkedChannelHistory();
				for (LinkedChannelHistoryEntry dc1 : ll2) {
					if (dc1.getChannel().getDialedChannel() != null) {
						s1 += " , aa : " + dc1.getChannel().getDialedChannel();
					}
					if (dc1.getChannel().getDialedChannelHistory() != null) {
						s1 += " , bb : " + dc1.getChannel().getDialedChannelHistory();
					}
					if (dc1.getChannel().getDialedChannels() != null) {
						s1 += " , cc : " + dc1.getChannel().getDialedChannels();
					}
					if (dc1.getChannel().getDialingChannel() != null) {
						s1 += " , dd : " + dc1.getChannel().getDialingChannel();
					}
					s1 += " , d1 : " + dc1.getChannel().getCallerId().getNumber();
					//s1 += " , d2 : " + dc1.getChannel().getCurrentExtension().getExtension(); //error
					//s1 += " , d3 : " + dc1.getChannel().getExtensionHistory();

				}

				System.out.println(s1);

				//ตัวอย่างข้อมูล
				/*
				 * 
				--================= ตัวอย่างโทรจาก 0902917888 เข้าที่ 7872
				*ตอนยังไม่รับสาย
				==>,getCallerId().getNumber() : 7872, getCallerId().getName() : 7872-PSTG-Preecha, getState() : RINGING, 
				      getName() : SIP/7872-000005d6
				==>,getCallerId().getNumber() : 0902917888, getCallerId().getName() : 0902917888, getState() : UP, 
				      getName() : SIP/TRUE-PSTGIssabel-Out-000005d5
				*รับสายแล้ว
				==>,getCallerId().getNumber() : 7872, linkNumber : 0902917888, getCallerId().getName() : 7872-PSTG-Preecha, 
				      getState() : UP, getName() : SIP/7872-000005d6 , bb : [] , cc : [] ,  
				      LinkedChannelHistoryEntry.getChannel().getCallerId().getNumber() : 0902917888
				==>,getCallerId().getNumber() : 0902917888, linkNumber : 7872, getCallerId().getName() : 0902917888, 
				      getState() : UP, getName() : SIP/TRUE-PSTGIssabel-Out-000005d5 , bb : [] , cc : [] , 
				      LinkedChannelHistoryEntry.getChannel().getCallerId().getNumber() : 7872
				
				--================= ตัวอย่างนี้คือกดจากเบอร์ 7872 ที่หัวโทรศัพท์โดยตรงออกข้างนอกไปที่ 0902917888 
				*โทรออกยังไม่รับสาย
				==> getCallerId().getNumber() : 0902917888, getCallerId().getName() : CID:20179000, getState() : RINGING, 
				   getName() : SIP/TRUE_IPDID2-000004af
				==> getCallerId().getNumber() : 20179000, getCallerId().getName() : null, getState() : RING, 
				   getName() : SIP/7872-000004ae
				*ปลายทางรับสาย
				==> getCallerId().getNumber() : 0902917888, getCallerId().getName() : CID:20179000, getState() : UP, 
				   getName() : SIP/TRUE_IPDID2-000004af , bb : [] , cc : [] , 
				   LinkedChannelHistoryEntry.getChannel().getCallerId().getNumber() : 20179000 , 
				   d3 : [ExtensionHistoryEntry[date=Fri Nov 19 15:40:56 ICT 2021,
				     extension=Extension[context='macro-dialout-trunk',extension='s',priority='22',application='null',appData=null]]]
				==> getCallerId().getNumber() : 20179000, getCallerId().getName() : , getState() : UP, 
				   getName() : SIP/7872-000004ae , bb : [] , cc : [] , 
				   LinkedChannelHistoryEntry.getChannel().getCallerId().getNumber() : 0902917888 , 
				   d3 : [ExtensionHistoryEntry[date=Fri Nov 19 15:40:56 ICT 2021,extension=null]]
							
				--================= ตัวอย่างโทรภายใน กด 7872 ไปที่ 7875
				**กดโทรที่หัวโทรศัพท์แต่ 7875 ยังไม่รับสาย
				==> getCallerId().getNumber() : 7872, getCallerId().getName() : 7872-PSTG-Preecha, 
				    getState() : UP, getName() : SIP/7872-00000493 , call from channel : 7872
				==> getCallerId().getNumber() : 7875, getCallerId().getName() : 7875-PSTG-Kai Dev, 
				    getState() : RINGING, getName() : SIP/7875-00000494
				
				**7875 รับสาย
				==> getCallerId().getNumber() : 7872, getCallerId().getName() : 7872-PSTG-Preecha, getState() : UP, 
				   getName() : SIP/7872-00000493 , 
				   call from channel : 7872 , bb : [] , cc : [] , dd : 
				   AsteriskChannel[id='1637310653.1453',name='SIP/7872-00000493',callerId='"7872-PSTG-Preecha" <7872>',
				   state='UP',account='',
				   dateOfCreation=Fri Nov 19 15:30:55 ICT 2021,
				   dialedChannel=AsteriskChannel[[id='1637310655.1454',name='SIP/7875-00000494'],],
				   dialingChannel=null,linkedChannel=AsteriskChannel[id='1637310655.1454',name='SIP/7875-00000494']] , 
				   d1 : 7875 , d3 : 7875
				
				==> getCallerId().getNumber() : 7875, getCallerId().getName() : 7875-PSTG-Kai Dev, getState() : UP, 
				   getName() : SIP/7875-00000494 , 
				   aa : AsteriskChannel[id='1637310655.1454',name='SIP/7875-00000494',
				   callerId='"7875-PSTG-Kai Dev" <7875>',state='UP',account='',
				   dateOfCreation=Fri Nov 19 15:30:57 ICT 2021,dialedChannel=null,
				   dialingChannel=AsteriskChannel[id='1637310653.1453',name='SIP/7872-00000493'],
				   linkedChannel=AsteriskChannel[id='1637310653.1453',name='SIP/7872-00000493']] , 
				   bb : [DialedChannelHistoryEntry[date=Fri Nov 19 15:30:57 ICT 2021,
				   channel=AsteriskChannel[id='1637310655.1454',name='SIP/7875-00000494',
				   callerId='"7875-PSTG-Kai Dev" <7875>',state='UP',account='',
				   dateOfCreation=Fri Nov 19 15:30:57 ICT 2021,dialedChannel=null,
				   dialingChannel=AsteriskChannel[id='1637310653.1453',name='SIP/7872-00000493'],
				   linkedChannel=AsteriskChannel[id='1637310653.1453',name='SIP/7872-00000493']]]] ,
				   cc : [AsteriskChannel[id='1637310655.1454',name='SIP/7875-00000494',
				   callerId='"7875-PSTG-Kai Dev" <7875>',state='UP',
				   account='',dateOfCreation=Fri Nov 19 15:30:57 ICT 2021,dialedChannel=null,
				   dialingChannel=AsteriskChannel[id='1637310653.1453',name='SIP/7872-00000493'],
				   linkedChannel=AsteriskChannel[id='1637310653.1453',name='SIP/7872-00000493']]] , d1 : 7872 , d3 : 7872
				
				 */
			}

		}
		disconnectServer();
	}

	/**
	 * ทำการสั่งให้ zoiper โทรออกโดยไม่ต้อง monitor ดูสถานะ
	 * @param fromExten เบอร์ของหัวโทรศัพท์หรือ soft phone ที่เครื่อง telesale (เบอร์ต้นทาง)
	 * @param callerIdName เช่น preecha
	 * @param context Context ของ fromExten
	 * @param toExten เบอร์ที่ต้องการโทรหา (เบอร์ปลายทาง) กรณีต้องกด 9 ก่อนกดเบอร์ให้ใส่ 9 นำหน้าก่อน รูปแบบคือ '9'+'เบอร์ที่โทรออกข้างนอก' เช่น 90619425887 เป็นต้น 
	 * @throws IOException
	 * @throws AuthenticationFailedException
	 * @throws TimeoutException
	 */
	public void useOriginateActionWithoutMonitor(
			String fromExten,
			String callerIdName,
			String context,
			String toExten) throws IOException, AuthenticationFailedException, TimeoutException {

		// connect asterisk server
		connectServer();

		// Create Action
		OriginateAction originateAction = new OriginateAction();

		mChannel = "SIP/" + fromExten; // SIP/7872
		mCallerId = callerIdName + "<" + fromExten + ">"; // preecha<7872>
		if (context.equals("")) {
			mContext = getPeerDataOfExten(fromExten, "Context");// เช่น from-internal
		} else {
			mContext = context;
		}
		mToExten = toExten;// 0902917888

		originateAction.setChannel(mChannel);  //จากตัวอย่างนี้คือโทรจากเบอร์ 2012  ซึ่งเป็น zoiper ที่เครื่อง Telesale
		originateAction.setCallerId(mCallerId); //จากตัวอย่าง ที่เครื่อง Tele จะแสดง "peepo<2012>" แต่เครื่องผู้รับจะแสดง "max<2012>" กรณีโทรออกภายนอกจะแสดง "เบอร์ที่โทรออกตามที่ Asterisk กำหนดไว้"
		originateAction.setContext(mContext);//จากตัวอย่างนี้คือ call07 ซึ่งเป็น context ของเบอร์ sip ต้นทาง
		originateAction.setExten(mToExten);// เบอร์ปลายทาง กรณี exten ดังกล่าวไม่อยู่ในหรือเป็น sip เราจะเขียนกฏให้โทรออกข้างนอกแทน อยู่ในไฟล์ extensions.conf
		originateAction.setPriority(Integer.valueOf(1));//ต้องตรงกับในไฟล์ extensions.conf
		originateAction.setAsync(Boolean.TRUE);
		originateAction.setTimeout(30000l);

		//==== Send the originate action and wait for a maximum of 30 seconds for Asterisk to send a reply
		//แบบที่ 1
		ManagerResponse originateResponse = asteriskServer.getManagerConnection().sendAction(originateAction, 30000l);
		// Print out whether the originate succeeded or not 
		System.out.println("Response=" + originateResponse.getResponse());
		//แบบที่ 2
		//*เป็น class ที่ต้อง implements SendActionCallback แบบนี้ก็ได้
		//MySendActionCallback mySendActionCallback = new MySendActionCallback();
		//managerConnection.sendAction(originateAction, mySendActionCallback); 

		disconnectServer();
	}

	/**
	 * โทรพร้อมกับ Monitor จนกว่าจะสิ้นสุดการโทร
	 * @param fromExten เบอร์ต้นทาง(เบอร์ที่หัวโทรศัพท์เรา)
	 * @param callerIdName ชื่อที่ต้องการให้แสดงที่หัวโทรศัพท์เรา เช่น preecha
	 * @param context Context ของ fromExten
	 * @param toExten เบอร์ปลายทางที่เราต้องการโทรหา อาจเป็นเบอร์ภายใน เช่น 7874 , 0902917xxx เป็นต้น
	 * @throws Exception
	 */
	public void useOriginateActionWithMonitor(
			String fromExten,
			String callerIdName,
			String context,
			String toExten) throws Exception {

		connectServer();

		mChannel = "SIP/" + fromExten; // เช่น SIP/7872
		mCallerId = callerIdName; // เช่น preecha	
		if (context.equals("")) {
			mContext = getPeerDataOfExten(fromExten, "Context");// เช่น from-internal
		} else {
			mContext = context;
		}
		mToExten = toExten;// เช่น 0902917xxx

		this.asteriskServer.originateToExtensionAsync(
				mChannel,
				mContext,
				mToExten,
				1,
				timeout,
				new CallerId(mCallerId, fromExten),
				null,
				new FOriginateCallback(this));

	}

	private String getPeerDataOfExten(
			String fromExten, String dataName) throws IllegalArgumentException,
			IllegalStateException, IOException, TimeoutException, AuthenticationFailedException {

		String ret = "";
		List<String> lstData = useCommandAction(this.asteriskServer, "sip show peer " + fromExten);
		for (String data : lstData) {
			System.out.println("data :" + data);
			if (data.trim().startsWith(dataName)) { //เช่น Context      : from-internal
				String[] s1 = data.split(":");
				if (s1.length == 2) {
					ret = s1[1].trim();
					break;
				}
			}
		}
		return ret;

	}

	public String getAsterisk_host() {
		return asterisk_host;
	}

	public void setAsterisk_host(String asterisk_host) {
		this.asterisk_host = asterisk_host;
	}

	public String getAsterisk_user() {
		return asterisk_user;
	}

	public void setAsterisk_user(String asterisk_user) {
		this.asterisk_user = asterisk_user;
	}

	public String getAsterisk_password() {
		return asterisk_password;
	}

	public void setAsterisk_password(String asterisk_password) {
		this.asterisk_password = asterisk_password;
	}

	public int getAsterisk_port() {
		return asterisk_port;
	}

	public void setAsterisk_port(int asterisk_port) {
		this.asterisk_port = asterisk_port;
	}

}

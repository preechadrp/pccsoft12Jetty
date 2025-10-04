package com.pcc.sys.lib.asterisk;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.LiveException;
import org.asteriskjava.live.OriginateCallback;

public class FOriginateCallback implements OriginateCallback {

	public FAsterisk fAsterisk = null;

	public FOriginateCallback(FAsterisk fAsterisk) {
		this.fAsterisk = fAsterisk;
	}

	@Override
	public void onDialing(AsteriskChannel channel) {
		System.out.println("onDialing");
		//ถ้ากดปุ่ม reject ที่หัวโทรศัพท์จะขึ้น Busy แป๊ปหนึ่งแล้วขึ้น Hangup

		this.fAsterisk.channel = channel;

		//reset ตัวจับเวลา
		this.fAsterisk.timeSuccess = 0l;
		this.fAsterisk.timeNoAnswer = 0l;
		this.fAsterisk.timeBusy = 0l;
		this.fAsterisk.timeHangup = 0l;
		this.fAsterisk.fRunnable = new FRunnable(this.fAsterisk);
		this.fAsterisk.scheduler = Executors.newScheduledThreadPool(1);
		//this.fAsterisk.scheduler.scheduleWithFixedDelay(this.fRunnable, 0, 1, TimeUnit.SECONDS);//loop ทุก 1 วินาที
		this.fAsterisk.scheduler.scheduleWithFixedDelay(this.fAsterisk.fRunnable, 0, 500, TimeUnit.MILLISECONDS);//loop ทุก 500 มิลิวินาที

	}

	public void onSuccess(AsteriskChannel channel) {
		System.out.println("onSuccess");
		//จากการทดสอบเรายกสายโทรศัพท์จะทำงาน

		this.fAsterisk.channel = channel;

		if (fAsterisk.timeSuccess == 0l) {
			fAsterisk.timeSuccess = System.currentTimeMillis();
		}

	}

	public void onNoAnswer(AsteriskChannel channel) {
		System.out.println("onNoAnswer");
		//จากการทดสอบจะทำงานเมื่อเราไม่ยอมยกสายเพื่อให้โทรศัพท์โทรออก เมื่อเกิดเหตุการนี้ channel จะ HUNGUP ทันที

		this.fAsterisk.channel = channel;

		if (fAsterisk.timeNoAnswer == 0l) {
			fAsterisk.timeNoAnswer = System.currentTimeMillis();
		}

	}

	public void onBusy(AsteriskChannel channel) {
		System.out.println("onBusy");
		//จากการทดสอบเรากดปุ่ม reject ที่โทรศัพท์จะทำงาน และจะ hangup ต่อ

		this.fAsterisk.channel = channel;

		if (fAsterisk.timeBusy == 0l) {
			fAsterisk.timeBusy = System.currentTimeMillis();
		}

	}

	public void onFailure(LiveException cause) {
		System.out.println("onFailure");
		//System.err.println("Failed: " + cause.getMessage());
	}

}

package com.pcc.sys.lib;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author preecha 31/3/60
 *
 */
public class FLockProcess {

	private List synchedList;
	private boolean test_run = false;

	public FLockProcess(boolean test) {
		this.test_run = test;
		synchedList = Collections.synchronizedList(new LinkedList());
	}

	public void lock() throws InterruptedException {
		this.lock("");
	}

	/**
	 * lock
	 * @param flg  ข้อความ flag ค่าไว้
	 * @throws InterruptedException
	 */
	public void lock(String flgLock) throws InterruptedException {
		if (test_run) {
			System.out.println("to_lock..." + flgLock);
		}
		synchronized (synchedList) {
			while (!synchedList.isEmpty()) {
				if (test_run) {
					System.out.println("Waiting..." + flgLock);
				}
				synchedList.wait();
				//คำสั่งถัดจากนี้จะทำงานถ้า thread ถูกปลุกให้ทำงาน
				//do something
			}
			synchedList.add("1");
			if (test_run) {
				System.out.println("Locked..." + flgLock);
			}
		}
	}

	/**
	 * รอกี่วินาทีจึงจะทำไม่ต้องรอ
	 * @param seconds ถ้าระบุ <= 0 จะหมายถึง 1 วินาที
	 * @throws InterruptedException
	 */
	public void lock(long seconds, String flgLock) throws InterruptedException {
		if (test_run) {
			System.out.println("to_lock..." + flgLock);
		}
		synchronized (synchedList) {
			if (!synchedList.isEmpty()) {
				if (test_run) {
					System.out.println("Waiting..." + flgLock);
				}
				if (seconds > 0) {
					synchedList.wait(seconds * 1000);
				} else {
					synchedList.wait(1000);//1 วินาที
				}
				//คำสั่งถัดจากนี้จะทำงานถ้า thread ถูกปลุกให้ทำงาน
				//do something
				if (!synchedList.isEmpty()) {//รอแล้วยังไม่ว่าง
					throw new InterruptedException("หมดเวลารอ Process งานถัดไป");
				}
			}
			synchedList.add("1");
			if (test_run) {
				System.out.println("Locked..." + flgLock);
			}
		}
	}

	public void unLock() {
		//	System.out.println("unLockProcess...");
		synchronized (synchedList) {
			if ((!synchedList.isEmpty())) {
				synchedList.remove(0);
			}
			//synchedList.notifyAll(); //ตัวนี้ thread ที่เกิดหลังอาจทำงานก่อนได้
			synchedList.notify(); //เพื่อให้ทำงานตามลำดับของ  Thread ที่เกิดก่อนหลัง
			//System.out.println("notifyAll called!");
		}
	}

	//Test
	public static void main(String[] args) {

		final FLockProcess demo = new FLockProcess(true);
		final String[] step = { "" };

		Runnable runA1 = new Runnable() {
			@Override
			public void run() {
				try {
					demo.lock(step[0]);
				} catch (InterruptedException ix) {
					System.out.println("Interrupted Exception!");
				} catch (Exception x) {
					System.out.println("Exception thrown.");
				}

			}

		};

		try {

			System.out.println("== > Start Lock");

			step[0] = "A";
			Thread threadA1 = new Thread(runA1, "A");
			threadA1.start();
			Thread.sleep(200);

			step[0] = "B";
			Thread threadA2 = new Thread(runA1, "B");
			threadA2.start();
			Thread.sleep(200);

			step[0] = "C";
			Thread threadA3 = new Thread(runA1, "C");
			threadA3.start();
			Thread.sleep(200);

			step[0] = "D";
			Thread threadA4 = new Thread(runA1, "D");
			threadA4.start();
			Thread.sleep(2000);//เว้นระยะก่อนปลดล๊อก 2 วินาทีก่อนไป unlock

			System.out.println("== > Start unlock");
			demo.unLock();
			Thread.sleep(1000);
			demo.unLock();
			Thread.sleep(1000);
			demo.unLock();
			Thread.sleep(1000);

			//System.out.println("threadA1.interrupt()");
			threadA1.interrupt();

			//System.out.println("threadA2.interrupt()");
			threadA2.interrupt();

			//System.out.println("threadA3.interrupt()");
			threadA3.interrupt();

			//System.out.println("threadA4.interrupt()");
			threadA4.interrupt();

		} catch (InterruptedException x) {
		}

	}

}

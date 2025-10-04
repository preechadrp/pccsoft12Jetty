package com.pcc.sys.tbm;

import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.tbf.TbfFRUNNING;
import com.pcc.sys.tbo.TboFRUNNING;

public class TbmFRUNNING {

	public static String getRunning(String comp_cde, String running_id,
			java.sql.Date docdate, int len, boolean withMonth) throws Exception {

		try (FDbc dbc = FDbc.connectMasterDb()) {
			return getRunning(dbc, comp_cde, running_id, docdate, len, withMonth);
		}

	}

	public static String getRunning(FDbc dbc, String comp_cde, String running_id,
			java.util.Date docdate, int len, boolean withMonth) throws Exception {

		String ret = "";

		TboFRUNNING ru = new TboFRUNNING();

		ru.setCOMP_CDE(comp_cde);
		ru.setRUNNING_ID(running_id.trim());

		String runPrefix = "";
		if (withMonth) {
			runPrefix = FnDate.getPrefixDocnoYY(docdate) + Fnc.getStrRight("0" + FnDate.getMonth(docdate), 2);
		} else {
			runPrefix = FnDate.getPrefixDocnoYY(docdate);
		}
		ru.setRUNNING_PREFIX(runPrefix);

		if (TbfFRUNNING.find(dbc, ru)) {

			int cur_no = ru.getRUNNING_NO();

			//== update
			if (cur_no < 0) {
				ru.setRUNNING_NO(1);
			} else {
				ru.setRUNNING_NO(cur_no + 1);
			}

			if (!TbfFRUNNING.update(dbc, ru, " RUNNING_NO = " + cur_no)) {
				throw new Exception("เลขเอกสารซ้ำกันกรุณาลองใหม่");
			}

			//== gen 
			ret = runPrefix + Fnc.getStrRight("0000000000" + (cur_no + 1), len);

		} else {

			ru.setRUNNING_NO(1);

			if (!TbfFRUNNING.insert(dbc, ru)) {
				throw new Exception("เลขเอกสารซ้ำกันกรุณาลองใหม่");
			}

			//== gen
			ret = runPrefix + Fnc.getStrRight("0000000000" + 1, len);

		}

		return ret;
	}

	/**
	 * reset เลขกลับถ้าเป็นเลขสุดท้ายจะคืนค่า true
	 * @param fdb
	 * @param comp_cde
	 * @param running_id
	 * @param docdate
	 * @param len
	 * @param withMonth
	 * @param running_prefix  ข้อความนำหน้าเลขที่รัน
	 * @param old_running เลขเอกสารเก่าที่รวม running_prefix แล้ว
	 * @return
	 * @throws Exception
	 */
	public static boolean resetRunning(FDbc fdb,
			String comp_cde, String running_id,
			java.util.Date docdate, int len, boolean withMonth,
			String running_prefix, String old_running) throws Exception {

		boolean b_ret = false;

		TboFRUNNING ru = new TboFRUNNING();

		ru.setCOMP_CDE(comp_cde);
		ru.setRUNNING_ID(running_id.trim());

		String yymmPrefix = "";
		if (withMonth) {
			yymmPrefix = FnDate.getPrefixDocnoYY(docdate) + Fnc.getStrRight("0" + FnDate.getMonth(docdate), 2);
		} else {
			yymmPrefix = FnDate.getPrefixDocnoYY(docdate);
		}
		ru.setRUNNING_PREFIX(yymmPrefix);

		if (TbfFRUNNING.find(fdb, ru)) {

			//== gen
			int last_no = ru.getRUNNING_NO();

			String last_running = running_prefix + yymmPrefix + Fnc.getStrRight("0000000000" + (last_no), len);
			if (old_running.equals(last_running)) {

				b_ret = true;

				ru.setRUNNING_NO(last_no - 1);
				if (ru.getRUNNING_NO() < 0) {
					ru.setRUNNING_NO(0);
				}

				if (!TbfFRUNNING.update(fdb, ru, " RUNNING_NO = " + last_no)) {
					throw new Exception("เลขเอกสารซ้ำกันกรุณาลองใหม่");
				}

			}

		}

		return b_ret;
	}

}

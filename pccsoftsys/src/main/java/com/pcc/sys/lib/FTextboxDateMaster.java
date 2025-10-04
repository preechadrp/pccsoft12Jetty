package com.pcc.sys.lib;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Textbox;

public class FTextboxDateMaster extends Textbox implements EventListener<Event> {

	private static final long serialVersionUID = 1L;
	private java.sql.Date date;
	private boolean nullable = true;
	private boolean present = true;
	private boolean past = true;
	private boolean future = true;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

	public FTextboxDateMaster() {
		addEventListener("onBlur", this);
		setMaxlength(10);
		setDate(FnDate.getTodaySqlDate());
	}

	public java.sql.Date getDate() {
		return date;
	}

	public void setDate(java.sql.Date date) {
		this.date = date;
		revalidateView();

	}

	private void revalidateView() {
		if (date == null) {
			setText("");
		} else {
			String temp = sdf.format(date);
			if (FnDate.useThaiDate) {
				setText(temp.substring(0, 6) + (Integer.parseInt(temp.substring(6, 10)) + FnDate.toBuddhistInc));
			} else {
				setText(temp.substring(0, 6) + Integer.parseInt(temp.substring(6, 10)));
			}
		}
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public void setText(String text) {
		super.setText(text);
		try {
			String prepareText = text.replaceAll("/", "");
			if (FnDate.useThaiDate) {
				date = Date.valueOf("" + (Integer.parseInt(prepareText.substring(4, 8)) - FnDate.toBuddhistInc) + "-" + prepareText.substring(2, 4) + "-" + prepareText.substring(0, 2));
			} else {
				date = Date.valueOf("" + Integer.parseInt(prepareText.substring(4, 8)) + "-" + prepareText.substring(2, 4) + "-" + prepareText.substring(0, 2));
			}
		} catch (Exception e) {
			date = null;
		}
	}

	public boolean isAsap() {
		return true;
	}

	public void onEvent(Event event) {
		if (event.getName().equalsIgnoreCase("onBlur")) {
			String error = isValidDate();
			if (!error.equals("")) {
				//focus();
				//select();
				throw new WrongValueException(this, error);
			}
		}
	}

	public String isValidDate() {
		String inputText = getText().trim();
		if (nullable && inputText.length() == 0) {
			return "";
		}

		//ถ้าผู้ใช้ input เป็นรูปแบบ dd/mm/yyyy ให้เป็น ddmmyyyy เพื่อจะได้ตรวจสอบความถูกต้องโดย logic เดิมของพี่ต้น
		String prepareText = inputText.replaceAll("/", "");

		try {
			if (FnDate.useThaiDate) {
				date = Date.valueOf("" + (Integer.parseInt(prepareText.substring(4, 8)) - FnDate.toBuddhistInc) + "-" + prepareText.substring(2, 4) + "-" + prepareText.substring(0, 2));
			} else {
				date = Date.valueOf("" + Integer.parseInt(prepareText.substring(4, 8)) + "-" + prepareText.substring(2, 4) + "-" + prepareText.substring(0, 2));
			}
		} catch (Exception e) {
			date = null;
			return "ต้องอยู่ในรูป ddmmyyyy หรือ dd/mm/yyyy เท่านั้น";
		}
		if (FnDate.useThaiDate) {
			if (Integer.parseInt(prepareText.substring(4, 8)) < 2400) {
				return "ใช้ปี พ.ศ. ไม่ใช่ ค.ศ.";
			}
		}
		int nday = Integer.parseInt(prepareText.substring(0, 2));
		int nmon = Integer.parseInt(prepareText.substring(2, 4));
		int nyear = 0;
		if (FnDate.useThaiDate) {
			nyear = Integer.parseInt(prepareText.substring(4, 8)) - FnDate.toBuddhistInc;
		} else {
			nyear = Integer.parseInt(prepareText.substring(4, 8));
		}
		nyear = nyear % 4;
		int maxday;
		if (nmon == 4 || nmon == 6 || nmon == 9 || nmon == 11) {
			maxday = 30;
		} else {
			maxday = 31;
		}
		if (nmon == 2) {
			if (nyear == 0) {
				maxday = 29;
			} else {
				maxday = 28;
			}
		}
		if (nmon < 1 || nmon > 12) {
			return "เดือนต้องอยู่ระหว่าง 01 ถึง 12";
		}
		if (nday < 1 || nday > maxday) {
			return "วันที่ต้องอยู่ระหว่าง 01 ถึง " + maxday;
		}

		Date currentDate = FnDate.getTodaySqlDate();
		if (!past && FnDate.compareDate(date, currentDate) < 0) {
			return "วันที่ต้องไม่น้อยกว่าวันปัจจุบัน";
		}
		if (!present && FnDate.compareDate(date, currentDate) == 0) {
			return "วันที่ต้องไม่เท่ากับวันปัจจุบัน";
		}
		if (!future && FnDate.compareDate(date, currentDate) > 0) {
			return "วันที่ต้องไม่มากกว่าวันปัจจุบัน";
		}

		revalidateView();
		return "";

	}

	public boolean isPresent() {
		return present;
	}

	public void setPresent(boolean present) {
		this.present = present;
	}

	public boolean isPast() {
		return past;
	}

	public void setPast(boolean past) {
		this.past = past;
	}

	public boolean isFuture() {
		return future;
	}

	public void setFuture(boolean future) {
		this.future = future;
	}

}

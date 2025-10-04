package com.pcc.gl.ui.acArClear;

import java.util.ArrayList;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;

import com.pcc.gl.lib.FConstAc;
import com.pcc.gl.progman.ManAcArClear;
import com.pcc.gl.ui.acEntrQry.AcEntrView;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FWinUtil;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.ZkUtil;

public class AcArClearView extends FWinUtil {

	private static final long serialVersionUID = 1L;

	private Grid gridAccList;
	private java.util.List<FModelHasMap> lst_gl_header = new ArrayList<FModelHasMap>();
	public Button btnExit;

	private String link_no;

	@Override
	public void setFormObj() {
		gridAccList = (Grid) getFellow("gridAccList");
		btnExit = (Button) getFellow("btnExit");
	}

	@Override
	public void addEnterIndex() {
		addEnterIndex(btnExit);
	}

	@Override
	public void formInit() {
		try {
			ZkUtil.setGridHeaderStyle(gridAccList);
			this.gridAccList.setRowRenderer(getRowRenderer1());
			clearData();
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	private void clearData() {
	}

	public void onOK() {
	}

	public void showData() {
		try {
			this.gridAccList.getRows().getChildren().clear();
			ManAcArClear.getDataQryByLinkNo(lst_gl_header, this.getLink_no(), this.getLoginBean());
			this.gridAccList.setModel(new SimpleListModel(lst_gl_header));
			this.gridAccList.renderAll();

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	private void doOnClickRow(Event event) {
		try {

			Row row = (Row) event.getTarget().getParent();

			String sVOU_TYPE = Fnc.getStr(row.getAttribute("VOU_TYPE"));
			String sVOU_NO = Fnc.getStr(row.getAttribute("VOU_NO"));

			if (!ZkUtil.isPopup("AcEntrView")) {
				AcEntrView fAcEntrView = (AcEntrView) ZkUtil.callZulFile("/com/pcc/gl/zul/AcEntrQry/AcEntrView.zul");
				fAcEntrView.setId("AcEntrView");// ต้องเรียกก่อนตัวอื่น
				fAcEntrView.setFormObj();// ต้องเรียกก่อนตัวอื่น
				fAcEntrView.vou_type = sVOU_TYPE;
				fAcEntrView.vou_no = sVOU_NO;
				fAcEntrView.setLoginBean(this.getLoginBean());
				fAcEntrView.showData();
				fAcEntrView.doModal();
			}

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	private RowRenderer getRowRenderer1() {

		return (row, dat, index) -> {

			FModelHasMap rs = (FModelHasMap) dat;
			int seq = index + 1;
			row.setSclass("rowGrid1");

			row.appendChild(ZkUtil.gridLabel(seq + ""));

			Button btn_view = new Button();
			btn_view.setLabel("ดูสมุดรายวัน");
			btn_view.setSclass("buttonView");
			btn_view.addEventListener(Events.ON_CLICK, (event) -> doOnClickRow(event));
			row.appendChild(btn_view);

			String postTypeName = "ขาตั้งหนี้";
			if (rs.getInt("POST_TYPE") != 1) {
				postTypeName = "ขาล้างหนี้";
			}
			row.appendChild(ZkUtil.gridLabel(postTypeName));

			row.appendChild(ZkUtil.gridLabel(rs.getString("VOU_TYPE") + rs.getString("VOU_NO")));
			row.appendChild(ZkUtil.gridLabel(FnDate.displayDateString(rs.getDate("POSTDATE"))));

			String custName = Fnc.getStr(rs.getString("TITLE")) + " " + Fnc.getStr(rs.getString("FNAME")) + " "
					+ Fnc.getStr(rs.getString("LNAME"));
			row.appendChild(ZkUtil.gridTextbox(custName));

			row.appendChild(ZkUtil.gridTextbox(rs.getString("DOCNO")));
			row.appendChild(ZkUtil.gridLabel(FnDate.displayDateString(rs.getDate("DOCDATE"))));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("DESCR")));

			String statName = FConstAc.get_gl_status_name(rs.getInt("RECSTA") + "");
			String sStyle = FConstAc.get_gl_status_style(rs.getInt("RECSTA") + "");
			row.appendChild(ZkUtil.gridLabel(statName, sStyle));

			row.appendChild(ZkUtil.gridLabel(rs.getString("INSBY")));
			row.appendChild(ZkUtil.gridLabel(FnDate.displayDateTimeString(rs.getTimestamp("INSDT"))));

			// === เพิ่ม Attribute
			row.setAttribute("VOU_TYPE", rs.getString("VOU_TYPE"));
			row.setAttribute("VOU_NO", rs.getString("VOU_NO"));
			row.setAttribute("VOU_SEQ", rs.getInt("VOU_SEQ"));
			row.setAttribute("VOU_DSEQ", rs.getInt("VOU_DSEQ"));
		};

	}

	public String getLink_no() {
		return link_no;
	}

	public void setLink_no(String link_no) {
		this.link_no = link_no;
	}

}

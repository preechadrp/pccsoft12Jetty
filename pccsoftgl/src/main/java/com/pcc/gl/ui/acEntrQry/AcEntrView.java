package com.pcc.gl.ui.acEntrQry;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;

import com.pcc.gl.lib.FConstAc;
import com.pcc.gl.progman.ManAcEntr;
import com.pcc.gl.progman.ManAcEntrView;
import com.pcc.gl.tbf.TbfACCT_VOU_TYPE;
import com.pcc.gl.tbf.TbfACGL_DETAIL;
import com.pcc.gl.tbf.TbfACGL_HEADER;
import com.pcc.gl.tbm.TbmACGL_DETAIL;
import com.pcc.gl.tbo.TboACCT_VOU_TYPE;
import com.pcc.gl.tbo.TboACGL_DETAIL;
import com.pcc.gl.tbo.TboACGL_HEADER;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FWinUtil;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.MyDatebox;
import com.pcc.sys.lib.MyDecimalbox;
import com.pcc.sys.lib.ZkUtil;

public class AcEntrView extends FWinUtil {

	private static final long serialVersionUID = 1L;
	public MyDatebox tdbPOSTDATE;
	public Textbox txtVOU_TYPE, txtVOU_NAME;
	public Textbox txtVOU_NO, txtDESCRH;
	public MyDecimalbox decSUM_DB, decSUM_CR, decSUM_DIFF;
	public Label lblStat;
	public Grid gridAccList;
	public Button btnExit;

	private java.util.List<FModelHasMap> lst_dat = new ArrayList<FModelHasMap>();

	public String vou_type = "";
	public String vou_no = "";

	@Override
	public void setFormObj() {

		if (!this.isDoSetFormObject()) {
			this.setDoSetFormObject(true);

			tdbPOSTDATE = (MyDatebox) getFellow("tdbPOSTDATE");
			txtVOU_TYPE = (Textbox) getFellow("txtVOU_TYPE");
			txtVOU_NAME = (Textbox) getFellow("txtVOU_NAME");
			txtVOU_NO = (Textbox) getFellow("txtVOU_NO");
			txtDESCRH = (Textbox) getFellow("txtDESCRH");
			decSUM_DB = (MyDecimalbox) getFellow("decSUM_DB");
			decSUM_CR = (MyDecimalbox) getFellow("decSUM_CR");
			decSUM_DIFF = (MyDecimalbox) getFellow("decSUM_DIFF");
			lblStat = (Label) getFellow("lblStat");
			gridAccList = (Grid) getFellow("gridAccList");
			btnExit = (Button) getFellow("btnExit");

		}

	}

	@Override
	public void addEnterIndex() {
	}

	@Override
	public void formInit() {

		ZkUtil.setGridHeaderStyle(this.gridAccList);
		this.gridAccList.setRowRenderer((Row row, Object dat, int index) -> {

			FModelHasMap rs = (FModelHasMap) dat;
			int seq = index + 1;
			row.setSclass("rowGrid1");

			row.appendChild(ZkUtil.gridLabel(seq + ""));

			Button btn_sub = new Button();
			btn_sub.setLabel("ตัวย่อย");
			btn_sub.setTooltiptext("ตัวย่อย");
			btn_sub.setSclass("buttonedit");
			btn_sub.addEventListener(Events.ON_CLICK, (event) -> onClick_BtnSubRow(event));
			row.appendChild(btn_sub);
			if (Fnc.getStr(rs.getString("SUB_APPR")).equals("1")) {// 0=ยังไม่ใส่ตัวย่อย,1=ใส่ตัวย่อยแล้ว,2=ไม่ใส่ตัวย่อย
				btn_sub.setVisible(true);
			} else {
				btn_sub.setVisible(false);
			}

			row.appendChild(ZkUtil.gridTextbox(rs.getString("ACCT_ID")));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("ACCT_NAME")));
			row.appendChild(ZkUtil.gridTextbox(rs.getString("SECT_ID")));
			if (rs.getBigDecimal("NUM_TYPE").intValue() == 1) {
				row.appendChild(ZkUtil.gridDecimalbox(rs.getBigDecimal("AMT")));
				row.appendChild(ZkUtil.gridTextbox(""));
			} else {
				row.appendChild(ZkUtil.gridTextbox(""));
				row.appendChild(ZkUtil.gridDecimalbox(rs.getBigDecimal("AMT")));
			}
			row.appendChild(ZkUtil
					.gridTextbox(
							Fnc.getStr(rs.getString("DESCR")) + " " + Fnc.getStr(rs.getString("DESCR_SUB"))));

			// === เพิ่ม Attribute
			row.setAttribute("REC_HASMAP", rs);
			row.setAttribute("VOU_SEQ_SHOW", rs.getInt("VOU_SEQ_SHOW"));

		});

	}

	public void onOK() {
	}

	public void showData() {

		try {

			TboACGL_HEADER acch = new TboACGL_HEADER();

			acch.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			acch.setVOU_TYPE(vou_type);
			acch.setVOU_NO(vou_no);

			if (TbfACGL_HEADER.find(acch)) {

				tdbPOSTDATE.setValue(acch.getPOSTDATE());
				txtVOU_TYPE.setValue(acch.getVOU_TYPE());
				txtVOU_NO.setValue(acch.getVOU_NO());
				txtDESCRH.setValue(acch.getDESCR());

				String statName = FConstAc.get_gl_status_name(acch.getRECSTA() + "");
				String sStyle = FConstAc.get_gl_status_style(acch.getRECSTA() + "");
				lblStat.setValue(statName);
				lblStat.setStyle(sStyle);

				TboACCT_VOU_TYPE vot = new TboACCT_VOU_TYPE();
				vot.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				vot.setVOU_TYPE(acch.getVOU_TYPE());
				if (TbfACCT_VOU_TYPE.find(vot)) {
					txtVOU_NAME.setValue(vot.getVOU_NAME());
				}

				showDetail();

			}

		} catch (Exception e) {
			Msg.error(e);
		}

	}

	private void showDetail() throws Exception {
		TbmACGL_DETAIL.getDataForAcEntr(lst_dat, txtVOU_TYPE.getValue(), txtVOU_NO.getValue(), this.getLoginBean());
		SimpleListModel rstModel = new SimpleListModel(lst_dat);
		this.gridAccList.setModel(rstModel);

		BigDecimal sum_debit = BigDecimal.ZERO;
		BigDecimal sum_credit = BigDecimal.ZERO;
		for (FModelHasMap mdl : lst_dat) {
			if (mdl.getBigDecimal("NUM_TYPE").intValue() == 1) {
				sum_debit = sum_debit.add(mdl.getBigDecimal("AMT"));
			} else {
				sum_credit = sum_credit.add(mdl.getBigDecimal("AMT"));
			}
		}

		decSUM_DB.setValue(sum_debit);
		decSUM_CR.setValue(sum_credit);
		decSUM_DIFF.setValue(sum_debit.subtract(sum_credit));

	}

	public void onClick_btnPrint() {
		try {
			ManAcEntr.printVoucher(this.getLoginBean(), txtVOU_TYPE.getValue(), txtVOU_NO.getValue());
		} catch (Exception e) {
			Msg.error(e);
		}
	}

	/**
	 * ตัวย่อย
	 * 
	 * @param event
	 */
	public void onClick_BtnSubRow(org.zkoss.zk.ui.event.Event event) {

		try {

			Row row = (Row) event.getTarget().getParent();
			FModelHasMap rs = (FModelHasMap) row.getAttribute("REC_HASMAP");

			TboACGL_DETAIL detail = new TboACGL_DETAIL();

			detail.setCOMP_CDE(rs.getString("COMP_CDE"));
			detail.setVOU_TYPE(rs.getString("VOU_TYPE"));
			detail.setVOU_NO(rs.getString("VOU_NO"));
			detail.setVOU_SEQ(rs.getInt("VOU_SEQ"));

			if (TbfACGL_DETAIL.find(detail)) {
				ManAcEntrView.viewSUB_GL(detail, this.getLoginBean());
			}

			showDetail();

		} catch (Exception e) {
			Msg.error(e);
		}

	}

}

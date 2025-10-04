package com.pcc.tk.ui.FrmTkJob;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;

import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FWinMenu;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.MyDatebox;
import com.pcc.sys.lib.ZkUtil;
import com.pcc.tk.find.FfTKCOURT;
import com.pcc.tk.find.FfTKJOBCODE;
import com.pcc.tk.find.FfTKLAWSTAT;
import com.pcc.tk.find.FfTKLAWTYPE;
import com.pcc.tk.find.FfTKZONE;
import com.pcc.tk.progman.ManTkJobClose;
import com.pcc.tk.tbf.TbfTKCOURT;
import com.pcc.tk.tbf.TbfTKJOB;
import com.pcc.tk.tbf.TbfTKJOBCODE;
import com.pcc.tk.tbf.TbfTKLAWSTAT;
import com.pcc.tk.tbf.TbfTKLAWTYPE;
import com.pcc.tk.tbf.TbfTKZONE;
import com.pcc.tk.tbo.TboTKCOURT;
import com.pcc.tk.tbo.TboTKJOB;
import com.pcc.tk.tbo.TboTKJOBCODE;
import com.pcc.tk.tbo.TboTKLAWSTAT;
import com.pcc.tk.tbo.TboTKLAWTYPE;
import com.pcc.tk.tbo.TboTKZONE;
import com.pcc.tk.ui.FrmTkJobLog.FrmTkJobLogEntr;

public class FrmTkJob extends FWinMenu {

	private static final long serialVersionUID = 1L;

	public Button btnExit;
	public MyDatebox tdbJOBDATE_FROM;
	public MyDatebox tdbJOBDATE_TO;
	public Textbox txtJOBNO;
	public Textbox txtLAWSTATID;
	public Textbox txtLAWSTATNAME;
	public Textbox txtJOBCODE;
	public Textbox txtJOBCODENAME;
	public Textbox txtLAWTYPEID;
	public Textbox txtLAWTYPENAME;
	public Textbox txtLAWBLACKNO;
	public Textbox txtLAWREDNO;
	public Textbox txtCLIENTFNAME;
	public Textbox txtCLIENTLNAME;
	public Textbox txtLAWYERNAME;
	public Textbox txtCOURTID;
	public Textbox txtCOURTNAME;
	public Textbox txtZONEID;
	public Textbox txtZONENAME;
	public Button btnShow;
	public Button btnAdd;
	public Grid grdViewt;
	public Column colDel;

	private java.util.List<FModelHasMap> lst_data = new java.util.ArrayList<FModelHasMap>();
	private String sel_jobno = "";

	@Override
	public void setFormObj() {
		btnExit = (Button) getFellow("btnExit");
		tdbJOBDATE_FROM = (MyDatebox) getFellow("tdbJOBDATE_FROM");
		tdbJOBDATE_TO = (MyDatebox) getFellow("tdbJOBDATE_TO");
		txtJOBNO = (Textbox) getFellow("txtJOBNO");
		txtLAWSTATID = (Textbox) getFellow("txtLAWSTATID");
		txtLAWSTATNAME = (Textbox) getFellow("txtLAWSTATNAME");
		txtJOBCODE = (Textbox) getFellow("txtJOBCODE");
		txtJOBCODENAME = (Textbox) getFellow("txtJOBCODENAME");
		txtLAWTYPEID = (Textbox) getFellow("txtLAWTYPEID");
		txtLAWTYPENAME = (Textbox) getFellow("txtLAWTYPENAME");
		txtLAWBLACKNO = (Textbox) getFellow("txtLAWBLACKNO");
		txtLAWREDNO = (Textbox) getFellow("txtLAWREDNO");
		txtCLIENTFNAME = (Textbox) getFellow("txtCLIENTFNAME");
		txtCLIENTLNAME = (Textbox) getFellow("txtCLIENTLNAME");
		txtLAWYERNAME = (Textbox) getFellow("txtLAWYERNAME");
		txtCOURTID = (Textbox) getFellow("txtCOURTID");
		txtCOURTNAME = (Textbox) getFellow("txtCOURTNAME");
		txtZONEID = (Textbox) getFellow("txtZONEID");
		txtZONENAME = (Textbox) getFellow("txtZONENAME");
		btnShow = (Button) getFellow("btnShow");
		btnAdd = (Button) getFellow("btnAdd");
		grdViewt = (Grid) getFellow("grdViewt");
		colDel = (Column) getFellow("colDel");

	}

	@Override
	public void addEnterIndex() {

		addEnterIndex(btnExit);
		addEnterIndex(tdbJOBDATE_FROM);
		addEnterIndex(tdbJOBDATE_TO);
		addEnterIndex(txtJOBNO);
		addEnterIndex(txtLAWSTATID);
		// addEnterIndex(txtLAWSTATNAME);
		addEnterIndex(txtJOBCODE);
		// addEnterIndex(txtJOBCODENAME);
		addEnterIndex(txtLAWTYPEID);
		// addEnterIndex(txtLAWTYPENAME);
		addEnterIndex(txtLAWBLACKNO);
		addEnterIndex(txtLAWREDNO);
		addEnterIndex(txtCLIENTFNAME);
		addEnterIndex(txtCLIENTLNAME);
		addEnterIndex(txtLAWYERNAME);
		addEnterIndex(txtCOURTID);
		addEnterIndex(txtZONEID);
		addEnterIndex(btnShow);

	}

	@Override
	public void formInit() {
		try {
			ZkUtil.setGridHeaderStyle(grdViewt);
			grdViewt.setRowRenderer(getRowRendererView1());
			clearData();
			if (this.getMenuId2().equals("FrmTkJob")) {
				btnAdd.setVisible(true);
			} else {
				btnAdd.setVisible(false);
			}
			
			if (this.getMenuId2().equals("FrmTkJobInq")) {
				colDel.setVisible(false);	
			}
			
			showGridViewt();
		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}

	}

	public void clearData() {
		grdViewt.getRows().getChildren().clear();
	}

	public void onOK() {
		try {
			super.onOK();
		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}

	}

	public void onClick_btnShow() {
		try {
			showGridViewt();
		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}

	}

	public void popupLAWSTATID() {
		if (!txtLAWSTATID.isReadonly()) {
			FfTKLAWSTAT.popup(this.getLoginBean(), this, "read_LAWSTATID");
		}
	}

	public boolean read_LAWSTATID(int lawstatid) throws Exception {

		TboTKLAWSTAT tb1 = new TboTKLAWSTAT();

		tb1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		tb1.setLAWSTATID(lawstatid);

		if (TbfTKLAWSTAT.find(tb1)) {
			txtLAWSTATID.setValue(tb1.getLAWSTATID() + "");
			txtLAWSTATNAME.setValue(tb1.getLAWSTATNAME());
			return true;
		} else {
			txtLAWSTATNAME.setValue("");
			return false;
		}

	}

	public void popupJOBCODE() {
		if (!txtJOBCODE.isReadonly()) {
			FfTKJOBCODE.popup(this.getLoginBean(), this, "read_JOBCODE");
		}
	}

	public boolean read_JOBCODE(int jobcode) throws Exception {

		TboTKJOBCODE tb1 = new TboTKJOBCODE();

		tb1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		tb1.setJOBCODE(jobcode);

		if (TbfTKJOBCODE.find(tb1)) {
			txtJOBCODE.setValue(tb1.getJOBCODE() + "");
			txtJOBCODENAME.setValue(tb1.getJOBNAME());
			return true;
		} else {
			txtJOBCODENAME.setValue("");
			return false;
		}

	}

	public void popupLAWTYPEID() {
		if (!txtLAWTYPEID.isReadonly()) {
			FfTKLAWTYPE.popup(this.getLoginBean(), this, "read_LAWTYPE");
		}
	}

	public boolean read_LAWTYPE(int lawtypeid) throws Exception {

		TboTKLAWTYPE tklawtype = new TboTKLAWTYPE();

		tklawtype.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		tklawtype.setLAWTYPEID(lawtypeid);

		if (TbfTKLAWTYPE.find(tklawtype)) {
			txtLAWTYPEID.setValue(tklawtype.getLAWTYPEID() + "");
			txtLAWTYPENAME.setValue(tklawtype.getLAWTYPENAME());
			return true;
		} else {
			txtLAWTYPENAME.setValue("");
			return false;
		}

	}

	public void popupCOURTID() {
		if (!txtCOURTID.isReadonly()) {
			FfTKCOURT.popup(this.getLoginBean(), this, "read_COURTID");
		}
	}

	public boolean read_COURTID(int courtid) throws Exception {

		TboTKCOURT tb1 = new TboTKCOURT();

		tb1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		tb1.setCOURTID(courtid);

		if (TbfTKCOURT.find(tb1)) {
			txtCOURTID.setValue(tb1.getCOURTID() + "");
			txtCOURTNAME.setValue(tb1.getCOURTNAME());
			return true;
		} else {
			txtCOURTNAME.setValue("");
			return false;
		}

	}

	public void popupZONEID() {
		if (!txtCOURTID.isReadonly()) {
			FfTKZONE.popup(this.getLoginBean(), this, "read_ZONEID");
		}
		
	}

	public boolean read_ZONEID(int zoneid) throws Exception {
		
		TboTKZONE tb1 = new TboTKZONE();

		tb1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		tb1.setZONEID(zoneid);
		
		if (TbfTKZONE.find(tb1)) {
			txtZONEID.setValue(tb1.getZONEID()+"");
			txtZONENAME.setValue(tb1.getZONENAME());
			return true;
		} else {
			txtZONENAME.setValue("");
			return false;
		}
		
	}
	
	public void onChange_Data(HtmlBasedComponent comp) {
		try {
			if (comp != null) {

				if (!Fnc.isEmpty(comp.getId())) {
					ZkUtil.setAndClearOnChang(comp, this.objectList);
				}

				if (comp.getId().equals("txtLAWSTATID")) {
					read_LAWSTATID(Fnc.getIntFromStr(txtLAWSTATID.getValue()));
				}
				if (comp.getId().equals("txtJOBCODE")) {
					read_JOBCODE(Fnc.getIntFromStr(txtJOBCODE.getValue()));
				}
				if (comp.getId().equals("txtCOURTID")) {
					read_COURTID(Fnc.getIntFromStr(txtCOURTID.getValue()));
				}
				if (comp.getId().equals("txtLAWTYPEID")) {
					read_LAWTYPE(Fnc.getIntFromStr(txtLAWTYPEID.getValue()));
				}
				if (comp.getId().equals("txtZONEID")) {
					read_ZONEID(Fnc.getIntFromStr(txtZONEID.getValue()));
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showGridViewt() throws WrongValueException, Exception {
		
		FModelHasMap para = new FModelHasMap();
		
		para.put("JOBDATE_FROM", tdbJOBDATE_FROM.getSqlDate());
		para.put("JOBDATE_TO", tdbJOBDATE_TO.getSqlDate());
		para.put("JOBNO", txtJOBNO.getValue());
		para.put("LAWSTATID", Fnc.getIntFromStr(txtLAWSTATID.getValue()));
		para.put("JOBCODE", Fnc.getIntFromStr(txtJOBCODE.getValue()));
		para.put("LAWTYPEID", Fnc.getIntFromStr(txtLAWTYPEID.getValue()));
		para.put("LAWBLACKNO", txtLAWBLACKNO.getValue());
		para.put("LAWREDNO", txtLAWREDNO.getValue());
		para.put("CLIENTFNAME", txtCLIENTFNAME.getValue());
		para.put("CLIENTLNAME", txtCLIENTLNAME.getValue());
		para.put("LAWYERNAME", txtLAWYERNAME.getValue());
		para.put("COURTID", Fnc.getIntFromStr(txtCOURTID.getValue()));
		para.put("ZONEID", Fnc.getIntFromStr(txtZONEID.getValue()));
		para.put("MENUID2", this.getMenuId2());
		
		ManTkJobClose.getDataByJobno(this.getLoginBean().getCOMP_CDE(), para, lst_data);
		SimpleListModel rstModel = new SimpleListModel(lst_data);
		this.grdViewt.setModel(rstModel);
		
	}

	private RowRenderer<?> getRowRendererView1() {
		return (row, data, index) -> {

			FModelHasMap rs = (FModelHasMap) data;
			int seq = index + 1;
			row.setStyle(ZkUtil.styleFindLookUp);

			row.setAttribute("DATA1", rs);

			// == append child ==//
			{
				Button btn1 = new Button();
				btn1.setLabel("ดูรายละเอียด");
				btn1.setAutodisable("self");
				btn1.addEventListener(Events.ON_CLICK, event -> onClick_ViewRow(event));
				row.appendChild(btn1);
				row.setAttribute("btn_view", btn1);
			}
			
			if (this.getMenuId2().equals("FrmTkJob")) {
				Button btn1 = new Button();
				btn1.setLabel("แก้ไข");
				btn1.setAutodisable("self");
				btn1.addEventListener(Events.ON_CLICK, event -> onClick_EditJobRow(event));
				row.appendChild(btn1);
				row.setAttribute("btn_edit", btn1);
				
			} else if (this.getMenuId2().equals("FrmTkJobLog")) {
				Button btn1 = new Button();
				btn1.setLabel("บันทึกสถานะ");
				btn1.setAutodisable("self");
				btn1.addEventListener(Events.ON_CLICK, event -> onClick_LogJobRow(event));
				row.appendChild(btn1);
				row.setAttribute("btn_edit", btn1);
				
			} else if (this.getMenuId2().equals("FrmTkJobDoc")) {
				Button btn1 = new Button();
				btn1.setLabel("เก็บเอกสาร");
				btn1.setAutodisable("self");
				btn1.addEventListener(Events.ON_CLICK, event -> onClick_DocJobRow(event));
				row.appendChild(btn1);
				row.setAttribute("btn_doc", btn1);
				
			} else if (this.getMenuId2().equals("FrmTkJobClose")) {
				Button btn1 = new Button();
				if (rs.getDate("CLOSEDATE")  == null) {
					btn1.setLabel("ปิดงาน");
				} else {
					btn1.setLabel("ยกเลิกปิดงาน");
				}
				btn1.setAutodisable("self");
				btn1.addEventListener(Events.ON_CLICK, event -> onClick_CloseJobRow(event));
				row.appendChild(btn1);
				row.setAttribute("btn_closejob", btn1);
				
			} else if (this.getMenuId2().equals("FrmTkJobCancel")) {
				Button btn1 = new Button();
				btn1.setLabel("ยกเลิก");
				btn1.setSclass("buttondel");
				btn1.setAutodisable("self");
				btn1.addEventListener(Events.ON_CLICK, event -> onClick_DelJobRow(event));
				row.appendChild(btn1);
				row.setAttribute("btn_del", btn1);
				
			} else {
				row.appendChild(ZkUtil.gridLabel("-"));
			}

			if (Fnc.getStr(rs.getString("JOBSTAT")).equals("9")) {
				row.appendChild(ZkUtil.gridLabel(rs.getString("JOBSTATNAME"),"color:red;"));
			} else if (Fnc.getStr(rs.getString("JOBSTAT")).equals("8")) {
				row.appendChild(ZkUtil.gridLabel(rs.getString("JOBSTATNAME"),"color:#ff6600;"));
			} else {
				row.appendChild(ZkUtil.gridLabel(rs.getString("JOBSTATNAME")));
			}
			row.appendChild(ZkUtil.gridIntbox(seq));
			row.appendChild(ZkUtil.gridLabel(rs.getString("JOBNO")));
			row.appendChild(ZkUtil.gridLabel(FnDate.displayDateString(rs.getDate("JOBDATE"))));
			row.appendChild(ZkUtil.gridLabel(rs.getString("LAWSTATNAME")));
			row.appendChild(ZkUtil.gridLabel(rs.getString("CUSTCONTACTNO")));
			row.appendChild(ZkUtil.gridDecimalbox(rs.getBigDecimal("SUECOSTAMT")));
			row.appendChild(ZkUtil.gridDecimalbox(rs.getBigDecimal("FEEAMT")));
			row.appendChild(ZkUtil.gridLabel(rs.getString("CLIENTNAMES")));
			row.appendChild(ZkUtil.gridLabel(rs.getString("LAWYERNAMES")));
			row.appendChild(ZkUtil.gridLabel(FnDate.displayDateString(rs.getDate("CLOSEDATE"))));
			row.appendChild(ZkUtil.gridLabel(rs.getString("UPDBY")));
			row.appendChild(ZkUtil.gridLabel(FnDate.displayDateTimeString(rs.getTimestamp("UPDDTE"))));

		};
	}

	private void onClick_ViewRow(Event event) {
		try {
			Component comp = event.getTarget().getParent();
			var rs = (FModelHasMap) comp.getAttribute("DATA1");
			var jobno = rs.getString("JOBNO");
			FrmTkJobView.showData(this.getLoginBean(), jobno, this, this.getMenuId2(), null);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}
	}

	private void onClick_EditJobRow(Event event) {

		try {
			
			Component comp = event.getTarget().getParent();
			FModelHasMap sel_rs = (FModelHasMap) comp.getAttribute("DATA1");
			sel_jobno = sel_rs.getString("JOBNO");
			
			{//check
				var jobtb = new TboTKJOB();
				
				jobtb.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				jobtb.setJOBNO(sel_jobno);
				
				if (TbfTKJOB.find(jobtb)) {
					if (jobtb.getJOBSTAT().equals("9")) {
						throw new Exception("รายการมีการยกเลิกแล้ว");
					}
					if (jobtb.getCLOSEDATE() != null) {
						throw new Exception("สถานะปิดงานแล้ว");
					}
				}
			}
			
			Msg.confirm("ยืนยันแก้ไข job ?", "?", (event1) -> {
				if (Messagebox.Button.YES.equals(event1.getButton())) {
					try {
						FrmTkJobEntr.showData(this.getLoginBean(), sel_jobno, false, this, this.getMenuId2() ,null);
					} catch (Exception ex) {
						ex.printStackTrace();
						Msg.error(ex);
					}
				}
			});
			
		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}
	}
	
	public void onClick_DocJobRow(Event event) {

		try {
			
			Component comp = event.getTarget().getParent();
			FModelHasMap sel_rs = (FModelHasMap) comp.getAttribute("DATA1");
			sel_jobno = sel_rs.getString("JOBNO");
			FrmTkJobDoc.showData(this.getLoginBean(), sel_jobno, this, this.getMenuId2() ,null);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}
		
	}
	
	public void doCloseJob(java.sql.Date close_date) {
		try {

			TboTKJOB jobtb2 = new TboTKJOB();

			jobtb2.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
			jobtb2.setJOBNO(sel_jobno);
			
			if (TbfTKJOB.find(jobtb2)) {
				jobtb2.setCLOSEDATE(close_date);
				jobtb2.setUPDBY(this.getLoginBean().getUSER_ID());
				jobtb2.setUPDDTE(FnDate.getTodaySqlDateTime());
				TbfTKJOB.update(jobtb2);
			}
			
			showGridViewt();

		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}
	}
	
	public void onClick_btnAdd() {
		try {
			FrmTkJobEntr.showData(this.getLoginBean(), "", true, this, this.getMenuId2() ,null);
		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}
	}

	public void onClick_LogJobRow(Event event) {
		try {
			
			Component comp = event.getTarget().getParent();
			FModelHasMap sel_rs = (FModelHasMap) comp.getAttribute("DATA1");
			sel_jobno = sel_rs.getString("JOBNO");
			
			{//check
				var jobtb = new TboTKJOB();
				
				jobtb.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				jobtb.setJOBNO(sel_jobno);
				
				if (TbfTKJOB.find(jobtb)) {
					if (jobtb.getJOBSTAT().equals("9")) {
						throw new Exception("รายการมีการยกเลิกแล้ว");
					}
					if (jobtb.getCLOSEDATE() != null) {
						throw new Exception("สถานะปิดงานแล้ว");
					}
				}
			}
			FrmTkJobLogEntr.showData(this.getLoginBean(), sel_jobno, this, this.getMenuId2(), null);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}
	}
	
	private void onClick_CloseJobRow(Event event) {

		try {
			
			Component comp = event.getTarget().getParent();
			FModelHasMap sel_rs = (FModelHasMap) comp.getAttribute("DATA1");
			sel_jobno = sel_rs.getString("JOBNO");
			java.sql.Date closedate = null;
			
			{// get closedate
				var jobtb = new TboTKJOB();
				jobtb.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				jobtb.setJOBNO(sel_jobno);
				if (TbfTKJOB.find(jobtb)) {
					closedate = jobtb.getCLOSEDATE();				
					if (jobtb.getJOBSTAT().equals("9")) {
						throw new Exception("รายการมีการยกเลิกแล้ว");
					}
				}
			}
			
			if (closedate != null) {
				
				Msg.confirm("ยกเลิกการปิด job ?", "?", (event1) -> {
					if (Messagebox.Button.YES.equals(event1.getButton())) {

						try {
							
							TboTKJOB jobtb2 = new TboTKJOB();

							jobtb2.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
							jobtb2.setJOBNO(sel_jobno);
							
							if (TbfTKJOB.find(jobtb2)) {
								jobtb2.setCLOSEDATE(null);
								jobtb2.setUPDBY(this.getLoginBean().getUSER_ID());
								jobtb2.setUPDDTE(FnDate.getTodaySqlDateTime());
								TbfTKJOB.update(jobtb2);
							}
							
							showGridViewt();

						} catch (Exception ex) {
							ex.printStackTrace();
							Msg.error(ex);
						}

					}
				});
				
			} else {
				Msg.inputDatebox2("ใส่วันที่ปิด job ที่ต้องการ", 0, 0, this, "doCloseJob");
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}
		
	}
	
	private void onClick_DelJobRow(Event event) {

		try {

			Component comp = event.getTarget().getParent();
			FModelHasMap sel_rs = (FModelHasMap) comp.getAttribute("DATA1");
			sel_jobno = sel_rs.getString("JOBNO");

			{
				var jobtb = new TboTKJOB();
				jobtb.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
				jobtb.setJOBNO(sel_jobno);
				if (TbfTKJOB.find(jobtb)) {
					if (jobtb.getJOBSTAT().equals("9")) {
						throw new Exception("รายการมีการยกเลิกแล้ว");
					}
				}
			}

			Msg.confirm("ยืนยันการยกเลิก job ?", "?", (event1) -> {
				if (Messagebox.Button.YES.equals(event1.getButton())) {

					try {

						TboTKJOB jobtb2 = new TboTKJOB();

						jobtb2.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
						jobtb2.setJOBNO(sel_jobno);

						if (TbfTKJOB.find(jobtb2)) {
							jobtb2.setJOBSTAT("9");
							jobtb2.setUPDBY(this.getLoginBean().getUSER_ID());
							jobtb2.setUPDDTE(FnDate.getTodaySqlDateTime());
							TbfTKJOB.update(jobtb2);
						}

						showGridViewt();

					} catch (Exception ex) {
						ex.printStackTrace();
						Msg.error(ex);
					}

				}
			});

		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}

	}

}

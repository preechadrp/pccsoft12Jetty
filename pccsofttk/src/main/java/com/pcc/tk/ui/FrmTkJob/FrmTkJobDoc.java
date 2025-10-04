package com.pcc.tk.ui.FrmTkJob;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FDbc;
import com.pcc.sys.lib.FWindow;
import com.pcc.sys.lib.FnDate;
import com.pcc.sys.lib.Fnc;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.MyDatebox;
import com.pcc.sys.lib.ZkUtil;
import com.pcc.tk.progman.ManTkJob;
import com.pcc.tk.tbf.TbfTKIMGS;
import com.pcc.tk.tbf.TbfTKJOB;
import com.pcc.tk.tbf.TbfTKJOBDOCS;
import com.pcc.tk.tbf.TbfTKLAWSTAT;
import com.pcc.tk.tbm.TbmTKIMGS;
import com.pcc.tk.tbm.TbmTKJOBDOCDESC;
import com.pcc.tk.tbm.TbmTKJOBDOCS;
import com.pcc.tk.tbo.TboTKIMGS;
import com.pcc.tk.tbo.TboTKJOB;
import com.pcc.tk.tbo.TboTKJOBDOCS;
import com.pcc.tk.tbo.TboTKLAWSTAT;

public class FrmTkJobDoc extends FWindow {

	private static final long serialVersionUID = 1L;
	
	public static void showData(LoginBean loginBean, String jobno ,Window winCall, String menuid2 , EventListener<Event> event1) throws Exception {
		
		winCall.setVisible(false);
		String newID = "FrmTkJobDoc" + menuid2; //ป้องกันซ้ำหลายเมนู
		System.out.println("newID :" + newID);
		FrmTkJobDoc frm1 = (FrmTkJobDoc) ZkUtil.callZulFile("/com/pcc/tk/zul/FrmTkJob/FrmTkJobDoc.zul");
		
		//==parameter
		frm1.setLoginBean(loginBean);
		frm1.p_JOBNO = jobno;
		frm1.p_win_call = winCall;
		frm1.p_Event = event1;
		
		//==ค่าอื่นๆ
		frm1.setAppendMode(newID, winCall.getWidth(), winCall.getParent());
		
	}

	public Button btnExit;
	public Textbox txtJOBNO;
	public MyDatebox tdbJOBDATE;
	public Textbox txtLAWSTATID;
	public Textbox txtLAWSTATNAME;
	public Combobox cmbDOCDESC;
	public Button btnAdd;
	public Grid grdData1;

	private java.util.List<TboTKJOBDOCS> lst_imgs = new java.util.ArrayList<TboTKJOBDOCS>();

	public String p_JOBNO;
	public Window p_win_call = null;
	public EventListener<Event> p_Event;

	@Override
	public void setFormObj() {

		btnExit = (Button) getFellow("btnExit");
		txtJOBNO = (Textbox) getFellow("txtJOBNO");
		tdbJOBDATE = (MyDatebox) getFellow("tdbJOBDATE");
		txtLAWSTATID = (Textbox) getFellow("txtLAWSTATID");
		txtLAWSTATNAME = (Textbox) getFellow("txtLAWSTATNAME");
		cmbDOCDESC = (Combobox) getFellow("cmbDOCDESC");
		btnAdd = (Button) getFellow("btnAdd");
		grdData1 = (Grid) getFellow("grdData1");

	}

	@Override
	public void addEnterIndex() {
		// addEnterIndex(btnExit);
		addEnterIndex(cmbDOCDESC);
		addEnterIndex(btnAdd);
	}

	@Override
	public void formInit() {
		try {
			ZkUtil.setGridHeaderStyle(grdData1);
			grdData1.setRowRenderer(getRowRendererImgs());

			// load
			loadDescName();

			clearData();
			if (!Fnc.isEmpty(this.p_JOBNO)) {
				read_record(this.p_JOBNO);
				showGridImgs();
			}

		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}

	}

	public void clearData() {

		// ==set visible, readonly ,disable ,focus
		// btnExit.setVisible(true);

		// == set value
		cmbDOCDESC.setValue("");
		lst_imgs.clear();
		// grdData1.getRows().getChildren().clear();

	}

	public void onOK() {
		try {
			super.onOK();
		} catch (Exception e) {
			Msg.error(e);
		}

	}
	
	public void onClose() {
		try {
			super.onClose();
			if (p_win_call != null) {
				p_win_call.setVisible(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void read_record(String jobno) throws Exception {

		TboTKJOB table1 = new TboTKJOB();

		table1.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
		table1.setJOBNO(jobno);

		if (TbfTKJOB.find(table1)) {
			tdbJOBDATE.setValue(table1.getJOBDATE());
			txtLAWSTATID.setValue(table1.getLAWSTATID() + "");
			read_LAWSTATID(table1.getLAWSTATID());
			txtJOBNO.setValue(table1.getJOBNO());
		}
	}
	
	private void loadDescName() throws Exception {
		cmbDOCDESC.getItems().clear();
		List<String> lst_data = new ArrayList<String>();
		ManTkJob.getTkjobdocdesc(this.getLoginBean().getCOMP_CDE(), lst_data);
		for (String ss : lst_data) {
			ZkUtil.appendItemToComboBox(cmbDOCDESC, ss, ss);
		}
	}

	public void onClick_btnAdd() {
		try {

			if (Fnc.isEmpty(cmbDOCDESC.getValue())) {
				throw new Exception("ต้องระบุ " + cmbDOCDESC.getTooltiptext());
			}

			ZkUtil.upLoadFile(1, 30, (e) -> {

				if (e.getMedia() != null) {

					System.out.println("size :" + e.getMedia().getByteData().length);
					String fname1 = e.getMedia().getName();
					String[] fname2 = fname1.split("\\.");
					String docformat = fname2[fname2.length - 1]; // เอานามสกุลไฟล์
					System.out.println("docformat : " + docformat);

					// insert TboTKIMGS
					TboTKIMGS tkimgs = new TboTKIMGS();

					tkimgs.setCOMP_CDE(this.getLoginBean().getCOMP_CDE());
					tkimgs.setSYS_CDE("FrmTkJobDoc");

					int new_IMGSEQ = TbmTKIMGS.getMax_IMGSEQ(this.getLoginBean().getCOMP_CDE(), "FrmTkJobDoc") + 1;
					tkimgs.setIMGSEQ(new_IMGSEQ);

					tkimgs.setIMGDESC(cmbDOCDESC.getValue().trim());
					tkimgs.setIMGTYPE(docformat);
					tkimgs.setIMGDATA(e.getMedia().getByteData());

					if (TbfTKIMGS.insert(tkimgs)) {

						// ===== insert
						TboTKJOBDOCS tkjobdoc = new TboTKJOBDOCS();

						tkjobdoc.setCOMP_CDE(tkimgs.getCOMP_CDE());
						tkjobdoc.setJOBNO(p_JOBNO);

						int new_DOCSEQ = TbmTKJOBDOCS.getMax_DOCSEQ(tkimgs.getCOMP_CDE(), p_JOBNO) + 1;
						tkjobdoc.setDOCSEQ(new_DOCSEQ);

						tkjobdoc.setDOCDESC(cmbDOCDESC.getValue().trim());
						tkjobdoc.setIMGSEQ(new_IMGSEQ);
						tkjobdoc.setIMGTYPE(docformat);
						tkjobdoc.setUPDBY(this.getLoginBean().getUSER_ID());
						tkjobdoc.setUPDDTE(FnDate.getTodaySqlDateTime());

						TbfTKJOBDOCS.insert(tkjobdoc);

						boolean inserD = TbmTKJOBDOCDESC.Insert_DOCDESC(tkimgs.getCOMP_CDE(), "FrmTkJobDoc", cmbDOCDESC.getValue().trim());
						if (inserD) {
							loadDescName();
						}

					}

					showGridImgs();

				}

			});

		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e);
		}

	}

	private void showGridImgs() throws WrongValueException, Exception {
		TbmTKJOBDOCS.getDataByJobno(this.getLoginBean().getCOMP_CDE(), p_JOBNO, lst_imgs);
		SimpleListModel rstModel = new SimpleListModel(lst_imgs);
		this.grdData1.setModel(rstModel);
	}

	private RowRenderer<?> getRowRendererImgs() {
		return (row, data, index) -> {

			TboTKJOBDOCS rs = (TboTKJOBDOCS) data;
			int seq = index + 1;
			row.setStyle(ZkUtil.styleFindLookUp);

			row.setAttribute("DATA1", rs);

			// == append child ==//
			{
				Button btn1 = new Button();
				btn1.setLabel("Download");
				btn1.setAutodisable("self");
				btn1.addEventListener(Events.ON_CLICK, event -> onClick_DownloadRow(event));
				row.appendChild(btn1);
				row.setAttribute("btn_load", btn1);
			}
			row.appendChild(ZkUtil.gridIntbox(seq));
			row.appendChild(ZkUtil.gridTextbox(rs.getDOCDESC()));
			row.appendChild(ZkUtil.gridTextbox(rs.getIMGTYPE()));
			{
				Button btn1 = new Button();
				btn1.setLabel("ลบ");
				btn1.setSclass("buttondel");
				btn1.setAutodisable("self");
				btn1.addEventListener(Events.ON_CLICK, event -> onClick_DelRow(event));
				row.appendChild(btn1);
				row.setAttribute("btn_del", btn1);
			}
			row.appendChild(ZkUtil.gridTextbox(rs.getUPDBY()));
			row.appendChild(ZkUtil.gridTextbox(FnDate.displayDateTimeString(rs.getUPDDTE())));

		};
	}

	private void onClick_DownloadRow(Event event) {

		try {

			Component comp = event.getTarget().getParent();
			var rs = (TboTKJOBDOCS) comp.getAttribute("DATA1");

			TboTKIMGS tkimgs = new TboTKIMGS();

			tkimgs.setCOMP_CDE(rs.getCOMP_CDE());
			tkimgs.setSYS_CDE("FrmTkJobDoc");
			tkimgs.setIMGSEQ(rs.getIMGSEQ());

			if (TbfTKIMGS.find(tkimgs)) {
				ZkUtil.saveFile(tkimgs.getIMGDATA(), tkimgs.getIMGTYPE(), "FrmTkJobDoc");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			Msg.error(ex);
		}

	}

	private void onClick_DelRow(Event event) {

		Component comp = event.getTarget().getParent();
		var rs = (TboTKJOBDOCS) comp.getAttribute("DATA1");

		Msg.confirm(Labels.getLabel("comm.label.deleteComfirm") + " ?", "?", (event1) -> {
			if (Messagebox.Button.YES.equals(event1.getButton())) {

				try (FDbc dbc = FDbc.connectMasterDb()) {

					// === ลบภาพก่อน
					TboTKIMGS tkimgs = new TboTKIMGS();

					tkimgs.setCOMP_CDE(rs.getCOMP_CDE());
					tkimgs.setSYS_CDE("FrmTkJobDoc");
					tkimgs.setIMGSEQ(rs.getIMGSEQ());

					TbfTKIMGS.delete(dbc, tkimgs);

					// === ลบ
					TbfTKJOBDOCS.delete(dbc, rs);

					showGridImgs();

				} catch (Exception ex) {
					ex.printStackTrace();
					Msg.error(ex);
				}

			}
		});

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

}

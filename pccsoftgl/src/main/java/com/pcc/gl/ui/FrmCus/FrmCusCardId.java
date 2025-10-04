package com.pcc.gl.ui.FrmCus;

import java.sql.SQLException;

import org.zkoss.image.AImage;
import org.zkoss.json.JSONObject;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zul.Grid;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;

import com.pcc.gl.tbm.TbmFCUS;
import com.pcc.sys.beans.LoginBean;
import com.pcc.sys.lib.FConfig;
import com.pcc.sys.lib.FModelHasMap;
import com.pcc.sys.lib.FWindow;
import com.pcc.sys.lib.Msg;
import com.pcc.sys.lib.ZkUtil;

public class FrmCusCardId extends FWindow {

	private static final long serialVersionUID = 1L;

	public org.zkoss.zul.Image img11;
	private Grid grid2;

	public Textbox txtMicroServiceLink;

	public Textbox txtCitizenID;
	public Textbox txtNameTH_Prefix;
	public Textbox txtNameTH_FirstName;
	public Textbox txtNameTH_SurName;
	public Textbox txtSex;
	public Textbox txtBirthDate;
	public Textbox txtAge;
	public Textbox txtAddress_No;
	public Textbox txtAddress_Moo;
	public Textbox txtAddress_Soi;
	public Textbox txtAddress_Road;
	public Textbox txtAddress_Tumbol;
	public Textbox txtAddress_Amphur;
	public Textbox txtAddress_Province;

	private java.util.List<FModelHasMap> lst_data = new java.util.ArrayList<FModelHasMap>();

	@Override
	public void setFormObj() {

		txtMicroServiceLink = (Textbox) getFellow("txtMicroServiceLink");
		txtCitizenID = (Textbox) getFellow("txtCitizenID");
		txtNameTH_Prefix = (Textbox) getFellow("txtNameTH_Prefix");
		txtNameTH_FirstName = (Textbox) getFellow("txtNameTH_FirstName");
		txtNameTH_SurName = (Textbox) getFellow("txtNameTH_SurName");
		txtSex = (Textbox) getFellow("txtSex");
		txtBirthDate = (Textbox) getFellow("txtBirthDate");
		txtAge = (Textbox) getFellow("txtAge");
		txtAddress_No = (Textbox) getFellow("txtAddress_No");
		txtAddress_Moo = (Textbox) getFellow("txtAddress_Moo");
		txtAddress_Soi = (Textbox) getFellow("txtAddress_Soi");
		txtAddress_Road = (Textbox) getFellow("txtAddress_Road");
		txtAddress_Tumbol = (Textbox) getFellow("txtAddress_Tumbol");
		txtAddress_Amphur = (Textbox) getFellow("txtAddress_Amphur");
		txtAddress_Province = (Textbox) getFellow("txtAddress_Province");

		img11 = (org.zkoss.zul.Image) getFellow("img11");
		grid2 = (Grid) getFellow("grid2");

		ZkUtil.setGridHeaderStyle(grid2);
		grid2.setRowRenderer(getRowRenderer1());
	}

	@Override
	public void addEnterIndex() {
	}

	@Override
	public void formInit() {
		String linkMs = FConfig.getConfig2("MicroServiceLink");
		if (!linkMs.endsWith("/")) {
			linkMs += "/";
		}
		System.out.println("linkMs=" + linkMs);
		txtMicroServiceLink.setValue(linkMs);
	}

	public void onOK() {

		try {
		} catch (Exception e) {
		}

	}

	@Listen("btnRead11Click")
	public void btnRead11Click(Event evt) {

		//ทดสอบผ่าน  01/02/65
		try {

			System.out.println("btnRead11Click");
			JSONObject data = (JSONObject) evt.getData();

			txtCitizenID.setValue(data.get("CitizenID").toString());
			txtNameTH_Prefix.setValue(data.get("NameTH_Prefix").toString());
			txtNameTH_FirstName.setValue(data.get("NameTH_FirstName").toString());
			txtNameTH_SurName.setValue(data.get("NameTH_SurName").toString());
			txtSex.setValue(data.get("Sex").toString().equals("1") ? "ชาย" : "หญิง");
			txtBirthDate.setValue(data.get("BirthDate").toString());
			txtAge.setValue(data.get("Age").toString());
			txtAddress_No.setValue(data.get("Address_No").toString());
			txtAddress_Moo.setValue(data.get("Address_Moo").toString());
			txtAddress_Soi.setValue(data.get("Address_Soi").toString());
			txtAddress_Road.setValue(data.get("Address_Road").toString());
			txtAddress_Tumbol.setValue(data.get("Address_Tumbol").toString());
			txtAddress_Amphur.setValue(data.get("Address_Amphur").toString());
			txtAddress_Province.setValue(data.get("Address_Province").toString());

			byte[] decode = java.util.Base64.getDecoder().decode(data.get("Image").toString());
			AImage aimage = new AImage("image001.jpg", decode);
			this.img11.setContent(aimage);

			//แสดงข้อมูลลูกค้าที่คล้ายกับบัตรนี้
			//1. ชื่อและนามสกุลเหมือน
			//2. รหัสลูกค้าเหมือน
			//3. เลขบัตรประชาชนเหมือน
			checkDupData();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void checkDupData() throws SQLException, Exception {
		//ตรวจสอบข้อมูลซ้ำ
		//System.out.println("checkDupData");
		TbmFCUS.getCust1(txtCitizenID.getValue(), txtNameTH_FirstName.getValue(),
				txtNameTH_SurName.getValue(), txtCitizenID.getValue(), this.getLoginBean(), lst_data);
		SimpleListModel<FModelHasMap> rstModel = new SimpleListModel<FModelHasMap>(lst_data);
		//rstModel.setMultiple(true);//กรณีใช้งาน checkbox ต้อง set muti ที่ model แทนไม่งั้น checkbox แบบ muti จะหาย
		this.grid2.setModel(rstModel);
		//this.grid2.renderAll(); //สั่งให้ render object ทุกแถวโดยไม่ต้องรอ click next ไปที่หน้านั้น  บางกรณีไม่จำเป็นต้องใช้เพราะจะทำให้ระบบช้า	

	}

	@Listen("showErrorMsg2")
	public void showErrorMsg2(Event evt) {
		Msg.error(evt.getData().toString());
	}

	public void btnNewCust11_Click() {
		//สร้างข้อมูลลูกค้าจากบัตรประชาชนที่อ่าน
		try {
			//..ยังไม่ทำ

		} catch (Exception e) {
			e.printStackTrace();
			Msg.error(e.getMessage());
		}
	}

	@Listen("showDataFile0001")
	public void showDataFile0001(Event evt) {
		System.out.println("showDataFile0001");
		System.out.println(evt.getData().toString());
		//System.out.println(evt.getData());
	}

	public RowRenderer<Object> getRowRenderer1() {

		return (row, dat, index) -> {

			FModelHasMap rs = (FModelHasMap) dat;
			int seq = index + 1;
			row.setStyle(ZkUtil.styleFindLookUp);

			row.appendChild(ZkUtil.gridLabel(seq + ""));
			row.appendChild(ZkUtil.gridLabel(rs.getString("CUST_CDE")));
			row.appendChild(ZkUtil.gridLabel(rs.getString("TITLE")));
			row.appendChild(ZkUtil.gridLabel(rs.getString("FNAME")));
			row.appendChild(ZkUtil.gridLabel(rs.getString("LNAME")));
			row.appendChild(ZkUtil.gridLabel(rs.getString("IDNO")));

			//=== เพิ่ม Attribute
			//row.setAttribute("xx", rs.getString("xx"));

		};

	}

	public static void popupForm(Object callObject, LoginBean p_loginbean) {

		try {
			java.io.InputStream inputStream = FrmCusCardId.class.getResourceAsStream(
					"/com/pcc/gl/zul/FrmCus/FrmCusCardId.zul");
			FrmCusCardId win1 = (FrmCusCardId) Executions.createComponentsDirectly(
					new java.io.InputStreamReader(inputStream, "UTF-8"), "zul", null, null);
			win1.setPosition("top,center");
			win1.setLoginBean(p_loginbean);
			win1.doModal();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

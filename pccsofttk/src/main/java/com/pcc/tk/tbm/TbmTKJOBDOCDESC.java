package com.pcc.tk.tbm;

import com.pcc.tk.tbf.TbfTKJOBDOCDESC;
import com.pcc.tk.tbo.TboTKJOBDOCDESC;

public class TbmTKJOBDOCDESC {

	public static boolean Insert_DOCDESC(String comp_cde, String sys_cde, String docdesc) throws Exception {

		TboTKJOBDOCDESC doc = new TboTKJOBDOCDESC();

		doc.setCOMP_CDE(comp_cde);
		doc.setSYS_CDE(sys_cde);
		doc.setDOCDESC(docdesc);

		if (TbfTKJOBDOCDESC.find(doc)) {
			return false;
		} else {
			TbfTKJOBDOCDESC.insert(doc);
			return true;
		}
		
	}

}

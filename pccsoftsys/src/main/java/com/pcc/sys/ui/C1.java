package com.pcc.sys.ui;

import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zul.Div;
//import org.zkoss.zul.Window;

//public class C1 extends SelectorComposer <Window>{
public class C1 extends SelectorComposer <Div>{
	private static final long serialVersionUID = 1L;

	public C1() {
		//this.doAfterCompose();
	}

//	public void doAfterCompose(Window w) {
//		//super.doAfterCompose(w);
//		System.out.println("this.getSelf().getId() :" +this.getSelf().getId());
//		System.out.println("w.getId() :"+w.getId());
//	}
	
	public void doAfterCompose(Div w) {
		//super.doAfterCompose(w);
		System.out.println("this.getSelf().getId() :" +this.getSelf().getId());
		System.out.println("w.getId() :"+w.getId());
	}

}

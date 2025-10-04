package com.pcc.sys.lib;

import java.util.Collection;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class FJRBeanCollectionDataSource extends JRBeanCollectionDataSource {

	public FJRBeanCollectionDataSource(Collection beanCollection) {
		super(beanCollection);
	}

}

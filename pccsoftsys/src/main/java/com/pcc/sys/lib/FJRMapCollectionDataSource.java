package com.pcc.sys.lib;

import java.util.Collection;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

public class FJRMapCollectionDataSource extends JRMapCollectionDataSource {
	public FJRMapCollectionDataSource(Collection<Map<String, ?>> col) {
		super(col);
	}
}

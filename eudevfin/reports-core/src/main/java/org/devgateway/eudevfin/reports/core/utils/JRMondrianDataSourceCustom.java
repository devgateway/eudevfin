package org.devgateway.eudevfin.reports.core.utils;

import mondrian.olap.Result;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.olap.mondrian.JRMondrianResult;

public class JRMondrianDataSourceCustom extends JROlapDataSourceCustom
{
	protected final Result result;

	public JRMondrianDataSourceCustom(JRDataset dataset, Result result)
	{
		super(dataset, new JRMondrianResult(result));

		this.result = result;
	}
}

/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
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

/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.reports.core.utils;

import java.util.Map;

import mondrian.olap.Connection;
import mondrian.olap.Query;
import mondrian.olap.Result;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.olap.JRMondrianQueryExecuter;
import net.sf.jasperreports.olap.JRMondrianQueryExecuterFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/*
 * 
 * 
 * 
 * */
public class JRMondrianQueryExecuterCustom extends JRMondrianQueryExecuter {
	private static final Log log = LogFactory
			.getLog(JRMondrianQueryExecuterCustom.class);

	private Connection connection;
	private Result result;

	public JRMondrianQueryExecuterCustom(
			JasperReportsContext jasperReportsContext, JRDataset dataset,
			Map<String, ? extends JRValueParameter> parametersMap) {
		super(jasperReportsContext, dataset, parametersMap);
		connection = (Connection) getParameterValue(JRMondrianQueryExecuterFactory.PARAMETER_MONDRIAN_CONNECTION);
		parseQuery();
	}

	@SuppressWarnings("deprecation")
	@Override
	public final JRDataSource createDatasource() throws JRException {
		JRDataSource dataSource = null;
		String queryStr = getQueryString();
		if (connection != null && queryStr != null) {
			Query query = connection.parseQuery(queryStr);
			result = connection.execute(query);
			dataSource = new JRMondrianDataSourceCustom(dataset, result);
		}

		return dataSource;
	}

}

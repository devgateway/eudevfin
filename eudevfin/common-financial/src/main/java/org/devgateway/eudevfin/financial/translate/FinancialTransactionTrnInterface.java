/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.financial.translate;

public interface FinancialTransactionTrnInterface {

	public String getDescription();

	public void setDescription(String description);

	public String getShortDescription();

	public void setShortDescription(String shortDescription);

	public void setChannelInstitutionName(String channelInstitutionName);

	public String getChannelInstitutionName();
}
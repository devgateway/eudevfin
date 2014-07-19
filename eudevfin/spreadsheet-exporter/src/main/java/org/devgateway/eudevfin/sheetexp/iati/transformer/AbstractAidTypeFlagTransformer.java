/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.iati.transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.sheetexp.iati.domain.AidtypeFlag;
import org.devgateway.eudevfin.sheetexp.iati.domain.CrsAdd;
import org.devgateway.eudevfin.sheetexp.iati.domain.IatiActivity;
import org.devgateway.eudevfin.sheetexp.iati.transformer.util.Conditions;

/**
 * @author alexandru-m-g
 * 
 */

public abstract class AbstractAidTypeFlagTransformer extends
		AbstractElementTransformer {

	public AbstractAidTypeFlagTransformer(final CustomFinancialTransaction ctx,
			final IatiActivity iatiActivity, final Map<String, Object> paramsMap) {
		super(ctx, iatiActivity, paramsMap);
	}

	protected List<AidtypeFlag> getFlagList() {
		CrsAdd crsAdd = this.getIatiActivity().getCrsAdd();
		if (crsAdd == null) {
			crsAdd = new CrsAdd();
			this.getIatiActivity().setCrsAdd(crsAdd);
			crsAdd.setAidtypeFlags(new ArrayList<AidtypeFlag>());
		}
		List<AidtypeFlag> flags = crsAdd.getAidtypeFlags();
		if (flags == null) {
			flags = new ArrayList<>();
			crsAdd.setAidtypeFlags(flags);
		}
		return flags;
	}

	@Override
	public void process() {
		final Boolean flagValue = this.getAidtypeFlag();
		if (flagValue != null) {
			boolean found = false;
			final List<AidtypeFlag> flags = this.getFlagList();
			for (final AidtypeFlag current : flags) {
				if (this.getFlagCode().equals(current.getCode())) {
					found = true;
					if (Conditions.IS_REVISION(this.getCtx()) || flagValue) {
						current.setSignificance(this
								.translateSignificance(flagValue));
					}

				}
			}
			if (!found) {
				final AidtypeFlag newFlag = new AidtypeFlag();
				newFlag.setCode(this.getFlagCode());
				newFlag.setSignificance(this.translateSignificance(flagValue));
				flags.add(newFlag);
			}

		}
	}

	protected String translateSignificance(final Boolean signBoolean) {
		if (new Boolean(true).equals(signBoolean)) {
			return "1";
		} else {
			return "0";
		}
	}

	protected abstract Boolean getAidtypeFlag();

	protected abstract String getFlagCode();

}

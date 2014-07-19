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
import java.util.ListIterator;
import java.util.Map;

import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.sheetexp.iati.domain.IatiActivity;
import org.devgateway.eudevfin.sheetexp.iati.domain.PolicyMarker;
import org.devgateway.eudevfin.sheetexp.iati.transformer.util.Conditions;

/**
 * @author alexandru-m-g
 *
 */
public abstract class AbstractMarkerTransformer extends AbstractElementTransformer {

	public AbstractMarkerTransformer(final CustomFinancialTransaction ctx, final IatiActivity iatiActivity,
			final Map<String, Object> paramsMap) {
		super(ctx, iatiActivity, paramsMap);
	}

	@Override
	public void process() {
		final Category markerCategory = this.findMarkerCategory();
		if (markerCategory != null){
			final PolicyMarker pm = new PolicyMarker(null, this.getPolicyMarkerCode(), "DAC", markerCategory.getDisplayableCode() );
			
			List<PolicyMarker> markers = getMarkerList();
			boolean found = false;
			final ListIterator<PolicyMarker> it = markers.listIterator();
			while (it.hasNext()){
				final PolicyMarker tempPm = it.next();
				if ( this.getPolicyMarkerCode().equals(tempPm.getCode()) ) {
					found = true;
					if (Conditions.IS_REVISION(this.getCtx()) ) {
						it.set(pm);
						break;
					}
					else {
						int currentPriority = 0;
						try {
							currentPriority = Integer.parseInt(tempPm.getSignificance());
						}
						catch(final NumberFormatException e) {
							it.set(pm);
							break;
						}
						int newPriority = 0;
						try {
							newPriority = Integer.parseInt(tempPm.getSignificance());
						}
						catch(final NumberFormatException e) {
							break;
						}
						if (newPriority > currentPriority) {
							it.set(pm);
						}
					}
				}
			}
			if (!found){
				markers.add(pm);
			}
		}
		
	}

	protected List<PolicyMarker> getMarkerList() {
		List<PolicyMarker> markers =  this.getIatiActivity().getPolicyMarkers();
		if ( markers == null ) {
			markers = new ArrayList<>();
			this.getIatiActivity().setPolicyMarkers(markers);
		}
		return markers;
	}
	
	protected abstract String getPolicyMarkerCode();
	
	protected abstract Category findMarkerCategory();
	
}
/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.iati.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.service.CustomFinancialTransactionService;
import org.devgateway.eudevfin.sheetexp.dto.EntityWrapperInterface;
import org.devgateway.eudevfin.sheetexp.integration.api.SpreadsheetTransformerService;
import org.devgateway.eudevfin.sheetexp.ui.model.Filter;
import org.devgateway.eudevfin.sheetexp.util.EntityWrapperListHelper;
import org.devgateway.eudevfin.sheetexp.util.ITransformationStarter;
import org.devgateway.eudevfin.ui.common.temporary.SB;
import org.joda.time.LocalDateTime;

/**
 * @author alexandru-m-g
 *
 */
public class IatiTransformationStarter implements ITransformationStarter {
	private static Logger logger = Logger.getLogger(IatiTransformationStarter.class);
	
	private List<EntityWrapperInterface<?>> finalList;

	@Override
	public IatiTransformationStarter prepareTransformation(final Filter filter, final CustomFinancialTransactionService txService) {
		final LocalDateTime now = LocalDateTime.now();
		
		this.finalList = new ArrayList<EntityWrapperInterface<?>>();
		
		final List<CustomFinancialTransaction> txList = 
				txService.findByApprovedTrueAndFormTypeInOrderByCrsIdAscCreatedDateAsc( this.getAllowedFormTypes() );
		new EntityWrapperListHelper<CustomFinancialTransaction>(txList, "iati-export", now, "en")
				.addToWrappedList(this.finalList);
		
		return this;
		
	}

	private Collection<String> getAllowedFormTypes() {
		return Arrays.asList(new String[] { SB.BILATERAL_ODA_CRS, SB.BILATERAL_ODA_FORWARD_SPENDING });
	}
	
	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.sheetexp.util.ITransformationStarter#executeTransformation(javax.servlet.http.HttpServletResponse, org.devgateway.eudevfin.sheetexp.integration.api.SpreadsheetTransformerService)
	 */
	@Override
	public void executeTransformation(final HttpServletResponse response, final SpreadsheetTransformerService transformerService) {
		try {
			final String filename	= "iati-export.xls";
			response.setContentType("application/xml");
			response.setHeader("Content-Disposition", "inline; filename=" + filename);
			final OutputStream out				= response.getOutputStream();
			transformerService.createIatiXmlOnStream(this.finalList,
					new BufferedOutputStream(out), "IATI" );
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

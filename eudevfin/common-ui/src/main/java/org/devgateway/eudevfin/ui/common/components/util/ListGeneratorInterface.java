package org.devgateway.eudevfin.ui.common.components.util;

import java.io.Serializable;

import org.devgateway.eudevfin.financial.util.PagingHelper;

public interface ListGeneratorInterface<T> extends Serializable {
	
	public PagingHelper<T> getResultsList(int pageNumber, int pageSize); 

}

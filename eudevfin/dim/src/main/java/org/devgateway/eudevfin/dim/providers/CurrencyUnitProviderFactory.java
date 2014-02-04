package org.devgateway.eudevfin.dim.providers;

import javax.annotation.PostConstruct;

import org.devgateway.eudevfin.dim.exceptions.CurrencyProviderExceptiom;
import org.devgateway.eudevfin.financial.service.CurrencyMetadataService;
import org.devgateway.eudevfin.financial.util.CurrencyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CurrencyUnitProviderFactory {
	
	public final static String NATIONAL_UNSORTED_CURRENCIES_PROVIDER = "national_unsorted_currencies_provider";
	public final static String ALL_SORTED_CURRENCIES_PROVIDER = "all_sorted_currencies_provider";
	
	@Autowired
	private CurrencyMetadataService service;
	
	@Value("#{commonProperties['sel.defaultSelectorPageSize']}")
	private Integer pageSize;
	
	private CurrencyUnitProvider nationalUnsorted;
	private CurrencyUnitProvider allSorted;
	
	@PostConstruct
	private void init() {
		this.nationalUnsorted	= new CurrencyUnitProvider(service, pageSize, false, 
				CurrencyConstants.NATIONAL_CURRENCIES_LIST);
		this.allSorted			= new CurrencyUnitProvider(service, pageSize, true, 
				CurrencyConstants.ALL_CURRENCIES_LIST);
	}
	
	public CurrencyUnitProvider getCurrencyUnitProviderInstance(String providerType) {
		if ( NATIONAL_UNSORTED_CURRENCIES_PROVIDER.equals(providerType) )
			return this.nationalUnsorted;
		else if ( ALL_SORTED_CURRENCIES_PROVIDER.equals(providerType) )
			return this.allSorted;
		throw new CurrencyProviderExceptiom("No such currency unit provider: " + providerType);
	}
}

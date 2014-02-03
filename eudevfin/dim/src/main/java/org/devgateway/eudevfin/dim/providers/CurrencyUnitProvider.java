/**
 * 
 */
package org.devgateway.eudevfin.dim.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.devgateway.eudevfin.common.spring.SpringPropertyExpressions;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.financial.service.CurrencyMetadataService;
import org.joda.money.CurrencyUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.vaynberg.wicket.select2.Response;
import com.vaynberg.wicket.select2.TextChoiceProvider;

/**
 * @author Alex
 *
 */
@Component
public class CurrencyUnitProvider extends TextChoiceProvider<CurrencyUnit> {
	
	private static final long serialVersionUID = -112087305890179315L;

	@Autowired
	private CurrencyMetadataService service;
	
	@Value(SpringPropertyExpressions.SELECT2_PAGE_SIZE)
	private Integer pageSize;

	@Override
	public String getDisplayText(CurrencyUnit choice) {
		return choice.getCode();
	}

	@Override
	public Object getId(CurrencyUnit choice) {
		return choice.getCode();
	}

	@Override
	public void query(String term, int page, Response<CurrencyUnit> response) {
		Pageable pageable			= new PageRequest(page, pageSize);
		Page<CurrencyUnit> results	= this.service.findBySearch(term, pageable);
		if (results != null) {
			for (CurrencyUnit currencyUnit : results) {
				response.add(currencyUnit);
			}
			response.setHasMore( results.hasNextPage() );

		}
		
	}

	@Override
	public Collection<CurrencyUnit> toChoices(Collection<String> ids) {
		List<CurrencyUnit> result	= new ArrayList<CurrencyUnit>();
		if ( ids != null ) {
			for (String code : ids) {
				if (code != null && !code.isEmpty()) {
					NullableWrapper<CurrencyUnit> nwUnit	
								= this.service.findByCode(code);
					if ( !nwUnit.isNull() ) {
						result.add(nwUnit.getEntity());
					}
				}
			}
		}
		return result;
	}

}

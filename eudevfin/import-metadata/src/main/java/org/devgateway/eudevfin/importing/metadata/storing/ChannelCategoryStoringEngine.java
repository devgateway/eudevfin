/**
 * 
 */
package org.devgateway.eudevfin.importing.metadata.storing;

import org.devgateway.eudevfin.importing.metadata.mapping.ChannelCategoryMapper;
import org.devgateway.eudevfin.importing.metadata.mapping.MapperInterface;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.ChannelCategory;
import org.springframework.stereotype.Component;

/**
 * @author Alex
 *
 */
@Component
public class ChannelCategoryStoringEngine extends CategoryStoringEngine {
	@Override
	public int importHashcode(final Category cat) {
		if ( cat instanceof ChannelCategory ) {
			throw new IllegalArgumentException("importHashcode() function in ChannelCategoryMapper needs a ChannelCategory instance as a parameter");
		}
		final ChannelCategory channelCat	= (ChannelCategory) cat;
		int result	=  super.importHashcode(cat);
		channelCat.setLocale("en");
		result = HASH_PRIME * result + this.hashcodeFromObject(channelCat.getCoefficient());
		result = HASH_PRIME * result + this.hashcodeFromObject(channelCat.getDac2a3a());
		result = HASH_PRIME * result + this.hashcodeFromObject(channelCat.getMcd());
		result = HASH_PRIME * result + this.hashcodeFromObject(channelCat.getAcronym());
		
		channelCat.setLocale("fr");
		result = HASH_PRIME * result + this.hashcodeFromObject(channelCat.getAcronym());
		
		return result;
	}
	
	@Override
	public Class<? extends MapperInterface> getRelatedMapper() {
		return ChannelCategoryMapper.class;
	}
}

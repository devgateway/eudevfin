package org.devgateway.eudevfin.importing.metadata.storing;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.common.dao.translation.AbstractTranslateable;

public abstract class AbstractStoringEngine<T extends AbstractTranslateable> 
		implements IStoringEngine<T> {
	
	private static Logger logger	= Logger.getLogger(AbstractStoringEngine.class);
	
	protected static final int HASH_PRIME	= 31;
	
	@Override
	public boolean process(final T entityFromFile) { 
		boolean result	= false;
		final T entityFromDb	= this.findEquivalentEntity(entityFromFile);
		if ( entityFromDb != null ) {
			if ( !this.checkSame(entityFromFile, entityFromDb) ){
				logger.info(String.format("Entity of class %s with id %d has changed. Object from file: %s . Overwriting...", 
						entityFromDb.getClass(), entityFromDb.getId(), entityFromFile.toString()));
				entityFromFile.setId(entityFromDb.getId());
				this.save(entityFromFile);
				result	= true;
			}
		}else {
			logger.debug( String.format("Entity %s does not exist in the database. Saving...", entityFromFile.toString()) );
			this.save(entityFromFile);
			result	= true;
		}
		return result;
	}
	
	@Override
	public boolean checkSame(final T entityFromFile, final T entityFromDb) {
		final int entityFromFileHash	= this.importHashcodeWrapper(entityFromFile);
		final int entityFromDbHash	= this.importHashcodeWrapper(entityFromDb);
		return entityFromFileHash == entityFromDbHash ;
	}
	
	
	private int importHashcodeWrapper(final T entity) {
		final String originalLocale	= entity.getLocale();
		final int result	= this.importHashcode(entity);
		
		entity.setLocale(originalLocale);
		return result;
		
	}
	
	protected int hashcodeFromObject(final Object o) {
		return o==null ? 0 : o.hashCode();
	}
}

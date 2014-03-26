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
		final int entityFromFileHash	= this.importHashcode(entityFromFile);
		final int entityFromDbHash	= this.importHashcode(entityFromDb);
		return entityFromFileHash == entityFromDbHash ;
	}
	
	protected int hashcodeFromObject(final Object o) {
		return o==null ? 0 : o.hashCode();
	}
}

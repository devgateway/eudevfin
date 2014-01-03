/**
 * 
 */
package org.devgateway.eudevfin.financial.dao;

import java.util.ArrayList;
import java.util.List;

import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.util.PagingHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Alex
 *
 */
public abstract class AbstractDaoImpl<Entity,Repo extends PagingAndSortingRepository<Entity, Long>> {
	
	abstract Repo getRepo(); 
	
	public List<Entity> findAllAsList() {
		ArrayList<Entity> ret	= new ArrayList<>(); 
		Iterable<Entity> result	= getRepo().findAll();
		for (Entity entity : result) {
			ret.add(entity);
			
		}
		return ret;
	}
	
	protected PagingHelper<Entity> createPagingHelperFromPage (Page<Entity> resultPage) {
		PagingHelper<Entity> pagingHelper	= 
				new PagingHelper<>(resultPage.getNumber(), resultPage.getTotalPages(), resultPage.getContent().size(), resultPage.getContent());
				
		return pagingHelper; 
		
	}
	
	public Entity save(Entity e) {
		Entity r = getRepo().save(e);
		return r;
	}
	
	public Iterable<Entity> findAll() {
		return getRepo().findAll();
	}
	
	public void delete(Entity e) {
		getRepo().delete(e);
	}
	
	public void delete(Iterable<Entity> iterable) {
		getRepo().delete(iterable);
	}
	
	public Entity findOne(Long id) {
		return getRepo().findOne(id);
	}
}

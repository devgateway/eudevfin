/**
 * 
 */
package org.devgateway.eudevfin.common.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.devgateway.eudevfin.common.service.PagingHelper;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Alex
 *
 */
public abstract class AbstractDaoImpl<Entity,IDType extends Serializable,Repo extends PagingAndSortingRepository<Entity,IDType>> {
	
	protected abstract Repo getRepo(); 
	
	public List<Entity> findAllAsList() {
		ArrayList<Entity> ret	= new ArrayList<>(); 
		Iterable<Entity> result	= getRepo().findAll();
		for (Entity entity : result) {
			ret.add(entity);
			
		}
		return ret;
	}
	
	/**
	 * return a new {@link NullableWrapper} wrapping the <Entity>. Use this for
	 * possible null replies.
	 * 
	 * @param entity
	 *            the <Entity> that has to be wrapped
	 * @return the {@link NullableWrapper} wrapping the <Entity>
	 */
	protected NullableWrapper<Entity> newWrapper(Entity entity) {
		return new NullableWrapper<Entity>(entity);
	}

	protected PagingHelper<Entity> createPagingHelperFromPage (Page<Entity> resultPage) {
		PagingHelper<Entity> pagingHelper	= 
				new PagingHelper<>(resultPage.getNumber(), resultPage.getTotalPages(), resultPage.getContent().size(), resultPage.getContent());
				
		return pagingHelper; 
		
	}
	
	public NullableWrapper<Entity> save(Entity e) {
		Entity r = getRepo().save(e);
		return newWrapper(r);
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
	
	public  NullableWrapper<Entity> findOne(IDType id) {
		return newWrapper(getRepo().findOne(id));
	}
}

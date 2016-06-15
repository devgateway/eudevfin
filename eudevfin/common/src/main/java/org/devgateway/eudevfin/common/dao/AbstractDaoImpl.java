/**
 * *****************************************************************************
 * Copyright (c) 2014 Development Gateway. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the GNU
 * Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************
 */
/**
 *
 */
package org.devgateway.eudevfin.common.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Alex
 *
 */
public abstract class AbstractDaoImpl<Entity, IDType extends Serializable, Repo extends JpaRepository<Entity, IDType>> {

    protected abstract Repo getRepo();

    public List<Entity> findAllAsList() {
        ArrayList<Entity> ret = new ArrayList<>();
        Iterable<Entity> result = getRepo().findAll();
        for (Entity entity : result) {
            ret.add(entity);

        }
        return ret;
    }

    /**
     * return a new {@link NullableWrapper} wrapping the <Entity>. Use this for
     * possible null replies.
     *
     * @param entity the <Entity> that has to be wrapped
     * @return the {@link NullableWrapper} wrapping the <Entity>
     */
    protected NullableWrapper<Entity> newWrapper(Entity entity) {
        return new NullableWrapper<Entity>(entity);
    }

//	public static PagingHelper<U> createPagingHelperFromPage (U type, Page<U> resultPage) {
//		PagingHelper<U> pagingHelper	= 
//				new PagingHelper<>(resultPage.getNumber(), resultPage.getTotalPages(), resultPage.getContent().size(), resultPage.getContent());
//				
//		return pagingHelper; 
//		
//	}
    /**
     * @see BaseEntityService#save(Object)
     * @param e
     * @return
     */
    public NullableWrapper<Entity> save(Entity e) {
        Entity r = getRepo().save(e);
        return newWrapper(r);
    }

    /**
     * @see BaseEntityService#findAll()
     * @return
     */
    public Iterable<Entity> findAll() {
        return getRepo().findAll();
    }

    public void delete(Entity e) {
        getRepo().delete(e);
    }

    public void delete(Iterable<Entity> iterable) {
        getRepo().delete(iterable);
    }

    /**
     * @see BaseEntityService#findOne(Long)
     * @param id
     * @return
     */
    public NullableWrapper<Entity> findOne(IDType id) {
        return newWrapper(getRepo().findOne(id));
    }

    /**
     * {@link Override} this to make smarter searches, this is just a dumb
     * CrudRepository#findAll
     *
     * @see BaseEntityService#findByGeneralSearchPageable(String, String, int,
     * int, Sort)
     * @param searchString never really used
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @param locale never really used
     * @return
     */
    public Page<Entity> findByGeneralSearch(String searchString, String locale, Pageable pageable) {
        return this.getRepo().findAll(pageable);
    }
}

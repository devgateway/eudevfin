/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
/**
 * 
 */
package org.devgateway.eudevfin.importing.metadata.storing;

import java.util.TreeSet;

import org.devgateway.eudevfin.financial.dao.CategoryDaoImpl;
import org.devgateway.eudevfin.importing.metadata.mapping.CategoryMapper;
import org.devgateway.eudevfin.importing.metadata.mapping.MapperInterface;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Alex
 *
 */
@Component
public class CategoryStoringEngine extends AbstractStoringEngine<Category> {
	
	@Autowired
	CategoryDaoImpl categDaoImpl;

	@Override
	public Category findEquivalentEntity(final Category entityFromFile) {
		return this.categDaoImpl.findByCodeAndClass(entityFromFile.getCode(), entityFromFile.getClass(), true).getEntity();
	}

	@Override
	public void save(final Category entityFromFile) {
		this.categDaoImpl.save(entityFromFile);
		
	}

	
	@Override
	public int importHashcode(final Category cat) {
		int result = 1;
		cat.setLocale("en");
		result = HASH_PRIME * result + this.hashcodeFromObject(cat.getCode());
		if (cat.getParentCategory() != null) {
			result = HASH_PRIME * result + this.hashcodeFromObject(cat.getParentCategory().getCode());
		}
		result = HASH_PRIME * result + this.hashcodeFromObject(cat.getName());
		result = HASH_PRIME * result + this.hashcodeFromObject(cat.getClass().getName());
		
		final TreeSet<String> tagCodes	= new TreeSet<String>();
				
		if ( cat.getTags() != null ) {
			for (final Category tag : cat.getTags()) {
				tagCodes.add(tag.getCode());
			}
		}
		result = HASH_PRIME * result + this.hashcodeFromObject(tagCodes);
		
		cat.setLocale("fr");
		result = HASH_PRIME * result + this.hashcodeFromObject(cat.getName());
		return result;
	}

	@Override
	public Class<? extends MapperInterface> getRelatedMapper() {
		return CategoryMapper.class;
	}

	
}

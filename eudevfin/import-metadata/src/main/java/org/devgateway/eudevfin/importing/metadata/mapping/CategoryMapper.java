/**
 * 
 */
package org.devgateway.eudevfin.importing.metadata.mapping;

import java.util.HashSet;
import java.util.Set;

import liquibase.exception.SetupException;

import org.devgateway.eudevfin.financial.dao.CategoryDaoImpl;
import org.devgateway.eudevfin.importing.metadata.exception.InvalidDataException;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.SectorCategory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Alex
 *
 */
public class CategoryMapper extends AbstractMapper<Category> {
	
	private static final String SECTOR = "sector";
	@Autowired
	CategoryDaoImpl categDao;

	
	
	public CategoryMapper() {
		super();
		try {
			this.setUp();
		} catch (SetupException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Category instantiate() {
		return new Category();
	}
	
	public Category __factory(String type){
		if (SECTOR.equals(type)) {
			return new SectorCategory();
		}
		else
			return new Category();
	}
	
	public void __insertParent(Category newCateg, String parentCode) {
		if (parentCode != null && parentCode.length() > 0) {
			Category parent	= categDao.findByCodeAndClass(parentCode, newCateg.getClass(), true).getEntity();
			if (parent != null) {
				Set<Category> children	= parent.getChildren();
				children.add(newCateg);
				newCateg.setParentCategory(parent);
			}
			else {
				throw new InvalidDataException(
						String.format("Found null parent category for code %s for category with code %s", 
								parentCode, newCateg.getCode() )
				);
			}
		}
	}
	
	public void __insertTag(Category newCateg, String tagCode) {
		if ( tagCode != null && tagCode.length() > 0 ) {
			Category tag	= categDao.findByCodeAndClass(tagCode, Category.class, true).getEntity();
			if (tag != null) {
				if ( newCateg.getTags() == null )
					newCateg.setTags(new HashSet<Category>());
				newCateg.getTags().add(tag);
			}
			else {
				throw new InvalidDataException(
						String.format("Found null tag category for code %s for category with code %s", 
								tagCode, newCateg.getCode() )
				);
			}
		}
	}

}

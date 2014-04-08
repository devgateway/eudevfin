/**
 * 
 */
package org.devgateway.eudevfin.importing.metadata.test.helper;

import java.util.HashSet;

import org.devgateway.eudevfin.financial.dao.CategoryDaoImpl;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.ChannelCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Alex
 *
 */
@Component
public class DbHelper {
	@Autowired
	CategoryDaoImpl categoryDaoImpl;
	
	
	
	public Category createCategory(final String code, final String name, final String parentCode, 
			final PossibleCategClass categClass, final String...tagCodes) {
		final Category c	= categClass.newInstance();
		
		c.setCode(code);
		c.setName(name);
		
		if ( parentCode != null ) {
			final Category parentCategory	= this.categoryDaoImpl.findByCodeAndClass(parentCode, categClass.getClazz(), false).getEntity();
			c.setParentCategory(parentCategory);
		}
		if (tagCodes != null) {
			c.setTags(new HashSet<Category>());
			for (final String tagCode: tagCodes) {
				final Category tag	= this.categoryDaoImpl.findByCodeAndClass(tagCode, Category.class, false).getEntity();;
				c.getTags().add(tag);
			}
		}
		
		return this.categoryDaoImpl.save(c).getEntity();
		
	}
	
	public static enum PossibleCategClass {
		CATEGORY(Category.class), CHANNELCATEGORY(ChannelCategory.class);
		
		private final Class<? extends Category> clazz;
		
		PossibleCategClass(final Class<? extends Category> clazz) {
			this.clazz	= clazz;
		}
		
		public Category newInstance() {
			try {
				return this.clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				return null;
			}
		}

		public Class<? extends Category> getClazz() {
			return this.clazz;
		}
		
	}
}

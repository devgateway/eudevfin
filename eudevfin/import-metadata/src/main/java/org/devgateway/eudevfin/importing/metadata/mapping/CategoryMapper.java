/**
 * 
 */
package org.devgateway.eudevfin.importing.metadata.mapping;

import java.util.HashSet;
import java.util.Set;

import liquibase.exception.SetupException;

import org.devgateway.eudevfin.financial.BiMultilateralCategory;
import org.devgateway.eudevfin.financial.Category;
import org.devgateway.eudevfin.financial.MarkerCategory;
import org.devgateway.eudevfin.financial.NatureOfSubmissionCategory;
import org.devgateway.eudevfin.financial.SectorCategory;
import org.devgateway.eudevfin.financial.TypeOfFinanceCategory;
import org.devgateway.eudevfin.financial.TypeOfFlowCategory;
import org.devgateway.eudevfin.financial.dao.CategoryDaoImpl;
import org.devgateway.eudevfin.importing.metadata.exception.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Alex
 *
 */
public class CategoryMapper extends AbstractMapper<Category> {
	
	private static final String NATURE_OF_SUBMISSION = "nature_of_submission";
	private static final String BI_MULTILATERAL = "bi_multilateral";
	private static final String TYPE_OF_FINANCE = "type_of_finance";
	private static final String TYPE_OF_FLOW = "type_of_flow";
	private static final String MARKER = "marker";
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
		else if (MARKER.equals(type)) {
			return new MarkerCategory();
		}
		else if (TYPE_OF_FLOW.equals(type)) {
			return new TypeOfFlowCategory();
		}
		else if (TYPE_OF_FINANCE.equals(type)) {
			return new TypeOfFinanceCategory();
		}
		else if (BI_MULTILATERAL.equals(type)) {
			return new BiMultilateralCategory();
		}
		else if (NATURE_OF_SUBMISSION.equals(type)) {
			return new NatureOfSubmissionCategory();
		}
		else
			return new Category();
	}
	
	public void __insertParent(Category newCateg, String parentCode) {
		if (parentCode != null && parentCode.length() > 0) {
			Category parent	= categDao.findByCodeAndClass(parentCode, newCateg.getClass(), true);
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
			Category tag	= categDao.findByCodeAndClass(tagCode, Category.class, true);
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

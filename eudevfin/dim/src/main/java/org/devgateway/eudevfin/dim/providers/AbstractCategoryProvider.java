/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.providers;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Session;
import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.financial.Category;
import org.devgateway.eudevfin.financial.service.CategoryService;
import org.json.JSONException;
import org.json.JSONWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.vaynberg.wicket.select2.Response;

/**
 * @author aartimon
 * @since 14/01/14
 */
public abstract class AbstractCategoryProvider extends AbstractTranslatableProvider<Category> {
	
	@Autowired
	protected transient CategoryService categoryService;

    protected AbstractCategoryProvider() {
      //  this.sort=new Sort(Direction.ASC,"code");
    }

    @Override
    protected BaseEntityService<Category> getService() {
        return categoryService;
    }

    @Override
    protected String getDisplayText(Category choice) {
//    	String indent	= "";
//		for (int i=1; i<choice.getLevel(); i++) {
//			indent += "- ";
//		}
        return choice.getDisplayableCode()+ " - "+ choice.getName();
    }

	@Override
	protected Page<Category> getItemsByTerm(String term, int page) {
		Page<Category> categories = categoryService.findByGeneralSearchAndTagsCodePaginated(Session.get().getLocale()
				.getLanguage(), term, getCategoryTag(), new PageRequest(page, pageSize, sort), false);
		return categories;
	}
	
	@Override
	public void query(String term, int page, Response<Category> response) {
		Page<Category> itemsByTerm = getItemsByTerm(term, page);
		response.setHasMore(itemsByTerm.hasNextPage());
		List<Category> responseItems = new ArrayList<>();
		if (itemsByTerm.getNumberOfElements() > 0) {
			for (Category category : itemsByTerm) {
				this.addCategoryToHierarchicalList(category, responseItems);
			}
		}
		response.addAll(responseItems);
	}
	
	private void addCategoryToHierarchicalList(Category category, List<Category> list) {
		if ( ! category.isLastAncestor() ) {
			Category parent	= category.getParentCategory();
			if ( !parent.getFilteredChildren().contains(category))
				parent.getFilteredChildren().add(category);
			this.addCategoryToHierarchicalList(category.getParentCategory(), list);
		}
		else if ( ! list.contains(category) ) {
			list.add( category );
		}
	}
	
	@Override
	public void toJson(Category choice, JSONWriter writer) throws JSONException {
		System.out.println("Dealing with choice: " + choice.getCode() + " -- "
				+ choice.getName());
		super.toJson(choice, writer);
		if (choice.getChildren() != null && choice.getFilteredChildren().size() > 0) {
			writer.key("children");
			writer.array();
			for (Category childChoice : choice.getFilteredChildren()) {
				writer.object();
				this.toJson(childChoice, writer);
				writer.endObject();
			}
			writer.endArray();

		}
	}
	
    protected abstract String getCategoryTag();
    
   

}

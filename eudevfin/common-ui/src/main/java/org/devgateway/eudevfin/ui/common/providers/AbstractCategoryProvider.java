/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.providers;

import com.vaynberg.wicket.select2.Response;
import org.apache.log4j.Logger;
import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.exception.CategoryOperationException;
import org.devgateway.eudevfin.metadata.common.service.CategoryService;
import org.json.JSONException;
import org.json.JSONWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aartimon
 * @since 14/01/14
 */
public abstract class AbstractCategoryProvider extends AbstractTranslatableProvider<Category> {
	
	private static Logger logger	= Logger.getLogger(AbstractCategoryProvider.class);

    @Autowired
    protected transient CategoryService categoryService;

    protected AbstractCategoryProvider() {

    }

    @Override
    protected BaseEntityService<Category> getService() {
        return categoryService;
    }

    @Override
    protected String getDisplayText(Category choice) {
        return choice.getDisplayableCode() + " - " + choice.getName();
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
    	try{
	        if (!category.isLastAncestor()) {
	            Category parent = category.getParentCategory();
	            if (!parent.getFilteredChildren().contains(category))
	                parent.getFilteredChildren().add(category);
	            this.addCategoryToHierarchicalList(category.getParentCategory(), list);
	        } else if (!list.contains(category)) {
	            list.add(category);
	        }
    	}
        catch (CategoryOperationException e) {
			logger.error("Problem with isLasAncestor:" + e.getMessage());
		}
    }

    @Override
    public void toJson(Category choice, JSONWriter writer) throws JSONException {
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

    @Override
    public void detach() {
        categoryService = null;
    }
}

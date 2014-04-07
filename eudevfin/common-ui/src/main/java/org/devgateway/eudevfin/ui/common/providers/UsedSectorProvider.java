package org.devgateway.eudevfin.ui.common.providers;

import org.apache.wicket.Session;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * @author idobre
 * @since 4/7/14
 */
public class UsedSectorProvider extends AbstractCategoryProvider {

    protected UsedSectorProvider() {

    }

    @Override
    protected Page<Category> getItemsByTerm(String term, int page) {
        Page<Category> categories = categoryService.findUsedSectorPaginated(Session.get().getLocale()
                .getLanguage(), term, new PageRequest(page, pageSize, sort), false);
        return categories;
    }
}
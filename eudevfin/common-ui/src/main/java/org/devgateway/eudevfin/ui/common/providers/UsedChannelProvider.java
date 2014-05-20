package org.devgateway.eudevfin.ui.common.providers;

import org.apache.wicket.Session;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.ChannelCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * @author idobre
 * @since 4/7/14
 */
public class UsedChannelProvider extends AbstractCategoryProvider {

    protected UsedChannelProvider() {

    }

    @Override
    protected Page<Category> getItemsByTerm(String term, int page) {
        Page<Category> categories = categoryService.findUsedChannelPaginated(Session.get().getLocale()
                .getLanguage(), term, new PageRequest(page, pageSize, sort));
        return categories;
    }
}
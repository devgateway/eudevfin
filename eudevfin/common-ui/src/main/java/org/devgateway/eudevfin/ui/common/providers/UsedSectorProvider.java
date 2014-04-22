package org.devgateway.eudevfin.ui.common.providers;

import com.vaynberg.wicket.select2.Response;
import org.apache.wicket.Session;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void query(String term, int page, Response<Category> response) {
        Page<Category> itemsByTerm = getItemsByTerm(term, page);
        response.setHasMore(itemsByTerm.hasNextPage());
        List<Category> responseItems = new ArrayList<>();
        if (itemsByTerm.getNumberOfElements() > 0) {
            for (Category category : itemsByTerm) {
                responseItems.add(category);
            }
        }
        response.addAll(responseItems);
    }
}
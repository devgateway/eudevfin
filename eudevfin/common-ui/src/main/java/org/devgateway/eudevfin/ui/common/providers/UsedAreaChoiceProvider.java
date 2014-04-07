package org.devgateway.eudevfin.ui.common.providers;

import org.apache.wicket.Session;
import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

/**
 * @author idobre
 * @since 4/7/14
 */
@Component
public class UsedAreaChoiceProvider extends AbstractTranslatableProvider<Area> {
    public UsedAreaChoiceProvider() {
    }

    @Autowired
    private AreaService areaService;


    @Override
    protected BaseEntityService<Area> getService() {
        return areaService;
    }

    @Override
    protected Page<Area> getItemsByTerm(String term, int page) {
        Page<Area> areas = areaService.findUsedAreaPaginated(Session.get().getLocale()
                .getLanguage(), term, new PageRequest(page, pageSize, sort));

        return areas;
    }

    @Override
    public String getDisplayText(Area choice) {
        return choice.getCode() + " - " + choice.getName();
    }

    @Override
    public void detach() {
        // Spring component no need to detach if added into wicket components with @SpringBean
    }
}

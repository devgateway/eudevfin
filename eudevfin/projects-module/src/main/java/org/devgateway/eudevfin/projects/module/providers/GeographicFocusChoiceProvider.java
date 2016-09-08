/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.providers;

import com.vaynberg.wicket.select2.Response;
import java.util.ArrayList;
import java.util.List;
import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.service.AreaService;
import org.devgateway.eudevfin.ui.common.providers.AbstractTranslatableProvider;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author alcr
 */

public class GeographicFocusChoiceProvider extends AbstractTranslatableProvider<Area> {
        
    private List<Area> filteredAreas;
    
    @Autowired
    private AreaService areaService;
       
    public GeographicFocusChoiceProvider (List<Area> filteredAreas, AreaService areaService ) {        
        this.filteredAreas = filteredAreas;
        this.areaService = areaService;
    }
    
    public GeographicFocusChoiceProvider() {
    }
    
    @Override
    public void query(final String term, final int page, final Response<Area> response) {
        final List<Area> ret = new ArrayList<>();
        List<Area> values;
        if (this.filteredAreas != null && this.filteredAreas.size() > 0) {
            values = this.filteredAreas;
        } else {
            values = new ArrayList<>();
        }

        for (final Area el : values) {
            if (getDisplayText(el).toLowerCase().contains(term)) {
                ret.add(el);
            }
        }
        response.addAll(ret);
    }
    
    @Override
    protected BaseEntityService<Area> getService() {
        return areaService;
    }

    @Override
    public String getDisplayText(Area choice) {
        return choice.getCode() + " - " + choice.getName();
    }

    @Override
    public void detach() {
        //Spring component no need to detach if added into wicket components with @SpringBean
    }
    
}

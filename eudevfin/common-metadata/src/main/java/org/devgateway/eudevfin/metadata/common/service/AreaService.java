package org.devgateway.eudevfin.metadata.common.service;

import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;


public interface AreaService extends BaseEntityService<Area>  {
    public Page<Area> findUsedAreaPaginated(
            @Header("locale") String locale, String searchString,
            @Header("pageable") Pageable page);
}

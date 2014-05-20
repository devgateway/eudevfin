package org.devgateway.eudevfin.ui.common.components.util;

import mondrian.olap.CacheControl;
import mondrian.olap.Connection;
import mondrian.olap.Cube;
import mondrian.olap.DriverManager;
import mondrian.olap.Util;
import mondrian.rolap.RolapConnectionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Created by idobre on 2/12/14.
 */

@Lazy(value=true)
@Component
public class MondrianCacheUtil {
    @Autowired
    private DataSource cdaDataSource;

    /**
     * method that clears the Mondrian cache.
     * It should be called after a new transactions is saved
     */
    public void flushMondrianCache () {
        Util.PropertyList propertyList = new Util.PropertyList();
        propertyList.put("Provider", "mondrian");
        // don't add the Catalog here (but the Connect string must contain a property 'Catalog')
        // it will be added in Schema Processor
        propertyList.put("Catalog", "somePath");
        propertyList.put(RolapConnectionProperties.DynamicSchemaProcessor.toString(),
                "org.devgateway.eudevfin.reports.core.utils.SchemaProcessor");

        Connection connection = DriverManager.getConnection(propertyList, null, cdaDataSource);

        // flush the cache
        CacheControl cacheControl = connection.getCacheControl(null);
        for (Cube cube : connection.getSchema().getCubes()) {
            cacheControl.flush(cacheControl.createMeasuresRegion(cube));
        }
        cacheControl.flushSchemaCache();
    }
}

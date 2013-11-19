package org.devgateway.eudevfin.exchange.json;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

public class ObjectMapperFactory {
    
    public static ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();      

        return mapper;
    }
}
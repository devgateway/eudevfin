@TypeDefs({ @TypeDef(name = "persistentExchangeRate", 
	defaultForType = org.devgateway.eudevfin.exchange.common.domain.HistoricalExchangeRate.class, 
	typeClass = org.jadira.usertype.exchangerate.joda.PersistentExchangeRate.class) })
package org.devgateway.eudevfin.exchange.common.domain;

 import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;


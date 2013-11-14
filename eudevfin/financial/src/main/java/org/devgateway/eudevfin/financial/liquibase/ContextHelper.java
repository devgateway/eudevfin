package org.devgateway.eudevfin.financial.liquibase;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ContextHelper implements ApplicationContextAware  {
	private static ApplicationContext appContext;
	private static ContextHelper singleton;
	
//	static {
//		singleton	= new ContextHelper();
//	}
	
	
	private PopulateDb populateDb;
	
	private ContextHelper() {
		;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ContextHelper.appContext	= applicationContext;
	}
	
	public static ContextHelper newInstance() {
		if ( ContextHelper.singleton == null ) {
			ContextHelper.singleton	= new  ContextHelper();
		}
		return singleton;
	}

	public PopulateDb getPopulateDb() {
		this.populateDb				= ContextHelper.appContext.getBean(PopulateDb.class);
		return populateDb;
	}

}

package org.devgateway.eudevfin.common.spring;

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
	
	public <T> T getBean(String id) {
		T ret 	= (T)ContextHelper.appContext.getBean(id);
		return ret;
	}
	
	public <T> T getBean(Class<T> clazz) {
		T ret 	= ContextHelper.appContext.getBean(clazz);
		return ret;
	}

//	public PopulateDb getPopulateDb() {
//		this.populateDb				= ContextHelper.appContext.getBean(PopulateDb.class);
//		return populateDb;
//	}

}

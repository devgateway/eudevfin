package org.devgateway.eudevfin.common.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component(value="contextHelper")
public class ContextHelper implements ApplicationContextAware  {
	private static ApplicationContext appContext;
	private static ContextHelper singleton;
	
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

	/**
	 * Returns the autowire factory that can inject dependencies in a non-spring
	 * initialized bean (if constructors are for example used outside spring's
	 * scope)
	 * 
	 * @return the factory
	 */
	public AutowireCapableBeanFactory getAutowireCapableBeanFactory() {
		return ContextHelper.appContext.getAutowireCapableBeanFactory();
	}
	
//	public PopulateDb getPopulateDb() {
//		this.populateDb				= ContextHelper.appContext.getBean(PopulateDb.class);
//		return populateDb;
//	}

}

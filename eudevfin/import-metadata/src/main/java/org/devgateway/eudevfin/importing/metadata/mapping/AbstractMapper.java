package org.devgateway.eudevfin.importing.metadata.mapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import liquibase.exception.SetupException;

import org.devgateway.eudevfin.common.spring.ContextHelper;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

public abstract class AbstractMapper<T> 
	implements MapperInterface<T> {

	List<String> metainfos; 
	HashMap<String, Method> methodMap;

	public AbstractMapper() {
		super();
		this.methodMap	= new HashMap<String, Method>();
		final Method[] methods = this.getClass().getMethods();
		for ( final Method method: methods ) {
			if ( method.getName().startsWith("__") ) {
				this.methodMap.put(method.getName(), method);
			}
		}
		
	}

	protected abstract T instantiate();
	
	public Object get(final T entity, final String property) {
		final BeanWrapper beanWrapper = new BeanWrapperImpl(entity);
        return beanWrapper.getPropertyValue(property);
	}

	public void set(final T entity, final String property, final Object value) {
		if (value != null && value.toString().length() > 0) {
			final BeanWrapper beanWrapper = new BeanWrapperImpl(entity);
			beanWrapper.setPropertyValue(property, value);
		}
	}

	@Override
	public T createEntity(final List<String> values) {
		if ( values != null  ) {
			T result	= this.instantiate();
			for (int i=0; i<values.size(); i++) {
				final Object value 			= values.get(i);
				final String unparsedInfo		= this.metainfos.get(i);
				final String[] infoParts		=  unparsedInfo.split("##");
				
				if ( infoParts != null && infoParts.length >= 2 ) {
					final String infoType	= infoParts[0];
					final String info		= infoParts[1];
					String lang		= null;
					if ( infoParts.length == 3 ) {
						lang	= infoParts[2];
						this.set(result, "locale", lang);
					}
					
					if ( "method".equals(infoType) ) {
						final Method m 	= this.methodMap.get(info);
						try {
							m.invoke(this, result ,value);
						} catch (IllegalAccessException
								| IllegalArgumentException
								| InvocationTargetException e) {
							e.printStackTrace();
						}
					}
					else if ("constructor".equals(infoType)) {
						final Method m 	= this.methodMap.get(info);
						try {
							result	= (T) m.invoke(this ,value);
						} catch (IllegalAccessException
								| IllegalArgumentException
								| InvocationTargetException e) {
							e.printStackTrace();
						}
					}
					else if ( "property".equals(infoType) ) {
						this.set(result, info, value);
					}
				}
			}
			return result;
		}
		return null;
	}

	@Override
	public List<String> getMetainfos() {
		return this.metainfos;
	}

	@Override
	public void setMetainfos(final List<String> metainfos) {
		this.metainfos = metainfos;
	}
	
	protected void setUp() throws SetupException {
		final AutowireCapableBeanFactory factory = ContextHelper.newInstance()
				.getAutowireCapableBeanFactory();
		factory.autowireBean(this);
	}
	
}

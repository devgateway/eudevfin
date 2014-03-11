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
		Method[] methods = this.getClass().getMethods();
		for ( Method method: methods ) {
			if ( method.getName().startsWith("__") ) {
				methodMap.put(method.getName(), method);
			}
		}
		
	}

	protected abstract T instantiate();
	
	public Object get(T entity, String property) {
		BeanWrapper beanWrapper = new BeanWrapperImpl(entity);
        return beanWrapper.getPropertyValue(property);
	}

	public void set(T entity, String property, Object value) {
		if (value != null && value.toString().length() > 0) {
			BeanWrapper beanWrapper = new BeanWrapperImpl(entity);
			beanWrapper.setPropertyValue(property, value);
		}
	}

	@Override
	public T createEntity(List<String> values) {
		if ( values != null  ) {
			T result	= this.instantiate();
			for (int i=0; i<values.size(); i++) {
				Object value 			= values.get(i);
				String unparsedInfo		= this.metainfos.get(i);
				String[] infoParts		=  unparsedInfo.split("##");
				
				if ( infoParts != null && infoParts.length >= 2 ) {
					String infoType	= infoParts[0];
					String info		= infoParts[1];
					String lang		= null;
					if ( infoParts.length == 3 ) {
						lang	= infoParts[2];
						this.set(result, "locale", lang);
					}
					
					if ( "method".equals(infoType) ) {
						Method m 	= this.methodMap.get(info);
						try {
							m.invoke(this, result ,value);
						} catch (IllegalAccessException
								| IllegalArgumentException
								| InvocationTargetException e) {
							e.printStackTrace();
						}
					}
					else if ("constructor".equals(infoType)) {
						Method m 	= this.methodMap.get(info);
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
		return metainfos;
	}

	@Override
	public void setMetainfos(List<String> metainfos) {
		this.metainfos = metainfos;
	}
	
	protected void setUp() throws SetupException {
		AutowireCapableBeanFactory factory = ContextHelper.newInstance()
				.getAutowireCapableBeanFactory();
		factory.autowireBean(this);
	}

}

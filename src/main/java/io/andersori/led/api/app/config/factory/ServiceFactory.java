package io.andersori.led.api.app.config.factory;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.ApplicationContext;

import io.andersori.led.api.domain.policy.RestrictionHandler;

public class ServiceFactory<T> extends AbstractFactoryBean<T> {
	
	private Class<?> classTypeIf;
	private Class<?> classTypeImp;
	private ApplicationContext ctx;
	
	public ServiceFactory(Class<T> classTypeIf, Class<?> classTypeImp, ApplicationContext ctx){
		this.classTypeIf = classTypeIf;
		this.classTypeImp = classTypeImp;
		this.ctx = ctx;
	}
	
	@Override
	public Class<?> getObjectType() {
		return classTypeIf;
	}

	@SuppressWarnings("unchecked")
	public T createInstance() throws Exception {
		return (T) RestrictionHandler.proxying(ctx.getBean(classTypeImp), classTypeIf);
	}

}

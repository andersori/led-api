package io.andersori.led.api.domain.policy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.andersori.led.api.app.web.util.AccountContext;
import io.andersori.led.api.domain.entity.RoleLed;
import io.andersori.led.api.domain.exception.MethodNotAllowedException;

public class RestrictionHandler implements InvocationHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RestrictionHandler.class);
	
	private final Object target;
	private final Class<?> classTypeTarget;
	
	public RestrictionHandler(Object target, Class<?> classTypeTarget) {
		this.target = target;
		this.classTypeTarget = classTypeTarget;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Restrictions r = method.getAnnotation(Restrictions.class);
		if(r != null) {
			List<RoleLed> requiredRoles = Arrays.asList(r.value());
			List<RoleLed> userRoles = new ArrayList<RoleLed>(AccountContext.getAccount().getRoles());
						
			List<String> lis1 = requiredRoles.stream().map(rr -> rr.toString()).collect(Collectors.toList());
			List<String> lis2 = userRoles.stream().map(rr -> rr.toString()).collect(Collectors.toList());
			
			LOGGER.info("["+lis1.toString()+"]"+AccountContext.getAccount().getUsername()+"["+lis2.toString()+"]");
			
			boolean canPerform = true;
			for(RoleLed role : requiredRoles) {
				if(!userRoles.contains(role)) {
					canPerform = false;
					break;
				}
			}
			
			if(canPerform) {
				try {
					return method.invoke(target, args);
				} catch(Exception e) {
					throw e.getCause();
				}
			} else {
				throw new MethodNotAllowedException("You don't have permission to execute "+method.getName()+".", classTypeTarget);
			}
		}
		
		try {
			return method.invoke(target, args);
		} catch(Exception e) {
			throw e.getCause();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T proxying(T target, Class<? extends T> iface) {
		return (T) Proxy.newProxyInstance(
				iface.getClassLoader(),
				new Class<?>[] {iface},
				new RestrictionHandler(target, iface));
	}
}

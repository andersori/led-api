package io.andersori.led.api.domain.policy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.andersori.led.api.app.web.util.AccountContext;
import io.andersori.led.api.domain.entity.RoleLed;
import io.andersori.led.api.domain.error.ErrorInfo;
import io.andersori.led.api.domain.exception.ForbiddenExecutionException;

public class RestrictionHandler implements InvocationHandler {
	
	private final Object target;
	
	public RestrictionHandler(Object target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Restrictions r = method.getAnnotation(Restrictions.class);
		if(r != null) {
			List<RoleLed> requiredRoles = Arrays.asList(r.value());
			List<RoleLed> userRoles = new ArrayList<RoleLed>(AccountContext.getAccount().getRoles());
			
			boolean canPerform = true;
			for(RoleLed role : requiredRoles) {
				if(!userRoles.contains(role)) {
					canPerform = false;
					break;
				}
			}
			
			if(canPerform) {
				return method.invoke(target, args);
			} else {
				ErrorInfo error = new ErrorInfo("You need more permissions.", "roles", userRoles);
				throw new ForbiddenExecutionException("You don't have permission.", null, Arrays.asList(error));
			}
		}
		
		return method.invoke(target, args);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T proxying(T target, Class<? extends T> iface) {
		return (T) Proxy.newProxyInstance(
				iface.getClassLoader(),
				new Class<?>[] {iface},
				new RestrictionHandler(target));
	}
}

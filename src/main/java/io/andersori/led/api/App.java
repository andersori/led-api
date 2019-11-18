package io.andersori.led.api;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import io.andersori.led.api.app.config.LedConfig;
import io.andersori.led.api.app.web.controller.filter.AuthorizationFilter;
import io.andersori.led.api.app.web.controller.filter.ResponseTypeFilter;
import io.andersori.led.api.app.web.controller.route.AccountController;
import io.andersori.led.api.app.web.controller.route.TokenController;
import spark.Spark;

public class App {
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(LedConfig.class);

		AuthorizationFilter authorizationFilter = context.getBean(AuthorizationFilter.class);
		AccountController accountController = context.getBean(AccountController.class);
		TokenController tokenController = context.getBean(TokenController.class);
		
		
		Spark.path("/api", () -> {
			Spark.before("/*", authorizationFilter);
			Spark.notFound("");
			Spark.path("/accounts", accountController);
			Spark.path("/tokens", tokenController);
			Spark.afterAfter("/*", ResponseTypeFilter.responseType);
		});
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			((ConfigurableApplicationContext) context).close();
		}));
	}
}

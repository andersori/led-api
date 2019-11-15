package io.andersori.led.api;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import io.andersori.led.api.app.config.LedConfig;
import io.andersori.led.api.app.web.controller.route.AccountController;
import spark.Spark;

public class App {
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(LedConfig.class);

		AccountController accountController = context.getBean(AccountController.class);
		
		Spark.path("/api", () -> {
			Spark.before("/*", (req, res) -> System.out.println("filtro"));
			Spark.path("/accounts", accountController);
		});
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			((ConfigurableApplicationContext) context).close();
		}));
	}
}

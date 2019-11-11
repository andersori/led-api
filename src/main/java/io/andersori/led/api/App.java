package io.andersori.led.api;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import io.andersori.led.api.app.config.LedConfig;

public class App {
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(LedConfig.class);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			((ConfigurableApplicationContext) context).close();
		}));
	}
}

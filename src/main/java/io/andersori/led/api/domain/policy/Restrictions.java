package io.andersori.led.api.domain.policy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.andersori.led.api.domain.entity.RoleLed;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Restrictions {
	RoleLed[] value() default {RoleLed.DEFAULT};
}

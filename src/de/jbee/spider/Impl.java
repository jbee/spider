package de.jbee.spider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention ( RetentionPolicy.RUNTIME )
@Target ( ElementType.PARAMETER )
public @interface Impl {

	String value() default "";
}

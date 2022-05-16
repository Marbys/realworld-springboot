package io.github.marbys.myrealworldapp;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockJwtContextFactory.class)
public @interface WithMockJwtUser {

    long userId() default 0l;
}

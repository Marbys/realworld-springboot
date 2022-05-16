package io.github.marbys.myrealworldapp;

import io.github.marbys.myrealworldapp.jwt.JwtPayload;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.lang.annotation.Annotation;
import java.util.Collections;

import static org.mockito.Mockito.mock;


public class WithMockJwtContextFactory implements WithSecurityContextFactory<WithMockJwtUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockJwtUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new MockJwtAuthenticationToken(mock(JwtPayload.class)));
        return context;
    }

    private static class MockJwtAuthenticationToken extends AbstractAuthenticationToken {
        private final JwtPayload jwtPayload;

        public MockJwtAuthenticationToken(JwtPayload jwtPayload) {
            super(Collections.singletonList(new SimpleGrantedAuthority("USER")));
            super.setAuthenticated(true);
            this.jwtPayload = jwtPayload;
        }

        @Override
        public Object getCredentials() {
            return "MOCKED CREDENTIALS";
        }

        @Override
        public Object getPrincipal() {
            return jwtPayload;
        }
    }
}

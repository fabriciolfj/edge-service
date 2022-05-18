package com.github.fabriciolfj.edgeservice.config;


import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         ReactiveClientRegistrationRepository reactiveClientRegistrationRepository) {
        return http
                .authorizeExchange(ex -> ex.anyExchange().authenticated())
                .logout(logoutSpec -> logoutSpec.logoutSuccessHandler(oidcLogoutSuccessHandler(reactiveClientRegistrationRepository)))
                .oauth2Login(Customizer.withDefaults())
                .build();
    }

    private ServerLogoutSuccessHandler oidcLogoutSuccessHandler(ReactiveClientRegistrationRepository reactiveClientRegistrationRepository) {
        var oidc = new OidcClientInitiatedServerLogoutSuccessHandler(reactiveClientRegistrationRepository);
        oidc.setPostLogoutRedirectUri("{baseUrl}");
        return oidc;
    }
}

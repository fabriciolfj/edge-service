package com.github.fabriciolfj.edgeservice.config;


import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
public class SecurityConfig {

    public static final String ACTUATOR = "/actuator/**";

    @Bean
    ServerOAuth2AuthorizedClientRepository auth2AuthorizedClientRepository() {
        return new WebSessionServerOAuth2AuthorizedClientRepository();
    }

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.
                exceptionHandling()
                .authenticationEntryPoint((serverWebExchange, e) ->
                        Mono.fromRunnable(() ->
                                serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)
                        )
                ).accessDeniedHandler((serverWebExchange, e) -> Mono.fromRunnable(() ->
                        serverWebExchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN)
                )).and()
                ///.redirectToHttps().and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeExchange()
                .pathMatchers(ACTUATOR, "/swagger-ui.html", "/webjars/**",
                        "/swagger-resources/**", "/v2/api-docs/**", "/.well-known/**", "/", "/favicon.ico", "/swagger-ui/**").permitAll()
                .anyExchange().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt();
        return http.build();
    }

    @Bean//normalmente o token csrf e enviado via header, mas para o angular vamos anviar via cookie
    WebFilter csrfWebFilter() {
        return ((exchange, chain) -> {
            exchange.getResponse().beforeCommit(() -> Mono.defer(() -> {
                Mono<CsrfToken> csrfToken = exchange.getAttribute(CsrfToken.class.getName());
                return csrfToken != null ? csrfToken.then() : Mono.empty();
            }));
            return chain.filter(exchange);
        });
    }

    private ServerLogoutSuccessHandler oidcLogoutSuccessHandler(ReactiveClientRegistrationRepository reactiveClientRegistrationRepository) {
        var oidc = new OidcClientInitiatedServerLogoutSuccessHandler(reactiveClientRegistrationRepository);
        oidc.setPostLogoutRedirectUri("{baseUrl}");
        return oidc;
    }
}

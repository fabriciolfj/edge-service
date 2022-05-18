package com.github.fabriciolfj.edgeservice.user;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping
    public Mono<User> getUser(@AuthenticationPrincipal OidcUser oidcUser) {
        System.out.println(oidcUser.getIdToken().getAccessTokenHash());
        return Mono.just(new User(oidcUser.getPreferredUsername(), oidcUser.getGivenName(), oidcUser.getFamilyName(), List.of("employee", "customer")));
    }
}

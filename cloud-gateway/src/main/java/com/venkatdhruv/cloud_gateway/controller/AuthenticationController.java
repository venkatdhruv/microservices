package com.venkatdhruv.cloud_gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.venkatdhruv.cloud_gateway.model.AuthenticationResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/authenticate")
public class AuthenticationController {

    @GetMapping("/login")
    public ResponseEntity<AuthenticationResponse> loging(@AuthenticationPrincipal OidcUser oidcUser, Model model,
            @RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient auth2AuthorizedClient) {

        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .userId(oidcUser.getEmail())
                .accessToken(auth2AuthorizedClient.getAccessToken().getTokenValue())
                .refreshToken(auth2AuthorizedClient.getRefreshToken().getTokenValue())
                .expiresAt(auth2AuthorizedClient.getAccessToken().getExpiresAt().toEpochMilli())
                .authorityList(oidcUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();
        return ResponseEntity.ok(authenticationResponse);
    }

}

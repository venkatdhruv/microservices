package com.venkatdhruv.order_service.client.intercept;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
public class OAuthRequestInterceptor implements RequestInterceptor {

    @Autowired
    private OAuth2AuthorizedClientManager authorizedClientManager;

    @Override
    public void apply(RequestTemplate template) {
        template.header("Authorization", "Bearer " + getAccessToken());
    }

    private String getAccessToken() {
        return authorizedClientManager.authorize(OAuth2AuthorizeRequest
                .withClientRegistrationId("internal-client")
                .principal("internal").build())
                .getAccessToken().getTokenValue();
    }

}

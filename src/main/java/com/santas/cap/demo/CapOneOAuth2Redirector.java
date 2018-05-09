package com.santas.cap.demo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.Crypt;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@Component
class CapOneOAuth2Redirector {
    public CapOneOAuth2Redirector(CapOneAuthResourceDetails authRes,
                                  CapOneAuthAccountDetails authAccountDetails) {
        this.authRes = authRes;
        this.accountDetails = authAccountDetails;
    }

    private CapOneAuthResourceDetails authRes;
    private CapOneAuthAccountDetails accountDetails;
    private Supplier<String> cypherSupplier = () ->
            Arrays.asList("THOTH", "IBIS", "KEY", "TIME", "ETC")
                    .get((int) (Math.random() * 5));

    String getRedirectUrl() {
        new SecureRandom("paper".getBytes()).nextInt(5);
        String simpleState = Crypt.crypt(cypherSupplier.get());
        String url = "%s?client_id=%s&redirect_uri=%s&scope=signin+openid" +
                "&response_type=code&state=%s";
        return String.format(url,
                accountDetails.getAuthorizeUri(),
                authRes.getClientId(),
                accountDetails.getRedirectUri(),
                simpleState
        );
    }
}

@Configuration
@EnableOAuth2Client
class CapOneOauth2ClientConfig {
    @Bean
    @ConfigurationProperties("capone.oauth2.client")
    CapOneAuthResourceDetails capOneOAuthResource() {
        return new CapOneAuthResourceDetails();
    }

    @Bean
    @ConfigurationProperties("capone.acct")
    CapOneAuthAccountDetails capOneOAuthDetails() {
        return new CapOneAuthAccountDetails();
    }

    @Bean
    @Qualifier("capOne")
    public OAuth2ClientContext oauth2ClientContext() {
        return new DefaultOAuth2ClientContext(new DefaultAccessTokenRequest());
    }

    @Bean
    @ConfigurationProperties("capone.oauth2.client")
    @Primary
    public ClientCredentialsResourceDetails oauth2RemoteResource() {
        return new ClientCredentialsResourceDetails();
    }

    @Bean
    @Qualifier("capOne")
    public OAuth2RestTemplate oauth2RestTemplate(@Qualifier("capOne") OAuth2ClientContext authContext,
                                                 CapOneAuthResourceDetails resourceDetails) {
        return new OAuth2RestTemplate(resourceDetails, authContext);
    }
}

@Data
@NoArgsConstructor
class CapOneAuthAccountDetails {
    String authorizeUri;
    String redirectUri;
    String authUser;
    String authPassword;
}

@Data
@NoArgsConstructor
class CapOneAuthResourceDetails implements OAuth2ProtectedResourceDetails {
    String id;
    String clientId;
    String accessTokenUri;
    boolean scoped;
    List<String> scope;
    boolean authenticationRequired;
    String clientSecret;
    AuthenticationScheme clientAuthenticationScheme;
    AuthenticationScheme authenticationScheme;
    boolean clientOnly;
    String grantType;
    String tokenName;
}
package com.santas.cap.demo;

import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = SignageApp.class)
@RunWith(SpringRunner.class)
public class DemoCapOneClientTest {
    @Autowired
    CapOneOAuth2Redirector capOneOAuth2Redirector;

    @Autowired @Qualifier("capOne")
    OAuth2RestTemplate capOneRestTemplate;

    @Test
    public void shouldProveRestTemplateExists() {
        Assertions.assertThat(capOneRestTemplate).isNotNull();
        Assertions.assertThat(capOneOAuth2Redirector.getRedirectUrl()).isNotNull();
        Assertions.assertThat(capOneRestTemplate.getResource()).isNotNull();
        Assertions.assertThat(capOneRestTemplate.getOAuth2ClientContext()).isNotNull();

        OAuth2ProtectedResourceDetails capOneDeets = capOneRestTemplate.getResource();
        Assertions.assertThat(capOneDeets.getAccessTokenUri()).isNotNull();
        Assertions.assertThat(capOneDeets.getClientId()).isNotNull();
        Assertions.assertThat(capOneDeets.getClientSecret()).isNotNull();
        Assertions.assertThat(capOneDeets.getGrantType()).isNotNull();
        Assertions.assertThat(capOneDeets.getAuthenticationScheme()).isNotNull();
        Assertions.assertThat(capOneDeets.getClientAuthenticationScheme()).isNotNull();
    }

    @Test
    @SneakyThrows
    public void shouldCallCapOne() {
        OAuth2AccessToken capOneAuthToken = capOneRestTemplate.getAccessToken();

        Assertions.assertThat(capOneAuthToken).isNotNull();
        Assertions.assertThat(capOneAuthToken.getValue()).isNotNull();
        Assertions.assertThat(capOneAuthToken.getScope()).isNotNull();
    }
}
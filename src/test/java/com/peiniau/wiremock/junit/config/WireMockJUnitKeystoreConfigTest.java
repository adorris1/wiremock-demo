package com.peiniau.wiremock.junit.config;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;


public class WireMockJUnitKeystoreConfigTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig()
            .httpsPort(8443)
            .keystorePath("/path/to/keystore")
            .keystorePassword("changeit"));

}

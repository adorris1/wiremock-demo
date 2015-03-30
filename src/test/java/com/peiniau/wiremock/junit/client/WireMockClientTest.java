package com.peiniau.wiremock.junit.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WireMockClientTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Test
    public void configure_static_client() {
        WireMock.configureFor(8080);
        assertThat(true).isTrue();
    }

    @Test
    public void configure_standard_client() {
        WireMock wireMock = new WireMock(8080);
        assertThat(wireMock.allStubMappings().getMappings()).isNotNull().isEmpty();
    }

    @Test
    public void configure_direct_client() {
        WireMock wireMock = new WireMock(wireMockRule);
        assertThat(wireMock.allStubMappings().getMappings()).isNotNull().isEmpty();
    }

}

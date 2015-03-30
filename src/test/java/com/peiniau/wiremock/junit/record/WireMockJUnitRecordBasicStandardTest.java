package com.peiniau.wiremock.junit.record;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.jayway.restassured.RestAssured;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;


public class WireMockJUnitRecordBasicStandardTest {

    private static final String BASE_URL = "http://localhost:";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());

    @Test
    public void should_connect_to_server() {
        // given
        int port = wireMockRule.port();
        WireMock wireMock = new WireMock(port);
        wireMock.register(get(urlEqualTo("/welcome")).willReturn(aResponse().withStatus(200)));
        // then
        RestAssured.get(BASE_URL + port + "/welcome").then().statusCode(200);
        RestAssured.get(BASE_URL + port + "/not-welcome").then().statusCode(404);
    }

}

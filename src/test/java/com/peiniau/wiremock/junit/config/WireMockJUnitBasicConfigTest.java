package com.peiniau.wiremock.junit.config;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.jayway.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;


public class WireMockJUnitBasicConfigTest {

    private static final int PORT = 9080;
    private static final String BASE_URL = "http://localhost:" + PORT;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(PORT));

    @BeforeClass
    public static void configureDefaultClient() {
        WireMock.configureFor(PORT);
    }

    @Test
    public void should_connect_to_server() {
        // given
        stubFor(get(urlEqualTo("/welcome")).willReturn(aResponse()
                .withHeader("Content-Type", "text/plain")
                .withBody("Hello world!")));
        // then
        RestAssured.get(BASE_URL + "/welcome").then().statusCode(200);
        RestAssured.get(BASE_URL + "/not-welcome").then().statusCode(404);
    }

}

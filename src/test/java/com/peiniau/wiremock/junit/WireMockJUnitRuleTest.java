package com.peiniau.wiremock.junit;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.jayway.restassured.RestAssured;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;


public class WireMockJUnitRuleTest {

    private static final String BASE_URL = "http://localhost:8080";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

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

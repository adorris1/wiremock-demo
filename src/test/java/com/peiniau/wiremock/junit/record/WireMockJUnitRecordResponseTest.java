package com.peiniau.wiremock.junit.record;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.jayway.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class WireMockJUnitRecordResponseTest {

    private static final String BASE_URL = "http://localhost:8080";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Test
    public void should_get_exact_url_and_response_with_header_and_body() {
        // given
        stubFor(get(urlEqualTo("/welcome")).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withHeader("Cache-Control", "no-cache")
                .withBody("{\"message\": \"Welcome\"}")));
        // then
        RestAssured.get(BASE_URL + "/welcome").then()
                .statusCode(200)
                .header("Content-Type", "application/json")
                .body("message", Matchers.equalTo("Welcome"));
    }

    @Test
    public void should_get_exact_url_and_response_header_with_and_body_file() {
        // given
        stubFor(get(urlEqualTo("/welcome")).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withHeader("Cache-Control", "no-cache")
                .withBodyFile("body.json"))); // from __files directory
        // then
        RestAssured.get(BASE_URL + "/welcome").then()
                .statusCode(200)
                .header("Content-Type", "application/json")
                .body("message", Matchers.equalTo("Welcome"));
    }

}

package com.peiniau.wiremock.junit.record;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.jayway.restassured.RestAssured;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

public class WireMockJUnitRecordPriorityTest {

    private static final String BASE_URL = "http://localhost:8080";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Test
    public void should_get_with_priority() {
        // low priority
        stubFor(get(urlMatching("/welcome/.*")).atPriority(5)
                .willReturn(aResponse().withStatus(401)));
        // high priority
        stubFor(get(urlEqualTo("/welcome/authorized")).atPriority(1)
                .willReturn(aResponse().withStatus(200)));
        // then
        RestAssured.get(BASE_URL + "/welcome/").then().statusCode(401);
        RestAssured.get(BASE_URL + "/welcome/not-authorized").then().statusCode(401);
        RestAssured.get(BASE_URL + "/welcome/authorized").then().statusCode(200);
    }

}

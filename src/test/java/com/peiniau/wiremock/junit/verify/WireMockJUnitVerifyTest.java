package com.peiniau.wiremock.junit.verify;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.jayway.restassured.RestAssured;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.findAll;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;

public class WireMockJUnitVerifyTest {

    private static final String BASE_URL = "http://localhost:8080";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Test
    public void should_verify_get() {
        // given
        stubFor(get(urlEqualTo("/welcome")).willReturn(aResponse().withStatus(200)));
        // when
        RestAssured.get(BASE_URL + "/welcome");
        // then
        verify(getRequestedFor(urlEqualTo("/welcome")));
    }

    @Test
    public void should_verify_get_twice() {
        // given
        stubFor(get(urlEqualTo("/welcome")).willReturn(aResponse().withStatus(200)));
        // when
        RestAssured.get(BASE_URL + "/welcome");
        RestAssured.get(BASE_URL + "/welcome");
        // then
        verify(2, getRequestedFor(urlEqualTo("/welcome")));
    }

    @Test
    public void should_verify_multiple_requests() {
        // given
        stubFor(get(urlMatching("/welcome/.*")).willReturn(aResponse().withStatus(201)));
        // when
        RestAssured.get(BASE_URL + "/welcome/one");
        RestAssured.get(BASE_URL + "/welcome/two");
        RestAssured.get(BASE_URL + "/welcome/tree");
        // then
        verify(3, getRequestedFor(urlMatching("/welcome/.*")));
        verify(getRequestedFor(urlEqualTo("/welcome/one")));
    }

    @Test
    public void should_verify_with_find_all() {
        // given
        stubFor(get(urlMatching("/welcome/.*")).willReturn(aResponse().withStatus(201)));
        // when
        RestAssured.get(BASE_URL + "/welcome/one");
        RestAssured.get(BASE_URL + "/welcome/two");
        RestAssured.get(BASE_URL + "/welcome/tree");
        // then
        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/welcome/.*")));
        assertThat(requests).hasSize(3)
                .extracting("url")
                .contains("/welcome/one", "/welcome/two", "/welcome/tree");
    }

    @Test
    public void should_verify_put_with_body() {
        // given
        stubFor(put(urlEqualTo("/welcome/1"))
                .withRequestBody(matchingJsonPath("$.name"))
                .willReturn(aResponse().withStatus(201)));
        // when
        RestAssured
                .given().body("{\"name\": \"etienne\"}")
                .when().put(BASE_URL + "/welcome/1");
        // then
        verify(putRequestedFor(urlEqualTo("/welcome/1"))
                .withRequestBody(equalToJson("{\"name\": \"etienne\"}")));
    }

}

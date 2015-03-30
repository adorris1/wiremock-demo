package com.peiniau.wiremock.junit.fault;

import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.jayway.restassured.RestAssured;
import org.apache.http.MalformedChunkCodingException;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

import java.net.SocketTimeoutException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.jayway.restassured.config.HttpClientConfig.httpClientConfig;
import static com.jayway.restassured.config.RestAssuredConfig.newConfig;

public class WireMockJUnitFaultTest {

    private static final String BASE_URL = "http://localhost:8080";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Test
    public void should_return_5xx_status() {
        // given
        stubFor(get(urlEqualTo("/error-welcome")).willReturn(aResponse().withStatus(500)));
        // then
        RestAssured.get(BASE_URL + "/error-welcome").then().statusCode(500);
    }

    @Test(expected = SocketTimeoutException.class)
    public void should_socket_timeout() {
        // given
        stubFor(get(urlEqualTo("/delayed-welcome")).willReturn(aResponse()
                .withStatus(200)
                .withFixedDelay(3000)));
        // timeout setup
        RestAssured.config = newConfig()
                .httpClient(httpClientConfig()
                        .setParam("http.socket.timeout", 2000));
        // then
        try {
            RestAssured.get(BASE_URL + "/delayed-welcome").then().statusCode(200);
        } finally {
            RestAssured.reset();
        }
    }

    @Test(expected = NoHttpResponseException.class)
    public void should_return_empty_response() {
        // given
        stubFor(get(urlEqualTo("/empty-welcome")).willReturn(aResponse()
                .withStatus(200)
                .withFault(Fault.EMPTY_RESPONSE)));
        // then
        RestAssured.get(BASE_URL + "/empty-welcome").then().statusCode(200);
    }

    @Test(expected = ClientProtocolException.class)
    public void should_return_random_data() {
        // given
        stubFor(get(urlEqualTo("/random-welcome")).willReturn(aResponse()
                .withStatus(200)
                .withFault(Fault.RANDOM_DATA_THEN_CLOSE)));
        // then
        RestAssured.get(BASE_URL + "/random-welcome").then().statusCode(200);
    }

    @Test(expected = MalformedChunkCodingException.class)
    public void should_return_malformed_response() {
        // given
        stubFor(get(urlEqualTo("/malformed-welcome")).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"message\": \"Will be overwritten\"}")
                .withFault(Fault.MALFORMED_RESPONSE_CHUNK)));
        // then
        RestAssured.get(BASE_URL + "/malformed-welcome").then()
                .statusCode(200)
                .body("message", Matchers.equalTo("Will be overwritten"));
    }

}

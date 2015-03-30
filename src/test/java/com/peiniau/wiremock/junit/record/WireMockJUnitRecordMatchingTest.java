package com.peiniau.wiremock.junit.record;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Header;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingXPath;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

public class WireMockJUnitRecordMatchingTest {

    private static final String BASE_URL = "http://localhost:8080";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Test
    public void should_get_exact_url() {
        // given
        stubFor(get(urlEqualTo("/welcome")).willReturn(aResponse().withStatus(200)));
        // then
        RestAssured.get(BASE_URL + "/welcome").then().statusCode(200);
        RestAssured.get(BASE_URL + "/not-welcome").then().statusCode(404);
        RestAssured.get(BASE_URL + "/welcome?name=etienne").then().statusCode(404);
    }

    @Test
    public void should_put_exact_url() {
        // given
        stubFor(put(urlEqualTo("/welcome")).willReturn(aResponse().withStatus(201)));
        // then
        RestAssured.put(BASE_URL + "/welcome").then().statusCode(201);
        RestAssured.put(BASE_URL + "/not-welcome").then().statusCode(404);
    }

    @Test
    public void should_any_exact_url() {
        // given
        stubFor(any(urlEqualTo("/welcome")).willReturn(aResponse().withStatus(200)));
        // then
        RestAssured.get(BASE_URL + "/welcome").then().statusCode(200);
        RestAssured.post(BASE_URL + "/welcome").then().statusCode(200);
        RestAssured.put(BASE_URL + "/welcome").then().statusCode(200);
        RestAssured.delete(BASE_URL + "/welcome").then().statusCode(200);
        RestAssured.head(BASE_URL + "/welcome").then().statusCode(200);
        RestAssured.options(BASE_URL + "/welcome").then().statusCode(200);
        RestAssured.patch(BASE_URL + "/welcome").then().statusCode(200);
    }

    @Test
    public void should_match_request_with_variable_prefix() {
        // given
        stubFor(get(urlMatching("/(not-)?welcome")).willReturn(aResponse().withStatus(200)));
        // then
        RestAssured.get(BASE_URL + "/welcome").then().statusCode(200);
        RestAssured.get(BASE_URL + "/not-welcome").then().statusCode(200);
    }

    @Test
    public void should_match_request_with_variable_suffix() {
        // given
        stubFor(put(urlMatching("/welcome/[1-9]+")).willReturn(aResponse().withStatus(201)));
        // then
        RestAssured.put(BASE_URL + "/welcome/1").then().statusCode(201);
        RestAssured.put(BASE_URL + "/welcome/42").then().statusCode(201);
    }

    @Test
    public void should_get_url_path() {
        // given
        stubFor(get(urlPathEqualTo("/welcome")).willReturn(aResponse().withStatus(200)));
        // then
        RestAssured.get(BASE_URL + "/welcome").then().statusCode(200);
        RestAssured.get(BASE_URL + "/welcome?name=etienne").then().statusCode(200);
    }

    @Test
    public void should_get_exact_url_with_exact_header() {
        // given
        stubFor(get(urlEqualTo("/welcome"))
                .withHeader("Accept", equalTo("application/json"))
                .willReturn(aResponse().withStatus(200)));
        // then
        RestAssured
                .given().header(new Header("Accept", "application/json"))
                .when().get(BASE_URL + "/welcome")
                .then().statusCode(200);
    }

    @Test
    public void should_get_url_path_with_exact_query_param() {
        // given
        stubFor(get(urlPathEqualTo("/welcome"))
                .withQueryParam("name", equalTo("etienne"))
                .willReturn(aResponse().withStatus(200)));
        // then
        RestAssured.get(BASE_URL + "/welcome?name=etienne").then().statusCode(200);
        RestAssured.get(BASE_URL + "/welcome?name=thomas").then().statusCode(404);
    }

    @Test
    public void should_get_url_path_with_query_param_matching() {
        // given
        stubFor(get(urlPathEqualTo("/welcome"))
                .withQueryParam("name", matching("[a-z]+"))
                .willReturn(aResponse().withStatus(200)));
        // then
        RestAssured.get(BASE_URL + "/welcome?name=etienne").then().statusCode(200);
        RestAssured.get(BASE_URL + "/welcome?name=thomas").then().statusCode(200);
        RestAssured.get(BASE_URL + "/welcome?name=Etienne").then().statusCode(404);
    }

    @Test
    public void should_put_exact_url_with_json_path_matching() {
        // given
        stubFor(put(urlEqualTo("/welcome/1"))
                .withRequestBody(matchingJsonPath("$.name"))
                .willReturn(aResponse().withStatus(201)));
        // then
        RestAssured
                .given().body("{\"name\": \"etienne\"}")
                .when().put(BASE_URL + "/welcome/1")
                .then().statusCode(201);
    }

    @Test
    public void should_post_exact_url_with_xpath_matching() {
        // given
        stubFor(post(urlEqualTo("/welcome"))
                .withRequestBody(matchingXPath("/xml/name"))
                .willReturn(aResponse().withStatus(201)));
        // then
        RestAssured
                .given().body("<xml><name>etienne</name></xml>")
                .when().post(BASE_URL + "/welcome")
                .then().statusCode(201);
    }

}

package com.peiniau.wiremock.junit.record;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import com.jayway.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class WireMockJunitRecordStatefullTest {

    private static final String WELCOME_SCENARIO = "WELCOME";
    private static final String STATE_WELCOME_DELETED = "WELCOME_DELETED";

    private static final String BASE_URL = "http://localhost:8080";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Test
    public void should_delete_welcome() {
        // given
        stubFor(get(urlEqualTo("/welcome")).inScenario(WELCOME_SCENARIO)
                .whenScenarioStateIs(Scenario.STARTED)
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"message\": \"Welcome\"}")));

        stubFor(delete(urlEqualTo("/welcome")).inScenario(WELCOME_SCENARIO)
                .whenScenarioStateIs(Scenario.STARTED)
                .willReturn(aResponse().withStatus(204))
                .willSetStateTo(STATE_WELCOME_DELETED));

        stubFor(get(urlEqualTo("/welcome")).inScenario(WELCOME_SCENARIO)
                .whenScenarioStateIs(STATE_WELCOME_DELETED)
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"message\": \"Not welcome\"}")));
        // then
        RestAssured.get(BASE_URL + "/welcome").then()
                .statusCode(200)
                .body("message", Matchers.equalTo("Welcome"));
        RestAssured.delete(BASE_URL + "/welcome").then()
                .statusCode(204);
        RestAssured.get(BASE_URL + "/welcome").then()
                .statusCode(200)
                .body("message", Matchers.equalTo("Not welcome"));
    }

}

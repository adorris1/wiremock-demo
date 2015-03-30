package com.peiniau.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.saveAllMappings;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class WireMockPrototypeProxy {

    public static void main(String[] args) {
        // Create the server
        WireMockServer wireMockServer = new WireMockServer(wireMockConfig()
                .withRootDirectory("/path/to/root"));
        wireMockServer.start();
        // Low priority catch-all proxies to the real API
        stubFor(get(urlMatching("/welcome/.*")).atPriority(100)
                .willReturn(aResponse().proxiedFrom("http://welcome.web")));
        // Add the mapping for the new service
        stubFor(get(urlEqualTo("/welcome/new/service")).atPriority(1)
                .willReturn(aResponse().withStatus(200)));
        // Save the mappings in the root directory
        saveAllMappings();
        // Stop the server
        wireMockServer.stop();
    }

}

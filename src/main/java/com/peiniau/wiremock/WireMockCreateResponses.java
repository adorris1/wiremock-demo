package com.peiniau.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.saveAllMappings;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class WireMockCreateResponses {

    public static void main(String[] args) {
        // Create the server
        WireMockServer wireMockServer = new WireMockServer(wireMockConfig()
                .withRootDirectory("/path/to/root"));
        wireMockServer.start();
        // Use static client to create responses
        for (int i = 0; i < 10; i++) {
            stubFor(WireMock.get(WireMock.urlEqualTo("/welcome-" + i)).willReturn(WireMock.aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("{\"message\": \"Welcome\"}")));
        }
        // Save the mappings in the root directory
        saveAllMappings();
        // Stop the server
        wireMockServer.stop();
    }

}

package com.egdbag.covid.bot.maps.yandex;

import com.egdbag.covid.bot.maps.yandex.model.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * Class for http requests to yandex maps API
 */
public class HttpRequester
{
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequester.class);

    private final HttpClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public HttpRequester()
    {
        client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();
    }

    /**
     * Requests organisations by the specified URL
     * @param url url to send GET request to, cannot be {@code null}
     * @return {@link CompletableFuture} with {@link Response}, never {@code null}
     */
    public CompletableFuture<Response> getOrganisations(String url)
    {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofMinutes(1))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(body -> {
                    try {
                        return mapper.readValue(body, Response.class);
                    } catch (JsonProcessingException e) {
                        LOGGER.error(e.getMessage(), e);
                        return null;
                    }
                }).exceptionally(e -> {
                    LOGGER.error(e.getMessage(), e);
                    return null;
                });
    }

    /**
     * Requests info of the coordinates by the specified URL
     * @param url url to send GET request to, cannot be {@code null}
     * @return {@link CompletableFuture} with {@link JsonNode}, never {@code null}
     */
    public CompletableFuture<JsonNode> getCoordinatesInfo(String url)
    {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofMinutes(1))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(body -> {
                    try {
                        return mapper.readValue(body, JsonNode.class);
                    } catch (JsonProcessingException e) {
                        LOGGER.error(e.getMessage(), e);
                        return null;
                    }
                }).exceptionally(e -> {
                    LOGGER.error(e.getMessage(), e);
                    return null;
                });
    }
}

package com.egdbag.covid.bot.registry.cases.debug;

import com.egdbag.covid.bot.maps.yandex.model.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class HttpRequester
{
    private static final Logger LOGGER = LoggerFactory.getLogger(com.egdbag.covid.bot.maps.yandex.HttpRequester.class);
    private static final String MASH_URL = "https://coronavirus.mash.ru/data.json";

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
     * Requests data on covid cases
     * @return {@link CompletableFuture} with {@link Response}, never {@code null}
     */
    public CompletableFuture<Response> getDiseaseCases()
    {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MASH_URL))
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
}

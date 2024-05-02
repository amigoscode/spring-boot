package com.amigoscode.jsonplaceholder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class JSONPlaceHolderConfig {

    @Bean("rest-client")
    JsonPlaceholderService jsonPlaceHolderRestClientService() {
        RestClient restClient = RestClient.create(
                "https://jsonplaceholder.typicode.com"
        );
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build()
                .createClient(JsonPlaceholderService.class);
    }

    @Primary
    @Bean("web-client")
    JsonPlaceholderService jsonPlaceHolderWebClientService() {
        WebClient restClient = WebClient.builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .defaultStatusHandler(HttpStatusCode::isError, clientResponse -> {
                    throw new ResponseStatusException(clientResponse.statusCode());
                }).build();

        return HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(restClient))
                .build()
                .createClient(JsonPlaceholderService.class);
    }

}

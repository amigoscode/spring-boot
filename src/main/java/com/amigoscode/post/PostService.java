package com.amigoscode.post;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class PostService {

    private final RestClient restClient;
    private final WebClient webClient;

    public PostService() {
        String baseUrl = "https://jsonplaceholder.typicode.com";
        this.restClient = RestClient.create(baseUrl);
        this.webClient = WebClient.create(baseUrl);
    }

    // using web client
    public Flux<List<Post>> getAllPosts() {
        return webClient.get().uri("/posts")
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<List<Post>>() {});
    }

    // using web client
    public Mono<Post> getPostById(Integer id) {
        return webClient.get().uri("/posts/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (response) -> {
                    throw new ResponseStatusException(
                            response.statusCode()
                    );
                })
                .bodyToMono(Post.class);
    }

    // using rest client
    public void createPost(Post post) {
        restClient.post()
                .uri("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .body(post)
                .retrieve()
                .toBodilessEntity();
    }

    // using rest client
    public void updatePost(Integer id, Post post) {
        restClient.put()
                .uri("/posts/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(post)
                .retrieve()
                .toBodilessEntity();
    }

    // using rest client
    public void deletePostById(Integer id) {
        restClient.delete()
                .uri("/posts/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }
}

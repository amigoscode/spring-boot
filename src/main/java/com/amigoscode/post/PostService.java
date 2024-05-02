package com.amigoscode.post;

import com.amigoscode.jsonplaceholder.JsonPlaceholderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PostService {

    private final RestClient restClient;

    public PostService(JsonPlaceholderService jsonPlaceholderService) {
        this.restClient = RestClient.create(
                "https://jsonplaceholder.typicode.com"
        );
    }

    public List<Post> getAllPosts() {
        return restClient.get().uri("/posts")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Post>>() {});
    }

    public Post getPostById(Integer id) {
        return restClient.get().uri("/posts/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new ResponseStatusException(
                            response.getStatusCode()
                    );
                })
                .body(Post.class);
    }

    public void createPost(Post post) {
        restClient.post()
                .uri("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .body(post)
                .retrieve()
                .toBodilessEntity();
    }

    public void updatePost(Integer id, Post post) {
        restClient.put()
                .uri("/posts/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(post)
                .retrieve()
                .toBodilessEntity();
    }

    public void deletePostById(Integer id) {
        restClient.delete()
                .uri("/posts/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }
}

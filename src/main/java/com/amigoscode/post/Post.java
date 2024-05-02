package com.amigoscode.post;

public record Post(
        Integer id,
        Integer userId,
        String title,
        String body
) {
}

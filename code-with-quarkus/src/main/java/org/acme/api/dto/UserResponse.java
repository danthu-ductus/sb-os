package org.acme.api.dto;

import java.time.Instant;

import org.acme.persistence.entity.User;

public class UserResponse {
    public Long userId;
    public String username;
    public Instant createdAt;

    public static UserResponse fromEntity(User u) {
        UserResponse resp = new UserResponse();
        resp.userId = u.id;
        resp.username = u.username;
        resp.createdAt = u.createdAt;
        return resp;
    } 
}

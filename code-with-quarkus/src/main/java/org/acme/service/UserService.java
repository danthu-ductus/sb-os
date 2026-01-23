package org.acme.service;

import org.acme.api.dto.UserRequest;
import org.acme.persistence.entity.User;
import org.acme.persistence.repository.UserRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UserService {
    
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public User create(UserRequest req) {
        String reqName = req.username; 
        
        if (reqName == null || reqName.isBlank()) {
            throw new BadRequestException("Invalid Username");
        }
        if (repo.userAlrdyExists(reqName)) {
            throw new BadRequestException("Username already exists");
        }
        
        User user = new User();
        user.username = reqName;
        repo.persist(user);
        return user; 
    }

    public Long getUserIdByName(String username) {
        if (username == null || username.isBlank()) {
            throw new BadRequestException("Invalid Username");
        }

        return repo.queryForUserId(username)
            .orElseThrow(() -> new NotFoundException("User does not exist")); 
    }

}

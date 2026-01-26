package org.acme.persistence.repository;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

import org.acme.persistence.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User>{
    
    public boolean userAlrdyExists(String username) {
        return count("username", username) > 0;
    }


    public Optional<Long> queryForUserId(String username) {
        return find("SELECT u.id FROM User u WHERE u.username = ?1", username)
            .project(Long.class)
            .firstResultOptional();
    }

}

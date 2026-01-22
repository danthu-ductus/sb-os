package org.acme.persistence.repository;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.persistence.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User>{
    public boolean userAlrdyExists(String username) {
        return count("username", username) > 0;
    }
}

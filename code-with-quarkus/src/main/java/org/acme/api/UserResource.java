package org.acme.api;

import org.acme.api.dto.UserRequest;
import org.acme.api.dto.UserResponse;
import org.acme.service.UserService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserService service;

    public UserResource(UserService service) {
        this.service = service;
    }

    @POST
    public UserResponse createUser(UserRequest req) {
        return UserResponse.UserResponsFromEntity(service.create(req));
    }

}

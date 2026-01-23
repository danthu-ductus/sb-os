package org.acme.api;

import org.acme.api.dto.UserRequest;
import org.acme.api.dto.UserResponse;
import org.acme.service.UserService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
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
    @Path("/createJson")
    @Operation(
        summary = "post a user to the db using JSON request body",
        description = "the firs simple endpoint"
    )
    public UserResponse createUser(UserRequest req) {
        return UserResponse.UserResponsFromEntity(service.create(req));
    }

    @POST
    @Path("/createParam")
    @Operation(
        summary = "post a user to the db using this param dependent endpoint instead of a JSON request body",
        description = "can add descriptions as well"
    )
    public UserResponse createUserWParam(
        @Parameter(
            description = "a unique username",
            required = true,
            example = "jacobTest"
        )
        @QueryParam("username") String username
    ) {
        UserRequest req = new UserRequest();
        req.username = username;
        return UserResponse.UserResponsFromEntity(service.create(req));
        
    }

}

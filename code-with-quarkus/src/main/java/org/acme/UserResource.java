package org.acme;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    // POST a new user to the database
    @POST
    @Transactional
    public User createUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new WebApplicationException("Name is required", 400);
        }
        user.persist();  // <-- save to DB
        return user;
    }

    // GET all users from the database
    @GET
    public List<User> getUsers() {
        return User.listAll(); // <-- fetch all from DB
    }

    @GET
    @Path("/id/{name}")
    public Long getIdByName(@PathParam("name") String name) {
        User user = User.find("name", name).firstResult();
        if (user == null) {
            throw new WebApplicationException("User not found", 404);
        }
        return user.getId();
    }
}

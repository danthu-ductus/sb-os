package org.acme;   // <- matchar mappen org/acme

import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/user")
public class UserResource {

    private static List<String> users = new ArrayList<>();

    @GET
    public String createUser(@QueryParam("name") String name) {
        if (name == null || name.isEmpty()) {
            return "Provide a name";
        }

        // Kolla om namnet redan finns
        for (String user : users) {
            if (user.equalsIgnoreCase(name)) {
                return "User already exists: " + name;
            }
        }

        // Lägg till nytt namn
        users.add(name);
        return "User created: " + name + ". Total users: " + users.size();
    }
}

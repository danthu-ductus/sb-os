package org.acme.api.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class UserRequest {
    @Schema(required = true, examples = {"jacob", "Daniel"}, description = "unique username")
    public String username;
}

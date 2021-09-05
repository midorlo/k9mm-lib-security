package com.midorlo.k9.model.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JsonWebToken {

    @JsonProperty("id_token")
    private final String idToken;
}

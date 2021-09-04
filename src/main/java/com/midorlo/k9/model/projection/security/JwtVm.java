package com.midorlo.k9.model.projection.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtVm {

    @JsonProperty("id_token")
    private String idToken;
}

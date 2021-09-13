package com.midorlo.k9.web.rest.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Used to deliver a response body after successful login.
 */
@Data
@AllArgsConstructor
public class JwtVm {

    @JsonProperty("id_token")
    private String idToken;
}

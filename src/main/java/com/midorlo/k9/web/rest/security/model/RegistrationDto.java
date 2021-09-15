package com.midorlo.k9.web.rest.security.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RegistrationDto {

    protected String displayName;
    protected String login;
    protected String password;
    protected String firstName;
    protected String lastName;
}

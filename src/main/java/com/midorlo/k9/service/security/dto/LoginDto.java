package com.midorlo.k9.service.security.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String login;
    private String password;
    private boolean remember;
}

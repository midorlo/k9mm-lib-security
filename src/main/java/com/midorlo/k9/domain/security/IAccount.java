package com.midorlo.k9.domain.security;

import org.springframework.data.domain.Auditable;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;
import java.util.Date;

public interface IAccount extends Persistable<Long>, Auditable<IAccount, Long, LocalDateTime>  {
    String getEmail();

    String getPassword();

    void setEmail(String email);

    void setPassword(String password);
}

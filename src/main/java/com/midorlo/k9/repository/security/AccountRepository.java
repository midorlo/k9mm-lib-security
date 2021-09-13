package com.midorlo.k9.repository.security;

import com.midorlo.k9.domain.security.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Transactional(propagation = Propagation.REQUIRES_NEW)

    List<Account> findByLogin(String email);

    Optional<Account> findAccountByLogin(String login);
}

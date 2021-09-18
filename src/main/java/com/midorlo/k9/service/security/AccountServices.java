package com.midorlo.k9.service.security;

import com.midorlo.k9.domain.core.Person;
import com.midorlo.k9.domain.security.Account;
import com.midorlo.k9.domain.security.property.AccountState;
import com.midorlo.k9.repository.security.AccountRepository;
import com.midorlo.k9.service.AbstractCrudService;
import com.midorlo.k9.web.rest.security.model.RegistrationDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServices extends AbstractCrudService<Account, Long, AccountRepository> {

    private final PasswordEncoder passwordEncoder;


    public AccountServices(AccountRepository repository,
                           PasswordEncoder passwordEncoder) {
        super(repository);
        this.passwordEncoder = passwordEncoder;
    }

    public Account registerNewAccount(RegistrationDto registrationDto) {
        Account account = new Account();
        account.setDisplayName(registrationDto.getDisplayName());
        account.setLogin(registrationDto.getLogin());
        account.setPasswordHash(passwordEncoder.encode(registrationDto.getPassword()));
        account.setState(AccountState.ACTIVATED);
        Person person = new Person();
        person.setFirstName(registrationDto.getFirstName());
        person.setLastName(registrationDto.getLastName());
        account.setOwner(person);
        return repository.save(account);
    }
}

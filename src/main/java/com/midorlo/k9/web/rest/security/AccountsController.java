package com.midorlo.k9.web.rest.security;

import com.midorlo.k9.domain.security.Account;
import com.midorlo.k9.repository.security.AccountRepository;
import com.midorlo.k9.service.security.AccountServices;
import com.midorlo.k9.web.problem.EmailAlreadyUsedException;
import com.midorlo.k9.web.problem.InvalidPasswordException;
import com.midorlo.k9.web.problem.LoginAlreadyUsedException;
import com.midorlo.k9.web.rest.DefaultCrudController;
import com.midorlo.k9.web.rest.security.model.RegistrationDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/accounts")
public class AccountsController extends DefaultCrudController<Account, Long, AccountRepository, AccountServices> {

    protected AccountsController(AccountServices service) {
        super(service);
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param registrationDto the managed user View Model.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody RegistrationDto registrationDto) {
        Account account = service.registerNewAccount(registrationDto);
    }
}

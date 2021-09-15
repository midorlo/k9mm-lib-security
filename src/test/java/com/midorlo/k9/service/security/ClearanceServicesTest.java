package com.midorlo.k9.service.security;

import com.midorlo.k9.IntegrationTest;
import com.midorlo.k9.domain.security.Clearance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@Transactional
class ClearanceServicesTest {

    @Autowired
    private ClearanceServices clearanceServices;

    @MockBean
    private DateTimeProvider dateTimeProvider;

    private Clearance clearance;

    @BeforeEach
    public void init() {
        clearance = new Clearance();
        clearance.setPath("/");
    }

    @Test
    @Transactional
    void assertThatClearancesCanBeStored() {
        Clearance           storedClearance = clearanceServices.create(clearance);
        Optional<Clearance> byId            = clearanceServices.findById(storedClearance.getId());
        assertThat(byId).isPresent();
    }
}

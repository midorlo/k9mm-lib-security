package com.midorlo.k9.domain.security.property;

/**
 * Possible States of an {@link com.midorlo.k9.domain.security.Account}
 */
public enum AccountState {

    /**
     * Alive.
     */
    ACTIVATED(0),

    /**
     * Newly registered.
     */
    INACTIVE_REQUIRES_EMAIL(1),

    /**
     * Within a 2fa flow.
     */
    INACTIVE_REQUIRES_PASSWORD(2),

    /**
     * Planned revocation.
     */
    INACTIVE_EXPIRED(3),

    /**
     * User disabled his account.
     */
    INACTIVE_DISABLED(4),

    /**
     * Operator disabled this account.
     */
    INACTIVE_SANCTIONED(5),

    /**
     * Scheduler disabled this account.
     */
    INACTIVE_FORCED(6),

    /**
     * Account disabled and frozen on behalf of a law enforcement agency.
     */
    INACTIVE_LEA(7);

    private final Integer value;

    AccountState(Integer value) {
        this.value = value;
    }

    @SuppressWarnings("unused")
    public Integer getValue() {
        return this.value;
    }
}

package com.midorlo.k9.domain.security.property;

public enum AccountState {

    /**
     * Alive.
     */
    ACTIVE(0),

    /**
     * Newly registered.
     */
    ACTIVATION_REQUIRED(1),

    /**
     * Within a 2fa flow.
     */
    EXPIRED_PASSWORD(2),

    /**
     * Planned revocation.
     */
    EXPIRED_SUBSCRIPTION(3),

    /**
     * User disabled his account.
     */
    DISABLED(4),

    /**
     * Operator disabled this account.
     */
    SANCTIONED(5),

    /**
     * Scheduler disabled this account.
     */
    BANNED(6),

    /**
     * Account disabled and frozen on behalf of a law enforcement agency.
     */
    LEA(7);

    private final Integer value;

    AccountState(Integer value) {
        this.value = value;
    }

    @SuppressWarnings("unused")
    public Integer getValue() {
        return this.value;
    }
}

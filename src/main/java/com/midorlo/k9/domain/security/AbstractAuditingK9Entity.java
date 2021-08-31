package com.midorlo.k9.domain.security;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.AbstractAuditable;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@NoArgsConstructor
public abstract class AbstractAuditingK9Entity extends AbstractAuditable<Account, Long> {

    /**
     * <p>Implement {@link #hashCode()} for all definitions under this.</p>
     * {@inheritDoc}
     *
     * @return object hash.
     * @see <a href="https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/">
     * Howto implement equals and hashcode for entities"
     * </a>
     * <p>
     */
    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }


    /**
     * Force an {@link #equals(Object)} implementation.
     */
    public abstract boolean equals(Object o);
}

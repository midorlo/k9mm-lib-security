package com.midorlo.k9.domain.security.util;

import com.midorlo.k9.domain.security.Account;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractAuditable;
import org.springframework.lang.NonNull;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@NoArgsConstructor
public abstract class AuditorAwareK9Entity extends AbstractAuditable<Account, Long> {

    private static final long serialVersionUID = 141481953116476081L;

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

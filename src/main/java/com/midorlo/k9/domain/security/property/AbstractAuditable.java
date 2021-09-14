package com.midorlo.k9.domain.security.property;

import com.midorlo.k9.domain.security.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Auditable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

/**
 * Mapped superclass providing auditing capabilities.
 *
 * @param <PK> any {@link Serializable} class.
 */

@Getter
@Setter
@NoArgsConstructor

@MappedSuperclass
public abstract class AbstractAuditable<PK extends Serializable> implements Serializable,
                                                                            Auditable<Account, PK, LocalDateTime> {

    private static final long serialVersionUID = 1L;

    @Nullable @ManyToOne                         private Account createdBy;
    @Nullable @ManyToOne                         private Account lastModifiedBy;
    @Nullable @Temporal(TemporalType.TIMESTAMP)  private Date    createdDate;
    @Nullable @Temporal(TemporalType.TIMESTAMP)  private Date    lastModifiedDate;

    @Override
    public boolean isNew() {
        return getId() == null;
    }

    /**
     * @see <a href="https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/">
     * Howto implement equals and hashcode for entities" </a>
     */
    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }

    //<editor-fold desc="Get/Set">
    /**
     * @see org.springframework.data.domain.Auditable#getCreatedBy()
     */
    @Override
    @NonNull
    public Optional<Account> getCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    /**
     * @see org.springframework.data.domain.Auditable#setCreatedBy(java.lang.Object)
     */
    @Override
    public void setCreatedBy(@NonNull Account createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @see org.springframework.data.domain.Auditable#getCreatedDate()
     */
    @Override
    @NonNull
    public Optional<LocalDateTime> getCreatedDate() {
        return null == createdDate ? Optional.empty()
                                   : Optional.of(LocalDateTime.ofInstant(createdDate.toInstant(),
                                                                         ZoneId.systemDefault()));
    }

    /**
     * @see org.springframework.data.domain.Auditable#setCreatedDate(java.time.temporal.TemporalAccessor)
     */
    @Override
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = Date.from(createdDate.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * @see org.springframework.data.domain.Auditable#getLastModifiedBy()
     */
    @Override
    @NonNull
    public Optional<Account> getLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    /**
     * @see org.springframework.data.domain.Auditable#setLastModifiedBy(java.lang.Object)
     */
    @Override
    public void setLastModifiedBy(@NonNull Account lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    /**
     * @see org.springframework.data.domain.Auditable#getLastModifiedDate()
     */
    @Override
    @NonNull
    public Optional<LocalDateTime> getLastModifiedDate() {
        return null == lastModifiedDate ? Optional.empty()
                                        : Optional.of(LocalDateTime.ofInstant(lastModifiedDate.toInstant(),
                                                                              ZoneId.systemDefault()));
    }

    /**
     * @see org.springframework.data.domain.Auditable#setLastModifiedDate(java.time.temporal.TemporalAccessor)
     */
    @Override
    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = Date.from(lastModifiedDate.atZone(ZoneId.systemDefault()).toInstant());
    }
    //</editor-fold>
}

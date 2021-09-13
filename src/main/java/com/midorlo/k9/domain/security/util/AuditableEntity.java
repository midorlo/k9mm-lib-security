package com.midorlo.k9.domain.security.util;


import com.midorlo.k9.domain.security.IAccount;
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
 * <p>Abstract base class for auditable entities. Stores the audition values in persistent fields.</p>
 * <p>Variation of {@link org.springframework.data.jpa.domain.AbstractAuditable} having no own id field.</p>
 *
 * @param <PK> the primary key Type of the extending entity.
 */
@MappedSuperclass
public abstract class AuditableEntity<PK extends Serializable> implements Auditable<IAccount, PK, LocalDateTime> {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @Nullable
    private IAccount createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Nullable
    private Date createdDate;

    @ManyToOne
    private IAccount lastModifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    /**
     * @see org.springframework.data.domain.Auditable#getCreatedBy()
     */
    @Override
    @NonNull
    public Optional<IAccount> getCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    /**
     * @see org.springframework.data.domain.Auditable#setCreatedBy(java.lang.Object)
     */
    @Override
    @NonNull
    public void setCreatedBy(@NonNull IAccount createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * (non-Javadoc)
     *
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
    @NonNull
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = Date.from(createdDate.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * @see org.springframework.data.domain.Auditable#getLastModifiedBy()
     */
    @Override
    @NonNull
    public Optional<IAccount> getLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    /**
     * @see org.springframework.data.domain.Auditable#setLastModifiedBy(java.lang.Object)
     */
    @Override
    @NonNull
    public void setLastModifiedBy(@NonNull IAccount lastModifiedBy) {
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
    @NonNull
    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = Date.from(lastModifiedDate.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public boolean isNew() {
        return getId() == null;
    }
}

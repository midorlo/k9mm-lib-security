package com.midorlo.k9.domain.security;

import com.midorlo.k9.domain.core.ServletDescription;
import com.midorlo.k9.domain.security.util.AuditorAwareK9Entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.springframework.http.HttpMethod;

import javax.persistence.*;

import static com.midorlo.k9.domain.security.Authority.Meta.*;
import static org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE;

@Getter
@Setter
@RequiredArgsConstructor
@Entity(name = ENTITY_NAME)
@Cache(usage = NONSTRICT_READ_WRITE)
public class Authority extends AuditorAwareK9Entity {

    private static final long serialVersionUID = 1L;

    static class Meta {
        static final String ENTITY_NAME      = "authorities";
        static final String HTTP_METHOD      = "http_method";
        static final String ID_REST_METADATA = "id_rest_metadata";
    }

    @Enumerated(EnumType.STRING)
    @Column(name = HTTP_METHOD, nullable = false, updatable = false)
    private HttpMethod method;

    @JoinColumn(name = ID_REST_METADATA, nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER,
               cascade = { CascadeType.MERGE })
    private ServletDescription servletDescription;

    public Authority(HttpMethod method, ServletDescription servletDescription) {
        this.method             = method;
        this.servletDescription = servletDescription;
    }
}

package com.midorlo.k9.domain.security;

import com.midorlo.k9.domain.security.util.AuditorAwareK9Entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpMethod;

import javax.persistence.*;

import static com.midorlo.k9.domain.security.Authority.Meta.*;

@Getter
@Setter
@RequiredArgsConstructor
@Entity(name = ENTITY_NAME)
public class Authority extends AuditorAwareK9Entity {

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
               cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH })
    private RestResourceMetadata restResourceMetadata;

    public Authority(HttpMethod method, RestResourceMetadata restResourceMetadata) {
        this.method               = method;
        this.restResourceMetadata = restResourceMetadata;
    }
}

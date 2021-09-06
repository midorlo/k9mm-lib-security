package com.midorlo.k9.util.security;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.MessageFormat;
import java.util.Date;
import java.util.EnumSet;

public class RestSecurityUtilities {

    public static final String HEADER_NAME = "Authorization";
    public static final String HEADER_LINKS_FORMATTER = "<{0}>; rel=\"{1}\"";
    public static final String HEADER_X_TOTAL_COUNT = "X-Total-Count";

    EnumSet<HttpMethod> ALL_METHODS =EnumSet.allOf(HttpMethod.class);

    /**
     * Creates a http header containing the given json web token.
     *
     * @param authentication authentication.
     * @param jsonWebToken   authentication token.
     * @return authentication headers.
     */
    public static HttpHeaders toHttpHeader(
            Authentication authentication,
            String jsonWebToken
    ) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HEADER_NAME, "Bearer " + jsonWebToken);
        return httpHeaders;
    }

    /**
     * Creates a page link.
     *
     * @param uriBuilder scoped uri builder.
     * @param pageNumber scoped page number.
     * @param pageSize   scoped page size.
     * @param relType    relation type.
     * @return page link.
     */
    public static String createPageLink(UriComponentsBuilder uriBuilder, int pageNumber, int pageSize, String relType) {
        return MessageFormat.format(
                HEADER_LINKS_FORMATTER,
                preparePageLink(uriBuilder, pageNumber, pageSize),
                relType
        );
    }

    protected static String preparePageLink(UriComponentsBuilder uriBuilder, int pageNumber, int pageSize) {
        return uriBuilder.replaceQueryParam("page", Integer.toString(pageNumber))
                .replaceQueryParam("size", Integer.toString(pageSize))
                .toUriString()
                .replace(",", "%2C")
                .replace(";", "%3B");
    }

    /**
     * Since Pageables are a lot of dull, dirty work, we wrap their creation into a single method.
     *
     * @param uriBuilder scoped uri builder.
     * @param page       scoped page.
     * @return HttpHeaders for a {@link ResponseEntity}
     */
    public static <E> HttpHeaders generatePaginationHttpHeaders(
            UriComponentsBuilder uriBuilder,
            Page<E> page
    ) {

        int pageNumber = page.getNumber();
        int pageSize = page.getSize();
        StringBuilder link = new StringBuilder();

        // include a "back" link
        if (pageNumber < page.getTotalPages() - 1) {
            link
                    .append(createPageLink(uriBuilder, pageNumber + 1, pageSize, "next"))
                    .append(",");
        }

        // include a "next" link
        if (pageNumber > 0) {
            link
                    .append(createPageLink(uriBuilder, pageNumber - 1, pageSize, "prev"))
                    .append(",");
        }

        // include a "last" link
        link
                .append(createPageLink(uriBuilder, page.getTotalPages() - 1, pageSize, "last"))
                .append(",")

                // include a "first" link
                .append(createPageLink(uriBuilder, 0, pageSize, "first"));

        HttpHeaders headers = new HttpHeaders();
        // include the total record count
        headers.add(HEADER_X_TOTAL_COUNT, Long.toString(page.getTotalElements()));
        headers.add(HttpHeaders.LINK, link.toString());
        return headers;
    }


    /**
     * Gets the configured lifetime of a json web token.
     *
     * @param extended true if the token should have an extended lifetime.
     * @return the lifetime in ms.
     */
    public static Date getTokenValidity(boolean extended) {
        return new Date(
                new Date().getTime() + 1000 * (extended
                        ? 10000
                        : 1000000)
        );
    }
}

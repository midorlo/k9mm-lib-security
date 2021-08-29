package com.midorlo.k9.components;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public interface K9Initializer {

    /**
     * Initializes a modules' persistent Objects. Multiple invocations of this object must not cause an error.
     *
     * @return runner.
     */
    CommandLineRunner initialize();
}

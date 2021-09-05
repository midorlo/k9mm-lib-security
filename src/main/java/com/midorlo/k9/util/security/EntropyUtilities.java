package com.midorlo.k9.util.security;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Random Password Generator.
 * <p>
 * References https://www.baeldung.com/java-generate-secure-password
 */
public class EntropyUtilities {

    private static Random getEntropySource() {
        return new SecureRandom();
    }

    public String createHumanRememberableRandomPassword() {

        Stream<Character> pwdStream = Stream.concat(
                createRandomNumerics(2),
                Stream.concat(
                        createRandomSpecials(2),
                        Stream.concat(
                                createRandomAlphas(2, true),
                                createRandomAlphas(4, false)
                        )
                )
        );
        List<Character> charList = pwdStream.collect(Collectors.toList());
        Collections.shuffle(charList);

        return charList.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    public Stream<Character> createRandomAlphas(int count, boolean upperCase) {
        Random entropySource = getEntropySource();
        IntStream characters = upperCase
                ? entropySource.ints(count, 65, 90)
                : entropySource.ints(count, 97, 122);
        return characters.mapToObj(data -> (char) data);
    }

    public Stream<Character> createRandomNumerics(int count) {
        IntStream numbers = getEntropySource().ints(count, 48, 57);
        return numbers.mapToObj(data -> (char) data);
    }

    public Stream<Character> createRandomSpecials(int count) {
        IntStream specialChars = getEntropySource().ints(count, 33, 45);
        return specialChars.mapToObj(data -> (char) data);
    }
}

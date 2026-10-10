package com.andrewpuglionesi.base64;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings({"PMD.CommentRequired", "PMD.CommentDefaultAccessModifier"})
public class Base64Constants {
    
    static final int BITS_PER_BYTE = 8;
    /**
     * The number of bits required to form a Base64 character.
     */
    static final int BITS_PER_SEXTET = 6;

    /**
     * Contains all Base64 characters. The character for a given a 6-bit sequence can be found by converting that
     * sequence to a decimal number and using that number as an index in the list.
     */
    static final List<Character> BASE64_CHARACTERS = List.of(
        // index 0-25
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        // index 26-51
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        // index 52-61
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        // index 62-63
        '+', '/'
    );

    /**
     * Maps Base64 characters to the corresponding 6-bit binary sequence, stored as a byte.
     */
    static final Map<Character, Byte> B64_CHAR_TO_BINARY_MAP = IntStream.range(0, BASE64_CHARACTERS.size())
            .boxed()
            .collect(Collectors.toUnmodifiableMap(BASE64_CHARACTERS::get, Integer::byteValue));
}

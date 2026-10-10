package com.andrewpuglionesi.base64;

import java.nio.charset.Charset;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import static com.andrewpuglionesi.base64.Base64Constants.BITS_PER_BYTE;
import static com.andrewpuglionesi.base64.Base64Constants.BITS_PER_SEXTET;

/**
 * Encodes binary or text content using the standard variant of Base64. Does not include linebreaks in output.
 * Includes padding in output.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Base64Encoder {

    /**
     * The minimum number of bytes that can be evenly broken up into Base64 sextets.
     */
    private static final int MODULUS = 3;

    /**
     * Encodes a string using the standard variant of Base64.
     * @param string the string to encode using Base64.
     * @param charset character set to use when converting the string to binary. The same character set
     * should be used to encode and decode data because character sets control the transformation of
     * strings to binary.
     * @return Base64-encoded string.
     */
    public static String encode(@NonNull final String string, @NonNull final Charset charset) {
        byte[] bytes = string.getBytes(charset);
        return encode(bytes);
    }
    
    /**
     * Encodes binary data using the standard variant of Base64.
     * @param bytes binary content to encode.
     * @return Base64-encoded string.
     */
    public static String encode(@NonNull final byte[] bytes) {
        StringBuilder b64Encoded = new StringBuilder();

        int excessBytes = bytes.length % MODULUS;
        int numBits = bytes.length * BITS_PER_BYTE;
        int b64CharCount = numBits / BITS_PER_SEXTET;
        if (excessBytes > 0) {
            b64CharCount += 1;
        }
        
        for (int i = 0; i < b64CharCount; i++) {
            int startOfSextet = i * BITS_PER_SEXTET;
            char nextChar = getB64Char(bytes, startOfSextet);
            b64Encoded.append(nextChar);
        }

        if (excessBytes > 0) {
            b64Encoded.append("=".repeat(MODULUS - excessBytes));
        }

        return b64Encoded.toString();
    }

    @SuppressWarnings("PMD.UselessParentheses")
    private static char getB64Char(final byte[] bitVector, final int start) {
        int b64CharIndex = 0;
        int bitIndex;
        for (bitIndex = start; (bitIndex < start + BITS_PER_SEXTET) && (bitIndex < bitVector.length * BITS_PER_BYTE); bitIndex++) {
            byte nextBit = readBit(bitVector, bitIndex);
            b64CharIndex = (b64CharIndex << 1) | nextBit;
        }
        // handle case where last sextet extends beyond length of bit vector (need zero-padding to produce correct character)
        int bitsProcessed = bitIndex - start;
        if (bitsProcessed < BITS_PER_SEXTET) {
            int bitsRemaining = BITS_PER_SEXTET - bitsProcessed;
            b64CharIndex = b64CharIndex << bitsRemaining;
        }
        return Base64Constants.BASE64_CHARACTERS.get(b64CharIndex);
    }

    /**
     * @return the indexth bit of bitVector (either 0 or 1).
     */
    private static byte readBit(final byte[] bitVector, final int index) {
        int byteIndex = index / BITS_PER_BYTE;
        int bitIndex = index % BITS_PER_BYTE;
        
        int distanceFromLsb = BITS_PER_BYTE - bitIndex - 1;
        return (byte) ((bitVector[byteIndex] >>> distanceFromLsb) & 1);
    }
}

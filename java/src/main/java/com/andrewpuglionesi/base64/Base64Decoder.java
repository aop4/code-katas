package com.andrewpuglionesi.base64;

import java.nio.charset.Charset;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import static com.andrewpuglionesi.base64.Base64Constants.BITS_PER_BYTE;
import static com.andrewpuglionesi.base64.Base64Constants.BITS_PER_SEXTET;

/**
 * Decodes content that has been encoded with the standard variant of Base64. The input does not need
 * to be padded.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Base64Decoder {

    /**
     * Decodes a Base64-encoded string using the supplied character set. Base64 data will first
     * be read as binary data, and then the binary data will be converted into a string.
     * The same character set should be used to encode and decode data because character sets control
     * the transformation of strings to binary.
     */
    public static String decode(@NonNull final String base64Input, @NonNull final Charset charset) {
        byte[] bytes = decode(base64Input);
        return new String(bytes, charset);
    }

    /**
     * Decodes a Base64-encoded string, returning its binary contents as a byte array.
     */
    public static byte[] decode(@NonNull final String base64Input) {
        String cleanedBase64 = cleanBase64String(base64Input);

        int base64CharCount = cleanedBase64.length();
        int numBits = base64CharCount * BITS_PER_SEXTET;
        int numBytes = numBits / BITS_PER_BYTE;

        byte[] bitVector = new byte[numBytes];

        for (int i = 0; i < cleanedBase64.length(); i++) {
            char b64Char = cleanedBase64.charAt(i);
            byte binarySextet = Base64Constants.B64_CHAR_TO_BINARY_MAP.get(b64Char);
            addSextetToBitVector(bitVector, i * BITS_PER_SEXTET, binarySextet);
        }
        return bitVector;
    }

    /**
     * Removes padding and unexpected characters from a Base64-encoded value.
     */
    private static String cleanBase64String(final String base64) {
        return base64.replaceAll("[^a-zA-Z0-9+/]", "");
    }

    private static void addSextetToBitVector(final byte[] bitVector, final int startIndex, final byte sextet) {
        int mask = 0x20;
        for (int bitIndex = 0; bitIndex < BITS_PER_SEXTET; bitIndex++) {
            if ((mask & sextet) != 0) {
                setBit(bitVector, startIndex + bitIndex);
            }
            mask = mask >>> 1;
        }
    }

    /**
     * Sets the indexth bit in bitVector to 1.
     */
    private static void setBit(final byte[] bitVector, final int index) {
        int byteIndex = index / BITS_PER_BYTE;
        int bitIndex = index % BITS_PER_BYTE;

        byte originalVal = bitVector[byteIndex];
        byte newVal = (byte) (originalVal | (0x80 >>> bitIndex));

        bitVector[byteIndex] = newVal;
    }
}

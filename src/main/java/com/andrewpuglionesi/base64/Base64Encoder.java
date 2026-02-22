package com.andrewpuglionesi.base64;

import java.nio.charset.Charset;
import java.util.BitSet;

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
        BitSet bitVector = buildBigEndianBitVector(bytes);

        int excessBytes = bytes.length % MODULUS;
        int numBits = bytes.length * BITS_PER_BYTE;
        int b64CharCount = numBits / BITS_PER_SEXTET;
        if (excessBytes > 0) {
            b64CharCount += 1;
        }
        
        for (int i = 0; i < b64CharCount; i++) {
            int startOfSextet = i * BITS_PER_SEXTET;
            char nextChar = getB64Char(bitVector, startOfSextet);
            b64Encoded.append(nextChar);
        }

        if (excessBytes > 0) {
            b64Encoded.append("=".repeat(MODULUS - excessBytes));
        }

        return b64Encoded.toString();
    }

    /**
     * Combines all bytes in the provided array into a continuous bit vector. The order of bytes is maintained,
     * as is the order of bits within each byte.
     * 
     * @implNote BitSet.valueOf(byte[]) differs in that each byte is written backward, with its least significant bits
     * at the lowest index, while the order of bytes is maintained. That would make Base64 encoding extremely
     * difficult, so this method builds a big endian vector instead.
     * 
     * Clearly it's not optimal to set every bit individually, this is just a compromise I'm making due to sunken
     * costs.
     */
    private static BitSet buildBigEndianBitVector(final byte[] bytes) {
        BitSet bitVector = new BitSet();
        for (int byteIndex = 0; byteIndex < bytes.length; byteIndex++) {
            byte currByte = bytes[byteIndex];
            short mask = 0x80;
            for (int bitIndex = byteIndex * BITS_PER_BYTE; bitIndex < (byteIndex + 1) * BITS_PER_BYTE; bitIndex++) {
                bitVector.set(bitIndex, (currByte & mask) != 0);
                mask >>>= 1;
            }
        }
        return bitVector;
    }

    private static char getB64Char(final BitSet bitVector, final int start) {
        int b64CharIndex = 0;
        int bitIndex;
        for (bitIndex = start; bitIndex < start + BITS_PER_SEXTET && bitIndex < bitVector.size(); bitIndex++) {
            byte nextBit = bitVector.get(bitIndex) ? (byte) 1 : (byte) 0;
            b64CharIndex = (b64CharIndex << 1) | nextBit;
        }
        // handle case where last sextet extends beyond length of bit vector (need zero-padding to produce correct character)
        // TODO: may be able to remove this because BitSet doesn't care if you overrun its array
        int bitsProcessed = bitIndex - start;
        if (bitsProcessed < BITS_PER_SEXTET) {
            int bitsRemaining = BITS_PER_SEXTET - bitsProcessed;
            b64CharIndex = b64CharIndex << bitsRemaining;
        }
        return Base64Constants.BASE64_CHARACTERS.get(b64CharIndex);
    }
}

package com.andrewpuglionesi.base64;

import java.nio.charset.Charset;
import java.util.BitSet;

import javax.annotation.Nonnull;

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
    public static String decode(@NonNull final String base64Input, @Nonnull final Charset charset) {
        byte[] bytes = decode(base64Input);
        return new String(bytes, charset);
    }

    /**
     * Decodes a Base64-encoded string, returning its binary contents as a byte array.
     */
    public static byte[] decode(@NonNull final String base64Input) {
        String cleanedBase64 = cleanBase64String(base64Input);

        int base64CharCount = cleanedBase64.length();
        BitSet bitVector = new BitSet(base64CharCount * BITS_PER_SEXTET);

        for (int i = 0; i < cleanedBase64.length(); i++) {
            char b64Char = cleanedBase64.charAt(i);
            byte binarySextet = Base64Constants.B64_CHAR_TO_BINARY_MAP.get(b64Char);
            addSextetToBitVector(bitVector, i * BITS_PER_SEXTET, binarySextet);
        }
        return bitVectorToBytes(bitVector, base64CharCount * BITS_PER_SEXTET);
    }

    /**
     * Removes padding and unexpected characters from a Base64-encoded value.
     */
    private static String cleanBase64String(final String base64) {
        return base64.replaceAll("[^a-zA-Z0-9+/]", "");
    }

    private static void addSextetToBitVector(final BitSet bitVector, final int start, final byte sextet) {
        for (int sextetIndex = 0; sextetIndex < BITS_PER_SEXTET; sextetIndex++) {
            int mask = 1 << (BITS_PER_SEXTET - sextetIndex - 1);
            bitVector.set(start + sextetIndex, (mask & sextet) != 0);
        }
    }

    /**
     * @param vectorLength length of vector in bits.
     * @return an array representing the bits in the BitSet in ascending order by index.
     * 
     * @implNote BitSet.toByteArray() does not suffice because it reverses the bits within each byte (little endian
     * instead of big endian).
     * 
     * Clearly it's not optimal to set every bit individually, this is just a compromise I'm making due to sunken
     * costs.
     */
    private static byte[] bitVectorToBytes(final BitSet bitVector, final int vectorLength) {
        int numBytes = vectorLength / BITS_PER_BYTE;
        byte[] bytes = new byte[numBytes];
        for (int byteIndex = 0; byteIndex < numBytes; byteIndex++) {
            byte currByte = 0;
            for (int bitIndex = byteIndex * BITS_PER_BYTE; bitIndex < (byteIndex + 1) * BITS_PER_BYTE; bitIndex++) {
                byte nextBit = bitVector.get(bitIndex) ? (byte) 1 : (byte) 0;
                currByte = (byte) ((currByte << 1) | nextBit);
            }
            bytes[byteIndex] = currByte;
        }
        return bytes;
    }
}

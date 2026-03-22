package com.andrewpuglionesi.datastructures;

import java.util.Arrays;

/**
 * A fixed-length bit vector in which each bit of data requires approximately 1 bit of memory.
 * 
 * One instance of BitArray can store no more than 2^31 - 1 bits (approximately 268.4 megabytes).
 */
public class BitArray {
    private static final int BITS_PER_BYTE = 8;

    private final byte[] bytes;
    private final int size;

    /**
     * Initializes a BitArray with the specified length. Initially, all bits will be set to 0.
     * @param numBits length of the BitArray in bits.
     * @throws IllegalArgumentException if the provided size is less than 1.
     */
    public BitArray(final int numBits) {
        if (numBits <= 0) {
            throw new IllegalArgumentException("BitArray size must be a positive number");
        }

        int quotient = numBits / BITS_PER_BYTE;
        int remainder = numBits % BITS_PER_BYTE;
        this.bytes = new byte[quotient + Math.min(remainder, 1)];

        this.size = numBits;
    }

    /**
     * Initializes a BitArray with the content of the underlying byte array. Big Endian ordering is used: the
     * most significant bit of the first byte will be at the 0th index of the BitArray; the least significant
     * bit of the last byte will be at the highest index of the BitArray.
     * 
     * Note that if the number of <em>bits</em> in the input exceeds {@link Integer#MAX_VALUE}, the content will be
     * truncated.
     * 
     * Modifying the BitArray will not modify the supplied array, and vice versa.
     * 
     * @param bytes initial contents for the BitArray.
     * 
     * @throws IllegalArgumentException if the provided byte array is null or empty.
     */
    @SuppressWarnings("PMD.UselessParentheses")
    public BitArray(final byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("byte array must not be null or empty");
        }

        int maxLength = (Integer.MAX_VALUE / BITS_PER_BYTE) + 1;
        if (bytes.length >= maxLength) {
            this.bytes = Arrays.copyOf(bytes, maxLength);
            this.size = Integer.MAX_VALUE;
        } else {
            this.bytes = Arrays.copyOf(bytes, bytes.length);
            this.size = bytes.length * BITS_PER_BYTE;
        }
    }

    /**
     * @return the number of bits in this bit array.
     */
    public int size() {
        return this.size;
    }

    /**
     * Returns the value of the indexth bit of this BitArray. Bits are zero-indexed.
     * @param index index of the bit to set.
     * @return true if the indexth bit of this BitArray is 1, false if the indexth bit is 0.
     */
    public boolean getBit(final int index) {
        this.validateIndex(index);
        
        int byteIndex = index / 8;
        int bitIndex = index % 8;

        int mask = 0x80 >> bitIndex;
        return (this.bytes[byteIndex] & mask) != 0;
    }

    /**
     * Sets the indexth bit of this BitArray. Bits are zero-indexed.
     * @param index index of the bit to set.
     * @param value if value is true, the bit will be set to 1. Else bit will be set to 0.
     */
    public void setBit(final int index, final boolean value) {
        this.validateIndex(index);
        
        int byteIndex = index / 8;
        int bitIndex = index % 8;

        if (value) {
            // set bit to 1
            int mask = 0x80 >> bitIndex;
            this.bytes[byteIndex] = (byte) (this.bytes[byteIndex] | mask);
        } else {
            // set bit to 0
            int mask = ~(0x80 >> bitIndex);
            this.bytes[byteIndex] = (byte) (this.bytes[byteIndex] & mask);
        }
    }

    /**
     * @return the contents of this BitArray as a byte array. If the length of this bit array is
     * not a multiple of 8, then the excess bits in the last byte of the array will be 0's.
     */
    public byte[] toByteArray() {
        this.clearExcessBits();
        return Arrays.copyOf(this.bytes, this.bytes.length);
    }

    /**
     * Negates (flips) the value of each bit in the bit array.
     */
    public void negate() {
        for (int i = 0; i < this.bytes.length; i++) {
            this.bytes[i] = (byte) ~this.bytes[i];
        }
    }

    /**
     * Sets all bits in the bit array to the specified value.
     * @param value if value is true, every bit will be set to 1. Else every bit will be set to 0.
     */
    public void fill(final boolean value) {
        byte byteVal = value ? (byte) 0xff : 0;
        Arrays.fill(this.bytes, byteVal);
    }

    private void validateIndex(final int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Index %d is out of bounds (size = %d)".formatted(index, this.size));
        }
    }

    /**
     * Sets bits of the underlying byte array to 0 if they are beyond the end of the bit array.
     */
    private void clearExcessBits() {
        int excessBits = this.size() % BITS_PER_BYTE;
        if (excessBits > 0) {
            int lastByteIdx = this.bytes.length - 1;
            int mask = 0xff << (BITS_PER_BYTE - excessBits);
            this.bytes[lastByteIdx] = (byte) (this.bytes[lastByteIdx] & mask);
        }
    }
}

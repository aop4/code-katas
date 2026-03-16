package com.andrewpuglionesi.datastructures;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class BitArrayTest {

    private static final int BITS_PER_BYTE = 8;
    private static final int MAX_SIZE_IN_BITS = Integer.MAX_VALUE;
    private static final int MAX_BYTE_ARRAY_SIZE = (MAX_SIZE_IN_BITS / BITS_PER_BYTE) + 1;
    
    @Test
    void constructorWithNegativeSize() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> new BitArray(-1)
        );
        assertEquals("BitArray size must be a positive number", thrown.getMessage());
    }

    @Test
    void constructorWithZeroSize() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> new BitArray(0)
        );
        assertEquals("BitArray size must be a positive number", thrown.getMessage());
    }

    @Test
    void constructorWithMaximumSize() {
        // sorry, RAM
        BitArray bitArray = new BitArray(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, bitArray.size());
        assertEquals(MAX_BYTE_ARRAY_SIZE, bitArray.toByteArray().length);
    }

    @Test
    void constructorWithNoRemainderBits() {
        BitArray bitArray = new BitArray(8);
        assertEquals(8, bitArray.size());
        assertEquals(1, bitArray.toByteArray().length);
    }

    @Test
    void constructorWithOneRemainderBit() {
        BitArray bitArray = new BitArray(9);
        assertEquals(9, bitArray.size());
        assertEquals(2, bitArray.toByteArray().length);
    }

    @Test
    void constructorWithSevenRemainderBits() {
        BitArray bitArray = new BitArray(15);
        assertEquals(15, bitArray.size());
        assertEquals(2, bitArray.toByteArray().length);
    }

    @Test
    void constructorInitializesBitArrayWithAllBitsZero() {
        BitArray bitArray = new BitArray(64);
        byte[] bytes = bitArray.toByteArray();
        
        assertEquals(8, bytes.length);
        for (byte b : bytes) {
            assertEquals(0, b);
        }
    }

    @Test
    void constructorWithNullByteArray() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> new BitArray(null)
        );
        assertEquals("byte array must not be null or empty", thrown.getMessage());
    }

    @Test
    void constructorWithEmptyByteArray() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> new BitArray(new byte[0])
        );
        assertEquals("byte array must not be null or empty", thrown.getMessage());
    }

    @Test
    void constructorWithSmallByteArray() {
        byte[] input = new byte[]{
            (byte) 0xff, (byte) 0xff
        };
        BitArray bitArray = new BitArray(input);

        assertEquals(16, bitArray.size());
        assertArrayEquals(input, bitArray.toByteArray());

        // Verify that modifying the input array has no impact on the BitArray
        input[0] = 0;
        input[1] = 0;
        assertArrayEquals(new byte[]{
            (byte) 0xff, (byte) 0xff
        }, bitArray.toByteArray());
    }

    @Test
    void constructorWithMaximumSizeByteArray() {
        byte[] input = new byte[MAX_BYTE_ARRAY_SIZE];
        BitArray bitArray = new BitArray(input);

        assertEquals(MAX_SIZE_IN_BITS, bitArray.size()); // overflow bits are ignored
        assertEquals(MAX_BYTE_ARRAY_SIZE, bitArray.toByteArray().length);
    }

    @Test
    void constructorWithLargerThanMaximumSizeByteArray() {
        byte[] input = new byte[MAX_BYTE_ARRAY_SIZE + 1];
        BitArray bitArray = new BitArray(input);

        assertEquals(MAX_SIZE_IN_BITS, bitArray.size()); // overflow bits are ignored
        assertEquals(MAX_BYTE_ARRAY_SIZE, bitArray.toByteArray().length);
    }

    @Test
    void setBitNegativeIndex() {
        BitArray bitArray = new BitArray(8);
        IndexOutOfBoundsException thrown = assertThrows(
            IndexOutOfBoundsException.class,
            () -> bitArray.setBit(-1, true)
        );
        assertEquals("Index -1 is out of bounds (size = 8)", thrown.getMessage());
    }

    @Test
    void setBitIndexTooLarge() {
        BitArray bitArray = new BitArray(8);
        IndexOutOfBoundsException thrown = assertThrows(
            IndexOutOfBoundsException.class,
            () -> bitArray.setBit(8, true)
        );
        assertEquals("Index 8 is out of bounds (size = 8)", thrown.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2,3,4,5,6,7})
    void setBitToOneAtAllBitIndexes(int indexToSet) {
        BitArray bitArray = new BitArray(8);

        bitArray.setBit(indexToSet, true);

        for (int curr = 0; curr < bitArray.size(); curr++) {
            if (curr == indexToSet) {
                assertEquals(true, bitArray.getBit(curr));
            } else {
                assertEquals(false, bitArray.getBit(curr));
            }
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2,3,4,5,6,7})
    void setBitToZeroAtAllBitIndexes(int indexToSet) {
        byte[] input = new byte[]{
            (byte) 0xff
        };
        BitArray bitArray = new BitArray(input);

        bitArray.setBit(indexToSet, false);

        for (int curr = 0; curr < bitArray.size(); curr++) {
            if (curr == indexToSet) {
                assertEquals(false, bitArray.getBit(curr));
            } else {
                assertEquals(true, bitArray.getBit(curr));
            }
        }
    }

    @Test
    void setBitToOneInDifferentBytes() {
        BitArray bitArray = new BitArray(24);

        bitArray.setBit(0, true);
        bitArray.setBit(9, true);
        bitArray.setBit(18, true);

        byte[] expected = new byte[]{
            (byte) 0b1000_0000,
                   0b0100_0000,
                   0b0010_0000
        };

        assertArrayEquals(expected, bitArray.toByteArray());
    }

    @Test
    void setBitToZeroInDifferentBytes() {
        byte[] input = new byte[]{
            (byte) 0xff,
            (byte) 0xff,
            (byte) 0xff
        };
        BitArray bitArray = new BitArray(input);

        bitArray.setBit(0, false);
        bitArray.setBit(9, false);
        bitArray.setBit(18, false);

        byte[] expected = new byte[]{
                   0b0111_1111,
            (byte) 0b1011_1111,
            (byte) 0b1101_1111
        };

        assertArrayEquals(expected, bitArray.toByteArray());
    }

    @Test
    void getBitNegativeIndex() {
        BitArray bitArray = new BitArray(8);
        IndexOutOfBoundsException thrown = assertThrows(
            IndexOutOfBoundsException.class,
            () -> bitArray.getBit(-1)
        );
        assertEquals("Index -1 is out of bounds (size = 8)", thrown.getMessage());
    }

    @Test
    void getBitIndexTooLarge() {
        BitArray bitArray = new BitArray(8);
        IndexOutOfBoundsException thrown = assertThrows(
            IndexOutOfBoundsException.class,
            () -> bitArray.getBit(8)
        );
        assertEquals("Index 8 is out of bounds (size = 8)", thrown.getMessage());
    }

    @Test
    void negate() {
        BitArray bitArray = new BitArray(new byte[]{
                   0b01010101,
            (byte) 0b10101010,
                   0b00000000,
            (byte) 0b11111111
        });

        bitArray.negate();

        byte[] expected = new byte[]{
            (byte) 0b10101010,
                   0b01010101,
            (byte) 0b11111111,
                   0b00000000
        };
        assertArrayEquals(expected, bitArray.toByteArray());
    }

    @ParameterizedTest
    @CsvSource({
        "1, 10000000",
        "2, 11000000",
        "3, 11100000",
        "4, 11110000",
        "5, 11111000",
        "6, 11111100",
        "7, 11111110",
        "8, 11111111",
    })
    void toByteArrayClearsExcessBits(int vectorLength, String negatedByte) {
        BitArray bitArray = new BitArray(vectorLength);
        // negate flips all bits in the underlying byte array (in this case, from 0 to 1)
        bitArray.negate();
        // confirm that bits beyond the vector length are still returned as 0's
        byte expected = (byte) Integer.parseInt(negatedByte, 2);
        assertEquals(expected, bitArray.toByteArray()[0]);
    }

    void clearExcessBitsAffectsOnlyLastByte() {
        BitArray bitArray = new BitArray(9);
        // negate flips all bits in the underlying byte array (in this case, from 0 to 1)
        bitArray.negate();
        // confirm that bits beyond the vector length are still returned as 0's
        byte[] expected = new byte[] {
            (byte) 0b11111111,
            (byte) 0b10000000
        };
        assertEquals(expected, bitArray.toByteArray());
    }

    void maximumIndexIsAccessible() {
        BitArray bitArray = new BitArray(MAX_SIZE_IN_BITS);
        assertEquals(false, bitArray.getBit(MAX_SIZE_IN_BITS - 1));

        bitArray.setBit(MAX_SIZE_IN_BITS - 1, true);
        
        assertEquals(true, bitArray.getBit(MAX_SIZE_IN_BITS - 1));
    }
}

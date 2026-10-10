package com.andrewpuglionesi.base64;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

public class Base64DecoderTest {
       
    @Test
    void decodeEmptyStringToString() {
        assertEquals("", Base64Decoder.decode("", StandardCharsets.UTF_8));
    }

    @Test
    void decodeEmptyStringToByteArray() {
        assertArrayEquals(new byte[0], Base64Decoder.decode(""));
    }

    @Test
    void decodeOneByteToString() {
        assertEquals("b", Base64Decoder.decode("Yg==", StandardCharsets.UTF_8));
    }

    @Test
    void decodeOneByteToArray() {
        assertArrayEquals(new byte[]{0x62}, Base64Decoder.decode("Yg=="));
    }

    @Test
    void decodeTwoBytesToString() {
        assertEquals("ba", Base64Decoder.decode("YmE=", StandardCharsets.UTF_8));
    }

    @Test
    void decodeTwoBytesToArray() {
        assertArrayEquals(new byte[]{0x62, 0x61}, Base64Decoder.decode("YmE="));
    }

    @Test
    void decodeThreeBytesToString() {
        assertEquals("ban", Base64Decoder.decode("YmFu", StandardCharsets.UTF_8));
    }

    @Test
    void decodeThreeBytesToArray() {
        assertArrayEquals(new byte[]{0x62, 0x61, 0x6e}, Base64Decoder.decode("YmFu"));
    }

    @Test
    void decodeFourBytesToString() {
        assertEquals("bana", Base64Decoder.decode("YmFuYQ==", StandardCharsets.UTF_8));
    }

    @Test
    void decodeFiveBytesToString() {
        assertEquals("banan", Base64Decoder.decode("YmFuYW4=", StandardCharsets.UTF_8));
    }

    @Test
    void decodeSixBytesToString() {
        assertEquals("banana", Base64Decoder.decode("YmFuYW5h", StandardCharsets.UTF_8));
    }

    @Test
    void decodeAllZeros() {
        assertArrayEquals(new byte[]{ 0, 0, 0 }, Base64Decoder.decode("AAAA"));
    }

    @Test
    void decodeAllOnes() {
        byte[] expected = new byte[]{ (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
        assertArrayEquals(expected, Base64Decoder.decode("////"));
    }

    @Test
    void decodeAllOnesWithOnePaddingChar() {
        byte[] expected = new byte[]{ (byte) 0xFF, (byte) 0xFF };
        assertArrayEquals(expected, Base64Decoder.decode("//8="));
    }

    @Test
    void decodeAllOnesWithTwoPaddingChars() {
        byte[] expected = new byte[]{ (byte) 0xFF };
        assertArrayEquals(expected, Base64Decoder.decode("/w=="));
    }

    @Test
    void decodeBase64WithPaddingMissing() {
        String input = "Yg"; // should be "Yg==", but encoder should tolerate incorrect padding
        assertEquals("b", Base64Decoder.decode(input, StandardCharsets.UTF_8));
    }

    @Test
    void decodeBase64WithUnnecessaryPadding() {
        String input = "YmFuYW5h=="; // should be "YmFuYW5h", but encoder should tolerate incorrect padding
        assertEquals("banana", Base64Decoder.decode(input, StandardCharsets.UTF_8));
    }

    @Test
    void decodeShouldIgnoreInvalidCharacters() {
        String input = "!@#$%  YmFuYW5h  !@#@!  YmFuYW5h  %$#@!";
        assertEquals("bananabanana", Base64Decoder.decode(input, StandardCharsets.UTF_8));
    }

    @Test
    void decodeLoremIpsum() {
        String base64 = "TG9yZW0gaXBzdW0gZG9sb3Igc2l0IGFtZXQsIGNvbnNlY3RldHVyIGFkaXBpc2NpbmcgZWxpdCwgc2VkIGRvIGVpdXNt"
            + "b2QgdGVtcG9yIGluY2lkaWR1bnQgdXQgbGFib3JlIGV0IGRvbG9yZSBtYWduYSBhbGlxdWEuIFV0IGVuaW0gYWQgbWluaW0gdmVuaWFtLCBx"
            + "dWlzIG5vc3RydWQgZXhlcmNpdGF0aW9uIHVsbGFtY28gbGFib3JpcyBuaXNpIHV0IGFsaXF1aXAgZXggZWEgY29tbW9kbyBjb25zZXF1YXQuI"
            + "ER1aXMgYXV0ZSBpcnVyZSBkb2xvciBpbiByZXByZWhlbmRlcml0IGluIHZvbHVwdGF0ZSB2ZWxpdCBlc3NlIGNpbGx1bSBkb2xvcmUgZXUgZnV"
            + "naWF0IG51bGxhIHBhcmlhdHVyLiBFeGNlcHRldXIgc2ludCBvY2NhZWNhdCBjdXBpZGF0YXQgbm9uIHByb2lkZW50LCBzdW50IGluIGN1bHBhIH"
            + "F1aSBvZmZpY2lhIGRlc2VydW50IG1vbGxpdCBhbmltIGlkIGVzdCBsYWJvcnVtLg==";

        String expected = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut"
            + " labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut"
            + " aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum"
            + " dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia"
            + " deserunt mollit anim id est laborum.";

        assertEquals(expected, Base64Decoder.decode(base64, StandardCharsets.UTF_8));
    }

    @Test
    void decodeEveryBase64Character() {

        String base64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

        byte[] expected = new byte[]{ 0, 16, -125, 16, 81, -121, 32, -110, -117, 48, -45, -113, 65, 20, -109, 81,
                85, -105, 97, -106, -101, 113, -41, -97, -126, 24, -93, -110, 89, -89, -94, -102, -85, -78, -37,
                -81, -61, 28, -77, -45, 93, -73, -29, -98, -69, -13, -33, -65};
                
        assertArrayEquals(expected, Base64Decoder.decode(base64));
    }

}

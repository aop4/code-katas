package com.andrewpuglionesi.base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

public class Base64EncoderTest {
    
    @Test
    void encodeEmptyByteArray() {
        assertEquals("", Base64Encoder.encode(new byte[0]));
    }
    
    @Test
    void encodeEmptyString() {
        assertEquals("", Base64Encoder.encode("", StandardCharsets.UTF_8));
    }

    @Test
    void encodeOneByteArray() {
        byte[] input = new byte[]{0x62};
        assertEquals("Yg==", Base64Encoder.encode(input));
    }

    @Test
    void encodeOneByteString() {
        String input = "b";
        assertEquals("Yg==", Base64Encoder.encode(input, StandardCharsets.UTF_8));
    }

    @Test
    void encodeTwoByteArray() {
        byte[] input = new byte[]{0x62, 0x61};
        assertEquals("YmE=", Base64Encoder.encode(input));
    }

    @Test
    void encodeTwoByteString() {
        String input = "ba";
        assertEquals("YmE=", Base64Encoder.encode(input, StandardCharsets.UTF_8));
    }

    @Test
    void encodeThreeByteArray() {
        byte[] input = new byte[]{0x62, 0x61, 0x6e};
        assertEquals("YmFu", Base64Encoder.encode(input));
    }

    @Test
    void encodeThreeByteString() {
        String input = "ban";
        assertEquals("YmFu", Base64Encoder.encode(input, StandardCharsets.UTF_8));
    }

    @Test
    void encodeFourByteString() {
        String input = "bana";
        assertEquals("YmFuYQ==", Base64Encoder.encode(input, StandardCharsets.UTF_8));
    }

    @Test
    void encodeFiveByteString() {
        String input = "banan";
        assertEquals("YmFuYW4=", Base64Encoder.encode(input, StandardCharsets.UTF_8));
    }

    @Test
    void encodeSixByteString() {
        String input = "banana";
        assertEquals("YmFuYW5h", Base64Encoder.encode(input, StandardCharsets.UTF_8));
    }

    @Test
    void encodeAllZeros() {
        byte[] input = new byte[]{ 0, 0, 0 };
        assertEquals("AAAA", Base64Encoder.encode(input));
    }

    @Test
    void encodeAllOnes() {
        byte[] input = new byte[]{ (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
        assertEquals("////", Base64Encoder.encode(input));
    }

    @Test
    void encodeAllOnesWithOnePaddingChar() {
        byte[] input = new byte[]{ (byte) 0xFF, (byte) 0xFF };
        assertEquals("//8=", Base64Encoder.encode(input));
    }

    @Test
    void encodeAllOnesWithTwoPaddingChars() {
        byte[] input = new byte[]{ (byte) 0xFF };
        assertEquals("/w==", Base64Encoder.encode(input));
    }

    @Test
    void encodeLoremIpsum() {
        String input = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut"
            + " labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut"
            + " aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum"
            + " dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia"
            + " deserunt mollit anim id est laborum.";

        String expected = "TG9yZW0gaXBzdW0gZG9sb3Igc2l0IGFtZXQsIGNvbnNlY3RldHVyIGFkaXBpc2NpbmcgZWxpdCwgc2VkIGRvIGVpdXNt"
            + "b2QgdGVtcG9yIGluY2lkaWR1bnQgdXQgbGFib3JlIGV0IGRvbG9yZSBtYWduYSBhbGlxdWEuIFV0IGVuaW0gYWQgbWluaW0gdmVuaWFtLCBx"
            + "dWlzIG5vc3RydWQgZXhlcmNpdGF0aW9uIHVsbGFtY28gbGFib3JpcyBuaXNpIHV0IGFsaXF1aXAgZXggZWEgY29tbW9kbyBjb25zZXF1YXQuI"
            + "ER1aXMgYXV0ZSBpcnVyZSBkb2xvciBpbiByZXByZWhlbmRlcml0IGluIHZvbHVwdGF0ZSB2ZWxpdCBlc3NlIGNpbGx1bSBkb2xvcmUgZXUgZnV"
            + "naWF0IG51bGxhIHBhcmlhdHVyLiBFeGNlcHRldXIgc2ludCBvY2NhZWNhdCBjdXBpZGF0YXQgbm9uIHByb2lkZW50LCBzdW50IGluIGN1bHBhIH"
            + "F1aSBvZmZpY2lhIGRlc2VydW50IG1vbGxpdCBhbmltIGlkIGVzdCBsYWJvcnVtLg==";
            
        assertEquals(expected, Base64Encoder.encode(input, StandardCharsets.UTF_8));
    }

    @Test
    void encodeUsingEveryBase64Character() {
        byte[] input = new byte[]{ 0, 16, -125, 16, 81, -121, 32, -110, -117, 48, -45, -113, 65, 20, -109, 81,
                85, -105, 97, -106, -101, 113, -41, -97, -126, 24, -93, -110, 89, -89, -94, -102, -85, -78, -37,
                -81, -61, 28, -77, -45, 93, -73, -29, -98, -69, -13, -33, -65};
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", Base64Encoder.encode(input));
    }

}

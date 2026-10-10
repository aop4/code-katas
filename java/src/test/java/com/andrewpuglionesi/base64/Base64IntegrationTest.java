package com.andrewpuglionesi.base64;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

public class Base64IntegrationTest {
    
    @Test
    void encodeAndDecodeImageFile() throws IOException {
        try (InputStream imageFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("base64/phone-keypad.jpg")) {
            byte[] imageFileBytes = imageFile.readAllBytes();

            String base64EncodedImage = Base64Encoder.encode(imageFileBytes);
            byte[] decodedImage = Base64Decoder.decode(base64EncodedImage);
            
            assertArrayEquals(imageFileBytes, decodedImage);
        }
    }
}

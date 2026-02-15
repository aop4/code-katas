package com.andrewpuglionesi.predictivet9;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

class PredictiveT9ServiceTest {
    private final PredictiveT9Service service = new PredictiveT9Service("wikipedia-word-frequencies.csv");

    public PredictiveT9ServiceTest() throws IOException {};

    @Test
    void buildServiceWithInvalidFile() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            // codon_map.json exists, but is not a CSV
            new PredictiveT9Service("codon_map.json");
        });
        assertEquals("CSV file is not correctly formatted", ex.getMessage());
    }
    
    @Test
    void predictWordEmptyInput() {
        List<String> output = service.predictWord("");
        assertEquals(Collections.emptyList(), output);
    }

    @Test
    void predictWordNullInput() {
        List<String> output = service.predictWord(null);
        assertEquals(Collections.emptyList(), output);
    }

    @Test
    void predictWordNonNumericInput() {
        List<String> output = service.predictWord("not-a-numeric-sequence");
        assertEquals(Collections.emptyList(), output);
    }

    @Test
    void predictWordSingleKeypress() {
        List<String> output = service.predictWord("4");
        assertEquals(List.of("i", "g", "h"), output);
    }

    @Test
    void predictWordMultipleKeypress() {
        List<String> output = service.predictWord("3333");
        assertEquals(List.of("feed", "deed"), output);
    }

    @Test
    void predictWordSequenceAbsentFromDataset() {
        List<String> output = service.predictWord("5555");
        assertEquals(Collections.emptyList(), output);
    }

    @Test
    void predictWordCommonPattern() {
        List<String> output = service.predictWord("2665");
        assertEquals(List.of("book", "cool", "cook", "bool"), output);
    }

    @Test
    void predictWordApostrophePresent() {
        List<String> output = service.predictWord("66657");
        assertEquals(List.of("monks", "monk's"), output);
    }

    @Test
    void predictWordUsingEveryDigit() {
        List<String> output = service.predictWord("9282462537");
        assertEquals(List.of("watchmaker"), output);
    }

    @Test
    void validateEveryCharacterToDigitMapping() {
        // Note: The string "the quick brown fox jumps over the lazy dog" contains every English letter.
        // Adding this test to confirm all digits are correctly mapped to letters.
        assertTrue(service.predictWord("843").contains("the"));
        assertTrue(service.predictWord("78425").contains("quick"));
        assertTrue(service.predictWord("27696").contains("brown"));
        assertTrue(service.predictWord("369").contains("fox"));
        assertTrue(service.predictWord("58677").contains("jumps"));
        assertTrue(service.predictWord("6837").contains("over"));
        assertTrue(service.predictWord("5299").contains("lazy"));
        assertTrue(service.predictWord("364").contains("dog"));
    }
}
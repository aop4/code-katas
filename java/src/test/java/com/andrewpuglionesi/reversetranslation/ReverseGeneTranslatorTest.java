package com.andrewpuglionesi.reversetranslation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReverseGeneTranslatorTest {
    private ReverseGeneTranslator translator;

    @BeforeEach
    void setup() throws IOException {
        translator = new ReverseGeneTranslator();
    }

    @Test
    void testReverseTranslateEmptyString() {
        assertEquals(Collections.singletonList(""), translator.reverseTranslateAminoAcidSequence(""));
    }

    @Test
    void testReverseTranslateEmptyArray() {
        assertEquals(Collections.singletonList(""), translator.reverseTranslateAminoAcidSequence(new String[0]));
    }

    @Test
    void testReverseTranslateNonexistentAminoAcid() {
        assertThrows(IllegalArgumentException.class, () -> translator.reverseTranslateAminoAcidSequence("Met-His-R2D2"));
    }

    @Test
    void testReverseTranslateWrongDelimiter() {
        assertThrows(IllegalArgumentException.class, () -> translator.reverseTranslateAminoAcidSequence("Phe,His,Cys"));
    }

    @Test
    void testReverseTranslateAminoAcidWithOneRnaSeq() {
        assertEquals(Collections.singletonList("AUG"), translator.reverseTranslateAminoAcidSequence("Met"));
    }

    @Test
    void testReverseTranslateAminoAcidWithMultipleRnaSeqs() {
        assertEquals(List.of("AAA", "AAG"), translator.reverseTranslateAminoAcidSequence("Lys"));
    }

    @Test
    void testReverseTranslateLongAminoAcidSequence() {
        List<String> expected = List.of("UUUCAUUGU", "UUUCAUUGC", "UUUCACUGU", "UUUCACUGC", "UUCCAUUGU", "UUCCAUUGC",
                                        "UUCCACUGU", "UUCCACUGC");
        assertEquals(expected, translator.reverseTranslateAminoAcidSequence("Phe-His-Cys"));
    }
}

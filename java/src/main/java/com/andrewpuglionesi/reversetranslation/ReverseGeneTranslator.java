package com.andrewpuglionesi.reversetranslation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Translates amino acid sequences to RNA sequences.
 */
public class ReverseGeneTranslator {

    private final Map<String, List<String>> aminoAcidToRnaMap;

    /**
     * @throws IOException if the configuration file mapping amino acids to RNA sequences cannot be read.
     */
    public ReverseGeneTranslator() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        try (InputStream json = Thread.currentThread().getContextClassLoader().getResourceAsStream("codon_map.json")) {
            aminoAcidToRnaMap = mapper.readValue(json, new TypeReference<>() {});
        }
    }

    /**
     * @param aminoAcidSeq an amino acid sequence containing three-letter abbreviations separated by hyphens. For
     *                     example: His-Leu-Met-Gln-Tyr
     * @return a list of all mRNA sequences that could have possibly produced the amino acid sequence.
     * @throws IllegalArgumentException if an invalid amino acid or format is provided.
     */
    public List<String> reverseTranslateAminoAcidSequence(@NonNull final String aminoAcidSeq) {
        if (aminoAcidSeq.isBlank()) {
            return new ArrayList<>(List.of(""));
        }
        String[] tokenized = aminoAcidSeq.split("-");
        return reverseTranslateAminoAcidSequence(tokenized);
    }

    /**
     * @param aminoAcidSeq an array of standard three-letter amino acid abbreviations.
     * @return a list of all mRNA sequences that could have possibly produced the amino acid sequence.
     * @throws IllegalArgumentException if an invalid amino acid is provided.
     */
    public List<String> reverseTranslateAminoAcidSequence(@NonNull final String[] aminoAcidSeq) {
        List<String> rnaSequences = new ArrayList<>();
        collectRnaSequences(aminoAcidSeq, 0, "", rnaSequences);
        return rnaSequences;
    }

    private void collectRnaSequences(final String[] aminoAcidSeq, final int index, final String currentRnaSeq,
                                     final List<String> rnaSequences) {
        if (index == aminoAcidSeq.length) {
            rnaSequences.add(currentRnaSeq);
        } else {
            String aminoAcid = aminoAcidSeq[index];
            List<String> rnaCodons = aminoAcidToRnaMap.get(aminoAcid);
            if (rnaCodons == null) {
                throw new IllegalArgumentException("Unexpected amino acid: " + aminoAcid);
            }
            for (String rnaCodon : rnaCodons) {
                collectRnaSequences(aminoAcidSeq, index + 1, currentRnaSeq + rnaCodon, rnaSequences);
            }
        }
    }
}

package com.andrewpuglionesi.predictivet9;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * An implementation of the T9 algorithm.
 */
public class PredictiveT9Service {

    /**
     * Maps letters to digits on a phone keypad.
     */
    private final Map<Character, Integer> letterToDigitMap;

    /**
     * Maps a string of digits (e.g., "3333") to words that can be typed with those digits.
     * The words will be sorted in descending order by frequency.
     */
    private final Map<String, TreeSet<WordWithFrequency>> t9WordMap;

    /**
     * @param wordFrequencyCsvPath file path for CSV containing word frequency data.
     * @throws IOException if the CSV file cannot be read.
     * @throws IllegalArgumentException if the CSV file is not correctly formatted.
     */
    public PredictiveT9Service(final String wordFrequencyCsvPath) throws IOException {
        this.letterToDigitMap = buildLetterToDigitMap();
        this.t9WordMap = buildT9WordMap(wordFrequencyCsvPath);
    }
    
    /**
     * Given a sequence of numbers, returns all words in the dataset that could be typed with that sequence using T9.
     * Output will be ordered by word frequency (most popular words first).
     * @param numSequence a sequence of numbers with no spaces or separators.
     * @return a list of words matching the sequence, or an empty list if none are found.
     */
    public List<String> predictWord(final String numSequence) {
        if (numSequence == null || numSequence.isBlank() || !t9WordMap.containsKey(numSequence)) {
            return Collections.emptyList();
        }

        return t9WordMap.get(numSequence)
            .stream()
            .map(WordWithFrequency::getWord)
            .toList();
    }

    private Map<Character, Integer> buildLetterToDigitMap() {
        Map<Character, Integer> map = new ConcurrentHashMap<>();

        Stream.of('a', 'b', 'c').forEach(character -> map.put(character, 2));
        Stream.of('d', 'e', 'f').forEach(character -> map.put(character, 3));
        Stream.of('g', 'h', 'i').forEach(character -> map.put(character, 4));
        Stream.of('j', 'k', 'l').forEach(character -> map.put(character, 5));
        Stream.of('m', 'n', 'o').forEach(character -> map.put(character, 6));
        Stream.of('p', 'q', 'r', 's').forEach(character -> map.put(character, 7));
        Stream.of('t', 'u', 'v').forEach(character -> map.put(character, 8));
        Stream.of('w', 'x', 'y', 'z').forEach(character -> map.put(character, 9));
        
        return map;
    }

    private Map<String, TreeSet<WordWithFrequency>> buildT9WordMap(final String wordFrequencyCsvPath) throws IOException {
        Map<String, TreeSet<WordWithFrequency>> map = new ConcurrentHashMap<>();

        try (InputStream csv = Thread.currentThread().getContextClassLoader().getResourceAsStream(wordFrequencyCsvPath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(csv));
             Stream<String> lines = reader.lines()) {
            
            lines.forEach(line -> {
                WordWithFrequency wordWithFrequency = readCsvLine(line);
                String t9Digits = convertWordToT9Digits(wordWithFrequency.getWord());
                
                if (map.containsKey(t9Digits)) {
                    TreeSet<WordWithFrequency> wordSet = map.get(t9Digits);
                    wordSet.add(wordWithFrequency);
                } else {
                    TreeSet<WordWithFrequency> wordSet = new TreeSet<>();
                    wordSet.add(wordWithFrequency);
                    map.put(t9Digits, wordSet);
                }
            });
        }

        return map;
    }

    private WordWithFrequency readCsvLine(final String csvLine) {
        try {
            String[] values = csvLine.toLowerCase(Locale.US).split(",");
            String word = values[0];
            String frequency = values[1];

            return WordWithFrequency.builder()
                        .word(word)
                        .frequency(Integer.parseInt(frequency))
                        .build();

        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            throw new IllegalArgumentException("CSV file is not correctly formatted", ex);
        }
    }

    private String convertWordToT9Digits(final String word) {
        StringBuilder t9Digits = new StringBuilder();
        for (Character character : word.toCharArray()) {
            Integer digit = this.letterToDigitMap.get(character);
            if (digit != null) {
                t9Digits.append(digit);
            }
        }
        return t9Digits.toString();
    }

    /**
     * Represents a word from the word frequency dataset.
     */
    @Getter
    @Setter
    @Builder
    private static class WordWithFrequency implements Comparable<WordWithFrequency> {
        private String word;
        private int frequency;

        @Override
        public int compareTo(final WordWithFrequency other) {
            // Words with higher frequency get a higher priority
            return Integer.compare(other.frequency, this.frequency);
        }
    }
}
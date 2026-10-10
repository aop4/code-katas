package com.andrewpuglionesi.autocomplete;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class PrefixTrieTest {
    
    @Test
    void constructorCalledWithInvalidWords() {
        PrefixTrie trie = new PrefixTrie(Stream.of("", null));
        assertTrue(trie.getNodeAtPath("").isEmpty());
        assertTrue(trie.getNodeAtPath(null).isEmpty());
    }

    @Test
    void getNodeAtPathEmptyTrie() {
        PrefixTrie trie = new PrefixTrie(Stream.of());
        assertTrue(trie.getNodeAtPath("").isEmpty());
        assertTrue(trie.getNodeAtPath("a").isEmpty());
    }

    @Test
    void getNodeAtPathOneLetterWord() {
        PrefixTrie trie = new PrefixTrie(Stream.of("a"));
        assertTrue(trie.getNodeAtPath(null).isEmpty());
        assertTrue(trie.getNodeAtPath("").isEmpty());
        assertTrue(trie.getNodeAtPath("a").isPresent());
        assertTrue(trie.getNodeAtPath("b").isEmpty());
    }

    @Test
    void getNodeAtPathWithPrefixesOfWord() {
        PrefixTrie trie = new PrefixTrie(Stream.of("cat"));
        assertTrue(trie.getNodeAtPath("c").isPresent());
        assertTrue(trie.getNodeAtPath("ca").isPresent());
        assertTrue(trie.getNodeAtPath("cat").isPresent());
    }

    @Test
    void getNodeAtPathWithExtensionsOfWord() {
        PrefixTrie trie = new PrefixTrie(Stream.of("cat"));
        assertTrue(trie.getNodeAtPath("cats").isEmpty());
        assertTrue(trie.getNodeAtPath("catsup").isEmpty());
    }

    @Test
    void getNodeAtPathIntersectingWords() {
        PrefixTrie trie = new PrefixTrie(Stream.of("bat", "bats", "bit", "bin", "bytes"));
        assertTrue(trie.getNodeAtPath("bat").isPresent());
        assertTrue(trie.getNodeAtPath("bats").isPresent());
        assertTrue(trie.getNodeAtPath("bit").isPresent());
        assertTrue(trie.getNodeAtPath("bin").isPresent());
        assertTrue(trie.getNodeAtPath("bytes").isPresent());

        assertTrue(trie.getNodeAtPath("ban").isEmpty());
        assertTrue(trie.getNodeAtPath("bans").isEmpty());
    }

    @Test
    void getNodeAtPathNonIntersectingWords() {
        PrefixTrie trie = new PrefixTrie(Stream.of("abc", "def"));
        assertTrue(trie.getNodeAtPath("abc").isPresent());
        assertTrue(trie.getNodeAtPath("def").isPresent());

        assertTrue(trie.getNodeAtPath("aef").isEmpty());
        assertTrue(trie.getNodeAtPath("abf").isEmpty());
        assertTrue(trie.getNodeAtPath("dbc").isEmpty());
        assertTrue(trie.getNodeAtPath("dec").isEmpty());
    }

    @Test
    void findWordsWithPrefixNonExistentPrefix() {
        PrefixTrie trie = new PrefixTrie(Stream.of("recognize"));
        List<String> wordsWithPrefix = trie.findWordsWithPrefix("pikachu", 1);
        assertEquals(Collections.emptyList(), wordsWithPrefix);
    }
    
    @Test
    void findWordsWithPrefixExactMatch() {
        PrefixTrie trie = new PrefixTrie(Stream.of("recognize"));
        List<String> wordsWithPrefix = trie.findWordsWithPrefix("recognize", 1);
        assertEquals(Collections.singletonList("recognize"), wordsWithPrefix);
    }

    @Test
    void findWordsWithPrefixMultipleMatches() {
        PrefixTrie trie = new PrefixTrie(Stream.of("recognizable", "recognize", "recognized", "recognizes",
                                                   "recognizing", "record"));
        List<String> wordsWithPrefix = trie.findWordsWithPrefix("recogniz", Integer.MAX_VALUE);
        assertEquals(List.of("recognize", "recognized", "recognizes", "recognizing", "recognizable"), wordsWithPrefix);
    }

    @Test
    void findWordsWithPrefixRestrictiveLimit() {
        PrefixTrie trie = new PrefixTrie(Stream.of("recognizable", "recognize", "recognized", "recognizes",
                                                   "recognizing", "record"));
        List<String> wordsWithPrefix = trie.findWordsWithPrefix("recogniz", 2);
        assertEquals(List.of("recognize", "recognized"), wordsWithPrefix);
    }

    @Test
    void findWordsWithPrefix_SpecialCharactersMustCountTowardStringLength() {
        PrefixTrie trie = new PrefixTrie(Stream.of("australia", "australia's", "australian", "australians"));
        List<String> wordsWithPrefix = trie.findWordsWithPrefix("australi", 2);
        assertEquals(List.of("australia", "australian"), wordsWithPrefix);
    }

    @Test
    void findWordsWithPrefixSpecialCharacterMatching() {
        String specialChars = "ε!#9⁂[.+Δ";
        PrefixTrie trie = new PrefixTrie(Stream.of(specialChars));
        List<String> wordsWithPrefix = trie.findWordsWithPrefix(specialChars, Integer.MAX_VALUE);
        assertEquals(List.of(specialChars), wordsWithPrefix);
    }
    
    @Test
    void findWordsWithPrefixLargeWordSet() throws IOException {
        String wordListPath = "wikipedia-word-list.txt";
        try (InputStream wordListFile = Thread.currentThread().getContextClassLoader().getResourceAsStream(wordListPath);
             BufferedReader fileReader = new BufferedReader(new InputStreamReader(wordListFile));
             Stream<String> wordsFromFile = fileReader.lines()) {

            PrefixTrie trie = new PrefixTrie(wordsFromFile);

            List<String> wordsWithPrefix = trie.findWordsWithPrefix("a", Integer.MAX_VALUE);
            assertEquals(4537, wordsWithPrefix.size());
            // a is the shortest word beginning with 'a'
            assertEquals("a", wordsWithPrefix.get(0));
            // antidifferentation is the longest word in the dataset beginning with 'a'
            assertEquals("antidifferentiation", wordsWithPrefix.get(wordsWithPrefix.size() - 1));

            wordsWithPrefix = trie.findWordsWithPrefix("subtract", Integer.MAX_VALUE);
            assertEquals(List.of("subtract", "subtracts", "subtracted", "subtracting", "subtraction", "subtractive", "subtractions"), wordsWithPrefix);
        }
    }
}
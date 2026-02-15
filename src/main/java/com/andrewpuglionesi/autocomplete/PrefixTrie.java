package com.andrewpuglionesi.autocomplete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.TreeMap;
import java.util.stream.Stream;

import com.google.common.annotations.VisibleForTesting;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * A trie data structure, i.e., a tree optimized for finding strings with a given prefix.
 * The trie is initialized with a dictionary of words. Each character in a word is represented as a node in the trie,
 * and words with a common prefix will share nodes in the trie until they diverge. For example, to represent "bit" and
 * "bin," only four nodes are needed:
 *          b
 *          |
 *          i
 *         / \
 *        t   n
 * Because the {@link Character} type is used to represent characters, only unicode characters between U+0000 to U+FFFF
 * should be used.
 */
public class PrefixTrie {

    private final TrieNode root = new TrieNode();

    /**
     * Initializes the trie with a dictionary of words.
     */
    public PrefixTrie(@NonNull final Stream<String> validWords) {
        validWords.forEach(this::addWord);
    }

    /**
     * Adds a new word to the trie.
     * If <code>word</code> is null or empty, no action is taken.
     */
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    public void addWord(final String word) {
        if (word == null || word.isEmpty()) {
            return;
        }
        
        TrieNode curr = this.root;

        for (Character character : word.toCharArray()) {
            if (curr.neighbors.containsKey(character)) {
                curr = curr.neighbors.get(character);
            } else {
                TrieNode next = new TrieNode();
                curr.neighbors.put(character, next);
                curr = next;
            }
        }
        curr.setEndOfWord(true);
    }

    /**
     * Returns a list of words in the trie that begin with the provided prefix.
     * @param limit the maximum number of words to return.
     * 
     * @return a list of words sorted primarily by word length, and sorted alphabetically when multiple words in the
     * output have equal length.
     * 
     * @implNote This method will first retrieve the node corresponding to the last character of the prefix. If
     * present, it will traverse the trie to find complete words that are descendants of that node. The traversal is
     * iterative, not recursive.
     * 
     * No literal sorting takes place, and only words that will be returned are collected. Ordering and limiting are
     * enforced by indexing of the underlying data and a queue-based traversal strategy.
     */
    public List<String> findWordsWithPrefix(final String prefix, final int limit) {
        Optional<TrieNode> lastCharOfPrefix = this.getNodeAtPath(prefix);
        if (lastCharOfPrefix.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<String> wordsWithPrefix = new ArrayList<>();

        Queue<TrieNodeWithPath> toVisit = new LinkedList<>();
        toVisit.add(new TrieNodeWithPath(lastCharOfPrefix.get(), prefix));

        while (wordsWithPrefix.size() < limit && !toVisit.isEmpty()) {
            TrieNodeWithPath curr = toVisit.poll();
            if (curr.node.isEndOfWord()) {
                wordsWithPrefix.add(curr.path);
            }
            curr.node.neighbors.forEach((character, neighbor) -> {
                toVisit.add(new TrieNodeWithPath(neighbor, curr.path + character));
            });
        }

        return wordsWithPrefix;
    }

    @VisibleForTesting
    Optional<TrieNode> getNodeAtPath(final String path) {
        if (path == null || path.isEmpty()) {
            return Optional.empty();
        }
        TrieNode curr = this.root;
        for (Character character : path.toCharArray()) {
            if (curr.neighbors.containsKey(character)) {
                curr = curr.neighbors.get(character);
            } else {
                return Optional.empty();
            }
        }
        return Optional.of(curr);
    }
    
    /**
     * A node representing a character in the trie. To save space, a node does not contain its own character, but
     * rather a mapping between itself and subsequent characters.
     */
    @Getter
    private static class TrieNode {
        /**
         * A mapping from this node to other nodes representing subsequent characters. For example, if the word "cat"
         * is present in the trie, the root node will have a map entry with key "c", which points to a node with a map
         * entry with key "a", and so on.
         */
        @SuppressWarnings("PMD.LooseCoupling")
        private final TreeMap<Character, TrieNode> neighbors = new TreeMap<>();
        
        /**
         * Whether this node represents the end of a complete word.
         */
        @Setter
        private boolean isEndOfWord;
    }

    private static record TrieNodeWithPath(TrieNode node, String path) {}
}

package com.andrewpuglionesi.yamlparserlite;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Builds trees of {@link YamlNode}s from YAML file inputs.
 */
public class YamlBuilder {
    private static final int SPACES_PER_INDENT = 2;

    /**
     * Builds a tree of {@link YamlNode}s from a file.
     * @param filePath the file path of an existing YAML file.
     * @return the root of the YAML tree that is constructed.
     * @throws IOException if the file at {@code filePath} cannot be opened or read.
     * @throws YamlFormatException if the content of the file is not properly formatted.
     */
    public YamlNode buildFromFile(final String filePath) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(filePath))) {
            return this.buildFromReadable(reader);
        }
    }

    /**
     * Builds a tree of {@link YamlNode}s from a {@link Readable}.
     * @param readable a {@link Readable} whose contents represent a YAML file.
     * @return the root of the YAML tree that is constructed.
     * @throws YamlFormatException if the content of the Readable is not properly formatted.
     */
    public YamlNode buildFromReadable(final Readable readable) throws YamlFormatException {
        final YamlNode root = new YamlNode();
        final Stack<YamlNode> stack = new Stack<>();
        stack.push(root);
        try (Scanner scanner = new Scanner(readable)) {
            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine(); // could replace leading tabs with <SPACES_PER_INDENT space characters> to allow indentation by tabs
                if (StringUtils.isNotBlank(line)) {
                    this.parseLine(line, stack);
                }
            }
        }
        return root;
    }

    @SuppressWarnings("PMD.PrematureDeclaration")
    private void parseLine(final String line, final Stack<YamlNode> stack) throws YamlFormatException {
        final Matcher matcher = this.parseRegexGroups(line);

        final String indent = matcher.group("indent");
        final String key = matcher.group("key");
        final String value = matcher.group("value");

        final int indentationLength = indent.length();

        if (indentationLength % SPACES_PER_INDENT != 0) {
            throw new YamlFormatException(String.format("Error parsing YAML. Invalid indentation (please indent by %d spaces). Line: %s", SPACES_PER_INDENT, line));
        }

        final int indentationLevel = indentationLength / SPACES_PER_INDENT;

        // the root has an indentation level of -1, its first child 0, and so on.
        final int parentIndentationLevel = stack.size() - 2;

        if (indentationLevel > parentIndentationLevel + 1) {
            throw new YamlFormatException("Error parsing YAML. Invalid indentation (no parent node for this field): " + line);
        }

        /* If this node appears at the same indentation as the head of the stack or an even lower level, pop the stack
         * until its parent is the head. This covers cases where the current key occurs right after a nested object, e.g.
         * <code>
         * key1:
         *   key2: someValue
         * currentKey: currentValue
         * </code>
         */
        for (int i = parentIndentationLevel; i >= indentationLevel; i--) {
            stack.pop();
        }

        if (StringUtils.isNotBlank(value)) {
            stack.peek().addValue(key, this.parseValue(value));
        } else {
            // The node does not have a value, but it may be a parent. Add an empty node to the stack in case it turns
            // out to have children.
            final YamlNode curr = new YamlNode();
            stack.peek().addChild(key, curr);
            stack.push(curr);
        }
    }

    @VisibleForTesting
    Matcher parseRegexGroups(final String line) throws YamlFormatException {
        final Pattern yamlLine = Pattern.compile("(?<indent>[ ]*)(?<key>[^\\s:]+)\\s*:(?<value>.*)");

        final Matcher matcher = yamlLine.matcher(line);
        if (!matcher.matches()) {
            throw new YamlFormatException("Error parsing YAML. Invalid format in line: " + line);
        }
        return matcher;
    }

    @VisibleForTesting
    Object parseValue(final String value) throws YamlFormatException {
        final String trimmed = value.trim();
        final Pattern yamlArray = Pattern.compile("\\[(?<arrayContents>.*)\\]");
        final Matcher matcher = yamlArray.matcher(trimmed);
        if (!matcher.matches()) {
            return trimmed; // value is not an array, treat it as a string
        }
        final String arrayContents = matcher.group("arrayContents");
        if (StringUtils.isBlank(arrayContents)) {
            return Collections.emptyList();
        }
        if (arrayContents.trim().startsWith(",")
               || arrayContents.replaceAll("\\s", "").contains(",,")) {
            throw new YamlFormatException("List is not parseable: " + value);
        }
        return Arrays.stream(arrayContents.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }
}

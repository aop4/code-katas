package com.andrewpuglionesi.yamlparserlite;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reads a YAML file into memory and processes queries of the YAML data.
 */
public class YamlReader {
    /**
     * The root of a tree of YAML nodes representing a YAML file.
     */
    private final YamlNode root;

    /**
     * Builds a YamlReader based on the contents of the file at {@code filePath}.
     * @param filePath path to a YAML file.
     * @throws IOException if the file at {@code filePath} cannot be opened or read.
     * @throws YamlFormatException if the content of the file is not properly formatted.
     */
    public YamlReader(final String filePath) throws IOException {
        this.root = new YamlBuilder().buildFromFile(filePath);
    }

    /**
     * Attempts to retrieve a value from the YAML tree represented by this {@link YamlReader}.
     * @param query a query string used to retrieve the value. To retrieve a top level field, provide the name of its
     *              key. To retrieve a field from a nested object, separate the name of each field in the path with a
     *              period. To retrieve a specific value from a list, use an array index as follows:
     *              <code>account.stock.holdings[0]</code>.
     * @return a String if the value is a string, a List of strings ({@code List<String>}) if the value is an array, or
     * null when the value is blank or nonexistent.
     * @throws IndexOutOfBoundsException if an invalid array index is used in the query. Arrays are 0-indexed.
     */
    public Object getValue(final String query) {
        final String[] nodePath = query.split("\\.", -1);
        YamlNode curr = root;
        for (int i = 0; i < nodePath.length - 1; i++) {
            curr = curr.getChildren().get(nodePath[i]);
            // when any key in the path is absent, short circuit because there is no hope of finding the desired value.
            if (curr == null) {
                return null;
            }
        }
        final String lastKey = nodePath[nodePath.length - 1];
        // handle queries that attempt to index a list
        final Pattern arrayIndex = Pattern.compile("(?<listKey>.+)\\[\\s*(?<listIndex>-*[0-9]+)\\s*\\]");
        final Matcher matcher = arrayIndex.matcher(lastKey);
        if (matcher.matches()) {
            final String listKey = matcher.group("listKey").trim();
            final int listIndex = Integer.parseInt(matcher.group("listIndex"));
            final Object currValue = curr.getValues().get(listKey);
            if (currValue instanceof List) {
                return ((List<?>) currValue).get(listIndex);
            }
        }

        return curr.getValues().get(lastKey);
    }
}

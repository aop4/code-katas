package com.andrewpuglionesi.yamlparserlite;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents an object/dictionary in a YAML tree. This object contains a key-value map for sub-objects and a key-value
 * map for all other types of values.
 */
@Getter
public class YamlNode {
    private Map<String, YamlNode> children = new ConcurrentHashMap<>();
    private Map<String, Object> values = new ConcurrentHashMap<>();

    /**
     * Adds a key-value pair under this node.
     * @param key key for the value.
     * @param value value to store.
     * @throws YamlFormatException if the key is already present.
     */
    public void addValue(final String key, final Object value) throws YamlFormatException {
        if (this.containsKey(key)) {
            throw new YamlFormatException(String.format("Error adding field. The field %s is already defined in this object.", key));
        }
        this.values.put(key, value);
    }

    /**
     * Adds a child node under this node.
     * @param key key for the child node.
     * @param value child node to store.
     * @throws YamlFormatException if the key is already present.
     */
    public void addChild(final String key, final YamlNode value) throws YamlFormatException {
        if (this.containsKey(key)) {
            throw new YamlFormatException(String.format("Error adding child. The field %s is already defined in this object.", key));
        }
        this.children.put(key, value);
    }

    /**
     * @return true if {@code key} is already in use to store a child-object or a value.
     */
    public boolean containsKey(final String key) {
        return this.children.containsKey(key) || this.values.containsKey(key);
    }
}

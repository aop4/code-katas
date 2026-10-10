package com.andrewpuglionesi.yamlparserlite;

import java.io.IOException;

/**
 * An exception indicating that there was an error parsing YAML input due to invalid formatting.
 */
public class YamlFormatException extends IOException {
    public static final long serialVersionUID = 7394271;

    /**
     * Constructor.
     * @param message exception detail message, ideally explaining the cause of the exception.
     */
    public YamlFormatException(final String message) {
        super(message);
    }
}

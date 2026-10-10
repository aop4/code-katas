package com.andrewpuglionesi.yamlparserlite;

import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class YamlBuilderTest {
    private final YamlBuilder yamlBuilder = new YamlBuilder();

    @Test
    void parseRegexGroupsNoColon() {
        assertThrows(YamlFormatException.class, () -> yamlBuilder.parseRegexGroups("key"));
    }

    @Test
    void parseRegexGroupsNoKey() {
        assertThrows(YamlFormatException.class, () -> yamlBuilder.parseRegexGroups(": value"));
    }

    @Test
    void parseRegexGroupsEmptyValue() throws YamlFormatException {
        Matcher matcher = yamlBuilder.parseRegexGroups("key:");

        assertEquals("key", matcher.group("key"));
        assertEquals("", matcher.group("value"));
        assertEquals("", matcher.group("indent"));
    }

    @Test
    void parseRegexGroupsNoWhitespace() throws YamlFormatException {
        Matcher matcher = yamlBuilder.parseRegexGroups("key:value");

        assertEquals("key", matcher.group("key"));
        assertEquals("value", matcher.group("value"));
        assertEquals("", matcher.group("indent"));
    }

    @Test
    void parseRegexGroupsMuchWhitespace() throws YamlFormatException {
        Matcher matcher = yamlBuilder.parseRegexGroups("  key    :    value   ");

        assertEquals("key", matcher.group("key"));
        assertEquals("    value   ", matcher.group("value"));
        assertEquals("  ", matcher.group("indent"));
    }

    @Test
    void parseRegexGroupsMultipleColons() throws YamlFormatException {
        Matcher matcher = yamlBuilder.parseRegexGroups("port: :8080");

        assertEquals("port", matcher.group("key"));
        assertEquals(" :8080", matcher.group("value"));
        assertEquals("", matcher.group("indent"));
    }

    @Test
    void parseRegexGroupsValueIsColon() throws YamlFormatException {
        Matcher matcher = yamlBuilder.parseRegexGroups("key: :");

        assertEquals("key", matcher.group("key"));
        assertEquals(" :", matcher.group("value"));
        assertEquals("", matcher.group("indent"));
    }

    @Test
    void parseValueNoBrackets() throws YamlFormatException {
        assertEquals("string", yamlBuilder.parseValue("string"));
    }

    @Test
    void parseValueJustLeftBracket() throws YamlFormatException {
        assertEquals("[string", yamlBuilder.parseValue("[string"));
    }

    @Test
    void parseValueJustRightBracket() throws YamlFormatException {
        assertEquals("string]", yamlBuilder.parseValue("string]"));
    }

    @Test
    void parseValueBracketsInMiddle() throws YamlFormatException {
        assertEquals("s[s]tring", yamlBuilder.parseValue("s[s]tring"));
    }

    @Test
    void parseValueEmptyList() throws YamlFormatException {
        assertEquals(Collections.emptyList(), yamlBuilder.parseValue("[]"));
    }

    @Test
    void parseValueSingletonList() throws YamlFormatException {
        assertEquals(List.of("abc"), yamlBuilder.parseValue("[abc]"));
    }

    @Test
    void parseValueListOfStrings() throws YamlFormatException {
        assertEquals(List.of("abc","def"), yamlBuilder.parseValue("[abc,def]"));
    }

    @Test
    void parseValueListContainingEmptyList() throws YamlFormatException {
        assertEquals(List.of("[]"), yamlBuilder.parseValue("[[]]"));
    }

    @Test
    void parseValueListOfLists() throws YamlFormatException {
        assertEquals(List.of("[a]", "[b]"), yamlBuilder.parseValue("[[a], [b]]"));
    }

    @Test
    void parseValueListIsOneComma() {
        assertThrows(YamlFormatException.class, () -> yamlBuilder.parseValue("[ , ]"));
    }

    @Test
    void parseValueListIsMultipleCommas() {
        assertThrows(YamlFormatException.class, () -> yamlBuilder.parseValue("[ , , , ]"));
    }

    @Test
    void parseValueLeadingComma() {
        assertThrows(YamlFormatException.class, () -> yamlBuilder.parseValue("[,val]"));
    }

    @Test
    void parseValueTrailingComma() throws YamlFormatException {
        assertEquals(List.of("val"), yamlBuilder.parseValue("[val,]"));
    }

    @Test
    void buildFromReadableEmptyYaml() throws YamlFormatException {
        YamlNode root = yamlBuilder.buildFromReadable(new StringReader(""));
        assertEquals(0, root.getChildren().size());
        assertEquals(0, root.getValues().size());
    }

    @Test
    void buildFromReadableWhiteSpaceYaml() throws YamlFormatException {
        YamlNode root = yamlBuilder.buildFromReadable(new StringReader("\n\n \n \n  \t"));
        assertEquals(0, root.getChildren().size());
        assertEquals(0, root.getValues().size());
    }

    @Test
    void buildFromReadableOneKeyValuePair() throws YamlFormatException {
        YamlNode root = yamlBuilder.buildFromReadable(new StringReader(
                """
                   key1: value1
                   """
        ));
        assertEquals(0, root.getChildren().size());
        assertEquals(1, root.getValues().size());
        assertEquals("value1", root.getValues().get("key1"));
    }

    @Test
    void buildFromReadableMultipleKeyValuePairs() throws YamlFormatException {
        YamlNode root = yamlBuilder.buildFromReadable(new StringReader(
                """
                   key1: value1
                   key2: value2
                   """
        ));
        assertEquals(0, root.getChildren().size());
        assertEquals(2, root.getValues().size());
        assertEquals("value1", root.getValues().get("key1"));
        assertEquals("value2", root.getValues().get("key2"));
    }

    @Test
    void buildFromReadableOneChild() throws YamlFormatException {
        YamlNode root = yamlBuilder.buildFromReadable(new StringReader(
                """
                   child1:
                     key2: value2
                     key3: value3
                   """
        ));
        assertEquals(1, root.getChildren().size());
        assertEquals(0, root.getValues().size());

        YamlNode child1 = root.getChildren().get("child1");

        assertEquals(2, child1.getValues().size());
        assertEquals("value2", child1.getValues().get("key2"));
        assertEquals("value3", child1.getValues().get("key3"));
        assertEquals(0, child1.getChildren().size());
    }

    @Test
    void buildFromReadableNestedChild() throws YamlFormatException {
        YamlNode root = yamlBuilder.buildFromReadable(new StringReader(
                """
                   child1:
                     child2:
                       key1: value1
                   """
        ));
        assertEquals(1, root.getChildren().size());
        assertEquals(0, root.getValues().size());

        YamlNode child1 = root.getChildren().get("child1");

        assertEquals(1, child1.getChildren().size());
        assertEquals(0, child1.getValues().size());

        YamlNode child2 = child1.getChildren().get("child2");

        assertEquals(0, child2.getChildren().size());
        assertEquals(1, child2.getValues().size());
        assertEquals("value1", child2.getValues().get("key1"));
    }

    @Test
    void buildFromReadableValuesAfterIndent() throws YamlFormatException {
        YamlNode root = yamlBuilder.buildFromReadable(new StringReader(
                """
                   child1:
                     child2:
                       key1: value1
                     key2: value2
                   key3: value3
                   """
        ));
        assertEquals(1, root.getChildren().size());
        assertEquals(1, root.getValues().size());

        YamlNode child1 = root.getChildren().get("child1");

        assertEquals(1, child1.getChildren().size());
        assertEquals(1, child1.getValues().size());

        YamlNode child2 = child1.getChildren().get("child2");

        assertEquals(0, child2.getChildren().size());
        assertEquals(1, child2.getValues().size());

        assertEquals("value1", child2.getValues().get("key1"));

        assertEquals("value2", child1.getValues().get("key2"));

        assertEquals("value3", root.getValues().get("key3"));
    }

    @Test
    void buildFromReadableChildrenAfterIndent() throws YamlFormatException {
        YamlNode root = yamlBuilder.buildFromReadable(new StringReader(
                """
                   child1:
                     child2:
                       child3:
                         key1: value1
                       child4:
                         key2: value2
                     child5:
                       key3: value3
                   child6:
                     key4: value4
                   key5: value5
                   """
        ));
        assertEquals(2, root.getChildren().size());
        assertEquals(1, root.getValues().size());
        assertEquals("value5", root.getValues().get("key5"));

        YamlNode child1 = root.getChildren().get("child1");

        assertEquals(2, child1.getChildren().size());
        assertEquals(0, child1.getValues().size());

        YamlNode child2 = child1.getChildren().get("child2");

        assertEquals(2, child2.getChildren().size());
        assertEquals(0, child2.getValues().size());

        YamlNode child3 = child2.getChildren().get("child3");

        assertEquals(0, child3.getChildren().size());
        assertEquals(1, child3.getValues().size());
        assertEquals("value1", child3.getValues().get("key1"));

        YamlNode child4 = child2.getChildren().get("child4");

        assertEquals(0, child4.getChildren().size());
        assertEquals(1, child4.getValues().size());
        assertEquals("value2", child4.getValues().get("key2"));

        YamlNode child5 = child1.getChildren().get("child5");

        assertEquals(0, child5.getChildren().size());
        assertEquals(1, child5.getValues().size());
        assertEquals("value3", child5.getValues().get("key3"));

        YamlNode child6 = root.getChildren().get("child6");

        assertEquals(0, child6.getChildren().size());
        assertEquals(1, child6.getValues().size());
        assertEquals("value4", child6.getValues().get("key4"));
    }

    @Test
    void buildFromReadableArrayValue() throws YamlFormatException {
        YamlNode root = yamlBuilder.buildFromReadable(new StringReader(
                """
                   coolInstruments: [guitar, church organ, cello]
                   """
        ));
        assertEquals(0, root.getChildren().size());
        assertEquals(1, root.getValues().size());
        assertEquals(List.of("guitar", "church organ", "cello"), root.getValues().get("coolInstruments"));
    }

    @Test
    void buildFromReadableEmptyNodes() throws YamlFormatException {
        YamlNode root = yamlBuilder.buildFromReadable(new StringReader(
                """
                   child1:
                   child2:
                   """
        ));
        assertEquals(2, root.getChildren().size());
        assertEquals(0, root.getValues().size());

        YamlNode child1 = root.getChildren().get("child1");

        assertEquals(0, child1.getChildren().size());
        assertEquals(0, child1.getValues().size());

        YamlNode child2 = root.getChildren().get("child2");

        assertEquals(0, child2.getChildren().size());
        assertEquals(0, child2.getValues().size());
    }

    @Test
    void buildFromReadableChildIndentedTooFar() {
        assertThrows(YamlFormatException.class, () -> {
            yamlBuilder.buildFromReadable(new StringReader(
                    """
                       key1:
                          value1:
                       """
            ));
        });
    }

    @Test
    void buildFromReadableParentIsNotAvailable() {
        assertThrows(YamlFormatException.class, () -> {
            yamlBuilder.buildFromReadable(new StringReader(
                    """
                       child1:
                         child2:
                           key1: value1
                       key2: value2
                         key3: value3
                       """
            ));
        });
    }

    @Test
    void buildFromReadableKeyIsAlreadyDefined() {
        assertThrows(YamlFormatException.class, () -> {
            yamlBuilder.buildFromReadable(new StringReader(
                    """
                       key1: value1
                       key1: value2
                       """
            ));
        });
    }

    @Test
    void buildFromReadableChildIsAlreadyDefined() {
        assertThrows(YamlFormatException.class, () -> {
            yamlBuilder.buildFromReadable(new StringReader(
                    """
                       child1:
                         key1: value1
                       child1:
                       """
            ));
        });
    }
}

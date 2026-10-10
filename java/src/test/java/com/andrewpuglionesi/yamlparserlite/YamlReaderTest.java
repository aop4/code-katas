package com.andrewpuglionesi.yamlparserlite;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * These tests do not cover reading a YAML file with invalid structure; {@see YamlBuilderTest} for that.
 */
public class YamlReaderTest {
    private YamlReader yamlReader;

    @BeforeEach
    void setup() throws IOException {
        this.yamlReader = new YamlReader("src/test/resources/yamlparserlite/test.yaml");
    }

    @Test
    void getValueEmptyStringQuery() {
        assertEquals(null, this.yamlReader.getValue(""));
    }

    @Test
    void getValueWhitespaceQuery() {
        assertEquals(null, this.yamlReader.getValue(" "));
    }

    @Test
    void getValueTopLevelKeyExists() {
        assertEquals("Alan Turing", this.yamlReader.getValue("author"));
    }

    @Test
    void getValueTopLevelKeyDoesNotExist() {
        assertEquals(null, this.yamlReader.getValue("createdDate"));
        assertEquals(null, this.yamlReader.getValue("city")); // it doesn't retrieve nodes--only values
    }

    @Test
    void getValueNestedKeyExists() {
        assertEquals("Pennsylvania", this.yamlReader.getValue("city.state"));
        assertEquals("Pittsburgh", this.yamlReader.getValue("city.name"));
        assertEquals("Ed Gainey", this.yamlReader.getValue("city.mayor"));
        assertEquals("Steelers", this.yamlReader.getValue("city.sports.football.teamName"));
        assertEquals("Pirates", this.yamlReader.getValue("city.sports.baseball.teamName"));
    }

    @Test
    void getValueNestedKeyDoesNotExist() {
        assertEquals(null, this.yamlReader.getValue("city.mascot"));
        assertEquals(null, this.yamlReader.getValue("city.sports.soccer"));
        assertEquals(null, this.yamlReader.getValue("city.sports.football.stadium"));
    }

    @Test
    void getValueKeyInMiddleOfPathDoesNotExist() {
        assertEquals(null, this.yamlReader.getValue("city.transit.ferries"));
    }

    @Test
    void getValueKeyExistsButHasNoValue() {
        assertEquals(null, this.yamlReader.getValue("city.boroughs"));
        assertEquals(null, this.yamlReader.getValue("city.sports")); // it doesn't retrieve nodes--only values
    }

    @Test
    void getValueDoubleDotsInQueryPath() {
        // this..that.that
        assertEquals(null, this.yamlReader.getValue("city..name"));
        assertEquals(null, this.yamlReader.getValue("city.sports..football"));
    }

    @Test
    void getValueQueryPathStartsWithDot() {
        assertEquals(null, this.yamlReader.getValue(".city.name"));
        assertEquals(null, this.yamlReader.getValue(".name"));
    }

    @Test
    void getValueQueryPathEndsWithDot() {
        assertEquals(null, this.yamlReader.getValue("city."));
        assertEquals(null, this.yamlReader.getValue("city.name."));
        assertEquals(null, this.yamlReader.getValue("city.boroughs."));
        assertEquals(null, this.yamlReader.getValue("city.sports."));
    }

    @Test
    void getValueKeyMappedToEmptyArray() {
        assertEquals(Collections.emptyList(), this.yamlReader.getValue("city.weatherForecast"));
    }

    @Test
    void getValueKeyMappedToPopulatedArray() {
        assertEquals(List.of("rainy", "cold", "humid"), this.yamlReader.getValue("city.weatherEvents"));
    }

    @Test
    void getValueQueryPathStartsWithListIndex() {
        assertEquals(null, this.yamlReader.getValue("[0]"));
        assertEquals(null, this.yamlReader.getValue("[0].name"));
    }

    @Test
    void getValueValueIsNotAListButQueryIndexesIt() {
        assertEquals(null, this.yamlReader.getValue("city[0]"));
        assertEquals(null, this.yamlReader.getValue("city.name[0]"));
        assertEquals(null, this.yamlReader.getValue("city.sports[0]"));
        assertEquals(null, this.yamlReader.getValue("city.boroughs[0]"));
    }

    @Test
    void getValueValidListIndex() {
        assertEquals("rainy", this.yamlReader.getValue("city.weatherEvents[0]"));
        assertEquals("cold", this.yamlReader.getValue("city.weatherEvents[1]"));
        assertEquals("humid", this.yamlReader.getValue("city.weatherEvents[2]"));
    }

    @Test
    void getValueListIndexOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> this.yamlReader.getValue("city.weatherEvents[-1]"));
        assertThrows(IndexOutOfBoundsException.class, () -> this.yamlReader.getValue("city.weatherEvents[3]"));
        assertThrows(IndexOutOfBoundsException.class, () -> this.yamlReader.getValue("city.weatherForecast[0]"));
    }

    @Test
    void getValueWhitespaceAroundArrayIndex() {
        assertEquals("rainy", this.yamlReader.getValue("city.weatherEvents[ 0 ]"));
    }
}

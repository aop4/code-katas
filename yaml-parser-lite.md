# YAML Parser Lite
## Prompt
Build a YAML parser capable of reading a YAML file with key-value pairs that may include nested objects.

## Specification
Build a class, `YamlReader`, that accepts the path to a YAML file in its constructor. Implement a `getValue()` method
that accepts a string representation of a field in the YAML structure and attempts to retrieve the corresponding value.

### Example input
```yaml
city:
  name: Pittsburgh
  state: Pennsylvania
  weatherEvents: [rainy, cold, humid]
  boroughs:
  sports:
    football:
      teamName: Steelers
    baseball:
      teamName: Pirates
author: Alan Turing
```
### Example output:
```java
YamlReader yamlReader = new YamlReader("pathToFile");
yamlReader.getValue("city.name") // "Pittsburgh"
yamlReader.getValue("city.sports.football.teamName") // "Steelers"
yamlReader.getValue("city.sports.football") // return null. This stores an object, not a value.
yamlReader.getValue("city.weatherEvents") // return a list of strings: ["rainy", "cold", "humid"]
yamlReader.getValue("city.weatherEvents[1]") // "cold"
yamlReader.getValue("city.boroughs") // return null
yamlReader.getValue("author") // "Alan Turing"
```

You may assume the YAML file is properly formatted with consistent indentation of 2 spaces. The above example covers all
the YAML syntax you can expect. There is no need to support other formats for lists or nesting lists/objects within
a list. Every value may be treated as a string, regardless of its typically inferred type in YAML.

Oh ya... don't use a library that parses YAML. That's the point of the exercise.

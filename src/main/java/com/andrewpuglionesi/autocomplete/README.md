# Autocomplete

## Problem statement
Write an autocomplete utility that retrieves words with a given prefix.

Your autocomplete tool will be initialized with a list of valid words. It should accept as arguments a string (`prefix`) and an integer (`limit`). When used, it will return up to `limit` strings with the provided `prefix`.

The returned list of strings should be sorted primarily by their length, and then alphabetically. In other words, find the shortest words with the provided prefix, sorted alphabetically.

If you're interested in testing with a large dataset, a long list of English words can be found [here](../../../../resources/wikipedia-word-list.txt). Feel free to initialize your autocomplete tool with either these or a smaller dataset.

## Examples
Input:
```
Valid words: ["recognizable", "recognize", "recognized", "recognizes", "recognizing", "record"]

prefix: "recogniz"
limit: 5
```

Output:
```
["recognize", "recognized", "recognizes", "recognizing", "recognizable"]
```

Explanation: The output is ordered primarily by word length, and then alphabetically. (Since there are only 5 matching words, the `limit` of 5 has no impact.)

<hr/>

Input:
```
Valid words: ["recognizable", "recognize", "recognized", "recognizes", "recognizing", "record"]

prefix: "recogniz"
limit: 2
```

Output:
```
["recognize", "recognized"]
```

Explanation: This is the same input as before, but with a smaller limit. Remember that we are simultaneously sorting and limiting the output.

`recognize` is the shortest word in our small valid word list, with `recognized` and `recognizes` tied for second-shortest. The tie is broken by alphabetical order.

<hr/>

Input:
```
Valid words: ["recognize"]

prefix: "recognize"
limit: 1
```

Output:
```
["recognize"]
```

Explanation: Feel free to include the prefix as part of the output if it's one of the words in the dataset.

<hr/>

Input:
```
Valid words: ["australia", "australia's", "australian", "australians"]

prefix: "australi"
limit: 2
```

Output:
```
["australia", "australian"]
```

Explanation: Special characters like apostrophes may be present in the dataset. They will count as a character, so `australian` is considered a shorter word than `australia's`. Feel free to assume all words and prefixes are lowercase, even if they'd usually be capitalized.


## Attribution  
- The provided list of English words is derived from the Wortschatz Leipzig dataset of word frequencies in Wikipedia articles (see: https://www.wortschatz.uni-leipzig.de/en/download/eng, Wikipedia 2021 100K dataset). I cleaned the data by:
    1. Excluding words containing whitespace, numbers, and special characters other than `'`
    2. Converting the words to lowercase
    3. Merging duplicate entries (e.g., the same word but with different capitalization)
    4. Sorting the words alphabetically
# Roman Numerals
## Problem statement
Write a solution that can:
1. Write any integer value between 1 and 3,999 as a Roman numeral.
2. Read any valid Roman numeral between 1 and 3,999 into an integer value.

Someone has to know how to write those Superbowl numbers, man.

## Examples and constraints
Let's start with the most basic numerals:
```
roman_numeral_to_int('I') // 1
roman_numeral_to_int('V') // 5
roman_numeral_to_int('X') // 10
roman_numeral_to_int('L') // 50
roman_numeral_to_int('C') // 100
roman_numeral_to_int('D') // 500
roman_numeral_to_int('M') // 1000
```

### Additive notation
"Additive notation" allows us to add together adjacent numerals:
```
roman_numeral_to_int('II') // 2
roman_numeral_to_int('XI') // 11
roman_numeral_to_int('MDCLX') // 1660
```

### Subtractive notation
"Subtractive notation" is used to cut down on repetition. When a numeral is smaller than the numeral to its right, the smaller numeral is subtracted from the total instead of being added:
```
roman_numeral_to_int('IV') // 4
roman_numeral_to_int('IX') // 9
roman_numeral_to_int('XL') // 40
roman_numeral_to_int('XC') // 90
roman_numeral_to_int('CD') // 400
roman_numeral_to_int('CM') // 900
```

When writing an integer as a Roman numeral, the solution should always use standard subtractive notation. The only acceptable numeral combinations in subtractive notation are:
- `IV`
- `IX`
- `XL`
- `XC`
- `CD`
- `CM`

```
int_to_roman_numeral(49) // XLIX
```
Note: It would be more efficient to write 49 as `IL`. But that's not allowed because it's not one of the accepted numeral combinations listed above. Further, you can't use more than 2 digits in subtractive notation, for example you can't write 8 as `IIX`.

### Validation
When reading a Roman numeral to an integer, you can assume that the input uses additive notation or a *valid* form of subtractive notation. **Validation of incorrectly written Roman numerals is outside the scope of this kata.**

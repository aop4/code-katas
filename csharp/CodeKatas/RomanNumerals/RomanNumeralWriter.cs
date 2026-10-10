using System;
using System.Collections.Generic;
using System.Text;

namespace CodeKatas.RomanNumerals;

public class RomanNumeralWriter
{
    private static readonly int MIN_VALUE = 1;
    private static readonly int MAX_VALUE = 3999;

    private static readonly List<RomanNumeralBlock> romanNumeralBlocks = [
        new RomanNumeralBlock(1000, "M"),
        new RomanNumeralBlock(900, "CM"),
        new RomanNumeralBlock(500, "D"),
        new RomanNumeralBlock(400, "CD"),
        new RomanNumeralBlock(100, "C"),
        new RomanNumeralBlock(90, "XC"),
        new RomanNumeralBlock(50, "L"),
        new RomanNumeralBlock(40, "XL"),
        new RomanNumeralBlock(10, "X"),
        new RomanNumeralBlock(9, "IX"),
        new RomanNumeralBlock(5, "V"),
        new RomanNumeralBlock(4, "IV"),
        new RomanNumeralBlock(1, "I")
    ];

    public string IntToRomanNumeral(int value)
    {
        if (value < MIN_VALUE || value > MAX_VALUE)
        {
            throw new ArgumentException($"Value {value} is out of accepted range for Roman numerals [{MIN_VALUE}-{MAX_VALUE}]", nameof(value));
        }

        StringBuilder builder = new();
        int remainingValue = value;
        int blockIndex = 0;
        while (remainingValue > 0)
        {
            RomanNumeralBlock block = romanNumeralBlocks[blockIndex];
            if (remainingValue >= block.value)
            {
                builder.Append(block.romanNumeral);
                remainingValue -= block.value;
            }
            else
            {
                blockIndex++;
            }
        }
        return builder.ToString();
    }

    record RomanNumeralBlock(int value, string romanNumeral);

}
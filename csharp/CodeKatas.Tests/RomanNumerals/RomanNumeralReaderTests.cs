using System;
using CodeKatas.RomanNumerals;

public class RomanNumeralReaderTests {

  private RomanNumeralReader reader = new RomanNumeralReader();

    [Theory]
    [InlineData("I", 1)]
    [InlineData("V", 5)]
    [InlineData("X", 10)]
    [InlineData("L", 50)]
    [InlineData("C", 100)]
    [InlineData("D", 500)]
    [InlineData("M", 1000)]
    public void ReadSimpleRomanNumerals(string romanNumeral, int expected)
    {
        Assert.Equal(expected, reader.RomanNumeralToInt(romanNumeral));
    }

    [Theory]
    [InlineData("II", 2)]
    [InlineData("III", 3)]
    [InlineData("VI", 6)]
    [InlineData("VII", 7)]
    [InlineData("VIII", 8)]
    [InlineData("MDCLXVI", 1666)]
    [InlineData("MMMDCCCLXXXVIII", 3888)]
    public void ReadValidAdditiveNotation(string romanNumeral, int expected)
    {
        Assert.Equal(expected, reader.RomanNumeralToInt(romanNumeral));
    }

    [Theory]
    [InlineData("IIII", 4)]
    [InlineData("IIIII", 5)]
    [InlineData("VIIII", 9)]
    [InlineData("XXXX", 40)]
    [InlineData("CCCC", 400)]
    [InlineData("MMMM", 4000)]
    [InlineData("VV", 10)]
    [InlineData("LL", 100)]
    [InlineData("DD", 1000)]
    public void ReadNonStandardAdditiveNotation(string romanNumeral, int expected)
    {
        // Note: If validation were in scope, these inputs would probably be rejected. However,
        // Romans didn't always use subtractive notation, so it kinda makes sense to let these cases slide.
        Assert.Equal(expected, reader.RomanNumeralToInt(romanNumeral));
    }

    [Theory]
    [InlineData("IV", 4)]
    [InlineData("IX", 9)]
    [InlineData("XL", 40)]
    [InlineData("XC", 90)]
    [InlineData("CD", 400)]
    [InlineData("CM", 900)]
    public void ReadValidIsolatedSubtractiveNotation(string romanNumeral, int expected)
    {
        Assert.Equal(expected, reader.RomanNumeralToInt(romanNumeral));
    }

    [Theory]
    [InlineData("IM", 999)]
    [InlineData("VM", 995)]
    [InlineData("XM", 990)]
    [InlineData("LM", 950)]
    [InlineData("DM", 500)]
    [InlineData("CMCDXCXLIXIV", 1443)]
    public void ReadNonStandardSubtractiveNotation(string romanNumeral, int expected)
    {
        // Note: If validation were in scope, these inputs would be rejected
        Assert.Equal(expected, reader.RomanNumeralToInt(romanNumeral));
    }

    [Theory]
    [InlineData("XIV", 14)]
    [InlineData("XLII", 42)]
    [InlineData("XLIX", 49)]
    [InlineData("CDXLIX", 449)]
    public void ReadMixedAdditiveAndSubtractiveNotation(string romanNumeral, int expected)
    {
        Assert.Equal(expected, reader.RomanNumeralToInt(romanNumeral));
    }

    [Fact]
    public void ReadRejectsNullString()
    {
        var thrown = Assert.Throws<ArgumentNullException>(() => reader.RomanNumeralToInt(null));
        Assert.Contains("Roman numeral may not be null", thrown.Message);
    }

    [Fact]
    public void ReadRejectsEmptyString()
    {
        var thrown = Assert.Throws<ArgumentException>(() => reader.RomanNumeralToInt(""));
        Assert.Contains("Roman numeral may not be empty", thrown.Message);
    }

    [Theory]
    [InlineData("Z")]
    [InlineData("3")]
    [InlineData("ZMDCLXVIZ")]
    public void ReadRejectsInvalidCharacters(string romanNumeral)
    {
        var thrown = Assert.Throws<ArgumentException>(() => reader.RomanNumeralToInt(romanNumeral));
        Assert.Contains($"Unexpected characters in input: {romanNumeral}", thrown.Message);
    }

}
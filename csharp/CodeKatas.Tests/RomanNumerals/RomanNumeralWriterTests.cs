using System;
using CodeKatas.RomanNumerals;

public class RomanNumeralWriterTests
{
  private readonly RomanNumeralWriter writer = new RomanNumeralWriter();

    [Theory]
    [InlineData(1, "I")]
    [InlineData(5, "V")]
    [InlineData(10, "X")]
    [InlineData(50, "L")]
    [InlineData(100, "C")]
    [InlineData(500, "D")]
    [InlineData(1000, "M")]
    public void WriteOneDigitNumerals(int value, string expected)
    {
        Assert.Equal(expected, writer.IntToRomanNumeral(value));
    }

    [Theory]
    [InlineData(2000, "MM")]
    [InlineData(2, "II")]
    [InlineData(3, "III")]
    [InlineData(6, "VI")]
    [InlineData(7, "VII")]
    [InlineData(8, "VIII")]
    [InlineData(1666, "MDCLXVI")]
    [InlineData(3888, "MMMDCCCLXXXVIII")]
    public void WriteAdditiveNotation(int value, string expected)
    {
        Assert.Equal(expected, writer.IntToRomanNumeral(value));
    }

    [Theory]
    [InlineData(4, "IV")]
    [InlineData(9, "IX")]
    [InlineData(40, "XL")]
    [InlineData(90, "XC")]
    [InlineData(400, "CD")]
    [InlineData(900, "CM")]
    public void WriteSubtractiveNotation(int value, string expected)
    {
        Assert.Equal(expected, writer.IntToRomanNumeral(value));
    }

    [Theory]
    [InlineData(14, "XIV")]
    [InlineData(39, "XXXIX")]
    [InlineData(43, "XLIII")]
    [InlineData(49, "XLIX")]
    [InlineData(3999, "MMMCMXCIX")]
    public void WriteMixedAdditiveAndSubtractiveNotation(int value, string expected)
    {
        Assert.Equal(expected, writer.IntToRomanNumeral(value));
    }

    [Theory]
    [InlineData(-1)]
    [InlineData(0)]
    [InlineData(4000)]
    public void WriteRejectsInvalidValues(int value)
    {
        var thrown = Assert.Throws<ArgumentException>(() => writer.IntToRomanNumeral(value));
        Assert.Contains($"Value {value} is out of accepted range for Roman numerals [1-3999]", thrown.Message);
    }
}
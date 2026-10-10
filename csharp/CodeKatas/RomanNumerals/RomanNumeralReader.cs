using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;

namespace CodeKatas.RomanNumerals;

public class RomanNumeralReader
{

  private static readonly Dictionary<string, int> romanNumerals = new()
  {
      {"M", 1000},
      {"D", 500},
      {"C", 100},
      {"L", 50},
      {"X", 10},
      {"V", 5},
      {"I", 1}
  };

  public int RomanNumeralToInt(string romanNumeral)
  {
    romanNumeral = this.standardizeNumeral(romanNumeral);
    this.validateNumeral(romanNumeral, nameof(romanNumeral));
    return this.convertToInt(romanNumeral);
  }

  private string standardizeNumeral(string romanNumeral)
  {
    return romanNumeral?.Trim().ToUpper();
  }

  public void validateNumeral(string romanNumeral, string paramName)
  {
    if (romanNumeral is null)
    {
      throw new ArgumentNullException(paramName, "Roman numeral may not be null");
    }
    if (romanNumeral.Length == 0)
    {
      throw new ArgumentException("Roman numeral may not be empty", paramName);
    }

    Regex expected = new("^[IVXLCDM]+$");
    if (!expected.IsMatch(romanNumeral))
    {
      throw new ArgumentException($"Unexpected characters in input: {romanNumeral}", paramName);
    }
  }

  private int convertToInt(string romanNumeral)
  {
    int total = 0;
    int prevDigitValue = 0;
    foreach (char digit in romanNumeral)
    {
      int digitValue = romanNumerals[digit.ToString()];
      if (prevDigitValue < digitValue)
      {
        total -= prevDigitValue;
      }
      else
      {
        total += prevDigitValue;
      }
      prevDigitValue = digitValue;
    }
    total += prevDigitValue;
    return total;
  }

}

using System;
using CodeKatas.FlightPath;

namespace CodeKatas.Tests.FlightPath;

public class PointTests
{
    [Theory]
    [InlineData(0, 0, 0)]
    [InlineData(double.MinValue, double.MinValue, double.MinValue)]
    [InlineData(double.MaxValue, double.MaxValue, double.MaxValue)]
    [InlineData(double.NegativeInfinity, double.NegativeInfinity, double.NegativeInfinity)]
    [InlineData(double.PositiveInfinity, double.PositiveInfinity, double.PositiveInfinity)]
    public void ConstructorAcceptsNumbers(double x, double y, double z)
    {
        Point p = new Point(x, y, z);

        Assert.Equal(x, p.X);
        Assert.Equal(y, p.Y);
        Assert.Equal(z, p.Z);
    }

    [Theory]
    [InlineData(double.NaN, 0, 0, "x")]
    [InlineData(0, double.NaN, 0, "y")]
    [InlineData(0, 0, double.NaN, "z")]
    public void ConstructorRejectsNaN(double x, double y, double z, string expectedParamName)
    {
        var thrown = Assert.Throws<ArgumentException>(() => new Point(x, y, z));
        
        Assert.Contains("Coordinate value may not be NaN", thrown.Message);
        Assert.Equal(expectedParamName, thrown.ParamName);
    }
}

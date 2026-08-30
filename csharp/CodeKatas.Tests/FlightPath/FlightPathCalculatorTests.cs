using System;
using CodeKatas.FlightPath;

namespace CodeKatas.Tests.FlightPath;

public class FlightPathCalculatorTests
{
    [Fact]
    public void CalculateFlightDistanceReturnsZeroForEmptyList()
    {
        double distance = FlightPathCalculator.CalculateFlightDistance([]);
        Assert.Equal(0, distance);
    }

    [Fact]
    public void CalculateFlightDistanceReturnsZeroForSingletonList()
    {
        double distance = FlightPathCalculator.CalculateFlightDistance([new Point(1, 1, 1)]);
        Assert.Equal(0, distance);
    }

    [Fact]
    public void CalculateFlightDistanceBetweenIdenticalPoints()
    {
        double distance = FlightPathCalculator.CalculateFlightDistance(
            [
                new Point(0, 0, 0),
                new Point(0, 0, 0)
            ]
        );
        Assert.Equal(0, distance);
    }

    [Fact]
    public void CalculateFlightDistanceWhenSingleAxisChanges()
    {
        double distance = FlightPathCalculator.CalculateFlightDistance(
            [
                new Point(0, 0, 0),
                new Point(1, 0, 0),
                new Point(1, 2, 0),
                new Point(1, 2, 3)
            ]
        );
        Assert.Equal(6, distance);
    }

    [Fact]
    public void CalculateFlightDistanceInPositiveDirection()
    {
        double distance = FlightPathCalculator.CalculateFlightDistance(
            [
                new Point(-1, -1, -1),
                new Point(1, 1, 1)
            ]
        );
        Assert.Equal(Math.Sqrt(12), distance);
    }

    [Fact]
    public void CalculateFlightDistanceInNegativeDirection()
    {
        double distance = FlightPathCalculator.CalculateFlightDistance(
            [
                new Point(1, 1, 1),
                new Point(-1, -1, -1)
            ]
        );
        Assert.Equal(Math.Sqrt(12), distance);
    }

    [Fact]
    public void CalculateFlightDistanceWithFractionalValues()
    {
        double distance = FlightPathCalculator.CalculateFlightDistance(
            [
                new Point(0, 0, 0),
                new Point(0.5, 0.5, 0.5)
            ]
        );
        Assert.Equal(Math.Sqrt(0.75), distance);
    }

    [Fact]
    public void CalculateFlightDistanceForLongPath()
    {
        double distance = FlightPathCalculator.CalculateFlightDistance(
            [
                new Point(0, 0, 0),
                new Point(2, 2, 2),
                new Point(4, 4, 4),
                new Point(6, 6, 6)
            ]
        );
        Assert.Equal(3 * Math.Sqrt(12), distance);
    }

    [Fact]
    public void CalculateFlightDistanceForCircularPath()
    {
        double distance = FlightPathCalculator.CalculateFlightDistance(
            [
                new Point(0, 0, 0),
                new Point(1, 1, 1),
                new Point(0, 0, 0)
            ]
        );
        Assert.Equal(2 * Math.Sqrt(3), distance);
    }

    [Fact]
    public void CalculateFlightDistanceForInfiniteValues()
    {
        double distance = FlightPathCalculator.CalculateFlightDistance(
            [
                new Point(double.NegativeInfinity, double.NegativeInfinity, double.NegativeInfinity),
                new Point(double.PositiveInfinity, double.PositiveInfinity, double.PositiveInfinity),
                new Point(double.NegativeInfinity, double.NegativeInfinity, double.NegativeInfinity)
            ]
        );
        Assert.Equal(double.PositiveInfinity, distance);
    }

    [Fact]
    public void CalculateFlightDistanceForNullList()
    {
        var thrown = Assert.Throws<ArgumentNullException>(() => FlightPathCalculator.CalculateFlightDistance(null));

        Assert.Equal("flightPath", thrown.ParamName);
    }

    [Fact]
    public void CalculateFlightDistanceForListWithNullItems()
    {
        var thrown = Assert.Throws<ArgumentException>(() => FlightPathCalculator.CalculateFlightDistance([null]));
        
        Assert.Equal("flightPath cannot contain null points", thrown.Message);
    }
}

using System;

namespace CodeKatas.FlightPath;

/// <summary>
/// Represents a point in a 3-dimensional coordinate space.
/// </summary>
public record Point
{
    public double X { get; init; }
    public double Y { get; init; }
    public double Z { get; init; }

    /// <exception cref="ArgumentException">
    /// Thrown when one of the coordinate's values is double.NaN.
    /// </exception>
    public Point(double x, double y, double z)
    {
        this.validateCoordinate(x, nameof(x));
        this.validateCoordinate(y, nameof(y));
        this.validateCoordinate(z, nameof(z));

        this.X = x;
        this.Y = y;
        this.Z = z;
    }

    /// <summary>
    /// Computes the distance between this point and another in a 3-dimensional coordinate space.
    /// </summary>
    public double DistanceFrom(Point other)
    {
        return Math.Sqrt(
            Math.Pow(this.X - other.X, 2) +
            Math.Pow(this.Y - other.Y, 2) +
            Math.Pow(this.Z - other.Z, 2)
        );
    }

    private void validateCoordinate(double coordVal, string coordName)
    {
        if (double.IsNaN(coordVal))
        {
            throw new ArgumentException("Coordinate value may not be NaN", coordName);
        }
    }
}

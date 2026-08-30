using System;
using System.Collections.Generic;
using System.Linq;

namespace CodeKatas.FlightPath;

public class FlightPathCalculator
{
    /// <summary>
    /// Calculates the total length of the path connecting a sequence of 3-dimensional points.
    /// </summary>
    public static double CalculateFlightDistance(List<Point> flightPath)
    {
        if (flightPath is null)
        {
            throw new ArgumentNullException(nameof(flightPath));
        }
        if (flightPath.Any(point => point is null))
        {
            throw new ArgumentException($"{nameof(flightPath)} cannot contain null points");
        }

        double distance = 0;
        for (int i = 1; i < flightPath.Count; i++)
        {
            distance += flightPath[i].DistanceFrom(flightPath[i - 1]);
        }
        return distance;
    }
}
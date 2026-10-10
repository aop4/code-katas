package com.andrewpuglionesi.portablebitmap;

/**
 * An image coordinate. When setting coordinate values, the 0th row is the top row of the image, and the 0th column is
 * the leftmost column of the image.
 */
public record Coordinate(int row, int col) {}
package com.andrewpuglionesi.portablebitmap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.andrewpuglionesi.datastructures.BitArray;

import lombok.Getter;
import lombok.NonNull;

/**
 * A tool for creating .pbm image files where the image contents are represented as raw binary (roughly one bit per
 * pixel). The "magic number" that describes this file format is "P4".
 */
@SuppressWarnings({"PMD.ShortVariable", "PMD.AvoidInstantiatingObjectsInLoops", "PMD.UselessParentheses"})
public class PbmWriter {

    private static final int BITS_PER_PYTE = 8;

    private final BitArray bitArray;
    /**
     * Width of image in pixels (bits)
     */
    @Getter
    private final int width;
    /**
     * Height of image in pixels (bits)
     */
    @Getter
    private final int height;
    /**
     * Width of image in pixels (bits), padded to the nearest byte.
     */
    private final int paddedWidth;
    
    /**
     * @param width image width measured in pixels.
     * @param height image height measured in pixels.
     */
    public PbmWriter(final int width, final int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException(
                String.format("Invalid dimensions for image: width %d, height %d", width, height)
            );
        }

        this.width = width;
        this.height = height;

        if (this.width % 8 == 0) {
            this.paddedWidth = this.width;
        } else {
            this.paddedWidth = (this.width / BITS_PER_PYTE * BITS_PER_PYTE) + BITS_PER_PYTE;
        }

        this.bitArray = new BitArray(this.paddedWidth * this.height);
    }

    /**
     * Sets the pixel at the specified coordinate to the specified fill color.
     */
    public void setPixel(final Coordinate coordinate, @NonNull final PixelFill pixelFill) {
        this.validateCoordinate(coordinate);

        int bitIndex = (coordinate.row() * this.paddedWidth) + coordinate.col();
        this.bitArray.setBit(bitIndex, pixelFill.getBitValue());
    }

    /**
     * Sets all pixels in the image to the specified fill color.
     */
    public void fillImage(@NonNull final PixelFill pixelFill) {
        this.bitArray.fill(pixelFill.getBitValue());
    }

    /**
     * Flips all pixels in the image to the opposite color (white becomes black and vice versa).
     */
    public void negateImage() {
        this.bitArray.negate();
    }

    /**
     * Draws a line between two points, rendering it with the specified fill color.
     */
    public void drawLine(final Coordinate from, final Coordinate to, @NonNull final PixelFill pixelFill) {
        this.validateCoordinate(from);
        this.validateCoordinate(to);

        if (from.col() == to.col()) {
            this.drawVerticalLine(from, to, pixelFill);
        } else {
            this.drawNonVerticalLine(from, to, pixelFill);
        }
    }

    /**
     * Draws a rectangle given two corners opposite each other.
     * @param corner1 a corner of the rectangle.
     * @param corner2 the corner of the rectangle opposite corner1.
     * @param pixelFill pixel fill color.
     */
    public void drawRectangle(final Coordinate corner1, final Coordinate corner2, @NonNull final PixelFill pixelFill) {
        this.drawLine(
            corner1,
            new Coordinate(corner1.row(), corner2.col()),
            pixelFill
        );
        this.drawLine(
            corner1,
            new Coordinate(corner2.row(), corner1.col()),
            pixelFill
        );
        this.drawLine(
            corner2,
            new Coordinate(corner2.row(), corner1.col()),
            pixelFill
        );
        this.drawLine(
            corner2,
            new Coordinate(corner1.row(), corner2.col()),
            pixelFill
        );
    }

    /**
     * Writes the image to a file at the provided path.
     * @param filePath fully qualified file path. You should use the file extension .pbm, but this is not enforced.
     * @throws IOException if the directory for the file does not exist or the file cannot be saved for another reason.
     */
    public void writeToFile(final String filePath) throws IOException {
        Path path = Paths.get(filePath);
        // Note: \n is correct even on Windows systems, since PBM renderers interpret anything after the third
        // whitespace char as part of image
        Files.writeString(path, "P4\n", StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        Files.writeString(path, String.format("%d %d\n", this.width, this.height), StandardOpenOption.APPEND);
        Files.write(path, this.bitArray.toByteArray(), StandardOpenOption.APPEND);
    }

    private void validateCoordinate(final Coordinate coord) {
        if (coord.row() < 0 || coord.row() >= this.height || coord.col() < 0 || coord.col() >= this.width) {
            String errorMsg = "Coordinate at row %d, col %d invalid for image with height %d, width %d";
            throw new IndexOutOfBoundsException(String.format(errorMsg, coord.row(), coord.col(), this.height, this.width));
        }
    }

    private void drawVerticalLine(final Coordinate c1, final Coordinate c2, final PixelFill pixelFill) {
        assert c1.col() == c2.col();

        final int col = c1.col();
        final int startRow = Math.min(c1.row(), c2.row());
        final int endRow = Math.max(c1.row(), c2.row());

        for (int row = startRow; row <= endRow; row++) {
            Coordinate coord = new Coordinate(row, col);
            this.setPixel(coord, pixelFill);
        }
    }

    private void drawNonVerticalLine(final Coordinate c1, final Coordinate c2, final PixelFill pixelFill) {
        assert c1.col() != c2.col();

        double slope = ((double) c2.row() - c1.row()) / ((double) c2.col() - c1.col());
        
        final Coordinate rightMost;
        final Coordinate leftMost;

        if (c1.col() < c2.col()) {
            leftMost = c1;
            rightMost = c2;
        } else {
            leftMost = c2;
            rightMost = c1;
        }

        int x;
        double y;
        Coordinate currentPoint;
        for (x = leftMost.col(), y = leftMost.row(); x < rightMost.col(); x++, y += slope) {
            currentPoint = new Coordinate((int) Math.round(y), x);
            this.setPixel(currentPoint, pixelFill);
        }
    }
}

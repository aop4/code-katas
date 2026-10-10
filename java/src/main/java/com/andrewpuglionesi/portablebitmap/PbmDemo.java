package com.andrewpuglionesi.portablebitmap;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import com.andrewpuglionesi.datastructures.BitArray;

/**
 * Generates image files to smoke test {@link PbmWriter}.
 */
@SuppressWarnings("PMD")
public class PbmDemo {

    private static final String pathPrefix = "/home/andrew/Pictures/pbm/";

    public static void main(String[] args) throws IOException {
        draw8By8BlackSquare();
        draw7By7BlackSquare();

        draw100By100WhiteSquare();
        draw100By100BlackSquare();

        drawWhitePixel();

        drawFilledImages();

        drawIntersectingLines();
        drawIntersectingLinesBackwards();

        drawConcentricSquares();

        drawRandomImages();

        drawImageWithEmbeddedMessage();
    }

    private static void draw8By8BlackSquare() throws IOException {
        PbmWriter pbmWriter = new PbmWriter(8, 8);
        for (int i = 0; i < 8; i++) {
            for (int k = 0; k < 8; k++) {
                pbmWriter.setPixel(new Coordinate(i,k), PixelFill.BLACK);
            }
        }
        pbmWriter.writeToFile(pathPrefix + "black-square-8x8.pbm");
    }
    
    private static void draw7By7BlackSquare() throws IOException {
        PbmWriter pbmWriter = new PbmWriter(7, 7);
        for (int i = 0; i < 7; i++) {
            for (int k = 0; k < 7; k++) {
                pbmWriter.setPixel(new Coordinate(i,k), PixelFill.BLACK);
            }
        }
        pbmWriter.writeToFile(pathPrefix + "black-square-7x7.pbm");
    }

    private static void draw100By100WhiteSquare() throws IOException {
        PbmWriter pbmWriter = new PbmWriter(100, 100);
        pbmWriter.writeToFile(pathPrefix + "all-white.pbm");
    }

    private static void draw100By100BlackSquare() throws IOException {
        PbmWriter pbmWriter = new PbmWriter(100, 100);
        pbmWriter.negateImage();
        pbmWriter.writeToFile(pathPrefix + "all-black.pbm");
    }

    private static void drawWhitePixel() throws IOException {
        PbmWriter pbmWriter = new PbmWriter(3, 3);
        pbmWriter.fillImage(PixelFill.BLACK);
        pbmWriter.setPixel(new Coordinate(2, 2), PixelFill.WHITE);
        pbmWriter.writeToFile(pathPrefix + "white-pixel-bottom-right.pbm");
    }

    private static void drawFilledImages() throws IOException {
        PbmWriter pbmWriter = new PbmWriter(100, 100);
        pbmWriter.fillImage(PixelFill.BLACK);
        pbmWriter.writeToFile(pathPrefix + "black-filled.pbm");
        pbmWriter.fillImage(PixelFill.WHITE);
        pbmWriter.writeToFile(pathPrefix + "white-filled.pbm");
    }

    private static void drawIntersectingLines() throws IOException {
        PbmWriter pbmWriter = new PbmWriter(1000, 1000);
        // diagonal from top-left to bottom-right
        pbmWriter.drawLine(
            new Coordinate(0,0),
            new Coordinate(999,999),
            PixelFill.BLACK
        );
        // diagonal line from bottom-left to top-right
        pbmWriter.drawLine(
            new Coordinate(999,0),
            new Coordinate(0,999),
            PixelFill.BLACK
        );
        // horizontal line
        pbmWriter.drawLine(
            new Coordinate(499,0),
            new Coordinate(499,999),
            PixelFill.BLACK
        );
        // vertical line
        pbmWriter.drawLine(
            new Coordinate(0,499),
            new Coordinate(999,499),
            PixelFill.BLACK
        );
        // downward sloping line with fractional slope
        pbmWriter.drawLine(
            new Coordinate(249,0),
            new Coordinate(749,999),
            PixelFill.BLACK
        );
        // upward sloping line with fractional slope
        pbmWriter.drawLine(
            new Coordinate(749,0),
            new Coordinate(249,999),
            PixelFill.BLACK
        );
        pbmWriter.writeToFile(pathPrefix + "intersecting-lines.pbm");
    }

    private static void drawIntersectingLinesBackwards() throws IOException {
        PbmWriter pbmWriter = new PbmWriter(1000, 1000);
        // diagonal from bottom-right to top-left
        pbmWriter.drawLine(
            new Coordinate(999,999),
            new Coordinate(0,0),
            PixelFill.BLACK
        );
        // diagonal line from top-right to bottom-left
        pbmWriter.drawLine(
            new Coordinate(0,999),
            new Coordinate(999,0),
            PixelFill.BLACK
        );
        // horizontal line
        pbmWriter.drawLine(
            new Coordinate(499,999),
            new Coordinate(499,0),
            PixelFill.BLACK
        );
        // vertical line
        pbmWriter.drawLine(
            new Coordinate(999,499),
            new Coordinate(0,499),
            PixelFill.BLACK
        );
        // downward sloping line with fractional slope
        pbmWriter.drawLine(
            new Coordinate(749,999),
            new Coordinate(249,0),
            PixelFill.BLACK
        );
        // upward sloping line with fractional slope
        pbmWriter.drawLine(
            new Coordinate(249,999),
            new Coordinate(749,0),
            PixelFill.BLACK
        );
        pbmWriter.writeToFile(pathPrefix + "intersecting-lines-backwards.pbm");
    }

    private static void drawConcentricSquares() throws IOException {
        int imageWidth = 99;
        int center = imageWidth / 2;
        PbmWriter pbmWriter = new PbmWriter(imageWidth, imageWidth);

        for (int sideLength = 0; sideLength < imageWidth / 2; sideLength += 2) {
            pbmWriter.drawRectangle(
                new Coordinate(center - sideLength, center - sideLength),
                new Coordinate(center + sideLength, center + sideLength),
                PixelFill.BLACK
            );
        }

        pbmWriter.writeToFile(pathPrefix + "concentric-squares.pbm");
    }

    private static void drawRandomImages() throws IOException {
        drawRandomImage(100, 100, "random_100x100.pbm");
        drawRandomImage(1000, 1000, "random_1000x1000.pbm");
        drawRandomImage(2160, 3840, "random_4k.pbm");
    }
    
    private static void drawRandomImage(int imgHeight, int imgWidth, String fileName) throws IOException {
        PbmWriter pbmWriter = new PbmWriter(imgWidth, imgHeight);
        Random random = new Random();

        for (int row = 0; row < pbmWriter.getHeight(); row++) {
            for (int col = 0; col < pbmWriter.getWidth(); col++) {
                PixelFill pixelFill = random.nextBoolean() ? PixelFill.BLACK : PixelFill.WHITE;
                pbmWriter.setPixel(new Coordinate(row, col), pixelFill);
            }
        }

        pbmWriter.writeToFile(pathPrefix + fileName);
    }

    private static void drawImageWithEmbeddedMessage() throws IOException {
        String message = "The sun is but a morning star. ";
        BitArray messageBits = new BitArray(message.getBytes(StandardCharsets.UTF_8));

        PbmWriter pbmWriter = new PbmWriter(messageBits.size(), messageBits.size());

        int messageIndex = 0;
        for (int row = 0; row < pbmWriter.getHeight(); row++) {
            for (int col = 0; col < pbmWriter.getWidth(); col++) {
                boolean messageBit = messageBits.getBit(messageIndex);
                if (messageBit) {
                    pbmWriter.setPixel(
                        new Coordinate(row, col),
                        PixelFill.BLACK
                    );
                }
                messageIndex = (messageIndex + 1) % messageBits.size();
            }
        }

        pbmWriter.writeToFile(pathPrefix + "quote.pbm");
    }
}

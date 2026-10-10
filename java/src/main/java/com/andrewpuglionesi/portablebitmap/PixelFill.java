package com.andrewpuglionesi.portablebitmap;

/**
 * Fill color for pixel in PBM image.
 */
public enum PixelFill {
    WHITE(false),
    BLACK(true);

    private boolean bitValue;

    PixelFill(final boolean bitValue) {
        this.bitValue = bitValue;
    }

    @SuppressWarnings("PMD.BooleanGetMethodName")
    public boolean getBitValue() {
        return this.bitValue;
    }
}
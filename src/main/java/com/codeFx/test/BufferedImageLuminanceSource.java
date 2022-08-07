package com.codeFx.test;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import com.google.zxing.LuminanceSource;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

public final class BufferedImageLuminanceSource extends LuminanceSource {
    private final BufferedImage image;
    private final int left;
    private final int top;
    private int[] rgbData;

    public BufferedImageLuminanceSource(BufferedImage image) {
        this(image, 0, 0, image.getWidth(), image.getHeight());
    }

    public BufferedImageLuminanceSource(BufferedImage image, int left, int top, int width, int height) {
        super(width, height);
        int sourceWidth = image.getWidth();
        int sourceHeight = image.getHeight();
        if (left + width <= sourceWidth && top + height <= sourceHeight) {
            this.image = image;
            this.left = left;
            this.top = top;
        } else {
            throw new IllegalArgumentException("Crop rectangle does not fit within image data.");
        }
    }

    public byte[] getRow(int y, byte[] row) {
        if (y >= 0 && y < this.getHeight()) {
            int width = this.getWidth();
            if (row == null || row.length < width) {
                row = new byte[width];
            }

            if (this.rgbData == null || this.rgbData.length < width) {
                this.rgbData = new int[width];
            }

            this.image.getRGB(this.left, this.top + y, width, 1, this.rgbData, 0, width);

            for(int x = 0; x < width; ++x) {
                int pixel = this.rgbData[x];
                int luminance = 306 * (pixel >> 16 & 255) + 601 * (pixel >> 8 & 255) + 117 * (pixel & 255) >> 10;
                row[x] = (byte)luminance;
            }

            return row;
        } else {
            throw new IllegalArgumentException("Requested row is outside the image: " + y);
        }
    }

    public byte[] getMatrix() {
        int width = this.getWidth();
        int height = this.getHeight();
        int area = width * height;
        byte[] matrix = new byte[area];
        int[] rgb = new int[area];
        this.image.getRGB(this.left, this.top, width, height, rgb, 0, width);

        for(int y = 0; y < height; ++y) {
            int offset = y * width;

            for(int x = 0; x < width; ++x) {
                int pixel = rgb[offset + x];
                int luminance = 306 * (pixel >> 16 & 255) + 601 * (pixel >> 8 & 255) + 117 * (pixel & 255) >> 10;
                matrix[offset + x] = (byte)luminance;
            }
        }

        return matrix;
    }

    public boolean isCropSupported() {
        return true;
    }

    public LuminanceSource crop(int left, int top, int width, int height) {
        return new com.codeFx.test.BufferedImageLuminanceSource(this.image, left, top, width, height);
    }

    public boolean isRotateSupported() {
        return this.image.getType() != 0;
    }

    public LuminanceSource rotateCounterClockwise() {
        if (!this.isRotateSupported()) {
            throw new IllegalStateException("Rotate not supported");
        } else {
            int sourceWidth = this.image.getWidth();
            int sourceHeight = this.image.getHeight();
            AffineTransform transform = new AffineTransform(0.0, -1.0, 1.0, 0.0, 0.0, (double)sourceWidth);
            BufferedImageOp op = new AffineTransformOp(transform, 1);
            BufferedImage rotatedImage = new BufferedImage(sourceHeight, sourceWidth, this.image.getType());
            op.filter(this.image, rotatedImage);
            int width = this.getWidth();
            return new com.codeFx.test.BufferedImageLuminanceSource(rotatedImage, this.top, sourceWidth - (this.left + width), this.getHeight(), width);
        }
    }
}


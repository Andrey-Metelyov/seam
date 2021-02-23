/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class SeamCarver {
    public static final int BORDER_PIXEL_ENERGY = 1000;
    private Picture picture;
    private double[][] energy;
    private int[][] pixels;
    private boolean transposed = false;
    private int width;
    private int height;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        width = picture.width();
        height = picture.height();
        computeEnergy();
    }

    private void computeEnergy() {
        energy = new double[height()][width()];
        pixels = new int[height()][width()];
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                pixels[y][x] = picture.getRGB(x, y);
            }
        }
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                energy[y][x] = computePixelEnergy(x, y);
            }
        }
    }

    private double computePixelEnergy(int x, int y) {
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
            return BORDER_PIXEL_ENERGY;
        }
        int xm1 = pixels[y][x - 1];
        int xm1r = (xm1 >> 16) & 0xFF;
        int xm1g = (xm1 >>  8) & 0xFF;
        int xm1b = (xm1) & 0xFF;
        int xp1 = pixels[y][x + 1];
        int xp1r = (xp1 >> 16) & 0xFF;
        int xp1g = (xp1 >>  8) & 0xFF;
        int xp1b = (xp1) & 0xFF;
        int ym1 = pixels[y - 1][x];
        int ym1r = (ym1 >> 16) & 0xFF;
        int ym1g = (ym1 >>  8) & 0xFF;
        int ym1b = (ym1) & 0xFF;
        int yp1 = pixels[y + 1][x];
        int yp1r = (yp1 >> 16) & 0xFF;
        int yp1g = (yp1 >>  8) & 0xFF;
        int yp1b = (yp1) & 0xFF;
        return Math.sqrt(((xm1r - xp1r) * (xm1r - xp1r)
                + (xm1g - xp1g) * (xm1g - xp1g)
                + (xm1b - xp1b) * (xm1b - xp1b)
                + (ym1r - yp1r) * (ym1r - yp1r)
                + (ym1g - yp1g) * (ym1g - yp1g)
                + (ym1b - yp1b) * (ym1b - yp1b)));
    }

    // current picture
    public Picture picture() {
        if (picture.width() != width() || picture.height() != height()) {
            updatePicture();
        }
        return new Picture(picture);
    }

    private void updatePicture() {
        Picture newPicture = new Picture(width, height);
        for (int x = 0; x < height(); x++) {
            for (int y = 0; y < width(); y++) {
                newPicture.setRGB(y, x, pixels[x][y]);
            }
        }
        picture = newPicture;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (!transposed) {
            return energy[y][x];
        }
        return energy[x][y];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        if (!transposed) {
            transposePixelsAndEnergy();
        }
        return findSeam();
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        if (transposed) {
            transposePixelsAndEnergy();
        }
        return findSeam();
    }

    private int[] findSeam() {
        int h = energy.length;
        int w = energy[0].length;
        StdOut.println("height = " + h + " width = " + w);
        int[] result = new int[h];
        double[][] minEnergy = new double[h][w];
        int[][] path = new int[h][w];
        for (int i = 0; i < minEnergy.length; i++) {
            System.arraycopy(energy[i], 0, minEnergy[i], 0, energy[i].length);
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < path[i].length; j++) {
                path[i][j] = j;
            }
        }
        for (int i = 1; i < h; i++) {
            int minJ;
            if (w > 1) {
                minJ = minEnergy[i - 1][0] < minEnergy[i - 1][1] ? 0 : 1;
                minEnergy[i][0] += minEnergy[i - 1][minJ];
                path[i][0] = minJ;
                minJ = minEnergy[i - 1][w - 1] < minEnergy[i - 1][w - 2] ? w - 1 : w - 2;
                minEnergy[i][w - 1] += minEnergy[i - 1][minJ];
                path[i][w - 1] = minJ;
            }
            for (int j = 1; j < w - 1; j++) {
                minJ = j - 1;
                if (minEnergy[i - 1][minJ] > minEnergy[i - 1][j]) {
                    minJ = j;
                }
                if (minEnergy[i - 1][minJ] > minEnergy[i - 1][j + 1]) {
                    minJ = j + 1;
                }

                minEnergy[i][j] += minEnergy[i - 1][minJ];
                path[i][j] = minJ;
            }
        }
        int min = 0;
        for (int i = 1; i < w; i++) {
            if (minEnergy[h - 1][i] < minEnergy[h - 1][min]) {
                min = i;
            }
        }
        result[h - 1] = min;
        for (int i = h - 2; i >= 0; i--) {
            result[i] = path[i + 1][result[i + 1]];
        }
        return result;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        // showPixels();
        if (seam.length != width()) {
            throw new IllegalArgumentException(
                    "width = " + width() + ", seam.length = " + seam.length);
        }
        if (!transposed) {
            transposePixelsAndEnergy();
        }
        for (int i = 0; i < width(); i++) {
            System.arraycopy(pixels[i], seam[i] + 1, pixels[i], seam[i], pixels[i].length - seam[i] - 1);
            energy[i][seam[i]] = computePixelEnergy(i, seam[i]);
            energy[i][seam[i] + 1] = computePixelEnergy(i, seam[i] + 1);
        }
        height--;
        // transposePixelsAndEnergy();
        showPixels();
    }

    private void transposePixelsAndEnergy() {
        if (pixels.length != energy.length || pixels[0].length != energy[0].length) {
            throw new IllegalArgumentException("pixels != energy");
        }
        int[][] newPixels = new int[pixels[0].length][pixels.length];
        double[][] newEnergy = new double[energy[0].length][energy.length];
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                newPixels[j][i] = pixels[i][j];
                newEnergy[j][i] = energy[i][j];
            }
        }
        transposed = !transposed;
        pixels = newPixels;
        energy = newEnergy;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        showPixels();
        if (seam.length != height()) {
            throw new IllegalArgumentException(
                    "height = " + height() + ", seam.length = " + seam.length);
        }
        if (transposed) {
            transposePixelsAndEnergy();
        }
        for (int i = 0; i < height(); i++) {
            System.arraycopy(pixels[i], seam[i] + 1, pixels[i], seam[i], pixels[i].length - seam[i] - 1);
            energy[i][seam[i]] = computePixelEnergy(i, seam[i]);
            energy[i][seam[i] + 1] = computePixelEnergy(i, seam[i] + 1);
        }
        width--;
        showPixels();
    }

    private void showPixels() {
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                StdOut.print(pixels[i][j] + " ");
            }
            StdOut.println();
        }
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        // Picture pic = new Picture("1x8.png");
        // Picture pic = new Picture("8x1.png");
        // String[] testPictures = { "1x8.png", "8x1.png", "3x4.png", "6x5.png"};
        // for (String testPicture : testPictures) {
        //     Picture pic = new Picture(testPicture);
        //     SeamCarver seamCarver = new SeamCarver(pic);
        //     StdOut.println(pic.width() + "x" + pic.height());
        //     for (int y = 0; y < seamCarver.height(); y++) {
        //         for (int x = 0; x < seamCarver.width(); x++) {
        //             StdOut.print(seamCarver.energy(x, y) + " ");
        //         }
        //         StdOut.println();
        //     }
        // }

        Picture pic = new Picture("6x5.png");
        SeamCarver seamCarver = new SeamCarver(pic);
        StdOut.println(Arrays.toString(seamCarver.findVerticalSeam()));
        StdOut.println(Arrays.toString(seamCarver.findHorizontalSeam()));

        // StdOut.println(pic.width() + "x" + pic.height());
        // seamCarver.removeVerticalSeam(new int[] { 3, 4, 3, 2, 2 });
        // seamCarver = new SeamCarver(pic);
        // StdOut.println(pic.width() + "x" + pic.height());
        // seamCarver.removeHorizontalSeam(new int[] { 2, 2, 1, 2, 1, 2 });
        // for (int y = 0; y < seamCarver.height(); y++) {
        //     for (int x = 0; x < seamCarver.width(); x++) {
        //         StdOut.print(seamCarver.energy(x, y) + " ");
        //     }
        //     StdOut.println();
        // }
    }
}

/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {
    public static final int BORDER_PIXEL_ENERGY = 1000;
    private Picture picture;
    private double[][] energy;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        computeEnergy();
    }

    private void computeEnergy() {
        energy = new double[height()][width()];
        int[][] pixels = new int[height()][width()];
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                pixels[y][x] = picture.getRGB(x, y);
            }
        }
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
                    energy[y][x] = BORDER_PIXEL_ENERGY;
                    continue;
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
                energy[y][x] = (xm1r - xp1r) * (xm1r - xp1r)
                        + (xm1g - xp1g) * (xm1g - xp1g)
                        + (xm1b - xp1b) * (xm1b - xp1b)
                        + (ym1r - yp1r) * (ym1r - yp1r)
                        + (ym1g - yp1g) * (ym1g - yp1g)
                        + (ym1b - yp1b) * (ym1b - yp1b);
            }
        }
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        return energy[y][x];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return null;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return null;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {

    }

    //  unit testing (optional)
    public static void main(String[] args) {
        // Picture pic = new Picture("1x8.png");
        // Picture pic = new Picture("8x1.png");
        Picture pic = new Picture("3x4.png");
        SeamCarver seamCarver = new SeamCarver(pic);
        StdOut.println(pic.width() + "x" + pic.height());
        for (int y = 0; y < seamCarver.height(); y++) {
            for (int x = 0; x < seamCarver.width(); x++) {
                StdOut.print(seamCarver.energy(x, y) + " ");
            }
            StdOut.println();
        }
    }
}

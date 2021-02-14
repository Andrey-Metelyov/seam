/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;

public class SeamCarver {
    public static final int BORDER_PIXEL_ENERGY = 1000;
    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = picture;
    }

    // current picture
    public Picture picture() {
        return picture;
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
        int width = width();
        int height = height();
        if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
            return BORDER_PIXEL_ENERGY;
        }
        Color xm1 = new Color(picture.getRGB(x - 1, y));
        Color xp1 = new Color(picture.getRGB(x + 1, y));
        Color ym1 = new Color(picture.getRGB(x, y - 1));
        Color yp1 = new Color(picture.getRGB(x, y + 1));
        int dX = (xm1.getRed() - xp1.getRed()) * (xm1.getRed() - xp1.getRed())
                + (xm1.getGreen() - xp1.getGreen()) * (xm1.getGreen() - xp1.getGreen())
                + (xm1.getBlue() - xp1.getBlue()) * (xm1.getBlue() - xp1.getBlue());
        int dY = (ym1.getRed() - yp1.getRed()) * (ym1.getRed() - yp1.getRed())
                + (ym1.getGreen() - yp1.getGreen()) * (ym1.getGreen() - yp1.getGreen())
                + (ym1.getBlue() - yp1.getBlue()) * (ym1.getBlue() - yp1.getBlue());
        return dX + dY;
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
        Picture pic = new Picture("10x10.png");
        // Picture pic = new Picture("3x4.png");
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

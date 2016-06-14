import javax.swing.*;
import java.awt.*;

/**
 * @author Rory O'Dwyer and Chris Kjellqvist
 * @date 6/14/16
 * Rory O'Dwyer with the aid of Chris Kjellqvist. Math concepts borrowed from desmos calculator project 12.6.15
 * Please reference all the creators of this project if code is borrowed.
 */
public class Display extends JPanel {
    static double scale = .007; //units/px size
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    double aRotation = -2.03; //for roation around z axis
    double bRotation = .4; //for roatating the z axis up or down (not exactly around x or y axis)
    double cRotation = 0; //for rotating the z axis left or right

    private Exception SquareRootLessThanZeroException = new Exception("Trying to take a square root of a number less than zero");

    //What do these do?
    double cubeVision = 10;
    double multiplier = 1;
    double sX = 0;
    double sY = 0;
    double sZ = 0;
    double lightX = 1;
    double lightY = 1;
    double lightZ = 1;

    //Position of top left corner of screen
    double xTranslation = 0;
    double yTranslation = 0;

    public static int numberOfColors = 10000;
    public static float[] color1 = Color.RGBtoHSB(130, 255, 255, null);
    public float[] color2 = Color.RGBtoHSB(0, 0, 0, null);
    public Color[] colorArray = ColorFun.getColorPallete(numberOfColors, color1, color2);


    public Display() {
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        boolean matcher = false;
        g.setColor(Color.black);
        double i, j, k; //points for plotting in 3d
        int h = (int) screenSize.getHeight();
        int w = (int) screenSize.getWidth();

        double cosA = Math.cos(aRotation);
        double cosB = Math.cos(bRotation);
        double cosC = Math.cos(cRotation);
        double sinA = Math.sin(aRotation);
        double sinB = Math.sin(bRotation);
        double sinC = Math.sin(cRotation);
        double Xx = cosC * cosA - sinC * sinA * sinB;
        double Xy = cosC * sinA * sinB + sinC * cosA;
        double Yx = -cosC * sinA - sinC * cosA * sinB;
        double Yy = cosC * cosA * sinB - sinC * sinA;
        double Zx = -sinC * cosB;
        double Zy = cosC * cosB;

        //Now for any point in 3D, the point on this plane is composed of those transformations times the actual point
        for (i = -cubeVision; i < cubeVision; i += .1) {
            for (j = -cubeVision; j < cubeVision; j += .1) {
                try {
                    k = function(i, j);
                } catch (Exception e) {
                    continue;
                }
                if ((Math.abs(sX - i) < .1) && (Math.abs(sY - j) < .1) && (Math.abs(sZ - k) < .1)) {
                    matcher = true;
                }
                drawRectangleAtPoint(g, i, j, k, Xx, Yx, Zx, Xy, Yy, Zy);
            }
        }
        g.setColor(Color.yellow);
        Point<Integer> coor = getReversePoints(transform(lightX, lightY, lightZ, Xx, Yx, Zx), transform(lightX, lightY, lightZ, Xy, Yy, Zy));
        g.fillRect(coor.x, coor.y, 5, 5);
        drawCoordinateAxes(g, Xx, Yx, Zx, Xy, Yy, Zy);
        drawFinder(g, Xx, Yx, Zx, Xy, Yy, Zy, matcher);
    }

    /*
    Unused
    public Point<Double> getPoint(Point<Integer> point) {
        double x = (point.x * scale) + xTranslation;//gets the i, j value of the pixel(0,0 upper right corner) then times the scale for pixels and add the translation for x
        double y = ((screenSize.getHeight() * scale)) - (((point.y * scale) + yTranslation));//same for as x but for y
        return new Point<>(x, y);
    }
    */

    public Point<Integer> getReversePoints(double x, double y) {
        int x2 = (int) ((x - xTranslation) / scale);
        int y2 = (int) (screenSize.getHeight() - ((y + yTranslation) / scale));
        return new Point<>(x2, y2);
    }

    public Point<Double> getCenter() {
        double x = xTranslation + scale * screenSize.getWidth() / 2;//Gives you the position of the center by finding how wide half the screen is in terms of real points
        double y = (scale * screenSize.getHeight()) / 2 - yTranslation;//(not pixels) and adding the yTranslation to it.
        return new Point<>(x, y);
    }

    public void setCenter(Point<Double> center) {
        xTranslation = center.x - scale * screenSize.getWidth() / 2;//Sets the x and y Translations to what they would be at the given center, allows someone to move the screen
        yTranslation = -center.y + scale * screenSize.getHeight() / 2;//by setting up the center of it at a different point in real space.
    }

    /**
     * Zooms in the graph
     *
     * @param scalar - scalar to determine magnitude of zoomage
     */
    public void zoom(double scalar) {
        Point<Double> center = this.getCenter();
        scale *= scalar;
        this.setCenter(center);
    }

    /**
     * ???
     * @param x
     * @param y
     * @return
     * @throws Exception
     */
    public double function(double x, double y) throws Exception {
        double reply = Math.sqrt(25 - x * x - y * y);
        if (!Double.isNaN(reply)) {
            return reply;
        } else {
            throw SquareRootLessThanZeroException;
        }
    }

    /**
     * ???
     * @param x
     * @param y
     * @param z
     * @param tX
     * @param tY
     * @param tZ
     * @return
     */
    public double transform(double x, double y, double z, double tX, double tY, double tZ) {
        return (tX * x + tY * y + tZ * z);
    }

    /**
     * ???
     * @param g
     * @param Xx
     * @param Yx
     * @param Zx
     * @param Xy
     * @param Yy
     * @param Zy
     */
    public void drawCoordinateAxes(Graphics g, double Xx, double Yx, double Zx, double Xy, double Yy, double Zy) {
        Point<Integer> origin = getReversePoints(transform(0, 0, 0, Xx, Yx, Zx), transform(0, 0, 0, Xy, Yy, Zy));
        Point<Integer> xReference = getReversePoints(transform(5, 0, 0, Xx, Yx, Zx), transform(5, 0, 0, Xy, Yy, Zy));
        Point<Integer> yReference = getReversePoints(transform(0, 5, 0, Xx, Yx, Zx), transform(0, 5, 0, Xy, Yy, Zy));
        Point<Integer> zReference = getReversePoints(transform(0, 0, 5, Xx, Yx, Zx), transform(0, 0, 5, Xy, Yy, Zy));
        g.setColor(Color.red);
        g.drawLine(origin.x, origin.y, xReference.x, xReference.y);
        g.drawLine(origin.x, origin.y, yReference.x, yReference.y);
        g.drawLine(origin.x, origin.y, zReference.x, zReference.y);
        g.setColor(Color.cyan);
        g.drawString("X", xReference.x, xReference.y);
        g.drawString("Y", yReference.x, yReference.y);
        g.drawString("Z", zReference.x, zReference.y);
    }

    /**
     * ???
     * @param g
     * @param Xx
     * @param Yx
     * @param Zx
     * @param Xy
     * @param Yy
     * @param Zy
     * @param matcher
     */
    public void drawFinder(Graphics g, double Xx, double Yx, double Zx, double Xy, double Yy, double Zy, boolean matcher) {
        g.setColor(Color.blue);
        if (matcher) {
            g.setColor(Color.pink);
        }
        Point<Integer> finder = getReversePoints(transform(sX, sY, sZ, Xx, Yx, Zx), transform(sX, sY, sZ, Xy, Yy, Zy));
        g.drawRect(finder.x, finder.y, 5, 5);
        g.setColor(Color.black);
        g.drawString("x: " + sX + ", y: " + sY + ", z: " + sZ, 800, 50);
        g.setColor(Color.green);
        Point<Integer> origin = getReversePoints(transform(0, 0, 0, Xx, Yx, Zx), transform(0, 0, 0, Xy, Yy, Zy));
        Point<Integer> xfseg = getReversePoints(transform(sX, 0, 0, Xx, Yx, Zx), transform(sX, 0, 0, Xy, Yy, Zy));
        Point<Integer> yfseg = getReversePoints(transform(sX, sY, 0, Xx, Yx, Zx), transform(sX, sY, 0, Xy, Yy, Zy));
        g.drawLine(origin.x, origin.y, xfseg.x, xfseg.y);
        g.drawLine(xfseg.x, xfseg.y, yfseg.x, yfseg.y);
        g.drawLine(yfseg.x, yfseg.y, finder.x, finder.y);
    }

    /**
     * ???
     * @param g
     * @param i
     * @param j
     * @param k
     * @param Xx
     * @param Yx
     * @param Zx
     * @param Xy
     * @param Yy
     * @param Zy
     */
    public void drawRectangleAtPoint(Graphics g, double i, double j, double k, double Xx, double Yx, double Zx, double Xy, double Yy, double Zy) {
        g.setColor(ColorAtPoint(i, j, k));
        Point<Integer> coor = getReversePoints(transform(i, j, k, Xx, Yx, Zx), transform(i, j, k, Xy, Yy, Zy));
        try {
            Point<Integer> coor2 = getReversePoints(transform(i + .2, j, function(i + .2, j), Xx, Yx, Zx), transform(i + .2, j, function(i + .2, j), Xy, Yy, Zy));
            Point<Integer> coor3 = getReversePoints(transform(i, j + .2, function(i, j + .2), Xx, Yx, Zx), transform(i, j + .2, function(i, j + .2), Xy, Yy, Zy));
            Point<Integer> coor4 = getReversePoints(transform(i + .2, j + .2, function(i + .2, j + .2), Xx, Yx, Zx), transform(i + .2, j + .2, function(i + .2, j + .2), Xy, Yy, Zy));
            int[] xArray = {coor.x, coor2.x, coor3.x, coor4.x};
            int[] yArray = {coor.y, coor2.y, coor3.y, coor4.y};
            g.fillPolygon(xArray, yArray, 4);
        } catch (Exception e) {
            return;
        }
    }

    /**
     * ???
     * @param i
     * @param j
     * @param k
     * @return
     */
    public Color ColorAtPoint(double i, double j, double k) {
        double distance = Math.pow((lightX - i) * (lightX - i) + (lightY - j) * (lightY - j) + (lightZ - k) * (lightZ - k), .5);
        try {
            return colorArray[(int) ((distance / (2 * cubeVision)) * numberOfColors)];
        } catch (ArrayIndexOutOfBoundsException e) {
            return Color.black;
        }
    }
}

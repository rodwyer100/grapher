import java.awt.*;

public class ColorFun
/**
 * @author Chris Kjellqvist and Rory O'Dwyer
 * Generates color shadings and combines RGB colors.
 */
{
    public ColorFun() {
    }

    /**
     * Gives a smooth gradient of n samples from RGB c1 to RGB c2
     * @param n - number of samples
     * @param c1 - color to go from
     * @param c2 - color to go to
     * @return - Gradient of colors
     */
    public static Color[] getColorPallete(int n, float[] c1, float[] c2) {
        Color[] ar = new Color[n];
        for (int i = 0; i < n; i++) {
            float[] temp = combineHSB(c1, c2, (float) i / n);
            ar[i] = new Color(Color.HSBtoRGB(temp[0], temp[1], temp[2]));
        }
        return ar;
    }

    /**
     * Combines two HSB colors in ratio
     * @param c1 - color 1
     * @param c2 - color 2
     * @param ratio - ratio by which to combine
     * @return - combined HSB color
     */
    private static float[] combineHSB(float[] c1, float[] c2, float ratio) {
        float[] toReturn = new float[3];
        for (int i = 0; i < 3; i++) {
            toReturn[i] = c1[i] * (1 - ratio) + c2[i] * (ratio);
        }
        return toReturn;
    }
}

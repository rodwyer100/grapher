import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author Rory O'Dwyer
 * @date 6/14/16
 * 3D graphing Software for Stuff. It still needs some patches
 * Rory O'Dwyer with the aid of Chris Kjellqvist. Math concepts borrowed from desmos calculator project 12.6.15
 * Please reference all the creators of this project if code is borrowed.
 * 6/14/15
 */
public class Main {
    public static void main(String[] args) {
        System.out.print("The Key for a Rotation is a,b,or c, and to switch their directions its O"
                + "\nThe Keys for Changing the Light Source are WER for XYZ respectively");
        JFrame frame = new JFrame("3D Grapher");
        Display display = new Display();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setPreferredSize(dim);
        display.setPreferredSize(dim);
        frame.add(display);
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                boolean changed = false;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        display.yTranslation -= display.scale * 15;
                        changed = true;
                        break;
                    case KeyEvent.VK_DOWN:
                        display.yTranslation += display.scale * 15;
                        changed = true;
                        break;
                    case KeyEvent.VK_RIGHT:
                        display.xTranslation += display.scale * 15;
                        changed = true;
                        break;
                    case KeyEvent.VK_LEFT:
                        display.xTranslation -= display.scale * 15;
                        changed = true;
                        break;
                    case KeyEvent.VK_EQUALS:
                        display.zoom(.9);
                        changed = true;
                        break;
                    case KeyEvent.VK_MINUS:
                        display.zoom(1.1);
                        changed = true;
                        break;
                    case KeyEvent.VK_A:
                        display.aRotation += (display.multiplier * .01);
                        changed = true;
                        break;
                    case KeyEvent.VK_B:
                        display.bRotation += (display.multiplier * .01);
                        changed = true;
                        break;
                    case KeyEvent.VK_C:
                        display.cRotation += (display.multiplier * .01);
                        changed = true;
                        break;
                    case KeyEvent.VK_O:
                        display.multiplier *= -1;
                        changed = true;
                        break;
                    case KeyEvent.VK_Q:
                        display.cubeVision += 1 * (display.multiplier);
                        changed = true;
                        break;
                    case KeyEvent.VK_X:
                        display.sX += (display.multiplier * display.scale * 5);
                        changed = true;
                        break;
                    case KeyEvent.VK_Y:
                        display.sY += (display.multiplier * display.scale * 5);
                        changed = true;
                        break;
                    case KeyEvent.VK_Z:
                        display.sZ += (display.multiplier * display.scale * 5);
                        changed = true;
                        break;
                    case KeyEvent.VK_W:
                        display.lightX += (display.multiplier * display.scale * 5);
                        changed = true;
                        break;
                    case KeyEvent.VK_E:
                        display.lightY += (display.multiplier * display.scale * 5);
                        changed = true;
                        break;
                    case KeyEvent.VK_R:
                        display.lightZ += (display.multiplier * display.scale * 5);
                        changed = true;
                        break;

                }
                if (changed) {
                    display.repaint();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        display.repaint();
    }

}

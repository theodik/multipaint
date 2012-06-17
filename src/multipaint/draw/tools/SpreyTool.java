/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package multipaint.draw.tools;

import java.awt.Graphics;
import java.util.Random;

/**
 *
 * @author Juditth
 */
public class SpreyTool extends AbstractTool {

    private Random random = new Random();

    @Override
    public void draw(Graphics g, int last_x, int last_y, int x, int y) {
        g.setColor(color);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < Math.random() * width; j++) {
                int rx = random.nextInt(width);
                int ry = random.nextInt(width);
                g.drawOval(last_x + rx, last_y + ry, 1, 1);
            }
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package multipaint.draw.tools;

import java.awt.Graphics;

/**
 *
 * @author Juditth
 */
public class BrushTool extends AbstractTool {

    @Override
    public void draw(Graphics g, int last_x, int last_y, int x, int y) {
        g.setColor(color);
        g.fillOval(x - width, y - width, width * 2, width * 2);
        g.drawLine(last_x, last_y, x, y);
        for (int i = 0; i < width; i++) {
            g.drawLine(last_x + i, last_y, x + i, y);
            g.drawLine(last_x - i, last_y, x - i, y);
            g.drawLine(last_x, last_y + i, x, y + i);
            g.drawLine(last_x, last_y - i, x, y - i);
        }
    }
}

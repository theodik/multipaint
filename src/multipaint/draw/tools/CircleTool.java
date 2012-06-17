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
public class CircleTool extends AbstractTool {

    @Override
    public void draw(Graphics g, int last_x, int last_y, int x, int y) {

        g.setColor(color);
        g.drawOval(x - width / 2, y - width / 2, width, width);
    }
}

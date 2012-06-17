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
public class LineTool extends AbstractTool{

    @Override
    public void draw(Graphics g, int last_x, int last_y, int x, int y) {
        g.setColor(color);
        g.drawLine(x, y, x+width, y+width*2);
    }
    
}

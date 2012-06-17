/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package multipaint.draw.tools;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Juditth
 */
public class RectangleTool extends AbstractTool{

    @Override
    public void draw(Graphics g, int last_x, int last_y, int x, int y) {        
        g.setColor(Color.white);
        g.drawLine(last_x, last_y, x, y);
        g.drawRect(last_x, last_y, x, y);                
    }
}

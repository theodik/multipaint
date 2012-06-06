package multipaint.draw.tools;

import java.awt.Graphics;

public class PenTool extends AbstractTool {

    @Override
    public void draw(Graphics g, int last_x, int last_y, int x, int y) {
        g.setColor(color);
        g.drawLine(last_x, last_y, x, y);
    }

}

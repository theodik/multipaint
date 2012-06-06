package multipaint.draw.tools;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author theodik
 */
public interface Tool {

    void setColor(Color c);

    Color getColor();

    void setWidth(int w);

    int getWidth();

    void draw(Graphics g, int last_x, int last_y, int x, int y);
}

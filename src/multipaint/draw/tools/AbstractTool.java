package multipaint.draw.tools;

import java.awt.Color;

public abstract class AbstractTool implements Tool {
    protected Color color = Color.BLACK;
    protected int width = 1;

    public AbstractTool() {
    }

    public AbstractTool(Color c) {
        color = c;
    }

    public AbstractTool(int width) {
        this.width = width;
    }

    public AbstractTool(Color c, int width) {
        color = c;
        this.width = width;
    }

    @Override
    public void setColor(Color c) {
        color = c;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setWidth(int w) {
        width = w;
    }

    @Override
    public int getWidth() {
        return width;
    }
}

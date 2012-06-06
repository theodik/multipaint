package multipaint.draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import multipaint.draw.tools.PenTool;
import multipaint.draw.tools.Tool;

public class Canvas {
    private ArrayList<ChangeListener> listeners = new ArrayList<>();
    private Tool tool;
    private Color background = null;
    private final BufferedImage img;
    private final Graphics g;

    public Canvas(int width, int height) {
        img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        g = img.createGraphics();
        tool = new PenTool();
    }

    public void setTool(Tool tool) {
        this.tool = tool;
        for (ChangeListener l : listeners) {
            l.changeTool(tool);
        }
    }

    public Canvas setBackground(Color c) {
        this.background = c;
        for (ChangeListener l : listeners) {
            l.changeColor(c);
        }
        return this;
    }

    public Canvas setForeground(Color c) {
        tool.setColor(c);
        for (ChangeListener l : listeners) {
            l.changeTool(tool);
        }
        return this;
    }

    public void setColor(Color c) {
        setForeground(c);
    }

    public int getWidth() {
        return img.getWidth();
    }

    public int getHeight() {
        return img.getHeight();
    }

    public void draw(int last_x, int last_y, int x, int y) {
        tool.draw(g, last_x, last_y, x, y);
        for (ChangeListener l : listeners) {
            l.draw(last_x, last_y, x, y);
        }
    }

    public void clear() {
        g.setColor(background);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        for (ChangeListener l : listeners) {
            l.clear();
        }
    }

    public void print(Graphics g, int x, int y) {
        g.drawImage(img, x, y, null);
    }

    //-- Events
    public void addChangeListener(ChangeListener evt) {
        listeners.add(evt);
    }

    public void removeChangeListener(ChangeListener evt) {
        listeners.remove(evt);
    }

    public interface ChangeListener {
        void draw(int last_x, int last_y, int x, int y);

        void changeTool(Tool newTool);

        void changeColor(Color newColor);

        void clear();
    }
}

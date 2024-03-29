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
    private Color background = Color.WHITE;
    private final BufferedImage img;
    private final Graphics g;

    public Canvas(int width, int height) {
        img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        g = img.createGraphics();
        tool = new PenTool();
    }

    public synchronized void setTool(Tool tool) {
        this.tool = tool;
        for (ChangeListener l : listeners) {
            l.changeTool(tool);
        }
    }

    public synchronized void setBackground(Color c) {
        this.background = c;
    }

    public synchronized void setForeground(Color c) {
        tool.setColor(c);
    }

    public synchronized void setColor(Color c) {
        setForeground(c);
    }

    public synchronized int getWidth() {
        return img.getWidth();
    }

    public synchronized int getHeight() {
        return img.getHeight();
    }

    public synchronized void draw(int last_x, int last_y, int x, int y) {
        tool.draw(g, last_x, last_y, x, y);
        for (ChangeListener l : listeners) {
            l.draw(tool.getColor(), last_x, last_y, x, y);
        }
    }

    public synchronized void clear() {
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
        void draw(Color color, int last_x, int last_y, int x, int y);

        void changeTool(Tool newTool);

        void clear();
    }
}

package multipaint.draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import multipaint.draw.tools.Tool;

public class DrawPanel extends JPanel {
    private int selfX, selfY, lastX, lastY;
    private int offX = 0, offY = 0;
    private boolean mouseDown = false;
    private Canvas canvas = null;

    public DrawPanel() {
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (canvas == null) {
                    return;
                }
                mouseDown = true;
                selfX = lastX = e.getX();
                selfY = lastY = e.getY();
                canvas.draw(selfX - offX, selfY - offY, selfX - offX, selfY - offY);
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (canvas == null) {
                    return;
                }
                mouseDown = false;
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (canvas == null) {
                    return;
                }
                if (mouseDown) {
                    lastX = selfX;
                    lastY = selfY;
                    selfX = e.getX();
                    selfY = e.getY();
                    canvas.draw(lastX - offX, lastY - offY, selfX - offX, selfY - offY);
                    repaint();
                }
            }
        };
        addMouseListener(ma);
        addMouseMotionListener(ma);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (canvas == null) {
                    return;
                }
                offX = getWidth() / 2 - canvas.getWidth() / 2;
                if (offX < 0) {
                    offX = 0;
                }
                offY = getHeight() / 2 - canvas.getHeight() / 2;
                if (offY < 0) {
                    offY = 0;
                }
                repaint();
            }
        });
        //setBorder(BorderFactory.createLineBorder(Color.black));
    }

    @Override
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(getForeground());
        if (canvas != null) {
            g.drawRect(offX - 1, offY - 1, canvas.getWidth() + 1, canvas.getHeight() + 1);
            canvas.print(g, offX, offY);
            g.setColor(Color.black);
            g.drawString("mouseDown:" + mouseDown, 10, 10);
            g.drawString("selfX: " + selfX + " selfY: " + selfY + " lastX: " + lastX + " lastY: " + lastY, 10, 30);
            g.drawString("offX: " + offX + ", offY: " + offY, 10, 60);
        } else {
            String str = "Waiting for connection.";
            int sw = g.getFontMetrics().stringWidth(str);
            g.drawString(str, getWidth() / 2 - sw / 2, getHeight() / 2);
        }
    }

    public void setCanvas(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        this.canvas = canvas;
        offX = getWidth() / 2 - canvas.getWidth() / 2;
        offY = getHeight() / 2 - canvas.getHeight() / 2;
        repaint();
    }
}

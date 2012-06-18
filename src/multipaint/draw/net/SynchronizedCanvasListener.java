package multipaint.draw.net;

import java.awt.Color;
import java.io.IOException;
import java.nio.channels.Pipe;
import java.nio.charset.Charset;
import multipaint.draw.Canvas;
import multipaint.draw.tools.Tool;

class SynchronizedCanvasListener implements Canvas.ChangeListener {
    private final Pipe.SinkChannel sinkpipe;
    private final Charset charset;
    private String me;
    public volatile boolean ignoreEvents;

    public SynchronizedCanvasListener(Pipe pipe, Charset charset) {
        this.sinkpipe = pipe.sink();
        this.charset = charset;
    }

    private void send(String data) {
        try {
            sinkpipe.write(charset.encode(data));
        } catch (IOException ex) {
        }
    }

    @Override
    public void draw(Color color, int last_x, int last_y, int x, int y) {
        if (!ignoreEvents) {
            send("draw " + me + " " + color.getRGB() + " " + last_x + " " + last_y + " " + x + " " + y + "\n");
        }
    }

    @Override
    public void changeTool(Tool newTool) {
    }

    @Override
    public void clear() {
        if (!ignoreEvents) {
            send("clear " + me + "\n");
        }
    }

    public synchronized void setMe(String me) {
        this.me = me;
    }
}

package multipaint.draw.net;

import java.awt.Color;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Pipe;
import java.nio.charset.Charset;
import multipaint.draw.Canvas;
import multipaint.draw.tools.Tool;

public class DrawServer implements DrawSocket {
    private ServerThread cthread;

    @Override
    public void disconnect() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void connect(Canvas canvas, String host, int port) throws DrawNetException {
        if (cthread != null) {
            throw new DrawNetException("Already connected.");
        }
        try {
            cthread = new ServerThread(canvas, port);
            cthread.start();
        } catch (IOException e) {
            DrawNetException ex = new DrawNetException();
            ex.addSuppressed(e);
            throw ex;
        }
    }

    private class ServerThread extends Thread {
        private final Charset charset = Charset.forName("UTF-8");
        private final Canvas canvas;
        private final InetSocketAddress address;
        private final SynchronizedCanvasListener listener;
        private final Pipe pipe;

        public ServerThread(Canvas canvas, int port) throws IOException {
            this.pipe = Pipe.open();
            this.canvas = canvas;
            this.listener = new SynchronizedCanvasListener(pipe, charset);
            canvas.addChangeListener(listener);
            this.address = new InetSocketAddress(port);
        }

        @Override
        public void run() {
            
        }
    }
}

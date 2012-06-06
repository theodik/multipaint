package multipaint.draw.net;

import java.awt.Color;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import multipaint.draw.Canvas;
import multipaint.draw.Canvas.ChangeListener;
import multipaint.draw.tools.Tool;

public class DrawClient implements DrawSocket {
    private ConnectionThread cthread;

    @Override
    public void connect(Canvas canvas, String host, int port) throws DrawNetException {
        if (cthread != null) {
            throw new DrawNetException("Already connected.");
        }
        try {
            cthread = new ConnectionThread(canvas, host, port);
            cthread.start();
        } catch (IOException e) {
            DrawNetException ex = new DrawNetException();
            ex.addSuppressed(e);
            throw ex;
        }
    }

    @Override
    public void disconnect() {
        if (cthread == null) {
            return;
        }
        try {
            cthread.interrupt();
            cthread.join();
        } catch (InterruptedException e) {
        } finally {
            cthread = null;
        }
    }

    private class ConnectionThread extends Thread {
        private static final int BUFFER_SIZE = 512;
        private static final int MAX_CONN = 3;
        private final Charset charset = Charset.forName("UTF-8");
        private final ChangeListener listener;
        private final Canvas canvas;
        private final InetSocketAddress address;
        private String me = null; // self identificator - from server
        private final Pipe pipe;
        private volatile int connected;

        public ConnectionThread(Canvas canvas, String host, int port) throws IOException {
            pipe = Pipe.open();
            this.canvas = canvas;
            this.listener = new CanvasChangeListener();
            canvas.addChangeListener(listener);
            address = new InetSocketAddress(host, port);
        }

        @Override
        public void run() {
            ByteBuffer buffer = ByteBuffer.allocateDirect(512);

            Selector selector;
            DatagramChannel channel;
            Pipe.SourceChannel sourcePipe = pipe.source();

            try {
                selector = Selector.open();

                channel = DatagramChannel.open();
                channel.configureBlocking(false);
                channel.register(selector, SelectionKey.OP_READ);
                channel.connect(address);

                sourcePipe.configureBlocking(false);
                sourcePipe.register(selector, sourcePipe.validOps());
            } catch (IOException ex) {
                return;
            }

            main:
            while (!interrupted()) {
                if(connected > MAX_CONN){
                    System.err.println("Force close after 3 tries.");
                    break;
                }
                try {
                    channel.write(charset.encode("baf\n"));
                } catch (IOException ex) {
                    break;
                }

                try {
                    selector.select(30000);
                } catch (IOException ex) {
                    connected++;
                    continue;
                }
                for (SelectionKey key : selector.selectedKeys()) {
                    buffer.clear();
                    if (key.channel() instanceof DatagramChannel) {
                        try {
                            channel.read(buffer);
                        } catch (IOException ex) {
                            connected++;
                            continue main;
                        }
                        buffer.flip();
                        debugOut(buffer);
                        String resp = charset.decode(buffer).toString();
                        String send = processResponse(resp);
                        if (send != null) {
                            try {
                                channel.write(charset.encode(send + "\n"));
                            } catch (IOException ex) {
                                connected++;
                                continue main;
                            }
                            connected = 0;
                        }
                    } else if (key.channel() instanceof Pipe.SourceChannel) {
                        try {
                            sourcePipe.read(buffer);
                            buffer.flip();
                            //debugOut(buffer);
                            channel.write(buffer);
                        } catch (IOException ex) {
                            connected++;
                            continue main;
                        }
                    }
                } // end foreach
                selector.selectedKeys().clear();
            } // end main while
            try {
                channel.write(charset.encode("bye " + me));
                channel.close();
            } catch (IOException ex) {
            }
        }

        private void debugOut(ByteBuffer bb) {
            byte[] buff = new byte[BUFFER_SIZE];
            bb.get(buff, 0, bb.limit());
            System.out.println(new String(buff, 0, bb.limit()));
            bb.flip();
        }

        private String processResponse(String resp) {
            String[] split = resp.trim().split(" ");
            switch (split[0]) {
                case "ping":
                    return "pong";
                case "pong":
                    connected++;
                    break;
                case "draw":
                    try {
                        canvas.draw(
                                Integer.parseInt(split[2]),
                                Integer.parseInt(split[3]),
                                Integer.parseInt(split[4]),
                                Integer.parseInt(split[5]));
                    } catch (NumberFormatException e) {
                    }
                    break;
                case "clear":
                    canvas.clear();
                    break;
                case "color":
                    try {
                        canvas.setColor(new Color(Integer.parseInt(split[2])));
                    } catch (NumberFormatException e) {
                    }
                    break;
                case "tool":
                    break;
            }
            return null;
        }

        private class CanvasChangeListener implements ChangeListener {
            Pipe.SinkChannel sinkpipe = pipe.sink();

            private void send(String data) {
                try {
                    sinkpipe.write(charset.encode(data));
                } catch (IOException ex) {
                }
            }

            @Override
            public void draw(int last_x, int last_y, int x, int y) {
                send("draw " + me + " " + last_x + " " + last_y + " " + x + " " + y + "\n");
            }

            @Override
            public void changeTool(Tool newTool) {
            }

            @Override
            public void changeColor(Color newColor) {
                send("color " + me + " " + newColor.getRGB() + "\n");
            }

            @Override
            public void clear() {
                send("clear " + me + "\n");
            }
        }
    }
}
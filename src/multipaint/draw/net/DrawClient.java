package multipaint.draw.net;

import java.awt.Color;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Pipe;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import multipaint.draw.Canvas;
import multipaint.draw.Canvas.ChangeListener;
import multipaint.draw.tools.Tool;

public class DrawClient implements DrawSocket {
    private ClientThread cthread;
    private String name;

    @Override
    public void connect(Canvas canvas, String host, int port) throws DrawNetException {
        if (cthread != null) {
            throw new DrawNetException("Already connected.");
        }
        try {
            cthread = new ClientThread(canvas, name, host, port);
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

    public void setName(String text) {
        this.name = text;
    }

    private class ClientThread extends Thread {
        private static final int BUFFER_SIZE = 512;
        private static final int MAX_CONN = 3;
        private final Charset charset = Charset.forName("UTF-8");
        private final SynchronizedCanvasListener listener;
        private final Canvas canvas;
        private final InetSocketAddress address;
        private String name = null; // self identificator - from server
        private final Pipe pipe;
        private volatile int connected;

        private ClientThread(Canvas canvas, String name, String host, int port) throws IOException {
            pipe = Pipe.open();
            this.canvas = canvas;
            this.name = name;
            this.listener = new SynchronizedCanvasListener(pipe, charset);
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
                try {
                    if (connected > MAX_CONN) {
                        System.err.println("Force close after 3 tries.");
                        break;
                    }
//                try {
//                    channel.write(charset.encode("baf\n"));
//                } catch (IOException ex) {
//                    break;
//                }

                    try {
                        selector.select(1000);
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
                                System.err.println("Channel read err" + ex);
                                connected++;
                                continue main;
                            }
                            buffer.flip();
                            //debugOut(buffer);
                            String resp = charset.decode(buffer).toString();
                            processResponse(resp);
                        } else if (key.channel() instanceof Pipe.SourceChannel) {
                            try {
                                sourcePipe.read(buffer);
                                buffer.flip();
                                //debugOut(buffer);
                                channel.write(buffer);
                            } catch (IOException ex) {
                                System.err.println("(Pipe)Send error: " + ex);
                                connected++;
                                continue main;
                            }
                        }
                    } // end foreach
                    selector.selectedKeys().clear();
                } catch (Exception e) {
                    System.err.println("DrawClient main loop error: " + e);
                    connected++;
                    continue;
                }
            } // end main while

            try {
                channel.write(charset.encode("bye\n"));
                channel.close();
            } catch (IOException ex) {
            }
        }

        private void debugOut(ByteBuffer bb) {
            byte[] buff = new byte[bb.limit()];
            bb.get(buff);
            System.out.println(new String(buff, 0, bb.limit()));
            bb.flip();
        }

        private void processResponse(String resp) {
            String[] split = resp.trim().split(" ");
            listener.ignoreEvents = true;
            switch (split[0]) {
                case "pong":
                    break;
                case "draw":
                    try {
                        canvas.setColor(new Color(Integer.parseInt(split[2])));
                        canvas.draw(
                                Integer.parseInt(split[3]),
                                Integer.parseInt(split[4]),
                                Integer.parseInt(split[5]),
                                Integer.parseInt(split[6]));
                    } catch (NumberFormatException e) {
                        System.err.println(e);
                    }
                    break;
                case "clear":
                    canvas.clear();
                    break;
                case "tool":
                    break;
            }
            listener.ignoreEvents = false;
        }
    }
}

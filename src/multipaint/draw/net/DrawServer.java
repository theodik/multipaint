package multipaint.draw.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import multipaint.draw.Canvas;
import multipaint.draw.net.server.Client;

public class DrawServer implements DrawSocket {
    private ServerThread sthread;

    @Override
    public void disconnect() {
        if (sthread == null) {
            return;
        }
        try {
            sthread.interrupt();
            sthread.join();
        } catch (InterruptedException e) {
        } finally {
            sthread = null;
        }
    }

    @Override
    public void connect(Canvas canvas, String host, int port) throws DrawNetException {
        if (sthread != null) {
            throw new DrawNetException("Already connected.");
        }
        try {
            sthread = new ServerThread(canvas, port);
            sthread.start();
        } catch (IOException e) {
            DrawNetException ex = new DrawNetException();
            ex.addSuppressed(e);
            throw ex;
        }
    }

    public Set<Client> getClientList() {
        if (sthread == null) {
            throw new IllegalStateException("Server is not bound.");
        }
        return sthread.getClientList();
    }

    private class ServerThread extends Thread {
        private static final int BUFFER_SIZE = 512;
        private final Charset charset = Charset.forName("UTF-8");
        HashSet<Client> clientList = new HashSet<>();
        private final Canvas canvas;
        private final InetSocketAddress address;
        private final SynchronizedCanvasListener listener;
        private final Pipe pipe;
        private DatagramChannel channel;

        public ServerThread(Canvas canvas, int port) throws IOException {
            this.pipe = Pipe.open();
            this.canvas = canvas;
            this.listener = new SynchronizedCanvasListener(pipe, charset);
            canvas.addChangeListener(listener);
            this.address = new InetSocketAddress(port);
        }

        @Override
        public void run() {
            //-- initialize --
            Pipe.SourceChannel source = pipe.source();
            Selector selector;
            try {
                selector = Selector.open();

                channel = DatagramChannel.open();
                channel.configureBlocking(false);
                channel.register(selector, SelectionKey.OP_READ);
                channel.bind(address);

                source.configureBlocking(false);
                source.register(selector, SelectionKey.OP_READ);
            } catch (IOException e) {
                System.err.println("Error in creating server: " + e);
                return;
            }

            //-- process --
            ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
            main:
            while (!interrupted()) {
                try {
                    selector.select();
                } catch (IOException e) {
                }


                for (Iterator<SelectionKey> it = selector.selectedKeys().iterator(); it.hasNext();) {
                    SelectionKey key = it.next();
                    buffer.clear();
                    try {
                        if (key.channel() == channel) {
                            Client client = new Client(channel.receive(buffer));
                            if (clientList.add(client)) {
                                client.update();
                            }
                            sendToAll(buffer, client);
                        } else if (key.channel() == source) {
                            source.read(buffer);
                            sendToAll(buffer);
                        }
                    } catch (ClosedByInterruptException c) {
                        break main;
                    } catch (IOException e) {
                        System.out.println("Exception in sending: " + e);
                    }
                }
            }

            //-- finish --

        }

        private void sendToAll(String data) throws IOException {
            sendToAll(charset.encode(data));
        }

        private void sendToAll(ByteBuffer data) throws IOException {
            sendToAll(data, null);
        }

        private void sendToAll(ByteBuffer data, Client except) throws IOException {
            for (Iterator<Client> it = clientList.iterator(); it.hasNext();) {
                Client client = it.next();
                if (!client.isConnected()) {
                    it.remove();
                }
                if (client == except) {
                    continue;
                }
                data.flip();
                channel.send(data, client.getAddress());
            }
        }

        private Set<Client> getClientList() {
            return Collections.unmodifiableSet(clientList);
        }
    }

    public static void main(String[] args) {
        DrawServer server = new DrawServer();
        try {
            System.out.println("Listening at port 1234...");
            server.connect(new Canvas(100, 100), null, 1234);
        } catch (DrawNetException ex) {
            System.err.println("Cannot create server: " + ex);
            return;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String data;
        do {
            try {
                System.out.print("> ");
                data = reader.readLine().trim();
                switch (data) {
                    case "list":
                        System.out.println(server.getClientList());
                        break;
                }
            } catch (IOException ex) {
                System.err.println("Cannot read input: " + ex);
                break;
            }
        } while (!data.equals("end"));
        System.out.println("Disconnecting...");
        server.disconnect();
        System.out.println("Bye.");
    }
}

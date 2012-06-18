package multipaint.draw.net;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
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
    private BroadcastInfoThread bthread;
    private String serverName;

    @Override
    public void disconnect() {
        if (sthread == null) {
            return;
        }
        try {
            bthread.interrupt();
            sthread.interrupt();
            sthread.join();
            bthread.join();
        } catch (InterruptedException e) {
        } finally {
            sthread = null;
            bthread = null;
            serverName = null;
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
            serverName = host;
            bthread = new BroadcastInfoThread();
            bthread.start();
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

    public void send(String data) {
        if (sthread == null) {
            throw new IllegalStateException("Server is not bound.");
        }
        try {
            sthread.sendToAll(data);
        } catch (IOException ex) {
        }
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
                            final SocketAddress clientAddr = channel.receive(buffer);
                            if (clientAddr == null) {
                                continue;
                            }
                            Client client = new Client(clientAddr);
                            if (clientList.add(client)) {
                                client.update();
                            }
                            sendToAll(buffer, client);
                        } else if (key.channel() == source) {
                            source.read(buffer);
                            sendToAll(buffer);
                        }
                        buffer.flip();
                        String resp = charset.decode(buffer).toString();
                        processResponse(resp);
                    } catch (ClosedByInterruptException c) {
                        break main;
                    } catch (IOException e) {
                        System.out.println("Exception in sending: " + e);
                    }
                }
            }
            try {
                //-- finish --
                sendToAll("end");
                channel.close();
            } catch (IOException ex) {
            }
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

        public int getPort() {
            return address.getPort();
        }

        public int getWidth() {
            return canvas.getWidth();
        }

        public int getHeihgt() {
            return canvas.getHeight();
        }

        public int getClientCount() {
            return clientList.size();
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

    private class BroadcastInfoThread extends Thread {
        public BroadcastInfoThread() {
        }

        @Override
        public void run() {
            Charset charset = Charset.forName("UTF-8");
            Selector selector;
            DatagramChannel channel;
            try {
                selector = Selector.open();
                channel = DatagramChannel.open();
                channel.setOption(StandardSocketOptions.SO_BROADCAST, true);
                channel.configureBlocking(false);
                channel.bind(new InetSocketAddress(12345));
                channel.register(selector, SelectionKey.OP_READ);
            } catch (IOException e) {
                System.err.println("BroadcastInfoThread exception: " + e);
                sthread.interrupt();
                return;
            }

            ByteBuffer buffer = ByteBuffer.allocateDirect(512);
            while (!interrupted()) {
                try {
                    selector.select();
                } catch (IOException ex) {
                    continue;
                }
                try {
                    buffer.clear();
                    final SocketAddress client = channel.receive(buffer);
                    buffer.flip();
                    final String data = charset.decode(buffer).toString();
                    if (data.trim().equals("info")) {
                        buffer.clear();
                        sthread.getName();
                        final int port = sthread.getPort();
                        final int clientCount = sthread.getClientCount();
                        final int width = sthread.getWidth();
                        final int heihgt = sthread.getHeihgt();
                        channel.send(charset.encode("info " + serverName + " " + port + " " + clientCount + " " + width + " " + heihgt + "\n"), client);
                    }
                } catch (IOException e) {
                    System.err.println("Broadcast ex:" + e);
                }
            }
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
                    case "stop":
                        System.out.println("Stopping server...");
                        break;
                    default:
                        server.send(data);
                }
            } catch (IOException ex) {
                System.err.println("Cannot read input: " + ex);
                break;
            }
        } while (!data.equals("stop"));
        System.out.println("Disconnecting...");
        server.disconnect();
        System.out.println("Bye.");
    }
}

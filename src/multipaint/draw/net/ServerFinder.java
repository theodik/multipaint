package multipaint.draw.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.HashSet;

public class ServerFinder {
    private static ServerFinder finder;
    private FindListener listener;
    private Thread thread;

    public static ServerFinder findServers(FindListener listener) {
        if (finder == null || !finder.thread.isAlive()) {
            finder = new ServerFinder(listener);
            finder.start();
        }
        return finder;
    }

    public static ServerFinder getInstance() {
        if (finder != null && !finder.thread.isAlive()) {
            finder = null;
        }
        return finder;
    }

    public interface FindListener {
        void serverFound(String name, String ip, int port, int clients, int width, int height);

        void done();
    }

    private ServerFinder(FindListener listener) {
        this.listener = listener;
        thread = new FinderThread();
    }

    private void start() {
        thread.start();
    }

    public void stop() {
        thread.interrupt();
    }

    private class FinderThread extends Thread {
        @Override
        public void run() {
            Charset charset = Charset.forName("UTF-8");
            DatagramChannel channel;
            Selector selector;
            try {
                selector = Selector.open();
                channel = DatagramChannel.open();
                channel.setOption(StandardSocketOptions.SO_BROADCAST, true);
                channel.configureBlocking(false);
                channel.bind(null);
                channel.register(selector, SelectionKey.OP_READ);
            } catch (IOException e) {
                System.err.println("Searching exception: " + e);
                listener.done();
                return;
            }

            HashSet<InetSocketAddress> servers = new HashSet<>();
            long startTime = System.currentTimeMillis();
            ByteBuffer buffer = ByteBuffer.allocateDirect(512);
            while (!interrupted()) {
                try {
                    channel.send(charset.encode("info\n"), new InetSocketAddress("255.255.255.255", 12345));
                } catch (ClosedByInterruptException e) {
                    break;
                } catch (IOException ex) {
                    System.err.println("Failed sending info:" + ex);
                    break;
                }
                try {
                    selector.select(1000);
                } catch (IOException ex) {
                    System.err.println("Timed out:" + ex);
                    break;
                }
                if (System.currentTimeMillis() - startTime > 10000) {
                    break;
                }
                try {
                    buffer.clear();
                    final InetSocketAddress server = (InetSocketAddress) channel.receive(buffer);
                    if (!servers.add(server)) {
                        continue;
                    }
                    buffer.flip();
                    final String info = charset.decode(buffer).toString();
                    try {
                        String[] data = info.trim().split(" ");
                        if (data[0].equals("info")) {
                            String name = data[1];
                            String ip = server.getHostName();
                            int port = Integer.parseInt(data[2]);
                            int clients = Integer.parseInt(data[3]);
                            int width = Integer.parseInt(data[4]);
                            int height = Integer.parseInt(data[5]);
                            listener.serverFound(name, ip, port, clients, width, height);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Erorr parsing response: " + e);
                        break;
                    }
                } catch (ClosedByInterruptException e) {
                    break;
                } catch (IOException e) {
                    System.err.println("Error receiving: " + e);
                    break;
                }
            }
            listener.done();
        }
    }

    public static void main(String[] args) {
        ServerFinder.findServers(new FindListener() {
            @Override
            public void serverFound(String name, String ip, int port, int clients, int width, int height) {
                System.out.println(name + " " + ip + " " + port + " " + clients + " " + width + " " + height);
            }

            @Override
            public void done() {
                System.out.println("done");
            }
        });
    }
}

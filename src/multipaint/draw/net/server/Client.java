package multipaint.draw.net.server;

import java.net.SocketAddress;

public class Client {
    private final SocketAddress address;
    private long updated = -1;
    private long limit = 60000;

    public Client(SocketAddress address) {
        this.address = address;
        //updated = getTime();
    }

    private long getTime() {
        return System.currentTimeMillis();
    }

    public SocketAddress getAddress() {
        return address;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public long getLimit() {
        return limit;
    }

    public boolean isConnected() {
        return getTime() - updated < limit;
    }

    public void update() {
        updated = getTime();
    }

    @Override
    public String toString() {
        return address.toString();
    }
}

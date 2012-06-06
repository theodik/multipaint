package multipaint.draw.net;

import java.net.SocketException;

public class DrawNetException extends SocketException {
    public DrawNetException() {
    }

    public DrawNetException(String msg) {
        super(msg);
    }
}

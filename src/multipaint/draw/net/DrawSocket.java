package multipaint.draw.net;

import multipaint.draw.Canvas;

/**
 *
 * @author theodik
 */
public interface DrawSocket {
    void connect(Canvas canvas, String host, int port) throws DrawNetException;

    void disconnect();
}

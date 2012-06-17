package multipaint.draw.net;

import java.awt.Color;
import multipaint.draw.Canvas;
import multipaint.draw.tools.Tool;

public class DrawServer implements DrawSocket {

    @Override
    public void disconnect() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void connect(Canvas canvas, String host, int port) throws DrawNetException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

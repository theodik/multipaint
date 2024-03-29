package multipaint;

import java.awt.*;
import java.awt.event.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import multipaint.draw.DrawPanel;
import multipaint.draw.net.*;
import multipaint.draw.tools.Tool;

/**
 *
 * @author theodik
 */
public class MainPanel extends javax.swing.JPanel {
    private DrawSocket sock;
    private multipaint.draw.Canvas canvas;
    private CanvasListener canvasListener = new CanvasListener();

    /**
     * Creates new form MainPanel
     */
    public MainPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        connectionPanel = new JPanel();
        loginPanel = new JPanel();
        jLabel1 = new JLabel();
        localIPLabel = new JLabel();
        jLabel2 = new JLabel();
        nameField = new JTextField();
        serversPane = new JScrollPane();
        serversList = new JTable();
        controlPanel = new JPanel();
        refreshButton = new JButton();
        joinButton = new JButton();
        hostButton = new JButton();
        drawingPanel = new JPanel();
        drawPanel = new DrawPanel();
        toolPanel = new JPanel();
        disconnect = new JButton();
        colorChoose = new JButton();

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setName("MainPanel");
        setPreferredSize(new Dimension(600, 400));
        setLayout(new CardLayout());

        connectionPanel.setLayout(new BorderLayout(10, 10));

        loginPanel.setAutoscrolls(true);
        loginPanel.setMinimumSize(new Dimension(300, 62));
        loginPanel.setPreferredSize(new Dimension(300, 62));

        jLabel1.setLabelFor(nameField);
        jLabel1.setText("Jméno: ");

        localIPLabel.setText("Vaše IP:");

        jLabel2.setText("127.0.0.1");

        nameField.setText("Guest");
        nameField.setToolTipText("<html><font color=red size=4>Jméno může mít jen znaky A-Z nebo čísla</font></html>");
        nameField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                nameFieldKeyPressed(evt);
            }
            public void keyTyped(KeyEvent evt) {
                nameFieldKeyTyped(evt);
            }
        });

        GroupLayout loginPanelLayout = new GroupLayout(loginPanel);
        loginPanel.setLayout(loginPanelLayout);
        loginPanelLayout.setHorizontalGroup(
            loginPanelLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(loginPanelLayout.createParallelGroup(Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(localIPLabel))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(loginPanelLayout.createParallelGroup(Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(nameField, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(411, Short.MAX_VALUE))
        );
        loginPanelLayout.setVerticalGroup(
            loginPanelLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(loginPanelLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(loginPanelLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(localIPLabel)
                    .addComponent(jLabel2))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        connectionPanel.add(loginPanel, BorderLayout.PAGE_START);

        serversList.setModel(new DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Jméno serveru", "IP Adresa", "Port", "Ping", "Počet hráčů", "Výška plochy", "Šířka plochy"
            }
        ) {
            Class[] types = new Class [] {
                String.class, String.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        serversList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        serversList.setShowHorizontalLines(false);
        serversList.setShowVerticalLines(false);
        serversList.getTableHeader().setReorderingAllowed(false);
        serversPane.setViewportView(serversList);
        serversList.getColumnModel().getColumn(2).setMaxWidth(50);
        serversList.getColumnModel().getColumn(3).setMaxWidth(50);

        connectionPanel.add(serversPane, BorderLayout.CENTER);

        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 5));

        refreshButton.setText("Obnovit");
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });
        controlPanel.add(refreshButton);

        joinButton.setText("Připojit");
        joinButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                joinButtonActionPerformed(evt);
            }
        });
        controlPanel.add(joinButton);

        hostButton.setText("Vytvořit server");
        hostButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                hostButtonActionPerformed(evt);
            }
        });
        controlPanel.add(hostButton);

        connectionPanel.add(controlPanel, BorderLayout.PAGE_END);

        add(connectionPanel, "card2");

        drawingPanel.setLayout(new BorderLayout(3, 3));

        drawPanel.setBackground(new Color(255, 255, 255));

        GroupLayout drawPanelLayout = new GroupLayout(drawPanel);
        drawPanel.setLayout(drawPanelLayout);
        drawPanelLayout.setHorizontalGroup(
            drawPanelLayout.createParallelGroup(Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
        drawPanelLayout.setVerticalGroup(
            drawPanelLayout.createParallelGroup(Alignment.LEADING)
            .addGap(0, 354, Short.MAX_VALUE)
        );

        drawingPanel.add(drawPanel, BorderLayout.CENTER);

        toolPanel.setLayout(new BorderLayout());

        disconnect.setText("Konec");
        disconnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                disconnectActionPerformed(evt);
            }
        });
        toolPanel.add(disconnect, BorderLayout.LINE_END);

        colorChoose.setText("Barva");
        colorChoose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                colorChooseActionPerformed(evt);
            }
        });
        toolPanel.add(colorChoose, BorderLayout.LINE_START);

        drawingPanel.add(toolPanel, BorderLayout.PAGE_END);

        add(drawingPanel, "card3");
    }// </editor-fold>//GEN-END:initComponents

    private void hostButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_hostButtonActionPerformed
        final Frame frame = (Frame) getTopLevelAncestor();
        CreateServerDialog csd = new CreateServerDialog(frame, true);
        csd.setLocationRelativeTo(frame);
        csd.setVisible(true);
        if (csd.resultAvailable()) {
            canvas = csd.getCanvas();
            canvas.addChangeListener(canvasListener);
            drawPanel.setCanvas(canvas);
            DrawServer server = new DrawServer();
            try {
                server.connect(canvas, csd.getName(), csd.getPort());
                sock = server;
                selectNextTab();
            } catch (DrawNetException ex) {
                canvas = null;
                JOptionPane.showConfirmDialog(this, "Nelze spustit server: " + ex.getLocalizedMessage());
            }
        }
    }//GEN-LAST:event_hostButtonActionPerformed

    private void refreshButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        ServerFinder finder;
        if ((finder = ServerFinder.getInstance()) != null) {
            finder.stop();
        } else {
            refreshButton.setText("Stop");
            ((DefaultTableModel) serversList.getModel()).setRowCount(0);
            ServerFinder.findServers(new ServerFinder.FindListener() {
                @Override
                public void serverFound(String name, String ip, int port, int clients, int width, int height) {
                    final DefaultTableModel model = (DefaultTableModel) serversList.getModel();
                    model.addRow(new Object[]{name, ip, port, 0, clients, width, height});
                }

                @Override
                public void done() {
                    refreshButton.setText("Obnovit");
                }
            });
        }
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void joinButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_joinButtonActionPerformed
        assert sock != null : "Socket musí být odpojen!";
        if (serversList.getSelectedRow() == -1) {
            return;
        }
        canvas = new multipaint.draw.Canvas(
                (Integer) serversList.getValueAt(serversList.getSelectedRow(), 5),
                (Integer) serversList.getValueAt(serversList.getSelectedRow(), 6));
        canvas.addChangeListener(canvasListener);
        assert canvas == null;
        DrawClient client = new DrawClient();
        sock = client;
        client.setName(nameField.getText());
        try {
            client.connect(
                    canvas,
                    (String) serversList.getValueAt(serversList.getSelectedRow(), 1),
                    (Integer) serversList.getValueAt(serversList.getSelectedRow(), 2));
            drawPanel.setCanvas(canvas);
        } catch (DrawNetException ex) {
            System.out.println("DNE:" + ex);
        }
        selectNextTab();
    }//GEN-LAST:event_joinButtonActionPerformed

    private void colorChooseActionPerformed(ActionEvent evt) {//GEN-FIRST:event_colorChooseActionPerformed
        final Frame frame = (Frame) getTopLevelAncestor();
        Color newColor = ColorDialog.showAndGetColor(frame);
        if (newColor != null) {
            drawPanel.getCanvas().setColor(newColor);
        }
    }//GEN-LAST:event_colorChooseActionPerformed

    private void disconnectActionPerformed(ActionEvent evt) {//GEN-FIRST:event_disconnectActionPerformed
        sock.disconnect();
        sock = null;
        // odstraňuji komplet canvas, nemusim odebírat listener
        //canvas.removeChangeListener(canvasListener);
        canvas = null;
        drawPanel.setCanvas(null);
        selectNextTab();
    }//GEN-LAST:event_disconnectActionPerformed

    private void nameFieldKeyPressed(KeyEvent evt) {//GEN-FIRST:event_nameFieldKeyPressed
        if (!Character.toString(evt.getKeyChar()).matches("[a-zA-Z0-9]")) {
            ToolTipManager.sharedInstance().mouseMoved(
                    new MouseEvent(nameField, 0, 0, 0,
                    0, 0, // X-Y of the mouse for the tool tip
                    0, false));
            evt.consume();
        }
    }//GEN-LAST:event_nameFieldKeyPressed

    private void nameFieldKeyTyped(KeyEvent evt) {//GEN-FIRST:event_nameFieldKeyTyped
        if (!Character.toString(evt.getKeyChar()).matches("[a-zA-Z0-9]")) {
            evt.consume();
        }
    }//GEN-LAST:event_nameFieldKeyTyped

    private void selectNextTab() {
        CardLayout cl = (CardLayout) getLayout();
        cl.next(this);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton colorChoose;
    private JPanel connectionPanel;
    private JPanel controlPanel;
    private JButton disconnect;
    private DrawPanel drawPanel;
    private JPanel drawingPanel;
    private JButton hostButton;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JButton joinButton;
    private JLabel localIPLabel;
    private JPanel loginPanel;
    private JTextField nameField;
    private JButton refreshButton;
    private JTable serversList;
    private JScrollPane serversPane;
    private JPanel toolPanel;
    // End of variables declaration//GEN-END:variables

    public void dispose() {
        if (sock != null) {
            sock.disconnect();
        }
    }

    private class CanvasListener implements multipaint.draw.Canvas.ChangeListener {
        private void update() {
            drawingPanel.repaint();
        }

        @Override
        public void draw(Color color, int last_x, int last_y, int x, int y) {
            update();
        }

        @Override
        public void changeTool(Tool newTool) {
            update();
        }

        @Override
        public void clear() {
            update();
        }
    }
}

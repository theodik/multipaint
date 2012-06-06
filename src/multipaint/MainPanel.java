package multipaint;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import multipaint.draw.DrawPanel;
import multipaint.draw.net.DrawClient;
import multipaint.draw.net.DrawNetException;
import multipaint.draw.net.DrawSocket;

/**
 *
 * @author theodik
 */
public class MainPanel extends javax.swing.JPanel {
    private DrawSocket sock;

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
        toolsBar = new JToolBar();
        jToggleButton1 = new JToggleButton();
        drawPanel = new DrawPanel();
        userPane = new JScrollPane();
        userList = new JList();
        chatPanel = new JPanel();
        jScrollPane1 = new JScrollPane();
        chatText = new JTextArea();
        chatMessage = new JTextField();

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
        nameField.setToolTipText("");
        nameField.setMinimumSize(new Dimension(100, 20));

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
                    .addComponent(nameField, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(380, Short.MAX_VALUE))
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
                {"server", "127.0.0.1",  new Integer(1234),  new Integer(0),  new Integer(0),  new Integer(100),  new Integer(100)}
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

        toolsBar.setRollover(true);

        jToggleButton1.setIcon(new ImageIcon(getClass().getResource("/multipaint/draw/images/pen.png")));         jToggleButton1.setSelected(true);
        jToggleButton1.setText("Pero");
        jToggleButton1.setFocusable(false);
        jToggleButton1.setVerticalTextPosition(SwingConstants.BOTTOM);
        toolsBar.add(jToggleButton1);

        drawingPanel.add(toolsBar, BorderLayout.PAGE_START);

        drawPanel.setBackground(new Color(255, 255, 255));
        drawPanel.setCanvas(null);

        GroupLayout drawPanelLayout = new GroupLayout(drawPanel);
        drawPanel.setLayout(drawPanelLayout);
        drawPanelLayout.setHorizontalGroup(
            drawPanelLayout.createParallelGroup(Alignment.LEADING)
            .addGap(0, 542, Short.MAX_VALUE)
        );
        drawPanelLayout.setVerticalGroup(
            drawPanelLayout.createParallelGroup(Alignment.LEADING)
            .addGap(0, 263, Short.MAX_VALUE)
        );

        drawingPanel.add(drawPanel, BorderLayout.CENTER);

        userList.setModel(new AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        userPane.setViewportView(userList);

        drawingPanel.add(userPane, BorderLayout.EAST);

        chatPanel.setLayout(new BorderLayout());

        chatText.setColumns(20);
        chatText.setRows(5);
        jScrollPane1.setViewportView(chatText);

        chatPanel.add(jScrollPane1, BorderLayout.CENTER);
        chatPanel.add(chatMessage, BorderLayout.PAGE_END);

        drawingPanel.add(chatPanel, BorderLayout.PAGE_END);

        add(drawingPanel, "card3");
    }// </editor-fold>//GEN-END:initComponents

    private void hostButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_hostButtonActionPerformed
        final Frame frame = (Frame) getTopLevelAncestor();
        CreateServerDialog csd = new CreateServerDialog(frame, true);
        csd.setLocationRelativeTo(frame);
        csd.setVisible(true);
        if (csd.getResult()) {
            drawPanel.setCanvas(csd.getCanvas());
            selectNextTab();
        }

    }//GEN-LAST:event_hostButtonActionPerformed

    private void refreshButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        selectNextTab();
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void joinButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_joinButtonActionPerformed
        assert sock != null : "Socket musí být odpojen!";
        multipaint.draw.Canvas canvas = new multipaint.draw.Canvas(
                (Integer) serversList.getValueAt(serversList.getSelectedRow(), 5),
                (Integer) serversList.getValueAt(serversList.getSelectedRow(), 6));
        assert canvas == null;
        sock = new DrawClient();
        try {
            sock.connect(
                    canvas,
                    (String) serversList.getValueAt(serversList.getSelectedRow(), 1),
                    (Integer) serversList.getValueAt(serversList.getSelectedRow(), 2));
            drawPanel.setCanvas(canvas);
        } catch (DrawNetException ex) {
            System.out.println("DNE:"+ex);
        }
        selectNextTab();
    }//GEN-LAST:event_joinButtonActionPerformed

    private void selectNextTab() {
        CardLayout cl = (CardLayout) getLayout();
        cl.next(this);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JTextField chatMessage;
    private JPanel chatPanel;
    private JTextArea chatText;
    private JPanel connectionPanel;
    private JPanel controlPanel;
    private DrawPanel drawPanel;
    private JPanel drawingPanel;
    private JButton hostButton;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JScrollPane jScrollPane1;
    private JToggleButton jToggleButton1;
    private JButton joinButton;
    private JLabel localIPLabel;
    private JPanel loginPanel;
    private JTextField nameField;
    private JButton refreshButton;
    private JTable serversList;
    private JScrollPane serversPane;
    private JToolBar toolsBar;
    private JList userList;
    private JScrollPane userPane;
    // End of variables declaration//GEN-END:variables
}

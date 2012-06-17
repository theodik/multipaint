package multipaint;

import java.awt.Color;
import java.awt.Frame;

/**
 *
 * @author theodik
 */
public class ColorDialog extends javax.swing.JDialog {
    private Color color;

    /**
     * Creates new form ColorDialog
     */
    public ColorDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
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

        colorChooser = new javax.swing.JColorChooser();
        controlPanel = new javax.swing.JPanel();
        ok = new javax.swing.JButton();
        cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Výběr barvy...");
        setAlwaysOnTop(true);
        setModal(true);
        getContentPane().add(colorChooser, java.awt.BorderLayout.CENTER);

        controlPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20));
        controlPanel.setLayout(new java.awt.BorderLayout());

        ok.setText("OK");
        ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okActionPerformed(evt);
            }
        });
        controlPanel.add(ok, java.awt.BorderLayout.LINE_END);

        cancel.setText("Storno");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });
        controlPanel.add(cancel, java.awt.BorderLayout.LINE_START);

        getContentPane().add(controlPanel, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        dispose();
    }//GEN-LAST:event_cancelActionPerformed

    private void okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okActionPerformed
        color = colorChooser.getColor();
        dispose();
    }//GEN-LAST:event_okActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancel;
    private javax.swing.JColorChooser colorChooser;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JButton ok;
    // End of variables declaration//GEN-END:variables

    public Color getColor() {
        return color;
    }

    public static Color showAndGetColor(Frame parent) {
        ColorDialog cd = new ColorDialog(parent, true);
        cd.setLocationRelativeTo(parent);
        cd.setVisible(true);
        return cd.getColor();
    }
}

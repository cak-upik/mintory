/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * buttonMaster.java
 *
 * Created on Mar 27, 2012, 9:35:19 AM
 */
package mintory.config;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;

/**
 *
 * @author i1440ns
 */
public class buttonUbahSaldo extends javax.swing.JPanel {

    /** Creates new form buttonMaster */
    public buttonUbahSaldo() {
        initComponents();
        defaultMode();
    }

    public void defaultMode() {
        btnSimpan.setEnabled(false);
        btnBatal.setEnabled(false);
        btnKeluar.setEnabled(true);
    }

    public void ubahMode() {
        btnSimpan.setEnabled(true);
        btnBatal.setEnabled(true);
        btnKeluar.setEnabled(false);
    }

    public void simpanMode() {
        btnSimpan.setEnabled(false);
        btnBatal.setEnabled(true);
        btnKeluar.setEnabled(false);
    }

    public void keluarMode() {
        btnSimpan.setEnabled(false);
        btnBatal.setEnabled(false);
        btnKeluar.setEnabled(false);
    }

    public JButton getBtnBatal() {
        return btnBatal;
    }

    public JButton getBtnKeluar() {
        return btnKeluar;
    }

    public JButton getBtnSimpan() {
        return btnSimpan;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnSimpan = new javax.swing.JButton();
        btnKeluar = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();

        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paperman/images/save.png"))); // NOI18N
        btnSimpan.setText("Simpan  F3");

        btnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paperman/images/dekstop.png"))); // NOI18N
        btnKeluar.setText("Keluar  F6");

        btnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paperman/images/stop.png"))); // NOI18N
        btnBatal.setText("Batal  F5");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBatal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnKeluar)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnBatal, btnKeluar, btnSimpan});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(btnSimpan)
                .addComponent(btnBatal)
                .addComponent(btnKeluar))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnBatal, btnKeluar, btnSimpan});

    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnSimpan;
    // End of variables declaration//GEN-END:variables

}
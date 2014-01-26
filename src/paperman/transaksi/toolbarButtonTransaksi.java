/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * toolbarButtonMaster.java
 *
 * Created on Apr 21, 2012, 2:03:33 PM
 */
package paperman.transaksi;

import javax.swing.JButton;

/**
 *
 * @author i1440ns
 */
public class toolbarButtonTransaksi extends javax.swing.JPanel {

    /** Creates new form toolbarButtonMaster */
    public toolbarButtonTransaksi() {
        initComponents();
        defaultMode();
    }

    public JButton getBtnBatal() {
        return btnBatal;
    }

    public JButton getBtnCetak() {
        return btnCetak;
    }

    public JButton getBtnHapus() {
        return btnHapus;
    }

    public JButton getBtnKeluar() {
        return btnKeluar;
    }

    public JButton getBtnSimpan() {
        return btnSimpan;
    }

    public JButton getBtnTambah() {
        return btnTambah;
    }

    public JButton getBtnUbah() {
        return btnUbah;
    }

    public void defaultMode() {
        btnTambah.setEnabled(true);
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
        btnSimpan.setEnabled(false);
        btnBatal.setEnabled(false);
        btnCetak.setEnabled(false);
        btnKeluar.setEnabled(true);
    }

    public void firstTransaksiMode() {
        btnTambah.setEnabled(false);
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
        btnSimpan.setEnabled(true);
        btnBatal.setEnabled(false);
        btnCetak.setEnabled(false);
        btnKeluar.setEnabled(false);
    }

    public void editMode() {
        btnTambah.setEnabled(false);
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
        btnSimpan.setEnabled(true);
        btnBatal.setEnabled(true);
        btnCetak.setEnabled(false);
        btnKeluar.setEnabled(false);
    }

    public void tableMode() {
        btnTambah.setEnabled(false);
        btnUbah.setEnabled(true);
        btnHapus.setEnabled(true);
        btnSimpan.setEnabled(false);
        btnBatal.setEnabled(true);
        btnCetak.setEnabled(true);
        btnKeluar.setEnabled(true);
    }

    public void keluarMode() {
        btnTambah.setEnabled(false);
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
        btnSimpan.setEnabled(false);
        btnBatal.setEnabled(false);
        btnCetak.setEnabled(false);
        btnKeluar.setEnabled(false);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnTambah = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnKeluar = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        btnCetak = new javax.swing.JButton();

        btnTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paperman/images/new.png"))); // NOI18N
        btnTambah.setText("Tambah F1");

        btnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paperman/images/stop.png"))); // NOI18N
        btnBatal.setText("Batal F6");

        btnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paperman/images/deletes.png"))); // NOI18N
        btnHapus.setText("Hapus F4");

        btnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paperman/images/dekstop.png"))); // NOI18N
        btnKeluar.setText("Keluar F7");

        btnUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paperman/images/edit.png"))); // NOI18N
        btnUbah.setText("Ubah F2");

        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paperman/images/save.png"))); // NOI18N
        btnSimpan.setText("Simpan F3");

        btnCetak.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paperman/images/print.png"))); // NOI18N
        btnCetak.setText("Cetak F5");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUbah)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSimpan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHapus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCetak)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBatal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnKeluar))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnBatal, btnCetak, btnHapus, btnKeluar, btnSimpan, btnTambah, btnUbah});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnUbah)
                .addComponent(btnSimpan)
                .addComponent(btnHapus)
                .addComponent(btnCetak, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnBatal)
                .addComponent(btnKeluar))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnBatal, btnCetak, btnHapus, btnKeluar, btnSimpan, btnTambah, btnUbah});

    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnCetak;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUbah;
    // End of variables declaration//GEN-END:variables
}
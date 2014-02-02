/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ClosingBulanan.java
 *
 * Created on 31 Agu 12, 20:31:03
 */
package mintory.transaksi;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JOptionPane;
import mintory.Main;
import mintory.TextComponentUtils;
import mintory.model.aksiClosing;
import mintory.model.closingBulanan;
import mintory.model.closingBulananPutih;
import mintory.model.isClosedFor;
import mintory.model.setoran;
import mintory.model.setoranPutih;
import mintory.model.sistem;

/**
 *
 * @author Nurul Chusna
 */
public class SaldoAwal extends javax.swing.JInternalFrame {

    /** Creates new form ClosingBulanan */
    private static sistem sys;
    private static SaldoAwal formSaldoAwal;
    private closingBulanan closingModel;
    private closingBulananPutih closingModelPutih;
    private List<setoran> listSetoran;
    private List<setoranPutih> listSetoranPutih;
    private List<closingBulanan> listClosingBulanan;
    private List<closingBulananPutih> listClosingBulananPutih;
    private BigDecimal totalAngsuran = BigDecimal.ZERO;
    private BigDecimal totalTabungan = BigDecimal.ZERO;
    private BigDecimal totalKasbon = BigDecimal.ZERO;
    private BigDecimal totalBayarKas = BigDecimal.ZERO;
    private BigDecimal totalOvertime = BigDecimal.ZERO;
    private BigDecimal totalCicilan = BigDecimal.ZERO;
    private BigDecimal totalSetoran = BigDecimal.ZERO;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("ddMMyyyy");
    public static boolean closingState = false;

    public static void inisialisasi() {
        formSaldoAwal = new SaldoAwal();
    }

    public static void destroy() {
        formSaldoAwal.dispose();
        formSaldoAwal = null;
        System.gc();
    }

    public static SaldoAwal getFormSaldoAwal() {
        return formSaldoAwal;
    }

    public SaldoAwal() {
        initComponents();
        TextComponentUtils.setCurrency(txtTotalAngsuran);
        TextComponentUtils.setCurrency(txtTotalTabungan);
        TextComponentUtils.setCurrency(txtTotalKasbon);
        TextComponentUtils.setCurrency(txtTotalBayarKas);
        TextComponentUtils.setCurrency(txtTotalOvertime);
        TextComponentUtils.setCurrency(txtTotalCicilan);
        TextComponentUtils.setCurrency(txtTotalSetoran);
        listSetoran = Main.getTransaksiService().getLatestSetoranCount();
        listSetoranPutih = Main.getTransaksiService().getLatestSetoranPutihCount();
        sys = Main.getSistemService().sistemRecord();
//        LoadDataToForm();
    }

//    private void LoadDataToForm() {
//        listSetoran = Main.getTransaksiService().getLatestSetoranCount();
//        listSetoranPutih = Main.getTransaksiService().getLatestSetoranPutihCount();
//        if (cmbJenisArmada.getModel().getSelectedItem().toString().equalsIgnoreCase("PUTIH")) {
//            listClosingBulananPutih = Main.getTransaksiService().closingBulananPutihRecordForSaldoAwal();
//            if (!listSetoranPutih.isEmpty() && !listClosingBulananPutih.isEmpty()) {
//                clearForm();
//                txtTotalAngsuran.setText(TextComponentUtils.formatNumber(listClosingBulananPutih.get(0).getTotalAngsuran()));
//                txtTotalTabungan.setText(TextComponentUtils.formatNumber(listClosingBulananPutih.get(0).getTotalTabungan()));
//                txtTotalKasbon.setText(TextComponentUtils.formatNumber(listClosingBulananPutih.get(0).getTotalKas()));
//                txtTotalBayarKas.setText(TextComponentUtils.formatNumber(listClosingBulananPutih.get(0).getTotalBayarKas()));
//                txtTotalOvertime.setText(TextComponentUtils.formatNumber(listClosingBulananPutih.get(0).getTotalOvertime()));
//                txtTotalCicilan.setText(TextComponentUtils.formatNumber(listClosingBulananPutih.get(0).getTotalCicilan()));
//                txtTotalSetoran.setText(TextComponentUtils.formatNumber(listClosingBulananPutih.get(0).getTotalSetor()));
//                txtSetoranKe.setText(listSetoranPutih.get(0).getCounter_setoran().toString());
//            } else {
//                txtSetoranKe.setText("1");
//            }
//        }else if(cmbJenisArmada.getModel().getSelectedItem().toString().equalsIgnoreCase("BIRU")) {
//            listClosingBulanan = Main.getTransaksiService().closingBulananRecordForSaldoAwal();
//            if (!listSetoran.isEmpty() && !listClosingBulanan.isEmpty()) {
//                clearForm();
//                txtTotalAngsuran.setText(TextComponentUtils.formatNumber(listClosingBulanan.get(0).getTotalAngsuran()));
//                txtTotalTabungan.setText(TextComponentUtils.formatNumber(listClosingBulanan.get(0).getTotalTabungan()));
//                txtTotalKasbon.setText(TextComponentUtils.formatNumber(listClosingBulanan.get(0).getTotalKas()));
//                txtTotalBayarKas.setText(TextComponentUtils.formatNumber(listClosingBulanan.get(0).getTotalBayarKas()));
//                txtTotalOvertime.setText(TextComponentUtils.formatNumber(listClosingBulanan.get(0).getTotalOvertime()));
//                txtTotalCicilan.setText(TextComponentUtils.formatNumber(listClosingBulanan.get(0).getTotalCicilan()));
//                txtTotalSetoran.setText(TextComponentUtils.formatNumber(listClosingBulanan.get(0).getTotalSetor()));
//                txtSetoranKe.setText(listSetoran.get(0).getCounter_setoran().toString());
//            } else {
//                txtSetoranKe.setText("1");
//            }
//        }
//    }
    private void clearForm() {
        txtTotalAngsuran.setText("");
        txtTotalBayarKas.setText("");
        txtTotalCicilan.setText("");
        txtTotalKasbon.setText("");
        txtTotalOvertime.setText("");
        txtTotalSetoran.setText("");
        txtTotalTabungan.setText("");
        txtSetoranKe.setText("");
    }

    private void setSaldoAwal() {
        if (closingModel == null) {
            closingModel = new closingBulanan();
        }
        totalSetoran = TextComponentUtils.parseNumberToBigDecimal(txtTotalSetoran.getText());
        totalAngsuran = TextComponentUtils.parseNumberToBigDecimal(txtTotalAngsuran.getText());
        totalTabungan = TextComponentUtils.parseNumberToBigDecimal(txtTotalTabungan.getText());
        totalKasbon = TextComponentUtils.parseNumberToBigDecimal(txtTotalKasbon.getText());
        totalBayarKas = TextComponentUtils.parseNumberToBigDecimal(txtTotalBayarKas.getText());
        totalOvertime = TextComponentUtils.parseNumberToBigDecimal(txtTotalOvertime.getText());
        totalCicilan = TextComponentUtils.parseNumberToBigDecimal(txtTotalCicilan.getText());

//        if (!listSetoran.isEmpty()) {
//            closingModel.setPeriodeBulan(listSetoran.get(0).getTglSPO());
//        } else {
        closingModel.setPeriodeBulan(Main.getSistemService().sistemRecord().getTglKerja());
//        }
//        closingModel.setTglClosing(dateSetSaldo.getDate());
        closingModel.setRefNoLambung(new Integer(txtNoLambung.getText()));
        closingModel.setTotalSetor(totalSetoran);
        closingModel.setTotalAngsuran(totalAngsuran);
        closingModel.setTotalTabungan(totalTabungan);
        closingModel.setTotalKas(totalKasbon);
        closingModel.setTotalBayarKas(totalBayarKas);
        closingModel.setTotalOvertime(totalOvertime);
        closingModel.setTotalCicilan(totalCicilan);
        closingModel.setRefSetoranKe(new Integer(txtSetoranKe.getText()));
        closingModel.setClosedFor(isClosedFor.CLOSING_SALDO_AWAL);
        closingModel.setActClosing(aksiClosing.DO_CLOSING_SALDO_AWAL);
        closingModel.setIdKomposisiRefSaldoAwal(Main.getSistemService().findKomposisiByName(cmbJenisArmada.getSelectedItem().toString()));
        closingModel.setUser(sys.getLastLoginUser().getNamaLogin());
    }

    private void setSaldoAwalPutih() {
        if (closingModelPutih == null) {
            closingModelPutih = new closingBulananPutih();
        }
        totalSetoran = TextComponentUtils.parseNumberToBigDecimal(txtTotalSetoran.getText());
        totalAngsuran = TextComponentUtils.parseNumberToBigDecimal(txtTotalAngsuran.getText());
        totalTabungan = TextComponentUtils.parseNumberToBigDecimal(txtTotalTabungan.getText());
        totalKasbon = TextComponentUtils.parseNumberToBigDecimal(txtTotalKasbon.getText());
        totalBayarKas = TextComponentUtils.parseNumberToBigDecimal(txtTotalBayarKas.getText());
        totalOvertime = TextComponentUtils.parseNumberToBigDecimal(txtTotalOvertime.getText());
        totalCicilan = TextComponentUtils.parseNumberToBigDecimal(txtTotalCicilan.getText());

//        if (!listSetoranPutih.isEmpty()) {
//            closingModelPutih.setPeriodeBulan(listSetoran.get(0).getTglSPO());
//        } else {
        closingModelPutih.setPeriodeBulan(Main.getSistemService().sistemRecord().getTglKerja());
//        }
//        closingModel.setTglClosing(dateSetSaldo.getDate());
        closingModelPutih.setRefNoLambung(new Integer(txtNoLambung.getText()));
        closingModelPutih.setRefSetoranKe(new Integer(txtSetoranKe.getText()));
        closingModelPutih.setTotalSetor(totalSetoran);
        closingModelPutih.setTotalAngsuran(totalAngsuran);
        closingModelPutih.setTotalTabungan(totalTabungan);
        closingModelPutih.setTotalKas(totalKasbon);
        closingModelPutih.setTotalBayarKas(totalBayarKas);
        closingModelPutih.setTotalOvertime(totalOvertime);
        closingModelPutih.setTotalCicilan(totalCicilan);
        closingModelPutih.setClosedFor(isClosedFor.CLOSING_SALDO_AWAL);
        closingModelPutih.setActClosing(aksiClosing.DO_CLOSING_SALDO_AWAL);
        closingModelPutih.setIdKomposisiRefSaldoAwal(Main.getSistemService().findKomposisiByName(cmbJenisArmada.getSelectedItem().toString()));
        closingModelPutih.setUser(sys.getLastLoginUser().getNamaLogin());
    }

    private boolean validateForm() {
        if (!txtTotalAngsuran.getText().isEmpty() && !txtTotalTabungan.getText().isEmpty() && !txtTotalKasbon.getText().isEmpty()
                && !txtTotalBayarKas.getText().isEmpty() && !txtTotalOvertime.getText().isEmpty() && !txtTotalCicilan.getText().isEmpty()
                && !txtTotalSetoran.getText().isEmpty() && !txtSetoranKe.getText().isEmpty()) {
            return true;
        }
        return false;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnProses = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtTotalAngsuran = new javax.swing.JTextField();
        txtTotalTabungan = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtTotalKasbon = new javax.swing.JTextField();
        txtTotalBayarKas = new javax.swing.JTextField();
        txtTotalOvertime = new javax.swing.JTextField();
        txtTotalCicilan = new javax.swing.JTextField();
        txtTotalSetoran = new javax.swing.JTextField();
        btnBatal = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txtSetoranKe = new javax.swing.JTextField();
        cmbJenisArmada = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtNoLambung = new javax.swing.JTextField();

        setClosable(true);
        setTitle("Pengaturan Saldo Awal");

        jLabel1.setText("Total Angsuran");

        jLabel3.setText("Total Tabungan");

        btnProses.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnProses.setText("PROSES");
        btnProses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProsesActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 51, 255));

        jLabel12.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("  Input Pengaturan Saldo Awal");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel12)
                .addContainerGap(240, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
        );

        jLabel2.setText("Total Kasbon");

        jLabel4.setText("Total Bayar Kas");

        jLabel5.setText("Total Overtime");

        jLabel6.setText("Total Cicilan");

        jLabel7.setText("Total Setoran");

        btnBatal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnBatal.setText("BATAL");
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });

        jLabel8.setText("Setoran Ke");

        cmbJenisArmada.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PUTIH", "BIRU", "NEW BIRU" }));
        cmbJenisArmada.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbJenisArmadaItemStateChanged(evt);
            }
        });

        jLabel9.setText("Jenis Armada");

        jLabel10.setText("No Lambung");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnProses, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(56, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtNoLambung)
                    .addComponent(cmbJenisArmada, 0, 167, Short.MAX_VALUE))
                .addGap(161, 161, 161))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTotalSetoran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTotalCicilan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTotalTabungan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTotalOvertime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTotalBayarKas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTotalKasbon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSetoranKe, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 37, Short.MAX_VALUE)
                        .addComponent(txtTotalAngsuran, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(62, 62, 62))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtTotalAngsuran, txtTotalBayarKas, txtTotalCicilan, txtTotalKasbon, txtTotalOvertime, txtTotalSetoran, txtTotalTabungan});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbJenisArmada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtNoLambung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTotalAngsuran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtTotalTabungan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtTotalKasbon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtTotalBayarKas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtTotalOvertime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtTotalCicilan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtTotalSetoran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSetoranKe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnProses, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnProsesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProsesActionPerformed
        // TODO add your handling code here:
        if (validateForm()) {
            if (cmbJenisArmada.getSelectedItem().toString().equalsIgnoreCase("PUTIH")) {
                setSaldoAwalPutih();
                Main.getTransaksiService().save(closingModelPutih);
                JOptionPane.showMessageDialog(this, "Input Saldo Awal Telah Berhasil Dilakukan Oleh '" + sys.getLastLoginUser().getNamaLogin() + "'", "Pesan Sistem", JOptionPane.INFORMATION_MESSAGE);
                listClosingBulananPutih = null;
                destroy();
            } else if (cmbJenisArmada.getSelectedItem().toString().equalsIgnoreCase("BIRU") || cmbJenisArmada.getSelectedItem().toString().equalsIgnoreCase("NEW BIRU")) {
                setSaldoAwal();
                Main.getTransaksiService().save(closingModel);
                JOptionPane.showMessageDialog(this, "Input Saldo Awal Telah Berhasil Dilakukan Oleh '" + sys.getLastLoginUser().getNamaLogin() + "'", "Pesan Sistem", JOptionPane.INFORMATION_MESSAGE);
                listClosingBulanan = null;
                destroy();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Input Saldo Awal Tidak Dapat Dilakukan\nHarap Periksa Lagi Pengisian Data", "Pesan Sistem", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnProsesActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        // TODO add your handling code here:
        destroy();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void cmbJenisArmadaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbJenisArmadaItemStateChanged
        // TODO add your handling code here:
//        LoadDataToForm();
    }//GEN-LAST:event_cmbJenisArmadaItemStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnProses;
    private javax.swing.JComboBox cmbJenisArmada;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtNoLambung;
    private javax.swing.JTextField txtSetoranKe;
    private javax.swing.JTextField txtTotalAngsuran;
    private javax.swing.JTextField txtTotalBayarKas;
    private javax.swing.JTextField txtTotalCicilan;
    private javax.swing.JTextField txtTotalKasbon;
    private javax.swing.JTextField txtTotalOvertime;
    private javax.swing.JTextField txtTotalSetoran;
    private javax.swing.JTextField txtTotalTabungan;
    // End of variables declaration//GEN-END:variables
}

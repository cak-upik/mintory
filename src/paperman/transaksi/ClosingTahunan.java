/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ClosingBulanan.java
 *
 * Created on 31 Agu 12, 20:31:03
 */
package paperman.transaksi;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import org.joda.time.DateTime;
import paperman.Main;
import paperman.model.closingTahunan;
import paperman.model.closingTahunanPutih;
import paperman.model.isClosedFor;
import paperman.model.isClosedStatus;
import paperman.model.setoran;
import paperman.model.setoranPutih;

/**
 *
 * @author Nurul Chusna
 */
public class ClosingTahunan extends javax.swing.JInternalFrame {

    /** Creates new form ClosingBulanan */
    private static ClosingTahunan formClosingTahun;
    private closingTahunan closingModel;
    private closingTahunanPutih closingModelPutih;
    private BigDecimal totalAngsuran = BigDecimal.ZERO;
    private BigDecimal totalTabungan = BigDecimal.ZERO;
    private BigDecimal totalKasbon = BigDecimal.ZERO;
    private BigDecimal totalBayarKas = BigDecimal.ZERO;
    private BigDecimal totalOvertime = BigDecimal.ZERO;
    private BigDecimal totalCicilan = BigDecimal.ZERO;
    private BigDecimal totalSetoran = BigDecimal.ZERO;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy");
    public static boolean closingState = false;

    public static void inisialisasi() {
        formClosingTahun = new ClosingTahunan();
    }

    public static void destroy() {
        formClosingTahun.dispose();
        formClosingTahun = null;
        System.gc();
    }

    public static ClosingTahunan getFormClosingTahun() {
        return formClosingTahun;
    }

    public ClosingTahunan() {
        initComponents();
        Date tglkerja = Main.getSistemService().sistemRecord().getTglKerja();
        dateAwal.setDate(new DateTime(tglkerja.getTime()).withDayOfYear(new DateTime(tglkerja.getTime()).dayOfYear().getMinimumValue()).toDate());
        dateAkhir.setDate(new DateTime(tglkerja.getTime()).withDayOfYear(new DateTime(tglkerja.getTime()).dayOfYear().getMaximumValue()).toDate());
    }

    private void setClosingBiru() {
        if (closingModel == null) {
            closingModel = new closingTahunan();
        }
        totalSetoran = Main.getTransaksiService().sumSetoran(dateAwal.getDate(), dateAkhir.getDate());
        totalAngsuran = Main.getTransaksiService().sumAngsuran(dateAwal.getDate(), dateAkhir.getDate());
        totalTabungan = Main.getTransaksiService().sumTabungan(dateAwal.getDate(), dateAkhir.getDate());
        totalKasbon = Main.getTransaksiService().sumKasbon(dateAwal.getDate(), dateAkhir.getDate());
        totalBayarKas = Main.getTransaksiService().sumBayarKasbon(dateAwal.getDate(), dateAkhir.getDate());
        totalOvertime = Main.getTransaksiService().sumOvertime(dateAwal.getDate(), dateAkhir.getDate());
        totalCicilan = Main.getTransaksiService().sumCicilan(dateAwal.getDate(), dateAkhir.getDate());

        closingModel.setPeriodeAwal(dateAwal.getDate());
        closingModel.setPeriodeAkhir(dateAkhir.getDate());
        closingModel.setTglClosing(Main.getSistemService().sistemRecord().getTglKerja());
        closingModel.setTotalSetor(totalSetoran);
        closingModel.setTotalAngsuran(totalAngsuran);
        closingModel.setTotalTabungan(totalTabungan);
        closingModel.setTotalKas(totalKasbon);
        closingModel.setTotalBayarKas(totalBayarKas);
        closingModel.setTotalOvertime(totalOvertime);
        closingModel.setTotalCicilan(totalCicilan);
        closingModel.setClosedFor(isClosedFor.CLOSING_TAHUN);
    }

    private void setClosingPutih() {
        if (closingModelPutih == null) {
            closingModelPutih = new closingTahunanPutih();
        }
        totalSetoran = Main.getTransaksiService().sumSetoranPutih(dateAwal.getDate(), dateAkhir.getDate());
        totalAngsuran = Main.getTransaksiService().sumAngsuranPutih(dateAwal.getDate(), dateAkhir.getDate());
        totalTabungan = Main.getTransaksiService().sumTabunganPutih(dateAwal.getDate(), dateAkhir.getDate());
        totalKasbon = Main.getTransaksiService().sumKasbonPutih(dateAwal.getDate(), dateAkhir.getDate());
        totalBayarKas = Main.getTransaksiService().sumBayarKasbonPutih(dateAwal.getDate(), dateAkhir.getDate());
        totalOvertime = Main.getTransaksiService().sumOvertimePutih(dateAwal.getDate(), dateAkhir.getDate());
        totalCicilan = Main.getTransaksiService().sumCicilanPutih(dateAwal.getDate(), dateAkhir.getDate());

        closingModelPutih.setPeriodeAwal(dateAwal.getDate());
        closingModelPutih.setPeriodeAkhir(dateAkhir.getDate());
        closingModelPutih.setTglClosing(Main.getSistemService().sistemRecord().getTglKerja());
        closingModelPutih.setTotalSetor(totalSetoran);
        closingModelPutih.setTotalAngsuran(totalAngsuran);
        closingModelPutih.setTotalTabungan(totalTabungan);
        closingModelPutih.setTotalKas(totalKasbon);
        closingModelPutih.setTotalBayarKas(totalBayarKas);
        closingModelPutih.setTotalOvertime(totalOvertime);
        closingModelPutih.setTotalCicilan(totalCicilan);
        closingModelPutih.setClosedFor(isClosedFor.CLOSING_TAHUN);
    }

    private void doBackupMysql(String host, String dbUsername, String dbPassword, String dbName, String path) {
        String backupCommand = "mysqldump -h" + host + " -u" + dbUsername + " -p" + dbPassword + " --add-drop-database -B " + dbName + " -r " + path;
        System.out.println("backup command = " + backupCommand);
        Process runtimeProcess;
        try {
            runtimeProcess = Runtime.getRuntime().exec(backupCommand);
            int processComplete = runtimeProcess.waitFor();

            if (processComplete == 0) {
                JOptionPane.showMessageDialog(this, "Proses Backup Database Sukses Dilakukan", "Pesan Sistem", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Proses Backup Database Gagal Dilakukan", "Pesan Sistem", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Terjadi Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
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
        dateAwal = new com.toedter.calendar.JDateChooser();
        checkBackUp = new javax.swing.JCheckBox();
        btnProses = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        dateAkhir = new com.toedter.calendar.JDateChooser();
        mTitleButton1 = new id.web.martinusadyh.panel.MTitleButton();
        jLabel4 = new javax.swing.JLabel();
        cmbJenisArmada = new javax.swing.JComboBox();

        setClosable(true);
        setTitle("Proses Akhir Tahun - Armada Putih");

        jLabel1.setText("Periode Awal");

        jLabel3.setText("Backup Data");

        dateAwal.setDateFormatString("dd MMM yyyy");

        checkBackUp.setText("Ya");
        checkBackUp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkBackUpMouseClicked(evt);
            }
        });

        btnProses.setFont(new java.awt.Font("Tahoma", 1, 11));
        btnProses.setText("PROSES");
        btnProses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProsesActionPerformed(evt);
            }
        });

        jLabel2.setText("Periode Akhir");

        mTitleButton1.setBorder(null);
        mTitleButton1.setFontTitle(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        mTitleButton1.setIconHelpVisible(false);
        mTitleButton1.setTextTitlePanel("  Closing Transaksi Setoran Tahunan");

        jLabel4.setText("Jenis Armada");

        cmbJenisArmada.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PUTIH", "BIRU" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbJenisArmada, 0, 135, Short.MAX_VALUE)
                    .addComponent(dateAwal, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkBackUp))
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(btnProses, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
                .addContainerGap(14, Short.MAX_VALUE))
            .addComponent(mTitleButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {dateAkhir, dateAwal});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mTitleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbJenisArmada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(dateAwal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dateAkhir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(checkBackUp))
                .addGap(10, 10, 10)
                .addComponent(btnProses, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnProsesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProsesActionPerformed
        // TODO add your handling code here:
        int maxDateOfYear = new DateTime(new Date().getTime()).dayOfYear().getMaximumValue();
        if (dateAwal.getDate() != null && dateAkhir.getDate() != null && checkBackUp.isSelected()) {
            if (cmbJenisArmada.getSelectedIndex() == 0) {
                if (new DateTime(Main.getSistemService().sistemRecord().getTglKerja().getTime()).getDayOfYear() == maxDateOfYear) {
                    closingState = true;
                    setClosingPutih();
                    doBackupMysql("localhost", "root", "root", "papermandb", "src\\paperman\\backupDB\\backupPutih" + dateFormatter.format(dateAkhir.getDate()) + ".sql");
                    Main.getTransaksiService().save(closingModelPutih);
                    List<setoranPutih> listSetoranPutih = Main.getTransaksiService().setoranPutihRecordInCustomRange(dateAwal.getDate(), dateAkhir.getDate());
                    for (setoranPutih stPutih : listSetoranPutih) {
                        stPutih.setIdClosingTahun(closingModelPutih);
                        stPutih.setClosedStatus(isClosedStatus.CLOSED);
                        Main.getTransaksiService().save(stPutih);
                    }
                    Main.getMainMenu().setTanggalKerja(new DateTime(dateAkhir.getDate().getTime()).plusYears(1).toDate());
                    JOptionPane.showMessageDialog(this, "Closing Tahunan Telah Dilakukan Oleh 'Set User'", "Pesan Sistem", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Tanggal Ini Belum Mencapai Akhir Tahun\nClosing Tahunan Tidak Dapat Dilakukan", "Pesan Sistem", JOptionPane.INFORMATION_MESSAGE);
                    dateAwal.setEnabled(false);
                    dateAkhir.setEnabled(false);
                    checkBackUp.setEnabled(false);
                    btnProses.setEnabled(false);
                }
            }else if(cmbJenisArmada.getSelectedIndex() == 1) {
                if (new DateTime(Main.getSistemService().sistemRecord().getTglKerja().getTime()).getDayOfYear() == maxDateOfYear) {
                    closingState = true;
                    setClosingBiru();
                    doBackupMysql("localhost", "root", "root", "papermandb", "src\\paperman\\backupDB\\backup" + dateFormatter.format(dateAkhir.getDate()) + ".sql");
                    Main.getTransaksiService().save(closingModel);
                    List<setoran> listSetoran = Main.getTransaksiService().setoranRecordInCustomRange(dateAwal.getDate(), dateAkhir.getDate());
                    for (setoran st : listSetoran) {
                        st.setIdClosingTahun(closingModel);
                        st.setClosedStatus(isClosedStatus.CLOSED);
                        Main.getTransaksiService().save(st);
                    }
                    Main.getMainMenu().setTanggalKerja(new DateTime(dateAkhir.getDate().getTime()).plusYears(1).toDate());
                    JOptionPane.showMessageDialog(this, "Closing Tahunan Telah Dilakukan Oleh 'Set User'", "Pesan Sistem", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Tanggal Ini Belum Mencapai Akhir Tahun\nClosing Tahunan Tidak Dapat Dilakukan", "Pesan Sistem", JOptionPane.INFORMATION_MESSAGE);
                    dateAwal.setEnabled(false);
                    dateAkhir.setEnabled(false);
                    checkBackUp.setEnabled(false);
                    btnProses.setEnabled(false);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Harap Isi Periode Bulan Dan Pilih Backup Database", "Pesan Sistem", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    }//GEN-LAST:event_btnProsesActionPerformed

    private void checkBackUpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkBackUpMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBackUpMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnProses;
    private javax.swing.JCheckBox checkBackUp;
    private javax.swing.JComboBox cmbJenisArmada;
    private com.toedter.calendar.JDateChooser dateAkhir;
    private com.toedter.calendar.JDateChooser dateAwal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private id.web.martinusadyh.panel.MTitleButton mTitleButton1;
    // End of variables declaration//GEN-END:variables
}

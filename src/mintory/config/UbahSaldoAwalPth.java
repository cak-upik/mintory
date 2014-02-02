/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * UbahSaldoAwal.java
 *
 * Created on Apr 29, 2013, 2:57:04 PM
 */

package mintory.config;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import mintory.Main;
import mintory.TextComponentUtils;
import mintory.model.aksiClosing;
import mintory.model.closingBulananPutih;
import mintory.model.isClosedFor;
import mintory.tablemodel.UbahSaldoPutihTableModel;

/**
 *
 * @author cak-upik
 */
public class UbahSaldoAwalPth extends javax.swing.JInternalFrame implements ListSelectionListener {
    private static UbahSaldoAwalPth ubahSaldoPth;
    private closingBulananPutih clbPutih;
    private List<closingBulananPutih> listClosingBulananPutih = new ArrayList<closingBulananPutih>();

    public static void inisialisasi() {
        ubahSaldoPth = new UbahSaldoAwalPth();
    }

    public static void destroy() {
        ubahSaldoPth.dispose();
        ubahSaldoPth = null;
        System.gc();
    }

    public static UbahSaldoAwalPth getUbahSaldoAwalPutih() {
        return ubahSaldoPth;
    }

    /** Creates new form UbahSaldoAwal */
    public UbahSaldoAwalPth() {
        initComponents();
        initButtonListener();
        listClosingBulananPutih = Main.getTransaksiService().closingBulananPutihRecordForSaldoAwal();
        tblSaldo.setAutoCreateColumnsFromModel(false);
        tblSaldo.getSelectionModel().addListSelectionListener(this);
        LoadDatabaseToTable();
        enableForm(false);
    }

    private void initButtonListener() {
        toolbarButton.getBtnSimpan().addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aksiSimpan();
            }
        });
        toolbarButton.getBtnBatal().addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aksiBatal();
            }
        });
        toolbarButton.getBtnKeluar().addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aksiKeluar();
            }
        });
    }

     // ----------------- BUTTON HANDLING -------------------//
    private void aksiSimpan() {
        if (validateForm()) {
            LoadFormToDatabase();
            Main.getTransaksiService().save(clbPutih);
            LoadDatabaseToTable();
            JOptionPane.showMessageDialog(this, "Proses Penyimpanan Sukses", "Pesan Sistem", JOptionPane.INFORMATION_MESSAGE);
            enableForm(false);
            enableSearchEngine(true);
            housekeeping();
            tblSaldo.setEnabled(true);
            toolbarButton.defaultMode();
        } else {
            JOptionPane.showMessageDialog(this, "Proses Penyimpanan Gagal\nPeriksa Lagi Isian Anda Apakah Sudah Benar", "Pesan Sistem", JOptionPane.ERROR_MESSAGE);
//            kenaTilang();
            enableForm(true);
            enableSearchEngine(false);
            tblSaldo.setEnabled(false);
            toolbarButton.ubahMode();
        }
    }

//    private void cekDuplicateData() {
//        if (txtJenisArmada.getText().equalsIgnoreCase("BIRU")) {
//            listSetoran = Main.getTransaksiService().setoranRecord();
//            for (int i = 0; i <= listSetoran.size(); i++) {
//                if (listSetoran.get(i).getKode().substring(13).compareTo(txtNomor.getText()) > 0) {
//                    JOptionPane.showMessageDialog(Main.getMainMenu(), "Nomor Yang Anda Masukkan Sudah Berjalan\nNomor Yang Dimasukkan Harus Lebih Dari Nomor Yang Berjalan");
//                    isDuplicateData = true;
//                    txtNomor.requestFocus();
//                    enableForm(true);
//                    enableSearchEngine(false);
//                    toolbarButton.tambahMode();
//                    tblData.setEnabled(false);
//                } else {
//                    isDuplicateData = false;
//                }
//            }
//        } else {
//            listSetoranPutih = Main.getTransaksiService().setoranPutihRecord();
//            for (int i = 0; i <= listSetoranPutih.size(); i++) {
//                if (listSetoranPutih.get(i).getKode().substring(13).compareTo(txtNomor.getText()) > 0) {
//                    JOptionPane.showMessageDialog(Main.getMainMenu(), "Nomor Yang Anda Masukkan Sudah Berjalan\nNomor Yang Dimasukkan Harus Lebih Dari Nomor Yang Berjalan");
//                    isDuplicateData = true;
//                    txtNomor.requestFocus();
//                    enableForm(true);
//                    enableSearchEngine(false);
//                    toolbarButton.tambahMode();
//                    tblData.setEnabled(false);
//                } else {
//                    isDuplicateData = false;
//                }
//            }
//        }
//    }

    private void aksiBatal() {
        enableForm(false);
        enableSearchEngine(true);
        housekeeping();
        tblSaldo.setEnabled(true);
        toolbarButton.defaultMode();
    }

    private void aksiKeluar() {
        enableForm(false);
        enableSearchEngine(false);
        housekeeping();
        toolbarButton.keluarMode();
        destroy();
    }

    // ---------------- FORM HANDLING -------------------//
    private void enableForm(boolean e) {
        txtNoLambung.setEnabled(e);
        txtSetoranKe.setEnabled(e);
        txtAngsuran.setEnabled(e);
        txtTabungan.setEnabled(e);
        txtKasbon.setEnabled(e);
        txtBayarKas.setEnabled(e);
        txtOvertime.setEnabled(e);
        txtCicilan.setEnabled(e);
        txtTotalSetoran.setEnabled(e);
    }

    private void enableSearchEngine(boolean e) {
        txtCari.setEnabled(e);
        cmbKriteria.setEnabled(e);
    }

    private void housekeeping() {
        txtNoLambung.setText("");
        txtSetoranKe.setText("");
        txtAngsuran.setText("");
        txtTabungan.setText("");
        txtKasbon.setText("");
        txtBayarKas.setText("");
        txtOvertime.setText("");
        txtCicilan.setText("");
        txtTotalSetoran.setText("");
        tblSaldo.getSelectionModel().clearSelection();
    }

//    private void kenaTilang() {
//        if (txtID.getText().isEmpty() && txtJenisArmada.getText().isEmpty() && txtJenisTransaksi.getText().isEmpty() && txtNomor.getText().isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Tidak Boleh Ada Field Yang Kosong\nPeriksa Lagi!!!", "Sistem", JOptionPane.ERROR_MESSAGE);
//            txtID.setText("TIDAK BOLEH KOSONG...");
//            txtID.requestFocus();
//            txtJenisArmada.setText("TIDAK BOLEH KOSONG...");
//            txtJenisTransaksi.setText("TIDAK BOLEH KOSONG...");
//        } else if (txtID.getText().isEmpty() || txtID.getText().equalsIgnoreCase("tidak boleh kosong...")) {
//            JOptionPane.showMessageDialog(this, "!!! Harap isi ID !!!", "Sistem", JOptionPane.ERROR_MESSAGE);
//            txtID.setText("TIDAK BOLEH KOSONG");
//            txtID.requestFocus();
//        } else if (txtJenisArmada.getText().isEmpty() || txtJenisArmada.getText().equalsIgnoreCase("tidak boleh kosong...")) {
//            JOptionPane.showMessageDialog(this, "!!! Harap isi Jenis Armada !!!", "Sistem", JOptionPane.ERROR_MESSAGE);
//            txtJenisArmada.setText("TIDAK BOLEH KOSONG");
//            txtJenisArmada.requestFocus();
//        } else if (txtJenisTransaksi.getText().isEmpty() || txtJenisTransaksi.getText().equalsIgnoreCase("tidak boleh kosong...")) {
//            JOptionPane.showMessageDialog(this, "!!! Harap isi Jenis Transaksi !!!", "Sistem", JOptionPane.ERROR_MESSAGE);
//            txtJenisTransaksi.setText("TIDAK BOLEH KOSONG");
//            txtJenisTransaksi.requestFocus();
//        }
//    }

    private void LoadFormToDatabase() {
        if (clbPutih == null) {
            clbPutih = new closingBulananPutih();
        }
        clbPutih.setRefNoLambung(new Integer(txtNoLambung.getText()));
        clbPutih.setActClosing(aksiClosing.NOPE);
        clbPutih.setClosedFor(isClosedFor.CLOSING_SALDO_AWAL);
        clbPutih.setRefSetoranKe(new Integer(txtSetoranKe.getText()));
        clbPutih.setTotalAngsuran(TextComponentUtils.parseNumberToBigDecimal(txtAngsuran.getText()));
        clbPutih.setTotalTabungan(TextComponentUtils.parseNumberToBigDecimal(txtTabungan.getText()));
        clbPutih.setTotalKas(TextComponentUtils.parseNumberToBigDecimal(txtKasbon.getText()));
        clbPutih.setTotalBayarKas(TextComponentUtils.parseNumberToBigDecimal(txtBayarKas.getText()));
        clbPutih.setTotalOvertime(TextComponentUtils.parseNumberToBigDecimal(txtOvertime.getText()));
        clbPutih.setTotalCicilan(TextComponentUtils.parseNumberToBigDecimal(txtCicilan.getText()));
        clbPutih.setTotalSetor(TextComponentUtils.parseNumberToBigDecimal(txtTotalSetoran.getText()));
    }

    private void LoadDatabaseToForm() {
        txtNoLambung.setText(clbPutih.getRefNoLambung().toString());
        txtSetoranKe.setText(clbPutih.getRefSetoranKe().toString());
        txtAngsuran.setText(TextComponentUtils.formatNumber(clbPutih.getTotalAngsuran()));
        txtTabungan.setText(TextComponentUtils.formatNumber(clbPutih.getTotalTabungan()));
        txtKasbon.setText(TextComponentUtils.formatNumber(clbPutih.getTotalKas()));
        txtBayarKas.setText(TextComponentUtils.formatNumber(clbPutih.getTotalBayarKas()));
        txtOvertime.setText(TextComponentUtils.formatNumber(clbPutih.getTotalOvertime()));
        txtCicilan.setText(TextComponentUtils.formatNumber(clbPutih.getTotalCicilan()));
        txtTotalSetoran.setText(TextComponentUtils.formatNumber(clbPutih.getTotalSetor()));
    }

    private void LoadDatabaseToTable() {
        listClosingBulananPutih = Main.getTransaksiService().closingBulananPutihRecordForSaldoAwal();
        tblSaldo.setModel(new UbahSaldoPutihTableModel(listClosingBulananPutih));
        initColumn();
    }

    private void initColumn() {
        tblSaldo.getColumnModel().getColumn(0).setMinWidth(100);
        tblSaldo.getColumnModel().getColumn(0).setMaxWidth(100);
        tblSaldo.getColumnModel().getColumn(1).setMinWidth(100);
        tblSaldo.getColumnModel().getColumn(1).setMaxWidth(120);
        tblSaldo.getColumnModel().getColumn(2).setMinWidth(100);
        tblSaldo.getColumnModel().getColumn(2).setMaxWidth(120);
        tblSaldo.getColumnModel().getColumn(3).setMinWidth(250);
        tblSaldo.getColumnModel().getColumn(3).setMaxWidth(300);
        tblSaldo.getColumnModel().getColumn(4).setMinWidth(250);
        tblSaldo.getColumnModel().getColumn(4).setMaxWidth(300);
        tblSaldo.getColumnModel().getColumn(5).setMinWidth(250);
        tblSaldo.getColumnModel().getColumn(5).setMaxWidth(300);
        tblSaldo.getColumnModel().getColumn(6).setMinWidth(250);
        tblSaldo.getColumnModel().getColumn(6).setMaxWidth(300);
        tblSaldo.getColumnModel().getColumn(7).setMinWidth(250);
        tblSaldo.getColumnModel().getColumn(7).setMaxWidth(300);
        tblSaldo.getColumnModel().getColumn(8).setMinWidth(250);
        tblSaldo.getColumnModel().getColumn(8).setMaxWidth(300);
        tblSaldo.getColumnModel().getColumn(9).setMinWidth(250);
        tblSaldo.getColumnModel().getColumn(9).setMaxWidth(300);
    }

    private boolean validateForm() {
        if (!txtNoLambung.getText().isEmpty()
                && !txtNoLambung.getText().equalsIgnoreCase("tidak boleh kosong...")
                && !txtSetoranKe.getText().isEmpty()
                && !txtSetoranKe.getText().equalsIgnoreCase("tidak boleh kosong...")
                && !txtAngsuran.getText().isEmpty()
                && !txtAngsuran.getText().equalsIgnoreCase("tidak boleh kosong...")
                && !txtTabungan.getText().isEmpty()
                && !txtTabungan.getText().equalsIgnoreCase("tidak boleh kosong...")
                && !txtKasbon.getText().isEmpty()
                && !txtKasbon.getText().equalsIgnoreCase("tidak boleh kosong...")
                && !txtBayarKas.getText().isEmpty()
                && !txtBayarKas.getText().equalsIgnoreCase("tidak boleh kosong...")
                && !txtOvertime.getText().isEmpty()
                && !txtOvertime.getText().equalsIgnoreCase("tidak boleh kosong...")
                && !txtCicilan.getText().isEmpty()
                && !txtCicilan.getText().equalsIgnoreCase("tidak boleh kosong...")
                && !txtTotalSetoran.getText().isEmpty()
                && !txtTotalSetoran.getText().equalsIgnoreCase("tidak boleh kosong...")) {
            return true;
        }
        return false;
    }

    public void valueChanged(ListSelectionEvent e) {
        if (tblSaldo.getSelectedRow() >= 0) {
            clbPutih = listClosingBulananPutih.get(tblSaldo.getSelectedRow());
            LoadDatabaseToForm();
            enableForm(false);
            enableSearchEngine(true);
            txtNoLambung.setEnabled(false);
            toolbarButton.ubahMode();
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

        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSaldo = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtNoLambung = new javax.swing.JTextField();
        txtSetoranKe = new javax.swing.JTextField();
        txtAngsuran = new javax.swing.JTextField();
        txtTabungan = new javax.swing.JTextField();
        txtKasbon = new javax.swing.JTextField();
        txtBayarKas = new javax.swing.JTextField();
        txtOvertime = new javax.swing.JTextField();
        txtCicilan = new javax.swing.JTextField();
        txtTotalSetoran = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCari = new javax.swing.JTextField();
        cmbKriteria = new javax.swing.JComboBox();
        toolbarButton = new mintory.config.buttonUbahSaldo();

        setIconifiable(true);
        setMaximizable(true);
        setTitle("Pengaturan Saldo Awal");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 14));
        jLabel3.setText("Pengaturan Saldo Awal");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(957, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jSplitPane2.setDividerLocation(300);

        tblSaldo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "  Jenis Armada", "  No Lambung", "  Setoran Ke", "  Total Angsuran", "  Total Tabungan", "  Total Kasbon", "  Total Bayar Kas", "  Total Overtime", "  Total Cicilan", "  Total Setoran"
            }
        ));
        tblSaldo.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblSaldo.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblSaldo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSaldoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSaldo);
        tblSaldo.getColumnModel().getColumn(0).setMinWidth(100);
        tblSaldo.getColumnModel().getColumn(0).setMaxWidth(100);
        tblSaldo.getColumnModel().getColumn(1).setMinWidth(100);
        tblSaldo.getColumnModel().getColumn(1).setMaxWidth(120);
        tblSaldo.getColumnModel().getColumn(2).setMinWidth(100);
        tblSaldo.getColumnModel().getColumn(2).setMaxWidth(120);
        tblSaldo.getColumnModel().getColumn(3).setMinWidth(250);
        tblSaldo.getColumnModel().getColumn(3).setMaxWidth(300);
        tblSaldo.getColumnModel().getColumn(4).setMinWidth(250);
        tblSaldo.getColumnModel().getColumn(4).setMaxWidth(300);
        tblSaldo.getColumnModel().getColumn(5).setMinWidth(250);
        tblSaldo.getColumnModel().getColumn(5).setMaxWidth(300);
        tblSaldo.getColumnModel().getColumn(6).setMinWidth(250);
        tblSaldo.getColumnModel().getColumn(6).setMaxWidth(300);
        tblSaldo.getColumnModel().getColumn(7).setMinWidth(250);
        tblSaldo.getColumnModel().getColumn(7).setMaxWidth(300);
        tblSaldo.getColumnModel().getColumn(8).setMinWidth(250);
        tblSaldo.getColumnModel().getColumn(8).setMaxWidth(300);
        tblSaldo.getColumnModel().getColumn(9).setMinWidth(250);
        tblSaldo.getColumnModel().getColumn(9).setMaxWidth(300);

        jSplitPane2.setRightComponent(jScrollPane1);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12));
        jLabel4.setText("  Pengaturan Saldo Awal");
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addContainerGap(339, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
        );

        jLabel5.setText("No Lambung");

        jLabel7.setText("Total Angsuran");

        jLabel8.setText("Setoran Ke");

        jLabel9.setText("Total Tabungan");

        jLabel10.setText("Total Kasbon");

        jLabel11.setText("Total Bayar Kas");

        jLabel12.setText("Total Overtime");

        jLabel13.setText("Total Cicilan");

        jLabel14.setText("Total Setoran");

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel15.setFont(new java.awt.Font("DejaVu Sans", 1, 12));
        jLabel15.setText("  Cari Saldo Awal");
        jLabel15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel15)
                .addContainerGap(382, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
        );

        jLabel1.setText("Cari Data Saldo");

        jLabel2.setText("Kriteria");

        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariKeyReleased(evt);
            }
        });

        cmbKriteria.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No Lambung", "Setoran Ke" }));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addGap(16, 16, 16)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNoLambung, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSetoranKe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAngsuran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTabungan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKasbon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBayarKas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtOvertime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCicilan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTotalSetoran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbKriteria, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtAngsuran, txtBayarKas, txtCicilan, txtKasbon, txtNoLambung, txtOvertime, txtSetoranKe, txtTabungan, txtTotalSetoran});

        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cmbKriteria, txtCari});

        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jPanel5, jPanel6});

        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbKriteria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtNoLambung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSetoranKe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtAngsuran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTabungan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKasbon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtBayarKas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtOvertime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtCicilan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtTotalSetoran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(130, Short.MAX_VALUE))
        );

        jSplitPane2.setLeftComponent(jPanel4);

        jSplitPane1.setBottomComponent(jSplitPane2);
        jSplitPane1.setLeftComponent(toolbarButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1127, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblSaldoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSaldoMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount() == 2) {
            enableForm(true);
            tblSaldo.setEnabled(false);
        }
    }//GEN-LAST:event_tblSaldoMouseClicked

    private void txtCariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyReleased
        // TODO add your handling code here:
        String pilihan;
        int i = 0;
        boolean found = false;
        for (; i < listClosingBulananPutih.size(); i++) {
            if(txtCari.getText().length() == 0) {
                tblSaldo.getSelectionModel().clearSelection();
                return;
            }
            if (cmbKriteria.getModel().getSelectedItem().equals("No Lambung")) {
                if (listClosingBulananPutih.get(i).getRefNoLambung().toString().startsWith(txtCari.getText())) {
                    found = true;
                    break;
                } else {
                    found = false;

                }
            } else if (cmbKriteria.getModel().getSelectedItem().equals("Setoran Ke")) {
                if (listClosingBulananPutih.get(i).getRefSetoranKe().toString().startsWith(txtCari.getText())) {
                    found = true;
                    break;
                } else {
                    found = false;
                }
            } else {
                return;
            }
        }

        if (found) {
            tblSaldo.getSelectionModel().setSelectionInterval(i, i);
            tblSaldo.scrollRectToVisible(new Rectangle(tblSaldo.getCellRect(i, 0, true)));
        } else {
            int optionType = JOptionPane.YES_NO_OPTION;
            int answer = JOptionPane.showConfirmDialog(this, "Data Tidak Ditemukan\nUlangi Pencarian ? ? ?", "Not Found", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (answer == JOptionPane.YES_OPTION) {
                enableForm(false);
                txtCari.requestFocusInWindow();
                tblSaldo.getSelectionModel().clearSelection();
            }
            if (answer == JOptionPane.NO_OPTION) {
                enableForm(false);
                toolbarButton.defaultMode();
                tblSaldo.getSelectionModel().clearSelection();
                housekeeping();
            }
            tblSaldo.getSelectionModel().clearSelection();
        }
    }//GEN-LAST:event_txtCariKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbKriteria;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTable tblSaldo;
    private mintory.config.buttonUbahSaldo toolbarButton;
    private javax.swing.JTextField txtAngsuran;
    private javax.swing.JTextField txtBayarKas;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtCicilan;
    private javax.swing.JTextField txtKasbon;
    private javax.swing.JTextField txtNoLambung;
    private javax.swing.JTextField txtOvertime;
    private javax.swing.JTextField txtSetoranKe;
    private javax.swing.JTextField txtTabungan;
    private javax.swing.JTextField txtTotalSetoran;
    // End of variables declaration//GEN-END:variables

}

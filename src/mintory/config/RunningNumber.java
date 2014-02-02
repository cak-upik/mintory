/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RunningNumber.java
 *
 * Created on 21 Jul 12, 1:13:42
 */
package mintory.config;

import java.awt.event.ActionEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import mintory.Main;
import mintory.TextComponentUtils;
import mintory.model.codeGenerator;
import mintory.model.setoran;
import mintory.model.setoranPutih;
import mintory.model.sistem;
import mintory.tablemodel.RunningNumberTableModel;

/**
 *
 * @author Nurul Chusna
 */
public class RunningNumber extends javax.swing.JInternalFrame implements ListSelectionListener {

    private static RunningNumber runningNumber;
    private codeGenerator genCode;
    private sistem sys;
    private List<setoran> listSetoran;
    private List<setoranPutih> listSetoranPutih;
    private List<codeGenerator> listCode = new ArrayList<codeGenerator>();
    private static final Integer OPTION = JOptionPane.OK_CANCEL_OPTION;
    private Integer idNumber = 0;
    private Integer a;
    private boolean isDuplicateData = false;

    public static void inisialisasi() {
        runningNumber = new RunningNumber();
    }

    public static void destroy() {
        runningNumber.dispose();
        runningNumber = null;
        System.gc();
    }

    public static RunningNumber getRuningNumber() {
        return runningNumber;
    }

    /** Creates new form RunningNumber */
    public RunningNumber() throws IndexOutOfBoundsException {
        initComponents();
        initButtonListener();
        enableForm(false);
        enableSearchEngine(true);
        toolbarButton.defaultMode();
        txtID.setEnabled(false);
        tblData.setEnabled(true);
        tblData.setAutoCreateColumnsFromModel(false);
        tblData.getSelectionModel().addListSelectionListener(this);
        jdcTanggalSistem.setEnabled(false);
        LoadDatabaseToTable();
        TextComponentUtils.setAutoUpperCaseText(txtJenisTransaksi);
        TextComponentUtils.setAutoUpperCaseText(txtJenisArmada);
    }

    private void initButtonListener() {
        toolbarButton.getBtnTambah().addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aksiTambah();
            }
        });
        toolbarButton.getBtnUbah().addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aksiUbah();
            }
        });
        toolbarButton.getBtnHapus().addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aksiHapus();
            }
        });
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
    private void aksiTambah() {
        enableForm(true);
        enableSearchEngine(false);
        listCode = Main.getSistemService().codeRecord();
        sys = Main.getSistemService().sistemRecord();
        txtID.setEnabled(false);
        txtJenisTransaksi.requestFocus();
        jdcTanggalSistem.setDate(sys.getTglKerja());
        if (!listCode.isEmpty()) {
            for (codeGenerator cg : listCode) {
                idNumber = cg.getId();
                idNumber++;
            }
            txtID.setText(idNumber.toString());
        } else if (listCode.isEmpty()) {
            txtID.setText("1");
        }
        jdcTanggalSistem.setDate(sys.getTglKerja());
        tblData.setEnabled(false);
        toolbarButton.tambahMode();
    }

    private void aksiUbah() {
        enableForm(true);
        enableSearchEngine(false);
        toolbarButton.tambahMode();
        txtJenisTransaksi.requestFocus();
        txtID.setEnabled(false);
        tblData.setEnabled(false);
    }

    private void aksiSimpan() {
        cekDuplicateData();
        if (validateForm() && isDuplicateData == false) {
            LoadFormToDatabase();
            Main.getSistemService().save(genCode);
            LoadDatabaseToTable();
            JOptionPane.showMessageDialog(this, "Data Nomor Kode Berhasil Di Simpan", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            enableForm(false);
            enableSearchEngine(true);
            housekeeping();
            tblData.setEnabled(true);
            toolbarButton.defaultMode();
        } else {
            JOptionPane.showMessageDialog(this, "Data Nomor Kode Gagal Disimpan\nPeriksa Lagi Pengisian Form Apakah Sudah Benar", "Peringatan", JOptionPane.ERROR_MESSAGE);
            kenaTilang();
            enableForm(true);
            enableSearchEngine(false);
            tblData.setEnabled(false);
            toolbarButton.tambahMode();
        }
    }

    private void cekDuplicateData() {
        if (txtJenisArmada.getText().equalsIgnoreCase("BIRU") || txtJenisArmada.getText().equalsIgnoreCase("NEW BIRU")) {
                if (Main.getTransaksiService().findByKodeSetoran(txtNomor.getText())!=null) {
                    isDuplicateData = true;
                    txtNomor.requestFocus();
                    enableForm(true);
                    enableSearchEngine(false);
                    toolbarButton.tambahMode();
                    JOptionPane.showMessageDialog(Main.getMainMenu(), "Nomor Yang Anda Masukkan Sudah Berjalan\nNomor Yang Dimasukkan Harus Lebih Dari Nomor Yang Berjalan", "Peringatan", JOptionPane.ERROR_MESSAGE);
                    tblData.setEnabled(false);
                    return;
                } else {
                    isDuplicateData = false;
                }
        } else {
            listSetoranPutih = Main.getTransaksiService().setoranPutihRecord();
            for (int i = 0; i <= listSetoranPutih.size() - 1; i++) {
                if (listSetoranPutih.get(i).getKode().substring(13).compareTo(txtNomor.getText()) > 0) {
                    JOptionPane.showMessageDialog(Main.getMainMenu(), "Nomor Yang Anda Masukkan Sudah Berjalan\nNomor Yang Dimasukkan Harus Lebih Dari Nomor Yang Berjalan", "Peringatan", JOptionPane.ERROR_MESSAGE);
                    isDuplicateData = true;
                    txtNomor.requestFocus();
                    enableForm(true);
                    enableSearchEngine(false);
                    toolbarButton.tambahMode();
                    tblData.setEnabled(false);
                } else {
                    isDuplicateData = false;
                }
            }
        }
    }

    private void aksiHapus() {
        String ID = listCode.get(tblData.getSelectedRow()).getId().toString();
        String trans = listCode.get(tblData.getSelectedRow()).getTagTransaction();
        String armada = listCode.get(tblData.getSelectedRow()).getTagArmada();
        String nomor = listCode.get(tblData.getSelectedRow()).getLastnum().toString();
        String tanggal = listCode.get(tblData.getSelectedRow()).getTanggal().toString();
        int konfirmasi = JOptionPane.showConfirmDialog(this, "Anda Yakin Akan Menghapus Data Ini ? ?\nRincian :\nID= " + ID + "\nJenisTransaksi=" + trans + "\nJenisArmada=" + armada + "\nNomor Saat Ini=" + nomor + "\nTanggal Hari Ini= " + tanggal, "Konfirmasi", OPTION, JOptionPane.QUESTION_MESSAGE);
        if (konfirmasi == JOptionPane.OK_OPTION) {
            Main.getSistemService().delete(genCode);
            LoadDatabaseToTable();
            JOptionPane.showMessageDialog(this, "Data Nomor Kode Berhasil Dihapus", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            housekeeping();
            enableForm(false);
            enableSearchEngine(true);
        }
        if (konfirmasi == JOptionPane.CANCEL_OPTION) {
            return;
        }
        toolbarButton.defaultMode();
        tblData.setEnabled(true);
    }

    private void aksiBatal() {
        enableForm(false);
        enableSearchEngine(true);
        housekeeping();
        tblData.setEnabled(true);
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
        txtJenisArmada.setEnabled(e);
        txtJenisTransaksi.setEnabled(e);
        txtNomor.setEnabled(e);
    }

    private void enableSearchEngine(boolean e) {
        txtCariData.setEnabled(e);
        cmbKriteria.setEnabled(e);
    }

    private void housekeeping() {
        txtID.setText("");
        txtJenisArmada.setText("");
        txtJenisTransaksi.setText("");
        txtNomor.setText("");
        jdcTanggalSistem.setDate(null);
    }

    private void kenaTilang() {
        if (txtID.getText().isEmpty() && txtJenisArmada.getText().isEmpty() && txtJenisTransaksi.getText().isEmpty() && txtNomor.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tidak Boleh Ada Field Yang Kosong\nPeriksa Lagi!!!", "Peringatan", JOptionPane.ERROR_MESSAGE);
            txtID.setText("TIDAK BOLEH KOSONG...");
            txtID.requestFocus();
            txtJenisArmada.setText("TIDAK BOLEH KOSONG...");
            txtJenisTransaksi.setText("TIDAK BOLEH KOSONG...");
        } else if (txtID.getText().isEmpty() || txtID.getText().equalsIgnoreCase("tidak boleh kosong...")) {
            txtID.setText("TIDAK BOLEH KOSONG");
            txtID.requestFocus();
        } else if (txtJenisArmada.getText().isEmpty() || txtJenisArmada.getText().equalsIgnoreCase("tidak boleh kosong...")) {
            txtJenisArmada.setText("TIDAK BOLEH KOSONG");
            txtJenisArmada.requestFocus();
        } else if (txtJenisTransaksi.getText().isEmpty() || txtJenisTransaksi.getText().equalsIgnoreCase("tidak boleh kosong...")) {
            txtJenisTransaksi.setText("TIDAK BOLEH KOSONG");
            txtJenisTransaksi.requestFocus();
        }
    }

    private void LoadFormToDatabase() {
        if (genCode == null) {
            genCode = new codeGenerator();
        }
        genCode.setId(new Integer(txtID.getText()));
        genCode.setTagTransaction(txtJenisTransaksi.getText());
        genCode.setTagArmada(txtJenisArmada.getText());
        genCode.setTanggal(jdcTanggalSistem.getDate());
        genCode.setLastnum(new Integer(txtNomor.getText()));
    }

    private void LoadDatabaseToForm() {
        txtID.setText(genCode.getId().toString());
        txtJenisTransaksi.setText(genCode.getTagTransaction());
        txtJenisArmada.setText(genCode.getTagArmada());
        txtNomor.setText(genCode.getLastnum().toString());
        jdcTanggalSistem.setDate(genCode.getTanggal());
    }

    private void LoadDatabaseToTable() {
        listCode = Main.getSistemService().codeRecord();
        tblData.setModel(new RunningNumberTableModel(listCode));
        initColumn();
    }

    private void initColumn() {
        tblData.getColumnModel().getColumn(0).setMinWidth(30);
        tblData.getColumnModel().getColumn(0).setMaxWidth(30);
        tblData.getColumnModel().getColumn(2).setMinWidth(100);
        tblData.getColumnModel().getColumn(2).setMaxWidth(100);
        tblData.getColumnModel().getColumn(3).setMinWidth(100);
        tblData.getColumnModel().getColumn(3).setMaxWidth(100);
        tblData.getColumnModel().getColumn(4).setMinWidth(100);
        tblData.getColumnModel().getColumn(4).setMaxWidth(150);
    }

    private boolean validateForm() {
        if (!txtID.getText().isEmpty()
                && !txtID.getText().equalsIgnoreCase("tidak boleh kosong...")
                && !txtJenisArmada.getText().isEmpty()
                && !txtJenisArmada.getText().equalsIgnoreCase("tidak boleh kosong...")
                && !txtJenisTransaksi.getText().isEmpty()
                && !txtJenisTransaksi.getText().equalsIgnoreCase("tidak boleh kosong...")) {
            return true;
        }
        return false;
    }

    public void valueChanged(ListSelectionEvent e) {
        if (tblData.getSelectedRow() >= 0) {
            genCode = listCode.get(tblData.getSelectedRow());
            LoadDatabaseToForm();
            enableForm(false);
            enableSearchEngine(true);
            toolbarButton.tabelMode();
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

        jSplitPane2 = new javax.swing.JSplitPane();
        jSplitPane3 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCariData = new javax.swing.JTextField();
        cmbKriteria = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        txtJenisTransaksi = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtJenisArmada = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtNomor = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jdcTanggalSistem = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblData = new javax.swing.JTable();
        toolbarButton = new mintory.master.buttonMaster();

        setTitle("Form Pengaturan Default Nomor Kode");

        jSplitPane2.setDividerLocation(450);

        jSplitPane3.setDividerLocation(-5);
        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel1.setText("Cari Data Nomor");

        jLabel2.setText("Kriteria");

        txtCariData.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariDataKeyReleased(evt);
            }
        });

        cmbKriteria.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ID", "Jenis Transaksi", "Jenis Armada" }));

        jPanel5.setBackground(new java.awt.Color(0, 72, 255));

        jLabel11.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13));
        jLabel11.setForeground(new java.awt.Color(254, 254, 254));
        jLabel11.setText("Pencarian Data Nomor Kode");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addContainerGap(245, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCariData, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                    .addComponent(cmbKriteria, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCariData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbKriteria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(4, 4, 4))
        );

        jSplitPane3.setLeftComponent(jPanel1);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jPanel4.setBackground(new java.awt.Color(0, 73, 255));

        jLabel12.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13));
        jLabel12.setForeground(new java.awt.Color(254, 254, 254));
        jLabel12.setText("Input Data Nomor Kode");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addContainerGap(276, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        jLabel3.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13));
        jLabel3.setText("ID");

        txtID.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtIDMouseClicked(evt);
            }
        });

        txtJenisTransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtJenisTransaksiMouseClicked(evt);
            }
        });
        txtJenisTransaksi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtJenisTransaksiKeyReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13));
        jLabel4.setText("Jenis Transaksi");

        txtJenisArmada.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtJenisArmadaMouseClicked(evt);
            }
        });
        txtJenisArmada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtJenisArmadaKeyReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13));
        jLabel5.setText("Jenis Armada");

        txtNomor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNomorKeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13));
        jLabel6.setText("Nomor Saat Ini");

        jLabel10.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13));
        jLabel10.setText("Tgl Update");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtJenisTransaksi, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                        .addComponent(txtNomor)
                        .addComponent(txtJenisArmada)
                        .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jdcTanggalSistem, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtJenisTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtJenisArmada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtNomor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jdcTanggalSistem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(153, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jdcTanggalSistem, txtNomor});

        jSplitPane3.setRightComponent(jPanel2);

        jSplitPane2.setLeftComponent(jSplitPane3);

        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Jenis Transaksi", "Jenis Armada", "No Saat Ini", "Tgl Update"
            }
        ));
        jScrollPane1.setViewportView(tblData);
        tblData.getColumnModel().getColumn(0).setMinWidth(30);
        tblData.getColumnModel().getColumn(0).setMaxWidth(30);
        tblData.getColumnModel().getColumn(2).setMinWidth(100);
        tblData.getColumnModel().getColumn(2).setMaxWidth(100);
        tblData.getColumnModel().getColumn(3).setMinWidth(100);
        tblData.getColumnModel().getColumn(3).setMaxWidth(100);
        tblData.getColumnModel().getColumn(4).setMinWidth(100);
        tblData.getColumnModel().getColumn(4).setMaxWidth(150);

        jSplitPane2.setRightComponent(jScrollPane1);

        toolbarButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(toolbarButton, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(toolbarButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCariDataKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariDataKeyReleased
//        String pilihan;
//        int i = 0;
//        boolean found = false;
//        for (; i < listKendaraan.size(); i++) {
//            if(txtCariKendaraan.getText().length() == 0) {
//                tblKendaraan.getSelectionModel().clearSelection();
//                return;
//            }
//            if (cmbKriteria.getModel().getSelectedItem().equals("No.Lambung")) {
//                if (listKendaraan.get(i).getNoLambung().toString().startsWith(txtCariKendaraan.getText())) {
//                    found = true;
//                    break;
        //                } if (txtCariBarang.getText().startsWith(evt. {
        //                    found = false;
        //                    JOptionPane.showMessageDialog(this, "Parameter Search Characters Salah, Cek Lagi Data Anda ! !\nSeharusnya Yang Di Isi Adalah Kode Bukan Nama!!", "Terjadi Kesalahan", JOptionPane.INFORMATION_MESSAGE);
        //                    break;
//                } else {
//                    found = false;
//
//                }
        //if (evt.getKeyChar() == KeyEvent.VK_BACK_SPACE && txtCariKendaraan.getText().length() == 0) {
        //                JOptionPane.showMessageDialog(this, "Pastikan Semua Data Yang Di Isikan Benar\nPeriksa Kembali Data Anda", "Error", JOptionPane.ERROR_MESSAGE);
        //  tblKendaraan.getSelectionModel().clearSelection();
        //  txtCariKendaraan.requestFocusInWindow();
        //}
//            } else if (cmbKriteria.getModel().getSelectedItem().equals("No. Polisi")) {
//                if (listKendaraan.get(i).getNoPolisi().startsWith(txtCariKendaraan.getText())) {
//                    found = true;
//                    break;
        //                } if (txtCariBarang.getText().matches(listBarang.get(i).getKode())) {
        //                    found = false;
        //                    JOptionPane.showMessageDialog(this, "Parameter Search Characters Salah, Cek Lagi Data Anda ! !\nSeharusnya Yang Di Isi Adalah Nama Bukan Kode!!", "Terjadi Kesalahan", JOptionPane.INFORMATION_MESSAGE);
        //                    break;
//                }else {
//                    found = false;
//                }
        //if (evt.getKeyChar() == KeyEvent.VK_BACK_SPACE && txtCariKendaraan.getText().length() == 0) {
        //                JOptionPane.showMessageDialog(this, "Pastikan Semua Data Yang Di Isikan Benar\nPeriksa Kembali Data Anda", "Error", JOptionPane.ERROR_MESSAGE);
        //  tblKendaraan.getSelectionModel().clearSelection();
        //  txtCarinKendaraan.requestFocusInWindow();
        //}
//            }else {
//                return;
//            }
//        }
//
//        if (found) {
//            tblKendaraan.getSelectionModel().setSelectionInterval(i, i);
//            tblKendaraan.scrollRectToVisible(new Rectangle(tblKendaraan.getCellRect(i, 0, true)));
//        } else {
//            int optionType = JOptionPane.YES_NO_OPTION;
//            int answer = JOptionPane.showConfirmDialog(this, "Data Tidak Ditemukan\nUlangi Pencarian ? ? ?", "Not Found", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//            if (answer == JOptionPane.YES_OPTION) {
//                enableForm(false);
//                txtCariKendaraan.requestFocusInWindow();
//                tblKendaraan.getSelectionModel().clearSelection();
//            }
//            if (answer == JOptionPane.NO_OPTION) {
//                enableForm(false);
//                toolbarButton.defaultMode();
//                tblKendaraan.getSelectionModel().clearSelection();
//                housekeeping();
//            }
//            tblKendaraan.getSelectionModel().clearSelection();
//            previousOption = tblKendaraan.getSelectedRow();
//        }
}//GEN-LAST:event_txtCariDataKeyReleased

    private void txtIDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtIDMouseClicked
        // TODO add your handling code here:
        if (txtID.getText().isEmpty() || txtID.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtID.setText("");
        } else {
            return;
        }
    }//GEN-LAST:event_txtIDMouseClicked

    private void txtJenisTransaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtJenisTransaksiMouseClicked
        // TODO add your handling code here:
        if (txtJenisTransaksi.getText().isEmpty() || txtJenisTransaksi.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtJenisTransaksi.setText("");
        } else {
            return;
        }
}//GEN-LAST:event_txtJenisTransaksiMouseClicked

    private void txtJenisTransaksiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJenisTransaksiKeyReleased
        // TODO add your handling code here:
}//GEN-LAST:event_txtJenisTransaksiKeyReleased

    private void txtJenisArmadaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtJenisArmadaMouseClicked
        // TODO add your handling code here:
        if (txtJenisArmada.getText().isEmpty() || txtJenisArmada.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtJenisArmada.setText("");
        } else {
            return;
        }
}//GEN-LAST:event_txtJenisArmadaMouseClicked

    private void txtJenisArmadaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJenisArmadaKeyReleased
        // TODO add your handling code here:
}//GEN-LAST:event_txtJenisArmadaKeyReleased

    private void txtNomorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomorKeyReleased
        // TODO add your handling code here:
//        if (evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && !txtNomor.getText().isEmpty()) {
//                cekDuplicateData();
//        } else {
//            isDuplicateData = false;
//        }
    }//GEN-LAST:event_txtNomorKeyReleased
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbKriteria;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private com.toedter.calendar.JDateChooser jdcTanggalSistem;
    private javax.swing.JTable tblData;
    private mintory.master.buttonMaster toolbarButton;
    private javax.swing.JTextField txtCariData;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtJenisArmada;
    private javax.swing.JTextField txtJenisTransaksi;
    private javax.swing.JTextField txtNomor;
    // End of variables declaration//GEN-END:variables
}

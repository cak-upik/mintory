/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MasterKendaraan.java
 *
 * Created on Mar 27, 2012, 9:07:14 AM
 */
package mintory.master;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.hibernate.JDBCException;
import org.springframework.transaction.TransactionException;
import mintory.Main;
import mintory.TextComponentUtils;
import mintory.model.Barang;
import mintory.tablemodel.BarangTableModel;

/**
 *
 * @author i1440ns
 */
public class MasterBarang extends javax.swing.JInternalFrame implements ListSelectionListener {

    private static MasterBarang mstBarang;
    private List<Barang> listBarang = new ArrayList<Barang>();
    private Barang barang;
    private static final Integer option = JOptionPane.OK_CANCEL_OPTION;

    public static void inisialisasi() {
        mstBarang = new MasterBarang();
    }

    public static void destroy() {
        mstBarang.dispose();
        mstBarang = null;
    }

    public static MasterBarang getMasterBarang() {
        return mstBarang;
    }

    /**
     * Creates new form MasterKendaraan
     */
    public MasterBarang() {
        initComponents();
        initButtonListener();
        initButtonHotkeyFunction();
        buttonMaster.defaultMode();
        enableForm(false);
        LoadDatabaseToTable();
        tblBarang.getSelectionModel().addListSelectionListener(this);
        tblBarang.setAutoCreateColumnsFromModel(false);
        tblBarang.getSelectionModel().clearSelection();
        TextComponentUtils.setNumericTextOnly(txtKodeBarang);
    }

    private void initButtonHotkeyFunction() {
        KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        kfm.addKeyEventDispatcher(new KeyEventDispatcher() {

            public boolean dispatchKeyEvent(KeyEvent ke) {
                /*  Button Tambah  */
                if (ke.getKeyCode() == KeyEvent.VK_F1 && ke.getID() == KeyEvent.KEY_PRESSED) {
                    buttonMaster.getBtnTambah().doClick();
                    System.out.println("F1 Pressed and GO aksiTambah");
                }
                /*  Button Ubah  */
                if (ke.getKeyCode() == KeyEvent.VK_F2 && ke.getID() == KeyEvent.KEY_PRESSED) {
                    buttonMaster.getBtnUbah().doClick();
                    System.out.println("F2 Pressed and GO aksiUbah");
                }
                /*  Button Simpan  */
                if (ke.getKeyCode() == KeyEvent.VK_F3 && ke.getID() == KeyEvent.KEY_PRESSED) {
                    buttonMaster.getBtnSimpan().doClick();
                    System.out.println("F3 Pressed and GO aksiSimpan");
                }
                /*  Button Hapus  */
                if (ke.getKeyCode() == KeyEvent.VK_F4 && ke.getID() == KeyEvent.KEY_PRESSED) {
                    buttonMaster.getBtnHapus().doClick();
                    System.out.println("F4 Pressed and GO aksiHapus");
                }
                /*  Button Batal  */
                if (ke.getKeyCode() == KeyEvent.VK_F5 && ke.getID() == KeyEvent.KEY_PRESSED) {
                    buttonMaster.getBtnBatal().doClick();
                    System.out.println("F5 Pressed and GO aksiBatal");
                }
                /*  Button Keluar  */
                if (ke.getKeyCode() == KeyEvent.VK_F6 && ke.getID() == KeyEvent.KEY_PRESSED) {
                    buttonMaster.getBtnKeluar().doClick();
                    System.out.println("F6 Pressed and GO aksiKeluar");
                }
                return false;
            }
        });

    }

    private void initButtonListener() {
        buttonMaster.getBtnTambah().addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                aksiTambah();
            }
        });

        buttonMaster.getBtnUbah().addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                aksiEdit();
            }
        });
        buttonMaster.getBtnHapus().addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                aksiHapus();
            }
        });
        buttonMaster.getBtnBatal().addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                aksiBatal();
            }
        });
        buttonMaster.getBtnSimpan().addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                aksiSimpan();
            }
        });
        buttonMaster.getBtnKeluar().addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                aksiKeluar();
            }
        });
    }

    private void aksiTambah() {
        enableForm(true);
        enableSearchEngine(false);
        txtKodeBarang.requestFocus();
        buttonMaster.tambahMode();
        tblBarang.setEnabled(false);
    }

    private void aksiEdit() {
        enableForm(true);
        enableSearchEngine(false);
        txtKodeBarang.requestFocus();
        buttonMaster.tambahMode();
        tblBarang.setEnabled(false);
    }

    private void aksiSimpan() {
        try {
            cekDuplicate();
        } catch (JDBCException jde) {
            int konfirmasi = JOptionPane.showConfirmDialog(this, "Data Dengan Kode Sama Sudah Ada\nGanti Kode Data ? ? ?", "Gagal Menyimpan", option, JOptionPane.QUESTION_MESSAGE);
            if (konfirmasi == JOptionPane.OK_OPTION) {
                enableForm(true);
                enableSearchEngine(false);
                txtKodeBarang.requestFocus();
                buttonMaster.tambahMode();
                tblBarang.setEnabled(false);
            } else if (konfirmasi == JOptionPane.CANCEL_OPTION) {
                enableForm(true);
                enableSearchEngine(false);
                housekeeping();
                buttonMaster.defaultMode();
                tblBarang.setEnabled(true);
            }
        } catch (TransactionException te) {
            String error = te.getMessage();
            TextArea txtError = new TextArea();
            txtError.setText(error);
            Object messageOutput[] = {"Koneksi Database Terputus\n", "Rincian Kesalahan :", txtError};
            JOptionPane msg = new JOptionPane();
            msg.setMessage(messageOutput);
            msg.setMessageType(JOptionPane.ERROR_MESSAGE);
            JDialog dialog = msg.createDialog(this, "Terjadi Kesalahan Koneksi Database");
            dialog.setVisible(true);
            housekeeping();
            enableForm(false);
            tblBarang.getSelectionModel().clearSelection();
            tblBarang.setEnabled(true);
            buttonMaster.defaultMode();
            barang = null;
        }
    }

    private void cekDuplicate() {
        if (validateForm()) {
            LoadFormToDatabase();
            Main.getMasterService().save(barang);
            LoadDatabaseToTable();
            housekeeping();
            enableForm(false);
            enableSearchEngine(true);
            tblBarang.setEnabled(true);
            JOptionPane.showMessageDialog(this, "Proses Penyimpanan Telah Berhasil", "Pemberitahuan", JOptionPane.INFORMATION_MESSAGE);
            tblBarang.getSelectionModel().clearSelection();
            buttonMaster.defaultMode();
            barang = null;
        } else {
            JOptionPane.showMessageDialog(this, "Terjadi Kesalahan\nGagal Menyimpan", "Pemberitahuan", JOptionPane.ERROR_MESSAGE);
            kenatilang();
            enableForm(true);
            enableSearchEngine(false);
            tblBarang.setEnabled(false);
            buttonMaster.tambahMode();
        }
    }

    private void aksiHapus() {
        String kodeBarang = listBarang.get(tblBarang.getSelectedRow()).getKodeBarang();
        String namaBarang = listBarang.get(tblBarang.getSelectedRow()).getNamaBarang();
        String satuan = listBarang.get(tblBarang.getSelectedRow()).getSatuan();
        String dateMasuk = listBarang.get(tblBarang.getSelectedRow()).getTglMasuk().toString();
        String keterangan = listBarang.get(tblBarang.getSelectedRow()).getKeterangan();
        int konfirmasi = JOptionPane.showConfirmDialog(this, "Anda Yakin Akan Menghapus Data Ini ? ?\nRincican :" + "\nKode Barang = " + kodeBarang + "\nNama Barang =  " + namaBarang + "\nSatuan = " + satuan + "\nTgl Masuk = " + dateMasuk + "\nKeterangan = " + keterangan, "Konfirmasi Hapus Data", option, JOptionPane.QUESTION_MESSAGE);
        if (konfirmasi == JOptionPane.OK_OPTION) {
            Main.getMasterService().delete(barang);
            LoadDatabaseToTable();
            housekeeping();
            barang = null;
        }
        if (konfirmasi == JOptionPane.CANCEL_OPTION) {
            housekeeping();
            barang = null;
        }
        enableForm(false);
        enableSearchEngine(true);
        housekeeping();
        tblBarang.getSelectionModel().clearSelection();
        tblBarang.setEnabled(true);
        buttonMaster.defaultMode();
    }

    private void aksiBatal() {
        housekeeping();
        enableForm(false);
        enableSearchEngine(true);
        tblBarang.getSelectionModel().clearSelection();
        tblBarang.setEnabled(true);
        barang = null;
        buttonMaster.defaultMode();
    }

    private void aksiKeluar() {
        enableForm(false);
        enableSearchEngine(false);
        housekeeping();
        tblBarang.getSelectionModel().clearSelection();
        tblBarang.setEnabled(false);
        buttonMaster.keluarMode();
        destroy();
    }

    private void kenatilang() {
        if (txtKodeBarang.getText().isEmpty() && txtNamaBarang.getText().isEmpty() && txtSatuan.getText().isEmpty() && dateBrgMasuk.getDate() == null) {
            JOptionPane.showMessageDialog(this, "!!! Tidak boleh ada field yang kosong \nPeriksa Lagi !!!", "Error", JOptionPane.ERROR_MESSAGE);
            txtKodeBarang.setText("TIDAK BOLEH KOSONG...");
            txtKodeBarang.setForeground(Color.red);
            txtNamaBarang.setText("TIDAK BOLEH KOSONG...");
            txtNamaBarang.setForeground(Color.red);
            txtSatuan.setText("TIDAK BOLEH KOSONG...");
            txtSatuan.setForeground(Color.red);
        } else if (txtKodeBarang.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi Kode Barang !!!", "Error", JOptionPane.ERROR_MESSAGE);
            txtKodeBarang.requestFocus();
            txtKodeBarang.setText("TIDAK BOLEH KOSONG...");
            txtKodeBarang.setForeground(Color.red);
        } else if (txtNamaBarang.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi Nama Barang !!!", "Error", JOptionPane.ERROR_MESSAGE);
            txtNamaBarang.requestFocus();
            txtNamaBarang.setText("TIDAK BOLEH KOSONG...");
            txtNamaBarang.setForeground(Color.red);
        } else if (txtSatuan.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi Satuan !!!", "Error", JOptionPane.ERROR_MESSAGE);
            txtSatuan.requestFocus();
            txtSatuan.setText("TIDAK BOLEH KOSONG...");
            txtSatuan.setForeground(Color.red);
        } else if(dateBrgMasuk.getDate() == null) {
           JOptionPane.showMessageDialog(this, "!!! Harap isi Tgl Masuk !!!", "Error", JOptionPane.ERROR_MESSAGE);
            dateBrgMasuk.requestFocus();
        }
    }

    private void enableForm(boolean e) {
        txtKodeBarang.setEnabled(e);
        txtSatuan.setEnabled(e);
        txtNamaBarang.setEnabled(e);
        txtKeterangan.setEnabled(e);
        dateBrgMasuk.setEnabled(e);
    }

    private void enableSearchEngine(boolean e) {
        txtCariBarang.setEnabled(e);
        cmbKriteria.setEnabled(e);
    }

    private void housekeeping() {
        txtKodeBarang.setText("");
        txtSatuan.setText("");
        txtNamaBarang.setText("");
//        txtNorang.setText("");
//        txtSTNK.setText("");
        txtKeterangan.setText("");
        dateBrgMasuk.setDate(null);
    }

    private boolean validateForm() {
        return !txtKodeBarang.getText().isEmpty() && !txtKodeBarang.getText().equals("TIDAK BOLEH KOSONG...")
                && !txtNamaBarang.getText().isEmpty() && !txtNamaBarang.getText().equals("TIDAK BOLEH KOSONG...")
                && !txtSatuan.getText().isEmpty() && !txtSatuan.getText().equals("TIDAK BOLEH KOSONG...")
                && dateBrgMasuk.getDate() != null;
    }

    private void LoadFormToDatabase() {
        if (barang == null) {
            barang = new Barang();
        }
        barang.setKodeBarang(txtKodeBarang.getText());
        barang.setNamaBarang(txtNamaBarang.getText());
        barang.setSatuan(txtSatuan.getText());
        barang.setTglMasuk(dateBrgMasuk.getDate());
        barang.setKeterangan(txtKeterangan.getText());
    }

    private void LoadDatabaseToForm() {
        txtKodeBarang.setText(barang.getKodeBarang());
        txtNamaBarang.setText(barang.getNamaBarang());
        txtSatuan.setText(barang.getSatuan());
        dateBrgMasuk.setDate(barang.getTglMasuk());
        txtKeterangan.setText(barang.getKeterangan());
    }

    private void LoadDatabaseToTable() {
        listBarang = Main.getMasterService().barangRecord();
        tblBarang.setModel(new BarangTableModel(listBarang));
        int i = 0;
        for (; i < listBarang.size(); i++) {
            tblBarang.getSelectionModel().setSelectionInterval(i, i);
            tblBarang.scrollRectToVisible(new Rectangle(tblBarang.getCellRect(i, 0, true)));
        }
        initColumnSize();
    }

    private void initColumnSize() {
        tblBarang.getColumnModel().getColumn(0).setMinWidth(100);
        tblBarang.getColumnModel().getColumn(0).setMaxWidth(150);
        tblBarang.getColumnModel().getColumn(1).setMinWidth(200);
        tblBarang.getColumnModel().getColumn(1).setMaxWidth(250);
        tblBarang.getColumnModel().getColumn(2).setMinWidth(50);
        tblBarang.getColumnModel().getColumn(2).setMaxWidth(80);
        tblBarang.getColumnModel().getColumn(3).setMinWidth(100);
        tblBarang.getColumnModel().getColumn(3).setMaxWidth(120);
        tblBarang.getColumnModel().getColumn(4).setMinWidth(150);
        tblBarang.getColumnModel().getColumn(4).setMaxWidth(150);
    }

    public void valueChanged(ListSelectionEvent lse) {
        if (lse.getValueIsAdjusting()) {
            return;
        }
        if (tblBarang.getSelectedRow() >= 0) {
            barang = listBarang.get(tblBarang.getSelectedRow());
            LoadDatabaseToForm();
            enableForm(false);
            enableSearchEngine(true);
            buttonMaster.tabelMode();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        jSplitPane3 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCariBarang = new javax.swing.JTextField();
        cmbKriteria = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtKodeBarang = new javax.swing.JTextField();
        txtNamaBarang = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtSatuan = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtKeterangan = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        dateBrgMasuk = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBarang = new javax.swing.JTable();
        buttonMaster = new mintory.master.buttonMaster();

        setIconifiable(true);
        setMaximizable(true);
        setTitle("Master Data Barang");

        jSplitPane1.setDividerLocation(-5);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jSplitPane2.setDividerLocation(375);
        jSplitPane2.setDividerSize(5);

        jSplitPane3.setDividerLocation(-5);
        jSplitPane3.setDividerSize(10);
        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel1.setText("Cari Barang");

        jLabel2.setText("Kriteria");

        txtCariBarang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariBarangKeyReleased(evt);
            }
        });

        cmbKriteria.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kode", "Nama" }));

        jPanel5.setBackground(new java.awt.Color(0, 72, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED)));

        jLabel11.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(254, 254, 254));
        jLabel11.setText("Pencarian Data Barang");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(86, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addGap(74, 74, 74))
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cmbKriteria, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 140, Short.MAX_VALUE))
                            .addComponent(txtCariBarang)))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCariBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbKriteria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(4, 4, 4))
        );

        jSplitPane3.setLeftComponent(jPanel1);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jPanel4.setBackground(new java.awt.Color(0, 73, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createCompoundBorder(null, javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED))));

        jLabel12.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 16)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(254, 254, 254));
        jLabel12.setText("Input Data Barang");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(110, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addGap(90, 90, 90))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        jLabel3.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13)); // NOI18N
        jLabel3.setText("Kode Barang");

        txtKodeBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtKodeBarangMouseClicked(evt);
            }
        });

        txtNamaBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNamaBarangMouseClicked(evt);
            }
        });
        txtNamaBarang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNamaBarangKeyReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13)); // NOI18N
        jLabel4.setText("Nama Barang");

        txtSatuan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSatuanMouseClicked(evt);
            }
        });
        txtSatuan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSatuanKeyReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13)); // NOI18N
        jLabel5.setText("Satuan");

        txtKeterangan.setColumns(20);
        txtKeterangan.setRows(5);
        txtKeterangan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKeteranganKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(txtKeterangan);

        jLabel8.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13)); // NOI18N
        jLabel8.setText("Keterangan");

        dateBrgMasuk.setBorder(null);

        jLabel10.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13)); // NOI18N
        jLabel10.setText("Tgl Masuk");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel10)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(dateBrgMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtKodeBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                                .addComponent(txtSatuan, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtNamaBarang, javax.swing.GroupLayout.Alignment.LEADING)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 24, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtKodeBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNamaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dateBrgMasuk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSplitPane3.setRightComponent(jPanel2);

        jSplitPane2.setLeftComponent(jSplitPane3);

        tblBarang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "  Kode Barang", "  Nama Barang", "  Satuan", "  Tgl Masuk", "  Keterangan"
            }
        ));
        tblBarang.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblBarang.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(tblBarang);
        if (tblBarang.getColumnModel().getColumnCount() > 0) {
            tblBarang.getColumnModel().getColumn(0).setMinWidth(100);
            tblBarang.getColumnModel().getColumn(0).setMaxWidth(150);
            tblBarang.getColumnModel().getColumn(1).setMinWidth(200);
            tblBarang.getColumnModel().getColumn(1).setMaxWidth(250);
            tblBarang.getColumnModel().getColumn(2).setMinWidth(50);
            tblBarang.getColumnModel().getColumn(2).setMaxWidth(80);
            tblBarang.getColumnModel().getColumn(3).setMinWidth(100);
            tblBarang.getColumnModel().getColumn(3).setMaxWidth(120);
            tblBarang.getColumnModel().getColumn(4).setMinWidth(150);
            tblBarang.getColumnModel().getColumn(4).setMaxWidth(150);
        }

        jSplitPane2.setRightComponent(jScrollPane2);

        jSplitPane1.setBottomComponent(jSplitPane2);

        buttonMaster.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jSplitPane1.setLeftComponent(buttonMaster);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    int previousOption = -1;
    private void txtCariBarangKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariBarangKeyReleased
        String pilihan;
        int i = 0;
        boolean found = false;
        for (; i < listBarang.size(); i++) {
            if (txtCariBarang.getText().length() == 0) {
                tblBarang.getSelectionModel().clearSelection();
                return;
            }
            if (cmbKriteria.getModel().getSelectedItem().equals("Kode")) {
                if (listBarang.get(i).getKodeBarang().startsWith(txtCariBarang.getText())) {
                    found = true;
                    break;
                } else {
                    found = false;
                }
            } else if (cmbKriteria.getModel().getSelectedItem().equals("Nama")) {
                if (listBarang.get(i).getNamaBarang().startsWith(txtCariBarang.getText())) {
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
            tblBarang.getSelectionModel().setSelectionInterval(i, i);
            tblBarang.scrollRectToVisible(new Rectangle(tblBarang.getCellRect(i, 0, true)));
        } else {
            int optionType = JOptionPane.YES_NO_OPTION;
            int answer = JOptionPane.showConfirmDialog(this, "Data Tidak Ditemukan\nUlangi Pencarian ? ? ?", "Not Found", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (answer == JOptionPane.YES_OPTION) {
                enableForm(false);
                txtCariBarang.requestFocusInWindow();
                tblBarang.getSelectionModel().clearSelection();
            }
            if (answer == JOptionPane.NO_OPTION) {
                enableForm(false);
                buttonMaster.defaultMode();
                tblBarang.getSelectionModel().clearSelection();
                housekeeping();
            }
            tblBarang.getSelectionModel().clearSelection();
            previousOption = tblBarang.getSelectedRow();
        }
}//GEN-LAST:event_txtCariBarangKeyReleased

    private void txtKodeBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtKodeBarangMouseClicked
        // TODO add your handling code here:
        if (txtKodeBarang.getText().isEmpty() || txtKodeBarang.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtKodeBarang.setText("");
            txtKodeBarang.setForeground(Color.BLACK);
        } else {
            return;
        }

    }//GEN-LAST:event_txtKodeBarangMouseClicked

    private void txtNamaBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNamaBarangMouseClicked
        // TODO add your handling code here:
        if (txtNamaBarang.getText().isEmpty() || txtNamaBarang.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtNamaBarang.setText("");
            txtNamaBarang.setForeground(Color.BLACK);
        } else {
            return;
        }
    }//GEN-LAST:event_txtNamaBarangMouseClicked

    private void txtSatuanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSatuanMouseClicked
        // TODO add your handling code here:
        if (txtSatuan.getText().isEmpty() || txtSatuan.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtSatuan.setText("");
            txtSatuan.setForeground(Color.BLACK);
        } else {
            return;
        }
    }//GEN-LAST:event_txtSatuanMouseClicked

    private void txtNamaBarangKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNamaBarangKeyReleased
        // TODO add your handling code here:
        TextComponentUtils.setAutoUpperCaseText(txtNamaBarang);
    }//GEN-LAST:event_txtNamaBarangKeyReleased

    private void txtSatuanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSatuanKeyReleased
        // TODO add your handling code here:
        TextComponentUtils.setAutoUpperCaseText(txtSatuan);
    }//GEN-LAST:event_txtSatuanKeyReleased

    private void txtKeteranganKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKeteranganKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '\n') {
            aksiSimpan();
        }
    }//GEN-LAST:event_txtKeteranganKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private mintory.master.buttonMaster buttonMaster;
    private javax.swing.JComboBox cmbKriteria;
    private com.toedter.calendar.JDateChooser dateBrgMasuk;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JTable tblBarang;
    private javax.swing.JTextField txtCariBarang;
    private javax.swing.JTextArea txtKeterangan;
    private javax.swing.JTextField txtKodeBarang;
    private javax.swing.JTextField txtNamaBarang;
    private javax.swing.JTextField txtSatuan;
    // End of variables declaration//GEN-END:variables

}

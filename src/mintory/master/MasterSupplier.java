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
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.hibernate.JDBCException;
import org.springframework.transaction.TransactionException;
import mintory.Main;
import mintory.TextComponentUtils;
import mintory.model.Supplier;
import mintory.tablemodel.SupplierTableModel;

/**
 *
 * @author i1440ns
 */
public class MasterSupplier extends javax.swing.JInternalFrame implements ListSelectionListener {

    private static MasterSupplier mstSupplier;
    private List<Supplier> listSupplier = new ArrayList<Supplier>();
    private Supplier supplier;
    private static final Integer option = JOptionPane.OK_CANCEL_OPTION;

    public static void inisialisasi() {
        mstSupplier = new MasterSupplier();
    }

    public static void destroy() {
        mstSupplier.dispose();
        mstSupplier = null;
    }

    public static MasterSupplier getMasterSupplier() {
        return mstSupplier;
    }

    /**
     * Creates new form MasterKendaraan
     */
    public MasterSupplier() {
        initComponents();
        initButtonListener();
        initButtonHotkeyFunction();
        buttonMaster.defaultMode();
        enableForm(false);
        LoadDatabaseToTable();
        tblSupplier.getSelectionModel().addListSelectionListener(this);
        tblSupplier.setAutoCreateColumnsFromModel(false);
        tblSupplier.getSelectionModel().clearSelection();
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
        txtKodeSupplier.requestFocus();
        buttonMaster.tambahMode();
        tblSupplier.setEnabled(false);
    }

    private void aksiEdit() {
        enableForm(true);
        enableSearchEngine(false);
        txtKodeSupplier.requestFocus();
        buttonMaster.tambahMode();
        tblSupplier.setEnabled(false);
    }

    private void aksiSimpan() {
        try {
            cekDuplicate();
        } catch (JDBCException jde) {
            int konfirmasi = JOptionPane.showConfirmDialog(this, "Data Dengan Kode Sama Sudah Ada\nGanti Kode Data ? ? ?", "Gagal Menyimpan", option, JOptionPane.QUESTION_MESSAGE);
            if (konfirmasi == JOptionPane.OK_OPTION) {
                enableForm(true);
                enableSearchEngine(false);
                txtKodeSupplier.requestFocus();
                buttonMaster.tambahMode();
                tblSupplier.setEnabled(false);
            } else if (konfirmasi == JOptionPane.CANCEL_OPTION) {
                enableForm(true);
                enableSearchEngine(false);
                housekeeping();
                buttonMaster.defaultMode();
                tblSupplier.setEnabled(true);
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
            tblSupplier.getSelectionModel().clearSelection();
            tblSupplier.setEnabled(true);
            buttonMaster.defaultMode();
            supplier = null;
        }
    }

    private void cekDuplicate() {
        if (validateForm()) {
            LoadFormToDatabase();
            Main.getMasterService().save(supplier);
            LoadDatabaseToTable();
            housekeeping();
            enableForm(false);
            enableSearchEngine(true);
            tblSupplier.setEnabled(true);
            JOptionPane.showMessageDialog(this, "Proses Penyimpanan Telah Berhasil", "Pemberitahuan", JOptionPane.INFORMATION_MESSAGE);
            tblSupplier.getSelectionModel().clearSelection();
            buttonMaster.defaultMode();
            supplier = null;
        } else {
            JOptionPane.showMessageDialog(this, "Terjadi Kesalahan\nGagal Menyimpan", "Pemberitahuan", JOptionPane.ERROR_MESSAGE);
            kenatilang();
            enableForm(true);
            enableSearchEngine(false);
            tblSupplier.setEnabled(false);
            buttonMaster.tambahMode();
        }
    }

    private void aksiHapus() {
        String kodeSupplier = listSupplier.get(tblSupplier.getSelectedRow()).getKodeSupplier();
        String namaSupplier = listSupplier.get(tblSupplier.getSelectedRow()).getNamaSupplier();
        String alamat = listSupplier.get(tblSupplier.getSelectedRow()).getAlamat();
        String telp = listSupplier.get(tblSupplier.getSelectedRow()).getTelp();
        int konfirmasi = JOptionPane.showConfirmDialog(this, "Anda Yakin Akan Menghapus Data Ini ? ?\nRincican :" + "\nKode Supplier = " + kodeSupplier + "\nNama Supplier =  " + namaSupplier + "\nAlamat = " + alamat + "\nNo. Telepon = " +telp, "Konfirmasi Hapus Data", option, JOptionPane.QUESTION_MESSAGE);
        if (konfirmasi == JOptionPane.OK_OPTION) {
            Main.getMasterService().delete(supplier);
            LoadDatabaseToTable();
            housekeeping();
            JOptionPane.showMessageDialog(this, "Data Berhasil Dihapus", "Pesan Sistem", JOptionPane.INFORMATION_MESSAGE);
            supplier = null;
        }
        if (konfirmasi == JOptionPane.CANCEL_OPTION) {
            housekeeping();
            supplier = null;
        }
        enableForm(false);
        enableSearchEngine(true);
        housekeeping();
        tblSupplier.getSelectionModel().clearSelection();
        tblSupplier.setEnabled(true);
        buttonMaster.defaultMode();
    }

    private void aksiBatal() {
        housekeeping();
        enableForm(false);
        enableSearchEngine(true);
        tblSupplier.getSelectionModel().clearSelection();
        tblSupplier.setEnabled(true);
        supplier = null;
        buttonMaster.defaultMode();
    }

    private void aksiKeluar() {
        enableForm(false);
        enableSearchEngine(false);
        housekeeping();
        tblSupplier.getSelectionModel().clearSelection();
        tblSupplier.setEnabled(false);
        buttonMaster.keluarMode();
        destroy();
    }

    private void kenatilang() {
        if (txtKodeSupplier.getText().isEmpty() && txtNamaSupplier.getText().isEmpty() && txtAlamat.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Tidak boleh ada field yang kosong \nPeriksa Lagi !!!", "Error", JOptionPane.ERROR_MESSAGE);
            txtKodeSupplier.setText("TIDAK BOLEH KOSONG...");
            txtKodeSupplier.setForeground(Color.red);
            txtNamaSupplier.setText("TIDAK BOLEH KOSONG...");
            txtNamaSupplier.setForeground(Color.red);
            txtAlamat.setText("TIDAK BOLEH KOSONG...");
            txtAlamat.setForeground(Color.red);
        } else if (txtKodeSupplier.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi Kode Supplier !!!", "Error", JOptionPane.ERROR_MESSAGE);
            txtKodeSupplier.requestFocus();
            txtKodeSupplier.setText("TIDAK BOLEH KOSONG...");
            txtKodeSupplier.setForeground(Color.red);
        } else if (txtNamaSupplier.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi Nama Supplier !!!", "Error", JOptionPane.ERROR_MESSAGE);
            txtNamaSupplier.requestFocus();
            txtNamaSupplier.setText("TIDAK BOLEH KOSONG...");
            txtNamaSupplier.setForeground(Color.red);
        } else if (txtAlamat.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi Alamat !!!", "Error", JOptionPane.ERROR_MESSAGE);
            txtAlamat.requestFocus();
            txtAlamat.setText("TIDAK BOLEH KOSONG...");
            txtAlamat.setForeground(Color.red);
        }
    }

    private void enableForm(boolean e) {
        txtKodeSupplier.setEnabled(e);
        txtAlamat.setEnabled(e);
        txtNamaSupplier.setEnabled(e);
        txtNoTelp.setEnabled(e);
    }

    private void enableSearchEngine(boolean e) {
        txtCariSupplier.setEnabled(e);
        cmbKriteria.setEnabled(e);
    }

    private void housekeeping() {
        txtKodeSupplier.setText("");
        txtAlamat.setText("");
        txtNamaSupplier.setText("");
        txtNoTelp.setText("");
    }

    private boolean validateForm() {
        return !txtKodeSupplier.getText().isEmpty() && !txtKodeSupplier.getText().equals("TIDAK BOLEH KOSONG...")
                && !txtNamaSupplier.getText().isEmpty() && !txtNamaSupplier.getText().equals("TIDAK BOLEH KOSONG...")
                && !txtAlamat.getText().isEmpty() && !txtAlamat.getText().equals("TIDAK BOLEH KOSONG...");
    }

    private void LoadFormToDatabase() {
        if (supplier == null) {
            supplier = new Supplier();
        }
        supplier.setKodeSupplier(txtKodeSupplier.getText());
        supplier.setNamaSupplier(txtNamaSupplier.getText());
        supplier.setAlamat(txtAlamat.getText());
        supplier.setTelp(txtNoTelp.getText());
    }

    private void LoadDatabaseToForm() {
        txtKodeSupplier.setText(supplier.getKodeSupplier());
        txtNamaSupplier.setText(supplier.getNamaSupplier());
        txtAlamat.setText(supplier.getAlamat());
        txtNoTelp.setText(supplier.getTelp());
    }

    private void LoadDatabaseToTable() {
        listSupplier = Main.getMasterService().supplierRecord();
        tblSupplier.setModel(new SupplierTableModel(listSupplier));
        int i = 0;
        for (; i < listSupplier.size(); i++) {
            tblSupplier.getSelectionModel().setSelectionInterval(i, i);
            tblSupplier.scrollRectToVisible(new Rectangle(tblSupplier.getCellRect(i, 0, true)));
        }
        initColumnSize();
    }

    private void initColumnSize() {
        tblSupplier.getColumnModel().getColumn(0).setMinWidth(150);
        tblSupplier.getColumnModel().getColumn(0).setMaxWidth(200);
        tblSupplier.getColumnModel().getColumn(1).setMinWidth(250);
        tblSupplier.getColumnModel().getColumn(1).setMaxWidth(300);
        tblSupplier.getColumnModel().getColumn(2).setMinWidth(250);
        tblSupplier.getColumnModel().getColumn(2).setMaxWidth(250);
        tblSupplier.getColumnModel().getColumn(3).setMinWidth(150);
        tblSupplier.getColumnModel().getColumn(3).setMaxWidth(150);
    }

    public void valueChanged(ListSelectionEvent lse) {
        if (lse.getValueIsAdjusting()) {
            return;
        }
        if (tblSupplier.getSelectedRow() >= 0) {
            supplier = listSupplier.get(tblSupplier.getSelectedRow());
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
        txtCariSupplier = new javax.swing.JTextField();
        cmbKriteria = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtKodeSupplier = new javax.swing.JTextField();
        txtNamaSupplier = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtAlamat = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtNoTelp = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSupplier = new javax.swing.JTable();
        buttonMaster = new mintory.master.buttonMaster();

        setIconifiable(true);
        setMaximizable(true);
        setTitle("Master Data Supplier");

        jSplitPane1.setDividerLocation(-5);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jSplitPane2.setDividerLocation(375);
        jSplitPane2.setDividerSize(5);

        jSplitPane3.setDividerLocation(-5);
        jSplitPane3.setDividerSize(10);
        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel1.setText("Cari Supplier");

        jLabel2.setText("Kriteria");

        txtCariSupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariSupplierKeyReleased(evt);
            }
        });

        cmbKriteria.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kode", "Nama" }));

        jPanel5.setBackground(new java.awt.Color(0, 72, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED)));

        jLabel11.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(254, 254, 254));
        jLabel11.setText("Pencarian Data Supplier");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addGap(79, 79, 79))
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
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cmbKriteria, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 132, Short.MAX_VALUE))
                    .addComponent(txtCariSupplier))
                .addContainerGap())
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCariSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        jLabel12.setText("Input Data Supplier");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addGap(96, 96, 96))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        jLabel3.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13)); // NOI18N
        jLabel3.setText("Kode Supplier");

        txtKodeSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtKodeSupplierMouseClicked(evt);
            }
        });

        txtNamaSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNamaSupplierMouseClicked(evt);
            }
        });
        txtNamaSupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNamaSupplierKeyReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13)); // NOI18N
        jLabel4.setText("Nama Supplier");

        txtAlamat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtAlamatMouseClicked(evt);
            }
        });
        txtAlamat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAlamatKeyReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13)); // NOI18N
        jLabel5.setText("Alamat");

        jLabel10.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13)); // NOI18N
        jLabel10.setText("No. Telepon");

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
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKodeSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNoTelp, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtKodeSupplier, txtNoTelp});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtAlamat, txtNamaSupplier});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtKodeSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNamaSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNoTelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(213, Short.MAX_VALUE))
        );

        jSplitPane3.setRightComponent(jPanel2);

        jSplitPane2.setLeftComponent(jSplitPane3);

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tblSupplier.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "  Kode Supplier", "  Nama Supplier", "  Alamat", "  No.Telepon"
            }
        ));
        tblSupplier.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblSupplier.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(tblSupplier);
        if (tblSupplier.getColumnModel().getColumnCount() > 0) {
            tblSupplier.getColumnModel().getColumn(0).setMinWidth(150);
            tblSupplier.getColumnModel().getColumn(0).setMaxWidth(200);
            tblSupplier.getColumnModel().getColumn(1).setMinWidth(250);
            tblSupplier.getColumnModel().getColumn(1).setMaxWidth(300);
            tblSupplier.getColumnModel().getColumn(2).setMinWidth(250);
            tblSupplier.getColumnModel().getColumn(2).setMaxWidth(250);
            tblSupplier.getColumnModel().getColumn(3).setMinWidth(150);
            tblSupplier.getColumnModel().getColumn(3).setMaxWidth(150);
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
    private void txtCariSupplierKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariSupplierKeyReleased
        String pilihan;
        int i = 0;
        boolean found = false;
        for (; i < listSupplier.size(); i++) {
            if (txtCariSupplier.getText().length() == 0) {
                tblSupplier.getSelectionModel().clearSelection();
                return;
            }
            if (cmbKriteria.getModel().getSelectedItem().equals("Kode")) {
                if (listSupplier.get(i).getKodeSupplier().startsWith(txtCariSupplier.getText())) {
                    found = true;
                    break;
                } else {
                    found = false;
                }
            } else if (cmbKriteria.getModel().getSelectedItem().equals("Nama")) {
                if (listSupplier.get(i).getNamaSupplier().startsWith(txtCariSupplier.getText())) {
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
            tblSupplier.getSelectionModel().setSelectionInterval(i, i);
            tblSupplier.scrollRectToVisible(new Rectangle(tblSupplier.getCellRect(i, 0, true)));
        } else {
            int optionType = JOptionPane.YES_NO_OPTION;
            int answer = JOptionPane.showConfirmDialog(this, "Data Tidak Ditemukan\nUlangi Pencarian ? ? ?", "Not Found", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (answer == JOptionPane.YES_OPTION) {
                enableForm(false);
                txtCariSupplier.requestFocusInWindow();
                tblSupplier.getSelectionModel().clearSelection();
            }
            if (answer == JOptionPane.NO_OPTION) {
                enableForm(false);
                buttonMaster.defaultMode();
                tblSupplier.getSelectionModel().clearSelection();
                housekeeping();
            }
            tblSupplier.getSelectionModel().clearSelection();
            previousOption = tblSupplier.getSelectedRow();
        }
}//GEN-LAST:event_txtCariSupplierKeyReleased

    private void txtKodeSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtKodeSupplierMouseClicked
        // TODO add your handling code here:
        if (txtKodeSupplier.getText().isEmpty() || txtKodeSupplier.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtKodeSupplier.setText("");
            txtKodeSupplier.setForeground(Color.BLACK);
        } else {
            return;
        }

    }//GEN-LAST:event_txtKodeSupplierMouseClicked

    private void txtNamaSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNamaSupplierMouseClicked
        // TODO add your handling code here:
        if (txtNamaSupplier.getText().isEmpty() || txtNamaSupplier.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtNamaSupplier.setText("");
            txtNamaSupplier.setForeground(Color.BLACK);
        } else {
            return;
        }
    }//GEN-LAST:event_txtNamaSupplierMouseClicked

    private void txtAlamatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAlamatMouseClicked
        // TODO add your handling code here:
        if (txtAlamat.getText().isEmpty() || txtAlamat.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtAlamat.setText("");
            txtAlamat.setForeground(Color.BLACK);
        } else {
            return;
        }
    }//GEN-LAST:event_txtAlamatMouseClicked

    private void txtNamaSupplierKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNamaSupplierKeyReleased
        // TODO add your handling code here:
        TextComponentUtils.setAutoUpperCaseText(txtNamaSupplier);
    }//GEN-LAST:event_txtNamaSupplierKeyReleased

    private void txtAlamatKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAlamatKeyReleased
        // TODO add your handling code here:
        TextComponentUtils.setAutoUpperCaseText(txtAlamat);
    }//GEN-LAST:event_txtAlamatKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private mintory.master.buttonMaster buttonMaster;
    private javax.swing.JComboBox cmbKriteria;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JTable tblSupplier;
    private javax.swing.JTextField txtAlamat;
    private javax.swing.JTextField txtCariSupplier;
    private javax.swing.JTextField txtKodeSupplier;
    private javax.swing.JTextField txtNamaSupplier;
    private javax.swing.JTextField txtNoTelp;
    // End of variables declaration//GEN-END:variables

}

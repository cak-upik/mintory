/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MasterKendaraan.java
 *
 * Created on Mar 27, 2012, 9:07:14 AM
 */
package paperman.master;

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
import paperman.Main;
import paperman.TextComponentUtils;
import paperman.model.kendaraanPutih;
import paperman.tablemodel.KendaraanPutihTableModel;

/**
 *
 * @author i1440ns
 */
public class MasterKendaraanPutih extends javax.swing.JInternalFrame implements ListSelectionListener{

    private static MasterKendaraanPutih masterKendaraanPutih;
    private List<kendaraanPutih> listKendaraanPutih = new ArrayList<kendaraanPutih>();
    private kendaraanPutih kendPutih;
    private static final Integer option = JOptionPane.OK_CANCEL_OPTION;

    public static void inisialisasi() {
        masterKendaraanPutih = new MasterKendaraanPutih();
    }

    public static void destroy() {
        masterKendaraanPutih.dispose();
        masterKendaraanPutih = null;
    }

    public static MasterKendaraanPutih getMasterKendaraanPutih() {
        return masterKendaraanPutih;
    }

    /** Creates new form MasterKendaraan */
    public MasterKendaraanPutih() {
        initComponents();
        initButtonListener();
        initButtonHotkeyFunction();
        buttonMaster.defaultMode();
        enableForm(false);
        LoadDatabaseToTable();
        tblKendaraan.getSelectionModel().addListSelectionListener(this);
        tblKendaraan.setAutoCreateColumnsFromModel(false);
        tblKendaraan.getSelectionModel().clearSelection();
        TextComponentUtils.setNumericTextOnly(txtNoLambung);
    }

    private void initButtonHotkeyFunction() {
        KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        kfm.addKeyEventDispatcher(new KeyEventDispatcher() {

            public boolean dispatchKeyEvent(KeyEvent ke) {
                /*  Button Tambah  */
                if(ke.getKeyCode() == KeyEvent.VK_F1 && ke.getID() == KeyEvent.KEY_PRESSED) {
                    buttonMaster.getBtnTambah().doClick();
                    System.out.println("F1 Pressed and GO aksiTambah");
                }
                /*  Button Ubah  */
                if(ke.getKeyCode() == KeyEvent.VK_F2 && ke.getID() == KeyEvent.KEY_PRESSED) {
                    buttonMaster.getBtnUbah().doClick();
                    System.out.println("F2 Pressed and GO aksiUbah");
                }
                /*  Button Simpan  */
                if(ke.getKeyCode() == KeyEvent.VK_F3 && ke.getID() == KeyEvent.KEY_PRESSED) {
                    buttonMaster.getBtnSimpan().doClick();
                    System.out.println("F3 Pressed and GO aksiSimpan");
                }
                /*  Button Hapus  */
                if(ke.getKeyCode() == KeyEvent.VK_F4 && ke.getID() == KeyEvent.KEY_PRESSED) {
                    buttonMaster.getBtnHapus().doClick();
                    System.out.println("F4 Pressed and GO aksiHapus");
                }
                /*  Button Batal  */
                if(ke.getKeyCode() == KeyEvent.VK_F5 && ke.getID() == KeyEvent.KEY_PRESSED) {
                    buttonMaster.getBtnBatal().doClick();
                    System.out.println("F5 Pressed and GO aksiBatal");
                }
                /*  Button Keluar  */
                if(ke.getKeyCode() == KeyEvent.VK_F6 && ke.getID() == KeyEvent.KEY_PRESSED) {
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
        txtNoLambung.requestFocus();
        buttonMaster.tambahMode();
        tblKendaraan.setEnabled(false);
    }

    private void aksiEdit() {
        enableForm(true);
        enableSearchEngine(false);
        txtNoLambung.requestFocus();
        buttonMaster.tambahMode();
        tblKendaraan.setEnabled(false);
    }

    private void aksiSimpan() {
        try {
            cekDuplicate();
        } catch (JDBCException jde) {
            int konfirmasi = JOptionPane.showConfirmDialog(this, "Data Dengan Kode Sama Sudah Ada\nGanti Kode Data ? ? ?", "Gagal Menyimpan", option, JOptionPane.QUESTION_MESSAGE);
            if (konfirmasi == JOptionPane.OK_OPTION) {
                enableForm(true);
                enableSearchEngine(false);
                txtNoLambung.requestFocus();
                buttonMaster.tambahMode();
                tblKendaraan.setEnabled(false);
            } else if (konfirmasi == JOptionPane.CANCEL_OPTION) {
                enableForm(true);
                enableSearchEngine(false);
                housekeeping();
                buttonMaster.defaultMode();
                tblKendaraan.setEnabled(true);
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
            tblKendaraan.getSelectionModel().clearSelection();
            tblKendaraan.setEnabled(true);
            buttonMaster.defaultMode();
            kendPutih = null;
        }
    }

    private void cekDuplicate() {
        if (validateForm()) {
            LoadFormToDatabase();
            Main.getMasterService().save(kendPutih);
            LoadDatabaseToTable();
            housekeeping();
            enableForm(false);
            enableSearchEngine(true);
            tblKendaraan.setEnabled(true);
            JOptionPane.showMessageDialog(this, "Proses Penyimpanan Telah Berhasil", "Pemberitahuan", JOptionPane.INFORMATION_MESSAGE);
            tblKendaraan.getSelectionModel().clearSelection();
            buttonMaster.defaultMode();
            kendPutih = null;
        } else {
            JOptionPane.showMessageDialog(this, "Terjadi Kesalahan\nGagal Menyimpan", "Pemberitahuan", JOptionPane.ERROR_MESSAGE);
            kenatilang();
            enableForm(true);
            enableSearchEngine(false);
            tblKendaraan.setEnabled(false);
            buttonMaster.tambahMode();
        }
    }

    private void aksiHapus() {
        String noLambung = listKendaraanPutih.get(tblKendaraan.getSelectedRow()).getNoLambung().toString();
        String noPol = listKendaraanPutih.get(tblKendaraan.getSelectedRow()).getNoPolisi();
        String noMesin = listKendaraanPutih.get(tblKendaraan.getSelectedRow()).getNoMesin();
        String noRangka = listKendaraanPutih.get(tblKendaraan.getSelectedRow()).getNoRangka();
        String noSTNK = listKendaraanPutih.get(tblKendaraan.getSelectedRow()).getNoSTNK();
        String jatuhTempo = listKendaraanPutih.get(tblKendaraan.getSelectedRow()).getTglJatuhTempo().toString();
        int konfirmasi = JOptionPane.showConfirmDialog(this, "Anda Yakin Akan Menghapus Data Ini ? ?\nRincican :" + "\nNo.Lambung = " + noLambung + "\nNo.Polisi =  " + noPol + "\nNo.Mesin = " + noMesin + "\nNo.Rangka = " + noRangka + "\nSTNK = " + noSTNK + "\nJatuh Tempo = " + jatuhTempo, "Konfirmasi Hapus Data", option, JOptionPane.QUESTION_MESSAGE);
        if (konfirmasi == JOptionPane.OK_OPTION) {
            Main.getMasterService().delete(kendPutih);
            LoadDatabaseToTable();
            housekeeping();
            kendPutih = null;
        }
        if (konfirmasi == JOptionPane.CANCEL_OPTION) {
            housekeeping();
            kendPutih = null;
        }
        enableForm(false);
        enableSearchEngine(true);
        housekeeping();
        tblKendaraan.getSelectionModel().clearSelection();
        tblKendaraan.setEnabled(true);
        buttonMaster.defaultMode();
    }

    private void aksiBatal() {
        housekeeping();
        enableForm(false);
        enableSearchEngine(true);
        tblKendaraan.getSelectionModel().clearSelection();
        tblKendaraan.setEnabled(true);
        kendPutih = null;
        buttonMaster.defaultMode();
    }

    private void aksiKeluar() {
        enableForm(false);
        enableSearchEngine(false);
        housekeeping();
        tblKendaraan.getSelectionModel().clearSelection();
        tblKendaraan.setEnabled(false);
        buttonMaster.keluarMode();
        destroy();
    }

    private void kenatilang() {
        if (txtNoLambung.getText().isEmpty() && txtNoMesin.getText().isEmpty() && txtNopol.getText().isEmpty() && txtNorang.getText().isEmpty() && txtSTNK.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Tidak boleh ada field yang kosong \nPeriksa Lagi !!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            txtNoLambung.setText("TIDAK BOLEH KOSONG...");
            txtNoMesin.setText("TIDAK BOLEH KOSONG...");
            txtNopol.setText("TIDAK BOLEH KOSONG...");
            txtNorang.setText("TIDAK BOLEH KOSONG...");
            txtSTNK.setText("TIDAK BOLEH KOSONG...");
        } else if (txtNoLambung.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi No. Lambung !!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            txtNoLambung.requestFocus();
            txtNoLambung.setText("TIDAK BOLEH KOSONG...");
        } else if (txtNoMesin.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi No. Mesin !!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            txtNoMesin.requestFocus();
            txtNoMesin.setText("TIDAK BOLEH KOSONG...");
        } else if (txtNopol.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi No. Polisi !!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            txtNopol.requestFocus();
            txtNopol.setText("TIDAK BOLEH KOSONG...");
        } else if (txtNorang.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi No. Rangka !!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            txtNorang.requestFocus();
            txtNorang.setText("TIDAK BOLEH KOSONG...");
        } else if (txtSTNK.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi No. STNK !!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            txtSTNK.requestFocus();
            txtSTNK.setText("TIDAK BOLEH KOSONG...");
        } else if (dateJatuhTempo.getDate() == null) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi tanggal jatuh tempo !!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            dateJatuhTempo.requestFocus();
        }
    }

    private void enableForm(boolean e) {
        txtNoLambung.setEnabled(e);
        txtNoMesin.setEnabled(e);
        txtNopol.setEnabled(e);
        txtNorang.setEnabled(e);
        txtSTNK.setEnabled(e);
        txtKeterangan.setEnabled(e);
        dateJatuhTempo.setEnabled(e);
    }

    private void enableSearchEngine(boolean e) {
        txtCariKendaraan.setEnabled(e);
        cmbKriteria.setEnabled(e);
    }

    private void housekeeping() {
        txtNoLambung.setText("");
        txtNoMesin.setText("");
        txtNopol.setText("");
        txtNorang.setText("");
        txtSTNK.setText("");
        txtKeterangan.setText("");
        dateJatuhTempo.setDate(null);
    }

    private boolean validateForm() {
        if (!txtNoLambung.getText().isEmpty() && !txtNoLambung.getText().equals("TIDAK BOLEH KOSONG...")
                && !txtNoMesin.getText().isEmpty() && !txtNoMesin.getText().equals("TIDAK BOLEH KOSONG...")
                && !txtNopol.getText().isEmpty() && !txtNopol.getText().equals("TIDAK BOLEH KOSONG...")
                && !txtNorang.getText().isEmpty() && !txtNorang.getText().equals("TIDAK BOLEH KOSONG...")
                && !txtSTNK.getText().isEmpty() && !txtSTNK.getText().equals("TIDAK BOLEH KOSONG...")
                && !txtKeterangan.getText().isEmpty() && dateJatuhTempo.getDate() != null) {
            return true;
        }
        return false;
    }

    private void LoadFormToDatabase() {
        if (kendPutih == null) {
            kendPutih = new kendaraanPutih();
        }
        kendPutih.setNoLambung(Integer.valueOf(txtNoLambung.getText()));
        kendPutih.setNoPolisi(txtNopol.getText());
        kendPutih.setNoMesin(txtNoMesin.getText());
        kendPutih.setNoRangka(txtNorang.getText());
        kendPutih.setNoSTNK(txtSTNK.getText());
        kendPutih.setTglJatuhTempo(dateJatuhTempo.getDate());
        kendPutih.setKeterangan(txtKeterangan.getText());
    }

    private void LoadDatabaseToForm() {
        txtNoLambung.setText(kendPutih.getNoLambung().toString());
        txtNopol.setText(kendPutih.getNoPolisi());
        txtNoMesin.setText(kendPutih.getNoMesin());
        txtNorang.setText(kendPutih.getNoRangka());
        txtSTNK.setText(kendPutih.getNoSTNK());
        dateJatuhTempo.setDate(kendPutih.getTglJatuhTempo());
        txtKeterangan.setText(kendPutih.getKeterangan());
    }

    private void LoadDatabaseToTable() {
        listKendaraanPutih = Main.getMasterService().kendaraanPutihRecord();
        tblKendaraan.setModel(new KendaraanPutihTableModel(listKendaraanPutih));
        int i = 0;
        for (; i < listKendaraanPutih.size(); i++) {
            tblKendaraan.getSelectionModel().setSelectionInterval(i, i);
            tblKendaraan.scrollRectToVisible(new Rectangle(tblKendaraan.getCellRect(i, 0, true)));
        }
        initColumnSize();
    }

    private void initColumnSize() {
        tblKendaraan.getColumnModel().getColumn(0).setMinWidth(200);
        tblKendaraan.getColumnModel().getColumn(0).setMaxWidth(250);
        tblKendaraan.getColumnModel().getColumn(1).setMinWidth(200);
        tblKendaraan.getColumnModel().getColumn(1).setMaxWidth(250);
        tblKendaraan.getColumnModel().getColumn(2).setMinWidth(200);
        tblKendaraan.getColumnModel().getColumn(2).setMaxWidth(250);
        tblKendaraan.getColumnModel().getColumn(3).setMinWidth(200);
        tblKendaraan.getColumnModel().getColumn(3).setMaxWidth(250);
        tblKendaraan.getColumnModel().getColumn(4).setMinWidth(200);
        tblKendaraan.getColumnModel().getColumn(4).setMaxWidth(250);
        tblKendaraan.getColumnModel().getColumn(5).setMinWidth(100);
        tblKendaraan.getColumnModel().getColumn(5).setMaxWidth(150);
        tblKendaraan.getColumnModel().getColumn(6).setMinWidth(100);
        tblKendaraan.getColumnModel().getColumn(6).setMaxWidth(150);
    }

    public void valueChanged(ListSelectionEvent lse) {
        if (lse.getValueIsAdjusting()) {
            return;
        }
        if (tblKendaraan.getSelectedRow() >= 0) {
            kendPutih = listKendaraanPutih.get(tblKendaraan.getSelectedRow());
            LoadDatabaseToForm();
            enableForm(false);
            enableSearchEngine(true);
            buttonMaster.tabelMode();
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

        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        jSplitPane3 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCariKendaraan = new javax.swing.JTextField();
        cmbKriteria = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtNoLambung = new javax.swing.JTextField();
        txtNopol = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtNoMesin = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtNorang = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtSTNK = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtKeterangan = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        dateJatuhTempo = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblKendaraan = new javax.swing.JTable();
        buttonMaster = new paperman.master.buttonMaster();

        setIconifiable(true);
        setMaximizable(true);
        setTitle("Data Kendaraan - Armada Putih");

        jSplitPane1.setDividerLocation(-5);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jSplitPane2.setDividerLocation(450);

        jSplitPane3.setDividerLocation(-5);
        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel1.setText("Cari Kendaraan");

        jLabel2.setText("Kriteria");

        txtCariKendaraan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariKendaraanKeyReleased(evt);
            }
        });

        cmbKriteria.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No.Lambung", "No. Polisi" }));

        jPanel5.setBackground(new java.awt.Color(255, 254, 254));

        jLabel11.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13)); // NOI18N
        jLabel11.setText("Pencarian Data Kendaraan");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel11)
                .addContainerGap(271, Short.MAX_VALUE))
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
                    .addComponent(txtCariKendaraan, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
                    .addComponent(cmbKriteria, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(txtCariKendaraan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbKriteria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(4, 4, 4))
        );

        jSplitPane3.setLeftComponent(jPanel1);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13)); // NOI18N
        jLabel12.setText("Input Data Kendaraan");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel12)
                .addContainerGap(304, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        jLabel3.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13));
        jLabel3.setText("No.Lambung");

        txtNoLambung.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNoLambungMouseClicked(evt);
            }
        });

        txtNopol.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNopolMouseClicked(evt);
            }
        });
        txtNopol.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNopolKeyReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13));
        jLabel4.setText("No.Polisi");

        txtNoMesin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNoMesinMouseClicked(evt);
            }
        });
        txtNoMesin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNoMesinKeyReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13));
        jLabel5.setText("No.Mesin");

        txtNorang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNorangMouseClicked(evt);
            }
        });
        txtNorang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNorangKeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13));
        jLabel6.setText("No.Rangka");

        txtSTNK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSTNKMouseClicked(evt);
            }
        });
        txtSTNK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSTNKKeyReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13));
        jLabel7.setText("STNK");

        txtKeterangan.setColumns(20);
        txtKeterangan.setRows(5);
        txtKeterangan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKeteranganKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(txtKeterangan);

        jLabel8.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13));
        jLabel8.setText("Keterangan");

        jLabel10.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13));
        jLabel10.setText("Tgl Jatuh Tempo");

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
                    .addComponent(jLabel7)
                    .addComponent(jLabel10)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtNopol, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                        .addComponent(txtSTNK, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                        .addComponent(txtNorang)
                        .addComponent(txtNoMesin)
                        .addComponent(txtNoLambung, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(dateJatuhTempo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtNoLambung, txtNoMesin, txtNopol, txtNorang, txtSTNK});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtNoLambung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNopol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtNoMesin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtNorang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtSTNK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dateJatuhTempo, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(100, 100, 100))
        );

        jSplitPane3.setRightComponent(jPanel2);

        jSplitPane2.setLeftComponent(jSplitPane3);

        tblKendaraan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "  No. Lambung", "  No. Polisi", "  No. Mesin", "  No. Rangka", "  STNK", "  Jatuh Tempo", "  Keterangan"
            }
        ));
        tblKendaraan.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblKendaraan.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(tblKendaraan);

        jSplitPane2.setRightComponent(jScrollPane2);

        jSplitPane1.setBottomComponent(jSplitPane2);

        buttonMaster.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jSplitPane1.setLeftComponent(buttonMaster);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1052, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 641, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    int previousOption = -1;
    private void txtCariKendaraanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKendaraanKeyReleased
        String pilihan;
        int i = 0;
        boolean found = false;
        for (; i < listKendaraanPutih.size(); i++) {
            if(txtCariKendaraan.getText().length() == 0) {
                tblKendaraan.getSelectionModel().clearSelection();
                return;
            }
            if (cmbKriteria.getModel().getSelectedItem().equals("No.Lambung")) {
                if (listKendaraanPutih.get(i).getNoLambung().toString().startsWith(txtCariKendaraan.getText())) {
                    found = true;
                    break;
                    //                } if (txtCariBarang.getText().startsWith(evt. {
                    //                    found = false;
                    //                    JOptionPane.showMessageDialog(this, "Parameter Search Characters Salah, Cek Lagi Data Anda ! !\nSeharusnya Yang Di Isi Adalah Kode Bukan Nama!!", "Terjadi Kesalahan", JOptionPane.INFORMATION_MESSAGE);
                    //                    break;
                } else {
                    found = false;
                    
                }
                //if (evt.getKeyChar() == KeyEvent.VK_BACK_SPACE && txtCariKendaraan.getText().length() == 0) {
                    //                JOptionPane.showMessageDialog(this, "Pastikan Semua Data Yang Di Isikan Benar\nPeriksa Kembali Data Anda", "Error", JOptionPane.ERROR_MESSAGE);
                  //  tblKendaraan.getSelectionModel().clearSelection();
                  //  txtCariKendaraan.requestFocusInWindow();
                //}

            } else if (cmbKriteria.getModel().getSelectedItem().equals("No. Polisi")) {
                if (listKendaraanPutih.get(i).getNoPolisi().startsWith(txtCariKendaraan.getText())) {
                    found = true;
                    break;
                    //                } if (txtCariBarang.getText().matches(listBarang.get(i).getKode())) {
                    //                    found = false;
                    //                    JOptionPane.showMessageDialog(this, "Parameter Search Characters Salah, Cek Lagi Data Anda ! !\nSeharusnya Yang Di Isi Adalah Nama Bukan Kode!!", "Terjadi Kesalahan", JOptionPane.INFORMATION_MESSAGE);
                    //                    break;
                }else {
                    found = false;
                }

                //if (evt.getKeyChar() == KeyEvent.VK_BACK_SPACE && txtCariKendaraan.getText().length() == 0) {
                    //                JOptionPane.showMessageDialog(this, "Pastikan Semua Data Yang Di Isikan Benar\nPeriksa Kembali Data Anda", "Error", JOptionPane.ERROR_MESSAGE);
                  //  tblKendaraan.getSelectionModel().clearSelection();
                  //  txtCarinKendaraan.requestFocusInWindow();
                //}
            }else {
                return;
            }
        }

        if (found) {
            tblKendaraan.getSelectionModel().setSelectionInterval(i, i);
            tblKendaraan.scrollRectToVisible(new Rectangle(tblKendaraan.getCellRect(i, 0, true)));
        } else {
            int optionType = JOptionPane.YES_NO_OPTION;
            int answer = JOptionPane.showConfirmDialog(this, "Data Tidak Ditemukan\nUlangi Pencarian ? ? ?", "Not Found", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (answer == JOptionPane.YES_OPTION) {
                enableForm(false);
                txtCariKendaraan.requestFocusInWindow();
                tblKendaraan.getSelectionModel().clearSelection();
            }
            if (answer == JOptionPane.NO_OPTION) {
                enableForm(false);
                buttonMaster.defaultMode();
                tblKendaraan.getSelectionModel().clearSelection();
                housekeeping();
            }
            tblKendaraan.getSelectionModel().clearSelection();
            previousOption = tblKendaraan.getSelectedRow();
        }
}//GEN-LAST:event_txtCariKendaraanKeyReleased

    private void txtNoLambungMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNoLambungMouseClicked
        // TODO add your handling code here:
        if (txtNoLambung.getText().isEmpty() || txtNoLambung.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtNoLambung.setText("");
        } else {
            return;
        }

    }//GEN-LAST:event_txtNoLambungMouseClicked

    private void txtNopolMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNopolMouseClicked
        // TODO add your handling code here:
        if (txtNopol.getText().isEmpty() || txtNopol.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtNopol.setText("");
        } else {
            return;
        }
    }//GEN-LAST:event_txtNopolMouseClicked

    private void txtNoMesinMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNoMesinMouseClicked
        // TODO add your handling code here:
        if (txtNoMesin.getText().isEmpty() || txtNoMesin.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtNoMesin.setText("");
        } else {
            return;
        }
    }//GEN-LAST:event_txtNoMesinMouseClicked

    private void txtNorangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNorangMouseClicked
        // TODO add your handling code here:
        if (txtNorang.getText().isEmpty() || txtNorang.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtNorang.setText("");
        } else {
            return;
        }
    }//GEN-LAST:event_txtNorangMouseClicked

    private void txtSTNKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSTNKMouseClicked
        // TODO add your handling code here:
        if (txtSTNK.getText().isEmpty() || txtSTNK.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtSTNK.setText("");
        } else {
            return;
        }
    }//GEN-LAST:event_txtSTNKMouseClicked

    private void txtNopolKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNopolKeyReleased
        // TODO add your handling code here:
        TextComponentUtils.setAutoUpperCaseText(txtNopol);
    }//GEN-LAST:event_txtNopolKeyReleased

    private void txtNoMesinKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoMesinKeyReleased
        // TODO add your handling code here:
        TextComponentUtils.setAutoUpperCaseText(txtNoMesin);
    }//GEN-LAST:event_txtNoMesinKeyReleased

    private void txtNorangKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNorangKeyReleased
        // TODO add your handling code here:
        TextComponentUtils.setAutoUpperCaseText(txtNorang);
    }//GEN-LAST:event_txtNorangKeyReleased

    private void txtSTNKKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSTNKKeyReleased
        // TODO add your handling code here:
        TextComponentUtils.setAutoUpperCaseText(txtSTNK);
    }//GEN-LAST:event_txtSTNKKeyReleased

    private void txtKeteranganKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKeteranganKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyChar() == '\n') {
            aksiSimpan();
        }
    }//GEN-LAST:event_txtKeteranganKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private paperman.master.buttonMaster buttonMaster;
    private javax.swing.JComboBox cmbKriteria;
    private com.toedter.calendar.JDateChooser dateJatuhTempo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
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
    private javax.swing.JTable tblKendaraan;
    private javax.swing.JTextField txtCariKendaraan;
    private javax.swing.JTextArea txtKeterangan;
    private javax.swing.JTextField txtNoLambung;
    private javax.swing.JTextField txtNoMesin;
    private javax.swing.JTextField txtNopol;
    private javax.swing.JTextField txtNorang;
    private javax.swing.JTextField txtSTNK;
    // End of variables declaration//GEN-END:variables

}

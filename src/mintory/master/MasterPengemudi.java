/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MasterPengemudi.java
 *
 * Created on Mar 27, 2012, 9:43:07 AM
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
import mintory.model.Pengemudi;
import mintory.tablemodel.PengemudiTableModel;

/**
 *
 * @author i1440ns
 */
public class MasterPengemudi extends javax.swing.JInternalFrame implements ListSelectionListener {

    private static MasterPengemudi masterPengemudi;
    private List<Pengemudi> listPengemudi = new ArrayList<Pengemudi>();
    private Pengemudi kemudi;
    private static final Integer option = JOptionPane.OK_CANCEL_OPTION;

    public static void inisialisasi() {
        masterPengemudi = new MasterPengemudi();
    }

    public static void destroy() {
        masterPengemudi.dispose();
        masterPengemudi = null;
    }

    public static MasterPengemudi getMasterPengemudi() {
        return masterPengemudi;
    }

    /**
     * Creates new form MasterPengemudi
     */
    public MasterPengemudi() {
        initComponents();
        initButtonListener();
        initButtonHotkeyFunction();
        enableForm(false);
        buttonMaster.defaultMode();
        LoadDatabaseToTable();
        tblPengemudi.setAutoCreateColumnsFromModel(false);
        tblPengemudi.getSelectionModel().addListSelectionListener(this);
        tblPengemudi.getSelectionModel().clearSelection();
        TextComponentUtils.setAutoUpperCaseText(txtNRP);
        TextComponentUtils.setAutoUpperCaseText(txtNama);
        TextComponentUtils.setAutoUpperCaseText(txtKota);
        TextComponentUtils.setAutoUpperCaseText(txtAlamat);
    }

    private void initButtonHotkeyFunction() {
        KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        kfm.addKeyEventDispatcher(new KeyEventDispatcher() {

            public boolean dispatchKeyEvent(KeyEvent ke) {
                /*  Button Tambah  */
                if (ke.getKeyCode() == KeyEvent.VK_F1 && ke.getID() == KeyEvent.KEY_PRESSED) {
                    buttonMaster.getBtnTambah().doClick();
                    System.out.println("F1 Pressed and GO aksiTambah");
                } /*  Button Ubah  */ else if (ke.getKeyCode() == KeyEvent.VK_F2 && ke.getID() == KeyEvent.KEY_PRESSED) {
                    buttonMaster.getBtnUbah().doClick();
                    System.out.println("F2 Pressed and GO aksiUbah");
                } /*  Button Simpan  */ else if (ke.getKeyCode() == KeyEvent.VK_F3 && ke.getID() == KeyEvent.KEY_PRESSED) {
                    buttonMaster.getBtnSimpan().doClick();
                    System.out.println("F3 Pressed and GO aksiSimpan");
                }/*  Button Hapus  */ else if (ke.getKeyCode() == KeyEvent.VK_F4 && ke.getID() == KeyEvent.KEY_PRESSED) {
                    buttonMaster.getBtnHapus().doClick();
                    System.out.println("F4 Pressed and GO aksiHapus");
                }/*  Button Batal  */ else if (ke.getKeyCode() == KeyEvent.VK_F5 && ke.getID() == KeyEvent.KEY_PRESSED) {
                    buttonMaster.getBtnBatal().doClick();
                    System.out.println("F5 Pressed and GO aksiBatal");
                }/*  Button Keluar  */ else if (ke.getKeyCode() == KeyEvent.VK_F6 && ke.getID() == KeyEvent.KEY_PRESSED) {
                    buttonMaster.getBtnKeluar().doClick();
                    System.out.println("F6 Pressed and GO aksiKeluar");
                } else {
                    return false;
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
        buttonMaster.tambahMode();
        tblPengemudi.setEnabled(false);
        txtNRP.requestFocus();
    }

    private void aksiEdit() {
        enableForm(true);
        enableSearchEngine(false);
        buttonMaster.tambahMode();
        tblPengemudi.setEnabled(false);
        txtNRP.requestFocus();
    }

    private void aksiSimpan() {
        try {
            cekDuplicate();
        } catch (JDBCException jde) {
            int konfirmasi = JOptionPane.showConfirmDialog(this, "Data Dengan No.Lambung / NRP Yang Sama Sudah Ada\nGanti Data ? ? ?", "Gagal Menyimpan", option, JOptionPane.QUESTION_MESSAGE);
            if (konfirmasi == JOptionPane.OK_OPTION) {
                enableForm(true);
                enableSearchEngine(false);
                buttonMaster.tambahMode();
                tblPengemudi.setEnabled(false);
                txtNRP.requestFocus();
            } else if (konfirmasi == JOptionPane.CANCEL_OPTION) {
                enableForm(false);
                enableSearchEngine(true);
                housekeeping();
                buttonMaster.defaultMode();
                tblPengemudi.setEnabled(true);
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
            enableSearchEngine(true);
            tblPengemudi.getSelectionModel().clearSelection();
            tblPengemudi.setEnabled(true);
            buttonMaster.defaultMode();
            kemudi = null;
        }
    }

    private void cekDuplicate() {
        if (validateForm()) {
            LoadFormToDatabase();
            Main.getMasterService().save(kemudi);
            LoadDatabaseToTable();
            housekeeping();
            enableForm(false);
            enableSearchEngine(true);
            JOptionPane.showMessageDialog(this, "Proses Penyimpanan Telah Berhasil", "Pemberitahuan", JOptionPane.INFORMATION_MESSAGE);
            tblPengemudi.getSelectionModel().clearSelection();
            tblPengemudi.setEnabled(true);
            buttonMaster.defaultMode();
            kemudi = null;
        } else {
            JOptionPane.showMessageDialog(this, "Terjadi Kesalahan\nGagal Menyimpan", "Pemberitahuan", JOptionPane.ERROR_MESSAGE);
            kenatilang();
            enableForm(true);
            enableSearchEngine(false);
            tblPengemudi.setEnabled(false);
            buttonMaster.tambahMode();
        }
    }

    private void aksiHapus() {
        String nrp = listPengemudi.get(tblPengemudi.getSelectedRow()).getNrp();
        String nama = listPengemudi.get(tblPengemudi.getSelectedRow()).getNama();
        String alamat = listPengemudi.get(tblPengemudi.getSelectedRow()).getAlamat();
        String kota = listPengemudi.get(tblPengemudi.getSelectedRow()).getKota();
        String keterangan = listPengemudi.get(tblPengemudi.getSelectedRow()).getKeterangan();
        int konfirmasi = JOptionPane.showConfirmDialog(this, "Anda Yakin Akan Menghapus Data Ini ? ?\nRincian : \nNRP =" + nrp + "\nNama = " + nama + "\nAlamat = " + alamat + "\nKota = " + kota + "\nKeterangan = " + keterangan, "Konfirmasi Hapus Data", option, JOptionPane.QUESTION_MESSAGE);
        if (konfirmasi == JOptionPane.OK_OPTION) {
            Main.getMasterService().delete(kemudi);
            LoadDatabaseToTable();
            housekeeping();
            JOptionPane.showMessageDialog(this, "Data Berhasil Dihapus", "Pesan Sistem", JOptionPane.INFORMATION_MESSAGE);
            kemudi = null;
        }
        if (konfirmasi == JOptionPane.CANCEL_OPTION) {
            housekeeping();
            kemudi = null;
        }
        enableForm(false);
        enableSearchEngine(true);
        housekeeping();
        tblPengemudi.getSelectionModel().clearSelection();
        tblPengemudi.setEnabled(true);
        buttonMaster.defaultMode();
    }

    private void aksiBatal() {
        housekeeping();
        enableForm(false);
        enableSearchEngine(true);
        tblPengemudi.getSelectionModel().clearSelection();
        tblPengemudi.setEnabled(true);
        kemudi = null;
        buttonMaster.defaultMode();
    }

    private void aksiKeluar() {
        enableForm(false);
        enableSearchEngine(false);
        housekeeping();
        tblPengemudi.getSelectionModel().clearSelection();
        tblPengemudi.setEnabled(false);
        buttonMaster.keluarMode();
        destroy();
    }

    private void kenatilang() {
        if (txtNRP.getText().isEmpty() &&txtNoLambung.getText().isEmpty() && txtNama.getText().isEmpty() && txtAlamat.getText().isEmpty() && txtKota.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Tidak boleh ada field yang kosong \nPeriksa Lagi !!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            txtNRP.setText("TIDAK BOLEH KOSONG...");
            txtNRP.setForeground(Color.red);
            txtNama.setText("TIDAK BOLEH KOSONG...");
            txtNama.setForeground(Color.red);
            txtAlamat.setText("TIDAK BOLEH KOSONG...");
            txtAlamat.setForeground(Color.RED);
            txtKota.setText("TIDAK BOLEH KOSONG...");
            txtKota.setForeground(Color.red);
        } else if (txtNoLambung.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi No Lambung !!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            txtNoLambung.requestFocus();
            txtNoLambung.setText("TIDAK BOLEH KOSONG...");
            txtNoLambung.setForeground(Color.red);
        } else if (txtNRP.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi NRP !!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            txtNRP.requestFocus();
            txtNRP.setText("TIDAK BOLEH KOSONG...");
            txtNRP.setForeground(Color.red);
        } else if (txtNama.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi Nama!!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            txtNama.requestFocus();
            txtNama.setText("TIDAK BOLEH KOSONG...");
            txtNama.setForeground(Color.red);
        } else if (txtAlamat.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi Alamat !!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            txtAlamat.requestFocus();
            txtAlamat.setText("TIDAK BOLEH KOSONG...");
            txtAlamat.setForeground(Color.red);
        } else if (txtKota.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi Kota!!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            txtKota.requestFocus();
            txtKota.setText("TIDAK BOLEH KOSONG...");
            txtKota.setForeground(Color.red);
        }
    }

    private void enableForm(boolean e) {
        txtNRP.setEnabled(e);
        txtNama.setEnabled(e);
        txtNoLambung.setEnabled(e);
        txtAlamat.setEnabled(e);
        txtKota.setEnabled(e);
        txtKeterangan.setEnabled(e);
    }

    private void enableSearchEngine(boolean e) {
        txtCariPengemudi.setEnabled(e);
        cmbKriteria.setEnabled(e);
    }

    private void housekeeping() {
        txtNRP.setText("");
        txtNama.setText("");
        txtNoLambung.setText("");
        txtAlamat.setText("");
        txtKota.setText("");
        txtKeterangan.setText("");
    }

    private boolean validateForm() {
        if (!txtNRP.getText().isEmpty() && !txtNRP.getText().equals("TIDAK BOLEH KOSONG...")
                && !txtNoLambung.getText().isEmpty() && !txtNoLambung.getText().equals("TIDAK BOLEH KOSONG...")
                && !txtNama.getText().isEmpty() && !txtNama.getText().equals("TIDAK BOLEH KOSONG...")
                && !txtAlamat.getText().isEmpty() && !txtAlamat.getText().equals("TIDAK BOLEH KOSONG...")
                && !txtKota.getText().isEmpty() && !txtKota.getText().equals("TIDAK BOLEH KOSONG...")) {
            return true;
        }
        return false;
    }

    private void LoadFormToDatabase() {
        if (kemudi == null) {
            kemudi = new Pengemudi();
        }
        kemudi.setNrp(txtNRP.getText());
        kemudi.setNoLB(txtNoLambung.getText());
        kemudi.setNama(txtNama.getText());
        kemudi.setAlamat(txtAlamat.getText());
        kemudi.setKota(txtKota.getText());
        kemudi.setKeterangan(txtKeterangan.getText());
    }

    private void LoadDatabaseToForm() {
        txtNRP.setText(kemudi.getNrp());
        txtNoLambung.setText(kemudi.getNoLB());
        txtNama.setText(kemudi.getNama());
        txtAlamat.setText(kemudi.getAlamat());
        txtKota.setText(kemudi.getKota());
        txtKeterangan.setText(kemudi.getKeterangan());
    }

    private void initColumnSize() {
        tblPengemudi.getColumnModel().getColumn(0).setMinWidth(100);
        tblPengemudi.getColumnModel().getColumn(0).setMaxWidth(100);
        tblPengemudi.getColumnModel().getColumn(1).setMinWidth(100);
        tblPengemudi.getColumnModel().getColumn(1).setMaxWidth(100);
        tblPengemudi.getColumnModel().getColumn(2).setMinWidth(200);
        tblPengemudi.getColumnModel().getColumn(2).setMaxWidth(200);
        tblPengemudi.getColumnModel().getColumn(3).setMinWidth(250);
        tblPengemudi.getColumnModel().getColumn(3).setMaxWidth(250);
        tblPengemudi.getColumnModel().getColumn(4).setMinWidth(100);
        tblPengemudi.getColumnModel().getColumn(4).setMaxWidth(100);
        tblPengemudi.getColumnModel().getColumn(5).setMinWidth(100);
        tblPengemudi.getColumnModel().getColumn(5).setMaxWidth(150);
    }

    private void LoadDatabaseToTable() {
        listPengemudi = Main.getMasterService().kemudiRecord();
        tblPengemudi.setModel(new PengemudiTableModel(listPengemudi));
        int i = 0;
        for (; i < listPengemudi.size(); i++) {
            tblPengemudi.getSelectionModel().setSelectionInterval(i, i);
            tblPengemudi.scrollRectToVisible(new Rectangle(tblPengemudi.getCellRect(i, 0, true)));
        }
        initColumnSize();
    }

    public void valueChanged(ListSelectionEvent lse) {
        if (lse.getValueIsAdjusting()) {
            return;
        }
        if (tblPengemudi.getSelectedRow() >= 0) {
            kemudi = listPengemudi.get(tblPengemudi.getSelectedRow());
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
        txtCariPengemudi = new javax.swing.JTextField();
        cmbKriteria = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtNRP = new javax.swing.JTextField();
        txtNama = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtAlamat = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtKota = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtKeterangan = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        txtNoLambung = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPengemudi = new javax.swing.JTable();
        buttonMaster = new mintory.master.buttonMaster();

        setIconifiable(true);
        setMaximizable(true);
        setTitle("Data Pengemudi - Armada Biru");

        jSplitPane1.setDividerLocation(-5);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jSplitPane2.setDividerLocation(430);

        jSplitPane3.setDividerLocation(-5);
        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel1.setText("Cari Pengemudi");

        jLabel2.setText("Kriteria");

        txtCariPengemudi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariPengemudiKeyReleased(evt);
            }
        });

        cmbKriteria.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "NRP", "NAMA" }));

        jPanel5.setBackground(new java.awt.Color(0, 72, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED)));

        jLabel11.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(254, 254, 254));
        jLabel11.setText("Pencarian Data Pengemudi");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addGap(76, 76, 76))
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
                    .addComponent(cmbKriteria, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCariPengemudi, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCariPengemudi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbKriteria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(4, 4, 4))
        );

        jSplitPane3.setLeftComponent(jPanel1);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel3.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13)); // NOI18N
        jLabel3.setText("NRP");

        txtNRP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNRPMouseClicked(evt);
            }
        });

        txtNama.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNamaMouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13)); // NOI18N
        jLabel4.setText("Nama");

        txtAlamat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtAlamatMouseClicked(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13)); // NOI18N
        jLabel5.setText("Alamat");

        txtKota.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtKotaMouseClicked(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13)); // NOI18N
        jLabel6.setText("Kota");

        jPanel4.setBackground(new java.awt.Color(0, 73, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED)));

        jLabel12.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 16)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(254, 254, 254));
        jLabel12.setText("Input Data Pengemudi");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(121, 121, 121)
                .addComponent(jLabel12)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        jLabel14.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        jLabel14.setText("Keterangan");

        txtKeterangan.setColumns(20);
        txtKeterangan.setRows(5);
        jScrollPane1.setViewportView(txtKeterangan);

        jLabel7.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        jLabel7.setText("No Lambung");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel7)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNRP, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNoLambung, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNama, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
                    .addComponent(txtAlamat)
                    .addComponent(txtKota)
                    .addComponent(jScrollPane1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNRP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoLambung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtKota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jSplitPane3.setRightComponent(jPanel2);

        jSplitPane2.setLeftComponent(jSplitPane3);

        jScrollPane2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setToolTipText("");
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tblPengemudi.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tblPengemudi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "  NRP", "  No Lambung", "  Nama", "  Alamat", "  Kota", "  Keterangan"
            }
        ));
        tblPengemudi.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblPengemudi.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(tblPengemudi);
        if (tblPengemudi.getColumnModel().getColumnCount() > 0) {
            tblPengemudi.getColumnModel().getColumn(0).setMinWidth(100);
            tblPengemudi.getColumnModel().getColumn(0).setMaxWidth(100);
            tblPengemudi.getColumnModel().getColumn(1).setMinWidth(100);
            tblPengemudi.getColumnModel().getColumn(1).setMaxWidth(100);
            tblPengemudi.getColumnModel().getColumn(2).setMinWidth(200);
            tblPengemudi.getColumnModel().getColumn(2).setMaxWidth(200);
            tblPengemudi.getColumnModel().getColumn(3).setMinWidth(250);
            tblPengemudi.getColumnModel().getColumn(3).setMaxWidth(250);
            tblPengemudi.getColumnModel().getColumn(4).setMinWidth(100);
            tblPengemudi.getColumnModel().getColumn(4).setMaxWidth(100);
            tblPengemudi.getColumnModel().getColumn(5).setMinWidth(100);
            tblPengemudi.getColumnModel().getColumn(5).setMaxWidth(150);
        }

        jSplitPane2.setBottomComponent(jScrollPane2);

        jSplitPane1.setBottomComponent(jSplitPane2);

        buttonMaster.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jSplitPane1.setLeftComponent(buttonMaster);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1096, Short.MAX_VALUE)
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
    private void txtCariPengemudiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariPengemudiKeyReleased
        // TODO add your handling code here:
        String pilihan;
        int i = 0;
        boolean found = false;
        for (; i < listPengemudi.size(); i++) {
            if (txtCariPengemudi.getText().length() == 0) {
                tblPengemudi.getSelectionModel().clearSelection();
                return;
            }
            if (cmbKriteria.getModel().getSelectedItem().equals("NRP")) {
                if (listPengemudi.get(i).getNrp().startsWith(txtCariPengemudi.getText())) {
                    found = true;
                    break;
                } else {
                    found = false;
                }
            } else if (cmbKriteria.getModel().getSelectedItem().equals("NAMA")) {
                if (listPengemudi.get(i).getNama().startsWith(txtCariPengemudi.getText())) {
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
            tblPengemudi.getSelectionModel().setSelectionInterval(i, i);
            tblPengemudi.scrollRectToVisible(new Rectangle(tblPengemudi.getCellRect(i, 0, true)));
        } else {
            int optionType = JOptionPane.YES_NO_OPTION;
            int answer = JOptionPane.showConfirmDialog(this, "Data Tidak Ditemukan\nUlangi Pencarian ? ? ?", "Not Found", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (answer == JOptionPane.YES_OPTION) {
                enableForm(false);
                txtCariPengemudi.requestFocusInWindow();
                tblPengemudi.getSelectionModel().clearSelection();
            }
            if (answer == JOptionPane.NO_OPTION) {
                enableForm(false);
                buttonMaster.defaultMode();
                tblPengemudi.getSelectionModel().clearSelection();
                housekeeping();
            }
            tblPengemudi.getSelectionModel().clearSelection();
            previousOption = tblPengemudi.getSelectedRow();
        }
}//GEN-LAST:event_txtCariPengemudiKeyReleased

    private void txtNRPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNRPMouseClicked
        // TODO add your handling code here:
        if (txtNRP.getText().isEmpty() || txtNRP.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtNRP.setText("");
            txtNRP.setForeground(Color.BLACK);
        } else {
            return;
        }
    }//GEN-LAST:event_txtNRPMouseClicked

    private void txtNamaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNamaMouseClicked
        // TODO add your handling code here:
        if (txtNama.getText().isEmpty() || txtNama.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtNama.setText("");
            txtNama.setForeground(Color.BLACK);
        } else {
            return;
        }
    }//GEN-LAST:event_txtNamaMouseClicked

    private void txtAlamatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAlamatMouseClicked
        // TODO add your handling code here:
        if (txtAlamat.getText().isEmpty() || txtAlamat.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtAlamat.setText("");
            txtAlamat.setForeground(Color.BLACK);
        } else {
            return;
        }
    }//GEN-LAST:event_txtAlamatMouseClicked

    private void txtKotaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtKotaMouseClicked
        // TODO add your handling code here:
        if (txtKota.getText().isEmpty() || txtKota.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtKota.setText("");
            txtKota.setForeground(Color.BLACK);
        } else {
            return;
        }
    }//GEN-LAST:event_txtKotaMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private mintory.master.buttonMaster buttonMaster;
    private javax.swing.JComboBox cmbKriteria;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JTable tblPengemudi;
    private javax.swing.JTextField txtAlamat;
    private javax.swing.JTextField txtCariPengemudi;
    private javax.swing.JTextArea txtKeterangan;
    private javax.swing.JTextField txtKota;
    private javax.swing.JTextField txtNRP;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtNoLambung;
    // End of variables declaration//GEN-END:variables
}

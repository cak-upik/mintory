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
import mintory.model.kendaraanPutih;
import mintory.model.pengemudiPutih;
import mintory.tablemodel.PengemudPutihiTableModel;

/**
 *
 * @author i1440ns
 */
public class MasterPengemudiPutih extends javax.swing.JInternalFrame implements ListSelectionListener {

    private static MasterPengemudiPutih masterPengemudiPutih;
    private List<pengemudiPutih> listPengemudiPutih = new ArrayList<pengemudiPutih>();
    private List<kendaraanPutih> listKendaraanPutih;
    private pengemudiPutih kemudiPutih;
    private kendaraanPutih kendPutih;
    private static final Integer option = JOptionPane.OK_CANCEL_OPTION;

    public static void inisialisasi() {
        masterPengemudiPutih = new MasterPengemudiPutih();
    }

    public static void destroy() {
        masterPengemudiPutih.dispose();
        masterPengemudiPutih = null;
    }

    public static MasterPengemudiPutih getMasterPengemudiPutih() {
        return masterPengemudiPutih;
    }

    /** Creates new form MasterPengemudi */
    public MasterPengemudiPutih() {
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
        TextComponentUtils.setNumericTextOnly(txtRefNoLambung);
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
        buttonMaster.tambahMode();
        tblPengemudi.setEnabled(false);
        txtRefNoLambung.requestFocus();
    }

    private void aksiEdit() {
        enableForm(true);
        enableSearchEngine(false);
        buttonMaster.tambahMode();
        tblPengemudi.setEnabled(false);
        txtRefNoLambung.requestFocus();
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
            kemudiPutih = null;
        }
    }

    private void cekDuplicate() {
        if (validateForm()) {
            LoadFormToDatabase();
            Main.getMasterService().save(kemudiPutih);
            LoadDatabaseToTable();
            housekeeping();
            enableForm(false);
            enableSearchEngine(true);
            JOptionPane.showMessageDialog(this, "Proses Penyimpanan Telah Berhasil", "Pemberitahuan", JOptionPane.INFORMATION_MESSAGE);
            tblPengemudi.getSelectionModel().clearSelection();
            tblPengemudi.setEnabled(true);
            buttonMaster.defaultMode();
            kemudiPutih = null;
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
        String nrp = listPengemudiPutih.get(tblPengemudi.getSelectedRow()).getNrp();
        String nama = listPengemudiPutih.get(tblPengemudi.getSelectedRow()).getNama();
        String alamat = listPengemudiPutih.get(tblPengemudi.getSelectedRow()).getAlamat();
        String kota = listPengemudiPutih.get(tblPengemudi.getSelectedRow()).getKota();
        String tglmasuk = listPengemudiPutih.get(tblPengemudi.getSelectedRow()).getTglMasuk().toString();
        String kontrakBulan = listPengemudiPutih.get(tblPengemudi.getSelectedRow()).getKontrakBulan().toString();
        String kontrakHari = listPengemudiPutih.get(tblPengemudi.getSelectedRow()).getKontrakHari().toString();
        int konfirmasi = JOptionPane.showConfirmDialog(this, "Anda Yakin Akan Menghapus Data Ini ? ?\nRincian : \nNRP =" + nrp + "\nNama = " + nama + "\nAlamat = " + alamat + "\nKota = " + kota + "\nTgl.Masuk = " + tglmasuk + "\nMasa Kontrak= " + kontrakBulan + " bulan" + kontrakHari + " hari", "Konfirmasi Hapus Data", option, JOptionPane.QUESTION_MESSAGE);
        if (konfirmasi == JOptionPane.OK_OPTION) {
            Main.getMasterService().delete(kemudiPutih);
            LoadDatabaseToTable();
            JOptionPane.showMessageDialog(this, "Data Berhasil Dihapus", "Pesan Sistem", JOptionPane.INFORMATION_MESSAGE);
            housekeeping();
            kemudiPutih = null;
        }
        if (konfirmasi == JOptionPane.CANCEL_OPTION) {
            housekeeping();
            kemudiPutih = null;
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
        kemudiPutih = null;
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
        if (txtRefNoLambung.getText().isEmpty() && txtNRP.getText().isEmpty() && txtNama.getText().isEmpty() && txtAlamat.getText().isEmpty() && txtKota.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Tidak boleh ada field yang kosong \nPeriksa Lagi !!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            txtRefNoLambung.setText("TIDAK BOLEH KOSONG");
            txtNRP.setText("TIDAK BOLEH KOSONG...");
            txtNama.setText("TIDAK BOLEH KOSONG...");
            txtAlamat.setText("TIDAK BOLEH KOSONG...");
            txtKota.setText("TIDAK BOLEH KOSONG...");
        } else if (txtRefNoLambung.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi No. Lambung !!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            txtRefNoLambung.requestFocus();
            txtRefNoLambung.setText("TIDAK BOLEH KOSONG...");
        } else if (txtNRP.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi NRP !!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            txtNRP.requestFocus();
            txtNRP.setText("TIDAK BOLEH KOSONG...");
        } else if (txtNama.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi Nama!!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            txtNama.requestFocus();
            txtNama.setText("TIDAK BOLEH KOSONG...");
        } else if (txtAlamat.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi Alamat !!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            txtAlamat.requestFocus();
            txtAlamat.setText("TIDAK BOLEH KOSONG...");
        } else if (txtKota.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi Kota!!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            txtKota.requestFocus();
            txtKota.setText("TIDAK BOLEH KOSONG...");
        } else if (tglMasuk.getDate() == null) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi Tanggal Masuk!!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            tglMasuk.requestFocus();
        } else if (spinHari.getValue().equals(0)) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi Masa Kontrak!!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            spinHari.requestFocus();
        }
    }

    private void enableForm(boolean e) {
        txtRefNoLambung.setEnabled(e);
        txtNRP.setEnabled(e);
        txtNama.setEnabled(e);
        txtAlamat.setEnabled(e);
        txtKota.setEnabled(e);
        tglMasuk.setEnabled(e);
        spinHari.setEnabled(e);
        spinBulan.setEnabled(e);
    }

    private void enableSearchEngine(boolean e) {
        txtCariPengemudi.setEnabled(e);
        cmbKriteria.setEnabled(e);
    }

    private void housekeeping() {
        txtRefNoLambung.setText("");
        txtNRP.setText("");
        txtNama.setText("");
        txtAlamat.setText("");
        txtKota.setText("");
        tglMasuk.setDate(null);
        spinHari.setValue(0);
        spinBulan.setValue(0);
    }

    private boolean validateForm() {
        if (!txtRefNoLambung.getText().isEmpty() && !txtRefNoLambung.getText().equals("TIDAK BOLEH KOSONG...")
                && !txtNRP.getText().isEmpty() && !txtNRP.getText().equals("TIDAK BOLEH KOSONG...")
                && !txtNama.getText().isEmpty() && !txtNama.getText().equals("TIDAK BOLEH KOSONG...")
                && !txtAlamat.getText().isEmpty() && !txtAlamat.getText().equals("TIDAK BOLEH KOSONG...")
                && !txtKota.getText().isEmpty() && !txtKota.getText().equals("TIDAK BOLEH KOSONG...")
                && tglMasuk.getDate() != null && !spinHari.getValue().equals(0)) {
            return true;
        }
        return false;
    }

    private void LoadFormToDatabase() {
        if (kemudiPutih == null) {
            kemudiPutih = new pengemudiPutih();
        }
        kemudiPutih.setKendPutih(kendPutih);
        kemudiPutih.setNrp(txtNRP.getText());
        kemudiPutih.setNama(txtNama.getText());
        kemudiPutih.setAlamat(txtAlamat.getText());
        kemudiPutih.setKota(txtKota.getText());
        kemudiPutih.setTglMasuk(tglMasuk.getDate());
        kemudiPutih.setKontrakHari(new Integer(spinHari.getValue().toString()));
        kemudiPutih.setKontrakBulan(new Integer(spinBulan.getValue().toString()));
    }

    private void LoadDatabaseToForm() {
        txtRefNoLambung.setText(kemudiPutih.getKendPutih().getNoLambung().toString());
        txtNRP.setText(kemudiPutih.getNrp());
        txtNama.setText(kemudiPutih.getNama());
        txtAlamat.setText(kemudiPutih.getAlamat());
        txtKota.setText(kemudiPutih.getKota());
        tglMasuk.setDate(kemudiPutih.getTglMasuk());
        spinHari.setValue(kemudiPutih.getKontrakHari());
        spinBulan.setValue(kemudiPutih.getKontrakBulan());
    }

    private void initColumnSize() {
        tblPengemudi.getColumnModel().getColumn(0).setMinWidth(80);
        tblPengemudi.getColumnModel().getColumn(0).setMaxWidth(80);
        tblPengemudi.getColumnModel().getColumn(1).setMinWidth(150);
        tblPengemudi.getColumnModel().getColumn(1).setMaxWidth(150);
        tblPengemudi.getColumnModel().getColumn(2).setMinWidth(250);
        tblPengemudi.getColumnModel().getColumn(2).setMaxWidth(250);
        tblPengemudi.getColumnModel().getColumn(3).setMinWidth(250);
        tblPengemudi.getColumnModel().getColumn(3).setMaxWidth(250);
        tblPengemudi.getColumnModel().getColumn(4).setMinWidth(100);
        tblPengemudi.getColumnModel().getColumn(4).setMaxWidth(100);
        tblPengemudi.getColumnModel().getColumn(5).setMinWidth(100);
        tblPengemudi.getColumnModel().getColumn(5).setMaxWidth(150);
        tblPengemudi.getColumnModel().getColumn(6).setMinWidth(150);
        tblPengemudi.getColumnModel().getColumn(6).setMaxWidth(150);
    }

    private void LoadDatabaseToTable() {
        listPengemudiPutih = Main.getMasterService().kemudiPutihRecord();
        tblPengemudi.setModel(new PengemudPutihiTableModel(listPengemudiPutih));
        int i = 0;
        for (; i < listPengemudiPutih.size(); i++) {
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
            kemudiPutih = listPengemudiPutih.get(tblPengemudi.getSelectedRow());
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
        tglMasuk = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        spinHari = new javax.swing.JSpinner();
        jLabel10 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtRefNoLambung = new javax.swing.JTextField();
        spinBulan = new javax.swing.JSpinner();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPengemudi = new javax.swing.JTable();
        buttonMaster = new mintory.master.buttonMaster();

        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Data Pengemudi - Armada Putih");

        jSplitPane1.setDividerLocation(-5);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jSplitPane2.setDividerLocation(450);

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

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13));
        jLabel11.setText("Pencarian Data Pengemudi");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel11)
                .addContainerGap(268, Short.MAX_VALUE))
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
                    .addComponent(txtCariPengemudi, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE))
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
                    .addComponent(txtCariPengemudi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbKriteria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(4, 4, 4))
        );

        jSplitPane3.setLeftComponent(jPanel1);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel3.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13));
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

        jLabel4.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13));
        jLabel4.setText("Nama");

        txtAlamat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtAlamatMouseClicked(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13));
        jLabel5.setText("Alamat");

        txtKota.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtKotaMouseClicked(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13));
        jLabel6.setText("Kota");

        jLabel7.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13));
        jLabel7.setText("Tgl Masuk");

        jLabel8.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13));
        jLabel8.setText("Masa Kontrak");

        jLabel10.setText("Hari");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13));
        jLabel12.setText("Input Data Pengemudi");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel12)
                .addContainerGap(301, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        jLabel9.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13));
        jLabel9.setText("No Lambung");

        txtRefNoLambung.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtRefNoLambungMouseClicked(evt);
            }
        });
        txtRefNoLambung.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRefNoLambungKeyReleased(evt);
            }
        });

        jLabel13.setText("Bulan");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7))
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAlamat, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                    .addComponent(txtKota, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                    .addComponent(tglMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(spinHari, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinBulan, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13))
                    .addComponent(txtRefNoLambung, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNRP, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {tglMasuk, txtNRP});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRefNoLambung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNRP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(7, 7, 7)
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
                .addGap(7, 7, 7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tglMasuk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spinHari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel10)
                    .addComponent(spinBulan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addContainerGap(201, Short.MAX_VALUE))
        );

        jSplitPane3.setRightComponent(jPanel2);

        jSplitPane2.setLeftComponent(jSplitPane3);

        jScrollPane2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        tblPengemudi.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tblPengemudi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "  Lambung", "  NRP", "  Nama", "  Alamat", "  Kota", "  Tgl Masuk", "  Masa Kontrak"
            }
        ));
        tblPengemudi.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane2.setViewportView(tblPengemudi);
        tblPengemudi.getColumnModel().getColumn(0).setMinWidth(80);
        tblPengemudi.getColumnModel().getColumn(0).setMaxWidth(80);
        tblPengemudi.getColumnModel().getColumn(1).setMinWidth(150);
        tblPengemudi.getColumnModel().getColumn(1).setMaxWidth(150);
        tblPengemudi.getColumnModel().getColumn(2).setMinWidth(250);
        tblPengemudi.getColumnModel().getColumn(2).setMaxWidth(250);
        tblPengemudi.getColumnModel().getColumn(3).setMinWidth(250);
        tblPengemudi.getColumnModel().getColumn(3).setMaxWidth(250);
        tblPengemudi.getColumnModel().getColumn(4).setMinWidth(100);
        tblPengemudi.getColumnModel().getColumn(4).setMaxWidth(100);
        tblPengemudi.getColumnModel().getColumn(5).setMinWidth(100);
        tblPengemudi.getColumnModel().getColumn(5).setMaxWidth(150);
        tblPengemudi.getColumnModel().getColumn(6).setMinWidth(150);
        tblPengemudi.getColumnModel().getColumn(6).setMaxWidth(150);

        jSplitPane2.setRightComponent(jScrollPane2);

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
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    int previousOption = -1;
    private void txtCariPengemudiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariPengemudiKeyReleased
        // TODO add your handling code here:
        String pilihan;
        int i = 0;
        boolean found = false;
        for (; i < listPengemudiPutih.size(); i++) {
            if (txtCariPengemudi.getText().length() == 0) {
                tblPengemudi.getSelectionModel().clearSelection();
                return;
            }
            if (cmbKriteria.getModel().getSelectedItem().equals("NRP")) {
                if (listPengemudiPutih.get(i).getNrp().startsWith(txtCariPengemudi.getText())) {
                    found = true;
                    break;
                    //                    //                } if (txtCariBarang.getText().startsWith(evt. {
                    //                    //                    found = false;
                    //                    //                    JOptionPane.showMessageDialog(this, "Parameter Search Characters Salah, Cek Lagi Data Anda ! !\nSeharusnya Yang Di Isi Adalah Kode Bukan Nama!!", "Terjadi Kesalahan", JOptionPane.INFORMATION_MESSAGE);
                    //                    //                    break;
                } else {
                    found = false;
                }
                //                if (evt.getKeyChar() == KeyEvent.VK_BACK_SPACE && txtCariBarang.getText().length() == 0) {
                //                    //                JOptionPane.showMessageDialog(this, "Pastikan Semua Data Yang Di Isikan Benar\nPeriksa Kembali Data Anda", "Error", JOptionPane.ERROR_MESSAGE);
                //                    tblBarang.getSelectionModel().clearSelection();
                //                    txtCariBarang.requestFocusInWindow();
                //                }
                //
            } else if (cmbKriteria.getModel().getSelectedItem().equals("NAMA")) {
                if (listPengemudiPutih.get(i).getNama().startsWith(txtCariPengemudi.getText())) {
                    found = true;
                    break;
                    //                    //                } if (txtCariBarang.getText().matches(listBarang.get(i).getKode())) {
                    //                    //                    found = false;
                    //                    //                    JOptionPane.showMessageDialog(this, "Parameter Search Characters Salah, Cek Lagi Data Anda ! !\nSeharusnya Yang Di Isi Adalah Nama Bukan Kode!!", "Terjadi Kesalahan", JOptionPane.INFORMATION_MESSAGE);
                    //                    //                    break;
                } else {
                    found = false;
                }
                //
                //                if (evt.getKeyChar() == KeyEvent.VK_BACK_SPACE && txtCariBarang.getText().length() == 0) {
                //                    //                JOptionPane.showMessageDialog(this, "Pastikan Semua Data Yang Di Isikan Benar\nPeriksa Kembali Data Anda", "Error", JOptionPane.ERROR_MESSAGE);
                //                    tblBarang.getSelectionModel().clearSelection();
                //                    txtCariBarang.requestFocusInWindow();
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
        } else {
            return;
        }
    }//GEN-LAST:event_txtNRPMouseClicked

    private void txtNamaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNamaMouseClicked
        // TODO add your handling code here:
        if (txtNama.getText().isEmpty() || txtNama.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtNama.setText("");
        } else {
            return;
        }
    }//GEN-LAST:event_txtNamaMouseClicked

    private void txtAlamatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAlamatMouseClicked
        // TODO add your handling code here:
        if (txtAlamat.getText().isEmpty() || txtAlamat.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtAlamat.setText("");
        } else {
            return;
        }
    }//GEN-LAST:event_txtAlamatMouseClicked

    private void txtKotaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtKotaMouseClicked
        // TODO add your handling code here:
        if (txtKota.getText().isEmpty() || txtKota.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtKota.setText("");
        } else {
            return;
        }
    }//GEN-LAST:event_txtKotaMouseClicked

    private void txtRefNoLambungMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtRefNoLambungMouseClicked
        // TODO add your handling code here:
        if (txtRefNoLambung.getText().isEmpty() || txtRefNoLambung.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtKota.setText("");
        } else {
            return;
        }
    }//GEN-LAST:event_txtRefNoLambungMouseClicked

    private void txtRefNoLambungKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRefNoLambungKeyReleased
        // TODO add your handling code here:
        int i = 0;
        listKendaraanPutih = Main.getMasterService().findKendaraanPutihByLambung(new Integer(txtRefNoLambung.getText()));
        kendPutih = new kendaraanPutih();
        if (!txtRefNoLambung.getText().isEmpty() && !listKendaraanPutih.isEmpty()) {
            for (kendaraanPutih kp : listKendaraanPutih) {
                kendPutih.setId(listKendaraanPutih.get(i).getId());
                kendPutih.setNoLambung(listKendaraanPutih.get(i).getNoLambung());
                kendPutih.setNoPolisi(listKendaraanPutih.get(i).getNoPolisi());
                kendPutih.setNoMesin(listKendaraanPutih.get(i).getNoMesin());
                kendPutih.setNoRangka(listKendaraanPutih.get(i).getNoRangka());
                kendPutih.setNoSTNK(listKendaraanPutih.get(i).getNoSTNK());
                kendPutih.setKeterangan(listKendaraanPutih.get(i).getKeterangan());
            }
        } else {
            JOptionPane.showMessageDialog(this, "No Lambung Belum Terdaftar", "Pesan Sistem", JOptionPane.ERROR_MESSAGE);
            txtRefNoLambung.requestFocus();
            return;
        }
    }//GEN-LAST:event_txtRefNoLambungKeyReleased
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private mintory.master.buttonMaster buttonMaster;
    private javax.swing.JComboBox cmbKriteria;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JSpinner spinBulan;
    private javax.swing.JSpinner spinHari;
    private javax.swing.JTable tblPengemudi;
    private com.toedter.calendar.JDateChooser tglMasuk;
    private javax.swing.JTextField txtAlamat;
    private javax.swing.JTextField txtCariPengemudi;
    private javax.swing.JTextField txtKota;
    private javax.swing.JTextField txtNRP;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtRefNoLambung;
    // End of variables declaration//GEN-END:variables

}

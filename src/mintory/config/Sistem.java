/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Sistem.java
 *
 * Created on Jun 18, 2012, 9:29:31 AM
 */
package mintory.config;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.joda.time.DateTime;
import mintory.Main;
import mintory.dialog.LookupTambahKomposisiSetoran;
import mintory.dialog.UserLoginUbahSistem;
import mintory.menu.MainMenu;
import mintory.model.SecurityConstants;
import mintory.model.codeGenerator;
import mintory.model.komposisiSetoran;
import mintory.model.sistem;
import mintory.tablemodel.KomposisiSetoranTableModel;

/**
 *
 * @author i1440ns
 */
public class Sistem extends javax.swing.JInternalFrame implements ListSelectionListener {

    private static Sistem sistemFrame;
    private MainMenu mainmenu;
    private sistem sistemModel;
    private codeGenerator genCode;
    private komposisiSetoran komposisiData;
    private List<codeGenerator> listCode;
    private List<sistem> listSistem = new ArrayList<sistem>();
    private List<komposisiSetoran> listKomposisi;
    public static boolean isEditOrDeleteFire = false;

    public static void inisialisasi() {
        sistemFrame = new Sistem();
    }

    public static void destroy() {
        sistemFrame.dispose();
        sistemFrame = null;
    }

    public static Sistem getSistem() {
        return sistemFrame;
    }

    /** Creates new form Sistem */
    public Sistem() {
        initComponents();
        sistemModel = Main.getSistemService().sistemRecord();
        LoadDatabaseToForm();
        LoadKomposisiDataToTable();
        if (sistemModel != null) {
            dateTglKerja.setDate(sistemModel.getTglKerja());
        } else {
            dateTglKerja.setDate(new Date());
        }
        dateTglKerja.setEnabled(false);
        btnUbahKomposisi.setEnabled(false);
        btnHapusKomposisi.setEnabled(false);
        tblKomposisi.getSelectionModel().addListSelectionListener(this);
    }

    private void LoadFormToDatabase() {
        if (sistemModel == null) {
            sistemModel = new sistem();
            sistemModel.setId(1);
        }
        sistemModel.setId(sistemModel.getId());
        sistemModel.setNamaPerusahaan(txtNama.getText());
        sistemModel.setAlamatPerusahaan(txtAlamat.getText());
        sistemModel.setKota(txtKota.getText());
        sistemModel.setTelp(txtTelp.getText());
        sistemModel.setEmail(txtEmail.getText());
        sistemModel.setTglKerja(dateTglKerja.getDate());
        if (!isEditOrDeleteFire) {
            for (int i=0; i<listKomposisi.size();i++) {
                listKomposisi.get(i).setIdSistem(sistemModel);
            }
//            listKomposisi.add(komposisiData);
            sistemModel.setListKomposisi(listKomposisi);
        }
    }

    private void LoadKomposisiDataToTable() {
        listKomposisi = Main.getSistemService().komposisiRecord();
        tblKomposisi.setModel(new KomposisiSetoranTableModel(listKomposisi));
        initColumnSize();
    }

    private void initColumnSize() {
        tblKomposisi.getColumnModel().getColumn(0).setMinWidth(50);
        tblKomposisi.getColumnModel().getColumn(0).setMaxWidth(50);
        tblKomposisi.getColumnModel().getColumn(1).setMinWidth(130);
        tblKomposisi.getColumnModel().getColumn(1).setMaxWidth(150);
        tblKomposisi.getColumnModel().getColumn(2).setMinWidth(150);
        tblKomposisi.getColumnModel().getColumn(2).setMaxWidth(200);
        tblKomposisi.getColumnModel().getColumn(3).setMinWidth(150);
        tblKomposisi.getColumnModel().getColumn(3).setMaxWidth(200);
    }

    private void LoadDatabaseToForm() {
        if (sistemModel == null) {
            enableForm(true);
            btnLock.setEnabled(false);
        } else if (sistemModel != null) {
            sistemModel = Main.getSistemService().sistemRecord();
            enableForm(false);
            btnLock.setEnabled(true);
            txtNama.setText(sistemModel.getNamaPerusahaan());
            txtAlamat.setText(sistemModel.getAlamatPerusahaan());
            txtKota.setText(sistemModel.getKota());
            txtTelp.setText(sistemModel.getTelp());
            txtEmail.setText(sistemModel.getEmail());
//            dateTglKerja.setDate(new DateTime(sistemModel.getTglKerja().getTime()).getDayOfMonth());
//            currentDate = new Date();
//            if (sistemModel.getTglKerja().compareTo(currentDate) == 0) {
            dateTglKerja.setDate(sistemModel.getTglKerja());
//                System.out.println("Result Date = " + sistemModel.getTglKerja().compareTo(currentDate));
//            } else if (sistemModel.getTglKerja().compareTo(currentDate) < 0) {
//                dateTglKerja.setDate(new DateTime(sistemModel.getTglKerja().getTime()).plusDays(1).toDate());
//                System.out.println("Result Date = " + new DateTime(sistemModel.getTglKerja().getTime()).plusDays(1).toDate());
//            } else if (sistemModel.getTglKerja().compareTo(currentDate) > 0) {
//                dateTglKerja.setDate(new DateTime(sistemModel.getTglKerja().getTime()).minusDays(1).toDate());
//                System.out.println("Result Date = " + new DateTime(sistemModel.getTglKerja().getTime()).plusDays(1).toDate());
//            } else {
//                JOptionPane.showMessageDialog(Main.getMainMenu(), "Tanggal Komputer & Tanggal Kerja Tidak Cocok\nHarap Set Tanggal Kerja");
//            }

            System.out.println("Total Hari dlm 1 bulan = " + new DateTime(sistemModel.getTglKerja().getTime()).dayOfMonth().getMaximumValue());
        }
    }

    private void enableForm(boolean e) {
        txtNama.setEnabled(e);
        txtAlamat.setEnabled(e);
        txtKota.setEnabled(e);
        txtTelp.setEnabled(e);
        txtEmail.setEnabled(e);
        btnAtur.setEnabled(e);
        btnBatal.setEnabled(e);
        tblKomposisi.setEnabled(e);
        btnTambahKomposisi.setEnabled(e);
    }

    private boolean validateForm() {
        if (!txtNama.getText().isEmpty() && !txtNama.getText().equalsIgnoreCase("Tidak Boleh Kosong...")
                && !txtAlamat.getText().isEmpty() && !txtAlamat.getText().equalsIgnoreCase("Tidak Boleh Kosong...")
                && !txtKota.getText().isEmpty() && !txtKota.getText().equalsIgnoreCase("Tidak Boleh Kosong...")
                && !txtTelp.getText().isEmpty() && !txtTelp.getText().equalsIgnoreCase("Tidak Boleh Kosong...")
                && !txtEmail.getText().isEmpty() && !txtEmail.getText().equalsIgnoreCase("Tidak Boleh Kosong...")
                && dateTglKerja.getDate() != null) {
            return true;
        } else if (txtNama.getText().isEmpty()
                && txtAlamat.getText().isEmpty()
                && txtKota.getText().isEmpty()
                && txtEmail.getText().isEmpty()
                && txtTelp.getText().isEmpty()) {
            txtNama.setText("Tidak Boleh Kosong...");
            txtNama.setForeground(Color.RED);
            txtAlamat.setText("Tidak Boleh Kosong...");
            txtAlamat.setForeground(Color.RED);
            txtKota.setText("Tidak Boleh Kosong...");
            txtKota.setForeground(Color.RED);
            txtTelp.setText("Tidak Boleh Kosong...");
            txtTelp.setForeground(Color.RED);
            txtEmail.setText("Tidak Boleh Kosong...");
            txtEmail.setForeground(Color.RED);
            return false;
        } else if (txtNama.getText().isEmpty()) {
            txtNama.setText("Tidak Boleh Kosong...");
            txtNama.setForeground(Color.RED);
            txtNama.requestFocus();
            return false;
        } else if (txtAlamat.getText().isEmpty()) {
            txtAlamat.setText("Tidak Boleh Kosong...");
            txtAlamat.setForeground(Color.RED);
            txtAlamat.requestFocus();
            return false;
        } else if (txtKota.getText().isEmpty()) {
            txtKota.setText("Tidak Boleh Kosong...");
            txtKota.setForeground(Color.RED);
            txtKota.requestFocus();
            return false;
        } else if (txtTelp.getText().isEmpty()) {
            txtTelp.setText("Tidak Boleh Kosong...");
            txtTelp.setForeground(Color.RED);
            txtTelp.requestFocus();
            return false;
        } else if (txtEmail.getText().isEmpty()) {
            txtEmail.setText("Tidak Boleh Kosong...");
            txtEmail.setForeground(Color.RED);
            txtEmail.requestFocus();
            return false;
        } else {
            return false;
        }
    }

//    public void restartApplication() {
//        try {
//            final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
//            final File currentJar = new File(Sistem.class.getProtectionDomain().getCodeSource().getLocation().toURI());
//            /* is it a jar file? */
//            if (!currentJar.getName().endsWith(".jar")) {
//                return;
//            }
//            final ArrayList<String> command = new ArrayList<String>();
//            command.add(javaBin);
//            command.add("-jar");
//            command.add(currentJar.getPath());
//            final ProcessBuilder builder = new ProcessBuilder(command);
//            builder.start();
//            System.exit(0);
//        } catch (IOException ex) {
//            Logger.getLogger(Sistem.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (URISyntaxException ex) {
//            Logger.getLogger(Sistem.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    public void valueChanged(ListSelectionEvent e) {
        if (tblKomposisi.getSelectedRow() >= 0) {
            komposisiData = listKomposisi.get(tblKomposisi.getSelectedRow());
            btnTambahKomposisi.setEnabled(false);
            btnUbahKomposisi.setEnabled(true);
            btnHapusKomposisi.setEnabled(true);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        txtAlamat = new javax.swing.JTextField();
        txtKota = new javax.swing.JTextField();
        txtTelp = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        dateTglKerja = new com.toedter.calendar.JDateChooser();
        btnAtur = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        btnLock = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblKomposisi = new javax.swing.JTable();
        btnTambahKomposisi = new javax.swing.JButton();
        btnUbahKomposisi = new javax.swing.JButton();
        btnHapusKomposisi = new javax.swing.JButton();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setTitle("Form Konfigurasi Sistem");

        jPanel1.setBackground(new java.awt.Color(9, 36, 251));

        jLabel1.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 15)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(254, 254, 254));
        jLabel1.setText("  Konfigurasi Sistem");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(342, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
        );

        jLabel2.setText("Nama Perusahaan");

        jLabel3.setText("Alamat Perusahaan");

        jLabel4.setText("No. Telepon");

        jLabel5.setText("Email");

        jLabel6.setText("Tanggal Kerja");

        jLabel9.setText("Kota");

        txtNama.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNamaMouseClicked(evt);
            }
        });

        txtAlamat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtAlamatMouseClicked(evt);
            }
        });

        txtKota.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtKotaMouseClicked(evt);
            }
        });

        txtTelp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTelpMouseClicked(evt);
            }
        });

        txtEmail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtEmailMouseClicked(evt);
            }
        });

        btnAtur.setText("Atur");
        btnAtur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAturActionPerformed(evt);
            }
        });

        btnBatal.setText("Batal");
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });

        btnLock.setText("Izinkan Mengubah");
        btnLock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLockActionPerformed(evt);
            }
        });

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Komposisi Setoran", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        tblKomposisi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Default", "  Nama Komposisi", "  Nilai Angsuran", "  Nilai Tabungan"
            }
        ));
        tblKomposisi.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblKomposisi.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblKomposisi);
        if (tblKomposisi.getColumnModel().getColumnCount() > 0) {
            tblKomposisi.getColumnModel().getColumn(0).setMinWidth(50);
            tblKomposisi.getColumnModel().getColumn(0).setMaxWidth(50);
            tblKomposisi.getColumnModel().getColumn(1).setMinWidth(100);
            tblKomposisi.getColumnModel().getColumn(1).setMaxWidth(150);
            tblKomposisi.getColumnModel().getColumn(2).setMinWidth(150);
            tblKomposisi.getColumnModel().getColumn(2).setMaxWidth(200);
            tblKomposisi.getColumnModel().getColumn(3).setMinWidth(150);
            tblKomposisi.getColumnModel().getColumn(3).setMaxWidth(200);
        }

        btnTambahKomposisi.setText("Tambah");
        btnTambahKomposisi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahKomposisiActionPerformed(evt);
            }
        });

        btnUbahKomposisi.setText("Ubah");
        btnUbahKomposisi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahKomposisiActionPerformed(evt);
            }
        });

        btnHapusKomposisi.setText("Hapus");
        btnHapusKomposisi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusKomposisiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnTambahKomposisi)
                            .addComponent(btnHapusKomposisi)
                            .addComponent(btnUbahKomposisi)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel9)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtAlamat, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
                            .addComponent(dateTglKerja, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                            .addComponent(txtEmail, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                            .addComponent(txtTelp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                            .addComponent(txtKota, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
                            .addComponent(txtNama, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE))))
                .addContainerGap(61, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(btnAtur, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBatal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLock)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAtur, btnBatal, btnLock});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {dateTglKerja, txtEmail, txtTelp});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnHapusKomposisi, btnTambahKomposisi, btnUbahKomposisi});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtKota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtTelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dateTglKerja, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 125, Short.MAX_VALUE)
                        .addComponent(btnTambahKomposisi, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUbahKomposisi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHapusKomposisi)
                        .addGap(29, 29, 29)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAtur)
                    .addComponent(btnBatal)
                    .addComponent(btnLock))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnHapusKomposisi, btnTambahKomposisi, btnUbahKomposisi});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLockActionPerformed
        // TODO add your handling code here:
        sistemModel = new UserLoginUbahSistem().showDialog(SecurityConstants.AUTHENTICATE_SYSTEM);
        if (sistemModel.getSecurityUser() != null && sistemModel.getSecurityUser().getChangeDataSistem()) {
            enableForm(true);
            btnLock.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "Untuk Merubah Data Sistem Silahkan Hubungi Admin", "Peringatan", JOptionPane.WARNING_MESSAGE);
            enableForm(false);
        }
    }//GEN-LAST:event_btnLockActionPerformed

    private void btnAturActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAturActionPerformed
        // TODO add your handling code here:
        if (sistemModel != null) {
            if (validateForm()) {
                LoadFormToDatabase();
                Main.getSistemService().save(sistemModel);
                JOptionPane.showMessageDialog(this, "Data Sistem Berhasil Dirubah", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                enableForm(false);
                destroy();
                Main.getMainMenu().setTanggalKerja(sistemModel.getTglKerja());
            } else {
                JOptionPane.showMessageDialog(this, "Data Sistem Gagal Disimpan\nCek Lagi Pengisian Data", "Peringatan", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        if (sistemModel == null) {
            if (validateForm()) {
                LoadFormToDatabase();
                Main.getSistemService().save(sistemModel);
                JOptionPane.showMessageDialog(this, "Data Sistem Berhasil Dirubah", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                enableForm(false);
                destroy();
                Main.getMainMenu().showMenu(true);
                Main.getMainMenu().setTanggalKerja(sistemModel.getTglKerja());
                JOptionPane.showMessageDialog(Main.getMainMenu(), "Sukses Memuat Ulang Sistem...", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Data Sistem Gagal Disimpan\nCek Lagi Pengisian Data", "Peringatan", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }//GEN-LAST:event_btnAturActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        // TODO add your handling code here:
        sistemModel = null;
        destroy();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void btnUbahKomposisiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahKomposisiActionPerformed
        // TODO add your handling code here:
        isEditOrDeleteFire = true;
        LookupTambahKomposisiSetoran.isUpdateMode = true;
        btnTambahKomposisi.setEnabled(true);
        btnUbahKomposisi.setEnabled(false);
        btnHapusKomposisi.setEnabled(false);
        komposisiData = new LookupTambahKomposisiSetoran().showDialog(komposisiData);
        if (komposisiData != null) {
            komposisiData.setId(komposisiData.getId());
            komposisiData.setIdSistem(sistemModel);
            Main.getSistemService().save(komposisiData);
            tblKomposisi.removeAll();
            LoadKomposisiDataToTable();
        } else {
            tblKomposisi.getSelectionModel().clearSelection();
            return;
        }
    }//GEN-LAST:event_btnUbahKomposisiActionPerformed

    private void btnTambahKomposisiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahKomposisiActionPerformed
        // TODO add your handling code here:.
        isEditOrDeleteFire = false;
        komposisiData = new LookupTambahKomposisiSetoran().showDialog();
        if (komposisiData != null) {
            listKomposisi.add(komposisiData);
            tblKomposisi.getSelectionModel().clearSelection();
            tblKomposisi.setModel(new KomposisiSetoranTableModel(listKomposisi));
            tblKomposisi.setEnabled(true);
            initColumnSize();
        } else {
            tblKomposisi.setEnabled(true);
            return;
        }
    }//GEN-LAST:event_btnTambahKomposisiActionPerformed

    private void btnHapusKomposisiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusKomposisiActionPerformed
        // TODO add your handling code here:
        btnTambahKomposisi.setEnabled(true);
        btnUbahKomposisi.setEnabled(false);
        btnHapusKomposisi.setEnabled(false);
        komposisiData = listKomposisi.get(tblKomposisi.getSelectedRow());
        Main.getSistemService().delete(komposisiData);
        LoadKomposisiDataToTable();
        isEditOrDeleteFire = true;
    }//GEN-LAST:event_btnHapusKomposisiActionPerformed

    private void txtNamaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNamaMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 1) {
            if (txtNama.getText().equalsIgnoreCase("Tidak Boleh Kosong...")) {
                txtNama.setText("");
                txtNama.setForeground(Color.BLACK);
            }
        }
    }//GEN-LAST:event_txtNamaMouseClicked

    private void txtAlamatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAlamatMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 1) {
            if (txtAlamat.getText().equalsIgnoreCase("Tidak Boleh Kosong...")) {
                txtAlamat.setText("");
                txtAlamat.setForeground(Color.BLACK);
            }
        }
    }//GEN-LAST:event_txtAlamatMouseClicked

    private void txtKotaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtKotaMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 1) {
            if (txtKota.getText().equalsIgnoreCase("Tidak Boleh Kosong...")) {
                txtKota.setText("");
                txtKota.setForeground(Color.BLACK);
            }
        }
    }//GEN-LAST:event_txtKotaMouseClicked

    private void txtTelpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTelpMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 1) {
            if (txtTelp.getText().equalsIgnoreCase("Tidak Boleh Kosong...")) {
                txtTelp.setText("");
                txtTelp.setForeground(Color.BLACK);
            }
        }
    }//GEN-LAST:event_txtTelpMouseClicked

    private void txtEmailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtEmailMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 1) {
            if (txtEmail.getText().equalsIgnoreCase("Tidak Boleh Kosong...")) {
                txtEmail.setText("");
                txtEmail.setForeground(Color.BLACK);
            }
        }
    }//GEN-LAST:event_txtEmailMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAtur;
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnHapusKomposisi;
    private javax.swing.JButton btnLock;
    private javax.swing.JButton btnTambahKomposisi;
    private javax.swing.JButton btnUbahKomposisi;
    private com.toedter.calendar.JDateChooser dateTglKerja;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblKomposisi;
    private javax.swing.JTextField txtAlamat;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtKota;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtTelp;
    // End of variables declaration//GEN-END:variables
}

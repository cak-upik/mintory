/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainMenu.java
 *
 * Created on Mar 27, 2012, 8:01:24 AM
 */
package mintory.menu;

import mintory.config.Sistem;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import org.springframework.remoting.RemoteConnectFailureException;
import mintory.Main;
import mintory.config.RunningNumber;
import mintory.config.UbahSaldoAwal;
import mintory.config.UbahSaldoAwalPth;
import mintory.config.UserLogin;
import mintory.dialog.LookupSetTanggal;
import mintory.dialog.UserLoginUbahSistem;
import mintory.master.MasterBarang;
import mintory.master.MasterPengemudi;
import mintory.master.MasterSupplier;
import mintory.model.SecurityConstants;
import mintory.model.SecurityUser;
import mintory.model.codeGenerator;
import mintory.model.sistem;
import mintory.transaksi.ClosingBulanan;
import mintory.transaksi.SaldoAwal;

/**
 *
 * @author i1440ns
 */
public class MainMenu extends javax.swing.JFrame {

    private sistem sys;
    private List<codeGenerator> ListGenCode;
    private UserLoginUbahSistem userLogin;
    private MainMenu mainmenu;
    private Dimension ukuranLayar;
    private Dimension ukuranFrame;
    private static final Integer OPTION = JOptionPane.OK_CANCEL_OPTION;
    private SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
    private Date current_date;
    public static boolean isSuperAdmin = false;

    /** Creates new form MainMenu */
    public JDesktopPane getDesktopPane() {
        return desktoPane;
    }

    public MainMenu() {
        initComponents();
        initButtonHotkeyFunction();
        sys = Main.getSistemService().sistemRecord();
        validasiSistem();
    }

    private void initButtonHotkeyFunction() {
        KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        kfm.addKeyEventDispatcher(new KeyEventDispatcher() {

            public boolean dispatchKeyEvent(KeyEvent ke) {
                /*  Ganti Tanggal  */
                if (ke.getKeyCode() == KeyEvent.VK_F12 && ke.getID() == KeyEvent.KEY_PRESSED) {
                    new LookupSetTanggal().showDialog();
                    System.out.println("F12 pressed and GO Setting Tanggal");
                } else {
                    return false;
                }
                return false;
            }
        });
    }

    private void validasiSistem() {
//        if (sys == null) {
//            Sistem.inisialisasi();
            showMenu(true);
            lblUser.setText("Lakukan Setting User Pada Konfigurasi Sistem");
            lblTanggalKerja.setText("Lakukan Setting Tanggal Pada Konfigurasi Sistem");
//            int konfirmasi = JOptionPane.showConfirmDialog(this, "Sistem Belum Di Konfigurasi\nLakukan Konfigurasi Sekarang ? ?", "Pesan Sistem", OPTION, JOptionPane.QUESTION_MESSAGE);
//            if (konfirmasi == JOptionPane.OK_OPTION) {
//                try {
//                    desktoPane.add(Sistem.getSistem());
//                    ukuranLayar = desktoPane.getSize();
//                    ukuranFrame = Sistem.getSistem().getSize();
//                    Sistem.getSistem().setVisible(true);
//                    //                Sistem.getSistem().setLocation((ukuranLayar.width - ukuranFrame.width) / 16 + 380, (ukuranLayar.height - ukuranFrame.height) / 16 + 100);
//                    Sistem.getSistem().setLocation((ukuranLayar.width - ukuranFrame.width) / 2, (ukuranLayar.height - ukuranFrame.height) / 2);
//                    Sistem.getSistem().setSelected(true);
//                } catch (PropertyVetoException ex) {
//                    Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//            if (konfirmasi == JOptionPane.CANCEL_OPTION) {
//                System.gc();
//                System.exit(0);
//            }
//        } else if (sys != null) {
//            MainLoginState();
//            setTanggalKerja(sys.getTglKerja());
//        }
    }

    public void showMenu(boolean e) {
        mnuMaster.setVisible(e);
        mnuTransaksi.setVisible(e);
        mnuUtility.setVisible(e);
        mnuLaporan.setVisible(e);
        mnuKonfigurasi.setVisible(e);
        mnuBantuan.setVisible(e);
    }

    private void applyPermission(SecurityUser su) {
        if (su.getViewAllTransactionMenu()) {
            showMenu(true);
        } else {
            mnuMaster.setVisible(true);
            mnuTransaksi.setVisible(true);
            mnuUtility.setVisible(false);
            mnuLaporan.setVisible(true);
            mnuKonfigurasi.setVisible(false);
            mnuBantuan.setVisible(true);
        }
        if (su.getOpenClosedTrans()) {
            isSuperAdmin = true;
        } else {
            isSuperAdmin = false;
        }
        if (su.getDoClosing()) {
            mnuUtility.setVisible(true);
            mnuItemClosingBulan.setVisible(true);
        } else {
            mnuUtility.setVisible(false);
            mnuItemClosingBulan.setVisible(false);
        }
        if (su.getAddUser()) {
            mnuKonfigurasi.setVisible(true);
            mnuItemUserRole.setVisible(true);
        } else {
            mnuKonfigurasi.setVisible(true);
            mnuItemUserRole.setVisible(false);
        }
        if (su.getChangeKomposisi()) {
            mnuUtility.setVisible(true);
            mnuItemSistem.setVisible(true);
        } else {
            mnuUtility.setVisible(false);
            mnuItemSistem.setVisible(false);
        }
        if (su.getChangeBonus()) {
            mnuUtility.setVisible(true);
        } else {
            mnuUtility.setVisible(false);
        }
        if (su.getChangeDataSistem()) {
            mnuItemSistem.setVisible(true);
        } else {
            mnuItemSistem.setVisible(false);
        }
        if (su.getDoSaldoAwal()) {
            mnuUtility.setVisible(true);
            mnuItemSaldoAwal.setVisible(true);
        } else {
            mnuUtility.setVisible(false);
            mnuItemSaldoAwal.setVisible(false);
        }
    }

    public void IntialSystemState() {
        showMenu(false);
        mnuItemLogin.setVisible(false);
        mnuItemLogout.setVisible(false);
    }

    public void MainLoginState() {
        showMenu(false);
        mnuItemLogin.setVisible(true);
        mnuItemLogout.setVisible(false);
        lblUser.setText("");
    }

    public void LoginSuksesState(SecurityUser s) {
//        showMenu(true);
        applyPermission(s);
        mnuItemLogin.setVisible(false);
        mnuItemLogout.setVisible(true);
    }

    public void setUser(String user) {
        lblUser.setText(user);
    }

    public void setTanggalKerja(Date date) {
        current_date = new Date();
        ListGenCode = Main.getSistemService().codeRecord();
        if (sys != null) {
            if (date.compareTo(current_date) == 0) {
                lblTanggalKerja.setText(formatDate.format(date));
            } else if (LookupSetTanggal.isManualDateChanged) {
                lblTanggalKerja.setText(formatDate.format(date));
                sys.setTglKerja(date);
                Main.getSistemService().save(sys);
                updateRunningNumber(date);
            } else if (ClosingBulanan.closingState) {
                lblTanggalKerja.setText(formatDate.format(date));
                sys.setTglKerja(date);
                Main.getSistemService().save(sys);
                updateRunningNumber(date);
            } else {
                sys.setTglKerja(current_date);
                Main.getSistemService().save(sys);
                lblTanggalKerja.setText(formatDate.format(sys.getTglKerja()));
                updateRunningNumber(sys.getTglKerja());
            }
        } else {
            lblTanggalKerja.setText(formatDate.format(date));
        }
    }

    private void updateRunningNumber(Date date) {
        if (!ListGenCode.isEmpty()) {
            for (codeGenerator cg : ListGenCode) {
                cg.setTanggal(date);
                Main.getSistemService().save(cg);
            }
        } else {
            JOptionPane.showMessageDialog(Main.getMainMenu(), "Running Number Kosong,\nHarap Periksa Lagi Konfigurasi Kode", "Pesan Sistem", JOptionPane.ERROR_MESSAGE);
            return;
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

        desktoPane = new javax.swing.JDesktopPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblTanggalKerja = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        mnuFile = new javax.swing.JMenu();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        mnuKonfigurasi = new javax.swing.JMenu();
        mnuItemSistem = new javax.swing.JMenuItem();
        mnuItemKode = new javax.swing.JMenuItem();
        mnuItemUserRole = new javax.swing.JMenuItem();
        mnuItemUbahSaldo = new javax.swing.JMenuItem();
        mnuItemKonfigSAPth = new javax.swing.JMenuItem();
        mnuItemLogin = new javax.swing.JMenuItem();
        mnuItemLogout = new javax.swing.JMenuItem();
        mnuItemExit = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        mnuMaster = new javax.swing.JMenu();
        mnuItemMasterBarang = new javax.swing.JMenuItem();
        mnuItemMasterSupplier = new javax.swing.JMenuItem();
        mnuItemMasterPengemudi = new javax.swing.JMenuItem();
        mnuTransaksi = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        mnuLaporan = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        mnuUtility = new javax.swing.JMenu();
        jSeparator11 = new javax.swing.JPopupMenu.Separator();
        mnuItemClosingBulan = new javax.swing.JMenuItem();
        mnuItemSaldoAwal = new javax.swing.JMenuItem();
        jSeparator12 = new javax.swing.JPopupMenu.Separator();
        mnuBantuan = new javax.swing.JMenu();
        jMenuItem17 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistem Informasi Inventaris Taxi Mandala - Mintory");

        jLabel1.setText("User = ");

        jLabel2.setText("Tanggal Kerja = ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblUser, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTanggalKerja, javax.swing.GroupLayout.DEFAULT_SIZE, 795, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblUser, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(lblTanggalKerja, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        mnuFile.setText("File");
        mnuFile.add(jSeparator4);

        mnuKonfigurasi.setText("Konfigurasi");

        mnuItemSistem.setText("Konfigurasi Sistem");
        mnuItemSistem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemSistemActionPerformed(evt);
            }
        });
        mnuKonfigurasi.add(mnuItemSistem);

        mnuItemKode.setText("Konfigurasi Nomor Kode");
        mnuItemKode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemKodeActionPerformed(evt);
            }
        });
        mnuKonfigurasi.add(mnuItemKode);

        mnuItemUserRole.setText("Konfigurasi User & Role");
        mnuItemUserRole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemUserRoleActionPerformed(evt);
            }
        });
        mnuKonfigurasi.add(mnuItemUserRole);

        mnuItemUbahSaldo.setText("Konfigurasi Saldo Awal");
        mnuItemUbahSaldo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemUbahSaldoActionPerformed(evt);
            }
        });
        mnuKonfigurasi.add(mnuItemUbahSaldo);

        mnuItemKonfigSAPth.setText("Konfigurasi Saldo Awal Putih");
        mnuItemKonfigSAPth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemKonfigSAPthActionPerformed(evt);
            }
        });
        mnuKonfigurasi.add(mnuItemKonfigSAPth);

        mnuFile.add(mnuKonfigurasi);

        mnuItemLogin.setText("Login");
        mnuItemLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemLoginActionPerformed(evt);
            }
        });
        mnuFile.add(mnuItemLogin);

        mnuItemLogout.setText("Logout");
        mnuItemLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemLogoutActionPerformed(evt);
            }
        });
        mnuFile.add(mnuItemLogout);

        mnuItemExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.ALT_MASK));
        mnuItemExit.setText("Exit");
        mnuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemExitActionPerformed(evt);
            }
        });
        mnuFile.add(mnuItemExit);
        mnuFile.add(jSeparator3);

        jMenuBar1.add(mnuFile);

        mnuMaster.setText("Master");

        mnuItemMasterBarang.setText("Master Barang");
        mnuItemMasterBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemMasterBarangActionPerformed(evt);
            }
        });
        mnuMaster.add(mnuItemMasterBarang);

        mnuItemMasterSupplier.setText("Master Supplier");
        mnuItemMasterSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemMasterSupplierActionPerformed(evt);
            }
        });
        mnuMaster.add(mnuItemMasterSupplier);

        mnuItemMasterPengemudi.setText("Master Pengemudi");
        mnuItemMasterPengemudi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemMasterPengemudiActionPerformed(evt);
            }
        });
        mnuMaster.add(mnuItemMasterPengemudi);

        jMenuBar1.add(mnuMaster);

        mnuTransaksi.setText("Transaksi");

        jMenuItem4.setText("Transaksi Pemakaian");
        mnuTransaksi.add(jMenuItem4);

        jMenuItem7.setText("Transaksi Pembelian");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        mnuTransaksi.add(jMenuItem7);

        jMenuBar1.add(mnuTransaksi);

        mnuLaporan.setText("Laporan");

        jMenuItem8.setText("Rekapitulasi Saldo Barang");
        mnuLaporan.add(jMenuItem8);

        jMenuBar1.add(mnuLaporan);

        mnuUtility.setText("Utility");
        mnuUtility.add(jSeparator11);

        mnuItemClosingBulan.setText("Proses Akhir Bulan");
        mnuItemClosingBulan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemClosingBulanActionPerformed(evt);
            }
        });
        mnuUtility.add(mnuItemClosingBulan);

        mnuItemSaldoAwal.setText("Input Saldo Awal");
        mnuItemSaldoAwal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemSaldoAwalActionPerformed(evt);
            }
        });
        mnuUtility.add(mnuItemSaldoAwal);
        mnuUtility.add(jSeparator12);

        jMenuBar1.add(mnuUtility);

        mnuBantuan.setText("Bantuan");

        jMenuItem17.setText("Tentang");
        mnuBantuan.add(jMenuItem17);

        jMenuBar1.add(mnuBantuan);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(desktoPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1097, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(desktoPane, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemExitActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_mnuItemExitActionPerformed

    private void mnuItemSistemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemSistemActionPerformed
        // TODO add your handling code here:
        try {
            if (Sistem.getSistem() == null || Sistem.getSistem().isClosed()) {
                Sistem.inisialisasi();
                desktoPane.add(Sistem.getSistem());
                ukuranLayar = desktoPane.getSize();
                ukuranFrame = Sistem.getSistem().getSize();
            } else {
                Sistem.getSistem().toFront();
                Sistem.getSistem().show();
            }
            Sistem.getSistem().setVisible(true);
            Sistem.getSistem().setLocation((ukuranLayar.width - ukuranFrame.width) / 2, (ukuranLayar.height - ukuranFrame.height) / 2);
            Sistem.getSistem().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
}//GEN-LAST:event_mnuItemSistemActionPerformed

    private void mnuItemKodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemKodeActionPerformed
        // TODO add your handling code here:
        try {
            if (RunningNumber.getRuningNumber() == null || RunningNumber.getRuningNumber().isClosed()) {
                RunningNumber.inisialisasi();
                desktoPane.add(RunningNumber.getRuningNumber());
                ukuranLayar = desktoPane.getSize();
                ukuranFrame = RunningNumber.getRuningNumber().getSize();
            } else {
                RunningNumber.getRuningNumber().toFront();
                RunningNumber.getRuningNumber().show();
            }
            RunningNumber.getRuningNumber().setVisible(true);
            RunningNumber.getRuningNumber().setLocation((ukuranLayar.width - ukuranFrame.width) / 2, (ukuranLayar.height - ukuranFrame.height) / 2);
            RunningNumber.getRuningNumber().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuItemKodeActionPerformed

    private void mnuItemClosingBulanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemClosingBulanActionPerformed
        // TODO add your handling code here:
        try {
            if (ClosingBulanan.getFormClosing() == null || ClosingBulanan.getFormClosing().isClosed()) {
                ClosingBulanan.inisialisasi();
                desktoPane.add(ClosingBulanan.getFormClosing());
                ukuranLayar = desktoPane.getSize();
                ukuranFrame = ClosingBulanan.getFormClosing().getSize();
            } else {
                ClosingBulanan.getFormClosing().toFront();
                ClosingBulanan.getFormClosing().show();
            }
            ClosingBulanan.getFormClosing().setVisible(true);
            ClosingBulanan.getFormClosing().setLocation((ukuranLayar.width - ukuranFrame.width) / 2, (ukuranLayar.height - ukuranFrame.height) / 4);
            ClosingBulanan.getFormClosing().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuItemClosingBulanActionPerformed

    private void mnuItemSaldoAwalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemSaldoAwalActionPerformed
        // TODO add your handling code here:
        try {
            if (SaldoAwal.getFormSaldoAwal() == null || SaldoAwal.getFormSaldoAwal().isClosed()) {
                SaldoAwal.inisialisasi();
                desktoPane.add(SaldoAwal.getFormSaldoAwal());
                ukuranLayar = desktoPane.getSize();
                ukuranFrame = SaldoAwal.getFormSaldoAwal().getSize();
            } else {
                SaldoAwal.getFormSaldoAwal().toFront();
                SaldoAwal.getFormSaldoAwal().show();
            }
            SaldoAwal.getFormSaldoAwal().setVisible(true);
            SaldoAwal.getFormSaldoAwal().setLocation((ukuranLayar.width - ukuranFrame.width) / 2, (ukuranLayar.height - ukuranFrame.height) / 4);
            SaldoAwal.getFormSaldoAwal().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuItemSaldoAwalActionPerformed

    private void mnuItemUserRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemUserRoleActionPerformed
        // TODO add your handling code here:
        try {
            if (UserLogin.getUserLogin() == null || UserLogin.getUserLogin().isClosed()) {
                UserLogin.inisialisasi();
                desktoPane.add(UserLogin.getUserLogin());
            } else {
                UserLogin.getUserLogin().toFront();
                UserLogin.getUserLogin().show();
            }
            UserLogin.getUserLogin().setVisible(true);
            UserLogin.getUserLogin().setSize(desktoPane.getSize());
            UserLogin.getUserLogin().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuItemUserRoleActionPerformed

    private void mnuItemLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemLogoutActionPerformed
        // TODO add your handling code here:
        MainLoginState();
        new UserLoginUbahSistem().showMainLoginDialog(SecurityConstants.LOGIN_SYSTEM);
    }//GEN-LAST:event_mnuItemLogoutActionPerformed

    private void mnuItemLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemLoginActionPerformed
        // TODO add your handling code here:
        new UserLoginUbahSistem().showMainLoginDialog(SecurityConstants.LOGIN_SYSTEM);
    }//GEN-LAST:event_mnuItemLoginActionPerformed

    private void mnuItemUbahSaldoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemUbahSaldoActionPerformed
        // TODO add your handling code here:
        try {
            if (UbahSaldoAwal.getUbahSaldoAwal() == null || UbahSaldoAwal.getUbahSaldoAwal().isClosed()) {
                UbahSaldoAwal.inisialisasi();
                desktoPane.add(UbahSaldoAwal.getUbahSaldoAwal());
            } else {
                UbahSaldoAwal.getUbahSaldoAwal().toFront();
                UbahSaldoAwal.getUbahSaldoAwal().show();
            }
            UbahSaldoAwal.getUbahSaldoAwal().setVisible(true);
            UbahSaldoAwal.getUbahSaldoAwal().setSize(desktoPane.getSize());
            UbahSaldoAwal.getUbahSaldoAwal().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuItemUbahSaldoActionPerformed

    private void mnuItemKonfigSAPthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemKonfigSAPthActionPerformed
        // TODO add your handling code here:
        try {
            if (UbahSaldoAwalPth.getUbahSaldoAwalPutih() == null || UbahSaldoAwalPth.getUbahSaldoAwalPutih().isClosed()) {
                UbahSaldoAwalPth.inisialisasi();
                desktoPane.add(UbahSaldoAwalPth.getUbahSaldoAwalPutih());
            } else {
                UbahSaldoAwalPth.getUbahSaldoAwalPutih().toFront();
                UbahSaldoAwalPth.getUbahSaldoAwalPutih().show();
            }
            UbahSaldoAwalPth.getUbahSaldoAwalPutih().setVisible(true);
            UbahSaldoAwalPth.getUbahSaldoAwalPutih().setSize(desktoPane.getSize());
            UbahSaldoAwalPth.getUbahSaldoAwalPutih().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuItemKonfigSAPthActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void mnuItemMasterBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemMasterBarangActionPerformed
        // TODO add your handling code here:
        try {
            if (MasterBarang.getMasterBarang() == null || MasterBarang.getMasterBarang().isClosed()) {
                MasterBarang.inisialisasi();
                desktoPane.add(MasterBarang.getMasterBarang());
            } else {
                MasterBarang.getMasterBarang().toFront();
                MasterBarang.getMasterBarang().show();
            }
            MasterBarang.getMasterBarang().setVisible(true);
            MasterBarang.getMasterBarang().setSize(desktoPane.getSize());
            MasterBarang.getMasterBarang().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuItemMasterBarangActionPerformed

    private void mnuItemMasterSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemMasterSupplierActionPerformed
        // TODO add your handling code here:
        try {
            if (MasterSupplier.getMasterSupplier() == null || MasterSupplier.getMasterSupplier().isClosed()) {
                MasterSupplier.inisialisasi();
                desktoPane.add(MasterSupplier.getMasterSupplier());
            } else {
                MasterSupplier.getMasterSupplier().toFront();
                MasterSupplier.getMasterSupplier().show();
            }
            MasterSupplier.getMasterSupplier().setVisible(true);
            MasterSupplier.getMasterSupplier().setSize(desktoPane.getSize());
            MasterSupplier.getMasterSupplier().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuItemMasterSupplierActionPerformed

    private void mnuItemMasterPengemudiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemMasterPengemudiActionPerformed
        // TODO add your handling code here:
        try {
            if (MasterPengemudi.getMasterPengemudi() == null || MasterPengemudi.getMasterPengemudi().isClosed()) {
                MasterPengemudi.inisialisasi();
                desktoPane.add(MasterPengemudi.getMasterPengemudi());
            } else {
                MasterPengemudi.getMasterPengemudi().toFront();
                MasterPengemudi.getMasterPengemudi().show();
            }
            MasterPengemudi.getMasterPengemudi().setVisible(true);
            MasterPengemudi.getMasterPengemudi().setSize(desktoPane.getSize());
            MasterPengemudi.getMasterPengemudi().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuItemMasterPengemudiActionPerformed
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane desktoPane;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu.Separator jSeparator11;
    private javax.swing.JPopupMenu.Separator jSeparator12;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JLabel lblTanggalKerja;
    private javax.swing.JLabel lblUser;
    private javax.swing.JMenu mnuBantuan;
    private javax.swing.JMenu mnuFile;
    private javax.swing.JMenuItem mnuItemClosingBulan;
    private javax.swing.JMenuItem mnuItemExit;
    private javax.swing.JMenuItem mnuItemKode;
    private javax.swing.JMenuItem mnuItemKonfigSAPth;
    private javax.swing.JMenuItem mnuItemLogin;
    private javax.swing.JMenuItem mnuItemLogout;
    private javax.swing.JMenuItem mnuItemMasterBarang;
    private javax.swing.JMenuItem mnuItemMasterPengemudi;
    private javax.swing.JMenuItem mnuItemMasterSupplier;
    private javax.swing.JMenuItem mnuItemSaldoAwal;
    private javax.swing.JMenuItem mnuItemSistem;
    private javax.swing.JMenuItem mnuItemUbahSaldo;
    private javax.swing.JMenuItem mnuItemUserRole;
    private javax.swing.JMenu mnuKonfigurasi;
    private javax.swing.JMenu mnuLaporan;
    private javax.swing.JMenu mnuMaster;
    private javax.swing.JMenu mnuTransaksi;
    private javax.swing.JMenu mnuUtility;
    // End of variables declaration//GEN-END:variables
}

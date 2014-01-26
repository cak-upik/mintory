/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainMenu.java
 *
 * Created on Mar 27, 2012, 8:01:24 AM
 */
package paperman.menu;

import paperman.config.Sistem;
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
import paperman.Main;
import paperman.config.RunningNumber;
import paperman.config.UbahSaldoAwal;
import paperman.config.UbahSaldoAwalPth;
import paperman.config.UserLogin;
import paperman.dialog.LookupSetTanggal;
import paperman.dialog.UserLoginUbahSistem;
import paperman.master.MasterKendaraan;
import paperman.master.MasterKendaraanPutih;
import paperman.master.MasterPengemudi;
import paperman.master.MasterPengemudiPutih;
import paperman.model.SecurityConstants;
import paperman.model.SecurityUser;
import paperman.model.codeGenerator;
import paperman.model.sistem;
import paperman.report.ui.LaporanDataLambungBulanan;
import paperman.report.ui.LaporanDataLambungBulananPth;
import paperman.report.ui.LaporanKartuBayar;
import paperman.report.ui.LaporanKartuBayarPth;
import paperman.report.ui.LaporanTransaksiBulanan;
import paperman.report.ui.LaporanTransaksiBulananPth;
import paperman.report.ui.LaporanTransaksiHarian;
import paperman.report.ui.LaporanTransaksiHarianPth;
import paperman.transaksi.ClosingBulanan;
import paperman.transaksi.ClosingTahunan;
import paperman.transaksi.InputBonusBulanan;
import paperman.transaksi.ManajemenPiutang;
import paperman.transaksi.ManajemenPiutangPutih;
import paperman.transaksi.SaldoAwal;
import paperman.transaksi.TransaksiSetoran;
import paperman.transaksi.TransaksiSetoranPutih;

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
        jMenuItem6.setArmed(false);
        jMenuItem5.setArmed(false);
        lblFastTransaksi.setVisible(true);
        lblFastSaldoAwal.setVisible(true);
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
        if (sys == null) {
            Sistem.inisialisasi();
            showMenu(false);
            lblUser.setText("Lakukan Setting User Pada Konfigurasi Sistem");
            lblTanggalKerja.setText("Lakukan Setting Tanggal Pada Konfigurasi Sistem");
            int konfirmasi = JOptionPane.showConfirmDialog(this, "Sistem Belum Di Konfigurasi\nLakukan Konfigurasi Sekarang ? ?", "Pesan Sistem", OPTION, JOptionPane.QUESTION_MESSAGE);
            if (konfirmasi == JOptionPane.OK_OPTION) {
                try {
                    desktoPane.add(Sistem.getSistem());
                    ukuranLayar = desktoPane.getSize();
                    ukuranFrame = Sistem.getSistem().getSize();
                    Sistem.getSistem().setVisible(true);
                    //                Sistem.getSistem().setLocation((ukuranLayar.width - ukuranFrame.width) / 16 + 380, (ukuranLayar.height - ukuranFrame.height) / 16 + 100);
                    Sistem.getSistem().setLocation((ukuranLayar.width - ukuranFrame.width) / 2, (ukuranLayar.height - ukuranFrame.height) / 2);
                    Sistem.getSistem().setSelected(true);
                } catch (PropertyVetoException ex) {
                    Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (konfirmasi == JOptionPane.CANCEL_OPTION) {
                System.gc();
                System.exit(0);
            }
        } else if (sys != null) {
            MainLoginState();
            setTanggalKerja(sys.getTglKerja());
        }
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
            mnuItemClosingTahunan.setVisible(true);
        } else {
            mnuUtility.setVisible(false);
            mnuItemClosingBulan.setVisible(false);
            mnuItemClosingTahunan.setVisible(false);
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
            mnuItemBonusBulanan.setVisible(true);
        } else {
            mnuUtility.setVisible(false);
            mnuItemBonusBulanan.setVisible(false);
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
        lblFastTransaksi = new javax.swing.JLabel();
        lblFastSaldoAwal = new javax.swing.JLabel();
        lblFastPiutang = new javax.swing.JLabel();
        lblFastMasterKendaraan = new javax.swing.JLabel();
        lblFastKartu = new javax.swing.JLabel();
        lblFastMasterPengemudi = new javax.swing.JLabel();
        lblBG = new javax.swing.JLabel();
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
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        mnuLimoPutih = new javax.swing.JMenu();
        mnuItemKendaraanPutih = new javax.swing.JMenuItem();
        mnuItemPengemudiPutih = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        mnuLimoBiru = new javax.swing.JMenu();
        mnuItemKendaraanBiru = new javax.swing.JMenuItem();
        mnuItemPengemudiBiru = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        mnuTransaksi = new javax.swing.JMenu();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        mnuTransaksiPutih = new javax.swing.JMenu();
        mnuItemTrSetoranPutih = new javax.swing.JMenuItem();
        mnuItemManajemenPiutangPutih = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
        mnuTransaksiBiru = new javax.swing.JMenu();
        mnuItemTransaksiSetoranBiru = new javax.swing.JMenuItem();
        mnuItemManajemenPiutangBiru = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JPopupMenu.Separator();
        mnuLaporan = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        mnuItemLaporanTransaksiHarianPutih = new javax.swing.JMenuItem();
        mnuItemRekapBulananPutih = new javax.swing.JMenuItem();
        mnuItemKartuBayarPutih = new javax.swing.JMenuItem();
        mnuItemLaporanDataKendaraanPutih = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        mnuItemLaporanTransaksiHarianBiru = new javax.swing.JMenuItem();
        mnuItemRekapBulananBiru = new javax.swing.JMenuItem();
        mnuItemKartuBayarBiru = new javax.swing.JMenuItem();
        mnuItemLaporanDataKendaraanBiru = new javax.swing.JMenuItem();
        mnuUtility = new javax.swing.JMenu();
        jSeparator11 = new javax.swing.JPopupMenu.Separator();
        mnuItemClosingBulan = new javax.swing.JMenuItem();
        mnuItemClosingTahunan = new javax.swing.JMenuItem();
        mnuItemSaldoAwal = new javax.swing.JMenuItem();
        mnuItemBonusBulanan = new javax.swing.JMenuItem();
        jSeparator12 = new javax.swing.JPopupMenu.Separator();
        mnuBantuan = new javax.swing.JMenu();
        jMenuItem17 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Aplikasi Payroll Taxi Mandala - Paperman");

        lblFastTransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paperman/images/transaksi.png"))); // NOI18N
        lblFastTransaksi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblFastTransaksi.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblFastTransaksi.setIconTextGap(0);
        lblFastTransaksi.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        lblFastTransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblFastTransaksiMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblFastTransaksiMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblFastTransaksiMouseExited(evt);
            }
        });
        lblFastTransaksi.setBounds(240, 260, 160, 130);
        desktoPane.add(lblFastTransaksi, javax.swing.JLayeredPane.DEFAULT_LAYER);

        lblFastSaldoAwal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paperman/images/AturSaldoAwal.png"))); // NOI18N
        lblFastSaldoAwal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblFastSaldoAwal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblFastSaldoAwalMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblFastSaldoAwalMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblFastSaldoAwalMouseExited(evt);
            }
        });
        lblFastSaldoAwal.setBounds(470, 430, 150, 160);
        desktoPane.add(lblFastSaldoAwal, javax.swing.JLayeredPane.DEFAULT_LAYER);

        lblFastPiutang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paperman/images/piutang.png"))); // NOI18N
        lblFastPiutang.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblFastPiutang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblFastPiutangMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblFastPiutangMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblFastPiutangMouseExited(evt);
            }
        });
        lblFastPiutang.setBounds(250, 430, 150, 150);
        desktoPane.add(lblFastPiutang, javax.swing.JLayeredPane.DEFAULT_LAYER);

        lblFastMasterKendaraan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paperman/images/kendaraan.png"))); // NOI18N
        lblFastMasterKendaraan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblFastMasterKendaraan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblFastMasterKendaraanMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblFastMasterKendaraanMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblFastMasterKendaraanMouseExited(evt);
            }
        });
        lblFastMasterKendaraan.setBounds(660, 230, 180, 170);
        desktoPane.add(lblFastMasterKendaraan, javax.swing.JLayeredPane.DEFAULT_LAYER);

        lblFastKartu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paperman/images/laporan.png"))); // NOI18N
        lblFastKartu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblFastKartu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblFastKartuMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblFastKartuMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblFastKartuMouseExited(evt);
            }
        });
        lblFastKartu.setBounds(670, 420, 150, 170);
        desktoPane.add(lblFastKartu, javax.swing.JLayeredPane.DEFAULT_LAYER);

        lblFastMasterPengemudi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paperman/images/pengemudi.png"))); // NOI18N
        lblFastMasterPengemudi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblFastMasterPengemudi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblFastMasterPengemudiMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblFastMasterPengemudiMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblFastMasterPengemudiMouseExited(evt);
            }
        });
        lblFastMasterPengemudi.setBounds(420, 250, 220, 140);
        desktoPane.add(lblFastMasterPengemudi, javax.swing.JLayeredPane.DEFAULT_LAYER);

        lblBG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paperman/images/bgNew1024x768.png"))); // NOI18N
        lblBG.setPreferredSize(desktoPane.getSize());
        lblBG.setBounds(0, -400, 2000, 1500);
        desktoPane.add(lblBG, javax.swing.JLayeredPane.DEFAULT_LAYER);

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
        mnuMaster.add(jSeparator6);

        mnuLimoPutih.setBackground(new java.awt.Color(255, 255, 255));
        mnuLimoPutih.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(255, 255, 255), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        mnuLimoPutih.setForeground(new java.awt.Color(102, 51, 0));
        mnuLimoPutih.setText("Armada Putih");
        mnuLimoPutih.setBorderPainted(true);
        mnuLimoPutih.setFont(new java.awt.Font("Segoe UI", 1, 12));

        mnuItemKendaraanPutih.setText("Master Kendaraan");
        mnuItemKendaraanPutih.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemKendaraanPutihActionPerformed(evt);
            }
        });
        mnuLimoPutih.add(mnuItemKendaraanPutih);

        mnuItemPengemudiPutih.setText("Master Pengemudi");
        mnuItemPengemudiPutih.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemPengemudiPutihActionPerformed(evt);
            }
        });
        mnuLimoPutih.add(mnuItemPengemudiPutih);

        mnuMaster.add(mnuLimoPutih);
        mnuMaster.add(jSeparator5);

        mnuLimoBiru.setBackground(new java.awt.Color(51, 51, 255));
        mnuLimoBiru.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, new java.awt.Color(51, 51, 255), java.awt.Color.blue, java.awt.Color.blue));
        mnuLimoBiru.setForeground(new java.awt.Color(51, 51, 255));
        mnuLimoBiru.setText("Armada Biru");
        mnuLimoBiru.setBorderPainted(true);
        mnuLimoBiru.setFont(new java.awt.Font("Segoe UI", 1, 12));

        mnuItemKendaraanBiru.setText("Master Kendaraan");
        mnuItemKendaraanBiru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemKendaraanBiruActionPerformed(evt);
            }
        });
        mnuLimoBiru.add(mnuItemKendaraanBiru);

        mnuItemPengemudiBiru.setText("Master Pengemudi");
        mnuItemPengemudiBiru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemPengemudiBiruActionPerformed(evt);
            }
        });
        mnuLimoBiru.add(mnuItemPengemudiBiru);

        mnuMaster.add(mnuLimoBiru);
        mnuMaster.add(jSeparator7);

        jMenuBar1.add(mnuMaster);

        mnuTransaksi.setText("Transaksi");
        mnuTransaksi.add(jSeparator8);

        mnuTransaksiPutih.setBackground(new java.awt.Color(255, 255, 255));
        mnuTransaksiPutih.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(255, 255, 255), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        mnuTransaksiPutih.setForeground(new java.awt.Color(102, 51, 0));
        mnuTransaksiPutih.setText("Armada Putih");
        mnuTransaksiPutih.setBorderPainted(true);
        mnuTransaksiPutih.setFont(new java.awt.Font("Segoe UI", 1, 12));

        mnuItemTrSetoranPutih.setText("Transaksi Setoran");
        mnuItemTrSetoranPutih.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemTrSetoranPutihActionPerformed(evt);
            }
        });
        mnuTransaksiPutih.add(mnuItemTrSetoranPutih);

        mnuItemManajemenPiutangPutih.setText("Manajemen Piutang");
        mnuItemManajemenPiutangPutih.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemManajemenPiutangPutihActionPerformed(evt);
            }
        });
        mnuTransaksiPutih.add(mnuItemManajemenPiutangPutih);

        mnuTransaksi.add(mnuTransaksiPutih);
        mnuTransaksi.add(jSeparator9);

        mnuTransaksiBiru.setBackground(new java.awt.Color(0, 51, 255));
        mnuTransaksiBiru.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.blue, java.awt.Color.blue, java.awt.Color.blue));
        mnuTransaksiBiru.setForeground(new java.awt.Color(51, 51, 255));
        mnuTransaksiBiru.setText("Armada Biru");
        mnuTransaksiBiru.setBorderPainted(true);
        mnuTransaksiBiru.setFont(new java.awt.Font("Segoe UI", 1, 12));

        mnuItemTransaksiSetoranBiru.setText("Transaksi Setoran");
        mnuItemTransaksiSetoranBiru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemTransaksiSetoranBiruActionPerformed(evt);
            }
        });
        mnuTransaksiBiru.add(mnuItemTransaksiSetoranBiru);

        mnuItemManajemenPiutangBiru.setText("Manajemen Piutang");
        mnuItemManajemenPiutangBiru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemManajemenPiutangBiruActionPerformed(evt);
            }
        });
        mnuTransaksiBiru.add(mnuItemManajemenPiutangBiru);

        mnuTransaksi.add(mnuTransaksiBiru);
        mnuTransaksi.add(jSeparator10);

        jMenuBar1.add(mnuTransaksi);

        mnuLaporan.setText("Laporan");

        jMenuItem6.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem6.setFont(new java.awt.Font("Segoe UI", 1, 13));
        jMenuItem6.setForeground(new java.awt.Color(102, 51, 0));
        jMenuItem6.setText("Armada PUTIH");
        jMenuItem6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(255, 255, 255), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jMenuItem6.setBorderPainted(true);
        jMenuItem6.setEnabled(false);
        jMenuItem6.setRequestFocusEnabled(false);
        mnuLaporan.add(jMenuItem6);
        mnuLaporan.add(jSeparator1);

        mnuItemLaporanTransaksiHarianPutih.setText("Laporan Transaksi Setoran");
        mnuItemLaporanTransaksiHarianPutih.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemLaporanTransaksiHarianPutihActionPerformed(evt);
            }
        });
        mnuLaporan.add(mnuItemLaporanTransaksiHarianPutih);

        mnuItemRekapBulananPutih.setText("Laporan Rekap Setoran");
        mnuItemRekapBulananPutih.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemRekapBulananPutihActionPerformed(evt);
            }
        });
        mnuLaporan.add(mnuItemRekapBulananPutih);

        mnuItemKartuBayarPutih.setText("Laporan Rekap Kartu Pembayaran");
        mnuItemKartuBayarPutih.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemKartuBayarPutihActionPerformed(evt);
            }
        });
        mnuLaporan.add(mnuItemKartuBayarPutih);

        mnuItemLaporanDataKendaraanPutih.setText("Laporan Data Kendaraan");
        mnuItemLaporanDataKendaraanPutih.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemLaporanDataKendaraanPutihActionPerformed(evt);
            }
        });
        mnuLaporan.add(mnuItemLaporanDataKendaraanPutih);

        jMenuItem5.setBackground(new java.awt.Color(0, 0, 255));
        jMenuItem5.setFont(new java.awt.Font("Segoe UI", 1, 13));
        jMenuItem5.setForeground(new java.awt.Color(51, 51, 255));
        jMenuItem5.setText("Armada BIRU");
        jMenuItem5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(255, 255, 255), new java.awt.Color(0, 0, 255), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 255)));
        jMenuItem5.setBorderPainted(true);
        jMenuItem5.setContentAreaFilled(false);
        jMenuItem5.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jMenuItem5.setEnabled(false);
        jMenuItem5.setRequestFocusEnabled(false);
        jMenuItem5.setVerifyInputWhenFocusTarget(false);
        mnuLaporan.add(jMenuItem5);
        mnuLaporan.add(jSeparator2);

        mnuItemLaporanTransaksiHarianBiru.setText("Laporan Transaksi Setoran");
        mnuItemLaporanTransaksiHarianBiru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemLaporanTransaksiHarianBiruActionPerformed(evt);
            }
        });
        mnuLaporan.add(mnuItemLaporanTransaksiHarianBiru);

        mnuItemRekapBulananBiru.setText("Laporan Rekap Setoran");
        mnuItemRekapBulananBiru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemRekapBulananBiruActionPerformed(evt);
            }
        });
        mnuLaporan.add(mnuItemRekapBulananBiru);

        mnuItemKartuBayarBiru.setText("Laporan Rekap Kartu Pembayaran");
        mnuItemKartuBayarBiru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemKartuBayarBiruActionPerformed(evt);
            }
        });
        mnuLaporan.add(mnuItemKartuBayarBiru);

        mnuItemLaporanDataKendaraanBiru.setText("Laporan Data Kendaraan");
        mnuItemLaporanDataKendaraanBiru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemLaporanDataKendaraanBiruActionPerformed(evt);
            }
        });
        mnuLaporan.add(mnuItemLaporanDataKendaraanBiru);

        jMenuBar1.add(mnuLaporan);

        mnuUtility.setText("Proses");
        mnuUtility.add(jSeparator11);

        mnuItemClosingBulan.setText("Proses Akhir Bulan");
        mnuItemClosingBulan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemClosingBulanActionPerformed(evt);
            }
        });
        mnuUtility.add(mnuItemClosingBulan);

        mnuItemClosingTahunan.setText("Proses Akhir Tahun");
        mnuItemClosingTahunan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemClosingTahunanActionPerformed(evt);
            }
        });
        mnuUtility.add(mnuItemClosingTahunan);

        mnuItemSaldoAwal.setText("Input Saldo Awal");
        mnuItemSaldoAwal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemSaldoAwalActionPerformed(evt);
            }
        });
        mnuUtility.add(mnuItemSaldoAwal);

        mnuItemBonusBulanan.setText("Input Bonus Bulanan");
        mnuItemBonusBulanan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemBonusBulananActionPerformed(evt);
            }
        });
        mnuUtility.add(mnuItemBonusBulanan);
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

    private void mnuItemKendaraanBiruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemKendaraanBiruActionPerformed
        try {
            // TODO add your handling code here:
            if (MasterKendaraan.getMasterKendaraan() == null || MasterKendaraan.getMasterKendaraan().isClosed()) {
                MasterKendaraan.inisialisasi();
                desktoPane.add(MasterKendaraan.getMasterKendaraan());
            } else {
                MasterKendaraan.getMasterKendaraan().setMaximum(true);
                MasterKendaraan.getMasterKendaraan().toFront();
            }
            MasterKendaraan.getMasterKendaraan().setVisible(true);
            MasterKendaraan.getMasterKendaraan().setSize(desktoPane.getSize());
            MasterKendaraan.getMasterKendaraan().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuItemKendaraanBiruActionPerformed

    private void mnuItemPengemudiBiruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemPengemudiBiruActionPerformed
        // TODO add your handling code here:
        try {
            if (MasterPengemudi.getMasterPengemudi() == null || MasterPengemudi.getMasterPengemudi().isClosed()) {
                MasterPengemudi.inisialisasi();
                desktoPane.add(MasterPengemudi.getMasterPengemudi());
            } else {
                MasterPengemudi.getMasterPengemudi().setMaximum(true);
                MasterPengemudi.getMasterPengemudi().toFront();
            }
            MasterPengemudi.getMasterPengemudi().setVisible(true);
            MasterPengemudi.getMasterPengemudi().setSize(desktoPane.getSize());
            MasterPengemudi.getMasterPengemudi().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuItemPengemudiBiruActionPerformed

    private void mnuItemTransaksiSetoranBiruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemTransaksiSetoranBiruActionPerformed
        // TODO add your handling code here:
        try {
            if (TransaksiSetoran.getTransaksiSetoran() == null || TransaksiSetoran.getTransaksiSetoran().isClosed()) {
                TransaksiSetoran.inisialisasi();
                desktoPane.add(TransaksiSetoran.getTransaksiSetoran());
            } else {
                TransaksiSetoran.getTransaksiSetoran().setMaximum(true);
                TransaksiSetoran.getTransaksiSetoran().toFront();
            }
            TransaksiSetoran.getTransaksiSetoran().setVisible(true);
            TransaksiSetoran.getTransaksiSetoran().setSize(desktoPane.getSize());
            TransaksiSetoran.getTransaksiSetoran().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuItemTransaksiSetoranBiruActionPerformed

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

    private void mnuItemLaporanTransaksiHarianBiruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemLaporanTransaksiHarianBiruActionPerformed
        // TODO add your handling code here:
        try {
            if (LaporanTransaksiHarian.getLaporanTransaksiHarian() == null || LaporanTransaksiHarian.getLaporanTransaksiHarian().isClosed()) {
                LaporanTransaksiHarian.inisialisasi();
                desktoPane.add(LaporanTransaksiHarian.getLaporanTransaksiHarian());
            } else {
                LaporanTransaksiHarian.getLaporanTransaksiHarian().setMaximum(true);
                LaporanTransaksiHarian.getLaporanTransaksiHarian().toFront();
            }
            LaporanTransaksiHarian.getLaporanTransaksiHarian().setVisible(true);
            LaporanTransaksiHarian.getLaporanTransaksiHarian().setSize(desktoPane.getSize());
            LaporanTransaksiHarian.getLaporanTransaksiHarian().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuItemLaporanTransaksiHarianBiruActionPerformed

    private void mnuItemRekapBulananBiruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemRekapBulananBiruActionPerformed
        // TODO add your handling code here:
        try {
            if (LaporanTransaksiBulanan.getLaporanTransaksiBulanan() == null || LaporanTransaksiBulanan.getLaporanTransaksiBulanan().isClosed()) {
                LaporanTransaksiBulanan.inisialisasi();
                desktoPane.add(LaporanTransaksiBulanan.getLaporanTransaksiBulanan());
            } else {
                LaporanTransaksiBulanan.getLaporanTransaksiBulanan().setMaximum(true);
                LaporanTransaksiBulanan.getLaporanTransaksiBulanan().toFront();
            }
            LaporanTransaksiBulanan.getLaporanTransaksiBulanan().setVisible(true);
            LaporanTransaksiBulanan.getLaporanTransaksiBulanan().setSize(desktoPane.getSize());
            LaporanTransaksiBulanan.getLaporanTransaksiBulanan().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuItemRekapBulananBiruActionPerformed

    private void mnuItemKartuBayarBiruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemKartuBayarBiruActionPerformed
        // TODO add your handling code here:
        try {
            if (LaporanKartuBayar.getKartuPembayaran() == null || LaporanKartuBayar.getKartuPembayaran().isClosed()) {
                LaporanKartuBayar.inisialisasi();
                desktoPane.add(LaporanKartuBayar.getKartuPembayaran());
            } else {
                LaporanKartuBayar.getKartuPembayaran().setMaximum(true);
                LaporanKartuBayar.getKartuPembayaran().toFront();
            }
            LaporanKartuBayar.getKartuPembayaran().setVisible(true);
            LaporanKartuBayar.getKartuPembayaran().setSize(desktoPane.getSize());
            LaporanKartuBayar.getKartuPembayaran().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuItemKartuBayarBiruActionPerformed

    private void mnuItemLaporanDataKendaraanBiruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemLaporanDataKendaraanBiruActionPerformed
        // TODO add your handling code here:
        try {
            if (LaporanDataLambungBulanan.getLaporanDataLambung() == null || LaporanDataLambungBulanan.getLaporanDataLambung().isClosed()) {
                LaporanDataLambungBulanan.inisialisasi();
                desktoPane.add(LaporanDataLambungBulanan.getLaporanDataLambung());
            } else {
                LaporanDataLambungBulanan.getLaporanDataLambung().setMaximum(true);
                LaporanDataLambungBulanan.getLaporanDataLambung().toFront();
            }
            LaporanDataLambungBulanan.getLaporanDataLambung().setVisible(true);
            LaporanDataLambungBulanan.getLaporanDataLambung().setSize(desktoPane.getSize());
            LaporanDataLambungBulanan.getLaporanDataLambung().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuItemLaporanDataKendaraanBiruActionPerformed

    private void mnuItemPengemudiPutihActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemPengemudiPutihActionPerformed
        // TODO add your handling code here:
        try {
            if (MasterPengemudiPutih.getMasterPengemudiPutih() == null || MasterPengemudiPutih.getMasterPengemudiPutih().isClosed()) {
                MasterPengemudiPutih.inisialisasi();
                desktoPane.add(MasterPengemudiPutih.getMasterPengemudiPutih());
            } else {
                MasterPengemudiPutih.getMasterPengemudiPutih().setMaximum(true);
                MasterPengemudiPutih.getMasterPengemudiPutih().toFront();
            }
            MasterPengemudiPutih.getMasterPengemudiPutih().setVisible(true);
            MasterPengemudiPutih.getMasterPengemudiPutih().setSize(desktoPane.getSize());
            MasterPengemudiPutih.getMasterPengemudiPutih().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuItemPengemudiPutihActionPerformed

    private void mnuItemKendaraanPutihActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemKendaraanPutihActionPerformed
        // TODO add your handling code here:
        try {
            if (MasterKendaraanPutih.getMasterKendaraanPutih() == null || MasterKendaraanPutih.getMasterKendaraanPutih().isClosed()) {
                MasterKendaraanPutih.inisialisasi();
                desktoPane.add(MasterKendaraanPutih.getMasterKendaraanPutih());
            } else {
                MasterKendaraanPutih.getMasterKendaraanPutih().setMaximum(true);
                MasterKendaraanPutih.getMasterKendaraanPutih().toFront();
            }
            MasterKendaraanPutih.getMasterKendaraanPutih().setVisible(true);
            MasterKendaraanPutih.getMasterKendaraanPutih().setSize(desktoPane.getSize());
            MasterKendaraanPutih.getMasterKendaraanPutih().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuItemKendaraanPutihActionPerformed

    private void mnuItemTrSetoranPutihActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemTrSetoranPutihActionPerformed
        // TODO add your handling code here:
        try {
            if (TransaksiSetoranPutih.getTransaksiSetoranPutih() == null || TransaksiSetoranPutih.getTransaksiSetoranPutih().isClosed()) {
                TransaksiSetoranPutih.inisialisasi();
                desktoPane.add(TransaksiSetoranPutih.getTransaksiSetoranPutih());
            } else {
                TransaksiSetoranPutih.getTransaksiSetoranPutih().setMaximum(true);
                TransaksiSetoranPutih.getTransaksiSetoranPutih().toFront();
            }
            TransaksiSetoranPutih.getTransaksiSetoranPutih().setVisible(true);
            TransaksiSetoranPutih.getTransaksiSetoranPutih().setSize(desktoPane.getSize());
            TransaksiSetoranPutih.getTransaksiSetoranPutih().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuItemTrSetoranPutihActionPerformed

    private void mnuItemLaporanTransaksiHarianPutihActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemLaporanTransaksiHarianPutihActionPerformed
        // TODO add your handling code here:
        try {
            if (LaporanTransaksiHarianPth.getLaporanTransaksiHarianPutih() == null || LaporanTransaksiHarianPth.getLaporanTransaksiHarianPutih().isClosed()) {
                LaporanTransaksiHarianPth.inisialisasi();
                desktoPane.add(LaporanTransaksiHarianPth.getLaporanTransaksiHarianPutih());
            } else {
                LaporanTransaksiHarianPth.getLaporanTransaksiHarianPutih().setMaximum(true);
                LaporanTransaksiHarianPth.getLaporanTransaksiHarianPutih().toFront();
            }
            LaporanTransaksiHarianPth.getLaporanTransaksiHarianPutih().setVisible(true);
            LaporanTransaksiHarianPth.getLaporanTransaksiHarianPutih().setSize(desktoPane.getSize());
            LaporanTransaksiHarianPth.getLaporanTransaksiHarianPutih().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuItemLaporanTransaksiHarianPutihActionPerformed

    private void mnuItemRekapBulananPutihActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemRekapBulananPutihActionPerformed
        // TODO add your handling code here:
        try {
            if (LaporanTransaksiBulananPth.getLaporanTransaksiBulananPutih() == null || LaporanTransaksiBulananPth.getLaporanTransaksiBulananPutih().isClosed()) {
                LaporanTransaksiBulananPth.inisialisasi();
                desktoPane.add(LaporanTransaksiBulananPth.getLaporanTransaksiBulananPutih());
            } else {
                LaporanTransaksiBulananPth.getLaporanTransaksiBulananPutih().setMaximum(true);
                LaporanTransaksiBulananPth.getLaporanTransaksiBulananPutih().toFront();
            }
            LaporanTransaksiBulananPth.getLaporanTransaksiBulananPutih().setVisible(true);
            LaporanTransaksiBulananPth.getLaporanTransaksiBulananPutih().setSize(desktoPane.getSize());
            LaporanTransaksiBulananPth.getLaporanTransaksiBulananPutih().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuItemRekapBulananPutihActionPerformed

    private void mnuItemKartuBayarPutihActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemKartuBayarPutihActionPerformed
        // TODO add your handling code here:
        try {
            if (LaporanKartuBayarPth.getKartuPembayaranPutih() == null || LaporanKartuBayarPth.getKartuPembayaranPutih().isClosed()) {
                LaporanKartuBayarPth.inisialisasi();
                desktoPane.add(LaporanKartuBayarPth.getKartuPembayaranPutih());
            } else {
                LaporanKartuBayarPth.getKartuPembayaranPutih().setMaximum(true);
                LaporanKartuBayarPth.getKartuPembayaranPutih().toFront();
            }
            LaporanKartuBayarPth.getKartuPembayaranPutih().setVisible(true);
            LaporanKartuBayarPth.getKartuPembayaranPutih().setSize(desktoPane.getSize());
            LaporanKartuBayarPth.getKartuPembayaranPutih().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuItemKartuBayarPutihActionPerformed

    private void mnuItemLaporanDataKendaraanPutihActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemLaporanDataKendaraanPutihActionPerformed
        // TODO add your handling code here:
        try {
            if (LaporanDataLambungBulananPth.getLaporanDataLambungPutih() == null || LaporanDataLambungBulananPth.getLaporanDataLambungPutih().isClosed()) {
                LaporanDataLambungBulananPth.inisialisasi();
                desktoPane.add(LaporanDataLambungBulananPth.getLaporanDataLambungPutih());
            } else {
                LaporanDataLambungBulananPth.getLaporanDataLambungPutih().setMaximum(true);
                LaporanDataLambungBulananPth.getLaporanDataLambungPutih().toFront();
            }
            LaporanDataLambungBulananPth.getLaporanDataLambungPutih().setVisible(true);
            LaporanDataLambungBulananPth.getLaporanDataLambungPutih().setSize(desktoPane.getSize());
            LaporanDataLambungBulananPth.getLaporanDataLambungPutih().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuItemLaporanDataKendaraanPutihActionPerformed

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

    private void mnuItemClosingTahunanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemClosingTahunanActionPerformed
        // TODO add your handling code here:
        try {
            if (ClosingTahunan.getFormClosingTahun() == null || ClosingTahunan.getFormClosingTahun().isClosed()) {
                ClosingTahunan.inisialisasi();
                desktoPane.add(ClosingTahunan.getFormClosingTahun());
                ukuranLayar = desktoPane.getSize();
                ukuranFrame = ClosingTahunan.getFormClosingTahun().getSize();
            } else {
                ClosingTahunan.getFormClosingTahun().toFront();
                ClosingTahunan.getFormClosingTahun().show();
            }
            ClosingTahunan.getFormClosingTahun().setVisible(true);
            ClosingTahunan.getFormClosingTahun().setLocation((ukuranLayar.width - ukuranFrame.width) / 2, (ukuranLayar.height - ukuranFrame.height) / 4);
            ClosingTahunan.getFormClosingTahun().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuItemClosingTahunanActionPerformed

    private void mnuItemManajemenPiutangBiruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemManajemenPiutangBiruActionPerformed
        // TODO add your handling code here:
        try {
            if (ManajemenPiutang.getManajemenPiutang() == null || ManajemenPiutang.getManajemenPiutang().isClosed()) {
                ManajemenPiutang.inisialisasi();
                desktoPane.add(ManajemenPiutang.getManajemenPiutang());
            } else {
                ManajemenPiutang.getManajemenPiutang().toFront();
                ManajemenPiutang.getManajemenPiutang().show();
            }
            ManajemenPiutang.getManajemenPiutang().setVisible(true);
            ManajemenPiutang.getManajemenPiutang().setSize(desktoPane.getSize());
            ManajemenPiutang.getManajemenPiutang().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuItemManajemenPiutangBiruActionPerformed

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

    private void mnuItemBonusBulananActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemBonusBulananActionPerformed
        // TODO add your handling code here:
        try {
            if (InputBonusBulanan.getFormInputBonusBulanan() == null || InputBonusBulanan.getFormInputBonusBulanan().isClosed()) {
                InputBonusBulanan.inisialisasi();
                desktoPane.add(InputBonusBulanan.getFormInputBonusBulanan());
                ukuranLayar = desktoPane.getSize();
                ukuranFrame = InputBonusBulanan.getFormInputBonusBulanan().getSize();
            } else {
                InputBonusBulanan.getFormInputBonusBulanan().toFront();
                InputBonusBulanan.getFormInputBonusBulanan().show();
            }
            InputBonusBulanan.getFormInputBonusBulanan().setVisible(true);
            InputBonusBulanan.getFormInputBonusBulanan().setLocation((ukuranLayar.width - ukuranFrame.width) / 2, (ukuranLayar.height - ukuranFrame.height) / 4);
            InputBonusBulanan.getFormInputBonusBulanan().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuItemBonusBulananActionPerformed

    private void lblFastTransaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFastTransaksiMouseClicked
        // TODO add your handling code here:
        try {
            if (TransaksiSetoran.getTransaksiSetoran() == null || TransaksiSetoran.getTransaksiSetoran().isClosed()) {
                TransaksiSetoran.inisialisasi();
                desktoPane.add(TransaksiSetoran.getTransaksiSetoran());
            } else {
                TransaksiSetoran.getTransaksiSetoran().setMaximum(true);
                TransaksiSetoran.getTransaksiSetoran().toFront();
            }
            TransaksiSetoran.getTransaksiSetoran().setVisible(true);
            TransaksiSetoran.getTransaksiSetoran().setSize(desktoPane.getSize());
            TransaksiSetoran.getTransaksiSetoran().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_lblFastTransaksiMouseClicked

    private void lblFastMasterPengemudiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFastMasterPengemudiMouseClicked
        // TODO add your handling code here:
        try {
            if (MasterPengemudi.getMasterPengemudi() == null || MasterPengemudi.getMasterPengemudi().isClosed()) {
                MasterPengemudi.inisialisasi();
                desktoPane.add(MasterPengemudi.getMasterPengemudi());
            } else {
                MasterPengemudi.getMasterPengemudi().setMaximum(true);
                MasterPengemudi.getMasterPengemudi().toFront();
            }
            MasterPengemudi.getMasterPengemudi().setVisible(true);
            MasterPengemudi.getMasterPengemudi().setSize(desktoPane.getSize());
            MasterPengemudi.getMasterPengemudi().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_lblFastMasterPengemudiMouseClicked

    private void lblFastPiutangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFastPiutangMouseClicked
        // TODO add your handling code here:
        try {
            if (ManajemenPiutang.getManajemenPiutang() == null || ManajemenPiutang.getManajemenPiutang().isClosed()) {
                ManajemenPiutang.inisialisasi();
                desktoPane.add(ManajemenPiutang.getManajemenPiutang());
            } else {
                ManajemenPiutang.getManajemenPiutang().toFront();
                ManajemenPiutang.getManajemenPiutang().show();
            }
            ManajemenPiutang.getManajemenPiutang().setVisible(true);
            ManajemenPiutang.getManajemenPiutang().setSize(desktoPane.getSize());
            ManajemenPiutang.getManajemenPiutang().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_lblFastPiutangMouseClicked

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

    private void lblFastMasterKendaraanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFastMasterKendaraanMouseClicked
        // TODO add your handling code here:
        try {
            if (MasterKendaraan.getMasterKendaraan() == null || MasterKendaraan.getMasterKendaraan().isClosed()) {
                MasterKendaraan.inisialisasi();
                desktoPane.add(MasterKendaraan.getMasterKendaraan());
            } else {
                MasterKendaraan.getMasterKendaraan().setMaximum(true);
                MasterKendaraan.getMasterKendaraan().toFront();
            }
            MasterKendaraan.getMasterKendaraan().setVisible(true);
            MasterKendaraan.getMasterKendaraan().setSize(desktoPane.getSize());
            MasterKendaraan.getMasterKendaraan().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_lblFastMasterKendaraanMouseClicked

    private void lblFastSaldoAwalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFastSaldoAwalMouseClicked
        // TODO add your handling code here:
        try {
            if (SaldoAwal.getFormSaldoAwal() == null || SaldoAwal.getFormSaldoAwal().isClosed()) {
                SaldoAwal.inisialisasi();
                desktoPane.add(SaldoAwal.getFormSaldoAwal());
                ukuranLayar = desktoPane.getSize();
                ukuranFrame = SaldoAwal.getFormSaldoAwal().getSize();
            } else {
                SaldoAwal.getFormSaldoAwal().setMaximum(true);
                SaldoAwal.getFormSaldoAwal().toFront();
            }
            SaldoAwal.getFormSaldoAwal().setVisible(true);
            SaldoAwal.getFormSaldoAwal().setLocation((ukuranLayar.width - ukuranFrame.width) / 2, (ukuranLayar.height - ukuranFrame.height) / 4);
            SaldoAwal.getFormSaldoAwal().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_lblFastSaldoAwalMouseClicked

    private void lblFastKartuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFastKartuMouseClicked
        // TODO add your handling code here:
        try {
            if (LaporanKartuBayar.getKartuPembayaran() == null || LaporanKartuBayar.getKartuPembayaran().isClosed()) {
                LaporanKartuBayar.inisialisasi();
                desktoPane.add(LaporanKartuBayar.getKartuPembayaran());
            } else {
                LaporanKartuBayar.getKartuPembayaran().setMaximum(true);
                LaporanKartuBayar.getKartuPembayaran().toFront();
            }
            LaporanKartuBayar.getKartuPembayaran().setVisible(true);
            LaporanKartuBayar.getKartuPembayaran().setSize(desktoPane.getSize());
            LaporanKartuBayar.getKartuPembayaran().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_lblFastKartuMouseClicked

    private void lblFastTransaksiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFastTransaksiMouseEntered
        // TODO add your handling code here:
//        for (int i =lblFastTransaksi.getY(); i == 20;) {
            lblFastTransaksi.setLocation(lblFastTransaksi.getX(), lblFastTransaksi.getY() - 20);
//        }
    }//GEN-LAST:event_lblFastTransaksiMouseEntered

    private void lblFastTransaksiMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFastTransaksiMouseExited
        // TODO add your handling code here:
        lblFastTransaksi.setLocation(lblFastTransaksi.getX(), lblFastTransaksi.getY() + 20);
    }//GEN-LAST:event_lblFastTransaksiMouseExited

    private void lblFastMasterPengemudiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFastMasterPengemudiMouseEntered
        // TODO add your handling code here:
        lblFastMasterPengemudi.setLocation(lblFastMasterPengemudi.getX(), lblFastMasterPengemudi.getY() - 20);
    }//GEN-LAST:event_lblFastMasterPengemudiMouseEntered

    private void lblFastMasterPengemudiMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFastMasterPengemudiMouseExited
        // TODO add your handling code here:
        lblFastMasterPengemudi.setLocation(lblFastMasterPengemudi.getX(), lblFastMasterPengemudi.getY() + 20);
    }//GEN-LAST:event_lblFastMasterPengemudiMouseExited

    private void lblFastMasterKendaraanMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFastMasterKendaraanMouseEntered
        // TODO add your handling code here:
        lblFastMasterKendaraan.setLocation(lblFastMasterKendaraan.getX(), lblFastMasterKendaraan.getY() - 20);
    }//GEN-LAST:event_lblFastMasterKendaraanMouseEntered

    private void lblFastMasterKendaraanMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFastMasterKendaraanMouseExited
        // TODO add your handling code here:
        lblFastMasterKendaraan.setLocation(lblFastMasterKendaraan.getX(), lblFastMasterKendaraan.getY() + 20);
    }//GEN-LAST:event_lblFastMasterKendaraanMouseExited

    private void lblFastPiutangMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFastPiutangMouseEntered
        // TODO add your handling code here:
        lblFastPiutang.setLocation(lblFastPiutang.getX(), lblFastPiutang.getY() - 20);
    }//GEN-LAST:event_lblFastPiutangMouseEntered

    private void lblFastPiutangMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFastPiutangMouseExited
        // TODO add your handling code here:
        lblFastPiutang.setLocation(lblFastPiutang.getX(), lblFastPiutang.getY() + 20);
    }//GEN-LAST:event_lblFastPiutangMouseExited

    private void lblFastSaldoAwalMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFastSaldoAwalMouseEntered
        // TODO add your handling code here:
        lblFastSaldoAwal.setLocation(lblFastSaldoAwal.getX(), lblFastSaldoAwal.getY() - 20);
    }//GEN-LAST:event_lblFastSaldoAwalMouseEntered

    private void lblFastSaldoAwalMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFastSaldoAwalMouseExited
        // TODO add your handling code here:
        lblFastSaldoAwal.setLocation(lblFastSaldoAwal.getX(), lblFastSaldoAwal.getY() + 20);
    }//GEN-LAST:event_lblFastSaldoAwalMouseExited

    private void lblFastKartuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFastKartuMouseEntered
        // TODO add your handling code here:
        lblFastKartu.setLocation(lblFastKartu.getX(), lblFastKartu.getY() - 20);
    }//GEN-LAST:event_lblFastKartuMouseEntered

    private void lblFastKartuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFastKartuMouseExited
        // TODO add your handling code here:
        lblFastKartu.setLocation(lblFastKartu.getX(), lblFastKartu.getY() + 20);
}//GEN-LAST:event_lblFastKartuMouseExited

    private void mnuItemManajemenPiutangPutihActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemManajemenPiutangPutihActionPerformed
        // TODO add your handling code here:
        try {
            if (ManajemenPiutangPutih.getManajemenPiutangPutih() == null || ManajemenPiutangPutih.getManajemenPiutangPutih().isClosed()) {
                ManajemenPiutangPutih.inisialisasi();
                desktoPane.add(ManajemenPiutangPutih.getManajemenPiutangPutih());
            } else {
                ManajemenPiutangPutih.getManajemenPiutangPutih().toFront();
                ManajemenPiutangPutih.getManajemenPiutangPutih().show();
            }
            ManajemenPiutangPutih.getManajemenPiutangPutih().setVisible(true);
            ManajemenPiutangPutih.getManajemenPiutangPutih().setSize(desktoPane.getSize());
            ManajemenPiutangPutih.getManajemenPiutangPutih().setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteConnectFailureException rc) {
            JOptionPane.showMessageDialog(this, "Koneksi Ke Server Terputus\nPeriksa Lagi Server Dan Kabel, Pastikan Dalam Keadaan Hidup dan Tersambung","Peringatan",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuItemManajemenPiutangPutihActionPerformed

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
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane desktoPane;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JPopupMenu.Separator jSeparator11;
    private javax.swing.JPopupMenu.Separator jSeparator12;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    private javax.swing.JLabel lblBG;
    private javax.swing.JLabel lblFastKartu;
    private javax.swing.JLabel lblFastMasterKendaraan;
    private javax.swing.JLabel lblFastMasterPengemudi;
    private javax.swing.JLabel lblFastPiutang;
    private javax.swing.JLabel lblFastSaldoAwal;
    private javax.swing.JLabel lblFastTransaksi;
    private javax.swing.JLabel lblTanggalKerja;
    private javax.swing.JLabel lblUser;
    private javax.swing.JMenu mnuBantuan;
    private javax.swing.JMenu mnuFile;
    private javax.swing.JMenuItem mnuItemBonusBulanan;
    private javax.swing.JMenuItem mnuItemClosingBulan;
    private javax.swing.JMenuItem mnuItemClosingTahunan;
    private javax.swing.JMenuItem mnuItemExit;
    private javax.swing.JMenuItem mnuItemKartuBayarBiru;
    private javax.swing.JMenuItem mnuItemKartuBayarPutih;
    private javax.swing.JMenuItem mnuItemKendaraanBiru;
    private javax.swing.JMenuItem mnuItemKendaraanPutih;
    private javax.swing.JMenuItem mnuItemKode;
    private javax.swing.JMenuItem mnuItemKonfigSAPth;
    private javax.swing.JMenuItem mnuItemLaporanDataKendaraanBiru;
    private javax.swing.JMenuItem mnuItemLaporanDataKendaraanPutih;
    private javax.swing.JMenuItem mnuItemLaporanTransaksiHarianBiru;
    private javax.swing.JMenuItem mnuItemLaporanTransaksiHarianPutih;
    private javax.swing.JMenuItem mnuItemLogin;
    private javax.swing.JMenuItem mnuItemLogout;
    private javax.swing.JMenuItem mnuItemManajemenPiutangBiru;
    private javax.swing.JMenuItem mnuItemManajemenPiutangPutih;
    private javax.swing.JMenuItem mnuItemPengemudiBiru;
    private javax.swing.JMenuItem mnuItemPengemudiPutih;
    private javax.swing.JMenuItem mnuItemRekapBulananBiru;
    private javax.swing.JMenuItem mnuItemRekapBulananPutih;
    private javax.swing.JMenuItem mnuItemSaldoAwal;
    private javax.swing.JMenuItem mnuItemSistem;
    private javax.swing.JMenuItem mnuItemTrSetoranPutih;
    private javax.swing.JMenuItem mnuItemTransaksiSetoranBiru;
    private javax.swing.JMenuItem mnuItemUbahSaldo;
    private javax.swing.JMenuItem mnuItemUserRole;
    private javax.swing.JMenu mnuKonfigurasi;
    private javax.swing.JMenu mnuLaporan;
    private javax.swing.JMenu mnuLimoBiru;
    private javax.swing.JMenu mnuLimoPutih;
    private javax.swing.JMenu mnuMaster;
    private javax.swing.JMenu mnuTransaksi;
    private javax.swing.JMenu mnuTransaksiBiru;
    private javax.swing.JMenu mnuTransaksiPutih;
    private javax.swing.JMenu mnuUtility;
    // End of variables declaration//GEN-END:variables
}

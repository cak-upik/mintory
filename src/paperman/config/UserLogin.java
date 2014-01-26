/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * UserLogin.java
 *
 * Created on 16 Okt 12, 16:17:05
 */
package paperman.config;

import java.awt.Color;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.hibernate.JDBCException;
import org.springframework.transaction.TransactionException;
import paperman.Main;
import paperman.TextComponentUtils;
import paperman.model.SecurityUser;
import paperman.model.userStatus;
import paperman.tablemodel.SecurityTableModel;

/**
 *
 * @author Nurul Chusna
 */
public class UserLogin extends javax.swing.JInternalFrame implements ListSelectionListener {

    /** Creates new form UserLogin */
    public static UserLogin user;
    private SecurityUser secUser;
    private List<SecurityUser> listSecurityUser;

    public static void inisialisasi() {
        user = new UserLogin();
    }

    public static void destroy() {
        user.dispose();
        user = null;
        System.gc();
    }

    public static UserLogin getUserLogin() {
        return user;
    }

    public UserLogin() {
        initComponents();
        initButtonListener();
        LoadDataToTable();
        buttonMaster.defaultMode();
        enableForm(false);
        tblUserData.setEnabled(true);
        tblUserData.getSelectionModel().addListSelectionListener(this);
        roleControl(false);
        TextComponentUtils.setAutoUpperCaseText(txtNamaLogin);
        TextComponentUtils.setAutoUpperCaseText(txtNamaLengkap);
        dateUpdate.setDate(new Date());
    }

    private void initButtonListener() {
        buttonMaster.getBtnTambah().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aksiTambah();
            }
        });

        buttonMaster.getBtnUbah().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aksiUbah();
            }
        });

        buttonMaster.getBtnHapus().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aksiHapus();
            }
        });

        buttonMaster.getBtnBatal().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aksiBatal();
            }
        });

        buttonMaster.getBtnSimpan().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aksiSimpan();
            }
        });

        buttonMaster.getBtnKeluar().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aksiKeluar();
            }
        });
    }

    private void aksiTambah() {
        enableForm(true);
//        houseKeeping();
        txtNamaLengkap.requestFocus();
        tblUserData.setEnabled(false);
        buttonMaster.tambahMode();
    }

    private void aksiUbah() {
        enableForm(true);
        txtNamaLengkap.requestFocus();
        buttonMaster.tambahMode();
        txtNamaLengkap.requestFocus();
        tblUserData.setEnabled(false);
    }

    private void aksiHapus() {
        String namaLgkp = listSecurityUser.get(tblUserData.getSelectedRow()).getNamaLgkp();
        String namaLogin = listSecurityUser.get(tblUserData.getSelectedRow()).getNamaLogin();
        String alamat = listSecurityUser.get(tblUserData.getSelectedRow()).getAlamat();
        String ttl = listSecurityUser.get(tblUserData.getSelectedRow()).getTtl();
        String noTelp = listSecurityUser.get(tblUserData.getSelectedRow()).getNoTelp();
        String status = listSecurityUser.get(tblUserData.getSelectedRow()).getStatusUsr().toString();
        String tglUpdate = listSecurityUser.get(tblUserData.getSelectedRow()).getTglUpdate().toString();
        int konfirmasi = JOptionPane.showConfirmDialog(this, "Anda Yakin Akan Menghapus Data Ini ? ?\nRincian : \nNama Lengkap =" + namaLgkp + "\nNama Login= " + namaLogin + "\nAlamat = " + alamat + "\nTempat Tanggal Lahir = " + ttl + "\nNo Telp = " + noTelp + "\nStatus= " + status + "\nTgl.Update= " + tglUpdate, "Konfirmasi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (konfirmasi == JOptionPane.OK_OPTION) {
            Main.getSistemService().delete(secUser);
            LoadDataToTable();
            JOptionPane.showMessageDialog(this, "Data User Berhasil Dihapus", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            houseKeeping();
            secUser = null;
        }
        if (konfirmasi == JOptionPane.CANCEL_OPTION) {
            houseKeeping();
            secUser = null;
        }
        enableForm(false);
        houseKeeping();
        tblUserData.getSelectionModel().clearSelection();
        tblUserData.setEnabled(true);
        buttonMaster.defaultMode();
    }

    private void aksiSimpan() {
        try {
            cekDuplicate();
        } catch (JDBCException jde) {
            int konfirmasi = JOptionPane.showConfirmDialog(this, "Data Yang Sama Sudah Masuk\nPeriksa Lagi Pengisian Data", "Konfirmasi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (konfirmasi == JOptionPane.OK_OPTION) {
                enableForm(true);
                buttonMaster.tambahMode();
                tblUserData.setEnabled(false);
                txtNamaLengkap.requestFocus();
            } else if (konfirmasi == JOptionPane.CANCEL_OPTION) {
                enableForm(false);
                houseKeeping();
                buttonMaster.defaultMode();
                tblUserData.setEnabled(true);
            }
        } catch (TransactionException te) {
            String error = te.getMessage();
            TextArea txtError = new TextArea();
            txtError.setText(error);
            Object messageOutput[] = {"Koneksi Database Terputus\n", "Rincian Kesalahan :", txtError};
            JOptionPane msg = new JOptionPane();
            msg.setMessage(messageOutput);
            msg.setMessageType(JOptionPane.ERROR_MESSAGE);
            JDialog dialog = msg.createDialog(this, "Informasi");
            dialog.setVisible(true);
            houseKeeping();
            enableForm(false);
            tblUserData.getSelectionModel().clearSelection();
            tblUserData.setEnabled(true);
            buttonMaster.defaultMode();
            secUser = null;
        }
    }

    private void cekDuplicate() {
        if (validateForm()) {
            LoadFormToDatabase();
            Main.getSistemService().save(secUser);
            LoadDataToTable();
            houseKeeping();
            enableForm(false);
            JOptionPane.showMessageDialog(this, "Data User Telah Berhasil Disimpan", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            tblUserData.getSelectionModel().clearSelection();
            tblUserData.setEnabled(true);
            buttonMaster.defaultMode();
            secUser = null;
        } else {
            JOptionPane.showMessageDialog(this, "Data User Gagal Disimpan\nPeriksa Lagi Pengisian Form", "Peringatan", JOptionPane.ERROR_MESSAGE);
            enableForm(true);
            tblUserData.setEnabled(false);
            buttonMaster.tambahMode();
        }
    }

    private void aksiBatal() {
        houseKeeping();
        enableForm(false);
        tblUserData.getSelectionModel().clearSelection();
//        LoadDataToTable();
        tblUserData.setEnabled(true);
        buttonMaster.defaultMode();
        secUser = null;
    }

    private void aksiKeluar() {
        enableForm(false);
        houseKeeping();
        buttonMaster.keluarMode();
        destroy();
    }

    private void enableForm(boolean e) {
        txtNamaLengkap.setEnabled(e);
        txtNamaLogin.setEnabled(e);
        txtPassword.setEnabled(e);
        txtNoTelp.setEnabled(e);
        txtAlamat.setEnabled(e);
        txtTTL.setEnabled(e);
        dateUpdate.setEnabled(e);
        cmbStatus.setEnabled(e);
    }

    private void roleControl(boolean en) {
        cbDoClosing.setEnabled(en);
        cbEditDataSistem.setEnabled(en);
        cbEditSaldoAwal.setEnabled(en);
        cbEditSaldoBonus.setEnabled(en);
        cbKomposisiTransaksi.setEnabled(en);
        cbMenuTransaksi.setEnabled(en);
        cbOpenTransaksi.setEnabled(en);
        cbTambahUser.setEnabled(en);
    }

    private void cbLoadState(Integer state) {
        if (state == 0) {
            cbDoClosing.setSelected(true);
            cbEditDataSistem.setSelected(true);
            cbEditSaldoAwal.setSelected(true);
            cbEditSaldoBonus.setSelected(true);
            cbKomposisiTransaksi.setSelected(true);
            cbMenuTransaksi.setSelected(true);
            cbOpenTransaksi.setSelected(true);
            cbTambahUser.setSelected(true);
        }
        if (state == 1) {
            cbDoClosing.setSelected(true);
            cbEditDataSistem.setSelected(false);
            cbEditSaldoAwal.setSelected(true);
            cbEditSaldoBonus.setSelected(true);
            cbKomposisiTransaksi.setSelected(true);
            cbMenuTransaksi.setSelected(true);
            cbOpenTransaksi.setSelected(true);
            cbTambahUser.setSelected(false);
        }
        if (state == 2) {
            cbDoClosing.setSelected(false);
            cbEditDataSistem.setSelected(false);
            cbEditSaldoAwal.setSelected(true);
            cbEditSaldoBonus.setSelected(true);
            cbKomposisiTransaksi.setSelected(true);
            cbMenuTransaksi.setSelected(false);
            cbOpenTransaksi.setSelected(false);
            cbTambahUser.setSelected(false);
        }
        if (state == 3) {
            cbDoClosing.setSelected(false);
            cbEditDataSistem.setSelected(false);
            cbEditSaldoAwal.setSelected(false);
            cbEditSaldoBonus.setSelected(false);
            cbKomposisiTransaksi.setSelected(false);
            cbMenuTransaksi.setSelected(false);
            cbOpenTransaksi.setSelected(false);
            cbTambahUser.setSelected(false);
        }
        if (state == 4) {
            cbDoClosing.setSelected(false);
            cbEditDataSistem.setSelected(false);
            cbEditSaldoAwal.setSelected(false);
            cbEditSaldoBonus.setSelected(false);
            cbKomposisiTransaksi.setSelected(false);
            cbMenuTransaksi.setSelected(false);
            cbOpenTransaksi.setSelected(false);
            cbTambahUser.setSelected(false);
        }
    }

    private void houseKeeping() {
        txtNamaLengkap.setText("");
        txtNamaLogin.setText("");
        txtPassword.setText("");
        txtNoTelp.setText("");
        txtTTL.setText("");
        txtAlamat.setText("");
        cmbStatus.setSelectedIndex(0);
        cbDoClosing.setSelected(false);
        cbEditDataSistem.setSelected(false);
        cbEditSaldoAwal.setSelected(false);
        cbEditSaldoBonus.setSelected(false);
        cbKomposisiTransaksi.setSelected(false);
        cbMenuTransaksi.setSelected(false);
        cbOpenTransaksi.setSelected(false);
        cbTambahUser.setSelected(false);
        tblUserData.getSelectionModel().clearSelection();
        dateUpdate.setDate(null);
        cbDoClosing.setFont(new Font("Tahoma", Font.PLAIN, 14));
        cbDoClosing.setText("ya");
        cbEditDataSistem.setFont(new Font("Tahoma", Font.PLAIN, 14));
        cbEditDataSistem.setText("ya");
        cbEditSaldoAwal.setFont(new Font("Tahoma", Font.PLAIN, 14));
        cbEditSaldoAwal.setText("ya");
        cbEditSaldoBonus.setFont(new Font("Tahoma", Font.PLAIN, 14));
        cbEditSaldoBonus.setText("ya");
        cbKomposisiTransaksi.setFont(new Font("Tahoma", Font.PLAIN, 14));
        cbKomposisiTransaksi.setText("ya");
        cbMenuTransaksi.setFont(new Font("Tahoma", Font.PLAIN, 14));
        cbMenuTransaksi.setText("ya");
        cbOpenTransaksi.setFont(new Font("Tahoma", Font.PLAIN, 14));
        cbOpenTransaksi.setText("ya");
        cbTambahUser.setFont(new Font("Tahoma", Font.PLAIN, 14));
        cbTambahUser.setText("ya");
    }

    private void LoadFormToDatabase() {
        if (secUser == null) {
            secUser = new SecurityUser();
        }
        secUser.setNamaLgkp(txtNamaLengkap.getText());
        secUser.setNamaLogin(txtNamaLogin.getText());
        secUser.setNoTelp(txtNoTelp.getText());
        secUser.setAlamat(txtAlamat.getText());
        secUser.setTtl(txtTTL.getText());
        secUser.setTglUpdate(dateUpdate.getDate());
        char[] pwd = txtPassword.getPassword();
        secUser.setPass(new String(pwd));
        if (cmbStatus.getSelectedIndex() == 0) {
            secUser.setStatusUsr(userStatus.SUPER_ADMIN);
        }
        if (cmbStatus.getSelectedIndex() == 1) {
            secUser.setStatusUsr(userStatus.ADMIN);
        }
        if (cmbStatus.getSelectedIndex() == 2) {
            secUser.setStatusUsr(userStatus.KASIR);
        }
        if (cmbStatus.getSelectedIndex() == 3) {
            secUser.setStatusUsr(userStatus.KEUANGAN);
        }
        if (cmbStatus.getSelectedIndex() == 4) {
            secUser.setStatusUsr(userStatus.OPERATOR);
        }
        if (cbMenuTransaksi.isSelected()) {
            secUser.setViewAllTransactionMenu(Boolean.TRUE);
        } else {
            secUser.setViewAllTransactionMenu(Boolean.FALSE);
        }
        if (cbMenuTransaksi.isSelected()) {
            secUser.setViewAllTransactionMenu(Boolean.TRUE);
        } else {
            secUser.setViewAllTransactionMenu(Boolean.FALSE);
        }
        if (cbOpenTransaksi.isSelected()) {
            secUser.setOpenClosedTrans(Boolean.TRUE);
        } else {
            secUser.setOpenClosedTrans(Boolean.FALSE);
        }
        if (cbDoClosing.isSelected()) {
            secUser.setDoClosing(Boolean.TRUE);
        } else {
            secUser.setDoClosing(Boolean.FALSE);
        }
        if (cbTambahUser.isSelected()) {
            secUser.setAddUser(Boolean.TRUE);
        } else {
            secUser.setAddUser(Boolean.FALSE);
        }
        if (cbKomposisiTransaksi.isSelected()) {
            secUser.setChangeKomposisi(Boolean.TRUE);
        } else {
            secUser.setChangeKomposisi(Boolean.FALSE);
        }
        if (cbEditSaldoBonus.isSelected()) {
            secUser.setChangeBonus(Boolean.TRUE);
        } else {
            secUser.setChangeBonus(Boolean.FALSE);
        }
        if (cbEditDataSistem.isSelected()) {
            secUser.setChangeDataSistem(Boolean.TRUE);
        } else {
            secUser.setChangeDataSistem(Boolean.FALSE);
        }
        if (cbEditSaldoAwal.isSelected()) {
            secUser.setDoSaldoAwal(Boolean.TRUE);
        } else {
            secUser.setDoSaldoAwal(Boolean.FALSE);
        }
    }

    private void LoadDatabaseToForm() {
        txtNamaLengkap.setText(secUser.getNamaLgkp());
        txtNamaLogin.setText(secUser.getNamaLogin());
        txtAlamat.setText(secUser.getAlamat());
        txtNoTelp.setText(secUser.getNoTelp());
        txtPassword.setText(secUser.getPass());
        txtTTL.setText(secUser.getTtl());
        cmbStatus.setSelectedIndex(secUser.getStatusUsr().ordinal());
        System.out.println("status user = " + secUser.getStatusUsr().ordinal());
        dateUpdate.setDate(secUser.getTglUpdate());
        if (secUser.getViewAllTransactionMenu().compareTo(Boolean.TRUE) == 0) {
            cbMenuTransaksi.setSelected(true);
        } else {
            cbMenuTransaksi.setSelected(false);
        }
        if (secUser.getDoClosing().compareTo(Boolean.TRUE) == 0) {
            cbDoClosing.setSelected(true);
        } else {
            cbDoClosing.setSelected(false);
        }
        if (secUser.getOpenClosedTrans().compareTo(Boolean.TRUE) == 0) {
            cbOpenTransaksi.setSelected(true);
        } else {
            cbOpenTransaksi.setSelected(false);
        }
        if (secUser.getAddUser().compareTo(Boolean.TRUE) == 0) {
            cbTambahUser.setSelected(true);
        } else {
            cbTambahUser.setSelected(false);
        }
        if (secUser.getChangeKomposisi().compareTo(Boolean.TRUE) == 0) {
            cbKomposisiTransaksi.setSelected(true);
        } else {
            cbKomposisiTransaksi.setSelected(false);
        }
        if (secUser.getChangeBonus().compareTo(Boolean.TRUE) == 0) {
            cbEditSaldoBonus.setSelected(true);
        } else {
            cbEditSaldoBonus.setSelected(false);
        }
        if (secUser.getChangeDataSistem().compareTo(Boolean.TRUE) == 0) {
            cbEditDataSistem.setSelected(true);
        } else {
            cbEditDataSistem.setSelected(false);
        }
        if (secUser.getDoSaldoAwal().compareTo(Boolean.TRUE) == 0) {
            cbEditSaldoAwal.setSelected(true);
        } else {
            cbEditSaldoAwal.setSelected(false);
        }
    }

    private void LoadDataToTable() {
        listSecurityUser = Main.getSistemService().SecurityRecord();
        tblUserData.setModel(new SecurityTableModel(listSecurityUser));
        initColumnSize();
    }

    private boolean validateForm() {
        if (!txtNamaLengkap.getText().isEmpty() && !txtNamaLengkap.getText().equalsIgnoreCase("Tidak Boleh Kosong...")
                && !txtNamaLogin.getText().isEmpty() && !txtNamaLogin.getText().equalsIgnoreCase("Tidak Boleh Kosong...")
                && txtPassword.getPassword().length != 0
                && !txtNoTelp.getText().isEmpty() && !txtNoTelp.getText().equalsIgnoreCase("Tidak Boleh Kosong...")
                && !txtAlamat.getText().isEmpty() && !txtAlamat.getText().equalsIgnoreCase("Tidak Boleh Kosong...")
                && !txtTTL.getText().isEmpty() && !txtTTL.getText().equalsIgnoreCase("Tidak Boleh Kosong...")) {
            return true;
        }else if(txtNamaLengkap.getText().isEmpty()
                && txtNamaLogin.getText().isEmpty()
                && txtPassword.getPassword().length == 0
                && txtNoTelp.getText().isEmpty()
                && txtAlamat.getText().isEmpty()
                && txtTTL.getText().isEmpty()) {
            txtNamaLengkap.setText("Tidak Boleh Kosong...");
            txtNamaLengkap.setForeground(Color.red);
            txtNamaLogin.setText("Tidak Boleh Kosong...");
            txtNamaLogin.setForeground(Color.red);
            txtPassword.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
            txtAlamat.setText("Tidak Boleh Kosong...");
            txtAlamat.setForeground(Color.red);
            txtNoTelp.setText("Tidak Boleh Kosong...");
            txtNoTelp.setForeground(Color.red);
            txtTTL.setText("Tidak Boleh Kosong...");
            txtTTL.setForeground(Color.red);
            return false;

        } else if (txtNamaLengkap.getText().isEmpty()) {
            txtNamaLengkap.setText("Tidak Boleh Kosong...");
            txtNamaLengkap.setForeground(Color.red);
            txtNamaLengkap.requestFocus();
            return false;
        } else if (txtNamaLogin.getText().isEmpty()) {
            txtNamaLogin.setText("Tidak Boleh Kosong...");
            txtNamaLogin.setForeground(Color.red);
            txtNamaLogin.requestFocus();
            return false;
        } else if (txtPassword.getPassword().length == 0) {
            txtPassword.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
            txtNamaLengkap.requestFocus();
            return false;
        } else if (txtNoTelp.getText().isEmpty()) {
            txtNoTelp.setText("Tidak Boleh Kosong...");
            txtNoTelp.setForeground(Color.red);
            txtNoTelp.requestFocus();
            return false;
        } else if (txtAlamat.getText().isEmpty()) {
            txtAlamat.setText("Tidak Boleh Kosong...");
            txtAlamat.setForeground(Color.red);
            txtAlamat.requestFocus();
            return false;
        } else if (txtTTL.getText().isEmpty()) {
            txtTTL.setText("Tidak Boleh Kosong...");
            txtTTL.setForeground(Color.red);
            txtTTL.requestFocus();
            return false;
        } else {
            return false;
        }
    }

    private void initColumnSize() {
        tblUserData.getColumnModel().getColumn(0).setMinWidth(200);
        tblUserData.getColumnModel().getColumn(0).setMaxWidth(200);
        tblUserData.getColumnModel().getColumn(1).setMinWidth(100);
        tblUserData.getColumnModel().getColumn(1).setMaxWidth(100);
        tblUserData.getColumnModel().getColumn(2).setMinWidth(100);
        tblUserData.getColumnModel().getColumn(2).setMaxWidth(100);
        tblUserData.getColumnModel().getColumn(3).setMinWidth(150);
        tblUserData.getColumnModel().getColumn(3).setMaxWidth(200);
        tblUserData.getColumnModel().getColumn(4).setMinWidth(120);
        tblUserData.getColumnModel().getColumn(4).setMaxWidth(150);
        tblUserData.getColumnModel().getColumn(5).setMinWidth(150);
        tblUserData.getColumnModel().getColumn(5).setMaxWidth(200);
        tblUserData.getColumnModel().getColumn(6).setMinWidth(120);
        tblUserData.getColumnModel().getColumn(6).setMaxWidth(150);
        tblUserData.getColumnModel().getColumn(7).setMinWidth(120);
        tblUserData.getColumnModel().getColumn(7).setMaxWidth(150);
        tblUserData.getColumnModel().getColumn(8).setMinWidth(120);
        tblUserData.getColumnModel().getColumn(8).setMaxWidth(150);
    }

    public void valueChanged(ListSelectionEvent e) {
        if (tblUserData.getSelectedRow() >= 0) {
            secUser = listSecurityUser.get(tblUserData.getSelectedRow());
            LoadDatabaseToForm();
            enableForm(false);
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

        jFileChooser1 = new javax.swing.JFileChooser();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        jSplitPane3 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNamaLengkap = new javax.swing.JTextField();
        dateUpdate = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtNamaLogin = new javax.swing.JTextField();
        txtTTL = new javax.swing.JTextField();
        txtAlamat = new javax.swing.JTextField();
        txtNoTelp = new javax.swing.JTextField();
        cmbStatus = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        cbMenuTransaksi = new javax.swing.JCheckBox();
        cbOpenTransaksi = new javax.swing.JCheckBox();
        cbDoClosing = new javax.swing.JCheckBox();
        cbTambahUser = new javax.swing.JCheckBox();
        cbKomposisiTransaksi = new javax.swing.JCheckBox();
        cbEditSaldoBonus = new javax.swing.JCheckBox();
        cbEditDataSistem = new javax.swing.JCheckBox();
        cbEditSaldoAwal = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUserData = new javax.swing.JTable();
        buttonMaster = new paperman.master.buttonMaster();

        setTitle("Form User Login");

        jPanel1.setBackground(new java.awt.Color(0, 51, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Pengaturan User");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(1238, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jSplitPane2.setDividerLocation(480);

        jSplitPane3.setDividerSize(1);
        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel3.setBackground(new java.awt.Color(0, 51, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Informasi User Login");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(316, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setText("Nama Lengkap");

        jLabel4.setText("Nama Login");

        txtNamaLengkap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNamaLengkapMouseClicked(evt);
            }
        });
        txtNamaLengkap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNamaLengkapKeyReleased(evt);
            }
        });

        jLabel5.setText("Tgl Update");

        txtPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPasswordMouseClicked(evt);
            }
        });
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPasswordKeyReleased(evt);
            }
        });

        jLabel15.setText("Password");

        jLabel16.setText("Tempat/Tgl Lahir");

        jLabel17.setText("Alamat");

        jLabel18.setText("No. Telp");

        jLabel19.setText("Status");

        txtNamaLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNamaLoginMouseClicked(evt);
            }
        });

        txtTTL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTTLMouseClicked(evt);
            }
        });

        txtAlamat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtAlamatMouseClicked(evt);
            }
        });

        txtNoTelp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNoTelpMouseClicked(evt);
            }
        });

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SUPER ADMIN", "ADMIN", "KEUANGAN", "KASIR", "OPERATOR", "MANUAL" }));
        cmbStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbStatusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addContainerGap(408, Short.MAX_VALUE))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel19)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(jLabel5))
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(dateUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                        .addGap(196, 196, 196))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txtAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtTTL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNoTelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNamaLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNamaLengkap, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(cmbStatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(119, 119, 119)))
                        .addContainerGap(77, Short.MAX_VALUE))))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtAlamat, txtNamaLengkap, txtNamaLogin, txtNoTelp, txtPassword, txtTTL});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtNamaLengkap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtNamaLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtTTL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txtAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoTelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dateUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSplitPane3.setTopComponent(jPanel2);

        jPanel5.setBackground(new java.awt.Color(0, 51, 255));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Informasi Hak Akses");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addContainerGap(334, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel7.setText("Melihat Semua Menu Transaksi");

        jLabel8.setText("Membuka Closed Transaksi");

        jLabel9.setText("Melakukan Closing Transaksi");

        jLabel10.setText("Menambah User Baru");

        jLabel11.setText("Mengubah Komposisi Transaksi");

        jLabel12.setText("Mengubah Saldo Bonus");

        jLabel13.setText("Mengubah Data Sistem");

        jLabel14.setText("Mengubah Saldo Awal");

        cbMenuTransaksi.setFont(new java.awt.Font("Tahoma", 1, 11));
        cbMenuTransaksi.setText("Ya");
        cbMenuTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbMenuTransaksiActionPerformed(evt);
            }
        });

        cbOpenTransaksi.setFont(new java.awt.Font("Tahoma", 1, 11));
        cbOpenTransaksi.setText("Ya");
        cbOpenTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbOpenTransaksiActionPerformed(evt);
            }
        });

        cbDoClosing.setFont(new java.awt.Font("Tahoma", 1, 11));
        cbDoClosing.setText("Ya");
        cbDoClosing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbDoClosingActionPerformed(evt);
            }
        });

        cbTambahUser.setFont(new java.awt.Font("Tahoma", 1, 11));
        cbTambahUser.setText("Ya");
        cbTambahUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTambahUserActionPerformed(evt);
            }
        });

        cbKomposisiTransaksi.setFont(new java.awt.Font("Tahoma", 1, 11));
        cbKomposisiTransaksi.setText("Ya");
        cbKomposisiTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbKomposisiTransaksiActionPerformed(evt);
            }
        });

        cbEditSaldoBonus.setFont(new java.awt.Font("Tahoma", 1, 11));
        cbEditSaldoBonus.setText("Ya");
        cbEditSaldoBonus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbEditSaldoBonusActionPerformed(evt);
            }
        });

        cbEditDataSistem.setFont(new java.awt.Font("Tahoma", 1, 11));
        cbEditDataSistem.setText("Ya");
        cbEditDataSistem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbEditDataSistemActionPerformed(evt);
            }
        });

        cbEditSaldoAwal.setFont(new java.awt.Font("Tahoma", 1, 11));
        cbEditSaldoAwal.setText("Ya");
        cbEditSaldoAwal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbEditSaldoAwalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addGap(17, 17, 17)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbMenuTransaksi)
                    .addComponent(cbOpenTransaksi)
                    .addComponent(cbDoClosing)
                    .addComponent(cbTambahUser)
                    .addComponent(cbKomposisiTransaksi)
                    .addComponent(cbEditSaldoBonus))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbEditDataSistem)
                    .addComponent(cbEditSaldoAwal))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(cbMenuTransaksi))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(cbOpenTransaksi))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbDoClosing)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbTambahUser)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbKomposisiTransaksi)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbEditSaldoBonus)
                            .addComponent(jLabel12)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbEditDataSistem)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbEditSaldoAwal)
                            .addComponent(jLabel14))))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        jSplitPane3.setRightComponent(jPanel4);

        jSplitPane2.setLeftComponent(jSplitPane3);

        tblUserData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "  Nama", "  Akses Menu", "  Akses Closing", "  Akses Closed Transaksi", "  Akses Ubah User", "  Akses Komposisi Transaksi", "  Akses Saldo Bonus", "  Akses Data Sistem", "  Akses Saldo Awal"
            }
        ));
        tblUserData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblUserData.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblUserData);
        tblUserData.getColumnModel().getColumn(0).setMinWidth(200);
        tblUserData.getColumnModel().getColumn(0).setMaxWidth(200);
        tblUserData.getColumnModel().getColumn(1).setMinWidth(100);
        tblUserData.getColumnModel().getColumn(1).setMaxWidth(100);
        tblUserData.getColumnModel().getColumn(2).setMinWidth(100);
        tblUserData.getColumnModel().getColumn(2).setMaxWidth(100);
        tblUserData.getColumnModel().getColumn(3).setMinWidth(150);
        tblUserData.getColumnModel().getColumn(3).setMaxWidth(200);
        tblUserData.getColumnModel().getColumn(4).setMinWidth(120);
        tblUserData.getColumnModel().getColumn(4).setMaxWidth(150);
        tblUserData.getColumnModel().getColumn(5).setMinWidth(150);
        tblUserData.getColumnModel().getColumn(5).setMaxWidth(200);
        tblUserData.getColumnModel().getColumn(6).setMinWidth(120);
        tblUserData.getColumnModel().getColumn(6).setMaxWidth(150);
        tblUserData.getColumnModel().getColumn(7).setMinWidth(120);
        tblUserData.getColumnModel().getColumn(7).setMaxWidth(150);
        tblUserData.getColumnModel().getColumn(8).setMinWidth(120);
        tblUserData.getColumnModel().getColumn(8).setMaxWidth(150);

        jSplitPane2.setRightComponent(jScrollPane1);

        jSplitPane1.setBottomComponent(jSplitPane2);

        buttonMaster.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jSplitPane1.setLeftComponent(buttonMaster);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1368, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 632, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbMenuTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbMenuTransaksiActionPerformed
        // TODO add your handling code here:
        if (cbMenuTransaksi.isSelected()) {
            cbMenuTransaksi.setText("YA");
            cbMenuTransaksi.setFont(new Font("Tahoma", Font.BOLD, 14));
        } else {
            cbMenuTransaksi.setText("ya");
            cbMenuTransaksi.setFont(new Font("Tahoma", Font.PLAIN, 14));
        }
    }//GEN-LAST:event_cbMenuTransaksiActionPerformed

    private void cbOpenTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbOpenTransaksiActionPerformed
        // TODO add your handling code here:
        if (cbOpenTransaksi.isSelected()) {
            cbOpenTransaksi.setText("YA");
            cbOpenTransaksi.setFont(new Font("Tahoma", Font.BOLD, 14));
        } else {
            cbOpenTransaksi.setText("ya");
            cbOpenTransaksi.setFont(new Font("Tahoma", Font.PLAIN, 14));
        }
    }//GEN-LAST:event_cbOpenTransaksiActionPerformed

    private void cbDoClosingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbDoClosingActionPerformed
        // TODO add your handling code here:
        if (cbDoClosing.isSelected()) {
            cbDoClosing.setText("YA");
            cbDoClosing.setFont(new Font("Tahoma", Font.BOLD, 14));
        } else {
            cbDoClosing.setText("ya");
            cbDoClosing.setFont(new Font("Tahoma", Font.PLAIN, 14));
        }
    }//GEN-LAST:event_cbDoClosingActionPerformed

    private void cbTambahUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTambahUserActionPerformed
        // TODO add your handling code here:
        if (cbTambahUser.isSelected()) {
            cbTambahUser.setText("YA");
            cbTambahUser.setFont(new Font("Tahoma", Font.BOLD, 14));
        } else {
            cbTambahUser.setText("ya");
            cbTambahUser.setFont(new Font("Tahoma", Font.PLAIN, 14));
        }
    }//GEN-LAST:event_cbTambahUserActionPerformed

    private void cbKomposisiTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbKomposisiTransaksiActionPerformed
        // TODO add your handling code here:
        if (cbKomposisiTransaksi.isSelected()) {
            cbKomposisiTransaksi.setText("YA");
            cbKomposisiTransaksi.setFont(new Font("Tahoma", Font.BOLD, 14));
        } else {
            cbKomposisiTransaksi.setText("ya");
            cbKomposisiTransaksi.setFont(new Font("Tahoma", Font.PLAIN, 14));
        }
    }//GEN-LAST:event_cbKomposisiTransaksiActionPerformed

    private void cbEditSaldoBonusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbEditSaldoBonusActionPerformed
        // TODO add your handling code here:
        if (cbEditSaldoBonus.isSelected()) {
            cbEditSaldoBonus.setText("YA");
            cbEditSaldoBonus.setFont(new Font("Tahoma", Font.BOLD, 14));
        } else {
            cbEditSaldoBonus.setText("ya");
            cbEditSaldoBonus.setFont(new Font("Tahoma", Font.PLAIN, 14));
        }
    }//GEN-LAST:event_cbEditSaldoBonusActionPerformed

    private void cbEditDataSistemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbEditDataSistemActionPerformed
        // TODO add your handling code here:
        if (cbEditDataSistem.isSelected()) {
            cbEditDataSistem.setText("YA");
            cbEditDataSistem.setFont(new Font("Tahoma", Font.BOLD, 14));
        } else {
            cbEditDataSistem.setText("ya");
            cbEditDataSistem.setFont(new Font("Tahoma", Font.PLAIN, 14));
        }
    }//GEN-LAST:event_cbEditDataSistemActionPerformed

    private void cbEditSaldoAwalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbEditSaldoAwalActionPerformed
        // TODO add your handling code here:
        if (cbEditSaldoAwal.isSelected()) {
            cbEditSaldoAwal.setText("YA");
            cbEditSaldoAwal.setFont(new Font("Tahoma", Font.BOLD, 14));
        } else {
            cbEditSaldoAwal.setText("ya");
            cbEditSaldoAwal.setFont(new Font("Tahoma", Font.PLAIN, 14));
        }
    }//GEN-LAST:event_cbEditSaldoAwalActionPerformed

    private void cmbStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbStatusActionPerformed
        // TODO add your handling code here:
        if (cmbStatus.getSelectedIndex() == 0) {
            cbLoadState(0);
            roleControl(false);
        }
        if (cmbStatus.getSelectedIndex() == 1) {
            cbLoadState(1);
            roleControl(false);
        }
        if (cmbStatus.getSelectedIndex() == 2) {
            cbLoadState(2);
            roleControl(false);
        }
        if (cmbStatus.getSelectedIndex() == 3) {
            cbLoadState(3);
            roleControl(false);
        }
        if (cmbStatus.getSelectedIndex() == 4) {
            cbLoadState(4);
            roleControl(false);
        }
        if (cmbStatus.getSelectedIndex() == 5) {
            roleControl(true);
        }
        System.out.println("cmbstatus Clicked");
    }//GEN-LAST:event_cmbStatusActionPerformed

    private void txtNamaLengkapKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNamaLengkapKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaLengkapKeyReleased

    private void txtNamaLengkapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNamaLengkapMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 1 && txtNamaLengkap.getText().equalsIgnoreCase("Tidak Boleh Kosong...")) {
            txtNamaLengkap.setText("");
            txtNamaLengkap.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtNamaLengkapMouseClicked

    private void txtNamaLoginMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNamaLoginMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 1 && txtNamaLogin.getText().equalsIgnoreCase("Tidak Boleh Kosong...")) {
            txtNamaLogin.setText("");
            txtNamaLogin.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtNamaLoginMouseClicked

    private void txtPasswordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPasswordMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPasswordMouseClicked

    private void txtPasswordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyReleased
        // TODO add your handling code here:
            txtPassword.setBorder(BorderFactory.createEtchedBorder());
    }//GEN-LAST:event_txtPasswordKeyReleased

    private void txtTTLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTTLMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 1 && txtTTL.getText().equalsIgnoreCase("Tidak Boleh Kosong...")) {
            txtTTL.setText("");
            txtTTL.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtTTLMouseClicked

    private void txtAlamatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAlamatMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 1 && txtAlamat.getText().equalsIgnoreCase("Tidak Boleh Kosong...")) {
            txtAlamat.setText("");
            txtAlamat.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtAlamatMouseClicked

    private void txtNoTelpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNoTelpMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 1 && txtNoTelp.getText().equalsIgnoreCase("Tidak Boleh Kosong...")) {
            txtNoTelp.setText("");
            txtNoTelp.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtNoTelpMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private paperman.master.buttonMaster buttonMaster;
    private javax.swing.JCheckBox cbDoClosing;
    private javax.swing.JCheckBox cbEditDataSistem;
    private javax.swing.JCheckBox cbEditSaldoAwal;
    private javax.swing.JCheckBox cbEditSaldoBonus;
    private javax.swing.JCheckBox cbKomposisiTransaksi;
    private javax.swing.JCheckBox cbMenuTransaksi;
    private javax.swing.JCheckBox cbOpenTransaksi;
    private javax.swing.JCheckBox cbTambahUser;
    private javax.swing.JComboBox cmbStatus;
    private com.toedter.calendar.JDateChooser dateUpdate;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JTable tblUserData;
    private javax.swing.JTextField txtAlamat;
    private javax.swing.JTextField txtNamaLengkap;
    private javax.swing.JTextField txtNamaLogin;
    private javax.swing.JTextField txtNoTelp;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtTTL;
    // End of variables declaration//GEN-END:variables
}

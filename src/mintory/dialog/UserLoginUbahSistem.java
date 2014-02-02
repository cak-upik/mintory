/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * UserLoginUbahSistem.java
 *
 * Created on Jun 18, 2012, 12:03:12 PM
 */
package mintory.dialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import mintory.Main;
import mintory.TextComponentUtils;
import mintory.model.SecurityConstants;
import mintory.model.SecurityUser;
import mintory.model.sistem;

/**
 *
 * @author i1440ns
 */
public class UserLoginUbahSistem extends javax.swing.JDialog {

    private UserLoginUbahSistem mainLogin;
    private sistem sistemModel;
    private SecurityUser secUser;
    private List<SecurityUser> listSecurityData;
    public static boolean isAdmin = false;
    private String loginAs;
    private boolean isEmptyPass = false;

    /** Creates new form UserLoginUbahSistem */
    public UserLoginUbahSistem() {
        super(Main.getMainMenu(), true);
        initComponents();
        this.setLocationRelativeTo(null);
        LblEror.setVisible(false);
        TextComponentUtils.setAutoUpperCaseText(txtUser);
    }

    public sistem showDialog(final String login) {
        this.loginAs = login;
        this.setVisible(true);
        return sistemModel;
    }

    public UserLoginUbahSistem showMainLoginDialog(final String login) {
        this.loginAs = login;
        this.setVisible(true);
        return mainLogin;
    }

    private void cekLogin() {
        secUser = Main.getSistemService().findByUsername(txtUser.getText());
        sistemModel = Main.getSistemService().sistemRecord();
        if (this.loginAs == null ? SecurityConstants.LOGIN_SYSTEM == null : loginAs.equals(SecurityConstants.LOGIN_SYSTEM)) {
            if (secUser != null && txtUser.getText().compareTo(secUser.getNamaLogin()) == 0 && Arrays.equals(passField.getPassword(), secUser.getPass().toCharArray())) {
                JOptionPane.showMessageDialog(this, "Login Sebagai " + secUser.getNamaLogin() + " Berhasil", "Pesan Sistem", JOptionPane.INFORMATION_MESSAGE);
                Main.getMainMenu().LoginSuksesState(secUser);
                Main.getMainMenu().setUser(secUser.getNamaLgkp());
                System.out.println("last logged in= "+secUser.getNamaLogin());
                sistemModel.setId(sistemModel.getId());
                sistemModel.setLastLoginUser(secUser);
//                sistemModel.setLicenseFor(SecurityConstants.AUTHENTICATE_FOR_LOGIN_SYSTEM);
                Main.getSistemService().save(sistemModel);
                txtUser.setText("");
                passField.setText("");
                this.dispose();
                secUser = null;
            } else if (secUser == null) {
                JOptionPane.showMessageDialog(this, "Username " + txtUser.getText() + " Belum Terdaftar", "Pesan Sistem", JOptionPane.ERROR_MESSAGE);
                Main.getMainMenu().MainLoginState();
                txtUser.setText("");
                passField.setText("");
                txtUser.requestFocus();
            } else {
                JOptionPane.showMessageDialog(this, "Login Sebagai " + secUser.getNamaLogin() + " Gagal\nPeriksa Lagi Password Anda", "Pesan Sistem", JOptionPane.ERROR_MESSAGE);
                Main.getMainMenu().MainLoginState();
                passField.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
                passField.requestFocus();
                isEmptyPass = true;
            }
        }
        if (this.loginAs == null ? SecurityConstants.AUTHENTICATE_SYSTEM == null : loginAs.equals(SecurityConstants.AUTHENTICATE_SYSTEM)) {
            if (secUser != null && txtUser.getText().compareTo(secUser.getNamaLogin()) == 0 && Arrays.equals(passField.getPassword(), secUser.getPass().toCharArray())) {
                JOptionPane.showMessageDialog(this, "Login Sebagai " + secUser.getNamaLogin() + " Berhasil", "Pesan Sistem", JOptionPane.INFORMATION_MESSAGE);
                sistemModel.setSecurityUser(secUser);
//                sistemModel.setLicenseFor(SecurityConstants.AUTHENTICATE_FOR_EDIT_SYSTEM);
//                Main.getSistemService().save(sistemModel);
                txtUser.setText("");
                passField.setText("");
                this.dispose();
                secUser = null;
            } else if (secUser == null) {
                JOptionPane.showMessageDialog(this, "Username " + txtUser.getText() + " Belum Terdaftar", "Pesan Sistem", JOptionPane.ERROR_MESSAGE);
                txtUser.setText("");
                passField.setText("");
                txtUser.requestFocus();
            } else {
                JOptionPane.showMessageDialog(this, "Login Sebagai " + secUser.getNamaLogin() + " Gagal\nPeriksa Lagi Password Anda", "Pesan Sistem", JOptionPane.ERROR_MESSAGE);
                passField.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
                passField.requestFocus();
                isEmptyPass = true;
            }
        }
    }

    private void kenaTilang() {
        if (txtUser.getText().isEmpty() && passField.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Login Error\nHarap Isi User Name & Password", "Pesan Sistem", JOptionPane.ERROR_MESSAGE);
            txtUser.setText("Tidak Boleh Kosong...");
            txtUser.setForeground(Color.red);
            txtUser.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
            txtUser.requestFocus();
            isEmptyPass = true;
            passField.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
        } else if (txtUser.getText().isEmpty() || txtUser.getText().equalsIgnoreCase("Tidak Boleh Kosong...")) {
            JOptionPane.showMessageDialog(this, "Login Error\nHarap Isi User Name", "Pesan Sistem", JOptionPane.ERROR_MESSAGE);
            txtUser.setText("Tidak Boleh Kosong...");
            txtUser.setForeground(Color.red);
            txtUser.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
            txtUser.requestFocus();
        } else if (passField.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Login Error\nHarap Isi Password", "Pesan Sistem", JOptionPane.ERROR_MESSAGE);
            isEmptyPass = true;
            passField.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
            passField.requestFocus();
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

        txtUser = new javax.swing.JTextField();
        passField = new javax.swing.JPasswordField();
        loginAction = new javax.swing.JLabel();
        LblEror = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Login User");
        setIconImage(null);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtUserMouseClicked(evt);
            }
        });
        txtUser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUserKeyReleased(evt);
            }
        });
        getContentPane().add(txtUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 130, 140, 30));

        passField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                passFieldKeyReleased(evt);
            }
        });
        getContentPane().add(passField, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 170, 140, 30));

        loginAction.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loginAction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paperman/images/login-button.png"))); // NOI18N
        loginAction.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loginAction.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        loginAction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loginActionMouseClicked(evt);
            }
        });
        getContentPane().add(loginAction, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 210, -1, -1));

        LblEror.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        LblEror.setForeground(new java.awt.Color(255, 0, 0));
        LblEror.setText("Harap Isi User Login");
        getContentPane().add(LblEror, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 100, -1, -1));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paperman/images/loginBG.png"))); // NOI18N
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loginActionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loginActionMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 1) {
            if (!txtUser.getText().isEmpty() && !txtUser.getText().equalsIgnoreCase("Tidak Boleh Kosong...") && passField.getPassword().length != 0) {
                cekLogin();
            } else {
//                LblEror.setVisible(true);
                kenaTilang();
            }
        }
    }//GEN-LAST:event_loginActionMouseClicked

    private void passFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passFieldKeyReleased
        // TODO add your handling code here:
        if (isEmptyPass) {
            passField.setBorder(BorderFactory.createEtchedBorder());
        }
        if (evt.getKeyChar() == '\n') {
            loginActionMouseClicked(new MouseEvent((Component) evt.getSource(), evt.getID(), evt.getWhen(), evt.getModifiers(), evt.getComponent().getX(), evt.getComponent().getY(), 1, false));
        }
    }//GEN-LAST:event_passFieldKeyReleased

    private void txtUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtUserMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 1) {
            if (txtUser.getText().equalsIgnoreCase("Tidak Boleh Kosong...")) {
                txtUser.setText("");
                txtUser.setForeground(Color.BLACK);
                txtUser.setBorder(BorderFactory.createEtchedBorder());
            }
        }
    }//GEN-LAST:event_txtUserMouseClicked

    private void txtUserKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUserKeyReleased
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LblEror;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel loginAction;
    private javax.swing.JPasswordField passField;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables
}

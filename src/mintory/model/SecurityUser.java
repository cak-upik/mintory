
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mintory.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Nurul Chusna
 */
@Entity
@Table(name="sec_user")
public class SecurityUser implements Serializable {
    @Id
    @GeneratedValue()
    private Integer id;

    @Column(name="nama")
    private String namaLgkp;

    @Column(name="user_login")
    private String namaLogin;

    @Column(name="password")
    private String pass;

    @Column(name="alamat")
    private String alamat;

    @Column(name="TTL")
    private String ttl;

    @Column(name="no_telp")
    private String noTelp;

    @Column(name="user_status")
    @Enumerated(EnumType.STRING)
    private userStatus statusUsr;

    @Temporal(TemporalType.DATE)
    private Date tglUpdate;

    @Column(name="role_view_transaction")
    private Boolean viewAllTransactionMenu = Boolean.FALSE;

    @Column(name="role_open_closing")
    private Boolean openClosedTrans = Boolean.FALSE;

    @Column(name="role_do_closing")
    private Boolean doClosing = Boolean.FALSE;

    @Column(name="role_add_user")
    private Boolean addUser = Boolean.FALSE;

    @Column(name="role_change_compose")
    private Boolean changeKomposisi = Boolean.FALSE;

    @Column(name="role_change_bonus")
    private Boolean changeBonus = Boolean.FALSE;

    @Column(name="role_change_sistem")
    private Boolean changeDataSistem = Boolean.FALSE;

    @Column(name="role_change_saldoAwal")
    private Boolean doSaldoAwal = Boolean.FALSE;

    public Boolean getAddUser() {
        return addUser;
    }

    public void setAddUser(Boolean addUser) {
        this.addUser = addUser;
    }

    public Boolean getChangeBonus() {
        return changeBonus;
    }

    public void setChangeBonus(Boolean changeBonus) {
        this.changeBonus = changeBonus;
    }

    public Boolean getChangeDataSistem() {
        return changeDataSistem;
    }

    public void setChangeDataSistem(Boolean changeDataSistem) {
        this.changeDataSistem = changeDataSistem;
    }

    public Boolean getChangeKomposisi() {
        return changeKomposisi;
    }

    public void setChangeKomposisi(Boolean changeKomposisi) {
        this.changeKomposisi = changeKomposisi;
    }

    public Boolean getDoClosing() {
        return doClosing;
    }

    public void setDoClosing(Boolean doClosing) {
        this.doClosing = doClosing;
    }

    public Boolean getDoSaldoAwal() {
        return doSaldoAwal;
    }

    public void setDoSaldoAwal(Boolean doSaldoAwal) {
        this.doSaldoAwal = doSaldoAwal;
    }

    public Boolean getOpenClosedTrans() {
        return openClosedTrans;
    }

    public void setOpenClosedTrans(Boolean openClosedTrans) {
        this.openClosedTrans = openClosedTrans;
    }

    public Boolean getViewAllTransactionMenu() {
        return viewAllTransactionMenu;
    }

    public void setViewAllTransactionMenu(Boolean viewTransaction) {
        this.viewAllTransactionMenu = viewTransaction;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNamaLgkp() {
        return namaLgkp;
    }

    public void setNamaLgkp(String namaLgkp) {
        this.namaLgkp = namaLgkp;
    }

    public String getNamaLogin() {
        return namaLogin;
    }

    public void setNamaLogin(String namaLogin) {
        this.namaLogin = namaLogin;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public userStatus getStatusUsr() {
        return statusUsr;
    }

    public void setStatusUsr(userStatus statusUsr) {
        this.statusUsr = statusUsr;
    }

    public Date getTglUpdate() {
        return tglUpdate;
    }

    public void setTglUpdate(Date tglUpdate) {
        this.tglUpdate = tglUpdate;
    }

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }
}

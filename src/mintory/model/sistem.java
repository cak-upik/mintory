/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mintory.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 *
 * @author i1440ns
 */
@Entity
@Table(name="sistem")
public class sistem implements Serializable {
    @Id
    private Integer id;

    @Temporal(TemporalType.DATE)
    private Date tglKerja;

    @Column(name="nama_perusahaan")
    private String namaPerusahaan;

    @Column(name="alamat_perusahaan")
    private String alamatPerusahaan;

    @Column(name="kota")
    private String kota;

    @Column(name="no_telp")
    private String telp;

    @Column(name="email")
    private String email;

//    @Column(name="auth_for")
//    private String licenseFor;

    @OneToMany(mappedBy="idSistem")
    @Cascade(CascadeType.ALL)
    private List<komposisiSetoran> listKomposisi = new ArrayList<komposisiSetoran>();

    @ManyToOne()
    @JoinColumn(name="last_editedBy", referencedColumnName="user_login")
    private SecurityUser securityUser;

    @ManyToOne()
    @JoinColumn(name="last_logged_in", referencedColumnName="user_login")
    private SecurityUser lastLoginUser;

    public SecurityUser getLastLoginUser() {
        return lastLoginUser;
    }

    public void setLastLoginUser(SecurityUser lastLoginUser) {
        this.lastLoginUser = lastLoginUser;
    }

    public SecurityUser getSecurityUser() {
        return securityUser;
    }

    public void setSecurityUser(SecurityUser securityUser) {
        this.securityUser = securityUser;
    }

    public String getAlamatPerusahaan() {
        return alamatPerusahaan;
    }

    public void setAlamatPerusahaan(String alamatPerusahaan) {
        this.alamatPerusahaan = alamatPerusahaan;
    }

    public List<komposisiSetoran> getListKomposisi() {
        return listKomposisi;
    }

    public void setListKomposisi(List<komposisiSetoran> listKomposisi) {
        this.listKomposisi = listKomposisi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getNamaPerusahaan() {
        return namaPerusahaan;
    }

    public void setNamaPerusahaan(String namaPerusahaan) {
        this.namaPerusahaan = namaPerusahaan;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public Date getTglKerja() {
        return tglKerja;
    }

    public void setTglKerja(Date tglKerja) {
        this.tglKerja = tglKerja;
    }
}

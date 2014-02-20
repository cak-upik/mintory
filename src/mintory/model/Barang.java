/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mintory.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author i1440ns
 */
@Entity
@Table(name="barang")
public class Barang implements Serializable  {

    @Id @GeneratedValue
    @Column(name="id")
    private Integer id;
    @Column(name="kode_brg", unique=true, nullable=false)
    private String kodeBarang;
    @Column(name="namaBarang")
    private String namaBarang;
    @Column(name="satuan")
    private String satuan;
    @Temporal(TemporalType.DATE)
    private Date tglMasuk;
    @Column(name="keterangan")
    private String keterangan;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKodeBarang() {
        return kodeBarang;
    }

    public void setKodeBarang(String kodeBarang) {
        this.kodeBarang = kodeBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public Date getTglMasuk() {
        return tglMasuk;
    }

    public void setTglMasuk(Date tglMasuk) {
        this.tglMasuk = tglMasuk;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 97 * hash + (this.kodeBarang != null ? this.kodeBarang.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Barang other = (Barang) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.kodeBarang == null) ? (other.kodeBarang != null) : !this.kodeBarang.equals(other.kodeBarang)) {
            return false;
        }
        return true;
    }
}

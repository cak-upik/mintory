/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paperman.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author i1440ns
 */
@Entity
@Table(name = "pengemudi")
public class pengemudi implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "id_ref_kendaraan", unique = true, nullable = false)
    private kendaraan kend;
    @Column(name = "nrp", unique = true, nullable = false)
    private String nrp;
    @Column(name = "nama")
    private String nama;
    @Column(name = "alamat")
    private String alamat;
    @Column(name = "kota")
    private String kota;
    @Temporal(TemporalType.DATE)
    private Date tglMasuk;
    @Column(name = "kontrakBulan", precision=11)
    private Integer kontrakBulan;
    @Column(name ="kontrakHari", precision=25)
    private Integer kontrakHari;

    public kendaraan getKend() {
        return kend;
    }

    public void setKend(kendaraan kend) {
        this.kend = kend;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getKontrakBulan() {
        return kontrakBulan;
    }

    public void setKontrakBulan(Integer kontrakBulan) {
        this.kontrakBulan = kontrakBulan;
    }

    public Integer getKontrakHari() {
        return kontrakHari;
    }

    public void setKontrakHari(Integer kontrakHari) {
        this.kontrakHari = kontrakHari;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNrp() {
        return nrp;
    }

    public void setNrp(String nrp) {
        this.nrp = nrp;
    }

    public Date getTglMasuk() {
        return tglMasuk;
    }

    public void setTglMasuk(Date tglMasuk) {
        this.tglMasuk = tglMasuk;
    }
}

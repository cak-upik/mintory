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
@Table(name = "pengemudi_pth")
public class pengemudiPutih implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id_pth")
    private Integer idPutih;
    @ManyToOne
    @JoinColumn(name = "id_ref_kendaraan", unique = true, nullable = false)
    private kendaraanPutih kendPutih;
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

    public kendaraanPutih getKendPutih() {
        return kendPutih;
    }

    public void setKendPutih(kendaraanPutih kendPutih) {
        this.kendPutih = kendPutih;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public Integer getId() {
        return idPutih;
    }

    public void setId(Integer id) {
        this.idPutih = id;
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

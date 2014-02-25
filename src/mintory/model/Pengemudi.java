/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mintory.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author i1440ns
 */
@Entity
@Table(name = "pengemudi")
public class Pengemudi implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    @Column(name = "nrp", unique = true, nullable = false)
    private String nrp;
    @Column(name = "lambung")
    private String noLB;
    @Column(name = "nama")
    private String nama;
    @Column(name = "alamat")
    private String alamat;
    @Column(name = "kota")
    private String kota;
    @Column(name ="ket")
    private String keterangan;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNoLB() {
        return noLB;
    }

    public void setNoLB(String noLB) {
        this.noLB = noLB;
    }
    
    public String getNrp() {
        return nrp;
    }

    public void setNrp(String nrp) {
        this.nrp = nrp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}

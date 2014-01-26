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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author i1440ns
 */
@Entity
@Table(name="kendaraan_pth")
public class kendaraanPutih implements Serializable  {

    @Id @GeneratedValue
    @Column(name="id_pth")
    private Integer idPutih;
    @Column(name="noLambung", unique=true, nullable=false)
    private Integer noLambung;
    @Column(name="noPolisi")
    private String noPolisi;
    @Column(name="noMesin")
    private String noMesin;
    @Column(name="noRangka")
    private String noRangka;
    @Column(name="noSTNK")
    private String noSTNK;
    @Temporal(TemporalType.DATE)
    private Date tglJatuhTempo;
    @Column(name="keterangan")
    private String keterangan;

    public Integer getId() {
        return idPutih;
    }

    public void setId(Integer id) {
        this.idPutih = id;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public Integer getNoLambung() {
        return noLambung;
    }

    public void setNoLambung(Integer noLambung) {
        this.noLambung = noLambung;
    }

    public String getNoMesin() {
        return noMesin;
    }

    public void setNoMesin(String noMesin) {
        this.noMesin = noMesin;
    }

    public String getNoPolisi() {
        return noPolisi;
    }

    public void setNoPolisi(String noPolisi) {
        this.noPolisi = noPolisi;
    }

    public String getNoRangka() {
        return noRangka;
    }

    public void setNoRangka(String noRangka) {
        this.noRangka = noRangka;
    }

    public String getNoSTNK() {
        return noSTNK;
    }

    public void setNoSTNK(String noSTNK) {
        this.noSTNK = noSTNK;
    }

    public Date getTglJatuhTempo() {
        return tglJatuhTempo;
    }

    public void setTglJatuhTempo(Date tglJatuhTempo) {
        this.tglJatuhTempo = tglJatuhTempo;
    }
}

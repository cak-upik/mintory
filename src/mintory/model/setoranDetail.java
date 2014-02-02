/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mintory.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author i1440ns
 */
@Entity
@Table(name="tr_setoran_detail")
public class setoranDetail implements Serializable {
    @Id @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name="id_setoran", nullable=false)
    private setoran setor_map;

    @ManyToOne
    @JoinColumn(name="id_kendaraan", nullable=false)
    private kendaraan kend;

    @ManyToOne
    @JoinColumn(name="id_pengemudi", nullable=false)
    private pengemudi kemudi;

    @Column(name="angsuran")
    private BigDecimal angsuran = BigDecimal.ZERO;

    @Column(name="tabungan")
    private BigDecimal tabungan = BigDecimal.ZERO;

    @Column(name="kasbon")
    private BigDecimal kasbon = BigDecimal.ZERO;

    @Column(name="pembayaran")
    private BigDecimal bayar = BigDecimal.ZERO;

    @Column(name="over_time")
    private BigDecimal ovtime = BigDecimal.ZERO;

    @Column(name="cicilanOther")
    private BigDecimal KS = BigDecimal.ZERO;

    @Column(name="keterangan")
    private String ket;

    public BigDecimal getKS() {
        return KS;
    }

    public void setKS(BigDecimal KS) {
        this.KS = KS;
    }

    public BigDecimal getAngsuran() {
        return angsuran;
    }

    public void setAngsuran(BigDecimal angsuran) {
        this.angsuran = angsuran;
    }

    public BigDecimal getBayar() {
        return bayar;
    }

    public void setBayar(BigDecimal bayar) {
        this.bayar = bayar;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getKasbon() {
        return kasbon;
    }

    public void setKasbon(BigDecimal kasbon) {
        this.kasbon = kasbon;
    }

    public pengemudi getKemudi() {
        return kemudi;
    }

    public void setKemudi(pengemudi kemudi) {
        this.kemudi = kemudi;
    }

    public kendaraan getKend() {
        return kend;
    }

    public void setKend(kendaraan kend) {
        this.kend = kend;
    }

    public String getKet() {
        return ket;
    }

    public void setKet(String ket) {
        this.ket = ket;
    }

    public BigDecimal getOvtime() {
        return ovtime;
    }

    public void setOvtime(BigDecimal ovtime) {
        this.ovtime = ovtime;
    }

    public setoran getSetor_map() {
        return setor_map;
    }

    public void setSetor_map(setoran setor_map) {
        this.setor_map = setor_map;
    }

    public BigDecimal getTabungan() {
        return tabungan;
    }

    public void setTabungan(BigDecimal tabungan) {
        this.tabungan = tabungan;
    }
}

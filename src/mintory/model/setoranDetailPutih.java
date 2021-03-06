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
@Table(name="tr_setoran_detail_pth")
public class setoranDetailPutih implements Serializable {
    @Id @GeneratedValue
    @Column(name="id_detail_pth")
    private Integer idPth;

    @ManyToOne
    @JoinColumn(name="id_setoran_pth", nullable=false)
    private setoranPutih setor_map_pth;

    @ManyToOne
    @JoinColumn(name="id_kendaraan_pth", nullable=false)
    private kendaraanPutih kendPutih;

    @ManyToOne
    @JoinColumn(name="id_pengemudi_pth", nullable=false)
    private Pengemudi kemudiPutih;

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
        return idPth;
    }

    public void setId(Integer id) {
        this.idPth = id;
    }

    public BigDecimal getKasbon() {
        return kasbon;
    }

    public void setKasbon(BigDecimal kasbon) {
        this.kasbon = kasbon;
    }

    public Pengemudi getKemudiPutih() {
        return kemudiPutih;
    }

    public void setKemudiPutih(Pengemudi kemudiPutih) {
        this.kemudiPutih = kemudiPutih;
    }

    public kendaraanPutih getKendPutih() {
        return kendPutih;
    }

    public void setKendPutih(kendaraanPutih kendPutih) {
        this.kendPutih = kendPutih;
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

    public setoranPutih getSetor_map_putih() {
        return setor_map_pth;
    }

    public void setSetor_map_putih(setoranPutih setor_map_pth) {
        this.setor_map_pth = setor_map_pth;
    }

    public BigDecimal getTabungan() {
        return tabungan;
    }

    public void setTabungan(BigDecimal tabungan) {
        this.tabungan = tabungan;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package paperman.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Nurul Chusna
 */
public class TandaTerimaReport {
    private Integer nLambung;
    private String kode;
    private String nama;
    private String nrp;
    private Integer setoranKe;
    private Date jatuhTempo;
    private Date spo;
    private BigDecimal angsuran;
    private BigDecimal tabungan;
    private BigDecimal kasbon;
    private BigDecimal bayarKasbon;
    private BigDecimal cicilanLain;
    private BigDecimal overtime;
    private BigDecimal totalAngsuran;
    private BigDecimal totalHutang;
    private String ket;

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public BigDecimal getAngsuran() {
        return angsuran;
    }

    public void setAngsuran(BigDecimal angsuran) {
        this.angsuran = angsuran;
    }

    public BigDecimal getBayarKasbon() {
        return bayarKasbon;
    }

    public void setBayarKasbon(BigDecimal bayarKasbon) {
        this.bayarKasbon = bayarKasbon;
    }

    public BigDecimal getCicilanLain() {
        return cicilanLain;
    }

    public void setCicilanLain(BigDecimal cicilanLain) {
        this.cicilanLain = cicilanLain;
    }

    public Date getJatuhTempo() {
        return jatuhTempo;
    }

    public void setJatuhTempo(Date jatuhTempo) {
        this.jatuhTempo = jatuhTempo;
    }

    public BigDecimal getKasbon() {
        return kasbon;
    }

    public void setKasbon(BigDecimal kasbon) {
        this.kasbon = kasbon;
    }

    public String getKet() {
        return ket;
    }

    public void setKet(String ket) {
        this.ket = ket;
    }

    public Integer getnLambung() {
        return nLambung;
    }

    public void setnLambung(Integer nLambung) {
        this.nLambung = nLambung;
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

    public BigDecimal getOvertime() {
        return overtime;
    }

    public void setOvertime(BigDecimal overtime) {
        this.overtime = overtime;
    }

    public Integer getSetoranKe() {
        return setoranKe;
    }

    public void setSetoranKe(Integer setoranKe) {
        this.setoranKe = setoranKe;
    }

    public Date getSpo() {
        return spo;
    }

    public void setSpo(Date spo) {
        this.spo = spo;
    }

    public BigDecimal getTabungan() {
        return tabungan;
    }

    public void setTabungan(BigDecimal tabungan) {
        this.tabungan = tabungan;
    }

    public BigDecimal getTotalAngsuran() {
        return totalAngsuran;
    }

    public void setTotalAngsuran(BigDecimal totalAngsuran) {
        this.totalAngsuran = totalAngsuran;
    }

    public BigDecimal getTotalHutang() {
        return totalHutang;
    }

    public void setTotalHutang(BigDecimal totalHutang) {
        this.totalHutang = totalHutang;
    }
}

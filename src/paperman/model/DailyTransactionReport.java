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
public class DailyTransactionReport {
    private Integer nLambung;
    private String nama;
    private String nrp;
    private Integer setKe;
    private Date tglJatuhTempo;
    private Date tglSPO;
    private BigDecimal angsuran;
    private BigDecimal tabungan;
    private BigDecimal kasbon;
    private BigDecimal bayarKasbon;
    private BigDecimal cicilanKas;
    private BigDecimal overtime;
    private BigDecimal totalAngsur;
    private String keterangan;

    public String getNrp() {
        return nrp;
    }

    public void setNrp(String nrp) {
        this.nrp = nrp;
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

    public BigDecimal getCicilanKas() {
        return cicilanKas;
    }

    public void setCicilanKas(BigDecimal cicilanKas) {
        this.cicilanKas = cicilanKas;
    }

    public BigDecimal getKasbon() {
        return kasbon;
    }

    public void setKasbon(BigDecimal kasbon) {
        this.kasbon = kasbon;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
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

    public BigDecimal getOvertime() {
        return overtime;
    }

    public void setOvertime(BigDecimal overtime) {
        this.overtime = overtime;
    }

    public BigDecimal getTabungan() {
        return tabungan;
    }

    public void setTabungan(BigDecimal tabungan) {
        this.tabungan = tabungan;
    }

    public Date getTglJatuhTempo() {
        return tglJatuhTempo;
    }

    public void setTglJatuhTempo(Date tglJatuhTempo) {
        this.tglJatuhTempo = tglJatuhTempo;
    }

    public Date getTglSPO() {
        return tglSPO;
    }

    public void setTglSPO(Date tglSPO) {
        this.tglSPO = tglSPO;
    }

    public Integer getSetKe() {
        return setKe;
    }

    public void setSetKe(Integer setKe) {
        this.setKe = setKe;
    }

    public BigDecimal getTotalAngsur() {
        return totalAngsur;
    }

    public void setTotalAngsur(BigDecimal totalAngsur) {
        this.totalAngsur = totalAngsur;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mintory.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Nurul Chusna
 */
public class KartuBayarReport {
    private Integer nLambung;
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
    private BigDecimal totalSaldoAngsuran;
    private BigDecimal totalTabungan;
    private BigDecimal totalSetoran;
    private BigDecimal totalKasbon;
    private BigDecimal totalBayar;
    private BigDecimal totalOvertime;
    private BigDecimal totalCicilan;
    private BigDecimal hutang;
    private String keterangan;

    public BigDecimal getHutang() {
        return hutang;
    }

    public void setHutang(BigDecimal hutang) {
        this.hutang = hutang;
    }

    public BigDecimal getTotalBayar() {
        return totalBayar;
    }

    public void setTotalBayar(BigDecimal totalBayar) {
        this.totalBayar = totalBayar;
    }

    public BigDecimal getTotalSaldoAngsuran() {
        return totalSaldoAngsuran;
    }

    public void setTotalSaldoAngsuran(BigDecimal totalSaldoAngsuran) {
        this.totalSaldoAngsuran = totalSaldoAngsuran;
    }

    public BigDecimal getTotalTabungan() {
        return totalTabungan;
    }

    public void setTotalTabungan(BigDecimal totalTabungan) {
        this.totalTabungan = totalTabungan;
    }

    public BigDecimal getTotalCicilan() {
        return totalCicilan;
    }

    public void setTotalCicilan(BigDecimal totalCicilan) {
        this.totalCicilan = totalCicilan;
    }

    public BigDecimal getTotalKasbon() {
        return totalKasbon;
    }

    public void setTotalKasbon(BigDecimal totalKasbon) {
        this.totalKasbon = totalKasbon;
    }

    public BigDecimal getTotalOvertime() {
        return totalOvertime;
    }

    public void setTotalOvertime(BigDecimal totalOvertime) {
        this.totalOvertime = totalOvertime;
    }

    public BigDecimal getTotalSetoran() {
        return totalSetoran;
    }

    public void setTotalSetoran(BigDecimal totalSetoran) {
        this.totalSetoran = totalSetoran;
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

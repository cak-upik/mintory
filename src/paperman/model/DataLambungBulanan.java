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
public class DataLambungBulanan {
    private Integer nLambung;
    private String nopol;
    private Integer setKe;
    private Date tglAngsuranAwal;
    private Date tglJatuhTempo;
    private Date tglSPO;
    private BigDecimal angsuran;
    private BigDecimal tabungan;
    private BigDecimal kasbon;
    private BigDecimal bayarKasbon;
    private BigDecimal cicilanKas;
    private BigDecimal overtime;
    private BigDecimal angsuranSaldoAwal;
    private BigDecimal totalSetoran;
    private BigDecimal totalhutang;
    private closingBulanan cb;

    public BigDecimal getTotalhutang() {
        return totalhutang;
    }

    public void setTotalhutang(BigDecimal totalhutang) {
        this.totalhutang = totalhutang;
    }

    public BigDecimal getTotalSetoran() {
        return totalSetoran;
    }

    public void setTotalSetoran(BigDecimal totalSetoran) {
        this.totalSetoran = totalSetoran;
    }

    public BigDecimal getAngsuranSaldoAwal() {
        return angsuranSaldoAwal;
    }

    public void setAngsuranSaldoAwal(BigDecimal angsuranSaldoAwal) {
        this.angsuranSaldoAwal = angsuranSaldoAwal;
    }

    public Date getTglSPO() {
        return tglSPO;
    }

    public void setTglSPO(Date tglSPO) {
        this.tglSPO = tglSPO;
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

    public Integer getnLambung() {
        return nLambung;
    }

    public void setnLambung(Integer nLambung) {
        this.nLambung = nLambung;
    }

    public String getNopol() {
        return nopol;
    }

    public void setNopol(String nopol) {
        this.nopol = nopol;
    }

    public BigDecimal getOvertime() {
        return overtime;
    }

    public void setOvertime(BigDecimal overtime) {
        this.overtime = overtime;
    }

    public Integer getSetKe() {
        return setKe;
    }

    public void setSetKe(Integer setKe) {
        this.setKe = setKe;
    }

    public BigDecimal getTabungan() {
        return tabungan;
    }

    public void setTabungan(BigDecimal tabungan) {
        this.tabungan = tabungan;
    }

    public Date getTglAngsuranAwal() {
        return tglAngsuranAwal;
    }

    public void setTglAngsuranAwal(Date tglAngsuranAwal) {
        this.tglAngsuranAwal = tglAngsuranAwal;
    }

    public Date getTglJatuhTempo() {
        return tglJatuhTempo;
    }

    public void setTglJatuhTempo(Date tglJatuhTempo) {
        this.tglJatuhTempo = tglJatuhTempo;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package paperman.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Nurul Chusna
 */
@Entity
@Table(name="tr_pth_closing_bulanan")
public class closingBulananPutih implements Serializable{
    @Id
    @GeneratedValue
    private Integer id;

    @Temporal(TemporalType.DATE)
    private Date tglClosing;

    @Temporal(TemporalType.DATE)
    private Date periodeBulan;

    @Column(name="ref_setoranKe")
    private Integer refSetoranKe;

    @Column(name="ref_noLambung")
    private Integer refNoLambung;

    @Column(name="user_set")
    private String user;

    @Column(name="total_setoran", columnDefinition="Decimal(19,2) default '0.00'")
    private BigDecimal totalSetor;

    @Column(name="total_angsuran", columnDefinition="Decimal(19,2) default '0.00'")
    private BigDecimal totalAngsuran;

    @Column(name="total_tabungan", columnDefinition="Decimal(19,2) default '0.00'")
    private BigDecimal totalTabungan;

    @Column(name="total_kasbon", columnDefinition="Decimal(19,2) default '0.00'")
    private BigDecimal totalKas;

    @Column(name="total_bayar", columnDefinition="Decimal(19,2) default '0.00'")
    private BigDecimal totalBayarKas;

    @Column(name="total_overtime", columnDefinition="Decimal(19,2) default '0.00'")
    private BigDecimal totalOvertime;

    @Column(name="total_cicilan", columnDefinition="Decimal(19,2) default '0.00'")
    private BigDecimal totalCicilan;

    @Column(name="hutang_pth_sld_awal", columnDefinition="Decimal(19,2) default '0.00'")
    private BigDecimal hutangSaldoAwal;

    @Column(name="closed_for")
    @Enumerated(EnumType.STRING)
    private isClosedFor closedFor;

    @Column(name="act_closing")
    @Enumerated(EnumType.STRING)
    private aksiClosing actClosing;

    @JoinColumn(name="id_komposisiRefSaldoAwal", referencedColumnName="id")
    @ManyToOne
    private komposisiSetoran idKomposisi;

    public komposisiSetoran getIdKomposisiRefSaldoAwal() {
        return idKomposisi;
    }

    public void setIdKomposisiRefSaldoAwal(komposisiSetoran idKomposisi) {
        this.idKomposisi = idKomposisi;
    }

    public aksiClosing getActClosing() {
        return actClosing;
    }

    public void setActClosing(aksiClosing actClosing) {
        this.actClosing = actClosing;
    }

    public BigDecimal getHutangSaldoAwal() {
        return hutangSaldoAwal;
    }

    public void setHutangSaldoAwal(BigDecimal hutangSaldoAwal) {
        this.hutangSaldoAwal = hutangSaldoAwal;
    }

    public Integer getRefNoLambung() {
        return refNoLambung;
    }

    public void setRefNoLambung(Integer refNoLambung) {
        this.refNoLambung = refNoLambung;
    }

    public Integer getRefSetoranKe() {
        return refSetoranKe;
    }

    public void setRefSetoranKe(Integer refSetoranKe) {
        this.refSetoranKe = refSetoranKe;
    }

    public isClosedFor getClosedFor() {
        return closedFor;
    }

    public void setClosedFor(isClosedFor closedFor) {
        this.closedFor = closedFor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getPeriodeBulan() {
        return periodeBulan;
    }

    public void setPeriodeBulan(Date periodeBulan) {
        this.periodeBulan = periodeBulan;
    }

    public Date getTglClosing() {
        return tglClosing;
    }

    public void setTglClosing(Date tglClosing) {
        this.tglClosing = tglClosing;
    }

    public BigDecimal getTotalBayarKas() {
        return totalBayarKas;
    }

    public void setTotalBayarKas(BigDecimal totalBayarKas) {
        this.totalBayarKas = totalBayarKas;
    }

    public BigDecimal getTotalCicilan() {
        return totalCicilan;
    }

    public void setTotalCicilan(BigDecimal totalCicilan) {
        this.totalCicilan = totalCicilan;
    }

    public BigDecimal getTotalKas() {
        return totalKas;
    }

    public void setTotalKas(BigDecimal totalKas) {
        this.totalKas = totalKas;
    }

    public BigDecimal getTotalOvertime() {
        return totalOvertime;
    }

    public void setTotalOvertime(BigDecimal totalOvertime) {
        this.totalOvertime = totalOvertime;
    }

    public BigDecimal getTotalSetor() {
        return totalSetor;
    }

    public BigDecimal getTotalAngsuran() {
        return totalAngsuran;
    }

    public void setTotalAngsuran(BigDecimal totalAngsuran) {
        this.totalAngsuran = totalAngsuran;
    }

    public BigDecimal getTotalTabungan() {
        return totalTabungan;
    }

    public void setTotalTabungan(BigDecimal totalTabungan) {
        this.totalTabungan = totalTabungan;
    }

    public void setTotalSetor(BigDecimal totalSetor) {
        this.totalSetor = totalSetor;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}

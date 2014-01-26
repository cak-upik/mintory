/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package paperman.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Cascade;

/**
 *
 * @author i1440ns
 */
@Entity
@Table(name="tr_setoran")
public class setoran implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    @Temporal(TemporalType.DATE)
    private Date tglSetoran;

    @Temporal(TemporalType.DATE)
    private Date tglJatuhTempo;

    @Temporal(TemporalType.DATE)
    private Date tglSPO;

    @Column(name="kode_setoran", unique=true, nullable=false)
    private String kode;

    @Column(name="total_setoran")
    private BigDecimal totalSetoran;

    @Column(name="total_hutang")
    private BigDecimal totalHutang;

    @Column(name="kode_user")
    private String user;

    @Enumerated(EnumType.STRING)
//    @Column(name="tr_status", columnDefinition="ENUM('L', 'U')")
    @Column(name="tr_status")
    private TransaksiStatus payedStatus;

    @Enumerated(EnumType.STRING)
    @Column(name="jalan_state")
    private JalanStatus jalanStatus;

    @Enumerated(EnumType.STRING)
    @Column(name="closing_status")
    private isClosedStatus closedStatus;

    @Column(name="setoran_counter")
    private Integer counter_setoran;

    @OneToMany(mappedBy="setor_map")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<setoranDetail> detail = new ArrayList<setoranDetail>();

    @JoinColumn(name="id_komposisi", referencedColumnName="id")
    @ManyToOne
    private komposisiSetoran idKomposisi;

    @JoinColumn(name="id_closing_bulan", referencedColumnName="id")
    @ManyToOne
    private closingBulanan idClosing;

    @JoinColumn(name="id_closing_tahun", referencedColumnName="id")
    @ManyToOne
    private closingTahunan idClosingTahun;
    
    public komposisiSetoran getIdKomposisi() {
        return idKomposisi;
    }

    public void setIdKomposisi(komposisiSetoran idKomposisi) {
        this.idKomposisi = idKomposisi;
    }
    
    public closingBulanan getIdClosing() {
        return idClosing;
    }

    public void setIdClosing(closingBulanan idClosing) {
        this.idClosing = idClosing;
    }

    public closingTahunan getIdClosingTahun() {
        return idClosingTahun;
    }

    public void setIdClosingTahun(closingTahunan idClosingTahun) {
        this.idClosingTahun = idClosingTahun;
    }

    public isClosedStatus getClosedStatus() {
        return closedStatus;
    }

    public void setClosedStatus(isClosedStatus closedStatus) {
        this.closedStatus = closedStatus;
    }

    public JalanStatus getJalanStatus() {
        return jalanStatus;
    }

    public void setJalanStatus(JalanStatus jalanStatus) {
        this.jalanStatus = jalanStatus;
    }

    public TransaksiStatus getPayedStatus() {
        return payedStatus;
    }

    public void setPayedStatus(TransaksiStatus payedStatus) {
        this.payedStatus = payedStatus;
    }


    public List<setoranDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<setoranDetail> detail) {
        this.detail = detail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public Integer getCounter_setoran() {
        return counter_setoran;
    }

    public void setCounter_setoran(Integer counter_setoran) {
        this.counter_setoran = counter_setoran;
    }

    public Date getTglSetoran() {
        return tglSetoran;
    }

    public void setTglSetoran(Date tglSetoran) {
        this.tglSetoran = tglSetoran;
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

    public BigDecimal getTotalSetoran() {
        return totalSetoran;
    }

    public void setTotalSetoran(BigDecimal totalSetoran) {
        this.totalSetoran = totalSetoran;
    }

    public BigDecimal getTotalHutang() {
        return totalHutang;
    }

    public void setTotalHutang(BigDecimal totalHutang) {
        this.totalHutang = totalHutang;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final setoran other = (setoran) obj;
        if ((this.kode == null) ? (other.kode != null) : !this.kode.equals(other.kode)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (this.kode != null ? this.kode.hashCode() : 0);
        return hash;
    }

}

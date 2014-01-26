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
@Table(name="tr_setoran_pth")
public class setoranPutih implements Serializable {
    @Id
    @GeneratedValue
    @Column(name="idSetr_pth")
    private Integer idPth;

    @Temporal(TemporalType.DATE)
    private Date tglSetoran;

    @Temporal(TemporalType.DATE)
    private Date tglJatuhTempo;

    @Temporal(TemporalType.DATE)
    private Date tglSPO;

    @Column(name="kode_setoran_pth", unique=true, nullable=false)
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
    @Column(name="jalan_status")
    private JalanStatus jalanStatus;

    @Enumerated(EnumType.STRING)
    @Column(name="closing_pth_status")
    private isClosedStatus closedStatus;

    @Column(name="setoran_counter")
    private Integer counter_setoran;
    
    @OneToMany(mappedBy="setor_map_pth")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<setoranDetailPutih> detailPutih = new ArrayList<setoranDetailPutih>();

    @JoinColumn(name="id_closing_pth_bulan", referencedColumnName="id")
    @ManyToOne
    private closingBulananPutih idClosing;

    @JoinColumn(name="id_closing_pth_tahun", referencedColumnName="id")
    @ManyToOne
    private closingTahunanPutih idClosingTahun;

    public isClosedStatus getClosedStatus() {
        return closedStatus;
    }

    public void setClosedStatus(isClosedStatus closedStatus) {
        this.closedStatus = closedStatus;
    }

    public closingBulananPutih getIdClosing() {
        return idClosing;
    }

    public void setIdClosing(closingBulananPutih idClosing) {
        this.idClosing = idClosing;
    }

    public closingTahunanPutih getIdClosingTahun() {
        return idClosingTahun;
    }

    public void setIdClosingTahun(closingTahunanPutih idClosingTahun) {
        this.idClosingTahun = idClosingTahun;
    }

    public Integer getIdPth() {
        return idPth;
    }

    public void setIdPth(Integer idPth) {
        this.idPth = idPth;
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


    public List<setoranDetailPutih> getDetailPutih() {
        return detailPutih;
    }

    public void setDetailPutih(List<setoranDetailPutih> detailPutih) {
        this.detailPutih = detailPutih;
    }

    public Integer getId() {
        return idPth;
    }

    public void setId(Integer id) {
        this.idPth = id;
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
        final setoranPutih other = (setoranPutih) obj;
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
